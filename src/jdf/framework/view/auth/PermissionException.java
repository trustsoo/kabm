package jdf.framework.view.auth;

/**
 * 권한관련 Exception
 * 
 * 로그인 실패나 해당 페이지 및 자원에 대한 권한이 얿을시
 * 이 Exception 을 발생한다.
 */
public class PermissionException extends RuntimeException implements Permission
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1918586267903544154L;
	// permission 에러코드
	private int permission_code;

	/**
	 * Constructor for PermissionException.
	 */
	public PermissionException()
	{
		super();
	}

	/**
	 * Constructor for PermissionException.
	 * @param arg0
	 */
	public PermissionException(String arg0)
	{
		super(arg0);
	}

	
	public PermissionException(int permission_code, String msg)
	{
		super(msg);

		this.permission_code = permission_code;
	}
	
	
	public int getPermissionCode()
	{
		return this.permission_code;
	}

}