/*
 * @(#)DumpOutputStream.java
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

import jdf.framework.core.log.AsyncLogWriter;
import jdf.framework.core.log.HexLogFormat;
import jdf.framework.core.log.LogFormat;

import java.io.*;


/**
 * <p>
 * OutputStream에서 bytestream을 가로채 16진수 dump를 뜬다. 단독으로 쓰이기 보다는 중간 매개체 
 * 역활을 하면서 이용한다.
 * </p>
 * 예제)
 *<pre>
 *   KoscomOutputStream out = new KoscomOutputStream    ( new DumpOutputStream( s.getOutputStream()  ));
 *           
 *</pre>
 *
 * @author
 * @version 1.0
 */

public class DumpOutputStream extends FilterOutputStream
{

	//private OutputStream out;
	private AsyncLogWriter backWriter; // background로 쓰기를 처리한다.
	private ByteArrayOutputStream dump; // dump를 위한 임시 저장소

	private String descDump = "output size:";

	/**
	 * OutputStream을 받는 가장 기본이 되는 생성자.기본적으로 System.out으로 출력된다.
	 */
	public DumpOutputStream(OutputStream out) throws IOException
	{
		this(out, System.out, new HexLogFormat());
		//this(out, System.out, null );
	}

	/**
	 * OutputStream을 받고 dump출력 PrintStream를 정의한다.
	 */
	public DumpOutputStream(OutputStream out, PrintStream writer) throws IOException
	{
		this(out, writer, new HexLogFormat());
	}

	/**
	 * OutputStream을 받고 dump출력 PrintStream를 배열로 정의한다. 
	 */
	public DumpOutputStream(OutputStream out, PrintStream writer, LogFormat format) throws IOException
	{
		super(out);

		this.dump = new ByteArrayOutputStream();
		this.backWriter = new AsyncLogWriter(writer, format);
	}

	public void setDescript(String desc)
	{
		descDump = desc + " output size:";
	}

	public synchronized void write(byte b[], int off, int len) throws IOException
	{

		out.write(b, off, len);

		dump.write(b, off, len);
	}

	public synchronized void write(byte b[]) throws IOException
	{
		out.write(b, 0, b.length);

		dump.write(b, 0, b.length);
	}

	public synchronized void write(int b) throws IOException
	{
		out.write(b);

		dump.write(b);
	}

	public synchronized void flush() throws IOException
	{
		out.flush();

		byte[] b = dump.toByteArray();
		backWriter.write("", descDump + b.length, b, false);

		dump.reset();

	}

	/**
	 * output stream 을 closing한다.
	 */
	public void close() throws IOException
	{
		try
		{
			flush();
		}
		catch (IOException ignored)
		{
		}
		out.close();
	}

}