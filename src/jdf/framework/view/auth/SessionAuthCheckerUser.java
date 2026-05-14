/*
 * Created on 2004. 10. 19.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jdf.framework.view.auth;

import jdf.framework.core.log.Logger;
import jdf.framework.view.menu.entity.MenuItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author
 * 
 * session 에서 "anytemplet.acl" 값을 체크해서
 * 
 * 
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SessionAuthCheckerUser extends DefaultAuthUser
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -702354822338949670L;
	
	private final static String LOG_ID = "<f:SessionAuthCheckerUser> ";

	/**
	 * 
	 *
	 */
    public SessionAuthCheckerUser()
    {
    	Logger.debug.println(LOG_ID+"object create");

    }

    /**
     * 
     * @see User#checkPrivilege(HttpServletRequest,
     *      HttpServletResponse,
     *      MenuItem)
     */
    public void checkPrivilege(HttpServletRequest req, HttpServletResponse res, MenuItem menu)
            throws PermissionException
    {

        String[] menuAcls = menu.getAuthLevel();

        //Logger.debug.println(LOG_ID+"> >="+menu.getName());
        //Logger.debug.println(LOG_ID+"> >="+menuAcls.length);

        HttpSession session = req.getSession();

        String acl = (String) session.getAttribute("anytemplet.acl");

        if (acl == null)
            acl = "anytemplet_is_great";

        if (menuAcls.length == 0)
            return;

        for (int i = 0; i < menuAcls.length; i++)
        {
            // guest 와 같은 role 또는 acl 이름
            String aclName = menuAcls[i];

            if (MenuItem.GUEST_LEVEL.equals(aclName) || acl.equals(aclName))
            {
                //Logger.debug.println(LOG_ID+">>="+aclName);
                return;
            }
        }

        //return;

        throw new PermissionException(NO_PERMISSION, "권한이 없습니다.");

    }

}