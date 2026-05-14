package jdf.framework.core.naming;


import javax.naming.Context;
import java.util.Hashtable;


public class InitialContextFactory implements javax.naming.spi.InitialContextFactory
{
    
    
    public Context getInitialContext(Hashtable env)
    {
        return new CompNamingContext(env);
    }
    
}


