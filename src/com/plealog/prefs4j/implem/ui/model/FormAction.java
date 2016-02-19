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
/*
 * Created on 5 mai 2005
 */
package com.plealog.prefs4j.implem.ui.model;


/**
 * This interface defines the basics to create an action to be used in association
 * with form&apos;items.
 * 
 * @author Patrick G. Durand
 */
public interface FormAction {
    /**
     * Execute an action.
     * 
     * @param target the GUI container performing the action.
     */
	public void executeAction(ItemGUIContainer target);
    /**
     * Returns a string representation of this FormAction.
     */
    public String getStringRepr();
    /**
     * Gets the name of this action.
     */
    public String getName();
	/**
	 * Returns the name of the Item that emits this action.
	 */
    public String getTargetItemName();
}
