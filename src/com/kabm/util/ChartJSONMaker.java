package com.kabm.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.HtmlFormat;
import jdf.framework.core.util.StringFormater;


/**
 * JSON 
 * 
 * http://www.json.org/
 * 
 * 
 * @author 
 * 
 */
public class ChartJSONMaker 
{
	private final static String LOG_ID = "<l:ChartJSONMaker> ";
	private final static String DATA_NM = "isDataNm";
	private final static String DATA_ID = "isDataId";
		
	public final static int LINE_CHART = 1;
	public final static int BAR_CHART = 2;
	public final static int PIE_CHART = 3;
	public final static int BUBBLE_CHART = 4;
	
	public int transform(int chartDiv, DataSet source, HttpServletResponse resp) 
	{
		IOSchema schema = source.getIOSchema();
		PrintWriter pw = null;
		try {
			
				resp.reset();			
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				pw = resp.getWriter();
				
				if( this.LINE_CHART == chartDiv)
					printLineDataSet(source, schema, pw);
				else if( this.BAR_CHART == chartDiv)
					printBarDataSet(source, schema, pw);
				else if( this.PIE_CHART == chartDiv)
					printPieDataSet(source, schema, pw);
				else if( this.BUBBLE_CHART == chartDiv)
					printBubbleDataSet(source, schema, pw);
				
				pw.flush();
			
		} catch (IOException ioe) {
			Logger.err.println(LOG_ID + "transform 에러!");
		}finally{
			if(pw != null ) pw.close();
		}

		
		return -1;
	}
	
