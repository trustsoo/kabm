package jdf.framework.logic.adapter.file;

import jdf.framework.core.data.schema.Processor;
import jdf.framework.core.log.Logger;
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

        Logger.info.println("anylogic.adapter.file.ProcessorFactoryImpl instance create");
    }
    
    
    /**
     * script 을 구현한 BLD 는 process 객체가 생성되서 넘어오고,
     * 그렇지 않은 BLD는 process 가 null 이기 때문에 객체를 생성해 주어야 한다.
     * 
     */
    protected Processor getProcessor(IOSchemaContext ctx, Processor process) throws Exception
    {
        if(process==null)
            return new FileParseProcessor();
        
        return process;
    }

}
