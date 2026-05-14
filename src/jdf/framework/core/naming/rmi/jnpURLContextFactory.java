package jdf.framework.core.naming.rmi;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

/**
 *   <description> 
 *      
 *   @see <related>
 *   @author $Author:$
 *   @version $Revision:$
 */
public class jnpURLContextFactory
   implements ObjectFactory
{
   // Constants -----------------------------------------------------
    
   // Attributes ----------------------------------------------------
    
   // Static --------------------------------------------------------
   
   // Constructors --------------------------------------------------
   
   // Public --------------------------------------------------------

   // ObjectFactory implementation ----------------------------------
   public Object getObjectInstance(Object obj,
                                Name name,
                                Context nameCtx,
                                Hashtable environment)
                         throws Exception
   {
      if (obj == null)
         return new NamingContext(environment, name, null);
      else if (obj instanceof String)
      {
         String url = (String)obj;
         Context ctx = new NamingContext(environment, name, null);
         
         Name n = ctx.getNameParser(name).parse(url.substring(url.indexOf(":")+1));
         if (n.size() >= 3)
         {
            // Provider URL?
            if (n.get(0).toString().equals("") &&
                n.get(1).toString().equals(""))
            {
               ctx.addToEnvironment(Context.PROVIDER_URL, n.get(2));
            }
         }
         return ctx;
      } else
      {
         return null;
      }
   }
    
   // Y overrides ---------------------------------------------------

   // Package protected ---------------------------------------------
    
   // Protected -----------------------------------------------------
    
   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}