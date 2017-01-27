/* Copyright (C) 2006-2017 Patrick G. Durand
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/agpl-3.0.txt
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 */
package com.plealog.genericapp.util;

public class EZUtilities {
  /**
   * Replace all occurrences of a string by another string within a string.
   * 
   * @param str
   *          source string
   * @param sFind
   *          string to find in str
   * @param sReplace
   *          replacement string
   * 
   * @return a new string or str if sFind was not found.
   */
  public static String replaceAll(String str, String sFind, String sReplace) {
    boolean bFound;
    int iPos = -1;

    String newStr = "";
    do {
      iPos = str.indexOf(sFind, ++iPos);
      if (iPos > -1) {
        newStr = newStr + str.substring(0, iPos) + sReplace + str.substring(iPos + sFind.length(), str.length());
        str = newStr;
        newStr = "";
        iPos += (sReplace.length() - 1);
        bFound = true;
      } else {
        bFound = false;
      }
    } while (bFound);
    return (str);
  }
}
