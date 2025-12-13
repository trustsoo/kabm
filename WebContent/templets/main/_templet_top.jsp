<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/tld/menu" prefix="menu"%>
<%@ taglib uri="/tld/layout" prefix="layout"%>	

<style>
.topArea .search_div{
	margin-bottom: 0px !important;
    padding-bottom: 0px !important;
}
.topArea .search_div span {
    width: 40px;
    height: 40px;
    background: #347ac2;
    padding-top: 24px;
}
.topArea .search_div span svg {
    padding: 0px;
}
.topArea svg:not(:root) {
    overflow: hidden;
}
.topArea svg[Attributes Style] {
    width: 40px;
    height: 40px;
}

.topArea .header_logo_div2 .search_div2 input {
    border: 2px solid #fff;
    vertical-align: text-bottom;
    margin-bottom: 4px;
    margin-left: 88px;
    font-family: Malgun Gothic;
	font-size: 11px;
	background-color: rgba(0, 0, 0, 0);
    opacity: 3;
    color:#fff;
    width: 200px;
}

</style>
	<div class="topArea">
		<form name="searchFrm" id="searchFrm" method="post" action="/board/board.jspx?cmd=search">
		<div class="header_div" style="float:left;">
			<div class="header_logo_div2">
				<div class="search_div2">
					<input type="text" placeholder="무엇을 찾고 계신가요?" onKeyUp="checkForSearch(event);" name="top_search_word" id="top_search_word"/>
					<a href="javascript:actionSearch()">
						<span class="svg-icon svg-icon-primary svg-icon-2x">
							<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="29px" height="29px" viewBox="0 0 29 22" version="1.1">
								<g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
									<rect x="0" y="0" width="29" height="29"/>
									<path d="M14.2928932,16.7071068 C13.9023689,16.3165825 13.9023689,15.6834175 14.2928932,15.2928932 C14.6834175,14.9023689 15.3165825,14.9023689 15.7071068,15.2928932 L19.7071068,19.2928932 C20.0976311,19.6834175 20.0976311,20.3165825 19.7071068,20.7071068 C19.3165825,21.0976311 18.6834175,21.0976311 18.2928932,20.7071068 L14.2928932,16.7071068 Z" fill="#ffffff" fill-rule="nonzero" />
									<path d="M11,16 C13.7614237,16 16,13.7614237 16,11 C16,8.23857625 13.7614237,6 11,6 C8.23857625,6 6,8.23857625 6,11 C6,13.7614237 8.23857625,16 11,16 Z M11,18 C7.13400675,18 4,14.8659932 4,11 C4,7.13400675 7.13400675,4 11,4 C14.8659932,4 18,7.13400675 18,11 C18,14.8659932 14.8659932,18 11,18 Z" fill="#ffffff" fill-rule="nonzero"/>
								</g>
							</svg><!--end::Svg Icon-->
						</span>
					</a>
				</div>
			</div>
		</div>
		</form>
		<div class="btnBox" style="float:right;width: 410px;">
		
		
		
		
		<% if(isLogin)
		   { 
				String close_yn = getUserObject(request, response).getDataSet().getText("close_yn");	
				if(close_yn != null && "Y".equals(close_yn)){
		%>
				<script>
					alert( '폐업처리된 정보입니다. 협회에 문의하세요. Tel. 02)465-5900');					
				</script>
		
		<%    } %>
			<a href="/logout.jsp">로그아웃</a>
			<a href="/member/member.jspx?cmd=memModifyView">정보변경</a>
			<a href="/member/member.jspx?cmd=passModifyView">비밀번호변경</a>
		<% }else{ %>
			<a href="/login.jsp">로그인</a>
			<a href="/member/join.jspx?cmd=join_step_1">회원가입</a>			
		<% } %>	
		</div>
	</div>
	
	

<script>
function checkForSearch(event)
{
	if ((event.which && event.which == 13) || (event.keyCode && event.keyCode == 13))
	{
		actionSearch();
	}
}
function actionSearch()
{
	if($('#top_search_word').val() == ''){
		alert( '검색어를 먼저 입력하세요');
		return;
	} 
	var f = $('#searchFrm');
	f.attr('action', '/board/board.jspx?cmd=search');	
	f.submit();
}
</script>