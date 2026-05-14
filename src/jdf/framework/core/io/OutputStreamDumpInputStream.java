package jdf.framework.core.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * 
 * @author
 *
 */
public class OutputStreamDumpInputStream extends FilterInputStream
{
    private OutputStream out;
    
    public OutputStreamDumpInputStream(InputStream in, OutputStream out)
    {
        super(in);
        
        this.out=out;
    }

    /**
     * 
     * @see FilterInputStream#read()
     */
    public int read() throws IOException
    {
        int tmp = super.read();
        out.write(tmp);
        
        return tmp;
    }

    /**
     * 
     * @see FilterInputStream#read(byte[], int, int)
     */
    public int read(byte[] arg0, int arg1, int arg2) throws IOException
    {
        int r= super.read(arg0, arg1, arg2);
        out.write(arg0, arg1,arg2);
        return r;
    }

    /**
     * 
     * @see FilterInputStream#read(byte[])
     */
    public int read(byte[] arg0) throws IOException
    {
        int r= super.read(arg0);
        out.write(arg0);
        
        return r;
    }
    
    

}