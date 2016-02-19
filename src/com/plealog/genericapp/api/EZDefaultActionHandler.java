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

/**
 * This interface defines the way to handle default application actions About, Exit and Preferences.
 */
public interface EZDefaultActionHandler {

	/**
	 * Handles the About application action.
	 */
	public void handleAbout();
	
	/**
	 * Handles the Exit application action. When creating a implementation of this method, please 
	 * do not forget to conform to {@link EZEnvironment#confirmBeforeExit()} method. 
	 * 
	 * @return to handle MacOS X integration, implementation should return true if application
	 * can exit. Use false otherwise. For all other operating systems, this method can use
	 * System.exit() method to finish application. 
	 */
	public boolean handleExit();
	
	/**
	 * Handles the Preferences application action.
	 */
	public void handlePreferences();
}
