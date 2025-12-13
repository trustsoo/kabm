package jdf.framework.logic.adapter.http;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.core.data.cci.ConnectionSpec;



public class ConnectionFactoryImpl implements ConnectionFactory
{
	public ConnectionFactoryImpl()
    {}



    /**
     *
     * 
     * @see jdf.framework.core.data.cci.ConnectionFactory#getConnection()
     */
    public Connection getConnection() throws ResourceException
    {
        return getConnection(null);
    }

    /**
     * @see jdf.framework.core.data.cci.ConnectionFactory#getConnection(jdf.framework.core.data.cci.ConnectionSpec)
     */
    public Connection getConnection(ConnectionSpec spec) throws ResourceException
    {        
        
        return new ConnectionImpl();

    }
}