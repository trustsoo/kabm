package jdf.framework.view.menu.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * 
 * @author
 *
 */
public class SelectedTag extends BodyTagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7331919679078584260L;
	private boolean isProcess= false;



    public int doStartTag() throws JspException
    {
        //System.out.println("SelectedTag:doStartTag");
        try
        {
            ListTag listTag= (ListTag) findAncestorWithClass(this, ListTag.class);

            if (listTag.isSelected()) 
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
        //System.out.println("SelectedTag:doEndTag");
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
