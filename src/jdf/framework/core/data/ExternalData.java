package jdf.framework.core.data;


/**
 * <b><code>ExternalData</code></b>
 * <p>
 * 외부 시스템의 데이타를 규정한 Data Object
 * </p>
 * 
 * 
 * @author
 * @version 1.0
 */

public interface ExternalData
{
	
	
	/**
     * byte[] 로 변환한다.
     * 
     * @return
	 */
	public byte[] toByteArray();
	
	
    /**
     * String 타입으로 변환한다. 
     * 
     * @return
     */
	public String toString();
	
}
