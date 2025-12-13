package jdf.framework.view.auth;



import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.Utility;
import jdf.framework.view.menu.entity.MenuItem;



public class DefaultAuthUser extends User
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String LOG_ID="<t:DefaultAuthUser> ";
    
    //private final static String USER_SESSION_NAME = "anytemplet_user_session";

	private final static int ENABLE = 0;

	// Permission 인터페이스에서 구현
	/*
	private final static int NO_PERMISSION = -1;
	private final static int UNREGISTED = -2;
	*/

	private static final DefaultAuthUser GUEST_USER = new DefaultAuthUser();
	private boolean is_login;

	public DefaultAuthUser()
	{
		super("FileAuthUser");
		is_login = false;

	}

	/**
	 * 
	 * 사용자 login 여부를 판단한다.
	 * 
	 */
	public boolean isLogin()
	{
		return is_login;
	}

	public void login(HttpServletRequest request, HttpServletResponse response) throws PermissionException, Exception
	{
		AclInfo acl = AclInfo.getAclInfoInstance();

		int result = ENABLE;

		RoleInfo role = null;

		String rolename = request.getParameter("role");

		if (rolename == null)
		{
			role = acl.getDefaultRoleInfo();

			result = checkAcl(request, response, role);
			if (result == ENABLE)
				return;
		}
		else
		{
			// admin,manage 이런식으로 오는 role을 배열로 변환
			// 각 role마다 테스트하여 하나라또 ENABLE이 떨어지면 통과
			String[] rolenames = SmartStringArray.split(",", rolename);

			for (int i = 0; i < rolenames.length; i++)
			{
				role = acl.getRoleInfo(rolenames[i]);
				result = checkAcl(request, response, role);

				if (result == ENABLE)
					return;
			}
		}

		switch (result)
		{
			case NO_PERMISSION :
				throw new PermissionException(NO_PERMISSION, "NO_PERMISSION");

			case UNREGISTED :
				throw new PermissionException(UNREGISTED, "UNREGISTED");
		}
	}

	/**
	 * 
	 * 사용자 login 과정
	 * 
	 */
	private int checkAcl(HttpServletRequest request, HttpServletResponse response, RoleInfo role) throws Exception
	{
		AclInfo acl = AclInfo.getAclInfoInstance();

		// 자체 acl.xml에서 user list를 조회하는 경우
		if (role.getSourceUri().indexOf("file:") == 0)
		{
			AclUser user = (AclUser) acl.getUserMap().get(getId());

			if (user != null)
			{
				if (user.getPassword() != null && user.getPassword().equals(getPassword()))
				{
					is_login = true;
					//request.getSession().setMaxInactiveInterval(30);
					request.getSession().setAttribute(DefaultAuthUser.SESSION_KEY, this);					
					setAuthLevel(user.getRoles());
					setIp(request.getRemoteAddr());

					return ENABLE;

				}
				else
					return NO_PERMISSION;
			}

			return UNREGISTED;
		}
		else if (role.getSourceUri().indexOf("io-schema:") == 0)
		{
			String uri = role.getSourceUri();
			String io_schema_name = uri.substring(uri.indexOf(":") + 1);

			Connection conn = null;

			try
			{
				DataSet input = new DataSet();
				/*input.put("user_id", this.getId());
				input.put("pwd", this.getPassword());*/
				
				input.put("user_id", request.getParameter("user_id"));
				input.put("pwd", request.getParameter("pwd"));
				

				conn = DefaultConnectionFactory.getConnection();
				Interaction interact = conn.createInteraction();
				DataSet output = interact.execute(io_schema_name, input);

				String userid = output.getText("user_id");

				Logger.debug.println(LOG_ID+"output=" + output.toString());

				// output으로 사용자 id가 튀어나오면 인증이 되었다는것임
				if (userid.length() > 0)
				{
					is_login = true;
					request.getSession().setAttribute(DefaultAuthUser.SESSION_KEY, this);
					request.getSession().setAttribute("user_seq_no", output.getText("user_seq_no"));
					String auth = output.getText("role");
					setAuthLevel(auth);

					setIp(request.getRemoteAddr());

					output.unfixNull();
					setId(userid);
					setName(output.getText("full_name"));
					setEmail(output.getText("email"));
					setUser_seq_no(output.getText("user_seq_no"));

					output.fixNull();
					setDataSet(output);

					return ENABLE;
				}
				else
					return UNREGISTED;

			}
			catch (Exception ee)
			{
				
				if(ee instanceof jdf.framework.core.data.ResourceException)
				{
					//jdf.framework.core.data.ResourceException  rex = (jdf.framework.core.data.ResourceException)ee;
					
					throw ee;
					
				}
				
				Logger.err.println("io-schema로부터 비교시 err");
				//ee.printStackTrace();
				//throw new PermissionException("권한이 없습니다. "+ee.toString());
				return NO_PERMISSION;

			}
			finally
			{
				try
				{
					if (conn != null)
						conn.close();
				}
				catch (Exception e)
				{
				}

			}
		}

		return NO_PERMISSION;

	}

	public void logout(HttpServletRequest request, HttpServletResponse response)
	{
		request.getSession().removeAttribute(DefaultAuthUser.SESSION_KEY);
	}

	private static String getRequestUrl(HttpServletRequest request)
	{

		
		StringBuffer url = jdf.framework.view.menu.util.RequestURL.getUrl(request);
		

		if (request.getQueryString() != null)
		{
			url.append('?');
			url.append(request.getQueryString());
		}

		return url.toString();
	}

	public void checkPrivilege(HttpServletRequest request, HttpServletResponse response, MenuItem menuitem)
		throws PermissionException
	{
		AclInfo acl = AclInfo.getAclInfoInstance();
		User usersession = findUser(request);
		//String s = usersession.getAuthLevel();
		PrintWriter out =  null;
		try
		{
			if (menuitem.hasAuthLevel(usersession))
				return;
		}
		catch (PermissionException permissionexception)
		{
			throw permissionexception;
		}
		catch (Exception exception)
		{
			return;
		}
		
		
		
		if (!usersession.isLogin())
		{
			if (response != null)
			{
				try
				{
					// 첫번째것을 page의 role로 간주
					String rolename = menuitem.getAuthLevel()[0];
					
					Logger.debug.println("menuitem.getUrl():"+menuitem.getUrl());
					
					RoleInfo role = acl.getRoleInfo(rolename);
					if (role == null)
						role = acl.getDefaultRoleInfo();

					
					
					String directUrl = menuitem.getParam("directUrl");
					String layerName = menuitem.getParam("layerName");
					String loadJsFunc = menuitem.getParam("loadJsFunc");
					
					request.setAttribute("directUrl", directUrl);
					request.setAttribute("layerName", layerName);
					request.setAttribute("loadJsFunc", loadJsFunc);
					request.setAttribute("isRedirect", "true");
					
					
					
					StringBuffer params = new StringBuffer();
					Enumeration enu = request.getParameterNames();
					String key  = null;
					String val = null;
					while(enu.hasMoreElements())
					{
						key = (String)enu.nextElement();
						val = request.getParameter(key);
						params.append(key+"="+val).append("&");
					}
					
					request.setAttribute("params", params.toString());
					Logger.debug.println(LOG_ID+"params>>>>>>>>>>>>>>>>"+params);
					
					
					out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
					StringBuffer sb = new StringBuffer();
					sb.append("SESSION_OUT|")
					.append("directUrl=").append(java.net.URLEncoder.encode(directUrl))
					.append("&layerName=").append(java.net.URLEncoder.encode(layerName))
					.append("&loadJsFunc=").append(java.net.URLEncoder.encode(loadJsFunc))
					.append("&params=").append(java.net.URLEncoder.encode(params.toString()));

					out.write(sb.toString());
					
					out.flush();
					
				}
				catch (Exception ee)
				{
					Logger.err.println(LOG_ID+"checkPrivilege err.", ee);
				} finally
				{
					if(out != null) try{out.close();}catch(Exception ex){}
				}

			}
			else
				throw new NotLoginException("로그인이 하지 않았습니다.");

		}

		else
			throw new PermissionException(NO_PERMISSION, "권한이 없습니다.");
	}
	
	public void checkAdminPrivilege(HttpServletRequest request, HttpServletResponse response, MenuItem menuitem)
	throws PermissionException 
{
		AclInfo acl = AclInfo.getAclInfoInstance();
        User usersession = findUser(request);
        String s = usersession.getAuthLevel();
        try
        {
            if(menuitem.hasAuthLevel(usersession))
                return;
        }
        catch(PermissionException permissionexception)
        {
            throw permissionexception;
        }
        catch(Exception exception)
        {
            return;
        }
        if(!usersession.isLogin())
        {
            if(response != null)
                try
                {
                    String rolename = menuitem.getAuthLevel()[0];
                    RoleInfo role = acl.getRoleInfo(rolename);
                    if(role == null)
                        role = acl.getDefaultRoleInfo();
                    StringBuffer redirectUrl = new StringBuffer();
                    redirectUrl.append(role.getLoginUrl());
                    redirectUrl.append("role=").append(menuitem.getAuthLevels());
                    String rdirectUrl = request.getParameter("redirect");
                    if(rdirectUrl == null || rdirectUrl.length() == 0)
                        rdirectUrl = getRequestUrl(request);
                    if(rdirectUrl == null)
                        rdirectUrl = menuitem.getUrl();
                    Logger.debug.println("<at:DefaultAuthUser:admin:> redirect URL:" + rdirectUrl);
                    redirectUrl.append("&redirect:admin=").append(java.net.URLEncoder.encode(rdirectUrl));
                    response.sendRedirect(redirectUrl.toString());
                }
                catch(Exception ee)
                {
                    Logger.err.println("<at:DefaultAuthUser:> checkAdminPrivilege err.", ee);
                }
            else
                throw new NotLoginException("로그인이 하지 않았습니다.");
        } else
        {
            throw new PermissionException(-1, "권한이 없습니다.");
        }
}

	private User findUser(HttpServletRequest httpservletrequest)
	{
		User usersession = (User) httpservletrequest.getSession().getAttribute(DefaultAuthUser.SESSION_KEY);
		if (usersession == null)
			return GUEST_USER;
		else
			return usersession;
	}

}