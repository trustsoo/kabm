/*
 * @(#)AbstractSocketClinet.java
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


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * <p>
 * 일반적인 Socket Client들이 가져야 하는 특징을 최대한 추상화 해서 만든 Class
 * </p>
 *
 * @author
 * @version 0.5
 */




public abstract class AbstractSocketClient implements Cloneable, Runnable
{
	
	protected Socket sock;
	
	protected BufferedInputStream in = null;
	protected OutputStream out = null;
	
	
	/**
	 * 무의미한 생성자는 미리 막는다.
	 */
	private AbstractSocketClient() {}
	
	
	
	/**
	 * 생성자
	 *
	 **/
	protected AbstractSocketClient(Socket sock)
		throws UnknownHostException, IOException
	{
		this.sock = sock;
		
		in = new BufferedInputStream( sock.getInputStream() );
		out = sock.getOutputStream();
		
		init();
	}
	
	
	
	protected AbstractSocketClient(String host, int port)
		throws UnknownHostException, IOException
	{
		this( new Socket(host, port) );
	}
	
	
	
	
	
	
	
	
	/**
	 * 소켓 연결후 바로 처리해야 할 작업을 구현한다.
	 *
	 **/
	protected abstract void init();
	
	
	/**
	 * 실제 Socket Client가 해야될 일을 구현한다.
	 *
	 **/
	protected abstract void process();
	
	
	/**
	 * process() 도중, 예기치 못한 예러를 처리한다.
	 *
	 **/
	protected abstract void processError(Throwable e);
	
	
	/**
	 * Socket Client가 모든 작업을 끝내고 연결을 끝은후 해야할 일을 구현한다.
	 *
	 */
	protected abstract void stop();
	
	
	
	/**
	 * 서버로 부터 데이타(byte stream)을 받는다.
	 *
	 * @return byte[] byte stream
	 * @exception IOException
	 * @roseuid 3A22499801C2
	 */
	protected byte[] receive() throws IOException
	{
		byte[] contents = null;
	    
		    	
		int buffer = -1;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
			
		do {
			buffer = in.read();
			bout.write( buffer );
				//System.out.println(" buffer:"+buffer );
		} while( in.available()>0 );
			
		contents = bout.toByteArray();
    	
    	return contents;
	}
	
	
	/**
	 * 서버에게  데이타(byte stream)를 보낸다.
	 *
	 * @param byte[] byte stream
	 * @return void
	 * @exception IOException
	 * @roseuid 3A22499801C2
	 */
	protected void send(byte[] packet) throws IOException
	{
		out.write(packet);
	}
	
	
	
	
	public void run()
	{
		try
		{
			process();
		}
		catch(Throwable e)
		{
			processError(e);
		}
		finally
		{
			close();
			stop();
		}
	}
	
	
	private void close()
	{
		try
		{
			sock.close();
		}
		catch(IOException ioex) {}
		
	}
	
	
	
}

