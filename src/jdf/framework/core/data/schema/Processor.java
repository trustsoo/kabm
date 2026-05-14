package jdf.framework.core.data.schema;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.pool.cache.CacheManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

;

/**
 * <b><code>Processor</code> </b>
 * <p>
 * IO Schema에서 Process부분의 정보를 가지고 있는 class
 * </p>
 * 
 * @author
 * @version 1.0
 */
public abstract class Processor implements Cloneable
{

	/**
	 * processor의 type이 정의되지 않은 경우
	 */
	public final static int UNDEFINED = -1;

	/**
	 * processor의 type인 DBMS인 경우
	 */
	public final static int DBMS = 0;

	/**
	 * processor의 type이 TCP/IP를 이용한 경우
	 */
	public final static int SOCKET = 1;

	/**
	 * processor의 type이 XML인 경우
	 */
	public final static int XML = 2;

	private int dataSourceType = UNDEFINED;

	// processor 와 연관관계를 가지는 IOSchema
	private IOSchema schema;

	// processor에서 처리해야하는 StoredQuery array
	private StoredQuery[] storedQuery = new StoredQuery[] {};

	// processor에서 처리해야하는 StoredQuery map 정보
	private Map queryMap = new HashMap();

	private CacheInfo cacheInfo;

	// 이 IO Schema의 DataSet을 저장할 CacheManager;
	private CacheManager cacheManager;

	// 이 IO Schema가 실행될때 reset 되어야 할 CacheManager;
	private String resetCacheTr;

	// processor 관련 property들을 설정한다.
	protected Properties processProperties = new Properties();

	// ?
	private Connection conn;

	public Processor() {
	}

	/**
	 * processor를 생성하면 그 type결정하고 생성하여야 한다.
	 * 
	 * ex) Processor prss = new Processor(DBMS);
	 * 
	 * @param type
	 */
	public Processor(int type) {
		dataSourceType = type;
	}

	/**
	 * 
	 * @param schema
	 */
	public void setIOSchema(IOSchema schema)
	{
		this.schema = schema;
	}

	public IOSchema getIOSchema()
	{
		return this.schema;
	}

	/**
	 * Returns the dataSourceType.
	 * 
	 * @return int
	 */
	public int getDataSourceType()
	{
		return dataSourceType;
	}

	/**
	 * Returns the storedQuery.
	 * 
	 * @return StoredQuery[]
	 */
	public StoredQuery[] getStoredQueryArray()
	{
		return storedQuery;
	}

	/**
	 * Sets the storedQuery.
	 * 
	 * @param storedQuery
	 *            The storedQuery to set
	 */
	public void setStoredQuery(StoredQuery[] storedQuery)
	{
		this.storedQuery = storedQuery;

		for (int i = 0; i < storedQuery.length; i++) {
			StoredQuery query = storedQuery[i];

			String key = query.getName();

			if (key != null && key.length() > 0)
				queryMap.put(key, query);

		}
	}

	public StoredQuery getStoredQueryByName(String name)
	{
		return (StoredQuery) queryMap.get(name);
	}

	/**
	 * compile된 process의 check time과 script check time을 비교 다른경우 재 컴파일한다.
	 * 
	 * @return
	 */
	public long getCheckTime()
	{
		return 0;
	}

	public void setDataSetConnection(Connection conn)
	{
		this.conn = conn;
	}

	public Connection getDataSetConnection()
	{
		return this.conn;
	}

	/**
	 * 
	 * 
	 * @param input
	 * @param output
	 * @throws Exception
	 */
	public void execute(DataSet input, DataSet output) throws Exception
	{

		executeAll(input, output);
	}

	/**
	 * 다른 BL을 수행한다.
	 * 
	 * executeBL과 틀린점은 동일한 Connection 객체를 이용하여 호출하는 경우 사용 이점 : 속도가 약간 빠르다. 동일한 Connection을 사용하므로, trasaction 지원이 가능
	 * 
	 * 단점 : 다른 adapter의 BL은 사용할 수 없다.
	 * 
	 * 
	 * @param trcode
	 * @param input
	 * @param output
	 * @throws ResourceException
	 */
	protected void executeTxBL(String trcode, DataSet input, DataSet output) throws ResourceException
	{
		if (conn == null)
			throw new ResourceException("jdf.framework.core.data.cci.Connection not set");

		Interaction interact = conn.getBaseConnection().createInteraction();

		interact.execute(trcode, input, output);
	}

