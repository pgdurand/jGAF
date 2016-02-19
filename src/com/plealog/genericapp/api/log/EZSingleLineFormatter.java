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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This class defines a single line logger record formatter.
 */
public class EZSingleLineFormatter extends Formatter {

  Date dat = new Date();
  private final static String format = "[{0,date}, {0,time}]";
  private MessageFormat formatter;
  private Object args[] = new Object[1];
  
  private boolean showLogSource = true;
  
  public EZSingleLineFormatter(){
	  this(true);
  }
  /**
   * Constructor.
   * 
   * @param  showLogSource show log source if true. Log source means method name, 
   * class name and line number if available.*/
  public EZSingleLineFormatter(boolean showLogSource){
	  super();
	  this.showLogSource = showLogSource;
  }
  
  // Line separator string.  This is the value of the line.separator
  // property at the moment that the SimpleFormatter was created.
  //private String lineSeparator = (String) java.security.AccessController.doPrivileged(
  //        new sun.security.action.GetPropertyAction("line.separator"));
  private String lineSeparator = System.getProperty("line.separator");

  /**
   * Format the given LogRecord.
   * @param record the log record to be formatted.
   * @return a formatted log record
   */
  public synchronized String format(LogRecord record) {

    StringBuilder sb = new StringBuilder();

    // Minimize memory allocations here.
    dat.setTime(record.getMillis());    
    args[0] = dat;


    // Date and time 
    StringBuffer text = new StringBuffer();
    if (formatter == null) {
      formatter = new MessageFormat(format);
    }
    formatter.format(args, text, null);
    sb.append(text);
    sb.append(" ");


    // Level
    sb.append("<");
    sb.append(record.getLevel().getLocalizedName());
    sb.append(">: ");

    String message = formatMessage(record);

    if(showLogSource){
        //Thread
    	sb.append("Thread-");
    	sb.append(record.getThreadID());
    	sb.append(": ");
    	int idx = message.indexOf("&&");
    	if(idx!=-1){
        sb.append(message.substring(0, idx));
        sb.append(": "); 
        sb.append(message.substring(idx+2));
    	}
    	else{
        // Class name 
        if (record.getSourceClassName() != null) {
          sb.append(record.getSourceClassName());
        } else {
          sb.append(record.getLoggerName());
        }

        // Method name 
        if (record.getSourceMethodName() != null) {
          sb.append(".");
          sb.append(record.getSourceMethodName());
          sb.append("()");
        }
        sb.append(": "); 
    	}
    }
    else{
      int idx = message.indexOf("&&");
      if(idx!=-1){
        sb.append(message.substring(idx+2));
      }
      else{
        sb.append(message);
      }
    }

    sb.append(lineSeparator);
    if (record.getThrown() != null) {
      try {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        record.getThrown().printStackTrace(pw);
        pw.close();
        sb.append(sw.toString());
      } catch (Exception ex) {
      }
    }
    return sb.toString();
  }
}