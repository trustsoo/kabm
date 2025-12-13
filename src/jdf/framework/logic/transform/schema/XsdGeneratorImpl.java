/*
 * Created on 2004-05-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.transform.schema;

import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.logic.spi.management.BLContextFactory;
import jdf.framework.logic.transform.TransformerException;

/**
 *  jdf.framework.core.data.schema.Block의 정보를 분석하여,
 *  XML Schema 문자열을 만든다.
 * 
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XsdGeneratorImpl implements XsdGenerator
{

	private StringBuffer xsdText = new StringBuffer();

	// XSD의 target namespace
	private String targetNamespace;

	// local namespace
	private String localName = "";

	// <s:schema> 의 예와 같이 s: 가 세팅된다.
	private String localNm = "";

	/**
	 * local name을 설정한다.
	 * 
	 */
	public void setLocalName(String name)
	{
		this.localName = ":" + name;
		this.localNm = name + ":";
	}

	/**
	 * schema의 target namespace를 정의한다.
	 */
	public void setTargetNamespace(String ns)
	{
		this.targetNamespace = ns;
	}

	/**
	 * Block 에 따라 스키마 정보를 생성한다.
	 * 
	 */
	private String generate(IOSchema ioschema, Block[] blocks, String name, boolean isAppend)
	{
		StringBuffer out = new StringBuffer();

		if (name != null)
		{
			out.append("<").append(localNm).append("element name=\"").append(name).append("\">");

			out.append("<").append(localNm).append("complexType>");
			out.append("<").append(localNm).append("sequence>");
		}

		for (int i = 0; i < blocks.length; i++)
		{
			Block b = blocks[i];

			// block의 id가 없는 경우만 추가한다.
			if (b.getId() == null)
				out.append(generate(ioschema, b, null, false, false));
		}

		if (name != null)
		{

			out.append("</").append(localNm).append("sequence>");
			out.append("</").append(localNm).append("complexType>");
			out.append("</").append(localNm).append("element>");
		}

		String result = out.toString();

		if (isAppend)
			xsdText.append(result);

		return result;
	}

	public static void main(String[] args) throws Exception
	{
		try
		{
			InteractionBean b = new InteractionBean();
			b.execute("xbrl/convert/list", new jdf.framework.core.data.DataSet());
		}
		catch (Exception e)
		{
		}

		BLContextFactory blf = BLContextFactory.getInstance();
		//blf.initialize();

		IOSchema schema = blf.getIOSchema("xbrl/convert/select");

		XsdGeneratorImpl gen = new XsdGeneratorImpl();
		gen.setTargetNamespace("TestScheme");
		gen.setLocalName("s");
		String x = gen.generate(schema, IOSchema.OUT, "result");

		System.out.println(x);
		System.out.println(gen.getXsdText());

	}

	public String generate(Block b, String elementName)
	{
		return generate(b, elementName, true);
	}

	/**
	 * 
	 * 
	 * @see jdf.framework.logic.transform.schema.XsdGenerator#generate(jdf.framework.core.data.schema.Block, java.lang.String)
	 */
	public String generate(Block b, String elementName, boolean isAppend)
	{
		return generate(null, b, elementName, isAppend, false);

	}

	/**
	 * 
	 * 
	 * @param ioschema
	 * @param b
	 * @param elementName
	 * @param isAppend
	 * @return
	 */
	private String generate(IOSchema ioschema, Block b, String elementName, boolean isAppend, boolean minOccurs)
	{
		if (b == null)
			return "";

		StringBuffer out = new StringBuffer();

		if (elementName == null)
			elementName = b.getName();
		
		//System.out.println( b.getIterationNum() +"========"+b.getIterationRefName() );
		

		out.append("<").append(localNm).append("element name=\"").append(elementName).append("\"");
		
		if(b.getIterationNum() == Block.UNBOUNDED) {
			out.append(" minOccurs=\"0\"");
			out.append(" maxOccurs=\"unbounded\"");
		}
		else {
		
			out.append(" minOccurs=\"0\"");
		    out.append(" maxOccurs=\"").append(b.getIterationNum()).append("\"");
		}
		
		if (minOccurs) {
			
			out.append(" minOccurs=\"0\"");
			out.append(" nillable=\"true\"");
		}
			
		out.append(">");

		Field[] fields = b.getFields();

		if (fields.length > 0)
		{
			out.append("<").append(localNm).append("complexType>");
			out.append("<").append(localNm).append("sequence>");
		}

		for (int j = 0; j < fields.length; j++)
		{
			Field f = fields[j];

			if (f.getRefId() != null)
			{

				Block childBlock = ioschema.getBlockById(f.getRefId());

				if (childBlock != null)
				{
					String result = generate(ioschema, childBlock, null, false, true);

					out.append(result);

				}

			}
			else
			{
				out.append("<").append(localNm).append("element");
				out.append(" name=\"").append(f.getName()).append("\" type=\"");
				out.append(localNm).append(f.getTypeName(Field.XSD)).append("\" minOccurs=\"0\" maxOccurs=\"1\"/>");
				//out.append(localNm).append(f.getTypeName(Field.XSD)).append("\" minOccurs=\"0\"/>");
			}

		}

		if (fields.length > 0)
		{
			out.append("</").append(localNm).append("sequence>");
			out.append("</").append(localNm).append("complexType>");
		}
		out.append("</").append(localNm).append("element>");

		String result = out.toString();

		if (isAppend)
			xsdText.append(result);

		return result;
	}

	/* (non-Javadoc)
	 * @see anylogic.transform.schema.XsdGenerator#getXsdText()
	 */
	public String getXsdText() throws TransformerException
	{
		if (this.targetNamespace == null)
			throw new TransformerException("target namespace is not set");

		StringBuffer xsdStrBuf = new StringBuffer();

		xsdStrBuf.append("<").append(localNm);
		xsdStrBuf.append("schema elementFormDefault=\"qualified\" targetNamespace=\"");
		xsdStrBuf.append(this.targetNamespace).append("\" ");
		xsdStrBuf.append("xmlns").append(localName);
		xsdStrBuf.append("=\"").append("http://www.w3.org/2001/XMLSchema").append("\" >");

		xsdStrBuf.append(xsdText.toString());

		xsdStrBuf.append("</").append(localNm).append("schema>");

		return xsdStrBuf.toString();
	}

	/**
	 * 
	 * 
	 * @see jdf.framework.logic.transform.schema.XsdGenerator#generate(jdf.framework.core.data.schema.IOSchema, int, java.lang.String)
	 */
	public String generate(IOSchema ioschema, int in_out, String elementName) throws TransformerException
	{
		switch (in_out)
		{
			case IOSchema.IN :
                /*
				Block inputBlock = null;
				try
				{
					// input은 첫번째 하나의 block만 선택한다.
					inputBlock = ioschema.getInputBlocks()[0];
				}
				catch (Exception e)
				{
				}

				return generate(ioschema, inputBlock, elementName, true, false);
                */
                return generate(ioschema, ioschema.getInputBlocks(), elementName, true);

			case IOSchema.OUT :
				return generate(ioschema, ioschema.getOutputBlocks(), elementName, true);

			default :
				throw new TransformerException("In/Out 중 올바른 모드가 아닙니다.");

		}

	}

	/**
	 * 추가 schema 정보를 text 형태로 추가한다.
	 * 
	 * @see jdf.framework.logic.transform.schema.XsdGenerator#appendXsdText(java.lang.String)
	 */
	public void appendXsdText(String txt)
	{
		this.xsdText.append(txt);

	}

}
