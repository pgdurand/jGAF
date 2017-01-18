/* Copyright (C) 2003-2016 Patrick G. Durand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.plealog.wizard.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.SwingUtilities;

import com.plealog.wizard.ui.WizardListener;
import com.plealog.wizard.ui.WizardListenerAdapter;


/**
 * This class describes the data model of a WizardDialog. It is actually made
 * of one or more WizardStepModel(s).
 * 
 */
public abstract class WizardModel {

  private WizardStepModel							          firstStep;
  private WizardStepModel							          currentStep			  = null;
  private final StepStack<WizardStepModel>      stepHistory			  = new StepStack<WizardStepModel>();
  private final List<WizardListener>				    listeners			    = new CopyOnWriteArrayList<WizardListener>();
  private WizardListener							          stepListener;
  private final StackListener<WizardStepModel>	stackListener;
  private boolean		                            finishable		  	= false;
  private boolean									              isMovingBackwards	= false;

  /**
   * Constructor.
   * 
   * @param firstStep the data model of the first wizard's step 
   * */
  public WizardModel(WizardStepModel firstStep) {

    this.firstStep = firstStep;

    // Init listener for current step
    this.stepListener = new WizardListenerAdapter() {

      @Override
      public void update() {
        super.update();
        WizardModel.this.fireFinishable(WizardModel.this.isFinishable(WizardModel.this.stepHistory));
        WizardModel.this.fireNextable();
        WizardModel.this.firePreviousable();
      }
    };

    // Init listener for step history
    this.stackListener = new StackListener<WizardStepModel>() {
      @Override
      public void update(Stack<WizardStepModel> stack) {
        Iterator<WizardListener> it = WizardModel.this.listeners.iterator();
        while (it.hasNext()) {
          WizardListener wizardListener = (WizardListener) it.next();
          wizardListener.historyChanged(stack);
        }

      }

    };
    this.stepHistory.addListener(this.stackListener);

    // Set first step
    this.setCurrentStep(this.next(), false);
    this.stepHistory.push(this.getCurrentStep());
    this.fireFinishable(this.isFinishable());

  }

  /**
   * Set a new step.
   * 
   * @param newStep the new step data model
   * @param oldStepAlreadyCommitted true if previous step has been committed
   */
  protected final void setCurrentStep(WizardStepModel newStep, boolean oldStepAlreadyCommitted) {
    WizardStepModel oldStep = getCurrentStep();
    if ((oldStep != newStep)) {

      if (oldStep != null) {

        if (!oldStepAlreadyCommitted) {
          if (!oldStep.getView().commit())
            return;
        }

        oldStep.stepExited();
        oldStep.removeListener(this.stepListener);
      }

      this.currentStep = newStep;
      this.currentStep.setModel(this);
      this.currentStep.addListener(this.stepListener);
      this.currentStep.stepEntered();

      fireCurrentStep(oldStep, this.currentStep);
      firePreviousable();
      fireNextable();
      fireFinishable(this.isFinishable());
    }
  }

