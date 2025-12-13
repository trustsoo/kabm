package jdf.framework.logic.adapter;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.Interaction;

/**
 * jdf.framework.core.data.cci.Connection 구현한 추상 class
 * 
 * @author
 */
public abstract class AbstractConnection implements Connection
{
    private Connection base_conn;

    /**
     * transaction 시작을 정의한다.
     * 
     * @see jdf.framework.core.data.cci.LocalTransaction#beginTransaction()
     */
    abstract public void beginTransaction() throws ResourceException;

    /**
     * transaction 실패로 rollback 시킨다.
     * 
     * 
     * @see jdf.framework.core.data.cci.LocalTransaction#rollbackTransaction()
     */
    abstract public void rollbackTransaction() throws ResourceException;

    /**
     * transaction을 성공적으로 완료한다.
     */
    abstract public void commitTransaction() throws ResourceException;

    /**
     * Connection을 종료한다.
     * 
     * @see jdf.framework.core.data.cci.Connection#close()
     */
    abstract public void close() throws ResourceException;

    /**
     * Interaction 구현객체를 생성한다.
     * 
     */
    abstract public Interaction createInteraction() throws ResourceException;

    /**
     * base 가 되는 Connection을 반환한다.
     * 
     */
    public Connection getBaseConnection()
    {
        return base_conn;
    }

    /**
     * base 가 Connection 을 설정한다.
     * 
     */
    public void setBaseConnection(Connection base_conn)
    {
        this.base_conn = base_conn;
    }

}