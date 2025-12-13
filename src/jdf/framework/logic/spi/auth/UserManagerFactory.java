/*
 * Created on 2005. 5. 19.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jdf.framework.logic.spi.auth;

/**
 * @author
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class UserManagerFactory
{
	private static UserManager mgr;

	public static UserManager getUserManager()
	{
		return getXmlUserManager();

	}

	private static UserManager getXmlUserManager()
	{
		if (mgr == null)
			mgr = new UserManagerImpl();

		return mgr;
	}

}