	/**
	 * 다른 Business Logic을 수행한다. 이 로직은 cache를 하지 않는다.
	 * 
	 * @param trcode
	 * @param input
	 * @param output
	 * @throws java.sql.SQLException
	 */
	protected void executeBL(String trcode, DataSet input, DataSet output) throws ResourceException
	{

		// 기존 Schema 정보 보전을 위하여
		IOSchema schema = input.getIOSchema();
		IOSchema outSchema = output.getIOSchema();

		Connection instant_conn = null;

		try {
			instant_conn = DefaultConnectionFactory.getConnection();

			Interaction inter = instant_conn.createInteraction();

			inter.execute(trcode, input, output);

		} catch (ResourceException e) {
			throw e;
		} finally {
			if (instant_conn != null)
				instant_conn.close();

			if (schema != null)
				input.setIOSchema(schema, IOSchema.IN);
			if (outSchema != null)
				output.setIOSchema(outSchema, IOSchema.OUT);
		}

	}

	/**
	 * 다른 Business Logic을 수행한다.
	 * 
	 * @param trcode
	 * @param input
	 * @param output
	 * @throws java.sql.SQLException
	 */
	protected DataSet executeBL(String trcode, DataSet input) throws ResourceException
	{

		// 기존 Schema 정보 보전을 위하여
		IOSchema schema = input.getIOSchema();

		Connection instant_conn = null;

		try {
			instant_conn = DefaultConnectionFactory.getConnection();

			Interaction inter = instant_conn.createInteraction();

			return inter.execute(trcode, input);

		} catch (ResourceException e) {
			throw e;
		} finally {
			if (instant_conn != null)
				instant_conn.close();

			input.setIOSchema(schema, IOSchema.IN);

		}

	}

	/**
	 * 
	 * 구현함수
	 * 
	 * @param intput
	 * @param output
	 * @throws Exception
	 */
	abstract protected void executeAll(DataSet input, DataSet output) throws Exception;

	/**
	 * 데이타의 복사판을 만든다.
	 * 
	 */
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public CacheInfo getCacheInfo()
	{
		return cacheInfo;
	}

	/**
	 * 
	 * 
	 * @param info
	 */
	public void setCacheInfo(CacheInfo info)
	{
		cacheInfo = info;
	}

	/**
	 * @return
	 */
	public CacheManager getCacheManager()
	{
		return cacheManager;
	}

	/**
	 * @param manager
	 */
	public void setCacheManager(CacheManager manager)
	{
		cacheManager = manager;
	}

	/**
	 * @return
	 */
	public String getResetCacheTrName()
	{
		return this.resetCacheTr;
	}

	/**
	 * @param manager
	 */
	public void setResetCacheTrName(String name)
	{
		this.resetCacheTr = name;
	}

	/**
	 * key 에 해당하는 값을 설정한다.
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value)
	{
		this.processProperties.setProperty(key, value);
	}

	/**
	 * key에 해당하는 값을 가져온다.
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key)
	{
		return this.processProperties.getProperty(key);
	}

	/**
	 * 
	 * 
	 * @param key
	 * @param ds
	 * @return
	 */
	public String getProperty(String key, DataSet ds)
	{
		String val = ds.getProperty(key);
		if (val == null) {
			val = this.getProperty(key);
		}
		return val;

	}

	/**
	 * BLD에 설정된 properties 정보를 가져온다.
	 * 
	 * @return
	 */
	public Properties getProperties()
	{
		return this.processProperties;
	}

	/**
	 * 데이터 파싱중 하나의 row 데이터만 분석이 끝마친후 호출된다.
	 * 모든 경우에 호출되는 경우는 아니고, anylogic resource adapter에 따라,
	 * 그리도 bld 의 script 에 어떠 함수를 호출하냐에 따라 틀리다.
	 * 
	 * @param idx
	 * @param oneRowDs
	 * @param output
	 * @throws ResourceException
	 */
	protected void readOneRowDataSet(int idx, DataSet oneRowDs, DataSet output) throws ResourceException
	{
		return;
	}

}