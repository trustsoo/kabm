package jdf.framework.core.http;

import java.io.Reader;
import java.util.Vector;

import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.ElementIterator;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

/**
 * <p>
 * 해당 사이트의 링크정보를 가져오기위한 Class이다. 단 해당문서가 정확하게 작성된 HTML 
 * 문서여야 한다.
 *
 * 예제
 * <pre>
 * URLReader page = new URLReader("www.yahoo.co.kr");
 * Vector links = HtmlLinks.getLinks( page.getReader() );
 *
 * </pre>
 *
 * </p>
 *
 * @author
 * @version 1.0
 */

public final class HtmlLinks
{

	/**
	 * HTML 페이지에서 link정보를 Vector로 return한다.
	 *
	 *@param Reader
	 *@return Vector link 정보
	 */
	public static Vector getLinks(Reader rd)
	{

		EditorKit kit = new HTMLEditorKit();
		Document doc = kit.createDefaultDocument();

		// The Document class does not yet 
		// handle charset's properly.
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

		Vector links = new Vector();

		try
		{

			// Create a reader on the HTML content.
			//Reader rd = getReader(args[0]);

			// Parse the HTML.
			kit.read(rd, doc, 0);

			// Iterate through the elements 
			// of the HTML document.
			ElementIterator it = new ElementIterator(doc);
			javax.swing.text.Element elem;

			while ((elem = it.next()) != null)
			{
				SimpleAttributeSet s = (SimpleAttributeSet) elem.getAttributes().getAttribute(HTML.Tag.A);

				//System.out.println( elem.getDocument().getText(0,10) );

				if (s != null)
				{
					//System.out.println( s.getAttribute(HTML.Attribute.HREF));
					String link = (String) s.getAttribute(HTML.Attribute.HREF);
					if (link != null && link.trim().length() != 0)
						links.addElement(link);
				}

				s = (SimpleAttributeSet) elem.getAttributes().getAttribute(HTML.Tag.AREA);

				//System.out.println( elem.getDocument().getText(0,10) );

				if (s != null)
				{
					//System.out.println("+++++++++"+ s.getAttribute(HTML.Attribute.HREF));
					String link = (String) s.getAttribute(HTML.Attribute.HREF);
					if (link != null && link.trim().length() != 0)
						links.addElement(link);
				}

			}

			return links;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return links;
	}

}