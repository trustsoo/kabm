package jdf.framework.core.util;

import jdf.framework.core.io.SmartFile;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;


/**
 * <p>
 * 간단한 메세지 암호화와 압축을 하는  Class
 * Message Digest는  MD5를 기본으로 한다.
 *
 * </p>
 *
 * @author
 * @version 1.0
 */

public class SimpleEncrypt
{
	//대문자,소문자 , 숫자
	private static final byte arrayBytes[] =
		{
			65,
			66,
			67,
			68,
			69,
			70,
			71,
			72,
			73,
			74,
			75,
			76,
			77,
			78,
			79,
			80,
			81,
			82,
			83,
			84,
			85,
			86,
			87,
			88,
			89,
			90,
			97,
			98,
			99,
			100,
			101,
			102,
			103,
			104,
			105,
			106,
			107,
			108,
			109,
			110,
			111,
			112,
			113,
			114,
			115,
			116,
			117,
			118,
			119,
			120,
			121,
			122,
			48,
			49,
			50,
			51,
			52,
			53,
			54,
			55,
			56,
			57,
			43,
			47,
			61 };

	private static final int SIZE = 1024;
	private static final byte ASCII_TEN = 10;
	private static final byte[] KEY =
		{(byte) 'r', (byte) 'i', (byte) 'c', (byte) 'e', (byte) 'm', (byte) 'a', (byte) 'n' };

	/**
	 * message digest
	 * 2001
	 **/

	private static final String DIGEST_ALGORITHM = "MD5";

	/**
	 * <b>MD5</b> 알고리즘에 따라 문자열을 압축해 준다.
	 * <p>패스워드의 저장등이 이용된다.
	 *
	 *@param msg 압축하고자 하는 메세지
	 *@return string 압축된 메세지
	 **/
	public static String digest(String msg)
	{

		try
		{
			MessageDigest md5 = MessageDigest.getInstance(DIGEST_ALGORITHM);
			byte[] pass = md5.digest(msg.getBytes());

			StringBuffer hexStr = new StringBuffer();

			for (int i = 0; i < pass.length; i++)
			{
				int unsigned = (int) (pass[i] & 0xff);

				hexStr.append(Integer.toHexString(unsigned));
			}

			return new String(hexStr.toString());
		}
		catch (Exception ex)
		{
			return "";
		}

	}

