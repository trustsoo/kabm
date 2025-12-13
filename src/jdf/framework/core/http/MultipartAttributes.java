/*
 * @(#)MultipartAttributes.java
 *
 *
 * NOTICE !
 * You can not copy or redistribute this code.
 *
 *
 * @author
 */

package jdf.framework.core.http;

import java.io.*;

import javax.servlet.http.HttpServletRequest;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;



/**
 * <p>
 * Multipart로 넘어오는 데이타를 parsing하여 file명,parameter,화일 byte stream 을 가져온다.
 * 
 * </p>
 * 
 * @author
 * @version 1.0
 */

public final class MultipartAttributes extends RequestAttributes
{
	private final static String LOG_ID = "<f:MultipartAttributes> ";

	private static final String NO_VALUE = null; // 값이 없을 경우 공백으로 한다.

	private static final String CNT_TYPE = "__CNTP"; // content type을 찾는 키

	// private static final String FILE_HANDLER_ID = "__FHID"; // content type을 찾는
	// 키

	private static final int DEFAULT_MAX_FILESIZE = 5 * 1000; // killio 단위

	private final static int CR = '\r';

	private final static int LF = '\n';

	private String boundary = null; // boundary

	private static Config conf = null;

	private static String ENCOD_TYPE = "UTF-8";

	static {
		try {
			
			conf = Configuration.lookup("/MultipartAttributes");
			
			ENCOD_TYPE = conf.getString("encoding");
			if (ENCOD_TYPE == null)
				ENCOD_TYPE = "UTF-8";

			
		} catch (Exception e) {
			Logger.err.println(LOG_ID + "get Config err. " + e.toString());
		}
	}

	public MultipartAttributes(HttpServletRequest req) throws Exception {
		super(req);

		process(req);
	}

	/*
     * 아래와 같은 문장을 처리해야 한다. -----------------------------7d111025190406 Content-Disposition: form-data; name="test1"
     * 
     * 13 -----------------------------7d111025190406 Content-Disposition: form-data; name="test2"
     * 
     * val2 -----------------------------7d111025190406 Content-Disposition: form-data; name="upload1";
     * filename="C:\total.txt" Content-Type: text/plain
     * 
     * m74p7wfc8p9ntp7w54fcnw976wwn;9w576m
     */
	
