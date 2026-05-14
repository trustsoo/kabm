/*
 * @(#)ReconnectOutputStream.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the outformation about the copyright notice 
 * and the author.
 * 
 * @author
 */
 
package jdf.framework.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


/**
 * <p>
 * 접속정보를 가지고, write 실패시 socket에 재접속하여 다시 OutputStream을 얻는다.
 * </p>
 *
 * @author
 * @version 1.0
 */
 
public final class ReconnectOutputStream extends OutputStream
{
    private OutputStream out;
    
    
    private String ip;
    private int port;
    private long period;
    
    private boolean isError=false;
    
    
    public ReconnectOutputStream(String ip, int port, long period)
    {
        this.ip=ip;
        this.port=port;
    
        this.period=period;    
        
        try
        {
            out = getOutputStream();
        }
        
        catch(IOException ioe)
        {
            //System.out.println("ReconnectOutputStream error");
            isError=true;
            out=null;
        }
        
        
    }
    
    
    
    private OutputStream getOutputStream() throws IOException
    {
        //System.out.println("getOutputStream:"+period);
        Socket sock = new Socket(ip,port);
        OutputStream out= sock.getOutputStream();
        isError=false;
        
        return out;
    }
    
    
    
    
    
    private void setError()
    {
        isError=true;
    }
    
    
    
    private long nextCheckTime = 0;
    
    
    
    private synchronized boolean isReconnect()
    {
        
        
        long now=System.currentTimeMillis();
        
        if(isError && nextCheckTime<now)
        {
            nextCheckTime = now + period;
            return true;
        }
        
        return false;
    }
    
    
    
    
    public synchronized void write(int b) throws IOException
    {
        try
        {
            out.write(b);
        }
        catch(Exception e)
        {
            setError();
            
            if(isReconnect())
                out=getOutputStream();
            
        }
    }
    
    
    
    
    public synchronized void write(byte[] data, int offset, int length)
        throws IOException
    {
        try
        {
            
            out.write(data,offset,length);
            //System.out.println("ReconnectOutputStream write2");
        }
        catch(Exception ioe)
        {
            setError();
            
            if(isReconnect())
                out=getOutputStream();
        }
    }
    
    
    
    
    public synchronized void flush() throws IOException
    {
        try
        {
            if(out !=null)
                out.flush();
        }
        catch(IOException ioe)
        {
            setError();
            if(isReconnect())
                out=getOutputStream();
        }
    }
        
    
    public synchronized void close() throws IOException
    {
        out.close();
    }        
            
            


      
}