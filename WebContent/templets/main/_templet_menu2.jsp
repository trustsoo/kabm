<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/tld/menu" prefix="menu"%>
<%@ taglib uri="/tld/layout" prefix="layout"%>	
<% boolean isMobile = isMobile(request); %>
	<div class="menu_div">
		<div class="menu">
			<div class="nav_div">
				<div class="nav_div_">
					<ul class="nav">
					<%if(isMobile){ %>
					<menu:list depth="2">
					    <menu:selected>
							<li class="fir_nav on"><a href="#"><menu:param attr="name"/></a></li>
						</menu:selected>
						<menu:unselected>
							<li class="fir_nav"><a href="#"><menu:param attr="name"/></a></li>
						</menu:unselected>
					</menu:list>
					<%}else{ %>
					<menu:list depth="2">
					    <menu:selected>
							<li class="fir_nav on"><a href="<menu:param attr="url"/>"><menu:param attr="name"/></a></li>
						</menu:selected>
						<menu:unselected>
							<li class="fir_nav"><a href="<menu:param attr="url"/>"><menu:param attr="name"/></a></li>
						</menu:unselected>
					</menu:list>
					
					<%} %>				
						<li class="fir_nav2 ">
							<ol>
							
							<% if(isLogin)
							   { 
									String close_yn = getUserObject(request, response).getDataSet().getText("close_yn");	
									if(close_yn != null && "Y".equals(close_yn)){
							%>
									<script>
										alert( '폐업처리된 정보입니다. 협회에 문의하세요. Tel. 02)465-5900');					
									</script>
							
							<%    } %>
								<dl><a href="/logout.jsp">로그아웃</a></dl>
								<dl><a href="/member/member.jspx?cmd=memModifyView">정보변경</a></dl>
								<dl><a href="/member/member.jspx?cmd=passModifyView">비밀번호변경</a></dl>
							<% }else{ %>
								<dl><a href="/login.jsp">로그인</a></dl>
								<dl><a href="/member/join.jspx?cmd=join_step_1">회원가입</a></dl>
							<% } %>	
							</ol>
						</li>
					</ul>
				</div>
				<div class="nav_second">
					<div class="nav_secton_div">
					<div class="nav_sub" style="display:inline-block;">
						<menu:list depth="2">
						    <ul>
						    	<menu:list depth="3">					    		
						    		<menu:selected>
										<li><a href="<menu:param attr="url"/>"><menu:param attr="name"/></a></li>
									</menu:selected>
									<menu:unselected>
										<menu:equals attr="name" value="월회비입금내역">
										<menu:accessible>
											<li><a href="<menu:param attr="url"/>"><menu:param attr="name"/></a></li>
										</menu:accessible>
										</menu:equals>
										<menu:notEquals attr="name" value="월회비입금내역">
											<li><a href="<menu:param attr="url"/>"><menu:param attr="name"/></a></li>
										</menu:notEquals>
									</menu:unselected>
						    	</menu:list>
						    </ul>
						</menu:list>
					</div>	
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<script language="javascript" type="text/javascript">
	//<![CDATA[
	
	jQuery(function(){
		gnbMenu();	
	});
	
	function goMenuPageEdu(_url , menu_auth, mem_div)
	{
		if( menu_auth == 'U' )
		{
			if(mem_div == null || mem_div == undefined || mem_div == 'undefined' || mem_div == '' || mem_div == 'null')
			{
				location.href='/login.jsp';
			}else{
				
				if(mem_div == '0' || mem_div == '1')
				{
					location.href=_url;
				}else{
					alert('요청하신 메뉴에 대한 접근 권한이 없습니다. \n교육수강을 원하시면 "회원가입"메뉴에서 회원가입하셔야 합니다.')
				}				
			} 
		}else{
			location.href=_url;
		}
		
	}
	
	//]]>
	</script>