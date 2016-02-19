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
package com.plealog.genericapp.ui.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/** 
 * Implementation of an AbstractAction for EZMenu system. In addition to the
 * standard AbstractAction capabilities, this action may have a user 
 * object and implements a default behavior for method actionPerformed():
 * it fires a property change. The name of the property if the one returned
 * by this.getKeyName().
 */
public class EZBasicAction extends AbstractAction {
//created September 10, 2013
	private static final long serialVersionUID = 2901251587680304519L;
	/**property name used by this.fireUpdate().*/
	public static final String GLOBAL_UPDT_PROP = "globalUpdate";
	/**a user object*/
	private Object _usrObject;
	/**the key name of this action. This is that name that is used by
	 * DDActionManager to store this Action in the ActionMap.*/
    private String   _name;
    /**figures out if this Action can be displayed in Menus for
	 * the current OS.*/
    private boolean  _validForThisOsMenu = true;
    
	/**
	 * Default constructor.
	 */
	public EZBasicAction() {
	}

	/**
	 * Construction with a user object.
	 */
	public EZBasicAction(Object usrObject) {
		_usrObject = usrObject;
	}

	/**
	 * Basic constructor with a key name.
	 */
	public EZBasicAction(String name) {
		super(name);
		_name=name;
	}

	/**
	 * Basic constructor with a user object and a key name.
	 */
	public EZBasicAction(Object usrObject, String name) {
		super(name);
		_usrObject = usrObject;
		_name=name;
	}


	/**
	 * Basic constructor with a user object, a key name and an icon.
	 */
	public EZBasicAction(Object usrObject, String name, Icon icon) {
		super(name, icon);
		_usrObject = usrObject;
	}

	/**
	 * Basic constructor a key name and an icon.
	 */
	public EZBasicAction(String name, Icon icon) {
		super(name, icon);
		_name=name;
	}

	protected String getClassNameWithoutPackage(Class<?> cl){
		String className = cl.getName();
		int    pos = className.lastIndexOf('.');
		if (pos == -1)
			pos = 0;
		else
            pos += 1;
        return className.substring(pos);
	}

	/** 
	 * Returns the key name of the action. This is that name that is used by
	 * EZActionManager to store this Action in the ActionMap.
	 */
	public String getKeyName() {
		if (_name==null){
            _name = getClassNameWithoutPackage(getClass());
        }
        return (_name);
	}
	
	/**
	 * Sets the key name of this action.
	 */
	public void setKeyName(String name) {
		if (name!=null)
			_name = name;
	}
	
	/**
	 * Enable or disable this action. Default implementation disables 
	 * this action if user object is null.
	 */
	public void update() {
        if (_usrObject == null)
			setEnabled(false);
		else
			setEnabled(true);
	}

	/**
	 * Sets the user object.
	 */
	public void setUserObject(Object usrObject) {
		_usrObject = usrObject;
	}

	/**
	 * Returns the user object associated to this action.
	 */
	public Object getUserObject() {
		return (_usrObject);
	}
	
	/**
	 * Sets whether or not this Action can be displayed in Menus for
	 * the current OS.
	 */
	public void validForThisOsMenu(boolean b){
		_validForThisOsMenu = b;
	}
	
	/**
	 * Figures out if this Action can be displayed in Menus for
	 * the current OS.
	 */
	public boolean isValidForThisOsMenu(){
		return _validForThisOsMenu;
	}
	
	/**
	 * Sends an update notification message to this action property change
	 * listeners. In the fired event, property name is set to
	 * EZBasicAction.GLOBAL_UPDT_PROP, and both new and old values are set
	 * to null.
	 */
	protected void fireUpdate(){
		firePropertyChange(GLOBAL_UPDT_PROP, null, null);
	}
	
	/**
	 * Implementation of AbstractAction method. This default implementation
	 * fires a property change with property name sets to this.getKeyName(), 
	 * old value sets to null and new value sets to ActionEvent 'e'.
	 */
	public void actionPerformed(ActionEvent e){
		firePropertyChange(_name, null, e);
	}
}
