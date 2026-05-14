package jdf.framework.core.naming.rmi;


import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import java.util.Properties;

/**
 *   <description> 
 *      
 *   @see <related>
 *   @author $Author:$
 *   @version $Revision:$
 */
public class NamingParser
   implements NameParser, java.io.Serializable
{
   // Constants -----------------------------------------------------
    
   // Attributes ----------------------------------------------------
    
   /**
	 * 
	 */
	private static final long serialVersionUID = -6126919088882273776L;
// Static --------------------------------------------------------
   static Properties syntax = new Properties();
   static 
   {
       syntax.put("jndi.syntax.direction", "left_to_right");
       syntax.put("jndi.syntax.ignorecase", "false");
       syntax.put("jndi.syntax.separator", "/");
   }
   
   // Constructors --------------------------------------------------
   
   // Public --------------------------------------------------------

   // NameParser implementation -------------------------------------
   public Name parse(String name) 
   	throws NamingException 
   {
   	return new CompoundName(name, syntax);
   }

   // Y overrides ---------------------------------------------------

   // Package protected ---------------------------------------------
    
   // Protected -----------------------------------------------------
    
   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}