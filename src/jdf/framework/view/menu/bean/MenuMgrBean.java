package jdf.framework.view.menu.bean;

import jdf.framework.core.http.JDFrameContextListener;
import jdf.framework.core.log.Logger;
import jdf.framework.view.menu.WebPagePublisher;
import jdf.framework.view.menu.dao.MenuDao;
import jdf.framework.view.menu.dao.XmlMenuDao;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.entity.WebSiteMenu;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Menu 관리
 * 이 class를사용하려면 web.xml에 UIFilter 가 등록되어 있어야 한다.
 * 예제)
 * MenuItem menu=MenuMgrBean.getMenuItem("/url/example.jsp");
 * MenuItem menu=MenuMgrBean.getMenuItemById("000003");
 * 
 * @author
 * 
 */
public class MenuMgrBean {

	private final static String LOG_ID = "<t:MenuMgrBean> ";

	private static Class menu_dao_class = XmlMenuDao.class;

	public final static String DEFAULT = "sitemap.xml";

	public final static String KEY = "site_menu";

	private static Map siteMenuMap = new HashMap();

	private static Map publishMenuMap = new HashMap(); // generate 할 메뉴 리스트의
	// map

	private static MenuItem defaultMenu = new MenuItem("default", "");

	private WebSiteMenu siteMenu;

	private MenuItem selectedMenu = defaultMenu;

	private String requestUrl;

	private String requestFullUrl;

	private static WebPagePublisher publisher;

	/**
	 * MenuDao class 를 세팅한다.
	 * 
	 * @param className
	 */
	public static void setMenuDaoClass(String className) throws IllegalArgumentException {
		try {
			menu_dao_class = Class.forName(className);

			menu_dao_class.newInstance();

		} catch (ClassNotFoundException e) {
			Logger.err.println(LOG_ID + "set MenuDao error. ClassNotFoundException:" + className);
			throw new IllegalArgumentException("ClassNotFoundException:" + className);
		} catch (Exception e) {
			Logger.err.println(LOG_ID + "set MenuDao instantiation error. " + className);
			throw new IllegalArgumentException("instantiation error:" + className);
		}

		Logger.info.println(LOG_ID + "MenuDao:" + menu_dao_class.getName());
	}

