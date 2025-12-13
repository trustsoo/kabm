<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<%@ page import="jdf.framework.core.data.InteractionBean"%>
<%@ page import="jdf.framework.core.data.DataSet"%>
<%@ include file="/common/common.jsp"%>

<%
	User session_user = null;
	SessionAttributes sessionAttribute = null;
	
	try
	{
		String user_id =   getUserObject(request,response).getId();
		InteractionBean interact = new InteractionBean();
		DataSet input = new DataSet();
		input.put("user_id", user_id);
		input.put("login_div", com.kabm.filter.JDFContext.LOGIN_DIV_LOGOUT);
		input.put("cmd", "insert_login_hist");
		interact.execute("/user/Login", input);
	} catch(Exception ex)
	{
		
	}
	
	try
	{	
		sessionAttribute = new SessionAttributes(request); 
		session_user = (User)(sessionAttribute).getAttribute( User.SESSION_KEY );		
		session_user.logout(request, response);
	} catch(Exception ex)
	{}
	request.getSession().invalidate();
	
	response.sendRedirect("/mobile/login.jsp");
%>
