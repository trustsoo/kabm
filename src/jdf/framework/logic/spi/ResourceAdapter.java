package jdf.framework.logic.spi;

import jdf.framework.core.data.cci.ConnectionFactory;


/**
 실질적인 각 EIS와  연결되는 Resource Adapter
 
 @author
 @version 1.0
 */
public interface ResourceAdapter 
{
    
    /**
     @roseuid 3E92747F0242
     */
    public void start();
    
    /**
     @roseuid 3E92748702CE
     */
    public void stop();
    
    
    
    public ConnectionFactory getConnectionFactory();
    
}