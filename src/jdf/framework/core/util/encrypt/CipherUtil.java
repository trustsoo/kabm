package jdf.framework.core.util.encrypt;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;

import jdf.framework.core.log.Logger;


public class CipherUtil 
{

	
	private static final int KEY_SIZE = 32;
	private static final int BLOCK_SIZE = 32;

	private static final int MODE_AES 	= 1;
	private static final int MODE_ARIA 	= 2;
	
	private static final String keyData = "_KABM0000_1Q2W3E4R5T00_HANNET_";
	private static final byte[] key = new byte[KEY_SIZE];
	private static Object objKey = null;
	private static ByteArrayOutputStream bout = new ByteArrayOutputStream();

	private static ARIAEngine ariaCipher = null;

	private static int cipherMode = MODE_ARIA;
	
	private static char byConstKey1[] = {
		0xe1, 0x56, 0x25, 0xb7, 0x81, 0x5d, 0xd7, 0x10,
		0xd9, 0x66, 0xe1, 0x7b, 0xd3, 0x9c, 0x41, 0x83,
		0xbb, 0x19, 0x92, 0xfd, 0x13, 0x1b, 0xbe, 0xe2,
		0x82, 0xc2, 0x58, 0x18, 0xb3, 0xc7, 0x0e, 0xfb};
	
	private static char byConstKey2[] = {
		0x26, 0x5b, 0x3e, 0x30, 0x96, 0xe8, 0x77, 0x87,
		0xaa, 0x12, 0xf4, 0x77, 0xd4, 0xfc, 0x8c, 0x3a,
		0x26, 0x5b, 0x3e, 0x30, 0x96, 0xe8, 0x77, 0x87,
		0xaa, 0x12, 0xf4, 0x77, 0xd4, 0xfc, 0x8c, 0x3a};

	private static final byte[] constKey1 = new byte[KEY_SIZE];
	private static final byte[] constKey2 = new byte[KEY_SIZE];	
	
	static {
		byte[] temp = keyData.getBytes();
		System.arraycopy(temp, 0, key, 0, temp.length);

		for(int i=0; i < KEY_SIZE; i++){
			constKey1[i] = (byte)byConstKey1[i];
			constKey2[i] = (byte)byConstKey2[i];
		}
		
		try {
			objKey = Rijndael_Algorithm.makeKey(key, BLOCK_SIZE);
			//printKey(objKey);
			
			ariaCipher = new ARIAEngine(256);
			ariaCipher.setKey(key);
			ariaCipher.setupRoundKeys();			
		}
		catch (InvalidKeyException ike) {
			Logger.debug.println("[FATAL] " + ike.getMessage());
		}
	}
	
	private CipherUtil() {}
	
	public static void setARIAKey(byte[] key){
		try {
			ariaCipher.setKey(key);
			ariaCipher.setupRoundKeys();			
		} catch (InvalidKeyException ike) {
			Logger.debug.println("[FATAL] setARIAKey : " + ike.getMessage());
		}
	}
	
	public static byte[] encode(byte[] keyvalue, byte[] text) {
		if(keyvalue == null)
			keyvalue = key;
		if(cipherMode == MODE_AES)
			return encodeAES(keyvalue,text);
		else if(cipherMode == MODE_ARIA)
			return encodeARIA(keyvalue,text);
		
		return null;
	}
	
	public static byte[] encodeAES(byte[] key, byte[] text) {
		bout.reset();
		byte[] cipherText = null;
		int offset = 0;
		System.out.println("[Plain text length=" + text.length + "]");
		
		int count = text.length / BLOCK_SIZE;
		System.out.println("[Block count=" + count + "]");
		if ((text.length % BLOCK_SIZE) > 0) count++;
		System.out.println("[Block count=" + count + "]");
		
		byte[] temp = new byte[count * BLOCK_SIZE];
		
		System.arraycopy(text, 0, temp, 0, text.length);
		
		while(true) {
			
			cipherText = Rijndael_Algorithm.blockEncrypt(temp, offset, objKey, BLOCK_SIZE);
			System.out.println("[RESULT][" + cipherText + "]");
			bout.write(cipherText, 0, cipherText.length);
			cipherText = null;
			
			offset += BLOCK_SIZE;
			
			if (text.length <= offset) 
				break;
		}
		
		return bout.toByteArray();
	}
	
	public static byte[] encodeARIA(byte[] key, byte[] text){
		bout.reset();

		int len = text.length;
		int size = ariaCipher.getBufferSize(len);
	    byte[] outbyte = new byte[size];

	    try {
			ariaCipher.setKey(key);
			ariaCipher.setupRoundKeys();
	    	ariaCipher.encrypt(text, outbyte, len);
	    }catch (InvalidKeyException ike) {
	    	Logger.debug.println("[FATAL] encodeARIA : " + ike.getMessage());
			return null;
		}
	    
	    bout.write(outbyte, 0, outbyte.length);
		
		return bout.toByteArray();
	}
	
	public static byte[] decode(byte[] keyvalue, byte[] cipherText) {
		if(keyvalue == null)
			keyvalue = key;
		if(cipherMode == MODE_AES)
			return decodeAES(keyvalue,cipherText);
		else if(cipherMode == MODE_ARIA)
			return decodeARIA(keyvalue,cipherText);
		
		return null;
	}
	
	public static byte[] decodeAES(byte[] key, byte[] cipherText) {
		bout.reset();
		byte[] text = null;
		int offset = 0;

		while(true) {
			text = Rijndael_Algorithm.blockDecrypt(cipherText, offset, objKey, BLOCK_SIZE);
			bout.write(text, 0, text.length);

			text = null;
			
			offset += BLOCK_SIZE;
			if (cipherText.length <= offset) 
				break;
		}
		
		return bout.toByteArray();
	}
	
