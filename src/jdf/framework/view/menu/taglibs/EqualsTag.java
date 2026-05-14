package jdf.framework.view.menu.taglibs;

import jdf.framework.view.menu.entity.MenuItem;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;


/**
 * 
 * 
 * @author
 *
 */
public class EqualsTag extends BodyTagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6381807316181563899L;


	private boolean isProcess= false;


     private String attr;

     private String value;

     public void setAttr(String attr)
     {
         this.attr= attr;
     }

     public void setValue(String value)
     {
         this.value= value;
     }




    public int doStartTag() throws JspException
    {
        //System.out.println("EqualsTag:doStartTag");
        try
        {
            ListTag listTag= (ListTag) findAncestorWithClass(this, ListTag.class);

            // list tag에 의해 현재 선택된 메뉴를 가져온다.
            MenuItem menu= listTag.getMenuItem();
            
            String val = menu.getParam(attr);
            if(val==null)
                val = "";
            
            if(val.equals(this.value))
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
        //System.out.println("EqualsTag:doEndTag");
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