package jdf.framework.logic.transform;

import java.util.Hashtable;
import java.util.Map;

/**
 * 
 * Transformer 구현객체를 얻기위한 Factory 성 class
 * 
 * ex)
 * 
 * <pre>
 * TransformerFactory factory = TransformerFactory.getInstance();
 * 
 * Transformer trans = factory.getTransformer(&quot;html&quot;);
 * </pre>
 * 
 * 
 * @author Eun Jeong-Ho
 * @version 1.0
 */
public class TransformerFactory {

	private static TransformerFactory instance = new TransformerFactory();

	private Map tables;

	/**
	 * 기본생성자
	 * 
	 */
	public TransformerFactory() {
		initialize();
	}

	/**
	 * 생성자를 얻는다.
	 * 
	 * @return
	 */
	public static TransformerFactory getInstance() {
		return instance;
	}

	/**
	 * 초기화 , 굳이 호출할 필요는 없다.
	 * 
	 */
	public void initialize() {

		tables = new Hashtable();

		tables.put(TransformerType.COMMA_TYPE, new CommaTransformerImpl());
		tables.put(TransformerType.BINARY_TYPE, new BinaryTransformerImpl());
		tables.put(TransformerType.BASE64_TYPE, new Base64TransformerImpl());
		tables.put(TransformerType.XML_TYPE, new XmlTransformerImpl());
		tables.put(TransformerType.GRID_TYPE, new JQGridXMLTransformerImpl());		
		tables.put(TransformerType.SOAP_TYPE, new SOAPTransformerImpl());
		
		
		// tables.put(TransformerType.ANYMETA_TYPE, new
		// AnyMetaTransformerImpl());
		// tables.put(TransformerType.ANYREPORT_TYPE, new
		// AnyReportTransformerImpl());

		tables.put(TransformerType.HTML_TYPE, new HtmlTransformerImpl());
		tables.put(TransformerType.EXCEL_TYPE, new XlsTransformerImpl());
		tables.put(TransformerType.CSV_TYPE, new ExcelTransformerImpl());

		// tables.put(TransformerType.ANYCHART_TYPE, new
		// AnyChartTransformerImpl());

		// JSON 변환
		tables.put(TransformerType.JSON_TYPE, new JSONTransformerImpl());
		tables.put(TransformerType.OBJECT_TYPE, new ObjectTransformerImpl());
		tables.put(TransformerType.TREE_JSON_TYPE, new TreeJSONTransformerImpl());
		tables.put(TransformerType.TREE_JSON_TYPE_EX, new TreeJSONTransformerImplEx());
		tables.put(TransformerType.DYNAMIC_TREE_JSON_TYPE, new DynamicTreeJSONTransformerImpl());

		// tables.put("xml_field", new FieldCentricXmlTransformer());
	}

	/**
	 * Transformer를 얻기위한 메쏘드
	 * 
	 * @param name
	 * @return
	 * @throws TransformerException
	 */
	public Transformer getTransformer(String name) throws TransformerException {
		Transformer transformer = (Transformer) tables.get(name);
		if (transformer == null)
			throw new TransformerException("Transfomer가 존재하지 않습니다. " + name);

		return transformer;
	}

	/**
	 * XML 변환 transformer 를 얻는다.
	 * 
	 * @return
	 * @throws TransformerException
	 */
	public XmlTransformer newXmlTransformer() throws TransformerException {
		return new XmlTransformerImpl();
	}

}
