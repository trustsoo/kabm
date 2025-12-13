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
	var url = '/member/signUp.jspx?cmd=join_step_2';
	jQuery('#frm').attr('action', url);
	
	jQuery('#frm').submit();
}



</script>

</head>

<body>

<form name="frm" id="frm" method="post" action="" >
<input type=hidden name="mem_div" id="mem_div" value="">
	<div class="pageTit">
		<h2><span class="tit_1">회원가입</span></h2>
	</div>
	<div id="content">
		<ul class="joinTab">
			<li><img src="/static/main/img/sub/join_newtab_1_1.jpg"  alt="회원선택" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_2_1_off.jpg"  alt="약관동의" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_4_off.jpg"  alt="본인확인" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_5_off.jpg"  alt="정보입력" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_7_off.jpg"  alt="등록확인" /></li>
		</ul>

   
    
		<div class="section-1">
			<div class="stit cen" style="font-family: 'nanum_square' !important; font-weight: 600;font-size: 30px;">
				가입하시고자 하는 대상을 선택하세요
			</div>
			<ul class="joinTypeChice">
				<li style="height: 200px;">
					<div class="stit3" style="color: #258de6;">
						개인회원
					</div>
					<div class="">
						<p style="height: 60px;">
							본인 확인을 통해 개인 아이디로 회원 가입을 합니다.<br />
							<strong>(본인확인을 위해 핸드폰이 필요합니다.)</strong>
						</p>
					</div>
					<div class="btnWrap">
						<a href="javascript:lf_requestSubmit(<%=MEM_DIV_PESN %>);" class="pbtn00 blueChk"><span class="">선택</span></a>
					</div>
					
				</li>
				<li style="height: 200px;">
					<div class="stit3" style="color: #4e8f01;">
						기업회원
					</div>
					<div class="">
						<p style="height: 60px;">
							사업자 등록번호를 이용하여 회원가입을 합니다.<br />
							<strong>(<font style="color:red;font-weight:bold;font-size:20px;">"사업자 등록번호"</font>가 필요합니다.)</strong>
						</p>
					</div>
					<div class="btnWrap">
						<a href="javascript:lf_requestSubmit(<%=MEM_DIV_CORP %>);" class="pbtn00 greenChk"><span class="">선택</span></a>
					</div>
				</li>
			</ul>
		</div>
	</div>
</form>

<div class="paging" id='listBtn' >		
			<a href="/member/join.jspx?cmd=join_step_1" class="pbtn02 mid"><span class="list">뒤로가기</span></a>		
</div>
</body>
</html>