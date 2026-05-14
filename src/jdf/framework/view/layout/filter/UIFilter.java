package jdf.framework.view.layout.filter;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.http.JDFrameContextListener;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.view.auth.RedirectPermissionException;
import jdf.framework.view.auth.User;
import jdf.framework.view.control.Command;
import jdf.framework.view.layout.LayoutManager;
import jdf.framework.view.layout.entity.Layout;
import jdf.framework.view.menu.MenuContext;
import jdf.framework.view.menu.bean.MenuMgrBean;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.entity.WebSiteMenu;
import jdf.framework.view.xslt.TransformerHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * WAS로 요청한 페이지에 UI를 입혀주는 Filter
 * 
 * 기본적으로 *.jsp와 *.htm 에 이 Filter가 반응하게 web.xml을 설정하여 준다.
 * 
 * url상의 명령
 * 
 * templet={templet이름} 화면의 templet을 결정한다. 이옵션이 없을시 sitemap.xml에 정의된 templet을
 * 사용한다.
 * 
 * bypass templet을 적용하지 않을경우
 * 
 * menu-reload sitemap.xml에서 메뉴를 다시 읽는다.
 * 
 * style-reload xsl 을 다시 읽는다.
 * 
 * 
 * 페이지 내에서 설정가능한 항목
 * 
 * 
 * request.setAttribute("bypass","true"); //templet을 사용하지 않는 경우
 * request.setAttribute("xsl","/xsl/content.xsl"); // xsl 화일을 정의하는 경우
 * request.setAttribute("templet","default"); // templet 명을 정의하는 경
 * 
 * 
 * @author
 */
public class UIFilter implements Filter, MenuContext {
	private final static String LOG_ID = "<t:UIFilter> ";

	private final static String KEY_TEMPLET = "templet";

	private final static String KEY_XSL = "xsl";

	private final static String KEY_TEMPLET_BYPASS = "templet-bypass";

	private final static String KEY_TEMPLET_BYPASS_INNER = "templet-bypass-inner";

	private final static String CHECKED = "checked";

	private String defaultCharEncoding = Configuration.getTargetEncoding();

	private String filterCharEncoding = Configuration.getTargetEncoding();

	private FilterConfig filterConfig;

	private LayoutManager layoutMgr;

	private ServletContext ctx;

	// HttpSession 에 사용되는 사용자 ID의 key 이름
	private String userIdSessionName = null;

	// templet적용을 배제하는 디렉토리
	private String[] excludeDirs;

	private User user;

	private String err_dispath_page;

	// compact-html
	boolean compactHtml = false;

	void printReqInfo(HttpServletRequest req) {
		System.out.println("getContextPath " + req.getContextPath());
		System.out.println("getServletPath " + req.getServletPath());
		System.out.println("getPathInfo " + req.getPathInfo());
		System.out.println("getRequestURI " + req.getRequestURI());
		System.out.println("getRequestURL "
				+ jdf.framework.view.menu.util.RequestURL.getUrl(req));

		System.out.println("toString "
				+ req.getAttribute("javax.servlet.include.path_info"));
	}

	/**
	 * WebLogic 6.1용
	 * 
	 * 
	 * 
	 * @param filterConfig
	 * @return
	 * @throws ServletException
	 */
	public void setFilterConfig(FilterConfig filterConfig)
			throws ServletException {
		init(filterConfig);
	}

	/**
	 * 
	 * FilterConfig 에서 값을 찾고 없으면, config.xml에서 값을 찾는다.
	 * 
	 * @param filterConfig
	 * @param name
	 * @return
	 */
	public String getParameter(FilterConfig filterConf, String name) {
		String val = filterConf.getInitParameter(name);

		if (val == null || val.length() == 0) {
			try {

				Config conf = Configuration.lookup("/resource/anytemplet");
				val = conf.getString(name);
			} catch (Exception e) {
			}
		}

		return val;
	}

