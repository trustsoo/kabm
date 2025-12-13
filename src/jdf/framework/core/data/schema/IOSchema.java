package jdf.framework.core.data.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.JavaBeanDataSet;
import jdf.framework.core.data.ResultSetDataSet;

import org.w3c.dom.Node;


/**
 * <b><code>FieldType</code></b>
 * <p>
 * Legacy 시스템이나 일반적인 TCP/IP 소켓을 이용한 데이타 처리 시스템과 데이타를 주고 받기 위해 사용되는 데이타에서 하나의 필드가 가지는 데이타 타입을 정의한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class IOSchema implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	private final static String LOG_ID = "<f:IOSchema> ";

	public static final int UNDEFINED = -1;

	public static final int IN = 0;

	public static final int OUT = 1;

	/**
	 * setAttribute나 getAttribute에서 사용하기 위한 key 이 IOSchema에 해당하는 XML Schema text 정보를 의미한다.
	 */
	public static final String XML_SCHEMA_TEXT = "anylogic.xsd";

	/**
	 * setAttribute나 getAttribute에서 사용하기 위한 key 이 IOSchema에 해당하는 고유 Schema text 정보를 의미한다.
	 */
	public static final String IOSCHEMA_TEXT = "anylogic.ioschema";

	private String name;

	private Block[] inBlocks;

	private Block[] outBlocks;

	// xml 로 데이터를 보내는 경우 기본 xml strutcture 를 가지는 node
	private Node inputXmlNode;
	
	// xml 로 데이터를 보내는 경우 기본 xml strutcture 를 가지는 text
	private String inputXmlText;

	private Map<String, Field> inFields = new HashMap<>();

	private Map<String, Field> outFields = new HashMap<>();

	// block의 ID별로 저장
	private Map<String, Block> blockIdMap = new HashMap<>();

	// 이 data 를 처리하기 위한 processor정보
	transient private Processor processor;

	transient private Class processorClass;

	// xbrl/list/getCompany 에서 getCompany 명만 세팅
	private String shortName;

	private Map<Object, Object> props = new HashMap<>();

	private boolean enableHttpAccess = false;

	private String description = "";

	/**
	 * 기본생성자
	 * 
	 * @param name
	 * @param inBlocks
	 * @param outBlocks
	 */
	public IOSchema(String name, Block[] inBlocks, Block[] outBlocks) {
		if (name == null)
			throw new IllegalArgumentException();

		if (inBlocks == null)
			inBlocks = new Block[0];

		if (outBlocks == null)
			outBlocks = new Block[0];

		this.name = name;

		int p = name.lastIndexOf("/");
		this.shortName = p < 0 ? name : name.substring(p + 1);

		this.inBlocks = inBlocks;
		this.outBlocks = outBlocks;

		setField(inFields, inBlocks);
		setField(outFields, outBlocks);
		selectChildBldFields(outBlocks);
	}
	
	/**
	 * bld읠  full id 에서 파일명에 해당하는 부분(확장자 제외)만 반환한다.
	 * ex) samples/db/getArticle  -> getArticle
	 * 
	 * @return 단순BLD 명
	 */
	public String getShortName()
	{
		return this.shortName;
	}

	private Field[] bldExecuteFieldArray;

	/**
	 * 
	 * @return
	 */
	public Field[] getBldExecuteFieldArray()
	{
		return this.bldExecuteFieldArray;
	}

	/**
	 * <block class='com.ci.test.Board'> <field name='articleList' properties='bld:/samples/listArticle?bid=${bid}'/>
	 * </block> 위와 같이 Board객내에 특정 property 가 별도의 bld를 통해 값을 받아 세팅해야 하는 경우 관련된 field정보만 미리 선택하여 보관하고 있는다.
	 * 
	 * @param blocks
	 */
	private void selectChildBldFields(Block[] blocks)
	{
		List<Field> result = new ArrayList<>();

		for (int i = 0; i < blocks.length; i++) {
			Field[] fields = blocks[i].getFields();

			for (int j = 0; j < fields.length; j++) {

				String val = fields[j].getProperty(Field.PROPERTY_KEY_BLD);
				if (val != null && val.length() > 0) {
					result.add(fields[j]);

					String bldUrl = val;
					int p = bldUrl.indexOf("?");
					if (p > 0) {
						bldUrl = bldUrl.substring(0, p);

						String parameters = val.substring(p + 1);

						String[] params = jdf.framework.core.util.SmartStringArray.split("&", parameters);
						for (int y = 0; y < params.length; y++) {
							
							String[] keyVal = jdf.framework.core.util.SmartStringArray.split("=", params[y]);
							
							String paramName = keyVal[0];
							String paramVal = keyVal[1];
							String fieldNm = null;
							if (paramVal.indexOf("${") == 0) {
								
								fieldNm = paramVal.substring(2);
								fieldNm = fieldNm.substring(0, fieldNm.length() - 1);
								
								paramVal = null;
							}

							fields[j].addParameter(paramName, paramVal, fieldNm);
						}

					}

					fields[j].setProperty(Field.PROPERTY_KEY_BLD_URL, bldUrl);
				}
			}
		}

		this.bldExecuteFieldArray = (Field[]) result.toArray(new Field[] {});
	}

	/**
	 * 
	 * 
	 * @param map
	 * @param blocks
	 */
	private void setField(Map<String, Field> map, Block[] blocks)
	{
		for (int j = 0; j < blocks.length; j++) {
			Field[] fields = blocks[j].getFields();

			String block_id = blocks[j].getId();

			if (block_id != null)
				blockIdMap.put(block_id, blocks[j]);

			// block_id가 없으면
			// 그냥 field명으로 세팅되고,
			// 그렇지 않으면 id.name 과 같은 형태로 설정된다.
			block_id = block_id == null ? "" : block_id + "";

			for (int i = 0; i < fields.length; i++) {

				// TODO: 정리필요
				map.put(fields[i].getName(), fields[i]);
				map.put(block_id + fields[i].getName(), fields[i]);
			}

		}
	}

	/**
	 * 입력 id를 가지는 Block 정보를 return 한다. 해당 block 이 없으면 null을 return 한다.
	 * 
	 * @param id
	 * @return
	 */
	public Block getBlockById(String id)
	{
		Block b = (Block) this.blockIdMap.get(id);
		return b;
	}

	/**
	 * 
	 * 
	 * @param name
	 * @return
	 */
	public Field getInputField(String name)
	{
		return (Field) inFields.get(name);
	}

	public Field getOutputField(String name)
	{
		return (Field) outFields.get(name);
	}

	/**
	 * Data 이름을 가져온다.
	 * 
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Data 이름을 가져온다.
	 * 
	 */
	public Block[] getInputBlocks()
	{
		return inBlocks;
	}

	public Block[] getOutputBlocks()
	{
		return outBlocks;
	}

	/**
	 * Processor 구현 객체를 반납한다.
	 * 
	 * 
	 * Returns the processor.
	 * 
	 * @return Processor
	 */
	public Processor getProcessor()
	{
		return processor;
	}

	/**
	 * Processor 구현객체를 생성하여 반납한다. 현재는 보관된 Processor 객체의 clone를 반납한다. clone이나 생성 실패시는 null을 반환
	 * 
	 * @return
	 */
	public Processor createProcessor()
	{
		try {
			return (Processor) processor.clone();
		} catch (Exception e) {
			jdf.framework.core.log.Logger.warn.println(LOG_ID + "createProcessor fail", e);
			return null;
		}

	}

	/**
	 * Sets the processor.
	 * 
	 * @param processor
	 *            The processor to set
	 */
	public void setProcessor(Processor processor)
	{
		this.processor = processor;

		processor.setIOSchema(this);
	}

	/**
	 * input block정보를 가지고 있는 DataSet 객체를 반환한다.
	 * 
	 */
	public DataSet getInputDataSetInstance()
	{
		return getDataSetInstance(IN);
	}

	/**
	 * output block정보를 가지고 있는 DataSet 객체를 반환한다.
	 * 
	 */
	public DataSet getOutputDataSetInstance()
	{
		return getDataSetInstance(OUT);
	}

	/**
	 * 해당 스키마명을 가지는 DataSet를 반납한다.
	 */
	private DataSet getDataSetInstance(int in_out)
	{
		DataSet output = null;

		// prefetch 를 할지여부
		// 즉 db 에서 query 한후 모든값을 DataSet에 미리 넣을것인가 (true)
		// 조회할때 마다 그때그때 추출할것인가 (false)
		// 결정
		String v = this.getProcessor().getProperty(ResultSetDataSet.PREFETCH_PROPERTY);
		if ("false".equals(v))
			output = new ResultSetDataSet(name);
		else if (in_out == OUT && this.outBlocks.length > 0 && this.outBlocks[0].getMappingClass() != null) {

			output = new JavaBeanDataSet(this.outBlocks[0].getMappingClass());

		} else {
			output = new DataSet(name);
		}

		// Block[] blocks = null;

		if (in_out == IN) {
			// blocks = inBlocks;
			output.setIOSchema(this, IN);
		} else {
			// blocks = outBlocks;
			output.setIOSchema(this, OUT);
		}

		/*
		 * try { for (int i = 0; i < blocks.length; i++) { Block block = blocks[i];
		 * 
		 * int iterationNum = 0;
		 * 
		 * if (block.isIterationNumSet()) { iterationNum = block.getIterationNum();
		 * 
		 * Field[] fields = block.getFields();
		 * 
		 * for (int j = 0; j < iterationNum; j++) { for (int k = 0; k < fields.length; k++) { Field field = fields[k];
		 * 
		 * String keyName = field.getName();
		 * 
		 * Object val = field.getDefaultValue();
		 * 
		 * try { if (val.toString().indexOf("{$") > -1) val = null; } catch (Exception ee) { }
		 * 
		 * output.put(keyName, val, j); } } } } } catch (Exception e) { Logger.err.println("<DataSet>
		 * getDataSet ERROR " + e.toString()); }
		 */

		return output;
	}

	private String toStr;

	/**
	 * String 정보를 반환
	 * 
	 * 
	 */
	public String toString()
	{

		if (toStr == null) {
			StringBuffer buf = new StringBuffer();

			buf.append("<transaction name=\"").append(shortName).append("\">\n");

			buf.append("<input>").append("\n");

			for (int i = 0; i < inBlocks.length; i++) {
				buf.append(inBlocks[i].toString()).append("\n");
			}
			buf.append("</input>");

			buf.append("<output>").append("\n");

			for (int i = 0; i < outBlocks.length; i++) {
				buf.append(outBlocks[i].toString()).append("\n");
			}
			buf.append("</output>").append("</transaction>");

			toStr = buf.toString();

		}

		return toStr;
	}

	/**
	 * Processor 구현 class 를 반환한다.
	 * 
	 * 
	 * @return
	 */
	public Class<?> getProcessorClass()
	{
		return processorClass;
	}

	/**
	 * Processor 구현 class를 설정한다.
	 * 
	 * @param class1
	 */
	public void setProcessorClass(Class<?> class1)
	{
		processorClass = class1;
	}

	/**
	 * 속성값을 설정한다.
	 * 
	 * @param key
	 * @param val
	 */
	public void setAttribute(Object key, Object val)
	{
		this.props.put(key, val);
	}

	/**
	 * 속성값을 반환한다.
	 * 
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttribute(Object key)
	{
		return this.props.get(key);
	}
	
	
	/**
	 * input xml 텍스트 출력
	 * 
	 * @return xml text
	 */
	public String getInputXmlText()
	{
		return this.inputXmlText;
	}
	
	/**
	 * input xml 텍스트 설정 
	 * @param xmlTxt xml text
	 */
	public void setInputXmlText(String xmlTxt)
	{
		this.inputXmlText=xmlTxt;
	}

	/**
	 * 
	 * @return xml node(dom)
	 */
	public Node getInputXmlNode()
	{
		return inputXmlNode;
	}

	/**
	 * 
	 * @param inputXmlNode xml node(dom)
	 */
	public void setInputXmlNode(Node inputXmlNode)
	{
		this.inputXmlNode = inputXmlNode;
	}

	public boolean isEnableHttpAccess()
	{
		return enableHttpAccess;
	}

	public void setEnableHttpAccess(boolean enableHttpAccess)
	{
		this.enableHttpAccess = enableHttpAccess;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		if (description != null)
			this.description = description;
	}

}