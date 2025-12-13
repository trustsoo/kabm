<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%
	String _dummy = String.valueOf( Math.random() );
%>

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
    
	<!-- common scripts -->
	<script src="/static/com/js/ko.js?_dummy=<%=_dummy %>"></script>
	
	<script type="text/javascript" src="/static/mobile/js/mobile_common.js?_=<%=_dummy %>"></script>
	
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
		#overlay_t { background-color: #000; bottom: 0; left: 0; opacity: 0.5; filter: alpha(opacity = 50); /* IE7 & 8 */ position: fixed; right: 0; top: 0; z-index: 100; display:none;}
		#layer_pop .pop_cont{position:absolute;margin:10px;z-index: 101;}
		#confirm_pop .pop_cont{position:absolute;margin:10px;z-index: 101;}
	
	</style>
	<layout:head></layout:head>

