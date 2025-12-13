/*
 * @(#)MultipartAttributes.java
 *
 * NOTICE !
 * You can not copy or redistribute this code.
 *
 *
 * @author
 */

package jdf.framework.core.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.JavaBeanDataSet;
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

public final class UpgradeMultipartAttributes extends RequestAttributes
{

	private static final String CNT_TYPE = "_UMPP2UXF_CNTP"; // content type을 찾는 키

	private static final int DEFAULT_MAX_FILESIZE = 5 * 1000; // killio 단위

	private static final String CONTENT_TYPE = "Content-type";

	/**
	 * HTTP content disposition header name.
	 */
	private static final String CONTENT_DISPOSITION = "Content-disposition";

	/**
	 * Content-disposition value for form data.
	 */
	private static final String FORM_DATA = "form-data";

	/**
	 * Content-disposition value for file attachment.
	 */
	private static final String ATTACHMENT = "attachment";

	/**
	 * Part of HTTP content type header.
	 */
	private static final String MULTIPART = "multipart/";

	/**
	 * HTTP content type header for multipart forms.
	 */
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";

	/**
	 * HTTP content type header for multiple uploads.
	 */
	private static final String MULTIPART_MIXED = "multipart/mixed";

	/**
	 * The maximum length of a single header line that will be parsed (1024 bytes).
	 */
	private static final int MAX_HEADER_SIZE = 1024;

	private static Config conf = null;

	static {
		try {
			conf = Configuration.lookup("/GeneralServlet");
		} catch (Exception e) {
			Logger.err.println("<MultipartAttributes> get Config err. " + e.toString());
		}
	}

	private HttpServletRequest request;

	public UpgradeMultipartAttributes(HttpServletRequest req) {
		super(req);

		this.request = req;
	}

