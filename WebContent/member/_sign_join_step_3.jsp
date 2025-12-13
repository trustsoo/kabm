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

function lf_requestSubmit()
{
	var url = '/member/signUp.jspx?cmd=join_step_4';
	jQuery('#frm').attr('action', url);
	jQuery('#frm').submit();
	
}

</script>

</head>

<body>

<form name="frm" id="frm" method="post" action="">
<input type=hidden name="mem_div" id="mem_div" value="<%=mem_div%>">
	<div class="pageTit">
		<h2><span class="tit_1">회원가입</span></h2>
	</div>
	<div id="content">
			<ul class="joinTab">
			<% if( MEM_DIV_PESN.equals(mem_div)){ %>		
			<!-- 개인회원 스탭 -->
				<li><img src="/static/main/img/sub/join_newtab_1_2.jpg"  alt="개인회원" /></li>
				<li class="on"><img src="/static/main/img/sub/join_tab_2_1_off.jpg"  alt="약관동의" /></li>
			<%}%>
			<% if( MEM_DIV_CORP.equals(mem_div)){ %>
			<!-- 법인회원  스탭 -->
				<li><img src="/static/main/img/sub/join_newtab_1_3_1.jpg"  alt="법인회원" /></li>
				<li class="on"><img src="/static/main/img/sub/join_tab_2_2_off.jpg"  alt="약관동의" /></li>
			<%}%>
				<li class=""><img src="/static/main/img/sub/join_tab_4_on.jpg"  alt="본인확인" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_5_off.jpg"  alt="정보입력" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_7_off.jpg"  alt="등록확인" /></li>
			</ul>
			

			<div class="section-1">
				<div class="tit_pin">
					<span class=""> 교육신청 전 필독사항</span>
				</div>
				<dl class="guide">
					<dt> 온라인교육 신청전 반드시 알아야할 사항</dt>
					<dd>
					- 동영상보기 ‘시작하기’클릭 후 반드시 2주(14일)만에 수강 완료하여야하며 기간이 지났을 경우 교육 재신청하여야합니다.(교육비환불 불가)<br />
					- 총6과목이며 매 과목당 평가하기가 있고, 각 과목당 제시되는 모든 문제를 풀어야 다음 강의 시청이 가능합니다.<br />
					- 동영상보기 강의를 일단 시작하면 집합교육으로 전환하고 싶을 경우에도 교육비는 환불 불가하오니 신중히 선택하시기 바랍니다.
					</dd>
					<dt>  위생교육비 환불 관련사항</dt>
					<dd>
					- 동영상 수강 전혀 하지않고 카드 결제만 한 후 환불 요청시, 수수료는 제외하고 반환됩니다. 
					</dd>
				</dl>
				<ul class="guide2">
					<li class="q"> * 교육신청전 필독사항을 숙지하셨습니까?</li>
					<li class="notice">
						필독사항을 숙지하지 않아서 발생하는 불이익에 대해서는 전적으로 교육생에게 있습니다.<br />
						이점 꼭 유의하시기 바랍니다.
					</li>
				</ul>
			</div>
			<div class="btnWrap">
				<a href="javascript:lf_requestSubmit();" class="pbtn02"><span class="">동의함</span></a>
			</div>
		</div>
</form>


</body>
</html>