	public static String decrypt(byte[] src) throws Exception
	{

		int i = src.length;
		byte[] pool = arrayChar2Byte(src);

		byte[] aResult0 = base64Dec(pool, i);
		byte[] aResult1 = ascii2Char(aResult0);
		byte[] aResult2 = gateXOR(aResult1);

		byte[] aResult3 = bit4Change(aResult2);

		String result = null; //=new String(aResult,"8859_1");
		try
		{
			result = new String(aResult3, "UTF-8");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 메세지를 복호화 한다.
	 *
	 *@param msg 복호화하고자 하는 메세지
	 *@return string 복원된  메세지
	 **/
	public static String decrypt(String src)
	{

		////System.out.println("=================================="+ src);

		byte[] pool = null;
		try
		{
			pool = src.getBytes("UTF-8");

			return decrypt(pool);

		}
		catch (Exception ex)
		{
			return null;
		}

	}

	/**
	 * 메세지를 암호화 한다.
	 *
	 *@param msg 암호화 하고자 하는 메세지
	 *@return string 암호화된 메세지
	 **/
	public static String encrypt(String src)
	{
		byte[] pool = null;
		try
		{
			pool = src.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException uee)
		{
			return "";
		}
		byte[] aResult4 = bit4Change(pool);

		byte[] aResult3 = gateXOR(aResult4);

		byte[] aResult2 = char2Ascii(aResult3);

		byte[] aResult1 = base64Enc(aResult2);

		byte[] aResult0 = arrayByte2Char(aResult1);

		//return aResult0;

		String returnValue = new String(aResult0);

		return returnValue;

	}

	public static byte[] base64Dec(byte[] in)
	{
		//String x = decode( new String(in) );
		//return x.getBytes();

		return Base64.decodeBytes(in);
	}

	public static final byte[] base64Dec(byte[] pool, int validLength)
	{
		int iCharLength = validLength * 3 / 4;

		boolean[] bResult = new boolean[validLength * 6];
		byte b;
		for (int l = 0; l < validLength; l++)
		{
			b = pool[l];
			for (int k = 0; k < 6; k++)
			{
				bResult[l * 6 + k] = (b & 0x20) != 0;
				b = (byte) (b << 1);
			}
		}
		byte[] aResult = new byte[iCharLength];
		for (int x = 0; x < iCharLength; x++)
		{
			aResult[x] = 0;
			for (int y = 0; y < 8; y++)
			{
				if (bResult[8 * x + y])
					aResult[x] += (byte) 1 << (7 - y);
			}
		}
		return aResult;
	}

	public static final byte[] base64Enc(byte[] source)
	{

		//String x = encode( source );
		//return x.getBytes();

		return Base64.encodeBytes(source);

		/*
		
		byte abyte0[]= new byte[(source.length * 4 + 2) / 3]; //3byte를 4 byte로 변결시킬 때 필요한 길이.
		
		int i= 0;
		int j;
		
		for (j= 0; j + 3 <= source.length; j += 3) //3개식 끊어 읽는다.
		{
		    // j가 index
		    abyte0[i]= (byte) get1To6(source, j);
		    i++;
		    abyte0[i]= (byte) get7To12(source, j);
		    i++;
		    abyte0[i]= (byte) get13To18(source, j);
		    i++;
		    abyte0[i]= (byte) get19To24(source, j);
		    i++;
		
		}
		
		switch (source.length - j) //마지막 처리. 그래야 4개 단위가 된다.
		{
		    case 1 :
		        abyte0[i]= (byte) get1To6(source, j);
		        i++;
		        abyte0[i]= (byte) get7To12(source, j);
		        //			abyte0[i]=fillFactor; i++;
		        //			abyte0[i]=fillFactor; i++;
		        break;
		
		    case 2 :
		        abyte0[i]= (byte) get1To6(source, j);
		        i++;
		        abyte0[i]= (byte) get7To12(source, j);
		        i++;
		        abyte0[i]= (byte) get13To18(source, j);
		        //			abyte0[i]=fillFactor; i++;
		        break;
		}
		return abyte0;
		*/
	}

	public static final byte[] arrayChar2Byte(byte[] b)
	{
		byte[] rtn = new byte[b.length];
		for (int l = 0; l < rtn.length; l++)
		{
			rtn[l] = arrayBytes[l % arrayBytes.length];
			for (int i = 0; i < arrayBytes.length; i++)
				if (arrayBytes[i] == b[l])
					rtn[l] = (byte) i;
		}
		return rtn;
	}
	public static final byte[] arrayByte2Char(byte[] b)
	{
		byte[] rtn = new byte[b.length];
		for (int l = 0; l < rtn.length; l++)
		{
			rtn[l] = arrayBytes[b[l]];
		}
		return rtn;

	}

	private static final int get19To24(byte abyte0[], int i)
	{
		int rtn = abyte0[i + 2] & 0x3f;
		return rtn;
	}

	private static final int get13To18(byte abyte0[], int i)
	{
		int rtn = (abyte0[i + 1] & 0xf) << 2 | (abyte0.length > i + 2 ? (abyte0[i + 2] & 0xc0) : 0) >>> 6;
		return rtn;
	}

	private static final int get7To12(byte abyte0[], int i)
	{
		int rtn = (abyte0[i] & 0x3) << 4 | (abyte0.length > i + 1 ? (abyte0[i + 1] & 0xf0) : 0) >>> 4;
		return rtn;
	}

	private static final int get1To6(byte abyte0[], int i)
	{
		int rtn = (abyte0[i] & 0xfc) >> 2;
		return rtn;
	}

	public static final byte[] ascii2Char(byte[] src)
	{
		if (src.length % 2 != 0)
		{
			//System.out.println("Odd Length Error");
			return null;
		}
		byte[] aByte = new byte[src.length / 2];
		String temp;
		for (int i = 0; i < src.length / 2; i++)
		{
			temp = new String(src, i * 2, 2);

			aByte[i] = (byte) Integer.parseInt(temp, 16);

		}
		return aByte;
	}

	public static final byte[] char2Ascii(byte[] src)
	{
		StringBuffer sb = new StringBuffer(src.length * 2);
		for (int i = 0; i < src.length; i++)
			sb.append(getAscii(src[i]));
		return sb.toString().getBytes();
	}

	private static final String getAscii(byte src)
	{
		char[] rtn = new char[2];
		int i = (src < 0) ? (src + 0x100) : src;
		int[] tmp = new int[2];
		tmp[0] = i / 0x10;
		tmp[1] = i % 0x10;
		return Integer.toString(tmp[0], 16) + Integer.toString(tmp[1], 16);
	}
	public static final byte[] bit4Change(byte[] src)
	{
		byte[] rtn = new byte[src.length];
		byte tmp = 0;
		for (int i = 0; i < src.length; i++)
		{
			rtn[i] = (byte) ((src[i] & 0xf0) / 16 + (src[i] & 0x0f) * 16);
		}
		return rtn;
	}
	public static final byte[] gateXOR(byte[] src)
	{
		return gateXOR(src, KEY);
	}
	public static final byte[] gateXOR(byte[] src, byte[] key)
	{

		try
		{

			byte[] rtn = new byte[src.length];
			for (int i = 0; i < rtn.length; i++)
				rtn[i] = (byte) (src[i] ^ key[i % key.length]);
			return rtn;
		}
		catch (Exception ex)
		{
			return null;
		}

	}

	//------------------------------------------------------------------------------------------------------------ 
	// Fields 
	//------------------------------------------------------------------------------------------------------------ 
	private final static char encodeTable[] =
		{
			'A',
			'B',
			'C',
			'D',
			'E',
			'F',
			'G',
			'H',
			'I',
			'J',
			'K',
			'L',
			'M',
			'N',
			'O',
			'P',
			'Q',
			'R',
			'S',
			'T',
			'U',
			'V',
			'W',
			'X',
			'Y',
			'Z',
			'a',
			'b',
			'c',
			'd',
			'e',
			'f',
			'g',
			'h',
			'i',
			'j',
			'k',
			'l',
			'm',
			'n',
			'o',
			'p',
			'q',
			'r',
			's',
			't',
			'u',
			'v',
			'w',
			'x',
			'y',
			'z',
			'0',
			'1',
			'2',
			'3',
			'4',
			'5',
			'6',
			'7',
			'8',
			'9',
			'+',
			'/' };
	private final static byte decodeTable[] =
		{
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			62,
			-1,
			-1,
			-1,
			63,
			52,
			53,
			54,
			55,
			56,
			57,
			58,
			59,
			60,
			61,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			0,
			1,
			2,
			3,
			4,
			5,
			6,
			7,
			8,
			9,
			10,
			11,
			12,
			13,
			14,
			15,
			16,
			17,
			18,
			19,
			20,
			21,
			22,
			23,
			24,
			25,
			-1,
			-1,
			-1,
			-1,
			-1,
			-1,
			26,
			27,
			28,
			29,
			30,
			31,
			32,
			33,
			34,
			35,
			36,
			37,
			38,
			39,
			40,
			41,
			42,
			43,
			44,
			45,
			46,
			47,
			48,
			49,
			50,
			51,
			-1,
			-1,
			-1,
			-1,
			-1 };

	//------------------------------------------------------------------------------------------------------------ 
	// Methods 
	//------------------------------------------------------------------------------------------------------------ 
	/** 
	 *  Base 64 문자열을 디코딩한다.  
	 * 
	 * @param  s  Base 64로 인코딩 되어 있는 문자열 
	 * @return    디코딩된 문자열 
	 */
	public static String decode(String s)
	{
		StringBuffer stringbuffer = new StringBuffer();
		int i = s.length();
		int j = 0;
		int k = 0;
		int l = 0;

		if (i % 4 != 0)
			throw new IllegalArgumentException("Not a multiple of 4 characters");

		for (; i > 0 && s.charAt(i - 1) == '='; i--)
		{
			;
		}

		if (s.length() - i > 2)
			throw new IllegalArgumentException("Too many trailing =");

		for (; j < i; j++)
		{
			char c = s.charAt(j);
			byte byte0 = c >= '\200' ? -1 : decodeTable[c];
			if (byte0 < 0)
				throw new IllegalArgumentException("Illegal character");

			k = k << 6 | byte0;
			if ((l += 6) >= 8)
			{
				l -= 8;
				stringbuffer.append((char) (0xff & k >> l));
			}
		}
		return stringbuffer.toString();
	}

	/** 
	 * 일반 문자열을 Base 64로 인코딩한다.  
	 * 
	 * @param  인코딩할 일반 문자열 
	 * @return Base 64로 인코딩된 문자열 
	 */
	public static String encode(String s)
	{
		StringBuffer stringbuffer = new StringBuffer();
		int i = s.length();
		int j = 0;
		int k = 0;
		for (int l = 0; l < i; l++)
		{
			char c = s.charAt(l);
			if (c >= '?')
				throw new IllegalArgumentException("Illegal character");

			j = j << 8 | c;
			for (k += 8; k >= 6;)
			{
				k -= 6;
				stringbuffer.append(encodeTable[0x3f & j >> k]);
			}
		}
		switch (k)
		{
			case 2 :
				// '\002' 
				stringbuffer.append(encodeTable[0x3f & j << 4]);
				stringbuffer.append('=');
				stringbuffer.append('=');
				break;
			case 4 :
				// '\004' 
				stringbuffer.append(encodeTable[0x3f & j << 2]);
				stringbuffer.append('=');
				break;
		}
		return stringbuffer.toString();
	}

	/** 
	 * 일반 문자열을 Base 64로 인코딩한다.  
	 * 
	 * @param  인코딩할 일반 문자열 
	 * @return Base 64로 인코딩된 문자열 
	 */
	public static String encode(byte[] s)
	{
		StringBuffer stringbuffer = new StringBuffer();
		int i = s.length;
		int j = 0;
		int k = 0;
		for (int l = 0; l < i; l++)
		{
			char c = (char) s[l];
			/*
			if (c >= '?')
			    throw new IllegalArgumentException("Illegal character");
			*/

			j = j << 8 | c;
			for (k += 8; k >= 6;)
			{
				k -= 6;
				stringbuffer.append(encodeTable[0x3f & j >> k]);
			}
		}
		switch (k)
		{
			case 2 :
				// '\002' 
				stringbuffer.append(encodeTable[0x3f & j << 4]);
				stringbuffer.append('=');
				stringbuffer.append('=');
				break;
			case 4 :
				// '\004' 
				stringbuffer.append(encodeTable[0x3f & j << 2]);
				stringbuffer.append('=');
				break;
		}
		return stringbuffer.toString();
	}

	public static void main(String[] args)
	{

		byte[] test = new byte[] {(byte) 0xff, (byte) 0xff };

		byte[] result = base64Enc(test);

		System.out.println(new String(result));

		System.out.println(test.length);
		System.out.println(result.length);

		//String tmp = "AAAACkRCMDAxMDAwAAAAAQAYAjwAAD8/AAAALAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAF0ZXN0ICAgIHRlc3R0ZXN0";
		//String tmp = "AAAACkRCMDAxMDAwAAAAAQAOClIAAP//AAAABAAAAAE=";
		//String tmp = new String(result);

		SmartFile file = new SmartFile("test.txt");

		String tmp = file.getContent();

		//String tmp="AAAACkRCMDAwMDAxAAAAAQBEAooAAP//AAABfgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAFtYW5wb3dlciAgICAgICAgICAgIHRlc3QgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgAAAAK3Rlc3R0ZXN0ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA=";

		byte[] result2 = base64Dec(tmp.getBytes());

		System.out.println(result2.length);
		//System.out.println( Hexa.logFormat(result2));

		//System.out.println("Target: "+args[0]);

		//byte[] encrypted = encrypt(args[0]);

		//        String encode = encrypt(args[0]);

		//System.out.println("Encrypted: "+encode);
		//System.out.println("Decrypted: "+decrypt(encode));

	}

}