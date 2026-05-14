package jdf.framework.core.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SeekableInputStream extends FilterInputStream 
{

    private static int defaultBufferSize = 2048;
    protected byte buf[];
    protected int count;
    protected int pos;
	protected int markpos = -1;
    protected int marklimit;

	public SeekableInputStream(InputStream in) 
	{
		this(in, defaultBufferSize);
    }

    public SeekableInputStream(InputStream in, int size) 
	{
		super(in);
        if (size <= 0)
            throw new IllegalArgumentException("Buffer size <= 0");
		
		buf = new byte[size];
    }

	public int getBufferSize()
	{
		return buf.length;
	}

    private void ensureOpen() throws IOException 
	{
		if (in == null)
			throw new IOException("Stream closed");
    }

    private void fill() throws IOException 
	{
		if (markpos < 0)
			pos = 0;
		else if (pos >= buf.length)
		{
			if (markpos > 0)
			{
				int sz = pos - markpos;
				System.arraycopy(buf, markpos, buf, 0, sz);
				pos = sz;
				markpos = 0;
			}
			else if (buf.length >= marklimit) 
			{
				markpos = -1;
				pos = 0;
			}
			else
			{
				int nsz = pos * 2;
				if (nsz > marklimit)
					nsz = marklimit;
				byte nbuf[] = new byte[nsz];
				System.arraycopy(buf, 0, nbuf, 0, pos);
				buf = nbuf;
			}
		}

		count = pos;
		int n = in.read(buf, pos, buf.length - pos);
		if (n > 0)
			count = n + pos;
    }

    public synchronized int read() throws IOException 
	{
        ensureOpen();
		if (pos >= count) {
			fill();
			if (pos >= count)
			return -1;
		}
		return buf[pos++] & 0xff;
    }

    private int read1(byte[] b, int off, int len) throws IOException 
	{
		int avail = count - pos;
		if (avail <= 0) 
		{
			if (len >= buf.length && markpos < 0)
				return in.read(b, off, len);

			fill();
			avail = count - pos;
			if (avail <= 0) return -1;
		}

		int cnt = (avail < len) ? avail : len;
		System.arraycopy(buf, pos, b, off, cnt);
		pos += cnt;
		return cnt;
    }
	
	public int seek(byte[] b) throws IOException
	{
		return seek(b, 0, b.length);
	}

	public int seek(byte[] b, int off, int len) throws IOException 
	{
		int avail = count - pos;
		if (avail <= 0) 
		{
			if (len >= buf.length && markpos < 0)
				return 0;

			fill();
			avail = count - pos;
			if (avail <= 0) return -1;
		}

		int cnt = (avail < len) ? avail : len;
		System.arraycopy(buf, pos, b, off, cnt);
		
		return cnt;
    }

    public synchronized int read(byte b[], int off, int len) throws IOException
    {
        ensureOpen();
        if ((off | len | (off + len) | (b.length - (off + len))) < 0)
			throw new IndexOutOfBoundsException();
		else if (len == 0)
			return 0;

		int n = read1(b, off, len);
		if (n <= 0) 
			return n;

		while ((n < len) && (in.available() > 0)) 
		{
			int n1 = read1(b, off + n, len - n);
			if (n1 <= 0) break;
			n += n1;
		}
		return n;
    }

    public synchronized long skip(long n) throws IOException 
	{
        ensureOpen();
		if (n <= 0)
		    return 0;
		
		long avail = count - pos;
     
        if (avail <= 0) 
		{
            if (markpos <0) 
                return in.skip(n);
            
            fill();
            avail = count - pos;
            if (avail <= 0)
                return 0;
        }
        
        long skipped = (avail < n) ? avail : n;
        pos += skipped;
        return skipped;
    }

    public synchronized int available() throws IOException 
	{
        ensureOpen();
		return (count - pos) + in.available();
    }

    public synchronized void mark(int readlimit) 
	{
		marklimit = readlimit;
		markpos = pos;
    }

    public synchronized void reset() throws IOException 
	{
        ensureOpen();
		if (markpos < 0)
			throw new IOException("Resetting to invalid mark");
		pos = markpos;
    }

    public boolean markSupported() 
	{
		return true;
    }

    public void close() throws IOException 
	{
        if (in == null)
            return;

        in.close();
        in = null;
        buf = null;
    }
}
