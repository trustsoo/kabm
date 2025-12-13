<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="exception" type="java.lang.Exception" scope="request"/>
<jsp:useBean id="code" type="java.lang.String" scope="request"/>
<jsp:useBean id="message" type="java.lang.String" scope="request"/>
<%
	if(exception instanceof jdf.framework.view.auth.PermissionException)
	{	
		code = "-401";
	}
%>
<result>
<code><%=code%></code>
<msg><%=exception.getMessage()%></msg>
<data></data>
</result>