
package jdf.framework.logic.transform;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResultSetDataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.FieldType;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringFormater;



/**
 * jQGrid에서 쓰이는 형태의 XML로 변환한다.
 * @see http://www.trirand.com/jqgridwiki/doku.php?id=wiki:first_grid
 * @author advan94
 *
 */

public class JQGridXMLTransformerImpl implements XmlTransformer
{
	private final static String LOG_ID = "<JQGridXMLTransformerImpl> ";
	private String defaultEncoding = "utf-8";
	
	JQGridXMLTransformerImpl() {
		Logger.debug.println(LOG_ID + "객체 생성");
	}
	
	
	private String blockElementNm = "row";
	private String uniqueKeyDelimeter = "|";
	private String uniquePropertyKey = "isUnique";
	private String tempUniqueKey = "<:@id>";
	/**
	 * 
	 * @see jdf.framework.logic.transform.XSLTransformer#setXmlType(int)
	 */
	public void setXmlType(int type) {
		

	}

	public void setStylesheet(URL xsl_url) {
	}

	public int transform(DataSet input, OutputStream out) throws TransformerException {
		return transform("rows", input, out);
	}

	public int transform(DataSet input, Writer writer) throws TransformerException {
		return transform("rows", input, writer);
	}
	
	/**
	 * 
	 * 
	 */
	public int transform(String rootElementName, DataSet input, OutputStream out) throws TransformerException {
		try {
			Block[] blocks = input.getBlocks();

			String tmp = transToJQGrid(rootElementName, blocks, input);

			byte[] result = tmp.getBytes(this.defaultEncoding);

			out.write(result);

			return result.length;
		} catch (IOException e) {
			throw new TransformerException("transform error", e);
		}
	}

	/**
	 * 
	 * 
	 * 
	 * @see jdf.framework.logic.transform.Transformer#transform(jdf.framework.core.data.DataSet,
	 *      java.io.Writer)
	 */
	public int transform(String rootElementName, DataSet source, Writer writer) throws TransformerException {
		try {
			Block[] blocks = source.getBlocks();

			String result = transToJQGrid(rootElementName, blocks, source);
			
			writer.write(result);

			return result.length();
		} catch (Exception e) {
			throw new TransformerException("transform error", e);
		}
	}
	
	
	protected String transToJQGrid(String rootElementName, Block[] blocks, DataSet dataset) {
		// return transToElementXmlSring(rootElementName, blocks, dataset, true,
		// blocks);
		return transToJQGrid(rootElementName, blocks, dataset, blocks);

	}
	
