package jdf.framework.logic.transform;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.HtmlFormat;
import jdf.framework.core.util.StringFormater;


/**
 * JSON 湲곕컲��蹂�솚湲�
 * 
 * http://www.json.org/
 * 
 * 
 * @author 
 * 
 */
public class TreeJSONTransformerImplEx extends TransformerBase
{
	private final static String LOG_ID = "<l:TreeJSONTransformerImplEx> ";
	
	private final static String DATA_NM = "isDataNm";
	private final static String DATA_NM_TEMP_KEY = "@tempDataNm";
	private final static String DATA_ID = "isDataId";
	private final static String CHECK_SUBDATA_FILED = "is_exsit_sub";
	
	TreeJSONTransformerImplEx() {
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

		} catch (IOException ioe) {
			throw new TransformerException("JSON transform error", ioe);
		} finally
		{
			if(pw != null) try{pw.close();}catch(Exception ex){}
		}

		// �ъ씠利��뺣낫瑜�紐⑤Ⅴ湲��뚮Ц��
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
		// NULL ��洹몃�濡�null ��return �섍쾶�쒕떎.
				source.unfixNull();

				// output ����븳 蹂�솚���섎�濡�output block �뺣낫瑜�媛�졇�⑤떎.
				Block[] blocks = schema.getOutputBlocks();

				pw.println("[");
				for (int i = 0; i < blocks.length; i++) {
					String blockName = blocks[i].getName();

					/*pw.print("\"");
					pw.print(blockName);
					pw.print("\":");*/

					// block �덉쓽 data 異쒕젰
					printBlockData(source, blocks[i], pw);
					
					/*2008.07.03 added by advan94@gmail.com*/
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
		
/*
 * {
    "data":{"icon":"/img/file.png","title":"CRM팀"},
    "attributes":{"id":"2","flag":"D","level":"1","dept_cd":"CRM","p_dept_no":"1","sort_no":"1","rel":"file","use_yn":"Y"}
  }
 * */
		String firstKeyName = fields[0].getName();			
		String data_nm_key = null;
		String data_nm = null;
		String data_id = null;
		String state = null;
		String parent = null;
		String id = null;
		String type = null;
		String starting_point = source.getText("starting_point");
		for (int j = 0; j < source.getCount(firstKeyName); j++) {
			
			
			if (j > 0)
				sb.append(",").append("\n");
			else
				sb.append("");
			sb.append("{ ")			
			  .append("\"li_attr\" : {");
			
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (i > 0)
					sb.append(", ");
				
				String keyName = fields[i].getName();
				String val = source.getText(keyName, j);
				if(i==2){
					parent =  val.equals(starting_point)?"#":val;
					state = val.equals(starting_point)?"open":"closed";
				}else if(i==0){
					id = val;
				}else if("rel".equals(keyName)){
					type = val;
				}
				val = StringFormater.replaceStr(HtmlFormat.translateNewline(val, "\\n"), "\"", "\\\"");
				sb.append("\"")
				.append(keyName)
				.append("\":")
				.append("\"")
				.append(val)
				.append("\"");
				
				if(field.getProperty(DATA_NM) != null && "true".equals(field.getProperty(DATA_NM)))
				{
					data_nm = val;
				}	
				
				if(field.getProperty(DATA_ID) != null && "true".equals(field.getProperty(DATA_ID)))
				{
					sb.append(", \"")
					.append("id")
					.append("\":")
					.append("\"")
					.append(val)
					.append("\"");
				}				
				
			}
			sb.append("},");
			sb.append("\"text\" : \""+DATA_NM_TEMP_KEY+"\",")
			.append("\"parent\" : \""+parent+"\",")
			.append("\"id\" : \""+id+"\",")
			.append("\"type\" : \""+type+"\",")
//			.append("\"data-jstree\" :"+"{\"type\" : \"default\"},")
			//  .append("\t\t\"data\" : {\"title\" : \""+DATA_NM_TEMP_KEY+"\"}, ")
			.append("\"state\":\""+state+"\"");
			sb.replace(sb.indexOf(DATA_NM_TEMP_KEY), DATA_NM_TEMP_KEY.length()+sb.indexOf(DATA_NM_TEMP_KEY), data_nm);
			sb.append("}");
		}
		pw.print(sb.toString());

	}
}