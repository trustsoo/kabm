/*
 * Created on 2005. 8. 29.
 *
 * Sorry !! Thread must be a lot of bug that I did not catch.
 * Somedat I will try to resolve this bug, but not now....
 * 2005.10.20
 */
package jdf.framework.core.util;

import java.io.IOException;

/**
 * @author Cho Seungtae
 *
 * Ascii Data Convert to Java Data type
 */
public class CStyleConverter {

	private static final int ASCII_NUMERIC_START = 0x30;
	private static final int ASCII_NUMERIC_END   = 0x39;

	private static final byte PLUS_SIGN = (byte)'+';
	private static final byte MINUS_SIGN = (byte)'-';
	
	private CStyleConverter() {}
	
	public static short bytesToShort(byte[] src) {
		return (short)bytesToInt(src);
	}
	
	public static short bytesToShort(byte msb, byte lsb) {
		return (short)bytesToInt(msb, lsb);
	}
	
	public static long bytesToLong (byte[] src) {
		return (long)bytesToInt(src);
	}
	
	public static int bytesToInt(byte msb, byte lsb) {
		byte[] src = new byte[2];
		
		src[0] = msb;
		src[1] = lsb;
		
		return bytesToInt(src);
	}
	
	public static int bytesToInt(byte[] src) {
		int dst = 0;
		boolean start = false;
		int sign = 0;
		
		for(int i = 0; i < src.length; i++) {

			if (start == false) {
				
				int check = checkPositive(src[i]); 
				if ( check != 0) {
					sign = check;
					continue;
				}
			}

			if (checkSkipData(src[i])) {
				if (start == true) break;
				else continue;
			}
			
			if (src[i] < ASCII_NUMERIC_START || src[i] > ASCII_NUMERIC_END) break;
			
			start = true;
			
			dst = (dst * 10) + ((int)src[i] - ASCII_NUMERIC_START);
		}
		
		if (sign < 0) dst *= sign;
		
		return dst;
	}
	
	public static double bytesToDouble(byte[] src) {
		double d = 0.0;
		int sign = 0;

		boolean start = false;
		boolean fractionStart = false;
		
		int integerValue = 0;
		int fractionValue = 0;
		int power = 1;
		
		for(int i = 0; i < src.length; i++) {

			if (start == false) {
				
				int check = checkPositive(src[i]); 
				if ( check != 0) {
					sign = check;
					continue;
				}
			}

			if (checkSkipData(src[i])) {
				if (start == true) break;
				else continue;
			}
			else if (src[i] == '.') {
				fractionStart = true;
				continue;
			}
			
			if (src[i] < ASCII_NUMERIC_START || src[i] > ASCII_NUMERIC_END) break;
			
			start = true;
			
			if (fractionStart == false) {
				integerValue = (integerValue * 10) + ((int)src[i] - ASCII_NUMERIC_START);
			}
			else {
				fractionValue = (fractionValue * 10) + ((int)src[i] - ASCII_NUMERIC_START);
				power *= 10;
				
			}
		}

		d = (double)integerValue + ((double)fractionValue / power);

		if (sign < 0) d *= sign;
		
		return d;
	}

	public static double bytesToDouble(byte[] src, int integer, int fraction) {

		if (src.length != (integer + fraction)) {
			return 0.0;
		}

		double d = 0.0;

		int integerValue = 0;
		int fractionValue = 0;
		int power = getPowerValue(10, fraction);

		byte[] bInteger = new byte[integer];
		byte[] bFraction = new byte[fraction + 1];
		
		bFraction[0] = 0x31;
		
		System.arraycopy(src, 0, bInteger, 0, integer);
		System.arraycopy(src, integer, bFraction, 1, fraction);
		
		integerValue = bytesToInt(bInteger);
		fractionValue = bytesToInt(bFraction);
		
		d = (double)integerValue + ((double)fractionValue / power); 
		
		return d;
	}
	
