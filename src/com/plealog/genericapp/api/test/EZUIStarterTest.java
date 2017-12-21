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
package com.plealog.genericapp.api.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.api.EZGenericApplication;
import com.plealog.genericapp.api.EZSplashScreen;
import com.plealog.genericapp.api.EZSplashScreenFactory;
import com.plealog.genericapp.api.EZUIStarterListener;
import com.plealog.genericapp.api.file.EZFileUtils;

public class EZUIStarterTest {

  public static void main(String[] args) {
    // this has to be done at the very beginning otherwise, it does not work at
    // all !!!
    EZGenericApplication.initialize("EZUIStarterTest");

    // to add an app starter listener, call EZUIStarter.setUIStarterListener();
    EZEnvironment.setUIStarterListener(new MyStarteListener());

    // to enable the Preferences Dialogue Box
    String confPath = EZFileUtils.terminatePath(System.getProperty("user.dir"));
    confPath += "conf";
    confPath += File.separator;
    confPath += "editor.desc";
    EZEnvironment.setPreferencesConfigurationFile(confPath);

    // to setup a specific user defined messages bundle, call
    // EZEnvironment.setUserDefinedMessagesResourceBundle(rb);
    // then use EZEnvironment.getString()

    // to setup a specific User Defined Actions Manager:
    // (see
    // https://github.com/pgdurand/jGAF-Tutorial/tree/master/src/com/plealog/gaf4j/tutorial/part3)
    // simply call EZEnvironment.setUserDefinedActionsResourceBundle(rb);
    // the resource bundle has to target a valid ActionsManager resource bundle
    // see also EZDEfaultActionHandler to manage About, Preferences and Exit
    // standard actions

    // to setup application branding, use class EZApplicationBranding before
    // calling startApplication()

    // to access Actions, use EZEnvironment.getActionsManager();
    // to work with generic actions, see example below

    // this is how to start application
    EZGenericApplication.startApplication(args);
  }

  private static class MyStarteListener implements EZUIStarterListener {
    private EZSplashScreen splash;

    private MyStarteListener() {

    }

    @Override
    public void preStart() {
      splash = EZSplashScreenFactory.startSplashSreen(EZEnvironment.getImageIcon("banner.png"), true);
      for (int i = 20; i <= 100; i += 20) {
        splash.setProgressPercent(i);
        splash.setMessage("Step " + ((i / 20)));
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
      }
    }

    @Override
    public void postStart() {
      splash.finish();
    }

    @Override
    public Component getApplicationComponent() {
      JPanel mainPanel;
      JTabbedPane tabPanel;

      mainPanel = new JPanel(new BorderLayout());
      tabPanel = new JTabbedPane();

      tabPanel.add("Tab 1", new JPanel());

      mainPanel.add(tabPanel, BorderLayout.CENTER);
      return mainPanel;
    }

    @Override
    public boolean isAboutToQuit() {
      return true;
    }

    @Override
    public void frameDisplayed() {
    }
  }
}
