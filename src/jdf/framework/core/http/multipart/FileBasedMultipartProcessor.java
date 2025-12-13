package jdf.framework.core.http.multipart;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBasedMultipartProcessor implements MultipartProcessor
{

	private Map fieldMap = new HashMap();

	private List outputStreamList = new ArrayList();

	private String baseDirectory;

	public FileBasedMultipartProcessor(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	public void intialize(Object obj)
	{
		return;
	}

	/**
	 * 
	 */
	public OutputStream getOutputStream(String fieldName, String filename, String contentType) throws IOException
	{
		String filePath = this.baseDirectory + File.separator + filename;
		OutputStream os = new BufferedOutputStream(new FileOutputStream(filePath));
		outputStreamList.add(os);

		this.fieldMap.put(fieldName, filePath);

		return os;

	}

	public InputStream getInputStream(String fieldName) throws IOException
	{
		String filePath = (String) this.fieldMap.get(fieldName);
		if (filePath == null)
			return null;

		try {
			InputStream is = new BufferedInputStream(new FileInputStream(filePath));

			return is;
		} catch (FileNotFoundException fne) {
			return null;
		}
	}

	public void close()
	{
		for (int i = 0; i < outputStreamList.size(); i++) {
			OutputStream os = (OutputStream) this.outputStreamList.get(i);
			try {
				os.close();
			} catch (IOException ioe) {

			}
		}

	}

}
