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
package com.plealog.genericapp.ui.common;

import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * This class can be used for JCombobox requiring that the displayed popup list
 * be resized according its content.
 * 
 * @author Patrick Durand, Korilog SARL
 */
public class ResizableComboboxPopupMenuListener implements PopupMenuListener{
	//code adapted from http://forums.java.net/jive/message.jspa?messageID=61267
	public ResizableComboboxPopupMenuListener() {
	}
	public void popupMenuCanceled(PopupMenuEvent e) {}
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		FontMetrics fm;
		JComboBox<?> box;
		JComponent  compo;
		JPopupMenu  popup = null;
		Dimension   dim;
		Object      obj;
		int         i, nItems, curSize, maxSize;
		
		//get the Popup displaying the list of items
		box = (JComboBox<?>) e.getSource();
		nItems = box.getUI().getAccessibleChildrenCount(box);
		for(i=0;i<nItems;i++){
			obj = box.getUI().getAccessibleChild(box, i);
			if (obj instanceof JPopupMenu){
				popup = (JPopupMenu) obj;
				break;
			}
		}
		if (popup == null) {
			return;
		}
		//compute the largest cell
		nItems = box.getModel().getSize();
		maxSize = 0;
		fm = box.getFontMetrics(box.getFont());
		for(i=0;i<nItems;i++){
			obj = box.getModel().getElementAt(i);
			if (obj!=null){
			curSize = fm.stringWidth(obj.toString());
			if (curSize>maxSize)
				maxSize = curSize;
			}
		}
		//a dirty hack for scroll bar width, panel border, ...
		maxSize += 25;
		//adjust the popup width if needed
		compo = (JComponent) popup.getComponent(0);
		dim = compo.getPreferredSize();
		if (maxSize<dim.width){
			return;
		}
		//set a limit to the size of the menu
		if (maxSize>=640)
			maxSize = 640;
		dim.width = maxSize;
		compo.setPreferredSize(dim);
		compo.setMaximumSize(dim);
	}
}