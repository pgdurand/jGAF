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
package com.plealog.prefs4j.api;

import java.util.Hashtable;

import com.plealog.prefs4j.implem.core.io.BasicPropertiesDataConnectorImplem;
import com.plealog.prefs4j.implem.core.io.BasicRawDataConnectorImplem;

/**
 * This class handles the various data connectors available in the Framework.
 */
public class DataConnectorFactory {
	
	private static Hashtable<String, DataConnector> _dConnectors;
	
	static {
		_dConnectors = new Hashtable<String, DataConnector>();
		_dConnectors.put(DataConnector.TYPE.props.toString(), new BasicPropertiesDataConnectorImplem());
		_dConnectors.put(DataConnector.TYPE.raw.toString(), new BasicRawDataConnectorImplem());
		_dConnectors.put(DataConnector.TYPE.none.toString(), new NoneDataConnectorImplem());
	}
	private DataConnectorFactory(){}
	/**
	 * Returns a data connector given its type.
	 * 
	 * @param type the data connector type. Either one of DataConnector.TYPE enum values or a user-defined one.
	 * 
	 * @return a data connector. Please note that data connectors are used as singleton.
	 */
	public static DataConnector getDataConnector(String type){
		return _dConnectors.get(type);
	}
	
	/**
	 * Register a new data connector.
	 * 
	 * @param type the data connector type. Either one of DataConnector.TYPE enum values or a user-defined one.
	 * @param dConn data connector. Please note that data connectors are used as singleton.
	 *
	 */
	public static void registerDataConnector(String type, DataConnector dConn){
		_dConnectors.put(type, dConn);
	}
	
	private static class NoneDataConnectorImplem implements DataConnector{
		public TYPE getType(){
			return DataConnector.TYPE.none;
		}

		@Override
		public DataConnector newInstance() {
			return this;
		}

		@Override
		public boolean canRead(String locator) {
			return true;
		}

		@Override
		public boolean canWrite(String locator) {
			return true;
		}
	}
}
