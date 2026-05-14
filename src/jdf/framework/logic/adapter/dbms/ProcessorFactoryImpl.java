package jdf.framework.logic.adapter.dbms;

import jdf.framework.core.Configuration;
import jdf.framework.core.data.schema.ProcedureMapper;
import jdf.framework.core.data.schema.Processor;
import jdf.framework.core.data.schema.StoredQuery;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.Utility;
import jdf.framework.core.xml.XMLReferer;
import jdf.framework.logic.spi.management.AbstractProcessorFactory;
import jdf.framework.logic.spi.management.IOSchemaContext;
import jdf.framework.logic.spi.process.BatchQueryOperator;
import jdf.framework.logic.spi.process.ProcedureQueryOperator;
import jdf.framework.logic.spi.process.QueryOperator;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProcessorFactoryImpl extends AbstractProcessorFactory
{
    private final static String LOG_ID = "<dbms:ProcessorFactoryImpl> ";

    /**
     *  
     */
    public ProcessorFactoryImpl()
    {
        super();

        Logger.info.println("anylogic.adapter.dbms.ProcessorFactoryImpl instance create");
    }

    /*
     * (non-Javadoc)
     * 
     * @see anylogic.spi.management.ProcessorFactory#getProcessor(anylogic.spi.management.IOSchemaContext)
     */
    public Processor getProcessor(IOSchemaContext ctx, Processor process) throws Exception
    {

        XMLReferer xmlDoc = ctx.getXMLReferer();

        xmlDoc.lookup("/transaction/processor-info");

        // processor 타입, DBMS,xml등등
        String type = xmlDoc.getString("type");

        String datasourceName= xmlDoc.getString("datasource");

        int typecode = Processor.UNDEFINED;

        if (process == null)
        {
            process = new DbmsProcessor();

        }

        //String transactionType= xmlDoc.find("transaction-type").getText();

        xmlDoc.lookup("/transaction/processor-info/sql");

        List<StoredQuery> list = new ArrayList<StoredQuery>();

        while (xmlDoc.next())
        {
            xmlDoc.mark();
            StoredQuery query = getStoredQuery(datasourceName, xmlDoc);

            list.add(query);

            Logger.debug.println(LOG_ID + "regist SQL ID : " + query.getName());

            xmlDoc.reset();
        }
        StoredQuery[] querys = (StoredQuery[]) list.toArray(new StoredQuery[list.size()]);

        process.setStoredQuery(querys);

        return process;

    }
    
    /**
     * DB datasource에 따른 db encoding 정보를 가져온다.
     * 
     * @param datasourceNm
     * @return
     */
    private String getDbEncoding(String datasourceNm)
    {
        try {
            
            // ex) ISO8859_1    <-- 전체를 적용하는 경우
            // ex2) dbPool:ISO8859_1,dbPool2:ISO8859_1    각 datasource 에 따른 틀린경우
            String tmp = Configuration.lookup("/resource/anylogic").getString("dbEncoding");
            
            if(tmp==null || tmp.length()==0)
                return null;
            
            
            String[] encodings = SmartStringArray.split(",", tmp);
            for(int i=0; i<encodings.length; i++) {
                String[] enc = SmartStringArray.split(":", encodings[i]);
                
                // ISO8859_1 처럼 앞에 dbPool 과 같이 아무것도 붙지 않은 경우
                // 모든 datasource에 동일하게 적용해야 한다.
                if(enc.length==1) {
                    return enc[0];
                }
                else {
                    if(datasourceNm.equals(enc[0]))
                        return enc[1];
                        
                }
                
                
            }
        }catch(Exception e) {
            
        }
        
        return null;
    }
    
    

    /**
     * xml 문서에서 StoredQuery 정보를 가져온다.
     *  
     */
    private StoredQuery getStoredQuery(String datasourceNm, XMLReferer reader) throws Exception
    {
        StoredQuery query = null;

        String queryType = reader.find("query").getString("type");
        
        String dbEncoding = getDbEncoding(datasourceNm);

        if ("storedprocedure".equals(queryType))
            query = new ProcedureQueryOperator(dbEncoding); // procedure 처리용
        else if("batch".equals(queryType))
            query = new BatchQueryOperator(dbEncoding);
        else
            query = new QueryOperator(dbEncoding); // 일반 query 처리

        query.setName(reader.getString("id"));

        String isLoop = reader.getString("loop");

        if ("true".equals(isLoop))
            query.setLoopQuery(true);

        query.setSqlQuery(reader.find("query").getText());

        query.setQueryType(reader.find("query").getString("type"));

        query.setDataSourceName(reader.getString("datasource"));

        try
        {
            query.setMaxRows(Integer.parseInt(reader.getString("max-rows")));
        } catch (Exception e)
        {
        }

        try
        {
            query.setFetchSize(Integer.parseInt(reader.getString("fetch-size")));
        } catch (Exception e)
        {
        }

        try
        {
            query.setQueryTimeOut(Integer.parseInt(reader.getString("time-out")));
        } catch (Exception e)
        {
        }
        
        String[] inFields = Utility.trim(SmartStringArray.split(",", reader.find("mapping/setProperty").getText()));
        String[] outFields = Utility.trim(SmartStringArray.split(",", reader.find("mapping/getProperty").getText()));

        query.setIntputFieldNames(inFields);
        query.setOutputFieldNames(outFields);

        reader.lookup("mapping-procedure/param");

        while (reader.next())
        {
            int seq = Integer.parseInt(reader.getString("seq"));
            int mode = "in".equals(reader.getString("mode")) ? ProcedureMapper.IN : ProcedureMapper.OUT;
            String typeNm = reader.getString("type");
            String sizeStr = reader.getString("size");
            String content = reader.getText();

            Logger.debug.println(LOG_ID + "set ProcedureMapper " + seq + " " + mode + " " + typeNm + " " + sizeStr
                    + " " + content);

            query.addProcedureMapper(new ProcedureMapper(seq, mode, typeNm, sizeStr, content));
        }

        return query;

    }

}