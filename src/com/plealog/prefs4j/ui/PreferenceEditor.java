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

import java.util.HashSet;

import javax.swing.JComponent;

import com.plealog.prefs4j.api.PreferenceException;
import com.plealog.prefs4j.api.PreferenceSection;


/**
 * This interfaces defines a Preference Editor.
 */
public interface PreferenceEditor {
	/**UI properties associated to a Preference Section.*/
	public static enum PROPERTY {
		/**Show the Restore Default button.*/
		RESTORE_DEF_BTN, 
		/**Show the Save Default button.*/
		SAVE_AS_DEF_BTN, 
		/**Show the Header title of a Preference Section.*/
		HEADER, 
		/**Show the Help area of a Preference Section.*/
		HELP};
	/**Default Editor types available in the Framework. User-defined data editors may be added using PreferenceEditorFactory.registerEditor().*/
	public static enum TYPE {
		/**Key-Value Pair editor. This is the classic editor used with Properties Data Connector.*/
		kvp, 
		/**Text editor. This is the classic editor used with Raw Data Connector.*/
		txt, 
		/**No editor.*/
		none};
	
	/**
	 * Creates a new instance of an editor.
	 * 
	 * @param cf the PreferenceSection for which the editor has to be created
	 * @param props the set of properties to apply to the editor
	 * 
	 * @return a new instance of an editor
	 */
	public PreferenceEditor newInstance(PreferenceSection cf, HashSet<PreferenceEditor.PROPERTY> props);
	/**
	 * Returns the name of the editor.
	 */
	public String getName();
	/**
	 * Figures out whether something has been edited.
	 */
	public boolean isEdited();
	/**
	 * Saves data. 
	 *
	 * @throws PreferenceException implementation should throw a PreferenceException if any kind of errors occurs
	 * while saving data.
	 */
	public void saveData() throws PreferenceException;
	/**
	 * Returns the GUI component of the editor.
	 */
	public JComponent getEditor();
	/**
	 * Method invoked by the framework when the editor is showed.
	 */
	public void editorShowed();
	/**
	 * Returns the ConfigurationFeature associated to the editor.
	 */
	public PreferenceSection getPreferenceSection();
}
