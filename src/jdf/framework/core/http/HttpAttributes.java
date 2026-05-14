/*
 * @(#)HttpAttributes.java
 *
 *
 * NOTICE !
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice
 * and the author.
 *
 * @author
 */

package jdf.framework.core.http;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.http.multipart.MemoryBasedMultipartProcessor;
import jdf.framework.core.http.multipart.MultipartProcessor;
import jdf.framework.core.util.BeanUtil;
import jdf.framework.core.util.SmartStringArray;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Properties;


/**
 * <b><code>HttpAttributes</code></b>
 * <p>
 * Attribute들이 기본적으로 가져야 할 기능을 구현하고, 추상화한 Class 각 영역(APPLICATION, SESSION, REQUEST)마다 setAttribute()와 getAttribute() 를
 * 구현해야 한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */

public abstract class HttpAttributes
{
	public final static String SAVE_PATH = "http.attribute.save.path";

	public final static String LIMIT_SIZE = "http.attribute.limit.size";

	public final static String ENCODING = "http.attribute.encoding";

	public final static String MULTIPART_PROCESSOR = "http.attribute.multipart.processor";

	/**
	 * 기본적인 저장소로 Map Interface를 구현한 객체(HashMap,Hashtable등)를 쓴다.
	 */
	// protected Map _attrs;
	protected DataSet _attrs;

	protected Properties props = new Properties();

	/**
	 * 기본적인 생성자
	 */
	protected HttpAttributes() {
		// _attrs = new HashMap(); //여기서는 기본적으로 HashMap()을 쓴다. 속도때문에...
		_attrs = new DataSet();
		// _attrs.unfixNull();
	}

	/**
	 * 속성값 설정
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value)
	{
		this.props.setProperty(key, value);

	}

	/**
	 * 속성값 설정
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, int value)
	{
		this.props.setProperty(key, String.valueOf(value));

	}

	/**
	 * 속성값 가져오기:string
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key)
	{
		return this.props.getProperty(key);
	}

	/**
	 * 속성값 int 형으로 가져오기
	 * 
	 * @param key
	 * @return
	 */
	public int getPropertyInt(String key)
	{
		String val = getProperty(key);
		try {
			return Integer.parseInt(val);

		} catch (NullPointerException ne) {

		} catch (NumberFormatException nfe) {

		}

		return 0;

	}

	/**
	 * 자동으로 HttpAttributes 구현객체를 생성한다.
	 * 
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static HttpAttributes getInstance(HttpServletRequest req) 
	{

		String contentType = req.getContentType();

		if (contentType != null && contentType.toLowerCase().startsWith("multipart"))
		{
			//return new UpgradeMultipartAttributes(req);
			
			try
			{
			 return new MultipartAttributes(req); // Multipart용
			} catch(Exception ex)
			{
				return null;
			}
		}
			

		else
			return new RequestAttributes(req);

	}

	/**
	 * 
	 * Cookie의 값을 HttpAttribute 에 넣어서 return 한다.
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static HttpAttributes getCookieInstance(HttpServletRequest req)
	{

		Cookie[] cookies = req.getCookies();
		return new CookieAttributes(cookies);

	}

	public static HttpAttributes getInstance(HttpSession session)
	{
		return new SessionAttributes(session);
	}

	public DataSet getDataSet()
	{
		// _attrs.fixNull();
		return _attrs;
	}

	/**
	 * <p>
	 * request에 해당필드가 값을 가지고 있는지 체크한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 String type값, 실패시는 default 값
	 */
	public boolean isSetValue(String fieldName)
	{
		try {
			if (getString(fieldName).length() > 0)
				return true;
		} catch (Exception ex) {
		}

		return false;

	}

