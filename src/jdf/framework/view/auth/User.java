package jdf.framework.view.auth;

import jdf.framework.core.data.DataSet;
import jdf.framework.view.menu.entity.MenuItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 다음 추상 class는 Web Application 상에서 일반적으로 로그인한 사용자를 의미하는 사용자 객체를 추상화한 class이다.
 * 
 * 
 * @version 1.0
 * @author
 */
public class User implements Serializable, Permission {
	/**
	 * 
	 */
	private static final long serialVersionUID = -820960894384649403L;

	public final static String GUEST = "guest";

	public final static String LOGIN_USER = "loginUser";

	public final static String ADMIN = "admin";

	public final static String SESSION_KEY = "user";

	


	
	private String user_id = "GUEST USER";
	private String pwd;
	private String name;
	private String email;	
	private String user_seq_no;
	private String ip;
	private String port;
	
	private String dept_no;
	private String dept_cd;
	private String dept_nm;
	private String hp_no;
	private String duty_cd;
	private String postn_cd;
	private String duty_nm;
	private String postn_nm;
	private String cmpny_emp_cd;
	private String cmpny_no;
	private String cti_emp_yn;
	private String grp_no;
	private String grp_cd;
	
	private HttpSession session;
	
	private String authLevel = GUEST;

	private Map store = new HashMap();

	private static Class userImplClass;

	private User user;

	public User() {
		try {
			if (userImplClass != null)
				user = (User) userImplClass.newInstance();

		} catch (IllegalAccessException ee) {
			ee.printStackTrace();
		} catch (InstantiationException ie) {
			ie.printStackTrace();

		}
	}

	public User(String name) {

	}

	public static void setUserImplClass(Class clss) {

		userImplClass = clss;

	}

	/**
	 * 사용자 로그인을 한다.
	 * 
	 * 이 메쏘드에서 구현해야 작업은 1) 사용자 로그인 가능 여부 확인 2) 가능한 경우 일자척으로 session이나 cookie를
	 * 이용하여 사용자 session을 유지 3) 사용자의 권한 레벨 정의 4) 로그인 실패시 Exception을 통한 메세지 전달
	 * 
	 * @see User#login(HttpServletRequest, HttpServletResponse)
	 */
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws PermissionException, Exception {

		// login 과정은 User 객체가 생성되지 않은 시점에서 일어난다.
		if (user != null) {

			user.login(request, response);

		}

		jdf.framework.core.log.Logger.info
				.println("<anytemplet.auth.User> login sucess. id="
						+ this.getId());

	}

	/**
	 * 사용자 로그아웃
	 * 
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response) {
	}

	/**
	 * 사용자 로그인 여부
	 * 
	 */
	public boolean isLogin() {
		if (user != null)
			return user.isLogin();

		return false;
	}

	/**
	 * 
	 * 사용자의 권한 체크를 한다. 주의 ) 이 메쏘뜨는 현재 이 사용자 객체의 권한 상태를 체크하는 것이 아니라 입력으로 들어온
	 * request에서 session이 유지되는 사용자 객체를 찾아 비교한다. response를 세팅하면 sendRedirect를 통해
	 * login url로 이동하고, response 객체를 세팅하지 않으면(null), PermissionException 을
	 * 발생시킨다.
	 * 
	 * @see User#checkPrivilege(MenuItem)
	 */
	public void checkPrivilege(HttpServletRequest request,
			HttpServletResponse response, MenuItem menu)
			throws PermissionException {
	}
	
	/**
	 * 
	 * 사용자의 권한 체크를 한다. 주의 ) 이 메쏘뜨는 현재 이 사용자 객체의 권한 상태를 체크하는 것이 아니라 입력으로 들어온
	 * request에서 session이 유지되는 사용자 객체를 찾아 비교한다. response를 세팅하면 sendRedirect를 통해
	 * login url로 이동하고, response 객체를 세팅하지 않으면(null), PermissionException 을
	 * 발생시킨다.
	 * 
	 * @see User#checkPrivilege(MenuItem)
	 */
	public void checkAdminPrivilege(HttpServletRequest request,
			HttpServletResponse response, MenuItem menu)
			throws PermissionException {
	}
	
	
	/*List에나올수 있는 리스트 체크*/
	public void checkPrivilegebyList(HttpServletRequest req, HttpServletResponse res, MenuItem menu){}
	
	
	public HttpSession getSession()
	{
		return session;
	}
	

	public void setSession(HttpSession session)
	{
		this.session = session;
	}
	
	/**
	 * Returns the email.
	 * 
	 * @return String
	 */
	public String getEmail() {
		if (user != null)
			return user.getEmail();
		return email;
	}

	/**
	 * Returns the id.
	 * 
	 * @return String
	 */
	public String getId() {
		if (user != null)
			return user.getId();
		return user_id;
	}

	/**
	 * Returns the ip.
	 * 
	 * @return String
	 */
	public String getIp() {
		if (user != null)
			return user.getIp();
		return ip;
	}
	
	/**
	 * Returns the port.
	 * 
	 * @return String
	 */
	public String getPort() {
		if (user != null)
			return user.getIp();
		return port;
	}

	/**
	 * Returns the name.
	 * 
	 * @return String
	 */
	public String getName() {
		if (user != null)
			return user.getName();
		return name;
	}

