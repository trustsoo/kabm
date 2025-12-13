package jdf.framework.core.log;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import jdf.framework.core.Config;
import jdf.framework.core.io.MultiOutputStream;



/**
 * <p>
 * 로그기록을 원하는 출력소로 비동기적으로 출력한다.
 * 
 * @author
 * @version 1.0
 */

public final class AsyncLoggerWriter implements LoggerWriter
{

	private AsyncLogWriter writer = null;

	/**
     * "SYS", "ERR", "WARN", "INFO", "DEBUG"
     */
	// private int mode = DEBUG; // Default Log MODE is DEBUG
	private String sMode; // 문자로 표현되는 mode
	
	private Config conf = null; // config

	private String serverId = "";

	private String logWriteDirectory;

	/*
     * public AsyncLoggerWriter(int mode, PrintStream ps) { //System.out.println("########## init "+mode+"
     * dir:"+logDir); this.mode = mode;
     * 
     * try { sMode = getDescript(mode);
     * 
     * conf = getConfig(mode);
     * 
     * writer = new AsyncLogWriter(ps); } catch (Exception e) { throw new IllegalArgumentException(); } }
     */

	/**
     * 기본 생성자
     * 
     */
	public AsyncLoggerWriter(PrintStream ps, Config conf, String mode) 
	{	
		this.conf = conf;				
		this.serverId = LoggerFactory.getServerId();
		this.sMode = mode;
		this.writer = new AsyncLogWriter(ps);
		//System.out.println(mode+":"+isPrintDBMode());

	}

	private MultiOutputStream mout;

	/**
     * 기본 생성자
     * 
     * @param mout
     * @param conf
     * @param mode
     */
	public AsyncLoggerWriter(MultiOutputStream mout, Config conf, String mode) {

		this(new PrintStream(mout), conf, mode);

		this.mout = mout;
	}

	/**
     * 로그의 OutputStream 객체를 반환
     * 
     */
	public OutputStream getMultiOutputStream()
	{
		return this.mout;
	}

	/**
     * OutputStream 객체를 추가한다.
     * 
     */
	public void addOutputStream(OutputStream out) throws Exception
	{
		if (mout == null)
			throw new Exception("This LoggerWriter is not support.");

		mout.addOutputStream(out);
	}

	/**
     * OutputStream 객체를 제거한다.
     * 
     */
	public void removeOutputStream(OutputStream out) throws Exception
	{
		if (mout == null)
			throw new Exception("This LoggerWriter is not support.");

		mout.removeOutputStream(out);
	}

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

	/*
     * 매번 config를 읽어 출력유무를 판단하면 속도에 문제가 있다고 생각되므로, 프로그램은 지저분해지지만, 속도를 위해서라면...
     */

	long lastUpdateTime = 0; // 마지막 수정된 시간 ( long형태)

	long checkTimeGap = 2000; // Config를 Check할 시간간격

	boolean isPrintable = false; // 
	boolean isPrintDB = false;
	/**
     * @return boolean
     */
	public boolean isPrintMode()
	{
		if (conf == null)
			return true;

		long time = System.currentTimeMillis();

		if (time - lastUpdateTime < checkTimeGap) {

			return isPrintable;
		}

		lastUpdateTime = time;

		// boolean isPrintable = true;
		isPrintable = conf.getBoolean("trace", true);
		isPrintDB = conf.getBoolean("db-trace", false);

		return isPrintable;
	}

	
	public boolean isPrintDBMode()
	{		
		
		if (conf == null)
			return false;

		long time = System.currentTimeMillis();

		if (time - lastUpdateTime < checkTimeGap) {

			return isPrintDB;
		}

		lastUpdateTime = time;

		// boolean isPrintable = true;
		isPrintable = conf.getBoolean("trace", true);
		isPrintDB = conf.getBoolean("db-trace", false);

		return isPrintDB;
	}
		
