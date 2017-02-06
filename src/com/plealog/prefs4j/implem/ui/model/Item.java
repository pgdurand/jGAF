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
package com.plealog.prefs4j.implem.ui.model;

import java.text.MessageFormat;
import java.util.Iterator;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.prefs4j.ui.ItemException;

/**
 * This class defines the basics of an item to be used in a Form.
 * 
 * @author Patrick G. Durand
 */
public class Item extends AbstractEntry{
    public static final int CTYPE_CHOICE = 1;
    public static final int CTYPE_VALUE  = 2;
    
    public static final int DTYPE_INT      = 1;
    public static final int DTYPE_REAL     = 2;
    public static final int DTYPE_STRING   = 3;
    public static final int DTYPE_BOOLEAN  = 4;
    public static final int DTYPE_FOLDER   = 5 ;
    public static final int DTYPE_FILE     = 6 ;
    public static final int DTYPE_FILES    = 7 ;

    public static final String S_CTYPE_CHOICE = "choice";
    public static final String S_CTYPE_VALUE  = "atom";

    public static final String S_DTYPE_INT      = "integer";
    public static final String S_DTYPE_REAL     = "real";
    public static final String S_DTYPE_STRING   = "string";
    public static final String S_DTYPE_BOOLEAN  = "boolean";
    public static final String S_DTYPE_FOLDER   = "folder";
    public static final String S_DTYPE_FILE     = "file";
    public static final String S_DTYPE_FILES    = "files";

    private static final MessageFormat RANGE_ERR_FORMATTER = 
    	new MessageFormat(EZEnvironment.getMessage("__EZItem.control.error.notInRange"));

    private Object  _default;
    private int     _ctype;
    private int     _dtype;
    private boolean _optional;
    private boolean _hasRange;
    private Range   _range;

    private boolean _hasChoice;
    private Choice  _choice;
    
    /**
     * Utility method. Returns the CType integer representation given its string
     * representation.
     * 
     * @param sCType the string representation of the container type. Should
     * be one of choice or atom.
     * 
     * @return one of CTYPE_CHOICE or CTYPE_VALUE
     * @throws ItemException if sCType is invalid.
     */
    public static int getCType(String sCType){
        if (sCType.equals(S_CTYPE_CHOICE))
            return CTYPE_CHOICE;
        else if (sCType.equals(S_CTYPE_VALUE))
            return CTYPE_VALUE;
        else throw new ItemException("Unknown CType: "+sCType);
    }

    /**
     * Utility method. Returns the string representation of CType given its integer
     * representation.
     * 
     * @param cType the integer representation of the container type. Should
     * be one of CTYPE_CHOICE (1) or CTYPE_VALUE (2).
     * 
     * @return one of CTYPE_CHOICE or CTYPE_VALUE
     * @throws ItemException if cType is invalid.
     */
    public static String getSCType(int cType){
        if (cType==CTYPE_CHOICE)
            return S_CTYPE_CHOICE;
        else if (cType==CTYPE_VALUE)
            return S_CTYPE_VALUE;
        else throw new ItemException("Unknown CType: "+cType);
    }

    /**
     * Utility method. Returns the integer representation of DType given its string
     * representation.
     * 
     * @param sDType the string representation of the data type. Should
     * be one of integer, real, string or bool.
     * 
     * @return one of DTYPE_INT, DTYPE_REAL, DTYPE_STRING, etc.
     * @throws ItemException if sDType is invalid.
     */
    public static int getDType(String sDType){
        if (sDType.equals(S_DTYPE_INT))
            return DTYPE_INT;
        else if (sDType.equals(S_DTYPE_REAL))
            return DTYPE_REAL;
        else if (sDType.equals(S_DTYPE_STRING))
            return DTYPE_STRING;
        else if (sDType.equals(S_DTYPE_BOOLEAN))
            return DTYPE_BOOLEAN;
        else if (sDType.equals(S_DTYPE_FOLDER))
            return DTYPE_FOLDER;
        else if (sDType.equals(S_DTYPE_FILE))
          return DTYPE_FILE;
        else if (sDType.equals(S_DTYPE_FILES))
          return DTYPE_FILES;
        else throw new ItemException("Unknown DType: "+sDType);
    }
    
    /**
     * Utility method. Returns the string representation of DType given its integer
     * representation.
     * 
     * @param dType the integer representation of the data type. Should
     * be one of DTYPE_INT (1), DTYPE_REAL (2), DTYPE_STRING (3) or DTYPE_BOOLEAN (4).
     * 
     * @return one of S_DTYPE_INT, S_DTYPE_REAL, S_DTYPE_STRING) or S_DTYPE_BOOLEAN
     * @throws ItemException if dType is invalid.
     */
    public static String getSDType(int dType){
        if (dType==DTYPE_INT)
            return S_DTYPE_INT;
        else if (dType==DTYPE_REAL)
            return S_DTYPE_REAL;
        else if (dType==DTYPE_STRING)
            return S_DTYPE_STRING;
        else if (dType==DTYPE_BOOLEAN)
            return S_DTYPE_BOOLEAN;
        else if (dType==DTYPE_FOLDER)
            return S_DTYPE_FOLDER;
        else if (dType==DTYPE_FILE)
          return S_DTYPE_FILE;
        else if (dType==DTYPE_FILES)
          return S_DTYPE_FILES;
        else throw new ItemException("Unknown DType: "+dType);
    }

    /**
     * Returns the container type of this Item.
     * 
     * @return one of CTYPE_CHOICE or CTYPE_VALUE
     */
    public int getCtype() {
		return _ctype;
	}
    
