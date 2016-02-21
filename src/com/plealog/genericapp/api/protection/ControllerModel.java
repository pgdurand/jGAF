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

import com.plealog.genericapp.protection.distrib.LicenseKeyProtector;

/**
 * The control model which enables to setup user name and associated license key.
 */
public final class ControllerModel {
	private static LicenseKeyProtector _LicenseKeyProtector;
	
	/**
	 * Use this method to setup the controller model.
	 * @param licenseKey the license key
	 * @param userName the user name
	 */
	public static final void setLicenseKeyProtectorModel(String licenseKey, String userName){
		_LicenseKeyProtector = new LicenseKeyProtector(licenseKey, userName);
	}
	
	/**
	 * Returns the Protector of the library.
	 * 
	 * @return current protector class.
	 */
	protected static final Protector getLicenseKeyProtector(){
		return _LicenseKeyProtector;
	}

}
