package jdf.framework.view.control;

import jdf.framework.core.http.HttpAttributes;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * SqlServlet 실행시 Command
 * 
 * @author
 */
public abstract class Command {
	private ServletContext ctx;

	private ServletConfig config;

	private HttpAttributes attr;

	/**
	 * ServletContext를 세팅한다
	 * 
	 * 
	 * @param ctx
	 *            context
	 */
	protected void setServletContext(ServletContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * 
	 * ServletConfig 를 세팅한다.
	 * 
	 * @param config
	 *            Config
	 */
	protected void setServletConfig(ServletConfig config) {
		this.config = config;
	}

	/**
	 * HttoAttributes를 세팅한다.
	 * 
	 * 
	 * @param attr
	 *            HttpAttribute
	 */
	protected void setHttpAttributes(HttpAttributes attr) {
		this.attr = attr;
	}

	/**
	 * ServletContext를 얻어온다
	 * 
	 * 
	 * @return ServletContext
	 */
	protected ServletContext getServletContext() {
		return this.ctx;
	}

	/**
	 * ServletConfig를 얻어온다
	 * 
	 * 
	 * @return ServletContext
	 */
	protected ServletConfig getServletConfig() {
		return this.config;
	}

	/**
	 * HttpAttributes를 얻어온다.
	 * 
	 * 
	 * @return ServletContext
	 */
	protected HttpAttributes getHttpAttributes() {
		return this.attr;
	}

	/**
	 * SqlServlet에서 실제 query를 수행하기 전에 수행되어야 하는 작업을 구현한다.
	 * 
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws Exception
	 *             Exception
	 */
	public abstract void preTask(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * SqlServlet에서 실제 query를 수행한후 수행되어야 하는 작업을 구현한다.
	 * 
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws Exception
	 *             Exception
	 */
	public abstract void postTask(HttpServletRequest request, HttpServletResponse response) throws Exception;

}