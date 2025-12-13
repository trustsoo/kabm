/*
 * @(#)ConnectionManager.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */

package jdf.framework.core.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.PropertyManager;
import jdf.framework.core.util.SmartStringArray;

/**
 * <b><code>ConnectionManager</code></b>
 * <p>
 * 모든 DB 연결은 이 Class로 처리한다.
 * </p>
 *
 * @author
 * @version 1.0
 */

public final class ConnectionManager
{

    //private static Hashtable drivers = new Hashtable();
    private static Map drivers= new HashMap();

    // getConnection 시 사용되는 기본 connection URL
    private static String defaultUrl= null;

    // JNDI 에서 DataSource 가 Binding 되는 위치
    private static String dsPrefix= "";

    // JNDI 통해서 Connection할것인지 여부
    //private static boolean useJNDI= false;
    
    
    
    static {
        try
        {
            Config conf= Configuration.lookup("/db");

            defaultUrl= conf.getString("dataSourceName", "");

            if (defaultUrl != null && defaultUrl.length() > 0)
            {
                //useJNDI= true;
                dsPrefix= conf.getString("dataSourceDir");

                if (dsPrefix != null && dsPrefix.length() > 0)
                    dsPrefix= dsPrefix + "/";
                else
                    dsPrefix= "";
                    
                Logger.info.println("use default DataSource:"+dsPrefix+defaultUrl);
            }
            else
            {
                defaultUrl= conf.getString("url");
                Logger.info.println("use default url:"+defaultUrl);
            }
            
        }
        catch (Exception e)
        {
        	Logger.err.println("read datasource name.", e);
        }
    }

    private static boolean isEmergencyLegacyDB()
    {
    	String dbMode = System.getProperty("isEmergencyLegacyDB");
    	
    	boolean result = false;
    	
    	if(dbMode != null && "1".equals(dbMode)) result = true;
    	
    	
    	return result;
    }
    
    
    private static boolean isEmergencyLegacyDB(String url)
    {
    	//jdbc:apache:commons:dbcp:BTDPool
    	String[] urlArr = SmartStringArray.split(":", url);
    	
    	String key = urlArr[urlArr.length -1];
    	
    	String dbMode = PropertyManager.getString( "1", key );
    	
    	boolean result = false;
    	
    	if(dbMode != null && "BACKUP".equals(dbMode)) result = true;
    	
    	
    	return result;
    }
	     
    
    /**
     * JDBC 드라이버 객체를 구한다. 이미 캐쉬된 객체는 또다시 instance 하지 않는다.
     *
     *
     **/
    private static Driver getJdbcDriver(String url, String driverName) throws Exception
    {

        Driver driver= null;

        driver= (Driver) drivers.get(url);
        
        //logger.debug("url:"+url);
        
        if (driver == null)
        {
            if (driverName == null) // jdbc driver class명이 전잘되지 않은 경우는 config에서 읽는다.
                driverName= Configuration.lookup("/db").getString("jdbcDriver");
            
            if(driverName.equals("jdf.framework.core.pool.jdbc.DBCP2PoolDriver"))
            { 
            	
            	Config conf = Configuration.lookup("/connectionPool/"+url.substring("jdbc:apache:commons:dbcp:".length()));
            	String jdbcdriver = conf.getString("driver");
            	//logger.debug("jdbcdriver:"+jdbcdriver);
            	try
            	{
            		Class.forName(jdbcdriver);
            	} catch(ClassNotFoundException nex)
            	{
            		Logger.debug.println("ClassNotFoundException:"+nex);
            		throw nex;
            	}
            }
            
            driver= (Driver) Class.forName(driverName).newInstance();
            drivers.put(url, driver);
        }
        return driver;
    }

