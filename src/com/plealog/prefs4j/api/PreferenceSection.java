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

import javax.swing.ImageIcon;

/**
 * This interface defines a preference section. When considering the Preference Dialogue Box, such
 * a section consists in a configuration page associated to a Preference Section Node. Each Preference Section
 * is associated to a Framework Descriptor File. 
 * 
 */
public interface PreferenceSection {
	/**
	 * Returns the name of this section.
	 */
	public String getName();
	/**
	 * Returns the unique ID of this section.
	 */
	public String getId();
	/**
	 * Returns the parent ID of this section.
	 */
	public String getParentId();
	/**
	 * Returns the icon of this section. May be null.
	 */
	public ImageIcon getIcon();
	/**
	 * Returns the absolute path to a resource file. May be null.
	 */
	public String getResourceLocator();
	/**
	 * Returns the absolute path to the configuration file that contains Section's configuration values.
	 * Cannot be null.
	 */
	public String getConfigurationLocator();
	/**
	 * Returns the absolute path to the Descriptor file that defines the data model of this section.
	 * Cannot be null.
	 */
	public String getDescriptorLocator();
	/**
	 * Returns the absolute path to the configuration file that contains Section's configuration default values.
	 * May be null.
	 */
	public String getDefaultConfigurationLocator();
	/**
	 * Returns the type of this section. 
	 * 
	 * @return one of DataConnector.TYPE values, or any additional user-defined values
	 * registered through DataConnectorFactory.registerDataConnector(String, dataConnector).
	 */
	public String getConfType();
	/**
	 * Returns the DataConnector instance object associated to this section.
	 */
	public DataConnector getDataConnector();
}
