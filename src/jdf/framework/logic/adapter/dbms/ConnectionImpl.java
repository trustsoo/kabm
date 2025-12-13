package jdf.framework.logic.adapter.dbms;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Interaction;

/**
 * 
 * core.data.cci.Connection 구현객체
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-12-08 오전 4:32:54
 *  
 */
public class ConnectionImpl extends jdf.framework.logic.adapter.AbstractConnection
{
    //private static int seq = 0;
    //private int idx = 0;

    private java.sql.Connection sql_connection;

    ConnectionImpl(java.sql.Connection conn)
    {
        this.sql_connection = conn;

        //seq++;
        //idx=seq;
        //Logger.info.println("<DBMS Conn> create "+idx);
    }

    /**
     * @see jdf.framework.core.data.cci.Connection#close()
     */
    public void close() throws ResourceException
    {
        //Logger.info.println("<DBMS Conn> close"+idx);

        try
        {
            this.sql_connection.setAutoCommit(true);
        } catch (Exception e)
        {
        }

        try
        {
            this.sql_connection.close();

            //Logger.info.println("<DBMS Conn> close ok " +idx);
        } catch (java.sql.SQLException se)
        {
            throw new ResourceException(se);
        } finally
        {
            this.sql_connection = null;
        }

    }

    /**
     * jdf.framework.core.data.cci.Interaction 을 생성한다.
     * 
     * 
     * @see jdf.framework.core.data.cci.Connection#createInteraction()
     */
    public Interaction createInteraction() throws ResourceException
    {
        return new DbmsInteraction(this);
    }

    /**
     * 트랜잭션 시작
     * 
     * @see jdf.framework.core.data.cci.Connection#beginTransaction()
     */
    public void beginTransaction() throws ResourceException
    {
        try
        {
            sql_connection.setAutoCommit(false);
        } catch (java.sql.SQLException sqe)
        {
            //close();
            throw new ResourceException(sqe);
        }

    }

    /**
     * 트랜잭션 완료
     * 
     * @see jdf.framework.core.data.cci.Connection#commitTransaction()
     */
    public void commitTransaction() throws ResourceException
    {
        try
        {
            sql_connection.commit();
        } catch (java.sql.SQLException sqe)
        {
            //close();
            throw new ResourceException(sqe);
        }

    }

    /**
     * 트랜잭션 실패로 원상복귀
     * 
     * @see jdf.framework.core.data.cci.Connection#rollbackTransaction()
     */
    public void rollbackTransaction() throws ResourceException
    {
        try
        {
            sql_connection.rollback();
        } catch (java.sql.SQLException sqe)
        {
            //close();
            throw new ResourceException(sqe);
        }

    }

    java.sql.Connection getSqlConnection()
    {
        return this.sql_connection;
    }

}