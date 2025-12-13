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
	var url = '/member/join.jspx?cmd=join_step_2';
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
			<li><img src="/static/main/img/sub/join_tab_1_1.jpg"  alt="수강선택" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_2_1_off.jpg"  alt="약관동의" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_3_off.jpg"  alt="교육동의" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_4_off.jpg"  alt="본인확인" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_5_off.jpg"  alt="기본정보입력" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_6_off.jpg"  alt="회사정보입력" /></li>
			<li class=""><img src="/static/main/img/sub/join_tab_7_off.jpg"  alt="등록확인" /></li>
		</ul>

		<div class="section-1">
			<div class="stit cen">
				<img src="/static/main/img/sub/join_step_tit_1.jpg" alt="수강하려는 과목을 선택해주세요." />
			</div>
			<ul class="joinTypeChice">
				<li style="height: 450px;">
					<div class="stit">
						<img src="/static/main/img/sub/join_step_tit_2.jpg" alt="신규개설 및 승계자 교육" />
					</div>
					<div style="text-align:left;display:block;padding-left: 10px;">
						<p style="height: 60px;">
							<span style="font-size:14px;font-weight:bold;">수강신청기간</span> <br />
							<span style="font-size:14px;font-weight:bold;">- ${new_start } ~ ${new_end }</span><br />
						</p>	
						<strong>※ 신규개설및승계자교육이란?</strong>				
						<p style="height: 100%;"><span style="font-weight:bold;color:blue;">건물위생관리업을 새로 창업(업종추가)</span>하거나<br><span style="font-weight:bold;color:blue;">지위승계(대표자변경)를 받은 대표자가 최초로</span> 받아야하는 교육</p><br>
						<strong>※ 신규개설및승계자교육 대상자는?</strong>
						<p style="height: 100%;"> 1.『건물위생관리업』영업신고 하고자 하는 대표자<br>
							2.『건물위생관리업』영업신고 이후 위생교육 미이수 대표자<br>
							3.『건물위생관리업』의 대표자가 승계된 경우 승계 받은 대표자<br>
							  - 대표자 승계(변경)된 경우, 변경된 대표자가 신규가입 하여야 함.<br>
						      <font style="color:blue;font-weight:bold;">- 기존의 전 대표자 아이디 연동 불가</font><br>
						</p>						
						<p style="height: 50px;">	
							<strong><font style="font-weight:bold;font-size:14px;">* 반드시 대표자 본인"</font>이 직접 이수해야 함<br><font style="color:red;">(회원가입자=대표자=교육생)</font></strong>
						</p>
					</div>
					<div class="btnWrap">
						<a href="javascript:lf_requestSubmit(0);" class="pbtn00 blueChk"><span class="">선택</span></a>
					</div>
				</li>
				<li style="height: 450px;">
					<div class="stit">
						<img src="/static/main/img/sub/join_step_tit_3.jpg" alt="공중위생관리책임자 교육" />
					</div>
					<div style="text-align:left;display:block;padding-left: 10px;padding-bottom: 25px;">
						<p style="height: 70px;">
							<span style="font-size:14px;font-weight:bold;">수강신청기간</span> <br />
							<span style="font-size:14px;font-weight:bold;">- ${old_start } ~ ${old_end }</span><br />
						</p>
						<strong>※ 공중위생관리책임자교육이란?</strong>
						<p >
							『건물위생관리업』 <span style="font-weight:bold;color:blue;">영업 최초 신고이후 신규교육 수료 후</span><br>
							<span style="font-weight:bold;color:blue;">다음해부터 매년 현장책임자가 받아야하는 교육</span><br>(기존업체에 해당)
						</p>
						<strong>※ 공중위생관리책임자교육 대상자는?</strong>
						<p style="height: 85px;">
							1. 모든 영업장 별로 관리소장 또는 반장 등의 현장책임자<br>
							2. 영업장이 없는 경우, 대표자 또는 본사에서 지정한 관리직원<br />
							
						</p>						
					</div>
					<div class="btnWrap">
						<a href="javascript:lf_requestSubmit(1);" class="pbtn00 greenChk"><span class="">선택</span></a>
					</div>
				</li>
			</ul>
		</div>
		<div style="margin-top:20px;display:block;text-align:center;font-size:16px;color:red;font-weight:bold;">
		※ 온라인교육 메뉴 외에는 회원가입하지 않으셔도 열람하실 수 있습니다.

		</div>
	</div>
</form>

<!-- <div class="paging" id='listBtn' >		
			<a href="/member/join.jspx?cmd=join_step_1" class="pbtn02 mid"><span class="list">뒤로가기</span></a>		
</div> -->
	
</body>
</html>