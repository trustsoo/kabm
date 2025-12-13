package jdf.framework.core.util;

/**
 * <p>
 * <b>ASCII 관련 byte stream</b>이나 숫자,문자를 서로 conversion
 * Performance와 관련된 programming에서 꼭 필요한 Class
 * </p>
 *
 *
 * @author
 * @version 1.0
 */

public final class AsciiUtil
{

	/**
	 * byte stream을 long type으로 변환
	 *
	 **/
	public static long bytesToLong(byte[] bs, int off, int count)
	{

		long result = 0;
		long temp = 0;
		long radio = 1;

		int max = off + count - 1;
		;

		for (int j = max; j >= off; j--)
		{
			temp = (bs[j] - 0x30);

			// ASCII로 0x30부터 0x39 이외는 모두 0으로 본다.
			// 문제의 소지가 있을수 있으므로 주의
			if (temp > 9 || temp < 0)
				return 0;

			result += temp * radio;
			radio *= 10;
		}

		return result;
	}

	/**
	 * byte stream을 int type으로 변환
	 *
	 **/
	public static int bytesToInt(byte[] bs, int off, int count)
	{
		int result = 0;
		int temp = 0;
		int radio = 1;

		int max = off + count - 1;
		
        int positive=1;
        
        if(bs[off]==0x2D) {
            positive=-1;
            off++;
        }

		for (int j = max; j >= off; j--)
		{
			temp = (bs[j] - 0x30);

			if (temp > 9 || temp < 0)
				return 0;

			result += temp * radio;
			radio *= 10;
		}

		return result*positive;
	}

	/**
	 * int type을 byte stream으로 변환
	 *
	 **/
	public static byte[] intToBytes(int in, int size)
	{
		byte[] result = new byte[size];

		int radio = 1;
		int temp;

		int one;
		int j = 0;
		while (true)
		{
			temp = in / radio;

			if (temp == 0)
				break;

			one = temp % 10;

			result[size - 1 - j] = (byte) (one + 0x30);
			j++;
			radio *= 10;
		}

		int max = size - j;
		for (int i = 0; i < max; i++)
			result[i] = 0x30;

		return result;

	}

	/**
	 * long type을 byte stream으로 변환
	 *
	 **/
	public static byte[] longToBytes(long in, int size)
	{
		//System.out.println(in+"/"+size);

		byte[] result = new byte[size];

		long radio = 1;
		long temp;
		long one;

		int j = 0;
		while (true)
		{
			temp = in / radio;

			if (temp == 0)
				break;

			one = temp % 10L;

			result[size - 1 - j] = (byte) (one + 0x30);
			j++;
			radio *= 10;
		}

		int max = size - j;
		for (int i = 0; i < max; i++)
			result[i] = 0x30;

		return result;

	}

	/**
	 * ASCII type을 byte stream으로 변환
	 * 즉 한글은 않된다는 예기....
	 **/
	public static byte[] asciiToBytes(String ascii)
	{
		int size = ascii.length();
		int i;
		byte[] buf = new byte[size];

		for (i = 0; i < size; i++)
			buf[i] = (byte) ascii.charAt(i);

		return buf;
	}

}
