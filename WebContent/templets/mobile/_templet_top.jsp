<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/tld/menu" prefix="menu"%>
<%@ taglib uri="/tld/layout" prefix="layout"%>	
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>

<script type="text/javascript" class="source">

function openPassword()
{
	location.href="/mobile/main.jspx?cmd=passModifyView";
	//window.open("/mobile/main.jspx?cmd=passModifyView", "pass", "width=300,height=200,toolbar=no,scroll=no,menubar=no");
}

</script>
<!-- top navigation -->
<div class="top_nav">
  <div class="nav_menu">
    <nav>
      <div class="nav toggle">
        <a id="menu_toggle"><i class="fa fa-bars"></i></a>
      </div>
	
      <ul class="nav navbar-nav navbar-right" style="width:auto;">
      	
      	<li class="">         
          <a href="javascript:;" class="user-profile dropdown-toggle profile_thumb" style="padding: 8px 13px;border-color:#5a738e;" data-toggle="dropdown" aria-expanded="false">
            <i class="fa fa-user blue" style="font-size: 30px;color:#5a738e;"></i> 
          </a>
          
          <ul class="dropdown-menu dropdown-usermenu pull-right">
            <li style="background-color:#2a3f54;font-weight:blod;font-size:14px;padding:3px 0px;"><a href="/mobile/member/member.jspx?cmd=memModifyView" style="color:#ffffff;">정보변경</a></li>
            <li><a href="/mobile/logout.jsp"><i class="fa fa-sign-out pull-right"></i> Log Out</a></li>
            <li><a href="javascript:openPassword();"><i class="fa fa-key pull-right"></i> 비밀번호변경</a></li>
          </ul>
        </li>
        
        <li class="mobile_title">        	
			<%=__templetUserNm %>
		</li>
      </ul>
    </nav>
  </div>
</div>
<!-- /top navigation -->