	private String transToJQGrid(String rootElementName, Block[] blocks, DataSet dataset, Block[] orgblocks) throws TransformerException {
		if (blocks == null)
			return "";

		dataset.unfixNull();
		StringBuffer out = new StringBuffer();

		if (rootElementName != null)
			out.append("<").append(rootElementName).append(">");
		
		String type = dataset.getProperty(TransFormConstant.GRID_TYPE);
		
		if(type == null) type = "";
		
		int totalCount = 0;
		int curPage = 1;
		int totalPage = 1;
		int rowPerPage = 0;
		if("".equals(type))
		{
			try
			{
				totalCount = Integer.parseInt(dataset.getProperty(TransFormConstant.TOT_CNT));
			} catch(Exception ex)
			{
				totalCount = dataset.getMaxDataSize();
			}
		} else
		{
			totalCount = 1;
		}
		
		try
		{
			curPage = Integer.parseInt(dataset.getProperty(TransFormConstant.CUR_PG));
		} catch(Exception ex)
		{			
		}
		
		try
		{
			rowPerPage = Integer.parseInt(dataset.getProperty(TransFormConstant.ROW_PER_PAGE));
		} catch(Exception ex)
		{	
			rowPerPage = totalCount;
		}
		
		try
		{
			if (totalCount % rowPerPage == 0) {
				totalPage = totalCount / rowPerPage;
            } else {
            	totalPage = (totalCount / rowPerPage) + 1;
            }
		} catch(Exception ex)
		{
			totalPage = 1;
		}
		out.append("<page>").append(curPage).append("</page>");
		out.append("<total>").append(totalPage).append("</total>");
		out.append("<records>").append(totalCount).append("</records>");
		List<String> uniqueVal = null;
		for (int i = 0; i < blocks.length; i++) {
			Block block = blocks[i];

			int iterationNum = 0;

			if (block.isIterationNumSet())
				iterationNum = block.getIterationNum();
			else {
				Object x = dataset.get(block.getIterationRefName());

				try {
					if (x.getClass() == java.lang.String.class)
						iterationNum = Integer.parseInt((String) x);

					else if (x.getClass() == java.lang.Integer.class)
						iterationNum = ((Integer) x).intValue();
				} catch (Exception ee) {
					iterationNum = 0;
				}
			}
			iterationNum = 0;

			Field[] fields = block.getFields();
			
			String firstKeyName = null;
			
			for (int k = 0; k < fields.length; k++) {
				Field field = fields[k];
				String keyName = field.getName();
				if (k == 0)
					firstKeyName = keyName;

				int tmpCount = dataset.getCount(keyName);
				if (tmpCount > iterationNum)
					iterationNum = tmpCount;
			}

			boolean isResultSetDataSet = false;
			if (dataset instanceof ResultSetDataSet) {
				isResultSetDataSet = true;
			}
			
			String uniqueValue = null;
			for (int j = 0; j < iterationNum; j++) {
				uniqueVal = new ArrayList<String>();
				out.append("<").append(blockElementNm).append(" ").append(tempUniqueKey).append(">");
				for (int k = 0; k < fields.length; k++) {
					boolean isTextConversion = true;

					Field field = fields[k];

					String keyName = field.getName();
					Properties prt = field.getProperties();
					
					if(field.getProperty(uniquePropertyKey) != null && "true".equals(field.getProperty(uniquePropertyKey)))
					{
						uniqueVal.add(dataset.getText(keyName, j));
					}
					
					Object val = null;
					if (field.getFormatter().existFormatInfo())
						val = dataset.getText(keyName, j);
					else {
						val = dataset.get(keyName, j);
					}

					if (val == null) {
						val = field.getDefaultValue();
						if (val instanceof java.lang.String) {
							String x = val.toString();

							int z = x.indexOf(".count");
							if (z > 0) {
								String refFieldName = x.substring(2, z);
								val = new Integer(dataset.getCount(refFieldName));
							}

						}

						dataset.put(keyName, val, j);
					}
					
					else if (val.getClass() == DataSet.class) {
						isTextConversion = false;

						Block tmpBlock = this.findBlockById(orgblocks, field.getRefId());

						if (tmpBlock != null)
							val = transToJQGrid(rootElementName, new Block[] { tmpBlock }, (DataSet) val, blocks);
						else
							Logger.warn.println(LOG_ID + field.getRefId() + " is not exist");

						// block�ȿ��� ��ȯ�� xml�� ���ٸ� �±��߰�����
						// �ٷ� �߰��Ѵ�.
						out.append(val);
						val = null;
					}

					else if (field.getType() == Field.BLOCK && "self".equals(val)) {
						isTextConversion = false;

						Block tmpBlock = this.findBlockById(orgblocks, field.getRefId());

						// System.out.println(" ******* "+field.getName());

						String parentTagName = field.getName();
						if ("".equals(parentTagName))
							parentTagName = null;

						if (tmpBlock != null)
							val = transToJQGrid(rootElementName, new Block[] { tmpBlock }, dataset, blocks);
						else
							Logger.warn.println(LOG_ID + field.getRefId() + " is not exist");
						
						out.append(val);
						val = null;

					}

					if (val != null) {
						out.append("<").append("cell").append(">");

						if (isTextConversion) {
							if (field.getType() == FieldType.BLOB)
								out.append("<!-- BINARY DATA -->");
							else {
								setXmlFormat(val.toString(), out);

							}

						}

						else
							out.append(val);

						out.append("</").append("cell").append(">");
					}

				} // field
				
				if(uniqueVal.size() <= 0) uniqueValue = j+"";
				else
				{
					uniqueValue = SmartStringArray.join(uniqueKeyDelimeter, uniqueVal.toArray(new String[uniqueVal.size()]));
				}	
					
				//System.out.println(j+"==========>"+uniqueVal.get(0));
				out.append("</").append(blockElementNm).append(">\n");
				if("".equals(type))
					out.replace(out.indexOf(tempUniqueKey), tempUniqueKey.length()+out.indexOf(tempUniqueKey), "id='"+uniqueValue+"'");
				else
					out.replace(out.indexOf(tempUniqueKey), tempUniqueKey.length()+out.indexOf(tempUniqueKey), "");
				
				if (isResultSetDataSet) {
					iterationNum = dataset.getCount(firstKeyName);

				}

			} // for (int j = 0; j < iterationNum; j++)

			

		}

		if (rootElementName != null)
			out.append("</").append(rootElementName).append(">");

		return out.toString();

	}
	
	private static void setXmlFormat(String data, StringBuffer buf) {
		String encodeStr = null;
		encodeStr = new XmlTransformerImpl().getDefaultEncoding();
		String encodeData = null;
		try
		{
			data = data.replaceAll("&quot;", "\"").replaceAll("&#039;", "\'");        
	        data = data.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			
			data = StringFormater.replaceStr(StringFormater.replaceStr(data, "<", "("), ">", ")");
			//encodeData = java.net.URLEncoder.encode(data, "UTF-8");
			encodeData = data;
		} catch(Exception ex)
		{
			encodeData = data;
		}
		if (encodeData.indexOf("<") > -1) {
			buf.append("<![CDATA[").append(encodeData).append("]]>");
			return;
		} else if (encodeData.indexOf("&") > -1) {
			buf.append("<![CDATA[").append(encodeData).append("]]>");
			return;
		} else if (encodeData.indexOf(">") > -1) {
			buf.append("<![CDATA[").append(encodeData).append("]]>");
			return;
		}

		buf.append(encodeData);

	}
	
	private Block findBlockById(Block[] blocks, String id) {

		if (id == null)
			return null;

		for (int i = 0; i < blocks.length; i++) {

			if (id.equals(blocks[i].getId()))
				return blocks[i];

		}

		return null;

	}

	@Override
	public void setDefaultEncoding(String encoding) {
		this.defaultEncoding = encoding;

	}
	
	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	@Override
	public String getContentType() {
		return "text/xml; charset=" + defaultEncoding;
	}

	@Override
	public void setLocalName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNamespace(String namespace) {
		// TODO Auto-generated method stub
		
	}
}