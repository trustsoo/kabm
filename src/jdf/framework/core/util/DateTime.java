package jdf.framework.core.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import jdf.framework.core.log.Logger;
/**
 * <b><code>DateTime</code></b>
 * <p>
 * 시간에 관련해서 원하는 약식에 맞추어 출력해 준다.
 *
 * </p>
 *
 * @author unkwon
 * @version 1.0
 */

public final class DateTime
{

	/**
	 * Don't let anyone instantiate this class
	 */
	private DateTime()
	{
	}

	/**
	 * check date string validation with the default format "yyyy-MM-dd".
	 * @param s date string you want to check with default format "yyyy-MM-dd".
	 */
	public static void check(String s) throws Exception
	{
		DateTime.check(s, "yyyy-MM-dd");
	}

	/**
	 * check date string validation with an user defined format.
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example, "yyyy-MM-dd".
	 */
	public static void check(String s, String format) throws java.text.ParseException
	{
		if (s == null)
			throw new NullPointerException("date string to check is null");
		if (format == null)
			throw new NullPointerException("format string to check date is null");

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, java.util.Locale.US);
		java.util.Date date = null;
		try
		{
			date = formatter.parse(s);
		}
		catch (java.text.ParseException e)
		{
			throw new java.text.ParseException(e.getMessage() + " with format \"" + format + "\"", e.getErrorOffset());
		}

