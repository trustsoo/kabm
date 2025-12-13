package com.kabm.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
 
public class CORSFilter implements Filter {
	private String[] includeDirs = null;
	
	private FilterConfig fc;
	public void destroy() {
		this.fc = null; 
	}
	
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
        
        String contextPath = request.getContextPath();
		String urlS = request.getRequestURI();
		
        if (isInclude(urlS, contextPath))
		{	
        	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Session, X-UserAgent-jdf");        
            response.setHeader("Access-Control-Allow-Origin", "*");   
            Logger.info.println( "CORS ACCEPT:"+ urlS );
		}
        
        chain.doFilter(req, response);
    }
    public void init(FilterConfig filterConf) throws ServletException
	{
		String param = getParameter(filterConf, "include-dir");
		
		if (param == null || param.length() == 0)
			includeDirs = new String[] {};
		else
			includeDirs = SmartStringArray.split(",", param);
	}
    
    public void setFilterConfig(FilterConfig filterConfig)
			throws ServletException {
		init(filterConfig);
	}
    public String getParameter(FilterConfig filterConf, String name) {
		String val = filterConf.getInitParameter(name);
		return val;
	}
    private boolean isInclude(String uri, String contextPath) {
		for (int i = 0; i < this.includeDirs.length; i++) {
			if (uri.indexOf(contextPath + includeDirs[i]) == 0)
				return true;
		}

		return false;

	}
}