	private static MenuDao getMenuDaoInstance() {
		try {
			return (MenuDao) menu_dao_class.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 기본 생성자
	 * 
	 * @param request
	 * @param isReload sitemap 정보를다시 읽을것인가?
	 */
	public MenuMgrBean(HttpServletRequest request, boolean isReload) {
		this(request, DEFAULT, isReload);
	}

	/**
	 * 기본생성자
	 * @param request
	 */
	public MenuMgrBean(HttpServletRequest request) {
		this(request, DEFAULT, false);
	}

	/**
	 * 
	 * @param request
	 * @param srcName
	 * @param isReload
	 */
	public MenuMgrBean(HttpServletRequest request, String srcName, boolean isReload) {

		this.siteMenu = getWebSiteMenu(request, srcName, isReload);
		this.selectedMenu = getSelectedMenu(request);

		/*
		 * HttpSession session= request.getSession(true); String userid=
		 * (String) session.getAttribute("userid");
		 * ezlog.log.Agent.info.println( selectedMenu.getIndexKey(), request,
		 * userid, requestUrl);
		 */

	}

	/**
	 * MenuMgrBean instance 객체를 가져온다.
	 * 
	 * @param request
	 * @return
	 */
	public static MenuMgrBean getInstance(HttpServletRequest request) {
		return getInstance(request, false);
	}

	/**
	 * MenuMgrBean 객체를 가져온다.
	 * 
	 * @param request
	 * @param isReload
	 * @return
	 */
	public static MenuMgrBean getInstance(HttpServletRequest request, boolean isReload) {
		MenuMgrBean menuMgr = (MenuMgrBean) request.getAttribute(KEY);

		if (menuMgr == null) {
			menuMgr = new MenuMgrBean(request, isReload);

			request.setAttribute(KEY, menuMgr);

			if (isReload) {
			}

		}
		return menuMgr;
	}

	/**
	 * 
	 * @param request
	 * @param srcName
	 * @param isReload
	 * @return
	 */
	public static MenuMgrBean getInstance(HttpServletRequest request, String srcName, boolean isReload) {
		MenuMgrBean menuMgr = (MenuMgrBean) request.getAttribute(KEY);

		if (menuMgr == null) {
			menuMgr = new MenuMgrBean(request, srcName, isReload);

			request.setAttribute(KEY, menuMgr);

		}
		// else Logger.debug.println("REUSE MenuMgrBean");

		return menuMgr;
	}

	/**
	 * sitemap 에 정의된 url에 해당하는 MenuItem 객체를 반환한다.
	 * 
	 */
	public static MenuItem getMenuItem(String url) {
		return getMenuItem(DEFAULT, url);
	}

	/**
	 * sitemap.xml 에 정의된 id에 해당되는 MenuItem 객체를 반환한다.
	 * 
	 * @param id
	 * @return
	 */
	public static MenuItem getMenuItemById(String id) {
		WebSiteMenu siteMenu = (WebSiteMenu) siteMenuMap.get(DEFAULT);

		return siteMenu.getMenuById(id);
	}

	static MenuItem getMenuItem(String srcName, String url) {
		WebSiteMenu siteMenu = (WebSiteMenu) siteMenuMap.get(srcName);

		return siteMenu.getMenu(url);
	}

	/**
	 * 메뉴 정보를 다시 로드한다.
	 * 
	 * @param documentRoot
	 * @param contextPath
	 */
	public static void loadSitemap(String documentRoot, String contextPath) {
		getWebSiteMenu(DEFAULT, documentRoot, true, contextPath);
	}

	/**
	 * 
	 * @param request
	 * @param srcName
	 * @param isReload
	 * @return
	 */
	private static WebSiteMenu getWebSiteMenu(HttpServletRequest request, String srcName, boolean isReload) 
	{
		return getWebSiteMenu(srcName, request.getRealPath("/"), isReload, request.getContextPath());
	}

	/**
	 * 
	 * @param srcName
	 * @param documentRoot
	 * @param isReload
	 * @param contextPath
	 * @return
	 */
	private static WebSiteMenu getWebSiteMenu(String srcName, String documentRoot, boolean isReload, String contextPath) {

		WebSiteMenu siteMenu = (WebSiteMenu) siteMenuMap.get(srcName);

		if (siteMenu == null || isReload) 
		{
			if(!JDFrameContextListener.getDocumentRoot().equals(documentRoot))
			{
				documentRoot = JDFrameContextListener.getDocumentRoot();
			}
			Logger.info.println(LOG_ID + "초기화  documentRoot:" + documentRoot);
			Logger.info.println(LOG_ID + "초기화  contextPath:" + contextPath);
			Logger.info.println(LOG_ID + "초기화  isReload:" + isReload);

			MenuDao dao = getMenuDaoInstance();
			if (dao == null) {
				Logger.err.println(LOG_ID + "getMenuDaoInstance err");
				return null;
			} else
				Logger.info.println(LOG_ID + "Menu DAO class : " + dao.getClass().getName());

			dao.setSourceInfo(srcName);
			dao.setContextPath(contextPath);

			siteMenu = dao.getWebSiteMenu();

			siteMenuMap.put(srcName, siteMenu);
			publishMenuMap.put(srcName, dao.getPublishMenuList());

			isReload = false;

			if (publisher != null)
				publisher.destroy();

			// menuMgr.getS

			String domainName = siteMenu.getParam("domain");
			// String documentRoot = request.getRealPath("/");

			publisher = new WebPagePublisher(domainName, documentRoot, publishMenuMap);
			publisher.start();

			siteMenu.setPublisher(publisher);

		}
		return siteMenu;
	}

	public WebSiteMenu getWebSiteMenu() {
		return siteMenu;
	}

	/**
	 * WebPagePublisher 객체를 return 한다.
	 * 
	 * @return
	 */
	public static WebPagePublisher getWebPagePublisher() {
		return publisher;
	}

	Map getPublishMenuMap() {
		return publishMenuMap;
	}

	public String getRequestURL() {
		return requestUrl;
	}

	public String getRequestFullURL() {
		return requestFullUrl;
	}

	private MenuItem getSelectedMenu(HttpServletRequest request) {
		MenuItem menu = null;

		try {
			StringBuffer url = jdf.framework.view.menu.util.RequestURL.getUrl(request);

			// REQUEST URL
			requestUrl = url.toString();

			// StringBuffer url = HttpUtils.getRequestURL(request);

			if (request.getQueryString() != null) {
				url.append('?');
				url.append(request.getQueryString());
			}

			// REQUEST FULL URL (query 포함)
			requestFullUrl = url.toString();

			// http://www.koreastock.co.kr 제거
			String reqUrl = requestFullUrl.substring(requestFullUrl.indexOf("/", 8));

			requestFullUrl = reqUrl;

			// Logger.debug.println(LOG_ID+request.getRemoteAddr()+" URL:" +
			// reqUrl);

			// MenuItem menu= siteMenu.getMenu(reqUrl);
			
			/*2015.02.25 Multi Tenant 수용 가능하게 수정*/			
			String tenant_id = null;
			
			try
			{			
				tenant_id = (String)request.getAttribute(MenuItem.MULTI_TENANT_KEY);
				
				if("".equals(tenant_id)) tenant_id = null;
				
			} catch(Exception ex)
			{
				tenant_id = null;
			}
			
			if(tenant_id == null)			
				menu = siteMenu.getMenu(request, reqUrl, request.getRequestURI());
			else
				menu = siteMenu.getMenu(request, reqUrl, request.getRequestURI(), tenant_id);
			
			/*2015.02.25 Multi Tenant 수용 가능하게 수정*/
			// Logger.debug.println(LOG_ID+"keyPath name : "+keyPath);

		} catch (Exception e) {
		}

		return menu;
	}

	public List getAllMenuList() {
		return siteMenu.getMenuList();
	}

	public MenuItem getSelectedMenuItem() {
		return selectedMenu;
	}

}
