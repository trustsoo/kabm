package jdf.framework.core.data.schema;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;

import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringFormater;



/**
 * <b><code>ScriptContext</code> </b>
 * <p>
 * IO Schema에서 process 부분의 처리를 보완해주는 script를 정의 정보 script은 java 화일로 변환되고, 컴파일 된후 실행된다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class ScriptContext implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String LOG_ID = "<f:ScriptContext> ";

	// 기본 패키지명
	private final static String PACKAGE_PREFIX = "ioprocess";

	// script의 아이디, 보통 io 스키마의 full id를 이용한다.
	private String id;

	// script 언어종류
	private String language;

	// import list
	private String importList;

	// 실제 script
	private String script;

	// 선언부
	private String declaration;

	// io schema의 수정시간, compile 된 class의 시간과 비교한다.
	private long checkTime;

	// package 명이 제외된 class명
	private String className;

	// package 명이 포함된 class명
	private String fullClassName;

	// 컴파일하기 위해 필요한 classpath
	private String classpath;

	// java source 위치
	private static String sourceDir;

	private String extendClass = "jdf.framework.logic.adapter.dbms.DbmsProcessor";

	static {

		// 우선 config.xml이 있는 위치 기준으로 ioschema_gen 디렉토리로 설정
		sourceDir = Configuration.getConfigPath() + "/ioschema_gen";

		try {
			// config.xml에 getDir이 설정되어 있으면 그 값을 이용
			String tmp = Configuration.lookup("/resource/anylogic/ioSchema").getString("genDir");
			if (tmp != null && tmp.length() > 0)
				sourceDir = tmp;
		} catch (Exception e) {
		}

		Logger.info.println(LOG_ID + "generate target dir:" + sourceDir);

		try {
			File f = new File(sourceDir);
			f.mkdirs();
		} catch (Exception e) {
		}

	}

	public ScriptContext(String id) {
		this.id = id;

		String className = StringFormater.replaceStr(id, "/", "_");
		className = StringFormater.replaceStr(className, "-", "_");

		this.className = "C" + className;
		// tr명이 숫자인 경우 문제가 생기므로 Class를 의미하는 C를 붙인다.
		this.fullClassName = PACKAGE_PREFIX + "." + this.className;
	}

	/**
	 * 언어종류와 실제 script 내용을 설정
	 * 
	 * @param language
	 * @param content
	 */
	public void setScript(String language, String content)
	{
		this.language = language;
		this.script = content;
	}

	/**
	 * BLD 내의 script 코드중 선언부에 해당되는 코드를 기술한다.
	 * 
	 * @param declaration
	 */
	public void setDeclaration(String declaration)
	{
		this.declaration = declaration;
	}

	/**
	 * BLD 내의 script 코드중 선언부에 해당되는 코드를 반한다.
	 * 
	 * @return
	 */
	public String getDeclaration()
	{
		return this.declaration;
	}

	/**
	 * script가 이용하는 import 화일리스트 정보 설정
	 * 
	 * @param importList
	 */
	public void setImportList(String importList)
	{
		this.importList = importList;
	}

	/**
	 * io schema의 수정시간 설정
	 * 
	 * @param time
	 */
	public void setCheckTime(long time)
	{
		this.checkTime = time;
	}

	/**
	 * script id 값을 가져온다. 보통 ioschema의 id와 동일
	 * 
	 * @return
	 */
	public String getId()
	{
		return this.id;
	}

	/**
	 * 실제 script 내용을 가져온다.
	 * 
	 * @return
	 */
	public String getScript()
	{
		return this.script;
	}

	/**
	 * script의 language type 정보를 가져온다.
	 * 
	 * @return
	 */
	public String getLanguage()
	{
		return this.language;
	}

	/**
	 * import list 정보를 가져온다.
	 * 
	 * @return
	 */
	public String getImportList()
	{
		return this.importList;
	}

	public String[] getImportListArray()
	{
		return SmartStringArray.split(",", this.importList);
	}

	/**
	 * 수정시간정보를 가져온다.
	 * 
	 * @return
	 */
	public long getCheckTime()
	{
		return this.checkTime;
	}

	/**
	 * package 명이 제외된 class 명을 반환한다.
	 * 
	 * @return
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * package 명이 포함된 class명을 반환한다.
	 * 
	 * @return
	 */
	public String getFullClassName()
	{
		return this.fullClassName;
	}

	/**
	 * package 명을 반환한다.
	 * 
	 * @return
	 */
	public String getPrefixPackageName()
	{
		return PACKAGE_PREFIX;
	}

	/**
	 * java로 생성된 source 화일의 위치정보를 반환한다.
	 * 
	 * @return
	 */
	public String getSourceDir()
	{
		return sourceDir;
	}

	/**
	 * class 화일로 컴파일되는 위치정보를 반환한다.
	 * 
	 * @return
	 */
	public String getClassDestdir()
	{
		return sourceDir;
	}

	/**
	 * 컴파일하기 위한 classpath 정보를 반환한다. framework classpath 정보만을 반환한다.
	 * 
	 * @return
	 */
	public String getClassPath()
	{

		if (classpath == null) {

			String delim = ":";

			try {
				// window 계열이면 classpath에서의 각 library 구분자는
				//if ("x86".equals(System.getProperties().getProperty("os.arch"))) {
					if (System.getProperties().getProperty("os.name").toLowerCase().indexOf("window") >= 0)
						delim = ";";

				//}
			} catch (Exception ee) {

			}

			try {
				// 우선 config.xml에 classpath 정보를 읽는다.
				classpath = Configuration.lookup("/resource/anylogic/ioSchema").getString("classpath");
			} catch (ConfigurationException e) {
			}

			// classpath 정보가 없으면
			if (classpath == null || classpath.length() == 0) {
				// WEB-INF 와 같은 library 가 있는 디렉토리 정보를 읽는다.
				String lib_dir = ".";

				try {
					lib_dir = Configuration.lookup("/resource/anylogic/ioSchema").getString("lib_dir");
				} catch (ConfigurationException cfe) {
				}

				// 그 정보도 없으면 config.xml 디렉토리 기준으로 lib 디렉토리를 찾는다.
				if (lib_dir == null || lib_dir.length() == 0) {
					String dir = Configuration.getConfigPath();

					File f = new File(dir);
					f = new File(f.getParentFile().toString() + "/lib");
					lib_dir = f.toString();

				}

				// , 로 구분되어 여러개가 올수있다.
				String[] lib_dirs = jdf.framework.core.util.SmartStringArray.split(",", lib_dir);
				StringBuffer classPathBuf = new StringBuffer();

				for (int k = 0; lib_dirs != null && k < lib_dirs.length; k++) {

					String each_lib_dir = lib_dirs[k];
					
					// 상대경로이면 config.xml 으로 상대경로 탐색
					if(each_lib_dir.indexOf(".")==0) {
						each_lib_dir = Configuration.getConfigPath()+each_lib_dir;
					}

					File lib_dir_f = new File(each_lib_dir);

					File[] jarFiles = getDescriptorFiles(lib_dir_f);

					// 자식 .jar 없다면 일반 class 가 컴파일된 디렉토리로 간주
					if (jarFiles.length == 0) {
						try {
							if (classPathBuf.length() > 0)
								classPathBuf.append(delim);
							classPathBuf.append(lib_dir_f.getCanonicalPath());
						} catch (IOException ioe) {
							Logger.warn.println(LOG_ID+"get canonical path error "+each_lib_dir);

						}

					}

					for (int i = 0; i < jarFiles.length; i++) {
						if (classPathBuf.length() > 0)
							classPathBuf.append(delim);
						try {
							classPathBuf.append(jarFiles[i].getCanonicalPath());
						} catch (IOException ioe) {
							Logger.warn.println(LOG_ID+"get canonical path error "+jarFiles[i].getAbsolutePath());

						}
					}
				}

				String dir = Configuration.getConfigPath();

				File f = new File(dir);
				f = new File(f.getParentFile().toString() + "/classes");
				String classesPath = f.toString();

				classPathBuf.append(delim).append(classesPath);

				classpath = classPathBuf.toString();
			}

			// java.class.path 에 정의된 classpath

			if (classpath != null && classpath.length() > 0)
				classpath = System.getProperty("java.class.path") + delim + classpath;
			else
				classpath = System.getProperty("java.class.path");

			Logger.info.println(LOG_ID + "classpath:" + classpath);
		}

		return classpath;
	}

	

	private static boolean isWindowsOs = System.getProperty("os.name").indexOf("Window") >= 0 ? true : false;

	private String getClassPath(String resourceNm, URL jarFileUrl)
	{
		/*
		 * weblogic인 경우
		 * 
		 * 1
		 * zip:D:/bea/user_projects/new_anylogic/./myserver/.wlnotdelete/DefaultWebApp_DefaultWebApp_6863191/jarfiles/WEB-INF/lib/anyframe34371.jar!/anyframe
		 * 2
		 * D:/bea/user_projects/new_anylogic/./myserver/.wlnotdelete/DefaultWebApp_DefaultWebApp_6863191/jarfiles/WEB-INF/lib/anyframe34371.jar!/anyframe
		 * 1
		 * zip:D:/bea/user_projects/new_anylogic/./myserver/.wlnotdelete/DefaultWebApp_DefaultWebApp_6863191/jarfiles/WEB-INF/lib/anyframe34371.jar!/anylogic
		 * 2
		 * D:/bea/user_projects/new_anylogic/./myserver/.wlnotdelete/DefaultWebApp_DefaultWebApp_6863191/jarfiles/WEB-INF/lib/anyframe34371.jar!/anylogic
		 * 
		 * 
		 * 
		 * tomcat 4.x 인 경우 1 jar:file:/D:/PROJECT/_SI/kbs.co.kr/prototype/htdocs/WEB-INF/lib/anyframe.jar!/anyframe 2
		 * file:/D:/PROJECT/_SI/kbs.co.kr/prototype/htdocs/WEB-INF/lib/anyframe.jar!/anyframe
		 * 
		 * 1 jar:file:/D:/PROJECT/_SI/kbs.co.kr/prototype/htdocs/WEB-INF/lib/anyframe.jar!/anylogic 2
		 * file:/D:/PROJECT/_SI/kbs.co.kr/prototype/htdocs/WEB-INF/lib/anyframe.jar!/anylogic
		 * 
		 * 1 file:/D:/PROJECT/_SI/kbs.co.kr/prototype/htdocs/WEB-INF/classes/fss/ 2
		 * /D:/PROJECT/_SI/kbs.co.kr/prototype/htdocs/WEB-INF/classes/fss/
		 * 
		 * 
		 * jeus /HP 인 경우
		 * jar:file://localhost/usr/jeusdev/jeus40/webhome/servlet_home/webapps/WEB-INF/lib/anyframe.jar!/anyframe
		 * 
		 * 
		 * tomcat 5.X (window) 인 경우 [CLS_URL1] file:/D:/Apache/Tomcat 5.0/work/Catalina/localhost/ir/loader/anyframe/
		 * [CLS_URL2] /D:/Apache/Tomcat 5.0/work/Catalina/localhost/ir/loader/anyframe/ [CLS_URL] D:/Apache/Tomcat
		 * 5.0/work/Catalina/localhost/ir/loader/ [CLS_URL1] file:/D:/Apache/Tomcat
		 * 5.0/work/Catalina/localhost/ir/loader/anylogi
		 * 
		 * 
		 * 
		 */

		Logger.debug.println(LOG_ID + "[CLS_URL1] " + jarFileUrl.toString());
		Logger.debug.println(LOG_ID + "[CLS_URL2] " + jarFileUrl.getFile());

		// String classpath = jarFileUrl.getFile();
		String classpath = jarFileUrl.toString();

		String result = ".";

		if (classpath.indexOf("file:") == 0) {
			result = classpath.substring(5, classpath.indexOf(resourceNm));
			// return classpath.substring(6, classpath.indexOf("!"));
		} else if (classpath.indexOf("zip:") == 0) {
			result = classpath.substring(4, classpath.indexOf("!"));
		} else if (classpath.indexOf("jar:file:") == 0) {
			result = classpath.substring(9, classpath.indexOf("!"));
		} else if (classpath.indexOf("jar:") == 0) {
			result = classpath.substring(4, classpath.indexOf("!"));
		}

		if (isWindowsOs && result.indexOf("/") == 0)
			result = result.substring(1);

		if (result.indexOf("//localhost") == 0)
			result = result.substring(11);

		Logger.debug.println(LOG_ID + "[CLS_URL] " + result);

		return result;

	}

	/**
	 * @return
	 */
	public String getExtendClass()
	{
		return extendClass;
	}

	/**
	 * @param string
	 */
	public void setExtendClass(String str)
	{
		if (str != null && str.length() > 0)
			extendClass = str;
	}

	/**
	 * 
	 * Java Library 리스트를 가져온다.
	 * 
	 * @param dir
	 * @return
	 */
	private static File[] getDescriptorFiles(File dir)
	{
		if ((!dir.exists()) || (!dir.isDirectory())) {
			return new File[0];
		}

		File[] contents = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name)
			{

				name = name.toLowerCase();
				if (name.endsWith(".jar") || name.endsWith(".zip"))
					return true;
				else
					return false;
			}
		});

		return contents;
	}

}