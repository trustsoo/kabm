package jdf.framework.logic.adapter.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import jdf.framework.core.ConfigurationException;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.log.Logger;
import jdf.framework.core.log.LoggerFactory;
import jdf.framework.core.log.LoggerWriter;
import jdf.framework.logic.spi.management.BLContextFactory;



public class HttpInteraction implements Interaction
{
	private static LoggerWriter logger = null;
	static
	{
		try
		{
			logger = LoggerFactory.getLoggerWriter("legacy");
		} catch(ConfigurationException ce)
		{
			
		}
	}
	
	private ConnectionImpl conn;
	
	private BLContextFactory mgr = BLContextFactory.getInstance();
	
	HttpInteraction(ConnectionImpl conn)
	{
		this.conn = conn;
	}

	@Override
	public void close() throws ResourceException {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return this.conn;
	}

	@Override
	public DataSet execute(String trcode, DataSet input) throws ResourceException 
	{
		// TODO Auto-generated method stub
		IOSchema schema = mgr.getIOSchema(trcode);

        DataSet output = schema.getOutputDataSetInstance();

        execute(trcode, input, output);

        return output;
	}

	@Override
	public void execute(String trcode, DataSet input, DataSet output)
			throws ResourceException 
	{
		
		DataSet fixedInput = new DataSet();
		
		try
        {
		
			 // 스키마정보
	        IOSchema schema = mgr.getIOSchema(trcode);
	
	        input.setIOSchema(schema, IOSchema.IN);
	        output.setIOSchema(schema, IOSchema.OUT);
	
	        // 데이타 처리를 위한 processor
	        //Processor prss= schema.getProcessor();
	        HttpProcessor prss = (HttpProcessor) schema.createProcessor();
	
	        if (prss == null)
	            throw new ResourceException("processor infomation not exist");
	        
	        prss.execute(input, output);
	        
        } catch(ClientProtocolException ce)
        {        	
        	Logger.err.println("<HttpInteraction> execute err [" + trcode + "]", ce);
        	logger.println("<HttpInteraction> execute err [" + trcode + "]", ce);
        	String errorCode = ce.getMessage();
        	int responseCode = 0;
        	try
        	{
        		responseCode = Integer.parseInt(ce.getMessage());
        	} catch(Exception ex)
        	{}
        	
        	
        	throw new ResourceException("기간계 API Server 측 오류입니다.", responseCode);
        } catch(ResourceException re)
        {
        	throw re;
        } catch(Exception ex)
        {
        	Logger.err.println("<HttpInteraction> execute err [" + trcode + "]", ex);
        	logger.println("<HttpInteraction> execute err [" + trcode + "]", ex);
        	throw new ResourceException(ex.getMessage());
        }
	}	
	
	
}