package jdf.framework.view.menu.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 메뉴리스트에서 구분선을 넣을지 여부 판단.
 * 
 * @author
 * sequence
 */
public class ISSeparator extends BodyTagSupport
{
	
	private boolean isProcess = false;

	public int doStartTag() throws JspException
	{
		try
		{
			ListTag listTag = (ListTag) findAncestorWithClass(this, ListTag.class);

			
			if(listTag.isFirst())
			{
				isProcess = false;
		
			} else if(listTag.isLast())
			{
				isProcess = false;		
			} else
			{
				//if(listTag.getSeq()-1 > 0 && (listTag.getSeq()-1) % 5 == 0)
				if(listTag.getSeq() % 5 == 0)
				{
					isProcess = true;
					return EVAL_BODY_AGAIN;
				} else
				{
					isProcess = false;
				}
				
			}
			
			
			
			/*if (listTag.isFirst())
			{
				isProcess = true;
				return EVAL_BODY_AGAIN;
			}*/

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