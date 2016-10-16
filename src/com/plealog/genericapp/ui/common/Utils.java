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
package com.plealog.genericapp.ui.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.plealog.genericapp.api.log.EZLogger;

/**
 * Some utility methods.
 * 
 * @author Patrick G. Durand
 * @since 2003
 */
public class Utils {
  private static final DecimalFormat _bytesFormatter = new DecimalFormat("####.00");

  public static final long TERA = (long) 1024 * 1024 * 1024 * 1024;
  public static final long GIGA = (long) 1024 * 1024 * 1024;
  public static final long MEGA = (long) 1024 * 1024;
  public static final long KILO = (long) 1024;
  
  /** an unknown String value for not defined key (?) */
  public static final String UNKNOWNSTRING = "?";

  /** an unknown Integer value for not defined key (0) */
  public static final Integer UNKNOWNINTEGER = new Integer(0);

  public static String getString(ResourceBundle rb, String key) {
    String szVal = UNKNOWNSTRING;

    try {
      szVal = rb.getString(key);
    } catch (MissingResourceException mre) {
      EZLogger.warn("Unable to find resources: " + mre.getMessage());
    } catch (NullPointerException npe) {
      EZLogger.warn("Key is null: " + npe.getMessage());
    }
    return (szVal);
  }

  public static Integer getInteger(ResourceBundle rb, String key) {
    Integer val = UNKNOWNINTEGER;
    String szVal;

    try {
      szVal = rb.getString(key);
      val = Integer.valueOf(szVal);
    } catch (MissingResourceException mre) {
      EZLogger.warn("Unable to find resources: " + mre.getMessage());
    } catch (NullPointerException npe) {
      EZLogger.warn("Key is not defined: " + npe.getMessage());
    } catch (NumberFormatException nfe) {
      EZLogger.warn("Value for key '" + key + "' does not contain an integer: " + nfe.getMessage());
    }
    return (val);
  }

  /**
   * Convert a number of bytes into a string suffixed with giga or mega or kilo.
   */
  public static String getBytes(long bytes) {
    StringBuffer buf = new StringBuffer();
    setBytes(buf, bytes);
    return buf.toString();
  }

  /**
   * Convert a number of bytes into a string suffixed with giga or mega or kilo.
   * Resulting string is added to a string buffer
   */
  public static void setBytes(StringBuffer buf, long bytes) {
    if (bytes > TERA) {
      buf.append(_bytesFormatter.format((double) bytes / (double) TERA));
      buf.append(" Tb");
    } else if (bytes > GIGA) {
      buf.append(_bytesFormatter.format((double) bytes / (double) GIGA));
      buf.append(" Gb");
    } else if (bytes > MEGA) {
      buf.append(_bytesFormatter.format((double) bytes / (double) MEGA));
      buf.append(" Mb");
    } else if (bytes > KILO) {
      buf.append(_bytesFormatter.format((double) bytes / (double) KILO));
      buf.append(" Kb");
    } else {
      buf.append(bytes);
      buf.append(" bytes");
    }
  }

  /**
   * Append the file sourceFile to targetFile. String should be absolute paths
   * to the files. Use only with text files.
   * 
   * @return true if success. False otherwise.
   */
  public static boolean appendFiles(String sourceFile, String targetFile, boolean appendCR) {
    boolean bOk = false;
    BufferedInputStream fis = null;
    BufferedOutputStream out = null;
    int dSize = 8192, n;
    byte buffer[] = new byte[dSize];
    byte[] CR = "\n".getBytes();

    try {
      fis = new BufferedInputStream(new FileInputStream(sourceFile), dSize);
      out = new BufferedOutputStream(new FileOutputStream(targetFile, true), dSize);
      if (appendCR) {
        out.write(CR);
      }
      while ((n = fis.read(buffer)) != -1) {
        out.write(buffer, 0, n);
      }
      bOk = true;
      out.flush();
    } catch (Exception e) {
      EZLogger.warn("Unable to append " + sourceFile + " with " + targetFile + ": " + e);
    } finally {
      try{if (out!=null){out.close();}}catch(Exception ex){};
      try{if (fis!=null){fis.close();}}catch(Exception ex){};
    }
    return (bOk);
  }

}
