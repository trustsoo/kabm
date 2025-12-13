package jdf.framework.core.data.cci;

import jdf.framework.core.data.ResourceException;



/**
 * <b><code>ConnectionFactory</code></b>
 * <p>
 * Legacy 시스템의 Connection 을 생성하는 factory
 * </p>
 *
 * @author 
 * @version 1.0
 */
public interface ConnectionFactory
{
    /**
     * Connection 구현 객체를 얻는다.
     * 
     * @return
     * @throws ResourceException
     */
	public Connection getConnection() throws ResourceException;
    
    
    /**
     * ConnectionSpec에 따라 Connection 구현객체를 받는다.
     * 
     * @param sepc
     * @return
     * @throws ResourceException
     */
    public Connection getConnection(ConnectionSpec sepc) throws ResourceException;

}
