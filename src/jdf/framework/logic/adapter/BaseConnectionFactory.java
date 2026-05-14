//Source file: D:\\PROJECT\\anylogic\\prototype\\src\\anylogic\\adapter\\BaseConnectionFactory.java

package jdf.framework.logic.adapter;


import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.core.data.cci.ConnectionSpec;
import jdf.framework.logic.spi.management.DeploymentManager;


/**
 * jdf.framework.core.data.cci.ConnectionFactory 기본 구현 class
 * 
 * @author
 *
 */
public class BaseConnectionFactory implements ConnectionFactory 
{
    
    public BaseConnectionFactory() 
    {
        // RA를 로드한다.
        DeploymentManager mgr = DeploymentManager.getInstance();
        
        if(!mgr.isLoad())
            mgr.load();
    }
    
    /**
     * Connection 구현체를 얻는다.
     * 
     */
    public Connection getConnection() throws ResourceException 
    {
        return new LocalConnection();
    }
    
    
    /**
     * Connection 구현객체를 얻는다.
     * 
     * @see ConnectionFactory#getConnection(ConnectionSpec)
     */
    public Connection getConnection(ConnectionSpec sepc) throws ResourceException
    {
        return new LocalConnection(sepc);
        //throw new ResourceException("not support");
    }

}