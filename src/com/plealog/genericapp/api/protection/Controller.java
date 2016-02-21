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
package com.plealog.genericapp.api.protection;

/**
 * Use this class to control application protection. It relies on a protection model.
 * First, setup a protection model using ControllerModel, then in the rest of the application, 
 * retrieve the Protector from this class and check its validity.
 * 
 */
public final class Controller {
	/**
	 * Returns the Protector of the library.
	 * 
	 * @return the Protector of the library.
	 */
  public static final Protector getProtector(){
		return ControllerModel.getLicenseKeyProtector();
	}
}
