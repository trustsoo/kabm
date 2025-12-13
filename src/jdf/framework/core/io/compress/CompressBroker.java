package jdf.framework.core.io.compress;

import java.io.*;

public class CompressBroker 
{
	private CompressBroker(){
	}

	public static void compress(InputStream in, OutputStream out) throws IOException
	{
		try
		{
			CodeOutputPacker codeOut = new CodeOutputPacker (out);

			int before_size = LZW.Compress (in, codeOut);
			int after_size = codeOut.getCount ();

			codeOut.flush();
			
			//System.out.println("before: " + before_size + "   after: " +after_size + " Ratio: 1:" + ((float)after_size / before_size));
		}
		catch (Exception e)
		{
			throw new IOException("Compressing fail! : " + e.toString());
		}
	}

	public static void uncompress(InputStream in, OutputStream out) throws IOException
	{
		try
		{
			CodeInputUnpacker codeIn = new CodeInputUnpacker (in);
			
			int after_size = LZW.Expand (codeIn, out);

		}
		catch (Exception e)
		{
			throw new IOException("Uncompressing fail! : " + e.toString());
		}
	}
	
	public static byte[] compress(byte[] bytes) throws IOException
	{
		try
		{
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

			CodeOutputPacker codeOut = new CodeOutputPacker (byteArrayOut);

			int before_size = LZW.Compress (bytes, codeOut);
			int after_size = codeOut.getCount ();

			codeOut.flush();
			
			byte[] result = byteArrayOut.toByteArray ();

			return result;
		}
		catch (Exception e)
		{
			throw new IOException("Compressing fail! : " + e.toString());
		}
	}

	public static byte[] uncompress(byte[] bytes) throws CompressException
	{
		try
		{
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

			CodeInputUnpacker codeIn = new CodeInputUnpacker (bytes);
			
			int after_size = LZW.Expand (codeIn, byteArrayOut);

			byte[] result = byteArrayOut.toByteArray ();
			
			return result;
		}
		catch (Exception e)
		{
			throw new CompressException("Uncompressing fail! : " + e.toString());
		}
	}








	public static void main(String[] args) throws Exception
	{
		/*바이트배열 일경우 테스트*/
		{
			byte[] b = "Hello.world! 1234567890 안녕!".getBytes();
		
			System.out.println(new String(b));

			byte[] cb = compress(b);

			//System.out.println(new String(cb));

			byte[] ub = uncompress(cb);
			
			System.out.println(new String(ub));

			System.out.println(b.length + " -> " + cb.length + " -> " + ub.length);
		}

		/*스트림(out)일 경우 테스트*/
		if (args.length >= 3)
		{
			if ( args[0].equals("out") )
			{
				BufferedInputStream bis = new BufferedInputStream (new FileInputStream (new File (args [1])));
				BufferedOutputStream bos = new BufferedOutputStream (new FileOutputStream (new File (args [2])));
				
				long time = System.currentTimeMillis();

				compress(bis, bos);

				time =  System.currentTimeMillis() -time;

				System.out.println("압축하는데 걸린 시간: " + time);
			}

			else if (args[0].equals("in") )
			{
				BufferedInputStream bis = new BufferedInputStream (new FileInputStream (new File (args [1])));
				BufferedOutputStream bos = new BufferedOutputStream (new FileOutputStream (new File (args [2])));
				
				long time = System.currentTimeMillis();

				uncompress(bis, bos);

				time =  System.currentTimeMillis() -time;

				System.out.println("압축푸는데 걸린 시간: " + time);
			}
		}
	}


}