	/**
	 * <p>
	 * request에 해당 필드가 존재하는지 여부를 체크한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 String type값, 실패시는 default 값
	 */
	public boolean isFieldExist(String fieldName)
	{
		return _attrs.containsKey(fieldName);
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 문자열 배열(String[])로 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 문자값
	 */
	public String[] getStringArray(String fieldName)
	{
		String x = getString(fieldName);
		if (getCount(fieldName) == 1 && "".equals(x))
			return new String[] { "" };
		else
			return SmartStringArray.split(",", getString(fieldName));
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 숫자(int)로 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 int값
	 */
	public int getInt(String fieldName)
	{
		return getInt(fieldName, 0);
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 숫자(long)로 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 long type 값
	 */
	public long getLong(String fieldName)
	{
		return getLong(fieldName, 0);
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 숫자(float)로 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 float type값
	 */
	public float getFloat(String fieldName)
	{
		return getFloat(fieldName, 0);
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 boolean형태로 반환한다. 값이 없는 경우 default 값으로 정의해 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 boolean type값, 실패시는 default 값
	 */
	public boolean getBoolean(String fieldName)
	{
		return getBoolean(fieldName, false);
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 boolean으로 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 boolean type 값
	 */
	public boolean getBoolean(String fieldName, boolean defaultVal)
	{
		try {
			return Boolean.valueOf(getString(fieldName)).booleanValue();
			// return new Boolean(getString(fieldName)).booleanValue();

			// return (new Boolean( (String) _attrs.get(fieldName) )).booleanValue();
		} catch (Exception e) {

			return defaultVal;
		}
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 문자열(String)로 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 문자값
	 */
	public String getString(String fieldName)
	{
		// 값의 길이가 1이상인 경우는 묶어서 값을 준다.
		int len = getCount(fieldName);
		if (len > 1) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < len; i++) {
				if (i != 0)
					buf.append(",");

				buf.append(getString(fieldName, "", i));
			}

			return buf.toString();

		}

		return getString(fieldName, "", 0);
	}

	public String getString(String fieldName, int depth)
	{
		return getString(fieldName, "", depth);
	}

	public String getString(String fieldName, String defaultVal)
	{
		return getString(fieldName, defaultVal, 0);
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 문자열로 반환한다. 값이 없는 경우 default 값으로 정의해 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 String type값, 실패시는 default 값
	 */
	public String getString(String fieldName, String defaultVal, int depth)
	{
		_attrs.unfixNull();
		Object val = _attrs.get(fieldName, depth);
		_attrs.fixNull();

		if (val == null)
			return defaultVal;

		// return String.valueOf( val );
		return val.toString();
	}

	public int getCount(String fieldName)
	{
		return _attrs.getCount(fieldName);
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 숫자값으로 반환한다. 값이 없는 경우 default 값으로 정의해 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 int type값, 실패시는 default 값
	 */
	public int getInt(String fieldName, int defaultVal)
	{
		try {
			return Integer.parseInt(getString(fieldName));
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 숫자값으로 반환한다. 값이 없는 경우 default 값으로 정의해 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 long type값, 실패시는 default 값
	 */
	public long getLong(String fieldName, long defaultVal)
	{
		try {
			return Long.parseLong(getString(fieldName));
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 숫자값으로 반환한다. 값이 없는 경우 default 값으로 정의해 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 long type값, 실패시는 default 값
	 */
	public float getFloat(String fieldName, long defaultVal)
	{
		try {
			return (Float.valueOf(getString(fieldName))).floatValue();
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 숫자(double)로 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 double type 값
	 */
	public double getDouble(String fieldName)
	{
		return getDouble(fieldName, 0);
	}

	/**
	 * <p>
	 * request에 의해 전달되어온 해당 필드명을 값을 숫자값으로 반환한다. 값이 없는 경우 default 값으로 정의해 반환한다.
	 * </p>
	 * 
	 * @param field명
	 *            request로 넘어오는 필드명
	 * @return String field명에 해당하는 double type값, 실패시는 default 값
	 */
	public double getDouble(String fieldName, double defaultVal)
	{

		try {
			return Double.valueOf(getString(fieldName)).doubleValue();
		} catch (Exception e) {
			return defaultVal;
		}

	}

	/**
	 * 
	 * @param entity
	 *            java.lang.Object
	 */
	public void copy(Object entity)
	{
		if (entity == null)
			throw new NullPointerException("trying to copy from box to null entity class");

		Class c = entity.getClass();
		java.lang.reflect.Field[] field = c.getFields();

		if (field.length == 0) // bean인 경우
		{
			copyBean(entity);
			return;
		}

		for (int i = 0; i < field.length; i++) {
			try {
				Class fieldtype = field[i].getType();
				String fieldname = field[i].getName();

				// System.out.println("? == "+fieldname);

				if (_attrs.containsKey(fieldname)) {
					if (fieldtype == String.class) {
						field[i].set(entity, getString(fieldname));
					} else if (fieldtype == Integer.TYPE) {
						field[i].setInt(entity, getInt(fieldname));
					} else if (fieldtype == Double.TYPE) {
						field[i].setDouble(entity, getDouble(fieldname));
					} else if (fieldtype == Long.TYPE) {
						field[i].setLong(entity, getLong(fieldname));
					} else if (fieldtype == Float.TYPE) {
						field[i].setFloat(entity, getFloat(fieldname));
					}

					else if (fieldtype == Boolean.TYPE) {
						field[i].setBoolean(entity, getBoolean(fieldname));
					}

				}
			} catch (Exception e) {
				// Debug.warn.println(_attrs, e.getMessage());
			}
		}
	}

	private void copyBean(Object bean)
	{

		Method[] me = BeanUtil.getSetMethods(bean.getClass());
		String propertyName;

		for (int j = 0; j < me.length; j++) {

			try {
				propertyName = methodNameToPropertyName(me[j].getName());
				Class typeName = me[j].getParameterTypes()[0]; // class type명

				// System.out.println( "p="+propertyName);
				// System.out.println( "t="+typeName);

				if (typeName == String.class)
					me[j].invoke(bean, new Object[] { getString(propertyName) });

				else if (typeName == Integer.TYPE)
					me[j].invoke(bean, new Object[] { new Integer(getInt(propertyName)) });

				else if (typeName == Long.TYPE)
					me[j].invoke(bean, new Object[] { new Long(getLong(propertyName)) });

				else if (typeName == Float.TYPE)
					me[j].invoke(bean, new Object[] { new Float(getFloat(propertyName)) });

				else if (typeName == Double.TYPE)
					me[j].invoke(bean, new Object[] { new Double(getDouble(propertyName)) });

				else if (typeName == Boolean.TYPE)
					me[j].invoke(bean, new Object[] { Boolean.valueOf(getBoolean(propertyName)) });

			}
			/*
			 * catch(IllegalArgumentException iiae) { }
			 */
			catch (Exception e) {
				System.err.println(e);
				// e.printStackTrace();
			}
		}
	}

	private static String methodNameToPropertyName(String methodName)
	{
		if (methodName.startsWith("get"))
			methodName = methodName.substring(3);
		else if (methodName.startsWith("set"))
			methodName = methodName.substring(3);
		else if (methodName.startsWith("is"))
			methodName = methodName.substring(2);
		else
			return null;

		if (methodName.length() == 0)
			return null;

		char ch = methodName.charAt(0);
		if (Character.isUpperCase(ch) && (methodName.length() == 1 || Character.isLowerCase(methodName.charAt(1)))) {
			methodName = Character.toLowerCase(ch) + methodName.substring(1);
		}

		return methodName;
	}

	/**
	 * <p>
	 * client에게 전달되어온 request에서 모든 변수,값 형태의 쌍으로 된 문자열을 return 한다.
	 * </p>
	 * 
	 * @return String request로 넘어온 변수,값 형태의 쌍으로 return한다.
	 */
	public synchronized String toString()
	{

		StringBuffer buf = new StringBuffer();

		Iterator keys = _attrs.keySet().iterator();

		buf.append("{");

		while (keys.hasNext()) {
			String key = (String) keys.next();

			String value = null;
			Object o = _attrs.get(key);

			if (o == null)
				value = "";
			else {
				Class c = o.getClass();
				if (c.isArray()) {
					int length = Array.getLength(o);

					if (length == 0)
						value = "";

					else if (length == 1) {
						Object item = Array.get(o, 0);
						if (item == null)
							value = "";
						else
							value = item.toString();
					} else {
						StringBuffer valueBuf = new StringBuffer();
						valueBuf.append("[");

						for (int j = 0; j < length; j++) {
							Object item = Array.get(o, j);
							if (item != null)
								valueBuf.append(item.toString());
							if (j < length - 1)
								valueBuf.append(",");
						}

						valueBuf.append("]");
						value = valueBuf.toString();
					}
				} else
					value = o.toString();
			}

			buf.append(key + "=" + value);
			if (keys.hasNext())
				buf.append(", ");
		}
		buf.append("}");

		return "Attributes=" + buf.toString();

	}

	/**
	 * Multipart전용 메쏘드 화일의 type을 가져온다. 나머지 경우에는 전부 일반 text를 뜻하는 "text/html"이다.
	 */
	public String getContentType(String fieldName)
	{
		return getContentType(fieldName, 0);
	}

	/**
	 * Multipart전용 메쏘드 화일의 type을 가져온다. 나머지 경우에는 전부 일반 text를 뜻하는 "text/html"이다.
	 */
	public String getContentType(String fieldName, int seq)
	{
		return "text/html";
	}

	public InputStream getInputStream(String fieldName) throws IOException
	{
		return getInputStream(fieldName, 0);
	}

	/**
	 * 해당 file 필드에 대해 InputStream을 얻는다. Multipart에서 화일을 처리하기위한 목적이다.
	 */
	public InputStream getInputStream(String fieldName, int seq)
	{
		try {
			return new ByteArrayInputStream(getString(fieldName, seq).getBytes());
		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * fieldName명에 해당하는 값을 byte Array로 return
	 * 
	 * Multipart인 경우는 upload file의 byte array를 return
	 * 
	 * @param fieldName
	 * @return
	 */
	public byte[] getByteArray(String fieldName)
	{
		return getByteArray(fieldName, 0);
	}

	/**
	 * fieldName명에 해당하는 값을 byte Array로 return
	 * 
	 * Multipart인 경우는 upload file의 byte array를 return
	 * 
	 * @param fieldName
	 * @return
	 */
	public byte[] getByteArray(String fieldName, int seq)
	{
		String val = getString(fieldName, seq);

		if (val == null)
			return null;

		return val.getBytes();
	}

	public abstract HttpSession getHttpSession();

	public abstract HttpAttributes getSessionAttributes();

	/**
	 * attribute를 해당 영역에 저장한다.
	 * 
	 * @param name
	 *            attribute 이름
	 * @param o
	 *            저장하고자 하는 객체
	 */
	public abstract void setAttribute(String key, Object obj);

	/**
	 * 해당영역에서 attribute의 값을 가져온다.
	 * 
	 * @param name
	 *            attribute명
	 * @return Object 해당 영역에서 유지되는 값을 전달한다.
	 */
	public abstract Object getAttribute(String key);

	/**
	 * 
	 * @param obj
	 * @throws IOException
	 */
	public void process(Object obj) throws IOException
	{

	}

	/**
	 * MultipartProcessor
	 * 
	 */
	protected MultipartProcessor multipartProcess = new MemoryBasedMultipartProcessor();

	/**
	 * multipart 처리시 좀더 효율적으로 처리하기 위한 processor 를 사용자가 작성해서 설정할 수 있다.
	 * 이것을 설정하지 않으시 메모리 기반으로 multipart 데이터를 처리한다.
	 * 
	 * 메모리 기반은 대용량 데이터 처리시 문제가 발생할 수 있다.
	 * 
	 * @param processor
	 */
	public void bindMultipartProcessor(MultipartProcessor processor)
	{
		this.multipartProcess = processor;

	}

}