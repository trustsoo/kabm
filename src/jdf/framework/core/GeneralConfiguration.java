/**
 * @(#)
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

package jdf.framework.core;

import java.util.List;

/**
 * <b><code>GeneralConfiguration</code></b>
 * <p>
 * <code>Config</code> Interface의 기본기능을 구현한 추상 Class. initialize method를 구현해
 * 주어야 한다.
 * </p>
 * 
 * @author WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 * @version 1.0
 */

public abstract class GeneralConfiguration implements Config {

	/*
	 * 설정화일 명
	 */
	protected static String configuration_file = null;

	protected String _prefix = "";

	/**
	 * GeneralConfiguration default Constructor
	 */
	public GeneralConfiguration() throws ConfigurationException {
		// initialize();

		configuration_file = Configuration.getConfigFilePath();
	}

	public GeneralConfiguration(String prefix) throws ConfigurationException {
		_prefix = prefix;
		// initialize();
		configuration_file = Configuration.getConfigFilePath();
	}

	/**
	 * boolean값으로 키값을 가져온다.
	 * 
	 * @return boolean
	 * @param key
	 *            java.lang.String
	 */
	public boolean getBoolean(String key) {

		try {
			return Boolean.valueOf(getString(key)).booleanValue();
			// return (new Boolean(getString(key))).booleanValue();

		} catch (Exception e) {
			throw new IllegalArgumentException("Illegal boolean Key : "
					+ _prefix + key);
		}

	}

	/**
	 * 정수형으로 키값을 가져온다.
	 * 
	 * @return int
	 * @param key
	 *            java.lang.String
	 */
	public int getInt(String key) {
		try {
			return Integer.parseInt(getString(key));

		} catch (Exception e) {
			throw new IllegalArgumentException("Illegal int Key : " + _prefix
					+ key);
		}

	}

	/**
	 * long type으로 키값을 가져온다.
	 * 
	 * @return int
	 * @param key
	 *            java.lang.String
	 */
	public long getLong(String key) {
		try {
			return Long.parseLong(getString(key));

		} catch (Exception e) {
			throw new IllegalArgumentException("Illegal int Key : " + _prefix
					+ key);
		}

	}

	/**
	 * 문자열로 키값을 가져온다. 키 값이 없을 경우 입력한 기본값을 가져온다.
	 * 
	 * @return String
	 * @param key
	 *            config 키 이름, defaultValue 기본값
	 */
	public String getString(String key, String defaultVal) {
		try {
			String result = getString(key);
			if (result != null)
				return result;
		} catch (Exception ex) {
		}
		return defaultVal;
	}

	/**
	 * 정수형으로 키값을 가져온다. Key가 없거나, 정수형이 아닌경우 기본값을 가져온다.
	 * 
	 * @return int
	 * @param key
	 *            config 키 이름, defaultValue 기본값
	 */
	public int getInt(String key, int defaultVal) {
		try {
			return Integer.parseInt(getString(key));
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	public long getLong(String key, long defaultVal) {
		try {
			return Long.parseLong(getString(key));
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	/**
	 * boolean 값으로 키값을 가져온다. Key가 없거나 잘못된 형태인 경우 기본값을 가져온다.
	 * 
	 * @return Boolean
	 * @param key
	 *            config 키 이름, defaultValue 기본값
	 */
	public boolean getBoolean(String key, boolean defaultVal) {
		try {
			String x = getString(key);

			if (x == null || x.length() == 0)
				return defaultVal;

			// return (new Boolean(x)).booleanValue();
			return Boolean.valueOf(x).booleanValue();
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	/**
	 * 경로를 찾는다.
	 * 
	 */
	public abstract Config lookup(String path) throws ConfigurationException;

	/**
	 * 초기화에 관계된 이 메쏘드를 구현해야 한다.
	 * 
	 */
	public abstract void initialize() throws ConfigurationException;

	/**
	 * 키값을 가져온다.
	 * 
	 */
	public abstract List keys();

	/**
	 * 문자열로 키 값을 가져온다.
	 * 
	 * @return java.lang.String
	 * @param key
	 *            java.lang.String
	 */

	public abstract String getString(String key);

}