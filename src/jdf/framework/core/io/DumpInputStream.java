/*
 * @(#)DumpInputStream.java
 *
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

import jdf.framework.core.log.AsyncLogWriter;
import jdf.framework.core.log.HexLogFormat;
import jdf.framework.core.log.LogFormat;

import java.io.*;


/**
 * <p>
 * InputStream에서 bytestream을 가로채 16진수 dump를 뜬다. 단독으로 쓰이기 보다는 중간 매개체 
 * 역활을 하면서 이용한다.
 * </p>
 * 예제)
 *<pre>
 *   KoscomInputStream in = new KoscomInputStream    ( new DumpInputStream( s.getInputStream()  ));
 *           
 *   EntityHeader header = (EntityHeader) in.readEntity( EntityHeader.class);
 *   Entity1071in body = (Entity1071in) in.readEntity( Entity1071in.class);
 *</pre>
 *
 * @author
 * @version 1.0
 */
public final class DumpInputStream extends FilterInputStream
{

	//private InputStream in;             // read()를 위해
	private ByteArrayOutputStream out; // dump를 위한 임시 저장소

	private AsyncLogWriter backWriter; // background로 쓰기를 처리한다.

	private String descDump = "input size:";

	/**
	 * InputStream을 받는 가장 기본이 되는 생성자
	 * 기본적으로 System.out으로 출력한다. 하지만 buffering을 하지않은 상태로 출력을 한다.
	 * buffering을 원하면, 다음과 같이
	 * new DumpInputStram(in, new PrintStream(new BufferedOutputStream(System.out)) )
	 * 생성자를 이용한다.
	 */
	public DumpInputStream(InputStream in) throws IOException
	{

		this(in, System.out, new HexLogFormat());
	}

	/**
	 * InputStream을 받고 dump출력 PrintStream를 정의한다.
	 */
	public DumpInputStream(InputStream in, PrintStream writer) throws IOException
	{
		this(in, writer, new HexLogFormat());
	}

	/**
	 * InputStream을 받고 dump출력 PrintStream를 정의한다.
	 *
	 */
	public DumpInputStream(InputStream in, PrintStream writer, LogFormat format) throws IOException
	{
		super(in);

		this.out = new ByteArrayOutputStream();
		this.backWriter = new AsyncLogWriter(writer, format);
	}

	public void setDescript(String desc)
	{
		descDump = desc + " input size:";
	}

	public int read() throws IOException
	{
		return in.read();
	}

	public final int read(byte b[]) throws IOException
	{
		int size = in.read(b, 0, b.length);

		if (size == -1)
			return -1;

		//System.out.println("size = "+size);
		//System.out.println("size = "+b.length);

		out.write(b, 0, size);

		//System.out.println("size = "+size);
		//System.out.println("size = "+b.length);

		if (size != b.length) //다 읽을때까지 out에 저장한다.
		{
			b = out.toByteArray();
			backWriter.write("", descDump + b.length, b, false);
			out.reset();
		}

		return size;

	}

	public final int read(byte b[], int off, int len) throws IOException
	{
		int size = in.read(b, off, len);

		out.write(b, off, size);

		if (size != b.length) //다 읽을때까지 out에 저장한다.
		{
			b = out.toByteArray();
			backWriter.write("", descDump + b.length, b, false);
			out.reset();
		}

		return size;

	}

	public int available() throws IOException
	{
		return in.available();
	}

}