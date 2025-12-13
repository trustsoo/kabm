package jdf.framework.core.http;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.log.AsyncLogWriter;
import jdf.framework.core.log.Logger;
import jdf.framework.core.log.LoggerFactory;
import jdf.framework.core.pool.ThreadPool;
import jdf.framework.view.menu.WebPagePublisher;
import jdf.framework.view.menu.bean.MenuMgrBean;



/**
 * 
 * 
 * @author
 * 
 */
public class JDFrameContextListener implements	javax.servlet.ServletContextListener 
{

	private static final String LOG_ID = "<JDFrame-CtxListener> ";

	// document root directory
	private static String documentRoot = null;
	
	private static boolean isConfigComplete=false;

	/**
	 * Document root의 경로를 return 한다.
	 * 
	 * @return
	 */
	public static String getDocumentRoot() {
		if (documentRoot == null) {
			try {
				Config conf = Configuration.lookup("/site");
				documentRoot = conf.getString("documentRoot");
			} catch (ConfigurationException ce) {

			}
		}
		return documentRoot;
	}

	/**
	 * context 초기화시 실행
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {

		log("\n\n");
		log("===============================================================================");

		ServletContext ctx = sce.getServletContext();

		//documentRoot = ctx.getRealPath("/");
		documentRoot = getDocumentRoot();

		log(LOG_ID + "jdf frame config init");
		setJdfFrameConfigPath(ctx);
		log(LOG_ID + "config complete");

		log(LOG_ID + "database connection init");
		// DB 커넥션 설정

		try {
			Connection conn = jdf.framework.core.db.ConnectionManager.getConnection();
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Throwable te) {
			te.printStackTrace();
		}
		log(LOG_ID + "database connection complete");

		// BLD 로드

		jdf.framework.logic.spi.management.DeploymentManager.getInstance().load();


		// anytemplet publisher 시작

		log("===============================================================================");

	}

	/**
	 * context 종료시 실행
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		String nm = sce.getServletContext().getServletContextName();
		log(LOG_ID + "STOP jdfFRAME [context:" + nm + "] start");

		try {

			// anytemplet publisher 종료
			WebPagePublisher publisher = MenuMgrBean.getWebPagePublisher();

			if (publisher != null)
				publisher.destroy();
			log(LOG_ID + "STOP jdfFRAME [WebPagePublisher]");

			AsyncLogWriter.stopService();
			LoggerFactory.close();
			log(LOG_ID + "STOP jdfFRAME [LogService]");

			// DB Pooling 기능종료
			//CacheDriver.stopExecute();
			log(LOG_ID + "STOP jdfFRAME [CacheDriver]");

			// Thread pool 종료
			// log thread 중단
			ThreadPool.stopExecute();
			log(LOG_ID + "STOP jdfFRAME [ThreadPool]");

			log(LOG_ID + "STOP jdfFRAME [context:" + nm + "] complete");
		} catch (Throwable e) {
			log(LOG_ID + "STOP jdfFRAME [context:" + nm + "] fail");
			e.printStackTrace();
		}

	}

	/**
	 * jdfFRAME config 경로 설정
	 * 
	 * @param ctx
	 */
	public static synchronized void setJdfFrameConfigPath(ServletContext ctx) {
		
		if(isConfigComplete) return;

		System.out.println("[init:Find jdf frame config] start");

		String path = System.getProperty("config.file");
		System.out.println("[init:Find jdf frame config] System Property:"
				+ path);

		// config.xml 관 같이 xml 형태의 config 파일을 사용하는 경우에만 적용
		// 한투와 같이 예전 kitc.conf 와 같은 config 설정파일이
		// -Dconfig.file=kitc.conf 로 세팅되는 경우 문제가 생기므로 xml 만 인식
		if (path != null && path.trim().length() > 0
				&& path.indexOf(".xml") > 0) {
			System.out
					.println(" * USE SYSTEM PROPERTY[config.file] configuration "
							+ path);
			return;
		}

		path = ctx.getInitParameter(Configuration.DEFAULT_CONFIG_PATH_KEY);
		System.out.println("[init:Find jdf frame config] web.xml[parameter]:"
				+ path);

		if (path == null || path.trim().length() == 0)
			path = "./config/config.xml";

		// 상대경로이면 ex)./config/config.xml
		if (path.indexOf((".")) == 0) {
			// path = cfg.getServletContext().getRealPath("/") + "WEB-INF" +
			// path.substring(1);
			String root = ctx.getRealPath("/");
			if (root.length() != root.lastIndexOf("\\") + 1
					&& root.length() != root.lastIndexOf("/") + 1)
				root = root + "/";

			path = root + "WEB-INF" + path.substring(1);
			System.out.println("[init:Find jdf frame config] default:" + path);
		}

		System.out.println("[init:Find jdf frame config] PATH:" + path);

		// 하위 framework 버전을 위해서 아래 내용을 삭제한다.
		Configuration.setConfigFilePath(path);
		
		isConfigComplete=true;
		
		Logger.info.println("jdf frame configuration complete. Version "+Configuration.getMajorVersion()+"."+Configuration.getMinorVersion());

	}

	private void log(String log) {
		System.out.println(log);
	}

}
