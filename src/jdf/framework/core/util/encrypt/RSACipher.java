package jdf.framework.core.util.encrypt;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.log.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class RSACipher
{
	private static final String LOG_ID="<at:RSACipher> ";
	
	public static DataSet getSecretKey()
	{
		KeyPairGenerator generator;
		DataSet result = new DataSet();
		try {
			generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);			
			KeyPair keyPair = generator.genKeyPair();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			
			
			// 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);			 			
			String publicKeyModulus = publicSpec.getModulus().toString(16);
			String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
			
			result.put("modulus", publicKeyModulus);
			result.put("exponent", publicKeyExponent);
			result.put("privateKey", privateKey);
			
			
		} catch (NoSuchAlgorithmException e) {
			Logger.warn.println(LOG_ID+e.toString());
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			Logger.warn.println(LOG_ID+e.toString());
		}
		return result;
	}
	
	public static String encode(PublicKey pubKey, String plainText)
	{
		javax.crypto.Cipher cipher;
		String result = null;
		try {
			cipher = javax.crypto.Cipher.getInstance("RSA");
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, pubKey);
			byte[] plainTextBytes = plainText.getBytes("utf-8");
			
			byte[] encBytes = cipher.doFinal(plainTextBytes);
			result = new String(encBytes);
			//result = jdf.framework.core.util.encrypt.CipherUtil.hexToString(encBytes);
		} catch (NoSuchAlgorithmException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static String decode(PrivateKey privateKey, String encodedTxt)
	{
		javax.crypto.Cipher cipher;
		String decryptedValue = null;;
		try {
			cipher = javax.crypto.Cipher.getInstance("RSA");
			byte[] encryptedBytes = CipherUtil.stringToHex(encodedTxt);
			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			decryptedValue = new String(decryptedBytes, "utf-8");
		} catch (NoSuchAlgorithmException e) {
			Logger.warn.println(LOG_ID+e.toString());
		} catch (NoSuchPaddingException e) {
			Logger.warn.println(LOG_ID+e.toString());
		} catch (InvalidKeyException e) {
			Logger.warn.println(LOG_ID+e.toString());
		} catch (IllegalBlockSizeException e) {
			Logger.warn.println(LOG_ID+e.toString());
		} catch (BadPaddingException e) {
			Logger.warn.println(LOG_ID+e.toString());
		} catch (UnsupportedEncodingException e) {
			Logger.warn.println(LOG_ID+e.toString());
		} 
        
        return decryptedValue;
	}
}