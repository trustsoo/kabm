package jdf.framework.core.pool.jdbc;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.PrintWriter;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class DBCP2PoolDriver implements Driver
{
	private final String CLASS_NAME = "<at:DBCP2PoolDriver> ";
	
	
	class Monitor extends Thread
    {
    	
		GenericObjectPool<PoolableConnection> pool = null;
    	String name = null;
    	public Monitor(String name, GenericObjectPool<PoolableConnection> pool)
    	{
    		this.pool = pool;
    		this.name = name;
    	}
    	
    	public void run()
    	{
    		while(true)
    		{
	    		Logger.info.println("<JDBC POOL> "+name+" check connection. pool idle:"+pool.getNumIdle()+", active:"+pool.getNumActive());
	    		try{Thread.sleep(30*1000);}catch(Exception ex){}
    		}
    	}
    }
	
	
	/** My URL prefix */
    protected static String URL_PREFIX = "jdbc:apache:commons:dbcp:";
    protected static int URL_PREFIX_LEN = URL_PREFIX.length();
    private static boolean accessToUnderlyingConnectionAllowed = false;
    
	static 
	{
        try 
        {
            DriverManager.registerDriver(new PoolingDriver());
        } catch(Exception e) {
        }
    }
	
	
	public DBCP2PoolDriver()
	{		
	}
	
	protected synchronized boolean isAccessToUnderlyingConnectionAllowed() {
        return accessToUnderlyingConnectionAllowed;
    }

    /**
     * Sets the value of the accessToUnderlyingConnectionAllowed property.
     * It controls if the PoolGuard allows access to the underlying connection.
     * (Default: false)
     * 
     * @param allow Access to the underlying connection is granted when true.
     */
	protected synchronized void setAccessToUnderlyingConnectionAllowed(boolean allow) {
        accessToUnderlyingConnectionAllowed = allow;
    }
	
	
	protected static java.util.concurrent.ConcurrentMap<String, GenericObjectPool<PoolableConnection>> _pools = new java.util.concurrent.ConcurrentHashMap<String, GenericObjectPool<PoolableConnection>>();
	
	
	public synchronized GenericObjectPool<PoolableConnection> getConnectionPool(String name) throws SQLException
	{
		GenericObjectPool<PoolableConnection> pool = _pools.get(name);
    	if(pool == null)
    	{
    		
    		Config conf = null;
    		String url = null;
    		
    		GenericObjectPool<PoolableConnection> _pool = null;
    		
    		try 
			{
				conf = Configuration.lookup("/connectionPool/"+name);
				
				url = conf.getString("url");
				
				AbandonedConfig aConfig = new AbandonedConfig();
	            aConfig.setLogAbandoned(conf.getBoolean("logAbandoned", false));
	            aConfig.setRemoveAbandonedOnBorrow(conf.getBoolean("removeAbandoned", false));
	            aConfig.setRemoveAbandonedTimeout(conf.getInt("removeAbandonedTimeout", 120));	
	            aConfig.setLogWriter(new PrintWriter(System.out));	            
				
				
				
				ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, conf.getString("user"), conf.getString("password"));				
				
				String validationQuery = conf.getString("validationQuery");
				PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
				poolableConnectionFactory.setValidationQuery(validationQuery);
				
				
				_pool = new GenericObjectPool<PoolableConnection>(poolableConnectionFactory);
				_pool.setMaxTotal(conf.getInt("maxTotal")); //<-- maxActive?
				_pool.setMaxIdle(conf.getInt("maxIdle"));
				_pool.setMinIdle(conf.getInt("minIdle"));
				_pool.setMaxWaitMillis(conf.getLong("maxWait"));
				_pool.setBlockWhenExhausted(conf.getBoolean("whenBlockExhausted"));//과거 commons-pool에서는 1이 block, 2가 grow //현재는 true가 블락, false가 grow
				//_pool.setWhenExhaustedAction(whenExhaustedActionByte);
		        _pool.setTestOnBorrow(conf.getBoolean("testOnBorrow"));
		        _pool.setTestOnReturn(conf.getBoolean("testOnReturn"));
		        _pool.setTestWhileIdle(conf.getBoolean("testWhileIdle"));
		        _pool.setNumTestsPerEvictionRun(conf.getInt("numTestsPerEvictionRun"));
		        _pool.setMinEvictableIdleTimeMillis(conf.getInt("minEvictableIdleTimeMillis"));
		        _pool.setTimeBetweenEvictionRunsMillis(conf.getInt("timeBetweenEvictionRunsMillis"));
				
		        setAccessToUnderlyingConnectionAllowed(conf.getBoolean("accessToUnderlyingConnectionAllowed"));
		        
				
				int defaultTransactionIsolationLevel = -1;
	            try
	            {
	            	defaultTransactionIsolationLevel = conf.getInt("defaultTransactionIsolation");
	            	poolableConnectionFactory.setDefaultTransactionIsolation(defaultTransactionIsolationLevel);
	            	Logger.info.println("<"+name+" Pool Config > maxActive :"+_pool.getMaxTotal() +", maxIdle:"+ _pool.getMaxIdle() +", minIdle:" +_pool.getMinIdle()+", transactionIsolationLevel:"+defaultTransactionIsolationLevel);
	            } catch(Exception ex)
	            {	
	            	Logger.info.println("<"+name+" Pool Config > maxActive :"+_pool.getMaxTotal() +", maxIdle:"+ _pool.getMaxIdle() +", minIdle:" +_pool.getMinIdle());
	            }
	            
	            
	            
				
				
				
				/*String whenExhaustedAction = conf.getString("whenExhaustedAction", "1");
				byte whenExhaustedActionByte = 1;
				
				GenericObjectPoolConfig config = new  GenericObjectPoolConfig();
				config.setWhenExhaustedAction(whenExhaustedActionByte);
				if("0".equals(whenExhaustedAction)) whenExhaustedActionByte = 0;
				else if("1".equals(whenExhaustedAction)) whenExhaustedActionByte = 1;
				else if("2".equals(whenExhaustedAction)) whenExhaustedActionByte = 2;			
				*
				*/
				
		        poolableConnectionFactory.setPool(_pool);
				
		       /* Class.forName("org.apache.commons.dbcp2.PoolingDriver");
		        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");				
		        
		        driver.registerPool("example",_pool);*/
		        
		        registerPool(name, _pool);
		        pool = (GenericObjectPool<PoolableConnection>) poolableConnectionFactory.getPool();
		        
		        
		        Monitor monitor = null;
	            try
	            {
	            	if(true)
	            	{
	            		monitor = new Monitor(name, pool); 
	            		monitor.start();
	            	}
	            } catch(Exception ex)
	            {
	            	Logger.warn.println("monitor failed", ex);
	            }
		        
		        
			} catch(Exception ex)
			{
				
			}    		
    	}    	
    	
    	return pool;
	}
	
	public synchronized void registerPool(String name, GenericObjectPool<PoolableConnection> pool) {
        _pools.put(name,pool);
    }
	
	
	public synchronized void closePool(String name) throws SQLException {
		GenericObjectPool<PoolableConnection> pool =  _pools.get(name);
        if (pool != null) 
        {
            _pools.remove(name);
            
            pool.close();
        }
    }
    
    public synchronized String[] getPoolNames() throws SQLException{
        Set<String> names = _pools.keySet();
        return names.toArray(new String[names.size()]);
    }
	

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		
		if(acceptsURL(url)) 
		{
			GenericObjectPool<PoolableConnection> pool = getConnectionPool(url.substring(URL_PREFIX_LEN));
            if(null == pool) 
            {
                throw new SQLException("No pool found for " + url + ".");
            } else 
            {            	
                try 
                {
                    Connection conn = (Connection)(pool.borrowObject());                    
                    if (conn != null) 
                    {
                        conn = new PoolGuardConnectionWrapper(pool, conn, url.substring(URL_PREFIX_LEN));                        
                    }                    
                    Logger.info.println("<DBCPPoolDriver :"+url.substring(URL_PREFIX_LEN)+"> API connect.");                    
                    return conn;
                } catch(SQLException e) {
                    throw e;
                } catch(RuntimeException e) {
                    throw e;
                } catch(Exception ex)
                {
                	//throw ex;
                }
            }
        } else 
        {
            return null;
        }
		return null;
		
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException 
	{
		try 
		{
            return url.startsWith(URL_PREFIX);
        } catch(NullPointerException e) {
            return false;
        }
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException 
	{
		return new DriverPropertyInfo[0];
	}

	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean jdbcCompliant() 
	{
		return true;
	}

	@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException 
	{
		return this.getParentLogger();
	}
	
	
	
	private class PoolGuardConnectionWrapper extends DelegatingConnection {

	    private GenericObjectPool<PoolableConnection> pool;
	    private Connection delegate;
	    private String url;

	    PoolGuardConnectionWrapper(GenericObjectPool<PoolableConnection> pool, Connection delegate, String url) {
	        super(delegate);
	        this.pool = pool;
	        this.delegate = delegate;
	        this.url = url;
	    }

	    protected void checkOpen() throws SQLException {
	        if(delegate == null) {
	            throw new SQLException("Connection is closed.");
	        }
	    }

	    public void close() throws SQLException {
	    	
	    	
	        //logger.debug(this.url);
	    	
	        checkOpen();
	        this.delegate.close();
	        this.delegate = null;
	        super.setDelegate(null);
	        
	        
	        
	        Logger.info.println("<DBCPPollDriver :"+this.url+"> API resource collection.");
	    }

	    public boolean isClosed() throws SQLException {
	        if (delegate == null) {
	            return true;
	        }
	        return delegate.isClosed();
	    }

	    public void clearWarnings() throws SQLException {
	        checkOpen();
	        delegate.clearWarnings();
	    }

	    public void commit() throws SQLException {
	        checkOpen();
	        delegate.commit();
	    }

	    public Statement createStatement() throws SQLException {
	        checkOpen();
	        return delegate.createStatement();
	    }

	    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
	        checkOpen();
	        return delegate.createStatement(resultSetType, resultSetConcurrency);
	    }

	    public boolean equals(Object obj) {
	        if (delegate == null){
	            return false;
	        }
	        return delegate.equals(obj);
	    }

	    public boolean getAutoCommit() throws SQLException {
	        checkOpen();
	        return delegate.getAutoCommit();
	    }

	    public String getCatalog() throws SQLException {
	        checkOpen();
	        return delegate.getCatalog();
	    }

	    public DatabaseMetaData getMetaData() throws SQLException {
	        checkOpen();
	        return delegate.getMetaData();
	    }

	    public int getTransactionIsolation() throws SQLException {
	        checkOpen();
	        return delegate.getTransactionIsolation();
	    }

	    public Map getTypeMap() throws SQLException {
	        checkOpen();
	        return delegate.getTypeMap();
	    }

	    public SQLWarning getWarnings() throws SQLException {
	        checkOpen();
	        return delegate.getWarnings();
	    }

	    public int hashCode() {
	        if (delegate == null){
	            return 0;
	        }
	        return delegate.hashCode();
	    }

	    public boolean isReadOnly() throws SQLException {
	        checkOpen();
	        return delegate.isReadOnly();
	    }

	    public String nativeSQL(String sql) throws SQLException {
	        checkOpen();
	        return delegate.nativeSQL(sql);
	    }

	    public CallableStatement prepareCall(String sql) throws SQLException {
	        checkOpen();
	        return delegate.prepareCall(sql);
	    }

	    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
	        checkOpen();
	        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
	    }

	    public PreparedStatement prepareStatement(String sql) throws SQLException {
	        checkOpen();
	        return delegate.prepareStatement(sql);
	    }

	    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
	        checkOpen();
	        return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
	    }

	    public void rollback() throws SQLException {
	        checkOpen();
	        delegate.rollback();
	    }

	    public void setAutoCommit(boolean autoCommit) throws SQLException {
	        checkOpen();
	        delegate.setAutoCommit(autoCommit);
	    }

	    public void setCatalog(String catalog) throws SQLException {
	        checkOpen();
	        delegate.setCatalog(catalog);
	    }

	    public void setReadOnly(boolean readOnly) throws SQLException {
	        checkOpen();
	        delegate.setReadOnly(readOnly);
	    }

	    public void setTransactionIsolation(int level) throws SQLException {
	    	
	    	//level = 1;
	    	
	    	Logger.info.println(CLASS_NAME+"setTransactionIsolation:"+level);
	    	
	        checkOpen();
	        delegate.setTransactionIsolation(level);
	    }

	    public void setTypeMap(Map map) throws SQLException {
	        checkOpen();
	        delegate.setTypeMap(map);
	    }

	    public String toString() {
	        if (delegate == null){
	            return null;
	        }
	        return delegate.toString();
	    }

	    // ------------------- JDBC 3.0 -----------------------------------------
	    // Will be commented by the build process on a JDBC 2.0 system

	/* JDBC_3_ANT_KEY_BEGIN */

	    public int getHoldability() throws SQLException {
	        checkOpen();
	        return delegate.getHoldability();
	    }

	    public void setHoldability(int holdability) throws SQLException {
	        checkOpen();
	        delegate.setHoldability(holdability);
	    }

	    public java.sql.Savepoint setSavepoint() throws SQLException {
	        checkOpen();
	        return delegate.setSavepoint();
	    }

	    public java.sql.Savepoint setSavepoint(String name) throws SQLException {
	        checkOpen();
	        return delegate.setSavepoint(name);
	    }

	    public void releaseSavepoint(java.sql.Savepoint savepoint) throws SQLException {
	        checkOpen();
	        delegate.releaseSavepoint(savepoint);
	    }

	    public void rollback(java.sql.Savepoint savepoint) throws SQLException {
	        checkOpen();
	        delegate.rollback(savepoint);
	    }

	    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
	        checkOpen();
	        return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	    }

	    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
	        checkOpen();
	        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	    }

	    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
	        checkOpen();
	        return delegate.prepareStatement(sql, autoGeneratedKeys);
	    }

	    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
	        checkOpen();
	        return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	    }

	    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
	        checkOpen();
	        return delegate.prepareStatement(sql, columnIndexes);
	    }

	    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
	        checkOpen();
	        return delegate.prepareStatement(sql, columnNames);
	    }

	/* JDBC_3_ANT_KEY_END */

	    /**
	     * @see org.apache.commons.dbcp.DelegatingConnection#getDelegate()
	     */
	    public Connection getDelegate() {
	        if (isAccessToUnderlyingConnectionAllowed()) {
	            return super.getDelegate();
	        } else {
	            return null;
	        }
	    }

	    /**
	     * @see org.apache.commons.dbcp.DelegatingConnection#getInnermostDelegate()
	     */
	    public Connection getInnermostDelegate() {
	        if (isAccessToUnderlyingConnectionAllowed()) {
	            return super.getInnermostDelegate();
	        } else {
	            return null;
	        }
	    }
	}
	
	
}




