package jdf.framework.core.naming;


import java.util.Hashtable;

import javax.naming.Context;



public class InitialContextFactory implements javax.naming.spi.InitialContextFactory
{
    
    
    public Context getInitialContext(Hashtable env)
    {
        return new CompNamingContext(env);
    }
    
}


