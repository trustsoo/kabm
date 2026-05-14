package com.kabm.filter;


import com.kabm.util.NetworkUtil;
import com.kabm.util.UserAgentUtil;
import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.http.JDFrameContextListener;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringFormater;
import jdf.framework.view.auth.*;
import jdf.framework.view.control.Command;
import jdf.framework.view.layout.LayoutManager;
import jdf.framework.view.layout.entity.Layout;
import jdf.framework.view.layout.filter.ByteArrayServletResponse;
import jdf.framework.view.layout.filter.HtmlCompactServletResponse;
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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


/**
 * WAS로 요청한 페이지에 UI를 입혀주는 Filter
 * 
 * 기본적으로 *.jsp, *.jspx와 *.htm 에 이 Filter가 반응하게 web.xml을 설정하여 준다.
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
 * @author 김성권
 */
public class CommonFilter implements Filter, MenuContext, JDFContext {
    private final static String LOG_ID = "<t:CommonFilter> ";
    
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
    
    
    private List<String> custNoKeyNames = null;
    
    // templet적용을 배제하는 디렉토리
    private String[] excludeDirs;
    
    private String[] unlogurls;
    
    
    private User user;

    private String err_dispath_page;
    private String xml_err_dispath_page;
    
    private String login_page;
    private String redirect_url_param = null;
    
    
    private String thisServerIP = null;
    private String thisPublicIP = null;
    
    // compact-html
    boolean compactHtml = false;
    private String default_cmpnyId = "voc";

