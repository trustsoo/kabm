package com.kabm.filter.auth;

import com.kabm.util.NetworkUtil;
import com.kabm.util.SitePropertyManager;
import com.kabm.util.UserAgentUtil;
import jdf.framework.core.Message;
import jdf.framework.core.MessageBox;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringUtil;
import jdf.framework.core.util.Utility;
import jdf.framework.core.util.encrypt.CipherUtil;
import jdf.framework.view.auth.*;
import jdf.framework.view.menu.entity.MenuItem;
import jdf.framework.view.menu.util.RequestURL;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class FileAuthUser extends User
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String LOG_ID="<at:FileAuthUser> ";
    

	private final static int ENABLE = 0;

	
	private static final DefaultAuthUser GUEST_USER = new DefaultAuthUser();
	private boolean is_login;

	public FileAuthUser()
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
		Message msg= new MessageBox();
		
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
		
		//패스워드 오류횟수에러
		int pass_err_num = 0; 
		if(result < UNMATCHED_PSSWD_CNT && result > LOCKED_PSSWD)
		{
		    pass_err_num = (result - UNMATCHED_PSSWD_CNT) * -1; 
		    int pass_err_view = Integer.parseInt(SitePropertyManager.getString("PASSWD_ERR_VIEW_COUNT")); 
		    
		    if( pass_err_num < pass_err_view  ) 
		    {
		        result = UNMATCHED_PSSWD;
		    }
		    else
		    {
		      result = UNMATCHED_PSSWD_CNT;
		    }
		}
		
		switch (result)
		{
			case NO_PERMISSION :
				throw new PermissionException(NO_PERMISSION, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0002"));

			case UNREGISTED :
				throw new PermissionException(UNREGISTED, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0000"));
				
			case UNMATCHED_PSSWD : 
				throw new PermissionException(UNMATCHED_PSSWD, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0004"));
				
			case UNMATCHED_PSSWD_CNT : 
			    String massge = msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0005");
			    massge = massge.replaceAll("#total#", SitePropertyManager.getString("PASSWD_ERR_LIMIT_COUNT") );
			    massge = massge.replaceAll("#count#", pass_err_num+"" );
			    throw new PermissionException(UNMATCHED_PSSWD_CNT, massge);
			    
            case LOCKED_PSSWD : 
                throw new PermissionException(UNMATCHED_PSSWD, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0006"));			    
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
					setIp(NetworkUtil.getRemoteAddr(request));

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
				
				String cmd = null;
				String user_id = request.getAttribute(SitePropertyManager.getString("SESSION_USER_ID")) == null ? request.getParameter(SitePropertyManager.getString("SESSION_USER_ID")) : (String)request.getAttribute(SitePropertyManager.getString("SESSION_USER_ID"));
				String passwd = request.getAttribute(SitePropertyManager.getString("SESSION_PASSWORD")) == null ? request.getParameter(SitePropertyManager.getString("SESSION_PASSWORD")) : (String)request.getAttribute(SitePropertyManager.getString("SESSION_PASSWORD"));
				String idSaveMode = request.getAttribute(com.kabm.filter.CommonFilter.COOKIE_KEY) == null ? request.getParameter(com.kabm.filter.CommonFilter.COOKIE_KEY) : (String)request.getAttribute(com.kabm.filter.CommonFilter.COOKIE_KEY);	
				
				if(idSaveMode == null) idSaveMode = "";
				
				
				input.put("cmd", "login_default");
				input.put(SitePropertyManager.getString("SESSION_USER_ID"), user_id);				
				input.put(SitePropertyManager.getString("SESSION_PASSWORD"), passwd);
				
				conn = DefaultConnectionFactory.getConnection();
				Interaction interact = conn.createInteraction();
				DataSet output = new DataSet();
				String userid = null;

				try{
					output = interact.execute(io_schema_name, input);
					if( output.getCount(SitePropertyManager.getString("SESSION_USER_ID")) > 0)
						userid = output.getText(SitePropertyManager.getString("SESSION_USER_ID"));						
				}catch(Exception e){
					Logger.debug.println(LOG_ID+"LOGIN ERROR!!"+Utility.getStackTrace(e));
				}
				
				// output으로 사용자 id가 튀어나오면 인증이 되었다는것임
				if(output.getCount(SitePropertyManager.getString("SESSION_USER_ID")) > 0)
				{
				     int pass_err_num = output.getInt(SitePropertyManager.getString("PASSWD_ERR_NUM"));    
				     int pass_err_limit = Integer.parseInt(SitePropertyManager.getString("PASSWD_ERR_LIMIT_COUNT")); 

				     input.put("cmd", "update_passwd_err_num");     

				     // 패스워드 실패횟수 증가 및 최대횟수 초과시 LOCK
					if("N".equals(output.getText(SitePropertyManager.getString("SESSION_IS_MATCHE_PASSWD"))))
					{
                        if( pass_err_num < pass_err_limit ) 
                        {
                            pass_err_num++;
                            input.put(SitePropertyManager.getString("PASSWD_ERR_NUM"), pass_err_num);	    
                            interact.execute(io_schema_name, input);
                        }					    
                        
					    if( pass_err_num >= pass_err_limit ) 
					    {
					        return LOCKED_PSSWD;
					    }
					    
                        return UNMATCHED_PSSWD_CNT - pass_err_num;					    
					}
					else if(pass_err_num >= pass_err_limit)
					{
					    return LOCKED_PSSWD;
					}
					
					
					is_login = true;
					
                    input.put(SitePropertyManager.getString("PASSWD_ERR_NUM"), 0);       
                    interact.execute(io_schema_name, input);					
					
					//System.out.println("checkAcl==>"+user.isLogin());
					request.getSession().setAttribute(DefaultAuthUser.SESSION_KEY, this);					
					
					setIp(NetworkUtil.getRemoteAddr(request));
					//setPort(NetworkUtil.getRemotePort(request));
					
					setAuthLevel(output.getText(SitePropertyManager.getString("SESSION_AUTH_CODE")));
					setId(userid);
					setName(output.getText(SitePropertyManager.getString("SESSION_USER_NAME")));
					setEmail(output.getText(SitePropertyManager.getString("SESSION_USER_EMAIL")));
					setUser_seq_no(output.getText(SitePropertyManager.getString("SESSION_EMP_NO")));
					setDeptNo(output.getText(SitePropertyManager.getString("SESSION_DEPT_NO")));
					setDeptNm(output.getText(SitePropertyManager.getString("SESSION_DEPT_NM")));
					setHpNo(output.getText(SitePropertyManager.getString("SESSION_HP_NO")));
					setDutyCd(output.getText(SitePropertyManager.getString("SESSION_DUTY_CD")));
					setDutyNm(output.getText(SitePropertyManager.getString("SESSION_DUTY_NM")));
					setPostnCd(output.getText(SitePropertyManager.getString("SESSION_POSTN_CD")));
					setPostnNm(output.getText(SitePropertyManager.getString("SESSION_POSTN_NM")));
					setCmpnyEmpCd(output.getText(SitePropertyManager.getString("SESSION_CMPNY_EMP_CD")));
					setCmpnyNo(output.getText(SitePropertyManager.getString("SESSION_CMPNY_NO")));					
					
					
					//remember me 체크 쿠키지정 // 암호화된 값을 넘겨주고 스크립트에서 쿠키에 저장
					String cid = StringUtil.nvl((String)request.getParameter(SitePropertyManager.getString("REMEMBER_ME")));
					if(cid.equals(""))
					{
					    //removeCookie(request,response);
					}
					else
					{
					    String[] cookieValue = {getCmpnyNo(), user_id };
					    //setCookie(request,response,cookieValue);
					    output.put(SitePropertyManager.getString("COOKIE_USER_ID_KEY"),getCookieText(cookieValue));
					}
					
					output.fixNull();
					setDataSet(output);
					
					
					SessionManager sessionManager = SessionManager.getInstance();
					sessionManager.put(request.getSession().getId(), this);
					
					
					return ENABLE;
				} else
				{
					return UNREGISTED;					
				}
			}
			catch (Exception ee)
			{				
				if(ee instanceof ResourceException)
				{					
					throw ee;					
				}
				
				Logger.err.println("io-schema로부터 비교시 err"+Utility.getStackTrace(ee));
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

	// 쿠키를 삭제한다.  현재 reponse 객체가 동작을 하지 않아 사용안함
	private void removeCookie(HttpServletRequest request, HttpServletResponse response)
	{
	    Cookie[] cookies = request.getCookies();
	    int cookiesCnt = cookies != null ? cookies.length : 0;
	    String key = SitePropertyManager.getString("COOKIE_USER_ID_KEY");
	    for (int i = 0; i < cookiesCnt; i++) 
	    {
	        if(key.equals(cookies[i]))
	        {
    	        cookies[i].setMaxAge(0);
    	        cookies[i].setPath(SitePropertyManager.getString("COOKIE_USER_ID_PATH"));
    	        response.addCookie(cookies[i]);
	        }
	    }
	}

	// 쿠키를 설정.  현재 reponse 객체가 동작을 하지 않아 사용안함
	private void setCookie(HttpServletRequest request, HttpServletResponse response, String[] cookieValue)
	{
	    try
	    {
    	   String key = SitePropertyManager.getString("COOKIE_USER_ID_KEY");
	       String value = cookieValue[0] +"^"+cookieValue[1];
	       
	        byte[] encryptValue = CipherUtil.encode(key.getBytes(), value.getBytes()); 

	        Cookie cookie = new Cookie(key, CipherUtil.hexToString(encryptValue));
	        
	        int expired_day = Integer.parseInt(SitePropertyManager.getString("COOKIE_USER_ID_EXPIRED_DAY"));
	        
	        cookie.setMaxAge(expired_day*24*60*60);    
	        cookie.setPath(SitePropertyManager.getString("COOKIE_USER_ID_PATH"));
	       // System.out.println("FileAuthUser.setCookie() :  expired_day : "+ expired_day+"\nkey : "+key+"\n value : "+value+"\n value:"+CipherUtil.hexToString(encryptValue));
	        request.getSession().setAttribute(DefaultAuthUser.SESSION_KEY, this);      
	        response.addCookie(cookie);   
	    }catch(Exception ex) { }
	}

	//쿠키를 설정하기 전에 암호화된 값을 불러온다.
	private String getCookieText(String[] cookieValue)
	{
        String key = SitePropertyManager.getString("COOKIE_USER_ID_KEY");
        String value = cookieValue[0] +"^"+cookieValue[1];
        
         byte[] encryptValue = CipherUtil.encode(key.getBytes(), value.getBytes());
         return CipherUtil.hexToString(encryptValue);
	}
	
	
	
	public void logout(HttpServletRequest request, HttpServletResponse response)
	{
		request.getSession().removeAttribute(DefaultAuthUser.SESSION_KEY);
		try
		{
			request.getSession().invalidate();
		} catch(Exception ex)
		{}
		
		
		
	}

	private static String getRequestUrl(HttpServletRequest request)
	{
		
		StringBuffer url = RequestURL.getUrl(request);
		

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
		Message msg= new MessageBox();
		
		AclInfo acl = AclInfo.getAclInfoInstance();
		User usersession = findUser(request);
		PrintWriter out =  null;
		try
		{
			if(menuitem.hasAuthLevel(usersession))
			{				
				return;
			}
		}
		catch (PermissionException permissionexception)
		{
			throw permissionexception;
		}
		catch (Exception exception)
		{
			return;
		}
		//System.out.println("checkPrivilege==>"+usersession.isLogin());
		if (!usersession.isLogin())
		{
			/*SmartCookie sCookie = new SmartCookie(request, response);
			String authToken = (String)sCookie.getAttribute(IBlugContext.COOKIE_AUTHKEY);
			
			if(authToken != null && !"".equals(authToken))
			{
				try
				{		
					request.setAttribute("token", authToken);
					login(request, response);
					response.sendRedirect(getRequestUrl(request));
				} catch(PermissionException pex)
				{
					throw pex;
				} catch(Exception ex)
				{
					
				}
			}*/
			
			
			Logger.debug.println("NotLoginException");
			throw new NotLoginException(msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0003"));
		} else
		{
			Logger.debug.println("PermissionException");
			throw new PermissionException(NO_PERMISSION, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0002"));
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
        catch (ResourceException re)
        {
        	Logger.warn.println(LOG_ID+re.toString());
        }
        catch (Exception e)
        {
        	Logger.warn.println(LOG_ID+e.toString());
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (ResourceException ee)
                {}
            }
        }

        return output;
    }

}