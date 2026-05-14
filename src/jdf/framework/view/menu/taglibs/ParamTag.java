package jdf.framework.view.menu.taglibs;

import jdf.framework.core.log.Logger;
import jdf.framework.view.menu.MenuContext;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.entity.WebSiteMenu;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;


/**
 * 
 * 메뉴의 속성을 출력하는 tag 라이브러
 * 
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-3-05 오전 9:40:36
 *  
 */
public class ParamTag extends TagSupport implements MenuContext
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3269166031795442429L;
	private final static String LOG_ID = "<t:ParamTag> ";
    private String attr;

    private String scope;
    
    private boolean chkParent = false;

    
    /**
     * 속성명을 설정
     * @param attr
     */
    public void setAttr(String attr)
    {
        this.attr = attr;
    }

    public void setCheckParent(boolean chk)
    {
        this.chkParent = chk;
    }

    
    
    /**
     * 속성을 찾을 범위를 결정
     * page 와 site 2가지가 있다.
     * 
     * @param scope
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }

    /**
     * 속성값을 화면에 출력한다.
     *
     */
    private void printPageMenu()
    {

        try
        {
            String val = null;

            MenuItem page_menu = (MenuItem) pageContext.findAttribute(MENU);

            if(chkParent)
                val = page_menu.getParentParam(this.attr);
            else
                val = page_menu.getParam(this.attr);

            if (val != null)
                pageContext.getOut().print(val);
        } catch (NullPointerException ne)
        {
        } catch (IOException ioe)
        {

            //throw new JspException("Error: IOException while writing to
            // client ");
        }
    }

    private void printSiteMenu()
    {

        try
        {
            String val = null;

            WebSiteMenu site_menu = (WebSiteMenu) pageContext.findAttribute(SITE_MENU);

            val = site_menu.getParam(this.attr);

            if (val != null)
                pageContext.getOut().print(val);
        } catch (NullPointerException ne)
        {
            //ne.printStackTrace();
            Logger.warn.println(LOG_ID+"WebSiteMenu not set");
        }

        catch (IOException ioe)
        {

            //throw new JspException("Error: IOException while writing to
            // client ");
        }
    }

    public int doStartTag() throws JspException
    {
        if ("page".equals(scope))
        {
            printPageMenu();
            return SKIP_BODY;
        } else if ("site".equals(scope))
        {
            printSiteMenu();
            return SKIP_BODY;
        }

        try
        {
            ListTag listTag = (ListTag) findAncestorWithClass(this, ListTag.class);

            // list tag에 의해 현재 선택된 메뉴를 가져온다.
            MenuItem menu = listTag.getMenuItem();

            // menu에서 해당 attrbute를 출력한다.
            if (menu != null && menu.getParam(attr) != null)
                pageContext.getOut().print(menu.getParam(attr));
        } catch (Exception e)
        {

            //e.printStackTrace();

            printPageMenu();
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }
}