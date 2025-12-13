package com.kabm.util;



import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import com.kabm.filter.auth.DbmsAuthCheckerUser;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.view.auth.SessionManager;
import jdf.framework.view.auth.User;

import eu.bitwalker.useragentutils.OperatingSystem;


/*http://user-agent-utils.java.net/javadoc/*/
public class UserAgentUtil
{
	public static boolean isMobile(HttpServletRequest request)
	{
		
		OperatingSystem os = getOperatingSystem(request.getHeader("User-Agent"));			
		return os.isMobileDevice();		
			
		
	}
	
	public static String getOSName(HttpServletRequest request)
	{
		
		OperatingSystem os = getOperatingSystem(request.getHeader("User-Agent"));			
		return os.getName();		
			
		
	}
	
	public static OperatingSystem getOperatingSystem(String agent)
	{
		return OperatingSystem.parseUserAgentString(agent);
	}
	
	
	public static String getUserLanguage(HttpServletRequest request)
	{
		String lang_code = request.getHeader("accept-language") == null ? "" : request.getHeader("accept-language").toLowerCase();
		String clientLanguage = null;
		try
		{
			clientLanguage = lang_code.split(",")[0].toLowerCase();
			clientLanguage = SmartStringArray.split("-", clientLanguage)[0];
		} catch(PatternSyntaxException pex)
		{
			clientLanguage = "ko";
		} catch(Exception ex)
		{
			clientLanguage = "ko";
		}
		return clientLanguage;
	}
	
	public static void refleshAuth(String key)
	{
		
		SessionManager sessionManager = SessionManager.getInstance();
		User user = sessionManager.getBySessionID(key);
		
		String[] grpNoArray = new String[user.getDataSet().getCount("grp_no")];
		for(int idx=0; idx<user.getDataSet().getCount("grp_no"); idx++)
		{
			grpNoArray[idx] = user.getDataSet().getText("grp_no", idx);						
		}					
		
		String p_dept_no_path = user.getDataSet().getText("p_dept_no_path");
		String[] prntDeptArray = SmartStringArray.split("/", p_dept_no_path);		
		
	}
	
}