	public static byte[] decodeARIA(byte[] key, byte[] cipherText){
		bout.reset();

		int size = cipherText.length;
	    byte[] outbyte = new byte[size];

	    try {
			ariaCipher.setKey(key);
			ariaCipher.setupRoundKeys();
			ariaCipher.decrypt(cipherText, outbyte, size);
	    }catch (InvalidKeyException ike) {
	    	Logger.debug.println("[FATAL] decodeARIA : " + ike.getMessage());
			return null;
		}
		
	    bout.write(outbyte, 0, outbyte.length);
	    
		return bout.toByteArray();
	}

	public static void printKey(Object obj) {
		
		Object[] o1 = (Object[])obj;
		int[][] encKey = (int[][])o1[0];
		int[][] decKey = (int[][])o1[1];
		
		printIntArray(encKey);
		printIntArray(decKey);
		
	}
	
	public static void printIntArray(int[][] array) {
		for(int i = 0; i < array.length; i++)
			System.out.println("{" + array[i][0] + ", " + array[i][1] + "}");
		System.out.println();
	}
	
	public static byte[] getCipherKey(ARIAEngine instance, byte[] serverKey, byte[] clientKey)
		throws InvalidKeyException
	{
		byte[] tempKey = new byte[KEY_SIZE];
	
		for (int i = 0; i < 32; i++)
		{
			tempKey[i] = (byte)(clientKey[i] | serverKey[i]);
			tempKey[i] &= clientKey[i];
			tempKey[i] = (byte)(tempKey[i] >> i);
		}
		
	    instance.setKey(constKey2);
	    instance.setupRoundKeys();
	

	    int size = instance.getBufferSize(tempKey.length);
		byte[] mainKey = new byte[size];

	    instance.encrypt(tempKey, mainKey, tempKey.length);
	    
		return mainKey;
	}
	
    public static String hexToString(byte[] x) 
    {
        int byteLen = x.length;
        
        StringBuffer tempHexStr = new StringBuffer();
        
        for(int i=0; i<byteLen;i++)
        {
        	int unsigned=(int)(x[i] & 0xff); 
        	
        	if( unsigned < 16) 
        		tempHexStr.append("0");
        	tempHexStr.append( Integer.toHexString(unsigned).toUpperCase());
        }
        
        return tempHexStr.toString();
   	}

    public static byte[] stringToHex(String input) 
    {
    	if (input == null)
    	      return new byte[0];

	    int len = input.length();
	    char[] hex = input.toCharArray();
	    byte[] buf = new byte[len / 2];

	    for (int pos = 0; pos < len / 2; pos++)
	      buf[pos] = (byte) (((toDataNibble(hex[2 * pos]) << 4) & 0xF0) | (toDataNibble(hex[2 * pos + 1]) & 0x0F));

	    return buf;
    }
    
    public static byte toDataNibble(char c)
    {
      if (('0' <= c) && (c <= '9'))
        return (byte) ((byte) c - (byte) '0');
      else if (('a' <= c) && (c <= 'f'))
        return (byte) ((byte) c - (byte) 'a' + 10);
      else if (('A' <= c) && (c <= 'F'))
        return (byte) ((byte) c - (byte) 'A' + 10);
      else
        return -1;
    }
    
	/*-----------------------------------------------------------------------
	 For test
	 ----------------------------------------------------------------------*/
	public static void main(String[] args) throws Exception{

		
		cipherMode = MODE_ARIA;
		/*
		String tempKey = "_KABM0000_1Q2W3E4R5T00_HANNET_";
		String tempVal = "1111";
		byte[] cipherText = CipherUtil.encode(tempKey.getBytes(), tempVal.getBytes("utf-8")); 
		String hexString = hexToString(cipherText);
		System.out.println(hexString);*/
		
		String plainKey =  "_KABM0000_1Q2W3E4R5T00_HANNET_";
		int plainKeyLen = plainKey.length();
		byte[] encryptKeyBytes = new byte[32];
		String input = "1111";
		
		KISA_SHA256.SHA256_Encrpyt( plainKey.getBytes(), plainKeyLen, encryptKeyBytes );		
		String data = CipherUtil.hexToString(CipherUtil.encode(encryptKeyBytes, input.getBytes("utf-8")));
		Logger.debug.println("input aria encrypt Data :"+data );
		
		/*String encPsswd = "647B62A9E3C99EF76C9F18C5F5C10A3B";
		String key = "_0000_DEV_TPTKDDPTJKWKDQKQH_";
		
		String decPsswd = new String(CipherUtil.decode(key.getBytes(),stringToHex(encPsswd)));
		System.out.println("[decPsswd:" + decPsswd + "]");
		
		
		
		byte[] cipherText = CipherUtil.encode(null, text.getBytes("utf-8")); 
		
		String hexString = hexToString(cipherText);
		//byte[] cipherText = CipherUtil.encode("_Naravision_DEV__Naravision_DEV_".getBytes(),text.getBytes());
		System.out.println("[CIPHER:" + hexToString(cipherText) + "]");
		
		stringToHex(hexString);
		
		String plainText = new String(CipherUtil.decode(null,stringToHex(hexString)));
		//String plainText = new String(CipherUtil.decode("_Naravision_DEV__Naravision_DEV_".getBytes(),cipherText));
		System.out.println("[PLAIN:" + plainText.trim() + "]");*/
	}
}
