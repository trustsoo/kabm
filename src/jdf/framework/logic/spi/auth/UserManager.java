/*
 * Created on 2005. 5. 19.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jdf.framework.logic.spi.auth;

import java.util.List;


/**
 * 관리자,FTP 등 관리자 ID 를 관리하는 Manager
 * 
 * 
 * @author
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface UserManager
{
	/**
     * 
     * 
     * @param userid
     * @param pwd
     * @return
     * @throws Exception
     */
	public User login(String userid, String pwd) throws Exception;

	/**
     * 
     * @param userid
     * @param pwd
     * @return
     * @throws Exception
     */
	public User loginSecurity(String userid, String pwd) throws Exception;

	/**
     * 
     * @param userid
     * @return
     */
	public User getUser(String userid);

	/**
     * 
     * @return
     */
	public List getUserList();

	/**
     * 
     * @param userInfo
     * @throws Exception
     */
	public void addUser(User user) throws Exception;

	/**
     * 
     * @param userid
     * @throws Exception
     */
	public void deleteUser(String userid) throws Exception;

	/**
     * 
     * @param userInfo
     * @throws Exception
     */
	public void updateUser(User userInfo) throws Exception;
}