package jdf.framework.view.menu.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import jdf.framework.view.menu.MenuContext;
import jdf.framework.view.menu.entity.MenuItem;



public class NotEqualsTag extends BodyTagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7010944524383101976L;


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
        //System.out.println("NotEqualsTag:doStartTag");
        try
        {
            ListTag listTag= (ListTag) findAncestorWithClass(this, ListTag.class);
            MenuItem menu= null;
            if(listTag==null)
                menu = (MenuItem) pageContext.findAttribute(MenuContext.MENU);
            else

               //  list tag에 의해 현재 선택된 메뉴를 가져온다.
                menu= listTag.getMenuItem();
            
            String val = menu.getParam(attr);
            if(val==null)
                val = "";
            
            if(!val.equals(this.value))
            {
                isProcess = true;
                return EVAL_BODY_AGAIN;
            }
                
        }
        catch (Exception e)
        {
            
            throw new JspException("NotEqualsTag doStartTag error "+e.toString());
        }
        isProcess = false;
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException
    {
        //System.out.println("NotEqualsTag:doEndTag");
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
            throw new JspException("NotEqualsTag doEndTag error "+ioe.toString());
        }
        return EVAL_PAGE;
    }

}
