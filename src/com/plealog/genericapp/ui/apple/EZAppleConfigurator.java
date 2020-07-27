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
import java.lang.reflect.Proxy;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.api.log.EZLogger;

/**
 * Utility class providing macOS desktop integration for both Java 8 and Java 9+
 * platforms. 
 * 
 * Considering Java 8, this class uses legacy OSXAdapater class.
 * 
 * Considering Java 9+, this class relies upon new java.awt.Desktop APIs.
 *  
 * In other words, this class can be compiled using JDK 8 and used with JRE 8, 9, 11,
 * etc.
 *  
 * */
public class EZAppleConfigurator {

  private static EZAppleConfigurator _singleton;

  //Starting with Java 9, OSXAdapter cannot be used anymore.
  //See https://docs.oracle.com/javase/9/migrate/toc.htm , section "Removed macOS-Specific Features"
  private static boolean _canUseOsxAdapter = false;
  private static boolean _isDebugMode = false;
  
  static {
    try {//avoid any possible software crashes at this step!
      //Use by Plealog softwares, e.g. BlastViewer
      _isDebugMode = Boolean.TRUE.toString().equalsIgnoreCase(System.getProperty("V_DEBUG"));
      String cVersion  = System.getProperty("java.class.version");
      if (_isDebugMode) {
        System.out.println("java.class.version: "+cVersion);
      }
      //Format of cVersion is: X.Y (major class version dot minor class version)
      int    icVersion = Integer.valueOf(cVersion.substring(0, cVersion.indexOf('.')));
      if (_isDebugMode) {
        System.out.println("Major class version"+icVersion);
      }
      //see https://stackoverflow.com/questions/5103121/how-to-find-the-jvm-version-from-a-program
      //    https://stackoverflow.com/questions/9170832/list-of-java-class-file-format-major-version-numbers
      _canUseOsxAdapter = icVersion<=52;//This is Java 8
      if (_isDebugMode) {
        System.out.println("Use OSXAdapter: "+_canUseOsxAdapter);
      }
    } catch (Throwable e) {
    }
  }
  
