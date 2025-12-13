package jdf.framework.core.service;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class ServiceFactory implements ObjectFactory 
{
    /**
     * 서비스객체를 반납한다.
     *
     */
    public Object getObjectInstance(Object refObj, Name name, Context nameCtx, Hashtable env) 
        throws Exception 
    {
	    
	    Reference ref = (Reference) refObj;
	    
	    String serviceName = ref.getClassName();
	    
	    ServiceManager mgr = ServiceManager.getInstance();
	    
	    return mgr.getRefService(serviceName);
	    
    }
}
