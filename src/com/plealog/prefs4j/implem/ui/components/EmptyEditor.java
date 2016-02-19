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
package com.plealog.prefs4j.implem.ui.components;

import java.awt.BorderLayout;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.plealog.genericapp.ui.starter.EZEnvironmentImplem;
import com.plealog.prefs4j.api.PreferenceException;
import com.plealog.prefs4j.api.PreferenceSection;
import com.plealog.prefs4j.ui.PreferenceEditor;

@SuppressWarnings("serial")
public class EmptyEditor extends JPanel implements PreferenceEditor {
	private JLabel header;
	
	public EmptyEditor(){}
	
	public EmptyEditor(PreferenceSection co){
		this(co, new HashSet<PreferenceEditor.PROPERTY>());
	}
	public EmptyEditor(PreferenceSection co, HashSet<PreferenceEditor.PROPERTY> props){
		JPanel headerPanel, pnl;
		
		pnl = new JPanel(new BorderLayout());
		header = new JLabel("Expand the tree to edit preferences for a specific feature.");
		pnl.add(header, BorderLayout.NORTH);
		pnl.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
		
		headerPanel = new JPanel(new BorderLayout());
		if (props.contains(PreferenceEditor.PROPERTY.HEADER))
			headerPanel.add(EZEnvironmentImplem.getTitlePanel(co.getName()), BorderLayout.NORTH);
		
		this.setLayout(new BorderLayout());
		this.add(headerPanel, BorderLayout.NORTH);
		this.add(pnl, BorderLayout.WEST);
	}
	public PreferenceEditor newInstance(PreferenceSection co, HashSet<PreferenceEditor.PROPERTY> props){
    	return new EmptyEditor(co, props);
    }
	
	@Override
	public String getName() {
		return "None";
	}

	@Override
	public boolean isEdited() {
		return false;
	}

	@Override
	public void saveData() throws PreferenceException{
	}

	@Override
	public JComponent getEditor() {
		return this;
	}

	@Override
	public void editorShowed() {
	}

	@Override
	public PreferenceSection getPreferenceSection(){
		return null;
	}

}
