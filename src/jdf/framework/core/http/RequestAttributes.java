package jdf.framework.core.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * <b><code>HttpRequestEntity</code></b>
 * <p>
 * <code>HttpServletRequest</code> 객체를 parsing하여 POST/GET 방식으로 넘어오는 값을 쉽게 읽기위한
 * Class이다.
 * 
 * </p>
 * 
 * @author
 * @version 1.0
 * @see SessionAttributes
 */

public class RequestAttributes extends HttpAttributes {

	private HttpServletRequest request;

	private SessionAttributes sessionAttr;

	private HttpSession session;

	RequestAttributes() {
		super();
	}

	/**
	 * 기본생성자
	 * 
	 * @param req
	 *            HttpServletRequest
	 */
	public RequestAttributes(HttpServletRequest req) {
		super();

		request = req;

		Enumeration fieldNames = req.getParameterNames();

		while (fieldNames.hasMoreElements()) {
			String paramName = (String) fieldNames.nextElement();
			// String paramValue = "";

			String[] values = req.getParameterValues(paramName);
			if (values == null)
				continue;

			for (int i = 0; i < values.length; i++) {
				// if (i !=0 ) paramValue += ",";
				Object paramValue = req.getParameterValues(paramName)[i];

				// System.out.println(i+" "+paramName+":"+paramValue);
				_attrs.put(paramName, paramValue, i);
			}
		}

		// _attrs.put("javax.servlet.http.HttpServletRequest", req);
	}

	/**
	 * 속성값 설정
	 */
	public void setAttribute(String key, Object obj) {
		request.setAttribute(key, obj);
		try {
			_attrs.put(key, obj);
		} catch (Error e) {
		}

	}

	public Object getAttribute(String key) {
		return request.getAttribute(key);
	}

	/**
	 * HttpSession 을 얻는다.
	 * 
	 */
	public HttpSession getHttpSession() {
		if (session == null)
			session = request.getSession(true);

		return session;
	}

	
	/**
	 * session 처리용 HttpAttributes 를 얻는다.
	 */
	public HttpAttributes getSessionAttributes() {
		if (sessionAttr == null)
			sessionAttr = new SessionAttributes(getHttpSession());

		return sessionAttr;
	}

}
