/*
 * Created on 2004-03-03
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jdf.framework.view.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.log.Logger;
import jdf.framework.view.menu.entity.MenuItem;


/**
 * 
 * @author
 * @version 1.0
 * @since 2004-03-03 오후 5:21:55
 * 
 */
public class DbmsAuthCheckerUser extends User
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String USER_AUTH_IOSCHEMA= "anytemplet/userauth";

    public DbmsAuthCheckerUser()
    {}

    /**
     *
     * @see jdf.framework.view.auth.User#checkPrivilege(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, jdf.framework.view.menu.entity.MenuItem)
     */
    public void checkPrivilege(HttpServletRequest req, HttpServletResponse res, MenuItem menu)
        throws PermissionException
    {

        HttpSession session= req.getSession();

        String group_id= (String) session.getAttribute("sDeptCD");
        String user_id= (String) session.getAttribute("sUserID");

        if (res == null)
        {

            if (group_id == null)
            {
                throw new PermissionException("권한이 없습니다.");
                //return;
            }

            DataSet menu_id_dataset= (DataSet) session.getAttribute("menu_ids");

            if (menu_id_dataset == null)
            {
                DataSet input= new DataSet();
                input.put("group_id", group_id);
                input.put("user_id", user_id);

                menu_id_dataset= execute(USER_AUTH_IOSCHEMA, input);

                Logger.debug.println(
                    "<DbmsAuthCheckerUser> menu_id_dataset=" + input.toString() + "\n" + menu_id_dataset.toString());

                session.setAttribute("menu_ids", menu_id_dataset);
            }

            String menu_id= menu.getIndexKey();

            Logger.info.println("<DbmsAuthCheckerUser> check " + menu_id + " " + group_id + " " + user_id);

            for (int i= 0; i < menu_id_dataset.getCount("menu_id"); i++)
            {
                String mn_id= menu_id_dataset.getText("menu_id", i);

                if (menu_id.indexOf(mn_id) >= 0)
                    return;
            }

            throw new PermissionException("사용자권한이 없습니다.");
        }

    }

    /**
     * 
     * @see jdf.framework.view.auth.User#isLogin()
     */
    public boolean isLogin()
    {
        return super.isLogin();
    }

    /**
     * 
     * @see jdf.framework.view.auth.User#login(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void login(HttpServletRequest arg0, HttpServletResponse arg1) throws PermissionException, Exception
    {
        super.login(arg0, arg1);
    }

    /**
     * 
     * @see jdf.framework.view.auth.User#logout(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void logout(HttpServletRequest arg0, HttpServletResponse arg1)
    {
        super.logout(arg0, arg1);
    }

    private DataSet execute(String ioschema, DataSet input)
    {
        Connection conn= null;
        DataSet output= new DataSet();
        try
        {
            conn= DefaultConnectionFactory.getConnection();
            Interaction interact= conn.createInteraction();

            output= interact.execute(ioschema, input);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (Exception ee)
                {}
            }
        }

        return output;
    }

}