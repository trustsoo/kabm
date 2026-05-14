package jdf.framework.core.naming.server;


import jdf.framework.core.naming.rmi.Naming;
import jdf.framework.core.naming.rmi.NamingContext;
import jdf.framework.core.naming.rmi.NamingParser;

import javax.naming.*;
import javax.naming.spi.ResolveResult;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class NamingServer implements Naming, java.io.Serializable
{
   // Constants -----------------------------------------------------
    
   // Attributes ----------------------------------------------------
   
   /**
	 * 
	 */
	private static final long serialVersionUID = -3973435530942018205L;
	
   Hashtable table = new Hashtable();
   Name prefix;

	/**
	 * 
	 * @uml.property name="parser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	NamingParser parser = new NamingParser();

	/**
	 * 
	 * @uml.property name="parent"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	NamingServer parent;

    
   
	// 이 객체를 구별하기 위한 키값이 들어간다.
	// 생성시간으로 하려고 한다.
   private long id;
   
   // Static --------------------------------------------------------
   
   // Constructors --------------------------------------------------
   public NamingServer()
      throws NamingException
   {
      this(null, null);
      
      
   }
   
   
   
   
   public NamingServer(Name prefix, NamingServer parent)
      throws NamingException
   {
      if (prefix == null) prefix = parser.parse("");
      this.prefix = prefix;
      
      this.parent = parent;
      
      id = System.currentTimeMillis();
   }
   
   
   
   
    public long getIndetity()
    {
        //System.out.println(id);
        return id;
    }
    
    
    
   
   // Public --------------------------------------------------------

   // Naming implementation -----------------------------------------
   
    public synchronized void bind(Name name, Object obj, String className)
       throws NamingException
    {
        
    	// 비었을 경우
        if (name.isEmpty())
        {
            // Empty names are not allowed
            throw new InvalidNameException();
        }
        
        
     // 자식을 가지고 있는 경우
        else if (name.size() > 1) 
        {
            // Recurse to find correct context
            //System.out.println("bind#"+name+"#");
           
            Object ctx = null;
            
            try
            {
               ctx = getObject(name);
            
            } catch(NameNotFoundException nnfe)
            {
                
                
                /*
                System.out.println("-bind#"+name+"#");
                System.out.println("-bind#"+name.getPrefix(0)+"#");
                System.out.println("-bind#"+name.getPrefix(1)+"#");
                
                System.out.println("- bind#"+name.getSuffix(0)+"#");
                System.out.println("- bind#"+name.getSuffix(1)+"#");
                */
                Context sctx = createSubcontext(name.getPrefix(1));
                   sctx.bind(name.getSuffix(1),obj);
                return;
            }
          
          
           
          
          
         if (ctx != null)
         {
            if (ctx instanceof NamingServer)
            {
               ((NamingServer)ctx).bind(name.getSuffix(1),obj, className);
            } else
            {
               throw new NotContextException();
            }
         } else
         {
            
            //createSubcontext(name.getSuffix(0)).bind(name.getSuffix(1),obj);
            
            throw new NameNotFoundException();
         }
      } else
      {
         // Bind object
         if (name.get(0).equals(""))
         {
            throw new InvalidNameException();
         } else
         {
//            System.out.println("bind "+name+"="+obj);
            try
            {
               getBinding(name);
               // Already bound
               throw new NameAlreadyBoundException();
            } catch (NameNotFoundException e)
            {
               setBinding(name,obj,className);
            }
         }
      }
   }

   public synchronized void rebind(Name name, Object obj, String className)
      throws NamingException
   {
      if (name.isEmpty())
      {
         // Empty names are not allowed
         throw new InvalidNameException();
      } else if (name.size() > 1) 
      {
         // Recurse to find correct context
         //System.out.println("rebind#"+name+"#");
         
         Object ctx = getObject(name);
         if (ctx instanceof NamingServer)
         {
            ((NamingServer)ctx).rebind(name.getSuffix(1),obj, className);
         } else
         {
            throw new NotContextException();
         }
      } else
      {
         // Bind object
         if (name.get(0).equals(""))
         {
            throw new InvalidNameException();
         } else
         {
//            System.out.println("rebind "+name+"="+obj);
            setBinding(name,obj,className);
         }
      }
   }
   
   public synchronized void unbind(Name name)
      throws NamingException
   {
      if (name.isEmpty())
      {
         // Empty names are not allowed
         throw new InvalidNameException();
      } else if (name.size() > 1) 
      {
         // Recurse to find correct context
//         System.out.println("unbind#"+name+"#");
         
         Object ctx = getObject(name);
         if (ctx instanceof NamingServer)
         {
            ((NamingServer)ctx).unbind(name.getSuffix(1));
         } else
         {
            throw new NotContextException();
         }
      } else
      {
         // Bind object
         if (name.get(0).equals(""))
         {
            throw new InvalidNameException();
         } else
         {
//            System.out.println("unbind "+name+"="+getBinding(name));
            if (getBinding(name) != null)
            {
               removeBinding(name);
            } else
            {
               throw new NameNotFoundException();
            }
         }
      }
   }


   public synchronized Object lookup(Name name)
      throws NamingException
   {
      if (name.isEmpty())
      {
         // Return this
         Name fullName = (Name)(prefix.clone());
         fullName.addAll(name);
         return new NamingContext(null, fullName, getRoot());
      } else if (name.size() > 1)
      {
         // Recurse to find correct context
//         System.out.println("lookup#"+name+"#");
         
         Object ctx = getObject(name);
         if (ctx instanceof NamingServer)
         {
            return ((NamingServer)ctx).lookup(name.getSuffix(1));
         } else if (ctx instanceof Reference)
         {
            return new ResolveResult(ctx, name.getSuffix(1));
         } else
         {
            throw new NotContextException();
         }
      } else
      {
         // Get object to return
         if (name.get(0).equals(""))
         {
            return new NamingContext(null, prefix, getRoot());
         } else
         {
//            System.out.println("lookup "+name);
            Object res = getObject(name);
            
            if (res instanceof NamingServer)
            {
               Name fullName = (Name)(prefix.clone());
               fullName.addAll(name);
               return new NamingContext(null, fullName, getRoot());
            }
            else
               return res;
         }
      }
   }
   
   public Collection list(Name name)
      throws NamingException
   {
//      System.out.println("list of #"+name+"#"+name.size());
      if (name.isEmpty())
      {
//         System.out.println("list "+name);
         
         Vector list = new Vector();
         Enumeration keys = table.keys();
         while (keys.hasMoreElements())
         {
            String key = (String)keys.nextElement();
            Binding b = getBinding(key);
            
            list.addElement(new NameClassPair(b.getName(),b.getClassName(),true)); // FIX to real name!
         }
         return list;
      } else
      {
//         System.out.println("list#"+name+"#");
         
         Object ctx = getObject(name);
         if (ctx instanceof NamingServer)
         {
            return ((NamingServer)ctx).list(name.getSuffix(1));
         } else
         {
            throw new NotContextException();
         }
      } 
   }
    
    
    
    public Context createSubcontext(Name name) throws NamingException
    {
        if (name.size() > 1)
        {
//          System.out.println("create#"+name.get(0)+"#");
         
            Object ctx = getObject(name);
            
            if (ctx != null)
            {
                if (ctx instanceof NamingServer)
                    return ((NamingServer)ctx).createSubcontext(name.getSuffix(1));
        
                else
                    throw new NotContextException();
            } 
            
            else
                throw new NameNotFoundException();
        } 
        else
        {
            if (name.get(0).equals(""))
                throw new NameAlreadyBoundException();
            
            else
            {
//              System.out.println("create subcontext "+name);
                Name fullName = (Name)(prefix.clone());
                fullName.addAll(name);
                setBinding(name, new NamingServer(fullName, this), NamingContext.class.getName());
                return new NamingContext(null, fullName, getRoot());
            }
        }
    }
    
    
    
    
    
    
      
   public Naming getRoot()
   {
      if (parent == null)
         return this;
      else
         return parent.getRoot();
   }

   // Y overrides ---------------------------------------------------

   // Package protected ---------------------------------------------
    
   // Protected -----------------------------------------------------
    
   
    
   /**
    * 실질적인 binding이 이루어 지는곳
    *
    *
    */ 
    private void setBinding(Name name, Object obj, String className)
    {
        //System.out.println("setBinding name:"+name);
        //System.out.println("setBinding obj:"+obj.getClass().getName());
        
        
        String n = name.toString();
        table.put(n, new Binding(n, className, obj, true));
    }

   private Binding getBinding(String key)
      throws NameNotFoundException
   {
      Binding b = (Binding)table.get(key);
      if (b == null)
         throw new NameNotFoundException();
      return b;
   }

   private Binding getBinding(Name key)
      throws NameNotFoundException
   {
      return getBinding(key.get(0));
   }
   
    
   /**
    * key에 대한 binding된 객체를 가져온다.
    *
    */
    private Object getObject(Name key) throws NameNotFoundException
    {
        return getBinding(key).getObject();
    }




   private void removeBinding(Name name)
   {
      table.remove(name.get(0));
   }

	/**
	 * 
	 * @uml.property name="parent"
	 */
	private NamingServer getParent() {
		return parent;
	}

   
   // Inner classes -------------------------------------------------
}