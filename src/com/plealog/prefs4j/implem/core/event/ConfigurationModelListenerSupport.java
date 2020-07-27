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
package com.plealog.prefs4j.implem.core.event;

import javax.swing.event.EventListenerList;

import com.plealog.prefs4j.api.PreferenceModel;

public abstract class ConfigurationModelListenerSupport implements PreferenceModel{
	private EventListenerList _listenerList;

	/**
	 * Default constructor.
	 */
	public ConfigurationModelListenerSupport(){
		_listenerList = new EventListenerList();
	}
    /**
     * Adds a ConfigurationModelListener on this viewer.
     */
	public void addPreferenceModelListener(PreferenceModelListener l) {
		_listenerList.add(PreferenceModelListener.class, l);
	}

    /**
     * Removes a ConfigurationModelListener from this viewer.
     */
	public void removePreferenceModelListener(PreferenceModelListener l) {
		_listenerList.remove(PreferenceModelListener.class, l);
	}
	/**
	 * Fire a selection event.
	 */
	public void fireConfigurationModelEvent(ConfigurationFeatureEvent event) {
	     Object[] listeners = _listenerList.getListenerList();
	     for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==PreferenceModelListener.class) {
	             ((PreferenceModelListener)listeners[i+1]).modelChanged(event);
	         }
	     }
	 }

}
