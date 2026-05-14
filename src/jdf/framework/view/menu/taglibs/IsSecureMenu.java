/*
 * Created on 2004-03-03
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.view.menu.taglibs;

import jdf.framework.view.menu.MenuContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;


/**
 * 
 * Menu의 Secure 여부 판단
 * 
 * @author advan94, advan94@weandsoft.com
 * @version 1.0
 * @since 2010-04
 * 
 */
public class IsSecureMenu extends BodyTagSupport implements MenuContext
{

	
	private boolean isProcess = false;

	public int doStartTag() throws JspException
	{
		//System.out.println("IsLastTag:doStartTag");
		try
		{
			ListTag listTag = (ListTag) findAncestorWithClass(this, ListTag.class);

			if (listTag.isSecureMenu())
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
		//System.out.println("IsLastTag:doEndTag");
		try
		{
			if (isProcess)
			{
				BodyContent bc = this.getBodyContent();
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