	private void initCommand(FilterConfig filterConf) {
		String cmdClassNm = getParameter(filterConf, "command");

		if (cmdClassNm == null) {
			Logger.info.println(LOG_ID + "Command 구현객체 미지정");
			return;
		}

		try {
			commonCmd = (Command) Class.forName(cmdClassNm).newInstance();
			Logger.info.println(LOG_ID + "Command 구현객체:" + cmdClassNm);
		} catch (Exception e) {
			Logger.err.println(LOG_ID + "Command 구현객체 초기화에러", e);
		}
	}

	/**
	 * Filter의 초기화 작업을 한다.
	 * 
	 * 
	 */
	public void init(FilterConfig filterConf) throws ServletException {

		System.out
				.println("*******************************************************************************");
		System.out.println("anyTEMPLET Filter init.");

		JDFrameContextListener.setJdfFrameConfigPath(filterConf
				.getServletContext());

		// setAnyframeConfigPath 전에 로그를 쓰지 말자.
		this.filterConfig = filterConf;

		// startup 은 ContextListener 를 통해 구현하길...
		// initStartupClass(filterConf);

		ctx = filterConf.getServletContext();

		String charEncoding = getParameter(filterConf,
				"request-characterEncoding");
		if (charEncoding != null && charEncoding.length() > 0)
			this.defaultCharEncoding = charEncoding;
		else if ("bypass".equals(charEncoding))
			this.defaultCharEncoding = null;

		String filterEncoding = getParameter(filterConf,
				"filter-characterEncoding");
		if (filterEncoding != null && filterEncoding.length() > 0)
			this.filterCharEncoding = filterEncoding;

		// filter 제외 디렉토리 설정
		String param = getParameter(filterConf, "exclude-dir");
		Logger.info.println(LOG_ID + "exclude-dir:" + param);

		if (param == null || param.length() == 0)
			excludeDirs = new String[] {};
		else
			excludeDirs = SmartStringArray.split(",", param);

		// 명령 Command 객체 초기화
		initCommand(filterConf);

		this.userIdSessionName = getParameter(filterConf, "userid-session-name");

		// 공백이면 null 로 처리한다.
		if (this.userIdSessionName != null
				&& this.userIdSessionName.length() == 0)
			this.userIdSessionName = null;

		// 사용자 Access 객체 초기화
		String userClass = getParameter(filterConf, "user-class");

		if (userClass != null && userClass.length() > 0) {

			try {
				Class userImplClass = Class.forName(userClass);
				user = (User) userImplClass.newInstance();

				User.setUserImplClass(userImplClass);
				// 순서를 바꾸니깐 이상해지는것 같다...

				Logger.info.println(LOG_ID + "User 구현객체:"
						+ userImplClass.getName());
			} catch (Throwable e) {
				Logger.warn.println(LOG_ID + "user instance err", e);
				Logger.sys.println(LOG_ID
						+ "User 구현객체가 없으므로 사이트에 대한 인증체크가 이루어 지지 않습니다.");
			}
		} else
			Logger.sys.println(LOG_ID
					+ "User 구현객체가 없으므로 사이트에 대한 인증체크가 이루어 지지 않습니다.");

		if ("true".equals(getParameter(filterConf, "compact-html"))) {
			this.compactHtml = true;
			Logger.info.println(LOG_ID + "compactHTML:true");
		} else
			Logger.info.println(LOG_ID + "compactHTML:false");

		try {
			err_dispath_page = getParameter(filterConf, "error-page");
			if (err_dispath_page != null && err_dispath_page.length() == 0)
				err_dispath_page = null;
			else {
				Logger.info.println(LOG_ID + "set error dispath page:"
						+ this.err_dispath_page);
				// err_dispath_page= err_dispath_page + "?" + KEY_BYPASS +
				// "=true";
			}

		} catch (Exception e) {
		}

		String layoutClassNm = getParameter(filterConf, "layoutdao-class");

		if (layoutClassNm != null) {
			Logger.info.println(LOG_ID + "layout DAO:" + layoutClassNm);
			LayoutManager.setLayoutDaoClassName(layoutClassNm);
		}

		layoutMgr = LayoutManager.getInstance();
		Logger.info.println(LOG_ID + "layout DAO:"
				+ layoutMgr.getClass().getName());

		// xml 방식의 stie
		String menudao = "jdf.framework.view.menu.dao.XmlMenuDao";
		try {
			// Menu DAO 초기화
			String tmp = getParameter(filterConf, "menudao-class");
			if (tmp != null && tmp.length() > 0)
				menudao = tmp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		MenuMgrBean.setMenuDaoClass(menudao);

		// Menu Load
		// ctx 내에서 context path를 구할 방법이 없다.
		// MenuMgrBean.loadSitemap(ctx.getRealPath("/"), null) ;

		System.out.println("anyTEMPLET Filter init complete.");
		System.out
				.println("*******************************************************************************");

	}

	private boolean reloadXsl(ServletRequest request) {
		if (request.getParameter("style-reload") != null)
			return true;
		else
			return false;
	}

	private boolean reloadMenu(ServletRequest request) {
		// menu reload
		if (request.getParameter("menu-reload") != null) {
			layoutMgr.load();
			return true;
		} else
			return false;
	}

	private String contextPath = null;

	/**
	 * 실제 Filtering 작업
	 * 
	 * 
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		
		// if( request.getAttribute(KEY_TEMPLET_BYPASS_INNER) !=null ) return;

		String tmplJsp = null;
		String urlS = "";

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (contextPath == null)
			contextPath = req.getContextPath();

		if (this.defaultCharEncoding != null)
			req.setCharacterEncoding(this.defaultCharEncoding);

		if (user != null) {
			request.setAttribute(USER_OBJ, user);
		}

		try {
			if (this.commonCmd != null)
				commonCmd.preTask(req, resp);
		} catch (Exception ee) {
			Logger.warn.println(LOG_ID + "common command pretask error", ee);
		}

		// 요청 URI ex) /demo/table.jsp
		urlS = req.getRequestURI();
		
		
		try {
			boolean isMenuReload = reloadMenu(request);
			MenuMgrBean mgr = MenuMgrBean.getInstance(req, isMenuReload);

			// 해당 URI가 exclude되는 dir에 있거나 bypass query 가 들어오면
			// templet 적용하지 않고 bypass
			if (isExclude(urlS, contextPath)
					|| request.getParameter(KEY_TEMPLET_BYPASS) != null
					|| request.getAttribute(KEY_TEMPLET_BYPASS_INNER) != null
					|| request.getAttribute(KEY_TEMPLET_BYPASS) != null
					|| request.getAttribute("bypass") != null) {
				// Logger.debug.println("*REQ " + mgr.getRequestFullURL());
				chain.doFilter(request, response);
				// Logger.debug.println("*RESP " + mgr.getRequestFullURL());
				return;
			}
			
			
			Logger.debug.println("[REQ] " + mgr.getRequestFullURL());
			
			String full_url = mgr.getRequestFullURL();
			
			if(full_url.indexOf("site_id=") != -1)
			{		
				String temp_site_id = full_url.substring(full_url.indexOf("site_id=")+8);
				String site_id = null;
				if(temp_site_id.indexOf('&') != -1)
					site_id = temp_site_id.substring(0, temp_site_id.indexOf('&'));
				else 
					site_id = temp_site_id;
				request.setAttribute("SITE_ID", site_id);
			}
			
			
			// printReqInfo(req);

			WebSiteMenu site = mgr.getWebSiteMenu();
			MenuItem menu = mgr.getSelectedMenuItem();

			request.setAttribute(CHECKED, CHECKED);
			request.setAttribute(BODY_URL, urlS);
			request.setAttribute(SITE_MENU, site);

			// 접속로그 만들기
			StringBuffer accLog = new StringBuffer();

			if (this.userIdSessionName != null) {
				accLog.append(req.getSession().getAttribute(
						this.userIdSessionName));
			} else
				accLog.append("GUEST");

			accLog.append(" ");

			// session Id
			accLog.append(req.getSession().getId());
			accLog.append(" ");
			// 접속 IP
			accLog.append(req.getRemoteAddr());
			accLog.append(" ");
			// menu id
			accLog.append(menu.getIndexKey());
			accLog.append(" ");
			// url
			accLog.append(mgr.getRequestFullURL());

			if (menu.hasUrl()) {
				request.setAttribute(MENU, menu);

			}
			// url 이 없는 MenuItem 이면 bypass
			else {

				request.removeAttribute(MenuMgrBean.KEY);
				// 페이지 reload 하는 경우 이미 이 Filter에 의해 MenuMgrBean 이
				// 로드된 상태면 reload 가 안되는 문제 때문에, MenuMgrBean 객체를 삭제

				request.setAttribute(KEY_TEMPLET_BYPASS_INNER, "");

				chain.doFilter(request, response);

				// bld 목록정보 세팅
				accLog.append(" ");

				Logger.user.println(accLog.toString());

				return;
			}

			Layout layout = menu.getLayout();

			if (layout == null) {
				layout = layoutMgr.getDefaultLayout();
				if (layout == null)
					throw new IOException("default layout이 존재하지 않습니다");
			}

			// request.setAttribute("javax.servlet.include.path_info",
			// req.getPathInfo());

			tmplJsp = layout.getTemplate();

			// template page가 없으면 그냥 bypass
			if (tmplJsp == null || tmplJsp.length() == 0) {
				chain.doFilter(request, response);
				return;
			}

			// 사용자가 templet이라는 변수명으로 layout이름을 정의한 경우
			String userLayoutName = request.getParameter(KEY_TEMPLET);

			if (userLayoutName != null) {
				Layout lay = layoutMgr.getLayout(userLayoutName);
				if (lay == null)
					throw new IOException(userLayoutName + " layout이 존재하지 않습니다");

				tmplJsp = lay.getTemplate();

			}

			// 권한 체크
			if (user != null) 
			{
				Logger.debug.println("ROLE-NAME:" + request.getParameter("role"));
				
				if( request.getParameter("role") != null && request.getParameter("role").equals("admin") )				
					user.checkAdminPrivilege(req, resp, menu);
				else
					user.checkPrivilege(req, resp, menu);
								
				if(request.getAttribute("isRedirect") != null && 
						"true".equals((String)request.getAttribute("isRedirect")))
				{
					/*AclInfo acl = AclInfo.getAclInfoInstance();
					String rolename = menu.getAuthLevel()[0];
					RoleInfo role = acl.getRoleInfo(rolename);
					if (role == null)
						role = acl.getDefaultRoleInfo();
					
					try
					{
						request.getRequestDispatcher(role.getLoginUrl()).forward(request, response);
					} catch(Exception ex)
					{
						
					}*/
					return;
				}
				
