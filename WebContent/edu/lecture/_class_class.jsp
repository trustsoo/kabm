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
<link rel="stylesheet" href="/static/lib/flowplayer-7.0.4/skin/skin.css">
<style>
.flowplayer .fp-color-play {
  fill:  #00abcd;
}
.flowplayer.is-ready .fp-player {
  background-color: #000;
}
</style>
<!-- <link href="/static/lib/video-js-6.2.0/video-js.css" rel="stylesheet">
<script src="/static/lib/video-js-6.2.0/ie8/videojs-ie8.min.js"></script>
<script src="/static/lib/video-js-6.2.0/video.js"></script>
<script src="/static/lib/video-js-6.2.0/plugin/videojs.disableProgress.js"></script> -->

<script type="text/javascript" src="/static/lib/flowplayer-7.0.4/flowplayer.min.js?cmd=111"></script>
<script type="text/javascript" src="/static/edu/js/common.js?cmd=111"></script>
<script type="text/javascript" class="source">

var estimate_ing = false;
var curr_examno = ${curr_examno};

var exam_open_time = [${exam_open_time}];

var exam_clear_status = [${exam_clear_status}];

var curr_play_position = 0;

flowplayer(function (api, root) {
	
	api.on("progress", function (e, api, target) {
		
		if(curr_play_position <= api.video.time)
			curr_play_position = api.video.time;
		fn_checkExam(curr_play_position , api);
	});
	
	api.on("ready", function () {
		var seek_limit = 0;		
	    // do something when a video is loaded and ready to play
		 if( curr_examno > 0 ) 
			 seek_limit = exam_open_time[curr_examno -1];
		 if( seek_limit > 0 ){
			 seek_limit = (seek_limit+1) * 60;
			 curr_play_position = seek_limit;
		 }
		 
	     api.seek(seek_limit, function(){
   	 		api.on("beforeseek", function (e, api, target) {   	 		
<c:if test="${ input.s_user_id != 'trustsoo' and input.s_user_id != 'kabm01' }">   	 			
   		    // prevent seeking beyond current position   		 
   			    if (target >= curr_play_position) {
   			      e.preventDefault();
   			    }
</c:if>	    		    
   		    });
		 });
	     
	 });
	
	api.on("finish", function (e, api, target) {
		
		fn_finish(api);
	});
	
});



