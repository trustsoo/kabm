package jdf.framework.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 문자열 처리에 관계된 로직을 가지고 있는 Class.
 * </p>
 * 
 * @author
 * @version 1.0
 */

public class StringFormater
{

	public static long parseLong(String str)
	{
		if (str.charAt(0) == '+')
			return Long.parseLong(str.trim().substring(1));
		return Long.parseLong(str.trim());
	}

	public static int parseInt(String str)
	{
		return Integer.parseInt(str.trim());
	}

	/**
     * 지정한 길이만큼 사이즈를 뒷쪽에 빈공간을 스페이스로 채워넣는다. 사이즈보다 문자가 큰 경우는 자른다.
     * 
     */
	public final static String fillSpace(String str, int size)
			throws RuntimeException
	{
		if (str == null)
			str = "";
		int spaces = size - str.length();

		if (spaces < 0)
			throw new RuntimeException("문자열이 제한길이를 넘었습니다. 길이제한:" + size + " 값:"
					+ str);

		StringBuffer sb = new StringBuffer(spaces);
		for (int i = 0; i < spaces; i++)
			sb.append(" ");
		return str.concat(sb.toString());

	}

	public final static int PRE = 0;

	public final static int POST = 1;

	/**
     * String을 byte[]로 변환하여 길이가 cnt가 될때까지 s문자를 반복하여 붙인다. gubun = 1 : 앞에 반복하여
     * 붙인다. gubun = 2 : 뒤에 반복하여 붙인다.
     * 
     * @param String
     *            in
     * @param int
     *            cnt
     * @param String
     *            s
     * @param String
     *            gubun
     * @return String
     */
	public static String fillString(String in, int cnt, String s, int gubun)
	{
		if (in == null)
			in = "";
		StringBuffer strbuf = new StringBuffer(in);
		byte[] b = in.getBytes();

		if (gubun == PRE)
			strbuf = strbuf.reverse();

		for (int i = 0; i < (cnt - b.length); i++) {
			strbuf.append(s);
		}
		if (gubun == PRE)
			strbuf = strbuf.reverse();

		return strbuf.toString();
	}

	public static int getByteSize(String str, int size)
	{
		if (str == null)
			return size;

		int len1 = str.length(); // 일반길이
		int len2 = str.getBytes().length; // byte배열로 했을때 길이

		return size + len1 - len2;
	}

	public final static String fillSpaceForBytes(String str, int size)
			throws RuntimeException
	{
		if (str == null)
			str = "";
		int spaces = size - str.getBytes().length;

		if (spaces < 0)
			throw new RuntimeException("문자열이 제한길이를 넘었습니다. 길이제한:" + size + " 값:"
					+ str);

		StringBuffer sb = new StringBuffer(spaces);
		for (int i = 0; i < spaces; i++)
			sb.append(" ");
		return str.concat(sb.toString());

	}

	private final static String ZEROS = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	public final static String fillZero(String str, int size)
			throws RuntimeException
	{
		/*
         * 
         * int zeros = size - str.length();
         * 
         * 
         * if (zeros < 0) throw new RuntimeException("숫자가 제한길이를 넘었습니다. 길이제한:" +
         * size + " 값:" + str);
         * 
         * StringBuffer sb = new StringBuffer(str); for (int i = 0; i < zeros;
         * i++) sb.insert(0, '0'); return sb.toString();
         */

		int zeros = size - str.length();
		if (zeros < 0)
			throw new RuntimeException("숫자가 제한길이를 넘었습니다. 길이제한:" + size + " 값:"
					+ str);
		else if (zeros == 0)
			return str;
		else
			return ZEROS.substring(0, zeros) + str;
	}

	public final static String fillBackZero(String str, int size)
			throws RuntimeException
	{

		int zeros = size - str.length();
		StringBuffer sb = new StringBuffer(str);

		if (zeros < 0)
			throw new RuntimeException("숫자가 제한길이를 넘었습니다. 길이제한:" + size + " 값:"
					+ str);

		for (int i = 0; i < zeros; i++)
			sb.append('0');
		return sb.toString();

	}

	/**
     * 문자열에서 특정 문자열을 치환한다.
     * 
     * @return the translated string.
     * @param source
     *            String 변환할 문자열
     * @param keyStr
     *            String 치환 대상 문자열
     * @param toStr
     *            String 치환될 문자열
     */
	public static String replaceStr(String source, String keyStr, String toStr)
	{
		int startIndex = 0;
		int curIndex = 0;
		StringBuffer result = new StringBuffer();

		if (toStr == null)
			toStr = " ";

		int sourceLen = source.length();
		int keyLen = keyStr.length();

		while ((curIndex = source.indexOf(keyStr, startIndex)) >= 0) {
			result.append(source.substring(startIndex, curIndex)).append(toStr);
			startIndex = curIndex + keyLen;
		}

		if (startIndex <= sourceLen)
			result.append(source.substring(startIndex, sourceLen));

		return result.toString();

	}

