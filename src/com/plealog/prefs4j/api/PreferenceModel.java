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

import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;

import com.plealog.prefs4j.implem.core.event.PreferenceModelListener;

/**
 * This interface defines the Preference data model.
 */
public interface PreferenceModel {
	/**
	 * Returns the location of the resource file. By now, the Framework only 
	 * handles File locations, i.e. absolute paths.
	 */
	public String getResourceLocator();
	/**
	 * Returns the location of the configuration model file. By now, the Framework 
	 * only handles File locations, i.e. absolute paths.
	 */
	public String getPreferenceModelLocator();
	/**
	 * Returns the location of the user configuration. By now, the Framework only 
	 * handles File locations, i.e. absolute paths.
	 */
	public String getUserPreferenceLocator();
	/**
	 * Returns a enumeration over the ConfurationFeatures defined in this model.
	 */
	public Enumeration<PreferenceSection> enumerator();
	/**
	 * Returns the Tree representation of the PreferenceSection hierarchy.
	 */
	public MutableTreeNode getPreferenceSectionsHierarchy();
	/**
	 * Adds a listener to this model.
	 */
	public void addPreferenceModelListener(PreferenceModelListener l);
	/**
	 * Remove a listener from this model.
	 */
	public void removePreferenceModelListener(PreferenceModelListener l);
}