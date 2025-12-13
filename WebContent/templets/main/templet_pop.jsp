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
<html lang="ko">
<head>
<%@ include file="/templets/main/_templet_header.jsp" %>
<link href="/static/main/css/sub.css?_dummy=<%=_dummy %>" type="text/css" rel="stylesheet"  media="screen" />
<link href="/static/main/css/popup.css?_dummy=<%=_dummy %>" type="text/css" rel="stylesheet"  media="screen" />
</head>
	<body>
		<div class="wrap">
		
			<layout:body>body</layout:body>
		</div>
		
		<div id="overlay_t">
</div>
<div id="layer_pop" style="display:none;">
	<div class="pop_cont" style="opacity:1;">			
		<div style="font-size: 1.6em;font-weight:bold;color:#ffffff;background-color:#3b85c3;height: 2.25em;padding-top: 20px;padding-left: 10px;">알림</div>
		<div style="font-size: 1.2em;font-weight:bold;line-height:1.25em;min-height:8.25em;background-color:#ffffff;padding: 10px;" id="msg_cont"></div>
		<a href="#" class="btn_close" style="display:block; font-weight:bold; font-size: 1.4em;color:#FFFFFF; padding:3px; text-align:right;">X close</a>
	</div>				
</div>

<div id="confirm_pop" style="display:none;">
	<div class="pop_cont" style="opacity:1;">			
		<div style="font-size: 1.6em;font-weight:bold;color:#ffffff;background-color:#3b85c3;height: 2.25em;padding-top: 20px;padding-left: 10px;" id="confirm_title">알림</div>
		<div style="font-size: 1.2em;font-weight:bold;line-height:1.25em;min-height:8.25em;background-color:#ffffff;padding: 10px;">
			<div id="confirm_cont"></div>
			<div class="LbtnWrap" style="text-align: center;margin:10px;">
				<a href="#"><span class="confirmOk">확인</span></a>
				<a href="#"><span class="confirmNo">취소</span></a>
			</div>
		</div>
	</div>				
</div>
	</body>
</html>