	public static String replaceStr2(String str, String problemStr,
			String replace)
	{
		//int len = str.length();
		int size = problemStr.length();

		if (replace == null)
			replace = " ";

		for (int i = str.lastIndexOf(problemStr); i >= 0; i = str.lastIndexOf(
				problemStr, i - 1))
			if (i == 0)

				str = replace + str.substring(i + size);
			else
				str = str.substring(0, i) + replace + str.substring(i + size);
		return str;

	}

	public static void main(String[] args)
	{
		String x = "test123123testt34234";
		String k = "test";
		String to = " REP ";

		String x1 = "";
		long s1 = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			x1 = replaceStr(x, k, to);
		}
		long s2 = System.currentTimeMillis();

		System.out.println(x1);
		System.out.println(s2 - s1);

		String x2 = "";

		long s3 = System.currentTimeMillis();

		for (int i = 0; i < 100000; i++) {
			x2 = replaceStr2(x, k, to);
		}
		long s4 = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			x2 = x.replaceAll(k, to);
			
		}
		long s5 = System.currentTimeMillis();

		System.out.println(x2);
		System.out.println(s4 - s3);
		System.out.println(s5 - s4);

	}

	/**
     * 문자열에서 특정 문자열을 치환한다. 문자열 배열의 차례대로 치환하되 더 이상 배열 값이 없으면 space 1칸으로 치환한다.
     * 
     * @return the translated string.
     * @param source
     *            String 변환할 문자열
     * @param keyStr
     *            String 치환 대상 문자열
     * @param toStr
     *            String[] 치환될 문자열 배열
     */
	public static String replaceStr(String source, String keyStr, String[] toStr)
	{
		int startIndex = 0;
		int curIndex = 0;
		int i = 0;
		StringBuffer result = new StringBuffer();
		String specialString = " ";

		while ((curIndex = source.indexOf(keyStr, startIndex)) >= 0) {
			if (i < toStr.length)
				if (toStr[i] != null)
					specialString = toStr[i++];

			result.append(source.substring(startIndex, curIndex)).append(
					specialString);
			startIndex = curIndex + keyStr.length();
		}

		if (startIndex <= source.length())
			result.append(source.substring(startIndex, source.length()));

		return result.toString();
	}

	/**
     * 
     * ex) removeZero("0000102385") ==> 102385
     * 
     * 
     * @param zeroStr :
     *            Number_String with Zero
     * @return retStr : Result (Processed) String
     */
	public static String removeZero(String zeroStr)
	{
		try {
			int pos = 0;
			int mark = 0;

			while (mark >= 0) {
				mark = zeroStr.indexOf("0", pos);

				if (pos != mark)
					break;
				pos++;
			}

			return zeroStr.substring(pos, zeroStr.length());
		} catch (Exception ex) {
			return "0";
		}
	}

	public static StringBuffer addStr(String[] arrStr, StringBuffer result)
	{
		for (int cnt = 0; cnt < arrStr.length; cnt++) {
			if (arrStr[cnt] != null)
				result.append("'" + arrStr[cnt] + "',");
		}
		return result;
	}
	
	public static boolean isValidEmail(String email) 
	{
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		if( !m.matches() ) return false;
		return true;
	}
	
	public static String removeHTMLTag(String html) throws Exception {
	    return html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}
	
	
	public static String contentFilter(String input) {
        if(input==null) {
            return null;
        }
        //String clean = new HTMLInputFilter().filter(input.replaceAll("\"", "%22").replaceAll("\'","%27").replaceAll("--", "%2D%2D").replaceAll(";", "%3B").replaceAll("\\+", ""));
        //String clean = input.replaceAll("\"", "%22").replaceAll("\'","%27").replaceAll("--", "%2D%2D").replaceAll(";", "%3B").replaceAll("\\+", "");
        //return clean.replaceAll("<", "%3C").replaceAll(">", "%3E");   
        //.replaceAll(";", "&#59;")
        //String clean = input.replaceAll("\"", "&quot;").replaceAll("\'","&#039;").replaceAll("\\+", "&#043;");
        String clean = input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        //clean = clean.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        clean = clean.replaceAll("eval\\((.*)\\)", "");
        clean = clean.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        clean = clean.replaceAll("script", "_script_");
        //clean = clean.replaceAll("document", "_document_");
        //clean = clean.replaceAll("forms", "_forms_");
        clean = clean.replaceAll("cookie", "_cookie_");
        //clean = clean.replaceAll("body", "_body_");
        clean = clean.replaceAll("[\\\"\\\'][\\s]*JAVASCRIPT:(.*)[\\\"\\\']", "\"\"");
        clean = clean.replaceAll("SCRIPT", "_SCRIPT_");
        clean = clean.replaceAll("DOCUMENT", "_DOCUMENT_");
		//clean = clean.replaceAll("FORMS", "_FORMS_");
        clean = clean.replaceAll("COOKIE", "_COOKIE_");
        //clean = clean.replaceAll("BODY", "_BODY_");
        return clean;
    }

}
