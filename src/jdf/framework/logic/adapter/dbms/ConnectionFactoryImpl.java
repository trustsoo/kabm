package jdf.framework.logic.adapter.dbms;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.core.data.cci.ConnectionSpec;
import jdf.framework.core.db.ConnectionManager;

import java.sql.SQLException;


/**
 * core.data.cci.ConnectionFactory 구현체
 * 
 * @author
 *
 */
public class ConnectionFactoryImpl implements ConnectionFactory
{

    
    public ConnectionFactoryImpl()
    {}



    /**
     *
     * 
     * @see ConnectionFactory#getConnection()
     */
    public Connection getConnection() throws ResourceException
    {
        return getConnection(null);
    }

    /**
     * @see ConnectionFactory#getConnection(ConnectionSpec)
     */
    public Connection getConnection(ConnectionSpec spec) throws ResourceException
    {
        String datasourceName= spec.getProperty("datasource");

        java.sql.Connection conn= null;

        try
        {           
            if (spec == null || datasourceName == null || datasourceName.length() == 0)
                conn= ConnectionManager.getConnection();
            else
                conn= ConnectionManager.getConnection(datasourceName);

        }
        catch(SQLException sqle)
        {
            throw new ResourceException(sqle);
        }
        
        return new ConnectionImpl(conn);

    }

}
