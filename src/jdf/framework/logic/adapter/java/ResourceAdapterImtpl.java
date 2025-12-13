package jdf.framework.logic.adapter.java;

import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.logic.spi.ResourceAdapter;

/**
 * 
 * 
 * @author
 *
 */
public class ResourceAdapterImtpl implements ResourceAdapter
{

    public void start()
    {
    }

    public void stop()
    {

    }

    public ConnectionFactory getConnectionFactory()
    {
        return new ConnectionFactoryImpl();
    }

}