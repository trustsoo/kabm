package jdf.framework.logic.spi.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;




/**
 * 
 * FRAMEWORK 관련 시스템을 접근 관련 사용자 정보를 담고있는 class
 * 
 * @author
 * 
 */
public class User
{

	private String id = "anonymous";

	private String roles;

	private String password;

	private String digestPassword;

	private boolean isAnonymousFlag = true;

	private Properties properties = new Properties();

	public User() {
		id = "anonymous";
		this.isAnonymousFlag = true;
	}

	/**
	 * 
	 * @param id
	 */
	public User(String id) {
		this.id = id;
		this.isAnonymousFlag = false;
	}

	/**
	 * 
	 * @param id
	 * @param pwd
	 */
	public User(String id, String pwd) {
		this.id = id;
		this.password = pwd;
		this.isAnonymousFlag = false;
	}

	/**
	 * get password
	 * 
	 * @return
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String getDigestPassword() throws NoSuchAlgorithmException
	{
		if (this.digestPassword == null)
			this.digestPassword = getDigestMessage(this.password);
		return this.digestPassword;
	}

	public void setDigestPassword(String digestPassword)
	{
		this.digestPassword = digestPassword;
	}

	/**
	 * set password
	 * 
	 * @param password
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * get user roles
	 * 
	 * @return
	 */
	public String getRoles()
	{
		return roles;
	}

	/**
	 * set user roles
	 * 
	 * @param roles
	 */
	public void setRoles(String roles)
	{
		this.roles = roles;
	}

	/**
	 * get user id
	 * 
	 * @return
	 */
	public String getId()
	{
		return id;
	}

	private static String getDigestMessage(String val) throws NoSuchAlgorithmException
	{
		byte[] getpassword = val.getBytes();

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(getpassword);

		val = toHex(md.digest());

		return val;
	}

	private static String toHex(byte[] digest)
	{
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			buf.append(Integer.toHexString((int) digest[i] & 0x00FF));
		}
		return buf.toString();
	}

	/**
	 * 익명 사용자(계정이 없는 사용자)지 여부
	 * 
	 * @return
	 */
	public boolean isAnonymous()
	{
		return this.isAnonymousFlag;
	}

	public String toEncodingString()
	{
		String result = this.id + " " + this.password;

		return SimpleEncrypt.encrypt(result);
	}

	public void decodingString(String code)
	{
		String str = SimpleEncrypt.decrypt(code);

		int p = str.indexOf(" ");

		this.id = str.substring(0, p);
		this.password = str.substring(p + 1);

	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key)
	{
		return properties.getProperty(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value)
	{
		this.properties.setProperty(key, value);
	}

}
