package jdf.framework.core.http.multipart;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * 
 * @author
 *
 */
public class MemoryBasedMultipartProcessor implements MultipartProcessor
{

	private Map fieldMap = new HashMap();

	public void intialize(Object obj)
	{
		return;
	}

	public OutputStream getOutputStream(String fieldName, String filename, String contentType) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		

		this.fieldMap.put(fieldName, bos);

		return bos;

	}

	public InputStream getInputStream(String fieldName) throws IOException
	{
		ByteArrayOutputStream bos = (ByteArrayOutputStream) this.fieldMap.get(fieldName);
		if (bos == null)
			return null;
		
		return new ByteArrayInputStream(bos.toByteArray());
	}

	public void close()
	{

	}

}
