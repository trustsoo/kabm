package jdf.framework.core.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * <p>
 * Session에 값을 읽거나 넣는다.
 *
 * </p>
 *
 * @author
 * @version 1.0
 * @see RequestAttributes
 */

public final class SessionAttributes extends HttpAttributes
{

	protected HttpSession session;

	private SessionAttributes()
	{
	}

	public SessionAttributes(HttpServletRequest request)
	{
		this(request.getSession(true));
	}

	public SessionAttributes(HttpSession session)
	{
		super();

		this.session = session;

		Enumeration fieldNames = session.getAttributeNames();

		while (fieldNames.hasMoreElements())
		{
			String paramName = (String) fieldNames.nextElement();
			Object paramValue = session.getAttribute(paramName);
			_attrs.put(paramName, paramValue);
		}

		_attrs.put("javax.servlet.http.HttpSession", session);

	}

	public void setAttribute(String key, Object obj)
	{
		_attrs.put(key, obj);
		session.setAttribute(key, obj);

	}

	public Object getAttribute(String key)
	{
		//return _attrs.get(fieldName);
		return session.getAttribute(key);
	}

	public HttpSession getHttpSession()
	{
		return session;
	}

	public HttpAttributes getSessionAttributes()
	{
		return this;
	}

}
