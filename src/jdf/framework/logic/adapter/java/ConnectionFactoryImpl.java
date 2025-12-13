package jdf.framework.logic.adapter.java;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.core.data.cci.ConnectionSpec;


/**
 * 
 * @author
 *
 */
public class ConnectionFactoryImpl implements ConnectionFactory
{

    public Connection getConnection() throws ResourceException
    {
        return new ConnectionImpl();
    }

    public Connection getConnection(ConnectionSpec sepc) throws ResourceException
    {
        
        return getConnection();
    }

}
