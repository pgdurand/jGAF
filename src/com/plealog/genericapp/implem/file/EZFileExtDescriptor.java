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
package com.plealog.genericapp.implem.file;

import com.plealog.genericapp.api.file.EZFileManager;

/**
 * This class is used to describe a file extension. It is mainly designed to be
 * used with the {@link EZFileExtSelector} that can be displayed within the File Chooser dialogue boxes
 * available from {@link EZFileManager}.
 */
public class EZFileExtDescriptor {
	private String _ext;
	private String _desc;
	private String _buf;
	public EZFileExtDescriptor(){}
	
	/**
	 * Constructor.
	 * 
	 * @param extension file extension
	 * @param description file description
	 * */
	public EZFileExtDescriptor(String extension, String description) {
		super();
		this._ext = extension;
		this._desc = description;
	}
	public String getExtension() {
		return _ext;
	}
	public void setExtension(String extension) {
		this._ext = extension;
		_buf = null;
	}
	public String getDescription() {
		return _desc;
	}
	public void setDescription(String description) {
		this._desc = description;
		_buf = null;
	}
	
	public String toString(){
		if (_buf==null)
			_buf = _desc;
		return _buf;
	}
}
