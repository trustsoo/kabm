/*
 * Created on 2004-05-17
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface AnyLogicControl
{
	
	public void init(ServletConfig config) throws ServletException;
	
	
	
	/**
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;
	
	

}