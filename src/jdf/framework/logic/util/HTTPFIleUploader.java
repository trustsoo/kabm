package jdf.framework.logic.util;

import java.io.*;
import java.net.*;

/**
 * 멀티파트 방식을 통하여 데이터를 업로드 한다.
 * 일반적인 <B>변수=값&변수2=값</B>의 형태가 아니라
 * Cotent-disposition: form-data; 변수="값"
 * ---------------------------7c226a700d0
 * 형태이다.
 *
 * @author
 * @version $Id: HTTPFIleUploader.java,v 1.1.1.1 2005/01/30 13:43:48 Exp $, since 2003/08/07
 */
class HTTPFIleUploader 
{
	private static final int BUFF_SIZE = 1024;
	
	/**
	 * HTTP Post 방식으로 매개변수 전송을 해주는 메소드
	 */
	private static void writeParam(String name, String value, DataOutputStream out, String boundary)
		throws IOException
	{
		out.writeBytes("content-disposition: form-data; name=\"" + name + "\"\r\n\r\n");
		out.writeBytes(value);
		out.writeBytes("\r\n" + "--" + boundary +"\r\n");
	}
	/**
	 * HTTP Post 방식으로 파일 전송을 해주는 메소드
	 */
	private static void writeFile(String name, String filePath, DataOutputStream out, String boundary)
		throws IOException
	{
		out.writeBytes("content-disposition: form-data; name=\"" + name + "\"; filename=\"" + filePath + "\"\r\n");
		out.writeBytes("content-type: application/octet-stream" + "\r\n\r\n");

		FileInputStream fis = new FileInputStream(filePath);
		
		byte[] buffer = new byte[BUFF_SIZE];
		
		while (true)
		{
			synchronized (buffer)
			{
				int amountRead = fis.read(buffer);
				if (amountRead == -1)
					break;
				out.write(buffer, 0, amountRead);
			}
		}
		fis.close();

		out.writeBytes("\r\n" + "--" + boundary + "\r\n");
	}
	 
	public static void main(String[] args) throws Exception 
	{
		URL url = new URL("http://127.0.0.1/upload.jsp");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);

		String boundary = "-----------------------7c226a700d0";

		conn.setRequestProperty("Content-type", "multipart/form-data; boundary=" + boundary);
		conn.setRequestProperty("Referer", "http://127.0.0.1/index.jsp");
		conn.setRequestProperty("Cache-Control", "no-cache");

		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes("--" + boundary + "\r\n");
		writeParam("name", "test", out, boundary);
		writeFile("fileName", "C:\\tmpfoo.txt", out, boundary);
		out.flush();
		out.close();

		BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
		int i = 0;
		while ((i = in.read()) != -1)
		{
			System.out.write(i);
		}
		in.close();

	}
}