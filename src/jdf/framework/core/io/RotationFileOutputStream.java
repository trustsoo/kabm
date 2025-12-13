/*
 * @(#)RotationFileOutputStream.java
 *
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the outformation about the copyright notice 
 * and the author.
 * 
 * @author
 */

package jdf.framework.core.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import jdf.framework.core.util.DateTime;


/**
 * <p>
 * 디렉토리에 날짜별 화일을 만들어서 output한다. 사이즈별로 나누는것은 다음버전으로...
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class RotationFileOutputStream extends OutputStream {
	private OutputStream out;

	private String logDirectory;

	private String type;

	private boolean append;

	/* 화일 쓰기도중 에러여부 */
	// private boolean isFileWriteError = false;
	/* 다음날의 00:00:00 시간의 숫자값 */
	private long nextDayStartTime = 0;

	private int logPreservationDay = 0;

	/**
	 * 기본생성자
	 * 
	 * @param dir
	 * @param append
	 */
	public RotationFileOutputStream(String dir, boolean append) {

		this(dir, "log", append, 0);
	}

	public RotationFileOutputStream(String dir, boolean append,
			int logPreservationDay) {

		this(dir, "log", append, logPreservationDay);
	}

	/**
	 * 
	 * @param dir
	 * @param type
	 * @param append
	 */
	public RotationFileOutputStream(String dir, String type, boolean append) {
		this(dir, type, append, 0);
	}

	/**
	 * 
	 * @param dir
	 * @param type
	 * @param append
	 * @param deleteDay
	 */
	public RotationFileOutputStream(String dir, String type, boolean append,
			int deleteDay) {

		this.logDirectory = dir;
		this.type = type;
		this.append = append;
		this.logPreservationDay = deleteDay;
	}

	private String today = null;

	/**
	 * 예전 파일을 지운다.
	 * 
	 * @param today
	 * @param deleteDay
	 *            몇일전것을 지울것인가.
	 */
	private void deleteOldFile(int deleteDayInt) {
		File logDir = new File(this.logDirectory);

		if (!logDir.exists())
			return;

		File[] f = logDir.listFiles();

		for (int i = 0; f != null && i < f.length; i++) {
			String fileName = f[i].getName();
			if (fileName.indexOf(".") < 0)
				continue;

			try {
				String fdate = fileName.substring(0, 8);
				int ifdate = Integer.parseInt(fdate); // 파일이름 -> 숫자로 변환

				if (ifdate < deleteDayInt) {
					boolean result = f[i].delete();
					System.out.println(f[i].getAbsolutePath() + " delete. "
							+ result);
				}
				// System.out.println(fdate + " not delete.");
			} catch (Exception e) {
				System.err.println(fileName + " " + e.toString());
			}

		}

	}

	/**
	 * file에 기록할때 buffering size 1024보다 512가 windows2000에서는 빠르다. Unix는?
	 */
	// private static final int FILE_BUFF_SIZE = 512;
	private synchronized OutputStream getOutputStream() throws IOException {
		long now = System.currentTimeMillis();

		// performnace를 위해 문자비교가 아닌 숫자 비교로 교체
		if (now < nextDayStartTime && out != null)
			return out;
		else {
			Calendar cal = Calendar.getInstance();

			cal.add(Calendar.DATE, 1);

			String yyyymmdd = DateTime.getString(cal.getTime(), "yyyyMMdd");

			// 다음날의 00:00:00 의 시간을 구한다.
			nextDayStartTime = DateTime.getTime(yyyymmdd, "yyyyMMdd");

			today = DateTime.getFormatString("yyyyMMdd");

			if (this.logPreservationDay > 0) {

				String deleteDay = DateTime.getAdjustDate(today,
						java.util.Calendar.DATE, this.logPreservationDay * -1);

				int deleteDayInt = Integer.parseInt(deleteDay);

				
				deleteOldFile(deleteDayInt);
			}
		}

		// String logname = today + ".log" ;
		String logname = today + "." + type;

		File file = new File(logDirectory, logname);

		try {
			File dirFile = new File(logDirectory);

			dirFile.mkdirs();

		} catch (Exception e) {
			e.printStackTrace();
		}

		String filename = file.getAbsolutePath();

		FileOutputStream fo = new FileOutputStream(filename, append);

		// out = new BufferedOutputStream(fo, FILE_BUFF_SIZE);
		if (out != null)
			out.close();
		out = fo;

		if (out == null)
			System.err.println(filename + "의 쓰기권한을 확인해 보세요");

		return out;
	}

	public void write(int b) throws IOException {
		getOutputStream().write(b);
	}

	public void write(byte[] data, int offset, int length) throws IOException {
		getOutputStream().write(data, offset, length);
	}

	public void flush() throws IOException {
		if (out != null)
			out.flush();

	}

	public void close() throws IOException {
		if (out != null)
			out.close();
	}

}