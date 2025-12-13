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
<%
	String _dummy = String.valueOf( Math.random() );
%>
<html>
<head>
	<meta charset="utf-8"/>
	<META HTTP-EQUIV="Expires" CONTENT="-1">
	<META HTTP-EQUIV="pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
	<meta name="description" content="사단법인 한국건물위생관리협회">
	<title>사단법인 한국건물위생관리협회</title>
	
	<!--inline styles related to this page-->
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!--basic styles-->
	
	<link href="/static/lib/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" />
	<link href="/static/lib/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet" />
	<link href="/static/lib/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="/static/lib/iCheck/skins/flat/green.css" />
	<link rel="stylesheet" href="/static/lib/animate.css/animate.min.css" />
	<link rel="stylesheet" href="/static/lib/nprogress/nprogress.css" />
	<!-- PNotify -->
    <link href="/static/lib/pnotify/dist/pnotify.css" rel="stylesheet">
    <link href="/static/lib/pnotify/dist/pnotify.buttons.css" rel="stylesheet">
    <link href="/static/lib/pnotify/dist/pnotify.nonblock.css" rel="stylesheet">
    <link href="/static/lib/jqgrid/css/ui.jqgrid.css" rel="stylesheet" />
    
	<!-- page specific plugin styles -->
	<link rel="stylesheet" href="/static/lib/bootstrap-datepicker/datepicker.css?_dummy=<%=_dummy %>" />
	<link rel="stylesheet" href="/static/lib/bootstrap-daterangepicker/daterangepicker.css" />

	
	<!-- page styles -->
	<link rel="stylesheet" href="/static/com/css/templetDefault.css?_dummy=<%=_dummy %>" />
	<link rel="stylesheet" href="/static/mobile/css/mobileDefault.css?_dummy=<%=_dummy %>" />

	<script type="text/javascript">
		window.jQuery || document.write("<script src='/static/lib/jquery/dist/jquery.min.js'>"+"<"+"/script>");
	</script>
	
	<script type="text/javascript" src="/static/lib/bootstrap/dist/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/static/lib/bootstrap-datepicker/bootstrap-datepicker.js"></script>
	<!-- PNotify -->
    <script src="/static/lib/pnotify/dist/pnotify.js"></script>
    <script src="/static/lib/pnotify/dist/pnotify.buttons.js"></script>
    <script src="/static/lib/pnotify/dist/pnotify.nonblock.js"></script>
    <!-- FastClick -->
    <script src="/static/lib/fastclick/lib/fastclick.js"></script>
    <!-- NProgress -->
    <script src="/static/lib/nprogress/nprogress.js"></script>
    <!-- iCheck -->
    <script src="/static/lib/iCheck/icheck.min.js"></script>
	
	<script type="text/javascript" src="/static/com/js/notification.js"></script>
	
	<!-- moment -->
    <script src="/static/lib/moment/min/moment-with-locales.min.js"></script>
    
	<!-- jqGrid scripts -->
	<script type="text/javascript" src="/static/lib/jqgrid/js/jquery.jqGrid.js"></script>
	<script type="text/javascript" src="/static/lib/jqgrid/js/i18n/grid.locale-kr.js"></script>
	<script type="text/javascript" src="/static/lib/jqgrid/plugins/jquery.contextmenu.js"></script>
	
	<!-- local scripts -->
	<script src="/static/com/js/ko.js"></script>

	<!-- common scripts -->
	<script src="/static/mng/js/mng_common.js"></script>

	
	
	<!-- 노드 추가 -->
	<script type="text/javascript" id="code">		
		jQuery.ajaxSetup({cache:false});

		var loader_opts = {
				lines: 13, // The number of lines to draw
				length: 11, // The length of each line
				width: 5, // The line thickness
				radius: 17, // The radius of the inner circle
				corners: 1, // Corner roundness (0..1)
				rotate: 0, // The rotation offset
				color: '#FFF', // #rgb or #rrggbb
				speed: 1, // Rounds per second
				trail: 60, // Afterglow percentage
				shadow: false, // Whether to render a shadow
				hwaccel: false, // Whether to use hardware acceleration
				className: 'spinner', // The CSS class to assign to the spinner
				zIndex: 2e9, // The z-index (defaults to 2000000000)
				top: 'auto', // Top position relative to parent in px
				left: 'auto' // Left position relative to parent in px
			};
		var loader_obj = null;
		function startLoader()
		{
			if(loader_obj != null) endLoader();
			var target = document.createElement("div");
			document.body.appendChild(target);
			var spinner = new Spinner(loader_opts).spin(target);
			var overlay = iosOverlay({
				text: "Loading",
				spinner: spinner
			});	
			
			loader_obj = overlay;
		}
		
		function endLoader()
		{
			try{
				if(loader_obj != null) loader_obj.hide();
				loader_obj = null;
			}catch(e){}
		}
		function toggleLoader(div)
		{
			if( div != null && div == 1) startLoader();
			else endLoader();
		}
		
		function confirmOpen(title, cont , _callback){
			$("#confirm_title").html(title);
			$("#confirm_cont").html(cont);
			
			$('#overlay_t').show(); 
			$("#confirm_pop").css("display","block");
			var tx = ( jQuery(window).width() - $("#confirm_pop .pop_cont").width() ) / 2+jQuery(window).scrollLeft();
			var ty = jQuery(window).scrollTop();
			$("#confirm_pop .pop_cont").css({left:tx+"px",top:ty+"px"});
			$("body").css("overflow","hidden");
			$(".confirmOk").click(_callback);
		}
	</script>


