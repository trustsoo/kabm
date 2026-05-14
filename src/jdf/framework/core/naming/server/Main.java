package jdf.framework.core.naming.server;

import jdf.framework.core.log.Logger;
import jdf.framework.core.naming.rmi.NamingContext;
import jdf.framework.core.service.AbstractService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;


public class Main extends AbstractService implements Runnable, MainMBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3201371212608098658L;
	
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
    
    
    //private final static anyframe.pool.ThreadPool pool = new anyframe.pool.ThreadPool("JNDI Server", 1,5);
    
    public Main()
    {
		this(1099);
    }

	public Main(int port)
    {
		setPort(port);
    }

	/**
	 * 
	 * @uml.property name="port"
	 */
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

    public void run()
    {
        setState(STARTING); // ���¸� '����'�����Ѵ�.
/*
        try
        {
            Config conf = getConfigInfo();
            port = conf.getInt("port");
        }
        catch(Exception e)
		{
			e.printStackTrace();
		
		}
        
        setPort(port);
*/        
        try
        {
            // Create remote object
            theServer = new NamingServer();
        
            NamingContext.setLocalNaming(theServer);
        
            // Export server
            serverStub = UnicastRemoteObject.exportObject(theServer);
        
            
            
            // Start listener
        
            serverSocket = null;
            serverSocket = new ServerSocket(getPort());
            
            setState(RUNNING); // ���¸� '����'�����Ѵ�.
            
            Logger.debug.println("Started on port " + getPort());
            
            
            while(true)
            {
                Socket socket = serverSocket.accept();
                
                //pool.execute(new MainThread(socket,serverStub));
                new Thread(new MainThread(socket,serverStub)).start();
                
            }
            
        }    
        catch(Exception e)
        {
            if(getState()==STOPPED || getState()==STOPPING)
            {
                Logger.warn.println("<Service:"+getName()+"> listen port is closed.");
            }
            else
            {
                StringWriter out = new StringWriter();
                PrintWriter writer = new PrintWriter(out);
                e.printStackTrace(writer);
                
                Logger.err.println("<Service:"+getName()+"> service fail. cause:\n"+out.toString());
            }
        }
        finally
        {
            try {
                serverSocket.close();
            } catch(Exception e) {}
            
            setState(STOPPED);
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








    
    
    
    
    class MainThread implements Runnable
    {
        Socket sock;
        Remote serverStub2;
        
        MainThread(Socket sock, Remote serverStub)
        {
            this.sock=sock;
            this.serverStub2=serverStub;
        }
        
        
        public void run()
        {
            
            try 
            {
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.writeObject(serverStub2);
            } 
            catch (IOException ex) 
            {
                Logger.warn.println("MainThread Error writing response: " + ex.getMessage());
                ex.printStackTrace();
            } 
            finally 
            {
                try 
                {
                    sock.close();
                } 
                catch (IOException ioe) 
                {
                }
            
                sock = null;
            }
        }
        
                
        
    }
    
  
}