package jdf.framework.view.auth;


/**
 * 
 * Jeus 서버의 버그로 인해 만든 class
 * sendRedirect 시 일부러 exception을 발생시킨다.
 * 
 * @author
 *
 */
public class RedirectPermissionException extends PermissionException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -782874808827094425L;

	public RedirectPermissionException()
    {
        super();
    }
    
    public RedirectPermissionException(String msg)
    {
        super(msg);
    }
    
    public RedirectPermissionException(int permission_code, String msg)
    {
        super(permission_code, msg);

        
    }
}
