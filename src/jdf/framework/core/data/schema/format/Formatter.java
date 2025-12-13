package jdf.framework.core.data.schema.format;

import jdf.framework.core.util.SmartStringArray;

/**
 * <b><code>Formatter</code> </b>
 * <p>
 * Legacy 시스템이나 일반적인 TCP/IP 소켓을 이용한 데이타 처리 시스템과 데이타를 주고 받기 위해 사용되는 데이타에서 하나의 필드가 가지는 데이타 타입을 정의한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class Formatter implements java.io.Serializable
{
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Formatter formatter;

	private final static int UNDEFINED = 0;

	private final static int DIVIDE = 1;

	private final static int MULPILE = 2;

	private int mode = UNDEFINED;

	private double anotherVal = 0;

	protected Formatter() {

	}

	public Formatter(String format) throws IllegalArgumentException {
		if (format != null && format.length() > 0) {
			// 시간 포맷
			if (format.indexOf("->") > 0) {

				String[] formatItems = SmartStringArray.split("->", format);

				try {
					// /100->#,### 에서 두번째항목에 # 가 있는지 체크한다.
					if (formatItems[1].indexOf("#") > -1) {

						formatter = new NumberFormatter(formatItems[1]);

						// 기호
						String mark = formatItems[0].substring(0, 1);

						if ("*".equals(mark))
							this.mode = MULPILE;
						else if ("/".equals(mark))
							this.mode = DIVIDE;
						else
							throw new IllegalArgumentException(mark + " is not handled");

						String val = formatItems[0].substring(1);
						this.anotherVal = Double.parseDouble(val);

						return;
					}
				} catch (IllegalArgumentException iae) {
					throw iae;
				} catch (Exception e) {
					throw new IllegalArgumentException(e.toString());
				}

				formatter = new DateTimeFormatter(format);
			}

			else if (format.indexOf("#") > -1)
				formatter = new NumberFormatter(format);
			else if(format.indexOf("mask") > -1)
			{
				formatter = new MaskingFormatter(format);
			} else if(format.indexOf("unescapeHtml4") > -1)
			{
				formatter = new UnEscapeHtmlFormatter("4.0");
			} else if(format.indexOf("unescapeHtml3") > -1)
			{
				formatter = new UnEscapeHtmlFormatter("3.0");
			}

			else
				throw new IllegalArgumentException("알수없는 포맷형태입니다.");

		}
	}
	
	/**
	 * 포맷정보가 존재하는가?
	 * @return
	 */
	public boolean existFormatInfo()
	{
		if(this.formatter!=null)
			return true;
		else
			return false;
	}

	/**
	 * data 를 formating 한다.
	 * 
	 * @param data
	 * @return
	 * @throws IllegalArgumentException
	 */
	public String format(Object data) throws IllegalArgumentException
	{
		Object inData = data;

		if (data == null)
			throw new IllegalArgumentException("data is null");

		if (formatter == null)
			return data.toString();

		else {
			try {

				if (this.mode == MULPILE) {
					inData = new Double(Double.parseDouble(data.toString()) * this.anotherVal);
				} else if (this.mode == DIVIDE) {
					inData = new Double(Double.parseDouble(data.toString()) / this.anotherVal);
				}

			} catch (Exception e) {
				return data.toString();
			}

			return formatter.format(inData);
		}

	}

}