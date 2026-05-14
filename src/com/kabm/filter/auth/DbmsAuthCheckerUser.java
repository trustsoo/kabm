package com.kabm.filter.auth;

/******************************************************************************** 
 * Program ID	: DbmsAuthCheckerUser
 * FileName		: DbmsAuthCheckerUser.java
 * @author		: 조은호
 * @version		: 1.0
 * Comment		: 실제 권한 체크를 담당한다.
 *
 * Modified
 * No       Date       Author	      Comment
 * ---   ----------   ----------   -----------------------------------------------
 * 01     2009-05-03   조은호       최초작성
 ********************************************************************************/

import com.kabm.filter.JDFContext;
import com.kabm.util.NetworkUtil;
import com.kabm.util.SitePropertyManager;
import com.kabm.util.UserAgentUtil;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.DefaultConnectionFactory;
import jdf.framework.core.data.cci.Interaction;
import jdf.framework.core.http.SessionAttributes;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringUtil;
import jdf.framework.core.util.Utility;
import jdf.framework.core.util.encrypt.CipherUtil;
import jdf.framework.view.auth.*;
import jdf.framework.view.menu.entity.MenuItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;


public class DbmsAuthCheckerUser extends User
{
	private final static String LOG_ID="<t:DbmsAuthCheckerUser> ";
	private static final long serialVersionUID = 1L;
	private final static String USER_AUTH_IOSCHEMA= "/common/MenuMgr";
	
	private final static int ENABLE = 0;
	private boolean is_login;
	
	/********************************************************************
	*  Function Name     : DbmsAuthCheckerUser()                                   
	*  Description       : Default Constructor  
	/********************************************************************/
    public DbmsAuthCheckerUser()
    {
    	super("DbmsAuthCheckerUser");
		is_login = false;
    }

    /********************************************************************
	*  Function Name     : isLogin()                                   
	*  Description       : 특정메뉴에 접근가능한지 여부를 판단한다.
	*  Input Data        : javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	*                      jdf.framework.view.menu.entity.MenuItem  
	/********************************************************************/
    public void checkPrivilege(HttpServletRequest req, HttpServletResponse res, MenuItem menu)
        throws PermissionException
    {

        HttpSession session= req.getSession();
        jdf.framework.core.Message msg= new jdf.framework.core.MessageBox();
        
        User session_user = null;
        
        if (res != null)
        {
        	String[] auths = menu.getAuthLevel();  
        	String auth = null;
        	for(String temp : auths)
            {
        		auth = temp;
            	if(temp != null && "N".equals(temp))
            	{	
            		return;
            	}
            }
        	
        	session_user = (User)(new SessionAttributes(req) ).getAttribute(User.SESSION_KEY );
        	
            if (session_user == null)
            {
            	throw new NotLoginException(msg.getMessage(UserAgentUtil.getUserLanguage(req), "ACL" ,  "ACL0003"));
            }else{
            	String myAuth = session_user.getAuthLevel();
            	for(String temp : auths)
                {
            		auth = temp;
                	if(temp != null && temp.equals(myAuth))
                	{	
                		return;
                	}
                	
                	if("U".equals(myAuth) || "M".equals(myAuth))
                	{
                		if(temp != null && ( "A".equals(temp) || "S".equals(temp) ))
                    	{	
                			throw new NotLoginException(msg.getMessage(UserAgentUtil.getUserLanguage(req), "ACL" ,  "ACL0003"));
                    	}
                	}
                	
                	if("A".equals(myAuth) || "S".equals(myAuth))
                	{
                		if(temp != null && ( "U".equals(temp) || "M".equals(temp) ))
                    	{	
                			throw new NotLoginException(msg.getMessage(UserAgentUtil.getUserLanguage(req), "ACL" ,  "ACL0003"));
                    	}
                	}
                }
            	throw new PermissionException(msg.getMessage(UserAgentUtil.getUserLanguage(req), "ACL" ,  "ACL0003"));
            }
            
        }

    }

    /********************************************************************
	*  Function Name     : isLogin()                                   
	*  Description       : 로그인 여부를 판별한다.                
	*  Output Data       : boolean
	/********************************************************************/
    public boolean isLogin()
    {
        return is_login;
    }

