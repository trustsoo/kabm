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
 	
	
%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/templets/mobile/_templet_header.jsp" %>
<style>
.main_container .x_panel2 {
	position: relative;
    width: 100%;
    margin-bottom: 10px;
    display: inline-block;
    background: #fff;
    border: 1px solid #E6E9ED;
    -webkit-column-break-inside: avoid;
    -moz-column-break-inside: avoid;
    column-break-inside: avoid;
    opacity: 1;
    transition: all .2s ease;
}
.main_container .x_panel2 .x_title2 {
	background-color: #34495e;
	padding: 4px 5px 2px;
	color: #fff;
	font-weight:bold;
}
</style>
</head>
<body class="nav-md" style="background: #ffffff;">
<div class="container body">
  <div class="main_container">    
    <div class="x_panel2">
		<div class="x_title2">
			<h2 id="mobileTitle"><menu:param attr="name"/></h2>			
			<div class="clearfix"></div>
		</div>
		<div class="x_content">
			<layout:body>body</layout:body>
		</div>
	</div>	
	
  </div>
</div>

</body>
</html>
