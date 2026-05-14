/*
 * @(#)Configuration.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */

package jdf.framework.core;

import jdf.framework.core.xml.Registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


/**
 * <b><code>Configuration</code> </b>
 * <p>
 * <code>GeneralConfiguration</code> 객체를 확장하여 XML형태의 Config화일을 읽어 환경을 설정하기 위한
 * class이다. 기존의 Config interfacing하여 사용한다.
 * </p>
 * 
 * @author
 * @version 1.1
 */

public final class Configuration {

	// XML 형태의 Config의 Root element 명
	private static final String ROOT_ELEMENT = "/config";

	// 매번 lookup으로 찾은 Config정보를 매번 생성하지 않게
	private static Map lookupConfigMap = new HashMap();

	private static Config conf = null;

	private static boolean isXmlConfig = false;

	private static String configFilePath;

	private static String configPath;

	private static String majorVersion = "4.0";

	private static String minorVersion;

	private static String licenseInfo;

	// 이 class가 처음 로드된 시간
	// 즉 Framework 이 처음 로드된 시간으로 봐도 된다.
	private static long loadDateTime;

	/**
	 * Framework config.xml 의 위치를 의미하는 parameter 의 key 기본적으로 Framework.config 가
	 * key 이다.
	 * 
	 */
	public static final String DEFAULT_CONFIG_PATH_KEY = "Framework.config";

	private static String targetEncoding = null;

	static {

		InputStream is = null;
		String configFilePath = System.getProperty("config.file");

		if (configFilePath == null) {
			configFilePath = System.getProperty(DEFAULT_CONFIG_PATH_KEY);
		}

		if (configFilePath == null) {
			try {
				URL u = Config.class.getResource("/Framework/Config.class");
				String FrameworkJarPath = u.getPath();

				int p = FrameworkJarPath.lastIndexOf("/", FrameworkJarPath
						.lastIndexOf(".jar"));
				if (p > 0) {
					String propertyPath = FrameworkJarPath.substring(6, p + 1)
							+ "Framework.properties";

					is = new FileInputStream(propertyPath);
					System.out.println("property exist > " + propertyPath);
					Properties properties = new Properties();
					properties.load(is);

					configFilePath = properties
							.getProperty(DEFAULT_CONFIG_PATH_KEY);
					System.out.println("property set config path="
							+ configFilePath);
				}

			} catch (Throwable e) {
				// e.printStackTrace();

			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException ioe) {

					}
				}
			}
		}

		if (configFilePath == null) {
			System.err
					.println("<WARNING> -Dconfig.file={Framework config file path} is not defined.");
		} else {
			setConfigFilePath(configFilePath);
		}

		try {
			ResourceBundle rb = ResourceBundle.getBundle("framework.build");

			minorVersion = rb.getString("build.number");

		} catch (MissingResourceException e) {
			//e.printStackTrace();
		}

		loadDateTime = System.currentTimeMillis();

		// isXmlConfig = Boolean.getBoolean( "xmlConfig" );
		// isXmlConfig = new Boolean( System.getProperty("xmlConfig") );
	}

	/**
	 * 이 class가 처음 로드된 시간, 이 class는 Framework을 쓰기위해서 사실상 가장 먼저 로드되는 class이므로,
	 * Framework 의 로드 시간으로 봐도 무방하다.
	 * 
	 * @return
	 */
	public static long getLoadDateTime() {
		return loadDateTime;

	}

	/**
	 * Framework의 major 버전
	 * 
	 * @return
	 */
	public static String getMajorVersion() {
		return majorVersion;
	}

	/**
	 * Framework의 minor 버전 보통 컴파일횟수를 의미한다.
	 * 
	 * @return
	 */
	public static String getMinorVersion() {
		return minorVersion;
	}

	public static String getLicenseInfo() {
		return licenseInfo;
	}

	public static void setLicenseInfo(String info) {
		licenseInfo = info;
	}

	/**
	 * 서비스 시스템의 기본 target encoding
	 * 
	 * @return
	 */
	public static String getTargetEncoding() {
		if (targetEncoding == null) {
			try {
				targetEncoding = Configuration.lookup("/site").getString(
						"target-encoding", "euc-kr");

			} catch (ConfigurationException cfe) {

			}
			if (targetEncoding == null) {
				targetEncoding = "euc-kr";
			}
		}

		return targetEncoding;
	}

	/**
	 * @exception ConfigurationException
	 */
	private Configuration() throws ConfigurationException {
		super();
	}

	public synchronized static void setConfigFilePath(String path) {
		if (path == null)
			return;

		System.out.println("Framework config:" + path);

		if (path.toLowerCase().indexOf(".xml") > 0)
			isXmlConfig = true;
		else
			isXmlConfig = false;

		try {
			File f = new File(path);
			if (!f.exists()) {
				System.err
						.println("Framework의  config.xml을 찾을 수 없습니다.-Dconfig.file VM 옵션으로 설정하세요");
				// throw new Error("config not found");
			}

			configFilePath = f.getAbsolutePath();
			configPath = f.getParent();

		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("Framework config dir:" + configPath);
	}

	/**
	 * Framework 에서 설정파일로 사용하는 config.xml의 절대 경로를 반환한다.
	 * 
	 * @return
	 */
	public static String getConfigFilePath() {
		return configFilePath;
	}

	/**
	 * Framework 에서 설정파일로 사용하는 config.xml 의 디렉토리 경로를 반환한다.
	 * 
	 * @return
	 */
	public static String getConfigPath() {
		return configPath;
	}

	/**
	 * Config 구현 객체를 얻기 위한 메쏘드 - 일반적으로 lookup 메쏘드를 통한 Config 구현객체 얻는 과정을 추천한다. -
	 * 이 메쏘드는 초기 Framework 버전을 위해 보전한다. ex)
	 * 
	 * <pre>
	 * Config conf = Configuration.getInitial();
	 * </pre>
	 * 
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public static Config getInitial() throws ConfigurationException {

		if (conf == null) {
			if (isXmlConfig)
				conf = new Registry();
			else
				throw new ConfigurationException(
						"not support other XML type Configuration");
		}

		return conf;

	}

	/**
	 * lookup은 사용자마다 새로운 객체를 반환해주어야 한다.
	 * 
	 */
	public static Config lookup(String prefix) throws ConfigurationException {

		String fullPath = "";

		if (isXmlConfig) {
			Config storeCfg = (Config) lookupConfigMap.get(prefix);

			if (storeCfg == null) {
				fullPath = ROOT_ELEMENT + prefix;
				storeCfg = new Registry(fullPath);
				lookupConfigMap.put(prefix, storeCfg);
			}

			return storeCfg;
		} else if (configFilePath == null) {
			throw new ConfigurationException("Not define Framework config.");
		} else {
			throw new ConfigurationException(
					"not support other XML type Configuration");
		}

	}

	/**
	 * Configuration 정보를 InputStream으로 setting 한다.
	 * 
	 */
	public static void load(InputStream is) throws Exception {
		if (isXmlConfig)
			Registry.load(is);

	}

}