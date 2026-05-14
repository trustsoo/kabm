package jdf.framework.core.io;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;

import java.io.*;
import java.net.Socket;


/**
 * <p>
 * InputStream, OutputStream에 관계된 Utility성 Class
 * </p>
 *
 * @author
 * @version 1.0
 */

public final class StreamUtil
{

    private static final int BUFFERSIZE=4096;
    
    private StreamUtil()
    {
    }

    /**
     *  InputStream을 OutputStream으로 복사한다.
     *  @param in InputStream
     *  @param out OutputStream
     */
    public static void copy(InputStream in, OutputStream out) throws IOException
    {
      synchronized(in)
      {
        synchronized(out)
        {
          byte[] buffer = new byte[BUFFERSIZE];
          while(true)
          {
            int bytesRead = in.read(buffer);
            if(bytesRead == -1) break;
            out.write(buffer,0,bytesRead);
          }
        }
      }
    }
  
  
  
    /**
     *  InputStream에서 blocking 되지 않는 범위의 byte stream을 얻는다.
     *  @param in InputStream
     */
    public static byte[] getBytes(InputStream in) throws IOException
	{
	    byte[] buffer = new byte[in.available()];
		
		in.read(buffer);
		
		return buffer;
		
	}
  
  
  
    /**
     *  InputStream에서 blocking 되지 않는 범위의 byte stream을 얻는다.
     *  @param in InputStream
     */
    public static byte[] getBytesOld(InputStream in) throws IOException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUFFERSIZE];
		
		int read=0;
		
		
		
		while( (read=in.read(buffer)) >= 0 )
		{
		    
		    
		     bout.write(buffer,0,read);
		
		     if (in.available() == 0)
		        break; // Blocking 되지 않는 범위에서 읽는다.
        }
	    
	    byte[] result = bout.toByteArray();
	    
	    
	    if(result.length == 0)
	        throw new IOException("no data available any more");
	    
	    return result;
	}
  
  
    /**
     *  InputStream에서 blocking 되지 않는 범위의 byte stream을 얻는다.
     *  @param in InputStream
     */  
    public static byte[] getBytes(InputStream in, int len) throws IOException
	{
	    
        if(len > 1500)
        {
            for(int i=0; len > in.available() && i<500;i++)
            {
                try {
                    //System.out.println(len+"미치 --- >"+in.available() );
                    Thread.sleep(10);
                } catch(Exception e) {}
            }
        }   
		
		
		byte[] buffer = new byte[len];
		
		int read=in.read(buffer);
		
		if(read <0)
            throw new IOException("InputStream closed. size="+read);
		
		/*
		if(read != len)
            throw new IOException("data abnoraml");
        */    
        
        return buffer;    
		
    }
    
    
    
    
    
    
    
    /**
     *  InputStream에서 blocking하며,  byte stream을 얻는다.
     *  @param in InputStream
     */
    public static byte[] getBlockingBytes(InputStream in) throws IOException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUFFERSIZE];
		
		int read=0;
		
		while( (read=in.read(buffer)) >= 0 )
		{
		    //System.out.println(read);
		     bout.write(buffer,0,read);
		     
		     if (in.available() == 0)
		        break; // Blocking 되지 않는 범위에서 읽는다.
		
        }
	    
	    byte[] result = bout.toByteArray();
	    
	    
	    if(result.length == 0)
	        throw new IOException("no data available any more");
	    
	    return result;
	}
  
    
    
    
    
    
    
    
    public static byte[] getBlockingBytes(InputStream in,int len) throws IOException
	{
	    /*
		byte[] buffer = new byte[len];
		
		int read=in.read(buffer);
		
		if(read != len)
            throw new IOException("data abnoraml");
        
        return buffer;    
        */
        
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[len];
		
		int read=0;
		
		
		
		for(int j=0; j<len; j++)
		{
		    
		    read=in.read();
		    bout.write(read);
        }
	    
	    byte[] result = bout.toByteArray();
	    
	    
	    
	    if(result.length !=len)
	        throw new IOException("no data available any more");
	    
	    return result;
	}
    
    
    /**
     * HTTP Header 정보를 걸러 본문만 return한다.
     *
     */
    
    public static byte[] cutOffHttpHeader(byte[] inBytes)
    {
        for(int p=0; p< inBytes.length-4;p++)
        {
            if( inBytes[p]=='\r'&& inBytes[p+1]=='\n'
                && inBytes[p+2]=='\r'&& inBytes[p+3]=='\n')
            {
                
                byte[] body = new byte[inBytes.length - p-4];
                
                System.arraycopy(inBytes, p+4, body, 0, body.length);
                
                return body;
            }
        }
        
        return new byte[] {};
    }
            
    
    public static byte[] getHttpBodyBytes(InputStream in) throws IOException
    {
        byte[] read = getBytes(in);
        
        
        return cutOffHttpHeader(read);
        
        
    }
    
    
    
    
    
    
    
    private static boolean isDumpMode=false;
    private static boolean isRBuffering=false;
    private static boolean isWBuffering=false;
    private static int readBufSize=512;
    private static int writeBufSize=512;
    
    
/*    
network.socket.dump.enable=false

network.socket.buffer.readSize=512
network.socket.buffer.writeSize=512  
*/
    
    static {
        try
        {
            Config conf = Configuration.lookup("/network/socket/dump");
            isDumpMode=conf.getBoolean("enable",false);
            
            conf = Configuration.lookup("/network/socket/buffer");
            readBufSize=conf.getInt("readSize",-1);
            writeBufSize=conf.getInt("writeSize",-1);
            
            isRBuffering = readBufSize > 0;
            isWBuffering = writeBufSize > 0;
            
        }
        catch(Exception e) {e.printStackTrace();}
        
    }
    
    
    
    
    
    
            
    /**
     * Socket 에서 InputStream을 가져온다.
     * 설정화일에 따라 Buffering 또는 Dump을 뜰수 있는 형태로 가져온다.
     */        
    public static InputStream getInputStream(Socket sock) throws IOException
    {   
        
        InputStream in = sock.getInputStream();
        
        if(isRBuffering)
            in = new BufferedInputStream(in,readBufSize);
        
        if(isDumpMode)
        {
            in = new DumpInputStream(in);
            ((DumpInputStream) in).setDescript(sock.toString());
            
        }
        
        
        return in;
    }
    
    
    /**
     * Socket 에서 OutputStream을 가져온다.
     * 설정화일에 따라 Buffering 또는 Dump을 뜰수 있는 형태로 가져온다.
     */
    public static OutputStream getOutputStream(Socket sock) throws IOException
    {   
        
        OutputStream out = sock.getOutputStream();
        
        if(isWBuffering)
            out = new BufferedOutputStream(out,writeBufSize);
        
        if(isDumpMode)
        {
            out = new DumpOutputStream(out);
            ((DumpOutputStream) out).setDescript(sock.toString());
        }
        
        
        return out;
    }
        
    
    
/*    
    
    Class SocketKey
    {
        int hashcode;
        
        public SocketKey(String ip,int port)
        {
            hashcode=ip.hashCode()+port;
        }
        
        public SocketKey(int port)
        {
            hashcode=port;
        }
        
        public int hashCode()
        {
            return hashcode;
        }
        
    }
*/    
    
}
