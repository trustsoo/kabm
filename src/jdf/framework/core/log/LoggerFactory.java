package jdf.framework.core.log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.io.DummyOutputStream;
import jdf.framework.core.io.MultiOutputStream;
import jdf.framework.core.io.ReconnectOutputStream;
import jdf.framework.core.io.RotationFileOutputStream;
import jdf.framework.core.io.UDPOutputStream;



/**
 * @(#) LoggerFactory.java Copyright 1999-2000 by LG-EDS Systems, Inc.,
 *      Information Technology Group, Application Architecture Team, Application
 *      Infrastructure Part. 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042,
 *      KOREA. All rights reserved.
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author
 */

public class LoggerFactory {
	static {
		String majorVersion = Configuration.getMajorVersion();
		String minorVersion = Configuration.getMinorVersion();

		String version = "4.0";
		String name = "community license";
		String expire = "9999/12/12";
		String company = "";

		String licenseInfo = name;

		if (company != null && company.length() > 0)
			licenseInfo = licenseInfo + " to " + company;
		Configuration.setLicenseInfo(licenseInfo);

		System.out
				.println("===============================================================================");
		System.out
				.println("          JDF Framework ");
		System.out.println("                               Version "
				+ majorVersion + "." + minorVersion);		
		System.out
				.println("      Copyright (c)  2017. All rights reserved.");
		System.out
				.println("===============================================================================");

	}

	public final static String DEFAULT_SVR_ID = "SVR1";

	private static Map<String, LoggerWriter> loggerWriterMap = new HashMap<>();

	private static List<MultiOutputStream> loggerOutputStreamList = new ArrayList<>();

	private LoggerFactory() {
	}

	/**
	 * �ش� log id �� �ش��ϴ� LoggerWriter �� ��´�.
	 * 
	 * @param logId
	 *            �α׾��̵�
	 * @return LoggerWriter ��ü
	 * @throws ConfigurationException
	 *             ��������
	 */
	public static LoggerWriter getLoggerWriter(String logId)
			throws ConfigurationException {
		return getLoggerWriter(logId, "");
	}

	/**
	 * �ش� log id �� �ش��ϴ� LoggerWriter �� ��´�.
	 * 
	 * @param logId
	 *            �α׾��̵�
	 * @param mark
	 *            �α� ��ũ
	 * @return LoggerWriter ��ü
	 * @throws ConfigurationException
	 *             ��������
	 */
	public synchronized static LoggerWriter getLoggerWriter(String logId,
			String mark) throws ConfigurationException {
		LoggerWriter writer = (LoggerWriter) loggerWriterMap.get(logId);

		if (writer == null) {
			boolean isAsync = false;
			boolean autoflush = true;
			boolean isConsoleOut = true;
			String modeKeyword = mark;

			String directory = "";

			Config conf = null;

			// �α����� �����Ⱓ
			int logPreservationDay = 0;

			try {
				Config base = Configuration.lookup("/logger");

				// �α������� ����� �Ⱓ
				// 0�̸� ��� ����
				logPreservationDay = base.getInt("logPreservationDay", 0);

				conf = Configuration.lookup("/logger/" + logId);

				isAsync = conf
						.getBoolean("isAsync", base.getBoolean("isAsync"));
				directory = conf.getString("dir", base.getString("dir"));
				autoflush = conf.getBoolean("autoflush", base
						.getBoolean("autoflush"));
				if (directory.indexOf(".") == 0 && directory.indexOf("..") != 0)
					directory = Configuration.getConfigPath()
							+ directory.substring(1);

				isConsoleOut = conf.getBoolean("consoleOutput", base
						.getBoolean("consoleOutput"));
				modeKeyword = conf.getString("keyword", mark);
			} catch (ConfigurationException cfe) {
				// cfe.printStackTrace();
				// System.err.println(logId+" log err "+cfe.getMessage());

				System.out
						.println(mark
								+ " LOG config error. redirect to [System.out] cause Configuration err");
			}

			OutputStream baseStream = null;

			// ���丮�� �ƹ������� ������ ȭ���� ������� �ʴ´�.
			if (directory.length() == 0)
				baseStream = new DummyOutputStream();
			else
				baseStream = new RotationFileOutputStream(directory, true,
						logPreservationDay);

			MultiOutputStream mout = new MultiOutputStream(baseStream);

			if (isConsoleOut) {

				if ("err".equals(logId))
					mout.addOutputStream(System.err);
				else
					mout.addOutputStream(System.out);

			}

			// �α׼��� ���
			try {

				Config base = Configuration.lookup("/logger/logserver");
				boolean isEnable = base.getBoolean("enable", false);
				String logServerIp = base.getString("ip");
				int logServerPort = base.getInt("port", 0);
				String protocol = base.getString("protocol", "udp");
				int logServerCheckTime = base.getInt("refresh", 0) * 1000;

				String logIds = base.getString("logIds");

				boolean useLogServer = false;
				// System.out.println(logId+"=="+isEnable+":"+logIds);
				// log server��
				if (isEnable && logIds.indexOf(logId) > -1) {
					useLogServer = true;
					OutputStream rout = null;
					if ("tcp".equals(protocol))
						rout = new ReconnectOutputStream(logServerIp,
								logServerPort, logServerCheckTime);

					else
						rout = new BufferedOutputStream(new UDPOutputStream(
								logServerIp, logServerPort), 256);

					if (rout != null)
						mout.addOutputStream(rout);
				}

				System.out.println("LOG[" + logId + "] console=" + isConsoleOut
						+ " async=" + isAsync + " protocol=" + protocol
						+ " \n  directory=" + directory);

			} catch (ConfigurationException ce) {

			} catch (IOException ioe) {
			}

			// PrintStream ps = new PrintStream(mout);

			if (isAsync)				
				writer = new AsyncLoggerWriter(mout, conf, modeKeyword);
			else
				writer = new NormalLoggerWriter(mout, autoflush, conf, modeKeyword);

			try {
				File wf = new File(directory);
				writer.setDirectoryName(wf.getAbsolutePath());
			} catch (Exception e) {

			}

			loggerWriterMap.put(logId, writer);
			loggerOutputStreamList.add(mout);

		}

		return writer;
	}

	/**
	 * ��� Log �� �����Ѵ�.
	 * 
	 */
	public static void close() {

		System.out.println("Log OutuptStream [" + loggerOutputStreamList.size()
				+ "] closing");

		for (int i = 0, n = loggerOutputStreamList.size(); i < n; i++) {

			try {
				MultiOutputStream mout = (MultiOutputStream) loggerOutputStreamList
						.get(i);

				mout.close();
			} catch (Throwable ioe) {
				ioe.printStackTrace();

			}

		}

		loggerOutputStreamList.clear();
		loggerWriterMap.clear();

	}

	/**
	 * server ID ������ �����´�.
	 * 
	 * 
	 * @return ServerID
	 */
	public static String getServerId() {
		try {
			Config c = Configuration.lookup("/logger");

			String serverId = c.getString("serverId");

			if (serverId == null || serverId.length() == 0) {
				String propsNm = c.getString("printSystemProperty");
				serverId = System.getProperties().getProperty(propsNm);

			} else if (serverId.indexOf("$") == 0) {
				serverId = System.getProperties().getProperty(
						serverId.substring(1));
			}

			if (serverId == null || serverId.length() == 0)
				return DEFAULT_SVR_ID;
			else if (serverId.length() > 0)
				return serverId;

		} catch (ConfigurationException ce) {

		} catch (NullPointerException ne) {

		}

		return DEFAULT_SVR_ID;
	}

}
