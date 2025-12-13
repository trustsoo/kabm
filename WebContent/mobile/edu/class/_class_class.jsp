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
<jsp:useBean id="cls_list" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="class_list" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="cur_user_class_seq_no" type="java.lang.String" scope="request" />
<jsp:useBean id="user_lecture_seq_no" type="java.lang.String" scope="request" />
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp"%>

<html>
<head>
<title>강의 수강</title>
<link href="https://vjs.zencdn.net/7.8.4/video-js.css" rel="stylesheet" />
<script src="https://vjs.zencdn.net/7.8.4/video.js"></script>
<script src="/static/lib/videosj/videojs.disableProgress.js" type="text/javascript" ></script>
<script type="text/javascript" src="/static/edu/js/common.js?cmd=111"></script>
<script type="text/javascript" class="source">

var estimate_ing = false;
var curr_examno = ${curr_examno};

var exam_open_time = [${exam_open_time}];

var exam_clear_status = [${exam_clear_status}];

var curr_play_position = 0;

var currentTime = 0;
var isInit = true;
var myPlayer;
var quizModal = 1;
var isSeeked = false;
function fn_createVideoJS(file_path, fileName)
{
	var options = {
			  controls: true,
			  fluid: true,		    
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
	myPlayer.src({type: 'application/x-mpegURL', src: source});
	//myPlayer.src({type: 'video/mp4', src: source});
	
	myPlayer.on('ready', function() {
		this.addClass('video-js');
				
		var seek_limit = 0;		
	    // do something when a video is loaded and ready to play
		 if( curr_examno > 0 ) 
			 seek_limit = exam_open_time[curr_examno -1];
		 if( seek_limit > 0 ){
			 seek_limit = (seek_limit+1) * 60;
			 curr_play_position = seek_limit;
		 }
		 currentTime = seek_limit;		 
		 //myPlayer.currentTime(seek_limit);
		 this.controlBar.progressControl.disable();
	});
	
	myPlayer.on("play", function(event) {
		if(isInit)
			myPlayer.currentTime(currentTime);
		
		isInit = false;
	});

    myPlayer.on("seeking", function(event) {
      if (!isInit && currentTime < myPlayer.currentTime()) {    	  
        myPlayer.currentTime(currentTime);
        
        $('#testBox_'+quizModal).modal('hide');
        $('#divMOVIE').html('');
        isSeeked = true;
      }
    });

    myPlayer.on("seeked", function(event) {    	
      if (!isInit && currentTime < myPlayer.currentTime()) {    	  
        myPlayer.currentTime(currentTime);
      }
    });
    
    setInterval(function() {
      if (!isSeeked && !myPlayer.paused()) {
        currentTime = myPlayer.currentTime();
      }
    }, 1000);
    
	myPlayer.on('timeupdate', function() {
		var currTime = this.currentTime();
		if(curr_play_position <= currTime)
			curr_play_position = currTime;
		fn_checkExam(curr_play_position, myPlayer);		
	});
	
	myPlayer.on('ended', function () {
		fn_finish();
	});
	
}

function getEstimateView(code ,divTabName)
{
	
	if( isSeeked || curr_examno < parseInt(code) ) 
	{
		msgStart( '<b style="color:red;">'+(exam_open_time[parseInt(code)]+1)+'</b>'+'분까지 강의를 수강해야 문제를 확인할 수 있습니다.');
		return;
	}
	
	quizModal = code;
	$('#testBox_'+code).modal();
	
}
function getClassView()
{
	if(estimate_ing == true) return;	
		
	if( '${detail.file_path}' == '' )
	{
		jQuery('#divMOVIE').html('<H5 style="padding:5px;">죄송합니다. 강의 파일 준비중입니다.</H5>');
	}else{
		
		var file_path = '${input.lecturePath}';
		var file_name = '${detail.file_path}';
		var file_name = file_name.replace('.mp4', '');
		var fileArr = file_name.split('/');
		var fileName = fileArr[1];
		
		fn_createVideoJS( file_path + '/' + file_name, fileName + '.m3u8' );
	}
}


function fn_doResult()
{
	jQuery.ajax({
		url : '/edu/estimate/action/estimateAction.jspx?cmd=setNewResult', 
		data : 'user_lecture_seq_no=<%=user_lecture_seq_no%>&user_class_seq_no=<%=cur_user_class_seq_no%>',
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			var isok = false;
			var msg = jsonObj.result.msg;
			if(jsonObj.result.code == '200'){
				
				if( msg == '03' )
				{
					alert(msg_edu_code_008);
					alert(msg_edu_code_009);
					alert('더 나은 교육시스템을 위해 여러분의 소중한 의견을 부탁드립니다.');
					
					location.href = '/edu/survey/survey.jspx?user_lecture_seq_no=<%=user_lecture_seq_no%>&lecture_seq_no=${detail.lecture_seq_no}&lecture_div=${detail.lecture_div}';
					
					try{
						//location.href = '/mobile/edu/class/classCtrl.jspx?cmd=list';
						opener.reload();
					}catch(e){}
				}else{
					alert(msg_edu_code_002);
					location.href="/mobile/edu/class/classCtrl.jspx?cmd=doClass&user_lecture_seq_no=<%=user_lecture_seq_no%>&user_class_seq_no=";
					
				}
			}else{
				msgStart( msg_com_code_010);
			}
		}
	});
	
}


