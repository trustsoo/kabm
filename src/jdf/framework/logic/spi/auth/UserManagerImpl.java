package jdf.framework.logic.spi.auth;

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import jdf.framework.core.xml.*;





/**
 * Xml FIle 기반의 사용자 관리
 * 
 * @author
 * 
 */
public class UserManagerImpl implements UserManager
{
	private final static String LOG_ID = "<x:UserManagerImpl> ";

	private static long filetime = 0;

	private static String aclFilePath = Configuration.getConfigPath() + "/admin-console-acl.xml";

	private XMLReferer xRef;

	private static Map userMap = new HashMap();

	private static List userList = new ArrayList();

	public UserManagerImpl() {

	}

	/*
     * (non-Javadoc)
     * 
     * 
     */
	public User login(String userid, String pwd) throws Exception
	{
		loadFile();

		User tmp = new User(userid, pwd);

		User user = (User) userMap.get(userid);

		if (user == null)
			throw new Exception(userid + " not regist");

		if (!user.getDigestPassword().equals(tmp.getDigestPassword()))
			throw new Exception(userid + " password incorrect");

		user.setPassword(pwd);

		return user;

	}

	public User loginSecurity(String userid, String pwd) throws Exception
	{
		loadFile();

		// pwd=getDigestMessage(pwd);

		User user = (User) userMap.get(userid);

		if (user == null)
			throw new Exception(userid + " not regist");

		if (!user.getDigestPassword().equals(pwd))
			throw new Exception(userid + " password incorrect");

		return user;
	}

	private void loadFile()
	{
		File f = new File(aclFilePath);

		// 파일이 변경되거나, 한번도 읽지 않았다면....
		if (filetime != f.lastModified()) {
			Logger.info.println(LOG_ID + "load acl file : " + aclFilePath);
			userMap.clear();
			userList.clear();

			xRef = new XMLReferer(f);

			xRef.lookup("/acl/user-list/user");

			while (xRef.next()) {

				String username = xRef.getString("username");
				String roles = xRef.getString("roles");
				String pwd = xRef.getString("password");

				if (username == null || pwd == null) {
					Logger.info.println(LOG_ID + "load acl file skip");
					continue;
				}

				// DataSet user = new DataSet();
				// user.put("username", username);
				User user = new User(username);
				user.setRoles(roles);
				user.setDigestPassword(pwd);

				userMap.put(username, user);
				userList.add(user);

			}
			
			filetime = f.lastModified();

		}

	}

	/**
     * 
     * @see  jdf.framework.logic.spi.auth.UserManager#getUserDataSet(java.lang.String)
     */
	public User getUser(String userid)
	{
		loadFile();
		return (User) userMap.get(userid);

	}

	/**
     * 
     * @see  jdf.framework.logic.spi.auth.UserManager#getUserDataSetList()
     */
	public List getUserList()
	{
		loadFile();
		return userList;
	}

	/**
     * 
     * @param userInfo
     * @throws Exception
     */
	public void updateUser(User userInfo) throws Exception
	{
		loadFile();

		xRef.lookup("/acl/user-list/user");

		String username = userInfo.getId();

		while (xRef.next()) {
			if (username.equals(xRef.getString("username"))) {
				// System.out.println("찾았다");
				break;
			}
		}

		xRef.setAttribute("roles", userInfo.getRoles());

		xRef.setAttribute("password", userInfo.getDigestPassword());

		writeFile(xRef);
	}

	/**
     * 
     *
     */
	public void addUser(User user) throws Exception
	{
		String username = user.getId();

		if (getUser(username) != null)
			throw new Exception(username + " 사용자가 이미 존재합니다.");

		loadFile();

		xRef.lookup("/acl/user-list");

		List list = new ArrayList();

		list.add(xRef.createAttribute("username", username));
		list.add(xRef.createAttribute("roles", user.getRoles()));
		list.add(xRef.createAttribute("password", user.getDigestPassword()));

		org.w3c.dom.Element em = xRef.createElement("user", null, list);
		xRef.appendChild(em);

		writeFile(xRef);

	}

	private static void writeFile(XMLReferer doc) throws IOException
	{

		File file = new File(aclFilePath);
		Writer writer = new FileWriter(file);

		DOMWriter dWriter = new DOMWriter(writer, false);
		DOMWriter.setWriterEncoding("euc-kr");

		dWriter.setNewlines(true);
		dWriter.print(doc.getDocument());
	}

	/**
     * 
     * @see  jdf.framework.logic.spi.auth.UserManager#deleteUser(java.lang.String)
     */
	public void deleteUser(String userid) throws Exception
	{
		loadFile();

		xRef.lookup("/acl/user-list/user");

		while (xRef.next()) {

			if (userid.equals(xRef.getString("username"))) {
				xRef.removeElement();
			}
		}

		writeFile(xRef);

	}
}