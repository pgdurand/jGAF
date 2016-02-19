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
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class manages a file filter for Open/Save dialogue boxes.
 */
public class EZFileFilter extends javax.swing.filechooser.FileFilter 
    implements java.io.FileFilter, java.io.FilenameFilter {

	private Hashtable<String, EZFileFilter> filters = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;
	private boolean acceptDir = true;

	/**
	 * Creates a file filter. If no filters are added, then all
	 * files are accepted.
	 */
	public EZFileFilter() {
		this.filters = new Hashtable<String, EZFileFilter>();
	}

	/**
	 * Creates a file filter that accepts files with the given extension.
	 * @param extension the file extension. Example: use jpg instead of .jpg .
	 */
	public EZFileFilter(String extension) {
		this(extension,null);
	}

	/**
	 * Creates a file filter that accepts the given file type.
	 * Example: new EZFileFilter("jpg", "JPEG Image Images");
	 *
	 * Note that the "." before the extension is not needed. If
	 * provided, it will be ignored.
	 */
	public EZFileFilter(String extension, String description) {
		this();
		if(extension!=null) addExtension(extension);
		if(description!=null) setDescription(description);
	}

	/**
	 * Creates a file filter from the given string array.
	 * Example: new EZFileFilter(String {"gif", "jpg"});
	 *
	 * Note that the "." before the extension is not needed and
	 * will be ignored.
	 */
	public EZFileFilter(String[] filters) {
		this(filters, null);
	}

	/**
	 * Creates a file filter from the given string array and description.
	 * Example: new EZFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
	 *
	 * Note that the "." before the extension is not needed and will be ignored.
	 */
	public EZFileFilter(String[] filters, String description) {
		this();
		for (int i = 0; i < filters.length; i++) {
			// add filters one by one
			addExtension(filters[i]);
		}
		if(description!=null) setDescription(description);
	}

	/**
	 * Returns true if this file should be shown in the directory panel,
	 * false if it should not be shown.
	 *
	 * Files that begin with "." are ignored.
	 */
	public boolean accept(File f) {
		if(f != null) {
			if(f.isDirectory() && acceptDir) {
				return true;
			}
			String extension = getExtension(f);

			if(extension != null && filters.get(getExtension(f)) != null) {
				return true;
			};
		}
		return false;
	}
	@Override
	public boolean accept(File dir, String name) {
		return accept(new File(EZFileUtils.terminatePath(dir.getAbsolutePath())+name));
	}

	/**
	 * Returns the extension portion of the file name.
	 */
	public static String getExtension(File f) {
		if(f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if(i>0 && i<filename.length()-1) {
				return filename.substring(i+1).toLowerCase();
			};
		}
		return null;
	}

	/**
	 * Gets the file name without its extension.
	 */
	public static String getWithoutExtension(File f) {
		if (f==null)
			return null;
		String name = null;
		String s = f.getAbsolutePath();
		int i = s.lastIndexOf('.');
		if (i >0 && i< s.length() -1) {
			name = s.substring(0,i);
		}
		else{
			name = s;
		}
		return name;
	}

	/**
	 * Adds a file extension to this filter.
	 *
	 * Note that the "." before the extension is not needed and will be ignored.
	 */
	public void addExtension(String extension) {
		if(filters == null) {
			filters = new Hashtable<String, EZFileFilter>();
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}


	/**
	 * Returns the description of this filter.
	 */
	public String getDescription() {
		if(fullDescription == null) {
			if(description == null || isExtensionListInDescription()) {
				fullDescription = description==null ? "(" : description + " (";
				// build the description from the extension list
				Enumeration<String> extensions = filters.keys();
				if(extensions != null) {
					fullDescription += "." + extensions.nextElement();

					while (extensions.hasMoreElements()) {
						fullDescription += ", ." + extensions.nextElement();
					}
				}
				fullDescription += ")";
			} else {
				fullDescription = description;
			}
		}
		return fullDescription;
	}

	/**
	 * Sets the description of this filter.
	 */
	public void setDescription(String description) {
		this.description = description;
		fullDescription = null;
	}

	/**
	 * Figures out whether or not the extension list (.jpg, .gif, etc) should
	 * show up in the description.
	 *
	 * Only relevant if a description was provided in the constructor
	 * or using setDescription method.
	 */
	public void setExtensionListInDescription(boolean b) {
		useExtensionsInDescription = b;
		fullDescription = null;
	}

	public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
	}

}
