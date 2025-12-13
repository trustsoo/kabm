package jdf.framework.core.log;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.io.MultiOutputStream;



/**
 * @(#) NormalLoggerWriter.java
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team,
 * Application Infrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 *
 * NOTICE !      You can copy or redistribute this code freely,
 * but you should not remove the information about the copyright notice
 * and the author.
 *
 * @author  WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 * @author  SuKyung Lim, sukyunglim@lgeds.lg.co.kr.
 * 1999.12.23. 등록 회원/비등록 회원의 로그를 위한 필드 추가(REG/UNREG)
 * 2000.01.20. 등록 회원/비등록 회원의 로그를 위한 필드를 삭제하고,
 *             com.kdb.ih.common.jdf.UserNormalLoggerWriter 클래스에서 담당
 */

/**
 * Print formatted representations of objects to a text-file output stream. This class implements all of the print
 * methods found in PrintStream. It does not contain methods for writing raw bytes, for which a program should use
 * unencoded byte streams.
 * 
 * <p>
 * Unlike the PrintStream class, if automatic flushing is enabled by setting in the configuration file (
 * jdf.framework.core.logger.autoflush=true ), it will be done only when one of the println() methods is invoked, rather than
 * whenever a newline character happens to be output. The println() methods use the platform's own notion of line
 * separator rather than the newline character.
 * 
 * <p>
 * Methods in this class never throw I/O exceptions. The client may inquire as to whether any errors have occurred by
 * invoking checkError().
 * 
 */

public class NormalLoggerWriter implements LoggerWriter
{

	/**
     * The PrintWriter Agent for real file logging. 이 PrintWriter 객체가 실제 파일에 기록을 담당한다. Logger.sys, Logger.err,
     * Logger.warn, Logger.info, Logger.debug 등 상이한 Reference를 통해 println() 이 호출되더라도 실제는 하나의 파일에 기록이 일어나므로 물리적인 파일 기록은
     * 하나의 PrintWriter가 담당할 수 밖에 없다.
     */

	private PrintStream writer = null;

	/**
     * synchronized lock object
     */
	private final static Object lock = new Object();

	/**
     * The name of log file today.
     */
	// private String today = null;
	/**
     * The indication of new line character is printed.
     */
	private boolean newLined = true;

	/**
     * "SYS", "ERR", "WARN", "INFO", "DEBUG"
     */
	// private int mode = DEBUG; // Default Log MODE is DEBUG
	/**
     * Line separator string. This is the value of the line.separator property at the moment that the stream was
     * created.
     */

	private Config conf;

	private String sMode = "<I>";

	private boolean autoflush = true;

	// 로그가 기록되는 디렉토리
	private String logWriteDirectory;

	/*
     * public NormalLoggerWriter(int mode, String logDir, boolean isConsoleOutput) { this.mode=mode;
     * 
     * this.sMode = getModeString(mode);
     * 
     * 
     * this.logDir=logDir; this.isConsoleOutput=isConsoleOutput;
     * 
     * synchronized(lock) { checkDate(); } }
     */

	private String serverId = "";

	public NormalLoggerWriter(PrintStream ps, boolean autoflush, Config conf, String mode) {
		sMode = mode;

		this.writer = ps;
		this.conf = conf;

		this.autoflush = autoflush;
		this.serverId = LoggerFactory.getServerId();
		
	}

	private MultiOutputStream mout;

	public NormalLoggerWriter(MultiOutputStream mout, boolean autoflush, Config conf, String mode) {

		this(new PrintStream(mout), autoflush, conf, mode);

		this.mout = mout;
	}

	public OutputStream getMultiOutputStream()
	{
		return this.mout;
	}

	public void addOutputStream(OutputStream out) throws Exception
	{
		if (mout == null)
			throw new Exception("This LoggerWriter is not support.");

		mout.addOutputStream(out);
	}

	public void removeOutputStream(OutputStream out) throws Exception
	{
		if (mout == null)
			throw new Exception("This LoggerWriter is not support.");

		mout.removeOutputStream(out);
	}

