package jdf.framework.logic.spi.auth;

import jdf.framework.core.util.URLUtil;

import java.security.MessageDigest;
import java.util.Random;


/**
 * 초간단 암/복호화 모듈
 * 
 * @author
 * 
 */
public class SimpleEncrypt
{
	private final static String COMMON_KEY = "sfdsdfq35sdf";

	private static final String DIGEST_ALGORITHM = "MD5";

	public static String encryptText(String key, String text)
	{
		long finalKey = 0;
		for (int i = 0; i < key.length(); i++) {
			long tempKey = key.charAt(i);
			tempKey *= 128;
			finalKey += tempKey;
		}

		Random generator = new Random(finalKey);
		String returnString = "";
		for (int i = 0; i < text.length(); i++) {
			int temp = (int) text.charAt(i);
			temp += generator.nextInt(95);
			if (temp > 126) {
				temp -= 95;
			}
			returnString += (char) temp;
		}

		return returnString;
	}

	public static String decryptText(String key, String text)
	{
		long finalKey = 0;
		for (int i = 0; i < key.length(); i++) {
			long tempKey = key.charAt(i);
			tempKey *= 128;
			finalKey += tempKey;
		}

		Random generator = new Random(finalKey);
		String returnString = "";
		for (int i = 0; i < text.length(); i++) {
			int temp = (int) text.charAt(i);
			temp -= generator.nextInt(95);
			if (temp < 36) {
				temp += 95;
			}

			if (temp > 126) {
				temp -= 95;
			}
			returnString += (char) temp;
		}

		return returnString;
	}

	/**
	 * 문자열을 암호화
	 * 
	 * @param src
	 * @return
	 */
	public static String encrypt(String src)
	{

		return encryptText(COMMON_KEY, URLUtil.encode(src));

	}

	/**
	 * 암호화된 문자열을 복호화
	 * 
	 * @param src
	 * @return
	 */
	public static String decrypt(String src)
	{
		String result = decryptText(COMMON_KEY, src);

		return URLUtil.decode(result);

	}

	public static String digest(String msg)
	{

		try {
			MessageDigest md5 = MessageDigest.getInstance(DIGEST_ALGORITHM);
			byte[] pass = md5.digest(msg.getBytes());

			StringBuffer hexStr = new StringBuffer();

			for (int i = 0; i < pass.length; i++) {
				int unsigned = (int) (pass[i] & 0xff);

				hexStr.append(Integer.toHexString(unsigned));
			}

			return new String(hexStr.toString());
		} catch (Exception ex) {
			return "";
		}

	}
}