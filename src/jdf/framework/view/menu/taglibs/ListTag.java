package jdf.framework.view.menu.taglibs;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import jdf.framework.core.log.Logger;
import jdf.framework.view.auth.PermissionException;
import jdf.framework.view.auth.User;
import jdf.framework.view.menu.MenuContext;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.entity.WebSiteMenu;


/**
 * 메뉴 list 태그
 * 
 * <pre>
 *  
 *  &lt;menu:list depth=&quot;2&quot;&gt;
 *   &lt;menu:param name=&quot;url&quot;/&gt;
 *  &lt;/menu:list&gt;
 *  
 * </pre>
 * 
 */
public class ListTag extends BodyTagSupport implements MenuContext
{
	/**
     * 
     */
	private static final long serialVersionUID = 1273581952637503306L;

	private final static String LOG_ID = "<t:ListTag> ";

	private boolean hasContent = false;

	// 메뉴의 깊이를 의미, 0부터 시작
	private int depth = 0;

	// 현재 메뉴의 위치
	private int seq = 0;

	// 사용자의 role 이름
	// 이 값이 세팅되면, 이 role 해당되는 메뉴만 보여준다.
	private String userRoleName;

	BodyContent bodyContent;

	// 사이트 전체메뉴 정보
	WebSiteMenu site_menu;

	// 보여지는 화면 menu정보
	MenuItem page_menu;

	// list상에서 선택된 메뉴
	MenuItem sel_menu;

	List menuList;

	// MenuItem의 권한을 체크하기 위한 User 구현 객체
	User user;

	HttpServletRequest request;

	public void setDepth(int depth)
	{
		this.depth = depth;
	}

	public int getDepth()
	{
		return this.depth;
	}

	/**
     * @return
     */
	public String getUserRoleName()
	{
		return userRoleName;
	}

	/**
     * @param string
     */
	public void setUserRoleName(String string)
	{
		userRoleName = string;
	}

	public boolean isSelected()
	{
		if (sel_menu == null)
			return false;
		return sel_menu.isOffspring(page_menu);
	}

	/**
     * 현재 메뉴가 처음인가?
     * 
     * @return
     */
	public boolean isFirst()
	{
		if (seq == 0)
			return true;

		return false;
	}

	/**
     * 현재 메뉴가 마지막인가?
     * 
     * @return
     */
	public boolean isLast()
	{
		if (sel_menu == null)
			return true;

		try {
			if (menuList.get(seq + 1) == null)
				return true;
			else
				return false;
		} catch (Exception e) {
			return true;
		}
	}
	
	public int getSeq()
	{
		return seq;
	}
	

	public boolean isExistChlidren()
	{
		//System.out.println("======================>"+sel_menu);
		
		
		if(sel_menu.getChildMenuItems() != null && sel_menu.getChildMenuItems().size() > 0) 
		{
			//System.out.println("======================>"+sel_menu.getChildMenuItems().size());
			return true;
		}
		
		return false;
	}
	
	/**
     * AccessibleTag에서 사용하기 위
     * 
     * @return
     * @throws PermissionException
     */
	public boolean isAccessible() throws PermissionException
	{

		this.request = (HttpServletRequest) pageContext.getRequest();
		Object tmp = request.getAttribute(USER_OBJ);

		try {
			this.user = null;
			this.user = (User) tmp;
			/*
             * if (user != null) request= (HttpServletRequest) pageContext.getRequest();
             */
		} catch (ClassCastException cce) {

			Logger.err.println(LOG_ID + "User:" + User.class.getClassLoader().getClass().getName());
			if (tmp != null)
				Logger.err.println(LOG_ID + "tmp:" + tmp.getClass().getClassLoader().getClass().getName());

		} catch (Exception e) {
			Logger.err.println(LOG_ID + "doStartTag err", e);
		}

		if (user != null) {
			user.checkPrivilege(request, (javax.servlet.http.HttpServletResponse)pageContext.getResponse(), sel_menu);
			return true;
		}

		return false;
	}

	/**
     * 현재 선택된 메뉴
     * 
     */
	public MenuItem getMenuItem()
	{
		return this.sel_menu;
	}

	public void setBodyContent(BodyContent bodyContent)
	{
		this.bodyContent = bodyContent;
	}

