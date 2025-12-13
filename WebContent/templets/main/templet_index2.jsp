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
<%@ include file="/templets/main/_templet_header2.jsp" %>
<script>
function fn_goNotice(brd_no, brd_mng_no)
{
	location.href='/board/board.jspx?cmd=notice_view&brd_no='+brd_no+'&brd_mng_no='+brd_mng_no;
}
</script>
</head>
	<body>
		<div class="wrap" id="wrap">
			
			<div class="header">
				<%@ include file="/templets/main/_templet_top2.jsp" %>
				
				<%@ include file="/templets/main/_templet_menu2.jsp" %>
			</div>
			
		
			<div class="container ">
			
			<layout:body>body</layout:body>
			
			</div>

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

			<%@ include file="/templets/main/_templet_footer2.jsp" %>
	
		</div>
		
<script type="text/javascript">


	$(document).ready(function(){
		
		$(".fir_nav").click(function () {
			var css = $(".nav_sub").css('display');
			if( css == 'none')
				TweenMax.to($(".nav_sub"),0.5,{opacity:1,display:'block'});
			else
				TweenMax.to($(".nav_sub"),0,{opacity:0,display:'none'});
		})
		
		$(".fir_nav").mouseenter(function () {
			TweenMax.to($(".nav_sub"),0.5,{opacity:1,display:'block'});			
		})
		
		$(".nav_sub").mouseleave(function () {
			TweenMax.to($(".nav_sub"),0,{opacity:0,display:'none'});			
		})

		var flag = false;

		$(".btn_close").bind('click',function(e) {
			e.preventDefault()
			if(flag == false){
				TweenMax.to($(".top_notice"),0.5,{height:0});
				TweenMax.to($(".top_notice_cont_div"),0,{opacity:0,display:'none'});
				$(this).html("열기")
				flag = true;
			}else{
				TweenMax.to($(".top_notice"),0.5,{height:"150px"});
				TweenMax.to($(".top_notice_cont_div"),0,{delay:0.3,opacity:1,display:'block'});
				$(this).html("X 닫기")
				flag = false
			}

		});

		$(".notice_div_nav_ul li").each(function(index, item){
			$(this).bind('click',function(e){
				e.preventDefault()
				TweenMax.to($(".notice_cont_div ul"),0,{opacity:0,display:'none'});
				$(".notice_div_nav_ul li").removeClass("notice_on");
				TweenMax.to($(".notice_cont_"+index),0.5,{opacity:1,display:'block'});
				$(this).addClass("notice_on");
			})
		})

		$(".event_div_nav_ul li").each(function(index, item){
			$(this).bind('click',function(e){
				e.preventDefault()
				TweenMax.to($(".event_cont_div ul"),0,{opacity:0,display:'none'});
				$(".event_div_nav_ul li").removeClass("event_on");
				TweenMax.to($(".event_cont_"+index),0.5,{opacity:1,display:'block'});
				$(this).addClass("event_on");
			})
		})


	})

</script>
		
</body>
</html>
