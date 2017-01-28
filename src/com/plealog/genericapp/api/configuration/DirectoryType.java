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

/**
 * A directory type defines a particular local data storage. It has to be seen
 * as a sub-directory located in
 * {@link DirectoryManager#getApplicationDataPath()}
 * 
 * @author Patrick G. Durand
 */
public interface DirectoryType {
  /**
   * Return directory name. It can be a simple string or a path. In the latter
   * case, DO NOT start path with the path separator character.
   * 
   * @return a directory name.
   */
  public String getDirectory();

  /**
   * Prepare the directory. This method is automatically called when calling
   * {@link DirectoryManager#prepareApplicationDataPath(boolean, List)}, and has the
   * opportunity to do some preparation of this particular sub-directory.
   * 
   * @throws DirectoryManagerException
   *           method implementation is advised to throw such an exception in
   *           case or error
   */
  public void prepareDirectory() throws DirectoryManagerException;
}
