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

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import com.plealog.wizard.model.WizardModel;
import com.plealog.wizard.model.WizardStepModel;
import com.plealog.wizard.model.WizardUtils;

/**
 * Setup a wizard dialogue box.
 * 
 */
public class WizardDialog extends JDialog {
  private static final long				serialVersionUID	= 1L;

  private WizardModel						wizardModel			= null;

  private JPanel							mainPanel;
  private JPanel							bottomPanel;
  private JPanel							stepPanel;
  private JPanel							componentPanel;

  private JButton							nextButton;
  private JButton							previousButton;
  private JButton							finishButton;
  private JButton							cancelButton;

  private static final int				DIALOG_WIDTH		= 640;
  private static final int				DIALOG_HEIGHT		= 350;


  private int								idx_gbc_stepPanel	= 0;

  /**
   * Constructor.
   * 
   * @param owner the parent of this dialogue box
   * @param model the data model
   */
  public WizardDialog(Frame owner, WizardModel model) {
    super(owner, true);

    this.wizardModel = model;

    init();
    initSize();

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
  }

  //setup the UI
  private void init() {
    updateStepPanel();
    initButtonPanel();
    initMainPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(this.mainPanel, BorderLayout.CENTER);
    initModelListener();
  }

  //setup a default size
  private void initSize() {
    Dimension screen, frame;

    frame = new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT);
    screen = getToolkit().getScreenSize();

    this.setMinimumSize(frame);
    this.setResizable(false);

