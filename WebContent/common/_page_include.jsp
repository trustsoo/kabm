<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<%
	String view_url = input.getText("view_url");	
%>

<%
	if("".equals(view_url))
	{
%>	
<div id="div_convert_page">
<%
	}
%>
	<jsp:include page="<%= view_url %>" flush="false" />
<%
	if("".equals(view_url))
	{
%>		
</div>
<%
	}
%>
	