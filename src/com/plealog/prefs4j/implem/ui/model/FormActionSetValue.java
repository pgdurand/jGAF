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

import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * This class defines an action setting a value to the GUI component
 * of an Item.
 * 
 * @author Patrick G. Durand
 */
public class FormActionSetValue extends AbstractFormAction{
    private List<String> _value;
    
    public FormActionSetValue(){
    }

    public FormActionSetValue(String name, String targetItemName, List<String> value){
        setName(name);
        setTargetItemName(targetItemName);
        setValue(value);
    }
	public Object getValue() {
		return _value;
	}
	public void setValue(List<String> value) {
		_value = value;
	}
    private void setTxtFieldValue(ItemGUIContainer target){
        target.getTxtField().setText(_value.get(0).toString());
    }
    private void setComboValue(ItemGUIContainer target){
        Item               item;
        String             key;
        Iterator<String>   iter;
        JComboBox<ChoiceEntry> combo;
        
        item = target.getItem();
        combo = target.getCombo();
        combo.removeAllItems();
        iter = _value.iterator();
        while(iter.hasNext()){
            key = iter.next().toString();
            combo.addItem(item.getChoice().getEntry(key));
        }
    }
    private void setSpinnerValue(ItemGUIContainer target){
        
    }
    /**
	 * Implementation of FormAction interface.
     */
    public void executeAction(ItemGUIContainer target){
        JComponent compo;
        
        if (_value==null)
            return;
        if (target==null)
            return;
        compo = target.getComponent();
        if (compo==null)
            return;

        switch(target.getType()){
             case ItemGUIContainer.TXT_FIELD_TYPE:
                setTxtFieldValue(target);
                break;
             case ItemGUIContainer.COMBO_TYPE:
                setComboValue(target);
                break;
             case ItemGUIContainer.SPINNER_TYPE:
                setSpinnerValue(target);
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
        szBuf.append(":value:");
        Iterator<String> iter = _value.iterator();
        while(iter.hasNext()){
            szBuf.append(iter.next());
            if (iter.hasNext())
                szBuf.append(",");
        }
        return szBuf.toString();
    }
}
