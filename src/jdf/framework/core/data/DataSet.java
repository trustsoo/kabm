package jdf.framework.core.data;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.data.schema.format.Formatter;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.Utility;


/**
 * <b><code>DataSet</code> </b>
 * <p>
 * 본 FRAMEWORK 에서 권장되는 Data Structure
 * 
 * java.util.Map 과 java.util.List 의 구조를 동시에 가지고 있는다.
 * 
 * </p>
 * 
 * 기본적의 HashMap의 구조를 가지고 있지만, 다계층의 값을 가질 수 있다.
 * 
 * @author
 * @version 1.0
 */

public class DataSet extends ConcurrentHashMap<Object, Object> implements Serializable,
		Cloneable {
	private static final long serialVersionUID = 35L;

	private static final String LOG_ID = "<f:DataSet> ";

	/**
	 * DBMS 어댑터에서 데이터를 추출할때 결과를 paging 하고 싶을 경우 사용하는 Property 명 이 속성은 DBMS Query
	 * 자체적으로 Paging 기능을 사용할 수 없는 경우 사용한다. - 이 속성키는 PAGE 번호를 결정할때 사용
	 * 
	 * <pre>
	 * DataSet input = new DataSet();
	 * input.setProperty(DataSet.PAGE, &quot;1&quot;);
	 * </pre>
	 * 
	 */
	public final static String PAGE = "dbms.page";

	/**
	 * DBMS 어댑터에서 데이터를 추출할때 결과를 paging 하고 싶을 경우 사용하는 Property 명 이 속성은 DBMS Query
	 * 자체적으로 Paging 기능을 사용할 수 없는 경우 사용한다. - 이 속성키는 한페이지당 가져오는 row갯수를 정의
	 * 
	 * <pre>
	 * DataSet input = new DataSet();
	 * input.setProperty(DataSet.ROWS_PER_PAGE, &quot;20&quot;);
	 * </pre>
	 * 
	 */
	public final static String ROWS_PER_PAGE = "dbms.row_per_page";

	/**
	 * DBMS 어댑터에서 데이터를 추출할때 결과를 paging 하고 싶을 경우 사용하는 Property 명 이 속성은 DBMS Query
	 * 자체적으로 Paging 기능을 사용할 수 없는 경우 사용한다. - 이 속성키는 paging 을 하고 나오는 DataSet에서 총
	 * 결과건수를 알고 싶은 경우 사용
	 * 
	 * <pre>
	 *             DataSet output = new DataSet();
	 *             interact.execute({TR코드}, input, output);
	 *             
	 *             String count = input.getProperty(DataSet.RESULT_COUNT);
	 * </pre>
	 * 
	 */
	public final static String RESULT_COUNT = "dbms.result_count";

	/**
	 * DBMS 어댑터에서 데이터를 추출할때 결과를 paging 하고 싶을 경우 사용하는 Property 명 이 속성은 DBMS Query
	 * 자체적으로 Paging 기능을 사용할 수 없는 경우 사용한다. - 이 속성키는 paging 을 하고 나오는 DataSet에서 총
	 * 페이지수를 알고 싶은 경우 사용
	 * 
	 * <pre>
	 *             DataSet output = new DataSet();
	 *             interact.execute({TR코드}, input, output);
	 *             
	 *             String maxPage = input.getProperty(DataSet.MAX_PAGE);
	 * </pre>
	 * 
	 */
	public final static String MAX_PAGE = "dbms.max_page";

	/**
	 * BLD에 정의된 datasource 속성값을 무시하고 program 상에서 조정하고 싶은 경우 사용
	 * 
	 * <pre>
	 * input.setProperty(DataSet.DB_DATASOURCE, &quot;ciPool&quot;);
	 * </pre>
	 * 
	 */
	public final static String DB_DATASOURCE = "dbms.datasource";
	
	
	public final static String FETCH_SIZE = "dbms.fetch_size";
	
	/*
	 * MaskingFormatter로 포매팅 된 데이터를 포매팅 하지 않고 보여줄수 있는 키
	 * */
	public final static String UNMASK_KEY = "IS_UNMASK";
	
	private final static int DEFAULT_SIZE = 101;

	private String name;

	protected String NULL = "";

	private String toStr = null;

	// transient private IOSchema schema;
	private IOSchema schema;

	private int inOutType = IOSchema.UNDEFINED;

	private ArrayList<Object> keyObjList = null;

	protected Properties properties;

	private boolean fixNum = false;

	/**
	 * data 값중 제일 큰 갯수의 크기
	 */
	private int maxDataSize = 0;

	/**
	 * Tuning parameter: list size at or below which insertion sort will be used
	 * in preference to mergesort or quicksort.
	 */
	private static final int INSERTIONSORT_THRESHOLD = 7;

	/**
	 * 생성자
	 * 
	 * @param name
	 */
	public DataSet(String name) {
		super();
		this.name = name;

		this.keyObjList = new ArrayList<>();
		this.properties = new Properties();

	}

	public DataSet() {
		this("");
	}

	/**
	 * DataSet의 이름을 명명한다.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * BLD xml 파일의 내용을 담고있는 jdf.framework.core.data.schema.IOSchema 객체를 세팅한다. type은 이
	 * DataSet 객체가 Input 용인지 Output용인지 결정한다.
	 * 
	 * 
	 * @param schema
	 * @param type
	 * 
	 * @see jdf.framework.core.data.schema.IOSchema
	 */
	public void setIOSchema(IOSchema schema, int type) {
		this.schema = schema;

		this.inOutType = type;
	}

	/**
	 * BLD xml 파일의 내용을 담고있는 jdf.framework.core.data.schema.IOSchema 객체를 반환한다.
	 * 
	 * @return IOSchema
	 * @see jdf.framework.core.data.schema.IOSchema
	 */
	public IOSchema getIOSchema() {
		return schema;
	}

	/**
	 * jdf.framework.core.data.schema.IOSchema 내부에 존재하는 jdf.framework.core.data.schema.Block 객체를
	 * return 한다.
	 * 
	 * @return
	 */
	public Block[] getBlocks() {
		switch (inOutType) {
		case IOSchema.IN:
			return this.schema.getInputBlocks();
		case IOSchema.OUT:
			return this.schema.getOutputBlocks();

		default:
			return new Block[] {};
		}

	}

	/**
	 * 이 DataSet이 input 용인지 output 용인지 구분코드를 return
	 * 
	 * @return
	 */
	public int getInOutType() {
		return inOutType;
	}

	/**
	 * jdf.framework.core.data.schema.Field 객체를 return
	 * 
	 * @param name
	 * @return
	 */
	public Field getField(String name) {
		if (schema == null)
			return null;

		switch (inOutType) {
		case IOSchema.IN:
			return schema.getInputField(name);

		case IOSchema.OUT:
			return schema.getOutputField(name);

		default:
			return null;
		}

	}

	/**
	 * 데이타가 없을시 null이 아닌 공백없는 String 으로 반환한다.
	 */
	public void fixNull() {
		NULL = "";
	}

	/**
	 * 데이터가 없을시 에러가 아니 0 로 return
	 * 
	 */
	public void fixNumber() {
		this.fixNum = true;
	}

	/**
	 * getInt나 getFloat 메쏘드 사용시 값이 없는 경우는 에러 값이 있고, 올바른 숫자값인 경우 숫자타입으로 반환 - 이
	 * 상태가 초기 상태이다.
	 */
	public void unfixNumber() {
		this.fixNum = false;
	}

	/**
	 * 데이타가 실제 없을시는 null로 return한다. BLD에 정의된 default 값도 무시된다.
	 */
	public void unfixNull() {
		NULL = null;
	}

	/**
	 * fix NULL 상태인지 확인
	 * 
	 * @return
	 */
	public boolean isFixNull() {
		if (NULL == null)
			return false;
		else
			return true;
	}

	/**
	 * DataSet의 이름을 반환한다.
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * 해당 키에 대한 값을 반환한다. get(key, 0) 과 동일한 역활을 한다.
	 * 
	 */
	public Object get(Object key) {
		return get(key, 0);
	}



	/**
	 * 
	 * 키에 대한 값을 항상 마지막 위치에 추가시킨다.
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public synchronized Object add(Object key, Object val) {

		return put(key, val, getCount(key));
	}

	/**
	 * DataSet 의 값으로 DataSet을 넣는 경우 자식으로 본다.
	 * 
	 * @param key
	 * @param seq
	 * @return
	 */
	public DataSet getChild(Object key, int seq) {
		ValueArray values = getValueArray(key);
		Object obj = values.get(seq);

		if (obj != null) {

			if (obj.getClass() == DataSet.class)
				return (DataSet) obj;

		} else {
			DataSet child = new DataSet();
			this.put(key, child, seq);

			return child;
		}

		return null;
	}

	/**
	 * 해당 key의 값 전체를 다른 DataSet으로 복사한다.
	 * 
	 * @param key
	 * @param input
	 */
	public void copy(Object key, DataSet input) {
		copy(key, input, key);
	}

	/**
	 * 해당 key의 값 전체를 다른 DataSet으로 복사한다.
	 * 
	 * @param key
	 * @param input
	 * @param toKey
	 */
	public void copy(Object key, DataSet input, Object toKey) {
		ValueArray values = getValueArray(key);

		if (toKey == null)
			toKey = key;

		input.setValueArray(toKey, values);

	}
	
	
	
	
	/**
	 * 다른 DataSet의 결과값을 추가시킨다.
	 * 
	 * @param ds
	 */
	public void append(DataSet ds) {
		ArrayList<Object> keyList = ds.getKeyObjectList();

		for (int i = 0; i < keyList.size(); i++) {
			Object key = keyList.get(i);

			int valCount = ds.getCount(key);

			for (int j = 0; j < valCount; j++) {
				this.add(key, ds.get(key, j));
			}
		}
	}

	/**
	 * 입력된 DataSet 내용을 복사한다.
	 * 
	 * @param ds
	 */
	public void copy(DataSet ds) {
		List<Object> keyList = ds.getKeyObjectList();

		for (int i = 0; i < keyList.size(); i++) {
			Object key = keyList.get(i);

			int valCount = ds.getCount(key);

			for (int j = 0; j < valCount; j++) {
				this.put(key, ds.get(key, j), j);

			}
		}
	}
	
	/**
	 * 입력된 DataSet 내용을 복사한다.
	 * 
	 * @param ds
	 */
	public void copy(DataSet ds, int start, int end) {
		List<Object> keyList = ds.getKeyObjectList();		
		int startNum = 0;
		int endNum = 0;
		for (int i = 0; i < keyList.size(); i++) {
			Object key = keyList.get(i);

			int valCount = ds.getCount(key);
			//System.out.println(valCount);
			if(start > end) return;
			if(start > valCount-1) return;
			if(end   > valCount-1) 
				endNum = valCount -1 ;
			else
				endNum = end;
			
			startNum = start;
			//System.out.println(">>>>>"+startNum+", "+endNum);
			
			
			//endNum = endNum;
			
			int k = 0;
			
			if(startNum == endNum)
			{
				this.put(key, ds.get(key, startNum), k);
			} else
			{			
			
				for (int j = startNum; j < endNum; j++) {
					//System.out.println(j);
					//System.out.println(">>>>>ds.get("+key+","+j+") ");
					this.put(key, ds.get(key, j), k);
					k++;
	
				}
			}
		}
	}
	

	/** IOSchema의 특정 Block에 해당되는 필드의 값을 입력된 DataSet에 복사한다.
	 * @param dest
	 * @param blockName
	 * @throws Exception
	 * @author advan94
	 */
	/*public void copyDataSetByBlockName(DataSet dest, String blockName) throws Exception
	{
		
		
		Block block = this.getIOSchema().getBlockById(blockName);
		
		if(block == null)
			throw new NullPointerException(blockName + "is null");
		
		
		Block[] blocks = new Block[]{block};
		
		Field[] fields = block.getFields();
		
		String firstKeyName = fields[0].getName();
		
		IOSchema ioschema = new IOSchema("/temp/"+java.util.UUID.randomUUID()+"/"+block.getName(), null, blocks);
		dest.setIOSchema(ioschema, IOSchema.OUT);
		
		for (int j = 0; j < this.getCount(firstKeyName); j++) 
		{
			for (int i = 0; i < fields.length; i++) {
				
				String key = fields[i].getName();
				Object val = this.get(key, j);
				dest.put(key, val, j);
			}
			
		}	
		
		dest.properties = this.getProperties();
		
		
	}
	*/
	
	
	
	/** 특정 Block에 해당되는 필드의 값을 입력된 DataSet에 복사한다.
	 * @param dest
	 * @param block
	 * @throws Exception
	 * @author advan94
	 */
	/*public void copyDataSetByBlock(DataSet dest, Block block) throws Exception
	{
		
		Block[] blocks = new Block[]{block};
		
		Field[] fields = block.getFields();
		
		String firstKeyName = fields[0].getName();
		
		IOSchema ioschema = new IOSchema("/temp/"+java.util.UUID.randomUUID()+"/"+block.getName(), null, blocks);
		dest.setIOSchema(ioschema, IOSchema.OUT);
		
				
		for (int j = 0; j < this.getCount(firstKeyName); j++) 
		{
			for (int i = 0; i < fields.length; i++) {
				
				String key = fields[i].getName();
				Object val = this.get(key, j);
				dest.put(key, val, j);
			}
			
		}	
		
		dest.properties = this.getProperties();
	}*/
	
	
	
	
	
	/** 특정 Block에 해당되는 필드의 값을 입력된 DataSet에 복사한다.
	 * @param dest
	 * @param blocks
	 * @throws Exception
	 * @author advan94
	 */
	public DataSet copyDataSetByBlocks(Block[] blocks) throws Exception
	{	
		
		
		DataSet dest = new DataSet();
		try
		{		
			dest = clone();
			
			IOSchema sc = new IOSchema("/temp/"+java.util.UUID.randomUUID(), null, blocks);
			dest.setIOSchema(sc, IOSchema.OUT);			
			//Block[] ddd = dest.getIOSchema().getOutputBlocks();
		
			return dest;
		} catch(Exception ex)
		{
			throw ex;
		}
		
	}
	
	
	/**
	 * IOSchema의 특정 Block에 해당되는 필드의 값을 입력된 DataSet에 복사한다.
	 * @param blockNames
	 * @return DataSet
	 * @throws Exceptions
	 * @author advan94
	 */
	public DataSet copyDataSetByBlockNames(String[] blockNames) throws Exception 
	{
		DataSet dest = new DataSet();
		try
		{		
			dest = clone();
			
			
			Block[] a = dest.getBlocks();
			List<Block> b = new ArrayList<Block>();
			
			List<String> aa = Arrays.asList(blockNames);
			
			for(Block a1 : a)
			{				
				if(aa.contains(a1.getName()))
				{
					
					b.add(a1);
				}			
			}
			
			Block[] blocks = (Block[])b.toArray(new Block[b.size()]);
			
			IOSchema sc = new IOSchema("/temp/"+java.util.UUID.randomUUID(), null, blocks);
			dest.setIOSchema(sc, IOSchema.OUT);			
			//Block[] ddd = dest.getIOSchema().getOutputBlocks();			
		
		
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return dest;
		
	}
	
	
	/** @deprecated 
	 * 특정 Block에 해당되는 필드의 값을 입력된 DataSet에 복사한다.
	 * @param dest
	 * @param blocks
	 * @throws Exception
	 * @author advan94
	 */
	public void copyDataSetByBlocks_old(DataSet dest, Block[] blocks) throws Exception
	{	
		
		for(int idx=0; idx<blocks.length; idx++)
		{
			Field[] fields = blocks[idx].getFields();
			
			String firstKeyName = fields[0].getName();
			
			IOSchema ioschema = new IOSchema("/temp/"+java.util.UUID.randomUUID()+"/"+blocks[idx].getName(), null, blocks);
			dest.setIOSchema(ioschema, IOSchema.OUT);
			
			for (int j = 0; j < this.getCount(firstKeyName); j++) 
			{
				for (int i = 0; i < fields.length; i++) {
					
					String key = fields[i].getName();
					Object val = this.get(key, j);
					dest.put(key, val, j);
				}
				
			}
		}
		
		dest.properties = this.getProperties();
	}
	
	/** @deprecated
	 * IOSchema의 특정 Block에 해당되는 필드의 값을 입력된 DataSet에 복사한다.
	 * @param dest
	 * @param blockNames
	 * @throws Exceptions
	 * @author advan94
	 */
	public void copyDataSetByBlockNames_old(DataSet dest, String[] blockNames) throws Exception
	{
		
		
		Block[] blocks = new Block[blockNames.length];
		for(int idx=0; idx<blockNames.length; idx++)
		{
			blocks[idx] = this.getIOSchema().getBlockById(blockNames[idx]);
			if(blocks[idx] == null)
				throw new NullPointerException(blockNames[idx] + " is null");
			
		}
		
		
		
		int loopingCnt = 0;
		for(int idx=0; idx<blocks.length; idx++)
		{
			Field[] fields = blocks[idx].getFields();
			
			try
			{
				for(int i=0;i<fields.length;i++)
				{
					if(fields[i].getLabel() == null || "".equals(fields[i].getLabel()))
					{
						if(this.getIOSchema().getOutputField(fields[i].getName()).getLabel() != null)
						{							
							fields[i].setLabel(this.getIOSchema().getOutputField(fields[i].getName()).getLabel());							
						}
					}
				}
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			String firstKeyName = fields[0].getName();
			
			if(fields[0].getProperty("datasetPrefix") != null)
			{
				firstKeyName = fields[0].getProperty("datasetPrefix") + firstKeyName.trim();
				
				//System.out.println(blocks[idx].getName()+" firstKeyName:"+firstKeyName+" .count:"+this.getCount(firstKeyName));
				
			}
			
			
			IOSchema ioschema = new IOSchema("/temp/"+java.util.UUID.randomUUID()+"/"+blocks[idx].getName(), null, blocks);
			dest.setIOSchema(ioschema, IOSchema.OUT);
			
			
			//jdf.framework.core.log.Logger.debug.println(">>>>>>>>>>>>>>"+blocks[idx].getName() + ":"+blocks[idx].getIterationNum());
			
			if(blocks[idx].getIterationNum() == 1)
				loopingCnt = 1;
			else
				loopingCnt = this.getCount(firstKeyName);
			
			for (int j = 0; j < loopingCnt; j++) 
			{
				for (int i = 0; i < fields.length; i++) {
					
					String key = fields[i].getName();
					
					if(fields[i].getProperty("datasetPrefix") != null)
					{
						key = fields[i].getProperty("datasetPrefix") + key.trim();
					}
					
					
					Object val = this.get(key, j);
					dest.put(key, val, j);
					//System.out.println(blocks[idx].getName()+" key:"+key+" .put:"+val);
				}
				
			}
		}
		
		dest.properties = this.getProperties();
		
	}
	
	public void copyDataSetByBlockNames(DataSet dest, String[] blockNames, int start, int end) throws Exception
	{	
		Block[] blocks = new Block[blockNames.length];
		for(int idx=0; idx<blockNames.length; idx++)
		{
			blocks[idx] = this.getIOSchema().getBlockById(blockNames[idx]);
			if(blocks[idx] == null)
				throw new NullPointerException(blockNames[idx] + "is null");
			
		}
		
		int loopingCnt = 0;		
		int startIdx = 0;
		int endIdx = 0;
		for(int idx=0; idx<blocks.length; idx++)
		{
			Field[] fields = blocks[idx].getFields();
			
			String firstKeyName = fields[0].getName();
			
			if(fields[0].getProperty("datasetPrefix") != null)
			{
				firstKeyName = fields[0].getProperty("datasetPrefix") + firstKeyName.trim();				
				//System.out.println(blocks[idx].getName()+" firstKeyName:"+firstKeyName+" .count:"+this.getCount(firstKeyName));				
			}
			
			
			IOSchema ioschema = new IOSchema("/temp/"+java.util.UUID.randomUUID()+"/"+blocks[idx].getName(), null, blocks);
			dest.setIOSchema(ioschema, IOSchema.OUT);
			
			if(blocks[idx].getIterationNum() == 1)
			{
				loopingCnt = 1;
				startIdx = 0;
				endIdx = 1;
			}
			else
			{
				loopingCnt = this.getCount(firstKeyName);
				startIdx = start;
				endIdx = end;
				
				if(endIdx > loopingCnt) endIdx = loopingCnt;
			}
			
			Logger.info.println("["+blocks[idx].getName()+"] loopingCnt="+loopingCnt+", start="+start+", end="+end);
			
			int kk = 0;
			for (int j = startIdx; j < endIdx; j++)
			{
				for (int i = 0; i < fields.length; i++)
				{					
					String key = fields[i].getName();
					
					if(fields[i].getProperty("datasetPrefix") != null)
					{
						key = fields[i].getProperty("datasetPrefix") + key.trim();
					}
					
					
					Object val = this.get(key, j);
					dest.put(key, val, kk);					
				}		
				kk++;
				
			}
		}
		
		dest.properties = this.getProperties();
	}
	
	
	
	
	
	private void setValueArray(Object key, ValueArray val) {
		super.put(key, val);
	}

	/**
	 * 해당 key에서 해당 값이 존재하는 index 번호를 배열로 return 한다. 없다면 int[0] 를 return
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public int[] findValueIndexArray(Object key, Object val) {
		ValueArray values = getValueArray(key);

		if (values == null)
			return new int[0];

		return values.findValueIndexArray(val);
	}

	/**
	 * 해당 key에서 해당 값이 존재하는 index 번호를 반환, 여러개가 있더라도 처음에 찾는것을 반환
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public int findValueIndex(Object key, Object val) {
		ValueArray values = getValueArray(key);

		if (values == null)
			return -1;

		return values.findValueIndex(val);
	}

	/**
	 * 해당 키에 대한 값중에서 seq번호에 위치한 값을 반환한다.
	 * 
	 */
	public Object get(Object key, int seq) {
		// ValueArray values = getValueArray(key);

		Object obj = null;
		ValueArray values = (ValueArray) super.get(key);
		if (values != null) {
			obj = values.get(seq);

			if (obj == null) {
				// unfixNull 이 세팅되면 그냥 null을 return 한다.
				if (NULL == null)
					return null;

				Field tmp = this.getField(key.toString());
				if (tmp != null)
					obj = tmp.getDefaultValue();

				if (obj != null)
					return obj;
			} else
				return obj;
		}

		return NULL;
	}

	/**
	 * 해당 키에 대한 값을 int형으로 가져온다.
	 * 
	 */
	public int getInt(String key) throws NumberFormatException {
		return getInt(key, 0);
	}

	/**
	 * 해당 키에 대한 값을 int형으로 가져온다.
	 * 
	 */
	public int getInt(Object key, int seq) throws NumberFormatException {
		// Object x = getValueArray(key)[seq+1];
		Object x = get(key, seq);

		try {

			if (x instanceof Integer)
				return ((Integer) x).intValue();

			else if (x instanceof Long)
				return ((Long) x).intValue();
			else {
				String val = x.toString().trim();
				return Integer.parseInt(val);
			}

		} catch (NumberFormatException nfe) {
			if (this.fixNum)
				return 0;
			throw new NumberFormatException(key.toString() + ":"
					+ nfe.getMessage());
		}

	}

	/**
	 * 값이 없는 경우는 default 값을 return;
	 * 
	 * @param key
	 * @param seq
	 * @param defaultVal
	 * @return
	 */
	public int getInt(Object key, int seq, int defaultVal) {
		try {

			return getInt(key, seq);

		} catch (Exception e) {
			return defaultVal;
		}
	}

	/**
	 * 해당 키에 대한 값을 long형으로 가져온다.
	 * 
	 */
	public long getLong(String key) throws NumberFormatException {
		return getLong(key, 0);
	}

	/**
	 * 해당 키에 대한 값을 long형으로 가져온다.
	 * 
	 */
	public long getLong(Object key, int seq) throws NumberFormatException {
		// Object x = getValueArray(key)[seq+1];
		Object x = get(key, seq);

		try {

			if (x instanceof Integer)
				return ((Integer) x).longValue();

			else if (x instanceof Long)
				return ((Long) x).longValue();
			else {
				String val = x.toString().trim();
				return Long.parseLong(val);
			}

		} catch (NumberFormatException nfe) {
			if (this.fixNum)
				return 0;
			throw new NumberFormatException(key.toString() + ":"
					+ nfe.getMessage());
		}

	}

	/**
	 * 값이 없는 경우는 default 값을 return;
	 * 
	 * @param key
	 * @param seq
	 * @param defaultVal
	 * @return
	 */
	public long getLong(Object key, int seq, long defaultVal) {
		try {

			return getLong(key, seq);

		} catch (Exception e) {
			return defaultVal;
		}
	}

	/**
	 * float 형으로 값을 가져온다.
	 * 
	 * 
	 * @param key
	 * @param seq
	 * @return
	 * @throws NumberFormatException
	 */
	public float getFloat(Object key, int seq) throws NumberFormatException {
		// Object x = getValueArray(key)[seq+1];
		Object x = get(key, seq);

		try {

			if (x instanceof Float)
				return ((Float) x).floatValue();

			else if (x instanceof Double)
				return ((Double) x).floatValue();
			else

			{
				String val = x.toString().trim();
				return Float.parseFloat(val);
			}

		} catch (NumberFormatException nfe) {
			if (this.fixNum)
				return 0;
			throw new NumberFormatException(key.toString() + ":"
					+ nfe.getMessage());
		}

	}

	/**
	 * float 형으로 값을 가져온다.
	 * 
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public float getFloat(Object key) throws NumberFormatException {
		return getFloat(key, 0);
	}

	/**
	 * float 형으로 값을 가져온다. 값이 없는경우는 default값으로 가져온다.
	 * 
	 * @param key
	 * @param seq
	 * @param defaultVal
	 * @return
	 * @throws NumberFormatException
	 */
	public float getFloat(Object key, int seq, float defaultVal)
			throws NumberFormatException {
		try {
			return getFloat(key, seq);
		} catch (Exception e) {
			return defaultVal;
		}
	}

	/**
	 * getText의 역활을 기본적으로 값을 String 타입으로 반환한다. 두번째 기능은 BLD에 format 속성이 정의된 경우 값을
	 * formating 하여 return한다.
	 * 
	 * @param key
	 * @return
	 */
	public String getText(Object key) {
		return getText(key, 0);
	}

	/**
	 * 해당 키에 대한 값을 text 포맷으로 가져온다.
	 * 
	 */
	public String getText(Object key, int seq) {

		// Object x = get(key, seq);
		Object x = NULL;
		ValueArray values = (ValueArray) super.get(key);
		Field field = null;
		if (values != null) {
			x = values.get(seq);

			if (x == null) {
				// unfixNull 이 세팅되면 그냥 null을 return 한다.
				if (NULL == null)
					return null;

				field = values.getField();
				if (field == null) {
					field = this.getField(key.toString());
					values.setField(field);
				}
				if (field != null)
					x = field.getDefaultValue();

			} else if (x.getClass().isArray()) {

				try {
					byte[] bre = (byte[]) x;

					x = new String(bre);
				} catch (java.lang.ClassCastException e) {

				}
			} else {
				field = values.getField();
				if (field == null) {
					field = this.getField(key.toString());
					values.setField(field);
				}
			}
		} else {
			field = this.getField(key.toString());
			if (field != null)
				x = field.getDefaultValue();
		}

		// if (this.schema == null) return x.toString();

		// Field field = this.getField(key.toString());

		if (field != null) 
		{
			Formatter formatter = field.getFormatter();
			if (formatter != null && x != null) 
			{
				if(field.getFormat() != null && field.getFormat().indexOf("mask:") > -1)
				{
					String isUnMask = this.getProperty(UNMASK_KEY);
					
					if(isUnMask != null && "true".equals(isUnMask))
					{						
						return x.toString();
					}
				}
				
				try 
				{
					return formatter.format(x);
				} catch (IllegalArgumentException ee) 
				{
					// ee.printStackTrace();
					jdf.framework.core.log.Logger.debug.println(LOG_ID + "[" + key
							+ "] format err. " + ee.getMessage());
				}
			} else if (x != null) 
			{
				return x.toString();
			}
		} else if (x == null)
			return NULL;
		else
			return x.toString();

		return NULL;
	}

	/**
	 * 앞뒤 공백을 제거하고, format 정보가 있으면 formating 까지 한 text 문자열을 반환
	 */
	public String getTrimmedText(Object key, int seq) {
		String val = getText(key, seq);
		if (val != null)
			val = val.trim();

		return val;
	}

	/**
	 * 앞뒤 공백을 제거하고, format 정보가 있으면 formating 까지 한 text 문자열을 반환
	 */
	public String getTrimmedText(Object key) {
		return getTrimmedText(key, 0);
	}

	/**
	 * double 형으로 값을 return
	 * 
	 * @param key
	 * @param seq
	 * @return
	 * @throws NumberFormatException
	 */
	public double getDouble(Object key, int seq) throws NumberFormatException {
		try {
			return Double.parseDouble(this.get(key, seq).toString());
		} catch (NumberFormatException e) {
			if (this.fixNum)
				return 0;
			throw e;
		}
	}

	/**
	 * double 형으로 값을 return
	 * 
	 * @param key
	 * @param seq
	 * @param defaultVal
	 * @return
	 */
	public double getDouble(Object key, int seq, double defaultVal) {
		try {
			return getDouble(key, seq);
		} catch (Exception e) {
			return defaultVal;
		}
	}

	/**
	 * 
	 * double 형으로 값을 return
	 * 
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public double getDouble(Object key) throws NumberFormatException {
		return getDouble(key, 0);
	}

	/**
	 * 해당 키에 대한 값을 text 포맷(#,###)으로 가져온다.
	 * 
	 * @param key
	 * @param seq
	 * @param format
	 * @return
	 */
	public String getTextNumber(Object key, int seq, String format) {
		double x = getDouble(key, seq);

		DecimalFormat df = new DecimalFormat();

		df.applyPattern(format);

		return df.format(x);
	}

	/**
	 * 해당 키에 대한 값을 text 포맷(#,###)으로 가져온다.
	 * 
	 * @param key
	 * @param seq
	 * @return
	 */
	public String getNumberFormatText(Object key, int seq) {
		double x = getDouble(key, seq);

		DecimalFormat df = new DecimalFormat();

		df.applyPattern("#,###");

		return df.format(x);
	}

	/*
	 * public String getDiv100Number(Object key, int seq) { int x = getInt(key,
	 * seq);
	 * 
	 * double y = (double) x / 100;
	 * 
	 * df.applyPattern("#,###.##");
	 * 
	 * return df.format(y).toString(); }
	 */
	
	/**
	 * 키에 대한 값을 입력한다.
	 * 
	 */
	public Object put(Object key, Object val) {

		
		if (val instanceof ValueArray) {
			super.put(key, val);
			return val;
		}
		return put(key, val, 0);
	}
	
	/**
	 * Object타입의 값을 해당 key 값과 seq 순서에 따라 입력한다.
	 * 
	 * 
	 * @param key
	 * @param val
	 * @param seq
	 * @return
	 */
	public Object put(Object key, Object val, int seq) {
		if (this == val)
			throw new Error("자기 자신의 DataSet을 담을수 없습니다.");

		ValueArray values = getValueArray(key);

		values.put(seq, val);

		toStr = null;

		if (this.maxDataSize <= seq)
			this.maxDataSize = seq + 1;

		return val;
	}

	/**
	 * 저장된 데이터중 가장 큰 갯수의 크기
	 * 
	 * @return
	 */
	public int getMaxDataSize() {
		return this.maxDataSize;
	}

	/**
	 * int 타입의 값을 해당 key 값과 seq 순서에 따라 입력한다.
	 * 
	 * @param key
	 * @param val
	 * @param seq
	 * @return
	 */
	public Object put(Object key, int val, int seq) {
		return put(key, new Integer(val), seq);
	}

	/**
	 * int 타입의 값을 해당 key 값과 첫번째 순서에 따라 입력한다.
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public Object put(Object key, int val) {
		return put(key, new Integer(val), 0);
	}

	/**
	 * long 타입의 값을 해당 key 값과 seq 순서에 따라 입력한다.
	 * 
	 * @param key
	 * @param val
	 * @param seq
	 * @return
	 */
	public Object put(Object key, long val, int seq) {
		return put(key, new Long(val), seq);
	}

	/**
	 * long 타입의 값을 해당 key 값과 첫번째 순서에 입력한다.
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public Object put(Object key, long val) {
		return put(key, new Long(val), 0);
	}

	/**
	 * float 타입의 값을 해당 key 값과 seq 순서에 입력한다.
	 * 
	 * @param key
	 * @param val
	 * @param seq
	 * @return
	 */
	public Object put(Object key, float val, int seq) {
		return put(key, new Float(val), seq);
	}

	/**
	 * float 타입의 값을 해당 key 값과 seq 순서에 입력한다.
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public Object put(Object key, float val) {
		return put(key, new Float(val), 0);
	}

	/**
	 * doeubl 타입의 값을 해당 key 값과 seq 순서에 입력한다.
	 * 
	 * @param key
	 * @param val
	 * @param seq
	 * @return
	 */
	public Object put(Object key, double val, int seq) {
		return put(key, new Double(val), seq);
	}

	/**
	 * doeubl 타입의 값을 해당 key 값과 seq 순서에 입력한다.
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public Object put(Object key, double val) {
		return put(key, new Double(val), 0);
	}

	/**
	 * 해당 key 이름으로 등록된 값을 총갯수를 반환한다.
	 * 
	 * @param key
	 * @return
	 */
	public int getCount(Object key) {
		ValueArray values = (ValueArray) super.get(key);

		if (values == null)
			return 0;
		else
			return values.getSize();
	}

	private ValueArray getValueArray(Object key) {
		ValueArray values = (ValueArray) super.get(key);

		if (values == null) {
			values = new ValueArray();
			super.put(key, values);
			keyObjList.add(key);
		}

		return values;
	}

	/**
	 * key Object를 세팅된 순서대로 되어있는 List를 반납한다.
	 * 
	 * @return
	 */
	public ArrayList<Object> getKeyObjectList() {
		return this.keyObjList;
	}

	/**
	 * 현재 DataSet에 저장된 key와 value 값을 모두 출력해 준다.
	 * 
	 */
	public String toString() {// System.out.println("3234234");
		if (toStr == null) {
			StringBuffer buf = new StringBuffer();

			if (name != null)
				buf.append(name + "\n");

			Iterator<Object> keys = super.keySet().iterator();

			for (int k = 0; keys.hasNext(); k++) {
				Object key = keys.next();

				buf.append(key).append("= ").append(this.getCount(key)).append(
						"[");

				// Object printObj = super.get(key);
				buf.append(super.get(key).toString());

				buf.append("]\n");
			}

			toStr = buf.toString() + " properties ="
					+ this.properties.toString();
		}

		return toStr;
	}

	public static void main(String[] args) throws Exception {
		DataSet ds = new DataSet();
		ds.put("aaaa", "0", 0);
		ds.put("aaaa", "1", 1);
		ds.put("aaaa", "2", 2);
		ds.put("aaaa", "3", 3);
		ds.put("aaaa", "4", 4);
		ds.put("aaaa", "5", 5);
		
		DataSet ds2 = new DataSet();
		
		ds2.copy(ds, 1, 10);
		System.out.println(ds2);
	}

	// xml test용
	public static void main3(String[] args) throws Exception {
		DataSet ds = new DataSet();

		ds.put("/article/sect1[1]/title", "t1");
		ds.put("/article/sect1[1]/para", "p1");

		ds.put("/article/sect1[1]/sect2[1]/title", "t11");
		ds.put("/article/sect1[1]/sect2[1]/para", "p11");

		ds.put("/article/sect1[1]/sect2[2]/title", "t22");
		ds.put("/article/sect1[1]/sect2[2]/para", "p22");

		ds.put("/article/sect1[2]/title", "t2");
		ds.put("/article/sect1[2]/para", "p2");

		ds.put("/article/sect1[2]/sect2[1]/title", "t-11");
		ds.put("/article/sect1[2]/sect2[1]/para", "p-11");

		ds.put("/article/sect1[2]/sect2[2]/title", "t-22");
		ds.put("/article/sect1[2]/sect2[2]/para", "p-22");

		System.out.println(ds.getXmlString());

	}

	// test용
	public static void main2(String[] args) {
		try {
			DataSet DataSet = new DataSet();

			DataSet.put("key1", "data1");
			DataSet.put("key2", "data2");
			DataSet.put("key3", "data3");
			DataSet.put("key4", "data4");
			DataSet.put("key5", "data5", 0);
			DataSet.put("key5", "data6", 1);
			DataSet.put("key5", "data7", 2);
			DataSet.put("key5", "data8", 3);
			DataSet.put("key5", "data7", 4);
			DataSet.put("key5", "data7", 7);

			System.out.println("key5 찾은 갯수:"
					+ DataSet.findValueIndexArray("key5", "data7").length);

			int[] va = DataSet.findValueIndexArray("key5", "data6");
			for (int i = 0; i < va.length; i++) {
				System.out.println(" val index " + va[i]);
			}

			System.out.println("key5 value:" + DataSet.get("key5", 2));

			System.out.println("key3 size:" + DataSet.getCount("key3"));
			System.out.println("key5 size:" + DataSet.getCount("key5"));
		} catch (Exception e) {
		}
	}

	/**
	 * 내용 자료형 이 형태대로 Map 에 여러값(배열형태)이 들어간다.
	 * 
	 * @author
	 * 
	 */
	final class ValueArray implements java.io.Serializable, Cloneable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		int size;

		Object[] values = new Object[DEFAULT_SIZE];

		Field field = null;

		ValueArray() {
		}

		ValueArray(Object[] values, int size) {
			this.values = values;
			this.size = size;
		}

		Object[] getArray() {
			return values;
		}

		int getSize() {
			return size;
		}

		void put(int idx, Object val) {
			// 기존 준비된 array보다 새로 들어갈 idx가 크면
			// array사이즈 증가시킴
			while (idx >= values.length) {
				int newSize = values.length * 2;
				Object[] newValues = new Object[newSize];

				System.arraycopy(values, 0, newValues, 0, values.length);

				values = newValues;
			}

			values[idx] = val;

			if (idx >= size)
				this.size = idx + 1;

		}

		Object get(int idx) {
			if (idx >= size)
				return null;
			return values[idx];
		}

		int[] findValueIndexArray(Object val) {
			int[] result = new int[size];
			int findCount = 0;

			for (int i = 0; i < size; i++) {
				if (values[i] != null && values[i].equals(val)) {

					result[findCount++] = i;

					// System.out.println(findCount+" 찾았다 "+i);
				}

			}
			int[] newResult = new int[findCount];
			System.arraycopy(result, 0, newResult, 0, findCount);

			return newResult;
		}

		int findValueIndex(Object val) {
			for (int i = 0; i < size; i++) {
				if (values[i] != null && values[i].equals(val)) {

					return i;
				}
			}
			return -1;
		}

		public String toString() {
			StringBuffer buf = new StringBuffer();

			for (int i = 0; i < size; i++) {
				if (buf.length() > 0)
					buf.append(",");

				if (values[i] != null && values[i].getClass() == DataSet.class)
					buf.append("jdf.framework.core.data.DataSet.class");
				else
					buf.append(values[i]);
			}

			return buf.toString();
		}

		public Object clone() {
			return new ValueArray((Object[]) this.values.clone(), this.size);

		}

		void setField(Field field) {
			this.field = field;
		}

		Field getField() {
			return this.field;
		}

	}

	/**
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg) {
		Object v1 = this.toString();
		Object v2 = null;
		if (arg == null) {
			if (v1 == null)
				return true;
		} else {
			v2 = arg.toString();
			if (v2.equals(v1))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.toString().hashCode();
	}

	/**
	 * DataSet에서 실제 의미론적인 데이터가 아니라, 속성에 해당하는 값을 이와 같은 함수로 값을 세팅한다.
	 * 
	 * @param key
	 * @param val
	 */
	public void setProperty(String key, String val) {
		this.properties.setProperty(key, val);
	}

	/**
	 * 속성값을 가죠온다.
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}

	public Properties getProperties() {
		return this.properties;
	}

	/**
	 * 속성값들을 세팅한다.
	 * 
	 * @param props
	 */
	public void setProperties(Properties props) {
		this.properties = props;
	}
	
	public DataSet clone()
	{		
		DataSet rtn = null;
		try {
			rtn = (DataSet)super.clone();
			rtn.properties = (Properties)properties.clone();
			rtn.keyObjList = (ArrayList<Object>)keyObjList.clone();
			//return (DataSet)super.clone();
		} catch (CloneNotSupportedException e) {			
			Logger.err.println(LOG_ID+Utility.getStackTrace(e));
		}
		return rtn;

	}

	// //////////////////////////////////////////////////////////////////
	private SimpleElement root;

	private ConcurrentMap<String, SimpleElement> nodeMap;

	/**
	 * key 가 xpath 형태로 세팅되어 있는경우 그 xpath 정보에 따라 XML 문자열을 생성한다.
	 * 
	 * @return
	 */
	public String getXmlString() {
		SimpleElement tmp = getSimpleElement();

		if (tmp == null)
			return "";

		return tmp.toString();
	}

	/**
	 * Namespace 를 세팅한 xml 문자열 생성
	 * 
	 * @param ns
	 * @return
	 */
	public String getXmlString(String ns, String prefix) {
		SimpleElement tmp = getSimpleElement();

		if (tmp == null)
			return "";

		return tmp.toString(ns, prefix);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public SimpleElement getSimpleElement() {
		nodeMap = new ConcurrentHashMap<String, SimpleElement>();
		List keyObjList = this.getKeyObjectList();

		for (int i = 0; i < keyObjList.size(); i++) {

			String xpath = (String) keyObjList.get(i);
			// 처음문자가 / 로 시작되지 않으면 bypass
			if (xpath.indexOf("/") != 0)
				continue;
			// System.out.println(" * "+xpath);

			for (int j = 0; j < this.getCount(xpath); j++) {
				SimpleElement parent = getParent(xpath, j);
				String elementNm = getLastElementName(xpath);
				if (parent != null)
					parent.appendChild(new SimpleElement(elementNm, this
							.getText(xpath, j)));

			}

		}

		return root;
	}

	/**
	 * xpath 의 마지막 element 이름을 return
	 * 
	 * @param xpath
	 * @return
	 */
	private String getLastElementName(String xpath) {
		String tmp = xpath.substring(xpath.lastIndexOf("/") + 1);

		// /test/name[2] 와 같이 뒤에 index 번호가 있는 경우
		if (tmp.indexOf("[") > 0) {
			tmp = tmp.substring(0, tmp.lastIndexOf("["));
		}
		// System.out.println(xpath+" LE : "+tmp );
		return tmp;

	}

	private String[] getXPathArray(String xpath) {

		String[] a = SmartStringArray.split("/", xpath);

		String[] xpathArray = new String[a.length - 1];
		for (int i = 0; i < xpathArray.length; i++) {
			if (i > 0)
				xpathArray[i] = xpathArray[i - 1];
			else
				xpathArray[i] = "";

			xpathArray[i] = xpathArray[i] + "/" + a[i + 1];

			// System.out.println( xpathArray[i]);
		}

		return xpathArray;

	}

	private SimpleElement getParent(String xpath, int seq) {
		String[] xpathArray = getXPathArray(xpath);

		SimpleElement parent = null;

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < xpathArray.length - 1; i++) {

			String path = xpathArray[i];
			// String nodeKey = path;
			buf.append(path);
			// System.out.println( " 1 NODEMAP : "+nodeKey);

			// xpath 의 맨 마지막 자식 node 의 바로 위 부모 node
			if (i == xpathArray.length - 2) {

				if (buf.indexOf("[") < 0)
					buf.append("[").append(seq + 1).append("]");
			}
			// System.out.println("path = "+path);

			String nodeKey = buf.toString();
			SimpleElement element = (SimpleElement) this.nodeMap.get(nodeKey);
			if (element == null) {
				element = new SimpleElement(getLastElementName(path));

				// System.out.println(" 2 NODEMAP : " + nodeKey);
				this.nodeMap.put(nodeKey, element);
				if (i == 0)
					this.root = element;
				if (parent != null)
					parent.appendChild(element);
			}

			parent = element;

		}

		return parent;

	}	
	
	/**
	 * 
	 * @author
	 * 
	 * TODO To change the template for this generated type comment go to Window -
	 * Preferences - Java - Code Style - Code Templates
	 */
	public static class SimpleElement implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		

		private StringBuffer buf;

		private String elementNm;

		private String value;

		private List<SimpleElement> children = new ArrayList<>();

		private List<Attribute> attrList = new ArrayList<>();

		public SimpleElement(String elementNm) {
			this.elementNm = elementNm;
		}

		public SimpleElement(String elementNm, String val) {
			this.elementNm = elementNm;
			this.value = val;
		}

		public void appendChild(SimpleElement doc) {
			children.add(doc);
		}

		public void appendAttribute(String name, String value) {
			Attribute attr = new Attribute(name, value);
			attrList.add(attr);

		}

		public void toString(StringBuffer buf, String ns, String prefix) {
			buf.append("<");

			if (prefix != null)
				buf.append(prefix).append(":");
			buf.append(elementNm);

			if (ns != null) {
				if (prefix != null)
					buf.append(" xmlns:").append(prefix).append("=\"");
				else
					buf.append(" xmlns=\"");
				buf.append(ns).append("\"");

				buf.append(" xmlns:ns1=\"http://www.w3.org/2001/XMLSchema\"");
				ns = null;
			}

			if (attrList.size() > 0) {
				for (int j = 0; j < attrList.size(); j++) {
					buf.append(" ").append(attrList.get(j));
				}
			}
			buf.append(">");

			for (int i = 0; i < children.size(); i++) {
				SimpleElement child = (SimpleElement) children.get(i);
				child.toString(buf, ns, prefix);
			}
			if (value != null)
				buf.append(value);

			buf.append("</");
			if (prefix != null)
				buf.append(prefix).append(":");
			buf.append(elementNm).append(">\n");
		}

		public String toString() {
			buf = new StringBuffer();
			toString(buf, null, null);

			return buf.toString();
		}

		// xmlns:ns0="http://www.iblug.com/kpmg"
		public String toString(String ns, String prefix) {
			buf = new StringBuffer();
			toString(buf, ns, prefix);

			return buf.toString();
		}

		private static class Attribute {
			private String name;

			private String value;

			Attribute(String name, String value) {
				this.name = name;
				this.value = value;
			}

			String getName() {
				return this.name;
			}

			String getValue() {
				return this.value;
			}

			public String toString() {
				return this.name + "=\"" + this.value + "\"";
			}

		}

	}

	/**
	 * 해당 key 를 기준으로 데이터들을 소팅한다.
	 * 
	 * @param key
	 * @param otherSortKeyArray
	 */
	public void sort(Object key, Object[] otherSortKeyArray) {
		this.toStr = null;

		ValueArray values = (ValueArray) super.get(key);

		if (values == null)
			return;

		Object[] keyArray = values.getArray();
		Object[] src = (Object[]) keyArray.clone();

		int low = 0;
		int high = values.getSize();

		ValueArray[] others = new ValueArray[0];

		if (otherSortKeyArray != null) {
			others = new ValueArray[otherSortKeyArray.length];

			for (int i = 0; i < others.length; i++) {
				others[i] = (ValueArray) super.get(otherSortKeyArray[i]);

			}
		}

		ValueArray[] src2 = new ValueArray[others.length];
		for (int k = 0; k < others.length; k++) {
			src2[k] = (ValueArray) others[k].clone();
		}

		mergeSort(src, keyArray, low, high, 0, src2, others);

	}

	/**
	 * 해당 key 를 기준으로 다른 키의 값들과 같이 내림차순으로 정렬한다.
	 * 
	 * 
	 * @param key
	 * @param otherSortKeyArray
	 */
	public void sortReverse(Object key, Object[] otherSortKeyArray) {
		this.toStr = null;
		ValueArray values = (ValueArray) super.get(key);

		if (values == null)
			return;

		Object[] keyArray = values.getArray();
		Object[] src = (Object[]) keyArray.clone();

		int low = 0;
		int high = values.getSize();

		ValueArray[] others = new ValueArray[0];

		if (otherSortKeyArray != null) {
			others = new ValueArray[otherSortKeyArray.length];

			for (int i = 0; i < others.length; i++) {
				others[i] = (ValueArray) super.get(otherSortKeyArray[i]);

			}
		}

		ValueArray[] src2 = new ValueArray[others.length];
		for (int k = 0; k < others.length; k++) {
			src2[k] = (ValueArray) others[k].clone();
		}

		mergeSort(src, keyArray, low, high, 0, Collections.reverseOrder(),
				src2, others);

	}

	/**
	 * key 의 값을 기준으로 정렬하되 java.util.Comparator 구현체를 이용하여 검색한다.
	 * 
	 * @param key
	 * @param otherSortKeyArray
	 * @param c
	 */
	public void sort(Object key, Object[] otherSortKeyArray, Comparator c) {
		this.toStr = null;
		ValueArray values = (ValueArray) super.get(key);

		if (values == null)
			return;

		Object[] keyArray = values.getArray();
		Object[] src = (Object[]) keyArray.clone();

		int low = 0;
		int high = values.getSize();

		ValueArray[] others = new ValueArray[0];

		if (otherSortKeyArray != null) {
			others = new ValueArray[otherSortKeyArray.length];

			for (int i = 0; i < others.length; i++) {
				others[i] = (ValueArray) super.get(otherSortKeyArray[i]);

			}
		}

		ValueArray[] src2 = (ValueArray[]) others.clone();

		mergeSort(src, keyArray, low, high, 0, c, src2, others);
	}

	/**
	 * merge sort 를 수행한다.
	 * 
	 * @param src
	 * @param dest
	 * @param low
	 * @param high
	 * @param off
	 * @param c
	 * @param srcValueArrays
	 * @param valueArrays
	 */
	private static void mergeSort(Object src[], Object dest[], int low,
			int high, int off, Comparator c, ValueArray[] srcValueArrays,
			ValueArray[] valueArrays) {
		int length = high - low;

		// Insertion sort on smallest arrays
		if (length < INSERTIONSORT_THRESHOLD) {
			for (int i = low; i < high; i++)
				for (int j = i; j > low && c.compare(dest[j - 1], dest[j]) > 0; j--)
					swap(dest, j, j - 1, valueArrays);
			return;
		}

		// Recursively sort halves of dest into src
		int destLow = low;
		int destHigh = high;
		low += off;
		high += off;
		int mid = (low + high) >>> 1;
		mergeSort(dest, src, low, mid, -off, c, valueArrays, srcValueArrays);
		mergeSort(dest, src, mid, high, -off, c, valueArrays, srcValueArrays);

		// If list is already sorted, just copy from src to dest. This is an
		// optimization that results in faster sorts for nearly ordered lists.
		if (c.compare(src[mid - 1], src[mid]) <= 0) {
			System.arraycopy(src, low, dest, destLow, length);

			for (int i = 0; i < valueArrays.length; i++) {
				Object[] arrSrc = srcValueArrays[i].getArray();
				Object[] arrDest = valueArrays[i].getArray();
				System.arraycopy(arrSrc, low, arrDest, destLow, length);
			}

			return;
		}

		// Merge sorted halves (now in src) into dest
		for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
			if (q >= high || p < mid && c.compare(src[p], src[q]) <= 0) {
				int pp = p++;
				dest[i] = src[pp];
				for (int k = 0; k < valueArrays.length; k++) {
					Object[] arrSrc = srcValueArrays[k].getArray();
					Object[] arrDest = valueArrays[k].getArray();
					arrDest[i] = arrSrc[pp];
				}
			} else {
				int qq = q++;
				dest[i] = src[qq];
				for (int k = 0; k < valueArrays.length; k++) {
					Object[] arrSrc = srcValueArrays[k].getArray();
					Object[] arrDest = valueArrays[k].getArray();
					// System.out.print(valueArrays.length+" * "+i +">"+qq);
					arrDest[i] = arrSrc[qq];
				}
			}
		}
	}

	/**
	 * mergeSort 를 사용한다.
	 * 
	 * 
	 * @param src
	 * @param dest
	 * @param low
	 * @param high
	 * @param off
	 * @param srcValueArrays
	 * @param valueArrays
	 */
	private static void mergeSort(Object src[], Object dest[], int low,
			int high, int off, ValueArray[] srcValueArrays,
			ValueArray[] valueArrays) {

		int length = high - low;

		// Insertion sort on smallest arrays
		if (length < INSERTIONSORT_THRESHOLD) {
			for (int i = low; i < high; i++) {

				for (int j = i; j > low
						&& ((Comparable) dest[j - 1])
								.compareTo((Comparable) dest[j]) > 0; j--) {

					swap(dest, j, j - 1, valueArrays);
				}
			}

			return;
		}

		// Recursively sort halves of dest into src
		int destLow = low;
		int destHigh = high;
		low += off;
		high += off;
		int mid = (low + high) >>> 1;
		mergeSort(dest, src, low, mid, -off, valueArrays, srcValueArrays);
		mergeSort(dest, src, mid, high, -off, valueArrays, srcValueArrays);

		// If list is already sorted, just copy from src to dest. This is an
		// optimization that results in faster sorts for nearly ordered lists.
		if (((Comparable) src[mid - 1]).compareTo((Comparable) src[mid]) <= 0) {
			System.arraycopy(src, low, dest, destLow, length);
			for (int i = 0; i < valueArrays.length; i++) {
				Object[] arrSrc = srcValueArrays[i].getArray();
				Object[] arrDest = valueArrays[i].getArray();
				System.arraycopy(arrSrc, low, arrDest, destLow, length);
			}
			return;
		}

		// Merge sorted halves (now in src) into dest
		for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
			if (q >= high || p < mid
					&& ((Comparable) src[p]).compareTo(src[q]) <= 0) {
				int pp = p++;
				dest[i] = src[pp];
				for (int k = 0; k < valueArrays.length; k++) {
					Object[] arrSrc = srcValueArrays[k].getArray();
					Object[] arrDest = valueArrays[k].getArray();
					arrDest[i] = arrSrc[pp];
				}
			} else {
				int qq = q++;
				dest[i] = src[qq];
				for (int k = 0; k < valueArrays.length; k++) {
					Object[] arrSrc = srcValueArrays[k].getArray();
					Object[] arrDest = valueArrays[k].getArray();
					arrDest[i] = arrSrc[qq];
				}
			}
		}

	}

	/**
	 * 값을 swap
	 * 
	 * @param x
	 * @param a
	 * @param b
	 */
	private static void swap(Object x[], int a, int b) {

		Object t = x[a];
		x[a] = x[b];
		x[b] = t;
	}

	/**
	 * 값을 치환
	 * 
	 * @param x
	 * @param a
	 * @param b
	 * @param valueArrays
	 */
	private static void swap(Object x[], int a, int b, ValueArray[] valueArrays) {
		swap(x, a, b);

		if (valueArrays == null)
			return;
		for (int i = 0; i < valueArrays.length; i++) {
			Object[] arr = valueArrays[i].getArray();
			swap(arr, a, b);
		}

	}

	/**
	 * DataSet을 하나의 row 단위의 DataSet 으로 구성된 Enumeration 으로 return 한다.
	 * 
	 * @return
	 */
	public java.util.Enumeration toEnumeration() {
		return new DataSetEnumeration(this);
	}

	/**
	 * List 형태로 가져온다.
	 * 
	 * @return
	 */
	public List toList() {
		return new DataSetList(this);
	}

	/**
	 * List 형태로 변환
	 * 
	 * @param key
	 * @return
	 */
	public List toList(Object key) {
		return new DataSetList(this, key);
	}

}