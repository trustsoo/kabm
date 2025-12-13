package jdf.framework.core.log;


import java.io.*;
import java.net.*;

import java.util.*;



public class MultiPrintStream extends PrintStream 
{
    List streams= new Vector();
    //List streams= new ArrayList();
    

    
    public MultiPrintStream(PrintStream ps)
    {
        super(ps);
        streams.add(ps);
    }
    
    private MultiPrintStream(OutputStream out)
    {
        super(out);
    }
    
    private MultiPrintStream(OutputStream out,boolean autoFlush )
    {
        super(out,autoFlush);
    }
    
    
    
    
    
    public void addPrintStream(PrintStream ps)
    {
        streams.add(ps);
        //System.out.println("size=========="+streams.size());
    }
    
    public void removePrintStream(PrintStream ps)
    {
        streams.remove(out);
    }
    
    
    private String ip=null;
    private int port;
    private boolean isReconnect;
    
    private Socket sock;
    
    private BufferedWriter textOut;
    
    private boolean isSocketError=true;
   
    
    public synchronized void setLogServerInfo(String ip, int port,boolean isReconnect)
    {
        this.ip=ip;
        this.port=port;
        this.isReconnect=isReconnect;
        
        connectLogServerSocket();
    }
    
    
    private void connectLogServerSocket()
    {
        try
        {
            if(isSocketError==true)
            {
                sock = new Socket(ip,port);
                
                System.out.println(sock);
                
                this.out = sock.getOutputStream();
                OutputStreamWriter charOut = new OutputStreamWriter(out);
	            this.textOut = new BufferedWriter(charOut);
            
                isSocketError=false;
            }
            
        }
        catch(Exception e)
        {
            System.err.println("socket connect error."+e.toString());
            isSocketError=true;
        }
    }
    
    
    private boolean isExecutable()
    {
        if(ip == null)
            return false;
            
        if(!isSocketError)
            return false;
        else
        {
            connectLogServerSocket();
            
            return ! isSocketError;
        }
    }
    
            
        
    
    
/********************************************** PrintStream 구현부 */    
    
    public boolean checkError()
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            
            if(ps.checkError())
                return true;
        }
        
        return false;
        
    }
    
    
    public void close()
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.close();
        }
        
        try {
            if(isExecutable())
                textOut.close();
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
        
    }
    
    
    public void flush()
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.flush();
        }
        
        try {
            if(isExecutable())
                textOut.flush();
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    public void print(boolean b)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(b);
        }
        
        
        try {
            if(isExecutable())
                textOut.write(String.valueOf(b));
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
        
    }
    
    
    public void print(char c)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(c);
        }
        
        try {
            if(isExecutable())
                textOut.write((int)c);
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }

        
    }
    
    
    public void print(char s[])
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(s);
        }
        
        try {
            if(isExecutable())
                textOut.write(s);
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    public void print(double d)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(d);
        }
        
        try {
            if(isExecutable())
                textOut.write(String.valueOf(d));
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    public void println(float f)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(f);
        }
        try {
            if(isExecutable())
                textOut.write(String.valueOf(f));
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    /*
    public void println(int i)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(i);
        }
        
        try {
            if(isExecutable())
                textOut.write(i);
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    */
    
    public void println(long l)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(l);
        }
        
        try {
            if(isExecutable())
                textOut.write(String.valueOf(l));
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    public void print(Object obj)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(obj);
        }
        
        try {
            if(isExecutable())
                textOut.write(obj.toString());
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    public void print(String s)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(s);
        }
        try {
            if(isExecutable())
                textOut.write(s);
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    public void println()
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.println();
        }
        
        try {
            if(isExecutable())
                textOut.write((int) '\n');
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    
    }
    
    
    public void println(boolean b)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.println(b);
        }
        
        try {
            if(isExecutable())
            {
                textOut.write(String.valueOf(b));
                textOut.write('\n');
            }
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    public void println(char x)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.println(x);
        }
        
        try {
            if(isExecutable())
            {
                textOut.write(x);
                textOut.write('\n');
            }
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    public void println(char x[])
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(x);
        }
        
        try {
            if(isExecutable())
            {
                textOut.write(x);
                textOut.write('\n');
            }
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    
    public void println(double x)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.print(x);
        }
        
        try {
            if(isExecutable())
            {
                textOut.write(String.valueOf(x));
                textOut.write('\n');
            }
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    
    public void println(int x)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.println(x);
        }
        
        try {
            if(isExecutable())
            {
                textOut.write(x);
                textOut.write('\n');
            }
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    
    /*
    public void println(long x)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.println(b);
        }
        
        try {
            if(isExecutable())
            {
                textOut.write(String.valueOf(x));
                textOut.write('\n');
            }
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    */
    
    
    public void println(String x)
    {
        
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.println(x);
        }
        
        try {
            
            
            if(isExecutable())
            {
                textOut.write(x);
                textOut.write('\n');
            }
            
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            isSocketError=true;
        }
    }
    
    
    
    public void println(Object x)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.println(x);
        }
        
        try {
            if(isExecutable())
            {
                textOut.write(x.toString());
                textOut.write('\n');
            }
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
    
    

    
    public void write(byte buf[])
    {
        write(buf, 0, buf.length);
    }

    
    
    public void write(byte buf[], int off, int len)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.write(buf,off,len);
        }
        
        
        try {
            if(isExecutable())
            {
                out.write(buf, off, len);
            }
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
        
    }
    
    
    public void write(int b)
    {
        for(int j=0; j<streams.size();j++)
        {
            PrintStream ps = (PrintStream) streams.get(j);
            ps.write(b);
        }        
        
        
        try {
            if(isExecutable())
            {
                out.write(b);
            }
            
        }
        catch(IOException ioe)
        {
            isSocketError=true;
        }
    }
}