package jdf.framework.logic.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.data.schema.ScriptContext;
import jdf.framework.core.http.JDFrameContextListener;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.DateTime;
import jdf.framework.core.util.SimpleEncrypt;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringFormater;
import jdf.framework.logic.spi.ProtocolParam;
import jdf.framework.logic.spi.auth.User;
import jdf.framework.logic.spi.auth.UserManager;
import jdf.framework.logic.spi.auth.UserManagerFactory;
import jdf.framework.logic.spi.management.DeploymentManager;
import jdf.framework.logic.spi.management.RADeployDescriptor;



/**
 * �� package�� �����ϴ� servlet�� �ϳ��� control �ϴ� servlet
 * 
 * 
 * @author
 */
public class AnyLogicControlServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String LOG_ID = "<l:AnyLogicControlServlet> ";

	private Map modules;

	private static String hostname = "http://localhost";

	private static String servletAliasName = null;

	private ServletContext ctx;

	private ScriptContext sc;

	// ������ Ű
	private static double randomKey = Math.random();

	public AnyLogicControlServlet() {
		super();
	}

	/**
	 * �� servlet �� ������� ����� User ��ü�� ��´�. ����� ������ ���� �� ��� ��� exception �߻�
	 * 
	 * 
	 * @return
	 */
	public static User getAccessUser(HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {

		String errMsg = null;

		String contextName = req.getContextPath();

		// session �� ���� id�� üũ�Ѵ�.

		String userid = (String) req.getSession().getAttribute(
				ProtocolParam.HTTP_HEADER_NAME_AUTH_ID + contextName);
		if (userid != null) {
			String roles = (String) req.getSession().getAttribute(
					ProtocolParam.HTTP_HEADER_NAME_AUTH_ROLES + contextName);
			User user = new User(userid);
			user.setRoles(roles);
			return user;
		}

		// HTTP Header ���� ���� �˻�
		String key = req.getHeader(ProtocolParam.HTTP_HEADER_NAME_AUTH_KEY);
		if (key == null || key.length() == 0) {
			key = req.getParameter(ProtocolParam.HTTP_HEADER_NAME_AUTH_KEY);
		}

		Cookie[] cookies = req.getCookies();
		// ������ cookie ���� üũ
		for (int i = 0; key == null && cookies != null && i < cookies.length; i++) {
			String name = cookies[i].getName();

			if (ProtocolParam.HTTP_HEADER_NAME_AUTH_KEY.equals(name)) {
				key = cookies[i].getValue();

			}
		}

		if (key != null) {

			/*
			 * byte[] result = (new sun.misc.BASE64Decoder()).decodeBuffer(key);
			 * String resolveStr = new String(result);
			 */
			String resolveStr = SimpleEncrypt.decrypt(key);

			// ù��°�� id�� ����־�� �ϰ�
			// �ι�°�� ip�� �־�� �Ѵ�.
			// ����°�� �ð��� ����־�� �Ѵ�.
			// �׹�°�� ����� ���� ������ �� �ִ�.
			// �ټ���°�� context �̸�
			String[] param = SmartStringArray.split("|", resolveStr);

			if (param != null && param.length == 5) {
				userid = param[0];

				String clientIp = param[1];
				long loginTime = 0;
				try {
					loginTime = Long.parseLong(param[2]);
				} catch (NumberFormatException nfe) {

				}

				String roles = param[3];
				String ctxName = param[4];

				String ip = getRemoteAddr(req);

				// context ���� /appserver/tms �� ���� �߰��� / �� ������ �̻�߻�
				// if (ip.equals(clientIp) && contextName.equals(ctxName)) {
				if (ip.equals(clientIp)) {

					// 12�ð����ȸ� ��ȿ
					if (System.currentTimeMillis() - loginTime < 100 * 60 * 60 * 12) {

						User user = new User(userid);
						user.setRoles(roles);
						return user;
					} else
						errMsg = "�α��� ���ð� ���� �ʰ�.";

				} else
					errMsg = "long IP Information > " + ip + ":" + clientIp
							+ " " + contextName + ":" + ctxName;

			}
		} else
			errMsg = "auth key not found.";

		res.setHeader("WWW-Authenticate", "Basic realm='anyFRAME Realm'");
		res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		// ���Ѿ���.
		throw new ServletException(errMsg);

	}

	/**
	 * �� AnyLogicControlServlet�� servlet alias ���� ��´�.
	 * 
	 * @return
	 */
	public static String getServletAliasName() {
		return servletAliasName;
	}

	public ServletContext getServletContext() {
		return this.ctx;
	}

	/**
	 * ��Ű�� URI�� Configration���� ���� �����´�.
	 * 
	 * @param config
	 *            ServletConfig
	 */
	public void init(ServletConfig config) throws ServletException {

		ctx = config.getServletContext();

		JDFrameContextListener.setJdfFrameConfigPath(ctx);

		// System.out.println(" >>> "+config.getServletName()); //
		// anylogicControl �̶�� ������ ��ȯ
		// anylogicControl

		this.sc = new ScriptContext("anyframe");

		modules = new HashMap();

		// ////////////////////////////////////////////// �ߵȰ͸� ���⿡

		/*
		 * modules.put(ProtocolParam.COMMAND__LIST_FILE, new ListContent());
		 * modules.put(ProtocolParam.COMMAND__READ_FILE, new ReadContent());
		 * modules.put(ProtocolParam.COMMAND__DELETE_FILE, new DeleteContent());
		 * modules.put(ProtocolParam.COMMAND__WRITE_FILE, new WriteContent());
		 * modules.put(ProtocolParam.COMMAND__SEARCH_FILE, new SearchContent());
		 * modules.put(ProtocolParam.COMMAND__RENAME_FILE, new RenameContent());
		 * modules.put(ProtocolParam.COMMAND_SCRIPT_SUPPORT, new
		 * ScriptSupport()); // sql ����
		 * modules.put(ProtocolParam.COMMAND_QUERY_PROCESS, new
		 * QueryProcessor());
		 */

		// ///////////////////////////////////////////////
		// Anylogic�� �����Ű�� ���? ������
		modules.put("processor", new Processor());

		/**
		 * ���� processor�� ��������� ���������� ������ Ʋ����.
		 * http://anyweb:3000/anylogic/process/grid/test.xml�� ���� ��û�ϸ� .xml���� xml
		 * Ÿ�� ����
		 * 
		 * http://anyweb:3000/anylogic/process/grid/test.binary�� ���� ��û�ϸ� .binary
		 * �� binary Ÿ������ �����Ѵ�.
		 */
		modules.put("process", new IOSchemaProcess());

		// Descriptor�� Path�� ��� ����Ʈ�� �����ִ� ��
		modules.put("ralist", new RAList());

		modules.put("poollist", new PoolList());

		modules.put("cache", new CacheEventReceiver());

		modules.put("cache_mgr", new CacheManagement());

		modules.put("ioschema", new IOSchemaInfo());

		// modules.put("process-mapping", new XsdMappingProcessor());
		// modules.put("process-nomapping", new XmlDataNoMappingProcessor());

		Iterator it = modules.keySet().iterator();

		Object key = null;

		while (it.hasNext()) {

			key = it.next();
			AnyLogicControl svlt = (AnyLogicControl) modules.get(key);

			try {
				svlt.init(config);
			} catch (Exception e) {
				Logger.info.println(LOG_ID + "regist error " + key, e);
			}

			Logger.info.println(LOG_ID + "regist control:" + key);

		}

		try {
			hostname = Configuration.lookup("/site").getString("domain");
		} catch (ConfigurationException e) {
		}

		/*
		 * // LocalContentManager���� // bld��
		 * LocalContentManager.setContentFactory(ProtocolParam.PATH_BLD_URL,
		 * BldContentFactory.class); // config ���Ͽ�
		 * LocalContentManager.setContentFactory(ProtocolParam.PATH_CONFIG_URL,
		 * ConfigContentFactory.class); // dbms��
		 * LocalContentManager.setContentFactory(ProtocolParam.PATH_DBMS_URL,
		 * DbmsInfoContentFactory.class);
		 */

	}

	/**
	 * ���� host �̸��� �����´�. http://project.iblug.com:80 �� ���� ���·� return
	 * 
	 * @return
	 */
	static String getHostName() {
		return hostname;
	}

	private void logPacket(HttpServletRequest req) throws IOException {

		java.io.InputStream ios = req.getInputStream();

		int readSize = 0;
		byte[] buf = new byte[4096];

		while (readSize > -1) {
			readSize = ios.read(buf);

			System.out.print(new String(buf, 0, readSize));
		}

	}

	private String login(String auth, HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {
		Logger.debug.println(LOG_ID + "login mode");

		String contextName = req.getContextPath();
		HttpSession session = req.getSession();
		String userId = (String) session
				.getAttribute(ProtocolParam.HTTP_HEADER_NAME_AUTH_ID
						+ contextName);
		if (userId != null && userId.length() > 0)
			return null;

		// ó��°�� Basic ����
		// �ι�°��[ ���̵�]:[��й�ȣ] �� base64 encode ��
		String[] param = SmartStringArray.split(" ", auth);

		try {

			Logger.debug.println(LOG_ID + "login BASE64 value=" + param[1]);

			//byte[] result = (new sun.misc.BASE64Decoder()).decodeBuffer(param[1]);
			
			String resolveStr = new String(DatatypeConverter.parseBase64Binary(param[1]) );

			Logger.debug.println(LOG_ID + "login BASE64 resolve value="
					+ resolveStr);

			// ù��°�� id
			// �ι�°�� pwd
			String[] param2 = SmartStringArray.split(":", resolveStr);

			String userid = param2[0];
			String pwd = param2[1];

			// ���� ��� �켱 return
			// ����� ������ �Ȱ��

			UserManager manager = UserManagerFactory.getUserManager();
			try {
				User user = manager.login(userid, pwd);

				// �α��� ����
				StringBuffer buf = new StringBuffer();
				buf.append(userid).append("|");
				buf.append(getRemoteAddr(req)).append("|");
				buf.append(System.currentTimeMillis()).append("|");
				buf.append(user.getRoles()).append("|");
				buf.append(contextName);

				Logger.debug.println(LOG_ID + "auth key value="
						+ buf.toString());

				String encVal = SimpleEncrypt.encrypt(buf.toString());
				// String encVal = (new
				// sun.misc.BASE64Encoder()).encode(buf.toString().getBytes());

				// cookie �� auth key ����
				Cookie c = new Cookie(ProtocolParam.HTTP_HEADER_NAME_AUTH_KEY,
						encVal);
				c.setMaxAge(-1);
				res.addCookie(c);

				// HTTP Header �� auth key ����
				res.setHeader(ProtocolParam.HTTP_HEADER_NAME_AUTH_KEY, encVal);

				// session ���δ� user id ����
				session.setAttribute(ProtocolParam.HTTP_HEADER_NAME_AUTH_ID
						+ contextName, userid);

				// �������� ����
				session.setAttribute(ProtocolParam.HTTP_HEADER_NAME_AUTH_ROLES
						+ contextName, user.getRoles());

				return encVal;

			} catch (Throwable ee) {
				ee.printStackTrace();
				// �α��� ����
				Logger.warn.println(LOG_ID + "login fail. " + ee.getMessage());
				// ee.printStackTrace();
			}

		} catch (java.lang.Exception ioe) {
			ioe.printStackTrace();
		}

		// �Ϲ� �������� ��츸 realm ������ ������.
		if (!ProtocolParam.HTTP_HEADER_USER_AGENT.equals(req
				.getHeader("User-Agent"))) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			res.setHeader("WWW-Authenticate", "Basic realm=\"anyFRAME Realm\"");
		}

		// ���Ѿ���.
		throw new ServletException("SC_UNAUTHORIZED");

	}

	/**
	 * 
	 * 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String auth = req.getHeader("Authorization");
		if (auth == null) {
			auth = req.getParameter("Authorization");
		}

		// Logger.debug.println(LOG_ID + "Authorization=" + auth);
		// �α��ν� ó��
		String authKey = null;
		try {

			if (auth != null && auth.length() > 0) {
				authKey = login(auth, req, res);

			}
		} catch (ServletException se) {
			se.printStackTrace();
			return;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		try {

			jdf.framework.core.Config conf = jdf.framework.core.Configuration
					.lookup("/resource/anylogic");

			if ("true".equals(conf.getString("packetCapture")))
				logPacket(req);

		} catch (ConfigurationException thre) {

		}

		String pathInfo = null;
		try {
			String reqEncode = req.getCharacterEncoding();

			if (reqEncode == null)
				req.setCharacterEncoding(Configuration.getTargetEncoding());
			else
				req.setCharacterEncoding(reqEncode);

			req.setAttribute("bypass", "true"); // anytemplet���� bypass�ϱ� ����

			pathInfo = req.getPathInfo();
			Logger.debug.println(LOG_ID + "pathInfo=" + pathInfo);
			if (pathInfo == null || pathInfo.length() < 2) {

				viewInfo(req, res, authKey);

				return;

			}
		} catch (Throwable te) {
			te.printStackTrace();
		}

		String page = pathInfo.substring(1);

		// servlet alias ���� ��´�.
		if (servletAliasName == null) {
			try {
				String reqUri = req.getRequestURI();
				servletAliasName = reqUri
						.substring(0, reqUri.indexOf(page) - 1);
				Logger.info.println(LOG_ID + "servlet Alias : "
						+ servletAliasName);
			} catch (IndexOutOfBoundsException ee) {

			}

			// WebContentFactory �ʱ�ȭ
			Logger.info.println(LOG_ID + " �ʱ�ȭ");

		}

		int idx = page.indexOf("/");

		if (idx > 0) {
			page = page.substring(0, idx);
		}

		AnyLogicControl svlt = (AnyLogicControl) modules.get(page);
		if (svlt == null) {
			throw new ServletException("command [" + page + "] is not exist.");
		} else
			Logger.debug.println(LOG_ID + "CALL:" + page);

		// �ֿ����� �������� �ٷ� �������
		/*
		 * if ("webup".equals(page) || "movefile".equals(page) ||
		 * "deletefile".equals(page) || "webdown".equals(page) ||
		 * "weblist".equals(page) || "webexist".equals(page)) { // User-Agent
		 * String agent = req.getHeader("User-Agent");
		 * 
		 * Logger.debug.println(LOG_ID + " user-agent:" + agent);
		 * 
		 * if (agent != null && agent.toLowerCase().indexOf("mozilla") >= 0) {
		 * res.sendError(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
		 * throw new ServletException("���Ѿ���"); } else svlt.doGet(req, res); }
		 * else
		 */

		try {
			svlt.doGet(req, res);
		} catch (ServletException se) {
			throw se;
		} catch (IOException ioe) {
			throw ioe;
		} catch (Throwable te) {
			te.printStackTrace();
		}

	}

	/**
	 * 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

	/**
	 * 
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

	/**
	 * ���� ���� üũ ���
	 * 
	 * 
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 */
	public static void checkAcl(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		return;
	}

	/**
	 * framework ���������� �����ش�.
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewInfo(HttpServletRequest req, HttpServletResponse res,
			String authKey) throws ServletException, IOException {

		User user = null;

		// �α����� �Ǵ� ��츸 �����ش�.
		try {
			user = AnyLogicControlServlet.getAccessUser(req, res);
		} catch (ServletException se) {
			// se.printStackTrace();
			Logger.warn.println(LOG_ID + "auth fail. " + se.getMessage());
			return;
		}

		// Jeus 4.X ���� ��������� ���õǸ� XML �� ����� ��¾ȵȴ�.
		// jeus �ʹ� �ȴ�.
		res.reset();
		// res.setHeader(ProtocolParam.HTTP_HEADER_NAME_AUTH_KEY, authKey);

		res.setContentType("text/xml; charset="
				+ Configuration.getTargetEncoding());

		java.io.PrintWriter writer = res.getWriter();

		writer.println("<?xml version=\"1.0\" encoding=\""
				+ Configuration.getTargetEncoding() + "\"?>");

		writer.println("<information>");
		writer.println("<name>anyFRAME</name>");
		writer.println("<version>" + Configuration.getMajorVersion() + "."
				+ Configuration.getMinorVersion() + "</version>");

		writer.println("<license>" + Configuration.getLicenseInfo()
				+ "</license>");
		writer.println("<edition>enterprise edition</edition>");

		// java �⺻����
		writer.print("<java-version>");
		writer.print(System.getProperty("java.vm.version"));
		writer.print("</java-version>");
		writer.print("<java-vendor>");
		writer.print(System.getProperty("java.vendor"));
		writer.print("</java-vendor>");
		writer.print("<java-file-encoding>");
		writer.print(System.getProperty("file.encoding"));
		writer.print("</java-file-encoding>");

		writer.print("<load-datetime>");
		writer.print(DateTime.getString(new Date(Configuration
				.getLoadDateTime()), "yyyy-MM-dd HH:mm:ss"));
		writer.print("</load-datetime>");

		// if ("true".equals(req.getParameter("check"))) {

		writer.println("<document-root>");
		writer.println(this.ctx.getRealPath("/"));
		writer.println("</document-root>");

		writer.print("<anyframe-class-loader>");
		writer.print(this.getClass().getClassLoader().getClass().getName());
		writer.println("</anyframe-class-loader>");

		/*
		 * writer.print("<anytemplet-class-loader>"); try { String nm =
		 * Class.forName("anytemplet.layout.filter.UIFilter").getClassLoader().getClass().getName();
		 * writer.print(nm); } catch (Throwable e) { writer.print(e.toString()); }
		 * writer.print("</anytemplet-class-loader>");
		 */

		writer.println("<class-location>");

		URL r = this.getClass().getResource("/anyframe/Configuration.class");
		String filepath;
		if (r == null)
			filepath = "not found";
		else
			filepath = r.getFile();

		filepath = StringFormater.replaceStr(filepath, "%20", " ");
		filepath = StringFormater.replaceStr(filepath,
				"!/anyframe/Configuration.class", "");
		writer.println("<anyframe>" + filepath + "</anyframe>");

		r = this.getClass().getResource(
				"/anytemplet/layout/filter/UIFilter.class");
		if (r == null)
			filepath = "not found or anyframe.jar �� classloader�� �ٸ�. classpath Ȯ�ο�";
		else {
			filepath = r.getFile();
			filepath = StringFormater.replaceStr(filepath, "%20", " ");
			filepath = StringFormater.replaceStr(filepath,
					"!/anytemplet/layout/filter/UIFilter.class", "");
		}
		writer.println("<anytemplet>" + filepath + "</anytemplet>");

		String anylogicCP = "";
		try {
			anylogicCP = sc.getClassPath();

		} catch (Throwable e) {
			anylogicCP = e.toString();
		}

		writer.print("<anylogic-classpath>" + anylogicCP
				+ "</anylogic-classpath>");

		writer.print("<java-classpath>"
				+ System.getProperties().getProperty("java.class.path")
				+ "</java-classpath>");

		writer.println("</class-location>");

		writer.print("<config-information>");

		File confFile = new File(Configuration.getConfigFilePath());

		writer.print("<core-config-file>");
		writer.print("<path>" + Configuration.getConfigFilePath() + "</path>");
		writer.print("<canRead>" + confFile.canRead() + "</canRead>");
		writer.print("<canWrite>" + confFile.canWrite() + "</canWrite>");
		writer.print("</core-config-file>");

		confFile = new File(Configuration.getConfigPath() + "/sitemap.xml");

		writer.print("<sitemap-config-file>");
		writer.print("<path>" + confFile.toString() + "</path>");
		writer.print("<exists>" + confFile.exists() + "</exists>");
		writer.print("<canRead>" + confFile.canRead() + "</canRead>");
		writer.print("<canWrite>" + confFile.canWrite() + "</canWrite>");
		writer.print("</sitemap-config-file>");

		// layout
		confFile = new File(Configuration.getConfigPath() + "/layout.xml");

		writer.print("<layout-config-file>");
		writer.print("<path>" + confFile.toString() + "</path>");
		writer.print("<exists>" + confFile.exists() + "</exists>");
		writer.print("<canRead>" + confFile.canRead() + "</canRead>");
		writer.print("<canWrite>" + confFile.canWrite() + "</canWrite>");
		writer.print("</layout-config-file>");

		// admin-console-acl.xml
		confFile = new File(Configuration.getConfigPath()
				+ "/admin-console-acl.xml");

		writer.print("<admin-acl-config-file>");
		writer.print("<path>" + confFile.toString() + "</path>");
		writer.print("<exists>" + confFile.exists() + "</exists>");
		writer.print("<canRead>" + confFile.canRead() + "</canRead>");
		writer.print("<canWrite>" + confFile.canWrite() + "</canWrite>");
		writer.print("</admin-acl-config-file>");

		// server.xml
		confFile = new File(Configuration.getConfigPath() + "/server.xml");

		writer.print("<server-list-file>");
		writer.print("<path>" + confFile.toString() + "</path>");
		writer.print("<exists>" + confFile.exists() + "</exists>");
		writer.print("<canRead>" + confFile.canRead() + "</canRead>");
		writer.print("<canWrite>" + confFile.canWrite() + "</canWrite>");
		writer.print("</server-list-file>");

		// log
		writer.print("<log-info>");

		// sys
		writer.print("<system-log>");
		writer.print("<name>Logger.sys</name>");
		writer.print("<directory>" + Logger.sys.getDirectoryName()
				+ "</directory>");
		writer.print("<enable>" + Logger.sys.isPrintMode() + "</enable>");
		writer.print("</system-log>");

		// info
		writer.print("<information-log>");
		writer.print("<name>Logger.info</name>");
		writer.print("<directory>" + Logger.info.getDirectoryName()
				+ "</directory>");
		writer.print("<enable>" + Logger.info.isPrintMode() + "</enable>");
		writer.print("</information-log>");

		// err
		writer.print("<error-log>");
		writer.print("<name>Logger.err</name>");
		writer.print("<directory>" + Logger.err.getDirectoryName()
				+ "</directory>");
		writer.print("<enable>" + Logger.err.isPrintMode() + "</enable>");
		writer.print("</error-log>");

		// warn
		writer.print("<warning-log>");
		writer.print("<name>Logger.warn</name>");
		writer.print("<directory>" + Logger.warn.getDirectoryName()
				+ "</directory>");
		writer.print("<enable>" + Logger.warn.isPrintMode() + "</enable>");
		writer.print("</warning-log>");

		// debug
		writer.print("<debug-log>");
		writer.print("<name>Logger.debug</name>");
		writer.print("<directory>" + Logger.debug.getDirectoryName()
				+ "</directory>");
		writer.print("<enable>" + Logger.debug.isPrintMode() + "</enable>");
		writer.print("</debug-log>");

		// sql
		writer.print("<sql-log>");
		writer.print("<name>Logger.sql</name>");
		writer.print("<directory>" + Logger.sql.getDirectoryName()
				+ "</directory>");
		writer.print("<enable>" + Logger.sql.isPrintMode() + "</enable>");
		writer.print("</sql-log>");

		// sql
		writer.print("<packet-log>");
		writer.print("<name>Logger.packet</name>");
		writer.print("<directory>" + Logger.packet.getDirectoryName()
				+ "</directory>");
		writer.print("<enable>" + Logger.packet.isPrintMode() + "</enable>");
		writer.print("</packet-log>");

		// user
		writer.print("<user-log>");
		writer.print("<name>Logger.user</name>");
		writer.print("<directory>" + Logger.user.getDirectoryName()
				+ "</directory>");
		writer.print("<enable>" + Logger.user.isPrintMode() + "</enable>");
		writer.print("</user-log>");
		writer.print("</log-info>");

		writer.println("</config-information>");

		writer.print("<total-memory>");
		writer.print(Runtime.getRuntime().totalMemory());
		writer.print("</total-memory>");

		writer.print("<free-memory>");
		writer.print(Runtime.getRuntime().freeMemory());
		writer.print("</free-memory>");
		// }

		writer.print("<user>");
		// userid
		writer.print("<name>");
		writer.print(user.getId());
		writer.print("</name>");

		writer.print("<roles>");
		writer.print(user.getRoles());
		writer.print("</roles>");

		String authTempKey = getAuthTempKey(req, user);

		writer.print("<auth-temp-key>");
		writer.print(jdf.framework.core.util.HtmlFormat.translate(authTempKey));
		writer.print("</auth-temp-key>");

		writer.print("<auth-key>");
		writer.print(jdf.framework.core.util.HtmlFormat.translate(authKey));
		writer.print("</auth-key>");

		writer.print("</user>");

		writer.print("<anylogic>");
		writer.print("<adapter-list>");

		RADeployDescriptor[] rds = DeploymentManager.getInstance()
				.getRADescriptors();
		for (int i = 0; i < rds.length; i++) {
			writer.print("<adapter name=\"");
			writer.print(rds[i].getName());
			writer.print("\" class=\"");
			writer.print(rds[i].getResourceAdapterClassName());
			writer.print("\"/>");
		}

		writer.print("</adapter-list>");

		writer.print("<datasource-list>");

		String names = "";
		try {
			Config conf = Configuration
					.lookup("/resource/anylogic/dbConnectionList");
			names = conf.getString("names");
		} catch (ConfigurationException ce) {

		}

		String[] nameArray = SmartStringArray.split(",", names);
		for (int n = 0; n < nameArray.length; n++) {
			writer.print("<datasource name=\"");
			writer.print(nameArray[n]);
			writer.print("\"/>");

		}

		writer.print("</datasource-list>");

		writer.print("</anylogic>");

		writer.println("</information>");

		writer.flush();

		Logger.debug.println(LOG_ID + "print anyFRAME info");
	}

	public static String getToken(HttpServletRequest req) {

		/*
		 * String remoteIp = getRemoteAddr(req); String token =
		 * getDigestMessage(remoteIp); if (token.length() > 5) token =
		 * token.substring(1, 4) + "-";
		 * 
		 * return token;
		 */

		return ",";
	}

	/**
	 * �ӽ� ���� ����Ű �� /anylogic/process �� ���� bld ����� �� ����Ű�� �־�� ����ȴ�.
	 * 
	 * @param req
	 * @return
	 */
	public static String getAuthTempKey(HttpServletRequest req, User user) {
		String serverIp = "localhost";
		try {
			serverIp = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}

		String remoteIp = getRemoteAddr(req);
		String token = getToken(req);

		if (user != null) {
			String encryptUserid = SimpleEncrypt
					.encrypt(user.getId());

			// ��ȣȭ ������ �ٽ�
			String result = getDigestMessage(serverIp + remoteIp) + token
					+ encryptUserid + token + getDigestMessage(encryptUserid)
					+ token + getDigestMessage(req.getContextPath());

			return result;
		} else
			return getDigestMessage(serverIp + remoteIp);

	}

	private static String getRemoteAddr(HttpServletRequest req) {
		String remoteIp = req.getRemoteAddr();
		// IPv6 , Vista �� ��� �̷��� ��µ�.
		if ("0:0:0:0:0:0:0:1".equals(remoteIp))
			remoteIp = "127.0.0.1";

		return remoteIp;
	}

	/**
	 * ���� Ű ������ �ùٸ��� �����Ǿ���� Ȯ��.
	 * 
	 * @param req
	 * @return
	 */
	public static boolean isValidAuthTempKey(HttpServletRequest req) {
		String authId = (String) req.getSession().getAttribute(
				ProtocolParam.HTTP_HEADER_NAME_AUTH_ID);
		if (authId != null) {
			Logger.warn.println(LOG_ID + "auth temp key invalid case:0");
			return true;
		}

		String val = req
				.getParameter(ProtocolParam.URL_PARAMETER_NAME_AUTH_KEY);
		if (val == null || val.trim().length() == 0) {
			Logger.warn.println(LOG_ID + "auth temp key invalid case:1");
			return false;
		}

		String remoteIp = getRemoteAddr(req);
		String token = getToken(req);
		String[] items = jdf.framework.core.util.SmartStringArray.split(token, val);
		if (items.length != 4) {
			Logger.warn.println(LOG_ID + "auth temp key invalid case:2");
			return false;
		}

		String serverIp = "localhost";
		try {
			serverIp = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}

		// remote ip �� ��쿡 ��� Ʋ������ ������������... �������� ����
		/*
		 * if (!items[0].equals(getDigestMessage(serverIp + remoteIp))) {
		 * Logger.warn.println(LOG_ID+"auth temp key invalid case:3 "+remoteIp);
		 * return false; }
		 */

		if (!items[2].equals(getDigestMessage(items[1]))) {
			Logger.warn.println(LOG_ID + "auth temp key invalid case:4");
			return false;
		}

		if (!items[3].equals(getDigestMessage(req.getContextPath()))) {
			Logger.warn.println(LOG_ID + "auth temp key invalid case:5");
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param val
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static String getDigestMessage(String val) {
		byte[] getpassword = val.getBytes();

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(getpassword);

			val = toHex(md.digest());
		} catch (NoSuchAlgorithmException nse) {

		}

		return val;
	}

	/**
	 * 
	 * @param digest
	 * @return
	 */
	private static String toHex(byte[] digest) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			buf.append(Integer.toHexString((int) digest[i] & 0x00FF));
		}
		return buf.toString();
	}
}