/*
 * Created on 2004-03-03
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.view.auth;

/**
 * 
 * Login 하지 않은 경우 발생하는 Exception
 * 
 * @author
 * @version 1.0
 * @since 2004-03-03 오전 10:19:29
 * 
 */
public class NotLoginException extends PermissionException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3194885259836186051L;

	/**
     * Constructor for PermissionException.
     */
    public NotLoginException()
    {
        super();
    }

    /**
     * Constructor for PermissionException.
     * @param arg0
     */
    public NotLoginException(String arg0)
    {
        super(arg0);
    }

}