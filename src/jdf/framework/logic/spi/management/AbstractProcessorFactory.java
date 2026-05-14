package jdf.framework.logic.spi.management;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.schema.Processor;
import jdf.framework.core.data.schema.ScriptContext;
import jdf.framework.core.xml.XMLReferer;
import jdf.framework.logic.spi.process.compiler.Compiler;

/**
 * 
 * 
 * AbstractProcessorFactory
 * 
 * BLD에 정의되어진 processor-info 의 구현 class
 * 
 * @author
 * 
 * To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and
 * Comments
 */
public abstract class AbstractProcessorFactory implements ProcessorFactory
{

	/**
	 * 기본 생성자.
	 */
	public AbstractProcessorFactory() {
	}

	/**
	 * Processor 객체를 만든다. 이부분은 <process-info> 내에서 공통적으로 처리해야할 내용을 먼저 처리한다. 가령 <script>이라던가 <property>등...
	 * 
	 * 
	 * @see ProcessorFactory#getProcessor(IOSchemaContext)
	 */
	public Processor getProcessor(RADeployDescriptor radd, IOSchemaContext ctx) throws Exception
	{

		String name = ctx.getId();

		long lastModifiedTime = ctx.getModifyTime();
		XMLReferer xmlDoc = ctx.getXMLReferer();

		Processor process = null;
		try {
			xmlDoc.lookup("/transaction/processor-info");

			// 선언부
			String declaration = xmlDoc.find("declaration").getText();

			// 본문 script
			String script = xmlDoc.find("script").getText();

			// script 내용이 있는 경우 컴파일 한다.
			if (script != null && script.length() > 0) {

				String language = xmlDoc.find("script").getString("language");
				String importList = xmlDoc.find("script").getString("import");
				String exetendClass = xmlDoc.find("script").getString("extend");

				// Logger.debug.println("<AbstractProcessorFactory> "+ctx.getId()+" has script.");

				/*
				 * System.out.println(" lang = " + language); System.out.println(" importList = " + importList);
				 */

				ScriptContext sctx = new ScriptContext(name);
				sctx.setScript(language, script);
				sctx.setImportList(importList);
				sctx.setCheckTime(lastModifiedTime);
				sctx.setExtendClass(radd.getProcessorClassName());
				sctx.setExtendClass(exetendClass);

				if (declaration != null && declaration.length() > 0)
					sctx.setDeclaration(declaration);

				Compiler compiler = new Compiler(sctx);

				Class scriptClass = compiler.getCompiledClass();

				boolean needCompile = true;

				if (scriptClass != null) {

					Processor preProcessor = (Processor) scriptClass.newInstance();

					if (preProcessor.getCheckTime() == sctx.getCheckTime()) {
						process = preProcessor;
						needCompile = false;
					}
				}

				if (needCompile) {
					compiler.generateJava();
					compiler.generateClass();

					scriptClass = compiler.getCompiledClass();

					if (scriptClass != null) {
						process = (Processor) scriptClass.newInstance();

					}

				}
			}

			process = getProcessor(ctx, process);

			xmlDoc.lookup("/transaction/processor-info/property");

			while (xmlDoc.next()) {
				String key = xmlDoc.getString("name");
				String val = xmlDoc.getString("value");

				if (key == null || key.length() == 0 || val == null || val.length() == 0)
					throw new ResourceException("<property> option invalid");

				process.setProperty(key, val);
			}

		} catch (ResourceException re) {
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceException(e.toString());
		}

		return process;

	}

	/**
	 * process 가 param으로 들어가는 경우는 그 객체를 그대로 이용하고, 그렇지 않은 경우는 이 class를 구현하는 class에서 Processor 객체를 반납시켜야 한다.
	 * 
	 * @param ctx
	 * @param process
	 * @return
	 * @throws Exception
	 */
	abstract protected Processor getProcessor(IOSchemaContext ctx, Processor process) throws Exception;

}