package jdf.framework.logic.adapter.file;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.core.data.cci.ConnectionSpec;

public class ConnectionFactoryImpl implements ConnectionFactory
{
    private AdapterProperties properties;
    
    ConnectionFactoryImpl(AdapterProperties properties)
    {
        this.properties=properties;
    }

    /**
     * 
     */
    public Connection getConnection() throws ResourceException
    {
        return new ConnectionImpl(properties);
    }

    public Connection getConnection(ConnectionSpec sepc) throws ResourceException
    {
        return new ConnectionImpl(properties);
    }

}