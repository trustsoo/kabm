package jdf.framework.core.data.cci;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.log.Logger;

/**
 * ConnectionFactory 객체를 얻을 수 있는 초기 Factory 형 class
 * 
 * <pre>
 * Connection conn = DefaultConnectionFactory.getConnection();
 * </pre>
 * 
 * 
 * @author
 * 
 */
public class DefaultConnectionFactory {
	private final static String LOG_ID = "<f:DefaultConnectionFactory> ";

	private static ConnectionFactory defaultFactory;

	static {

		try {
			defaultFactory = (ConnectionFactory) Class.forName(
					"jdf.framework.logic.adapter.BaseConnectionFactory").newInstance();
			Logger.info.println(LOG_ID + "initialize.");
		} catch (Exception e) {

			Logger.err.println(LOG_ID + "init err", e);
		}

	}

	/**
	 * Connection 구현객체를 얻는다.
	 * 
	 * @see jdf.framework.core.data.cci.ConnectionFactory#getConnection()
	 */
	public static Connection getConnection() throws ResourceException {
		if (defaultFactory == null)
			throw new ResourceException("ConnectionFactory is not defined.");

		return defaultFactory.getConnection();
	}

	public static Connection getConnection(ConnectionSpec spec)
			throws ResourceException {
		if (defaultFactory == null)
			throw new ResourceException("ConnectionFactory is not defined.");

		return defaultFactory.getConnection(spec);
	}

}