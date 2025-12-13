package jdf.framework.logic.spi.process.compiler;

import jdf.framework.core.data.ResourceException;


/**
 * 
 * @author
 *
 */
public class CompileException extends ResourceException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2216651703270770593L;
	
	
	public CompileException(String msg)
	{
		super(msg);
	}

}