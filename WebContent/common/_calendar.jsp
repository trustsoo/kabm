<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/common/common.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" href="/static/lib/bootstrap/css/fullcalendar.css" />
<style>
body{font-size:13px !important;}
.fc-sat { color:blue; }
.fc-sun { color:red;  }
.fc-week .fc-sat .fc-day-number { color:blue; }
.fc-week .fc-sun .fc-day-number { color:red;  }
.fc-widget-header{
	background: #347ac2;
	text-align: center!important;
	color: #fff;
	height: 24px;
	vertical-align: middle!important;
}

.modal-header, h4, .close {
  background-color: #347ac2;
  color:white !important;
  font-size: 30px;
}
.modal-footer {
  background-color: #f9f9f9;
}
.fc-event {
    border: 1px solid #1e90f1;
    background-color: #1e90f1;
    color: #fff;
    font-size: .85em;
    cursor: default;
}

.calListWrap{margin:25px 3px 40px; padding:0 20px; background-color:#eee; box-shadow:0 0 5px 1px rgba(145,145,145,.35); border:1px solid #dedede; border-radius:4px;}
.calListWrap li{padding:20px 0; color:#747474; border-top:1px dotted #666;}
.calListWrap li:first-child{border-top:0;}
.calListWrap li .tit{margin-bottom:10px;}
.calListWrap li .tit a{color:#337ab7; font-size:17px; font-weight:bold;}
.calListWrap li .date{margin-top:10px; color:#a94442; font-size:14px;}
.calListWrap li .date:before{content:''; display:inline-block; margin-right:5px; width:13px; height:13px; background:url('../images/ico_calendar.png'); background-size:100% 100%; vertical-align:middle;}
.calListWrap li .time{margin-top:5px; color:#de935f; font-size:14px;}
.calListWrap li .time:before{content:''; display:inline-block; margin-right:5px; width:13px; height:13px; background:url('../images/ico_time.png'); background-size:100% 100%; vertical-align:middle;}
.calListWrap li .txt{margin-top:8px; font-size:14px; color:#666; line-height:25px;}

.calDetailWrap{margin:25px 3px 40px; padding:25px 20px; background-color:#eee; box-shadow:0 0 5px 1px rgba(145,145,145,.35); border:1px solid #dedede; border-radius:4px;}
.calDetailWrap .tit{color:#337ab7; font-size:22px;}
.calDetailWrap .date{margin-top:15px; color:#a94442; font-size:14px;font-family: 'nanum_square' !important;}
.calDetailWrap .date:before{content:''; display:inline-block; margin-right:5px; width:13px; height:13px; background:url('../images/ico_calendar.png'); background-size:100% 100%; vertical-align:middle;}
.calDetailWrap .time{margin-top:8px; color:#de935f; font-size:14px;}
.calDetailWrap .time:before{content:''; display:inline-block; margin-right:5px; width:13px; height:13px; background:url('../images/ico_time.png'); background-size:100% 100%; vertical-align:middle;}
.calDetailWrap .addr{margin-top:8px; color:#de935f; font-size:14px;}
.calDetailWrap .addr:before{content:''; display:inline-block; margin-right:4px; width:12px; height:15px; background:url('../images/ico_location.png'); background-size:100% 100%; vertical-align:middle;}
.calDetailWrap .txt{margin-top:20px; padding-top:15px; border-top:1px solid #e2e2e2; font-size:16px; color:#666; line-height:22px;}

</style>
<script src="/static/lib/bootstrap/js/fullcalendar.min.js"></script>
<script src="/static/lib/bootstrap/js/bootbox.min.js"></script>
<script type="text/javascript" type="text/javascript">
//<![CDATA[
var calendar = null;
//var prsn_vctn_view = true;
var ch_allDay = 'Y';

var $glbl_schdl_type = '${glbl_schdl_type}';

jQuery(document).ready(function(){
	
	$(".date-picker").datepicker({
		format: 'yyyy-mm-dd',			
		todayHighlight: true,
		autoclose: true
	});
	
	
	YearSelectBox('year_type', 5);
	MonthSelectBox('month_type');
		
	jQuery('#external-events div.external-event').each(function() {

		// create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
		// it doesn't need to have a start or end
		var eventObject = {
			title: jQuery.trim(jQuery(this).text()) // use the element's text as the event title
		};

		// store the Event Object in the DOM element so we can get to it later
		jQuery(this).data('eventObject', eventObject);

		// make the event draggable using jQuery UI
		jQuery(this).draggable({
			zIndex: 999,
			revert: true,      // will cause the event to go back to its
			revertDuration: 0  //  original position after the drag
		});
		
	});
	
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
    
	var clickTimer = null;
	var doubleClick = null;
	
	calendar = jQuery('#calendar').fullCalendar({
		allDayText : '종일',
		monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
		dayNames: ['일','월','화','수','목','금','토'],
		dayNamesShort: ['일','월','화','수','목','금','토'],
		// time formats
		titleFormat: {
			month: 'yyyy년 MMMM',
			//week: "MMM d[ yyyy]{ '&#8212;'[ MMM] d yyyy}",
			week : "yyyy.MM.dd{' &#8212; 'yyyy.MM.dd}",
			day: 'yyyy.MM.dd' //'dddd, MMM d, yyyy'
		},
		columnFormat: {
			month: 'ddd',
			week: 'ddd M/d',
			day: 'dddd M/d'
		},
		timeFormat: { // for event elements
			//'': 'h(:mm)t' // default
			'': '<H:mm>'
		},
		buttonText: {
			prev: '<i class="icon- fa fa-chevron-left"></i>',
			next: '<i class="icon- fa fa-chevron-right"></i>',
			today: '오늘',
			month: '월간',
			week: '주간',
			day: '일간',
			list: '목록'
		},	
		header: {
			left: 'prev,next today',
			center: 'title',
			right: ''//month,agendaWeek,agendaDay'
		},
		events: {
            url : '/common/action/calendar.jspx?cmd=list',
            data : { search_word : jQuery('#search_word').val(),
            	     search_schdl_type : '${glbl_schdl_type}'
            	   }
        },
		editable: true,
		droppable: true, // this allows things to be dropped onto the calendar !!!
		drop: function(date, allDay) { // this function is called when something is dropped
			
			// retrieve the dropped element's stored Event Object
			var originalEventObject = jQuery(this).data('eventObject');
			if( originalEventObject == null || jQuery.type(originalEventObject) == 'undefined' )
				return;
		
			var $extraEventClass = jQuery(this).attr('data-class');
			var $extraEventType = jQuery(this).attr('data-cd');
			var $extraEventDiv = jQuery(this).attr('data-div');
			
			// we need to copy it, so that multiple events don't have a reference to the same object
			var copiedEventObject = jQuery.extend({}, originalEventObject);
			
			// assign it the date that was reported
			copiedEventObject.start = date;
			copiedEventObject.allDay = allDay;
			var enddt = date;
					
			if( allDay )
		    {
		        enddt = jQuery.fullCalendar.formatDate( enddt  , 'yyyy-MM-dd' ) + ' 23:50';	
		        enddt = jQuery.fullCalendar.parseDate(enddt);
		    }else{
		    	var tempDate = new Date(date);
		    	enddt = new Date(tempDate.setHours(tempDate.getHours()+1));
		    }
			
			copiedEventObject.end = enddt;
			
			copiedEventObject.editable = true;
			
			if($extraEventClass) copiedEventObject['className'] = [$extraEventClass];
			if($extraEventType) copiedEventObject['schdl_type'] = [$extraEventType];
			if($extraEventDiv) copiedEventObject['schdl_div'] = [$extraEventDiv];
			
			var start_dt = jQuery.fullCalendar.formatDate( date  , 'yyyy-MM-dd HH:mm' );
			
		    var end_dt = jQuery.fullCalendar.formatDate( enddt , 'yyyy-MM-dd HH:mm' );
		    
			var isok = js_saveEventsData(0 , start_dt, end_dt , allDay , copiedEventObject.title , $extraEventDiv, $extraEventType,$extraEventClass );    
			
		}
		,		
		eventResize:function( calEvent, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
			
			js_dragEvent(calEvent);
            
        }
		,
		eventDrop:function(calEvent,dayDelta,minuteDelta,allDay,revertFunc){
			js_dragEvent(calEvent);	//드래그 일정수정 모두 풀어달라 요청			
		}
		,
		selectable: true,
		selectHelper: false,
		select: function(start, end, allDay) {
			if(  (end - start) <= 1800000  ) return;
            
            //js_clickDate(start, end, allDay);
		}
		,
		eventClick: function(calEvent, jsEvent, view) {
			js_clickEvent(calEvent);
			
		},
		dayClick:function( date, allDay, jsEvent, view ) {
			var singleClick = date.toUTCString();

        },
        loading:function(calEvent, jsEvent){
        	var cur_start = jQuery('#calendar').fullCalendar('getView').start;
        	
        	jQuery('#year_type').val(jQuery.fullCalendar.formatDate(cur_start, 'yyyy'));
        	jQuery('#month_type').val(time_Form(jQuery.fullCalendar.formatDate(cur_start, 'M')));
        },
        eventDestroy: function( event, element, view ) { 
        	jQuery("div.fc-view").scrollTop(0);
        }
		
	});
		
	setHourSelectBoxCalendar("start_hh", "start_hour_option");
    setHourSelectBoxCalendar("end_hh", "end_hour_option");
    
    setMinSelectBoxCalendar("start_mm", "start_min_option", 1);
    setMinSelectBoxCalendar("end_mm", "end_min_option" , 1);
    
    jQuery('#start_hour_option, #start_min_option, #end_hour_option, #end_min_option').find('select').css('width', '65px');
    
    jQuery("#_schedule_list").click(function() {    	
    	var year_type = jQuery('#year_type').val();
    	var month_type = jQuery('#month_type').val();
    	
    	if($glbl_schdl_type == '01')
    		location.href='/common/calendar.jspx?cmd=calendar_list&year_type='+year_type+'&month_type='+month_type;
    	else
    		location.href='/common/calendar.jspx?cmd=edu_calendar_list&year_type='+year_type+'&month_type='+month_type;
   }); 
    
    jQuery("#_schedule_box").click(function() {    	
    	var year_type = jQuery('#year_type').val();
    	var month_type = jQuery('#month_type').val();
    	
    	if($glbl_schdl_type == '01')
    		location.href='/common/calendar.jspx?cmd=calendar';
    	else
    		location.href='/common/calendar.jspx?cmd=edu_calendar';
   });
    
    if($glbl_schdl_type == '01')
		jQuery('#popTitle').html('행사안내');
	else
		jQuery('#popTitle').html('교육일정');  
    
});

function time_Form(data){
	if(data < 10){
		data = "0"+data;
	}
	return data;
}

function js_clickEvent(calEvent)
{		
	var seq_no = calEvent.seq_no;    
    calendar.fullCalendar('unselect');
    
	var http = jQuery.ajax({
		url : '/common/action/calendar.jspx?cmd=getCalendarInfo',
		data : 'seq_no='+seq_no,
		type : 'POST',
		async : false,
		datatype: 'json',
		error : function(xhr)
   		{
			alert(xhr.status);
		},
		success:function(json) 
		{
			var code = json.result.code;
	   		var msg =  json.result.msg;
	   		var data = json.result.data;
					
	   		if(code == '200')
	   		{  			
	   			
	   			if( data.calendar_list.length >0 )
	   			{		   				
	   				var schdl_dt = '';
	   				
	   				if(data.calendar_list[0].start_dt == data.calendar_list[0].end_dt)
	   				{
	   					if(data.calendar_list[0].start_time == data.calendar_list[0].end_time)
		   					schdl_dt =  data.calendar_list[0].start_dt + ' ' + data.calendar_list[0].start_time;
	   					else
	   						schdl_dt =  data.calendar_list[0].start_dt + ' ' + data.calendar_list[0].start_time + " ~ " + data.calendar_list[0].end_time;
			   			
				   		
	   				}else{
	   					if( data.calendar_list[0].start_time == data.calendar_list[0].end_time)
		   					schdl_dt =  data.calendar_list[0].start_dt + ' ' + data.calendar_list[0].start_time;
	   					else
	   						schdl_dt =  data.calendar_list[0].start_dt + ' ' + data.calendar_list[0].start_time + " ~ " + data.calendar_list[0].end_dt + ' ' + data.calendar_list[0].end_time;
			   			
				   	}	   					
   					
	   				jQuery('#schdl_nm').html( "<i class='fa fa-tasks'></i> " + data.calendar_list[0].schdl_nm);
	   				jQuery('#schdl_dt').html( "<i class='fa fa-clock-o'></i> " + schdl_dt);
	   				//jQuery('#schdl_tm').html(schdl_tm);
	   				jQuery('#schdl_txt').html( prettyHtml( unescapeHTML(data.calendar_list[0].schdl_cntnt) ) );
	                
	   				$("#myModal").modal();
	   			} else {
	   				msgOpen(msg_com_code_010);
	   			}

	   		}else{
	   			msgOpen(msg_com_code_010);
	   		}		
		}
	});
    
}

function js_closeView()
{
	$('#ImagePop').hide();
	jQuery('#overlay_t').hide();
}


function gotoDate(){
	jQuery('#calendar').fullCalendar('gotoDate', jQuery('#year_type').val(), parseInt(jQuery('#month_type').val())-1);
}

//]]>
</script>
</head>
<body>
<div id="content">	
	<form name='board_list_form' id='board_list_form' method="POST" onsubmit="return false;" >
	<div style="display:block;margin-bottom:10px;height: 40px;">
	<span type="button" id="_schedule_box" class="btn btn-info" style="float:left;margin-right:5px;background-color: #347ac2;border-color: #347ac2;color:#ffffff;"><i class="fa fa-calendar-o"></i> 달력보기</span>
	<span type="button" id="_schedule_list" class="btn btn-default" style="float:left;"><i class="fa fa-list"></i> 목록보기</span>
	<span class="pull-right">
		<select id="year_type" name="year_type" class="form-control" onchange="javascript:gotoDate();" style="width: 100px;min-width: 84px;float:left;"></select>
		<select id="month_type" name="month_type" class="form-control" onchange="javascript:gotoDate();" style="width: 80px;min-width: 80px;float:left;"></select>
	</span>
	</div>
	</form>
	<div class="widget-box modify-css"> 			
		
		<div class="row">
			<div class="col-xs-12">
				<!-- PAGE CONTENT BEGINS -->

				<div class="row">
					<div class="col-xs-12">
						<div class="space"></div>

						<div id="calendar"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
  <div class="modal-dialog" style="width:700px">
  
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header" style="padding:0px 15px;">
        <button type="button" class="close" data-dismiss="modal" style="margin-top: 10px;">&times;</button>
        <h4><span class="glyphicon glyphicon-calendar" style="top: 5px;"></span> <span id="popTitle">협회일정</span></h4>
      </div>
      <div class="modal-body" style="padding:15px 20px;">
        	<div class="calDetailWrap" style="margin:0px;">
		         <p class="tit" id="schdl_nm"></p>
		         <p class="date" id="schdl_dt"></p>
		         <!-- <p class="time" id="schdl_tm"></p> -->
		         <div class="txt" id="schdl_txt"></div>
		     </div>
      </div>
      <div class="modal-footer">
        <button type="submit" class="btn btn-danger btn-default pull-left" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span> 닫기</button>
      </div>
    </div>
    
  </div>
</div> 

</body>
</html>