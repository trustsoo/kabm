/*
 * Created on 2004. 7. 29.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jdf.framework.core.data.schema;

import jdf.framework.core.util.SmartStringArray;

import java.sql.Types;


/**
 * 
 * StoreProcedure를 위한 Mapper Class
 * 
 * <pre>
 *  &lt;param seq=&quot;1&quot; type=&quot;out&quot; class=&quot;Types.REF&quot;&gt;co_snm:nm,co_snm,co_snm &lt;/param&gt;
 *  &lt;param seq=&quot;2&quot; type=&quot;in&quot;&gt;type &lt;/param&gt; &lt;param seq=&quot;3&quot; type=&quot;out&quot;
 *  class=&quot;Types.VARCHAR&quot; size=&quot;10&quot;&gt;sql &lt;/param&gt;
 * </pre>
 * 
 * @author
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ProcedureMapper implements java.io.Serializable
{
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public final static int IN = 0;

	public final static int OUT = 1;

	private int sequence;

	// in/out 모드
	private int mode = IN;

	// DB type
	private int type = 0;

	private int db_size = 0;

	private boolean isResultSetMode = false;

	/**
     * 생성자
     * 
     * @param seq
     * @param mode
     * @param typeName
     * @param sizeStr
     * @param content
     * @throws Exception
     */
	public ProcedureMapper(int seq, int mode, String typeName, String sizeStr,
			String content) throws IllegalArgumentException {
		this.sequence = seq;
		this.mode = mode;

		if (typeName != null && typeName.length() > 0)
			this.type = getDBType(typeName);
		this.db_size = getDBSize(sizeStr);

		setFieldMapping(content);
	}

	/**
     * type명(String) 을 int형으로 바꾼다.
     * 
     * @param typeName
     * @return
     * @throws Exception
     */
	private int getDBType(String typeName) throws IllegalArgumentException
	{
		if ("VARCHAR".equals(typeName))
			return Types.VARCHAR;

		else if ("INTEGER".equals(typeName))
			return Types.INTEGER;

		else if ("REF".equals(typeName)) {
			isResultSetMode = true;
			return Types.REF;
		} else if ("OracleTypes.CURSOR".equals(typeName)) {
			isResultSetMode = true;
			// OracleTypes.CURSOR 인 경우 -10 이다.
			return -10;
		}

		else if ("FLOAT".equals(typeName))
			return Types.FLOAT;

		else if ("CHAR".equals(typeName))
			return Types.CHAR;

		else if ("CLOB".equals(typeName))
			return Types.CLOB;

		else
			throw new IllegalArgumentException(typeName + " is not defined.");

	}

	public boolean isResultSetMode()
	{
		return this.isResultSetMode;
	}

	/**
     * DB 사이즈를 return 한다.
     * 
     * 
     * @param sizeStr
     * @return
     */
	private int getDBSize(String sizeStr)
	{

		try {
			return Integer.parseInt(sizeStr);
		} catch (Exception e) {
			return -1;
		}
	}

	private String[] fieldNames;

	private String[] fieldDbMappingNames;

	/**
     * co_snm:nm,co_snm,co_snm 와 같이 들어오는 문자열을 파싱하여 Field 의 매핑되는 정보를 설정한다.
     * 
     * @param content
     */
	public void setFieldMapping(String content)
	{
		String[] fieldArray = SmartStringArray.split(",", content);

		this.fieldNames = new String[fieldArray.length];
		this.fieldDbMappingNames = new String[fieldArray.length];

		for (int i = 0; i < fieldArray.length; i++) {
			// dbFieldNm:ioSchemaNm 과 같은 형태인 경우와
			// ioSchemaNm 인 경우를 고려
			String[] tmp = SmartStringArray.split(":", fieldArray[i].trim());

			// System.out.println("<ProcedureMapper> "+tmp.length+" >>
			// "+content);
			if (tmp.length == 2) {
				this.fieldDbMappingNames[i] = tmp[0];
				this.fieldNames[i] = tmp[1];
			} else if (tmp.length == 1) {
				this.fieldDbMappingNames[i] = null;
				this.fieldNames[i] = tmp[0];
			} else {
				this.fieldDbMappingNames[i] = null;
				this.fieldNames[i] = null;

			}
		}

	}

	/**
     * 필드의 갯수를 return
     * 
     * @return
     */
	public int getFieldNum()
	{
		return this.fieldDbMappingNames.length;
	}

	/**
     * 해당 IO Schema Field와 mapping되는 DB 컬럼명을 가져온다. 컬럼명이 정의가 안되어 있으면 null return
     * 이런 경우는 index 순서로 조회하여야 한다.
     * 
     * @return Returns the fieldDbMappingNames.
     */
	public String getDbMappingName(int seq)
	{
		return fieldDbMappingNames[seq];
	}

	/**
     * 필드 이름 설정
     * 
     * @return Returns the fieldNames.
     */
	public String getFieldName(int seq)
	{
		return fieldNames[seq];
	}

	/**
     * 현재 모드를 반환
     * 
     * @return Returns the mode.
     */
	public int getMode()
	{
		return mode;
	}

	/**
     * 순서정보를 반환
     * 
     * @return Returns the sequence.
     */
	public int getSequence()
	{
		return sequence;
	}

	/**
     * DB 측의 타입 정보를 반환
     * 
     * @return Returns the type.
     */
	public int getDBType()
	{
		return type;
	}

	/**
     * DB 측의 출력 사이즈를 반환
     * 
     * @return
     */
	public int getDBOutSize()
	{
		return this.db_size;

	}
}