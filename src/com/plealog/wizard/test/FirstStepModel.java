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