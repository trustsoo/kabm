package jdf.framework.core.data.schema;

/**
 * <b><code>FieldType</code></b>
 * <p>
 * Legacy 시스템이나 일반적인 TCP/IP 소켓을 이용한 데이타 처리 시스템과 데이타를 주고 받기 위해 사용되는 데이타에서 하나의 필드가 가지는 데이타 타입을 정의한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public interface FieldType
{

	public final static int ALIGN_LEFT = 100;

	public final static int ALIGN_RIGHT = 200;

	public final static int UNDEFINED = -1;

	/**
	 * java.lang.String 와 동일
	 * 
	 */
	public final static int STRING = 10;

	/**
	 * premetive type의 char와 동일
	 * 
	 */
	public final static int CHAR = 20;

	/**
	 * premetive type의 int와 동일
	 * 
	 */
	public final static int INTEGER = 30;

	/**
	 * premetive type의 long와 동일
	 * 
	 */
	public final static int LONG = 40;

	/**
	 * premetive type의 float와 동일
	 * 
	 */
	public final static int FLOAT = 50;

	/**
	 * premetive type의 double와 동일
	 * 
	 */
	public final static int DOUBLE = 60;

	/**
	 * premetive type의 byte와 동일
	 * 
	 */
	public final static int BYTE = 70;

	/**
	 * premetive type의 byte array와 동일
	 * 
	 */
	public final static int BYTE_ARRAY = 80;

	/**
	 * premetive type의 boolean와 동일
	 * 
	 */
	public final static int BOOLEAN = 90;

	/**
	 * java.lang.String 와 동일
	 * 
	 */
	public final static int STRING_NUMBER = 11;

	/**
	 * BLOB type
	 * 
	 */
	public final static int BLOB = 81;

	/**
	 * CLOB type
	 * 
	 */
	public final static int CLOB = 12;

	// ioschema 의 block type
	public final static int BLOCK = 200;

}
