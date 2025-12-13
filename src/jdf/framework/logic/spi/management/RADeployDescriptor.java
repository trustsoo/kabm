package jdf.framework.logic.spi.management;



/**
 Resource Adapter를 deploy하기 위한 Descriptor 정보를 의미한다.
 
 
 @author
 @version 1.0
 */
public class RADeployDescriptor
{
    // RA deploy Descriptor 명
    private String name;

    // config property 리스트
    private RAConfigProperty[] properties;

    private ProcessorFactory psinfoFactory;


    
    // Resource Adapter class 명
    private String resourceAdapterClassName;


    // processor class 명
    private String ProcessorClassName;

    public RADeployDescriptor()
    {}
    
    
    
    /**
     * Resource Adapter 대표명
     * 
     * 
     * @param name
     */
    public void setName(String name)
    {
        this.name= name;
    }
    
    
    /**
     * Resource Adapter Property 설정정보
     * 
     * 
     * @param properties
     */
    public void setConfigPropertyArray(RAConfigProperty[] properties)
    {
        this.properties= properties;
    }

    
    /**
     * BL Descriptor 에서 process-info부분의 정보를 읽고 ProcessInfo객체를 생성하기 위한
     * Factory 객체를 세팅한다.
     * 
     * @param psinfoFactory
     */
    public void setProcessorFactory(ProcessorFactory psinfoFactory)
    {
        this.psinfoFactory= psinfoFactory;
    }


    /**
     * 
     * 
     * @param name
     */
    public void setResourceAdpaterClassName(String name)
    {
        this.resourceAdapterClassName= name;
    }
    
    
    
    public String getResourceAdapterClassName()
    {
        return this.resourceAdapterClassName;
    }

    /**
     * 
     * 
     @roseuid 3E9255D401A5
     */
    public String getName()
    {
        return name;
    }

    /**
     * 
     @roseuid 3E9256790167
     */
    public RAConfigProperty[] getConfigPropertyArray()
    {
        return this.properties;
    }

    /**
     @roseuid 3E9257CD00DA
     */
    public ProcessorFactory getProcessorFactory()
    {
        return this.psinfoFactory;
    }
    /**
     * @return
     */
    public String getProcessorClassName()
    {
        return ProcessorClassName;
    }

    /**
     * @param string
     */
    public void setProcessorClassName(String string)
    {
        ProcessorClassName= string;
    }

}
