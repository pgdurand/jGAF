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

import javax.swing.JComponent;

/**
 * This class defines an action enabling or disabling the GUI component
 * of an Item.
 * 
 * @author Patrick G. Durand
 */
public class FormActionSetEnable extends AbstractFormAction{
    private boolean _value;
    
    public FormActionSetEnable(){
    }

    public FormActionSetEnable(String name, String targetItemName, boolean value){
        setName(name);
        setTargetItemName(targetItemName);
        setValue(value);
    }
	public boolean getValue() {
		return _value;
	}
	public void setValue(boolean value) {
		_value = value;
	}
    /**
	 * Implementation of FormAction interface.
     */
    public void executeAction(ItemGUIContainer target){
        JComponent compo;
        if (target==null)
            return;
        
        compo = target.getComponent();
        if (compo==null)
            return;
        compo.setEnabled(_value);
    }
    /**
	 * Implementation of FormAction interface.
     */
    public String getStringRepr(){
        StringBuffer szBuf = new StringBuffer();
        szBuf.append(getName());
        szBuf.append(":enable:");
        szBuf.append(getValue());
        return szBuf.toString();
    }
}
