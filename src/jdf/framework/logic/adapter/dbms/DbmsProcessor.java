/*
 * Created on 2003-11-11
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.logic.adapter.dbms;


import java.util.*;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.data.schema.Processor;
import jdf.framework.core.data.schema.StoredQuery;

/**
 * DbmsProcessor
 * 
 * DBMS 관련 로직을 수행하는 쿼리
 * 
 * @author
 * @version 1.0
 * @since 2003-11-11 오후 4:04:28
 * 
 */
public class DbmsProcessor extends Processor
{
	/*
	protected LoggerWriter info = Logger.info;
	protected LoggerWriter debug = Logger.debug;
	protected LoggerWriter err = Logger.err;
	*/

	/**
	 * 
	 * java.sql.Connection 구현 객체
	 */
	protected java.sql.Connection connection;

	public DbmsProcessor()
	{
		super(DBMS);
	}

	public void setJdbcConnection(java.sql.Connection conn)
	{
		this.connection = conn;
	}

	/**
	 * 저장되어있는 모든쿼리를 순서대로 수행시킨다.
	 * 
	 * @param connection
	 * @param input
	 * @param output
	 * @throws java.sql.SQLException
	 */
	protected void executeAll(DataSet input, DataSet output) throws Exception
	{
		StoredQuery[] query = getStoredQueryArray();

		for (int i = 0; i < query.length; i++)
		{
			query[i].execute(connection, this.getIOSchema(), input, output);

		}
	}

	/**
	 * 지정된 id의 쿼리를 수행한다.
	 * 
	 * @param id
	 * @param connection
	 * @param input
	 * @param output
	 * @throws java.sql.SQLException
	 */
	protected void executeSQL(String id, java.sql.Connection connection, DataSet input, DataSet output)
		throws java.sql.SQLException, Exception
	{
		StoredQuery query = this.getStoredQueryByName(id);
		if (query == null)
			throw new java.sql.SQLException(id + " id를 가지는 query가 존재하지 않습니다");

		query.execute(connection, this.getIOSchema(), input, output);
	}

	//protected boolean executeOneRow(String id,java.sql.Connection connection, DataSet input, DataSet output) throws java.sql.SQLException

	protected StoredQuery st_query = null;

	protected int fetchSeq = 0;

	protected StoredQuery.SQLResult result;

	private Map resultSetMap = new HashMap();

	/**
	 * 
	 * 
	 * @param id
	 * @param connection
	 * @param input
	 * @param output
	 * @throws java.sql.SQLException
	 */
	private StoredQuery.SQLResult getSQLResult(
		String id,
		java.sql.Connection connection,
		DataSet input,
		DataSet output)
		throws java.sql.SQLException
	{
	  

		StoredQuery.SQLResult sResult = (StoredQuery.SQLResult) this.resultSetMap.get(id);

		st_query = this.getStoredQueryByName(id);
		
		

		if (sResult == null)
		{
		

			if (st_query == null)
				throw new java.sql.SQLException(id + " id를 가지는 query가 존재하지 않습니다");

			//query.execute(connection, this.getIOSchema(), input, output);

			sResult = st_query.getSQLResult(connection, this.getIOSchema(), input, output);
			this.result = sResult;

			this.resultSetMap.put(id, sResult);
		}

		return sResult;
	}

	/**
	 * 
	 * 
	 * 
	 * @param id
	 * @param connection
	 * @param input
	 * @param output
	 * @param oneRow
	 * @return
	 * @throws java.sql.SQLException
	 */
	protected boolean fetchOneRow(
		String id,
		java.sql.Connection connection,
		DataSet input,
		DataSet output,
		DataSet oneRow)
		throws java.sql.SQLException
	{

		StoredQuery.SQLResult result = getSQLResult(id, connection, input, output);

		boolean rsb = st_query.fetchOneRow(result.getResultSet(), output, oneRow, fetchSeq);
		fetchSeq++;

		return rsb;

	}

	/**
	 * sql id에 해당하는 resultset을 종료한다.
	 * 
	 * 
	 * @param id
	 */
	protected void closeFetch(String id)
	{

		//StoredQuery.SQLResult result = (StoredQuery.SQLResult) this.resultSetMap.get(id);
		StoredQuery.SQLResult result = (StoredQuery.SQLResult) this.resultSetMap.remove(id);
		try
		{
			if (result != null)
				//result.getResultSet().close();
				result.close();  // ResultSet과 Statement 를 모두 닫는다.
		}
		catch (Exception e)
		{
		}

		try
		{
			if (result != null)
				result.getStatement().close();
		}
		catch (Exception e)
		{
		}

		this.result = null;
	}

	/**
	 * 
	 * 
	 * 
	 *
	 */
	protected void closeFetch()
	{
		try
		{
			if (result != null)
				this.result.getResultSet().close();
		}
		catch (Exception e)
		{
		}

		try
		{
			if (result != null)
				this.result.getStatement().close();
		}
		catch (Exception e)
		{
		}

		result = null;
		this.resultSetMap.clear();
	}

	public static void main(String[] args) throws Exception
	{
		InteractionBean bean = new InteractionBean();

		bean.execute("example/db/clob_select", new DataSet());

	}

}
