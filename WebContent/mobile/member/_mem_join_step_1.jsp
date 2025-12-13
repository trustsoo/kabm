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

function lf_requestSubmit(div)
{
	jQuery('#mem_div').val(div);
	var url = '/mobile/member/join.jspx?cmd=join_step_2';
	jQuery('#frm').attr('action', url);
	
	jQuery('#frm').submit();
}



</script>
<style>
.stepContainer .x_panel {
	padding: 0px 0px;
	border: 1px solid #d4d4d4;
}
.stepContainer .x_panel .x_title {
	background-color: #34495e;
	padding: 4px 5px 2px;
	color: #fff;
	font-weight:bold;
}

.stepContainer .panel_toolbox>li>a:hover {
    background-color: transparent;
}

.stepContainer .panel_toolbox {
    min-width: 20px;
}

.stepContainer .x_panel hr {
    margin-top: 10px;
    margin-bottom: 10px;
    border: 0;
    border-top: 2px dotted #73879c;
}
.stepContainer .x_panel .col-md-12 {
	width:100%;
}

</style>

</head>
<body>

<div id="wizard_verticle" class="form_wizard wizard_verticle">
	<ul class="list-unstyled wizard_steps anchor">
		<li>
			<a href="#step-11" class="selected" isdone="1" rel="1">
				<span class="step_no">1</span>
			</a>
		</li>
		<li>
			<a href="#step-22" class="disabled" isdone="0" rel="2">
				<span class="step_no">2</span>
			</a>
		</li>
		<li>
			<a href="#step-33" class="disabled" isdone="0" rel="3">
				<span class="step_no">3</span>
			</a>
		</li>
		<li>
			<a href="#step-44" class="disabled" isdone="0" rel="4">
				<span class="step_no">4</span>
			</a>
		</li>
		<li>
			<a href="#step-55" class="disabled" isdone="0" rel="5">
				<span class="step_no">5</span>
			</a>
		</li>
		
		<li>
			<a href="#step-66" class="disabled" isdone="0" rel="6">
				<span class="step_no">6</span>
			</a>
		</li>
		<li>
			<a href="#step-77" class="disabled" isdone="0" rel="7">
				<span class="step_no">7</span>
			</a>
		</li>
	</ul>

	<div class="stepContainer" >
	
		<div id="step-11" class="content" style="display: block;">
			<h2 class="StepTitle">수강과목 선택</h2>
			
			<div class="x_panel">
				<div class="x_title">
					<h2>신규개설및승계자교육 </h2>
					<ul class="nav navbar-right panel_toolbox">
					<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
					</li>
					</ul>
					<div class="clearfix"></div>
				</div>
				<div class="x_content">
					<div style="text-align:left;display:block;padding-left: 10px;">
						<p style="height: 60px;">
							<span style="font-size:14px;font-weight:bold;">수강신청기간</span> <br />
							<span style="font-size:14px;font-weight:bold;">${new_start } ~ ${new_end }</span>
						</p>	
						<strong>※ 신규개설및승계자교육이란?</strong>				
						<p style="height: 100%;"><span style="font-weight:bold;color:blue;">건물위생관리업을 새로 창업(업종추가)</span>하거나<br><span style="font-weight:bold;color:blue;">지위승계(대표자변경)를 받은 대표자가 최초로</span> 받아야하는 교육</p>
						<strong>※ 신규개설및승계자교육 대상자란?</strong>
						<p style="height: 100%;"> 1.『건물위생관리업』영업신고하기 위한 대표자<br>
							2.『건물위생관리업』영업신고 이후 위생교육 미이수 대표자<br>
							3.『건물위생관리업』의 대표자가 승계된 경우 승계받은 대표자<br>
							  *대표자  승계된 경우, 신규대표자가 신규가입 하여야 함.<br>
						      <font style="color:blue;font-weight:bold;">(기존의 전 대표자 아이디 연동 불가)</font><br>
						</p>						
						<p style="height: 50px;">	
							<strong>(<font style="color:red;font-weight:bold;">"★반드시 대표자 본인"</font>이 직접 이수해야 함)<br><font style="color:red;">(회원가입자=교육생=대표자)</font></strong>
						</p>
					</div>
					
					<div class="actionBar">
						<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
						<a href="javascript:lf_requestSubmit(0);" class="btn btn-info">신규개설및승계자 선택</a>
					</div>			
				
				</div>
			</div>
			
			<div class="x_panel">
				<div class="x_title">
					<h2>공중위생관리책임자 </h2>
					<ul class="nav navbar-right panel_toolbox">
					<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
					</li>
					</ul>
					<div class="clearfix"></div>
				</div>
				<div class="x_content">
					<div style="text-align:left;display:block;padding-left: 10px;padding-bottom: 25px;">
						<p style="height: 70px;">
							<span style="font-size:14px;font-weight:bold;">수강신청기간</span> <br />
							<span style="font-size:14px;font-weight:bold;">${old_start } ~ ${old_end }</span><br />
						</p>
						<strong>※ 공중위생관리책임자교육이란?</strong>
						<p >
							『건물위생관리업』영업 신고하고 <span style="font-weight:bold;color:blue;">신규교육 수료 후 다음해부터 매년 현장책임자가 받아야하는 교육</span>(기존업체에 해당)
						</p>
						<strong>※ 공중위생관리책임자교육 대상자란?</strong>
						<p style="height: 85px;">
							1. 모든 영업장 별로 관리소장 또는 반장 등의  현장책임자<br>
							2. 영업장이 없는 경우, 대표자  또는 본사에서 지정한 관리직원<br />
							
						</p>						
					</div>
					<div class="actionBar">
						<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
						<a href="javascript:lf_requestSubmit(1);" class="btn btn-success">공중위생관리책임자 선택</a>
					</div>	
				
				</div>
			</div>	
	
	
		</div>		
	</div>
	
</div>

<form name="frm" id="frm" method="post" action="" >
<input type=hidden name="mem_div" id="mem_div" value="">

</form>

	
</body>
</html>