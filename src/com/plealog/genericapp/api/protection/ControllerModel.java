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

public final class ControllerModel {
	private static LicenseKeyProtector _LicenseKeyProtector;
	
	public static final void setLicenseKeyProtectorModel(String licenseKey, String userName){
		_LicenseKeyProtector = new LicenseKeyProtector(licenseKey, userName);
	}
	
	protected static final LicenseKeyProtector getLicenseKeyProtector(){
		return _LicenseKeyProtector;
	}

}