	public void process(Object obj) throws IOException
	{
		this.process(this.request, obj);
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

	private void process(HttpServletRequest req, Object obj) throws IOException
	{
		if (obj != null) {
			if (obj instanceof DataSet)
				this._attrs = (DataSet) obj;
			else {
				this._attrs = new JavaBeanDataSet(obj);
			}
		}

		String contentType = req.getHeader(CONTENT_TYPE);

		if ((null == contentType) || (!contentType.startsWith(MULTIPART))) {
			throw new IOException("the request doesn't contain a " + MULTIPART_FORM_DATA + " or " + MULTIPART_MIXED
					+ " stream, content type header is " + contentType);

		}

		int requestSize = req.getContentLength();

		if (requestSize == -1) {
			throw new IOException("the request was rejected because it's size is unknown");
		}

		String savePath = this.getProperty(SAVE_PATH);
		int limitSize = this.getPropertyInt(LIMIT_SIZE);
		String headerEncoding = this.getProperty(ENCODING);

		if (savePath == null) {
			savePath = ".";
		}
		if (limitSize < 1) {

			limitSize = conf.getInt("maxUploadFileKilloBytes", DEFAULT_MAX_FILESIZE) * 1000;
		}

		if (limitSize < requestSize)
			throw new IOException("제한된 업로드 화일 사이즈를 넘었습니다. maxsize=" + limitSize);

		int boundaryIndex = contentType.indexOf("boundary=");
		if (boundaryIndex < 0) {
			throw new IOException("the request was rejected because " + "no multipart boundary was found");
		}
		byte[] boundary = contentType.substring(boundaryIndex + 9).getBytes();

		InputStream input = req.getInputStream();

		MultipartStream multi = new MultipartStream(input, boundary);
		if (headerEncoding != null)
			multi.setHeaderEncoding(headerEncoding);

		boolean nextPart = multi.skipPreamble();

		try {
			while (nextPart) {
				Map headers = parseHeaders(multi.readHeaders());
				String fieldName = getFieldName(headers);
				System.out.println("fieldName = " + fieldName);
				System.out.println("headers = " + headers);
				if (fieldName != null) {
					String subContentType = getHeader(headers, CONTENT_TYPE);
					if (subContentType != null && subContentType.startsWith(MULTIPART_MIXED)) {
						// Multiple files.
						byte[] subBoundary = subContentType.substring(subContentType.indexOf("boundary=") + 9)
								.getBytes();
						multi.setBoundary(subBoundary);
						boolean nextSubPart = multi.skipPreamble();
						while (nextSubPart) {
							headers = parseHeaders(multi.readHeaders());

							String fileName = getFileName(headers);

							this.setAttribute(fieldName, fileName);

							// System.out.println("1 fileName = " + fileName);

							if (fileName != null) {

								OutputStream os = this.multipartProcess.getOutputStream(fieldName, fileName,
										subContentType);
								multi.readBodyData(os);

								/*
								 * FileItem item = createItem(headers, false); OutputStream os = item.getOutputStream();
								 * try { multi.readBodyData(os); } finally { os.close(); } items.add(item);
								 */
							} else {
								// Ignore anything but files inside
								// multipart/mixed.
								multi.discardBodyData();
							}
							nextSubPart = multi.readBoundary();
						}
						multi.setBoundary(boundary);
					} else {
						String fileName = getFileName(headers);
						if (fileName != null) {

							// System.out.println("2 fileName = " + fileName);

							OutputStream os = this.multipartProcess
									.getOutputStream(fieldName, fileName, subContentType);
							multi.readBodyData(os);

							this.setAttribute(fieldName, fileName);

							// A single file.
							/**
							 * FileItem item = createItem(headers, false); OutputStream os = item.getOutputStream(); try {
							 * multi.readBodyData(os); } finally { os.close(); } items.add(item);
							 */

						} else {
							// A form field.
							/**
							 * FileItem item = createItem(headers, true); OutputStream os = item.getOutputStream(); try {
							 * multi.readBodyData(os); } finally { os.close(); } items.add(item);
							 */
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							multi.readBodyData(bos);

							// 일반 form field 값 세팅
							this.setAttribute(fieldName, new String(bos.toByteArray()));
							// System.out.println( new String(bos.toByteArray()) );
						}
					}
				} else {
					// Skip this part.
					multi.discardBodyData();
				}
				nextPart = multi.readBoundary();
			}
		} finally {
			if(this.multipartProcess!=null)
				this.multipartProcess.close();

		}

	}

	/**
	 * 
	 * @param headerPart
	 * @return
	 */
	protected Map /* String, String */parseHeaders(String headerPart)
	{
		Map headers = new HashMap();
		char buffer[] = new char[MAX_HEADER_SIZE];
		boolean done = false;
		int j = 0;
		int i;
		String header, headerName, headerValue;
		try {
			while (!done) {
				i = 0;
				// Copy a single line of characters into the buffer,
				// omitting trailing CRLF.
				while (i < 2 || buffer[i - 2] != '\r' || buffer[i - 1] != '\n') {
					buffer[i++] = headerPart.charAt(j++);
				}
				header = new String(buffer, 0, i - 2);
				if (header.equals("")) {
					done = true;
				} else {
					if (header.indexOf(':') == -1) {
						// This header line is malformed, skip it.
						continue;
					}
					headerName = header.substring(0, header.indexOf(':')).trim().toLowerCase();
					headerValue = header.substring(header.indexOf(':') + 1).trim();
					if (getHeader(headers, headerName) != null) {
						// More that one heder of that name exists,
						// append to the list.
						headers.put(headerName, getHeader(headers, headerName) + ',' + headerValue);
					} else {
						headers.put(headerName, headerValue);
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// Headers were malformed. continue with all that was
			// parsed.
		}
		return headers;

	}

	/**
	 * 
	 * @param headers
	 * @return
	 */
	protected String getFieldName(Map /* String, String */headers)
	{
		String fieldName = null;
		String cd = getHeader(headers, CONTENT_DISPOSITION);
		if (cd != null && cd.startsWith(FORM_DATA)) {
			int start = cd.indexOf("name=\"");
			int end = cd.indexOf('"', start + 6);
			if (start != -1 && end != -1) {
				fieldName = cd.substring(start + 6, end);
			}
		}
		return fieldName;
	}

	/**
	 * 
	 * @param headers
	 * @param name
	 * @return
	 */
	protected final String getHeader(Map headers, String name)
	{
		return (String) headers.get(name.toLowerCase());
	}

	public String getContentType(String fieldName)
	{
		return (String) _attrs.get(fieldName + CNT_TYPE);
	}

	/**
	 * 
	 * @param headers
	 * @return
	 */
	protected String getFileName(Map /* String, String */headers)
	{
		String fileName = null;
		String cd = getHeader(headers, CONTENT_DISPOSITION);
		if (cd.startsWith(FORM_DATA) || cd.startsWith(ATTACHMENT)) {
			int start = cd.indexOf("filename=\"");
			int end = cd.indexOf('"', start + 10);
			if (start != -1 && end != -1) {
				fileName = cd.substring(start + 10, end).trim();
			}
		}
		if (fileName != null) {
			int p = fileName.lastIndexOf("\\");
			if (p > 0)
				fileName = fileName.substring(p + 1);
			p = fileName.lastIndexOf("/");
			if (p > 0)
				fileName = fileName.substring(p + 1);
		}
		return fileName;
	}

	/**
	 * 
	 */
	public InputStream getInputStream(String fieldName) throws IOException
	{
		return this.multipartProcess.getInputStream(fieldName);
	}

}
