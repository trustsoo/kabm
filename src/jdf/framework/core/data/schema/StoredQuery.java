package jdf.framework.core.data.schema;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jdf.framework.core.data.DataSet;


/**
 * <b><code>StoredQuery</code></b>
 * <p>
 * DBMS의 SQL query 정보를 가지고 있는 class 
 * </p>
 *
 * @author 
 * @version 1.0
 */
public class StoredQuery 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// query의 id 또는 name
    private String name;

    // datasource (connection pool) 이름
    private String dataSourceName;

    // 일반 query 문인지, procedure 인지
    private String queryType; //sql query, storeprocedure

    // 실제 query문
    private String sqlQuery;

    // query문과 mapping되는 input 필드명
    private String[] intputFieldNames= new String[0];

    // query문과 mapping되는 output 필드명
    private String[] outputFieldNames= new String[0];

    private boolean isSelectQuery= false;

    // 실행시 query 가 변경되어야하는지 여부
    // 즉 query 내에 $name 을 썼는지 여부 
    private boolean isDanymicQuery= false;
    
    // max row 수
    private int maxRows=0;
    
    // the number of rows to fetch
    private int fetchSize=0;
    
    //the number of query timeout(Statement.setQueryTimeout(seconds))
    private int queryTimeOut = 0;
    
    // loop를 돌면서 쿼리를 실행할 것인가?
    // 여러 입력값을 loop 돌면서 입려하는 경우
    private boolean isLoopQuery=false;
    
    
    private List<ProcedureMapper> procedureMappings = new ArrayList<>();

    public StoredQuery()
    {}

    /**
     * query와 mapping 되는 input field name을 배열로 넘긴다.
     * 
     * Returns the intputFieldNames.
     * @return String[]
     */
    public String[] getIntputFieldNames()
    {
        return intputFieldNames;
    }
    
    
    private Field[] inputFieldArray;
    
    /**
     * output용 field 정보를 return 한다.
     * 
     * @param schema
     * @return
     */
    public Field[] getInputFieldArray(IOSchema schema)
    {
    	if(inputFieldArray==null) {
    		inputFieldArray = new Field[intputFieldNames.length];
    		
    		for(int i=0;i<inputFieldArray.length;i++)
    		{
    			inputFieldArray[i] = schema.getInputField(intputFieldNames[i]);
    		}
    		
    	}
    	
    	return inputFieldArray;
    }

    /**
     * query 의 이름 즉 id를 넘긴다.
     * 
     * Returns the name.
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * query의 타입정보를 반환한다.
     * 일반적인 경우는 null, procedure인 경우는 'procedure'를 반환한다.
     * 
     * Returns the name.
     * @return String
     */
    public String getQueryType()
    {
        return queryType;
    }

    /**
     * query와 mapping 되는 output field name을 배열로 넘긴다.
     * 
     * Returns the outputFieldNames.
     * @return String[]
     */
    public String[] getOutputFieldNames()
    {
        return outputFieldNames;
    }
    
    
    
    
    private Field[] outputFieldArray;
    
    /**
     * output용 field 정보를 return 한다.
     * 
     * @param schema
     * @return
     */
    public Field[] getOutputFieldArray(IOSchema schema)
    {
    	if(outputFieldArray==null) {
    		outputFieldArray = new Field[outputFieldNames.length];
    		
    		for(int i=0;i<outputFieldArray.length;i++)
    		{
    			outputFieldArray[i] = schema.getOutputField(outputFieldNames[i]);
    		}
    		
    	}
    	
    	return outputFieldArray;
    }
    

    /**
     * query 문을 넘긴다.
     * 
     * Returns the query.
     * @return String
     */
    public String getSqlQuery()
    {
        return sqlQuery;
    }

    /**
     * Sets the intputFieldNames.
     * 
     * @param intputFieldNames The intputFieldNames to set
     */
    public void setIntputFieldNames(String[] intputFieldNames)
    {
        if (intputFieldNames != null)
            this.intputFieldNames= intputFieldNames;
    }

    /**
     * Sets the name.
     * 
     * @param name The name to set
     */
    public void setName(String name)
    {
        this.name= name;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setQueryType(String queryType)
    {
        this.queryType= queryType;
    }

    /**
     * Sets the outputFieldNames.
     * @param outputFieldNames The outputFieldNames to set
     */
    public void setOutputFieldNames(String[] outputFieldNames)
    {
        if (outputFieldNames != null)
            this.outputFieldNames= outputFieldNames;
    }

    /**
     * Sets the query.
     * @param query The query to set
     */
    public void setSqlQuery(String query)
    {

        if (query == null || query.trim().length() == 0)
            throw new IllegalArgumentException("not valid query");

        this.sqlQuery= query.trim();

        if (this.sqlQuery.toUpperCase().indexOf("SELECT") == 0)
            isSelectQuery= true;
        else
            this.isSelectQuery= false;

        if (this.sqlQuery.indexOf("$") > -1 || this.sqlQuery.indexOf("#") > -1)
            isDanymicQuery= true;
        else
            isDanymicQuery= false;

    }
    
    
    public boolean isDanymicQuery()
    {
        return this.isDanymicQuery;
    }
    

    public boolean isSelectQuery()
    {
        if (isSelectQuery)
            return true;
        else
        {
            if (outputFieldNames.length > 0)
                return true;
        }

        return false;
    }

    /**
    
    
    /**
     * Returns the dataSourceName.
     * @return String
     */
    public String getDataSourceName()
    {
        return dataSourceName;
    }

    /**
     * Sets the dataSourceName.
     * @param dataSourceName The dataSourceName to set
     */
    public void setDataSourceName(String dataSourceName)
    {
    	if(dataSourceName!=null && dataSourceName.trim().length()==0)
    		return;
    		
        this.dataSourceName= dataSourceName;
    }

    /**
     * 해당 query를 수행하기위해 호출되는 method,
     * 자체적으로는 수행할 수 없고, 이것을 확장한 class에서 정의하여야 한다.
     * 
     * @see jdf.framework.core.data.processor.QueryOperator
     * 
     * @param conn
     * @param schema
     * @param input
     * @param output
     * @throws java.sql.SQLException
     * @throws Exception 
     */
    public void execute(java.sql.Connection conn, IOSchema schema, DataSet input, DataSet output)
        throws java.sql.SQLException, Exception
    {
        throw new java.sql.SQLException("execute 함수가 정의되지 않았습니다.");
    }

    /**
     * 
     * 
     * @param conn
     * @param schema
     * @param input
     * @param output
     * @return
     * @throws java.sql.SQLException
     */
    public SQLResult getSQLResult(java.sql.Connection conn, IOSchema schema, DataSet input, DataSet output)
        throws java.sql.SQLException
    {
        throw new java.sql.SQLException("getResultSet 함수가 정의되지 않았습니다.");
    }

    
    /**
     * 
     * 
     * @param rset
     * @param output
     * @param oneRow
     * @param i
     * @return
     * @throws java.sql.SQLException
     */
    public boolean fetchOneRow(java.sql.ResultSet rset, DataSet output, DataSet oneRow, int i)
        throws java.sql.SQLException
    {
        throw new java.sql.SQLException("fetchResultSet 함수가 정의되지 않았습니다.");
    }

    
    /**
     * 
     * 
     * @author
     *
     */
    public static class SQLResult
    {
        private Statement stmt;
        private java.sql.ResultSet rset;

        public SQLResult(Statement stmt, java.sql.ResultSet rset)
        {
            this.stmt= stmt;
            this.rset= rset;
        }

        public Statement getStatement()
        {
            return this.stmt;
        }

        public java.sql.ResultSet getResultSet()
        {
            return this.rset;
        }
        
        
        public void close()
        {
            try {
              this.rset.close();
            } catch(Exception e) {
            }
            
            try {
              this.stmt.close();
            } catch(Exception e) {
            }
          
        }

    }


	/**
	 * @return
	 */
	public int getMaxRows()
	{
		return maxRows;
	}


	/**
	 * @param i
	 */
	public void setMaxRows(int i)
	{
		maxRows = i;
	}
	
	
	/**
     * @return Returns the queryTimeOut.
     */
	public int getQueryTimeOut()
	{
		return queryTimeOut;
	}
	
	/**
     * 
     */
	public void setQueryTimeOut(int queryTimeOut)
	{
		this.queryTimeOut = queryTimeOut;
	}
	
	
	
    /**
     * @return Returns the fetchSize.
     */
    public int getFetchSize()
    {
        return fetchSize;
    }
    
    /**
     * @param fetchSize The fetchSize to set.
     */
    public void setFetchSize(int fetchSize)
    {
        this.fetchSize = fetchSize;
    }
	/**
	 * loop 수행을 하는 query인가? 세팅
	 * 
	 * @param isLoop
	 */
	public void setLoopQuery(boolean isLoop)
	{
		this.isLoopQuery=isLoop;
	}
	
	/**
	 * loop 수행을 하는 query 인가?
	 * 
	 * @return
	 */
	public boolean isLoopQuery()
	{
		return this.isLoopQuery;
	}
	
	
	/**
	 * ProcedureMapper를 추가한다.
	 * 
	 * @param mapper
	 */
	public void addProcedureMapper(ProcedureMapper mapper)
	{
	    this.procedureMappings.add(mapper);
	}
	
	
	public int getProcedureMapperNum()
	{
	    return this.procedureMappings.size();
	}
	
	public ProcedureMapper getProcedureMapper(int seq)
	{
	    return (ProcedureMapper) this.procedureMappings.get(seq);
	}
	
	private String[] inputProcedureMappingFields;
	
	public String[] getInputProcedureMappingFields()
	{
	    if(inputProcedureMappingFields==null)
	    {
	        int size = getProcedureMapperNum();
		    this.inputProcedureMappingFields = new String[size];
		    
		    for(int i=0;i<size;i++)
		    {
		        ProcedureMapper map = getProcedureMapper(i);
		        
		        if(map.getMode() == ProcedureMapper.IN)
		        {
		            inputProcedureMappingFields[i] = map.getFieldName(0);    
		        }
		        else
		        {
		            inputProcedureMappingFields[i] = null;
		        }
		    }
	    }
	    
	    return inputProcedureMappingFields;
	}

}
