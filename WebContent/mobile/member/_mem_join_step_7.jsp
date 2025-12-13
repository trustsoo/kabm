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
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp" %>

<%
	String mem_div = input.getText("mem_div");

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" class="source">

</script>

<style>
.x_content .tit_pin			{text-align:center; }
.x_content .tit_pin span		{display:inline-block; font-size:16px;color:#212121; font-weight:bold; padding:3px 0 0 25px; background:url(/static/main/img/sub/icon_pin.jpg) no-repeat 0 0; }
.x_content dl.guide				{}
.x_content dl.guide dt			{height:26px; border-top:1px solid #ff0000; font-size:13px; line-height:26px; border-bottom:1px solid #ff0000; color:#ff0000; width:260px; }
.x_content dl.guide dd			{padding:10px 0; color:#484848; font-size:12px; line-height:24px; }
.x_content ul.guide2			{overflow:hidden; padding:11px 0; background:#fff; }
.x_content ul.guide2 li.q		{color:#ff0000;  text-align:center; padding:17px 0 0; }
.x_content ul.guide2 li.notice	{font-weight: bold;padding:10px 0 10px 70px; color:#1e90f1; background:url(/static/main/img/sub/icon_notice.jpg) no-repeat 25px 50%; }

</style>
</head>

<body>

<form name="frm" id="frm" method="post" action="" onsubmit="return false;">
<input type=hidden name="mem_div" id="mem_div" value="<%=mem_div%>">

<div id="wizard_verticle" class="form_wizard wizard_verticle">
	<ul class="list-unstyled wizard_steps anchor">
		<li>
			<a href="#step-11" class="selected" isdone="1" rel="1">
				<span class="step_no">1</span>
			</a>
		</li>
		<li>
			<a href="#step-22" class="selected" isdone="1" rel="2">
				<span class="step_no">2</span>
			</a>
		</li>
		<li>
			<a href="#step-33" class="selected" isdone="1" rel="3">
				<span class="step_no">3</span>
			</a>
		</li>
		<li>
			<a href="#step-44" class="selected" isdone="1" rel="4">
				<span class="step_no">4</span>
			</a>
		</li>
		<li>
			<a href="#step-55" class="selected" isdone="1" rel="5">
				<span class="step_no">5</span>
			</a>
		</li>
		
		<li>
			<a href="#step-66" class="selected" isdone="1" rel="6">
				<span class="step_no">6</span>
			</a>
		</li>
		<li>
			<a href="#step-77" class="selected" isdone="1" rel="7">
				<span class="step_no">7</span>
			</a>
		</li>
	</ul>

	<div class="stepContainer" >
	
		<div id="step-11" class="content" style="display: block;">
			<h2 class="StepTitle">가입완료</h2>
			
			<div class="x_content">
				
						
				<ul class="guide2" style="padding-left:0px;color:#1e90f1;">
					<li class="q"> <img src="/static/main/img/sub/join_end.jpg" width="155" height="123" alt="" /></li>
					<li class="notice">
						축하합니다.<br />
						회원가입이 성공적으로 완료되었습니다.<br />
						로그인 후 강의 수강 신청을 진행하시기 바랍니다.<br />
						감사합니다.
					</li>
				</ul>
				
				<div class="actionBar">
					<a href="/mobile/login.jsp" class="btn btn-default">로그인페이지로</a>
				</div>	
			</div>			
		</div>
	</div>
	
</div>

</form>


</body>
</html>