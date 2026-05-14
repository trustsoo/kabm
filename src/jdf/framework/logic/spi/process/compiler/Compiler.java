package jdf.framework.logic.spi.process.compiler;

import jdf.framework.core.Configuration;
import jdf.framework.core.data.schema.ScriptContext;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.logic.spi.classloader.ReverseClassLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


/**
 * 
 * 
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-11-11 오후 4:04:28
 * 
 */
public class Compiler
{
	private final static String LOG_ID = "<Script Compiler> ";

	private ScriptContext ctx;

	// java source file
	private File javaSrcFile;

	private String javaSrcFileStr;

	public Compiler(ScriptContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	public void generateJava() throws Exception
	{

		String[] importList = SmartStringArray.split(",", ctx.getImportList());

		this.javaSrcFileStr = ctx.getSourceDir() + File.separator + ctx.getClassName() + ".java";
		this.javaSrcFile = new File(this.javaSrcFileStr);

		//PrintWriter writer = new PrintWriter(new FileWriter(javaSrcFile));
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(javaSrcFile), Configuration.getTargetEncoding()));

		writer.println("package " + ctx.getPrefixPackageName() + ";");

		writer.println("import java.sql.*;");
		writer.println("import jdf.framework.core.log.*;");
		writer.println("import jdf.framework.core.data.*;");
		writer.println("import jdf.framework.core.data.schema.*;");

		for (int i = 0; i < importList.length; i++) {
			writer.println("import " + importList[i] + ";");
		}

		writer.println("final public class " + ctx.getClassName() + " extends " + ctx.getExtendClass());
		writer.println("{");

		if (ctx.getDeclaration() != null)
			writer.println(ctx.getDeclaration());

		writer.println("public " + ctx.getClassName() + "() {");
		writer.println("super();");
		// writer.println(Logger.debug.println(\"[" + ctx.getFullClassName() + "] instance create. \");");
		writer.println("}");

		writer.println("public void execute(final DataSet input, final DataSet output) throws Exception {");
		writer.println(ctx.getScript());
		writer.println("}");

		writer.println("public long getCheckTime() {");
		writer.println(" return " + ctx.getCheckTime() + "L;");
		writer.println("}");

		writer.println("}");

		writer.flush();
		writer.close();

		Logger.info.println("<Compiler> source : " + ctx.getFullClassName() + ".java");
	}

	/**
	 * 
	 * @throws CompileException
	 */
	public void generateClass() throws CompileException
	{
		JavaCompiler javac = new JavaCompiler();

		javac.setClasspath(ctx.getClassPath());
		javac.setDestdir(ctx.getSourceDir());
		javac.setSourceFile(this.javaSrcFileStr);

		Logger.info.println(LOG_ID + "compile ...........................>\n" + this.javaSrcFileStr);
		if (!javac.execute()) {
			Logger.err.println(LOG_ID + "\n" + javac.getResultText());
			throw new CompileException(ctx.getClassName() + " compile error\n" + javac.getResultText());
		}

	}

	/*
	 * public void generateClass2() throws Exception { try {
	 * 
	 * Project project= new Project(); project.setBasedir(ctx.getSourceDir()); project.init();
	 * 
	 * //project.addBuildListener( new AnylogicBuildListener() );
	 * 
	 * Javac javac= (Javac) project.createTask("javac");
	 * 
	 * Path path= new Path(project); //path.setPath(System.getProperty("java.class.path"));
	 * path.setPath(ctx.getClassPath());
	 * 
	 * //System.out.println("CLASSPATH---->" + System.getProperty("java.class.path"));
	 * //System.out.println("CLASSPATH---->" + ctx.getClassPath());
	 * //System.out.println(System.getProperty("JAVA_HOME"));
	 * 
	 * File srcFile= new File(ctx.getSourceDir()); // Initializing sourcepath Path srcPath= new Path(project);
	 * srcPath.setLocation(srcFile); //srcPath.setLocation(this.javaSrcFile);
	 * 
	 * javac.setClasspath(path); javac.setOptimize(true); // javac.setDebug(true);
	 * 
	 * javac.setDestdir(srcFile); javac.setSrcdir(srcPath);
	 * 
	 * 
	 * 
	 * //System.out.println(" 1 ------------- "+javaSrcFileStr);
	 * 
	 * //javac.setSourcepath(srcPath); //javac.setSource( this.ctx.getClassName() + ".java" );
	 * 
	 * //System.out.println(" 2 ------------- "+javaSrcFileStr);
	 * 
	 * //javac.setSourcepath()
	 * 
	 * //javac.setCompiler(options.getCompiler());
	 * 
	 * Logger.info.println(LOG_ID+"execute ...............................>"+srcPath); javac.execute(); } catch
	 * (Exception e) { e.printStackTrace(); throw e; }
	 * 
	 * javaSrcFile.delete();
	 * 
	 * Logger.info.println("<Compiler> result : " + ctx.getFullClassName()); }
	 */
	public Class getCompiledClass()
	{
		try {
			ReverseClassLoader loader = new ReverseClassLoader(
					Configuration.class.getClassLoader());

			loader.setLocalClassPath();

			// loader.addClassPath(ctx.getClassPath());
			loader.addClassPath(ctx.getSourceDir());

			loader.setPackagePrefix(ctx.getPrefixPackageName()); // 포함되는 팩키지명

			Class c = loader.loadCls(ctx.getFullClassName());

			return c;

		} catch (Throwable e) {
			// e.printStackTrace();
			Logger.warn.println(LOG_ID + "getCompiledClass err " + e.toString());
			return null;
		}

	}

	/*
	 * public Class getCompiledClass44() { try {
	 * 
	 * File file= new File(ctx.getSourceDir()); File classfile= new File(ctx.getClassPath());
	 * 
	 * URL[] urls= new URL[] { file.toURL(), classfile.toURL()};
	 * 
	 * URLClassLoader loader= new URLClassLoader(urls);
	 * 
	 * return loader.loadClass(ctx.getFullClassName()); } catch (Exception e) { e.printStackTrace(); return null; } }
	 */

	public Class getCompiledClass22()
	{

		try {
			return Class.forName(ctx.getFullClassName());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
