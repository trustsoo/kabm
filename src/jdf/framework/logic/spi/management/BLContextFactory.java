package jdf.framework.logic.spi.management;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.schema.*;
import jdf.framework.core.log.Logger;
import jdf.framework.core.pool.cache.CacheManager;
import jdf.framework.core.pool.cache.CacheManagerFactory;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.xml.XMLReferer;
import jdf.framework.logic.spi.ResourceAdapter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FilenameFilter;
import java.util.*;


/**
 * <b><code>BLContextFactory</code> </b>
 * <p>
 * Legacy 시스템이나 일반적인 TCP/IP 소켓을 이용한 데이타 처리 시스템과 데이타를 주고 받기 위해 사용되는 데이타에서 하나의 필드가
 * 가지는 데이타 타입을 정의한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public final class BLContextFactory {
	private final static String LOG_ID = "<l:BLContextFactory> ";

	private static BLContextFactory instance = new BLContextFactory();

	private Map<String, BusinessLogicContext> tables;

	private DeploymentManager deployer = DeploymentManager.getInstance();

	private String workingDir;

	private long check_interval_time = 5000;

	// private long lastCheckTime;

	private static Map<String, BldLoadListener> bldLoadListenerList = new HashMap<>();



	private BLContextFactory() {
		// initialize();
	}

	/**
	 * BldLoadListener 를 설정한다.
	 * 
	 * @param name
	 * @param listener
	 */
	public static void setBldLoadListener(String name, BldLoadListener listener) {
		bldLoadListenerList.put(name, listener);
	}

	private void callBldLoadListenerBeforeLoadMethod(String bldFullName,
			XMLReferer doc) throws ResourceException {
		Collection<BldLoadListener> c = bldLoadListenerList.values();
		Iterator<BldLoadListener> i = c.iterator();

		while (i.hasNext()) {

			BldLoadListener listener = i.next();

			listener.onBeforeLoad(bldFullName, doc);
		}
	}

	/**
	 * XMLReferer 를 실행한다.
	 * 
	 * @param doc
	 */
	private void callBldLoadListenerOnLoadCompleteMethod(String bldFullName,
			XMLReferer doc) {
		Collection<BldLoadListener> c = bldLoadListenerList.values();
		Iterator<BldLoadListener> i = c.iterator();

		while (i.hasNext()) {

			BldLoadListener listener = (BldLoadListener) i.next();

			listener.onLoadComplete(bldFullName, doc);
		}
	}

	/**
	 * BLD 가 위치하는 기본 디렉토리 위치 정보를 반환한다.
	 * 
	 * @return
	 */
	public String getWorkingDir() {
		if (this.workingDir == null)
			loadWorkDir();

		return this.workingDir;
	}

	private String loadWorkDir() {
		String dir = Configuration.getConfigPath() + File.separator
				+ "io_schema";

		try {

			Config conf = Configuration.lookup("/resource/anylogic/ioSchema");
			String dir2 = conf.getString("dir");

			if (dir2 != null && dir2.trim().length() > 0) {

				if (dir2.indexOf("file:///") == 0)
					dir2 = dir2.substring(8);

				if (dir2.indexOf("./") >= 0)
					dir2 = Configuration.getConfigPath() + dir2.substring(1);

				dir = dir2;
			}

			workingDir = dir;

		} catch (Exception e) {
		}

		return dir;
	}

	/**
	 * io schema 화일을 읽어들이고 초기화 하는 작업을 한다.
	 * 
	 * 
	 */
	public void initialize() {

		String dir = loadWorkDir();

		tables = new Hashtable<>();

		boolean isLoadAll = true;

		try {
			Config conf = Configuration.lookup("/resource/anylogic/ioSchema");
			check_interval_time = conf.getLong("checkIntervalTime", 5000L);
			Logger.info.println(LOG_ID + "check_interval_time:"
					+ check_interval_time);

			isLoadAll = conf.getBoolean("initLoadAll", true);
			Logger.info.println(LOG_ID + "initLoadAll:" + isLoadAll);

		} catch (Exception e) {
			Logger.info.println(LOG_ID + "fail. ", e);
		}

		Logger.info.println(LOG_ID + "load schema root dir : " + dir);

		if (isLoadAll) {

			loadSchemaFile(dir, null);

			// io schema 디렉토리에 하위디렉토리 명을 뽑아온다.
			String relativeDirNames[] = getDirNameArray(new File(dir));

			for (int i = 0; i < relativeDirNames.length; i++) {
				String location = dir + "/" + relativeDirNames[i];

				loadSchemaFile(location, relativeDirNames[i]);
			}

			// lastCheckTime = System.currentTimeMillis();
		}

	}

	/**
	 * 해당 디렉토리에서 xml 화일을 선별하고 그중 schema file memory에 load시킨다.
	 * 
	 * @param java.lang
	 *            String dir 디렉토리명
	 * @param java.lang.String
	 *            trName 상대 디렉토리명을 가지는 tr 이름
	 */
	private void loadSchemaFile(String dir, String trNameDir) {
		//int loadFileNum = 0;

		String files[] = getDescriptorFiles(new File(dir));

		// Logger.debug.println(LOG_ID + "load dir:" + dir);

		for (int i = 0; i < files.length; i++) {
			String filename = files[i];

			try {
				getBusinessLogicContext(dir, trNameDir, filename);
				//loadFileNum++;
			} catch (Exception ee) {
				System.err.println(ee);
			}

		}

		if (trNameDir == null)
			trNameDir = ".";
		// Logger.info.println(LOG_ID + "load schema [#" + loadFileNum + "] from
		// " + trNameDir);
	}

	private CacheManagerFactory cacheFact = CacheManagerFactory.getInstance();

	/**
	 * 
	 * 
	 * 
	 * 
	 * @param dir
	 * @param trNameDir
	 * @param filename
	 * @return
	 */
	BusinessLogicContext getBusinessLogicContext(String dir, String trNameDir,
			String filename) throws ResourceException {

		IOSchemaContext ctx = null;

		try {
			File xmlFile = new File(dir, filename);
			// xmlFile.lastModified()

			XMLReferer xmlDoc = new XMLReferer(xmlFile);

			xmlDoc.lookup("/transaction");

			if (!xmlDoc.next())
				throw new Exception("abnormal IO Schema");

			// while (xmlDoc.next())
			{
				String name = xmlDoc.getString("name");

				// BLD를 미리 검사
				callBldLoadListenerBeforeLoadMethod(name, xmlDoc);

				// System.out.println(filename+" ;;; "+name);

				if (filename.indexOf(name + ".") != 0)
					throw new Exception(name + " mismatch filename");

				if (trNameDir != null && trNameDir.length() > 0)
					name = trNameDir + "/" + name;

				xmlDoc.mark();
				xmlDoc.lookup("/transaction/processor-info");
				String type = xmlDoc.getString("type");
				String datasource = xmlDoc.getString("datasource");

				String transactionType = xmlDoc.lookup("transaction-type")
						.getText();

				xmlDoc.lookup("/transaction/processor-info");

				Processor process = null;
				boolean isProcessIoschema = true;

				// type 정보가 없다면 process 기반의 ioschema가 아니다.
				// 순수 I/O를 정의하는 ioschema
				if (type == null || type.length() == 0)
					isProcessIoschema = false;

				if (isProcessIoschema) {

					RADeployDescriptor rdc = deployer.getRADescriptor(type);

					if (rdc == null)
						throw new ResourceException(type + " RA not exist");

					ProcessorFactory factory = rdc.getProcessorFactory();

					ctx = new IOSchemaContext(name, xmlDoc, xmlFile.getAbsolutePath());
					ctx.setModifyTime(xmlFile.lastModified());

					xmlDoc.reset();

					process = factory.getProcessor(rdc, ctx);

					// cache 정보를 processor 객체에 남는다.
					xmlDoc.lookup("/transaction/processor-info/cache");

					String eventTrCode = xmlDoc.find("event").getText();

					if (eventTrCode != null && eventTrCode.length() > 0) {
						process.setResetCacheTrName(eventTrCode);
					}

					if ("true".equals(xmlDoc.find("enable").getText())) {
						try {
							String expire = xmlDoc.find("expire").getText();
							String maxObjNum = xmlDoc.find("max-object-num")
									.getText();

							if (maxObjNum == null || maxObjNum.length() == 0)
								maxObjNum = "100";

							CacheInfo cacheInfo = new CacheInfo();
							cacheInfo.setExpireSecondTime(Long
									.parseLong(expire) * 1000l);
							cacheInfo.setMaxObjectCount(Integer
									.parseInt(maxObjNum));

							// process.setCacheInfo(cacheInfo);

							CacheManager mgr = cacheFact.createCacheManager(
									name, cacheInfo);
							process.setCacheManager(mgr);
						} catch (Exception eee) {
							Logger.warn
									.println(LOG_ID + "load cache info err. "
											+ name + " ", eee);
						}
					}

				}

				// Processor process= getProcess(name, xmlFile.lastModified(),
				// xmlDoc);

				Block[] inBlocks = null;
				{
					try {
						xmlDoc.lookup("/transaction/input/block");

						inBlocks = getBlockArray(xmlDoc, IOSchema.IN);

						// xml에 input/block이 정의되어있지 않으면
						// process 정보에서 inBlocks 정보를 추출한다.

						/*
						 * if (inBlocks.length == 0) inBlocks=
						 * getInputBlockArray(process);
						 * 
						 */
					} catch (IllegalArgumentException iae) {
						throw iae;
					} catch (ResourceException re) {
						throw re;
					} catch (Exception ee) {
						Logger.warn.println(LOG_ID
								+ "input block not exist. id:" + name);
					}

				}

				Block[] outBlocks = null;
				{

					try {
						xmlDoc.lookup("/transaction/output/block");

						outBlocks = getBlockArray(xmlDoc, IOSchema.OUT);

						/*
						 * if (outBlocks.length == 0) outBlocks=
						 * getOutputBlockArray(process);
						 */
					} catch (IllegalArgumentException iae) {
						throw iae;
					} catch (ResourceException re) {
						throw re;
					} catch (Exception ee) {
						// ee.printStackTrace();
						Logger.warn.println(LOG_ID + "out block not exist. id:"
								+ name, ee);
					}

				}

				ResourceAdapter ra = deployer.getResourceAdapter(type);

				IOSchema ioSchema = new IOSchema(name, inBlocks, outBlocks);

				if (isProcessIoschema && process != null) {

					// 데이터 검증
					validateSchema(ioSchema, process);

					// 필요에 따라 생성 Process를 이용할 수 있고
					ioSchema.setProcessor(process);
					// 아니면 그때 그때 생성해서 쓴다.
					ioSchema.setProcessorClass(process.getClass());
				}
				xmlDoc.lookup("/transaction/info");

				String enableHttpAccessTxt = xmlDoc.find("http-access")
						.getText();
				boolean enableHttpAccess = false;
				if ("true".equals(enableHttpAccessTxt))
					enableHttpAccess = true;

				ioSchema.setEnableHttpAccess(enableHttpAccess);

				String desc = xmlDoc.find("description").getText();
				ioSchema.setDescription(desc);

				xmlDoc.lookup("/transaction/input/xml-document");

				if (xmlDoc.next()) {

					Node tmp = xmlDoc.getNode();
					// System.out.println("--3 " + tmp.getNodeName());

					// 자식들
					NodeList children = tmp.getChildNodes();

					Node child = null;
					for (int i = 0; i < children.getLength(); i++) {
						child = children.item(i);
						if (child.getNodeType() != Node.TEXT_NODE)
							break;
					}

					// System.out.println("--4 " + child.getNodeName());

					java.io.StringWriter w1 = new java.io.StringWriter();
					jdf.framework.core.xml.DOMWriter w = new jdf.framework.core.xml.DOMWriter(w1);
					w.print(child);

					ioSchema.setInputXmlText(w1.toString());

					// System.err.println(">>>" + w1.toString());

					ioSchema.setInputXmlNode(child);
				}

				BusinessLogicContext blctx = new BusinessLogicContext(name, ra,
						ioSchema);

				// 화일의 최종 수정읽을 기록
				blctx.setModifyTime(xmlFile.lastModified());
				blctx.setFileInfo(dir, filename);

				blctx.setProperty("datasource", datasource);

				if ("rollback".equals(transactionType))
					blctx.setTransactionSupport(true);

				tables.put(name, blctx);

				// 메모리 적재완료시간
				ctx.setLoadTime(System.currentTimeMillis());

				// BLD 로드 완료후 BLD 로드 완료된것을 알고싶어하는 class 들을
				// 호출해준다.
				callBldLoadListenerOnLoadCompleteMethod(name, xmlDoc);

				Logger.debug.println(LOG_ID + "LOAD:" + name);

				return blctx;
			}
		} catch (ResourceException re) {
			Logger.warn.println(LOG_ID + "FAIL:" + filename);
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.warn.println(LOG_ID + "FAIL:" + filename);
			throw new ResourceException("BLD load error. " + filename + ":"
					+ e.getMessage(), e);
		} catch (Error err) {
			Logger.err.println(LOG_ID + "FAIL:" + filename);
			throw new ResourceException("BLD load error. " + filename + ":"
					+ err.getMessage(), err);
		}

		// return null;
	}

	/**
	 * 검증한다.
	 * 
	 * @param ioschema
	 *            IOSchema 객체
	 * @param processor
	 *            Process 객체
	 * @throws Exception
	 *             검증오류
	 */
	private void validateSchema(IOSchema ioschema, Processor processor)
			throws ResourceException {

		StoredQuery[] query = processor.getStoredQueryArray();

		for (int i = 0; i < query.length; i++) {
			String[] inputFieldNames = query[i].getIntputFieldNames();
			isExistInSchema(inputFieldNames, ioschema);

			String[] outputFieldNames = query[i].getOutputFieldNames();
			isExistInSchema(outputFieldNames, ioschema);

		}

	}

	private void isExistInSchema(String[] fieldNames, IOSchema ioschema)
			throws ResourceException {
		for (int i = 0; i < fieldNames.length; i++) {
			if ("".equals(fieldNames[i]))
				continue;

			if (ioschema.getInputField(fieldNames[i]) == null) {
				if (ioschema.getOutputField(fieldNames[i]) == null)
					throw new ResourceException("Query property ["
							+ fieldNames[i] + "] is not <field> define.");
			}

		}

	}

	/**
	 * 
	 * DataSchemaManager instance를 얻는다.
	 * 
	 * @return DataSchemaManager
	 */
	public static BLContextFactory getInstance() {
		/*
		 * if (instance == null) instance= new BLContextFactory();
		 */

		return instance;
	}

	/**
	 * 해당 이름을 가지는 DataSchema
	 * 
	 */
	public IOSchema getIOSchema(String name) throws ResourceException {
		BusinessLogicContext ctx = getBusinessLogicContext(name);
		if (ctx == null)
			throw new ResourceException(name + " not exist.");
		return ctx.getIOSchema();
	}

	/**
	 * BusinessLogicContext 구현객체를 얻는다.
	 * 
	 * @param name
	 *            bld fullpath 명
	 * @return
	 * @throws ResourceException
	 */
	public BusinessLogicContext getBusinessLogicContext(String name)
			throws ResourceException {
		BusinessLogicContext result = (BusinessLogicContext) tables.get(name);

		// 없는 경우는 실제 resource를 검색
		if (result == null) {
			String dir = this.workingDir;
			String filename = name;
			String trNameDir = null;
			int idx = name.lastIndexOf("/");

			if (idx > 0) {

				trNameDir = name.substring(0, idx);
				filename = name.substring(idx + 1);

				dir = dir + "/" + trNameDir;
			}

			filename = filename + ".xml";

			result = this.getBusinessLogicContext(dir, trNameDir, filename);
		}

		// check interval 시간이 0 이상이어야한 화일 시간체크를 한다.
		else if (check_interval_time > 0) {
			long crntTime = System.currentTimeMillis();

			if (crntTime - result.getCheckTime() > check_interval_time) {
				/*
				 * System.out.println( " 1 ** "+ crntTime ); System.out.println( "
				 * 2 ** "+ result.getCheckTime() ); System.out.println( " 3 ** "+ (
				 * crntTime-result.getCheckTime() ));
				 */

				// 화일 수정일 일어났다면
				if (result.getModifyTime() != result.getIOSchemaFile()
						.lastModified()) {
					String dir = result.getDir();
					String filename = result.getFilename();

					String trNameDir = null;
					int idx = name.lastIndexOf("/");

					if (idx > 0)
						trNameDir = name.substring(0, idx);

					result = this.getBusinessLogicContext(dir, trNameDir,
							filename);

				} else
					result.setCheckTime(crntTime);
			}

		}

		if (result == null)
			throw new ResourceException(name + " schema is not exist");

		return result;

	}

	/**
	 * BLD의 block 정보를 분석하여 jdf.framework.core.data.schema..Block 객체를 생성한다.
	 * 
	 * @param xmlDoc
	 * @param inOutType
	 * @return
	 */
	private Block[] getBlockArray(XMLReferer xmlDoc, int inOutType)
			throws IllegalArgumentException, ResourceException {

		List<Block> list = new ArrayList<>();
		
		int seq=0;

		while (xmlDoc.next()) {
			xmlDoc.mark();

			String name = xmlDoc.getString("name");
			String repeat = xmlDoc.getString("repeat");
			String block_id = xmlDoc.getString("id");
			String accessLevel = xmlDoc.getString("access");
			String properties = xmlDoc.getString("properties");
			String className = xmlDoc.getString("class");
			String bldBlockId = xmlDoc.getString("ref"); // 내/외부 참조 bld block
			String xpathExpression = xmlDoc.getString("xpath");
			
			seq++;
			if(name==null)
				name="block"+seq;

			Field[] fields = null;
			Block block = null;

			Class<?> mappingClass = null;
			// POJO 기반으로 bld를 사용하는 경우
			// class를 정의하고 그 class의 property를 field로 사용한다.
			if (className != null && className.length() > 0) {
				try {
					Class<?> modelClass = Class.forName(className);
					fields = getFieldArray(modelClass);

					mappingClass = modelClass;

					// class에서 기본 field정보를 추출하고
					// <field> 가 추가적으로 정의가 되어있다면
					// 기존 field 정보를 치환한다.
					xmlDoc.lookup("field");
					Field[] replacefields = getFieldArray(xmlDoc);

					for (int u = 0; u < replacefields.length; u++) {

						boolean isFind = false;
						for (int p = 0; p < fields.length; p++) {

							String fieldNm = fields[p].getName();

							if (fieldNm.equals(replacefields[u].getName())) {

								fields[p] = replacefields[u];
								isFind = true;
								break;
							}
						}
						if (!isFind) {
							Field[] tmp = new Field[fields.length + 1];

							System.arraycopy(fields, 0, tmp, 0, fields.length);
							tmp[tmp.length - 1] = replacefields[u];
							fields = tmp;
						}

					}

				} catch (ClassNotFoundException cnfe) {
					throw new IllegalArgumentException(cnfe.toString());
				} catch (IntrospectionException ise) {
					throw new IllegalArgumentException(ise.toString());
				}

			}

			// 외부 BLD 에서 Field 모델정보를 정의한 경우
			else if (bldBlockId != null && bldBlockId.length() > 0) {

				int p = bldBlockId.indexOf("#");

				if (p > 0) {
					String bldName = bldBlockId.substring(0, p);
					bldBlockId = bldBlockId.substring(p + 1);
					IOSchema schema = getIOSchema(bldName);

					// 해당 ID 의 block 만 가져온다.
					block = schema.getBlockById(bldBlockId);

				}
				// bldName.blockId 형태로 되어있지 않고
				// bldName 으로만 되어있다면 전체를 return
				else {

					IOSchema schema = getIOSchema(bldBlockId);

					if (inOutType == IOSchema.IN) {
						return schema.getInputBlocks();
					} else if (inOutType == IOSchema.OUT) {
						return schema.getOutputBlocks();
					}
				}

			}

			else {
				xmlDoc.lookup("field");
				fields = getFieldArray(xmlDoc);
			}

			// 위에서 block 이 생성되었다면
			// 아래 부분은 수행할 필요가 없다.
			if (block == null) {

				try {
					int repeatNum = 1;
					if (repeat != null && repeat.length() > 0) {
						repeatNum = Integer.parseInt(repeat);
						block = new Block(name, repeatNum, fields);
					} else
						block = new Block(name, null, fields);
				} catch (Exception e) {
					block = new Block(name, repeat, fields);
				}

				block.setId(block_id);
				block.setAccessLevel(accessLevel);
				block.setPropertyStr(properties);
				block.setXpathExpression(xpathExpression);
				block.setMappingClass(mappingClass);
			}

			list.add(block);

			xmlDoc.reset();
		}

		return (Block[]) list.toArray(new Block[list.size()]);
	}

	/**
	 * class 로부터 field 정보를 추출한다.
	 * 
	 * @param javaClass
	 * @return
	 * @throws IntrospectionException
	 */
	private Field[] getFieldArray(Class<?> javaClass)
			throws IntrospectionException {
		BeanInfo info = Introspector.getBeanInfo(javaClass);

		PropertyDescriptor[] properties = info.getPropertyDescriptors();

		List<Field> list = new ArrayList<>();
		for (int i = 0; i < properties.length; i++) {
			// 이름
			String name = properties[i].getName();

			Class<?> typClass = properties[i].getPropertyType();
			if (typClass == null) {
				Logger.debug.println(LOG_ID + "클래스필드정보 SKIP [type is null "
						+ name);
				continue;
			}
			String type = typClass.getName();

			int typeP = type.lastIndexOf(".");
			// java.lang.String 과 같이 되있다면 String 만 얻는다.
			if (typeP > 0)
				type = type.substring(typeP + 1);

			String format = "";
			String def = ""; // 기본값
			int decimalPoint = 0;

			String toType = "";
			int iType = Field.getType(type);
			int iToType = Field.getType(toType);
			int iSize = 0;

			// 정의할수 없는 타입인 경우 continue
			if (iType == Field.UNDEFINED) {
				Logger.debug.println(LOG_ID + "클래스필드정보 SKIP " + name + " "
						+ type);
				continue;
			}

			Field f = new Field(name, type, iType, iSize, def, format,
					decimalPoint, iToType);

			list.add(f);
		}

		return (Field[]) list.toArray(new Field[list.size()]);
	}

	/**
	 * XML 문서로 부터 Field 정보를 추출한다.
	 * 
	 * @param xmlDoc
	 * @return
	 */
	private Field[] getFieldArray(XMLReferer xmlDoc) {
		List<Field> list = new ArrayList<>();

		while (xmlDoc.next()) {
			String name = xmlDoc.getString("name");

			if (name == null)
				continue;

			String type = xmlDoc.getString("type");
			String size = xmlDoc.getString("size");
			String def = xmlDoc.getString("default");
			String decimal = xmlDoc.getString("decimal");
			String format = xmlDoc.getString("format");
			String toType = xmlDoc.getString("toType");
			String xpath = xmlDoc.getString("xpath");

			String label = xmlDoc.getString("label");
			String little_endian = xmlDoc.getString("little-endian"); // little
			// endian
			// 여부

			// String typeProcessor = xmlDoc.getString("processor");

			String propertiesStr = xmlDoc.getString("properties");

			int decimalPoint = 0;
			try {
				decimalPoint = Integer.parseInt(decimal);
			} catch (Exception ee) {
			}

			int iType = Field.getType(type);

			int iToType = Field.getType(toType);
			int iSize = 0;

			if (size != null && size.length() > 0)
				iSize = Integer.parseInt(size);

			if (def == null || type.length() == 0) {
				if (iType == Field.INTEGER || iType == Field.LONG
						|| iType == Field.DOUBLE || iType == Field.FLOAT)
					def = "0";
			}

			// field type name은 대문자로 통일
			Field field = new Field(name, type, iType, iSize, def, format,
					decimalPoint, iToType);

			if (label != null && label.length() > 0)
				field.setLabel(label);

			if ("true".equals(little_endian))
				field.setLittleEndianType(true);

			if (propertiesStr != null && propertiesStr.length() > 0)
				field.setPropertyStr(propertiesStr);

			if (xpath != null && xpath.length() > 0)
				field.setXpathExpression(xpath);

			// block의 id 설정
			field.setRefId(xmlDoc.getString("ref"));

			/*
			 * if (typeProcessor != null && typeProcessor.length() > 0)
			 * field.setTypeProcessor(typeProcessor);
			 */

			list.add(field);
		}

		return (Field[]) list.toArray(new Field[list.size()]);

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

	private static void setDirNameArray(File dir, String baseDir,
			SmartStringArray array) {
		if ((!dir.exists()) || (!dir.isDirectory())) {
			return;
		}

		String[] dirNames = dir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {

				File f = new File(dir, name);

				if (f.isDirectory())
					return true;
				else
					return false;
			}
		});

		for (int i = 0; i < dirNames.length; i++) {
			StringBuffer dirNameBuf = new StringBuffer();

			// String dirName = "";
			if (baseDir != null)
				dirNameBuf.append(baseDir).append("/");
			// dirName = baseDir + "/";

			dirNameBuf.append(dirNames[i]);

			String dirName = dirNameBuf.toString();

			// dirName = dirName + dirNames[i];
			array.add(dirName);
			// Logger.debug.println(">>> " + dirName);

			File f = new File(dir, dirNames[i]);

			setDirNameArray(f, dirName, array);
		}

	}

	private static String[] getDirNameArray(File dir) {
		SmartStringArray array = new SmartStringArray();

		setDirNameArray(dir, null, array);

		return array.toArray();

	}

}