    setLocation(screen.width / 2 - frame.width / 2, screen.height / 2 - frame.height / 2);
  }

  //inner class listener
  private void initModelListener() {
    this.wizardModel.addListener(new WizardListener() {
      @Override
      public void update() {
      }

      @Override
      public void finishableChanged(boolean canFinish) {
        WizardDialog.this.finishButton.setEnabled(canFinish);
      }

      @Override
      public void nextableChanged(boolean canNext) {
        WizardDialog.this.nextButton.setEnabled(canNext);
        WizardDialog.this.updateButtonsTooltip();
      }

      @Override
      public void currentStep(WizardStepModel oldStep, WizardStepModel newStep) {
        WizardDialog.this.updateComponentPanel(newStep);
      }

      @Override
      public void previousChanged(boolean canPrevious) {
        WizardDialog.this.previousButton.setEnabled(canPrevious);
        WizardDialog.this.updateButtonsTooltip();
      }

      @Override
      public void historyChanged(List<WizardStepModel> stepHistory) {
        WizardDialog.this.updateStepPanel();
      }

      @Override
      public void userFinishes() {
        WizardDialog.this.dispose();
      }

      @Override
      public void userCancels() {
        WizardDialog.this.dispose();
      }


    });
  }

  protected void updateComponentPanel(WizardStepModel newStep) {
    int vsbPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
    int hsbPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;

    this.componentPanel.removeAll();

    final JComponent ui = newStep.getView().getUI();
    if (newStep.getView().disableWizardDialogScrollPane()) {
      vsbPolicy = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
    }
    this.componentPanel.add(new JScrollPane(ui, vsbPolicy, hsbPolicy), BorderLayout.CENTER);

    final JComponent helpAera = newStep.getView().getHelpArea();
    this.componentPanel.add(helpAera, BorderLayout.SOUTH);

    this.componentPanel.revalidate();
    this.componentPanel.repaint();
  }

  protected void updateButtonsTooltip() {

    final WizardStepModel previousStep = this.wizardModel.getCurrentStep().getModel().previous();

    if (previousStep != null) {
      this.previousButton.setToolTipText(previousStep.getShortDescription());
    } else {
      this.previousButton.setToolTipText(null);
    }

    final WizardStepModel nextStep = this.wizardModel.getCurrentStep().getModel().next();

    if (nextStep != null) {
      this.nextButton.setToolTipText(nextStep.getShortDescription());
    } else {
      this.nextButton.setToolTipText(null);
    }
  }

  //creates UI
  private void initMainPanel() {

    if (this.mainPanel == null) {
      this.mainPanel = new JPanel();
      this.mainPanel.setLayout(new BorderLayout());

      // step panel
      this.mainPanel.add(this.stepPanel, BorderLayout.LINE_START);

      // component panel
      this.componentPanel = new JPanel();
      this.componentPanel.setLayout(new BorderLayout());
      this.componentPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      this.mainPanel.add(this.componentPanel, BorderLayout.CENTER);

      // bottom panel
      this.mainPanel.add(this.bottomPanel, BorderLayout.PAGE_END);

    }

  }

  //setup the step panels
  private void updateStepPanel() {
    WizardStepModel currentStepModel = null;
    int idx = 1;

    this.idx_gbc_stepPanel = 0;


    if (this.stepPanel == null) {
      this.stepPanel = new JPanel(new GridBagLayout());
      this.stepPanel.setPreferredSize(new Dimension(210, 500));
      this.stepPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    } else {
      this.stepPanel.removeAll();
    }


    // Step History
    Iterator<WizardStepModel> iterator = this.wizardModel.getStepHistory().iterator();
    while (iterator.hasNext()) {
      WizardStepModel wizardStepModel = (WizardStepModel) iterator.next();
      boolean isCurrent = !iterator.hasNext();

      // Step description
      addStepDescriptionLabel(wizardStepModel, idx++, true);

      // Step summary (if this is not the current stage)
      if (WizardUtils.isNotBlank(wizardStepModel.getSummary()) && !isCurrent) {
        addStepSummaryLabel(wizardStepModel);
      }

      // set the current step
      if (isCurrent) {
        currentStepModel = wizardStepModel;
      }

    }

    setTitle(idx-1 + ": " + currentStepModel.getDescription());

    // Next steps
    iterator = currentStepModel.getNextSteps().iterator();
    while (iterator.hasNext()) {
      WizardStepModel wizardStepModel = (WizardStepModel) iterator.next();

      if (wizardStepModel!=null) {
        // Step description
        addStepDescriptionLabel(wizardStepModel, idx++, false);
      }

    }

    GridBagConstraints g = new GridBagConstraints();
    g.gridy = this.idx_gbc_stepPanel++;
    g.weighty = 1;
    g.weightx = 1;
    this.stepPanel.add(new JLabel(), g);

    this.stepPanel.revalidate();
    this.stepPanel.repaint();
  }

  //setup the description area
  private void addStepDescriptionLabel(WizardStepModel stepModel, int idx, boolean enabled) {
    GridBagConstraints g = new GridBagConstraints();
    g.gridy = this.idx_gbc_stepPanel++;
    g.weightx = 1;
    g.fill = GridBagConstraints.HORIZONTAL;
    g.insets = new Insets(0, 2, 0, 0);

    JLabel label = new JLabel();
    label.setText(idx + ": "+ stepModel.getShortDescription());
    label.setEnabled(enabled);

    this.stepPanel.add(label, g);
  }

  private void addStepSummaryLabel(WizardStepModel stepModel) {
    GridBagConstraints g = new GridBagConstraints();
    g.gridy = this.idx_gbc_stepPanel++;
    g.weightx = 1;
    g.fill = GridBagConstraints.HORIZONTAL;
    g.insets = new Insets(0, 20, 0, 0);

    JLabel label = new JLabel();
    label.setText(stepModel.getSummary());

    Font italic = new Font(label.getFont().getName(), Font.ITALIC, label.getFont().getSize()-1);
    label.setFont(italic);

    this.stepPanel.add(label, g);
  }

  //setup the buttons panel; previous, next, cancel
  private void initButtonPanel() {

    this.bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    this.previousButton = new JButton("Previous");
    this.bottomPanel.add(this.previousButton);

    this.nextButton = new JButton("Next");
    this.bottomPanel.add(this.nextButton);

    this.finishButton = new JButton("Finish");
    this.bottomPanel.add(this.finishButton);

    this.cancelButton = new JButton("Cancel");
    this.bottomPanel.add(this.cancelButton);

    Dimension buttonDimension = new Dimension(110, 25);
    this.previousButton.setPreferredSize(buttonDimension);
    this.nextButton.setPreferredSize(buttonDimension);
    this.finishButton.setPreferredSize(buttonDimension);
    this.cancelButton.setPreferredSize(buttonDimension);

    this.bottomPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    this.bottomPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    this.previousButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        WizardDialog.this.wizardModel.previousStep();
      }
    });

    this.nextButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        WizardDialog.this.wizardModel.nextStep();
      }

    });

    this.finishButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        boolean isCommit = WizardDialog.this.wizardModel.getCurrentStep().getView().commit();

        if (!isCommit)
          return;

        WizardDialog.this.wizardModel.finish();
      }
    });

    this.cancelButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        WizardDialog.this.wizardModel.cancel();
      }
    });

  }

}