		if (!formatter.format(date).equals(s))
			throw new java.text.ParseException("Out of bound date:\"" + s + "\" with format \"" + format + "\"", 0);
	}

	/**
	 * check date string validation with the default format "yyyyMMdd".
	 * @param s date string you want to check with default format "yyyyMMdd"
	 * @return boolean true 날짜 형식이 맞고, 존재하는 날짜일 때
	 *                 false 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
	 */
	public static boolean isValid(String s) throws Exception
	{
		return DateTime.isValid(s, "yyyyMMdd");
	}

	/**
	 * check date string validation with an user defined format.
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example, "yyyy-MM-dd".
	 * @return boolean true 날짜 형식이 맞고, 존재하는 날짜일 때
	 *                 false 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
	 */
	public static boolean isValid(String s, String format)
	{
		/*
				if ( s == null )
					throw new NullPointerException("date string to check is null");
				if ( format == null ) 
					throw new NullPointerException("format string to check date is null");
		*/
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, java.util.Locale.US);
		java.util.Date date = null;
		try
		{
			date = formatter.parse(s);
		}
		catch (java.text.ParseException e)
		{
			return false;
		}

		if (!formatter.format(date).equals(s))
			return false;

		return true;
	}

	private final static java.text.SimpleDateFormat form1 =
		new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);

	/**
	 * yyyy-MM-dd 형식으로 출력한다.
	 *
	 * @return formatted string representation of current day with  "yyyy-MM-dd".
	 */
	public static String getDateString()
	{

		return form1.format(new java.util.Date());
	}

	/**
	 *
	 * For example, String time = DateTime.getFormatString("yyyy-MM-dd HH:mm:ss");
	 *
	 * @param java.lang.String pattern  "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with  your pattern.
	 */
	public static String getFormatString(String pattern)
	{
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(pattern, java.util.Locale.US);
		String dateString = formatter.format(new java.util.Date());
		return dateString;
	}

	private final static java.text.SimpleDateFormat form3 =
		new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.US);
	/**
	 * 현재 날짜을 yyyyMMdd 형식으로 출력한다.
	 *
	 * @return formatted string representation of current day with  "yyyyMMdd".
	 */
	public static String getShortDateString()
	{

		return form3.format(new java.util.Date());
	}

	private final static java.text.SimpleDateFormat form2 =
		new java.text.SimpleDateFormat("HHmmss", java.util.Locale.US);

	/**
	 * 현재 시간을 HHmmss 형식으로 출력한다.
	 * @return formatted string representation of current time with  "HHmmss".
	 */
	public static String getShortTimeString()
	{

		return form2.format(new java.util.Date());
	}
	
	private final static java.text.SimpleDateFormat formS1 =
		new java.text.SimpleDateFormat("HHmmssSSS", java.util.Locale.US);
	
	
	/**
	 * 현재 시간을 HHmmssSSS 형식으로 출력한다.
	 * @return formatted string representation of current time with  "HHmmss".
	 */
	public static String getShortMilliTimeString()
	{

		return formS1.format(new java.util.Date());
	}	
	

	private final static java.text.SimpleDateFormat form5 =
		new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US);
	/**
	 * 현재 시간을 "yyyy-MM-dd-HH:mm:ss" 형식으로 출력한다.
	 * @return formatted string representation of current time with  "yyyy-MM-dd-HH:mm:ss".
	 */
	public static String getTimeStampString()
	{

		//java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyy-MM-dd HH:mm:ss:SSS", java.util.Locale.US);
		return form5.format(new java.util.Date());
	}

	/**
	 * 현재 시간을 "HH:mm:ss" 형식으로 출력한다.
	 * @return formatted string representation of current time with  "HH:mm:ss".
	 */
	public static String getTimeString()
	{
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.US);
		return formatter.format(new java.util.Date());
	}

	private final static java.text.SimpleDateFormat form4 =
		new java.text.SimpleDateFormat("HH:mm:ss SSS", java.util.Locale.US);
	/**
	 * 현재 시간을 "HH:mm:ss SSS" 형식으로 출력한다.
	 * @return formatted string representation of current time with  "HH:mm:ss SSS".
	 */
	public static String getMilliTimeString()
	{

		return form4.format(new java.util.Date());
	}

	private final static java.text.SimpleDateFormat form10 =
		new java.text.SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.US);

	public static String getTimestampString()
	{
		return form10.format(new java.util.Date());
	}

	public static String getAdjustDateString(int yy, int mm, int dd)
	{
		return getAdjustDateString(yy, mm, dd, "yyyyMMdd");
	}

	public static String getAdjustDateString(int yy, int mm, int dd, String format)
	{
		Calendar can = Calendar.getInstance();

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);

		if (yy != 0)
			can.add(Calendar.YEAR, yy);

		if (mm != 0)
			can.add(Calendar.MONTH, mm);

		if (dd != 0)
			can.add(Calendar.DATE, dd);

		return formatter.format(can.getTime());
	}

	/**
	 * 입력받은 date (java.util.Date, java.sql.Date, java.sql,Time 등) 를 
	 * 원하는 formart으로 출력한다.
	 * <p>
	 * 예) jdf.framework.core.util.DateTime.getString( date, "yyyy M월 d일")
	 *
	 * @param date  날짜 또는 시간
	 * @param formate 형식 예) yyyy-MM-dd HH:mm:ss, yyyy/mm/dd 
	 * @return 입력받은 시간의 정규화된 문자열
	 */
	public static String getString(java.util.Date date, String format)
	{
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, java.util.Locale.US);
		return formatter.format(date);
	}

	/**
	 * 입력받은 date (문자열 20010606) 를 
	 * 원하는 formart으로 출력한다.
	 * <p>
	 * 예) jdf.framework.core.util.DateTime.getString( date, "yyyy M월 d일")
	 *
	 * @param date  날짜 또는 시간
	 * @param formate 형식 예) yyyy-MM-dd HH:mm:ss, yyyy/mm/dd 
	 * @return 입력받은 시간의 정규화된 문자열
	 */
	public static java.util.Date getDate(String date, String format)
	{
		try
		{
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, java.util.Locale.US);
			return formatter.parse(date);
		}
		catch (Exception e)
		{
			return null;
		}

	}

	
	private final static java.text.SimpleDateFormat formUS =
		new java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", java.util.Locale.US);
	
	
	/**@author advan94
	 * @param timezone
	 * @return US Time Formatted CurrentTime
	 */
	public static String getTimeZoneCurrentDate(String timezone)
	{
		String time = null;
		try
		{
			formUS.setTimeZone(TimeZone.getTimeZone(timezone));	
			time =  formUS.format(new java.util.Date());
		} catch(Exception ex)
		{
			Logger.warn.println(ex.toString());
			time = "unknown";
		}
		
		return time;
	}
	
	
	public static Date getTimeZoneDate(Date date, String timezone)
	{
		Date transDate = null;
		try
		{
			formUS.setTimeZone(TimeZone.getTimeZone(timezone));	
			String time =  formUS.format(date);
			transDate = getDate(time, "EEE, dd MMM yyyy HH:mm:ss");
		} catch(Exception ex)
		{
			Logger.warn.println(ex.toString());			
		}
		
		return transDate;
	}
	
	
	public static String trans(String date, String preFormat, String postFormat) throws IllegalArgumentException
	{
		Date datetime = getDate(date, preFormat);

		if (datetime == null)
			throw new IllegalArgumentException(date + "를 " + preFormat + "으로 변환에러");

		return getString(datetime, postFormat);
	}

	public static long getTime(String date, String format)
	{
		try
		{
			java.util.Date dat = getDate(date, format);
			return dat.getTime();
		}
		catch (Exception e)
		{
			return 0;
		}

	}

	/**
	 * replace to yyyy년mm월dd일 from yyyymmdd
	 * @param s date string you want to check.
	 * @return String yyyy년mm월dd일
	 */
	public static String replaceKRType(String s)
	{

		if (s == null)
		{
			System.err.println("date string to check is null");
			return null;
		}

		try
		{
			if (isValid(s) == false)
			{
				System.err.println("date string to check is null");
				return null;
			}
		}
		catch (Exception ex)
		{
			System.err.println(ex.getMessage());
			return null;
		}

		int mm = Integer.parseInt(s.substring(4, 6));
		int dd = Integer.parseInt(s.substring(6, 8));

		return s.substring(0, 4) + "년" + mm + "월" + dd + "일";

	}

	/**
	 * replace to yyyy년mm월dd일 from yyyymmdd
	 * @param s date string you want to check.
	 * @return String yyyy년mm월dd일
	 */
	public static String replaceKRMonthType(String s)
	{

		if (s == null)
		{
			System.err.println("date string to check is null");
			return null;
		}

		try
		{
			if (isValid(s, "yyyymm") == false)
			{
				System.err.println("date string to check is null");
				return null;
			}
		}
		catch (Exception ex)
		{
			System.err.println(ex.getMessage());
			return null;
		}

		int mm = Integer.parseInt(s.substring(4, 6));

		return s.substring(0, 4) + "년" + mm + "월";
	}

	public static String getDay(int day, String lang)
	{
		String yoil = null;

		if (lang.equals("K"))
		{
			switch (day)
			{
				case 0 :
					yoil = "일요일";
					break;
				case 1 :
					yoil = "월요일";
					break;
				case 2 :
					yoil = "화요일";
					break;
				case 3 :
					yoil = "수요일";
					break;
				case 4 :
					yoil = "목요일";
					break;
				case 5 :
					yoil = "금요일";
					break;
				case 6 :
					yoil = "토요일";
					break;
			}
		}
		if (lang.equals("E"))
		{
			switch (day)
			{
				case 0 :
					yoil = "Sunday";
					break;
				case 1 :
					yoil = "Monday";
					break;
				case 2 :
					yoil = "Tuesday";
					break;
				case 3 :
					yoil = "Wednesday";
					break;
				case 4 :
					yoil = "Thursday";
					break;
				case 5 :
					yoil = "Friday";
					break;
				case 6 :
					yoil = "Saturday";
					break;
			}
		}

		return yoil;
	}

	/**
	*
    * java.util.Calendar.DATE 를 쓰면 날짜단위로,
    * java.util.Calendar.MONTH 를 쓰면 달 단위로,
    * java.util.Calendar.YEAR 를 쓰면 년 단위로 이동
	*
	* @param String curYYMM
	* @param int field
	* @param int move
	 * @return String getFormatDate( cal.getTime(), "yyyyMM" );
	 */
	public static String getDateMove(String curYYMM, int field, int move)
	{
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, Integer.parseInt(curYYMM.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(curYYMM.substring(4, 6)) - 1);

		cal.add(field, move);
		return getString(cal.getTime(), "yyyyMM");
	}
	
	/**
	 * 날짜를 이동시킨다.
	 * 
	 * 
	 * @deprecated getAdjustDate 대신사용
	 * 
	 * @param curYYYYMMDD
	 * @param field
	 * @param move
	 * @return
	 */
	public static String getDateMove2(String curYYYYMMDD, int field, int move)
	{
		return getAdjustDate(curYYYYMMDD,field,move);
	}
	
	/**
	*
	* 날짜를 이동시킨다.
	* java.util.Calendar.DATE 를 쓰면 날짜단위로,
	* java.util.Calendar.MONTH 를 쓰면 달 단위로,
	* java.util.Calendar.YEAR 를 쓰면 년 단위로 이동
  *
	*
	* @param String curYYMMDD
	* @param int field
	* @param int move
	* @return String getFormatDate( cal.getTime(), "yyyyMMdd" );
	*/
	public static String getAdjustDate(String curYYYYMMDD, int field, int move)
	{
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, Integer.parseInt(curYYYYMMDD.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(curYYYYMMDD.substring(4, 6)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(curYYYYMMDD.substring(6)));

		cal.add(field, move);
		return getString(cal.getTime(), "yyyyMMdd");
	}

	/**
	 * 다음년도를 가져온다.
	 * @param String curYYMM.
	 * @return String getDateMove( curYYMM, Calendar.YEAR, 1 )
	 */
	public static String getDateNextYY(String curYYMM)
	{
		return getDateMove(curYYMM, Calendar.YEAR, 1);
	}

	/**
		 * 전년도를 가져온다.
		 * @param String curYYMM.
		 * @return String getDateMove( curYYMM, Calendar.YEAR, -1 )
		 */
	public static String getDatePrevYY(String curYYMM)
	{
		return getDateMove(curYYMM, Calendar.YEAR, -1);
	}

	/**
	* 다음달을 가져온다.
	* @param String curYYMM.
	* @return String getDateMove( curYYMM, Calendar.MONTH, 1 )
	*/
	public static String getDateNextMM(String curYYMM)
	{
		return getDateMove(curYYMM, Calendar.MONTH, 1);
	}

	/**
	* 이전달을 가져온다.
	* @param String curYYMM.
	* @return String getDateMove( curYYMM, Calendar.MONTH, -1  )
	*/
	public static String getDatePrevMM(String curYYMM)
	{
		return getDateMove(curYYMM, Calendar.MONTH, -1);
	}

	/**
	* 다음일을 가져온다.
	* @param String curYYMM.
	* @return String getDateMove( curYYYYMMDD, Calendar.DATE, 1  )
	*/
	public static String getDateNextDD(String curYYYYMMDD)
	{
		return getAdjustDate(curYYYYMMDD, Calendar.DATE, 1);
	}

	/**
	* 이전음일을 가져온다.
	* @param String curYYMM.
	* @return String getDateMove( curYYYYMMDD, Calendar.DATE, -1  )
	*/
	public static String getDatePrevDD(String curYYYYMMDD)
	{
		return getAdjustDate(curYYYYMMDD, Calendar.DATE, -1);
	}

	public static Date getThisMonthEndDt(String year, String month)
	{
		if (year == null || year.equals(""))
		{
			year = DateTime.getFormatString("yyyy");
		}
		
		if (month == null || month.equals(""))
			month = "12";

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		cal.set(Calendar.DATE, 1);

		cal.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_MONTH) + 1);

		cal.add(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);

		return cal.getTime();
		//return getString(cal.getTime(), "M월 dd일");
	}

}
