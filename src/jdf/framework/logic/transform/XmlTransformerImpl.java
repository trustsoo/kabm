package jdf.framework.logic.transform;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResultSetDataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.FieldType;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.StringFormater;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;


/**
 * 
 * XmlTransformer 구현체
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-11-15
 * 
 */
public class XmlTransformerImpl implements XmlTransformer {
	private static javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();

	private int xml_trans_type = XmlTransformer.XML_ELEMENT_CENTRIC;

	private URL xsl_url;

	private String defaultEncoding = "utf-8";

	// private TransformerException tfe;

	// namespace localName
	private String localNm = "";

	private String namespace;

	private String nsPrefix = "";

	// private String nsDeclare;

	public XmlTransformerImpl() {
	}

	public String getContentType() {
		return "text/xml; charset=" + defaultEncoding;
	}

	public void setDefaultEncoding(String encoding) {
		this.defaultEncoding = encoding;

	}
	
	public String getDefaultEncoding() {
		return defaultEncoding;
	}
	
	

	/**
	 * 
	 * @see jdf.framework.logic.transform.XSLTransformer#setXmlType(int)
	 */
	public void setXmlType(int type) {
		this.xml_trans_type = type;

	}

	public void setStylesheet(URL xsl_url) {
		this.xsl_url = xsl_url;
	}

	public int transform(DataSet input, OutputStream out) throws TransformerException {
		return transform("result", input, out);
	}

	public int transform(DataSet input, Writer writer) throws TransformerException {
		return transform("result", input, writer);
	}

