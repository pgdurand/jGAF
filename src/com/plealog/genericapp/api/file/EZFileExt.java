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
package com.plealog.genericapp.api.file;

import java.io.File;

import com.plealog.genericapp.implem.file.EZFileExtDescriptor;

/**
 * This class is used to handle a file with a particular file extension descriptor. For internal use only.
 */
public class EZFileExt {
	private File                file;
	private EZFileExtDescriptor fileType;

	public EZFileExt(File file) {
		super();
		this.file = file;
	}
	public EZFileExt(File file, EZFileExtDescriptor fileType) {
		super();
		this.file = file;
		this.fileType = fileType;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public EZFileExtDescriptor getFileType() {
		return fileType;
	}
	public void setFileType(EZFileExtDescriptor fileType) {
		this.fileType = fileType;
	}
}
