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

import java.awt.Frame;
import java.util.Set;

import com.plealog.prefs4j.api.PreferenceException;
import com.plealog.prefs4j.api.PreferenceModel;
import com.plealog.prefs4j.implem.ui.components.PreferenceDialog;
import com.plealog.prefs4j.implem.ui.components.PreferencePanel;

/**
 * This factory defines method to instantiates PreferenceModel viewer.
 * 
 */
public class PreferenceUIFactory {
	private PreferenceUIFactory(){}
	/**
	 * Display the PreferenceModel Editor Dialog Box. Dialog box is modal.
	 */
	public static void showPreferenceDialog(Frame owner, String dlgTitle, PreferenceModel model) throws PreferenceException{
		PreferenceDialog dlg;
		
		dlg = new PreferenceDialog(owner, dlgTitle, model);
		dlg.showDlg();
	}
	
	/**
	 * Display the PreferenceModel Editor Dialog Box. Dialog box is modal.
	 */
	public static void showPreferenceDialog(Frame owner, String dlgTitle, PreferenceModel model, Set<PreferenceEditor.PROPERTY> props) throws PreferenceException{
		PreferenceDialog dlg;
		
		dlg = new PreferenceDialog(owner, dlgTitle, model, props);
		dlg.showDlg();
	}
	/**
	 * Creates a UI Component given a PreferenceModel.
	 */
	public static PreferenceComponent getPreferenceComponent(PreferenceModel model) throws PreferenceException{
		return new PreferencePanel(model);
	}
}
