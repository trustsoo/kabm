package jdf.framework.logic.adapter.file;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.logic.adapter.AbstractInteraction;

/**
 * 
 * 
 * @author
 *
 */
public class InteractionImpl extends AbstractInteraction implements Interaction
{
    private ConnectionImpl conn;
    
    InteractionImpl(ConnectionImpl conn)
    {
        this.conn=conn;
    }

    /**
     * 
     */
    protected void execute(String trcode, DataSet input, DataSet output, IOSchema schema) throws ResourceException
    {
        FileParseProcessor prss = (FileParseProcessor) schema.createProcessor();
        
        // Adapter Properties 정보 세팅
        prss.setAdapterProperties(this.conn.getAdapterProperties());
        

        try {
            prss.execute(input, output);
        }catch(ResourceException re) {
            throw re;
        }catch(Exception e)
        {
            e.printStackTrace();
            throw new ResourceException(e);
        }

    }

    public Connection getConnection()
    {
        return this.conn;
    }

    public void close() throws ResourceException
    {
        // TODO Auto-generated method stub

    }

}