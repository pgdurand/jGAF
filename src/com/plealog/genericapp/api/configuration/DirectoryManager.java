/* Copyright (C) 2003-2017 Patrick G. Durand
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
package com.plealog.genericapp.api.configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.plealog.genericapp.api.EZApplicationBranding;
import com.plealog.genericapp.api.file.EZFileUtils;
import com.plealog.genericapp.api.log.EZLogger;
import com.plealog.genericapp.util.EZUtilities;

/**
 * This class handle the directories used by the software to store some internal
 * stuffs.
 * 
 * <br><br>
 * 
 * It is highly recommended to use this class as follows: simply call
 * {@link DirectoryManager#prepareApplicationDataPath(boolean, List)} somewhere
 * at the very beginning of your application startup code BUT after calling
 * {@link EZApplicationBranding#setAppName(String)}.
 * 
 * <br><br>
 * 
 * You can override default behavior of
 * {@link DirectoryManager#prepareApplicationDataPath(boolean, List)} method by
 * using JRE argument "V_CONF", e.g. java ... -DV_CONF=/some/path <br><br>
 * V_CONF argument is defined by constant {@link DirectoryManager#JVM_ARG_CONF}
 * that can be overridden if needed.
 * 
 * @author Patrick G. Durand
 */
public class DirectoryManager {
  public static final String NAME = "DirectoryManager";

  public static String JVM_ARG_CONF = "V_CONF";
  
  private static final String MSG1 = "Storage path: %s";
  private static final String MSG2 = "Unable to create: %s";
  private static final String MSG3 = "Unable to prepare application data path";
  private static final String MSG4 = ": %s: %s";

  private static String _appPath;
  private static boolean _enableLogMsg = true;

  /**
   * No constructor.
   */
  private DirectoryManager() {
  }

  private static void createPath(String path) throws IOException {
    File f;

    f = new File(path);
    if (f.exists()) {
      return;
    }
    if (!f.mkdirs()) {
      throw new IOException(String.format(MSG2, path));
    }
  }

  /**
   * Get the full path to the directory where the application stores its stuffs.
   * 
   * It is worth noting that the method will try to create that path if it does
   * not exist.
   * 
   * @return a path. Note that path is terminated with OS-dependent path
   *         separator character.
   * 
   * @throws IOException
   *           if the method failed to create the path.
   */
  public static String getApplicationDataPath() throws IOException {
    if (_appPath != null)
      return _appPath;

    _appPath = EZFileUtils.terminatePath(System.getProperty("user.home")) + "."
        + EZFileUtils.terminatePath(EZUtilities.replaceAll(EZApplicationBranding.getAppName(), " ", "_"));

    createPath(_appPath);

    if (_enableLogMsg)
      EZLogger.info(String.format(MSG1, _appPath));

    return _appPath;
  }

  /**
   * Set the full path to the directory where the application stores its stuffs.
   * Called from {@link DirectoryManager#prepareApplicationDataPath(boolean)}.
   * 
   * It is worth noting that the method will try to create that path if it does
   * not exist.
   * 
   * @return a path
   * 
   * @throws IOException
   *           if the method failed to create the path.
   */
  private static void setApplicationDataPath(String appPath) throws IOException {
    if (appPath != null) {
      createPath(appPath);
      _appPath = EZFileUtils.terminatePath(appPath);
      if (_enableLogMsg)
        EZLogger.info(String.format(MSG1, _appPath));
    }
  }

  /**
   * Get the full path to the directory where the application stores its data.
   * 
   * It is worth noting that the method will try to create that path if it does
   * not exist.
   * 
   * @param type
   *          directory type
   * 
   * @return an absolute path. Note that path is terminated with OS-dependent
   *         path separator character.
   * 
   * @throws IOException
   *           if the method failed to create the path.
   */
  public static String getPath(DirectoryType type) throws IOException {

    String path = getApplicationDataPath() + EZFileUtils.terminatePath(type.getDirectory());

    createPath(path);

    return path;
  }

  /**
   * Prepare the application data storage path. By default it is a directory
   * stored in the home directory of the user and called ".APP_NAME" where
   * APP_NAME is the name of the software; it is prefixed with a dot character
   * to create a hidden directory (Unix systems). Can be overridden using JRE
   * argument: DV_CONF, with value targeting a directory; use absolute path.
   *
   * @param exitIfError
   *          exit from application if true. Throw
   *          {@link DirectoryManagerException} otherwise. Mostly, it depends
   *          whether or not this module is used as a plugin in another
   *          application, or as a standalone application.
   * @param dts
   *          list of DirectoryTypes
   */
  public static void prepareApplicationDataPath(boolean exitIfError, List<DirectoryType> dts) {
    String confP = System.getProperty(JVM_ARG_CONF);
    String path = "";

    try {
      if (confP != null) {
        path = confP;
        setApplicationDataPath(confP);
      }
      for (DirectoryType dt : dts) {
        path = dt.getDirectory();
        getPath(dt);
        dt.prepareDirectory();
      }
    } catch (IOException e) {
      if (_enableLogMsg)
        EZLogger.error(String.format(MSG3 + MSG4, path, e.toString()));
      if (exitIfError) {
        System.exit(0);
      } else {
        throw new DirectoryManagerException(MSG3);
      }
    }
  }
  
  /**
   * Figure out whether or not this class can log some messages. Default is true.
   * 
   * @param enable set false to disable loggin messages from this class
   */
  public static void enableLogMessage(boolean enable){
    _enableLogMsg = enable;      
  }

}
