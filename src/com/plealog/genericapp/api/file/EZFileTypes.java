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

import java.util.Hashtable;

/**
 * This class manages file types. The easiest way to use this class consists in using method
 * registerFileTypes(String, String) to add a file type, then use getFileFilter(String) to
 * retrieve a instance of EZFileFilter.
 */
public abstract class EZFileTypes {
    /**list of valid file types*/
    private static Hashtable<String, String> _acceptedFiles = new Hashtable<String, String>();
    
    /**
     * Registers a new file type.
     * 
     * @param extension file extension
     * @param description file extension description
     * */
    public static void registerFileType(String extension, String description){
        _acceptedFiles.put(extension, description);
    }

    /**
     * Gets a file filter for the given extension file type.
     * 
     * @param extension file extension to search for
     * 
     * @return a EZFileFilter object or null if extension has not been registered to this manager.
     */
    public static EZFileFilter getFileFilter (String extension){
        EZFileFilter glff;
        
        if (_acceptedFiles.containsKey(extension)==false)
            return null;
        glff = new EZFileFilter(extension,(String) _acceptedFiles.get(extension));
        
        return(glff);
    }
    
}
