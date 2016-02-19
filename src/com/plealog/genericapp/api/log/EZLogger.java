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
package com.plealog.genericapp.api.log;

import java.util.logging.Logger;

import com.plealog.genericapp.api.EZApplicationBranding;

/**
 * Entry point of the Generic Application logging system. This class is a wrapper around the
 * standard Java Util Logging API. If you want to use this class, please configure the logging system
 * using EZLoggerManager class. The use of this EZLogger class avoid the use of class-based loggers. 
 * Indeed, EZLogger analyzes the stack trace to locate the method that does the call to EZLogger, then
 * reports that information within logging message. 
 */
public class EZLogger {
	private static Logger LOGGER = Logger.getLogger(EZApplicationBranding.getAppName());
	
	public static void info(String msg){
	  String caller = getMethodCaller();
	  if(caller!=null)
	    LOGGER.info(formatMessage(caller, msg));
	  else
	    LOGGER.info(msg);
	}
	public static void warn(String msg){
    String caller = getMethodCaller();
    if(caller!=null)
      LOGGER.warning(formatMessage(caller, msg));
    else
      LOGGER.warning(msg);
	}
	public static void error(String msg){
    String caller = getMethodCaller();
    if(caller!=null)
      LOGGER.severe(formatMessage(caller, msg));
    else
      LOGGER.severe(msg);
	}
	public static void debug(String msg){
    String caller = getMethodCaller();
    if(caller!=null)
      LOGGER.config(formatMessage(caller, msg));
    else
      LOGGER.config(msg);
	}
	private static String formatMessage(String caller, String msg){
	  StringBuffer buf = new StringBuffer(caller);
	  
	  if (EZLoggerManager.isInitialized())
	    buf.append("&&");
	  else
      buf.append(": ");
	  buf.append(msg);
	  return buf.toString();
	}
	private static String getMethodCaller(){
	  StackTraceElement[] elements = new Throwable().getStackTrace();
	  if (elements.length<3)
	    return null;
	  else
	    return elements[2].toString();
	}
}