	/**
	 * 
	 * 
	 */
	public int transform(String rootElementName, DataSet input, OutputStream out) throws TransformerException {
		try {
			Block[] blocks = input.getBlocks();

			String tmp = transToXmlSring(rootElementName, xml_trans_type, blocks, input);

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
	 * @see jdf.framework.logic.transform.Transformer#transform(DataSet,
	 *      Writer)
	 */
	public int transform(String rootElementName, DataSet source, Writer writer) throws TransformerException {
		try {
			Block[] blocks = source.getBlocks();

			String result = transToXmlSring(rootElementName, xml_trans_type, blocks, source);

			if (this.xsl_url != null) {
				Transformer tfi = tf.newTransformer(new StreamSource(xsl_url.openStream()));
				tfi.transform(new StreamSource(new java.io.StringReader(result)), new StreamResult(writer));
				return 0;

			}

			writer.write(result);

			return result.length();
		} catch (Exception e) {
			throw new TransformerException("transform error", e);
		}
	}

	protected String transToXmlSring(String rootElementName, int type, Block[] blocks, DataSet dataset)
			throws TransformerException {
		switch (type) {
		case XmlTransformer.XML_ELEMENT_CENTRIC:
			return transToElementXmlSring(rootElementName, blocks, dataset);

		case XmlTransformer.XML_ATTRIBUTE_CENTRIC:
			return transToAttrXmlSring(rootElementName, blocks, dataset);

		default:
			throw new TransformerException("unkown type defined");

		}
	}

	protected String transToElementXmlSring(String rootElementName, Block[] blocks, DataSet dataset) {
		// return transToElementXmlSring(rootElementName, blocks, dataset, true,
		// blocks);
		return transToElementXmlSring(rootElementName, blocks, dataset, false, blocks);

	}

	/**
	 * DataSet �� element ����� XML ���ڿ��� �ٲ۴�.
	 * 
	 * 
	 * @param blocks
	 * @param blocks
	 * @param isAppendBlockTag
	 *            rootElement ������ block �±׸� ������ ���ΰ�?
	 * @return byteArray
	 */
	private String transToElementXmlSring(String rootElementName, Block[] blocks, DataSet dataset,
			boolean isAppendBlockTag, Block[] orgblocks) throws TransformerException {
		if (blocks == null)
			return "";

		dataset.unfixNull();
		StringBuffer out = new StringBuffer();

		if (rootElementName != null)
			out.append("<").append(localNm).append(rootElementName).append(this.getNamespaceDeclare()).append(">");

		for (int i = 0; i < blocks.length; i++) {
			Block block = blocks[i];

			/*
			if (blocks.length > 1 && block.getId() != null)
				continue;
			*/

			int iterationNum = 0;

			if (block.isIterationNumSet())
				iterationNum = block.getIterationNum();
			else {
				Object x = null;

				try {
					x = dataset.get(block.getIterationRefName());
					if (x.getClass() == String.class)
						
						iterationNum = Integer.parseInt((String) x);

					else if (x.getClass() == Integer.class)
						iterationNum = ((Integer) x).intValue();
				} catch (Exception ee) {
					iterationNum = 0;
				}
			}
			iterationNum = 0;

			Field[] fields = block.getFields();
			String blockElementNm = block.getName();

			if (isAppendBlockTag)
				out.append("<").append(localNm).append("block>");

			String firstKeyName = null;

			// max row ���� ���Ѵ�.
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

			for (int j = 0; j < iterationNum; j++) {

				out.append("<").append(localNm).append(blockElementNm).append(">");
				for (int k = 0; k < fields.length; k++) {
					boolean isTextConversion = true;

					Field field = fields[k];

					String keyName = field.getName();

					Object val = null;
					if (field.getFormatter().existFormatInfo())
						val = dataset.getText(keyName, j);
					else {
						val = dataset.get(keyName, j);
					}

					if (val == null) {
						val = field.getDefaultValue();
						if (val instanceof String) {
							String x = val.toString();

							int z = x.indexOf(".count");
							if (z > 0) {
								String refFieldName = x.substring(2, z);
								val = new Integer(dataset.getCount(refFieldName));
							}

						}

						dataset.put(keyName, val, j);
					}

					// �ڽ� DataSet�� �� �ִٸ�
					else if (val.getClass() == DataSet.class) {
						isTextConversion = false;

						Block tmpBlock = this.findBlockById(orgblocks, field.getRefId());

						if (tmpBlock != null)
							val = transToElementXmlSring(null, new Block[] { tmpBlock }, (DataSet) val, false, blocks);
						else
							Logger.warn.println("<XmlTransformerImpl> " + field.getRefId() + " is not exist");

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
							val = transToElementXmlSring(parentTagName, new Block[] { tmpBlock }, dataset, false,
									blocks);
						else
							Logger.warn.println("<XmlTransformerImpl> " + field.getRefId() + " is not exist");

						// block�ȿ��� ��ȯ�� xml�� ���ٸ� �±��߰�����
						// �ٷ� �߰��Ѵ�.
						out.append(val);
						val = null;

					}

					if (val != null) {
						out.append("<").append(localNm).append(keyName).append(">");

						if (isTextConversion) {
							if (field.getType() == FieldType.BLOB)
								out.append("<!-- BINARY DATA -->");
							else {
								setXmlFormat(val.toString(), out);

							}

						}

						else
							out.append(val);

						out.append("</").append(localNm).append(keyName).append(">");
					}

				} // field
				out.append("</").append(localNm).append(blockElementNm).append(">\n");

				if (isResultSetDataSet) {
					iterationNum = dataset.getCount(firstKeyName);

				}

			} // for (int j = 0; j < iterationNum; j++)

			if (isAppendBlockTag)
				out.append("</").append(localNm).append("block>");

		}

		if (rootElementName != null)
			out.append("</").append(localNm).append(rootElementName).append(">");

		return out.toString();

	}

	/**
	 * Block �迭���� �ش� id�� block�� �����Ѵ�.
	 * 
	 * @param blocks
	 * @param id
	 * @return
	 */
	private Block findBlockById(Block[] blocks, String id) {

		if (id == null)
			return null;

		for (int i = 0; i < blocks.length; i++) {

			if (id.equals(blocks[i].getId()))
				return blocks[i];

		}

		return null;

	}

	private static void setXmlFormat(String data, StringBuffer buf) {
		String encodeStr = null;
		encodeStr = new XmlTransformerImpl().getDefaultEncoding();
		String encodeData = null;
		try
		{
			data = StringFormater.contentFilter(data);
			
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

	/**
	 * DataSet �� Attribute ����� XML ���ڿ��� �ٲ۴�.
	 * 
	 * 
	 * @param blocks
	 * @param blocks
	 * 
	 * @return byteArray
	 */
	protected String transToAttrXmlSring(String rootElementName, Block[] blocks, DataSet dataset)
			throws TransformerException {
		dataset.unfixNull();

		StringBuffer out = new StringBuffer();

		out.append("<").append(localNm).append(rootElementName).append(this.getNamespaceDeclare()).append(">");

		try {

			for (int i = 0; i < blocks.length; i++) {
				Block block = blocks[i];

				int iterationNum = 0;

				if (block.isIterationNumSet())
					iterationNum = block.getIterationNum();
				else {
					Object x = dataset.get(block.getIterationRefName());

					if (x.getClass() == String.class)
						iterationNum = Integer.parseInt((String) x);

					else if (x.getClass() == Integer.class)
						iterationNum = ((Integer) x).intValue();
				}

				Field[] fields = block.getFields();
				String blockElementNm = block.getName();

				out.append("<").append(localNm).append("block>");

				for (int j = 0; j < iterationNum; j++) {

					out.append("<").append(localNm).append(blockElementNm).append(" ");
					for (int k = 0; k < fields.length; k++) {
						Field field = fields[k];

						String keyName = field.getName();

						Object val = dataset.get(keyName, j);

						if (j == 0 && k == 0) {
							iterationNum = dataset.getCount(keyName);
						}

						if (val == null) {
							val = field.getDefaultValue();
							if (val instanceof String) {
								String x = val.toString();

								int z = x.indexOf(".count");
								if (z > 0) {
									String refFieldName = x.substring(2, z);
									val = new Integer(dataset.getCount(refFieldName));
								}

							}

							dataset.put(keyName, val, j);
						}

						if (val != null) {
							out.append(keyName).append("=\"");
							out.append(val.toString());
							out.append("\" ");
						}

					}
					out.append("/>");
				}

				out.append("</").append(localNm).append("block>");

			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
			throw new TransformerException(ioe.toString());
		}

		out.append("</").append(localNm).append(rootElementName).append(">");

		return out.toString();

	}

	/**
	 * <xs:schema>�� ���� prefix �� localName�� �����Ѵ�.
	 * 
	 * @see XmlTransformer#setLocalName(String)
	 */
	public void setLocalName(String name) {
		this.nsPrefix = ":" + name;
		this.localNm = name + ":";
	}

	/**
	 * 
	 * 
	 * @see XmlTransformer#setNamespace(String)
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	private String getNamespaceDeclare() {
		if (this.namespace == null)
			return "";
		return " xmlns" + this.nsPrefix + "='" + this.namespace + "'";
	}

}