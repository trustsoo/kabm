package jdf.framework.core.naming.rmi;


import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

/**
 *   <description> 
 *      
 *   @see <related>
 *   @author $Author:$
 *   @version $Revision:$
 */
public class NamingContextFactory
    implements InitialContextFactory, ObjectFactory
{
    
    
    private static Hashtable cache = new Hashtable();
    
    
    
    
    public NamingContextFactory()
    {
        //System.out.println("test =============== **********");
    }
    
    
    public Context getInitialContext(Hashtable env) 
       throws NamingException
    {
        NamingContext ctx = (NamingContext) cache.get(env);
        
        if(ctx==null)
        {
            ctx = new NamingContext(env, null, null);
            cache.put(env, ctx);
        }
        else
        {
            Naming nameServ = ctx.getNaming();
            ctx = new NamingContext(env, null, nameServ);
        }
        
        
        return ctx;
     
        //return new NamingContext(env, null, null);
    }
   
   // ObjectFactory implementation ----------------------------------
   public Object getObjectInstance(Object obj,
                                Name name,
                                Context nameCtx,
                                Hashtable environment)
                         throws Exception
   {
      //System.out.println(obj+" "+name+" "+nameCtx+" "+environment);
      Context ctx = getInitialContext(environment);
      Reference ref = (Reference)obj;
      return ctx.lookup((String)ref.get("URL").getContent());
   }
    
   // Y overrides ---------------------------------------------------

   // Package protected ---------------------------------------------
    
   // Protected -----------------------------------------------------
    
   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}