function fn_doEstimate( exam_num, estimate_seq_no, exam_seq_no)
{
	var isstatus_ok = true;
	for(i=0;i< exam_num ;i++)
	{
		if( exam_clear_status[i] == 0 )
		{
			isstatus_ok = false;
			msgStart( (i+1) + '번'+msg_edu_code_005);
			break;
		}
	}
	if( !isstatus_ok ) return;
	
	var item_seq_no = jQuery(":input:radio[name=answr_ctnt_"+estimate_seq_no+"]:checked").val();
	if( item_seq_no == null || item_seq_no == undefined || item_seq_no == 'undefined')
	{
		msgStart( msg_edu_code_004);
		return;
	}
	
	jQuery.ajax({
		url : '/edu/estimate/action/estimateAction.jspx?cmd=getAnswerCheck', 
		data : 'user_class_seq_no=<%=cur_user_class_seq_no%>'+'&estimate_seq_no='+estimate_seq_no+'&exam_seq_no='+exam_seq_no+'&item_seq_no=' +item_seq_no,
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			var isok = false;
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.item_info;
				var jsonCnt = jsonObj.result.data.item_info.length;
				
				if(jsonCnt > 0){
					var check_yn  = json[0].check_yn;
					
					if( check_yn == 'Y')
					{
						isok = true;
					}
				}else{
					isok = false;
				}
			}
			
			if(isok)
			{
				exam_clear_status[exam_num] = 1;
				
				msgStart( msg_edu_code_001 );
				jQuery(":input:radio[name=answr_ctnt_"+estimate_seq_no+"]").attr('disabled','true');
				jQuery('#exam_title_' + estimate_seq_no).html('<b style="color:red;">(정답)</b>');
				jQuery('#exam_tab_'+ (exam_num+1)).removeClass('red').removeClass('glyphicon-remove-circle').addClass('glyphicon-ok-circle');
				jQuery('#edu-video').show();
				jQuery('#btn_'+estimate_seq_no).hide();
				fn_doEstimateClose( exam_num, estimate_seq_no, exam_seq_no);
				
				estimate_ing = false;
				
			}else{
				msgStart( msg_edu_code_003);
			}
		}
	});
	
}

function fn_doEstimateClose( exam_num, estimate_seq_no, exam_seq_no)
{
	$('#testBox_'+(exam_num + 1)).modal('toggle');
}

function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

/*
function fn_createFlowPlayer(_url)
{
	console.log(_url);
	var player = flowplayer("#edu-video", {
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
*/

//초계산
function getTimer(){
	
	var now = moment();
	var to = moment('${detailLecture.user_lecture_end_dt}:59' , 'YYYY.MM.DD HH:mm:ss'); 
	var from = moment('${detailLecture.user_lecture_start_dt}:00' , 'YYYY.MM.DD HH:mm:ss'); 
	var diff = moment(to,"DD/MM/YYYY HH:mm:ss").diff(moment(from,"DD/MM/YYYY HH:mm:ss"));
	//to = to.endOf('day'); 
	var ms = moment(to,"DD/MM/YYYY HH:mm:ss").diff(moment(now,"DD/MM/YYYY HH:mm:ss"));
	var md = moment.duration(ms);
	var days = ( md.asDays()|0);
	var hours = md.hours();
	var mins = md.minutes();
	var secs = md.seconds() ;
	
	if( days <= 0 ) days = '';
	else days = days +  "일 ";
	
	if( days <= 0 && hours <= 0 ) hours = '';
	else hours = hours + "시간 ";
	
	var s = days + hours + mins +"분 "+ secs + "초";
	$("#diffTime").html( s );
	//$("#nowTime").html( moment(now).format('YYYY년 MM월 DD일 HH:mm:ss') );
	var width =  Math.floor((diff-ms)*100/diff);
	$('.progress-bar').css('width',width+'%');
	setTimeout("getTimer()", 1000);
}
/** */
function fn_finish()
{
	var isstatus_ok = true;
	for(i=0;i< exam_clear_status.length ;i++)
	{
		if( exam_clear_status[i] == 0 )
		{
			isstatus_ok = false;
			msgStart( (i+1) + '번'+msg_edu_code_005);
			
			break;
		}
	}
	if( !isstatus_ok ) return;
	
	fn_doResult();
}

