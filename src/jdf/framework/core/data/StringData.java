package jdf.framework.core.data;

/**
 * <b><code>DataSet</code></b>
 * <p>
 * 데이타의 집합체
 * </p>
 * 
 * 기본적의 HashMap의 구조를 가지고 있지만, 다계층의 값을 가질 수 있다.
 * 
 * @author
 * @version 1.0
 */

public class StringData implements ExternalData
{
	private final byte[] toBytes;

	private String toStr;

	/**
     * 기본 생성자
     * 
     * @param str
     */
	public StringData(String str) {
		this.toStr = str;
		this.toBytes = str.getBytes();
	}

	/**
     * byte[] 로 값을 반환한다.
     */
	public byte[] toByteArray()
	{
		return toBytes;
	}

	/**
     * 문자열로 값을 반환한다.
     * 
     */
	public String toString()
	{
		return toStr;
	}

}