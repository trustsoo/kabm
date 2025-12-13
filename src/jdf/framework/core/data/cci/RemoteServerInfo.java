package jdf.framework.core.data.cci;

import java.util.Properties;

/**
 * ConnectionSpec 구현체
 * 외부 anyLOGIC Server의 정보를 설정하는데 사용
 * 
 * @author
 * 
 */
public class RemoteServerInfo extends Properties implements ConnectionSpec {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4521860443542815256L;

	public RemoteServerInfo(String url) {
		super();

		super.setProperty(ConnectionSpec.SERVER_URL, url);
	}

	public String getProperty(String key) {
		return super.getProperty(key);
	}

	public boolean isTransactionSupport() {

		return false;
	}

}
