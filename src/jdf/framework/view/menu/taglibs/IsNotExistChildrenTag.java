/*
 * Created on 2004-03-03
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.view.menu.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import jdf.framework.view.auth.PermissionException;
import jdf.framework.view.menu.MenuContext;


/**
 * 
 * 현재 메뉴의 하위 메뉴가 존재하지 않는지 체크
 * 
 * @author
 * @version 1.0
 * @since 2004-03-03 오전 10:36:00
 * 
 */
public class IsNotExistChildrenTag extends BodyTagSupport implements MenuContext
{	
	private boolean isProcess= false;

    public int doStartTag() throws JspException
    {
        //System.out.println("SelectedTag:doStartTag");
        try
        {
            ListTag listTag= (ListTag) findAncestorWithClass(this, ListTag.class);

            if (!listTag.isExistChlidren())
            {
                isProcess= true;
                return EVAL_BODY_AGAIN;
            }

        }
        catch (PermissionException pe)
        {
            isProcess= true;
            return EVAL_BODY_AGAIN;

        }
        catch (Exception e)
        {
            throw new JspException("Error: IOException while writing to client ");

        }
        isProcess= false;
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