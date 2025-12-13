package jdf.framework.view.menu.entity;

import java.util.*;

import jdf.framework.view.auth.User;
import jdf.framework.view.layout.entity.*;


/**
 * Web의 page(htm/jsp,uri)를 의미하는 모든 정보는 가지고 있는 Entity 객체
 * 
 * 
 * @see WebSiteMenu
 */

public class MenuItem extends HashMap<Object, Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4905317835425935056L;

	public static final String MENU_IDX_KEY = "menu_idx";
	
	public static final String MULTI_TENANT_KEY = "tenant_id";
	

	public static final String GUEST_LEVEL = "guest";

	protected final static String[] GUEST_LEVEL_LIST = new String[] { GUEST_LEVEL };

	// 다른 MenuItem과 구별할 수 있는 Index Key
	private String indexKey = "-1";

	// 메뉴명
	protected String name;

	// full URL
	protected String url;

	// 메뉴 깊이
	protected int depth;

	// 인증 레벨
	protected String[] authLevel;

	protected String authLevels;

	// base URL
	protected String baseUrl;

	// 자식 menu Item
	protected List<MenuItem> childMenuItems;

	// 보이는 속성
	protected boolean visiable = true;

	// 부모 MenuItem
	private MenuItem parent;

	// layout
	private Layout layout;

	// css
	private String css;

	// xslt용 xsl 화일명
	private String xsl;

	// 실제 directory 경로 정보
	// 이 디렉토리에 화일이 존재하면 같은 메뉴로 본다.
	private String contentDir;

	private Class<?> commandClass;

	// 이 메뉴 ITEM은 항상 유지 되어야 하는가?
	// 동일 URL 정보가 들어오느 경우, 이 메뉴아이템을 덮어쒸울것인지,
	// 아니면 그대로 유지할것인 결정하기 위한 option
	private boolean hold = false;

	private String memo = "";

	// 이 MenuItem을 의미하는 유일한 key이며, 메뉴의 이동시나 변경에도 관계없이 유지되는 key
	private String uniqueKey;
	
	
	private String parentMenuId;
	
	
	private String menuAlias;
	
	
	private String popup_yn;
	
	
	private String tenant_id;
	
	private String log_yn;
	
	
	public MenuItem() {
		this("UNDEFINED", "");
	}

	public MenuItem(String name, String url) {
		this(name, url, 0, GUEST_LEVEL_LIST);
	}

	public MenuItem(String name, String url, int depth, String[] authLevel) {
		super();
		// this.name= name;
		// this.url = url;
		// this.depth = depth;
		this.authLevel = authLevel;
		
		setAuthLevels(getAuthLevels());
		setName(name);
		setUrl(url);
		setDepth(depth);

		// System.out.println("Menu DEBUG >>>>>>>>>>>> "+url);

	}

	/**
	 * 이 MenuItem의 ID 값을 가져온다.
	 * ID 값을 sitemap 내에서 유일해야 되는 값이며, 메뉴 이동시도 유지되는 값이다.
	 * 이 ID 값은 수작업으로 sitemap을 수정하여, sitemap.xml 의 menu 내에 id값이 없는 경우
	 * null 값이 return 된다.
	 * @return ID값
	 */
	public String getId() {
		return uniqueKey;
	}

	/**
	 * MenuItem의 id값을 설정한다..
	 * 
	 * @param id 아이디값
	 */
	public void setId(String id) {
		this.uniqueKey = id;
		super.put("uniqueKey", id);
	}

	/**
	 * url 정보를 가지고 있는가?
	 * 
	 * @return url정보 존재여부
	 */
	public boolean hasUrl() {
		try {
			if (this.url.length() > 0)
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	private void setName(String name) {
		this.name = name;
		super.put("name", name);
	}

	/**
	 * URL 정보를 설정
	 * 
	 * @param url
	 *            URL
	 */
	public void setUrl(String url) {
		this.url = url;
		// this.url = HtmlUtil.translate(url);
		super.put("url", this.url);
	}

	/**
	 * 메뉴의 depth 정보를 설정한다.
	 * 
	 * @param depth
	 *            menu depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * 메뉴 index 키를 설정한다. index키는 메뉴마다 자동으로 부여되는 고유키지만, 메뉴위치 변동시 이 key값은 변경될 수
	 * 있다.
	 * 
	 * @param indexKey
	 *            index키
	 */
	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
		super.put("idx", indexKey);
		super.put(MENU_IDX_KEY, indexKey);
	}

	/**
	 * 메뉴 index 키값을 가져온다. index키는 메뉴마다 자동으로 부여되는 고유키지만, 메뉴위치 변동시 이 key값은 변경될 수
	 * 있다.
	 * 
	 * @return
	 */
	public String getIndexKey() {
		return indexKey;
	}

	/**
	 * 속성의 설정한다.
	 * 
	 * @param key
	 *            속성명
	 * @param val
	 *            속성값
	 */
	public void setParam(String key, String val) {
		super.put(key, val);
	}

	/**
	 * 속성값을 설정한다.
	 * 
	 * @param key
	 *            속성키
	 * @return 속성값
	 */
	public String getParam(String key) {
		return (String) super.get(key);
	}

	/**
	 * 부모 메뉴의 속성값을 가져온다. 만약 부모 속성의 값이 없으면, 그 부모의 속성을 재귀적으로 호출하여 값을 찾는다.
	 * 
	 * @param key
	 *            속성키
	 * @return 속성값
	 */
	public String getParentParam(String key) {
		String val = getParam(key);

		if (val == null) {
			if (parent != null)
				return parent.getParentParam(key);
		}

		return val;
	}

	/**
	 * 권한 레벨정보를 없는다.
	 * 
	 * @return 권한레벨정보
	 */
	public String getAuthLevels() {
		if (authLevel == null || authLevel.length == 0)
			return null;
		else if (this.authLevels == null) {
			authLevels = "";

			for (int i = 0; i < this.authLevel.length; i++) {
				if (i != 0)
					this.authLevels = this.authLevels + ",";

				this.authLevels = this.authLevels + this.authLevel[i];
			}
		}

		return this.authLevels;
	}
	
	public void setAuthLevels(String authLevels) {
		super.put("auth", authLevels);
	}

	/**
	 * , 로 분리된 권한정보를 배열로 반환한다.
	 * 
	 * @return 권한정보
	 */
	public String[] getAuthLevel() {
		if (authLevel == null) {
			if (parent != null)
				authLevel = parent.getAuthLevel();

			else
				authLevel = GUEST_LEVEL_LIST;
		}

		return authLevel;
	}

	/**
	 * 
	 * 현재 이 메뉴가 가지고 있는 권한 레벨에서 입력값으로 들어온 레벨이 있는지 검색
	 * 
	 */
	public boolean hasAuthLevel(User user) {
		if (this.authLevel.length == 0)
			return true;

		String userAuthLevel = user.getAuthLevel();

		for (int i = 0; i < this.authLevel.length; i++) {
			String crntAuthLevel = this.authLevel[i];

			if (crntAuthLevel.equals(userAuthLevel)
					|| crntAuthLevel.equals(GUEST_LEVEL))
				return true;
		}
		return false;
	}

	/**
	 * 사용자의 authlevel(즉 role 이름)을 가져와 현재 이 메뉴의 권한이 있는지 없는지를 판단한다.
	 * 
	 * @param userAuthLevel
	 * @return
	 */
	public boolean hasAuthLevel(String userAuthLevel) {
		if (this.authLevel.length == 0)
			return true;

		for (int i = 0; i < this.authLevel.length; i++) {
			String crntAuthLevel = this.authLevel[i];

			if (crntAuthLevel.equals(userAuthLevel)
					|| crntAuthLevel.equals(GUEST_LEVEL))
				return true;
		}
		return false;
	}

	/**
	 * 메뉴명을 가져온다.
	 * 
	 * @return 메뉴명
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the baseUrl.
	 * 
	 * @return String
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Returns the childMenuItems.
	 * 
	 * @return List
	 */
	public List<MenuItem> getChildMenuItems() {
		return childMenuItems;
	}

	/**
	 * Returns the depth.
	 * 
	 * @return int
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Returns the parent.
	 * 
	 * @return MenuItem
	 */
	public MenuItem getParent() {
		return parent;
	}

	/**
	 * Returns the url.
	 * 
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the baseUrl.
	 * 
	 * @param baseUrl
	 *            The baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Sets the childMenuItems.
	 * 
	 * @param childMenuItems
	 *            The childMenuItems to set
	 */
	public void setChildMenuItems(List<MenuItem> childMenuItems) {
		this.childMenuItems = childMenuItems;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            The parent to set
	 */
	public void setParent(MenuItem parent) {
		this.parent = parent;
	}

	/**
	 * Returns the visiable.
	 * 
	 * @return boolean
	 */
	public boolean isVisiable() {
		return visiable;
	}

	/**
	 * Sets the visiable.
	 * 
	 * @param visiable
	 *            The visiable to set
	 */
	public void setVisiable(boolean visiable) {
		this.visiable = visiable;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setCss(String css) {
		this.css = css;
		super.put("css", css);
	}

	public String getCss() {
		return this.css;
	}

	/**
	 * 자손 메뉴인지 여부를 판단한다. 자신과 직계인지 여부 판단.
	 * 
	 * indexKey로 판별하는데. 가령 현재 이 메뉴의 indexKey가 0010이면 자손인지 판단을 원하는 메뉴의 인텍스 키가
	 * 001000이면 0001000에서 0010이 첫번째 위치하는 지 판단한다.
	 */
	public boolean isOffspring(MenuItem menu) {
		if (menu == null)
			return false;

		if (menu.getIndexKey().indexOf(this.indexKey) == 0)
			return true;
		/*
		 * else if(this.indexKey.indexOf(menu.getIndexKey()) == 0) return true;
		 */
		else
			return false;
	}

	/***************************************************************************
	 * publish part
	 * 
	 * 
	 */

	// publish 할 원본 페이지
	private String originPage;

	// publish 시간간격
	private long publishTimeGap;

	// 화일 생성 시간
	private long lastPublishTime;

	public void setOriginPage(String page) {
		this.originPage = page;
	}

	public String getOriginPage() {
		return this.originPage;
	}

	public void setGenTimeGap(int time) {
		this.publishTimeGap = time * 1000;
	}

	public long getPublishTimeGap() {
		return this.publishTimeGap;
	}

	public void checkPublishTime() {
		this.lastPublishTime = System.currentTimeMillis();
	}

	/**
	 * Returns the lastPublishTime.
	 * 
	 * @return long
	 */
	public long getLastPublishTime() {
		return lastPublishTime;
	}

	// 기본 페이지 사이즈
	private int pageSize = 10 * 1000; // 10k

	public void setPageSize(int kilo) {
		this.pageSize = kilo * 1000;
	}

	public int getPageSize() {
		return pageSize;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();		
		buf.append("name:").append(name);
		buf.append(" id:").append(uniqueKey);
		buf.append(" indexkey:").append(indexKey);
		buf.append(" url:").append(url);
		buf.append(" depth:").append(depth);
		buf.append(" log_yn:").append(log_yn);
		buf.append(" authLevelList:");

		for (int i = 0; i < this.authLevel.length; i++) {
			buf.append(this.authLevel[i]);

			if (i != this.authLevel.length - 1)
				buf.append(",");
		}

		return buf.toString();
	}

	/**
	 * @return
	 */
	public String getXsl() {
		return xsl;
	}

	/**
	 * @param string
	 */
	public void setXsl(String string) {
		xsl = string;
	}

	/**
	 * 
	 * 이 메뉴정보를 가질수 있는 화일들의 디렉토리 정보를 return
	 * 
	 * @return
	 */
	public String getContentDir() {
		return contentDir;
	}

	/**
	 * 
	 * 이 메뉴정보를 가질수 있는 화일들의 디렉토리 정보를 set
	 * 
	 * @param string
	 */
	public void setContentDir(String string) {
		contentDir = string;
	}

	/**
	 * @return
	 */
	public Class<?> getCommandClass() {
		return commandClass;
	}

	/**
	 * @param class1
	 */
	public void setCommandClass(Class<?> class1) {
		commandClass = class1;
	}

	public boolean isHoldUrl() {
		return this.hold;
	}

	public void setHoldUrl(boolean h) {
		this.hold = h;
	}

	/**
	 * 이 메뉴에 관련된 memo 내용을 세팅
	 * 
	 * @param m
	 *            메모
	 */
	public void setMemo(String m) {
		this.memo = m;
	}

	/**
	 * 이 메뉴에 관련되 memo 내용을 return
	 * 
	 * @return 메모정보
	 */
	public String getMemo() {
		return this.memo;
	}
	
	public String getMenuAlias()
	{
		return this.menuAlias;
	}
	
	public void setMenuAlias(String menuAlias)
	{
		this.menuAlias = menuAlias;
	}
	
	public String getPopupYN()
	{
		return popup_yn;
	}
	
	public void setPopupYN(String popup_yn)
	{
		this.popup_yn = popup_yn;
		super.put("popup_yn", popup_yn);
	}
	
	
	public void setParentMenuId(String parentMenuId)
	{
		this.parentMenuId = parentMenuId;
	}
	
	public String getParentMenuId()
	{
		return parentMenuId;
	}
	
	public void setTenantId(String tenant_id)
	{
		this.tenant_id=tenant_id;
	}
	
	public String getTeanantId()
	{
		return tenant_id;
	}
	
	public void setLogYn(String log_yn)
	{
		this.log_yn = log_yn;
	}
	
	public String getLogYn()
	{
		return log_yn;
	}
	
	

}
