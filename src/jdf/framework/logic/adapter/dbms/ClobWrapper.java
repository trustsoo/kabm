package jdf.framework.logic.adapter.dbms;


/**
 * 사용하지 않음
 * 
 * @author
 *
 */
public interface ClobWrapper
{
    
    public java.sql.Clob getClob(java.sql.Connection conn, String data) throws java.sql.SQLException; 
    
    
}