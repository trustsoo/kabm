/*
 * @(#)TimeoutSocket.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */
 
package jdf.framework.core.io;

import java.io.IOException;
import java.net.Socket;


/**
 * <p>
 * Socket연결시 잘못된 ip나 host name에서 시간지연현상을 timeout시간을 주어
 * 대기시간을 설정할 수 있게 한 Class
 * <p>
 *
 *
 * <b>예제</b>
 * <pre>
 * TimeoutSocket ts = new TimeoutSocket("1.1.1.1",100);
 * Socket s = ts.getSocket(10*1000); // 10초 동안만 대기한다.
 * </pre>
 *
 * @author
 * @version 1.0
 */
public class TimeoutSocket implements Runnable,Cloneable
{
    
    
    private String ip;
    private int port;
    
    
    public TimeoutSocket(String ip, int port)
    {
        this.ip=ip;
        this.port=port;    
    }
    
    
    
    
    public Socket getSocket(int timeout) throws IOException
    {
        TimeoutSocket tso;
        Thread thread;
        
        try
        {
            tso = (TimeoutSocket) clone();
            
            thread = new Thread(tso);
            thread.setDaemon(true);
            thread.start();
            
            thread.join(timeout);
            //thread.interrupted();
            
            
            Socket so = tso.getSocket();
            
            if(tso.ioe !=null)
                throw tso.ioe;
            
            if(so == null)
                throw new IOException(timeout+"milli seconds timeout.");
            
            return so;
        }
        catch(IOException ioe)
        {
            throw ioe;
        }
        catch(Exception e)
        {
            throw new IOException(e.toString());
        }
        finally
        {
            tso=null;
            thread=null;
        }
        
            
        
    }
    
/*  **************** Thread process ******************/            

    private Socket sock;
    private IOException ioe;;
    
    public void run()
    {
        try
        {
            this.sock = new Socket(ip, port);
        }
        
        /*catch(InterruptedException ie)
        {
            this.ioe = new IOException("timeout");
        }*/
        catch(IOException ioe)
        {
            this.ioe=ioe;
        }
    }
    
    
    
    private Socket getSocket()
    {
        return this.sock;
    }
    
    
}
