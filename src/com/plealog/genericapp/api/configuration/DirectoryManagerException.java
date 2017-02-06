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
 * A RuntimeException flavor for the Directory manager.
 * 
 * @author Patrick G. Durand
 */
public class DirectoryManagerException extends RuntimeException {
  private static final long serialVersionUID = 8646674334142417052L;

  /**
   * Constructor.
   * 
   * @param message a message
   */
  public DirectoryManagerException(String message) {
    super(message);
  }
}
