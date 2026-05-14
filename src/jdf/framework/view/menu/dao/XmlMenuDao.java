package jdf.framework.view.menu.dao;

import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import jdf.framework.core.xml.XMLReferer;
import jdf.framework.view.layout.entity.Layout;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.entity.WebSiteMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Xml file 형태의 sitemap을 읽는 menu dao
 * 
 * @author
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class XmlMenuDao extends MenuDaoBase implements MenuDao {
	private final static String LOG_ID = "<t:XmlMenuDao> ";

	private static String DEFAULT = "sitemap.xml";

	private String filename = DEFAULT;

	protected List<MenuItem> contentDirList = new ArrayList<>();

	/**
	 * 기본 생성자
	 * 
	 */
	public XmlMenuDao() {
		super();
	}

	/**
	 * 
	 * @see MenuDao#setSourceInfo(String)
	 */
	public void setSourceInfo(String source) {
		this.filename = source;

	}

	public List<MenuItem> getPublishMenuList() {
		return publishMenuList;
	}

	public WebSiteMenu getWebSiteMenu() {
		return getWebSiteMenu(filename);
	}

	private WebSiteMenu getWebSiteMenu(String filename) {

		String dir = Configuration.getConfigPath();

		return getWebSiteMenu(dir, filename);

	}

	public WebSiteMenu getWebSiteMenu(String dir, String filename) {
		Logger.info.println(LOG_ID + "load menu dir:" + dir + " name:" + filename);
		Logger.info.print("LOADING MENU ITEM ");
		List<MenuItem> menuList = null;

		String siteName = null;
		String domain = null;

		int p = filename.indexOf(".");

		String fname = filename.substring(0, p);
		String type = filename.substring(p);

		for (int i = 0; i < 20; i++) {

			String menuFileName = filename;

			if (i != 0) {
				menuFileName = fname + i + type;
			}

			File menuFile = new File(dir, menuFileName);

			if (!menuFile.exists()) {
				// Logger.info.println(LOG_ID+"menu file
				// :"+menuFile.toString());
				continue;
			}

			try {
				XMLReferer xmlDoc = new XMLReferer(menuFile);

				try {
					if (siteName == null)
						siteName = xmlDoc.lookup("/site-menu").getString("name");

					if (domain == null)
						domain = xmlDoc.lookup("/site-menu").getString("domain");

					xmlDoc.lookup("/site-menu/start-page");

					String url = xmlDoc.find("url").getText();
					MenuItem menu = new MenuItem("startPage", this.contextPath + url);

					menu.setOriginPage(xmlDoc.find("publish/source-url").getText());
					menu.setGenTimeGap(getInt(xmlDoc.find("publish/time-gap").getText()));
					menu.setPageSize(getInt(xmlDoc.find("publish/min-page-size").getText()));

					if (menu.getPublishTimeGap() > 0)
						publishMenuList.add(menu);
				} catch (Exception eee) {
				}

				xmlDoc.lookup("/site-menu/topmenu");

				// 각 자식 메뉴 load
				menuList = getMenuList(null, xmlDoc, null);

			} catch (Exception e) {
				e.printStackTrace();
				Logger.err.println(LOG_ID + "load menu file[" + menuFileName + "] fail. " + e.toString());
			}
		}

		Logger.info.println("", false);
		Logger.info.println(LOG_ID + "load menu info sucess.");

		WebSiteMenu site_menu = new WebSiteMenu(siteName, menuList, menuMap, menuMapForIndex, contentDirList);
		site_menu.setParam("name", siteName);
		site_menu.setParam("domain", domain);

		return site_menu;
	}

	/**
	 * 
	 * 
	 * @param menu
	 * @param parent
	 * @param parentIdx
	 * @param index
	 * @param menuMap
	 */
	void setMenuInfo(MenuItem menu, MenuItem parent, String parentIdx, int index, Map<?, ?> menuMap) {
		String idx = getMenuPathMark(parentIdx, index);

		menu.setParent(parent);
		menu.setIndexKey(idx);

		setMenuMap(menu.getUrl(), menu);

		List<?> childMenuList = menu.getChildMenuItems();

		if (childMenuList == null)
			return;

		for (int i = 0; i < childMenuList.size(); i++) {
			MenuItem child = (MenuItem) childMenuList.get(i);
			child.setDepth(parentIdx.length() / 2);

			setMenuInfo(child, menu, idx, i, menuMap);
		}

	}

	/**
	 * 
	 * 메뉴 정보를 읽어서 MenuItem 객체를 생성하고 그 List 를 반납한다.
	 * 
	 * 
	 * @param parent
	 * @param xmlDoc
	 * @param parentIdx
	 * @return
	 */
	protected List<MenuItem> getMenuList(MenuItem parent, XMLReferer xmlDoc, String parentIdx) {

		List<MenuItem> menuList = new ArrayList<>();
		int index = 0;

		while (xmlDoc.next()) {

			// <visual>false</visual> 로 되어있다면 load 하지 않는다.
			// if ("false".equals(xmlDoc.find("visual").getText()))

			// continue;
			
			String id = xmlDoc.getString("id");
			String name = xmlDoc.getString("name");
			String url = xmlDoc.find("url").getText();

			if (url == null)
				url = "UNDEFINED";

			String baseUrl = xmlDoc.find("base-url").getText();
			String auth = xmlDoc.find("auth").getText();
			String[] authLevels = jdf.framework.core.util.SmartStringArray.split(",", auth);

			String layoutName = xmlDoc.find("layout").getText();
			String mobileLayoutName = xmlDoc.find("mobile-layout").getText();
			String css = xmlDoc.find("css").getText();
			String xsl = xmlDoc.find("xsl").getText();
			String hold = xmlDoc.find("hold").getText();
			String memo = xmlDoc.find("memo").getText();
			
			if (baseUrl == null || baseUrl.length() == 0) {
				if (parent != null)
					baseUrl = parent.getBaseUrl();
				else
					baseUrl = "";
			}

			if (authLevels.length == 0 && parent != null)
				authLevels = parent.getAuthLevel();

			if (baseUrl != null && baseUrl.length() > 0 && url.indexOf("/") != 0 && url.indexOf("javascript:") != 0)
				url = baseUrl + "/" + url;

			String idx = getMenuPathMark(parentIdx, index);

			int depth = 0;

			if (parentIdx != null)
				depth = parentIdx.length() / 2;

			url = this.contextPath + url;

			MenuItem menu = new MenuItem(name, url, depth, authLevels);

			menu.setBaseUrl(baseUrl);
			menu.setIndexKey(idx);

			if ("true".equals(hold))
				menu.setHoldUrl(true);
			if (memo != null) {
				menu.setMemo(memo);
				menu.setParam("memo", memo);
			}

			menu.setParam("xsl", xsl);
			menu.setParam("auth", auth);
			// System.out.println("auth > "+auth);
			menu.setParam("css", css);
			menu.setParam("layout", layoutName);
			menu.setParam("mobile-layout", mobileLayoutName);

			// menu.setParam(WebSiteMenu.MENU_ID, idx);

			menu.setOriginPage(xmlDoc.find("publish/source-url").getText());
			menu.setGenTimeGap(getInt(xmlDoc.find("publish/time-gap").getText()));
			menu.setPageSize(getInt(xmlDoc.find("publish/min-page-size").getText()));

			// command class 세팅
			try {
				String clssNm = xmlDoc.find("command-class").getText();
				Class<?> cmdClss = Class.forName(clssNm);
				menu.setCommandClass(cmdClss);
			} catch (Exception eee) {
			}

			// 이 디렉토리에 webpage가 존재하면 동일한 메뉴로 본다.
			String contentDir = xmlDoc.find("content-dir").getText();

			if (contentDir != null && contentDir.length() > 0) {
				menu.setContentDir(contentDir);

				contentDirList.add(menu);
			}

			// <another-url> 파트 리스트를 읽어 로드
			xmlDoc.mark();
			xmlDoc.lookup("another-url");

			while (xmlDoc.next()) {
				String another_url = xmlDoc.getText();
				if (baseUrl != null && baseUrl.length() > 0 && another_url.indexOf("/") != 0
						&& another_url.indexOf("javascript:") != 0)
					another_url = baseUrl + "/" + another_url;

				setMenuMap(this.contextPath + another_url, menu);

			}
			xmlDoc.reset();

			// <param> 파트 리스트를 읽어 로드
			xmlDoc.mark();
			xmlDoc.lookup("param");

			while (xmlDoc.next()) {
				String key = xmlDoc.getString("name");
				String val = xmlDoc.getText();

				menu.setParam(key, val);
			}
			xmlDoc.reset();

			// layout 틀을 정의한다.
			Layout layout = layoutMgr.getLayout(layoutName);

			// 정의된 layout이 없으면
			// 기본 layout을 정의
			if (layout == null)
				layout = layoutMgr.getDefaultLayout();

			menu.setLayout(layout);
			menu.setCss(css);

			// menu map에 url를 키로 세팅
			setMenuMap(url, menu);

			if (id != null && id.length() > 0) {
				menu.setId(id);
				this.menuMapForIndex.put(WebSiteMenu.ID_PREFIX + id, menu);
			}

			xmlDoc.mark();
			xmlDoc.lookup("child/menu");

			List<MenuItem> childMenus = getMenuList(menu, xmlDoc, idx);
			xmlDoc.reset();

			index++;

			// 자식 세팅
			if (childMenus.size() > 0)
				menu.setChildMenuItems(childMenus);

			// 부모 세팅
			menu.setParent(parent);

			// depth 설정
			menu.setDepth(depth);

			if ("false".equals(xmlDoc.find("visual").getText())) {
				menu.setVisiable(false);
			} else
				menuList.add(menu);

		}

		return menuList;
	}
}