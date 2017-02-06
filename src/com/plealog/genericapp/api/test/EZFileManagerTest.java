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

import com.plealog.genericapp.api.file.EZFileManager;
import com.plealog.genericapp.api.log.EZLogger;

/**
 * Make some UI tests on the EZFileManager dialogs.
 * 
 * @author Patrick G. Durand
 */
public class EZFileManagerTest {

  public static void main(String[] args) {
    File f;

    f = EZFileManager.chooseDirectory();
    if (f != null)
      EZLogger.info("Chosen directory is: " + f.getAbsolutePath());
    
    f = EZFileManager.chooseFileForOpenAction();
    if (f != null)
      EZLogger.info("Chosen file is: " + f.getAbsolutePath());
    
    File[] files ;
    
    files = EZFileManager.chooseFilesForOpenAction();
    if (files != null){
      for(File ff : files){
        EZLogger.info("Chosen file is: " + ff.getAbsolutePath());
      }
    }
    
    EZFileManager.useOSNativeFileDialog(false);

    files = EZFileManager.chooseFilesForOpenAction();
    if (files != null){
      for(File ff : files){
        EZLogger.info("Chosen file is: " + ff.getAbsolutePath());
      }
    }
  }

}
