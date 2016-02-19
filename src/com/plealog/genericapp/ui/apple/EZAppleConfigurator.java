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
package com.plealog.genericapp.ui.apple;

import java.awt.Image;
import java.awt.Window;
import java.lang.reflect.Method;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.api.log.EZLogger;

public class EZAppleConfigurator {

  private static EZAppleConfigurator _singleton;

  public static void initialize(String appName){
    if (_singleton!=null)
      return;
    _singleton = new EZAppleConfigurator();
    try{
      // set the name of the application menu item
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", 
          appName);
      // take the menu bar off the main Frame
      System.setProperty("apple.laf.useScreenMenuBar", "true");
      // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
      // use as delegates for various com.apple.eawt.ApplicationListener methods
      OSXAdapter.setQuitHandler(_singleton, _singleton.getClass().getDeclaredMethod("quit", (Class[])null));
      OSXAdapter.setAboutHandler(_singleton, _singleton.getClass().getDeclaredMethod("about", (Class[])null));
      OSXAdapter.setPreferencesHandler(_singleton, _singleton.getClass().getDeclaredMethod("preferences", (Class[])null));
      //OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
    }
    catch (Throwable ex){
      EZLogger.warn("unable to setup MacOS X integration: "+ex);
    }
  }
  public static void enableFullScreenMode(Window window) {
    String className = "com.apple.eawt.FullScreenUtilities";
    String methodName = "setWindowCanFullScreen";

    try {
      Class<?> clazz = Class.forName(className);
      Method method = clazz.getMethod(methodName, new Class<?>[] {
          Window.class, boolean.class });
      method.invoke(null, window, true);
    } catch (Throwable t) {
      EZLogger.warn("Full screen mode is not supported");
    }
  }
  public void about() {
    EZEnvironment.getActionsManager().getDefaultActionHandler().handleAbout();
  }
  //see documentation of OSXAdapter
  public Boolean quit() {
    return EZEnvironment.getActionsManager().getDefaultActionHandler().handleExit();

  }

  public void preferences() {
    EZEnvironment.getActionsManager().getDefaultActionHandler().handlePreferences();
  }


  public static void setDockIcon(Image image){
    OSXAdapter.setDockIconImage(image);
  }
}
