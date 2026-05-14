package jdf.framework.view.menu.entity;

import jdf.framework.view.menu.WebPagePublisher;
import jdf.framework.view.menu.util.PartialSearcher;

import java.util.*;


/**
 * 
 * 사이트의 논리적 단위의 메뉴정보를 가지고 있는다. 즉 한 사이트내에서도 관리화면의 메뉴, 사용자 화면의 메뉴와 같이 분리될수 있는데, 그중
 * 한 단위의 전체메뉴정보를 가지고 있는다.
 * 
 * @author
 */
public class WebSiteMenu extends HashMap<String, String> {

	public static final String ID_PREFIX = "idx:";

	private static final long serialVersionUID = -7160440182243909566L;

	private final static List<MenuItem> EMPTY_LIST = new ArrayList<>();

	private static MenuItem defaultMenu = new MenuItem();

	private String name;

	// top menu list
	private List<MenuItem> menuList;

	// url을 키로 가지는 menu map
	private Map<String, MenuItem> menuMap;

	// index key를 키로 가지는 menu map
	private Map<String, MenuItem> menuMapForIndex;

	// 해당 url 정보외 content dir 정보를 가지는 menu list
	// conent dir은 이 디렉토리에 화일이 존재하면 같은 메뉴로 본다.
	private List<MenuItem> contentDirList;

	private PartialSearcher searcher = null;

	private WebPagePublisher publisher;

	public WebSiteMenu(String name, List<MenuItem> menuList, Map<String, MenuItem> menuMap, Map<String, MenuItem> menuMapForIndex, List<MenuItem> contentDirList) {
		super();

		this.name = name;
		this.menuList = menuList;
		this.menuMap = menuMap;
		this.menuMapForIndex = menuMapForIndex;
		this.contentDirList = contentDirList;

		this.searcher = new PartialSearcher(menuMap);
		
		
	}

	public String getName() {
		return name;
	}

	public List<MenuItem> getMenuList() {
		return menuList;
	}

	public MenuItem getMenu(String key) {
		MenuItem menu = menuMap.get(key);

		if (menu == null)
			menu = defaultMenu;

		return menu;
	}

	/**
	 * Index 값에의해 MenuItem 정보를 가져온다.
	 * 
	 * @param idx
	 * @return
	 */
	public MenuItem getMenuByIndex(String idx) {
		return (MenuItem) menuMapForIndex.get(idx);
	}
	
	
	
	public List<MenuItem> getMenuListById(Object key)
	{
		List<MenuItem> list = new ArrayList<MenuItem>();
		
		Iterator<String> ite = menuMap.keySet().iterator();
		String _key = null;
		MenuItem item = null;
		while(ite.hasNext())
		{
			_key = ite.next();			
			item = menuMap.get(_key);
			
			//System.out.println(item);
			
			if(item.getId().startsWith((String)key))
			{				
				list.add(item);
			}
		}
		
		
		return list;
	}
	

	/**
	 * id값으로 MenuItem을 찾아 반환한다.
	 * 
	 * @param id menu ID값
	 * @return MenuItem
	 */
	public MenuItem getMenuById(String id) {
		return (MenuItem) menuMapForIndex.get(ID_PREFIX + id);
	}

	/**
	 * 가정 /board/list.jsp?bid=1 과 같은 요청이 있을시
	 * 
	 * full url은 /board/list.jsp?bid=1 이고, uri는 /board/list.jsp 라고 정의한다.
	 * 
	 * 
	 * client로 부터 요청받은 full url정보를 이용하여 메뉴를 찾고, 해당 정보가 없을시, uri로 부분검색을 하여 메뉴를
	 * 찾는다.
	 * 
	 */
	public MenuItem getMenu(javax.servlet.http.HttpServletRequest request, String fullUrl, String uri) {
		MenuItem menu = null;

		// 우선 index 값으로 검색
		String menuIdx = request.getParameter(MenuItem.MENU_IDX_KEY);

		if (menuIdx != null)
			menu = (MenuItem) menuMapForIndex.get(menuIdx);

		if (menu == null)
			menu = (MenuItem) menuMap.get(fullUrl);

		if (menu == null) {
			Object[] result = this.searcher.match(uri);

			if (result == null || result.length == 0) {
				// 검색결과 존재하지 않으면
				// content dir에 검색
				for (int i = 0; i < contentDirList.size(); i++) {
					MenuItem dirMenu = (MenuItem) contentDirList.get(i);

					String dirInfo = dirMenu.getContentDir();

					if (uri.indexOf(dirInfo) == 0)
						return dirMenu;
				}

				return defaultMenu;
			}

			for (int i = result.length - 1; i > -1; i--)
			// for (int i= 0; i < result.length; i++)
			{
				String menuUrl = (String) result[i];
				if (fullUrl.indexOf(menuUrl) == 0)
					return (MenuItem) menuMap.get(menuUrl);

			}

			menu = defaultMenu;

		}

		return menu;
	}
	
