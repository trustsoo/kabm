package jdf.framework.view.auth;

import java.util.HashMap;
import java.util.Map;

import jdf.framework.core.Configuration;
import jdf.framework.core.xml.XMLReferer;



/**
 * 
 * Role 정보를 가지고 있는 객
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AclInfo
{
	private String defaultRoleName;
	private RoleInfo defaultRoleInfo;

	private Map roles = new HashMap();

	private Map users = new HashMap();

	private static AclInfo instance;

	private AclInfo()
	{
		init();
	}

	public synchronized static AclInfo getAclInfoInstance()
	{
		if (instance == null)
			instance = new AclInfo();
		return instance;
	}

	public void init()
	{
		//Logger.info.println("<AclInfo> init start");

		try
		{
			XMLReferer xmlDoc = new XMLReferer(Configuration.getConfigPath(), "acl.xml");

			defaultRoleName = xmlDoc.lookup("/acl/default-role").getText();

			xmlDoc.lookup("/acl/role");

			while (xmlDoc.next())
			{
				String name = xmlDoc.getString("name");

				String login_url = xmlDoc.find("login").getString("url");
				String user_source = xmlDoc.find("user").getString("source-uri");
				
				StringBuffer buf = new StringBuffer();
				buf.append(login_url);
				
				if (login_url.indexOf("?") > 0)
					buf.append("&");
				else
					buf.append("?");

				//login_url = login_url+"role="+name+"&";    

				RoleInfo role = new RoleInfo(name);
				role.setLoginUrl(buf.toString());
				role.setSourceUri(user_source);

				roles.put(name, role);

				//Logger.info.println("<AclInfo> regist role. [" + name + "] url:" + login_url);

				if (name.equals(defaultRoleName))
					defaultRoleInfo = role;
			}

			xmlDoc.lookup("/acl/user-list/user");

			while (xmlDoc.next())
			{

				String username = xmlDoc.getString("username");
				String password = xmlDoc.getString("password");
				String roles = xmlDoc.getString("roles");

				AclUser user = new AclUser(username, password, roles);

				users.put(username, user);

				//Logger.info.println("<AclInfo> regist user. " + username);

			}

		}
		catch (Exception ee)
		{
			//Logger.err.println("<AclInfo> init err. ", ee);
		}

	}

	public RoleInfo getRoleInfo(String name)
	{
		return (RoleInfo) roles.get(name);
	}

	public RoleInfo getDefaultRoleInfo()
	{
		return this.defaultRoleInfo;
	}

	public Map getUserMap()
	{
		return this.users;
	}

}