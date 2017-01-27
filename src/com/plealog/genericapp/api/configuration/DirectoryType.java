/* Copyright (C) 2007-2017 Patrick G. Durand
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/agpl-3.0.txt
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
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
