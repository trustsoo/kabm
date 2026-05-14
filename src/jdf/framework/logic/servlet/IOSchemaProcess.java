package jdf.framework.logic.servlet;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.http.JDFrameContextListener;
import jdf.framework.core.io.HttpServlerResponseWriter;
import jdf.framework.core.io.SmartFile;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.HtmlFormat;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringFormater;
import jdf.framework.core.xml.DOMWriter;
import jdf.framework.core.xml.DocBuilder;
import jdf.framework.core.xml.XMLReferer;
import jdf.framework.logic.spi.management.BLContextFactory;
import jdf.framework.logic.spi.management.BusinessLogicContext;
import jdf.framework.logic.transform.Transformer;
import jdf.framework.logic.transform.TransformerFactory;
import jdf.framework.logic.transform.TransformerType;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;


/**
 * 
 * XML ������ IO schema�� �о� ó���� �ش�.
 * 
 * 
 * SOAP ���� HTTP-GET ���������� �����ϴ� XML Web services�� �� ������ �׼����� �� �ֽ��ϴ�.
 * anyLOGIC���� ���� XML Web services�� �⺻������ HTTP-GET, HTTP-POST �� HTTP-SOAP�� �����մϴ�.
 * �׷��� ��� ���������� ��� �޼��带 ���������� �ʽ��ϴ�. �Ϲ������� HTTP-GET �� HTTP-POST ���������� �����ϴ� ������
 * ����� HTTP-SOAP���� ���ѵǾ� �ֽ��ϴ�.
 * 
 * @author
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IOSchemaProcess extends HttpServlet implements AnyLogicControl,
		SoapMeta {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String LOG_ID = "<al-svlt:IOSchemaProcess> ";

	private static final String FIELD_NM_WEBPONENT_GRID_LAYOUT = "_WEBPONENT_GRID_LAYOUT";

	private static final byte[] XML_DECLARE = "<?xml version=\"1.0\" encoding=\"euc-kr\"?>"
			.getBytes();

	private ServletContext ctx;

	/**
	 * 
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		ctx = config.getServletContext();

	}

	/**
	 * 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		// /process/grid/test.xml �� ���� ���´�.
		String loc = req.getPathInfo();

		// ������ . �� ��ġ
		int lp = loc.lastIndexOf(".");

		String trCode = loc.substring(loc.indexOf("/", 1) + 1, lp);
		String resType = loc.substring(lp + 1);

		Logger.debug.println(LOG_ID + "tr:" + trCode + " type:" + resType);

		boolean checkAcl = true;
		try {
			if (TransformerType.OBJECT_TYPE.equals(resType)) {

				// ������ ������ ����ð�
				String execTimeStr = req.getHeader("fw.execute.time");
				long execTime = Long.parseLong(execTimeStr);

				// ȣ���ϴ� ���ϰ� �����ϴ� ���ϰ� �ð����̰� 1���̳����� ����
				if (Math.abs(System.currentTimeMillis() - execTime) < 1000 * 60)
					checkAcl = false;
			}

			if (checkAcl) {
				BusinessLogicContext blc = BLContextFactory.getInstance()
						.getBusinessLogicContext(trCode);
				IOSchema schema = blc.getIOSchema();

				// �ܺ� http �� ���� bld ������ ������� �ʴ� ���
				if (!schema.isEnableHttpAccess()) {

					if (!AnyLogicControlServlet.isValidAuthTempKey(req)) {

						res.setStatus(HttpServletResponse.SC_FORBIDDEN);
						Logger.warn.println(LOG_ID + "forbidden access");
						return;
					}

				}
			}

		} catch (ResourceException re) {

		}

		String soapAction = req.getHeader("SOAPAction");
		if (soapAction != null || "soap".equals(resType)) {
			doSoap(trCode, soapAction, req, res);
			return;
		}

		String monitor = req.getParameter("trace_perform");
		// �������� �α׸� �������ΰ�?
		boolean isMonitor = "true".equals(monitor) ? true : false;

		// ��������
		// SessionTracer tracer = SessionTracer.getInstance(this, trCode,
		// isMonitor);
		// tracer.start();

		Connection connection = null;

		// �ʼ� attribute
		//
		DataSet input = new DataSet();
		DataSet output = null;

		try {
			setDataSet(req, input);

			connection = DefaultConnectionFactory.getConnection();
			Interaction inter = connection.createInteraction();
			output = inter.execute(trCode, input);

			String layoutSrc = req.getParameter(FIELD_NM_WEBPONENT_GRID_LAYOUT);
			if (layoutSrc != null && layoutSrc.length() > 0) {

				processLayoutXml(req, res, layoutSrc, input, output);
				return;
			}

			TransformerFactory tFactory = TransformerFactory.getInstance();

			if (resType == null || resType.length() <= 0)
				resType = "comma";
			Transformer transformer = tFactory.getTransformer(resType);

			java.io.OutputStream osteam = res.getOutputStream();

			ByteArrayOutputStream bas = new ByteArrayOutputStream();

			String encoding = "euc-kr";

			String contentType = req.getContentType();

			try {
				encoding = SmartStringArray.split("=", contentType)[1]
						.toUpperCase();
				encoding = StringFormater.replaceStr(encoding, "\"", "");

			} catch (Exception e) {
			}
			Logger.debug.println(LOG_ID + "response encoding:" + encoding);

			// �⺻ encoding ���� ����
			transformer.setDefaultEncoding(encoding);

			if (TransformerType.XML_TYPE.equals(resType)) {

				res.setContentType("text/xml; charset=" + encoding);

				bas.write(XML_DECLARE);

				bas.write("<dataset>".getBytes(encoding));

				// osteam.write(output.getIOSchema().toString().getBytes());

				// ��������
				/*
				 * SessionTracer tracer2 =
				 * SessionTracer.getInstance(transformer, trCode);
				 * tracer2.start(); transformer.transform(output, bas);
				 * tracer2.end();
				 * 
				 * if (isMonitor) {
				 * 
				 * tracer.end();
				 * 
				 * bas.write(tracer.toXmlString().getBytes(encoding)); }
				 */

				bas.write("</dataset>".getBytes());
			} else {
				res.setContentType(transformer.getContentType());

				// DATA ���̴� ��� ������ ���
				// report �� ����
				// anyreport �� chucked �� length ������ HTTP Header �� ���� �ִ°�� ������
				// ������.
				// Jeus �� SUNOne WebServer �� ���յǴ� ��� ���� ���� ������ �߻�....
				// ��� chunked ������� �������
				/*if ("true".equals(req.getParameter("DIRECT_FLUSH"))
						|| TransformerType.ANYREPORT_TYPE.equals(resType)) {
					transformer.transform(output, osteam);
					res.flushBuffer();
					return;
				}*/

				// HTML Layout���� �����ش�.
				boolean htmlLayout = false;
				if ("true".equals(req.getParameter("HTML_LAYOUT"))) {
					res.setContentType("text/html; charset=euc-kr");
					htmlLayout = true;
					bas
							.write("<textarea style='width:100%;height:100%;border:0px;overflow:auto'>"
									.getBytes());
				}

				// ��������
				/*
				 * SessionTracer tracer3 =
				 * SessionTracer.getInstance(transformer, trCode);
				 * tracer3.start(); transformer.transform(output, bas);
				 * tracer3.end(); if (htmlLayout) bas.write("</textarea>".getBytes());
				 */
			}

			byte[] reByteArray = bas.toByteArray();
			// res.setContentLength(reByteArray.length);

			res.setContentLength(reByteArray.length);

			// ��������
			/*
			 * SessionTracer tracer4 = SessionTracer.getInstance(osteam,
			 * trCode); tracer4.start(); osteam.write(reByteArray);
			 * tracer4.end();
			 */

			res.flushBuffer();

		} catch (Exception e) {
			e.printStackTrace();
			// throw new ServletException(e.getMessage());
			processError(e, req, res);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (ResourceException re) {

				}
			}

			if (output != null)
				output.clear();

			// tracer.end();

			// ThreadSesion �� ���� ����� bld ����� �� �ִµ�,
			// �� ������ clear ���ش�.
			// ThreadSession session = ThreadSession.getInstance();
			// session.clear();
		}

	}

	/**
	 * 
	 * 
	 * @param e
	 * @param req
	 * @param res
	 * @throws ServletException
	 */
	private void processError(Throwable e, HttpServletRequest req,
			HttpServletResponse res) throws ServletException {
		Logger.err.println(LOG_ID + "process error.", e);

		res.setContentType("text/xml; charset=euc-kr");

		StringWriter strWriter = new StringWriter();
		PrintWriter printwriter = new PrintWriter(strWriter);
		e.printStackTrace(printwriter);

		try {
			PrintWriter writer = res.getWriter();

			writer.println(new String(XML_DECLARE));

			writer.println("<exception>");
			writer.print("<msg>" + HtmlFormat.translate(e.getMessage())
					+ "</msg>");
			writer.println("<class>" + e.getClass().getName() + "</class>");

			writer.print("<extend>");
			writer.print("<trace>");
			writer.println(HtmlFormat.translate(strWriter.toString()));
			writer.print("</trace>");
			writer.print("</extend>");
			writer.println("</exception>");
		} catch (IOException ioe) {
			throw new ServletException(ioe.getMessage());
		}
	}

	private void processSoapRequest(HttpServletRequest req, DataSet input)
			throws ServletException {
		XMLReferer doc = null;

		try {
			InputStream is = req.getInputStream();

			doc = new XMLReferer(DocBuilder.getDocument(is));

			if (Logger.packet.isPrintMode()) {
				StringWriter sw = new StringWriter();
				DOMWriter dw = new DOMWriter(sw);
				dw.print(doc.getDocument());
				Logger.packet.println("IOSCHEMA SOAP REQ "
						+ req.getRemoteAddr() + "\n" + sw.toString());
			}

			// /soap:Envelope/soap:Body
			doc.lookupFirstChildElement(true).lookupFirstChildElement(true);

			// System.out.println(" 1>>>>>> "+doc.getNode().getNodeName());
			if (!"Body".equals(doc.getNode().getLocalName()))
				doc.nextSibling();
			// System.out.println(" 2>>>>>> "+doc.getNode().getLocalName());
			// namespace �� ���� ������ SOAP �޼����� �ƴѰ����� �Ǵ�.

			/*
			 * if(!"http://www.w3.org/2001/12/soap-envelope".equals(doc.getNode().getNamespaceURI()) ) {
			 * throw new ServletException("Invalid Soap Message"); }
			 */

		} catch (Exception e) {
			Logger.err.println(LOG_ID + "processSoapRequest error", e);
			throw new ServletException(e);

		}

		// body ������ �����ʹ� �߸�� �� ������ �����Ѵ�.
		try {
			// /soap:Envelope/soap:Body/dataset/request/arg(?)
			doc.lookupFirstChildElement(true).lookupFirstChildElement(true)
					.lookupFirstChildElement(false);

			while (doc.nextSibling()) {
				doc.mark();

				doc.lookupFirstChildElement(false);

				// System.out.println(" 3>>>>>> "+doc.getNode().getNodeName());
				while (doc.nextSibling()) {
					String key = doc.getNode().getLocalName();
					String val = doc.getText();
					input.add(key, val);

					Logger.debug.println(LOG_ID + "soap request " + key + "="
							+ val);
				}

				doc.reset();
			}

		} catch (Exception e) {

		}

		return;
	}

	/**
	 * 
	 * 
	 * @param req
	 * @param input
	 * @throws ServletException
	 */
	private void setDataSet(HttpServletRequest req, DataSet input)
			throws ServletException {
		Enumeration fieldNames = req.getParameterNames();

		while (fieldNames.hasMoreElements()) {
			String paramName = (String) fieldNames.nextElement();

			if (FIELD_NM_WEBPONENT_GRID_LAYOUT.equals(paramName))
				continue;

			try {
				for (int i = 0; i < req.getParameterValues(paramName).length; i++) {
					Object paramValue = req.getParameterValues(paramName)[i];

					input.put(paramName, paramValue, i);
					// Logger.debug.println("anylogic.servlet.Processor set
					// "+paramName+"["+i+"]="+paramValue);
				}

			} catch (Exception ex) {
				throw new ServletException(ex);
			}

		}

		// GRID ���� POST ����� parameter �� ������ ���� ���� ������...
		// Q_PARAMSET= 1[DT|20050926^DT|20050927^DT|20050928^]
		String Q_PARAMSET = req.getParameter("Q_PARAMSET");
		if (Q_PARAMSET != null) {
			try {
				String[] keyVals = SmartStringArray.split("^", Q_PARAMSET);
				for (int i = 0; i < keyVals.length; i++) {
					String[] keyVal = SmartStringArray.split("|", keyVals[i]);
					input.add(keyVal[0], keyVal[1]);
				}
			} catch (Exception e) {
			}

		}

		// Logger.debug.println(LOG_ID + "input:" + input.toString());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

	/**
	 * webponent���� ����ϴ� xml���� ${������} �� ���� �κ��� ���� ������ ��ȯ��Ų��.
	 * 
	 * @param req
	 * @param res
	 * @param src
	 * @param input
	 * @param output
	 */
	private void processLayoutXml(HttpServletRequest req,
			HttpServletResponse res, String src, DataSet input, DataSet output)
			throws ServletException, IOException {
		res.setContentType("text/xml; charset=euc-kr");
		PrintWriter writer = res.getWriter();

		try {
			// Config conf = Configuration.lookup("/site");
			// String htdocs = conf.getString("documentRoot");
			String htdocs = JDFrameContextListener.getDocumentRoot();

			String layoutUri = htdocs + src;

			SmartFile sf = new SmartFile(layoutUri);

			if (!sf.exists()) {

				// layoutUri = req.getRealPath(src);
				layoutUri = ctx.getRealPath(src);
				sf = new SmartFile(layoutUri);
			}

			String content = sf.getContent();

			if (content.indexOf("${") > 0) {
				Block[] blocks = output.getIOSchema().getOutputBlocks();

				for (int i = 0; i < blocks.length; i++) {
					Field[] fields = blocks[i].getFields();

					for (int j = 0; j < fields.length; j++) {
						String fieldNm = fields[j].getName();

						content = StringFormater.replaceStr(content, "${"
								+ fieldNm + "}", output.getText(fieldNm));
					}
				}
			}

			writer.write(content);

			res.flushBuffer();

		} catch (Throwable e) {
			processError(e, req, res);

			StringWriter strWriter = new StringWriter();
			PrintWriter printwriter = new PrintWriter(strWriter);
			e.printStackTrace(printwriter);

			writer.println(strWriter.toString());

		}

	}

	/**
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 */
	private void doSoap(String trCode, String soapAction,
			HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		Logger.debug.println(LOG_ID + "process SOAP");

		String contentType = req.getContentType();

		String encoding = "euc-kr";

		try {
			// text/xml; charset=UTF-8 ���� UTF-8 �� �����´�.
			encoding = SmartStringArray.split("=", contentType)[1]
					.toUpperCase();
			encoding = StringFormater.replaceStr(encoding, "\"", "");
			Logger.debug.println(LOG_ID + " soap encoding:" + encoding);
		} catch (Exception e) {
			Logger.warn.println(LOG_ID + "default encoding:" + encoding);
		}

		res.setContentType("text/xml; charset=" + encoding);

		// ByteArrayOutputStream bas = new ByteArrayOutputStream();
		HttpServlerResponseWriter httpWriter = new HttpServlerResponseWriter(
				res, encoding);

		// �� url�� target namespace�� �ȴ�.
		String reqUrl = req.getRequestURL().toString();

		int pt = reqUrl.indexOf("/", 8);
		// reqUrl = AnyLogicControlServlet.getHostName() + reqUrl.substring(pt);
		reqUrl = reqUrl.substring(pt + 1);// �տ� / �� �ִ� ��� xpath�� ����� �ȵǴ� ��찡
		// �ִ�.
		// IOSchemaInfo�͵� �����־�� �Ѵ�.

		// ��������
		// SessionTracer tracer = SessionTracer.getInstance(this, trCode);
		// tracer.start();

		try {
			httpWriter.write("<?xml version=\"1.0\" encoding=\"" + encoding
					+ "\"?>");

			// httpWriter.write(SOAP_ROOT_S);
			httpWriter.write("<" + SOAP_PREFIX + ":Envelope ");
			httpWriter.write("xmlns:" + SOAP_PREFIX + "=\"" + SOAP_NS + "\" ");
			httpWriter.write("xmlns=\"" + reqUrl + "\" ");
			httpWriter
					.write("soap:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\">");

			httpWriter.write(SOAP_HEADER_S);
			httpWriter.write(SOAP_HEADER_E);
			httpWriter.write(SOAP_BODY_S);

			DataSet input = new DataSet();

			try {
				// req.getParameterNames().hasMoreElements()
				// SOAP �׽�Ʈ��
				if (req.getParameter("SOAPTEST") != null)
					setDataSet(req, input);
				else
					processSoapRequest(req, input);

				DataSet output = null;
				Connection connection = null;

				try {
					connection = DefaultConnectionFactory.getConnection();
					Interaction inter = connection.createInteraction();
					output = inter.execute(trCode, input);
				} catch (Exception ee) {
					throw ee;

					// throw e;
				} finally {
					if (connection != null)
						connection.close();
				}

				TransformerFactory tFactory = TransformerFactory.getInstance();

				Transformer transformer = tFactory.getTransformer("xml");

				// bas.write(DATASET_S);
				// String d = "<dataset xmlns=\"" + reqUrl + "\">";

				httpWriter.write("<dataset xmlns=\"");
				httpWriter.write(reqUrl);
				httpWriter.write("\">");

				transformer.transform(output, httpWriter);

				httpWriter.write("</dataset>");

			} catch (Exception e) {
				httpWriter.write(SOAP_FAULT_S);
				httpWriter.write(SOAP_FAULT_STR_S);
				String errMsg = e.toString();
				errMsg = HtmlFormat.translate(errMsg);

				httpWriter.write(errMsg);
				httpWriter.write(SOAP_FAULT_STR_E);

				httpWriter.write(SOAP_FAULT_E);

			}
			httpWriter.write(SOAP_BODY_E);
			httpWriter.write(SOAP_ROOT_E);

			httpWriter.flush();

			res.flushBuffer();
		} catch (IOException ioe) {
			throw new ServletException(ioe);
		} finally {
			// tracer.end();
		}

	}

	public static void main(String[] args) throws Exception {
		// String loc = req.getPathInfo();
		String loc = "/process/grid/demo1.xml";

		// ������ . �� ��ġ
		int lp = loc.lastIndexOf(".");

		String trname = loc.substring(loc.indexOf("/", 1) + 1, lp);
		String type = loc.substring(lp + 1);

		System.out.println(trname);
		System.out.println(type);

	}

}