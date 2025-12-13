/*
 * Created on 2005. 3. 7.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jdf.framework.core.service;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface Manageable {

	public static final String CONFIG_DIRECTORY  = "jdf.framework.core.service.config.dir";
	public static final String CONFIG_FILENAME   = "jdf.framework.core.service.config.file";
	public static final String ROOT_ELEMENT_NAME = "jdf.framework.core.service.xml.name";
		
	public void initialize() throws Exception;
	
	public void finalize() throws Exception;
}
