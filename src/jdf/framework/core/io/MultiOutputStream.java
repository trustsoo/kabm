/*
 * @(#)MultiOutputStream.java
 *
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

import java.io.*;
import java.util.*;



/**
 * <p>
 * 여러 OutputStream으로 출력을 보낸다.
 * </p>
 *
 * @author
 * @version 1.0
 */
 
public class MultiOutputStream extends FilterOutputStream
{
    //List streams= new Vector();
    List streams= new ArrayList();
    
    private OutputStream[] outArray;
    
    
    private boolean isErrorRaise=false;
    
    
    
    public MultiOutputStream(OutputStream out)
    {
        super(out);
        streams.add(out);
        setArray();
    }
    
    
    private void setArray()
    {
        this.outArray = (OutputStream[]) this.streams.toArray(new OutputStream[] {});
    }
    
    
    public void checkError(boolean errorCheck)
    {
        this.isErrorRaise = errorCheck;
    }
    
    
    public synchronized void addOutputStream(OutputStream out)
    {
        streams.add(out);
        setArray();
    }
    
    
    public synchronized void removeOutputStream(OutputStream out)
    {
        streams.remove(out);
        setArray();
    }
    
    public synchronized void removeAllOutputStream()
    {
        streams.clear();
        setArray();
    }
    
    
    public void write(int b) throws IOException
    {
        for(int j=0; j<this.outArray.length;j++)
        {
            try
            {
                
                OutputStream out = this.outArray[j];
                out.write(b);
            }
            catch(IOException ioe)
            {
                if(isErrorRaise)
                    throw ioe;
            }
                
        }
    }
    
    
    
    public void write(byte[] b) throws IOException
    {
        for(int j=0; j<this.outArray.length;j++)
        {
            try
            {
                
                OutputStream out = this.outArray[j];
                out.write(b);
            }
            catch(IOException ioe)
            {
                if(isErrorRaise)
                    throw ioe;
            }
                
        }
    }
    
    
    
    
    public void write(byte[] data, int offset, int length)
        throws IOException
    {
        for(int j=0; j<this.outArray.length;j++)
        {
            try 
            {
                //System.out.println("------------"+j);
                OutputStream out = this.outArray[j];
                out.write(data, offset, length);
            }
            catch(IOException ioe)
            {
                if(isErrorRaise)
                    throw ioe;
            }
        }
    }
    
    
    public void flush() throws IOException
    {
        for(int j=0; j<this.outArray.length;j++)
        {
            try
            {
                OutputStream out = this.outArray[j];
                out.flush();
            }
            catch(IOException ioe)
            {
                if(isErrorRaise)
                    throw ioe;
            }
                
        }
    }
        
    
    public synchronized void close() throws IOException
    {
        for(int j=0; j<this.outArray.length;j++)
        {
            try
            {
                OutputStream out = this.outArray[j];
                out.close();
            }
            catch(IOException ioe)
            {
                if(isErrorRaise)
                    throw ioe;
            }
        }
    }        
            
            


      
}
