/*
 * @(#)UDPOutputStream.java
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
import java.net.*;


/**
 * <p>
 * 접속정보를 가지고, write 실패시 socket에 재접속하여 다시 OutputStream을 얻는다.
 * </p>
 *
 * @author
 * @version 1.0
 */
 
public final class UDPOutputStream extends OutputStream
{
    private DatagramSocket out;
    
    
    private String ip;
    private int port;
    
    private InetAddress inet;
    
    
    public UDPOutputStream(String ip, int port) throws UnknownHostException, SocketException
    {
        this.ip=ip;
        this.port=port;
        
        this.inet = InetAddress.getByName(ip);
        
        out = new DatagramSocket(); 
        
    }
    
    
    
    public void write(int b) throws IOException
    {
        DatagramPacket packet = new DatagramPacket( new byte[] {(byte)b}, 1, inet, port);
        
        out.send( packet);
        
    }
    
    
    
    
    public void write(byte[] data, int offset, int len)
        throws IOException
    {
        if(len < 3) // ? OutputStream의 println시 \r\n이 가는것은 막기 위해서
            return;
            
        DatagramPacket packet = new DatagramPacket(data, offset, len , inet, port);
        
        //System.out.println( jdf.framework.core.util.Hexa.logFormat( data,len) );
        
        out.send( packet);
    }
    
    
    
    
    public void flush() throws IOException
    {
        return;
    }
        
    
    public synchronized void close() throws IOException
    {
        out.close();
    }        
            
            


      
}
