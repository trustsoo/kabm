package jdf.framework.core.data.schema;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jdf.framework.core.util.SmartStringArray;


/**
 * <b><code>Block</code></b>
 * <p>
 * Legacy 시스템이나 일반적인 TCP/IP 소켓을 이용한 데이타 처리 시스템과 데이타를 주고 받기 위해 사용되는 데이타에서 하나의 필드가
 * 가지는 데이타 타입을 정의한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class Block implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	public final static int UNBOUNDED = Integer.MAX_VALUE;

	private String name;

	private int iterationNum = UNBOUNDED;

	private Field[] fields;

	private String iterationRefName;

	// block 안으 각 필드 길이의 합
	private int totalFieldSize = 0;

	private short accessLevel = DataType.PUBLIC_ACCESS;

	private Map<String, Field> map = new HashMap<>();

	// block을 구별할 수 있는 id
	private String id;

	private Properties properies;
	
	// xpath 정보
	private String xpathExpression;
	
	
	private Class<?> mappingClass;

	/**
     * 기본 생성자
     * 
     * @param name
     * @param iterationNum
     * @param fields
     */
	public Block(String name, int iterationNum, Field[] fields) {
		if (name == null)
			name = "block";
		this.name = name;
		this.iterationNum = iterationNum;
		this.fields = fields;

		for (int i = 0; i < fields.length; i++) {
			map.put(fields[i].getName(), fields[i]);

			totalFieldSize = totalFieldSize + fields[i].getSize();
		}
	}

	/**
     * 생성자
     * 
     * @param name
     * @param iterationRefName
     * @param fields
     */
	public Block(String name, String iterationRefName, Field[] fields) {
		if (name == null)
			name = "block";

		this.name = name;
		this.fields = fields;
		this.iterationRefName = iterationRefName;
		for (int i = 0; i < fields.length; i++) {
			map.put(fields[i].getName(), fields[i]);
			totalFieldSize = totalFieldSize + fields[i].getSize();
		}
	}

	
	
	public Class<?> getMappingClass()
	{
		return mappingClass;
	}

	public void setMappingClass(Class<?> mappingClass)
	{
		this.mappingClass = mappingClass;
	}

	/**
     * 해당 Block 에 정의된 모든 필드의 사이즈 총합을 반환한다.
     * 
     * @return
     */
	public int getTotalFieldSize()
	{
		return this.totalFieldSize;
	}

	/**
     * Block의 이름을 가져온다.
     * 
     * @return name block이름
     */
	public String getName()
	{
		return name;
	}

	/**
     * Block의 고유한 id를 return 한다.
     * 
     * @param id
     */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
     * Block의 ID를 반환한다.
     * 
     * @return
     */
	public String getId()
	{
		return this.id;
	}

	/**
     * block의 반복횟수가 숫자로 정의되었는지 여부
     * 
     * @return
     */
	public boolean isIterationNumSet()
	{
		if (iterationNum != UNBOUNDED && this.iterationRefName == null)
			return true;
		/*
         * else if (this.iterationRefName == null) { this.iterationNum= 1;
         * return true; }
         */
		else
			return false;
	}

	/**
     * 반복횟수를 가져온다.
     * 
     * @return length 반복횟수
     */
	public int getIterationNum()
	{
		return iterationNum;
	}

	/**
     * 반복횟수를 정의한다.
     * 
     * @param num
     */
	public void setIterationNum(int num)
	{
		this.iterationNum = num;
	}

	/**
     * 반복횟수가 설정된 Field 명을 가져온다.
     * 
     * @return length 반복횟수
     */
	public String getIterationRefName()
	{
		return iterationRefName;
	}

	/**
     * Field array 를 반환한다.
     * 
     * @return fields Field 정보
     */
	public Field[] getFields()
	{
		return fields;
	}

	public Field getField(String name)
	{
		return (Field) map.get(name);
	}

	private String toStr;

	public String toString()
	{

		if (toStr == null) {

			StringBuffer buf = new StringBuffer();

			buf.append("<block name=\"").append(name);

			if (isIterationNumSet()) {
				buf.append("\" repeat=\"").append(iterationNum);
				buf.append("\" iterationNum=\"").append(iterationNum);
			} else {
				buf.append("\" repeat=\"").append(iterationRefName);
				buf.append("\" iterationRefName=\"").append(iterationRefName);
			}

			buf.append("\">\n");

			for (int i = 0; i < fields.length; i++) {
				buf.append(fields[i].toString()).append("\n");
			}

			buf.append("</block>");

			this.toStr = buf.toString();
		}

		return this.toStr;
	}

	/**
     * @return Returns the accessLevel.
     */
	public short getAccessLevel()
	{
		return accessLevel;
	}

	/**
     * @param accessLevel
     *            The accessLevel to set.
     */
	public void setAccessLevel(String accessLevel)
	{
		if (DataType.PRIVATE_ACCESS_STR.equals(accessLevel))
			this.accessLevel = DataType.PRIVATE_ACCESS;

		else if (DataType.PUBLIC_ACCESS_STR.equals(accessLevel))
			this.accessLevel = DataType.PUBLIC_ACCESS;

	}

	/**
     * properties="view:false;width:100px;" 이런식으로 IO Schema 에 세팅된 값을 읽는다.
     * 
     * @param props
     */
	public void setPropertyStr(String props)
	{
		if (props == null || props.length() == 0)
			return;

		this.properies = new Properties();

		try {
			String[] tmp = SmartStringArray.split(";", props);
			for (int i = 0; i < tmp.length; i++) {
				String[] property = SmartStringArray.split(":", tmp[i]);

				this.properies.setProperty(property[0], property[1]);

			}
		} catch (Exception e) {

		}
	}

	/**
     * properties 속성에 정의된 property 값을 return 한다.
     * 
     * @param key
     * @return
     */
	public String getProperty(String key)
	{
		if (this.properies == null)
			return null;

		return this.properies.getProperty(key);
	}
	
	/**
     * field 에 정의된 xpath 정보를 가져온다.
     * 
     * @return
     */
	public String getXpathExpression()
	{
		return xpathExpression;
	}

	/**
     * field에 정의된 xpath 정보를 설정
     * 
     * @param xpathExpression
     */
	public void setXpathExpression(String xpathExpression)
	{
		if(xpathExpression!=null && xpathExpression.length()==0)
			return;
		this.xpathExpression = xpathExpression;
	}
	
}
