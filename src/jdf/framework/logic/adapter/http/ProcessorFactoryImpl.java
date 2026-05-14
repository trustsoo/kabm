package jdf.framework.logic.adapter.http;

import jdf.framework.core.data.schema.Processor;
import jdf.framework.core.log.Logger;
import jdf.framework.logic.spi.management.AbstractProcessorFactory;
import jdf.framework.logic.spi.management.IOSchemaContext;



public class ProcessorFactoryImpl extends AbstractProcessorFactory
{

	public ProcessorFactoryImpl()
    {
        super();

        Logger.info.println("ajdf.framework.logic.adapter.http.ProcessorFactoryImpl instance create");
    }
	
	
	@Override
	protected Processor getProcessor(IOSchemaContext ctx, Processor process)
			throws Exception {
		if(process==null)
            return new HttpProcessor();
        
        return process;
	}
}