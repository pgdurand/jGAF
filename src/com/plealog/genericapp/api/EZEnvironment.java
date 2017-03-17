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
package com.plealog.genericapp.api;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import com.plealog.genericapp.api.persistense.PrefSafeBox;
import com.plealog.genericapp.ui.menu.EZActionManager;
import com.plealog.genericapp.ui.starter.EZEnvironmentImplem;

/**
 * This class contains various methods to handle application environment.
 */
public class EZEnvironment {

  /** Windows OS. */
  public static final int      WINDOWS_OS = 0;
  /** MacOSX OS. */
  public static final int      MAC_OS     = 1;
  /** Linux OS. */
  public static final int      LINUX_OS   = 2;
  /** Other OS. */
  public static final int      OTHER_OS   = 3;
  /** Access this string array using the xxx_OS constants defined here */
  public static final String[] OS_NAMES   = { "windows", "macos", "linux",
      "other"                            };

  private static PrefSafeBox   _persistense;

  private EZEnvironment() {
  }

  /**
   * Returns a message from the user-defined messages resource bundle.
   * 
   * @param key
   *          message key.
   * 
   * @see EZEnvironment#setUserDefinedMessagesResourceBundle(ResourceBundle)
   */
  public static String getMessage(String key) {
    return EZEnvironmentImplem.getMessage(key);
  }

  /**
   * Returns an image given a key. Images are usually contains within a resource
   * package. Please refer to {@link EZEnvironment#addResourceLocator(Class)}
   * methods.
   * 
   * @param key
   *          image key.
   */
  public static ImageIcon getImageIcon(String key) {
    return EZEnvironmentImplem.getImageIcon(key);
  }

  /**
   * Add a class used to enable JRE to locate resources such as images.
   */
  public static void addResourceLocator(Class<?> cl) {
    EZEnvironmentImplem.addResourceLocator(cl);
  }

  /**
   * Remove a class used to enable JRE to locate resources such as images.
   */
  public static void removeResourceLocatro(Class<?> cl) {
    EZEnvironmentImplem.removeResourceLocator(cl);
  }

  /**
   * Clear the resource locator.
   */
  public static void clearResourceLocator() {
    EZEnvironmentImplem.clearResourceLocator();
  }

  /**
   * Returns the operating system type.
   * 
   * @return one of the OS_XXX constants.
   */
  public static int getOSType() {
    return EZEnvironmentImplem.getOSType();
  }

  /**
   * Returns the operating system name.
   */
  public static String getOSName() {
    return EZEnvironment.OS_NAMES[getOSType()];
  }

  /**
   * Sets the resource bundle that contains your messages.
   */
  public static void setUserDefinedMessagesResourceBundle(ResourceBundle rb) {
    EZEnvironmentImplem.setUserDefinedMessagesResourceBundle(rb);
  }

  /**
   * Sets the resource bundle that contains your Menu Actions.
   */
  public static void setUserDefinedActionsResourceBundle(ResourceBundle rb) {
    EZEnvironmentImplem.setUserDefinedActionsResourceBundle(rb);
  }

  /**
   * Returns the ActionManager singleton instance.
   */
  public static EZActionManager getActionsManager() {
    return EZEnvironmentImplem.getActionsManager();
  }

  /**
   * Returns the application main frame.
   */
  public static Frame getParentFrame() {
    return EZEnvironmentImplem.getParentFrame();
  }

  /**
   * Sets the application main frame. For internal use only: this method is
   * automatically called by the Generic Application Framework when creating the
   * application main frame.
   */
  public static void setParentFrame(Frame compo) {
    EZEnvironmentImplem.setParentFrame(compo);
  }

  /**
   * Sets the application command-line arguments. For internal use only: this
   * method is automatically called by the Generic Application Framework at
   * startup.
   */
  public static void setApplicationArguments(String[] args) {
    EZEnvironmentImplem.setApplicationArguments(args);
  }

  /**
   * Returns the application command-line arguments.
   */
  public static String[] getApplicationArguments() {
    return EZEnvironmentImplem.getApplicationArguments();
  }

  /**
   * Registers a UIStarterListener.
   */
  public static void setUIStarterListener(EZUIStarterListener listener) {
    EZEnvironmentImplem.setUIStarterListener(listener);
  }

  /**
   * Returns the current UIStarterListener.
   */
  public static EZUIStarterListener getUIStarterListener() {
    return EZEnvironmentImplem.getUIStarterListener();
  }

  /**
   * Sets the Preferences configuration file.
   */
  public static void setPreferencesConfigurationFile(String confFile) {
    EZEnvironmentImplem.setMasterConfigurationFile(confFile);
  }

  /**
   * Gets the Preferences configuration file.
   */
  public static String getPreferencesConfigurationFile() {
    return EZEnvironmentImplem.getMasterConfigurationFile();
  }

  /**
   * Display the Wait cursor.
   */
  public static void setWaitCursor() {
    EZEnvironmentImplem.setWaitCursor();
  }