<style>
.flowplayer .fp-color-play {
  fill:  #00abcd;
}
.flowplayer.is-ready .fp-player {
  background-color: #000;
}
</style>


<script type="text/javascript" src="/static/edu/js/common.js?cmd=111"></script>
<script type="text/javascript" class="source">

function js_goclass(user_lecture_seq_no,user_class_seq_no)
{
	location.href='/mobile/edu/class/classCtrl.jspx?cmd=testClass&user_lecture_seq_no='+ user_lecture_seq_no + '&user_class_seq_no=' + user_class_seq_no;
}

function getClassView()
{
	
	var file_path = '${input.lecturePath}';
	var file_name = '${detail.file_path}';
	var file_name = file_name.replace('.mp4', '');
	var fileArr = file_name.split('/');
	var fileName = fileArr[1];
	
	console.log("file_path:",file_path);
	console.log("file_name:",file_name);
	console.log("fileName:",fileName);
	
	fn_createVideoJS( file_path + '/' + file_name, fileName + '.m3u8' );
}
var isInit = true;
var currentTime = 200;
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
	//source = '/repository/lecture/10001/2020_1_1/2020_1_1.m3u8';
	//source = 'http://220.76.68.212/ekabm/lecture/10001/2020_1_1/2020_1_1.m3u8';
	
	myPlayer.src({type: 'application/x-mpegURL', src: source});
	
	myPlayer.ready(function() {
		this.addClass('video-js');
		//this.currentTime(currentTime);
		this.controlBar.progressControl.disable();
		
	});
	
	myPlayer.on("play", function(event) {
		console.log('=================>play');
		
		if(isInit)
			this.currentTime(currentTime);
		
		isInit = false;
	});
	
	myPlayer.on("loadedmetadata", function(event) {
		console.log('=================>loadedmetadata');
		this.currentTime(currentTime);
		
	});
	
	
        
    myPlayer.on("seeking", function(event) {
      if (currentTime < myPlayer.currentTime()) {
        myPlayer.currentTime(currentTime);
      }
    });

    myPlayer.on("seeked", function(event) {
      if (currentTime < myPlayer.currentTime()) {
        myPlayer.currentTime(currentTime);
      }
    });

    setInterval(function() {
      if (!myPlayer.paused()) {
        currentTime = myPlayer.currentTime();
      }
    }, 1000);
	
    myPlayer.on('timeupdate', function() {
		var currTime = this.currentTime();
		var remain = this.remainingTime();
		$('#playTime').html(currTime + ":" + remain );
		
	});
     
    myPlayer.on('ended', function () {
		
	});
	
}

function js_playControl(payload)
{
	if(payload == 'start' )
		myPlayer.play();
	else if(payload == 'stop' )
		myPlayer.pause();
	else if(payload == 'fullscreen' ){
		if(payload.isFullscreen()) myPlayer.isFullscreen(false);
		else myPlayer.isFullscreen(true);
	}else
		myPlayer.pause();
}
jQuery(document).ready(function(){
	
	getClassView();
	
});

</script>
<style>
	#overlay_t { background-color: #000; bottom: 0; left: 0; opacity: 0.5; filter: alpha(opacity = 50); /* IE7 & 8 */ position: fixed; right: 0; top: 0; z-index: 100; display:none;}
	#layer_pop .pop_cont{position:absolute;margin:10px;z-index: 101;}
	#confirm_pop .pop_cont{position:absolute;margin:10px;z-index: 101;}
.video-js{
width: 100%;
    height: 29vh;
}
</style>

<link href="https://vjs.zencdn.net/7.8.4/video-js.css" rel="stylesheet" />
<link
  href="https://unpkg.com/@videojs/themes@1/dist/city/index.css"
  rel="stylesheet"
/>
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
			
			<video id="edu-video" controls="false" controlsList="nodownload"></video>
		</div>
		<div class="btnWrap" style="" >
			<a href="#" class="btn btn-sm btn-primary" onclick="js_playControl('start');"><span class="">재생시작</span></a>
			<a href="#" class="btn btn-sm btn-primary" onclick="js_playControl('stop');"><span class="">재생종료</span></a>
			<a href="#" class="btn btn-sm btn-primary" onclick="js_playControl('fullscreen');"><span class="">전체화면보기</span></a>
			<a href="#" class="btn btn-sm btn-primary"><span id="playTime" class="">00:00:00</span></a>
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
<script src="https://vjs.zencdn.net/7.8.4/video.js"></script>
<script src="/static/lib/videosj/videojs.disableProgress.js" type="text/javascript" ></script>
	

</body>
</html>