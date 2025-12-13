
package jdf.framework.logic.adapter.dbms;

import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.logic.spi.ResourceAdapter;

/**
 * 
 * DBMS Resource Adapter
 * 
 * @author
 *
 */
public class ResourceAdapterImpl implements ResourceAdapter 
{
    
    public ResourceAdapterImpl() 
    {
    }
    
    /**
     * 
     * 
     */
    public void start() 
    {
        
    }
    

    /**
     * 
     * 
     */
    public void stop() 
    {
    }

    /**
     * 
     */
    public ConnectionFactory getConnectionFactory()
    {
        return new ConnectionFactoryImpl();
    }
    
    
    /*
    public void setIp(String ip)
    {
    }
    */

}