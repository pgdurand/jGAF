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
package com.plealog.prefs4j.test;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.plealog.prefs4j.api.PreferenceModel;
import com.plealog.prefs4j.api.PreferenceModelFactory;
import com.plealog.prefs4j.ui.PreferenceUIFactory;

public class EZFormTreeTest {
  private static final String cfgPath = "./conf/";

  public static void locateOnOpticalScreenCenter(Component component) {
    Dimension paneSize = component.getSize();
    Dimension screenSize = component.getToolkit().getScreenSize();
    component.setLocation((screenSize.width - paneSize.width) / 2,
        (int) ((screenSize.height - paneSize.height) * 0.45));
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // ConfigurationEditor getEditor;

    try {
      PreferenceModel model;

      JFrame frame = new JFrame();
      frame.setTitle(EZFormTreeTest.class.getName());
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      // Patrick Durand : L8P78-A5RL7-J4YP2-F3BBZ-7W57Y
      // PreferenceSystem.setUserInformation("Patrick Durand",
      // "L8P78-A5RL7-J4YP2-F3BBZ-7W57Y");
      model = PreferenceModelFactory.getModel(cfgPath + "editor.desc");
      // PreferencePanel editor = new ConfigurationPanel(model);
      // JPanel mainpanel.add(editor,BorderLayout.CENTER);
      // frame.getContentPane().add(mainpanel);
      frame.getContentPane().add(new JButton(new ValidateAction(model)));
      frame.pack();
      EZFormTreeTest.locateOnOpticalScreenCenter(frame);
      frame.setVisible(true);
    } catch (Exception e) {
      System.err.println("Error: " + e);
    }
  }

  @SuppressWarnings("serial")
  private static class ValidateAction extends AbstractAction {
    private PreferenceModel model;

    private ValidateAction(final PreferenceModel model) {
      super("...");
      this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      try {
        PreferenceUIFactory.showPreferenceDialog(null, "Config", model);
      } catch (Exception e) {
        System.err.println(e);
      }
    }

  }
}
