package jdf.framework.logic.spi.management;

import jdf.framework.core.xml.XMLReferer;


/**
 * BLD 가 로드된후 호출되는 Class의 Interface
 * 
 * @author
 *
 */
public interface BldLoadListener
{
	
	/**
	 * BLD를 load 하기전에 체크할 것을 구현한다.
	 * 
	 * @param bldFullName
	 * @param doc
	 * @throws jdf.framework.core.data.ResourceException
	 */
	public void onBeforeLoad(String bldFullName, XMLReferer doc) throws jdf.framework.core.data.ResourceException;
	
	
	
	/**
	 * BLD 가 정상적으로 load 된후 호출되는 메쏘드
	 * 
	 * @param doc
	 */
	public void onLoadComplete(String bldFullName, XMLReferer doc);
	

}