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
	var url = '/member/join.jspx?cmd=join_step_4';
	jQuery('#frm').attr('action', url);
	jQuery('#frm').submit();
	
}

</script>
<style>
.guide strong {
    display: block;
    height: 25px;
    font-size: 15px;
    padding: 4px 0 0 25px;
    line-height: 25px;
    color: #1e90f1;
    background: url(/static/main/img/sub/bul_notice.jpg) no-repeat 0 50%;
}
</style>
</head>

<body>

<form name="frm" id="frm" method="post" action="">
<input type=hidden name="mem_div" id="mem_div" value="<%=mem_div%>">
	<div class="pageTit">
		<h2><span class="tit_1">회원가입</span></h2>
	</div>
	<div id="content">
			<ul class="joinTab">				
			<% if( MEM_DIV_NEW.equals(mem_div)){ %>
			<!-- 신규교육 스탭 -->
				<li><img src="/static/main/img/sub/join_tab_1_2.jpg"  alt="신규교육" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_2_1_off.jpg"  alt="약관동의" /></li>
			<%}%>
			<% if( MEM_DIV_REP.equals(mem_div)){ %>
			<!-- 책임자교육  스탭 -->
				<li><img src="/static/main/img/sub/join_tab_1_4.jpg"  alt="책임자교육" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_2_2_off.jpg"  alt="약관동의" /></li>
			<%}%>
				<li class="on"><img src="/static/main/img/sub/join_tab_3_on.jpg"  alt="교육동의" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_4_off.jpg"  alt="본인확인" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_5_off.jpg"  alt="기본정보입력" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_6_off.jpg"  alt="회사정보입력" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_7_off.jpg"  alt="등록확인" /></li>
			</ul>
			

			<div class="section-1">
				<div class="tit_pin">
					<span class=""> 교육신청 전 필독사항</span>
				</div>
				<dl class="guide">
					<dt> &nbsp;&nbsp;교육 신청전 반드시 알아야할 사항</dt>
					<dd>
					<strong>온라인교육</strong>
					- 총 3시간(3과목) 수강 완료해야하며, 매 과목당 정해진 시간 미이수시에는 다음 과목 시청이 불가능합니다.<br />
					- 동영상보기 ‘시작하기’ 클릭 후 반드시 2주(14일)이내에 수강 완료해야하며, 기간이 지났을 경우 나의강의실에서 ‘기간연장’을 클릭하세요.<br />
					&nbsp;&nbsp;&nbsp;<b style="font-weight:bold;">단, 책임자교육 신청자의 경우 반드시 당해연도 12월31일까지 수강 완료하여야 합니다.</b><br />					
					- 학습내용을 얼마나 잘 이해하고 있는지 확인하기 위해 동영상 수강 중에 객관식 10문제의 퀴즈풀기가 있으며, 정답을 맞출 수 있는 횟수 제한은 없습니다.
					</dd>
					<dd>
					<strong>집합교육</strong>
					- 신청하려는 교육일시, 교육장소, 계좌번호를 정확히 확인 후 신청하시기 바랍니다.
					</dd>
					<dt>  &nbsp;&nbsp;위생교육비 환불 관련사항</dt>
					<dd>
					<strong>온라인교육</strong>
					<b style="font-weight:bold;">* 결제만 하고 동영상보기 시작하기 버튼 누르기 전 환불 요청의 경우</b><br />
					 &nbsp;&nbsp;&nbsp;- 취소 요청한 건은 당사의 환불 시스템을 통해 현금이 환불계좌로 송금됩니다.<br />
					 &nbsp;&nbsp;&nbsp;- 단, 결제확인 후 정산이후의 취소 요청한 건은 수수료를 제외하고 반환됩니다.<br />
					
					<b style="font-weight:bold;">* 결제하고 동영상보기 시작하기 버튼 누른 후 환불 요청의 경우</b><br />
					 &nbsp;&nbsp;&nbsp;- 동영상보기 강의를 일단 시작하면 교육비는 환불 불가하오니 신중히 선택하시기 바랍니다.
					</dd>
					<dd>
					<strong>집합교육</strong>
					- 무통장 입금의 경우 환불 요청시 통장사본을 팩스(02-6234-0376) 또는 이메일(kabm@kabm.org)로 송부 후 협회로 연락주시면 환불계좌로 일주일 이내에 송금됩니다.
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