function fn_checkExam(time , player)
{
	var min = time/60|0;
	var str_open_time = exam_open_time[curr_examno];
	
	if(str_open_time == '' || str_open_time == undefined  ) return;
	//str_open_time = 5*(curr_examno+1);
	
	var cur_open_time = parseInt(str_open_time)+1;//소수점 반올림
	if( cur_open_time == 0 ) return;
	//cur_open_time = 5*(curr_examno+1);
	
	
	if(  curr_examno <= exam_open_time.length && exam_clear_status[curr_examno] != 1 
			&& exam_clear_status[exam_open_time.length -1 ] != 1 
			&& cur_open_time <= min && !estimate_ing)
	{
		estimate_ing = true;
		curr_examno = curr_examno + 1;
		
		player.pause();
		jQuery('#edu-video').hide();
		jQuery('#exam_a_'+curr_examno).removeClass('btn-default').addClass('btn-primary');
		jQuery('#exam_icon_'+curr_examno).removeClass('red').removeClass('glyphicon-minus-sign').addClass('glyphicon-play-circle');
		
		getEstimateView(curr_examno, 'quizCont');
		
		
	}
	
}

jQuery(document).ready(function(){
	getTimer();
	
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

<div class="x_panel">
	<div class="x_title">
		<h2><i class="fa fa-clock-o"></i> 강의진행현황</h2>
		<ul class="nav navbar-right panel_toolbox" style="min-width: 0px;">
		<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
		</li>
		
		<li><a class="close-link"><i class="fa fa-close"></i></a>
		</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
		<div id="wizard" class="form_wizard wizard_horizontal">
			<ul class="wizard_steps anchor" style="padding-left:0px;">
		       
		<% if( class_list != null && class_list.getCount("user_class_seq_no") >0 ) 
		{
			for( int idx=0; idx < class_list.getCount("user_class_seq_no") ; idx++)
			{
				String pass_yn = class_list.getText("estimate_pass_yn",idx);
				String user_class_seq_no = class_list.getText("user_class_seq_no",idx);
				String on = "";
				String cls = "disabled";
				if( pass_yn != null && "Y".equals(pass_yn) )
				{
					cls = "selected";
				}else if( user_class_seq_no != null && user_class_seq_no.equals(cur_user_class_seq_no) )
				{
					cls = "selected";
					on = "on";
				}else{
					cls = "disabled";
				}	
		%>
				<li>
					<a href="#step-<%=(idx+1) %>" class="<%=cls %>" isdone="<%=(idx+1) %>" rel="<%=(idx+1) %>">
						<span class="step_no <%=on %>"><%=(idx+1) %></span>
						<span class="step_descr">
						<%=(idx+1) %>교시
						</span>
					</a>
				</li>
				
		<%
			} 
		}
		%>  
			</ul>
		</div>
		<div class="progress" style="margin-bottom: 5px;">
			<div class="progress-bar progress-bar-striped bg-danger" role="progressbar" style="width: 0%;position: initial;" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
			<div class="txtBox dark " style="position: absolute;z-index: 999;font-weight:bold;">${detailLecture.user_lecture_start_dt}</div>
            <div class="txtBox dark " style="position: absolute;z-index: 999;font-weight:bold;right:0px;">${detailLecture.user_lecture_end_dt}</div>
			
			</div>
		</div>
		
         <div class="limit_cont_2" style="clear:both;">
             <span class="blue">유효시간이 </span> 
             <span class="red" id="diffTime"></span> 남았습니다.
         </div>
	</div>
</div>

<div class="x_panel">
	<div class="x_title">
		<h2><i class="fa fa-align-left"></i> 강의</h2>
		<ul class="nav navbar-right panel_toolbox" style="min-width: 0px;">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>		
			<li><a class="close-link"><i class="fa fa-close"></i></a></li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content" id="divMOVIE">
		<video id="edu-video" controlsList="nodownload" playsinline > </video>		
	</div>
</div>

<div class="x_panel">
	<div class="x_title">
		<h2><i class="fa fa-align-left"></i> 문제</h2>
		<ul class="nav navbar-right panel_toolbox" style="min-width: 0px;">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>		
			<li><a class="close-link"><i class="fa fa-close"></i></a></li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content" >
		<div class="quizCont">
			<div class="queTab">
				<% for( int idx=0; idx < output.getCount("estimate_seq_no"); idx++){ 
					String answer = output.getText("answer",idx);
					boolean isPass = false;
					if( "1".equals(output.getText("exam_type",idx)) ){ 
					
						DataSet reqView = (DataSet)output.get("reqView",idx);
						
						for( int jdx=0 ; jdx < reqView.getCount("item_seq_no"); jdx++){
							String check_yn = reqView.getText("check_yn",jdx);
							String item_seq_no = reqView.getText("item_seq_no",jdx);
							if("Y".equals(check_yn) && item_seq_no.equals(answer) ){
								isPass = true;
								break;
							}
						}
					}
					
					if(isPass)
					{
				%>	
					<a id="exam_a_<%=(idx+1)%>" code="<%=(idx+1) %>" class="btn btn-sm btn-primary" onclick="getEstimateView('<%=(idx+1) %>', 'quizCont');"><span id="exam_icon_<%=(idx+1)%>" class="glyphicon glyphicon-play-circle" aria-hidden="true"></span> <%=(idx+1) %>번 <span id="exam_tab_<%=(idx+1)%>" class="glyphicon glyphicon-ok-circle" aria-hidden="true"></span></a>
				<% }else{ %>
					<a id="exam_a_<%=(idx+1)%>" code="<%=(idx+1) %>" class="btn btn-sm btn-default" onclick="getEstimateView('<%=(idx+1) %>', 'quizCont');"><span id="exam_icon_<%=(idx+1)%>" class="glyphicon glyphicon-minus-sign red" aria-hidden="true"></span> <%=(idx+1) %>번 <span id="exam_tab_<%=(idx+1)%>" class="glyphicon glyphicon-remove-circle red" aria-hidden="true"></span></a>
				
				<% } }%>
			</div>
		
			<div class="quesBox">
			
				
			
			<% for( int idx=0; idx < output.getCount("estimate_seq_no"); idx++){ 
				String answer = output.getText("answer",idx);
				boolean isPass = false;
				
				if( "1".equals(output.getText("exam_type",idx)) ){ 
					DataSet reqView = (DataSet)output.get("reqView",idx);
					for( int jdx=0 ; jdx < reqView.getCount("item_seq_no"); jdx++){ 
						String check_yn = reqView.getText("check_yn",jdx);
						String item_seq_no = reqView.getText("item_seq_no",jdx);
						if("Y".equals(check_yn) && item_seq_no.equals(answer) ){
							isPass = true;
							break;
						}
					}
				 }
			%>
				<input type="hidden" name="estimate_seq_no" id="estimate_seq_no" value="<%=output.getText("estimate_seq_no",idx)%>"> 
				
				
				<div id="testBox_<%=(idx+1) %>" style="display:none;margin: 10px;background: #fff;" class="pricing modal fade bs-edu-modal-<%=(idx+1) %>" tabindex="-1" role="dialog" aria-hidden="true" code="<%=(idx+1) %>">
				
					<div class="title" style="padding: 12px 0;height:auto;background:#2a3f54;">
						<h4><%=output.getText("exam_num",idx)%>. <%=output.getText("exam_title",idx)%><span id="exam_title_<%=output.getText("estimate_seq_no",idx)%>"><b style="color:red;">(<% if( isPass ){ %>정답<%}else{ %>X<%} %>)</b></span></h4>
					</div>
					<div class="x_content">
						<ul class="list-unstyled text-left chk">
							
								<% if( "1".equals(output.getText("exam_type",idx)) ){ 
									DataSet reqView = (DataSet)output.get("reqView",idx);
								%>
									<% for( int jdx=0 ; jdx < reqView.getCount("item_seq_no"); jdx++){ 
										String check_yn = reqView.getText("check_yn",jdx);
										String item_seq_no = reqView.getText("item_seq_no",jdx);
										
										if( !isPass ){
									%>		
										<li style="clear:both;margin:5px;">
										
								      		<div style="float:left;"><input type="radio" class="radio" exam_num="<%=output.getText("exam_num",idx)%>" name="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" id="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" value="<%=reqView.getText("item_seq_no",jdx)%>" /></div>
								      		<div style="width: 90%;margin-left:20px;"><%=jdx+1 %>) <%=reqView.getText("item_nm",jdx)%></div>
								      	
										</li>
									<%		
										}else{
											
										if("Y".equals(check_yn) && item_seq_no.equals(answer) ){
																	
									%>
									<li style="clear:both;margin:5px;">							
								      		<div style="width: 90%;"><font color="red"><b><%=jdx+1 %>) <%=reqView.getText("item_nm",jdx)%>(O)</b></font></div>					      	
									</li>
								 <% 	}else{ %>
								 	<li style="clear:both;margin:5px;">							
								      		<div style="width: 90%;"><%=jdx+1 %>) <%=reqView.getText("item_nm",jdx)%></div>					      	
									</li>
								 <%		} } %> 
								 <%	} %> 
								 <%} else if( "3".equals(output.getText("exam_type",idx)) ){  %>
								 	<li>
										
										      예<input type="radio" class="radio" exam_num="<%=output.getText("exam_num",idx)%>" name="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" id="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" value="예" />&nbsp;&nbsp;
										      아니오<input type="radio" class="radio" exam_num="<%=output.getText("exam_num",idx)%>" name="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" id="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" value="아니오" />
								      
									</li>
								 <%}else{ %> 
								 	<li>
									 
								     		<textarea exam_num="<%=output.getText("exam_num",idx)%>" name="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" id="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" class="textarea_BoxBlue" cols="140" rows="5"></textarea>
								     
									</li>
								 <%} %>
									
						</ul>
					</div>
					<% if( output.getText("chart_path",idx) != null && !"".equals(output.getText("chart_path",idx))){ %>
						<div class="img">
							<img src="${photoPath}/<%=output.getText("chart_path",idx)%>" style="max-width:100%">
						</div>
					<%} %>
					<div class="pricing_footer">
						<div class="btnWrap" style="clear: both;" >
							<% if( !isPass ){ %>
							<a href="#" class="btn btn-sm btn-primary" onclick="fn_doEstimate(<%=(idx) %>, '<%=output.getText("estimate_seq_no",idx)%>', '<%=output.getText("exam_seq_no",idx)%>' );" id="btn_<%=output.getText("estimate_seq_no",idx)%>"><span class="">확인</span></a>
							<% } %>
							<a href="#" class="btn btn-sm btn-primary" onclick="fn_doEstimateClose(<%=(idx) %>, '<%=output.getText("estimate_seq_no",idx)%>', '<%=output.getText("exam_seq_no",idx)%>' );"><span class="">닫기</span></a>
							
						</div>
					</div>
				</div>
				
				<% } %>
			
			</div>
			
		</div>
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
							<h2 class="title">
								<%=class_list.getText("title",idx) %>
							</h2>
						</div>
					
					</div>
				</li>
			<% }} %>			
			
		</ul>
	</div>