				// Logger.debug.println(">>>>>>>>>>>>>>>>>>>>>> BYPASS");

				if (response.isCommitted())
					return;

				// 
				// request.setAttribute(USER_OBJ, user);
			}

			// Command 살행
			Command cmd = getItemCommandInstance(menu);

			try {
				if (cmd != null)
					cmd.preTask(req, resp);
			} catch (Exception ee) {
				Logger.warn.println(LOG_ID + "command["
						+ cmd.getClass().getName() + "] pretask error", ee);
				return;
			}

			/*
			 * String contextPage = req.getContextPath();
			 * 
			 * if (contextPage != null && contextPage.length() > 0) tmplJsp =
			 * contextPage + tmplJsp;
			 */

			// ByteArrayServletResponse respWrap = new
			// ByteArrayServletResponse(resp,compactHtml);
			ByteArrayServletResponse respWrap = new ByteArrayServletResponse(
					resp, false, this.filterCharEncoding);

			// weblogic 6.1용
			// request.removeAttribute("javax.servlet.jsp.PageContext.out");

			request.setAttribute(KEY_TEMPLET_BYPASS_INNER, "");
			chain.doFilter(request, respWrap);

			// bld 목록정보 세팅
			accLog.append(" ");
			Logger.user.println(accLog.toString());

			// chain.doFilter(request, new NullServletResponse());

			if (respWrap.isRedirectMode()) {

				Logger.debug.println(LOG_ID + "redirect url");
				respWrap.copy(resp);

				return;
			}

			respWrap.flushBuffer();

			// 응답 HTML
			String result = null;

			if (request.getAttribute(KEY_TEMPLET_BYPASS) != null
					|| request.getAttribute("bypass") != null) {

				Logger.debug.println(LOG_ID + "onProcess bypass");

				if (respWrap.getContentType() != null) {

					response.setContentType(respWrap.getContentType());
				}

				if (this.filterCharEncoding != null)
					result = new String(respWrap.toByteArray(),
							this.filterCharEncoding);
				else
					result = respWrap.toString();

				response.getWriter().print(result);
				return;
			}
			// 다중 실행방지
			request.setAttribute(KEY_TEMPLET_BYPASS, "");

			String xslName = menu.getParentParam("xsl");

			if (xslName == null || xslName.length() == 0) {
				if (request.getAttribute(KEY_XSL) != null)
					xslName = request.getAttribute(KEY_XSL).toString();

			} else {

				// 만약 "" (공백) 으로 세팅되면 XSL을 적용하지 않는다.
				if (request.getAttribute(KEY_XSL) != null
						&& request.getAttribute(KEY_XSL).toString().length() == 0)
					xslName = null;
			}

			// xsl 화일이 설정된 경우
			if (xslName != null && xslName.length() > 0) {

				try {
					boolean isCache = !reloadXsl(request);

					ByteArrayOutputStream baos = new ByteArrayOutputStream();

					TransformerHelper.trans(ctx, xslName,
							new ByteArrayInputStream(respWrap.toByteArray()),
							baos, isCache);
					byte[] xformBytes = baos.toByteArray();

					result = new String(xformBytes);

					Logger.info.println(LOG_ID + "XSLT completed : " + xslName);
				} catch (Exception e) {
					Logger.err.println(LOG_ID + "xml transform error "
							+ xslName, e);
					throw new ServletException("Unable to transform document",
							e);
				}
			} else {
				if (this.filterCharEncoding != null)
					result = new String(respWrap.toByteArray(),
							this.filterCharEncoding);
				else
					result = respWrap.toString();

			}

			request.setAttribute(BODY_CONTENT, result);

			try {
				response.reset();
				respWrap.copy(resp);

			} catch (Exception ee) {
				return;
			}

			// JSP 에서 request.setAttribute("templet", ""); 으로 세팅한 경우
			if (request.getAttribute(KEY_TEMPLET) != null) {

				String tmplt_name = request.getAttribute(KEY_TEMPLET)
						.toString();

				Layout lay = layoutMgr.getLayout(tmplt_name);
				if (lay == null)
					throw new IOException(tmplt_name + " layout이 존재하지 않습니다");

				tmplJsp = lay.getTemplate();

			}

			String contextPage = req.getContextPath();

			if (contextPage == null || contextPage.length() < 2)
				contextPage = "";

			request.setAttribute("TEMPLET_NAME", tmplJsp);
			request.setAttribute("TEMPLET_DIR", contextPage
					+ tmplJsp.substring(0, tmplJsp.lastIndexOf("/")));

			RequestDispatcher rd = req.getRequestDispatcher(tmplJsp);

			boolean isCompact = compactHtml;

			// HTML을 compact하게 처리할것인가?
			if ("false".equals(menu.getParam("compact")))
				isCompact = false;
			else if ("true".equals(menu.getParam("compact")))
				isCompact = true;

			if (isCompact) {

				HttpServletResponse compactResp = new HtmlCompactServletResponse(
						resp);

				rd.forward(request, compactResp);
			}

			else
				rd.forward(request, response);
			// rd.include(request, response);

			Logger.debug.println(LOG_ID + tmplJsp + " << " + urlS);

			try {
				if (cmd != null)
					cmd.postTask(req, resp);
			} catch (Exception ee) {
				Logger.warn.println(LOG_ID + "command["
						+ cmd.getClass().getName() + "] posttask error", ee);
			}

		} catch (IOException ioe) {
			Logger.err.println("IOException raised in UIFilter", ioe);
			// io.printStackTrace();

			if (!dispathErroPage(request, response, ioe, tmplJsp))
				throw ioe;
		} catch (ServletException se) {
			// Logger.err.println("ServletException raised in UIFilter", se);
			// se.printStackTrace();
			if (!dispathErroPage(request, response, se, tmplJsp))
				throw se;
		} catch (RedirectPermissionException rpe) {

			// jeus 버그 때문에 만든 Exception
			// 이 exception 시는 그냥 return 시킨다.
			// Logger.info.println(" ---------------------
			// RedirectPermissionException");
			// chain.doFilter(request, response);

			return;
		}

		catch (Exception ee) {
			Logger.err.println(LOG_ID + "exception from " + urlS, ee);

			dispathErroPage(request, response, ee, tmplJsp);
		} finally {
			try {
				if (this.commonCmd != null)
					commonCmd.postTask(req, resp);
			} catch (Exception ee) {
			}
		}

	}

	public void destroy() {
		return;
	}

	public FilterConfig getFilterConfig() {
		return this.filterConfig;
	}

	private boolean isExclude(String uri, String contextPath) {
		for (int i = 0; i < this.excludeDirs.length; i++) {
			if (uri.indexOf(contextPath + excludeDirs[i]) == 0)
				return true;
		}

		return false;

	}

	// 공통 command class
	// 모든 URL 에 반응
	private Command commonCmd;

	/**
	 * Command instance를 얻는다.
	 * 
	 * @param menu
	 * @return
	 */
	private Command getItemCommandInstance(MenuItem menu) {

		Class clss = menu.getCommandClass();

		if (clss != null) {
			try {
				Command cmd = (Command) clss.newInstance();
				return cmd;
			} catch (Exception e) {
			}

		}

		return null;
	}

	private boolean dispathErroPage(ServletRequest request,
			ServletResponse response, Exception err, String templetPage) {
		if (this.err_dispath_page == null || err == null)
			return false;

		try {
			request.setAttribute("exception", err);
			request.setAttribute(KEY_TEMPLET_BYPASS, "true");

			if (templetPage != null) {
				ByteArrayServletResponse respWrap = new ByteArrayServletResponse(
						(HttpServletResponse) response);

				// System.out.println("------> "+err_dispath_page);

				RequestDispatcher rd = request
						.getRequestDispatcher(err_dispath_page);
				// rd.include(request, respWrap);
				rd.forward(request, respWrap);

				String result = respWrap.toString();

				if (this.filterCharEncoding != null)
					result = new String(result
							.getBytes(this.filterCharEncoding));
				request.setAttribute(BODY_CONTENT, result);

				/*
				 * try { response.reset(); } catch(IllegalStateException ile) {}
				 */

				rd = request.getRequestDispatcher(templetPage);
				rd.forward(request, response);
				// rd.include(request, response);

			} else {
				// System.out.println("* ------> "+err_dispath_page);
				RequestDispatcher rd = request
						.getRequestDispatcher(err_dispath_page);
				rd.forward(request, response);

			}
			return true;

		} catch (Exception e) {
			err_dispath_page = null;
		}

		return false;

	}

}