	private void process(HttpServletRequest req) throws Exception
	{
		BufferedInputStream inStream;
		ByteArrayOutputStream tmp;
		ByteArrayOutputStream fileOutput;

		try {
			String key = null;
			String filename = null;

			inStream = new BufferedInputStream(req.getInputStream(), 4096);
			tmp = new ByteArrayOutputStream();

			int maxSize = DEFAULT_MAX_FILESIZE * 1000;

			try {
				maxSize = conf.getInt("maxUploadFileKilloBytes", DEFAULT_MAX_FILESIZE) * 1000;
			} catch (Exception ee) {
			}

			if (maxSize < req.getContentLength())
				throw new IOException("제한된 업로드 화일 사이즈를 넘었습니다. maxsize=" + maxSize);

			// int size = req.getContentLength();
			// 사이즈 제한하자.

			int buffer = 0;

			String type = req.getContentType();
			boundary = extractBoundary(type);

			// System.out.println( " boundary = "+boundary);

			boolean isDispositionLine = false;
			boolean isTypeLine = false;
			// boolean isContentLine = false;
			boolean isR = false;

			int nextBodyRow = 0;

			int seq = 0;

			fileOutput = new ByteArrayOutputStream();

			while (buffer != -1) {
				buffer = inStream.read();

				// System.out.print( (char) buffer );

				if (buffer == CR) {
					if (isR)
						tmp.write(CR);

					isR = true;
				}

				else if (isR && buffer == LF) {
					isR = false;
					String line = new String(tmp.toByteArray(), ENCOD_TYPE);

					if (isTypeLine) // type이 기술된 line인 경우
					{
						if (line.indexOf("Content-Type") == 0) {
							// nextBodyRow = 3;
							_attrs.put(key + CNT_TYPE, line.substring(14, line.length()), seq); // content
							// type
						}
						// else nextBodyRow = 2;

						if (line.trim().length() == 0) {
							nextBodyRow = 2;

							isTypeLine = false;

							fileOutput = new ByteArrayOutputStream();

						}

					}

					if (isDispositionLine) // disposition line인 경우
					{
						// System.out.println( getValue(line,"name") );
						// System.out.println( getValue(line,"filename") );
						key = getValue(line, "name");
						seq = _attrs.getCount(key);

						filename = getValue(line, "filename");

						isDispositionLine = false;
						isTypeLine = true;
					}

					if (line.indexOf(boundary) >= 0) {
						isDispositionLine = true;
						nextBodyRow = 0;

						if (key != null) {
							if (filename != null && filename.length() > 0) // 화일명이
							// 정의되어
							// 있으면
							{

								_attrs.put(key, getFileName(filename), seq); // 변수값에
								// 들어가는
								// 값은
								// 화일명이다.
								_attrs.put(key + boundary, fileOutput.toByteArray(), seq);
								// _attrs.put(key + boundary, fileOutput, seq);
								// 변수값+boundary는 화일의 byte array이 들어간다.

								// System.out.println("저장 =============== ");

							} else {
								// System.out.println(key+"=============================="+fileOutput.toString());
								_attrs.put(key, fileOutput.toString(ENCOD_TYPE), seq); // 일반
								// parameter
							}
						}
					} else if (nextBodyRow < 1) {
						fileOutput.write(CR);
						fileOutput.write(LF);
					}

					if (nextBodyRow <= 1) {
						fileOutput.write(tmp.toByteArray());
						// System.out.print( new String(tmp.toByteArray()));
					}

					--nextBodyRow;

					// System.out.print( line );

					tmp.reset();
				} else {
					if (isR)
						tmp.write(CR);

					isR = false;

					tmp.write(buffer);
				}

			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {
			inStream = null;
			tmp = null;
			fileOutput = null;
		}

	}

	private String extractBoundary(String line)
	{
		int index = line.indexOf("boundary=");
		if (index == -1) {
			return null;
		}
		String boundary = line.substring(index + 9); // 9 for "boundary="

		// The real boundary is always preceeded by an extra "--"
		boundary = "--" + boundary;

		return boundary;
	}

	private String getValue(String content, String fieldName)
	{
		try {
			if (content.indexOf(fieldName) < 0)
				return NO_VALUE;

			int startMark = content.indexOf(fieldName) + fieldName.length() + 2; // fieldName="
			int endMark = content.indexOf("\"", startMark);

			return content.substring(startMark, endMark);
		} catch (Exception ex) {
			return NO_VALUE;
		}
	}

	private String getFileName(String fullname)
	{
		try {

			int startMark = fullname.lastIndexOf("\\");

			if (startMark < fullname.lastIndexOf("/"))
				startMark = fullname.lastIndexOf("/");

			return fullname.substring(startMark + 1, fullname.length());
		} catch (Exception ex) {
			return NO_VALUE;
		}

	}

	public String getContentType(String fieldName, int seq)
	{
		return (String) _attrs.get(fieldName + CNT_TYPE, seq);
	}

	public InputStream getInputStream(String fieldName, int seq)
	{

		InputStream in = new ByteArrayInputStream((byte[]) _attrs.get(fieldName + boundary, seq));

		// _attrs.remove(fieldName + boundary);

		return in;
	}

	public byte[] getByteArray(String fieldName, int seq)
	{
		// return (byte[]) _attrs.get(fieldName + boundary, seq);
		ByteArrayOutputStream bos = (ByteArrayOutputStream) _attrs.get(fieldName + boundary, seq);
		return bos.toByteArray();

	}

}