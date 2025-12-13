package jdf.framework.core.data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.http.HttpAttributes;
import jdf.framework.core.log.Logger;


/**
 * 
 * JSP에서 &lt;jsp:useBean 에서 사용하기 위한 bean이다.
 * 
 * 
 * 
 * @author 성권
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InteractionBean {
	private final static String LOG_ID = "<f:InteractionBean> ";

	private boolean isCommit = false;

	/**
	 * HttpServletRequest에서 HttpAttributes를 얻는다.
	 * 
	 * 
	 */
	public HttpAttributes getHttpAttributes(HttpServletRequest req)
			throws Exception {
		return HttpAttributes.getInstance(req);

	}

	/**
	 * HttpSession에서 HttpAttributes를 얻는다.
	 * 
	 * 
	 */
	public HttpAttributes getHttpAttributes(HttpSession session) {
		return HttpAttributes.getInstance(session);
	}

	/**
	 * HttpServletReq에서 DataSet 객체를 얻는다.
	 * 
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public DataSet getDataSet(HttpServletRequest req) throws Exception {
		return getHttpAttributes(req).getDataSet();
	}

	/**
	 * HttpSession에서 DataSet 객체를 얻는다.
	 * 
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public DataSet getDataSet(HttpSession session) throws Exception {
		return getHttpAttributes(session).getDataSet();
	}

	/**
	 * 
	 * request에서 각 입력property에 따라 input DataSet을 만들고 trcode에 해당하는 io schema를
	 * 수행한다.
	 * 
	 * @param trcode
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public DataSet execute(String trcode, HttpServletRequest req)
			throws Exception {
		return execute(trcode, getDataSet(req));
	}

	/**
	 * input DataSet 과 trcode에 해당하는 io schema를 수행한다.
	 * 
	 * 
	 * @param trcode
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public DataSet execute(String trcode, Object inputObj)
			throws ResourceException {

		DataSet input = null;
		if (inputObj instanceof DataSet)
			input = (DataSet) inputObj;
		else
			input = new JavaBeanDataSet(inputObj);

		// boolean isLogEnable= Logger.debug.isPrintMode();
		boolean isLogEnable = false;
		long chkTime = 0;

		if (isLogEnable) {
			Logger.debug.println(LOG_ID + "[SEND:" + trcode + "] " + input);
			chkTime = System.currentTimeMillis();
		}
		Connection conn = null;
		DataSet output = null;

		try {
			conn = getConnection();

			Interaction interact = conn.createInteraction();

			output = interact.execute(trcode, input);

		} catch (ResourceException e) {
			throw e;
		} finally {
			close(conn);
		}
		if (isLogEnable) {
			chkTime = System.currentTimeMillis() - chkTime;
			Logger.debug.println(LOG_ID + "[RECV:" + trcode + "] [" + chkTime
					+ "] " + output);
		}

		return output;

	}

	/**
	 * input DataSet 과 trcode에 해당하는 io schema를 수행한다.
	 * 
	 * 
	 * @param trcode
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public void execute(String trcode, Object inputObj, Object outputObj)
			throws ResourceException {
		DataSet input = null;
		DataSet output = null;

		if (inputObj instanceof DataSet)
			input = (DataSet) inputObj;
		else
			input = new JavaBeanDataSet(inputObj);

		if (outputObj instanceof DataSet)
			output = (DataSet) outputObj;
		else
			output = new JavaBeanDataSet(outputObj);

		Connection conn = null;

		try {

			conn = getConnection();

			Interaction interact = conn.createInteraction();

			interact.execute(trcode, input, output);
		} catch (ResourceException e) {
			throw e;
		} finally {
			close(conn);

		}

	}

	private Connection conn;

	private boolean isTransactionMode = false;

	private Connection getConnection() throws ResourceException {
		if (this.isTransactionMode)
			return this.conn;

		Connection conn = DefaultConnectionFactory.getConnection();
		return conn;
	}

	private void close(Connection conn) throws ResourceException {

		if (this.isTransactionMode)
			return;

		if (conn != null)
			conn.close();
	}

	/**
	 * transaction 모드 시작
	 * 
	 * @throws ResourceException
	 */
	public void beginTransaction() throws ResourceException {
		this.conn = DefaultConnectionFactory.getConnection();
		this.conn.beginTransaction();
		this.isTransactionMode = true;
		this.isCommit = false;

	}

	/**
	 * rollback transaction
	 * 
	 * @throws ResourceException
	 */
	public void rollbackTransaction() throws ResourceException {
		try
		{
			if (this.conn != null)
				this.conn.rollbackTransaction();
		} catch(ResourceException ex)
		{
			throw ex;
		} finally
		{
			this.conn.close();
			this.isTransactionMode = false;
		}
		

		
	}

	/**
	 * commit transaction
	 * 
	 * @throws ResourceException
	 */
	public void commitTransaction() throws ResourceException {
		try
		{
			if (this.conn != null) 
				this.conn.commitTransaction();
		} catch(ResourceException ex)
		{
			throw ex;
		} finally
		{			
			this.conn.close();
			this.isTransactionMode = false;
			this.isCommit = true;
		}
		

		
	}

	/**
	 * 전에 commit에 수행이 안되고, 이 메쏘드가 수행된 경우는 rollback 시킨다.
	 */
	public void endTransaction() {
		if (!this.isCommit && this.conn != null) {
			try {
				this.conn.rollbackTransaction();
			} catch (ResourceException re) {

			}
		} else
		{
			if(conn != null)
			{
				try
				{
					this.conn.close();
					this.isTransactionMode = false;
				} catch(Exception ex){}
			}
		}
		
		
	}

}
