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
