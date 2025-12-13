package jdf.framework.view.menu.taglibs.layout;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import jdf.framework.core.log.Logger;
import jdf.framework.view.menu.MenuContext;


/**
 * <layout:body/> : page의 body 태그 안의 내용을 표기한다.
 * 
 * 
 * @author
 * 
 */
public class BodyTag extends BodyTagSupport implements MenuContext
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -501461573763836379L;

	private final static String LOG_ID = "<t:BodyTag> ";

	public int doEndTag() throws JspException
	{

		try {
			Object val = pageContext.findAttribute(BODY_CONTENT);

			if (val != null) {

				String content = (String) val;

				int stx = content.indexOf("<body>");
				if (stx > 0) {
					stx = stx + 6;
					int etx = content.lastIndexOf("</body");
					//int etx = content.indexOf("</body>", stx);
					String body = content.substring(stx, etx);
					this.pageContext.getOut().print(body);
				}
				else 
					this.pageContext.getOut().print(content);

			}

		} catch (IOException ioe) {
			Logger.warn.println(LOG_ID + "doEndTag", ioe);
			// throw new JspException(ioe.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
			Logger.warn.println(LOG_ID + "doEndTag", e);
			// throw new JspException(e.getMessage());
		}

		return EVAL_PAGE;
	}

}