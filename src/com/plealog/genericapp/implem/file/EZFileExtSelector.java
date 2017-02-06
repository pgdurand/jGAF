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
package com.plealog.genericapp.implem.file;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.plealog.genericapp.ui.common.ResizableComboboxPopupMenuListener;

/**
 * This is a basic component aims at displaying a list of {@link EZFileExtDescriptor} within
 * a JComboBox. For internal use only
 */
public class EZFileExtSelector extends JPanel{
	private static final long serialVersionUID = 3087118831467808734L;

	private JComboBox<EZFileExtDescriptor> _selector;
	private List<EZFileExtDescriptor> _types;
	
	public EZFileExtSelector(List<EZFileExtDescriptor> types){
		_types = types;
		createGUI();
	}
	public EZFileExtDescriptor getSelectedFType(){
		return (EZFileExtDescriptor)_selector.getSelectedItem();
	}
	private void createGUI(){
		JPanel pnl;

		_selector = new JComboBox<>();
		_selector.addPopupMenuListener(new ResizableComboboxPopupMenuListener());
		for(EZFileExtDescriptor dft:_types){
			_selector.addItem(dft);
        }
		pnl = new JPanel(new BorderLayout());
		pnl.add(_selector, BorderLayout.CENTER);
		pnl.setBorder(BorderFactory.createTitledBorder("Export format to use:"));
		this.setLayout(new BorderLayout());
		this.add(pnl, BorderLayout.CENTER);
	}
}
