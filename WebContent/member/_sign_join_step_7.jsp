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

</head>

<body>

<form name="frm" id="frm" method="post" action="" onsubmit="return false;">
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
				<li class=""><img src="/static/main/img/sub/join_tab_4_off.jpg"  alt="본인확인" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_5_off.jpg"  alt="기본정보입력" /></li>
				<li class="on"><img src="/static/main/img/sub/join_tab_7_on.jpg"  alt="등록확인" /></li>
			</ul>
			

			<div class="section-1">
				<div class="join_end">
					<div class="">
						<img src="/static/main/img/sub/join_end.jpg" width="155" height="123" alt="" />
						축하합니다.<br />
						회원가입이 성공적으로 완료되었습니다.<br />
						로그인 후 이용하시기 바랍니다.<br />
						감사합니다.
					</div>
				</div>
				<div class="btnWrap">
					<a href="/login.jsp" class="pbtn02"><span class="">로그인페이지로</span></a>
					<a href="/" class="pbtn02"><span class="">메인화면으로</span></a>
				</div>
			</div>
		</div>
</form>


</body>
</html>