package jdf.framework.core;

/**
 * @(#) PropsConfiguration.java
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team, 
 * Application Intrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 * 
 * NOTICE !      You can copy or redistribute this code freely, 
 * but you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author  WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 */

import jdf.framework.core.util.StringFormater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;


/**
 * <b><code>PropsConfiguration</code></b>
 * <p>
 * 일반 Properties 양식의 설정화일을 읽기 위한 Class
 * </p>
 * @author
 * @version 1.0
 */

public final class PropsConfiguration extends GeneralConfiguration
{

	/*  자신의 객체정보  */
	private static Properties props = null;

	private static boolean isRemoteConfigMode = false;

	/*  설정화일의 마지막 수정일 */
	private static long last_modified = 0;

	private static String configuration_file = null;

	private long refresh = 1000; // 화일 체크하는 최소주기

	private static long lastCheckTime = 0;

	static {
		
		if (configuration_file == null)
		{
			File default_file = null;
			String configFile = null;

			default_file = new File(System.getProperty("user.dir"), "jdf.properties");
			configuration_file = System.getProperty("config.file", default_file.getAbsolutePath());
		}

	}

	/**
	 * @exception ConfigurationException
	 */
	public PropsConfiguration() throws ConfigurationException
	{
		super();

		initialize();
	}

	private PropsConfiguration(String prefix) throws ConfigurationException
	{
		super(prefix); //_prefix=prefix;

		initialize();
	}

	/**
	 * @exception ConfigurationException
	 */
	public void initialize() throws ConfigurationException
	{
		try
		{

			// Remote Configuration Service를 실시하면 initialize하지 않는다.
			if (isRemoteConfigMode)
				return;

			/* refresh 기간만큼은 다시 initialize() 하지 않는다 */
			long checkTime = System.currentTimeMillis();
			if (checkTime - lastCheckTime < refresh)
				return;

			lastCheckTime = checkTime;

			File file = new File(configuration_file);

			if (!file.canRead())
				throw new ConfigurationException(
					this.getClass().getName() + " - Con't open configuration file: " + configuration_file);

			if (last_modified == file.lastModified())
				return;

			FileInputStream fin = new FileInputStream(file);
			props = new Properties();
			props.load(fin);

			fin.close();
			last_modified = file.lastModified();
		}
		catch (ConfigurationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			props = null;
			last_modified = 0;
			throw new ConfigurationException(
				this.getClass().getName() + " - Con't open configuration file: " + e.getMessage());
		}
	}

	/**
	 * 키에 대한 값을 가져온다.
	 * @param key 키값
	 * @return value 키에 대한 값
	 */
	public String getString(String key)
	{
		try
		{
			//return CharConversion.E2K( getString(key) );
			return props.getProperty(_prefix + key);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Illegal String Key : " + _prefix + key);
		}
	}

	/**
	 * 키 이름을 List 형태로 가져온다.
	 *
	 * @return List 키 이름의 List
	 */
	public List keys()
	{
		//return props.propertyNames();

		Enumeration keys = props.propertyNames();

		List list = new ArrayList();

		while (keys.hasMoreElements())
		{
			list.add(keys.nextElement());
		}

		return list;
	}

	/**
	 * 키에 대한 위치를 찾아 Config를 return 한다.
	 *
	 * @param prefix 경로
	 * @return Config Config 객체
	 * @exception ConfigurationException
	 */
	public Config lookup(String prefix) throws ConfigurationException
	{
		//현재는 Properties 기반이라 /을 .로 바꾸고 
		if (prefix.startsWith("/"))
			prefix = prefix.substring(1);
		prefix = StringFormater.replaceStr(prefix, "/", ".") + ".";

		_prefix = prefix;

		return this;

		//return new PropsConfiguration(prefix); 
	}

	/**
	 * 설정화일을 읽는다.
	 *
	 * @param is InputStream
	 * @exception IOException
	 */
	public static void load(InputStream is) throws IOException
	{
		try
		{
			Properties inProps = new Properties();
			inProps.load(is);

			props = inProps;
			isRemoteConfigMode = true;
		}
		catch (IOException ioe)
		{
			throw ioe;
		}

	}

}