function getEstimateView(code ,divTabName)
{
	
	if( curr_examno < parseInt(code) ) 
	{
		msgOpen( '<b style="color:red;">'+(exam_open_time[parseInt(code)-1]+1)+'</b>'+'분까지 강의를 수강해야 문제를 확인할 수 있습니다.');
		return;
	}
	
	jQuery('div.' + divTabName + ' > ul > li > a').each(
			function(i)
			{  
				if(  code == jQuery(this).attr("code") )
				{
					jQuery(this).addClass("on");
					jQuery('div#testBox_'+(i+1)).css('display','block');
				}
				else
				{
					jQuery(this).removeClass("on");
					jQuery('div#testBox_'+(i+1)).css('display','none');
				}
			}
		);
	jQuery('.directions').css('display','none');
	
	
}
function getClassView(code, type, divTabName)
{
	if(estimate_ing == true) return;
	
	jQuery('div#' + divTabName + ' > ul > li > a').each(
		function(i)
		{
			if(  code == jQuery(this).attr("code") )
			{
				jQuery(this).addClass("on");
			}
			else
				jQuery(this).removeClass("on");
		}
	);


	switch(type)
	{
		case 'L' :
			jQuery('#divMOVIE').css("display","none");
			//jQuery('#divHTML').css("display","none");
			jQuery('#divDETAIL').css("display","none");
			break;
		case 'I' :
			var file_path = '${input.lecturePath}/${detail.file_path}';

			jQuery('#divMOVIE').css("display","block");
			//jQuery('#divHTML').css("display","none");
			jQuery('#divDETAIL').css("display","none");

			if( '${detail.file_path}' == '' )
			{
				jQuery('#divMOVIE').html('<H5 style="padding:20px 0 0 100px;">죄송합니다. 강의 파일 준비중입니다.</H5>');
			}else{
				//fn_videoPlayer( file_path );
				fn_createFlowPlayer( file_path );
			}
			break;
        case 'M' :

			jQuery('#divMOVIE').css("display","block");
			//jQuery('#divHTML').css("display","none");
			jQuery('#divDETAIL').css("display","none");

			break;
		case 'E' :
			jQuery('#divMOVIE').css("display","none");
			//jQuery('#divHTML').css("display","none");
			jQuery('#divDETAIL').css("display","block");
			break;

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
						//if(window.opener) { window.opener.document.location.href = '/edu/lecture/classCtrl.jspx?cmd=viewClassList';} 
						//self.close();
						
						opener.reload();
					}catch(e){}
				}else{
					alert(msg_edu_code_002);
					//location.reload();
					location.href="/edu/lecture/classCtrl.jspx?cmd=doClass&user_lecture_seq_no=<%=user_lecture_seq_no%>&user_class_seq_no=";
					try{
						opener.reload();
					}catch(e){}
				}
			}else{
				msgOpen( msg_com_code_010);
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
			msgOpen( (i+1) + '번'+msg_edu_code_005);
			break;
		}
	}
	if( !isstatus_ok ) return;
	
	var item_seq_no = jQuery(":input:radio[name=answr_ctnt_"+estimate_seq_no+"]:checked").val();
	if( item_seq_no == null || item_seq_no == undefined || item_seq_no == 'undefined')
	{
		msgOpen( msg_edu_code_004);
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
				
				msgOpen( msg_edu_code_001 );
				jQuery(":input:radio[name=answr_ctnt_"+estimate_seq_no+"]").attr('disabled','true');
				jQuery('#exam_title_' + estimate_seq_no).html('<b style="color:red;">(정답)</b>');
				jQuery('#exam_tab_' + estimate_seq_no).removeClass('taero').removeClass('glyphicon-remove-circle').addClass('glyphicon-ok-circle').addClass('tgreen');
				jQuery('#testBox_'+(exam_num + 1)).removeClass('testBox');
				jQuery('#flow_player').show();
				jQuery('#btn_'+estimate_seq_no).hide();
				
				estimate_ing = false;
					
				
			}else{
				msgOpen( msg_edu_code_003);
			}
		}
	});
	
}

function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
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

//초계산
function getTimer(){
	
	var now = moment();
	var to = moment('${detailLecture.user_lecture_end_dt}:59' , 'YYYY.MM.DD HH:mm:ss'); 
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
	$("#nowTime").html( moment(now).format('YYYY년 MM월 DD일 HH:mm:ss') );
	
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
			msgOpen( (i+1) + '번'+msg_edu_code_005);
			
			break;
		}
	}
	if( !isstatus_ok ) return;
	
	fn_doResult();
}

function fn_checkExam(time , api)
{
	var min = time/60|0;
	var str_open_time = exam_open_time[curr_examno];
	
	if(str_open_time == '' || str_open_time == undefined  ) str_open_time = 5*(curr_examno+1);
	
	var cur_open_time = parseInt(str_open_time)+1;//소수점 반올림
	if( cur_open_time == 0 ) cur_open_time = 5*(curr_examno+1);
	
	
	if(  curr_examno <= exam_open_time.length && exam_clear_status[curr_examno] != 1 
			&& exam_clear_status[exam_open_time.length -1 ] != 1 
			&& cur_open_time <= min && !estimate_ing)
	{
		estimate_ing = true;
		api.pause();
		jQuery('#testBox_'+(curr_examno + 1)).addClass('testBox');
		jQuery('#flow_player').hide();		
		jQuery('#exam_icon_'+curr_examno).removeClass('tred').removeClass('glyphicon-minus-sign').addClass('glyphicon-play-circle').addClass('tblue');
		curr_examno = curr_examno + 1;
		getEstimateView(curr_examno, 'testCont');
		
		
	}
	
}

jQuery(document).ready(function(){
	getTimer();
	
	getClassView('MV', 'I', 'study_content');
	
});

</script>

</head>

<body>
<div id="popup_content">
        <!-- start :: content -->
        <!-- start :: content -->


            <div class="mypage">
                

                <div class="class_popup_tit ">
                    <strong>학습진행현황</strong>
                </div>
                <div class="section-1 mt0">