	/**
     * 특별한 목적을 위해 사용한다. 즉 두개의 변수중 첫번째 변수가 action을 의미하며, 뒷 부분은 저장할 내용이다.
     * 
     * 
     */
	/*
     * public void println(String key, String x) { if ( ! isPrintMode() ) return;
     * 
     * AsyncLogWriter writer = getPrintWriter(key);
     * 
     * synchronized (writer) { writer.write(getDescript(),x); } }
     */

	public void println(String key, String x)
	{
		if (!isPrintMode())
			return;

		// writer.write(getDescript(), "<ID:"+key+"> "+x);

		StringBuffer str = new StringBuffer();
		str.append("<ID:").append(key).append("> ").append(x);

		writer.write(getSMode(), getServerId(), str.toString(), isPrintDBMode());
		
	}

	/** Print an array of chracters. */
	public void print(char x[])
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), new String(x), isPrintDBMode());

	}

	/** Print a character. */
	public void print(char x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Print a double. */
	public void print(double x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());
	}

	/** Print a float. */
	public void print(float x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Print an integer. */
	public void print(int x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Print a long. */
	public void print(long x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());
	}

	/** Print an object. */
	public void print(Object x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/**
     * Print predefined information. Print the predefined information of Object p, and then the string of message Object
     * p.
     */
	/*
     * public void print(Object p, Object x) { if ( ! isPrintMode() ) return;
     * 
     * writer.write(getDescript()+getPrefixInfo(p), String.valueOf(x)); }
     */

	public void print(String key, String x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), "<ID:" + key + "> " + x, isPrintDBMode());
	}

	public void print(String x, boolean printTime)
	{
		if (!isPrintMode())
			return;

		if (printTime)
			writer.write(getSMode(), getServerId(), x, isPrintDBMode());
		else
			writer.write("", "", x, isPrintDBMode());
	}

	public void println(String desc, Throwable err)
	{
		if (!isPrintMode())
			return;

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

		writer.write(getSMode(), getServerId(), desc + File.separator + errMsg, isPrintDBMode());
	}

	/** Print a String. */
	public void print(String x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), x, isPrintDBMode());

	}

	/** Print a boolean. */
	public void print(boolean x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Finish the line. */
	public void println()
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), "", isPrintDBMode());

	}

	/** Print an array of characters, and then finish the line. */
	public void println(char x[])
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Print a character, and then finish the line. */
	public void println(char x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Print a double, and then finish the line. */
	public void println(double x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Print a float, and then finish the line. */
	public void println(float x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Print an integer, and then finish the line. */
	public void println(int x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Print a long, and then finish the line. */
	public void println(long x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());

	}

	/** Print an Object, and then finish the line. */
	public void println(Object x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());
	}

	/**
     * Print predefined information. Print the predefined information of Object p, and then the string of message Object
     * p.
     */
	public void println(Object p, Object x)
	{

		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId() + getPrefixInfo(p), String.valueOf(x), isPrintDBMode());
	}

	/** Print a String, and then finish the line. */
	public void println(String x)
	{
		if (!isPrintMode())
			return;

		// System.out.println(x);

		writer.write(getSMode(), getServerId(), x, isPrintDBMode());
	}

	public void println(String x, boolean printTime)
	{
		if (!isPrintMode())
			return;

		// System.out.println(x);

		if (printTime)
			writer.write(getSMode(), getServerId(), x, isPrintDBMode());
		else
			writer.write("", "", x, isPrintDBMode());
	}

	/** Print a boolean, and then finish the line. */
	public void println(boolean x)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(x), isPrintDBMode());
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

	private String getSMode()
	{
		return sMode;
	}
	
	private String getServerId()
	{
		return serverId;
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

		writer.write(getSMode(), getServerId(), new String(buf, off, len), isPrintDBMode());
	}

	/*
     * Exception-catching, synchronized output operations, which also implement the write() methods of Writer
     */

	/** Write a single character. */
	public void write(int c)
	{
		if (!isPrintMode())
			return;

		writer.write(getSMode(), getServerId(), String.valueOf(c), isPrintDBMode());
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

		writer.write(getSMode(), getServerId(), s.substring(off, len), isPrintDBMode());
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
	
	
}