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

public class XSSFilter implements Filter 
{

	private final String LOG_ID = "<at:XSSFilter>";
	
	private String[] excludeDirs = null;
	
	private FilterConfig fc;
	public void destroy() {
		this.fc = null; 
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		//System.out.println(LOG_ID+"no-apply-xss"+">>>>>>>>>>>>>>"+req.getAttribute("no-apply-xss"));
		
		String contextPath = req.getContextPath();
		String urlS = req.getRequestURI();
		
		if (isExclude(urlS, contextPath))
		{	
			Logger.info.println(LOG_ID+urlS+" is XSSFilter will not apply.");
			chain.doFilter(request, response);			
			return;
		}
		
		
		chain.doFilter(new com.kabm.filter.RequestWrapper(req), response);

	}
	
	
	public void setFilterConfig(FilterConfig filterConfig)
			throws ServletException {
		init(filterConfig);
	}
	
	public String getParameter(FilterConfig filterConf, String name) {
		String val = filterConf.getInitParameter(name);
		return val;
	}
	
	public void init(FilterConfig filterConf) throws ServletException
	{
		String param = getParameter(filterConf, "exclude-dir");
		
		if (param == null || param.length() == 0)
			excludeDirs = new String[] {};
		else
			excludeDirs = SmartStringArray.split(",", param);
	}
	
	private boolean isExclude(String uri, String contextPath) {
		for (int i = 0; i < this.excludeDirs.length; i++) {
			if (uri.indexOf(contextPath + excludeDirs[i]) == 0)
				return true;
		}

		return false;

	}
}