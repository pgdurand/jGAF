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
package com.plealog.prefs4j.implem.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.plealog.genericapp.ui.starter.EZEnvironmentImplem;
import com.plealog.prefs4j.api.DataConnector;
import com.plealog.prefs4j.api.PropertiesDataConnector;

public class BasicPropertiesDataConnectorImplem implements PropertiesDataConnector {
	public BasicPropertiesDataConnectorImplem(){
	}
	
	@Override
	public void save(String locator, Properties props) throws IOException {
		FileOutputStream fos;
		
		fos = new FileOutputStream(new File(locator));
		EZEnvironmentImplem.saveProperties(props, fos, null);
		fos.flush();
		fos.close();
	}

	@Override
	public Properties load(String locator) throws IOException {
		FileInputStream fis;
		fis = new FileInputStream(new File(locator));
		Properties props = new Properties();
		props = EZEnvironmentImplem.loadProperties(fis);
		fis.close();
		return props;
	}
	public TYPE getType(){
		return DataConnector.TYPE.props;
	}

	@Override
	public DataConnector newInstance() {
		return this;
	}
	@Override
	public boolean canRead(String locator) {
		if (locator==null)
			return false;
		return 
			new File(locator).canRead();
	}
	@Override
	public boolean canWrite(String locator) {
		if (locator==null)
			return false;
		return 
			new File(locator).canWrite();
	}

}
