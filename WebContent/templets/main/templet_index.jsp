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
	String __mem_div = "";
	
	boolean isLogin = false;
	if(__templetUser != null && ( "U".equals(__templetUser.getAuthLevel()) ||"M".equals(__templetUser.getAuthLevel()) ||"P".equals(__templetUser.getAuthLevel())  ) )
	{
		__templetUserNm = __templetUser.getName();
		emp_no = (String)__templetUser.getUser_seq_no();
		__mem_div = (String)__templetUser.getDataSet().getText("mem_div");
		isLogin = true;
	}
	
	 	
%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/templets/main/_templet_header.jsp" %>
</head>
	<body>
		<div class="wrap">
			
			<%@ include file="/templets/main/_templet_top.jsp" %>
		
			
			<div class="headerBnr" style="margin: 0 auto;width:1002px;">
				<div style="position:relative;height:319px;width:1002px;overflow: hidden;" id="gngThumListBox">
					<ul class="cont">
						<li><img src="/static/main/img/main/top_btn_3.jpg" width="1002px" height="319px" alt="" /></li>
						<li><img src="/static/main/img/main/top_btn_4.jpg" width="1002px" height="319px" alt="" /></li>
						<li><img src="/static/main/img/main/top_btn_2.jpg" width="1002px" height="319px" alt="" /></li>
						<li><img src="/static/main/img/main/top_btn_1.jpg" width="1002px" height="319px" alt="" /></li>
					</ul>
					<div class="tab">
						<a href="#" class="on"></a>
						<a href="#"></a>
						<a href="#"></a>
						<a href="#"></a>
					</div>
				</div>
			</div>
			<script language="javascript" type="text/javascript">
				//<![CDATA[
					var rolling_gnb_visual = new krcodeTab ({
						tab : "#gngThumListBox .tab a"	
						,content :"#gngThumListBox .cont li"	
						,addClass :  'on'						
						,eventType : "mouseover focus"				
						,effect  : 'slide'						
						,interval : 5000				
						,rolling : true							
					});
				//]]>
				</script>
			
			<%@ include file="/templets/main/_templet_menu.jsp" %>
			
			
			<layout:body>body</layout:body>
			

	
<div class="subRightBanner">
	<img src="/static/main/img/sub/edu_quick_menu_4.jpg" alt="" usemap="#sub_right_banner" />

	<map name="sub_right_banner">
		<area shape="rect" coords="5,33, 82, 100" href="#" 		target="_blank" outline="none">
		<area shape="rect" coords="5,106, 82, 174" href="/static/edu/doc/온라인교육신청방법안내_20240808.pdf" 		target="_blank" outline="none">
		<area shape="rect" coords="5,178, 82, 244" href="/static/edu/doc/책임자온라인교육신청방법(고령자버전)_20250701.pdf" 		target="_blank" outline="none">
		<area shape="rect" coords="5,250, 82, 318" href="javascript:goMenuPageEdu('/edu/lecture/lectureCtrl.jspx?cmd=certList', 'U', '<%=__mem_div %>' );" 		target="_self" outline="none">
		<area shape="rect" coords="5,324, 82, 394" href="javascript:goMenuPageEdu('/edu/payment/paymentCtrl.jspx?cmd=viewPaymentList', 'U', '<%=__mem_div %>' );" 		target="_self" outline="none">
		<area shape="rect" coords="5,400, 82, 464" href="/board/board.jspx?cmd=faq_list" 		target="_self" outline="none">
		<area shape="rect" coords="5,472, 82, 538" href="/edu/lecture/classCtrl.jspx?cmd=viewClassList" 		target="_self" outline="none">
	</map>
</div>
			<%@ include file="/templets/main/_templet_footer.jsp" %>
	
		</div>
	</body>
</html>
