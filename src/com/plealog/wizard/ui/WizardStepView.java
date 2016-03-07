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

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.plealog.wizard.model.WizardStepModel;

/**
 * Viewer associated to the data model of a single wizard step.
 */
public abstract class WizardStepView {

  private WizardStepModel	model;
  private WizardListener	listener;

  /**
   * Constructor.
   * 
   * @param wizardStepModel the step data model
   */
  public WizardStepView(WizardStepModel wizardStepModel) {
    this.model = wizardStepModel;
    this.listener = new WizardListenerAdapter() {

      @Override
      public void update() {
        super.update();
        WizardStepView.this.updateUI();
      }
    };
    wizardStepModel.addListener(this.listener);
  }

  /**
   * Request to commit UI data values to associated data model.
   * 
   * @return if commit was OK.
   */
  public abstract boolean commit();

  /**
   * Return the data model.
   */
  public WizardStepModel getModel() {
    return this.model;
  }

  /**
   * Update the UI according to the model state.
   */
  public abstract void updateUI();


  /**
   * @return The associated component for this view.
   */
  public abstract JComponent getUI();


  /**
   * @return The help area for this view.
   */
  public JComponent getHelpArea() {
    return new JPanel();
  }

  /**
   * Method called by the WizardDialog to figures out whether or not it can disable
   * Wizard scroll panel. This can happen when this View provides its own scroll panel.
   */
  public boolean disableWizardDialogScrollPane() {
    return false;
  }


  /**
   * Clean up this view.
   */
  public void cleanup() {
    this.model.removeListener(this.listener);
  }

}