	public static String bytesToString(byte[] src) {
		int index = 0;
		for(index = 0; index < src.length; index++) {
			if (src[index] != 0x00) {
				break;
			}
		}
		
		String dst = new String(src, index, src.length - index);
		
		return dst.trim();
	}

	public static short get2BytesToShort(byte[] b) {
		return get2BytesToShort(b, 0);
	}
	
	public static short get2BytesToShort(byte[] b, int pos) {
		if (b.length < (pos + 1)) return -1;
		
		int[] i = new int[2];
		
		i[0] = new Byte(b[pos + 0]).intValue();
		i[1] = new Byte(b[pos + 1]).intValue();

		i[0] = (i[0] << 8 ) & 0x0000FF00;
		i[1] = i[1] & 0x000000FF;
		
		return (short)(i[0] | i[1]);
	}
	
	public static byte[] getShortTo2Bytes(short s) {
		byte[] b = new byte[2];
		
		b[0] = (byte)((s >> 8) & 0x000000FF);
		b[1] = (byte)(s  & 0x000000FF);
		
		return b;
	}

	public static short get2BytsToShortReverse(byte[] b) {
		return get2BytesToShortReverse(b, 0);
	}
	
	public static short get2BytesToShortReverse(byte[] b, int pos) {
		
		if (b.length < (pos + 1)) return -1;
		
		int[] i = new int[2];
		
		i[1] = new Byte(b[pos + 0]).intValue();
		i[0] = new Byte(b[pos + 1]).intValue();
		
		i[0] = (i[0] << 8 ) & 0x0000FF00;
		i[1] = i[1] & 0x000000FF;
		
		return (short)(i[0] | i[1]);
	}
	
	public static byte[] getShortTo2BytesReverse(short s) {
		byte[] b = new byte[2];
		
		b[1] = (byte)((s >> 8) & 0x000000FF);
		b[0] = (byte)(s  & 0x000000FF);
		
		return b;
	}

	public static int get4BytesToInt(byte[] b) {
		return get4BytesToInt(b, 0);
	}
	
	public static int get4BytesToInt(byte[] b, int pos) {
		
		if (b.length < (pos + 3)) return -1;

		int[] i = new int[4];
		
		i[0] = new Byte(b[pos + 0]).intValue();
		i[1] = new Byte(b[pos + 1]).intValue();
		i[2] = new Byte(b[pos + 2]).intValue();
		i[3] = new Byte(b[pos + 3]).intValue();
		
		i[0] = (i[0] << 24) & 0xFF000000;
		i[1] = (i[1] << 16) & 0x00FF0000;
		i[2] = (i[2] << 8 ) & 0x0000FF00;
		i[3] = i[3] & 0x000000FF;
		
		return (i[0] | i[1] | i[2] | i[3]);
	}
	
	public static byte[] getIntTo4Bytes(int i) {
		byte[] b = new byte[4];
		
		b[0] = (byte)((i >> 24) & 0x000000FF);
		b[1] = (byte)((i >> 16) & 0x000000FF);
		b[2] = (byte)((i >> 8) & 0x000000FF);
		b[3] = (byte)(i  & 0x000000FF);
		
		return b;
	}

	public static int get4BytsToIntReverse(byte[] b) {
		return get4BytesToIntReverse(b, 0);
	}
	
	public static int get4BytesToIntReverse(byte[] b, int pos) {
		
		if (b.length < (pos + 3)) return -1;
		
		int[] i = new int[4];
		
		i[3] = new Byte(b[pos + 0]).intValue();
		i[2] = new Byte(b[pos + 1]).intValue();
		i[1] = new Byte(b[pos + 2]).intValue();
		i[0] = new Byte(b[pos + 3]).intValue();
		
		i[0] = (i[0] << 24) & 0xFF000000;
		i[1] = (i[1] << 16) & 0x00FF0000;
		i[2] = (i[2] << 8 ) & 0x0000FF00;
		i[3] = i[3] & 0x000000FF;
		
		return (i[0] | i[1] | i[2] | i[3]);
	}
	
