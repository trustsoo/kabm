package jdf.framework.core.data.cci;


import jdf.framework.core.data.*;


/**
 * <b><code>LocalTransaction</code></b>
 * <p>
 * Legacy 시스템의 Connection 
 * </p>
 *
 * @author
 * @version 1.0
 */
public interface LocalTransaction
{
	
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
     * trasaction을 완료한다.
     * 
     * @throws ResourceException
     */
    public void commitTransaction() throws ResourceException;
	
	
}