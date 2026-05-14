package jdf.framework.view.layout.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * BufferTextStream
 * 
 * <p>
 * ServletOutputStream을 확장하여, Jsp /servlet의 output을 실제 socket output stream으로
 * 출력하지 않고, 메모리 상에서 buffering 하는 역활을 한다.
 * </p>
 * 
 * 특이 기능으로 html을 compact 하게 줄이는 기능과 HTTP Compression 기능을 지원한다.
 * 
 * @author
 * @version 1.0 2003/03/01
 * 
 * @see ServletOutputStream
 * 
 */

public class ByteArrayServletResponse extends HttpServletResponseWrapper {
	private String contentType = null;

	private ByteArrayPrintWriter pw;

	// cookie 정보 리스트
	private java.util.List cList = new java.util.ArrayList();

	// header 정보 리스트
	private java.util.List hList = new java.util.ArrayList();

	// setStatus 메쏘드가 호출됐는지 여부
	private boolean isSetStatus = false;

	public ByteArrayServletResponse(HttpServletResponse resp) {
		super(resp);
		pw = new ByteArrayPrintWriter(false, false, null);
	}

	public ByteArrayServletResponse(HttpServletResponse resp,
			boolean html_compact, String charSet) {
		super(resp);
		pw = new ByteArrayPrintWriter(html_compact, false,charSet);

	}

	public ByteArrayServletResponse(HttpServletResponse resp,
			boolean html_compact, boolean httpCompress, String charSet) {
		super(resp);
		pw = new ByteArrayPrintWriter(html_compact, httpCompress, charSet);

	}

	public PrintWriter getWriter() {
		// System.out.println("getWriter ByteArrayServletResponse");
		return pw.getWriter();
	}

	public ServletOutputStream getOutputStream() {
		// System.out.println("getOutputStream ByteArrayServletResponse");
		return pw.getStream();
	}

	public void flush() throws IOException {
		// System.out.println("flush");
		pw.getWriter().flush();
		pw.getStream().flush();
		return;
	}

	/**
	 * @see javax.servlet.ServletResponse#flushBuffer()
	 */
	public void flushBuffer() throws IOException {
		// System.out.println("flushBuffer");
		pw.getWriter().flush();
		pw.getStream().flush();
		return;
	}

	public void addCookie(Cookie cookie) {
		cList.add(cookie);
		// super.addCookie(cookie);

	}

	public void copy(HttpServletResponse resp) {
		// cookie 값 이전

		for (int i = 0; i < cList.size(); i++) {
			resp.addCookie((Cookie) cList.get(i));
		}

		// header 값 이전

		for (int i = 0; i < hList.size(); i++) {
			HeaderInfo info = (HeaderInfo) hList.get(i);
			if (info.getMode() == HeaderInfo.MODE_SET)
				resp.setHeader(info.getKey(), info.getValue());
			else
				resp.addHeader(info.getKey(), info.getValue());
		}

	}

	public void addHeader(String name, String value) {
		hList.add(new HeaderInfo(HeaderInfo.MODE_ADD, name, value));
		// super.addHeader(name, value);
	}

	public void setHeader(String name, String value) {
		// Tomcat에서 header에 ETag 나 Last-Modifed가 setting된후 이상징후 발생
		if ("ETag".equals(name) || "Last-Modified".equals(name))
			return;

		hList.add(new HeaderInfo(HeaderInfo.MODE_SET, name, value));
		// super.setHeader(name, value);
	}

	public void reset() {
		// System.out.println("reset ** ");
	}

	public String toString() {
		return pw.toString();
	}

	public String toString(String enc) throws UnsupportedEncodingException {
		return pw.toString(enc);
	}

	public byte[] toByteArray() {
		return pw.toByteArray();
	}

	public PrintWriter getPrintWriter() {
		return this.pw.getPrintWriter();
	}

	private static class ByteArrayServletStream extends ServletOutputStream {
		OutputStream baos;

