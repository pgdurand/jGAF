package com.plealog.wizard.test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.plealog.wizard.model.WizardModel;
import com.plealog.wizard.model.WizardStepModel;
import com.plealog.wizard.ui.WizardDialog;
import com.plealog.wizard.ui.WizardListenerAdapter;

/**
 * A simple Wizard illustrating how to use the API.
 */
public class WizardTest {

  private static JFrame	frame;

  public static void main(String[] args) {
    frame = new JFrame();

    frame.setTitle("Wizard Test");
    frame.setSize(1200, 650);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new FlowLayout());

    JButton button = new JButton(new AbstractAction() {
      private static final long	serialVersionUID	= 1L;

      @Override
      public void actionPerformed(ActionEvent event) {
        execute();
      }
    });
    button.setText("New wizard");

    frame.add(button);
    frame.setVisible(true);

  }

  private static void execute() {

    final SecondStepModel secondStep = new SecondStepModel();
    final FirstStepModel firstStep = new FirstStepModel(secondStep);


    final WizardModel wizardModel = new WizardModel(firstStep) {

      @Override
      public boolean isFinishable(Stack<WizardStepModel> stack) {
        WizardStepModel currentStep = this.getCurrentStep();
        if (currentStep != null && currentStep.getClass().equals(SecondStepModel.class))
          return true;
        return false;
      }

      @Override
      public String getDescription() {
        return "TEST";
      }

    };
    wizardModel.addListener(new WizardListenerAdapter() {
      @Override
      public void userFinishes() {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {

            JOptionPane.showMessageDialog(
                frame,
                firstStep.getSummary() + "\n" + secondStep.getSummary(),
                "Result",
                JOptionPane.INFORMATION_MESSAGE);

          }
        });
      }
    });
    final WizardDialog wizardDialog = new WizardDialog(frame, wizardModel);
    wizardDialog.setVisible(true);
  }

}
