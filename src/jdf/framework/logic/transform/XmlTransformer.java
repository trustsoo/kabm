package jdf.framework.logic.transform;

import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;

import jdf.framework.core.data.DataSet;


/**
 * 
 * XML document 로 변환하는 Transformer 의 interface
 * 
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-12-17 오전 9:23:55
 *
 */
public interface XmlTransformer extends Transformer
{
	public final static int XML_ELEMENT_CENTRIC = 0;

	public final static int XML_ATTRIBUTE_CENTRIC = 1;

	public void setXmlType(int type);

	public void setStylesheet(URL xsl_url);

	
	
	public void setLocalName(String name);
	
	/**
	 * namespcae를 설정한다.
	 * 
	 * @param namespcae
	 */
	public void setNamespace(String namespace);
	
	



	/**
	   * OutputStream을 통해 변환/출력한다.
	   * 
	   * 
	   * @param source
	   * @param out
	   * @return
	   * @throws TransformerException
	   */
	public int transform(String rootElementName, DataSet source, OutputStream out) throws TransformerException;

	/**
	 * 
	 * Writer를 통해 변환/출력한다.
	 * 
	 * @param source
	 * @param writer
	 * @return
	 * @throws TransformerException
	 */
	public int transform(String rootElementName, DataSet source, Writer writer) throws TransformerException;

}