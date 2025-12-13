package jdf.framework.view.menu.taglibs;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import jdf.framework.core.log.Logger;
import jdf.framework.view.layout.LayoutManager;
import jdf.framework.view.layout.entity.Layout;
import jdf.framework.view.menu.MenuContext;
import jdf.framework.view.menu.bean.MenuMgrBean;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.entity.WebSiteMenu;


/**
 * J2EE 1.2 환경, 즉 Filter 기능을 쓰지 못하는 경우를 위해 만든 태그
 * 
 * @author
 * 
 */
public class HeaderTag extends BodyTagSupport implements MenuContext
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7297616840936541299L;
	
	
	private final static String LOG_ID = "<t:HeaderTag> ";

    public int doEndTag() throws JspException
    {

        try
        {
            HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();

            MenuMgrBean mgr = MenuMgrBean.getInstance(request, false);
            WebSiteMenu site = mgr.getWebSiteMenu();
            MenuItem menu = mgr.getSelectedMenuItem();

            if (menu.hasUrl())
            {
                // request.setAttribute(MenuContext.BODY_URL, urlS);
                request.setAttribute(MenuContext.SITE_MENU, site);
                request.setAttribute(MenuContext.MENU, menu);

                Layout layout = menu.getLayout();

                if (layout == null)
                {
                    LayoutManager layoutMgr = LayoutManager.getInstance();

                    layout = layoutMgr.getDefaultLayout();
                    if (layout == null)
                        throw new Exception("default layout이 존재하지 않습니다");
                }
                String tmplJsp = layout.getTemplate();
                
                String userLayoutName = request.getParameter("templet");

                if (userLayoutName != null)
                {
                    LayoutManager layoutMgr = LayoutManager.getInstance();
                    Layout lay = layoutMgr.getLayout(userLayoutName);
                    if (lay == null)
                        throw new IOException(userLayoutName + " layout이 존재하지 않습니다");

                    tmplJsp = lay.getTemplate();

                }

                request.setAttribute(MenuContext.TEMPLET_DIR, tmplJsp);

                String templetJsp = (String) this.pageContext.findAttribute(MenuContext.TEMPLET_DIR);
                this.pageContext.getRequest().setAttribute(MenuContext.HEADER, "true");
                this.pageContext.getRequest().setAttribute(MenuContext.FOOTER, "false");

                if (templetJsp == null)
                    throw new Exception("templet jsp is not defined.");

                this.pageContext.include(templetJsp);
            }

        } catch (Throwable e)
        {
            Logger.err.println(LOG_ID + "doEndTag error", e);
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

}