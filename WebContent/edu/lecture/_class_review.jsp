<%
/********************************************************************************
 * Program ID	:  평가
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
<jsp:useBean id="class_list" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="cur_user_class_seq_no" type="java.lang.String" scope="request" />
<jsp:useBean id="user_lecture_seq_no" type="java.lang.String" scope="request" />
<%@ include file="/common/common.jsp"%>

<html>
<head>
<title>강의 다시보기</title>
<link rel="stylesheet" href="/static/lib/flowplayer-7.0.4/skin/skin.css">
<style>
.flowplayer .fp-color-play {
  fill:  #00abcd;
}
.flowplayer.is-ready .fp-player {
  background-color: #000;
}
</style>

<script type="text/javascript" src="/static/lib/flowplayer-7.0.4/flowplayer.min.js?cmd=111"></script>
<script type="text/javascript" src="/static/edu/js/common.js?cmd=111"></script>
<script type="text/javascript" class="source">

function js_goclass(user_lecture_seq_no,user_class_seq_no)
{
	location.href='/edu/lecture/classCtrl.jspx?cmd=reViewClass&user_lecture_seq_no='+ user_lecture_seq_no + '&user_class_seq_no=' + user_class_seq_no;
}

function getClassView()
{
	
	var file_path = '${input.lecturePath}/${detail.file_path}';
	fn_createFlowPlayer( file_path );
}


function fn_createFlowPlayer(_url)
{
	console.log(_url);
	var player = flowplayer("#flow_player", {
		key: '#$7a5ed64aa707c8d0ce5',
		debug : true,
		fullscreen : true,
		share : false,
	    clip: {
	        sources: [
	              { type: "video/mp4",
	                src:  "${input.domain}" + _url }
	        ]
	    }
	});
	
}

jQuery(document).ready(function(){
	
	getClassView();
	
});

</script>

</head>

<body>
<div id="popup_content">
        <!-- start :: content -->
        <!-- start :: content -->


            <div class="mypage">
                
                <div class="class_popup_tit ">
                    <strong>강좌 다시보기</strong>
                </div>
          <div class="studyBox">
              <ul class="study_menu">
              		
            <% if( class_list != null ){
            	String s_mem_div = input.getText("s_mem_div");
				for(int idx =0 ; idx< class_list.getCount("user_class_seq_no"); idx++){ 
					String user_class_seq_no = class_list.getText("user_class_seq_no",idx);
					String style = "";
					if( user_class_seq_no != null && user_class_seq_no.equals(cur_user_class_seq_no) )
						style  = "on";
			%>
				<li class="<%=style%>"  >
				<span style="cursor:pointer;" onclick="js_goclass('<%=user_lecture_seq_no%>','<%=user_class_seq_no%>');"><%=class_list.getText("title",idx) %></span>
				</li>
			<% }} %>	
                  
              </ul>
              
              
              
              <div class="study_content" id="study_content">
                  
                  
                  		<div class="movieBox" style="position:relative;float: none;display:block;padding:10px 10 10 10;" id="divMOVIE">
                      		 <div id="flow_player" style="margin-top:1px;"> </div>
						</div>
						
	
              </div>
          </div>
      </div>


  <!-- end :: content -->
<!-- end :: content -->
</div>


</body>
</html>