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
package com.plealog.genericapp.protection.local;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.plealog.genericapp.protection.distrib.HStringCoder;
import com.plealog.genericapp.protection.distrib.HStringUtils;
import com.plealog.genericapp.protection.distrib.LibraryProtectorBase;

/**
 * This class is intended to control the validity of the library. It is used
 * to copy a template class into a concrete one where two strings are setup: the Date (YYYYmmdd, hexa encoded) 
 * specifying when the library expired, and a hexa representation of the date hashcode. The second one is
 * used to check any attempt to alter the date. 
 * 
 * DO NOT distribute this class with the library.
 */
public class LibraryDateProtectorGenerator {
	/**
	 * @param fTemplate it is the absolute path to LibraryProtectorController.template contained in this package
	 * @param fClass it is the absolute path LibraryProtectorController.class contained in protection.distrib package
	 * @param days number of validity days of the library. Start counting from today's date.
	 * */
	private static void createStamp(String fTemplate, String fClass, int days) throws IOException{
		String      uid;
		long        value1;
		int         value2;
		
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        System.out.println("Now is: "+cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, days);
        System.out.println("Expired on: "+cal.getTime());
        
        value1 = cal.getTimeInMillis();
		uid = LibraryProtectorBase.DATE_FORM.format(new Date(value1));
		value2 = uid.hashCode();

		copyFile(
				new File(fTemplate), 
				new File(fClass), 
				HStringCoder.obfuscateNoComment(HStringUtils.encryptHexString(uid)), 
				HStringCoder.obfuscateNoComment(HStringUtils.encryptHexString(String.valueOf(value2))));
	}
	public static void copyFile(File source, File dest, String lid, String hid) throws IOException{
	    FileWriter     fw = null;
	    FileReader     fr = null;
	    BufferedReader br = null;
	    BufferedWriter bw = null;
	    String         line;
	    int            fileLength;
	    
	    try {
			fr = new FileReader(source);
			fw = new FileWriter(dest);
			br = new BufferedReader(fr);
			bw = new BufferedWriter(fw);

			/* Determine the size of the buffer to allocate */
			fileLength = (int) source.length();
			//test for empty files
			if (fileLength!=0){
				fileLength = Math.min(2048, fileLength);
				
			    while ((line=br.readLine())!=null){
			    	line = replaceAll(line, "@_LID_@", lid);
			    	line = replaceAll(line, "@_HID_@", hid);
			        bw.write(line);
			        bw.write("\n");
			    }
			    bw.flush();
			}
		} catch (IOException e) {
			throw e;
		}
		finally{
			try{if (bw!=null)bw.close();}catch(Exception ex){}
			try{if (br!=null)br.close();}catch(Exception ex){}
		}
	}

    public static String replaceAll(String str, String sFind, String sReplace) {
        boolean bFound;
        int iPos = -1;
        
        String newStr = "";
        do {
            iPos = str.indexOf(sFind, ++iPos);
            if (iPos > -1) {
                newStr = newStr + str.substring(0, iPos) + sReplace + str.substring(iPos+sFind.length(),str.length());
                str = newStr;
                newStr = "";
                iPos += (sReplace.length()-1);
                bFound = true;
            } else {
                bFound = false;
            }
        } while ( bFound );
        return(str);
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
    //[0]: the template class absolute path
    //[1]: the target class absolute path
    //[2]: number of days during which library remains valid
	  System.out.println("Generate LibraryTimeStamp: convert '"+args[0]+"' to '"+args[1]+"'.");
		System.out.println("Validity: "+args[2]+" days.");
		try {
			createStamp(args[0], args[1], Integer.valueOf(args[2]));
		} catch (Exception e) {
			System.err.println("Error: "+e);
			System.exit(0);
		}
	}

}
