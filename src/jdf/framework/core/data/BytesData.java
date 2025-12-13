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

public class BytesData implements ExternalData
{

    private final byte[] data;

    private int offset;

    private int length;

    private String toStr;

    /**
     * 기본 생성자
     * @param data
     */
    public BytesData(byte[] data) {
	this.data = data;
	this.offset = 0;
	this.length = data.length;
    }

    /**
     * 생성자
     * 
     * @param data
     * @param offset
     * @param length
     */
    public BytesData(byte[] data, int offset, int length) {
	this.data = data;
	this.offset = offset;
	this.length = length;
    }

    /**
     * 원본 byte array 로 return
     */
    public byte[] toByteArray()
    {
	if (this.length == this.data.length)
	    return data;
	else {
	    byte[] tmp = new byte[this.data.length];
	    System.arraycopy(this.data, this.offset, tmp, 0, this.length);
	    return tmp;
	}

    }

    /**
     * 문자열로 return
     * 
     */
    public String toString()
    {
	if (toStr == null)
	    toStr = new String(data);

	return toStr;
    }

}