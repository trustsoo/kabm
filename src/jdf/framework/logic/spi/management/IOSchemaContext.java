package jdf.framework.logic.spi.management;

import jdf.framework.core.xml.XMLReferer;

/**
 * 
 * IO Schema(BLD) context 는 이름과 정보의 근원이 되는 XML 즉 XMLReferer 정보를 가지고 있는 객
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-11-14 오후 11:54:26
 * 
 */

public class IOSchemaContext {
	private String id;

	private String bldRealPath;

	private XMLReferer xmlDoc;

	private long modifyTime;

	// bld 가 메모리에 적재된 시간
	private long loadTime;

	/**
	 * BLD에 대당하는 id 과 XMLReferer 로 객체생성
	 * 
	 * @param id
	 * @param xmlDoc
	 */
	public IOSchemaContext(String id, XMLReferer xmlDoc, String bldRealPath) {
		this.id = id;
		this.xmlDoc = xmlDoc;
		this.bldRealPath=bldRealPath;

	}

	/**
	 * id 값을 얻는다.
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * XMLReferer 구현 객체를 얻는다.
	 * 
	 * @return
	 */
	public XMLReferer getXMLReferer() {
		return this.xmlDoc;

	}

	/**
	 * BLD 수정시간을 설정한다.
	 * 
	 * @param time
	 */
	public void setModifyTime(long time) {
		this.modifyTime = time;
	}

	/**
	 * BLD 수정시간을 얻는다.
	 * 
	 * @return
	 */
	public long getModifyTime() {
		return this.modifyTime;
	}

	/**
	 * BLD가 메모리에 적재 완료된 시간
	 * 
	 * @param time
	 *            milli second
	 */
	public void setLoadTime(long time) {
		this.loadTime = time;
	}

	/**
	 * BLD가 메모리에 적재(load)된 시간.
	 * 
	 * @return
	 */
	public long getLoadTime() {
		return this.loadTime;
	}

	/**
	 * BLD의 실제 경로
	 * @return BLD의 실제 경로
	 */
	public String getBldRealPath() {
		return bldRealPath;
	}


}