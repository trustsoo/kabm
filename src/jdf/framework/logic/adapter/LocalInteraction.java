package jdf.framework.logic.adapter;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.ConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.data.cci.SimpleConnectionSpec;
import jdf.framework.core.data.schema.Processor;
import jdf.framework.core.log.Logger;
import jdf.framework.core.pool.cache.CacheManager;
import jdf.framework.core.pool.cache.CacheManagerFactory;
import jdf.framework.core.util.StopWatch;
import jdf.framework.logic.spi.management.BLContextFactory;
import jdf.framework.logic.spi.management.BusinessLogicContext;

/**
 * Interaction 구현 class
 * 
 * @author
 * @version 1.0
 */
public class LocalInteraction implements Interaction
{
    private final static String LOG_ID = "<LocalInteraction> ";

    // 기본 Connection
    private LocalConnection base_conn;

    // transaction 관리
    private jdf.framework.logic.spi.transaction.TransactionManager txManager;

    // BL Context Factory
    private BLContextFactory blc_factory;

    // interaction
    private Interaction interact;

    /**
     * 기본생성자
     * 
     * @param conn
     */
    public LocalInteraction(LocalConnection conn)
    {
        this.base_conn = conn;
        this.txManager = conn.getTransactionManager();

        blc_factory = BLContextFactory.getInstance();
    }

    /**
     * 
     * trcode와 input 데이터에 따라 데이타 통신을 한다.
     *  
     */
    public DataSet execute(String trcode, DataSet input) throws ResourceException
    {
        Logger.debug.println(LOG_ID + "execute " + trcode);
        boolean isTransaction = false;
        
        StopWatch sw = new StopWatch("ADPT "+trcode);

        try
        {

            Connection conn = null;

            BusinessLogicContext blctx = blc_factory.getBusinessLogicContext(trcode);
            Processor prss = blctx.getIOSchema().getProcessor();
            
            if(prss==null)
                throw new ResourceException("해당 어댑터 정보를 가져오지 못했습니다. Resource Adapter 설정을 다시 확인하십시오.");

            CacheManager cmgr = prss.getCacheManager();
            DataSet clonInput = null;

            if (cmgr != null)
            {
                Object o = cmgr.call(input);

                if (o != null)
                {
                    DataSet cacheOut = (DataSet) o;

                    //return cacheOut;
                    return (DataSet) cacheOut.clone();
                }

                clonInput = (DataSet) input.clone();
                //clonInput.remove("javax.servlet.http.HttpServletRequest");
            }

            // 이미 TransactionManager에서 transaction을 시작했다면
            // begin 하거나 commit,rollback할 필요는 없다.
            if (!txManager.isTransactionMode())
            {
                isTransaction = blctx.isTransactionSupport();
            }

            if (isTransaction)
            {
                txManager.begin();
            }

            String datasource = getConnName(blctx);
            
            SimpleConnectionSpec connSpec = blctx.createSimpleConnectionSpec();
            // input에  세팅된 datasource 명을 가져온다.
            String datasourceNm = input.getProperty(DataSet.DB_DATASOURCE);
            if(datasourceNm!=null && datasourceNm.length()>0) {
                datasource = datasourceNm;
                connSpec.setProperty("datasource", datasourceNm);
            }
                
            

            conn = txManager.getConnection(datasource);

            if (conn == null)
            {

                ConnectionFactory conn_factory = blctx.getResourceAdapter().getConnectionFactory();
                
                

                
                conn = conn_factory.getConnection(connSpec);
                //conn = conn_factory.getConnection(blctx);

                conn.setBaseConnection(this.base_conn);
                this.base_conn.setConnectionImpl(conn);

                if (txManager.isTransactionMode())
                    txManager.join(conn);

                txManager.setConnection(datasource, conn);

            } else
                Logger.debug.println(LOG_ID + "Connection reuse. " + datasource);

            interact = conn.createInteraction();

            DataSet output = interact.execute(trcode, input);

            if (isTransaction)
                txManager.commit();

            if (cmgr != null)
            {
                cmgr.insert(clonInput, output);
            }

            if (prss.getResetCacheTrName() != null)
            {
                CacheManagerFactory cft = CacheManagerFactory.getInstance();
                cmgr = cft.getCacheManager(prss.getResetCacheTrName());
                if (cmgr != null)
                {
                    cmgr.reset();
                }

            }

            return output;

        } catch (ResourceException re)
        {
            Logger.err.println(LOG_ID + "interact err [" + trcode + "]", re);
            throw re;
        } finally
        {
            if (isTransaction)
                txManager.rollback();
            
            sw.printLog();
        }

    }

    private String getConnName(BusinessLogicContext blctx)
    {
        return blctx.getResourceAdapter().getClass().getName() + ":"
                + blctx.getProperty("datasource");
    }

    /**
     * 실제 로직을 수행한다.
     * 
     * @roseuid 3ECDEA770263
     */
    public void execute(String trcode, DataSet input, DataSet output) throws ResourceException
    {

        Logger.debug.println(LOG_ID + "execute " + trcode);

        boolean isTransaction = false;

        try
        {

            Connection conn = null;

            // trcode에 해당하는 BusinessLogic Context를 가져온다.
            BusinessLogicContext blctx = blc_factory.getBusinessLogicContext(trcode);

            // 이미 TransactionManager에서 transaction을 시작했다면
            // begin 하거나 commit,rollback할 필요는 없다.
            if (!txManager.isTransactionMode())
            {
                isTransaction = blctx.isTransactionSupport();
            }

            if (isTransaction)
            {
                txManager.begin();
            }

            // business logic context에서 고유의 connection 명을 가져온다
            String datasource = getConnName(blctx);
            
            SimpleConnectionSpec connSpec = blctx.createSimpleConnectionSpec();
            // input에  세팅된 datasource 명을 가져온다.
            String datasourceNm = input.getProperty(DataSet.DB_DATASOURCE);
            if(datasourceNm!=null && datasourceNm.length()>0) {
                datasource = datasourceNm;
                connSpec.setProperty("datasource", datasourceNm);
            }

            // transaction manager에서 해당 datasource의 connection을 가져온다.
            conn = txManager.getConnection(datasource);

            // transaction manager에 해당 connection이 없으면
            // 새롭게 가져온다.
            if (conn == null)
            {

                ConnectionFactory conn_factory = blctx.getResourceAdapter().getConnectionFactory();
                
                //conn = conn_factory.getConnection(blctx);
                conn = conn_factory.getConnection(connSpec);
                conn.setBaseConnection(this.base_conn);
                this.base_conn.setConnectionImpl(conn);

                if (txManager.isTransactionMode())
                    txManager.join(conn);

                txManager.setConnection(datasource, conn);

            } else
                Logger.debug.println(LOG_ID + "Connection reuse. " + datasource);

            interact = conn.createInteraction();

            interact.execute(trcode, input, output);

            if (isTransaction)
                txManager.commit();

        } catch (ResourceException re)
        {
            Logger.err.println(LOG_ID + "interact err [" + trcode + "]", re);
            throw re;
        } finally
        {
            if (isTransaction)
                txManager.rollback();
        }

    }

    /**
     * Connection 구현체를 얻는다.
     * 
     * @roseuid 3ECDEA770267
     */
    public Connection getConnection()
    {
        return base_conn;
    }

    /**
     * Connection 종료한다.
     * 
     * @roseuid 3ECDEA770271
     */
    public void close() throws ResourceException
    {
        if (interact != null)
            interact.close();
    }
}