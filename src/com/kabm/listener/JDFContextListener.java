package com.kabm.listener;


import com.kabm.util.SitePropertyManager;
import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.db.ConnectionManager;
import jdf.framework.core.log.AsyncLogWriter;
import jdf.framework.core.log.Logger;
import jdf.framework.core.log.LoggerFactory;
import jdf.framework.core.pool.ThreadPool;
import jdf.framework.core.schedule.ScheduleRegister;
import jdf.framework.core.service.ServiceManager;
import jdf.framework.core.util.Utility;
import jdf.framework.logic.spi.management.DeploymentManager;
import jdf.framework.view.menu.WebPagePublisher;
import jdf.framework.view.menu.bean.MenuMgrBean;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.sql.SQLException;


public class JDFContextListener implements javax.servlet.ServletContextListener
{
	private static final String LOG_ID = "<JDFContextListener> ";
	
	// document root directory
	private static String documentRoot = null;
	
	private static boolean isConfigComplete=false;
	
	private static ServiceManager serviceMgr = null;
	private static ScheduleRegister scheduler = null;
	
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
				Logger.err.println("**********************Configuration init faile");	
			}
		}
		return documentRoot;
	}

	/**
	 * context 초기화시 실행
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {

		log("\n\n");
		log("===============================================================================");

		ServletContext ctx = sce.getServletContext();
		
		log(LOG_ID + "coreframe config init");
		setFrameworkConfigPath(ctx);
		log(LOG_ID + "config complete");

		//documentRoot = ctx.getRealPath("/");
		documentRoot = getDocumentRoot();

		

		log(LOG_ID + "database connection init");
		// DB 커넥션 설정

		try {
			startDBPool();
		} catch (SQLException sqle) {
			log(LOG_ID+sqle.toString());
		} catch (Throwable te) {
			log(LOG_ID+te.toString());
		}
		log(LOG_ID + "database connection complete");

		// BLD 로드

		DeploymentManager.getInstance().load();
		
		
		SitePropertyManager.reflesh();
		
		
		Config conf = null;
		try
		{	conf = Configuration.lookup("/scheduler");
			
			boolean isSchedule = conf.getBoolean("isEnable", false);
		
			if(isSchedule)
			{
	    		String configfile = conf.getString("file", "schedule.xml");
	    		
	    		scheduler = ScheduleRegister.getInstance();
				scheduler.setConfigFile(configfile);
				scheduler.initialize();
			}
			
		
		} catch(Exception ex)
		{
			log(Utility.getStackTrace(ex));
		} 
		
		
		log("===============================================================================");

	}
	
	
	private void startDBPool() throws ConfigurationException, SQLException,Exception
	{
		Logger.info.println("**********************<JDBC Pool> Pool create DB Pool");		
		try {
			
			Config conf= Configuration.lookup("/db");

            String defaultUrl= conf.getString("dataSourceName", "");

            if (defaultUrl != null && defaultUrl.length() > 0)
            {
            	Logger.info.println("**********************<JDBC Pool> USE JNDI");	
            }else{
				String root = "/connectionPool";
				Config config = Configuration.getInitial();
				
				java.util.List<String> keys = config.keys();
				
				for(int i = 0; i < keys.size(); i++) 
				{
					String key = (String) keys.get(i);
					
					int l = key.lastIndexOf("maxTotal");
					
					if (key.startsWith(root) && (l > 0)) 
					{
						String poolName = key.substring(root.length() + 1, l-1);
						
						conf = Configuration.lookup("/connectionPool/"+poolName);				
						java.sql.Connection con = ConnectionManager.getConnection("jdbc:apache:commons:dbcp:"+poolName);
						con.close();
					}
					
				}
            }
		}
		catch(ConfigurationException cfe)
		{
			Logger.err.println("Create DB Pool ERROR. Mesage -> " + cfe.getMessage());
			throw cfe;
		} catch(SQLException sqlex)
		{
			Logger.err.println("Create DB Pool ERROR. Mesage -> " + sqlex.getMessage());
			throw sqlex;
		}
		catch(Exception e) {
			Logger.err.println("Create DB Pool ERROR. Mesage -> " + e.getMessage());
			throw e;
		}
	}

	/**
	 * context 종료시 실행
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		String nm = sce.getServletContext().getServletContextName();
		log(LOG_ID + "STOP jdfFRAME [context:" + nm + "] start");

		try {

			// anytemplet publisher 종료
			WebPagePublisher publisher = MenuMgrBean.getWebPagePublisher();

			if (publisher != null)
				publisher.destroy();
			log(LOG_ID + "STOP coreFrame [WebPagePublisher]");

			AsyncLogWriter.stopService();
			LoggerFactory.close();
			log(LOG_ID + "STOP coreFrame [LogService]");

			// DB Pooling 기능종료
			//CacheDriver.stopExecute();
			log(LOG_ID + "STOP coreFrame [CacheDriver]");

			// Thread pool 종료
			// log thread 중단
			ThreadPool.stopExecute();
			log(LOG_ID + "STOP coreFrame [ThreadPool]");

			log(LOG_ID + "STOP coreFrame [context:" + nm + "] complete");
		} catch (RuntimeException e) {
			log(LOG_ID + "STOP coreFrame [context:" + nm + "] fail");
		}catch (Throwable e) {
			log(LOG_ID + "STOP coreFrame [context:" + nm + "] fail");
		}

	}

	/**
	 * jdfFRAME config 경로 설정
	 * 
	 * @param ctx
	 */
	public static synchronized void setFrameworkConfigPath(ServletContext ctx) {
		
		if(isConfigComplete) return;
		System.out.println("[init:Find cor config] start");

		String path = System.getProperty("config.file");
		System.out.println("[init:Find coreFrame config] System Property:"
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
		System.out.println("[init:Find coreFrame config] web.xml[parameter]:"
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
			System.out.println("[init:Find coreFrame config] default:" + path);
		}

		System.out.println("[init:Find coreFrame config] PATH:" + path);

		// 하위 anyframe 버전을 위해서 아래 내용을 삭제한다.
		Configuration.setConfigFilePath(path);
		
		isConfigComplete=true;
		
		Logger.info.println("coreFrame configuration complete. Version "+Configuration.getMajorVersion()+"."+Configuration.getMinorVersion());

	}
	
	
	private void log(String log) {
		System.out.println(LOG_ID+log);
	}
}