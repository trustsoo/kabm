/*
 * Created on 2004-05-18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.servlet;

import jdf.framework.core.pool.cache.CacheManagerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CacheManagement extends HttpServlet implements AnyLogicControl
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private CacheManagerFactory cft;
	/**
	 * 스키마 URI를 Configration으로 부터 가져온다.
	 * @param config ServletConfig
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		//cft = CacheManagerFactory.getInstance();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
	    
	    
	    String method = req.getParameter("method");
	    String val = req.getParameter("value");
	    
	    
	    // 현재는 method는 clear value 는 ALL 또는 tr 명이 된다.
	    
	    if("clear".equals(method))
	    {
	        if("ALL".equals(val))
	        {
	            CacheManagerFactory.getInstance().clearCache();
	        }
	        else
	            CacheManagerFactory.getInstance().clearCache(val);
	    }
	    
	   
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
	    doGet(req, res);
	}

}