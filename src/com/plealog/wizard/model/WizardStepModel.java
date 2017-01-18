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
import java.util.List;

import com.plealog.wizard.ui.WizardListener;
import com.plealog.wizard.ui.WizardStepView;

/**
 * Data model of a single wizard step.
 */
public abstract class WizardStepModel {

  private final List<WizardListener>	listeners = new ArrayList<WizardListener>();
  private WizardModel				        	model;
  private String						          summary	= "";


  /**
   * Get the view associated to this data model.
   *
   * @return a wizard step model
   */
  public abstract WizardStepView getView();

  /**
   * Add a listener to the model.
   *
   * @param listener
   */
  public final void addListener(WizardListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove a listener from the model.
   *
   * @param listener
   */
  public final void removeListener(WizardListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Figures out whether or not the data model is in a state compatible with Wizard step progression.
   * @return true if model can be commited
   */
  public boolean isCommitable() {
    return true;
  }

  /**
   * Clean up the data model.
   */
  public void cleanup() {
    this.listeners.clear();
    WizardStepView localWizardStepView = getView();
    if ((localWizardStepView != null))
      localWizardStepView.cleanup();
  }

  /**
   * Human readable description of this data model.
   */
  public abstract String getDescription();

  /**
   * Return a short description of this data model
   */
  public abstract String getShortDescription();


  /**
   * Return the next step.
   * 
   * @return the next step or null if it is the last step.
   */
  public abstract WizardStepModel next();


  /**
   * Returns whether or not this step is the current step of the wizard data model.
   */
  public final boolean isCurrentStep() {
    return this.model.getCurrentStep() == this;
  }

  /**
   * Method called when the step is entered.
   */
  protected void stepEntered() {
    getView().getUI();
    getView().updateUI();
  }

  /**
   * Method called when exit from the step
   */
  protected void stepExited() {
  }


  /**
   * Get the global WizardModel
   * @return the wizard model
   */
  public final WizardModel getModel() {
    return this.model;
  }

  /**
   * Set the global wizard model.
   * 
   * @param paramWizardModel
   */
  public final void setModel(WizardModel paramWizardModel) {
    this.model = paramWizardModel;
  }

  /**
   * Return a summary of this step after validation.
   */
  public String getSummary() {
    return summary;
  }

  /**
   * Set the summary of this step.
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }

  /**
   * @return The next steps
   */
  public final List<WizardStepModel> getNextSteps() {
    List<WizardStepModel> nextSteps = new ArrayList<WizardStepModel>();

    WizardStepModel wizardStepModel = next();
    do {
      if (wizardStepModel != null) {
        wizardStepModel.setModel(this.model);
        nextSteps.add(wizardStepModel);

        // go to next
        wizardStepModel = wizardStepModel.next();
      }
    } while (wizardStepModel != null);

    return nextSteps;
  }


}
