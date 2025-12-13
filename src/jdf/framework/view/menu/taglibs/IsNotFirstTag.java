package jdf.framework.view.menu.taglibs;

import java.io.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * 해당 메뉴가 해당 depth에서 처음인지 여부를 판단한다.
 * 
 * 
 */
public class IsNotFirstTag extends BodyTagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5502088029024545960L;
	private boolean isProcess= false;



	public int doStartTag() throws JspException
	{
		//System.out.println("IsLastTag:doStartTag");
		try
		{
			ListTag listTag = (ListTag) findAncestorWithClass(this, ListTag.class);

			if (!listTag.isFirst())
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