  public static void initialize(String appName){
    if (_singleton!=null)
      return;
    _singleton = new EZAppleConfigurator();
    if (_canUseOsxAdapter) {
      try {
        // set the name of the application menu item
        System.setProperty("com.apple.mrj.application.apple.menu.about.name",
            appName);
        // take the menu bar off the main Frame
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
        // use as delegates for various com.apple.eawt.ApplicationListener methods
        OSXAdapter.setQuitHandler(_singleton,
            _singleton.getClass().getDeclaredMethod("quit", (Class[]) null));
        OSXAdapter.setAboutHandler(_singleton,
            _singleton.getClass().getDeclaredMethod("about", (Class[]) null));
        OSXAdapter.setPreferencesHandler(_singleton, _singleton.getClass()
            .getDeclaredMethod("preferences", (Class[]) null));
        //OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
      } catch (Throwable ex) {
        EZLogger.warn("unable to setup MacOS X integration: " + ex);
      } 
    }
    else {
      // Use Proxy to enable class compilation using java 8
      // still providing ascending compatibility with Java 9+.
      try {
        Object aboutHandler = java.lang.reflect.Proxy.newProxyInstance(
          Proxy.class.getClassLoader(),
          new java.lang.Class[] { 
              Class.forName("java.awt.desktop.AboutHandler"), 
              Class.forName("java.awt.desktop.PreferencesHandler"), 
              Class.forName("java.awt.desktop.QuitHandler") },
          new java.lang.reflect.InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method, 
                Object[] args) throws java.lang.Throwable {
              String method_name = method.getName();
    
              if (method_name.equals("handleAbout")) {
                EZEnvironment.getActionsManager().getDefaultActionHandler().handleAbout();
              } 
              else if (method_name.equals("handlePreferences")) {
                EZEnvironment.getActionsManager().getDefaultActionHandler().handlePreferences();
              } 
              else if (method_name.equals("handleQuitRequestWith")) {
                Class<?> clazz = Class.forName("java.awt.desktop.QuitResponse");
                if (EZEnvironment.getActionsManager().getDefaultActionHandler().handleExit()){
                  clazz.getMethod("performQuit", (Class[]) null).invoke(args[1]);
                }
                else {
                  clazz.getMethod("cancelQuit", (Class[]) null).invoke(args[1]);
                }
              } 
              return null;
            }
          });
        //We use the created Proxy class as handlers to Desktop
        Class<?> clazz = Class.forName("java.awt.Desktop");
        Method method = clazz.getMethod("getDesktop", (Class[]) null);
        Object deskTop = method.invoke(null);
        method = clazz.getMethod("setAboutHandler", 
            new Class<?>[] { Class.forName("java.awt.desktop.AboutHandler") });
        method.invoke(deskTop, aboutHandler);
        method = clazz.getMethod("setPreferencesHandler", 
            new Class<?>[] { Class.forName("java.awt.desktop.PreferencesHandler") });
        method.invoke(deskTop, aboutHandler);
        method = clazz.getMethod("setQuitHandler", 
            new Class<?>[] { Class.forName("java.awt.desktop.QuitHandler") });
        method.invoke(deskTop, aboutHandler);
      } catch (Exception e) {
        if (_isDebugMode){
          e.printStackTrace();
        }
      }
      //Only valid for Java 9+  
      // ... code to use as soon as we drop down support of Java 8
      /*Desktop.getDesktop().setAboutHandler(new AboutHandler() {
        @Override
        public void handleAbout(AboutEvent e) {
          EZEnvironment.getActionsManager().getDefaultActionHandler().handleAbout();
        }
      });
      Desktop.getDesktop().setPreferencesHandler(new PreferencesHandler() {
        @Override
        public void handlePreferences(PreferencesEvent e) {
          EZEnvironment.getActionsManager().getDefaultActionHandler().handlePreferences();
        }
      });
      Desktop.getDesktop().setQuitHandler(new QuitHandler() {
        @Override
        public void handleQuitRequestWith(QuitEvent e, QuitResponse response) {
          if (EZEnvironment.getActionsManager().getDefaultActionHandler().handleExit()){
            response.performQuit();
          }
          else {
            response.cancelQuit();
          }
        }
      });*/
    }
  }
  public static void enableFullScreenMode(Window window) {
    if (_canUseOsxAdapter) {
      String className = "com.apple.eawt.FullScreenUtilities";
      String methodName = "setWindowCanFullScreen";
      try {
        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod(methodName,
            new Class<?>[] { Window.class, boolean.class });
        method.invoke(null, window, true);
      } catch (Throwable t) {
        EZLogger.warn("Full screen mode is not supported");
      } 
    }
  }
  public void about() {
    EZEnvironment.getActionsManager().getDefaultActionHandler().handleAbout();
  }

  public Boolean quit() {
    return EZEnvironment.getActionsManager().getDefaultActionHandler().handleExit();

  }

  public void preferences() {
    EZEnvironment.getActionsManager().getDefaultActionHandler().handlePreferences();
  }


  public static void setDockIcon(Image image){
    if (_canUseOsxAdapter) {
      OSXAdapter.setDockIconImage(image);
    }
    else {
      try {
        // written using java.lang.reflect to compile with JDK 1.8
        Class<?> clazz = Class.forName("java.awt.Taskbar");
        Method method = clazz.getMethod("getTaskbar", (Class[]) null);
        Object tb = method.invoke(null);
        method = clazz.getMethod("setIconImage", new Class<?>[] { Image.class });
        method.invoke(tb, image);
        // as soon as we switch to JDK 1.9 or above, use:
        //Taskbar.getTaskbar().setIconImage(image);
      } catch (Exception e) {
        if (_isDebugMode){
          e.printStackTrace();
        }
      }
    }
  }
}
