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
package com.plealog.genericapp.api.log;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JComponent;

//http://www.vogella.com/tutorials/Logging/article.html
/**
 * Utility class to setup logging system. This class is a convenient wrapper to setup
 * default Java Logging system.
 */
public class EZLoggerManager {

  private static EZUIHandler txtAreaHandler;

  private static boolean     enableConsoleLogger = true;
  private static boolean     enableUILogger = false;
  private static LogLevel    lvl = LogLevel.info;
  private static Formatter   consoleLoggerFormatter;
  private static Formatter   uiLoggerFormatter;
  private static Formatter   fileLoggerFormatter;
  private static FileHandler fileHandler = null;
  private static int         uiLoggerSizeLimit;
  private static boolean     initialized = false;
  
  public static enum LogLevel {error, warn, info, debug};
  
  /**
   * Set the log level. This method can be called before and after initialize() method.
   * Default is INFO level. EZLoggerManager only handles SEVERE, WARNING, INFO and CONFIG levels. 
   * CONFIG level is used as a DEBUG-like level.
   * 
   * @param lvl the log level.
   */
  public static void setLevel(LogLevel lvl){
    updateLevel(lvl);
    if (txtAreaHandler!=null){
      txtAreaHandler.changeLevel(lvl);
    }
  }
  /**
   * For internal use only.
   */
  protected static void updateLevel(LogLevel lvl){
    //System.out.println("updateLevel: "+lvl);
    Level level;
    
    EZLoggerManager.lvl = lvl;
    //adapted from http://blog.cag.se/?p=835
    Logger logger = Logger.getLogger("");
    
    switch(lvl){
      case error:
        level = Level.SEVERE;
        break;
      case warn:
        level = Level.WARNING;
        break;
      case info:
        level = Level.INFO;
        break;
      case debug:
      default:
        level = Level.CONFIG;
          break;
    }
    logger.setLevel(level);
    Handler[] handlers = logger.getHandlers();
    for(Handler h : handlers){
      h.setLevel(level);
    }
  }
  /**
   * Returns the current log level.
   */
  public static LogLevel getLevel(){
    return EZLoggerManager.lvl;
  }
  /**
   * Figures out whether or not the default Java Logging Console logger has to be enabled.
   * Default is true.
   * 
   * @param enable use true to enable console logger, false otherwise.
   */
  public static void enableConsoleLogger(boolean enable){
    enableConsoleLogger = enable;
  }
  /**
   * Figures out whether or not the UI logger has to be enabled.
   * Default is false.
   * 
   * @param enable use true to enable UI logger, false otherwise.
   */
  public static void enableUILogger(boolean enable, int sizeLimit){
    enableUILogger = enable;
    uiLoggerSizeLimit = Math.max(1, sizeLimit);
  }
  /**
   * Figures out whether or not a file logger has to be enabled.
   * Default is false.
   * 
   * @param fileName the file name to use. Parameter can be a simple file name, or a full path
   * to a file.
   * @param append use true to append log content to the file over application sessions.
   * 
   * @throws IOException if file cannot be created.
   */
  public static void enableFileLogger(String fileName, boolean append) throws IOException{
    fileHandler = new FileHandler(fileName, append);
  }
  /**
   * Sets the console logger formatter. Default is SimpleFormatter.
   * 
   * See java.util.logging.SimpleFormatter.
   */
  public static void setConsoleLoggerFormatter(Formatter formatter){
    consoleLoggerFormatter = formatter;
  }
  /**
   * Sets the UI logger formatter. Default is SimpleFormatter.
   * 
   * See java.util.logging.SimpleFormatter.
   */
  public static void setUILoggerFormatter(Formatter formatter){
    uiLoggerFormatter = formatter;
  }
  /**
   * Sets the file logger formatter. Default is SimpleFormatter.
   * 
   * See java.util.logging.SimpleFormatter.
   */
  public static void setFileLoggerFormatter(Formatter formatter){
    fileLoggerFormatter = formatter;
  }

  /**
   * Call this method to setup the logging framework. A call to this method has to be done 
   * after calling the various enableXXX() and setXXX() methods of this class, as needed.
   */
  public static void initialize(){
    Logger logger = Logger.getLogger("");

    //from: http://stackoverflow.com/questions/6029454/disabling-awt-swing-debug-fine-log-messages
    Logger.getLogger("java.awt").setLevel(Level.OFF);
    Logger.getLogger("sun.awt").setLevel(Level.OFF);
    Logger.getLogger("sun.lwawt").setLevel(Level.OFF);
    Logger.getLogger("javax.swing").setLevel(Level.OFF);

    if (uiLoggerFormatter==null){
      uiLoggerFormatter = new SimpleFormatter();
    }

    if (consoleLoggerFormatter==null){
      consoleLoggerFormatter = new SimpleFormatter();
    }
    Handler[] handlers = logger.getHandlers();
    // suppress the logging output to the console
    if (!enableConsoleLogger){
      if (handlers[0] instanceof ConsoleHandler) {
        logger.removeHandler(handlers[0]);
      }
    }
    else{
      if (handlers[0] instanceof ConsoleHandler) {
        handlers[0].setFormatter(consoleLoggerFormatter);
      }
    }

    setLevel(lvl);

    if(fileHandler!=null){
      if (fileLoggerFormatter==null){
        fileLoggerFormatter = new SimpleFormatter();
      }
      fileHandler.setFormatter(fileLoggerFormatter);
      logger.addHandler(fileHandler);
    }

    if(enableUILogger){
      txtAreaHandler = new EZUIHandler(uiLoggerSizeLimit);
      txtAreaHandler.setFormatter(uiLoggerFormatter);
      logger.addHandler(txtAreaHandler);
    }
    
    initialized = true;
  }

  /**
   * For internal use only.
   */
  protected static boolean isInitialized(){
    return initialized;
  }
  /**
   * Returns the UI Logger component.
   * */
  public static JComponent getUILogger() {
    if (txtAreaHandler!=null)
      return txtAreaHandler.getComponent();
    else
      return null;
  }
}
