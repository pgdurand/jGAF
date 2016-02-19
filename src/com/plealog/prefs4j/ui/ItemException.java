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
package com.plealog.prefs4j.ui;

/**
 * This class defines an exception associated to Item.
 * 
 * @author Patrick G. Durand
 */
public class ItemException extends RuntimeException {

	private static final long serialVersionUID = 8114562610820998845L;

	public ItemException(String msg){
        super (msg);
    }
}
