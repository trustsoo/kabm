package jdf.framework.logic.adapter.file;

import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.logic.spi.ResourceAdapter;


/**
 * 
 * File Access Resource Adapter
 * 
 * @author
 *
 */
public class ResourceAdapterImpl extends AdapterProperties implements ResourceAdapter
{
    
    public ResourceAdapterImpl()
    {
        
    }

    public ConnectionFactory getConnectionFactory()
    {
        return new ConnectionFactoryImpl(this);
    }

    
    public void start()
    {
        // TODO Auto-generated method stub

    }

    public void stop()
    {
        // TODO Auto-generated method stub

    }


}