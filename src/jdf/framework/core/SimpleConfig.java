package jdf.framework.core;

import java.util.*;

/**
 * <b><code>SimpleConfigurable</code></b>
 * <p>
 * Config 인터페이스를 구현한 간단한 설정 객체
 * </p>
 * 
 * @author 
 * @version 1.0
 */
public class SimpleConfig implements Config {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1211312323096861010L;

	public Map conf;

	public SimpleConfig(Map conf) {
		this.conf = conf;
	}

	private SimpleConfig() {
		//
	}

	/**
	 * boolean값으로 키값을 가져온다.
	 * 
	 * @return boolean
	 * @param key
	 *            java.lang.String
	 */
	public boolean getBoolean(String key) {
		return Boolean.valueOf(key).booleanValue();
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
			throw new IllegalArgumentException("Illegal int Key : " + key);
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
			throw new IllegalArgumentException("Illegal int Key : " + key);
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
		String x = getString(key);

		if (x == null || x.length() == 0)
			return defaultVal;

		return Boolean.valueOf(key).booleanValue();

	}

	/**
	 * 초기화에 관계된 이 메쏘드를 구현해야 한다.
	 * 
	 */
	public void initialize() throws ConfigurationException {
		//
	}

	public List keys() {
		Set keySet = conf.entrySet();
		Iterator iterator = keySet.iterator();

		ArrayList list = new ArrayList();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) (iterator.next());
			list.add(entry.getKey());
		}

		return list;
	}

	/**
	 * 문자열로 키 값을 가져온다.
	 * 
	 * @return java.lang.String
	 * @param key
	 *            java.lang.String
	 */

	public String getString(String key) {
		return (String) conf.get(key);
	}

	public Config lookup(String path) throws ConfigurationException {
		return null;
	}

}
