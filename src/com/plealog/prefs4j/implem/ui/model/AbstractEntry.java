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
 * This class defines the basics of a form entry.
 * 
 * @author Patrick G. Durand
 */
public abstract class AbstractEntry {
    private String    _name;
    private String    _description;
    private String    _key;
    
    /**
     * Returns the description of this entry.
     */
    public String getDescription() {
        return _description;
    }
    /**
     * Sets the description of this entry.
     */
    public void setDescription(String description) {
        this._description = description;
    }
    /**
     * Returns the key of this entry.
     */
    public String getKey() {
        return _key;
    }
    /**
     * Sets the key of this entry. This key is used as this entry&apos;id.
     * When using multiple entries is a same form, be sure to use unique key
     * for the various entries.
     */
    public void setKey(String key) {
        this._key = key;
    }
    /**
     * Returns the key of this entry. This name is used for display purpose
     * in the GUI.
     */
    public String getName() {
        return _name;
    }
    /**
     * Sets the key of this entry.
     */
    public void setName(String name) {
        this._name = name;
    }
    
    /**
     * Returns a string representation of this entry.
     */
    public String toString(){
        return _name;   
    }
    /**
     * Returns a string representation of this entry.
     */
    public String getStringRepr(){
        StringBuffer szBuf = new StringBuffer();
        szBuf.append("Name: "+_name+", ");
        szBuf.append("Key: "+_key+", ");
        szBuf.append("Description: "+_description);
        return szBuf.toString();
    }
}
