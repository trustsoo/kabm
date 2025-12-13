package jdf.framework.logic.spi.transaction;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.log.Logger;



/**
 * JTA 기반의 Transaction 관리
 * 
 * 
 * @author
 * @version 1.0
 * @since 2007-06-04 오전 10:12:13
 * 
 */
public class JTATransactionManager {
	private final static String LOG_ID = "<l:JTATransactionManager> ";

	private static Context ctx;

	private UserTransaction ut;

	private static int timeout = 60;

	static {
		try {
			Config conf = Configuration.lookup("/resource/anylogic");
			int to = conf.getInt("jtaTimeout"); // timeout
			if (to > 0)
				timeout = to;
		} catch (ConfigurationException ce) {

		}
	}

	/**
	 * 
	 * 
	 */
	public JTATransactionManager() {

		try {
			if (ctx == null)
				ctx = new InitialContext();

			ut = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
			ut.setTransactionTimeout(timeout);

		} catch (NamingException ne) {
			Logger.err.println(LOG_ID + "init err", ne);
		} catch (SystemException se) {
			Logger.err.println(LOG_ID + "init err", se);
		}
	}

	/**
	 * 
	 * @throws ResourceException
	 */
	public void begin() throws ResourceException {
		try {
			if (this.ut != null) {
				this.ut.begin();

			}
		} catch (NotSupportedException nse) {
			throw new ResourceException(nse);

		} catch (SystemException se) {
			throw new ResourceException(se);
		}
	}

	/**
	 * commit 한다.
	 * 
	 * @see jdf.framework.core.data.cci.LocalTransaction#commitTransaction()
	 */
	public void commit() throws ResourceException {

		try {
			if (this.ut != null) {
				this.ut.commit();
				return;
			}
		} catch (HeuristicMixedException nse) {
			throw new ResourceException(nse);

		} catch (HeuristicRollbackException hre) {
			throw new ResourceException(hre);
		} catch (RollbackException re) {
			throw new ResourceException(re);
		} catch (SystemException se) {
			throw new ResourceException(se);
		}

	}

	/**
	 * rollback을 한다.
	 * 
	 * @see jdf.framework.core.data.cci.LocalTransaction#rollbackTransaction()
	 */
	public void rollback() {
		try {
			if (this.ut != null) {
				this.ut.rollback();
				return;
			}
		} catch (SystemException se) {

		}

	}
}