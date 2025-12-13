package jdf.framework.logic.transform;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.StringFormater;


/**
 * JSON 기반의 변환기
 * 
 * http://www.json.org/
 * 
 * 
 * @author
 * 
 */
public class DynamicTreeJSONTransformerImpl extends TransformerBase
{
	private final static String LOG_ID = "<l:DynamicTreeJSONTransformerImpl> ";

	DynamicTreeJSONTransformerImpl() {
		Logger.debug.println(LOG_ID + "객체 생성");

	}

	
	public int transform(DataSet source, Writer writer) throws TransformerException
	{
		IOSchema schema = source.getIOSchema();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(writer);

			printDataSet(source, schema, pw);

			writer.flush();
			
			pw.flush();
			pw.close();
			
		} catch (IOException ioe) {
			throw new TransformerException("JSON transform error", ioe);
		} finally
		{
			if(pw != null) try{pw.close();}catch(Exception ex){}
		}

		// 사이즈 정보를 모르기 때문에.
		return -1;
	}

	/**
     * 
     * @param source
     * @param schema
     * @param pw
     * @throws IOException
     */
	private void printDataSet(DataSet source, IOSchema schema, PrintWriter pw) throws IOException
	{
		// NULL 은 그대로 null 이 return 되게한다.
		source.unfixNull();

		// output 에 대한 변환을 하므로 output block 정보를 가져온다.
		Block[] blocks = schema.getOutputBlocks();


		for (int i = 0; i < blocks.length; i++) {
			String blockName = blocks[i].getName();

			// block 안의 data 출력
			printBlockData(source, blocks[i], pw);
			
			/*2008.07.03 added by advan94@gmail.com*/
			if(i != blocks.length - 1)
				pw.print(",");
		}
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

		// 배열이므로
		pw.println("[");

		String firstKeyName = fields[0].getName();
		
		if(fields[0].getProperty("datasetPrefix") != null)
		{
			firstKeyName = fields[0].getProperty("datasetPrefix") + firstKeyName.trim();
			
			//System.out.println(block.getName()+" firstKeyName:"+firstKeyName+" .count:"+source.getCount(firstKeyName));
			
		}
		
		int iterationNum = block.getIterationNum();
		if(iterationNum > source.getCount(firstKeyName))
		{
			iterationNum = source.getCount(firstKeyName);
		}

		//for (int j = 0; j < source.getCount(firstKeyName); j++) {
		for (int j = 0; j < iterationNum; j++) {

			if (j > 0)
				pw.println(",");
			pw.println("{");

			for (int i = 0; i < fields.length; i++) {

				if (i > 0)
					pw.println(",");

				String keyName = fields[i].getName();
				
				if(fields[i].getProperty("datasetPrefix") != null)
				{
					keyName = fields[i].getProperty("datasetPrefix") + keyName.trim();
				}
				
				//System.out.println(block.getName()+" keyName:"+keyName);
				
				pw.print("\"");
				pw.print(keyName);
				pw.print("\":");

				String val = source.getText(keyName, j);
				if (val != null) {
					pw.print("\"");
					//pw.print(java.net.URLEncoder.encode(val, defaultEncoding));
					//pw.print(val);
					pw.print(org.json.simple.JSONObject.escape(StringFormater.contentFilter(val)));
					//pw.print(org.json.simple.JSONObject.escape(val));
					//pw.print(StringFormater.replaceStr(HtmlFormat.translateNewline(val, "\\n"), "\"", "\\\""));
					//pw.print(StringFormater.replaceStr(val, "\"", "\\\""));
					//pw.print(HtmlFormat.translateJson(val));
					pw.print("\"");
				} else
				{
					pw.print("\"");
					pw.print("\"");
				}

			}
			pw.println("}");
		}

		pw.println("]");

	}
}