  /**
   * Notify listeners that current step has changed.
   * 
   * @param oldStep previous step data model
   * @param newStep new step data model
   */
  protected final void fireCurrentStep(WizardStepModel oldStep, WizardStepModel newStep) {
    Iterator<WizardListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      WizardListener listener = (WizardListener) iterator.next();
      listener.currentStep(oldStep, newStep);
    }

  }

  /**
   * Get the next step data model.
   *
   * @return the next step
   */
  public final WizardStepModel next() {
    WizardStepModel nextStep = null;

    if (this.currentStep == null) {
      nextStep = getFirstStep();
    } else {
      nextStep = this.currentStep.next();
      if (nextStep == null)
        return null;

      nextStep.setModel(this);
    }

    return nextStep;
  }

  /**
   * Get the previous step data model.
   *
   * @return the previous step
   */
  public final WizardStepModel previous() {
    WizardStepModel previousStep = null;

    if (this.canPrevious()) {
      int nbStep = this.stepHistory.size();
      if (nbStep >= 2) {
        previousStep = this.stepHistory.elementAt(nbStep-2);
      }
    }

    return previousStep;
  }

  /**
   * Progress to next step.
   */
  public void nextStep() {
    WizardStepModel oldStep = getCurrentStep();

    this.setCurrentStep(this.next(), false);

    if ((oldStep != getCurrentStep())) {
      this.stepHistory.push(this.getCurrentStep());
      this.fireFinishable(this.isFinishable());
      this.firePreviousable();
    } else {
      // TODO : do something ??
    }

  }

  /**
   * Notify listeners of that next status has been selected.
   */
  protected void fireNextable() {
    Iterator<WizardListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      WizardListener listener = (WizardListener) iterator.next();
      listener.nextableChanged(isNextable());
    }
  }

  /**
   * Is it possible to progress to next step ?
   */
  public boolean isNextable() {
    return ((getCurrentStep().isCommitable()) && (next() != null));
  }

  /**
   * Progress to previous step.
   */
  public void previousStep() {
    this.isMovingBackwards = true;
    getCurrentStep().getView().commit();
    this.isMovingBackwards = false;
    this.stepHistory.pop();
    setCurrentStep((WizardStepModel)this.stepHistory.peek(), true);
  }

  /**
   * Return true while executing commit on current step view during previousStep().
   */
  public boolean isMovingBackwards() {
    return this.isMovingBackwards;
  }

  /**
   * Notify listeners that previous status has been selected.
   */
  protected final void firePreviousable() {
    Iterator<WizardListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      WizardListener listener = (WizardListener) iterator.next();
      listener.previousChanged(canPrevious());
    }
  }

  /**
   * Is it possible to progress to previous step.
   */
  public final boolean canPrevious() {
    return this.stepHistory.size() > 1;
  }

  /**
   * Is it possible to progress to finish status.
   */
  public final boolean isFinishable() {
    return isFinishable(this.stepHistory);
  }

  /**
   * Is it possible to finish ?
   * 
   * @param stack the step data models
   * @return true if wizard can be closed, false otherwise.
   */
  public abstract boolean isFinishable(Stack<WizardStepModel> stack);


  /**
   * Notify listeners that finish status has been selected or not.
   * 
   * @param finishable
   */
  protected void fireFinishable(boolean finishable) {
    Iterator<WizardListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      WizardListener listener = (WizardListener) iterator.next();
      listener.finishableChanged(finishable);
    }
  }

  /**
   * Update the finish status.
   */
  public final void setFinishable(boolean finishable) {
    if (this.finishable != finishable) {
      this.finishable = finishable;
      fireFinishable(finishable);
    }
  }

  /**
   * Method call when Wizard progress to finish status.
   */
  public void finish() {
    Iterator<WizardListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      WizardListener listener = (WizardListener) iterator.next();
      listener.userFinishes();
    }
    this.currentStep.stepExited();
  }

  /**
   * Method call when Wizard progress to cancel status.
   */
  public void cancel() {
    Iterator<WizardListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      WizardListener listener = (WizardListener) iterator.next();
      listener.userCancels();
    }
  }

  /**
   * @return The history of data model steps.
   */
  public List<WizardStepModel> getStepHistory() {
    return this.stepHistory;
  }


  /**
   * @return The current step.
   */
  public final WizardStepModel getCurrentStep() {
    return this.currentStep;
  }

  /**
   * @return The first step.
   */
  protected WizardStepModel getFirstStep() {
    return this.firstStep;
  }


  /**
   * Retruns a human readable description of this Wizard.
   */
  public abstract String getDescription();


  /**
   * Add a listener.
   * 
   * @param wizardListener
   */
  public final void addListener(final WizardListener wizardListener) {
    this.listeners.add(wizardListener);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        wizardListener.currentStep(null, WizardModel.this.getCurrentStep());
        wizardListener.finishableChanged(WizardModel.this.isFinishable());
        wizardListener.nextableChanged(WizardModel.this.isNextable());
        wizardListener.previousChanged(WizardModel.this.canPrevious());
      }
    });
  }

  /**
   * Clean the data model.
   */
  public void cleanup() {
    getCurrentStep().stepExited();
    getCurrentStep().removeListener(this.stepListener);
    this.stepHistory.removeListener(this.stackListener);
    this.listeners.clear();
  }

  /**
   * Remove a listener
   * @param wizardListener
   */
  public final void removeListener(WizardListener wizardListener) {
    this.listeners.remove(wizardListener);
  }

  private class StepStack<E> extends Stack<E> {
    private static final long	serialVersionUID	= 1L;

    private ArrayList<StackListener<E>>	listeners	= new ArrayList<StackListener<E>>();

    public void addListener(StackListener<E> listener) {
      this.listeners.add(listener);
    }

    public void removeListener(StackListener<E> listener) {
      this.listeners.remove(listener);
    }

    public E push(E item) {
      E ret = super.push(item);
      fireStackChanged();
      return ret;
    }

    public E pop() {
      E ret = super.pop();
      fireStackChanged();
      return ret;
    }

    private void fireStackChanged() {
      Iterator<StackListener<E>> localIterator = this.listeners.iterator();
      while (localIterator.hasNext()) {
        StackListener<E> listener = (StackListener<E>) localIterator.next();
        listener.update(this);
      }
    }

  }

  private abstract interface StackListener<E> {
    public abstract void update(Stack<E> stack);
  }

}
