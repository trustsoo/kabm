/*
 * Created on 2004-05-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.transform.schema;

/**
 * XsdGenerator 구현 class를 생성한다.
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XsdGeneratorFactory
{
	
	
	/**
	 * XsdGenerator 구현객체를 반납한다.
	 * 
	 * @return
	 */
	public static XsdGenerator newXsdGenerator()
	{
		return new XsdGeneratorImpl();
	}

}
