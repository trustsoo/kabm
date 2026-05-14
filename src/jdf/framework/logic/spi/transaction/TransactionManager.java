package jdf.framework.logic.spi.transaction;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.LocalTransaction;
import jdf.framework.core.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TrasactionMAnager는 BusinessLogic Descriptor에 수행되는 BusinessLogic의 한 transaction을 총괄 관리한다.
 * 
 * transaction 단위는 다음으로 정의한다. 한번의 사용자 요청으로 최종결과가 오는 시점을 의미한다. (주의 : 다른 워크플로우 transaction 제품처럼 주문/발송/취소 처럼 여러 단개가 한
 * 입력/출력이 아니라 여러 일에 거쳐 일어나는 transaction을 의미하지는 않는다.
 * 
 * 
 * transaction Manager에서 하는 일은 - transaction의 시작/종료 관리 - commit/rollback 관리 - jdf.framework.core.data.cci.Connection의 관리
 * (datasource 명을 기준으로 관리한다)
 * 
 * 
 * 추후 JTA를 지원한다.
 * 
 * @author
 * @version 1.0
 * @since 2003-12-09 오전 10:12:13
 * 
 */
public class TransactionManager
{
	private final static String LOG_ID = "<l:TransactionManager> ";

	private boolean isTransactionMode = false;

	private List<LocalTransaction> txList;

	private JTATransactionManager jta;

	// jdf.framework.core.data.Connection map 리스트
	private Map<String, Connection> connList = new HashMap<>();

	public TransactionManager() {

		try {
			Config conf = Configuration.lookup("/resource/anylogic");

			// JTA 기반의 transaction을 사용할 것인가?
			if (conf.getBoolean("useJTA", false)) {
				jta = new JTATransactionManager();
			}
		} catch (ConfigurationException cfe) {
			Logger.err.println(LOG_ID + "init err", cfe);

		}

	}

	/**
     * 
     * transaction context 상에서 join 하는것은 transaction 시작(begin)을 의미한다.
     * 
     * 
     * @param trans
     */
	public void join(LocalTransaction trans) throws ResourceException
	{
		// Logger.info.println(LOG_ID+"join");

		if (this.jta == null) {
			trans.beginTransaction();
			txList.add(trans);
		}

	}

	/**
     * datasource명을 기준으로 Connection을 관리한다.
     * 
     * @param datasource
     * @param conn
     */
	public void setConnection(String datasource, Connection conn)
	{
		// datasource 가 "" 인 경우는 dbms 에서 default connection을 의미한다.
		if (datasource == null)
			return;
		connList.put(datasource, conn);
	}

	/**
     * datasource에 따른 Connection을 가져온다.
     * 
     * @param datasource
     * @return
     */
	public Connection getConnection(String datasource)
	{
		return (Connection) connList.get(datasource);
	}

	/**
     * 현재 트랜잭션 모드인지 여부를 반환
     * 
     * @return
     */
	public boolean isTransactionMode()
	{
		return this.isTransactionMode;
	}

	/**
     * 트랜잭션을 시작한다.
     * 
     * @throws ResourceException
     */
	public void begin() throws ResourceException
	{
		if (jta != null) {
			jta.begin();
		} else {
			Logger.debug.println(LOG_ID + "begin");
			isTransactionMode = true;
			txList = new ArrayList<>();
		}
	}

	/**
     * commit 한다.
     * 
     * @see LocalTransaction#commitTransaction()
     */
	public void commit() throws ResourceException
	{
		if (jta != null) {
			jta.begin();
			return;
		}

		if (!isTransactionMode)
			return;

		Logger.debug.println(LOG_ID + "commit size=" + txList.size());
		try {
			for (int i = 0; i < txList.size(); i++) {
				LocalTransaction tx = (LocalTransaction) txList.get(i);

				tx.commitTransaction();
			}

		} finally {
			close();
		}

	}

	/**
     * rollback을 한다.
     * 
     * @see LocalTransaction#rollbackTransaction()
     */
	public void rollback()
	{
		if (jta != null) {
			jta.rollback();
			return;
		}

		if (!isTransactionMode)
			return;

		Logger.debug.println(LOG_ID + "rollback size=" + txList.size());
		try {

			for (int i = 0; txList != null && i < txList.size(); i++) {
				LocalTransaction tx = (LocalTransaction) txList.get(i);

				try {
					tx.rollbackTransaction();
				} catch (Exception e) {
					Logger.err.println("<TransactionManager> rollback err", e);
				}

			}
		} finally {
			close();
		}

	}

	/**
     * 상황종료
     * 
     */
	private void close()
	{
		// isTransactionMode= false;
		// txList= null;
	}

	public void end()
	{
		isTransactionMode = false;
		txList = null;

		//Logger.debug.println(LOG_ID + "complete");
	}

}