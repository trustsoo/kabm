/*
 * @(#)EntitySQLQueryBuilder.java
 *
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
 
package jdf.framework.core.io.compress;

/**
 *
 * @author 
 * @version 1.0 2001/05/01
 *
 * @see jdf.framework.core.manageobject.dbms.SQLQueryImpl
 *
 **/
 
 
 
public class CompressException extends RuntimeException {

	private byte[] inputBytes;
	private String className;
	
	/**
	 * 
	 */
	public CompressException() {
		super();
	}


	/**
	 * @param s java.lang.String
	 */
	public CompressException(String s) {
		super(s);
	}
	
	
	public void setParsingBytes(byte[] inputBytes)
	{
	    this.inputBytes = inputBytes;
	}
	
	public void setClassName(String className)
	{
	    this.className = className;
	}
	
	
	
	public byte[] getParsingBytes()
	{
	    return inputBytes;
	}
	
	public String getClassName()
	{
	    return className;
	}
	
	
	
}