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
package com.plealog.genericapp.protection.local;

import com.plealog.genericapp.protection.distrib.HStringCoder;


/**
 * This class is used to generate obfuscated strings. 
 * 
 * DO NOT distribute this class with the library.
 */
public class ObfuscateStringGenerator {

	public static void main(String[] args) {
		
		System.out.println(HStringCoder.obfuscate("GenericApp Library"));
		//System.out.println(new HStringCoder("2RuVXZpJ0YwFCcMBWayJXY5J").toString() /* => GenericApp Library */);
	}

}
