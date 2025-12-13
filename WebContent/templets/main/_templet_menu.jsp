<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/tld/menu" prefix="menu"%>
<%@ taglib uri="/tld/layout" prefix="layout"%>	
<% boolean isMobile = isMobile(request); %>
	<div class="headerWrap" style="clear: both;">
	<div class="header">
		<div class="logo">
			<a href="/index.jsp"><img src="/static/main/img/common/logo2.jpg" width="100%" alt="사단법인 한국건물위생관리협회" /></a>
		</div>
		<div class="gnbWrap" id="gnb">
			<ul class="menu">
				<%if(isMobile){ %>
				<menu:list depth="2">
				    <menu:selected>
						<li><a style="color: #00b9ed !important;" href="#"><menu:param attr="name"/></a></li>
					</menu:selected>
					<menu:unselected>
						<li><a href="#"><menu:param attr="name"/></a></li>
					</menu:unselected>
				</menu:list>
				<%}else{ %>
				<menu:list depth="2">
				    <menu:selected>
						<li><a style="color: #00b9ed !important;" href="<menu:param attr="url"/>"><menu:param attr="name"/></a></li>
					</menu:selected>
					<menu:unselected>
						<li><a href="<menu:param attr="url"/>"><menu:param attr="name"/></a></li>
					</menu:unselected>
				</menu:list>
				
				<%} %>
			</ul>
			<div class="gnbSubWrap">
				<div class="gnbSub">
					<menu:list depth="2">
					    <ul class="">
					    	<menu:list depth="3">					    		
					    		<menu:selected>
									<li><a style="color: yellow !important;" href="<menu:param attr="url"/>"><menu:param attr="name"/></a></li>
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