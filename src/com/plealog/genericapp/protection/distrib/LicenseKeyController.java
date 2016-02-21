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
package com.plealog.genericapp.protection.distrib;

import java.security.MessageDigest;

//adapted from: http://stackoverflow.com/questions/9193376/how-to-generate-a-license-key-using-java
/**
 * Utility class used by the LicenseKeyProtector.
 */
public final class LicenseKeyController {
	private static final String charset = new HStringCoder(
			"UQDJERGV0RKh0SNxlTRBlUUNVVXZFWalTMzIDN2UzN5g").toString() 
			/* => ABCDEFGHJKLMNPQRSTUVWXYZ123456789 */;

	private static final String ALGO = new HStringCoder("0UBhTL1IgN$$").toString() 
			/* => SHA-256 */;
	private static final String ERR = new HStringCoder("2a5VGIldmbyVXYvRicmBWYslWZ$Q").toString() 
			/* => key generator failed */;
	private static String SEED = new HStringCoder("VRGp3btJGTil").toString() 
			/* => EZFormLib */;
	
	/**
	 * Encode a value into a license key.
	 * 
	 * @param valueToEncode the value to encode; usually it is the user name.
	 * @return the license key deduced from the value to encode.
	 */
	public final static String generate(String valueToEncode)
	{
		char[] charArray;
		byte[] passwd = new String(SEED+valueToEncode).getBytes();
		charArray = charset.toCharArray();

		byte[] data = new byte[15];
		byte[] tohash = new byte[5+ passwd.length];
		System.arraycopy(passwd, 0, tohash, 5, passwd.length);
		try {
			byte[] hash = getHash(tohash);
			System.arraycopy(hash, 0, data, 0, 15);
		} catch (Exception e) {
			return ERR;
		}
		int num=0;//17
		for (int i = 0; i < tohash.length; i++) num += tohash[i];
		String serial = Encode(charArray, data) + charArray[num & 31];
		String ret = "";
		for (int i = 0; i < 5; i++)
		{
			ret += serial.substring((i*5),(i*5)+5);
			if (i < 4) ret += "-";
		}
		return ret;
	}
	private final static byte[] getHash(byte[] toHash) throws Exception {
		MessageDigest digest = MessageDigest.getInstance(ALGO);
		digest.reset();
		return digest.digest(toHash);
	}
	private final static String Encode(char[] charArray, byte[] data){
		String ret="" ;
		for (int i = 0; i < data.length; i += 5){
			ret += charArray[data[i] >> 3 & 0x1f];
			ret += charArray[(data[i] << 2 | data[i + 1] >> 6) & 0x1f];
			ret += charArray[(data[i + 1] >> 1) & 0x1f];
			ret += charArray[(data[i + 1] << 4 | data[i + 2] >> 4) & 0x1f];
			ret += charArray[(data[i + 2] << 1 | data[i + 3] >> 7) & 0x1f];
			ret += charArray[data[i + 3] >> 2 & 0x1f];
			ret += charArray[(data[i + 3] << 3 | data[i + 4] >> 5) & 0x1f];
			ret += charArray[data[i + 4] & 0x1f];
		}
		return ret;
	}

	//Decode is not distributed with the library...
	
	/*private final static byte[] Decode(String serial){
		char[] x = strToChar(serial);
		byte[] table = new byte[256];
		for (int i = 0; i < charArray.length; i++)
		{
			table[charArray[i]] = (byte)i;
		}
		byte[] ret = new byte[x.length * 5 / 8];
		int pos = 0;
		for (int i = 0; i <= x.length - 8; )
		{
			byte b1 = table[x[i++]];
			byte b2 = table[x[i++]];
			byte b3 = table[x[i++]];
			byte b4 = table[x[i++]];
			byte b5 = table[x[i++]];
			byte b6 = table[x[i++]];
			byte b7 = table[x[i++]];
			byte b8 = table[x[i++]];

			ret[pos++] = (byte)(b1 << 3 | b2 >> 2);
			ret[pos++] = (byte)(b2 << 6 | b3 << 1 | b4 >> 4);
			ret[pos++] = (byte)(b4 << 4 | b5 >> 1);
			ret[pos++] = (byte)(b5 << 7 | b6 << 2 | b7 >> 3);
			ret[pos++] = (byte)(b7 << 5 | b8);
		}
		return ret;
	}*/

	/*private final static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}*/

}
