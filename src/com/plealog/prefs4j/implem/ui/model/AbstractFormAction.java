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

/**
 * This class defines the basics of an action that can happen
 * in a form.
 * 
 * @author Patrick G. Durand
 */
public abstract class AbstractFormAction implements FormAction{
    private String _name;
    private String _targetItemName;
    
    /**
	 * Implementation of FormAction interface.
     */
	public String getName() {
		return _name;
	}
    /**
     * Sets the name of this action.
     */
	public void setName(String name) {
		_name = name;
	}
	/**
	 * Implementation of FormAction interface.
	 */
    public String getTargetItemName() {
        return _targetItemName;
    }
	/**
	 * Sets the name of the Item that emits this action.
	 */
    public void setTargetItemName(String name) {
        _targetItemName = name;
    }
}
