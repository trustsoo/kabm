package jdf.framework.logic.adapter.dbms;

import java.sql.SQLException;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.log.Logger;
import jdf.framework.logic.spi.management.BLContextFactory;


/*
 * Interaction을 구현한 class로 일반적인 DBMS를 통해 query를 수행해 주는 역활을 한다.
 * 
 * 
 * 예제 <pre> import core.data.DataSet; import core.data.cci.Connection;
 * import core.data.cci.Interaction; import
 * jdf.framework.core.data.cci.ConnectionFactory; import
 * jdf.framework.core.data.cci.DefaultConnectionFactory;
 * 
 * 
 * String trcode; // trasaction id DataSet input; // input값 정의
 * 
 * Connection conn = DefaultConnectionFactory.getConnection(); Interaction
 * interact = conn.createInteraction();
 * 
 * DataSet output = interact.execute(trcode, input);
 * 
 * </pre>
 * 
 * 
 * 
 * @author
 * 
 * @version 1.0 2003/02/01 
 * @since JDK1.2
 */

public class DbmsInteraction implements Interaction
{

    private jdf.framework.logic.adapter.dbms.ConnectionImpl conn;

    private BLContextFactory mgr = BLContextFactory.getInstance();

    public DbmsInteraction(jdf.framework.core.data.cci.Connection conn)
    {
        this.conn = (ConnectionImpl) conn;
    }

    public DataSet execute(String trcode, DataSet input) throws ResourceException
    {
        //		스키마정보
        IOSchema schema = mgr.getIOSchema(trcode);

        DataSet output = schema.getOutputDataSetInstance();

        execute(trcode, input, output);

        return output;
    }

    /**
     * 실행을 한다.
     */
    public void execute(String trcode, DataSet input, DataSet output) throws ResourceException
    {

        boolean autoCommit = true;
        java.sql.Connection sql_conn = null;

        try
        {

            // 스키마정보
            IOSchema schema = mgr.getIOSchema(trcode);

            input.setIOSchema(schema, IOSchema.IN);
            output.setIOSchema(schema, IOSchema.OUT);

            // 데이타 처리를 위한 processor
            //Processor prss= schema.getProcessor();
            DbmsProcessor prss = (DbmsProcessor) schema.createProcessor();

            if (prss == null)
                throw new ResourceException("processor infomation not exist");

            sql_conn = this.conn.getSqlConnection();
            prss.setJdbcConnection(sql_conn);
            prss.setDataSetConnection(conn);

            prss.execute(input, output);

        } catch (ResourceException re)
        {
            throw re;
        } catch (SQLException sqex)
        {
            //sqex.printStackTrace();
            Logger.err.println("<DbmsInteraction> execute err [" + trcode + "]", sqex);
            throw new ResourceException(sqex);
        } catch (Exception ex)
        {
            //ex.printStackTrace();
            Logger.err.println("<DbmsInteraction> execute err [" + trcode + "]", ex);
            throw new ResourceException(ex.getMessage());
        }

        //return output;

    }

    /**
     * @see jdf.framework.core.data.cci.Interaction#close()
     */
    public void close() throws ResourceException
    {
    }

    /**
     * @see jdf.framework.core.data.cci.Interaction#getConnection()
     */
    public jdf.framework.core.data.cci.Connection getConnection()
    {
        return conn;
    }

    public static void main(String[] args) throws Exception
    {

        System.out.println("testst");
        System.out.println("testst");
        System.out.println("testst");

    }

}