	private boolean isFileWriteError = false;

	/**
	 * 
	 */
	protected void finalize()
	{
		try {
			if (writer != null)
				writer.close();
		} catch (Exception e) {
		}
	}

	/** Flush the stream. */
	public void flush()
	{
		if (isPrintMode())
			writer.flush();
	}

	/**
     * Get the pre-defined Object's information. You must be implement this method.
     * 
     * @return java.lang.String
     * @param o
     *            java.lang.Object
     */
	protected String getPrefixInfo(Object o)
	{
		StringBuffer info = new StringBuffer();

		String className = o.getClass().getName();

		if (o == null) {
			info.append("null");
		} else if (o instanceof javax.servlet.http.HttpServletRequest) {
			javax.servlet.http.HttpServletRequest req = (javax.servlet.http.HttpServletRequest) o;
			info.append(req.getRequestURI() + ",");
			String user = req.getRemoteUser();
			if (user != null)
				info.append(user + ",");
			info.append(req.getRemoteAddr());
		}
		// 서블릿 클래스인 경우
		else if (o instanceof javax.servlet.http.HttpServlet) {
			info.append("Servlet ");
			info.append(className);
		}
		// EJB 클래스인 경우
		else if (o instanceof javax.ejb.EnterpriseBean) {
			info.append("EJB ");
			info.append(className);
		}
		// DBWrapper 클래스는 주로 static 메쏘드로 사용하므로,생략
		else {
			info.append("other ");
			info.append(className);
		}
		info.append(' ');
		return info.toString();
	}

	boolean isPrintable = true;
	boolean isPrintDB = false;
	private long lastChkTime = 0;

	/**
     * @return boolean
     */
	public boolean isPrintMode()
	{
		if (this.conf == null)
			return true;

		long now = System.currentTimeMillis();

		if (1000 < (now - lastChkTime)) {
			lastChkTime = now;
			isPrintable = conf.getBoolean("trace", true);
			isPrintDB = conf.getBoolean("db-trace", false);
		}
		return isPrintable;
	}
	
	public boolean isPrintDBMode()
	{
		if (this.conf == null)
			return false;
		
		long now = System.currentTimeMillis();

		if (1000 < (now - lastChkTime)) {
			lastChkTime = now;
			isPrintable = conf.getBoolean("trace", true);
			isPrintDB = conf.getBoolean("db-trace", false);
		}
		return isPrintDB;
		
	}

	/**
     * 특별한 목적을 위해 사용한다. 즉 두개의 변수중 첫번째 변수가 action을 의미하며, 뒷 부분은 저장할 내용이다.
     * 
     * 
     */
	/*
     * public void println(String key, String x) {
     * 
     * 
     * 
     * if ( ! isPrintMode() ) return;
     * 
     * PrintWriter writer = getPrintWriter(key); synchronized (writer) { if ( newLined ) printTime(); writer.print(key);
     * writer.print(" "); writer.println(x); endLine(); }
     * 
     * if( cWriter !=null) { synchronized(cWriter) { printTime(cWriter); cWriter.write(key); cWriter.write(" ");
     * cWriter.write(x); } } }
     */

