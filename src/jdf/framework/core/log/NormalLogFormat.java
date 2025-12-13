package jdf.framework.core.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Date;

/**
 * <p>
 * 일반적인 byte stream에서 16진수의 값과 문자열값의 출력형태를 얻기위한 Class
 * </p>
 * 
 * <pre>
 *   다음과 같은 출력을 얻을 수 있다.
 *   6E 61 6D 65 20 20 20 20 20 20 00 86 14 73 00  : name      
 *   00 1C 54 00 00 00 1F 55 35 31 9A              :
 * </pre>
 * 
 * @author
 * @version 1.0
 */

public class NormalLogFormat implements LogFormat
{

	private static String NEWLINE = System.getProperty("line.separator");

	public NormalLogFormat() {
		super();

		String format = Logger.DEFAULT_LOG_TIME_FORMAT;

		try {
			jdf.framework.core.Config conf = jdf.framework.core.Configuration.lookup("/logger");

			format = conf.getString("dateFormat", Logger.DEFAULT_LOG_TIME_FORMAT);
		} catch (Exception e) {
		}

		formatter = new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
	}

	public void setDateFormat(String form)
	{
		formatter = new java.text.SimpleDateFormat(form, java.util.Locale.KOREA);
	}

	java.text.SimpleDateFormat formatter;

	// java.util.Date datetime = new java.util.Date();

	/*
     * 매번 포맷팅의 불합리를 해결해 보고자 하는 노력 프로그램은 지저분해지지만, 속도를 위해서라면...
     */

	// long lastUpdateTime = 0; // 마지막 수정된 시간 ( long형태)
	// long checkTimeGap = 200; // format을 바꿀 시간 간격
	// String lastStringDate = ""; //
	/**
     * 날짜 formating
     * 
     * @param time
     * @return
     */
	private String getFormatDate(long time)
	{

		return formatter.format(new Date(time));

		/*
         * if( time-lastUpdateTime > checkTimeGap) { datetime.setTime(time); lastStringDate =
         * formatter.format(datetime); lastUpdateTime = time; }
         * 
         * return lastStringDate;
         */
	}

	/**
     * LogInfo에서 정보를 꺼내와 로그출력을 한다.
     * 
     */
	public String formating(LogInfo log)
	{
		StringBuffer result = new StringBuffer();

		result.append(getFormatDate(log.time));

		// thread id
		result.append(" ").append(log.threadId);

		if (log.sMode != null)
			result.append(" ").append(log.sMode);

		switch (log.mode) {
		case LogInfo.INFO:
			result.append(" ").append(log.strData);

			break;

		case LogInfo.DUMP:

			result.append(NEWLINE).append(new String(log.data));

			break;

		case LogInfo.ERR:

			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			PrintWriter writer = new PrintWriter(bytes, true);
			log.error.printStackTrace(writer);

			result.append(NEWLINE).append(bytes.toString());
			bytes = null;
			writer = null;
			break;
		}

		// result.append(NEWLINE);

		return result.toString();
	}

}
