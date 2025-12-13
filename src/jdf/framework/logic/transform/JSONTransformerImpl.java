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
public class JSONTransformerImpl extends TransformerBase
{
	private final static String LOG_ID = "<l:JSONTransformerImpl> ";

	JSONTransformerImpl() {
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

		pw.print("{");

		for (int i = 0; i < blocks.length; i++) {
			String blockName = blocks[i].getName();

			pw.print("\"");
			pw.print(blockName);
			pw.print("\":");

			// block 안의 data 출력
			printBlockData(source, blocks[i], pw);
			
			/*2008.07.03 added by advan94@gmail.com*/
			if(i != blocks.length - 1)
				pw.print(",");
		}
		
		
		String json_type = source.getText(TransFormConstant.JSON_DATA_TYPE);
		
		if(json_type != null && "jqgrid".equals(json_type))
		{		
		
			pw.print(",");
			pw.print("\"");
			pw.print("model_info");
			pw.print("\"");
			pw.print(":");
			pw.println("[");
			pw.println("{");
			for (int i = 0; i < blocks.length; i++) 
			{
				String blockName = blocks[i].getName();
				
				pw.print("\"");
				pw.print(blockName);
				pw.print("\":");
				pw.println("[");			
				pw.println("{");
				printHeaderData(blocks[i], pw);
				
				pw.println("}");
				pw.println("]");			
				
				
				if(i != blocks.length - 1)
					pw.print(",");
				
			}
			
			pw.println("}");
			pw.println("]");
		}
		
		pw.print("}");
	}
	
	
	
	private void printHeaderData(Block block, PrintWriter pw)
	{
		Field[] fields = block.getFields();
		if (fields.length == 0)
			return;
		
		List<StringBuffer> colModels = new ArrayList<StringBuffer>();
		List<StringBuffer> labels = new ArrayList<StringBuffer>();
		
		
		StringBuffer colModelBuffer = new StringBuffer();
		StringBuffer labelBuffer = new StringBuffer();
		
		for(int i=0; i<fields.length; i++)
		{
			String keyName = fields[i].getName();	
			String label = fields[i].getLabel() == null ? keyName : fields[i].getLabel();
			
			Properties p = null;
			
			try
			{			
				boolean pass = false;
				p = fields[i].getProperties();
				
				//hidden상태 판단.
				Enumeration<Object> hchk = p.keys();
				while(hchk.hasMoreElements())
				{
					String k = (String)hchk.nextElement();
					String val = p.getProperty(k);
					
					if(k.equals("hidden") && val.equals("true")){
						pass = true;
						break;
					}
				}
				
				if(pass)
					continue;
				
			} catch(Exception ex)
			{
				
			}
			
			colModelBuffer.append("{");
			
			if(p != null)
			{
				if(!p.containsKey("index"))
				{
					colModelBuffer.append("\"")
								  .append("index")
								  .append("\"")
								  .append(":")
								  .append("\"")
								  .append(keyName)
								  .append("\",");
				}
			} else
			{
				colModelBuffer.append("\"")
				  .append("index")
				  .append("\"")
				  .append(":")
				  .append("\"")
				  .append(keyName)
				  .append("\",");
			}
			
			if(p != null)
			{
				if( !p.containsKey("name"))
				{
					colModelBuffer.append("\"")
					  .append("name")
					  .append("\"")
					  .append(":")
					  .append("\"")
					  .append(keyName)
					  .append("\"");
				}
			} else
			{
				colModelBuffer.append("\"")
				  .append("name")
				  .append("\"")
				  .append(":")
				  .append("\"")
				  .append(keyName)
				  .append("\"");				
				
			}
			
			if(p != null)
			{
			
				colModelBuffer.append(",");
				Enumeration<Object> enu = p.keys();
				while(enu.hasMoreElements())
				{
					String k = (String)enu.nextElement();
					String val = p.getProperty(k);
					
					colModelBuffer.append("\"")
								  .append(k)
								  .append("\"")							  
								  .append(":")
								  .append("\"")
								  .append(val)
								  .append("\"")
								  .append(",");				
				}			
				
			
				colModelBuffer.delete(colModelBuffer.length()-1, colModelBuffer.length());
				
			}
			
			labelBuffer.append("\"")
					   .append(label)
					   .append("\"");
			
			colModelBuffer.append("}");
			colModels.add(colModelBuffer);
			labels.add(labelBuffer);		
			
			colModelBuffer = new StringBuffer();
			labelBuffer = new StringBuffer();
		}
		
		pw.write("\"");
		pw.write("colModelList");
		pw.write("\"");
		pw.write(":[");
		for(int idx=0; idx<colModels.size(); idx++)
		{
			pw.write(colModels.get(idx).toString());
			
			if(idx != colModels.size() - 1)
				pw.print(",");
		}
		pw.write("],");
		
		
		pw.write("\"");
		pw.write("columnNames");
		pw.write("\"");
		pw.write(":[");
		for(int idx=0; idx<labels.size(); idx++)
		{
			pw.write(labels.get(idx).toString());
			
			if(idx != labels.size() - 1)
				pw.print(",");
		}
		pw.write("]");
		
		
	}
	
	
	private void printLabelData(Block block, PrintWriter pw)
	{
		Field[] fields = block.getFields();
		if (fields.length == 0)
			return;

		// 배열이므로
		pw.println("[");
		
		pw.println("{");

		for (int i = 0; i < fields.length; i++) {

			if (i > 0)
				pw.println(",");

			String keyName = fields[i].getName();
			pw.print("\"");
			pw.print(keyName);
			pw.print("\":");

			
			
			String val = fields[i].getLabel() == null ? keyName :  fields[i].getLabel();
			if (val != null) {
				pw.print("\"");
				//pw.print(java.net.URLEncoder.encode(val, defaultEncoding));
				pw.print(val);
				pw.print("\"");
			}

		}
		pw.println("}");
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
					if(fields[i].getFormat() == null || fields[i].getFormat().indexOf("unescapeHtml") <= -1)
						pw.print(org.json.simple.JSONObject.escape(StringFormater.contentFilter(val)));
					else
						pw.print(org.json.simple.JSONObject.escape(val));
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
