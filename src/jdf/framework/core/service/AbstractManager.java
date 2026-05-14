/*
 * Created on 2005. 3. 7.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jdf.framework.core.service;

import javax.naming.InitialContext;
import java.util.Hashtable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractManager implements Manageable{
	
	static {
		try {
			String ctxFactory = System.getProperty("java.naming.factory.initial");

			if(ctxFactory == null || ctxFactory.length()==0)
                System.setProperty("java.naming.factory.initial", "jdf.framework.core.naming.InitialContextFactory");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static InitialContext ctx;
	
	protected Hashtable env = null;
	
	protected AbstractManager() {}
	
	protected AbstractManager(Hashtable env) {
		this.env = env;
	}
	
	protected InitialContext getInitialContext() throws javax.naming.NamingException
	{
		if (ctx == null) {
			ctx = new InitialContext();
		}
		
		return ctx;
	}
	
	public abstract void initialize() throws Exception;
	
	public void finalize() throws Exception {}
}
