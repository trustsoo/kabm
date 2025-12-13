package jdf.framework.core.data.cci;

/**
 * <b><code>ConnectionSpec</code> </b>
 * <p>
 * Legacy 시스템의 ConnectionSpec
 * </p>
 * 
 * @author
 * @version 1.0
 */
public interface ConnectionSpec {
	
	/**
	 * remote anyLOGIC 서버 경로
	 * ex) http://localhost:8080/contextName
	 */
	public final static String SERVER_URL = "anylogic.server.url";

	/**
	 * Connection 설정에 관련된 property 값을 얻는다. ex) datasource 명
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key);

	/**
	 * Connection이 Transaction을 지원하는지 여
	 * 
	 * @return
	 */
	public boolean isTransactionSupport();

}