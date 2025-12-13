package jdf.framework.view.menu.dao;

import java.util.List;

import jdf.framework.view.menu.entity.WebSiteMenu;


/**
 * 
 * MenuDao
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface MenuDao
{

    /**
     * Sitemap 설정파일의 이름
     * 
     * @param source
     */
	public void setSourceInfo(String source);

    /**
     * WebSiteMenu명을 가져온다.
     * 
     * @return
     */
	public WebSiteMenu getWebSiteMenu();

    /**
     * Publishing 할 MenuList를 가져온다.
     * 
     * @return
     */
	public List getPublishMenuList();
    
    
    /**
     * Context Path 를 설정한다.
     * 
     * @param contextPath
     */
    public void setContextPath(String contextPath);

}
