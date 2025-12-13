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
</head>
<body class="nav-md" style="background: #ffffff;">
<div class="container body">
  <div class="main_container">
    
    <div class="x_panel">
		<div class="x_title">
			<h2>
				<menu:list depth="2">
					<menu:selected>
						<menu:isNotExistChild>
							<h2><span class="tit_1"><menu:param attr="name"/></span></h2>
						</menu:isNotExistChild>
						<menu:list depth="3">
							<menu:selected>
								<menu:isNotExistChild>
									<h2><span class="tit_1"><menu:param attr="name"/></span></h2>
								</menu:isNotExistChild>
								<menu:list depth="4">
										<menu:selected>
											<h2><span class="tit_1"><menu:param attr="name"/></span></h2>
										</menu:selected>
								</menu:list>
							</menu:selected>
						</menu:list> 	
					</menu:selected>	
				</menu:list>
			
			</h2>			
			<div class="clearfix"></div>
		</div>
		<div class="x_content">
			<layout:body>body</layout:body>
		</div>
	</div>	
	
  </div>
</div>


<!-- 맨 아래 있어야 함  -->
<script type="text/javascript" src="/static/com/js/common.js"></script>
				
<script>	

jQuery(document).ready(function() {
				
});	
</script>

</body>
</html>