    /********************************************************************
	*  Function Name     : login()                                   
	*  Description       : 시스템에 로긴한다.                
	*  Input Data        : javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse
	/********************************************************************/
    public void login(HttpServletRequest request, HttpServletResponse response) throws PermissionException, Exception
    {
    	AclInfo acl = AclInfo.getAclInfoInstance();
		jdf.framework.core.Message msg= new jdf.framework.core.MessageBox();
		
		int result = ENABLE;

		RoleInfo role = null;
		String rolename = request.getAttribute("role") == null ? request.getParameter("role") : (String)request.getAttribute("role");

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
			// 각 role마다 테스트하여  ENABLE이 떨어지면 통과
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
				throw new PermissionException(NO_PERMISSION, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0002"));
	
			case UNREGISTED :
				throw new PermissionException(UNREGISTED, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0000"));
	           
	        case PSSWD_UPD_DT_EXPIRER :
	        	throw new PermissionException(PSSWD_UPD_DT_EXPIRER, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0013"));
	        case LOCKED_PSSWD : 
	            throw new PermissionException(UNMATCHED_PSSWD, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0006"));
	        /*case INITIAL_PSSWD :
	        	throw new PermissionException(INITIAL_PSSWD, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0014"));*/
	        default :
	        	int pass_err_num = 0; 
	    		//-31<-30 || -31 > -99
	    		if(result < UNMATCHED_PSSWD_CNT || result > LOCKED_PSSWD)
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
	    		
	    		switch(result)
	    		{
		    		case UNMATCHED_PSSWD_CNT : 
					    String massge = msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0005");
					    massge = massge.replaceAll("#total#", SitePropertyManager.getString("PASSWD_ERR_LIMIT_COUNT") );
					    massge = massge.replaceAll("#count#", pass_err_num+"" );
					    throw new PermissionException(UNMATCHED_PSSWD_CNT, massge);		
					    
					case UNMATCHED_PSSWD :     
						throw new PermissionException(UNMATCHED_PSSWD, msg.getMessage(UserAgentUtil.getUserLanguage(request), "ACL" ,  "ACL0004"));
	    		}
	    		
	        /*
				
	        */
	        
		}
    }
    
    
    /********************************************************************
	*  Function Name     : checkAcl()                                   
	*  Description       : 사용자 login 과정          
	*  Input Data        : javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, 
	*  					   jdf.framework.view.authRoleInfo
	/********************************************************************/
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
					request.getSession().setAttribute(DbmsAuthCheckerUser.SESSION_KEY, this);					
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
			
			String user_id = request.getAttribute(SitePropertyManager.getString("SESSION_USER_ID")) == null ? request.getParameter(SitePropertyManager.getString("SESSION_USER_ID")) : (String)request.getAttribute(SitePropertyManager.getString("SESSION_USER_ID"));
			String passwd = request.getAttribute(SitePropertyManager.getString("SESSION_PASSWORD")) == null ? request.getParameter(SitePropertyManager.getString("SESSION_PASSWORD")) : (String)request.getAttribute(SitePropertyManager.getString("SESSION_PASSWORD"));
			String user_div = request.getAttribute(SitePropertyManager.getString("SESSION_USER_DIV")) == null ? request.getParameter(SitePropertyManager.getString("SESSION_USER_DIV")) : (String)request.getAttribute(SitePropertyManager.getString("SESSION_USER_DIV"));
			
			
			Connection conn = null;
			try
			{
				DataSet input = new DataSet();
				
				conn = DefaultConnectionFactory.getConnection();
				Interaction interact = conn.createInteraction();
				
				//passwd
				String encKey =  SitePropertyManager.getString("ENCRYPTION_KEY");
				input.put("cmd" , "login_default");				
				input.put("ENCRYPTION_KEY", encKey);
				input.put(SitePropertyManager.getString("SESSION_USER_ID"), user_id);				
				input.put(SitePropertyManager.getString("SESSION_PASSWORD"),passwd);
				
				DataSet output = new DataSet();
				String userid = null;
				
				try
				{
					output = interact.execute(io_schema_name, input);
					if( output.getCount(SitePropertyManager.getString("SESSION_USER_ID")) > 0)
						userid = output.getText(SitePropertyManager.getString("SESSION_USER_ID"));						
				}catch(Exception e)
				{
					Logger.debug.println(LOG_ID+"LOGIN ERROR!!"+Utility.getStackTrace(e));
				}
								
				
				// output으로 사용자 id가 튀어나오면 인증이 되었다는것임
				if(output.getCount(SitePropertyManager.getString("SESSION_USER_ID")) > 0)
				{
					 				
					is_login = true;					
                    
					request.getSession().setAttribute(DefaultAuthUser.SESSION_KEY, this);					
					
					setIp(NetworkUtil.getRemoteAddr(request));
					setAuthLevel(output.getText(SitePropertyManager.getString("SESSION_AUTH_CODE")));
					setId(userid);
					setUser_seq_no(output.getText(SitePropertyManager.getString("SESSION_USER_SEQ_NO")));
					setName(output.getText(SitePropertyManager.getString("SESSION_USER_NAME")));
					setEmail(output.getText(SitePropertyManager.getString("SESSION_USER_EMAIL")));
					setHpNo(output.getText(SitePropertyManager.getString("SESSION_HP_NO")));
					setSession(request.getSession());
					
					
					//remember me 체크 쿠키지정 // 암호화된 값을 넘겨주고 스크립트에서 쿠키에 저장
					String cid = StringUtil.nvl((String)request.getParameter(SitePropertyManager.getString("REMEMBER_ME")));
					if(cid.equals(""))
					{
					}
					else
					{
					    String[] cookieValue = {getCmpnyNo(), userid };
					    output.put(SitePropertyManager.getString("COOKIE_USER_ID_KEY"),getCookieText(cookieValue));
					}
					
					output.fixNull();
					setDataSet(output);
					
					
					SessionManager sessionManager = SessionManager.getInstance();
					
					User oldUser = sessionManager.getUserByUserID(getId());		
					if(oldUser != null)
					{	
						//oldUser.setAttribute(AgentContext.DUPLICATE_LOGIN_OLD_KEY, oldUser.getIp());
						oldUser.setAttribute(JDFContext.DUPLICATE_LOGIN_KEY, "Y");//기존에 로그인한 사람에게 알림.
						request.setAttribute(JDFContext.DUPLICATE_LOGIN_KEY, "Y");//새로 로그인한 사람에게 알림
						
					}
					sessionManager.put(request.getSession().getId(), this);
					
					
					input.put("cmd", "insert_login_hist");
					input.put("client_ip", NetworkUtil.getRemoteAddr(request));
					input.put("login_div", JDFContext.LOGIN_DIV_LOGIN);
					input.put("user_div", user_div);
					interact.execute(io_schema_name, input);
					
					return ENABLE;
					
				} else
				{
					return UNREGISTED;
				}
				
			} catch (Exception ee)
			{
				
				if(ee instanceof jdf.framework.core.data.ResourceException)
				{	
					throw ee;
				}
				
				
				Logger.err.println("io-schema로부터 비교시 err"+Utility.getStackTrace(ee));				
				return NO_PERMISSION;
			}finally
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
	
    
	/********************************************************************
	*  Function Name     : logout()                                   
	*  Description       : 사용자 logout 과정          
	*  Input Data        : javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse
	/********************************************************************/
	public void logout(HttpServletRequest request, HttpServletResponse response)
	{
		logout( AclInfo.getAclInfoInstance().getDefaultRoleInfo().getName(),  request,  response);
	}
	
	public void logout(String roleName, HttpServletRequest request, HttpServletResponse response)
    {    	
    	User session_user = (User)(new SessionAttributes(request)).getAttribute(User.SESSION_KEY);
    	
    	javax.naming.Context initCtx = null;
    	javax.naming.Context envCtx = null;
    	try
    	{
    		initCtx = new javax.naming.InitialContext();
    		envCtx = (javax.naming.Context) initCtx.lookup("java:comp/env");				
    		java.util.Hashtable<String, Long> securityStorage = (java.util.Hashtable<String, Long>)envCtx.lookup("storage/security");
    		securityStorage.remove(session_user.getUser_seq_no());
    	} catch(Exception ex)
    	{    		
    	}
    	
    	request.getSession().removeAttribute(DbmsAuthCheckerUser.SESSION_KEY);
    	request.getSession().removeAttribute("USER_ID");
    	
    	SessionManager.getInstance().remove(request.getSession().getId());
    }

    /********************************************************************
	*  Function Name     : execute()                                   
	*  Description       : BLD 실행      
	*  Input Data        : java.lang.String, jdf.framework.core.data.DataSet
	/********************************************************************/
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
        	Logger.warn.println(Utility.getStackTrace(e));
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
    
    /********************************************************************
	*  Function Name     : isReAuthPass()                                   
	*  Description       : 민감정보관련 메뉴에 접근시 민감정보인증과정을 거쳤는지 체크한다.  
	*  Input Data        : java.lang.String
	*  Output Data       : boolean
	/********************************************************************/
    private boolean isReAuthPass(String user_seq_no)    
    {
    	try
    	{
    		javax.naming.Context initCtx = new javax.naming.InitialContext();
			javax.naming.Context envCtx = (javax.naming.Context) initCtx.lookup("java:comp/env");				
			java.util.Hashtable securityStorage = (java.util.Hashtable)envCtx.lookup("storage/security");
			long expiredMilliSeconds = 0;
			try
			{
				expiredMilliSeconds = ((Long)securityStorage.get(user_seq_no)).longValue();			
			} catch(Exception ex)
			{
				expiredMilliSeconds = 0;
			}
			
			if( expiredMilliSeconds >= System.currentTimeMillis())
			{
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MINUTE, 10);
				securityStorage.put(user_seq_no, cal.getTimeInMillis());
				return true;
			} else
			{
				Logger.debug.println("<"+this.getClass().getName()+"> "+user_seq_no +" storage/security expired.");
				securityStorage.remove(user_seq_no);
			}
    	} catch(Exception ex)
    	{  
    		Logger.warn.println(Utility.getStackTrace(ex));
    	}
    	
    	return false;
    }
    
    /********************************************************************
	*  Function Name     : isReAuthPass()                                   
	*  Description       : 사용자가 접근가능한 메뉴인지 판별한다.
	*  Input Data        : javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	*  					   jdf.framework.view.menu.entity.MenuItem
	/********************************************************************/
    @SuppressWarnings("unchecked")
	public void checkPrivilegebyList(HttpServletRequest req, HttpServletResponse res, MenuItem menu)
	    throws PermissionException
	{
	
	    HttpSession session= req.getSession();
	    jdf.framework.core.Message msg= new jdf.framework.core.MessageBox();
	    String group_no= null;
	    String emp_no = null;
	    String dept_no = null;
	    
	    User session_user = null;
	    
	    if (res != null)
	    {
	    	
	    	String[] auths = menu.getAuthLevel();  
	    	String auth = null;
	    	for(String temp : auths)
	        {
	    		auth = temp;
	        	if(temp != null && "N".equals(temp))
	        	{	
	        		return;
	        	}
	        }
	
	    	session_user = (User)(new SessionAttributes(req) ).getAttribute(User.SESSION_KEY );
	    	
	        if (session_user == null)
	        {
	        	throw new NotLoginException(msg.getMessage(UserAgentUtil.getUserLanguage(req), "ACL" ,  "ACL0003"));
	        }
	        
	        group_no = session_user.getGrpNo();
	        emp_no = session_user.getUser_seq_no();
	        dept_no = session_user.getDeptNo();
	        
	        java.util.List<String> menu_id_dataset= (java.util.List<String>) session_user.getAttribute("menu_dataset");
	        
	
	        //String menu_id = menu.getIndexKey();
	        String menu_id = menu.getId();
	        
	
	        Logger.info.println("<DbmsAuthCheckerUser.checkPrivilegebyList> check menu:" + menu_id + " grp_no:" + group_no + " emp_no:" + emp_no);
	        
	        
	        if(menu_id_dataset.contains(menu_id))
	        {
	        	return;
	        }
	        
	        throw new PermissionException(NO_PERMISSION, msg.getMessage(UserAgentUtil.getUserLanguage(req), "ACL" ,  "ACL0002"));
	        
	        
	        /*for (int i= 0; i < menu_id_dataset.getCount("menu_id"); i++)
	        {
	            String mn_id= menu_id_dataset.getText("menu_id", i);
	
	        
	            if(menu_id.equals(mn_id))
	            {
	            	return;
	            }
	            	
	        }
	        throw new PermissionException(NO_PERMISSION, msg.getMessage(UserAgentUtil.getUserLanguage(req), "ACL" ,  "ACL0002"));*/
	    }
	
	}
    
    private String getCookieText(String[] cookieValue)
	{
        String key = SitePropertyManager.getString("COOKIE_USER_ID_KEY");
        String value = cookieValue[0] +"^"+cookieValue[1];
        
         byte[] encryptValue = CipherUtil.encode(key.getBytes(), value.getBytes());
         return CipherUtil.hexToString(encryptValue);
	}

}
