/*
 * @(#)HttpOutputStream.java
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

import jdf.framework.core.util.AsciiUtil;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * <p>
 * OutputStream에서 bytestream을 가로채 16진수 dump를 뜬다. 단독으로 쓰이기 보다는 중간 매개체 
 * 역활을 하면서 이용한다.
 * </p>
 * 예제)
 *<pre>
 *   KoscomOutputStream out = new KoscomOutputStream    ( new HttpOutputStream( s.getOutputStream()  ));
 *           
 *</pre>
 *
 * @author
 * @version 1.0
 */
 
public final class HttpOutputStream extends FilterOutputStream
{
    private static byte[] NEWLINE = "\r\n".getBytes();
    
    private static byte[] CONTENT_LENGTH = "Content-Length: ".getBytes();
    private static byte[] CONTENT_TYPE = "Content-Type: ".getBytes();
    
    
    
    private static byte[] TYPE_BINARY = "binary/dat".getBytes();
    
    
    
    
    private byte[] header;
    
    
    private ByteArrayOutputStream dump = new ByteArrayOutputStream();  // dump를 위한 임시 저장소
    
    
    private boolean isFirstSend = true;
    
    
    /**
     * OutputStream을 받는 가장 기본이 되는 생성자.기본적으로 System.out으로 출력된다.
     */
    public HttpOutputStream(OutputStream out, String header) 
    {
        super(out);
        
        this.header = header.getBytes();
        
    
    }

    
    public HttpOutputStream(OutputStream out) 
    {
        super(out);
    }
    
    
    
    
    
    
    
    
    public void write(byte b[], int off, int len) throws IOException
    {
	    dump.write(b,off,len);
    }
    
    
    public void write(byte b[]) throws IOException
    {
        
	    dump.write(b, 0, b.length);
    }
    
    
    
    public void write(int b) throws IOException
    {
	    dump.write(b);
    }
    
    
    
    
    
    /**
     * HTTP HEADER에 특정 argument와 값을 세팅한다.
     * new line인 필요없지만, 여러개를 한꺼번에 넣는 경우는 알아서
     * new line을 넣야한다.
     *
     */
    public void writeHead(String header) throws IOException
    {
        if(isFirstSend)
        {
            out.write( this.header );
            out.write(NEWLINE); 
            
            isFirstSend=false;
        }
        
        out.write(AsciiUtil.asciiToBytes(header) );
        out.write(NEWLINE);
        
    }
    
    
    
    
    
    public void flush() throws IOException 
    {
        
        
        isFirstSend=true;
        
        byte[] b=dump.toByteArray();
        
        out.write(CONTENT_LENGTH);
        out.write( AsciiUtil.asciiToBytes(String.valueOf(b.length) ) );
        out.write(NEWLINE);
        out.write(TYPE_BINARY);
        out.write(NEWLINE);
        
        
        
        out.write(NEWLINE);
        out.write(b,0,b.length);
        out.flush();
        
            
        dump.reset();
        
            
    }
    
    /**
     * output stream 을 closing한다.
     */
    public void close() throws IOException 
    {
	    try {
	        flush();
	        
	        dump = null;
	    
	    } catch (IOException ignored) {
	    }
	    out.close();
    }
    
      
}
