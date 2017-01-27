/* Copyright (C) 2007-2017 Patrick G. Durand
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.plealog.genericapp.api.EZApplicationBranding;
import com.plealog.genericapp.api.EZGenericApplication;
import com.plealog.genericapp.api.configuration.DirectoryManager;
import com.plealog.genericapp.api.configuration.DirectoryManagerException;
import com.plealog.genericapp.api.configuration.DirectoryType;
import com.plealog.genericapp.api.file.EZFileUtils;
import com.plealog.genericapp.api.log.EZLogger;

/**
 * This test class illustrates the use of the DirectoryManager API.
 * 
 * @author Patrick G. Durand
 */
public class DirectoryManagerTest {

  public static void main(String[] args) {
    // 1/ This has to be done at the very beginning, i.e. first method call within
    // main(). 
    EZGenericApplication.initialize("DirectoryManagerTest");

    // 2/ Add application branding

    // it is worth noting that the following AppName will be used
    // to setup the application data directory; see below the call to
    // DirectoryManager.prepareApplicationDataPath()
    EZApplicationBranding.setAppName("DirectoryManagerTest");

    EZApplicationBranding.setAppVersion("1.0.0");
    EZApplicationBranding.setCopyRight("Patrick G. Durand");
    EZApplicationBranding.setProviderName("Plealog");

    // 3/ Deploy the directory structure of the application as defined
    // in the implementation of MyDirectoryType
    DirectoryManager.enableLogMessage(true);
    DirectoryManager.prepareApplicationDataPath(true, MyDirectoryType.getAllValues());

    // 4/
    // now, have a look at your home directory:
    // you'll see a sub-directory called ".DirectoryManagerTest" that contains
    // all directories defined in MyDirectoryType
    
    // 5/ Easy access to application data directory
    try {
      EZLogger.info("Doc path is: "+DirectoryManager.getPath(MyDirectoryType.DOCUMENTS));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // We implement a concrete DirectoryType.
  // In such a way we will be able to provide DirctoryManager with the
  // application data directory structure. The root directory will be
  // ${user_home}/.${EZApplicationBranding.getAppName()}. Then, all
  // DirectoryTypes defined below will be concrete sub-directories within
  // that root directory.
  private enum MyDirectoryType implements DirectoryType {
    // In this implementation, each DirectoryType defines:
    // a. a directory name; this is required, see getDirectory()
    // b. a numerical ID; this is optional, but we use it for
    //    the sake of simplicity in method prepareDirectory()
    CONF        ("conf",      0), 
    DOCUMENTS   ("documents", 1), 
    FILTER      ("filter",    2), 
    WEB_TEMPLATE("web",       3);

    private final String _dName;
    private final int _code;

    //enum constructor
    MyDirectoryType(String dname, int code) {
      _dName = dname;
      _code = code;
    }

    @Override
    public String getDirectory() {
      return _dName;
    }

    @Override
    public void prepareDirectory() throws DirectoryManagerException {
      // web-template?
      if (_code == 3) {
        // for instance, here, we can deploy some files in
        // the web directory
        String path;

        try {
          path = DirectoryManager.getPath(WEB_TEMPLATE);
          EZFileUtils.copyFile(
              new File("build.xml"), 
              new File(path+"build.xml")//"path" ends with File.separator
          );
        } catch (IOException ex) {
          throw new DirectoryManagerException("Unable to prepare web template directory: " + ex.toString());
        }
      }
    }

    public static List<DirectoryType> getAllValues() {
      ArrayList<DirectoryType> dts = new ArrayList<>();
      dts.add(CONF);
      dts.add(DOCUMENTS);
      dts.add(FILTER);
      dts.add(WEB_TEMPLATE);
      return dts;
    }

  }
}
