package jdf.framework.core.data.schema.format;

/**
 * 
 * 날짜 포매팅용
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-12-07 오후 7:01:35
 *
 */
public class DateTimeFormatter extends Formatter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String LOG_ID="<f:DateTimeFormatter> ";
	private String preForm;
	private String postForm;

	public DateTimeFormatter(String format) throws IllegalArgumentException
	{
		int idx = format.indexOf("->");

		if (idx < 0)
			throw new IllegalArgumentException("날짜포맷은 -> 로 이전/이후 포맷이 정의되어야 합니다.");

		try
		{
			preForm = format.substring(0, idx);
			postForm = format.substring(idx + 2);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("parsing err. " + e.getMessage());
		}

	}

	/**
	 * 
	 * 
	 * @see Formatter#format(Object)
	 */
	public String format(Object data) throws IllegalArgumentException
	{
		if (data == null)
			throw new IllegalArgumentException("data is null");

		String dt = data.toString();

		if (dt.length() == 0)
			throw new IllegalArgumentException("data is empty");

		while (preForm.length() > dt.length())
            dt = "0" + dt;

		try
		{
			return jdf.framework.core.util.DateTime.trans(dt, preForm, postForm);
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Exception e)
		{
			jdf.framework.core.log.Logger.warn.println(LOG_ID+preForm + "->" + postForm + " " + data.toString());
			throw new IllegalArgumentException(e.toString());
		}
	}

}