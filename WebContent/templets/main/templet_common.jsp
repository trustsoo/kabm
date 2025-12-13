<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/tld/menu" prefix="menu"%>
<%@ taglib uri="/tld/layout" prefix="layout"%>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ page import="jdf.framework.core.data.*, jdf.framework.core.util.*"%>
<%@ page import="jdf.framework.core.*, jdf.framework.view.menu.entity.MenuItem"%>
<%
	String currentPage = "https://"+request.getServerName()+(String)request.getAttribute(jdf.framework.view.menu.MenuContext.FULL_URL);
	Object mItem = request.getAttribute(jdf.framework.view.menu.MenuContext.MENU);
	
	MenuItem __templetMENU = new MenuItem();
	if( mItem != null )
		__templetMENU = (MenuItem)request.getAttribute(jdf.framework.view.menu.MenuContext.MENU);
	
	Config conf = Configuration.lookup("/site");
	String host = conf.getString("domain");

	
	// 노드 추가됨
	User __templetUser = null;
	
	__templetUser = getUserObject(request, response);
	
	String __templetUserNm = "";
	String emp_no = "";
	
	boolean isLogin = false;
	if(__templetUser != null && ( "U".equals(__templetUser.getAuthLevel()) ||"M".equals(__templetUser.getAuthLevel()) ||"P".equals(__templetUser.getAuthLevel())  ) )
	{
		__templetUserNm = __templetUser.getName();
		emp_no = (String)__templetUser.getUser_seq_no();
		isLogin = true;
	}

	 	
%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/templets/main/_templet_header.jsp" %>
<link href="/static/main/css/sub.css?_dummy=<%=_dummy %>" type="text/css" rel="stylesheet"  media="screen" />

</head>
	<body>
	<div class="wrap">
		<%@ include file="/templets/main/_templet_top.jsp" %>
		<%@ include file="/templets/main/_templet_menu.jsp" %>
		<div class="container">
		<layout:body>body</layout:body>
		</div>
		<%@ include file="/templets/main/_templet_footer.jsp" %>
	</div>
	
	</body>
</html>
