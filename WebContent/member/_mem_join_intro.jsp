<%
/********************************************************************************
 * Program ID	: 멤버정보 작성
 * FileName		:
 * @version		: 1.0
 *  Comment		:
 ********************************************************************************/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ include file="/common/common.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" class="source">


</script>

</head>

<body>

<form name="frm" id="frm" method="post" action="" >
<input type=hidden name="mem_div" id="mem_div" value="">
	<div class="pageTit">
		<h2><span class="tit_1">회원가입</span></h2>
	</div>
	<div id="content">
		  
    
		<div class="section-1">
			<div class="stit cen" style="font-family: 'nanum_square' !important; font-weight: 600;font-size: 28px;">
				※교육생등록과 홈페이지 회원가입은 별개이며 아이디 연동은 불가합니다.				
			</div>
			<ul class="joinTypeChice">
				<li style="height: 200px;">
					<div class="">
						<p style="height: 60px;">
							온라인 교육 신청 및 수강을 위해 가입합니다..<br />
							<strong><font style="color:red;font-weight:bold;font-size:20px;">교육수강</font>을 원하시면 클릭</strong>
						</p>
					</div>
					<div class="btnWrap">
						<a href="/member/join.jspx?cmd=join_step_1" class="pbtn06 greenChk"><span class="">교육생등록</span></a>
					</div>
					
				</li>
				<li style="height: 200px;">
					<div class="">
						<p style="height: 60px;">
							홈페이지 정보를 조회하거나 게시물 등록을 위해 가입합니다.<br />
							<strong><font style="color:red;font-weight:bold;font-size:20px;">홈페이지 정보</font>을 원하시면 클릭</strong>
						</p>
					</div>
					<div class="btnWrap">
						<a href="/member/signUp.jspx" class="pbtn06 blueChk"><span class="">회원가입</span></a>
					</div>
				</li>
			</ul>
		</div>
		<div style="display:block;text-align:center;font-size:16px;color:red;font-weight:bold;">*위 버튼 클릭이 안되시면 "알림마당 > 자주묻는질문" 에서 꼭 확인하시기 바랍니다. ( <a href="/board/board.jspx?cmd=faq_list&faq_no=37"> 바로가기 클릭 </a> )</div>
	</div>
</form>


</body>
</html>