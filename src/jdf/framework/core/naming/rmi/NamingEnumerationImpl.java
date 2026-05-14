package jdf.framework.core.naming.rmi;


import javax.naming.NamingEnumeration;
import java.util.Collection;
import java.util.Iterator;

/**
 *   <description> 
 *      
 *   @see <related>
 *   @author $Author:$
 *   @version $Revision:$
 */
public class NamingEnumerationImpl
   implements NamingEnumeration
{
   // Constants -----------------------------------------------------
    
   // Attributes ----------------------------------------------------
   Iterator enumeration;
    
   // Static --------------------------------------------------------
   
   // Constructors --------------------------------------------------
   NamingEnumerationImpl(Collection list)
   {
      enumeration = list.iterator();
   }
   
   // Public --------------------------------------------------------

   // Enumeration implementation ------------------------------------
   public boolean hasMoreElements()
   {
      return enumeration.hasNext();
   }
   
   public Object nextElement()
   {
      return enumeration.next();
   }

   // NamingEnumeration implementation ------------------------------
   public boolean hasMore()
   {
      return enumeration.hasNext();
   }
   
   public Object next()
   {
      return enumeration.next();
   }
   
   public void close()
   {
      enumeration = null;
   }

   // Y overrides ---------------------------------------------------

   // Package protected ---------------------------------------------
    
   // Protected -----------------------------------------------------
    
   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}