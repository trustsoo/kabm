package jdf.framework.core.log;

import java.io.OutputStream;

/**
 * @(#) LoggerWriter.java
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
 *             com.kdb.ih.common.jdf.UserLoggerWriter 클래스에서 담당
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

public interface LoggerWriter
{

	/**
     * Log Mode. SYS : Java Development Framework의 에러 로그. 개발자는 이 부분에 대하여서는 고려하지 않아도 된다. 그러나 Framework을 개발하는 사람은 이러한 에러가
     * 발생할 시 반드시 확인하고 문제가 있으면 잡아 주어야 한다.
     */
	public final static int SYS = 0;

	public final static String MARK_SYS = "<S>";

	/**
     * Log Mode. ERR : 비즈니즈 적으로 일어나면 안될 심각한 상황의 에러로그.
     */
	public final static int ERR = 1;

	public final static String MARK_ERR = "<E>";

	/**
     * Log Mode. WARN :비즈니즈 적으로 일어나면 안되지만 그리 심각하지 않은 상황의 에러로그. 그러나 반드시 차후에 이러한 상황에 대하여 확인 단계를 거쳐야 할 로그.
     */
	public final static int WARN = 2;

	public final static String MARK_WARN = "<W>";

	/**
     * Log Mode. INFO : 비즈니즈 적으로 충분히 일어날 수 있으며, 필요에 의해 남겨야 할 로그.
     */
	public final static int INFO = 3;

	public final static String MARK_INFO = "<I>";

	/**
     * Log Mode. DEBUG : 개발시에 개발자가 보기 위한 것으로, 혹은 향후 어떤 문제가 생겼을 때, 모든 로그 TRACE 를 보고자 할 때 남을 수 있는 상황의 로그.
     */
	public final static int DEBUG = 4;

	public final static String MARK_DEBUG = "<D>";

	/**
     * 사용자 access 로그관련
     */
	public final static int USER = 5;

	public final static String MARK_USER = "<U>";

	// 확장을 위한

	/**
     * Log Mode. SQL : SQL Query문장을 남긴다. 개발시 디버그를 위해 남길 수 있고, production 운영시는 data 분석을 위해 남길 수 있다.
     */
	public final static int SQL = 10;

	public final static String MARK_SQL = "<Q>";

	/**
     * Log Mode. SQL : network 데이타의 byte stream 코드를 남긴다.
     */
	public final static int PACKET = 11;

	public final static String MARK_PACKET = "<P>";

	/**
     * 성능측정 관련 로그
     */
	public final static int MONITOR = 12;

	public final static String MARK_MONITOR = "<M>";

	public void addOutputStream(OutputStream out) throws Exception;

	public OutputStream getMultiOutputStream();

	public void removeOutputStream(OutputStream out) throws Exception;

	/** Print an array of chracters. */
	public void print(char x[]);

	/** Print a character. */
	public void print(char x);

	/** Print a double. */
	public void print(double x);

	/** Print a float. */
	public void print(float x);

	/** Print an integer. */
	public void print(int x);

	/** Print a long. */
	public void print(long x);

	/** Print an object. */
	public void print(Object x);

	/** Print a String. */
	public void print(String x);

	/** Print a boolean. */
	public void print(boolean x);

	/** Finish the line. */
	public void println();

	/** Print an array of characters, and then finish the line. */
	public void println(char x[]);

	/** Print a character, and then finish the line. */
	public void println(char x);

	/** Print a double, and then finish the line. */
	public void println(double x);

	/** Print a float, and then finish the line. */
	public void println(float x);

	/** Print an integer, and then finish the line. */
	public void println(int x);

	/** Print a long, and then finish the line. */
	public void println(long x);

	/** Print an Object, and then finish the line. */
	public void println(Object x);

	/**
     * Print predefined information. Print the predefined information of Object p, and then the string of message Object
     * p.
     */
	public void println(String p, String x);

	public void println(String p, boolean printTime);

	public void print(String p, boolean printTime);

	public void println(String desc, Throwable err);

	public void println(Object p, Object x);

	/** Print a String, and then finish the line. */
	public void println(String x);

	/** Print a boolean, and then finish the line. */
	public void println(boolean x);

	/**
     * Write an array of characters. This method cannot be inherited from the Writer class because it must suppress I/O
     * exceptions.
     */
	public void write(char buf[]);

	/** Write a portion of an array of characters. */
	public void write(char buf[], int off, int len);

	/*
     * Exception-catching, synchronized output operations, which also implement the write() methods of Writer
     */

	/** Write a single character. */
	public void write(int c);

	/**
     * Write a string. This method cannot be inherited from the Writer class because it must suppress I/O exceptions.
     */
	public void write(String s);

	/** Write a portion of a string. */
	public void write(String s, int off, int len);

	public void flush();

	public boolean isPrintMode();
		

	/**
     * log 가 기록되는 디렉토리명을 return
     * 
     * @return
     */
	public String getDirectoryName();

	/**
     * log 가 기록되는 디렉토리명을 설정 실제로 이 mehtod 를 설정한다고 해서 로그 디렉토리가 변경되는 것은 아니다.
     * 
     * @param dir
     */
	public void setDirectoryName(String dir);
	
}


