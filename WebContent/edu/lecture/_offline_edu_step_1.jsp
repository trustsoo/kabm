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


function js_nextStep()
{
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep2';
	jQuery('#frm').attr('action', _url);
	jQuery('#frm').submit();
}


</script>

</head>

<body>

<form name="frm" id="frm" method="post" action="" >
<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type='hidden' name='cp_code' id='cp_code' value='${input.cp_code}'>
<input type='hidden' name='ji_code' id='ji_code' value='${input.ji_code}'>
<input type='hidden' name='dcm_no' id='dcm_no' value='${input.dcm_no}'>
<input type='hidden' name='next_step' id='next_step' value='2'>
	<div id="content">
		<div id="smartwizard" class="sw-main sw-theme-arrows" style="margin-bottom: 15px;">
		<ul class="nav nav-tabs step-anchor">
            <li class="nav-item active"><a href="#step-1"><strong>1 단계</strong><br><strong>개인정보 제공 동의</strong></a></li>
            <li class="nav-item done"><a href="#step-2"><strong>2 단계</strong><br><strong>신청자 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-3"><strong>3 단계</strong><br><strong>신청업체 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-4"><strong>4 단계</strong><br><strong>영업장 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-5"><strong>5 단계</strong><br><strong>책입자 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-6"><strong>6 단계</strong><br><strong>입금정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-7"><strong>7 단계</strong><br><strong>접수완료</strong></a></li>
            <li class="nav-item done"><a href="#step-8"><strong>8 단계</strong><br><strong>결제 하기</strong></a></li>
        </ul>
		</div>
		<div class="section-1">
			
			<dl>
				<dt style="font-size:20px;">
					<strong>공중위생관리법 제17조 제3항 및 같은법 시행규칙 제23조 규정과 보건복지부 위생교육실시 지침에 의한 공중위생관리책임자 위생교육에 참가를 신청합니다.</strong>
				</dt>
				<dd><br><br>
				○ 위생교육 신청과정에서 수집된 <strong>개인정보(성명, 생년월일, 휴대전화번호, 이메일주소)</strong>는 협회의 교육관련 사항 및 정보제공과 관할 시.군.구청 및 보건소에 결과보고용으로만 활용되며, 수집된 개인정보는 건물위생관리업을 폐업신고하기 전까지 이용됩니다.<br><br> 
				○ <strong>본인의 개인정보를 사단법인 한국건물위생관리협회에 제공하는데 동의합니다. <br>&nbsp; (수집에 거부할 권리가 있으며 거부시 위생교육 신청이 불가함을 알려드립니다.)</strong>
				
				</dd>
			</dl>
			
		</div>
		<div class="btnWrap">
			<a href="/edu/lecture/offlineCtrl.jspx?cmd=offlineSelect" class="pbtn01"><span class="">거부함</span></a>
			<a href="javascript:js_nextStep();" class="pbtn02"><span class="">동의함</span></a>
		</div>
	</div>
</form>

	
</body>
</html>