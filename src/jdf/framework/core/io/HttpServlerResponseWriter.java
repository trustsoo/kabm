package jdf.framework.core.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import jdf.framework.core.log.Logger;


/**
 * 
 * 
 * @author
 *
 */
public class HttpServlerResponseWriter extends Writer
{
    private ByteArrayOutputStream bos = new ByteArrayOutputStream();
    
    private String encoding = "euc-kr";
    
    private HttpServletResponse httpResponse;
    
    public HttpServlerResponseWriter(HttpServletResponse res, String encoding)
    {
        super();
        
        this.httpResponse=res;
        
        this.encoding=encoding;
    }

    public void write(char[] arg0, int arg1, int arg2) throws IOException
    {
        String w = new String(arg0, arg1, arg2);
        
        bos.write(w.getBytes(encoding));

    }

    public void flush() throws IOException
    {
        byte[] result = bos.toByteArray();
        
        if(Logger.packet.isPrintMode())
            Logger.packet.println("HTTP RESPONSE\n"+new String(result));
        
        this.httpResponse.setContentLength(result.length);
        
        OutputStream os = this.httpResponse.getOutputStream();
        os.write(result);
        os.flush();

    }

    public void close() throws IOException
    {
        return;

    }

    /**
     * @see java.io.Writer#write(java.lang.String)
     */
    public void write(String w) throws IOException
    {
        bos.write(w.getBytes(encoding));
    }

}