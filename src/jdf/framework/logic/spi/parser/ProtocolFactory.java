package jdf.framework.logic.spi.parser;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import jdf.framework.core.xml.XMLReferer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;


/**
 * Protocol 객체를 얻기위한 Factory 성 class
 * 
 * <pre>
 * Protocol protocol = ProtocolFactory.getProtocol(&quot;text&quot;);
 * 
 * DataSetParser parser = protocol.getParser();
 * 
 * </pre>
 * 
 * @author
 */

public class ProtocolFactory {

	// 기본 프로토콜
	private static Protocol defaultProtocol;

	// 프로토콜의 묶음
	private static Map protocols = new HashMap();

	private static boolean isInit = false;

	/**
	 * 기본 Protocol 을 얻는다.
	 * 
	 * 
	 */
	public static Protocol getProtocol() {
		init();

		if (defaultProtocol == null)
			Logger.err
					.println("<ProtocolFactory> not setting defualt protocol");

		return defaultProtocol;
	}

	/**
	 * 
	 * 특정 이름의 Protocol을 가져온다.
	 * 
	 */
	public static Protocol getProtocol(String name) {
		init();

		return (Protocol) protocols.get(name);
	}

	private static void init() {
		if (!isInit) {
			load();
			isInit = true;
		}
	}

	/**
	 * protocol 정의 화일로 부터 내용을 읽어 Protocol을 생성한다.
	 * 
	 * 
	 */
	private static void load() {
		try {

			String dir = Configuration.getConfigPath() + File.separator
					+ "protocol";

			Config conf = Configuration.lookup("/resource/anylogic/protocol");
			String dir2 = conf.getString("dir", dir);

			if (dir2 != null && dir2.length() > 0)
				dir = dir2;

			String files[] = getDescriptorFiles(new File(dir));

			Logger.debug.println("<ProtocolFactory> road dir:" + dir);

			for (int i = 0; i < files.length; i++) {
				String filename = files[i];

				try {
					XMLReferer xmlDoc = new XMLReferer(dir, filename);

					xmlDoc.lookup("/business-protocol-definition");

					String protocolName = xmlDoc.getString("name");
					String isDefault = xmlDoc.getString("default");

					String parserClassName = xmlDoc.find("parse-driver")
							.getText();

					DataSetParser parser = (DataSetParser) Class.forName(
							parserClassName).newInstance();

					Protocol protocol = new Protocol(protocolName, parser);

					xmlDoc.lookup("type-definition/localType");

					while (xmlDoc.next()) {
						String name = xmlDoc.getString("name");
						String type = xmlDoc.getString("transType");
						String align = xmlDoc.getString("opt-align");
						String fill = xmlDoc.getString("opt-fill");
						String decimal = xmlDoc.getString("opt-decimal");
						String format = xmlDoc.getString("format");

						FieldParser childField = null;

						String type2 = xmlDoc.find("remoteType").getString(
								"transType");
						String format2 = xmlDoc.find("remoteType").getString(
								"format");

						childField = parser.getFieldParserInstance().setInfo(
								"", type2, format2, FieldParser.ALIGN_LEFT,
								(byte) 0x00, 0);

						// FieldTypeDef(String keyName, String
						// typeClassName, String format, int align, byte
						// fillData, int decimalPoint)

						int iAlign = FieldParser.ALIGN_LEFT;
						if ("right".equals(align))
							iAlign = FieldParser.ALIGN_RIGHT;

						byte bFill = (byte) 0x00;

						if (fill != null && fill.length() > 0)
							bFill = fill.getBytes()[0];

						int decimalPoint = 0;

						try {
							decimalPoint = Integer.parseInt(decimal);
						} catch (Exception eee) {
						}

						FieldParser field = parser.getFieldParserInstance()
								.setInfo(name, type, format, iAlign, bFill,
										decimalPoint);
						field.setRemoteType(childField);

						protocol.put(field);

					}

					protocols.put(protocolName, protocol);

					if ("true".equals(isDefault))
						defaultProtocol = protocol;

				} catch (ClassNotFoundException cne) {
					Logger.warn
							.println("<ProtocolFactory> parse-driver not found. "
									+ cne.getMessage());
				} catch (InstantiationException ine) {
					Logger.warn
							.println("<ProtocolFactory> parse-driver create err. "
									+ ine.getMessage());
				}

			}
		} catch (Exception ee) {
			Logger.err.println("<ProtocolFactory> err2 " + ee.getMessage());
			ee.printStackTrace();
		}

	}

	private static String[] getDescriptorFiles(File dir) {
		if ((!dir.exists()) || (!dir.isDirectory())) {
			return new String[0];
		}

		String[] contents = dir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(".xml"))
					return true;
				else
					return false;
			}
		});

		return contents;
	}

}