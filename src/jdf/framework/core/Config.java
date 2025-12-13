/*
 * @(#) Config.java
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
 **/
package jdf.framework.core;

/**
 * <b><code>Config</code></b>
 * <p>
 * 시스템의 전환적인 설정을 Property형식의 화일,XML, 또는 DB를 이용할 수 있는 기본 Interface이다.
 * </p>
 * 
 * @author WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 * @version 1.0
 */
public interface Config extends java.io.Serializable {

	/**
	 * 문자열로 키 값을 가져온다.
	 * 
	 * 
	 * 
	 * 
	 * @return configuration value
	 * @param key
	 *            config key
	 */
	String getString(String key);

	/**
	 * boolean값으로 키값을 가져온다.
	 * 
	 * @return boolean
	 * @param key
	 *            java.lang.String
	 */
	boolean getBoolean(String key);

	/**
	 * 정수형으로 키값을 가져온다.
	 * 
	 * @return int
	 * @param key
	 *            java.lang.String
	 */
	int getInt(String key);

	/**
	 * long형으로 키값을 가져온다.
	 * 
	 * @return long
	 * @param key
	 *            java.lang.String
	 */
	long getLong(String key);

	/**
	 * 문자열로 키값을 가져온다. 키 값이 없을 경우 입력한 기본값을 가져온다.
	 * 
	 * @return String
	 * @param key
	 *            config 키 이름
	 * @param defaultValue
	 *            기본값
	 */
	String getString(String key, String defaultValue);

	/**
	 * 정수형으로 키값을 가져온다. Key가 없거나, 정수형이 아닌경우 기본값을 가져온다.
	 * 
	 * @return int
	 * @param key
	 *            config 키 이름
	 * @param defaultValue
	 *            기본값
	 */
	int getInt(String key, int defaultValue);

	/**
	 * long형으로 키값을 가져온다. Key가 없거나, long형이 아닌경우 기본값을 가져온다.
	 * 
	 * @return int
	 * @param key
	 *            config 키 이름
	 * @param defaultValue
	 *            기본값
	 */
	long getLong(String key, long defaultValue);

	/**
	 * boolean 값으로 키값을 가져온다. Key가 없거나 잘못된 형태인 경우 기본값을 가져온다.
	 * 
	 * @return Boolean
	 * @param key
	 *            config 키 이름
	 * @param defaultValue
	 *            기본값
	 */
	boolean getBoolean(String key, boolean defaultValue);

	/**
	 * boolean 값으로 키값을 가져온다. Key가 없거나 잘못된 형태인 경우 기본값을 가져온다.
	 * 
	 * @return 자식키값 list
	 */
	java.util.List keys();

	/**
	 * 위치를 찾는다.
	 * 
	 * @throws ConfigurationException config 오류
	 * @return Config
	 * @param path
	 *            위치정보
	 */
	Config lookup(String path) throws ConfigurationException;

	/**
	 * 초기화 작업을 한다.
	 * 
	 * @throws ConfigurationException config 오류
	 */
	void initialize() throws ConfigurationException;
}
