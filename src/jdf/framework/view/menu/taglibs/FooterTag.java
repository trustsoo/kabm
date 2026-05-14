package jdf.framework.view.menu.taglibs;

import jdf.framework.view.menu.MenuContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * J2EE 1.2 환경, 즉 Filter 기능을 쓰지 못하는 경우를 위해 만든 태그
 * 
 * @author
 * 
 */
public class FooterTag extends BodyTagSupport implements MenuContext
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5718923895526133247L;

    public int doEndTag() throws JspException
    {
        /*
        if(this.pageContext.getResponse().isCommitted())
            return EVAL_PAGE;
        */
        try
        {
            String templetJsp = (String) this.pageContext.findAttribute(MenuContext.TEMPLET_DIR);

            if (templetJsp != null)
            {
                this.pageContext.getRequest().setAttribute(MenuContext.HEADER, "false");
                this.pageContext.getRequest().setAttribute(MenuContext.FOOTER, "true");

                this.pageContext.include(templetJsp);
            }
        } catch (Throwable e)
        {
            //Logger.err.println(LOG_ID + "doEndTag error", e);
            //throw new JspException(e);
        }

        return EVAL_PAGE;
    }

}
