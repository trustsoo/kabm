package jdf.framework.core.io.compress;

import java.io.*;

public class CompressInputStream extends FilterInputStream
{
	private ByteArrayOutputStream byteArrayOut;
	private ByteArrayInputStream byteArrayIn;

	public CompressInputStream(InputStream in)
	{
		super(in);
		byteArrayOut = new ByteArrayOutputStream();
		//byteArrayIn = new ByteArrayInputStream();
	}
	
	/**
     * non blocking 상태까지 데이타(byte stream)를 읽어온다.
     *
     * @return byte stream
     */
	public byte[] unCompress() throws IOException 
	{
		CodeInputUnpacker bais = new CodeInputUnpacker (in);
		
		int size = LZW.Expand (bais, byteArrayOut);

		byte[] result = byteArrayOut.toByteArray ();
		byteArrayOut.reset();
		return result;
    }
}