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
package com.plealog.wizard.ui;

import java.util.List;

import com.plealog.wizard.model.WizardStepModel;

/**
 * Listener of changes applied on a WizardStepModel.
 * 
 */
public abstract interface WizardListener {

  /**
   * Method called when an update has been made.
   */
  public abstract void update();

  /**
   * Method called when finish status has changed.
   * 
   * @param canFinish
   */
  public abstract void finishableChanged(boolean canFinish);

  /**
   * Method called when next status has changed.
   * 
   * @param canNext
   */
  public abstract void nextableChanged(boolean canNext);

  /**
   * Method called when previous status has changed.
   * @param canPrevious
   */
  public abstract void previousChanged(boolean canPrevious);

  /**
   * Method called when the current step has changed.
   * 
   * @param oldStep
   * @param newStep
   */
  public abstract void currentStep(WizardStepModel oldStep, WizardStepModel newStep);

  /**
   * Method called when the history has changed.
   * 
   * @param stepHistory
   */
  public abstract void historyChanged(List<WizardStepModel> stepHistory);

  /**
   * Method called when the user selects the finish status.
   */
  public abstract void userFinishes();

  /**
   * Method called when the user selects the cancel status.
   */
  public abstract void userCancels();
}
