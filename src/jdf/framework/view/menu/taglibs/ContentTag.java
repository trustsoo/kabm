package jdf.framework.view.menu.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import jdf.framework.core.log.Logger;
import jdf.framework.view.menu.MenuContext;



public class ContentTag extends BodyTagSupport implements MenuContext
{
    private final static String LOG_ID="<at:ContentTag> ";
    
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
