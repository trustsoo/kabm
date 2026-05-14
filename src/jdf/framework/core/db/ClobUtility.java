/**
 * @(#) ClobUtility.java
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team, 
 * Application Infrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 * 
 * NOTICE !      You can copy or redistribute this code freely, 
 * but you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */
 
package jdf.framework.core.db;


import jdf.framework.core.Config;
import jdf.framework.core.Configuration;

import java.sql.*;


/**
 * <p>
 * java.sql.Clob 클래스를 핸들링하기 위한 Utility.
 * dbms_lob라고 하는 오라클 stored procedure 패키지를 사용한다. 
 * </p>
 *
 * @author
 */ 
 
/**

 */ 
public final class ClobUtility {

    public static final long WRITE_CHUCK = 2000; //2KByte
    public static final long READ_CHUCK = 20000; //20KByte
    
    
    /**
	 * Don't let anyone instantiate this class
	 */
	private ClobUtility() {}

	public final static int JDF = 0;
	public final static int TOMCAT = 1;
	public final static int JEUS = 2;
	public final static int WEBLOGIC = 3;
	public final static int WEBSPHERE = 4;
	
	public static Clob getClob(ResultSet rset) throws SQLException
	{
		int vendor = 0;
		try
		{
			Config conf = Configuration.lookup("/resource/anylogic/ioSchema");
			vendor = conf.getInt("poolVender", 0);
		} catch(Exception x)
		{
			vendor = 0;
		}
		
		return getClob(vendor, rset);
		
	}
	
	
	public static Clob getClob(int vendor, ResultSet rset) throws SQLException
	{
		Clob clob = null;
		
		
		switch(vendor)
		{
			case JDF : clob = getJDFClob(rset); break;
			case TOMCAT : clob = getTOMCATClob(rset); break;
			case JEUS : clob = getJEUSClob(rset); break;
			case WEBLOGIC : clob = getWeblogicClob(rset); break;
			case WEBSPHERE : clob = getWebsphere(rset); break;			
			default : clob = null; throw new SQLException("지원하지 않는 vendor입니다.");
		}
		
		
		return clob;
	}
	
	
	private static Clob getJDFClob(ResultSet rset) throws SQLException
	{
		return (((org.apache.commons.dbcp2.DelegatingResultSet)rset).getInnermostDelegate()).getClob(1);
	}
	
	private static Clob getTOMCATClob(ResultSet rset) throws SQLException
	{
		return (((org.apache.tomcat.dbcp.dbcp2.DelegatingResultSet)rset).getInnermostDelegate()).getClob(1);
	}
	
	
	private static Clob getJEUSClob(ResultSet rset) throws SQLException
	{
		return rset.getClob(1);
	}
	
	
	private static Clob getWeblogicClob(ResultSet rset) throws SQLException
	{
		return rset.getClob(1);
	}
	
	private static Clob getWebsphere(ResultSet rset) throws SQLException
	{
		return rset.getClob(1);
	}
	
	
	

	/**
     * Clob 타입의 컬럼에 데이터를 입력할때 사용하는 메쏘드
     * 단 Oracel 전용이다.
     * 
     * 
	 * @param java.sql.Clob clob
	 * @param java.lang.String content 저장할 문자열
	 * @param java.sql.Connection conn
     * @exception SQLException
	 */
	public static void write(Clob clob, String content, Connection conn)
		throws SQLException
	{
		long i = 0;
		long chunk = WRITE_CHUCK;
		long length = content.length();
		
		CallableStatement cstmt = null;
		
		try {
			cstmt = conn.prepareCall ("begin dbms_lob.write (?, ?, ?, ?); end;");

    		while (i < length)
   			{
      			//Logger.debug.println( " ____________ CLOB ="+ i);
      			
      			if (length - i < chunk)	chunk = length - i;
      			
    			cstmt.setClob (1, clob);
      			cstmt.setLong (2, chunk);
	      		cstmt.setLong (3, i + 1);
    	  		cstmt.setString(4, content.substring((int)i,(int)(i+chunk)));
      			cstmt.execute ();
				
      			i += chunk;
	    	}
	    	
	    	//Logger.debug.println("writeCLOB " );
	    
		}
		finally
		{
			if(cstmt!= null) cstmt.close();
		}
	}
	
		
	/**
     * Clob 타입의 컬럼 데이터를 읽어올때 쓰는 메쏘드
     * 
	 * @param java.sql.Clob clob
	 * @param java.sql.Connection conn
	 * @return java.lang.String content 읽어온 문자열
     * @exception SQLException
	 */
	public static String read(Clob clob, Connection conn)
		throws SQLException
	{
		
		long i = 0;
		long chunk = READ_CHUCK; 
		long length = 0;
		
		StringBuffer result = new StringBuffer();
		CallableStatement cstmt1 = null;
		CallableStatement cstmt2 = null;
		
		
		try {
			
			cstmt1 = conn.prepareCall ("begin ? := dbms_lob.getLength (?); end;");
			cstmt2 = conn.prepareCall ("begin dbms_lob.read (?, ?, ?, ?); end;");

    		cstmt1.registerOutParameter(1, Types.NUMERIC);
    		cstmt1.setClob (2, clob);
    		cstmt1.execute ();

    		length = cstmt1.getLong (1);

    		while (i < length)
    		{
      			cstmt2.setClob (1, clob);
		      	cstmt2.setLong (2, chunk);
      			cstmt2.registerOutParameter (2, Types.NUMERIC);
      			cstmt2.setLong (3, i + 1);
      			cstmt2.registerOutParameter (4, Types.VARCHAR);
      			cstmt2.execute ();

      			long read_this_time = cstmt2.getLong(2);
      			String string_this_time = cstmt2.getString(4);
      			
      			i += read_this_time;
      			result.append(string_this_time);
    		}
	    	
	    	//Logger.debug.println("readCLOB " );

    		return result.toString();
		}
		finally
		{
			if(cstmt1!= null) cstmt1.close();
			if(cstmt2!= null) cstmt2.close();
		}
	}
	
}