	public int doStartTag() throws JspException
	{
		hasContent = false;

		// System.out.println("ListTag:doStartTag");
		// Object tmp = pageContext.findAttribute(USER_OBJ);

		try {
			// if(site_menu==null)
			site_menu = (WebSiteMenu) pageContext.findAttribute(SITE_MENU);

			// if(page_menu==null)
			page_menu = (MenuItem) pageContext.findAttribute(MENU);

			ListTag listTag = (ListTag) findAncestorWithClass(this, ListTag.class);

			// list 태그에 의해 감싸진 경우라면...
			if (listTag != null) {

				// 상위 list tag의 depth가 현재 depth보다 작은경우만 처리
				if (listTag.getDepth() < this.getDepth()) {

					// 상위 list태그에 의해 선택된 menu의 자식을 looping한다.
					menuList = listTag.getMenuItem().getChildMenuItems();

					if (menuList == null)
						return SKIP_BODY;

					sel_menu = (MenuItem) menuList.get(seq);

					return EVAL_BODY_AGAIN;

				}

			}

			if (depth >= 0) {
				// HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
				// System.out.println("******************** "+depth);

				if (this.userRoleName == null)
					menuList = site_menu.getMenuList(depth, page_menu);

				// 사용자 role 이 설정되어 있다면 권한이 있는 메뉴list만 가져옮
				// 즉 권한이 있는 메뉴만 보여줌.
				else
					menuList = site_menu.getMenuList(depth, page_menu, this.userRoleName);

				sel_menu = (MenuItem) menuList.get(seq);

				return EVAL_BODY_AGAIN;
			}

		} catch (Exception e) {
			// Logger.err.println("<ListTag> E2 depth:"+depth+" "+e.toString());
		}

		sel_menu = null;
		return SKIP_BODY;

	}

	public int doAfterBody() throws JspException
	{
		// System.out.println("ListTag:doAfterBody");
		++seq;
		hasContent = true;

		try {
			sel_menu = (MenuItem) menuList.get(seq);

			return EVAL_BODY_AGAIN;

		} catch (IndexOutOfBoundsException ioe) {
			initValue();
		} catch (Exception e) {
			// Logger.err.println("<ListTag> E3 depth:"+depth+" "+e.toString());
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException
	{
		// System.out.println("ListTag:doEndTag");

		try {
			if (hasContent && bodyContent != null) {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}
		} catch (IOException e) {
			throw new JspException("Error: " + e.getMessage());
		}
		return EVAL_PAGE;
	}

	
	public boolean isAccessibleChildren() throws PermissionException
	{
		this.request = (HttpServletRequest) pageContext.getRequest();
		Object tmp = request.getAttribute(USER_OBJ);
		boolean result = false;
		try {
			this.user = null;
			this.user = (User) tmp;
			/*
             * if (user != null) request= (HttpServletRequest) pageContext.getRequest();
             */
		} catch (ClassCastException cce) {

			Logger.err.println(LOG_ID + "User:" + User.class.getClassLoader().getClass().getName());
			if (tmp != null)
				Logger.err.println(LOG_ID + "tmp:" + tmp.getClass().getClassLoader().getClass().getName());

		} catch (Exception e) {
			Logger.err.println(LOG_ID + "doStartTag err", e);
		}
		
		if (user != null) 
		{
			List childList = sel_menu.getChildMenuItems();
			MenuItem childItem = null;
			for(int idx=0; idx<childList.size();idx++)
			{		
				childItem = (MenuItem)childList.get(idx);
				try
				{
					user.checkPrivilegebyList(request, (javax.servlet.http.HttpServletResponse)pageContext.getResponse(), (MenuItem)sel_menu.getChildMenuItems().get(idx));
					return true;
				} catch(Exception ex)
				{
					Logger.debug.println(sel_menu.getChildMenuItems().get(idx)+" is not accessiable");
				}
			}
		}
		return false;
		
	}
	
	
	public boolean isSecureMenu()
	{	
		String auth = sel_menu.getAuthLevels();
		if(auth != null && "S".equals(auth))
			return true;
		
		return false;
	}
	
	
	public boolean isVisiable()
	{
		boolean isView = sel_menu.isVisiable();
		return isView;
	}
	
	
	
	/**
     * 
     * tomcat에서는 이 메쏘드가 호출이 안됨....ㅎㅎ
     * 
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
	public void release()
	{
		// System.out.println("ListTag:release");
		super.release();

		// this.seq = 0;
		// this.page_menu = null;
		// this.sel_menu = null;
	}

	private void initValue()
	{
		this.seq = 0;
		this.page_menu = null;
		this.sel_menu = null;
	}

}
