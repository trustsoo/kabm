/*
 * Created on 2004-07-08
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.spi.process.compiler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;



/**
 * 
 * Java 파일 Compiler
 * 
 * 
 * 
 * @author
 * 
 */
public class JavaCompiler
{
	private final static String LOG_ID = "<JavaCompiler> ";

	private List argList = new ArrayList();

	private StringBuffer result;

	private String sourceFile;

	private static String javacCmd = "javac";

	private static boolean isExternalSetting = false;

	static {

		try {
			Config conf = Configuration.lookup("/resource/anylogic/ioSchema");
			javacCmd = conf.getString("compiler");

			if (javacCmd != null && javacCmd.length() > 0)
				isExternalSetting = true;
			else
				javacCmd = "javac";
		} catch (Exception e) {
		}

	}

	/**
     * 
     */
	public JavaCompiler() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setClasspath(String classpath)
	{
		if (classpath.indexOf(" ") > 0)
			classpath = "\"" + classpath + "\"";

		this.argList.add("-classpath");
		this.argList.add(classpath);

	}

	public void setDestdir(String path)
	{
		if (path.indexOf(" ") > 0)
			path = "\"" + path + "\"";

		this.argList.add("-d");
		this.argList.add(path);
	}

	public void setSourceFile(String filepath)
	{
		if (filepath.indexOf(" ") > 0)
			filepath = "\"" + filepath + "\"";

		this.sourceFile = filepath;
	}

	private String[] getArguments()
	{
		if (sourceFile != null) {
			this.argList.add(this.sourceFile);
			this.sourceFile = null;
		}

		return (String[]) argList.toArray(new String[] {});
	}

	
	/**
	 * 
	 * @return
	 */
	public boolean execute()
	{

		this.result = new StringBuffer();
		// OutputStream os = Logger.err.getMultiOutputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			if (isExternalSetting)
				return executeCmd();

			Class c = Class.forName("sun.tools.javac.Main");

			Constructor cons = c.getConstructor(new Class[] { OutputStream.class, String.class });
			Object compiler = cons.newInstance(new Object[] { bos, "javac" });

			String[] args = getArguments();

			// Call the compile() method
			Method compile = c.getMethod("compile", new Class[] { String[].class });
			Boolean ok = (Boolean) compile.invoke(compiler, new Object[] { args });

			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < args.length; i++) {
				buf.append(" ").append(args[i]);
			}
			Logger.debug.println(LOG_ID + "sun.tools.javac.Main" + buf.toString());

			this.result.append(new String(bos.toByteArray()));

			return ok.booleanValue();
		} catch (ClassNotFoundException ce) {
			Logger.warn.println(LOG_ID + "Cannot use classic compiler. set classpath tools.jar");

			try {
				return executeCmd();
			} catch (IOException ioe) {
				return false;
			}
		} catch (IOException ioe) {

			return false;
		} catch (Exception e) {
			return false;
		}

	}

	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean executeCmd() throws IOException
	{

		StringBuffer buf = new StringBuffer();
		buf.append(javacCmd);

		String[] args = getArguments();

		for (int i = 0; i < args.length; i++) {
			buf.append(" ").append(args[i]);
		}

		String cmd = buf.toString();

		Logger.debug.println(LOG_ID + "comand:" + cmd);

		boolean isSuccess = true;
		Process p = null;
		InputStream is = null;

		try {

			p = Runtime.getRuntime().exec(cmd);

			is = p.getErrorStream();
			// is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int temp;
			this.result = new StringBuffer();
			BufferedReader br = new BufferedReader(isr);

			while ((temp = br.read()) != -1) {
				isSuccess = false;
				result = result.append((char) temp);
			}

			/*
             * 문제있음... String line=null; while( (line=br.readLine())!=null ) result.append(line).append("\n");
             */

			br.close();
			isr.close();
		} finally {
			if (p != null) {
				p.destroy();
				p = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}

		}

		return isSuccess;

	}

	public StringBuffer getResultText()
	{
		return this.result;
	}

	public static void main(String[] args) throws Exception
	{

		JavaCompiler javac = new JavaCompiler();
		javac.setClasspath("D:\\PROJECT\\_SI\\kbs.co.kr\\prototype\\htdocs\\WEB-INF\\lib\\frame.jar");
		javac.setDestdir("D:\\PROJECT\\_SI\\kbs.co.kr\\prototype\\htdocs\\WEB-INF\\config\\ioschema_gen");
		javac
				.setSourceFile("D:\\PROJECT\\_SI\\kbs.co.kr\\prototype\\htdocs\\WEB-INF\\config\\ioschema_gen\\Cxbrl_editor_list_xbrl_data.java");

		boolean is = javac.execute();

		if (is)
			System.out.println("true");
		else
			System.out.println("false");

		System.out.println(javac.getResultText());

	}

}