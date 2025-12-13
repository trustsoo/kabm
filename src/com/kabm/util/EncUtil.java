package com.kabm.util;



import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EncUtil
{
	
	public static String encrypt(String s, String instCode)
	{
		if( null == s || s.equals("")){
			return "";
		}
		
		String result = null;
		if( instCode.trim().length() == 0 )
			instCode = new SimpleDateFormat("00yyyyMMdd").format(Calendar.getInstance().getTime());
		
		int c1 = toWord(Integer.parseInt( instCode.substring(0,5)));
		int c2 = toWord(Integer.parseInt( instCode.substring(5,10)));
		int mykey = toWord(Integer.parseInt(instCode.substring(2,4) + instCode.substring(8,10)));
		
		String sEncrypt = "";
		for( int i = 0; i < s.length();++i)
		{
			int curValue = ( toByte(s.charAt(i)) ^ ( mykey >> 8 ));
			sEncrypt += ((char)curValue);
			mykey = (toByte(sEncrypt.charAt(i)) + mykey)*c1+c2;
			mykey = toWord(mykey);			
		}
		
		String rEncrypt = "";
		for( int i =0 ; i < sEncrypt.length(); ++i){
			char rChar = sEncrypt.charAt(i);
			rEncrypt += (format3digit(rChar));
		}
		result = rEncrypt.toString();
		return result;
	}
	
	public static String decrypt(String s, String instCode){
		String result = null;
		if( null == s || s.equals("")){
			return null;
		}
		String rDecrypt = "";
		for(int i = 0; i< s.length(); i+=3){
			String cur = s.substring(i,i+3);
			char value = (char)Integer.parseInt(cur);
			rDecrypt += value;
			
		}
		if( instCode.trim().length() == 0 )
			instCode = new SimpleDateFormat("00yyyyMMdd").format(Calendar.getInstance().getTime());//key
		
		int c1 = toWord(Integer.parseInt( instCode.substring(0,5)));
		int c2 = toWord(Integer.parseInt( instCode.substring(5,10)));
		int mykey = toWord(Integer.parseInt(instCode.substring(2,4) + instCode.substring(8,10)));
		
		String sDecrypt = "";
		for( int i = 0; i < rDecrypt.length();++i)
		{
			int curChar = ( toByte(rDecrypt.charAt(i)) ^ ( mykey >> 8 ));
			sDecrypt += ((char)curChar);
			mykey = (toByte(rDecrypt.charAt(i)) + mykey)*c1+c2;
			mykey = toWord(mykey);			
		}
		result = sDecrypt.toString();
		return result;
	}
	
	static String format3digit(int i){
		String result = null;
		result = "" + (char)('0' + (i%1000/100)) + (char)('0' + (i%100/10)) + (char)('0' + (i%10));
		return result;
	}
	
	static int toByte(int i ){
		if( i < 0 ) {
			i = i & ( 0x7fffffff);			
		}
		return i % 256;
	}
	
	static int toWord(int i ){
		if( i < 0 ) {
			i = i & ( 0x7fffffff);			
		}
		return i % 65536;
	}
}