package jdf.framework.logic.adapter.file;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.logic.adapter.AbstractConnection;

/**
 * 
 * @author
 *
 */
public class ConnectionImpl extends AbstractConnection implements Connection
{
    private AdapterProperties properties;
    
    ConnectionImpl(AdapterProperties properties)
    {
        this.properties=properties;
    }

    AdapterProperties getAdapterProperties()
    {
        return this.properties;
    }
    
    /**
     * Interaction 생성
     * 
     */
    public Interaction createInteraction() throws ResourceException
    {
        return new InteractionImpl(this);
    }
    
    public void beginTransaction() throws ResourceException
    {
        // TODO Auto-generated method stub

    }

    public void rollbackTransaction() throws ResourceException
    {
        // TODO Auto-generated method stub

    }

    public void close() throws ResourceException
    {
        // TODO Auto-generated method stub

    }

    public void commitTransaction() throws ResourceException
    {
        // TODO Auto-generated method stub

    }



}