    void printReqInfo(HttpServletRequest req) {
        Logger.info.println("getContextPath " + req.getContextPath());
        Logger.info.println("getServletPath " + req.getServletPath());
        Logger.info.println("getPathInfo " + req.getPathInfo());
        Logger.info.println("getRequestURI " + req.getRequestURI());
        Logger.info.println("getRequestURL "
                + jdf.framework.view.menu.util.RequestURL.getUrl(req));

        Logger.info.println("toString "
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
            }catch(ConfigurationException ce){
                Logger.warn.println(LOG_ID+ce.toString());
            }catch(Exception e){
                Logger.warn.println(LOG_ID+e.toString());
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

        System.out.println("*******************************************************************************");
        System.out.println("TEMPLET Filter init.");

        JDFrameContextListener.setJdfFrameConfigPath(filterConf.getServletContext());

        // setAnyframeConfigPath 전에 로그를 쓰지 말자.
        this.filterConfig = filterConf;

        // startup 은 ContextListener 를 통해 구현하길...
        // initStartupClass(filterConf);

        ctx = filterConf.getServletContext();
        
        String cmpnyId = getParameter(filterConf, "default-company-id");
        if( cmpnyId != null && !"".equals(cmpnyId)) default_cmpnyId = cmpnyId;
        	
        String charEncoding = getParameter(filterConf, "request-characterEncoding");
        if (charEncoding != null && charEncoding.length() > 0)
            this.defaultCharEncoding = charEncoding;
        else if ("bypass".equals(charEncoding))
            this.defaultCharEncoding = null;

        String filterEncoding = getParameter(filterConf, "filter-characterEncoding");
        if (filterEncoding != null && filterEncoding.length() > 0)
            this.filterCharEncoding = filterEncoding;

        // filter 제외 디렉토리 설정
        String param = getParameter(filterConf, "exclude-dir");
        Logger.info.println(LOG_ID + "exclude-dir:" + param);

        if (param == null || param.length() == 0)
            excludeDirs = new String[] {};
        else
            excludeDirs = SmartStringArray.split(",", param);

        param = getParameter(filterConf, "unlog-url");
        Logger.info.println(LOG_ID + "unlog-url:" + param);

        if (param == null || param.length() == 0)
        	unlogurls = new String[] {};
        else
        	unlogurls = SmartStringArray.split(",", param);
        
        // 명령 Command 객체 초기화
        initCommand(filterConf);

        this.userIdSessionName = getParameter(filterConf, "userid-session-name");
        
        
        this.custNoKeyNames = Arrays.asList(SmartStringArray.split(",", getParameter(filterConf, "cust_keys")));        

        // 공백이면 null 로 처리한다.
        if (this.userIdSessionName != null && this.userIdSessionName.length() == 0)
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
                Logger.sys.println(LOG_ID + "User 구현객체가 없으므로 사이트에 대한 인증체크가 이루어 지지 않습니다.");
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
            
            xml_err_dispath_page = getParameter(filterConf, "xml-error-page");
            if (xml_err_dispath_page != null && xml_err_dispath_page.length() == 0)
                xml_err_dispath_page = null;
            
            
            login_page = getParameter(filterConf, "login-page");
            if (login_page != null && login_page.length() == 0)
                login_page = null;
            
            redirect_url_param = getParameter(filterConf, "redirect-url-param");
            if (redirect_url_param != null && redirect_url_param.length() == 0)
                redirect_url_param = null;
            
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
        }catch (NullPointerException ne) {
            Logger.warn.println(LOG_ID+ne.toString());
        }catch(Exception e){
            Logger.warn.println(LOG_ID+e.toString());
        }
        MenuMgrBean.setMenuDaoClass(menudao);

        // Menu Load
        // ctx 내에서 context path를 구할 방법이 없다.
        // MenuMgrBean.loadSitemap(ctx.getRealPath("/"), null) ;
        
        thisServerIP = NetworkUtil.getHostAddress();
        if( thisPublicIP == null ) thisPublicIP = thisServerIP;
        System.out.println("This Server IP : "+thisServerIP);
        
        System.out.println("TEMPLET Filter init complete.");
        System.out.println("*******************************************************************************");
        
        /*try
        {
            startDBPool();
        } catch(Exception e)
        {
            
        }*/

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

        boolean isAjax = false;
        
        
        jdf.framework.core.Message msg= new jdf.framework.core.MessageBox();
        String tmplJsp = null;
        String urlS = "";
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        if("XMLHttpRequest".equals(req.getHeader("x-requested-with")))
        {
            isAjax = true;
        }
        
        String clientLanguage = null;
        
        String userAgent = req.getHeader("user-agent") == null ? "" : req.getHeader("user-agent").toLowerCase();
        String lang_code = req.getHeader("accept-language") == null ? "" : req.getHeader("accept-language").toLowerCase();
        try
        {
            if(!"".equals(lang_code) && lang_code.indexOf('-') != -1)
            {
                clientLanguage = lang_code.split(",")[0].toLowerCase();
                clientLanguage = SmartStringArray.split("-", clientLanguage)[0];
            }
        } catch(Exception ex)
        {
            clientLanguage = null;
        }
        
                
        if(clientLanguage== null || clientLanguage.length() != 2) clientLanguage = "ko";
        
        
        req.setAttribute(LANGCODE, StringFormater.replaceStr(clientLanguage, " ", ""));
        
        String osName = null;
        
        try
        {
            osName = UserAgentUtil.getOSName(req);
        } catch(Exception ex)
        {
            osName = "";
        }
                
        
        Logger.info.println(LOG_ID+"[REQ] "+jdf.framework.view.menu.util.RequestURL.getFullUrl(req)+" ("+osName+")"+" AJAX:"+isAjax+" "+" LANG:"+clientLanguage);
        
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
        
        Config conf = null;
        String thisServerDomain = null;
        try
        {
            conf = Configuration.lookup("/site");
            thisServerDomain = conf.getString("server_domain");
        } catch(Exception ex)
        {           
        }
        
        
        String uri = req.getRequestURL().toString();
        
        if(uri != null && uri.startsWith("http://"))
        {
            uri = uri.substring(7);
        }
        
        
        request.setAttribute(MenuItem.MULTI_TENANT_KEY, default_cmpnyId);
        
        urlS = req.getRequestURI();

        try 
        {           
            boolean isMenuReload = reloadMenu(request);
            MenuMgrBean mgr = MenuMgrBean.getInstance(req, isMenuReload);           
            Logger.debug.println(LOG_ID+"[REQ] "+mgr.getRequestFullURL()+" - AJAX:"+isAjax+" ");
            
            WebSiteMenu site = mgr.getWebSiteMenu();
            MenuItem menu = mgr.getSelectedMenuItem();
            Logger.debug.println(LOG_ID+"MENUINFO:"+menu.toString());
            
            request.setAttribute(FULL_URL, mgr.getRequestFullURL());
            request.setAttribute(CHECKED, CHECKED);
            request.setAttribute(BODY_URL, urlS);
            request.setAttribute(SITE_MENU, site);
            
            // 접속로그 만들기
            StringBuffer accLog = new StringBuffer();

            if (this.userIdSessionName != null && req.getSession().getAttribute(this.userIdSessionName) != null) 
            {
                accLog.append(req.getSession().getAttribute(this.userIdSessionName));
            } else
                accLog.append("0");
            
            accLog.append(" ");

            // session Id
            /*accLog.append(req.getSession().getId());
            accLog.append(" ");*/
            // 접속 IP
            accLog.append(NetworkUtil.getRemoteAddr(req));
            accLog.append(" ");
            
            // menu id
            /*accLog.append(menu.getIndexKey());
            accLog.append(" ");*/
            
            // full_url
            accLog.append(mgr.getRequestFullURL());
            accLog.append(" ");
            
            String cust_no = "0";
            
            
            
            Enumeration<String> enu = req.getParameterNames();
            while(enu.hasMoreElements())
            {
            	String kk = enu.nextElement();
            	if(this.custNoKeyNames.contains(kk.toLowerCase()))
            	{
            		cust_no = req.getParameter(kk);            		                    
            		break;
            	}
            }
            
            accLog.append(cust_no);
            
            if(menu.hasUrl()) 
            {	
            	boolean isPrintLog = true;
            	for(String a : unlogurls)
        		{
        			if(mgr.getRequestFullURL().indexOf(a) > -1)
        			{
        				isPrintLog = false;
        				break;
        			}
        			
        		}
                request.setAttribute(MENU, menu);
                
                if(isPrintLog)
                	Logger.user.println(accLog.toString());
                
            } else // url 이 없는 MenuItem 이면 bypass 
            {
            	
            	if (!isExclude(urlS, contextPath)
                        && request.getParameter(KEY_TEMPLET_BYPASS) != null
                        		&& request.getAttribute(KEY_TEMPLET_BYPASS_INNER) != null
                        				&& request.getAttribute(KEY_TEMPLET_BYPASS) != null
                        						&& request.getAttribute("bypass") != null) {
            		
            		System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	System.err.println("*****************************메뉴에 등록되지 않은 URL입니다.***************************"+mgr.getRequestFullURL());
                	
                	throw new Exception("메뉴에 등록되지 않은 URL입니다."+mgr.getRequestFullURL());
            		
            	} else
            	{
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
            	
            	
            	
            	
            	
                /*request.removeAttribute(MenuMgrBean.KEY);
                // 페이지 reload 하는 경우 이미 이 Filter에 의해 MenuMgrBean 이
                // 로드된 상태면 reload 가 안되는 문제 때문에, MenuMgrBean 객체를 삭제
                request.setAttribute(KEY_TEMPLET_BYPASS_INNER, "");
                chain.doFilter(request, response);
                // bld 목록정보 세팅
                accLog.append(" ");
                Logger.user.println(accLog.toString());
                return;*/
            }           
            
            
            Layout layout = menu.getLayout();
            
            String uLanguage = null;
            try
            {
                uLanguage = UserAgentUtil.getUserLanguage(req);
            } catch(Exception ex)
            {
                uLanguage = "ko";
            }
            if (layout == null) {
                layout = layoutMgr.getDefaultLayout();
                if (layout == null)
                    throw new IOException(msg.getMessage(uLanguage, "WARN" ,  "W0001"));
            }
            
            tmplJsp = layout.getTemplate();

            
            //Logger.debug.println("tmplJsp>>>>>>>>>>>>>>>>>>>>>>>>"+tmplJsp);
            

            // 사용자가 templet이라는 변수명으로 layout이름을 정의한 경우
            String userLayoutName = request.getParameter(KEY_TEMPLET);
            
            if(userLayoutName == null)
            {
                try
                {
                    userLayoutName = (String)request.getAttribute(KEY_TEMPLET);
                } catch(Exception ex)
                {}
            }
            
            if (userLayoutName != null) {
                Layout lay = layoutMgr.getLayout(userLayoutName);
                if (lay == null)
                    throw new IOException(userLayoutName + " " +msg.getMessage(UserAgentUtil.getUserLanguage(req), "WARN" ,  "W0001"));

                tmplJsp = lay.getTemplate();
            }
            
            // 권한 체크
            if (user != null) 
            {
                Logger.debug.println(LOG_ID+"ROLE-NAME:" + request.getParameter("role"));
                
                user.checkPrivilege(req, resp, menu);                               
                
                if(request.getAttribute("isRedirect") != null && 
                        "true".equals((String)request.getAttribute("isRedirect")))
                {
                    AclInfo acl = AclInfo.getAclInfoInstance();
                    String rolename = menu.getAuthLevel()[0];
                    RoleInfo role = acl.getRoleInfo(rolename);
                    if (role == null)
                        role = acl.getDefaultRoleInfo();
                    
                    try
                    {
                        request.getRequestDispatcher(role.getLoginUrl()).forward(request, response);
                    } catch(Exception ex)
                    {                       
                    }
                }

                if (response.isCommitted())
                    return;             
            }
            
            
            
            // template page가 없으면 그냥 bypass
            if (tmplJsp == null || tmplJsp.length() == 0) {
                chain.doFilter(request, response);
                return;
            }
            
            
            
            // 해당 URI가 exclude되는 dir에 있거나 bypass query 가 들어오면
            // templet 적용하지 않고 bypass
            if (isExclude(urlS, contextPath)
                    || request.getParameter(KEY_TEMPLET_BYPASS) != null
                    || request.getAttribute(KEY_TEMPLET_BYPASS_INNER) != null
                    || request.getAttribute(KEY_TEMPLET_BYPASS) != null
                    || request.getAttribute("bypass") != null) {    
                chain.doFilter(request, response);
                // Logger.debug.println("*RESP " + mgr.getRequestFullURL());
                return;
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
//System.out.println(respWrap.toString());
            // bld 목록정보 세팅
            //accLog.append(" ");
            //Logger.user.println(accLog.toString());

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
            //Logger.debug.println(LOG_ID+"(2) session id "+req.getSession().getId());
            try {
                response.reset();
                respWrap.copy(resp);

            } catch (Exception ee) {
                return;
            }
            //Logger.debug.println(LOG_ID+"(3) session id "+req.getSession().getId());
            // JSP 에서 request.setAttribute("templet", ""); 으로 세팅한 경우
            if (request.getAttribute(KEY_TEMPLET) != null) {

                String tmplt_name = request.getAttribute(KEY_TEMPLET)
                        .toString();

                Layout lay = layoutMgr.getLayout(tmplt_name);
                if (lay == null)
                    throw new IOException(tmplt_name + " " +msg.getMessage(UserAgentUtil.getUserLanguage(req), "WARN" ,  "W0001"));

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
            Logger.err.println(LOG_ID+"IOException raised in "+this.filterConfig.getFilterName(), ioe);
            
            if(!isAjax) 
            {
                if (!dispatchErrorPage(request, response, ioe, tmplJsp))
                    throw ioe;
            } else
            {
                dispathErrorXML(request, response, ioe, "500");
            }
        } catch (ServletException se) {
            Logger.err.println(LOG_ID+"ServletException raised in "+this.filterConfig.getFilterName(), se);         
            if(!isAjax) 
            {
                if (!dispatchErrorPage(request, response, se, tmplJsp))
                    throw se;
            } else
            {
                dispathErrorXML(request, response, se, "500");
            }
            
        } catch (RedirectPermissionException rpe) {

            // jeus 버그 때문에 만든 Exception
            // 이 exception 시는 그냥 return 시킨다.
            return;
        } catch( NotLoginException ne)
        {
            //Logger.info.println(LOG_ID+"[REQ] "+jdf.framework.view.menu.util.RequestURL.getFullUrl(req)+" - INAPP:" + isInApp + " AJAX:"+isAjax+" "+" LANG:"+clientLanguage);
            
            if(!isAjax)         
            {               
                redirecLoginPage( request, response);
            }
            else
            {
                dispathErrorXML(request, response, ne, "403");
            }
        }

        catch (Exception ee) 
        {
            Logger.err.println(LOG_ID + "exception from " + urlS, ee);
            if(!isAjax) 
                dispatchErrorPage(request, response, ee, tmplJsp);
            else
                dispathErrorXML(request, response, ee, "500");
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
    
    private boolean redirecLoginPage(ServletRequest request, ServletResponse response) {
        if (this.login_page == null )
            return false;

        try
        {
            HttpServletResponse resp = (HttpServletResponse) response;
            String param = null;
            if(redirect_url_param != null)
            {
                param = (String)request.getAttribute(FULL_URL);
            }
            
            if(param != null)
                resp.sendRedirect(this.login_page+"?"+this.redirect_url_param+"="+java.net.URLEncoder.encode(param, "utf-8"));
            else
                resp.sendRedirect(this.login_page);
            return true;
        } catch (Exception e) 
        {           
        }
        return false;
    }

    private boolean dispatchErrorPage(ServletRequest request,
            ServletResponse response, Exception err, String templetPage) {
        
        //templetPage = null;
        
        if (this.err_dispath_page == null || err == null)
            return false;

        try {
            request.setAttribute("exception", err);
            request.setAttribute("message", err.getMessage());
//          response.setCharacterEncoding(this.filterCharEncoding);
            if (templetPage != null)
            {
                
                ByteArrayServletResponse respWrap = new ByteArrayServletResponse(
                        (HttpServletResponse) response, false, this.filterCharEncoding);
//              ByteArrayServletResponse respWrap = new ByteArrayServletResponse((HttpServletResponse) response);
                RequestDispatcher rd = request.getRequestDispatcher(err_dispath_page);

                rd.forward(request, respWrap);

                String result = null;                               
                
                if (this.filterCharEncoding != null)
                    result = respWrap.toString(this.filterCharEncoding);
                else
                    result = respWrap.toString();               
                
                
                request.setAttribute(BODY_CONTENT, result);
                                
                rd = request.getRequestDispatcher(templetPage);
                rd.forward(request, response);
                

            } else {
                System.out.println("* ------> "+err_dispath_page);
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
    
    private boolean dispathErrorXML(ServletRequest request,
            ServletResponse response, Exception err, String code) { 
        
        
        try {
            request.setAttribute("exception", err);
            request.setAttribute("message", err.getMessage());
            request.setAttribute("code", code);
            RequestDispatcher rd = request.getRequestDispatcher(xml_err_dispath_page);
            rd.forward(request, response);
            
            return true;

        } catch (Exception e) {
            xml_err_dispath_page = null;
        } 

        return false;
    }
    
    /*private void startDBPool() throws Exception
    {
        Logger.info.println("**********************<JDBC Pool> Pool create DB Pool");       
        try {
            String root = "/connectionPool";
            Config config = Configuration.getInitial();
            
            java.util.List<String> keys = config.keys();
            
            for(int i = 0; i < keys.size(); i++) 
            {
                String key = (String) keys.get(i);
                
                int l = key.lastIndexOf("maxActive");
                
                if (key.startsWith(root) && (l > 0)) 
                {
                    String poolName = key.substring(root.length() + 1, l-1);
                    
                    Config conf = Configuration.lookup("/connectionPool/"+poolName);                
                    java.sql.Connection con = jdf.framework.core.db.ConnectionManager.getConnection("jdbc:apache:commons:dbcp:"+poolName);
                    con.close();
                }
                
            }
        }
        catch(Exception e) {
            Logger.err.println("Create DB Pool ERROR. Mesage -> " + e.getMessage());
            throw e;
        }
    }*/
    
    

}