  /**
   * Display the Arrow cursor.
   */
  public static void setDefaultCursor() {
    EZEnvironmentImplem.setDefaultCursor();
  }

  /**
   * Display the Hand cursor.
   */
  public static void setHandCursor() {
    EZEnvironmentImplem.setHandCursor();
  }

  /**
   * Display an input value modal dialogue box.
   * 
   * @param parent
   *          parent of this dialogue box. Either another dialogue box or the
   *          main frame. Passing null is authorized; in that case, the main
   *          frame is considered as the parent of the dialogue box.
   * @param msg
   *          message
   * @param initValue
   *          initial value displayed in the dialogue box
   * 
   * @return the user-defined value or null if dialogue has been canceled.
   */
  public static String inputValueMessage(Component parent, String msg,
      String initValue) {
    return EZEnvironmentImplem.inputValueMessage(parent, msg, initValue);
  }

  /**
   * Display an input value modal dialogue box.
   * 
   * @param parent
   *          parent of this dialogue box. Either another dialogue box or the
   *          main frame. Passing null is authorized; in that case, the main
   *          frame is considered as the parent of the dialogue box.
   * @param msg
   *          message
   * 
   * @return the user-defined value or null if dialogue has been canceled.
   */
  public static String inputValueMessage(Component parent, String msg) {
    return EZEnvironmentImplem.inputValueMessage(parent, msg);
  }

  /**
   * Display a confirmation modal dialogue box.
   * 
   * @param parent
   *          parent of this dialogue box. Either another dialogue box or the
   *          main frame. Passing null is authorized; in that case, the main
   *          frame is considered as the parent of the dialogue box.
   * @param msg
   *          message
   * 
   * @return user answer.
   */
  public static boolean confirmMessage(Component parent, String msg) {
    return EZEnvironmentImplem.confirmMessage(parent, msg);
  }

  /**
   * Display an error modal dialogue box.
   * 
   * @param parent
   *          parent of this dialogue box. Either another dialogue box or the
   *          main frame. Passing null is authorized; in that case, the main
   *          frame is considered as the parent of the dialogue box.
   * @param msg
   *          message
   */
  public static void displayErrorMessage(Component parent, String msg) {
    EZEnvironmentImplem.displayErrorMessage(parent, msg);
  }

  /**
   * Display a warning modal dialogue box.
   * 
   * @param parent
   *          parent of this dialogue box. Either another dialogue box or the
   *          main frame. Passing null is authorized; in that case, the main
   *          frame is considered as the parent of the dialogue box.
   * @param msg
   *          message
   */
  public static void displayWarnMessage(Component parent, String msg) {
    EZEnvironmentImplem.displayWarnMessage(parent, msg);
  }

  /**
   * Display an information modal dialogue box.
   * 
   * @param parent
   *          parent of this dialogue box. Either another dialogue box or the
   *          main frame. Passing null is authorized; in that case, the main
   *          frame is considered as the parent of the dialogue box.
   * @param msg
   *          message
   */
  public static void displayInfoMessage(Component parent, String msg) {
    EZEnvironmentImplem.displayInfoMessage(parent, msg);
  }

  /**
   * Figures out whether or not the application asks to user if the application
   * can be closed. Default is true. Usually this method is called when
   * implementing {@link EZDefaultActionHandler#handleExit()} method.
   */
  public static boolean confirmBeforeExit() {
    return EZEnvironmentImplem.confirmBeforeExit();
  }

  /**
   * Sets whether or not the application asks to user if the application can be
   * closed.
   */
  public static void setConfirmBeforeExit(boolean confirm) {
    EZEnvironmentImplem.setConfirmBeforeExit(confirm);
  }

  /**
   * Sets a persistent application property.
   * 
   * @param key
   *          property key
   * @param value
   *          property value
   */
  public static void setApplicationProperty(String key, String value) {
    if (_persistense == null) {
      _persistense = new PrefSafeBox();
    }
    _persistense.setProperty(key, value);
    _persistense.save();
  }

  /**
   * Returns a persistent property.
   * 
   * @param key
   *          property key
   * 
   * @return property value or null if not found
   */
  public static String getApplicationProperty(String key) {
    if (_persistense == null) {
      _persistense = new PrefSafeBox();
    }
    return _persistense.getProperty(key);
  }

  /**
   * Clears all stored application properties.
   */
  public static void resetApplicationProperty() {
    if (_persistense == null) {
      _persistense = new PrefSafeBox();
    }
    _persistense.uninstall();
  }

  /**
   * Returns list of keys of persistent property.
   * 
   * @return list or null if no properties available
   */
  public static List<String> getApplicationPropertyKeys() {
    return _persistense == null ? null : _persistense.getPropertyKeys();
  }

  /**
   * Set system text color
   * 
   * @param color
   */
  public static void setSystemTextColor(Color color) {
    EZEnvironmentImplem.setSystemTextColor(color);
  }

  /**
   * Return system text color
   * 
   * @return system text color
   */
  public static Color getSystemTextColor() {
    return EZEnvironmentImplem.getSystemTextColor();
  }
}
