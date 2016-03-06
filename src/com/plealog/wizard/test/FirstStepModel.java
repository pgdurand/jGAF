package com.plealog.wizard.test;

import com.plealog.wizard.model.WizardStepModel;
import com.plealog.wizard.ui.WizardStepView;

/**
 * Data model of the first wizard's step.
 */
public class FirstStepModel extends WizardStepModel {

  private FirstStepView	view;
  private WizardStepModel	nexStep;

  public FirstStepModel(WizardStepModel nexStep) {
    this.nexStep = nexStep;
    this.view = new FirstStepView(this);
  }

  @Override
  public WizardStepModel next() {
    return this.nexStep;
  }

  @Override
  public WizardStepView getView() {
    return this.view;
  }

  @Override
  public String getDescription() {
    return "Who are you ?";
  }

  @Override
  public String getShortDescription() {
    return "First and last name";
  }

}