<% if( class_list != null && class_list.getCount("user_class_seq_no") >4 ){ %>  <ul class="eduStep"><% }else{ %> <ul class="eduStep4"> <%} %>            
<% if( class_list != null && class_list.getCount("user_class_seq_no") >0 ) 
{
	for( int idx=0; idx < class_list.getCount("user_class_seq_no") ; idx++)
	{
		String pass_yn = class_list.getText("estimate_pass_yn",idx);
		String user_class_seq_no = class_list.getText("user_class_seq_no",idx);
		
			if( pass_yn != null && "Y".equals(pass_yn) ){
%>		                                    
                        <li class="on">
 <% 		}else if( user_class_seq_no != null && user_class_seq_no.equals(cur_user_class_seq_no) ){ %>                         
                        <li class="ing">
 <% 		}else{ %>                         
                        <li>
<%			} %>
							<strong><%=(idx+1) %></strong>
                            <span class="">교시</span>
                        </li>
<%
	} 
}
%>                                 
                    </ul>


                    <div class="limit_cont_1">
                        <a class="pbtn03 bold"><span class="pbtn03 bold bul">강의기간</span></a>
                        <span class="txtBox gray">${detailLecture.user_lecture_start_dt}</span>
                        <span class="red bold" id="nowTime"></span>
                        <span class="txtBox blue">${detailLecture.user_lecture_end_dt}</span>
                        
                    </div>

                    <div class="limit_cont_2">
                        <span class="blue">강의 유효시간이 </span> 
                        <span class="red" id="diffTime"></span> 남았습니다.
                    </div>
                </div>


                <div class="edu_tit mt20">
                    <strong>강좌보기</strong>
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
				<% if( s_mem_div != null && "9".equals(s_mem_div)){ %>				
					<a href="#" onclick="js_goclass('<%=user_lecture_seq_no%>','<%=user_class_seq_no%>');"><%=class_list.getText("title",idx) %></a>
				<% }else{ %>
					<%=class_list.getText("title",idx) %>
				<% } %>				
				</li>
			<% }} %>	
                  
              </ul>
              
              
              
              <div class="study_content" id="study_content">
                  <ul class="study_tab tab3" id="study_tab">
                      <li><a  code="MV" class="on" onclick="getClassView('MV', 'M', 'study_content');" code="MV">강의영상</a></li>
                      <li><a  code="EV" class="" onclick="getClassView('EV', 'E', 'study_content');" code="EV">강사이력</a></li>
                  </ul>
                  
                  		<div class="movieBox" style="position:relative;float: none;display:block;padding:10px 10 10 10;" id="divMOVIE">
                      		 <div id="flow_player" style="margin-top:1px;"> </div>
						</div>
						
						<!-- <div class="movieBox" style="position:relative;float: none;display:none;padding:10px 10 10 10;" id="divHTML">

						</div> -->
						
						<div class="movieBox" style="position:relative;float: none;display:none;" id="divDETAIL">
							<div class="edu_info" style="display:block;padding:10px 10px;margin:0 0;background:#fff;">
								<table cellpadding="0" cellspacing="0" class="" summary="" style="width:100%;">
									<colgroup>
										<col width="150px" />
										<col/>
									</colgroup>
									<tbody>
										<tr>
											<th>제목</th>
											<td>${detail.title}</td>
										</tr>
										<tr>
											<th>강사이력</th>
											<td id="cont_td" style="min-height:300px;">
												${detail.summary}
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
<div class="testCont">
	<ul class="queTab tab" style="clear: both;">
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
			<li><a code="<%=(idx+1) %>" class="" onclick="getEstimateView('<%=(idx+1) %>', 'testCont');"><span id="exam_icon_<%=(idx)%>" class="glyphicon glyphicon-play-circle tblue" aria-hidden="true"></span> <%=(idx+1) %>번 <span id="exam_tab_<%=output.getText("estimate_seq_no",idx)%>" class="glyphicon glyphicon-ok-circle tgreen" aria-hidden="true"></span></a></li>
		<% }else{ %>
			<li><a code="<%=(idx+1) %>" class="" onclick="getEstimateView('<%=(idx+1) %>', 'testCont');"><span id="exam_icon_<%=(idx)%>" class="glyphicon glyphicon-minus-sign tred" aria-hidden="true"></span> <%=(idx+1) %>번 <span id="exam_tab_<%=output.getText("estimate_seq_no",idx)%>" class="glyphicon glyphicon-remove-circle taero" aria-hidden="true"></span></a></li>
		
		<% } }%>
	</ul>

	<div class="quesBox">
	
		<div class="directions">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
				<tbody>
					<tr>
						<th scope="row">알림</th>
						<td>
							- 동영상보기 “시작하기” 클릭후 반드시 2주(14일)일안에 수강 완료하여야하며,기간이 지났을 경우 교육 재신청하여야합니다.     <br />
							  &nbsp; &nbsp; ※ 단, 책임자교육 신청자의 경우 반드시 당해연도 12월31일까지 이수완료하여야합니다.  <br />
							- 동영상을 보면서 중간에 제시되는 문제를 풀어야 끝까지 시청할 수 있습니다. <br />
							<span class="glyphicon glyphicon-minus-sign tred" aria-hidden="true"></span> 동영상을 시청해야 문제를 풀 수 있습니다.<br />
							<span class="glyphicon glyphicon-play-circle tblue" aria-hidden="true"></span> 클릭하시면 해당 문제를 풀 수 있습니다.<br />
							<span class="glyphicon glyphicon-remove-circle taero" aria-hidden="true"></span>문제를 아직 풀지 않았거나 정답이 아닙니다.<br />
							<span class="glyphicon glyphicon-ok-circle tgreen" aria-hidden="true"></span>문제를 풀었고 정답 입니다.<br />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	
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
		
		
		<div id="testBox_<%=(idx+1) %>" style="display:none;margin: 10px;" class="" code="<%=(idx+1) %>">
		
		<p class="q"><%=output.getText("exam_num",idx)%>. <%=output.getText("exam_title",idx)%><span id="exam_title_<%=output.getText("estimate_seq_no",idx)%>"><b style="color:red;">(<% if( isPass ){ %>정답<%}else{ %>X<%} %>)</b></span></p>
		<div class="a">
			<ul class="chk">
				
					<% if( "1".equals(output.getText("exam_type",idx)) ){ 
						DataSet reqView = (DataSet)output.get("reqView",idx);
					%>
						<% for( int jdx=0 ; jdx < reqView.getCount("item_seq_no"); jdx++){ 
							String check_yn = reqView.getText("check_yn",jdx);
							String item_seq_no = reqView.getText("item_seq_no",jdx);
							
							if( !isPass ){
						%>		
							<li style="clear:both;">
							
					      		<div style="float:left;"><input type="radio" class="radio" exam_num="<%=output.getText("exam_num",idx)%>" name="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" id="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" value="<%=reqView.getText("item_seq_no",jdx)%>" /></div>
					      		<div style="width: 90%;"><%=jdx+1 %>) <%=reqView.getText("item_nm",jdx)%></div>
					      	
							</li>
						<%		
							}else{
								
							if("Y".equals(check_yn) && item_seq_no.equals(answer) ){
														
						%>
						<li style="clear:both;">							
					      		<div style="width: 90%;"><font color="red"><b><%=jdx+1 %>) <%=reqView.getText("item_nm",jdx)%>(O)</b></font></div>					      	
						</li>
					 <% 	}else{ %>
					 	<li style="clear:both;">							
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
		
		<div class="btnWrap" style="clear: both;" id="btn_<%=output.getText("estimate_seq_no",idx)%>">
			<a href="#" class="pbtn02" onclick="fn_doEstimate(<%=(idx) %>, '<%=output.getText("estimate_seq_no",idx)%>', '<%=output.getText("exam_seq_no",idx)%>' );"><span class="">확인</span></a>
		</div>
		
		</div>
		
		<% } %>
	
	</div>
	
</div>
              </div>
          </div>
      </div>


  <!-- end :: content -->
<!-- end :: content -->
</div>


</body>
</html>