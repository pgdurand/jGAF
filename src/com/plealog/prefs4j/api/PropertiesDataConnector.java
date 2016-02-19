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
import java.util.Properties;


/**
 * Defines a data connector capable of serializing standard Java Properties objects.
 */
public interface PropertiesDataConnector extends DataConnector{
	
	/**
	 * Saves a Properties.
	 * 
	 * @param locator the place where to save the Properties.
	 * @param props the properties to save
	 * 
	 * @throws IOException when serialization fails.
	 */
	public void save(String locator, Properties props) throws IOException;
	
	/**
	 * Loads a Properties.
	 * 
	 * @param locator the place where to read the Properties. It's up to the implementation
	 * to figure out how to interpret the content of the locator String.
	 * 
	 * @return the properties read from locator
	 * 
	 * @throws IOException when serialization fails.
	 */
	public Properties load(String locator) throws IOException;
	
}
