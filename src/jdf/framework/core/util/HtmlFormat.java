package jdf.framework.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @(#) HtmlUtil.java Copyright 1999-2000 by LG-EDS Systems, Inc., Information Technology Group, Application
 *      Architecture Team, Application Intrastructure Part. 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 *      All rights reserved.
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not remove the information about the copyright
 * notice and the author.
 * 
 * @author WonYoung Lee, wyounglee@lgeds.lg.co.kr. 2000.01.07 NL2BR 메쏘드 추가 2000.01.14 fixLength 메쏘드 추가 2000.01.19 NL2BR
 *         메쏘드명을 translateNewline 로 변경 및 추가
 */

public final class HtmlFormat extends jdf.framework.core.util.StringFormater
{

	private final static String DOUBLE_PATTERN = "###,##0.00";

	private final static String INTEGER_PATTERN = "###,###";

	/**
	 * You can't call the constructor.
	 */
	private HtmlFormat() {
	}

	/**
	 * Translates special strings into special HTML tag format.
	 * 
	 * <xmp> & --> &amp; < --> &lt; > --> &gt; " --> &quot; ' --> &#039;
	 * ----------------------------------------------------------------- <option type=radio name=r value="xxxxxxxx">
	 * yyyyyyy <input type=hidden name=h value="xxxxxxxx"> <input type=text name=t value="xxxxxxxx"> <textarea name=msg
	 * rows=20 cols=53>xxxxxxx </textarea>- 위와 같은 HTML 소스를 생성할 때, xxxxxxx 부분의 문자열 중에서 아래에 있는 몇가지 특별한 문자들을 변환하여야 합니다. 만약
	 * JSP 라면 미리 변환하여 HTML 전체 TAG를 만들거나, 혹은 아래처럼 사용하세요. - <option type=radio name=r value=" <%= HtmlUtil.translate(s)
	 * %>"> yyyyyyy <input type=hidden name=n value=" <%= HtmlUtil.translate(s) %>"> <input type=text name=n value=" <%=
	 * HtmlUtil.translate(s) %>"> <textarea name=body rows=20 cols=53> <%= HtmlUtil.translate(s) %> </textarea>- - 또
	 * 필요하다면 yyyyyyy 부분도 translate(s)를 할 필요가 있을 겁니다. 필요할 때 마다 사용하세요. - - </xmp>
	 * 
	 * 
	 * see <a
	 * href="http://power.lgeds.lg.co.kr/~java/bbs/read.cgi?b=www&c=r_p&n=934284565">http://power.lgeds.lg.co.kr/~java/bbs/read.cgi?b=www&c=r_p&n=934284565
	 * </a> <br>
	 * see <a
	 * href="http://developer.netscape.com/docs/manuals/htmlguid/tags22.htm#1819476">http://developer.netscape.com/docs/manuals/htmlguid/tags22.htm#1819476
	 * </a>
	 * 
	 * @return the translated string.
	 * @param s
	 *            java.lang.String
	 */
	public final static String translate(String s)
	{
		if (s == null)
			return null;

		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '&')
				buf.append("&amp;");
			else if (c[i] == '<')
				buf.append("&lt;");
			else if (c[i] == '>')
				buf.append("&gt;");
			else if (c[i] == '"')
				buf.append("&quot;");
			else if (c[i] == '\'')
				buf.append("&#039;");
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}
	
	public final static String translateJson(String s)
	{
		return s.replace("\\", "\\\\")
				//.replace("\'", "\\\'")
				.replace("\"", "\\\"")
				.replace("\r\n", "\\n")
				.replace("\n", "\\n")
				.replace("\t", " ");
				
	}

	/**
	 * String에 포함된 모든 newline문자(\n)를 변환하여 줌 디폴트는 " <br/>" 로 변환함
	 * 
	 * @return the translated string.
	 * @param source
	 *            String 변환할 string
	 */
	public static String translateNewline(String source)
	{
		String DEFAULT_STRING = "<br/>";
		return translateNewline(source, DEFAULT_STRING);
	}

	/**
	 * String에 포함된 모든 newline문자(\n)를 특정String으로 변환하여 줌
	 * 
	 * @return the translated string.
	 * @param source
	 *            String 변환할 string
	 * @param specialString
	 *            newline문자를 대체할 string
	 */
	public static String translateNewline(String source, String specialString)
	{

		if (source == null)
			return "";

		/*
		 * String result = ""; java.util.StringTokenizer st = new java.util.StringTokenizer(source, "\n");
		 * while(st.hasMoreTokens()) result += st.nextToken() + specialString; return result;
		 */

		StringBuffer result = new StringBuffer(source.length() + 16);
		StringBuffer result2 = new StringBuffer(source.length() + 16);

		int i = 0;
		int j = source.indexOf('\n');

		while (j >= 0) {
			result.append(source.substring(i, j)).append(specialString);
			i = j + 1;
			j = source.indexOf('\n', i);
		}

		result.append(source.substring(i));
		String source2 = result.toString();
		i = 0;
		j = source2.indexOf((char) (13));

		while (j >= 0) {
			result2.append(source2.substring(i, j)).append("");
			i = j + 1;
			j = source2.indexOf((char) (13), i);
		}
		result2.append(source2.substring(i));

		return result2.toString();
	}

	/**
	 * 문자열을 15자 만큼만 보여주고 그 길이에 초과되는 문자열일 경우 "..."를 덧붙여 보여준다.
	 * 
	 * @return the translated string.
	 * @param s
	 *            String 변환할 문자열
	 */
	public static String fixLength(String input)
	{
		return fixLength(input, 15, "...");
	}

	/**
	 * 문자열을 일정길이 만큼만 보여주고 그 길이에 초과되는 문자열일 경우 "..."를 덧붙여 보여준다.
	 * 
	 * @return the translated string.
	 * @param s
	 *            String 변환할 문자열
	 * @param limitLength
	 *            int 문자열의 제한 길이
	 */
	public static String fixLength(String input, int limit)
	{
		return fixLength(input, limit, "...");
	}

	/**
	 * 문자열을 일정길이 만큼만 보여주고 그 길이에 초과되는 문자열일 경우 특정문자를 덧붙여 보여준다.
	 * 
	 * @return the translated string.
	 * @param s
	 *            String 변환할 문자열
	 * @param limitLength
	 *            int 문자열의 제한 길이
	 * @param postfix
	 *            String 덧붙일 문자열
	 */
	/*public static String fixLength(String input, int limit, String postfix)
	{

		if (input == null)
			return "";

		StringBuffer buffer = new StringBuffer();
		char[] charArray = input.toCharArray();
		if (limit >= charArray.length)
			return input;

		for (int j = 0; j < limit; j++)
			buffer.append(charArray[j]);

		buffer.append(postfix);

		return buffer.toString();
	}*/
	
	public static String fixLength(String str, int byteLength, String postfix) 
	{ 
	    int retLength = 0;    
	    int tempSize = 0;    
	    int asc;    
	    if(str == null || "".equals(str) || "null".equals(str)){
	        str = "";
	    }
	 
	    int length = str.length();
	     
	    for (int i = 1; i <= length; i++) {        
	        asc = (int) str.charAt(i - 1);        
	        if (asc > 127) {            
	            if (byteLength >= tempSize + 2) {                
	                tempSize += 2;                
	                retLength++;            
	            } else {                
	                return str.substring(0, retLength) + postfix;            
	            }       
	        } else {           
	            if (byteLength > tempSize) {
	                tempSize++;
	                retLength++;            
	            }        
	        }    
	    }   
	     
	    return str.substring(0, retLength);
	}

	/**
	 * 문자열에서 스페이스 뿐이거나 1byte이면 "&nbsp;"로 치환해준다.
	 * 
	 * @return the translated string.
	 * @param source
	 *            String 변환할 문자열
	 * @param keyStr
	 *            String 치환 대상 문자열
	 * @param toStr
	 *            String 치환될 문자열
	 */
	public static String replaceBlank(String source)
	{

		if (source == null || (source != null && source.trim().length() == 0)) {
			return "&nbsp;";
		} else {
			return source;
		}

	}

	/**
	 * 문자열에서 스페이스 뿐이거나 1byte이면 다른 문자로열로 치환해준다.
	 * 
	 * @return the translated string.
	 * @param source
	 *            String 변환할 문자열
	 * @param keyStr
	 *            String 치환 대상 문자열
	 * @param toStr
	 *            String 치환될 문자열
	 */
	public static String replaceBlank(String source, String target)
	{

		if (source == null || (source != null && source.trim().length() == 0)) {
			return target;
		} else {
			return source;
		}

	}

	/**
	 * 날짜 문자열에서 delimiter가 표기된 형태로 보여준다.
	 * 
	 * @return the translated string.
	 * @param date
	 *            String 변환할 문자열
	 */
	public static String printDate(String date)
	{
		if (date == null || date.equals(""))
			return "";
		return date.substring(0, 10).replace('-', '/');
	}

	/**
	 * 날짜 문자열에서 delimiter가 표기된 형태로 보여준다.
	 * 
	 * @return the translated string.
	 * @param date
	 *            String 변환할 문자열
	 * @param seperator
	 *            char delimiter
	 */
	public static String printDate(String date, char seperator)
	{
		if (date == null || date.equals(""))
			return "";
		return date.substring(0, 10).replace('-', seperator);
	}

	/**
	 * 날짜시간 문자열에서 delimiter가 표기된 형태로 보여준다.
	 * 
	 * @return the translated string.
	 * @param date
	 *            String 변환할 문자열
	 */
	public static String printDateTime(String date)
	{
		if (date == null || date.equals(""))
			return "";
		return date.substring(0, 16).replace('-', '/');
	}

	/**
	 * table내에서 값을 표시할때 공백인 경우 &gt;TD&lt; 태그내의 boarder가 보이지 않는 문제점을 해결하기 위한 method이다. 즉 공백의 필드값은 &nbsp;로 자동으로 바꾸어 준다.
	 * 
	 */
	public static void fixBlank(Object o)
	{
		if (o == null)
			return;

		Class c = o.getClass();
		if (c.isPrimitive())
			return;

		Field[] fields = c.getFields();
		for (int i = 0; i < fields.length; i++) {
			try {

				Object f = fields[i].get(o);
				Class fc = fields[i].getType();

				if (fc.getName().equals("java.lang.String")) {
					int mod = fields[i].getModifiers();
					if (Modifier.isStatic(mod) && Modifier.isFinal(mod))
						continue;

					if (f == null || ((String) f).trim().equals(""))
						fields[i].set(o, "&nbsp;");
					else {
						// String item = Utility.trim( (String)f );
						String item = ((String) f).trim();

						fields[i].set(o, item);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 리스트의 갯수가 많을 경우 한화면당 특정 개수만 보여주고 나머지는 화면이동 버튼을 두어 이동할수 있도록 하는 메소드
	 * 
	 * @param tCnt
	 *            리스트의 총 갯수
	 * @param max
	 *            한 화면에 출력되는 리스트의 갯수
	 * @param cNum
	 *            현재의 페이지 번호
	 * @return String HTML source
	 */
	public static String setPageInfo(long tCnt, int max, int cNum)
	{
		// String str = "";
		StringBuffer buf = new StringBuffer();

		int page, MAX_PAGE_CNT;
		int iDecNum = 0;

		MAX_PAGE_CNT = 10; // 한화면에 출력될 페이지 갯수
		// 한화면에 표시되는 기본 row 갯수보다 리스트가 적으면 종료
		if (max >= tCnt)
			return buf.toString();

		// 화면 페이지 갯수를 구한다.
		if (tCnt < max) {
			page = 1;
		} else {
			if ((tCnt % max) == 0)
				page = (int) (tCnt / max);
			else
				page = (int) (tCnt / max) + 1;
		}

		// First의 처리
		if (cNum == 1)
			buf.append("<td>[First]</td>\n");
		else
			buf.append("<td><a href=\"javascript:call(" + 1 + ")\">[First]</a></td>\n");

		// Prev의 처리
		iDecNum = (int) ((cNum - 1) / MAX_PAGE_CNT);
		if (iDecNum > 0) {
			int i = (iDecNum - 1) * MAX_PAGE_CNT + 1;
			buf.append("<td><a href=\"javascript:call(" + i + ")\">[Prev]</a></td>");
		}

		// page번호 출력처리
		for (int i = (iDecNum * MAX_PAGE_CNT + 1); i <= (iDecNum * MAX_PAGE_CNT + MAX_PAGE_CNT); i++) {
			// 출력 할 페이지가 없으면
			if (i > page) {
				break;
			}
			// 자신의 페이지는 선택이 안되도록 함.
			if (i == cNum)
				buf.append("<td>[" + i + "]");
			else
				buf.append("<td><a href=\"javascript:call(").append(i).append(")\">[").append(i).append("]</a></td>");
		}
		// 출력할 페이지가 있을경우 [Next] 출력
		if (((iDecNum + 1) * MAX_PAGE_CNT) < page) {
			int i = (iDecNum + 1) * MAX_PAGE_CNT + 1;
			buf.append("<td><a href=\"javascript:call(" + i + ")\">[Next]</a></td>");
		}

		// Last의 처리
		if (cNum == page)
			buf.append("<td>[Last]</td>");
		else
			buf.append("<td><a href=\"javascript:call(" + page + ")\">[Last]</a></td>");

		return buf.toString();
	}

	/**
	 * 리스트의 갯수가 많을 경우 한화면당 특정 개수만 보여주고 나머지는 화면이동 버튼을 두어 이동할수 있도록 하는 메소드
	 * 
	 * @param tCnt
	 *            리스트의 총 갯수
	 * @param max
	 *            한 화면에 출력되는 리스트의 갯수
	 * @param cNum
	 *            현재의 페이지 번호
	 * @return String HTML source
	 */
	public static String setPageNumber(long tCnt, int max, int cNum)
	{
		// String str = "";
		StringBuffer buf = new StringBuffer();

		int page, MAX_PAGE_CNT;
		int iDecNum = 0;

		MAX_PAGE_CNT = 10; // 한화면에 출력될 페이지 갯수
		// 한화면에 표시되는 기본 row 갯수보다 리스트가 적으면 종료
		if (max >= tCnt)
			return buf.toString();

		// 화면 페이지 갯수를 구한다.
		if (tCnt < max) {
			page = 1;
		} else {
			if ((tCnt % max) == 0)
				page = (int) (tCnt / max);
			else
				page = (int) (tCnt / max) + 1;
		}

		// First의 처리
		if (cNum == 1)
			buf.append("[First]&nbsp;\n");
		else
			buf.append("<a href=\"javascript:call(" + 1 + ")\">[First]</a>&nbsp;\n");

		// Prev의 처리
		iDecNum = (int) ((cNum - 1) / MAX_PAGE_CNT);
		if (iDecNum > 0) {
			int i = (iDecNum - 1) * MAX_PAGE_CNT + 1;
			buf.append("<a href=\"javascript:call(" + i + ")\">[Prev]</a>&nbsp;");
		}

		// page번호 출력처리
		for (int i = (iDecNum * MAX_PAGE_CNT + 1); i <= (iDecNum * MAX_PAGE_CNT + MAX_PAGE_CNT); i++) {
			// 출력 할 페이지가 없으면
			if (i > page) {
				break;
			}
			// 자신의 페이지는 선택이 안되도록 함.
			if (i == cNum)
				buf.append("[" + i + "]&nbsp;");
			else
				buf.append("<a href=\"javascript:call(" + i + ")\">[" + i + "]</a>&nbsp;");
		}
		// 출력할 페이지가 있을경우 [Next] 출력
		if (((iDecNum + 1) * MAX_PAGE_CNT) < page) {
			int i = (iDecNum + 1) * MAX_PAGE_CNT + 1;
			buf.append("<a href=\"javascript:call(" + i + ")\">[Next]</a>&nbsp;");
		}

		// Last의 처리
		if (cNum == page)
			buf.append("[Last]&nbsp;");
		else
			buf.append("<a href=\"javascript:call(" + page + ")\">[Last]</a>&nbsp;");

		return buf.toString();
	}

	public static String formatDouble(double amount, String pattern)
	{
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		DecimalFormat df = (DecimalFormat) nf;
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		df.setDecimalSeparatorAlwaysShown(true);
		// String pattern = "###,###.##";
		df.applyPattern(pattern);
		return df.format(amount);
	}

	
	/**
	 * 숫자값을 ###,###.## 형태로 출력
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatDouble(double amount)
	{
		return formatDouble(amount, "###,###.##");
	}

	/**
	 * 숫자값을 ###,### 형태로 표시
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatLong(long amount)
	{
		return formatDouble((double) amount, "###,###");
	}

	/**
	 * 숫자값을 pattern 에 따라 출력
	 * 
	 * @param amount
	 * @param pattern
	 * @return
	 */
	public static String formaLong(long amount, String pattern)
	{
		return formatDouble((double) amount, pattern);
	}

	/**
	 * 숫자값을 ###,### 형태로 출력
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatInt(int amount)
	{
		return formatDouble((double) amount, "###,###");
	}

	public static String formatInt(int amount, String pattern)
	{
		return formatDouble((double) amount, pattern);
	}

	public static String formatInt(String amount)
	{
		String result = "";

		try {
			double tmp = Double.parseDouble(amount);
			result = formatDouble(tmp, INTEGER_PATTERN);
		} catch (Exception e) {
			result = amount;
		}

		return result;
	}

	public static String formatDouble(String amount)
	{
		String result = "";

		try {
			double tmp = Double.parseDouble(amount);
			result = formatDouble(tmp, DOUBLE_PATTERN);
		} catch (Exception e) {
			result = amount;
		}

		return result;
	}

}