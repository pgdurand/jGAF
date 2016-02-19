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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.plealog.prefs4j.api.DataConnector;
import com.plealog.prefs4j.api.RawDataConnector;

public class BasicRawDataConnectorImplem implements RawDataConnector {
	
	public BasicRawDataConnectorImplem(){
	}
	@Override
	public void save(String locator, String data) throws IOException {
		PrintWriter writer;
		FileWriter  fw;
		
		fw = new FileWriter(locator);
		writer = new PrintWriter(new BufferedWriter(fw));
		writer.println(data);
		writer.flush();
		fw.close();
	}

	@Override
	public String load(String locator) throws IOException {
        StringBuffer   szBuf;
        BufferedReader in=null;
        String         line;
        
        in = new BufferedReader(new FileReader(locator));
        szBuf = new StringBuffer();
        while ((line=in.readLine()) != null) {
            szBuf.append(line);
            szBuf.append("\n");
        }
        in.close();
        return  szBuf.toString();
	}

	@Override
	public TYPE getType(){
		return DataConnector.TYPE.raw;
	}	@Override
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
