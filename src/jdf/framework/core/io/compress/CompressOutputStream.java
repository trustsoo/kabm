package jdf.framework.core.io.compress;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CompressOutputStream extends FilterOutputStream
{
	private boolean isPrintLog = false;
	private ByteArrayOutputStream byteArrayOut;

	public CompressOutputStream(OutputStream out)
	{
		super(out);
		byteArrayOut = new ByteArrayOutputStream();
	}
	
	/**
     * 데이터를 압축하여 내보낸다.
     */
	public void flush() throws IOException
	{
		
		CodeOutputPacker baos = new CodeOutputPacker (out);
		int before_size = LZW.Compress (byteArrayOut.toByteArray(), baos);
		int after_size = baos.getCount ();

		if (isPrintLog)
			System.out.println("before: " + before_size + "   after: " +after_size + " Ratio: 1:" + ((float)after_size / before_size));
		
		baos.flush();
		byteArrayOut.reset();
	}
	
	public void write(int b) throws IOException 
	{
		byteArrayOut.write(b);
    }

	public void write(byte b[]) throws IOException {
		byteArrayOut.write(b, 0, b.length);
    }

	public void write(byte b[], int off, int len) throws IOException {
		for (int i = 0 ; i < len ; i++) {
			write(b[off + i]);
		}
    }
	
	/**
     * 로그 출력여부를 설정한다.
     */
	public void setLog(boolean isPrintLog){
		this.isPrintLog = isPrintLog;
	}
}
