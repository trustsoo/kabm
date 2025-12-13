<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/tld/menu" prefix="menu"%>
<%@ taglib uri="/tld/layout" prefix="layout"%>	

<div class="col-md-3 left_col">
          <div class="left_col scroll-view">
            <div class="navbar nav_title" style="border: 0;">
              <a href="/mng/main.jspx" class="site_title"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> <span>한국건물위생관리협회</span></a>
            </div>

            <div class="clearfix"></div>
            
<!-- sidebar menu -->
            <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
              <div class="menu_section">
              
              		<ul class="nav side-menu">					
					<menu:list depth="2">
					    <menu:accessible>
						<menu:selected>
							<li class="active"><a><i class="fa <menu:param attr="menu_icon"/>"></i> <menu:param attr="name"/> <span class="fa fa-chevron-down"></span></a>
                    		<ul class="nav child_menu" style="display: block;">
							<menu:list depth="3">
								<menu:accessible>
									<menu:selected>
										<li class="current-page"><a href="javascript:goMenuLink('<menu:param attr="popup_yn"/>','<menu:param attr="idx"/>','<menu:param attr="url"/>','<menu:param attr="auth"/>');"><menu:param attr="name"/></a></li>
									</menu:selected>
									<menu:unselected>
										<li><a href="javascript:goMenuLink('<menu:param attr="popup_yn"/>','<menu:param attr="idx"/>','<menu:param attr="url"/>','<menu:param attr="auth"/>');"><menu:param attr="name"/></a></li>
									</menu:unselected>
								</menu:accessible>
							</menu:list>
							</ul>
						</menu:selected>
						<menu:unselected>
							<li><a><i class="fa <menu:param attr="menu_icon"/>"></i> <menu:param attr="name"/> <span class="fa fa-chevron-down"></span></a>
                    		<ul class="nav child_menu">
							<menu:list depth="3">
								<menu:accessible>
									<menu:selected>
										<li><a href="javascript:goMenuLink('<menu:param attr="popup_yn"/>','<menu:param attr="idx"/>','<menu:param attr="url"/>','<menu:param attr="auth"/>');"><menu:param attr="name"/></a></li>
									</menu:selected>
									<menu:unselected>
										<li><a href="javascript:goMenuLink('<menu:param attr="popup_yn"/>','<menu:param attr="idx"/>','<menu:param attr="url"/>','<menu:param attr="auth"/>');"><menu:param attr="name"/></a></li>
									</menu:unselected>
								</menu:accessible>
							</menu:list>
							</ul>
						</menu:unselected>
						</menu:accessible>	
					</menu:list>
					</ul>
              
              
              </div>

            </div>
            <!-- /sidebar menu -->
            
            <!-- /menu footer buttons -->
            <div class="sidebar-footer hidden-small">
              <a data-toggle="tooltip" data-placement="top" title="">
                <span class="glyphicon " aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="">
                <span class="glyphicon " aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="비밀번호변경" href="/mng/main.jspx?cmd=passModifyView">
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="Logout" href="/mng/logout.jsp">
                <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
              </a>
            </div>
            <!-- /menu footer buttons -->
            
 </div>
</div>           

