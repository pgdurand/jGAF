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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class defines the basic element contained in a Choice.
 * 
 * @author Patrick G. Durand
 */
public class ChoiceEntry extends AbstractEntry{
    private ArrayList<FormAction> _actions = new ArrayList<FormAction>();
    
    /**
     * Adds an action associated to this ChoiceEntry.
     */
    public void addAction(FormAction action){
        _actions.add(action);
    }
    /**
     * Returns an iterator over the actions that are assiociated to this
     * ChoiceEntry.
     */
    public Iterator<FormAction> getActions(){
        return _actions.iterator();   
    }
    /**
     * Returns true is this ChoiceEntry has actions associated to it.
     */
    public boolean hasActions(){
        return (!_actions.isEmpty());   
    }
    /**
     * Returns a string representation of this entry.
     */
    public String getStringRepr(){
        StringBuffer szBuf = new StringBuffer();
        szBuf.append(super.getStringRepr());
        if (hasActions()){
            szBuf.append("\nActions:\n");
            Iterator<FormAction> iter = getActions();
            while(iter.hasNext()){
                szBuf.append("  "+(iter.next()).getStringRepr());
                if (iter.hasNext())
                    szBuf.append("\n");
            }
        }
        return szBuf.toString();
    }
}
