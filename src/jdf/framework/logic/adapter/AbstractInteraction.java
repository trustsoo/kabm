package jdf.framework.logic.adapter;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.logic.spi.management.BLContextFactory;

/**
 * jdf.framework.core.data.cci.Interaction 구현한 추상 class
 * 
 * @author
 * 
 */
public abstract class AbstractInteraction implements Interaction
{

    private BLContextFactory bl_factory = BLContextFactory.getInstance();

    /**
     * trcode 이름을 가지는 BLD를 수행한다.
     * 
     */
    public DataSet execute(String trcode, DataSet input) throws ResourceException
    {

        DataSet output = new DataSet();
        execute(trcode, input, output);

        return output;
    }

    /**
     * BLContextFactory를 반환한다.
     * 
     * @return
     */
    protected BLContextFactory getBLContextFactory()
    {
        return this.bl_factory;
    }

    /**
     * trcode 이름을 가지는 BLD를 수행한다. 이 메쏘드는 구현해주어야 한다.
     * 
     * @param trcode
     * @param input
     * @param output
     * @param schema
     * @throws ResourceException
     */
    abstract protected void execute(String trcode, DataSet input, DataSet output, IOSchema schema)
            throws ResourceException;

    /**
     * jdf.framework.core.data.cci.Connection 구현객체를 반환한다. - 구현요망
     */
    abstract public Connection getConnection();

    /**
     * trcode 이름을 가지는 BLD를 수행한다.
     */
    public void execute(String trcode, DataSet input, DataSet output) throws ResourceException
    {
        IOSchema schema = this.bl_factory.getIOSchema(trcode);

        output.setIOSchema(schema, IOSchema.OUT);
        input.setIOSchema(schema, IOSchema.IN);

        execute(trcode, input, output, schema);
    }

}