package jdf.framework.core.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Cookie에 값을 읽고 쓰기위한 Utility성 Class
 * </p>
 * setDomain을 알아서 해준다.
 *
 * @author
 * @version 1.0
 */

public final class SmartCookie extends HttpAttributes
{

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String domainName;

	public static final int BROWSER_LIVE = -1;

	private SmartCookie()
	{
	}

	public SmartCookie(HttpServletRequest req, HttpServletResponse res)
	{
		super();
		request = req;
		response = res;
		domainName = getDomainName(req);

		Cookie[] cookies = req.getCookies();
		if (cookies != null)
		{

			for (int i = 0; cookies != null && i < cookies.length; i++)
			{
				String key = cookies[i].getName();
				String value = cookies[i].getValue();

				if (value == null)
					value = "";

				_attrs.put(key, value);
			}
		}
	}

	/**
	 * request로 부터 서버 도메인 명을 구한다.
	 * cookie의 setDomain을 자동으로 하기위해...
	 * null이 return되는 경우는 localhost, ip인 경우다.
	 * 실제 프로젝트 production 단계에서는 바로 도메인 명을 리턴하게 하여도 된다.
	 */
	private String getDomainName(HttpServletRequest req)
	{
		String serverName = req.getServerName();

		int firstPoint = serverName.indexOf(".");
		if (firstPoint < 0)
			return null; //localhost또는 컴퓨명인경우 null

		try
		{
			Integer.parseInt(serverName.substring(serverName.lastIndexOf(".") + 1, serverName.length()));
		}
		catch (NumberFormatException nex)
		{
			return serverName;
		}
		return null;
	}

	public void add(String key, String value)
	{
		add(key, value, BROWSER_LIVE);
	}

	public void add(String key, String value, int age)
	{
		if (key != null && value != null)
		{
			Cookie cookie = new Cookie(key, value);

			cookie.setMaxAge(age);

			if (domainName != null)
				cookie.setDomain(domainName);
			cookie.setPath("/");

			response.addCookie(cookie); // must call addCookie method before calling getWriter
		}
	}

	public void setAttribute(String key, Object obj)
	{
		_attrs.put(key, obj);
	}

	public Object getAttribute(String key)
	{
		return _attrs.get(key);
	}

	private HttpSession session;

	public HttpSession getHttpSession()
	{
		if (session == null)
			session = request.getSession(true);

		return session;
	}

	private SessionAttributes sessionAttr;

	public HttpAttributes getSessionAttributes()
	{
		if (sessionAttr == null)
			sessionAttr = new SessionAttributes(getHttpSession());

		return sessionAttr;
	}

}