	/**
	 * 가정 /board/list.jsp?bid=1 과 같은 요청이 있을시
	 * 
	 * full url은 /board/list.jsp?bid=1 이고, uri는 /board/list.jsp 라고 정의한다.
	 * 
	 * 
	 * client로 부터 요청받은 full url정보를 이용하여 메뉴를 찾고, 해당 정보가 없을시, uri로 부분검색을 하여 메뉴를
	 * 찾는다.
	 * 
	 */
	public MenuItem getMenu(javax.servlet.http.HttpServletRequest request, String fullUrl, String uri, String tenant_id) {
		MenuItem menu = null;

		// 우선 index 값으로 검색
		String menuIdx = request.getParameter(MenuItem.MENU_IDX_KEY);

		if (menuIdx != null)
			menu = (MenuItem) menuMapForIndex.get(menuIdx);

		if (menu == null)
			menu = (MenuItem) menuMap.get(tenant_id+fullUrl);

		if (menu == null) {
			Object[] result = this.searcher.match(tenant_id+uri);

			if (result == null || result.length == 0) {
				// 검색결과 존재하지 않으면
				// content dir에 검색
				for (int i = 0; i < contentDirList.size(); i++) {
					MenuItem dirMenu = (MenuItem) contentDirList.get(i);

					String dirInfo = dirMenu.getContentDir();

					if (uri.indexOf(dirInfo) == 0)
						return dirMenu;
				}

				return defaultMenu;
			}

			for (int i = result.length - 1; i > -1; i--)
			// for (int i= 0; i < result.length; i++)
			{
				String menuUrl = (String) result[i];
				if ((tenant_id+fullUrl).indexOf(menuUrl) == 0)
					return (MenuItem) menuMap.get(menuUrl);

			}

			menu = defaultMenu;

		}

		return menu;
	}
	
	
	
	
	

	public List<MenuItem> getMenuList(int depth, MenuItem menu) {
		// topmenu인 경우
		if (depth == 0)
			return menuList;

		return getMenuList(depth, menu, menuList);
	}

	public List<MenuItem> getMenuList(int depth, MenuItem menu, jdf.framework.view.auth.User user) {
		return getMenuList(depth, menu, user.getAuthLevel());
	}

	public List<MenuItem> getMenuList(int depth, MenuItem menu, String userAuthLevel) {
		List<MenuItem> tmpMenuList = null;

		if (depth == 0)
			tmpMenuList = menuList;
		else if (depth > 0)
			tmpMenuList = getMenuList(depth, menu);
		else
			return null;

		List<MenuItem> newMenuList = new ArrayList<MenuItem>();
		for (int i = 0; tmpMenuList != null && i < tmpMenuList.size(); i++) {
			MenuItem selMenu = (MenuItem) tmpMenuList.get(i);

			if (selMenu.hasAuthLevel(userAuthLevel))
				newMenuList.add(selMenu);

		}

		return newMenuList;
	}

	private List<MenuItem> getMenuList(int depth, MenuItem menu, List mnList) {
		List<MenuItem> tmpList = null;

		for (int i = 0; i < mnList.size(); i++) {
			MenuItem mn = (MenuItem) mnList.get(i);

			// System.out.println(mn.getName());

			if (mn.isOffspring(menu)) {

				tmpList = mn.getChildMenuItems();

				// System.out.println("테스트2");

				if (depth == 1) {
					// System.out.println("테스트 3");
					return tmpList;

				}

				else {
					// System.out.println("테스트 ********** 3 ");
					return getMenuList(--depth, menu, tmpList);
				}

			}
		}

		if (mnList != null && mnList.size() > 0) {
			MenuItem mn = (MenuItem) mnList.get(0);

			return mn.getChildMenuItems();
		} else
			return EMPTY_LIST;
	}

	public void setParam(String key, String val) {
		super.put(key, val);
	}

	public String getParam(String key) {
		return (String) super.get(key);
	}

	/**
	 * Website의 메뉴기능과 연관된 WebPagePublisher를 얻는다.
	 * 
	 * @return
	 */
	public WebPagePublisher getPublisher() {
		return this.publisher;
	}

	/**
	 * Website의 메뉴와 연관되는 WebPagePublisher를 설정한다.
	 * 
	 * @param pub
	 */
	public void setPublisher(WebPagePublisher pub) {
		this.publisher = pub;
	}

	/**
	 * menu 3번째 depth 이고 1번째 depth의 메뉴를 요구하면 menu의 부모 menu중에서 1번째 메뉴를 보내준다.
	 * 
	 * 
	 * 
	 */
	/*
	 * private MenuItem getMenuItem(MenuItem menu, int depth) throws
	 * ArrayIndexOutOfBoundsException { int menuDepth = menu.getDepth();
	 * 
	 * if(depth==menuDepth) return menu;
	 * 
	 * else if(depth< menuDepth) { MenuItem parent = menu.getParent();
	 * 
	 * if(parent == null) return null;
	 * 
	 * return getMenuItem( parent, depth); } else throw new
	 * ArrayIndexOutOfBoundsException("메뉴 depth 초과"); }
	 */

}
