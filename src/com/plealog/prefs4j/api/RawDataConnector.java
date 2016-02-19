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

import java.io.IOException;


/**
 * Defines a data connector capable of serializing standard Java String objects.
 */
public interface RawDataConnector extends DataConnector{

	/**
	 * Saves a String. Please note that this String may be huge in size. It's up to the 
	 * implementation to handle it.
	 * 
	 * @param locator the place where to save the String.
	 * @param data the data to save
	 * 
	 * @throws IOException when serialization fails.
	 */
	public void save(String locator, String data) throws IOException;
	
	/**
	 * Loads a String. Please note that this String may be huge in size. It's up to the 
	 * implementation to handle it.
	 * 
	 * @param locator the place where to load the String.
	 * 
	 * @return the String read from locator
	 * 
	 * @throws IOException when serialization fails.
	 */
	public String load(String locator) throws IOException;
}