    /**
     * 우선 driver hashtable을 값이 이용한다.
     * 그냥 poolname은 driver 객체가 들어가고
     * poolname+"_props"는 Propertes 객체가 들어간다
     *
     **/
    private static Properties getConnPorperties(String url) throws Exception
    {
    	Properties props = null;

        if (!drivers.containsKey(url + "_props"))
        {
            try
            {
                Config conf = Configuration.lookup("/db");

                String user = conf.getString("user", "");

                if (user.length() != 0)
                {

                    props = new Properties();
                    drivers.put(url + "_props", props);

                    props.put("user", user);
                    props.put("password", conf.getString("password"));
                }

            } catch (Exception ee)
            {
            	Logger.warn.println(ee);
            }

            return props;

        }
        props = (Properties) drivers.get(url + "_props");

        return props;

    }

    /**
     * Config에 의해 기본설정된 db connection url로 DB연결을 한다.
     *
     * @return Conneciton DB Conneciton
     **/
    public static Connection getConnection() throws SQLException
    {
        return getConnection(defaultUrl, null);
    }

    /**
     * Connection 객체를 얻는다.
     * <p>
     * 예) Connection conn = getConnection("jdbc:weblogic:pool:devPool");
     * <p>
     * 단 다음과 같은 경우는 jdbc driver는 System config 화일에 의해 설정된 driver로 설정된다.
     * 
     * @param url database 의 url , poolname이 될수도 있다.
     * @return Connection
     **/
    public static Connection getConnection(String url) throws SQLException
    {
        return getConnection(url, null);
    }

    /**
     * JDNI 에서 DataSource를 가져온후 Connection 객체를 가져온다,
     *
     *
     */
    private static Connection getConectionFromJNDI(String name) throws SQLException
    {
        try
        {
            InitialContext ctx= new InitialContext();
//System.out.println(dsPrefix + name);
            DataSource ds= (DataSource) ctx.lookup(dsPrefix + name);

            return ds.getConnection();
        }
        catch (NamingException ne)
        {
            throw new SQLException(ne.getMessage());
        }

    }

    private static boolean isJNDIString(String url)
    {
    	if(url.indexOf("java:comp") != -1)
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Connection 객체를 얻는다. 이 메소드는 EJB Session Bean에서 Deployment description 화일에 의해
     * 결정된 환경변수에서 url과 driver를 설정하여 Connection 객체를 얻을 때 사용하기 위함이다.
     *
     * 
     * @param url database 의 url , poolname이 될수도 있다.
     * @param driverName jdbc 드라이버 
     * @return Connection
     **/
    public static Connection getConnection(String url, String driverName) throws SQLException
    {

        //if (useJNDI)
    	if(isJNDIString(url))
        {
        	Logger.debug.println(url+" jndi connection string.");
            return getConectionFromJNDI(url);
        }
        try
        {
        	
        	if(!url.equals(defaultUrl))
        	{
	        	boolean isEmergencyLegacyDB = isEmergencyLegacyDB(url);
	        	
	        	
	        	if(isEmergencyLegacyDB)
	        	{
	        		url = url + "_Emergency";	        	
	        		Logger.info.println("Emergency DB connection!!! : "+url);
	        	}
        	}
        	
            Driver driver= getJdbcDriver(url, driverName);            
            Properties props= getConnPorperties(url);

            return driver.connect(url, props);

        }
        catch (SQLException se)
        {
            //Logger.debug.println("<ConnectionManager> url="+url+" driver="+driverName);
            se.printStackTrace();
            throw se;
        }
        catch (Exception e)
        {
            //Logger.debug.println("<ConnectionManager> url="+url+" driver="+driverName);
            e.printStackTrace();
            throw new SQLException(e.toString());
        }

    }

    /**
     * Connection 연결을 끝는다.
     *
     **/
    public static void close(java.sql.Connection conn)
    {
        try
        {
            if (!conn.isClosed())
                conn.close();

        }
        catch (Exception ex)
        {
        	Logger.warn.println("Close Failed." ,ex);
        }

        conn= null; //gabarge collection에 도움이 될까?
    }
}
