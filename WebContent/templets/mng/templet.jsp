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
 
	
	String session_id = session.getId();
	String user_id = getUserObject(request, response).getId();
	
	// 노드 추가됨
	User __templetUser = null;
	
	__templetUser = getUserObject(request, response);
	
	String __templetUserNm = "";
	if(__templetUser != null && ( "A".equals(__templetUser.getAuthLevel()) ||"S".equals(__templetUser.getAuthLevel()) ) )
	{
		__templetUserNm = __templetUser.getName();
	}
	
	String emp_no = (String)__templetUser.getUser_seq_no();
	

	 	
%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/templets/mng/_templet_header.jsp" %>
</head>
<body class="nav-md">
<div class="container body">
  <div class="main_container">
    
<%@ include file="/templets/mng/_templet_left.jsp" %>
        

<%@ include file="/templets/mng/_templet_top.jsp" %>
    

    <!-- page content -->
    <div class="right_col" role="main">
      <div class="">
        <div class="page-title">
          <div class="title_left">
            
            <span>
			<menu:list depth="2">
				<menu:selected>
					<menu:isNotExistChild>
						<h3><menu:param attr="name"/>
					</menu:isNotExistChild>
					<menu:isExistChild>
						<h3><menu:param attr="name"/>
						
					</menu:isExistChild>
					<menu:list depth="3">
						<menu:accessible>
							<menu:selected>
								> <menu:param attr="name"/>
							</menu:selected>
						</menu:accessible>
					</menu:list>
					<menu:isExistChild>
						 </h3>
					</menu:isExistChild>
				</menu:selected>
			</menu:list>
			</span> 
          </div>              
        </div>

        <div class="clearfix"></div>

        <div class="row">
        	<layout:body>body</layout:body>
        </div>
      </div>
    </div>
    <!-- /page content -->

    <!-- footer content -->
    <footer>
      <div class="pull-right">
        	한국건물위생관리협회
      </div>
      <div class="clearfix"></div>
    </footer>
    <!-- /footer content -->
  </div>
</div>

<!-- 맨 아래 있어야 함  -->
<script type="text/javascript" src="/static/com/js/common.js"></script>
				
<script>	

jQuery(document).ready(function() {
	init_sidebar();
	//init_autosize();
			
});	
</script>

</body>
</html>
