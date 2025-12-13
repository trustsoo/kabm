package jdf.framework.core.http;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Applicatio 영역(ServletContext)에 값을 읽거나 넣는다.
 *
 * </p>
 *
 * @author 
 * @version 1.0
 */

public final class ApplicationAttributes extends HttpAttributes
{

	protected ServletContext context;

	private ApplicationAttributes()
	{
	}

	public ApplicationAttributes(ServletContext context)
	{
		super();

		this.context = context;

		Enumeration fieldNames = context.getAttributeNames();

		while (fieldNames.hasMoreElements())
		{
			String paramName = (String) fieldNames.nextElement();
			Object paramValue = context.getAttribute(paramName);
			_attrs.put(paramName, paramValue);
		}

	}

	public void setAttribute(String key, Object obj)
	{
		_attrs.put(key, obj);
		context.setAttribute(key, obj);

	}

	public Object getAttribute(String key)
	{
		//return _attrs.get(fieldName);
		return context.getAttribute(key);
	}

	public HttpSession getHttpSession()
	{
		return null;
	}

	public HttpAttributes getSessionAttributes()
	{
		return null;
	}

}