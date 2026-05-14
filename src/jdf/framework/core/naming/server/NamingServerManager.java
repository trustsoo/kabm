package jdf.framework.core.naming.server;


import jdf.framework.core.log.Logger;
import jdf.framework.core.naming.rmi.NamingContext;
import jdf.framework.core.service.AbstractManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;


public class NamingServerManager extends AbstractManager implements Runnable
{
   
	
    public final static String PORT = "service.port";
    
    
	/**
	 * 
	 * @uml.property name="theServer"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	NamingServer theServer = null;

   Remote serverStub;
   ServerSocket serverSocket;
   
   int port = 1099;
   boolean logging = false;
   
   


    private static Hashtable objStore = new Hashtable();
    
    private static Hashtable defaultEnv = null;
    
    private Hashtable env = null;
    
    

    // Constructors --------------------------------------------------
    private NamingServerManager()
    {
        this(null);
    }


    private NamingServerManager(Hashtable env)
    {
        
        if(env==null)
            env = getDefaultEnv();
        
        this.env = env;
        
        objStore.put(env, this);
    }

	/**
	 * 
	 * @uml.property name="instance"
	 */
	public static NamingServerManager getInstance() {
		return getInstance(null);
	}

    
    
    public static NamingServerManager getInstance(Hashtable env)
    {
        if(env == null)
            env = getDefaultEnv();
        
        NamingServerManager mgr = (NamingServerManager) objStore.get(env);
        
        if(mgr==null)
        {
            mgr = new NamingServerManager(env);
            objStore.put(env, mgr);
        }
        
        instance = mgr;
        
        return mgr;
    }

	/**
	 * 
	 * @uml.property name="instance"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static NamingServerManager instance;

    
    public static NamingServerManager getLastInstance()
    {
        if(instance==null)
            throw new RuntimeException("���� �ʱ�ȭ ���� �ʾҽ��ϴ�.");
            
        return instance;
    }

	/**
	 * 
	 * @uml.property name="defaultEnv"
	 */
	private static Hashtable getDefaultEnv() {
		if (defaultEnv == null) {
			defaultEnv = new Hashtable();
			defaultEnv.put(PORT, "1099");

		}

		return defaultEnv;
	}

	/**
	 * 
	 * @uml.property name="port"
	 */
	// Public --------------------------------------------------------
	public void setPort(int p) {
		port = p;
	}

	/**
	 * 
	 * @uml.property name="port"
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * @uml.property name="logging"
	 */
	public void setLogging(boolean l) {
		logging = l;
	}

	/**
	 * 
	 * @uml.property name="logging"
	 */
	public boolean isLogging() {
		return logging;
	}

    
    
    
    public void initialize() throws Exception
    {
        
        try
        {
            String sPort = (String) env.get(PORT);
            
            if(sPort == null || sPort.length() ==0)
                throw new RuntimeException("you must set port number");
            
            port = Integer.parseInt(sPort);
            
        }
        catch(Exception e) {}
        
        
        setPort(port);
        setLogging(false);
        
        
        // Create remote object
        theServer = new NamingServer();
        
        NamingContext.setLocalNaming(theServer);
        
        // Export server
        serverStub = UnicastRemoteObject.exportObject(theServer);
        
        // Start listener
        try
        {
           serverSocket = null;
           serverSocket = new ServerSocket(getPort());
           
           Logger.debug.println("Started on port " + getPort());
           
           listen();
        } catch (IOException e)
        {
        	Logger.warn.println("Could not start on port " + getPort());
        }
    }
   
   
   
   
   
   public void stop()
   {
      try
      {
    	  Logger.debug.println("Stopped");
         
         // Unexport server
         UnicastRemoteObject.unexportObject(theServer, false);

         // Stop listener
         ServerSocket s = serverSocket;
         serverSocket = null;
         s.close();
      } catch (Exception e) {}
   }

   // Runnable implementation ---------------------------------------
   public void run()
   {
        Socket socket = null;
      
      
      
        // Accept a connection
        try 
        {
            socket = serverSocket.accept();
        } 
        
        catch (IOException e) 
        {
            if (serverSocket == null) return; // Stopped by normal means
         
            Logger.err.println("Naming stopped: " + e.getMessage());
            e.printStackTrace();
            Logger.warn.println("Restarting naming");
            try
            {
                initialize();
                
            } catch (Exception ex)
            {
            	Logger.warn.println("Restart failed");
                return;
            }
        }
      
        // Create a new thread to accept the next connection
        listen();

        // Return the naming server stub
        try 
        {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        
            out.writeObject(serverStub);
        } 
        catch (IOException ex) 
        {
            Logger.warn.println("Error writing response: " + ex.getMessage());
            ex.printStackTrace();
      
        } 
        finally 
        {
            try 
            {
                socket.close();
            } catch (IOException e) {}
         
        }
    }
    


   protected void listen()
   {
      new Thread(this).start();
   }
    
  
}