	/** Print an array of chracters. */
	public void print(char x[])
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(x);			
			endLine(false);
			writeDB(new String(x));
		}

	}

	/** Print a character. */
	public void print(char x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(x);			
			endLine(false);
			writeDB(new String(new char[]{x}));
		}
	}

	/** Print a double. */
	public void print(double x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(x);			
			endLine(false);
			writeDB(x+"");
		}

	}

	/** Print a float. */
	public void print(float x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(x);			
			endLine(false);
			writeDB(x+"");
		}
	}

	/** Print an integer. */
	public void print(int x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(x);		
			endLine(false);
			writeDB(x+"");
		}
	}

	/** Print a long. */
	public void print(long x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(x);			
			endLine(false);
			writeDB(x+"");
		}
	}

	/** Print an object. */
	public void print(Object x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(x);			
			endLine(false);
			writeDB(x.toString());
		}
	}

	/**
     * Print predefined information. Print the predefined information of Object p, and then the string of message Object
     * p.
     */
	public void print(Object p, Object x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(getPrefixInfo(p));
			writer.print(x);			
			endLine(false);
			writeDB(getPrefixInfo(p)+x);
		}
	}

	/** Print a String. */
	public void print(String x)
	{

		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(x);			
			endLine(false);
			writeDB(x);
		}
	}

	/**
     * 시간을 찍을 지 결정
     * 
     */
	public void print(String x, boolean printTime)
	{

		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined && printTime)
				printTime();
			writer.print(x);			
			endLine(false);
			writeDB(x);
		}
	}

	/** Print a boolean. */
	public void print(boolean x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(x);
			endLine(false);
			writeDB(new Boolean(x).toString());
		}
	}

	/** Finish the line. */
	public void println()
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.println();
			endLine(true);
		}
	}

	/** Print an array of characters, and then finish the line. */
	public void println(char x[])
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.println(x);			
			endLine(true);
			writeDB(new String(x));
		}
	}

	/** Print a character, and then finish the line. */
	public void println(char x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.println(x);
			endLine(true);
			writeDB(new String(new char[]{x}));
		}
	}

	/** Print a double, and then finish the line. */
	public void println(double x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.println(x);
			endLine(true);
			writeDB(x+"");
		}
	}

	/** Print a float, and then finish the line. */
	public void println(float x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.println(x);
			endLine(true);
			writeDB(x+"");
		}
	}

	/** Print an integer, and then finish the line. */
	public void println(int x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.println(x);
			endLine(true);
			writeDB(x+"");
		}
	}

	/** Print a long, and then finish the line. */
	public void println(long x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.println(x);
			endLine(true);
			writeDB(x+"");
		}
	}

	/** Print an Object, and then finish the line. */
	public void println(Object x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.println(x);
			endLine(true);
			writeDB(x.toString());
		}
	}

	/**
     * Print predefined information. Print the predefined information of Object p, and then the string of message Object
     * p.
     */
	public void println(String p, String x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();

			StringBuffer str = new StringBuffer();
			str.append("<ID:").append(p).append("> ").append(x);
			writer.println(str.toString());

			/*
             * writer.print(" <ID"+p+"> "); writer.println(x);
             */

			endLine(true);
			
			writeDB(str.toString());
		}
	}

	public void println(String p, Throwable err)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();

			writer.println(p);

			boolean trace = false;
			if (conf != null)
				trace = conf.getBoolean("error-trace", false);

			String errMsg = null;

			if (trace) {
				StringWriter strWriter = new StringWriter();
				PrintWriter printwriter = new PrintWriter(strWriter);
				err.printStackTrace(printwriter);

				errMsg = strWriter.toString();
			} else
				errMsg = err.toString();

			writer.println(errMsg);
			writeDB(errMsg);
			endLine(true);
		}
	}

	public void println(Object p, Object x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(getPrefixInfo(p));
			writer.print(x);
			endLine(true);
			writeDB(getPrefixInfo(p)+x.toString());
		}
	}

	/** Print a String, and then finish the line. */
	public void println(String x)
	{
		if (!isPrintMode())
			return;

		String time = "";
		if (newLined)
			time = getPrintTime();

		writer.println(time + x);		
		
		/*
         * synchronized (this) { writer.print(time); writer.println(x); }
         */
		endLine(true);
		writeDB(x);

	}

	public void println(String x, boolean printTime)
	{
		if (!isPrintMode())
			return;

		synchronized (lock) {
			if (newLined && printTime)
				printTime();
			writer.println(x);
			endLine(true);
			writeDB(x);
		}

	}

	/** Print a boolean, and then finish the line. */
	public void println(boolean x)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.println(x);
			endLine(true);
			writeDB(new Boolean(x).toString());
		}
		
	}

	/**
     * 가령,
     * 
     * 
     */

	protected void performDivLogic(String key, String x)
	{

		print(key + " | " + x);
	}

	protected String getModeString(int mode)
	{
		String serverty = " ";
		switch (mode) {
		case SYS:
			serverty = MARK_SYS;
			break;
		case ERR:
			serverty = MARK_ERR;
			break;
		case WARN:
			serverty = MARK_WARN;
			break;
		case INFO:
			serverty = MARK_INFO;
			break;
		case DEBUG:
			serverty = MARK_DEBUG;
			break;

		case USER:
			serverty = MARK_USER;
			break;

		}

		return serverty;
	}

	/**
     * print current time
     */
	protected void printTime()
	{

		if (!isFileWriteError) {

			// writer.print(DateTime.getTimeStampString());
			writer.print(form.format(new java.util.Date()));
			writer.print(" ");
			// thread의 hashcode 를 출력하여 thread 별 로그추적 가능하도록 함.
			writer.print(Integer.toHexString(Thread.currentThread().hashCode()));
			writer.print(" ");
			writer.print(serverId);
			writer.print(" ");
			writer.print(sMode);
			writer.print(" ");
		}

		// writer.print(DateTime.getTimeString()+serverty) ;
	}

	private String getPrintTime()
	{
		if (!isFileWriteError) {
			StringBuffer buf = new StringBuffer();

			// writer.print(DateTime.getTimeStampString());
			buf.append(form.format(new java.util.Date()));
			buf.append(" ");
			buf.append(Integer.toHexString(Thread.currentThread().hashCode()));
			buf.append(" ");
			buf.append(serverId);
			buf.append(" ");
			buf.append(sMode);
			buf.append(" ");

			return buf.toString();
		} else
			return "";

	}

	private static java.text.SimpleDateFormat form;

	static {
		String format = Logger.DEFAULT_LOG_TIME_FORMAT;

		try {
			Config conf = Configuration.lookup("/logger");

			format = conf.getString("dateFormat", Logger.DEFAULT_LOG_TIME_FORMAT);
		} catch (ConfigurationException e) {
		}

		form = new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
	}

	/**
     * Write an array of characters. This method cannot be inherited from the Writer class because it must suppress I/O
     * exceptions.
     */
	public void write(char buf[])
	{
		write(buf, 0, buf.length);
	}

	/** Write a portion of an array of characters. */
	public void write(char buf[], int off, int len)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(new String(buf, off, len));
			endLine(false);
			writeDB(new String(buf, off, len));
		}
	}

	/*
     * Exception-catching, synchronized output operations, which also implement the write() methods of Writer
     */

	/** Write a single character. */
	public void write(int c)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(c);			
			endLine(false);
			writeDB(c+"");
		}
	}

	/**
     * Write a string. This method cannot be inherited from the Writer class because it must suppress I/O exceptions.
     */
	public void write(String s)
	{
		write(s, 0, s.length());
	}

	/** Write a portion of a string. */
	public void write(String s, int off, int len)
	{
		if (!isPrintMode())
			return;
		synchronized (lock) {
			if (newLined)
				printTime();
			writer.print(s.substring(off, len));			
			endLine(false);
			writeDB(s.substring(off, len));
		}
	}

	private void endLine(boolean newLine)
	{
		newLined = newLine;

		if (!this.autoflush) {
			this.writer.flush();

		}

	}

	/**
     * @see jdf.framework.core.log.LoggerWriter#getDirectoryName()
     */
	public String getDirectoryName()
	{
		return this.logWriteDirectory;
	}

	/**
     * 
     * @see jdf.framework.core.log.LoggerWriter#setDirectoryName(java.lang.String)
     */
	public void setDirectoryName(String dir)
	{
		this.logWriteDirectory = dir;

	}
	
	public void writeDB(String content)
	{
		if(isPrintDBMode())
			DBLogWriter.write(Integer.toHexString(Thread.currentThread().hashCode()), serverId, sMode, content);		
	}

}