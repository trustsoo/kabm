package jdf.framework.view.menu.taglibs.layout;

import jdf.framework.core.log.Logger;
import jdf.framework.view.menu.MenuContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;


/**
 * <layout:head/> : page html 내에서 title을 제외한 head 부분을 표기한다.
 * 
 * 
 * @author
 * 
 */
public class HeadTag extends BodyTagSupport implements MenuContext
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3064628243230886513L;

	private final static String LOG_ID = "<t:HeaderTag> ";

	public int doEndTag() throws JspException
	{

		try {
			Object val = pageContext.findAttribute(BODY_CONTENT);

			if (val != null) {

				String content = val.toString();

				int stx = content.indexOf("<head>");
				if (stx > 0) {
					stx = stx + 6;
					int etx = content.indexOf("</head>", stx);
					String header = content.substring(stx, etx);
					this.pageContext.getOut().print(header);
				}

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