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
 * This class defines a form. A form contains a set of Items. In order
 * to make form of forms, a form is also defined as an Item.
 * 
 * @author Patrick G. Durand
 */
public class FormItem extends AbstractEntry{
    private ArrayList<String> _items = new ArrayList<String>();
    
    private boolean foldable_ = false;
    private boolean folded_ = true;
    
    /**
     * Adds an item to this form. Please note that only the key
     * of a given Item is added to this form. Items are supposed to be
     * stored elsewhere.
     */
    public void addItem(String itemKey){
        _items.add(itemKey);
    }
    /**
     * Returns an iterator over the Items contained in this form.
     */
    public Iterator<String> getItems(){
        return _items.iterator();   
    }
    /**
     * Returns true if this form contains some Items.
     */
    public boolean hasItems(){
        return (!_items.isEmpty());   
    }
    
    public boolean isFoldable() {
		return foldable_;
	}
	public void setFoldable(boolean foldable) {
		this.foldable_ = foldable;
	}
	public boolean isFolded() {
		return folded_;
	}
	public void setFolded(boolean folded) {
		this.folded_ = folded;
	}
	/**
     * Returns a string representation of this form.
     */
    public String getStringRepr(){
        StringBuffer szBuf = new StringBuffer();
        szBuf.append(super.getStringRepr());
        if (hasItems()){
            szBuf.append("\nItems: ");
            Iterator<String> iter = getItems();
            while(iter.hasNext()){
                szBuf.append(iter.next().toString());
                if (iter.hasNext())
                    szBuf.append(", ");
            }
        }
        return szBuf.toString();
    }

}
