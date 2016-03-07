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
 * Basic implementation of WizardListener.
 * 
 */
public class WizardListenerAdapter implements WizardListener {

  @Override
  public void update() {
  }

  @Override
  public void finishableChanged(boolean canFinish) {
  }

  @Override
  public void nextableChanged(boolean canNext) {
  }

  @Override
  public void previousChanged(boolean canPrevious) {
  }

  @Override
  public void currentStep(WizardStepModel oldStep, WizardStepModel newStep) {
  }

  @Override
  public void historyChanged(List<WizardStepModel> stepHistory) {
  }

  @Override
  public void userFinishes() {
  }

  @Override
  public void userCancels() {
  }

}
