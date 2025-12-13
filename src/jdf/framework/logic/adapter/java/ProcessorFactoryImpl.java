package jdf.framework.logic.adapter.java;

import jdf.framework.core.data.schema.Processor;
import jdf.framework.logic.spi.management.AbstractProcessorFactory;
import jdf.framework.logic.spi.management.IOSchemaContext;


/**
 * 
 * @author
 *
 */
public class ProcessorFactoryImpl extends AbstractProcessorFactory
{

    public ProcessorFactoryImpl()
    {
        super();
    }
    
    protected Processor getProcessor(IOSchemaContext ctx, Processor process) throws Exception
    {
        return process;
    }

}