package jdf.framework.core.data.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jdf.framework.core.data.schema.format.Formatter;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.DateTime;
import jdf.framework.core.util.SmartStringArray;


/**
 * <b><code>FieldType</code> </b>
 * <p>
 * Legacy 시스템이나 일반적인 TCP/IP 소켓을 이용한 데이타 처리 시스템과 데이타를 주고 받기 위해 사용되는 데이타에서 하나의 필드가 가지는 데이타 타입을 정의한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class Field implements FieldType, java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	private final static String LOG_ID = "<f:Field> ";

	public final static String PROPERTY_KEY_BLD = "bld";

	public final static String PROPERTY_KEY_BLD_URL = "bld url";

	private String name;

	// field명의 label
	private String label;

	private String typeName;

	private int type;

	private int toType;

	private int size;

	private Object defaultVal = "";

	private String format;

	private Formatter formatter;

	private int decimalPoint;

	// block의 ref id
	private String refId;

	private boolean isLittleEndianVal = false;

	private Properties properies;

	// xpath 정보
	private String xpathExpression;

	private List<Parameter> parmeterList = new ArrayList<>();

	/**
	 * 기본생성자
	 * 
	 * @param name
	 * @param typeName
	 * @param type
	 * @param size
	 * @param defaultVal
	 * @param format
	 * @param decimal
	 * @param toType
	 */
	public Field(String name, String typeName, int type, int size, Object defaultVal, String format, int decimal,
			int toType) {
		this.name = name;
		this.typeName = typeName.toLowerCase();

		this.type = type;
		this.size = size;

		this.defaultVal = defaultVal;
		this.format = format;
		try {
			this.formatter = new Formatter(format);
		} catch (Exception e) {
			Logger.warn.println(LOG_ID + "Filed Schema err ", e);
		}

		this.decimalPoint = decimal;
		this.toType = toType;
	}

	/**
	 * properties="view:false;width:100px;" 이런식으로 IO Schema 에 세팅된 값을 읽는다.
	 * 
	 * @param props
	 */
	public void setPropertyStr(String props)
	{
		if (props == null || props.length() == 0)
			return;

		this.properies = new Properties();

		try {
			String[] tmp = SmartStringArray.split(";", props);
			for (int i = 0; i < tmp.length; i++) {
				String[] property = SmartStringArray.split(":", tmp[i]);

				this.properies.setProperty(property[0], property[1]);

			}
		} catch (Exception e) {

		}
	}

	/**
	 * 특정 속성값 추가
	 * 
	 * @param key
	 * @param val
	 */
	public void setProperty(String key, String val)
	{

		if (this.properies == null)
			this.properies = new Properties();

		this.properies.setProperty(key, val);

	}

	/**
	 * properties 속성에 정의된 property 값을 return 한다.
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key)
	{
		if (this.properies == null)
			return null;

		return this.properies.getProperty(key);
	}

	/**
	 * properties 속성에 정의된 property 들을 return 한다.
	 * 
	 * @return
	 */
	public Properties getProperties()
	{
		return this.properies;
	}

	/**
	 * 이 필드의 값이 LittleEndian 인지 여부 설정
	 * 
	 * @param isLE
	 */
	public void setLittleEndianType(boolean isLE)
	{
		this.isLittleEndianVal = isLE;
	}

	/**
	 * 필드의 속성이 LittleEndian 여부인지 반환
	 * 
	 */
	public boolean isLittleEndian()
	{
		return this.isLittleEndianVal;
	}
	
	private String refBldId;
	
	private String refBlockId;
	

	/**
	 * type이 block 인 경우 참조하는 block의 id를 정의한다.
	 * 
	 * @param id
	 */
	public void setRefId(String refId)
	{
		if (refId == null || refId.trim().length() == 0) {
			refId = null;
		}
		else {
			String[] items = SmartStringArray.split("#", refId);
			if(items.length!=2) {
				throw new IllegalArgumentException("ref format err.[ BldID#blockId or #blockId]");
			}
			
			this.refBldId=items[0];
			this.refBlockId=items[1];
			
				
		}
		// 추후 block 의 id뿐만 아니라 다른 ID를 설정할 가능성이 있다.
		
		this.refId = refId;
	}
	
	/**
	 * 이 Field에 서 참조하는 block 인 경우 만약 외부 bld의 block을 참조하는 경우
	 * 해당 BLD 의 id 값을 return 한다.
	 * @return 참조 BLD id
	 */
	public String getRefBldId()
	{
		return this.refBldId;
	}
	
	/**
	 * 이 Field에서 참조하는 block이 있는 경우 해당 block id 값을 return 한다.
	 * @return 참조 block id
	 */
	public String getRefBlockId()
	{
		return this.refBlockId;
	}

	/**
	 * block id를 return 한다.
	 * 
	 * @return
	 */
	public String getRefId()
	{
		return this.refId;
	}

	/**
	 * field 명을 가져온다.
	 * 
	 * @return name file명
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * field type을 가져온다.
	 * 
	 * @return type field 타입
	 */
	public int getType()
	{
		return type;
	}

	public String getTypeName()
	{
		return this.typeName;
	}

	/**
	 * getTypeName(mode) 메쏘드에 사용되는 상수로 이 경우는 IO Schema(BLD) 를 의미한다.
	 * 
	 */
	public final static int IOSCHEMA = 0;

	/**
	 * getTypeName(mode) 메쏘드에 사용되는 상수로 이 경우는 XML Schema 의 type을 의미한다.
	 */
	public final static int XSD = 1;

	/**
	 * BLD에 정의된 type이 각각 XSD 와 BLD에서 어떤 이름으로 정의되어 있는지 반환
	 * 
	 * @param mode
	 * @return
	 */
	public String getTypeName(int mode)
	{
		switch (mode) {

		case IOSCHEMA:
			return getTypeName4IO();

		case XSD:
			return getTypeName4Xsd();
		}

		return "";
	}

	/**
	 * IO Schema의 type 명으로 return 한다.
	 * 
	 * @return
	 */
	private String getTypeName4IO()
	{
		switch (this.type) {

		case STRING:
		case STRING_NUMBER:
		case CHAR:
		case CLOB:
			return "string";

		case INTEGER:
			return "int";

		case LONG:
			return "long";

		case FLOAT:
			return "float";

		case DOUBLE:
			return "double";

		case BOOLEAN:
			return "boolean";

		case BLOB:
			return "blob";

		case BLOCK:
			return "block";

		case BYTE_ARRAY:
			return "binary";
		}

		return "undefined";
	}

	/**
	 * Xml Schema에서 사용하는 type명으로 return 한다.
	 * 
	 * 
	 * @return
	 */
	private String getTypeName4Xsd()
	{
		switch (this.type) {

		case STRING:
		case STRING_NUMBER:
		case CHAR:
		case CLOB:
			return "string";

		case INTEGER:
			return "int";

		case LONG:
			return "long";

		case FLOAT:
			return "float";

		case DOUBLE:
			return "double";

		case BOOLEAN:
			return "boolean";

		case BLOB:
		case BYTE_ARRAY:
			return "base64Binary";
		}

		return "undefined";
	}

	/**
	 * ioshema에서 정의된 문자형의 타입을 숫자 코드로 변환한다.
	 * 
	 * @param fieldType
	 * @return
	 */
	public static int getType(String fieldType)
	{
		if (fieldType == null || fieldType.length() == 0)
			return FieldType.UNDEFINED;

		fieldType = fieldType.toLowerCase();

		if ("string".equals(fieldType))
			return FieldType.STRING;

		else if ("int".equals(fieldType))
			return FieldType.INTEGER;

		else if ("long".equals(fieldType))
			return FieldType.LONG;

		else if ("float".equals(fieldType))
			return FieldType.FLOAT;

		else if ("double".equals(fieldType))
			return FieldType.DOUBLE;

		else if ("boolean".equals(fieldType))
			return FieldType.BOOLEAN;

		else if ("integer".equals(fieldType))
			return FieldType.INTEGER;

		else if ("blob".equals(fieldType))
			return FieldType.BLOB;

		else if ("clob".equals(fieldType))
			return FieldType.CLOB;

		else if ("block".equals(fieldType))
			return FieldType.BLOCK;

		else if ("binary".equals(fieldType))
			return FieldType.BYTE_ARRAY;

		else if ("stringnumber".equals(fieldType))
			return FieldType.STRING_NUMBER;

		return FieldType.UNDEFINED;
	}

	/**
	 * 변환하려는 type명
	 * 
	 * @return type field 타입
	 */
	public int getToType()
	{
		return toType;
	}

	/**
	 * field의 길이정보를 가져온다. 데이타 형태에 따라 무의미할 수 있다.
	 * 
	 * @return size 파싱을 위한 길이정보
	 */
	public int getSize()
	{
		return size;
	}

	private DateValue dateVal;

	private TimeValue timeVal;

	/**
	 * 기본값을 가져온다.
	 * 
	 * {$today} {$today-5day} {$today-5month} {$today-5year} {$now}
	 * 
	 * @return defaultValue 기본값
	 */
	public Object getDefaultValue()
	{
		if (dateVal != null)
			return dateVal.getValue();

		if (timeVal != null)
			return timeVal.getValue();

		if (defaultVal != null) {
			String def = defaultVal.toString();

			if (def.indexOf("${") == 0) {

				// 날짜 {$today} 에 대한 처리

				int i = def.indexOf("today");

				if (i > 0) {
					int idx = def.indexOf("year");

					if (idx > 0) {
						dateVal = new DateValue(Integer.parseInt(def.substring(i + 5, idx)), 0, 0);
						return dateVal.getValue();
					}

					idx = def.indexOf("month");
					if (idx > 0) {
						dateVal = new DateValue(0, Integer.parseInt(def.substring(i + 5, idx)), 0);
						return dateVal.getValue();
					}

					idx = def.indexOf("day", i + 5);
					if (idx > 0) {
						dateVal = new DateValue(0, 0, Integer.parseInt(def.substring(i + 5, idx)));
						return dateVal.getValue();
					}

					dateVal = new DateValue(0, 0, 0);
					return dateVal.getValue();
				}

				// 시간 {$now<150000} 에 대한 처리
				i = def.indexOf("now");

				if (i > 0) {
					int idx = def.indexOf("|");

					if (idx > 0) {
						// 시간만 가져온다.
						String maxTime = def.substring(i + 4, i + 10);

						timeVal = new TimeValue(maxTime);
						return timeVal.getValue();

					} else {
						timeVal = new TimeValue();
						return timeVal.getValue();
					}

				}

			}
		}

		return defaultVal;
	}

	/**
	 * format 정보
	 * 
	 * @return
	 */
	public String getFormat()
	{
		return format;
	}

	public Formatter getFormatter()
	{
		return this.formatter;
	}

	public int getDecimalPoint()
	{
		return decimalPoint;
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label
	 *            The label to set.
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	private String toStr;

	public String toString()
	{

		if (toStr == null) {
			StringBuffer buf = new StringBuffer();

			buf.append("<field name=\"").append(name).append("\" size=\"").append(size).append("\" type=\"").append(
					getTypeName(IOSCHEMA)).append("\"");

			if (this.label != null)
				buf.append(" label=\"").append(this.label).append("\"");

			if (defaultVal != null) {
				buf.append(" default=\"").append(defaultVal).append("\"");
			}

			if (format != null) {
				buf.append(" format=\"").append(format).append("\"");
			}

			buf.append("/>");

			toStr = buf.toString();
		}

		return toStr;
	}

	/**
	 * 날짜 정보를 담는 class
	 * 
	 * @author
	 * 
	 */
	public static class DateValue implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int year;

		private int month;

		private int day;

		private String format;

		public DateValue(int year, int month, int day, String format) {
			this.year = year;
			this.month = month;
			this.day = day;

			this.format = format;
		}

		public DateValue(int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;

			this.format = "yyyyMMdd";
		}

		public Object getValue()
		{
			return DateTime.getAdjustDateString(year, month, day, format);
		}
	}

	/**
	 * 시간정보를 담는 class
	 * 
	 * @author
	 * 
	 */
	public static class TimeValue implements Serializable
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int maxTime;

		private String maxStrTime;

		public TimeValue() {
			this(-1);
		}

		public TimeValue(String maxStrTime) {
			this.maxStrTime = maxStrTime;
			this.maxTime = Integer.parseInt(maxStrTime);
		}

		public TimeValue(int maxTime) {
			this.maxTime = maxTime;
			this.maxStrTime = String.valueOf(maxTime);
		}

		public Object getValue()
		{
			String time = DateTime.getString(new java.util.Date(), "HHmmss");

			if (maxTime > 0 && Integer.parseInt(time) > maxTime)
				return maxStrTime;

			return time;

		}
	}

	/**
	 * field 에 정의된 xpath 정보를 가져온다.
	 * 
	 * @return
	 */
	public String getXpathExpression()
	{
		return xpathExpression;
	}

	/**
	 * field에 정의된 xpath 정보를 설정
	 * 
	 * @param xpathExpression
	 */
	public void setXpathExpression(String xpathExpression)
	{
		if (xpathExpression != null && xpathExpression.length() == 0)
			return;
		this.xpathExpression = xpathExpression;
	}

	public void addParameter(String name, String value, String fieldName)
	{
		Parameter param = new Parameter(name, value, fieldName);
		this.parmeterList.add(param);
	}

	public int getParameterSize()
	{
		return this.parmeterList.size();
	}

	public Parameter getParameter(int idx)
	{
		return (Parameter) this.parmeterList.get(idx);
	}

	/**
	 * 
	 * @version 1.0, 2008. 01. 09
	 * @since 1.0
	 */
	public final static class Parameter
	{

		private String name;

		private String value;

		private String fieldName;

		public Parameter(String name, String value, String fieldName) {

			this.name = name;
			this.value = value;
			this.fieldName = fieldName;

		}

		public String getName()
		{
			return this.name;
		}

		public String getVal()
		{
			return value;
		}

		public String getFieldName()
		{
			return fieldName;
		}

	}

}