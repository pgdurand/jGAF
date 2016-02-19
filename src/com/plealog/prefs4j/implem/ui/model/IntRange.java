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

/**
 * This class defines a Range made of integers.
 * 
 * @author Patrick G. Durand
 */
public class IntRange implements Range{
    private int    _rangeFrom;
    private int    _rangeTo;
    private int    _rangeDef;
    

	/**
	 * Returns the default value of this range.
	 */
	public int getRangeDef() {
		return _rangeDef;
	}
	/**
	 * Sets the default value of this range.
	 */
	public void setRangeDef(int def) {
		_rangeDef = def;
	}
	/**
	 * Returns the lower limit value of this range.
	 */
	public int getRangeFrom() {
		return _rangeFrom;
	}
	/**
	 * Sets the lower limit value of this range.
	 */
	public void setRangeFrom(int from) {
		_rangeFrom = from;
	}
	/**
	 * Returns the upper limit value of this range.
	 */
	public int getRangeTo() {
		return _rangeTo;
	}
	/**
	 * Sets the upper limit value of this range.
	 */
	public void setRangeTo(int to) {
		_rangeTo = to;
	}
    /**
     * Returns a string representation of this entry.
     */
    public String toString(){
    	return (_rangeFrom+" - "+_rangeTo+" ("+_rangeDef+")");   
    }
    public boolean isInRange(Number n){
    	int i = n.intValue();
    	return (i>=_rangeFrom && i<= _rangeTo);
    }
    public Number getMin(){
    	return new Integer(_rangeFrom);
    }
	public Number getMax(){
    	return new Integer(_rangeTo);
    }
}
