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

<link href="https://vjs.zencdn.net/7.8.4/video-js.css" rel="stylesheet" />
<script src="https://vjs.zencdn.net/7.8.4/video.js"></script>
<script type="text/javascript" src="/static/edu/js/common.js?cmd=111"></script>
<script type="text/javascript" class="source">

function js_goclass(user_lecture_seq_no,user_class_seq_no)
{
	location.href='/mobile/edu/class/classCtrl.jspx?cmd=reViewClass&user_lecture_seq_no='+ user_lecture_seq_no + '&user_class_seq_no=' + user_class_seq_no;
}

function getClassView()
{
	
	var file_path = '${input.lecturePath}';
	var file_name = '${detail.file_path}';
	var file_name = file_name.replace('.mp4', '');
	var fileArr = file_name.split('/');
	var fileName = fileArr[1];	
	
	fn_createVideoJS( file_path + '/' + file_name, fileName + '.m3u8' );
}

var myPlayer;

function fn_createVideoJS(file_path, fileName)
{
	var options = {
			  controls: true,
			  autoplay: false,
			  preload: 'auto',
			  nativeControlsForTouch: false,
			  nativeVideoTracks: false,
			  controlBar: {
				    progressControl: {
				      seekBar: true
				    },
				    fullscreenToggle: true
				  }
			};
	myPlayer = videojs('edu-video', options);
	var source = '${input.domain}' + file_path + '/' + fileName;
	myPlayer.src({type: 'application/x-mpegURL', src: source});
	
	myPlayer.ready(function() {
		this.addClass('video-js');
	});
	
}


jQuery(document).ready(function(){
	
	getClassView();
	
});

</script>
<style>
.video-js {
	width: 100%;
    height: 29vh;
}
</style>
</head>

<body>
<div id="content">
<!-- start :: content -->

	<div class="x_panel">
		<div class="x_title">
			<h2><i class="fa fa-align-left"></i> 강좌 다시보기</h2>
			<ul class="nav navbar-right panel_toolbox" style="min-width: 0px;">
				<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>		
				<li><a class="close-link"><i class="fa fa-close"></i></a></li>
			</ul>
			<div class="clearfix"></div>
		</div>
		<div class="x_content" id="divMOVIE">
			<video id="edu-video"> </video>		
		</div>
	</div>


	<div class="x_panel">
		<div class="x_title">
			<h2><i class="fa fa-align-left"></i> 수강과목</h2>
			<ul class="nav navbar-right panel_toolbox" style="min-width: 0px;">
				<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>		
				<li><a class="close-link"><i class="fa fa-close"></i></a></li>
			</ul>
			<div class="clearfix"></div>
		</div>
		<div class="x_content">
			<ul class="list-unstyled timeline widget">
			
				<% if( class_list != null ){
	            	for(int idx =0 ; idx< class_list.getCount("user_class_seq_no"); idx++){ 
						String user_class_seq_no = class_list.getText("user_class_seq_no",idx);
						String style = "";
						if( user_class_seq_no != null && user_class_seq_no.equals(cur_user_class_seq_no) )
							style  = "on";
				%>
					<li class="<%=style%>"  >
						<div class="block">
							<div class="block_content">
								<h2 class="title" style="cursor:pointer;" onclick="js_goclass('<%=user_lecture_seq_no%>','<%=user_class_seq_no%>');">
									<%=class_list.getText("title",idx) %>
								</h2>
							</div>
						
						</div>
					</li>
				<% }} %>			
				
			</ul>
		</div>
	</div>	


<!-- end :: content -->
</div>


</body>
</html>