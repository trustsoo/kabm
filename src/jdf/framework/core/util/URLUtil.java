package jdf.framework.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * URL ��� Utility
 * 
 * @author
 *
 */
public class URLUtil
{

	/**
	 * url�� encoding �Ѵ�.
	 * 
	 * @param q
	 * @return
	 */
	public static String encode(String q)
	{
		try {
			return URLEncoder.encode(q, "euc-kr");
		} catch (Throwable e) {
			return URLEncoder.encode(q);
		}
	}
	
	
	/**
	 * 
	 * @param q
	 * @param encode
	 * @return
	 */
	public static String encode(String q, String encode)
	{
		try {
			return URLEncoder.encode(q, encode);
		} catch (Throwable e) {
			return URLEncoder.encode(q);
		}
	}
	
	
	/**
	 * 
	 * 
	 * @param q
	 * @return
	 */
	public static String decode(String q)
	{
		try {
			return URLDecoder.decode(q, "euc-kr");
		} catch (Throwable e) {
			return URLDecoder.decode(q);
		}
		
	}
	
	public static Map<String, List<String>> getQueryMap(String url) throws UnsupportedEncodingException
	{
		Map<String, List<String>> params = new HashMap<String, List<String>>();
	    String[] urlParts = url.split("\\?");
	    if (urlParts.length > 1) {
	        String query = urlParts[1];
	        for (String param : query.split("&")) {
	            String pair[] = param.split("=");
	            String key = URLDecoder.decode(pair[0], "UTF-8");
	            String value = "";
	            if (pair.length > 1) {
	                value = URLDecoder.decode(pair[1], "UTF-8");
	            }
	            List<String> values = params.get(key);
	            if (values == null) {
	                values = new ArrayList<String>();
	                params.put(key, values);
	            }
	            values.add(value);
	        }
	    }
	    return params;
	}

}
