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
package com.plealog.prefs4j.ui;

import java.util.Enumeration;

import javax.swing.JComponent;

import com.plealog.prefs4j.api.PreferenceException;

/**
 * Defines a UIComponent for PreferenceModel data model.
 */
public interface PreferenceComponent {
	/**
	 * Returns the UI component containing all editors of the PreferenceSection
	 * contained in a PreferenceModel.
	 */
	public JComponent getComponent();
	/**
	 * Utility method that can be used to programmatically do a general save of all
	 * edited ConfigurationFeatures.
	 * 
	 * @throws PreferenceException is save operation failed on a editor.
	 */
	public void saveData() throws PreferenceException;
	/**
	 * Selects a particular editor by name.
	 */
	public void selectEditor(String editorName);
	/**
	 * Returns an enumeration over all ConfigurationEditor contained in this component.
	 */
	public Enumeration<PreferenceEditor> enumerator();
}
