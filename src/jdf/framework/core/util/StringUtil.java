package jdf.framework.core.util;

import java.util.ArrayList;

public class StringUtil
{
	/**
	 * 한 문자열 안에 특정 문자열이 처음으로 나타난 위치를 int값으로 반환합니다. 발견하지 못한다면, -1을 리턴한다.
	 * 
	 * @param start
	 *            시작 위치
	 * @param string1
	 *            해당 문자열
	 * @param string2
	 *            찾을 특정 문자열
	 * @param compare
	 *            대 소문자 비교 여부
	 */
	public static int getInStr(int start, String string1, String string2, boolean compare)
	{
		int result = 0;
		if (!compare) {
			result = string1.toUpperCase().indexOf(string2.toUpperCase(), start);
		} else {
			result = string1.indexOf(string2, start);
		}
		System.out.println("Result Value: " + result);
		return result;
	}

	/**
	 * 문자열의 왼쪽으로부터 지정된 수의 문자를 String값으로 반환합니다.
	 * 
	 * @param string
	 *            해당 문자열입니다.
	 * @param length
	 *            반환할 문자 수
	 */
	public static String getLeft(String string, int length)
	{
		return string.substring(0, length);
	}

	/**
	 * 문자열의 오른쪽으로부터 지정된 수의 문자를 String값으로 반환합니다.
	 * 
	 * @param string
	 *            해당 문자열입니다.
	 * @param length
	 *            반환할 문자 수
	 */
	public static String getRight(String string, int length)
	{
		return string.substring(string.length() - length, string.length());
	}

	/**
	 * 한 문자열에서 지정된 수의 문자를 String 값으로 반환합니다.
	 * 
	 * @param string
	 *            해당 문자열입니다.
	 * @param start
	 *            시작 인덱스입니다.
	 * @param length
	 *            반환할 문자 수
	 */
	public static String getMid(String string, int start, int length)
	{
		if (string.length() <= (length - start)) {
			length = string.length() - start;
		}
		return string.substring(start, start + length);
	}

	/**
	 * 문자열에서 첫 글자에 해당하는 문자 코드를 나타내는 int 값을 반환합니다.
	 * 
	 * @param string
	 *            해당 문자열입니다.
	 */
	public static int getAsc(String string)
	{
		byte[] b = string.getBytes();
		return (int) b[0];
	}

	/**
	 * 지정된 문자 코드와 관련된 문자가 포함된 String 값을 반환합니다.
	 * 
	 * @param charcode
	 *            인수는 문자를 식별하는 int 값입니다.
	 */
	public static String getChr(int charcode)
	{
		byte[] b = { (byte) charcode };
		return new String(b);
	}

	/**
	 * 해당 숫자의 16진수 값을 String 값으로 반환합니다.
	 * 
	 * @param number
	 *            10진수에 해당하는 int 값입니다.
	 */
	public static String getHex(int number)
	{
		return Integer.toHexString(number);
	}

	/**
	 * 지정된 길이의 반복되는 문자열을 String값으로 반환합니다
	 * 
	 * @param number
	 *            반환되는 문자열의 길이입니다.
	 * @param character
	 *            문자를 지정하는 문자 코드나 반환 문자열을 구성하는 첫 문자로 사용되는 문자식
	 */
	public static String getString(int number, String character)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < number; i++) {
			sb.append(character);
		}
		return sb.toString();
	}

	/**
	 * length 만큼 스트링을 잘라, 스트링 배열로 반환
	 * 
	 * @param length
	 *            자를 길이
	 * @param string
	 *            자를 대상
	 * @param ignored
	 *            length만큼 자를후 맨뒤의 길이가 length보다 작다면, 버림을 할것인지 여부
	 */
	public static String[] splitLength(int length, String string, boolean ignored)
	{
		int temp = string.length() / length;

		int na = (string.length() % length);

		if (!ignored) {
			if (na > 0)
				temp++;
		}

		int size = temp;

		String[] strings = new String[size];

		for (int i = 0; i < size; i++) {
			if ((length * i + length) >= string.length())
				strings[i] = string.substring(length * i);
			else
				strings[i] = string.substring(length * i, (length * i + length));
		}

		return strings;
	}

	/**
	 * 대상 문자열을 구분자로 구분한 스트링을 배열로 반환
	 * 
	 * @param str
	 *            대상문자열
	 * @param d
	 *            구분자(문자열)
	 */
	public static String[] split(String str, String d)
	{
		// javascript의 str.split(d); 과 동일
		ArrayList list = new ArrayList();

		int dLen = d.length();

		if (str.indexOf(d) == -1)
			list.add(str);
		else
			while (str.indexOf(d) != -1) {
				list.add(str.substring(0, str.indexOf(d)));
				str = str.substring(str.indexOf(d) + dLen);
				if (str.indexOf(d) == -1)
					list.add(str);
			}

		String[] array = new String[list.size()];
		list.toArray(array);

		return array;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String removeChar(String str)
	{
		// String val = "";
		StringBuffer buf = new StringBuffer();
		char[] arr = str.toCharArray();
		for (int i = 0; i < arr.length; i++)
			if (!isNaN(arr[i]))
				buf.append(arr[i]);

		return buf.toString();
	}

	/**
	 * 대상 문자열을 숫자로 바꿀수 없는지 여부. 없으면 true, 있으면 false
	 * 
	 * @param str
	 *            대상문자열
	 */
	public static boolean isNaN(char str)
	{
		return isNaN(String.valueOf(str));
	}

	public static boolean isNaN(String str)
	{
		// String str = (String)obj;
		// char dot = '.';
		try {
			// 소숫점은 어떻게 처리할 것인가?
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return true;
		}
		return false;
	}

	public static boolean hasLength(String str)
	{
		return (str != null && str.length() > 0);
	}

	public static boolean hasText(String str)
	{
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	
	public static String nvl(String str)
	{
		if(str == null) return "";
		else return str;
	}
}