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

import javax.swing.ImageIcon;

import com.plealog.genericapp.ui.apple.EZAppleConfigurator;

/**
 * Contains the branding of the application.
 * This class is used as a singleton, so use static setter methods. If not used, this class automatically
 * defines application name and version using library branding.
 */
public class EZApplicationBranding {
    private static String           _appName = EZEnvironment.getMessage("__EZ.appname");
    private static String           _appVersion = EZEnvironment.getMessage("__EZ.appversion");
    private static String           _providerName = EZEnvironment.getMessage("__EZ.provider");
    private static String           _copyright = EZEnvironment.getMessage("__EZ.copyright")+" - "+_providerName;
    private static ImageIcon        _appIcon = EZEnvironment.getImageIcon(EZEnvironment.getMessage("__EZ.appicon"));
    
    private EZApplicationBranding(){
    	
    }
    
    /**
     * Sets the application name.
     */
    public static void setAppName(String name){
        if (name!= null && name.length()>0){
            _appName = name;   
        }
    }
    
    /**
     * Gets the application name.
     */
    public static String getAppName(){
        return _appName;   
    }

    /**
     * Sets the application version.
     */
    public static void setAppVersion(String ver){
        if (ver!= null && ver.length()>0){
            _appVersion = ver;   
        }
    }
    
    /**
     * Gets the application version.
     */
    public static String getAppVersion(){
        return _appVersion;   
    }

    /**
     * Sets the application icon.
     */
    public static void setAppIcon(ImageIcon icon){
    	if (icon!= null){
    		_appIcon = icon;   
    		if(EZEnvironment.getOSType()==EZEnvironment.MAC_OS){
    			EZAppleConfigurator.setDockIcon(_appIcon.getImage());
    		}
        }
    }

    /**
     * Gets the application icon.
     */
    public static ImageIcon getAppIcon(){
    	return _appIcon;
    }
        
    /**
     * Sets the application provider.
     */
    public static void setProviderName(String provider){
    	_providerName = provider;
    }

    /**
     * Gets the application provider.
     */
    public static String getProviderName(){
    	return _providerName;
    }
    
    /**
     * Sets the application provider.
     */
    public static void setCopyRight(String copy){
    	_copyright = copy;
    }

    /**
     * Gets the application provider.
     */
    public static String getCopyRight(){
    	return _copyright;
    }
}
