package jdf.framework.core.data.cci;


import jdf.framework.core.data.ResourceException;


/**
 * <b><code>Connection</code></b>
 * <p>
 * Legacy 시스템의 Connection 
 * </p>
 *
 * @author
 * @version 1.0
 */
public interface Connection extends LocalTransaction
{
	
	
	/**
     * Interaction 객체를 생성한다.
     * 
     * 
	 * @return
	 * @throws ResourceException
	 */
    public Interaction createInteraction() throws ResourceException;
	
	
	/**
     * Connection을 close한다.
     * 
	 * @throws ResourceException
	 */
	public void close() throws ResourceException;
    
    
    
    public Connection getBaseConnection();
    
    public void setBaseConnection(Connection conn);
    
    
    /**
     * 
     * transaction을 시작한다.
     * 
     * @throws ResourceException
     */
    public void beginTransaction() throws ResourceException;
    
    
    /**
     * transaction을 rollback 시킨다.
     * 
     * @throws ResourceException
     */
    public void rollbackTransaction() throws ResourceException;
	
    
    /**
     * 
     * 
     * @throws ResourceException
     */
    public void commitTransaction() throws ResourceException;
	
	
}