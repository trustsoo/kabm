package jdf.framework.core.util;

import java.io.ByteArrayOutputStream;

/**
 * <p>
 * byte stream 을 16진수로 변환시켜서 표현한다. 주문이나 byte stream을 다루는 곳에서 로그출력위해 formating하기 위한 목적을 가진 Class
 * </p>
 * 
 * <pre>
 *   
 *    다음과 같은 출력을 얻을 수 있다.
 *    6E 61 6D 65 20 20 20 20 20 20 00 86 14 73 00  : name      
 *    00 1C 54 00 00 00 1F 55 35 31 9A              :
 *    
 * </pre>
 * 
 * @author
 * @version 1.0
 */

public class Hexa
{

	private final static int HEXA_LENGTH = 15;

	private static String NEWLINE = System.getProperty("line.separator");

	private final static String[] HEXA_CODE = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
			"B", "C", "D", "E", "F" };

	/**
     * FF 와 같은 코드값을 int로 변환
     * 
     * @param FF
     * @return
     */
	public static int getInt(String FF)
	{
		String first = FF.substring(0, 1);
		String second = FF.substring(1, 2);

		int h1 = getHexInt(first) * 16;
		int h2 = getHexInt(second);

		return h1 + h2;
	}

	/**
     * FF F0 02 FX EE 문자 데이터를 byte[] 로 변환
     * 
     * @param lineData
     * @return
     */
	public static byte[] getHexaBytes(String lineData)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		String[] d = SmartStringArray.split(" ", lineData);
		for (int i = 0; i < d.length; i++) {
			String hexaStr = d[i].trim();
			if (hexaStr.length() == 2)
				baos.write(getInt(hexaStr));
		}

		return baos.toByteArray();
	}

	public static void main(String[] args) throws Exception
	{
		String line = "FF 0D 3D 55 66";

		System.out.println(logFormat(getHexaBytes(line)));

	}

	private static int getHexInt(String F)
	{
		for (int i = 0; i < HEXA_CODE.length; i++) {
			if (HEXA_CODE[i].equals(F))
				return i;
		}

		return 0;
	}

	/**
     * 문자열을 16진수로 표시한다.
     * 
     */
	public static String logFormat(String x)
	{
		return logFormat(x.getBytes());
	}

	/**
     * byte stream을 16진수로 표시한다.
     * 
     */
	public static String logFormat(byte[] x)
	{
		return logFormat(HEXA_LENGTH, x, x.length);
	}

	/**
     * 정해진 폭 으로 표시한다.
     * 
     * @param width
     * @param x
     * @return
     */
	public static String logFormat(int width, byte[] x)
	{
		return logFormat(width, x, x.length);
	}

	/**
     * 정해진 데이터 길이까지만 표시한다.
     * 
     * @param x
     * @param size
     * @return
     */
	public static String logFormat(byte[] x, int size)
	{
		return logFormat(HEXA_LENGTH, x, size);
	}

	/**
     * byte stream을 길이만큼 16진수로 표시한다.
     * 
     * @param width
     * @param x
     * @param size
     * @return
     */
	public static String logFormat(int width, byte[] x, int size)
	{
		int byteLen = x.length;

		if (size < byteLen)
			byteLen = size;

		ByteArrayOutputStream tempBytes = new ByteArrayOutputStream();
		StringBuffer tempHexStr = new StringBuffer();

		for (int i = 0; i < byteLen; i++) {
			// if(x[i] < ' ' || x[i] > '~')
			if (x[i] < ' ')
				tempBytes.write(' '); // 공백으로 넣는다.
			else
				tempBytes.write(x[i]);

			int unsigned = (int) (x[i] & 0xff);

			if (unsigned < 16) // 15까지는 한자리가 나오므로
				tempHexStr.append("0");

			tempHexStr.append(Integer.toHexString(unsigned).toUpperCase() + " ");

			int mod;
			if (i == byteLen - 1 && (mod = (i + 1) % width) != 0)
				for (int j = 0; j < width - mod; j++)
					tempHexStr.append("   ");

			// if( i == (i/HEXA_LENGTH)-1+ HEXA_LENGTH*(i/HEXA_LENGTH) || i==
			// byteLen-1)
			if (((i + 1) % width) == 0 || i == byteLen - 1) {
				tempHexStr.append(" :" + tempBytes.toString());
				tempHexStr.append(NEWLINE);
				tempBytes.reset();
			}
		}

		return tempHexStr.toString();
	}

	/**
	 * Hexa 형태로 출력한다.
	 * 
	 * @param x
	 * @return
	 */
	public static String toHexString(byte[] x)
	{
		int byteLen = x.length;

		StringBuffer tempHexStr = new StringBuffer();

		for (int i = 0; i < byteLen; i++) {
			int unsigned = (int) (x[i] & 0xff);

			if (unsigned < 16) // 15까지는 한자리가 나오므로
				tempHexStr.append("0");

			tempHexStr.append(Integer.toHexString(unsigned).toUpperCase() + " ");
		}

		return tempHexStr.toString();
	}

}