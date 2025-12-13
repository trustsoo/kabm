//Source file: D:\\PROJECT\\anylogic\\prototype\\src\\anylogic\\spi\\management\\business\\ProcessInfoFactory.java

package jdf.framework.logic.spi.management;

import jdf.framework.core.data.schema.Processor;

/**
 * 
 * ProcessorFactory
 * 
 * BLD에 정의되어진 processor-info 의 구현 class 를 위한 기본 interface
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-11-20 오후 9:29:58
 *
 */
public interface ProcessorFactory
{

    /**
     * jdf.framework.core.data.schema.Processor 객체를 얻는 
     * 
     * 
     * @see jdf.framework.core.data.schema.Processor
     */
    public Processor getProcessor(RADeployDescriptor radd, IOSchemaContext ctx) throws Exception;
}