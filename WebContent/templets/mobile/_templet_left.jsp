<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/tld/menu" prefix="menu"%>
<%@ taglib uri="/tld/layout" prefix="layout"%>	

<div class="col-md-3 left_col">
          <div class="left_col scroll-view">
            <div class="navbar nav_title" style="border: 0;">
              <a href="/mobile/index.jsp" class="site_title"><i class="glyphicon glyphicon-home" aria-hidden="true"></i></a>
            </div>

            <div class="clearfix"></div>
            
<!-- sidebar menu -->
            <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
              <div class="menu_section">
              
              		<ul class="nav side-menu">					
					<menu:list depth="2">
					    <menu:accessible>
						<menu:selected>
							<li class="active"><a ><i class="fa <menu:param attr="menu_icon"/>"></i> <menu:param attr="name"/> <span class="fa fa-chevron-down"></span></a>
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
							<li><a ><i class="fa <menu:param attr="menu_icon"/>"></i> <menu:param attr="name"/> <span class="fa fa-chevron-down"></span></a>
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
            
 </div>
<div style="position: fixed;bottom: 0px;text-align: center;display: block;width: 70px;height: 34px;background-color: #17232f;padding-top: 10px;">
<a href="/index.jsp" style="font-size: 10px;font-weight: bold;color: #fff;">PC화면보기</a>
</div>
</div>           

