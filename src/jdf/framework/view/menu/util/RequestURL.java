package jdf.framework.view.menu.util;

import javax.servlet.http.HttpServletRequest;

/**
 * J2EE 1.2 와 1.3 환경 모두 URL 정보를 가져오기 위해 만들었음.
 * 
 * 
 * @author
 *
 */
public class RequestURL
{
    private static boolean isJ2EE13 = true;

    
    /**
     * query string을 제외한 url
     * 
     * @param request
     * @return
     */
    public static StringBuffer getUrl(HttpServletRequest request)
    {
        if(isJ2EE13)
        {
            try {
                return request.getRequestURL();
            }catch(Throwable e) {
                isJ2EE13=false;
            }
        }
        
        return javax.servlet.http.HttpUtils.getRequestURL(request);
        
    }
    
    /**
     * parameter 를 포함한 url
     * 
     * @param request
     * @return
     */
    public static StringBuffer getFullUrl(HttpServletRequest request)
    {
    	StringBuffer url = getUrl(request);
    	if (request.getQueryString() != null) {
			url.append('?');
			url.append(request.getQueryString());
		}
    	
    	return url;
    }
}