	public int transform(int chartDiv, DataSet source, Writer writer) 
	{
		IOSchema schema = source.getIOSchema();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(writer);
			
			if( this.LINE_CHART == chartDiv)
				printLineDataSet(source, schema, pw);
			else if( this.BAR_CHART == chartDiv)
				printBarDataSet(source, schema, pw);
			else if( this.PIE_CHART == chartDiv)
				printPieDataSet(source, schema, pw);
			else if( this.BUBBLE_CHART == chartDiv)
				printBubbleDataSet(source, schema, pw);
			

			writer.flush();

		} catch (IOException ioe) {
			Logger.err.println(LOG_ID + "transform 에러!");
		}finally{
			if(pw != null ) pw.close();
		}

		
		return -1;
	}
	
	/**
     * 
     * @param source
     * @param schema
     * @param pw
     * @throws IOException
     */
	private void printLineDataSet(DataSet source, IOSchema schema, PrintWriter pw) throws IOException
	{
		// NULL 
		source.unfixNull();

		// output 
		Block[] blocks = schema.getOutputBlocks();

		pw.println("[");
		for (int i = 0; i < blocks.length; i++) {
			String blockName = blocks[i].getName();

			printBlockData(source, blocks[i], pw);
			
			if(i != blocks.length - 1)
				pw.print(",");
		}
		
		pw.println("\n]");
	}
	
	/**
     * 
     * @param source
     * @param schema
     * @param pw
     * @throws IOException
     */
	private void printBarDataSet(DataSet source, IOSchema schema, PrintWriter pw) throws IOException
	{
		// NULL 
		source.unfixNull();

		// output 
		Block[] blocks = schema.getOutputBlocks();

		pw.println("[");
		for (int i = 0; i < blocks.length; i++) {
			String blockName = blocks[i].getName();

			printBlockDataReverse(source, blocks[i], pw);
			
			if(i != blocks.length - 1)
				pw.print(",");
		}
		
		pw.println("\n]");
	}
	
	/**
     * 
     * @param source
     * @param schema
     * @param pw
     * @throws IOException
     */
	private void printPieDataSet(DataSet source, IOSchema schema, PrintWriter pw) throws IOException
	{
		// NULL 
		source.unfixNull();

		// output 
		Block[] blocks = schema.getOutputBlocks();

		pw.println("[");
		for (int i = 0; i < blocks.length; i++) {
			String blockName = blocks[i].getName();

			printBlockData(source, blocks[i], pw);
			
			if(i != blocks.length - 1)
				pw.print(",");
		}
		
		pw.println("\n]");
	}
	
	/**
     * 
     * @param source
     * @param schema
     * @param pw
     * @throws IOException
     */
	private void printBubbleDataSet(DataSet source, IOSchema schema, PrintWriter pw) throws IOException
	{
		// NULL 
		source.unfixNull();

		// output 
		Block[] blocks = schema.getOutputBlocks();

		pw.println("[");
		for (int i = 0; i < blocks.length; i++) {
			String blockName = blocks[i].getName();

			printBlockData(source, blocks[i], pw);
			
			if(i != blocks.length - 1)
				pw.print(",");
		}
		
		pw.println("\n]");
	}
	
	/**
     * 
     * @param source
     * @param block
     * @param pw
     * @throws IOException
     */
	private void printBlockData(DataSet source, Block block, PrintWriter pw) throws IOException
	{

		Field[] fields = block.getFields();
		if (fields.length == 0)
			return;
		StringBuffer sb = new StringBuffer();
		
    	String firstKeyName = fields[0].getName();			
    	String data_nm_key = null;
    	String data_nm = null;
    	String data_id = null;
    	String state = null;
    	
    	for (int i = 0; i < fields.length; i++) {
    		
    		if (i > 0)
    			sb.append(",").append("\n");
    		else
    			sb.append("");
    		sb.append("[ ");
    		
    		
    		Field field = fields[i];
    		String keyName = fields[i].getName();
    	
	    	for (int j = 0; j < source.getCount(firstKeyName); j++) {
	    		
	    		
	    		if (j > 0)
					sb.append(", ");
	    			
	    			String val = source.getText(keyName, j);			
	    			val = StringFormater.replaceStr(HtmlFormat.translateNewline(val, "\\n"), "\"", "\\\"");
	    			sb.append("")
	    			.append(val);
	    			
	    			if(field.getProperty(DATA_NM) != null && "true".equals(field.getProperty(DATA_NM)))
	    			{
	    				data_nm = val;
	    			}	
	    			
	    			if(field.getProperty(DATA_ID) != null && "true".equals(field.getProperty(DATA_ID)))
	    			{
	    				sb.append(",")
	    				.append(val);
	    			}
	    	}
	    	sb.append("]");
    	}
		
		
    	pw.print(sb.toString());

	}
	
	/**
     * 
     * @param source
     * @param block
     * @param pw
     * @throws IOException
     */
	private void printBlockDataReverse(DataSet source, Block block, PrintWriter pw) throws IOException
	{

		Field[] fields = block.getFields();
		if (fields.length == 0)
			return;
		StringBuffer sb = new StringBuffer();
		
		String firstKeyName = fields[0].getName();			
    	String data_nm_key = null;
    	String data_nm = null;
    	String data_id = null;
    	String state = null;
    	
    	for (int i = 0; i < fields.length; i++) {
    		
    		if (i > 0)
    			sb.append(",").append("\n");
    		else
    			sb.append("");
    		sb.append("[ ");
    		
    		
    		Field field = fields[i];
    		String keyName = fields[i].getName();
    	
	    	for (int j = 0; j < source.getCount(firstKeyName); j++) {
	    		
	    		
	    		if (j > 0)
					sb.append(", ");
	    			
	    			String val = source.getText(keyName, j);			
	    			val = StringFormater.replaceStr(HtmlFormat.translateNewline(val, "\\n"), "\"", "\\\"");
	    			sb.append("")
	    			.append(val);
	    			
	    			if(field.getProperty(DATA_NM) != null && "true".equals(field.getProperty(DATA_NM)))
	    			{
	    				data_nm = val;
	    			}	
	    			
	    			if(field.getProperty(DATA_ID) != null && "true".equals(field.getProperty(DATA_ID)))
	    			{
	    				sb.append(",")
	    				.append(val);
	    			}
	    	}
	    	sb.append("]");
    	}
		
		
    	pw.print(sb.toString());

	}
	
}