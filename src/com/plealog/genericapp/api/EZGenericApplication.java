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

import com.plealog.genericapp.ui.apple.EZAppleConfigurator;
import com.plealog.genericapp.ui.starter.EZUIStarterImplem;
import com.plealog.resources.Accessor;

/**
 * This class is the entry point of Generic Application. Basically, it aims at starting the application.
 */
public class EZGenericApplication {
	private static boolean isMacOSX() {
        return System.getProperty("os.name").indexOf("Mac OS X") >= 0;
    }
	
	private EZGenericApplication(){}
	
	/**
	 * Initializes application. To be called at the very beginning of the main() method.
	 */
	public static void initialize(String appName){
	  if(isMacOSX()){
            EZAppleConfigurator.initialize(appName);
		}
    EZEnvironment.addResourceLocator(Accessor.class);
    if(isMacOSX()){
      EZAppleConfigurator.setDockIcon(EZApplicationBranding.getAppIcon().getImage());
    }
	}
	/**
	 * Starts UI. Since this method starts a Swing-based application, it never returns until the application
	 * end.
	 * 
	 * @param args command-line arguments.
	 * */
	public static void startApplication(String[] args){
		EZUIStarterImplem starter = new EZUIStarterImplem();
		starter.startApplication(args);
	}
}