		ByteArrayServletStream(OutputStream baos) {
			this.baos = baos;
		}

		public void write(int param) throws IOException {
			// System.out.print(param);
			baos.write(param);
		}

		/**
		 * @see ServletOutputStream#println(String)
		 */
		public void println(String arg0) throws IOException {
			System.out.println(arg0);
			super.println(arg0);
		}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {
			// TODO Auto-generated method stub
			
		}

	}

	/**
	 * 
	 * @author
	 * 
	 */
	private final static class HeaderInfo {
		final static int MODE_SET = 0;

		final static int MODE_ADD = 1;

		private int mode = 0;

		private String key;

		private String value;

		public HeaderInfo(int mode, String key, String value) {
			this.mode = mode;
			this.key = key;
			this.value = value;
		}

		public int getMode() {
			return this.mode;
		}

		public String getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}

	}

	private static class ByteArrayPrintWriter {
		private ByteArrayOutputStream baos;

		private OutputStream hos;

		private PrintWriter pw;

		private ServletOutputStream sos;

		private GZIPOutputStream gzipout;

		ByteArrayPrintWriter() {
			this(false, false, null);
		}

		ByteArrayPrintWriter(boolean compact, boolean httpComrpess,
				String charsetName)

		{
			baos = new ByteArrayOutputStream();

			if (compact) {
				hos = new HtmlCompactOutputStream(baos);
			} else
				hos = baos;

			if (httpComrpess) {

				OutputStream tmp = hos;

				try {

					gzipout = new GZIPOutputStream(hos);
					hos = gzipout;

				} catch (IOException ioe) {
					hos = tmp;
					System.err.println(ioe.getMessage());
				}

			}

			try {
				pw = new PrintWriter(new OutputStreamWriter(hos, charsetName));
			} catch (UnsupportedEncodingException use) {
				pw = new PrintWriter(hos);
			}
			sos = new ByteArrayServletStream(hos);
		}

		public PrintWriter getWriter() {
			return pw;
		}

		public ServletOutputStream getStream() {
			return sos;
		}

		public byte[] toByteArray() {
			if (gzipout != null) {
				try {

					gzipout.finish();
					gzipout.flush();
					gzipout.close();

				} catch (IOException ioe) {
					System.err.println(ioe.getMessage());
				}

				gzipout = null;
			}

			return baos.toByteArray();
		}

		public String toString() {
			return baos.toString();
		}

		public String toString(String enc) throws UnsupportedEncodingException {
			return baos.toString(enc);
		}

		public PrintWriter getPrintWriter() {
			return this.pw;
		}

	}

	/**
	 * 
	 * @see javax.servlet.ServletResponse#setContentLength(int)
	 */
	public void setContentLength(int arg0) {
		// super.setContentLength(arg0);
	}

	/**
	 * 
	 * @see javax.servlet.ServletResponse#setContentType(String)
	 */
	public void setContentType(String arg0) {
		super.setContentType(arg0);

		this.contentType = arg0;
	}

	public String getContentType() {
		return this.contentType;
	}

	/**
	 * setStatus 메쏘드가 호출되었는지 여부판단.
	 * 
	 * @return
	 */
	public boolean isSetStatus() {
		return this.isSetStatus;
	}

	/**
	 * 
	 * @see HttpServletResponse#setStatus(int)
	 */
	public void setStatus(int code) {
		this.isSetStatus = true;
		super.setStatus(code);
	}

	private boolean isRedirect = false;

	/**
	 * 
	 * @see HttpServletResponseWrapper#sendRedirect(String)
	 */
	public void sendRedirect(String arg0) throws IOException {
		this.isRedirect = true;
		super.sendRedirect(arg0);
	}

	public boolean isRedirectMode() {
		return this.isRedirect;
	}

	/**
	 * @see HttpServletResponse#setStatus(int,
	 *      String)
	 */
	/*
	 * public void setStatus(int code, String desc) { this.isSetStatus = true;
	 * super.setStatus(code, desc); }
	 */

}