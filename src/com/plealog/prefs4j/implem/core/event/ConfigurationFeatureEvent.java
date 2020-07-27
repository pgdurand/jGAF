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

import java.util.EventObject;

import com.plealog.prefs4j.api.PreferenceSection;

public class ConfigurationFeatureEvent extends EventObject {
	private static final long serialVersionUID = -6090678263826488520L;
	private PreferenceSection _conf;
	
	public ConfigurationFeatureEvent(Object src){
		super(src);
	}
	
	public ConfigurationFeatureEvent(Object src, PreferenceSection co) {
		super(src);
		setConfigurationFeature(co);
	}
	
	public void setConfigurationFeature(PreferenceSection co){
		_conf = co;
	}
	public PreferenceSection getConfigurationFeature(){
		return _conf;
	}
}