	public static byte[] getIntTo4BytesReverse(int i) {
		byte[] b = new byte[4];
		
		b[3] = (byte)((i >> 24) & 0x000000FF);
		b[2] = (byte)((i >> 16) & 0x000000FF);
		b[1] = (byte)((i >> 8) & 0x000000FF);
		b[0] = (byte)(i  & 0x000000FF);
		
		return b;
	}

	public static int fillBytes(byte[] b, byte atom) {
		
		int count = 0;
		
		for(count = 0; count < b.length; count++) {
			b[count] = atom;
		}
		
		return count;
	}

	public static int tokenCount(byte[] src, byte[] tokens) throws IOException {
		int count = 0;

		if (src == null || tokens == null ) {
			throw new IOException();
		}
		
		for(int i = 0; i < src.length; i++) {
			for(int j = 0; j < tokens.length; j++) {
				if (src[i] == tokens[j]) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	public static byte[] nextTokens(byte[] src, byte[] tokens, int start) 
		throws IOException
	{
		byte[] b = null;
		if ((src == null || tokens == null || src.length < start) ) {
			throw new IOException();
		}
		
		return b;
	}

	/**
	 * bytes  1 2 3 4 5 6 7 8 9 0
	 * index |0|1|2|3|4|5|6|7|8|9|
	 * data   X X T X X X T X X T
	 * 
	 * if start position is 3 then next token position is 6
	 * And this method return (6 - 3)
	 * Else if start position is null or 0 then this method return 2
	 *            
	 * @param src       Source byte array
	 * @param tokens    Token byte array
	 * @return          Next token position
	 * @throws IOException src is null or tokens is null or start position is invalid 
	 */
	public static int getLengthToToken(byte[] src, byte[] tokens) throws IOException {
		return getLengthToToken(src, tokens, 0);
	}
	
	public static int getLengthToToken(byte[] src, byte[] tokens, int start) 
		throws IOException
	{
		boolean isFind = false;
		int i = 0;

		for(i = start; i < src.length; i++) {
			for(int j = 0; j < tokens.length; j++) {
				if (src[i] == tokens[j]) {
					isFind = true;
					break;
				}
			}
			
			if (isFind == true) {
				break; 
			}
		}
		
		return (i - start);
	}

	/**
	 * bytes  1 2 3 4 5 6 7 8 9 0
	 * index |0|1|2|3|4|5|6|7|8|9|
	 * data   X X T X X X T X X T
	 * 
	 * if start position is 3 then next token position is 6
	 * And this method return 6
	 * Else if start position is null or 0 then this method return 2
	 *            
	 * @param src       Source byte array
	 * @param tokens    Token byte array
	 * @return          Next token position
	 * @throws IOException src is null or tokens is null or start position is invalid 
	 */

	public static int getTokenPosition(byte[] src, byte[] tokens) 
		throws IOException 
	{
		return getTokenPosition(src, tokens, 0);
	}
	
	public static int getTokenPosition(byte[] src, byte[] tokens, int start) 
		throws IOException 
	{
		int i = 0;

		for(i = start; i < src.length; i++) {
			for(int j = 0; j < tokens.length; j++) {
				if (src[i] == tokens[j]) {
					return i;
				}
			}
		}
		
		return i;
	}
	
	private static int getPowerValue(int number, int power) {
		int ret = 1;
		
		for(int i = 0; i < power; i++) {
			ret *= number;
		}
		
		return ret;
	}
	
	private static int checkPositive(byte b) {
		
		int isPositive = 0;
		
		switch(b) {
			case (byte)'-':
				isPositive = -1;
				break;
			
			case (byte)'+':
				isPositive = +1;
				break;
			
			default:
				isPositive = 0;
				break;
		}
		
		return isPositive;
	}
	
	private static boolean checkSkipData(byte b) {
		
		boolean ret = false;
		
		switch (b) {
			case 0x00:
			case 0x20:
				ret = true;
				break;
				
			default:
				ret = false;
				break;
		}
		
		return ret;
	}
}
