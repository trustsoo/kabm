package jdf.framework.view.menu.taglibs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;


/**
 * 
 * @author
 *
 */
public class UnselectedTag extends BodyTagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 439306471089697796L;
	private boolean isProcess= false;



    public int doStartTag() throws JspException
    {
        try
        {
            ListTag listTag= (ListTag) findAncestorWithClass(this, ListTag.class);

            if (!listTag.isSelected()) 
            {
                isProcess = true;
                return EVAL_BODY_AGAIN;
            }
         
        }
        catch (Exception e)
        {
            throw new JspException("Error: IOException while writing to client ");
        }
        isProcess = false;
        return SKIP_BODY;
    }



    public int doEndTag() throws JspException
    {
        try
        {
            if (isProcess)
            {
                BodyContent bc= this.getBodyContent();
                bc.writeOut(bc.getEnclosingWriter());
            }

        }
        catch (IOException ioe)
        {
            throw new JspException("Error: IOException while writing to client ");
        }
        return EVAL_PAGE;
    }

}