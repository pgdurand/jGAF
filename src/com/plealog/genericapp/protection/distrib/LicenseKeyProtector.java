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
package com.plealog.genericapp.protection.distrib;

import com.plealog.genericapp.api.protection.Protector;

/**
 * Concrete implementation of a Protector relying on a user-name and license key pair of values.
 */
public final class LicenseKeyProtector implements Protector {
	private String _licenseKey;
	private String _userName;
	
	private LicenseKeyProtector(){
		super();
	}
	
	public LicenseKeyProtector(String licenseKey, String userName) {
		this();
		this._licenseKey = licenseKey;
		this._userName = userName;
	}

	private final boolean hasKeys(){
		return (_licenseKey!=null && _userName!=null);
	}
	private final boolean isLibraryOk(){
		return _licenseKey.equals(LicenseKeyController.generate(_userName));
	}
	@Override
	public final boolean isLibraryInvalid() {
		if (hasKeys()){
			return !isLibraryOk();
		}
		else{
	    return false;
		}
	}

  @Override
  public boolean isLibraryExpired() {
    return LibraryDateProtectorController.libExpired();
  }

}
