package jdf.framework.core.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;


/**
 * CookieAttributes
 * 
 * @author
 *
 */
public class CookieAttributes extends HttpAttributes
{
    
    public CookieAttributes(Cookie[] cookies)
    {
        super();
        
        for(int i=0;cookies!=null && i<cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
        
            _attrs.add(name, value);
        }
        
        
    }
    
    
    public HttpSession getHttpSession()
    {
        return null;
    }

    public HttpAttributes getSessionAttributes()
    {
        return null;
    }

    public void setAttribute(String key, Object obj)
    {
        _attrs.put(key, obj);

    }

    public Object getAttribute(String key)
    {
        return _attrs.get(key);
    }

}
