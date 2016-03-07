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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.plealog.prefs4j.implem.ui.tools.DesignGridLayout;
import com.plealog.wizard.model.WizardStepModel;
import com.plealog.wizard.model.WizardUtils;
import com.plealog.wizard.ui.WizardStepView;

/**
 * View of the first wizard's step.
 */
public class FirstStepView extends WizardStepView {

  private boolean		init	= false;
  private JPanel		ui;
  private JTextField	firstName;
  private JTextField	lastName;

  public FirstStepView(WizardStepModel wizardStepModel) {
    super(wizardStepModel);
  }

  @Override
  public boolean commit() {
    String firstName_str = "";
    if (WizardUtils.isNotBlank(firstName.getText()))
      firstName_str = firstName.getText();

    String lastName_str = "";
    if (WizardUtils.isNotBlank(lastName.getText()))
      lastName_str = lastName.getText();

    this.getModel().setSummary(firstName_str + ", " + lastName_str);
    return true;
  }

  @Override
  public void updateUI() {
    init();
    ui.updateUI();
  }

  @Override
  public JComponent getUI() {
    init();
    return ui;
  }

  private void init() {
    //other ways to setup a form: 
    //http://stackoverflow.com/questions/3180535/how-to-make-an-input-form-in-java-code-not-netbeans-using-jform
    if (!init) {
      ui = new JPanel();
      firstName = new JTextField();
      lastName = new JTextField();
      DesignGridLayout layout = new DesignGridLayout(ui);
      layout.row().grid(new JLabel("First name: ")).add(firstName);
      layout.row().grid(new JLabel("Last name: ")).add(lastName);
      init = true;
    }
  }
}