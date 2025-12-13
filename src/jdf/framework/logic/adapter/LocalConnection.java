//Source file: D:\\PROJECT\\anylogic\\prototype\\src\\anylogic\\adapter\\BaseConnection.java

package jdf.framework.logic.adapter;

import java.util.*;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.ConnectionSpec;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.logic.spi.transaction.TransactionManager;


/**
 * 같은 VM 상테서 connection을 얻고자 하는 경우 사용하는 class
 * 
 * 
 * @author
 * @version 1.0
 */
public class LocalConnection extends AbstractConnection
{

	private TransactionManager txManager;
	
	private ConnectionSpec connSpec;

	// connection  객체
	private Connection conn;

	private List connList = new ArrayList();


    /**
     * 기본 생성자
     *
     */
	public LocalConnection()
	{
		this.setBaseConnection(this);
		txManager = new TransactionManager();
	}
	
    /**
     * 기본생성자
     * 
     * @param connSpec
     */
	public LocalConnection(ConnectionSpec connSpec)
	{
	    this();
	    this.connSpec=connSpec;
	    
	}

    /**
     * 기본생성자
     * @param txManager
     */
	public LocalConnection(TransactionManager txManager)
	{
		this.setBaseConnection(this);
		this.txManager = txManager;
	}
	
	/**
     * ConnectionSpec 구현객체를 얻는다.
     * 
     * @return
	 */
	public ConnectionSpec getConnectionSpec()
	{
	    return this.connSpec;
	}

	/**
	 * Connection 종료
     * 
	 * @see jdf.framework.core.data.cci.Connection#close()
	 */
	public void close() throws ResourceException
	{
		txManager.end();
		
		ResourceException error = null;

		for (int i = 0; i < connList.size(); i++)
		{
			try
			{
				Connection conn = (Connection) connList.get(i);

				conn.close();

			}
			catch (ResourceException re)
			{
				error = re;
			}
			catch (Exception e)
			{
				error = new ResourceException(e.getMessage());
			}

		}

		if (error != null)
			throw error;
	}

	/**
	 * Interaction 구현객체 생성
	 * 
	 * @see jdf.framework.core.data.cci.Connection#createInteraction()
	 */
	public Interaction createInteraction() throws ResourceException
	{
		return new LocalInteraction(this);
	}

	/**
	 * transaction 시작
     * 
	 * @see jdf.framework.core.data.cci.Connection#beginTransaction()
	 */
	public void beginTransaction() throws ResourceException
	{
		txManager.begin();
	}

	/**
	 * transaction 완료
     * 
	 * @see jdf.framework.core.data.cci.Connection#commitTransaction()
	 */
	public void commitTransaction() throws ResourceException
	{
		txManager.commit();
	}

	/**
	 * transaction 실패시 원복
     * 
	 * @see jdf.framework.core.data.cci.Connection#rollbackTransaction()
	 */
	public void rollbackTransaction() throws ResourceException
	{
		txManager.rollback();

	}
	
	public void endTransaction() throws ResourceException
	{
		txManager.end();
	}

	TransactionManager getTransactionManager()
	{
		return this.txManager;
	}

	void setConnectionImpl(Connection conn)
	{
		this.conn = conn;
		connList.add(conn);
	}

}
