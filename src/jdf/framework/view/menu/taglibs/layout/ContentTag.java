package jdf.framework.view.menu.taglibs.layout;

import jdf.framework.core.log.Logger;
import jdf.framework.view.menu.MenuContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;


public class ContentTag extends BodyTagSupport implements MenuContext
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8383152954249530341L;

	private final static String LOG_ID="<t:ContentTag> ";
    
    private final static String MSG ="This is anyTEMPLET's content tag message.<br/>"
                                       +" CONTENT AREA ";
    
   
    public int doEndTag() throws JspException
    {

        try
        {
            Object val = pageContext.findAttribute(BODY_CONTENT);
            
            if(val==null)
                this.pageContext.getOut().print(MSG);
            else    
                this.pageContext.getOut().print(val);
				//this.pageContext.getResponse().getWriter().print(val);
            
        }
        catch(IOException ioe)
        {
            Logger.warn.println(LOG_ID+"doEndTag",ioe);
            //throw new JspException(ioe.getMessage());
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            Logger.warn.println(LOG_ID+"doEndTag",e);
            //throw new JspException(e.getMessage());
        }   
        
        
        return EVAL_PAGE;
    }

}
