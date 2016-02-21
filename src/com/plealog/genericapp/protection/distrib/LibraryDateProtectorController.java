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

/**
 * Auto generated class: do not modify by hand!
 * */
import java.util.Calendar;
import java.util.Date;

/**
 * Class used to control the validity of the library given an expiration date.
 */
public final class LibraryDateProtectorController {
	/**
	 * Returns true if the library has expired.
	 * 
	 * @return Returns true if the library has expired.
	 */
	public static final boolean libExpired(){
		String                 uid, control;
        boolean                bRet = false;
        Date                   d1, d2;
        
		try {
			uid = HStringUtils.decryptHexString(new HStringCoder("TM1I").toString());
			control = HStringUtils.decryptHexString(new HStringCoder("TM1I").toString());
			if (uid.hashCode()!=Integer.valueOf(control)){
				bRet = true;
			}
			d1 = Calendar.getInstance().getTime();
			d2 = LibraryProtectorBase.DATE_FORM.parse(uid);
			if (d1.after(d2)){
				bRet = true;
			}
		} catch (Exception e) {
			bRet = true;
		}
		return bRet;
	}
}
