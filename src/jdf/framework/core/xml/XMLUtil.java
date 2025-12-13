/*
 * Created on 2004-06-16
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.core.xml;

import java.io.File;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jdf.framework.core.util.StringFormater;
import jdf.framework.logic.transform.XmlTransformerImpl;

/**
 * XML 관련 Utility 성 class
 * 
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XMLUtil
{
	private final static String LOG_ID = "<XMLUtil> ";

	
	/**
	 * 
	 * 
	 * @param node
	 * @return
	 */
	/*
    static public Element skipUntilElement(Node node)
	{
		while (node != null && node.getNodeType() != Node.ELEMENT_NODE)
			node = node.getNextSibling();
		return (Element) node;
	}
    */

	/**
	 * 
	 * @param root
	 * @param path
	 * @return
	 */
    /*
	static public String evaluateLoadpath(final String root, final String path)
	{
		if (path.indexOf(":") < 0)
			return root + path;
		else
			return path;
	}
    */

	/**
	 * 
	 * 
	 * @param script
	 * @param root
	 * @param prefix
	 * @return
	 * @throws XmlException
	 */
    /*
	static public Document replaceInclude(Document script, final String root, String prefix) throws XmlException
	{
		try
		{
			NodeList includelist = XPathAPI.selectNodeList(script, "//" + prefix + ":include");
			for (int i = 0; i < includelist.getLength(); i++)
			{
				Element include = (Element) includelist.item(i);
				NodeList nodelist =
					replaceInclude(
						DocBuilder.getDocument(new File(evaluateLoadpath(root, include.getAttribute("href")))),
						root,
						prefix)
						.getDocumentElement()
						.getChildNodes();
				for (int j = 0; j < nodelist.getLength(); j++)
					include.getParentNode().insertBefore(script.importNode(nodelist.item(j), true), include);
				include.getParentNode().removeChild(include);
			}
			return script;
		}
		catch (Exception xe)
		{
			throw new XmlException("[[SynchInclude.run]] source : " + xe.getMessage());
		}
	}
    */

	/**
	 * attribute을 문자열로 바꾼다.
     * 
     * 
	 * @param list
	 * @return
	 */
	static private String attributes2str(final NamedNodeMap list)
	{
		int size = list.getLength();
		if (size > 0)
		{
			String result = "";
			for (int i = 0; i < size; i++)
				result =
					result
						+ " "
						+ ((Attr) list.item(i)).getName()
						+ "=\""
						+ encode(((Attr) list.item(i)).getValue())
						+ "\"";
			return result;
		}
		else
			return "";
	}

	/**
	 * 
     * 선택된 element 의 text 값을 변경한다.
     * 
	 * @param target
	 * @param element
	 * @param value
	 * @throws XmlException
	 */
	static public void replaceTextNode(Document target, Element element, final String value) throws XmlException
	{
		NodeList childlist = element.getChildNodes();
		for (int i = 0; i < childlist.getLength(); i++)
		{
			Node cur = childlist.item(i);
			if (cur.getNodeType() == Node.TEXT_NODE)
			{
				element.removeChild(cur);
			}
		}
		element.appendChild(target.createTextNode(value));
		//.setNodeValue(value);
	}

	/**
	 * 문자열을 xml 문서에 맞는 문자열로 바꾼다.
     * 
     * ex) & -> &amp;
     * 
	 * @param input
	 * @return
	 */
	static public String encode(final String input)
	{
		StringBuffer buffer = new StringBuffer(input.length());
		for (int i = 0; i < input.length(); i++)
			switch (input.charAt(i))
			{
				case '&' :
					buffer.append("&amp;");
					break;
				case '<' :
					buffer.append("&lt;");
					break;
				case '"' :
					buffer.append("&quot;");
					break;
				default :
					buffer.append(input.charAt(i));
					break;
			}
		return buffer.toString();
	}

	/**
	 * xml에서 사용되는 문자열을 일반 일자열로 변환한다.
     * 
	 * @param input
	 * @return
	 */
	static public String decode(final String input)
	{
		int length = input.length();
		StringBuffer buffer = new StringBuffer(length);
		for (int i = 0; i < length; i++)
			if (input.charAt(i) == '&')
				if (i + 1 < length)
					switch (input.charAt(i + 1))
					{
						case 'a' :
							if (i + 4 < input.length() && input.substring(i, i + 5).equals("&amp;"))
							{
								buffer.append('&');
								i += 4;
								break;
							}
							else
							{
								buffer.append(input.charAt(i));
								break;
							}
						case 'l' :
							if (i + 3 < input.length() && input.substring(i, i + 4).equals("&lt;"))
							{
								buffer.append('<');
								i += 3;
								break;
							}
							else
							{
								buffer.append(input.charAt(i));
								break;
							}
						case 'q' :
							if (i + 5 < input.length() && input.substring(i, i + 6).equals("&quot;"))
							{
								buffer.append('"');
								i += 5;
								break;
							}
							else
							{
								buffer.append(input.charAt(i));
								break;
							}
						default :
							buffer.append(input.charAt(i));
							break;
					}
				else
					buffer.append(input.charAt(i));
			else
				buffer.append(input.charAt(i));
		return buffer.toString();
	}

	/**
	 * selectnodelistincontext
	 * 
	 * @param select
	 * @param context
	 * @param doc
	 * @return
	 * @throws TransformerException
	 * @throws XmlException
	 */
	static public NodeList selectnodelistincontext(final String select, final Node context, final Document doc)
		throws TransformerException, XmlException
	{
		if (select.length() == 0)
			throw new XmlException("[[XMLUtil.selectnodelistincontext]] source : select attribute is mandatory");
		if (context == null)
			return XPathAPI.selectNodeList(doc, select);
		else
			return XPathAPI.selectNodeList(context, select);
	}
	
	public static String replaceCDATA(String data) 
	{		
		StringBuffer buf = new StringBuffer();
		try
		{
			data = data.replaceAll("&quot;", "\"").replaceAll("&#039;", "\'");        
	        data = data.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			
			data = StringFormater.replaceStr(StringFormater.replaceStr(data, "<", "("), ">", ")");
			
		} catch(Exception ex)
		{			
		}
		
		if (data.indexOf("<") > -1) {
			buf.append("<![CDATA[").append(data).append("]]>");
		} else if (data.indexOf("&") > -1) {
			buf.append("<![CDATA[").append(data).append("]]>");
		} else if (data.indexOf(">") > -1) {
			buf.append("<![CDATA[").append(data).append("]]>");
		}else{
			buf.append(data);
		}
		return buf.toString();

	}
}
