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
package com.plealog.prefs4j.implem.ui.model;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * This class defines an action selecting an entry within a Choice.
 * 
 * @author Patrick G. Durand
 */
public class FormActionSelectValue extends AbstractFormAction{
    private String _key;
    
    public FormActionSelectValue(){
    }

    public FormActionSelectValue(String name, String targetItemName, String key){
        setName(name);
        setTargetItemName(targetItemName);
        setKey(key);
    }
	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		_key = key;
	}
    private void selectComboValue(ItemGUIContainer target){
        Item               item;
        JComboBox<ChoiceEntry> combo;
        Choice             choice;
        ChoiceEntry        ce;
        
        item = target.getItem();
        combo = target.getCombo();
        choice = item.getChoice();
        ce = choice.getEntry(_key); 
        if (ce!=null){
            combo.setSelectedItem(ce);
        }        
    }
    /**
	 * Implementation of FormAction interface.
     */
    public void executeAction(ItemGUIContainer target){
        JComponent compo;
        
        if (_key==null)
            return;
        if (target==null)
            return;
        compo = target.getComponent();
        if (compo==null)
            return;

        switch(target.getType()){
             case ItemGUIContainer.COMBO_TYPE:
                selectComboValue(target);
                break;
        }
    }
    /**
	 * Implementation of FormAction interface.
     */
    public String getStringRepr(){
        StringBuffer szBuf = new StringBuffer();
        szBuf.append(getName());
        szBuf.append("=");
        szBuf.append(getTargetItemName());
        szBuf.append(":select:");
        szBuf.append(getKey());
        return szBuf.toString();
    }
}
