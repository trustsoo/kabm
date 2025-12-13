<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/common.jsp"%>
<%

boolean isHttp = request.getScheme().equals("http");
String domain = request.getServerName();  // 도메인

if(isHttp) {
	response.sendRedirect("https://"+domain+"/mobile/index.jsp");
}

    User session_user = null;
	SessionAttributes sessionAttribute = null;
	String data = null;
	try
	{
		sessionAttribute = new SessionAttributes(request); 
		session_user = (User)(sessionAttribute).getAttribute( User.SESSION_KEY );	
		
		
		if(session_user != null && session_user.isLogin())
		{			
			response.sendRedirect("/mobile/main.jspx");
		} else
		{            
		    response.sendRedirect("/mobile/login.jsp");
		}
		
	} catch(Exception ex)
	{
		//response.sendRedirect("/mobile/login.jsp");
		ex.printStackTrace();
	}
%>