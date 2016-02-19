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
package com.plealog.prefs4j.api;

import com.plealog.prefs4j.implem.core.PreferenceModelImplem;

/**
 * This class can be used to create concrete PreferenceModel instances.
 * 
 */
public class PreferenceModelFactory {
	private PreferenceModelFactory(){}
	/**
	 * Creates a new PreferenceModel instance.
	 * 
	 * @param masterPrefLocator the location to the master preference descriptor file. Use absolute file path.
	 */
	public static PreferenceModel getModel(String masterPrefLocator){
		return new PreferenceModelImplem(masterPrefLocator);
	}
	/**
	 * Creates a new PreferenceModel instance capable of handling a separation location
	 * to handle user-defined specific preferences.
	 * 
	 * @param masterPrefLocator the location to the master preference descriptor file. Use absolute file path.
	 * @param userPrefLocator the location handling user-specific configuration files.
	 */
	public static PreferenceModel getModel(String masterPrefLocator, String userPrefLocator){
		return new PreferenceModelImplem(masterPrefLocator, userPrefLocator);
	}
}