    /**
     * Sets the container type of this Item.
     * 
     * @param ctype one of CTYPE_CHOICE or CTYPE_VALUE
     */
	public void setCtype(int ctype) {
		this._ctype = ctype;
	}
    /**
     * Returns the data type of this Item.
     * 
     * @return one of DTYPE_INT, DTYPE_REAL, DTYPE_STRING or DTYPE_BOOLEAN
     */
	public int getDtype() {
		return _dtype;
	}
    /**
     * Sets the data type of this Item.
     * 
     * @param dtype one of DTYPE_INT, DTYPE_REAL, DTYPE_STRING or DTYPE_BOOLEAN
     */
	public void setDtype(int dtype) {
		this._dtype = dtype;
	}
	/**
	 * Returns the default value associated to thie Item.
	 */
    public Object getDefault() {
        return _default;
    }
	/**
	 * Sets the default value associated to thie Item. Returns null if not default
	 * value is associated to this Item.
	 */
    public void setDefault(Object def) {
        this._default = def;
    }
    /**
     * Sets the Range associated to this Item. 
     */
    public void setRange(Range range){
        _range = range;
        _hasRange = true;
    }
    /**
     * Returns the Range associated to this Item. Returns null if not Range
     * is associated to this Item.
     */
    public Range getRange(){
        return _range;   
    }

    /**
     * Sets the Choice associated to this Item. A Choice is used to set values
     * for container type CTYPE_CHOICE.
     */
    public void setChoice(Choice choice){
        _choice = choice;
        _hasChoice = true;
    }
    /**
     * Returns the Choice associated to this Item. Returns null if not Choice
     * is associated to this Item.
     */
    public Choice getChoice(){
        return _choice;   
    }

    /**
     * Sets the optional status of this Item. Pass true if this Item is optional.
     */
    public void setOptional(boolean optional){
        _optional = optional;
    }

    /**
     * Returns the optional status of this Item. Return true if this Item is optional.
     */
    public boolean getOptional(){
        return _optional;   
    }
    private static final Integer I_ZERO = new Integer(0);
    private static final Double D_ZERO = new Double(0);
    /**
     * Checks a value to figure out if it conforms to the definition of this Item.
     * 
     * @param obj the value to check
     * 
     * @throws ItemException if obj content is invalid regarding the definition
     * of this item (container and data types)
     * 
     */
    public void controlValue(String obj) throws ItemException{
        Double  d = null;
        Integer i = null;
        
    	if (obj==null)
            throw new ItemException(getName()+": "+
                    EZEnvironment.getMessage("__EZItem.control.error.null"));
        //value is empty and optional : do not check anything
    	if (obj.length()==0 && getOptional()){
        	return;
        }
    	switch(_dtype){
            case DTYPE_INT:
            	if (obj.length()!=0){//this test to allow empty value (sort of reset)
	                try{
	                   i = Integer.valueOf(obj);
	                }
	                catch(NumberFormatException nfe){
	                    throw new ItemException(getName()+": "+ obj + ": "+
	                            EZEnvironment.getMessage("__EZItem.control.error.notInt"));
	                }
            	}
            	else{
            		i = I_ZERO;
            	}
                if (_hasRange && !_range.isInRange(i)){
                	throw new ItemException(getName()+": "+i+": "+
                			RANGE_ERR_FORMATTER.format(new Object[]{_range.getMin(),_range.getMax()}));
                }
                break;
            case DTYPE_REAL: 
            	if (obj.length()!=0){//this test to allow empty value (sort of reset)
	                try{
	                    d = Double.valueOf(obj);
	                 }
	                 catch(NumberFormatException nfe){
	                     throw new ItemException(getName()+": "+ obj + ": "+
	                            EZEnvironment.getMessage("__EZItem.control.error.notReal"));
	                 }
            	}
            	else{
            		d = D_ZERO;
            	}
                 if (_hasRange && !_range.isInRange(d)){
                 	throw new ItemException(getName()+": "+d+": "+
                 			RANGE_ERR_FORMATTER.format(new Object[]{_range.getMin(),_range.getMax()}));
                 }
                break;
            case DTYPE_BOOLEAN:
                if (obj.toUpperCase().equals("TRUE")==false){
                    if (obj.toUpperCase().equals("FALSE")==false){
                        throw new ItemException(getName()+": "+ obj + ": "+
                                EZEnvironment.getMessage("__EZItem.control.error.notBool"));
                    }
                }
                break;
        }
        if (_hasChoice){
        	Iterator<String> iter = _choice.getEntriesName().iterator();
        	String   key, name, val;
        	val = obj.toString();
        	while(iter.hasNext()){
        		key = iter.next().toString();
        		name = _choice.getEntry(key).getKey();
        		if (val.equals(name)){
        			return;
        		}
        	}
        	throw new ItemException(getName()+": "+ val+": "+
                    EZEnvironment.getMessage("__EZItem.control.error.notInChoice"));
        }
    }
    /**
     * Returns a string representation of this Item.
     */
    public String toString(){
        StringBuffer szBuf = new StringBuffer();
        szBuf.append(super.getStringRepr()+"\n");
        szBuf.append("CType: "+getSCType(_ctype)+"\n");
        szBuf.append("DType: "+getSDType(_dtype)+"\n");
        if (_hasRange){
            szBuf.append("Range: "+_range+"\n");
        }
        if (_hasChoice){
            szBuf.append("Choice: \n"+_choice+"\n");
        }
        return szBuf.toString();
    }
}