</div>	

<div class="alert alert-custom alert-dismissible " role="alert">
	<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
	</button>
	<div class="mail_list">
		<div class="left"><i class="fa fa-star"></i></div>
		<div class="right">
			<h3>알림 </h3>
		</div>
	</div>
	<div class="mail_list">
		<div class="left"><i class="fa fa-paperclip"></i></div>
		<div class="right">
			<p>동영상보기 “시작하기” 클릭후 반드시 2주(14일)일안에 수강 완료하여야하며,기간이 지났을 경우 교육 재신청하여야합니다.</p>
			<strong>※ 단, 책임자교육 신청자의 경우 반드시 당해연도 12월31일까지 이수완료하여야합니다.</strong>
		</div>
	</div>
	<div class="mail_list">
		<div class="left"><i class="fa fa-paperclip"></i></div>
		<div class="right">
			<p>동영상을 보면서 중간에 제시되는 문제를 풀어야 끝까지 시청할 수 있습니다.</p>
			<span class="glyphicon glyphicon-minus-sign tred" aria-hidden="true"></span> 동영상을 시청해야 문제를 풀 수 있습니다.<br />
			<span class="glyphicon glyphicon-play-circle tblue" aria-hidden="true"></span> 클릭하시면 해당 문제를 풀 수 있습니다.<br />
			<span class="glyphicon glyphicon-remove-circle taero" aria-hidden="true"></span>문제를 아직 풀지 않았거나 정답이 아닙니다.<br />
			<span class="glyphicon glyphicon-ok-circle tgreen" aria-hidden="true"></span>문제를 풀었고 정답 입니다.<br />
		</div>
	</div>
</div>	

	
	
<!-- end :: content -->
</div>

</body>
</html>