	/**
	 * Returns the password.
	 * 
	 * @return String
	 */
	public String getPassword() {
		if (user != null)
			return user.getPassword();
		return pwd;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            The email to set
	 */
	public void setEmail(String email) {
		if (user != null)
			user.setEmail(email);
		this.email = email;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            The id to set
	 */
	public void setId(String id) {
		if (user != null)
			user.setId(id);
		this.user_id = id;
	}

	/**
	 * Sets the ip.
	 * 
	 * @param ip
	 *            The ip to set
	 */
	public void setIp(String ip) {
		if (user != null)
			user.setIp(ip);
		this.ip = ip;
	}
	
	/**
	 * Sets the port.
	 * 
	 * @param port
	 *            The port to set
	 */
	public void setPort(String port) {
		if (user != null)
			user.setPort(port);
		this.port = port;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		if (user != null)
			user.setName(name);
		this.name = name;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            The password to set
	 */
	public void setPassword(String password) {
		if (user != null)
			user.setPassword(password);
		this.pwd = password;
	}

	/**
	 * 로그인한 사용자 객체에 특정 객체를 보관한고 싶은 경우 이용한다.
	 * 
	 * @param key
	 *            특정 object을 구별할 수 있는 key값
	 * @param value
	 *            저장되는 객체
	 */
	public void setAttribute(Object key, Object value) {
		if (user != null)
			user.setAttribute(key, value);
		store.put(key, value);
	}

	/**
	 * 로그인한 사용자 객체에 특정 객체를 보관한고 싶은 경우 이용한다.
	 * 
	 * @param key
	 *            특정 object을 구별할 수 있는 key값
	 * @return value 저장되어있는 객체
	 */
	public Object getAttribute(Object key) {
		if (user != null)
			return user.getAttribute(key);
		return store.get(key);
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		if (user != null)
			return user.toString();

		StringBuffer buf = new StringBuffer();

		buf.append("id:").append(this.user_id).append(",ip:").append(this.ip).append(", dataset:"+this.getDataSet());

		return buf.toString();
	}

	/**
	 * Returns the authLevel.
	 * 
	 * @return String
	 */
	public String getAuthLevel() {
		if (user != null)
			return user.getAuthLevel();

		return authLevel;
	}

	/**
	 * Sets the authLevel.
	 * 
	 * @param authLevel
	 *            The authLevel to set
	 */
	public void setAuthLevel(String authLevel) {
		if (user != null)
			user.setAuthLevel(authLevel);
		this.authLevel = authLevel;
	}

	private DataSet dataset;

	public void setDataSet(DataSet dataset) {
		if (user != null)
			user.setDataSet(dataset);
		this.dataset = dataset;
	}

	public DataSet getDataSet() {
		if (user != null)
			return user.getDataSet();
		return this.dataset;

	}

	public String getUser_seq_no() {
		return user_seq_no;
	}

	public void setUser_seq_no(String user_seq_no) {
		this.user_seq_no = user_seq_no;
	}
	
	public void setDeptNo(String dept_no)
	{
		this.dept_no = dept_no;
	}
	
	public String getDeptNo()
	{
		return dept_no;
	}
	
	
	public String getDeptCd() {
		return dept_cd;
	}

	public void setDeptCd(String dept_cd) {
		this.dept_cd = dept_cd;
	}

	public void setDeptNm(String dept_nm)
	{
		this.dept_nm = dept_nm;
	}
	
	public String getDeptNm()
	{
		return dept_nm;
	}
	
	
	public void setHpNo(String hp_no)
	{
		this.hp_no = hp_no;
	}
	
	public String getHpNo()
	{
		return hp_no;
	}
	
	public void setDutyCd(String duty_cd)
	{
		this.duty_cd = duty_cd;
	}
	
	public String getDutyCd()
	{
		return duty_cd;
	}
	
	public void setDutyNm(String duty_nm)
	{
		this.duty_nm = duty_nm;
	}
	
	public String getDutyNm()
	{
		return duty_nm;
	}
	
	public void setPostnCd(String postn_cd)
	{
		this.postn_cd = postn_cd;
	}
	
	public String getPostnCd()
	{
		return postn_cd;
	}
	
	public void setPostnNm(String postn_nm)
	{
		this.postn_nm = postn_nm;
	}
	
	public String getPostnNm()
	{
		return postn_nm;
	}
	
	public void setCmpnyEmpCd(String cmpny_emp_cd)
	{
		this.cmpny_emp_cd = cmpny_emp_cd;
	}
	
	public String getCmpnyEmpCd()
	{
		return cmpny_emp_cd;
	}
	
	public void setCmpnyNo(String cmpny_no)
	{
		this.cmpny_no = cmpny_no;
	}
	
	public String getCmpnyNo()
	{
		return cmpny_no;
	}
	
	
	public void setGrpNo(String grp_no)
	{
		this.grp_no = grp_no;
	}
	
	public String getGrpNo()
	{
		return grp_no;
	}
	
	public void setGrpCd(String grp_cd)
	{
		this.grp_cd = grp_cd;
	}
	
	public String getGrpCd()
	{
		return grp_cd;
	}
	
	public void setCtiEmpYn(String cti_emp_yn)
	{
		this.cti_emp_yn = cti_emp_yn;
	}
	
	public String getCtiEmpYn()
	{
		return cti_emp_yn;
	}

}