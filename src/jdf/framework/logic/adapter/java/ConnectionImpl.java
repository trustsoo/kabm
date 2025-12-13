package jdf.framework.logic.adapter.java;

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
    ConnectionImpl()
    {
        super();
    }

    public void beginTransaction() throws ResourceException
    {

    }

    public void rollbackTransaction() throws ResourceException
    {
    }

    public void close() throws ResourceException
    {

    }

    public void commitTransaction() throws ResourceException
    {
    }

    public Interaction createInteraction() throws ResourceException
    {
        return new InteractionImpl(this);
    }

}