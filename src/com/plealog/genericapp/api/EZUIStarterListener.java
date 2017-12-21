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

import java.awt.Component;

/**
 * Interface enabling to listen to application startup procedure.
 * 
 */
public interface EZUIStarterListener {
	
	/**
	 * This method is called at the very beginning of application startup procedure.
	 */
	public void preStart();
	
	/**
	 * This method is called when the application frame is about to be displayed.
	 */
	public void postStart();
	
  /**
   * This method is called when the application frame has been displayed.
   */
	public void frameDisplayed();
	
	/**
	 * This method is called when application is going to install the user-defined
	 * component within the main frame.
	 */
	public Component getApplicationComponent();
	
	/**
	 * This method is called whenthe application is about to quit.
	 * 
	 * @return false to prevent application from terminating. Return true is application can exit.
	 */
	public boolean isAboutToQuit();
	
}
