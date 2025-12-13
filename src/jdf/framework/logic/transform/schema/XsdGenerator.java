/*
 * Created on 2004-05-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.transform.schema;

import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.logic.transform.TransformerException;

/**
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface XsdGenerator
{
	/**
	 * Target Namespace를 정의한다.
	 * 
	 * @param ns
	 */
	public void setTargetNamespace(String ns);
	
	
	/**
	 * XML Schema 자체 namespace의 localname(prefix)를 정의한다.
	 * 
	 * @param name
	 */
	public void setLocalName(String name);
	
	
	/**
	 * IOSchema에서 in/out 중에 하나를 선택하여 element Name을 가지는 schema를 생성
	 * 
	 * 
	 * @param ioschema
	 * @param in_out
	 * @param elementName
	 * @return
	 */
	public String generate(IOSchema ioschema, int in_out, String elementName) throws TransformerException;
	
	
	public void appendXsdText(String txt);
	
	public String getXsdText() throws TransformerException;
	
	

}