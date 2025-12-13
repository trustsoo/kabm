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
	background: #2ea9d0;
	text-align: center!important;
	color: #fff;
	height: 30px;
	vertical-align: middle!important;
}
.modal-header, h4, .close {
  background-color: #2ea9d0;
  color:white !important;
  text-align: center;
  font-size: 30px;
}
.modal-footer {
  background-color: #f9f9f9;
}
select {
    height: 30px;
    padding: 2px 2px;
    color: #666;
    border-radius: 3px;
    border: 1px solid #ccc;
    line-height: 12px;
    vertical-align: middle;
}
</style>
<script src="/static/lib/bootstrap/js/fullcalendar.min.js"></script>
<script src="/static/lib/bootstrap/js/bootbox.min.js"></script>
<script type="text/javascript" type="text/javascript">
//<![CDATA[
var calendar = null;
//var prsn_vctn_view = true;
var ch_allDay = 'Y';

jQuery(document).ready(function(){
	
	$(".date-picker").datepicker({
		format: 'yyyy-mm-dd',			
		todayHighlight: true,
		autoclose: true
	});
	
	auto_search(document, 'btn_search_board');
	
	YearSelectBox('year_type', 5);
	MonthSelectBox('month_type');
	
	//날짜달력 조건생성.
	//Create_datepicker('date-picker');
	
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
			right: 'month,agendaWeek,agendaDay'
		},
		events: {
            url : '/mng/content/action/calendar.jspx?cmd=list',
            data : { search_word : jQuery('#search_word').val(),
            	search_schdl_type : jQuery('#search_schdl_type').val()
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
            
            js_clickDate(start, end, allDay);
		}
		,
		eventClick: function(calEvent, jsEvent, view) {
 			
			js_clickEvent(calEvent);
			
		},
		dayClick:function( date, allDay, jsEvent, view ) {
			
			var singleClick = date.toUTCString();

            if(doubleClick==singleClick){
                console.log('Double-click!');                
                doubleClick = null;
                //prsn_vctn_view = false;
                
                js_clickDate(date, date, allDay, 'dayClick');
            }else{
                doubleClick=date.toUTCString();
                clearInterval(clickTimer);
                clickTimer = setInterval(function(){
                    doubleClick = null;
                    clearInterval(clickTimer);
                }, 500);
            }
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
    
    jQuery("#btnReset").click(function() {  
        jQuery("form").each(function() {  
               if(this.id == "board_list_form") this.reset();  
            });  
    }); 
    
    jQuery("#btn_search_board").click(function() {    	
    	jQuery("div.fc-view").scrollTop(0);
    	js_searchCalendar();
    }); 
    
    jQuery("#search_schdl_type").on('change' , function() {    	
    	jQuery("div.fc-view").scrollTop(0);
    	js_searchCalendar();
    });
    /* jQuery("#btn_search_board").click(function() {    	
    	var year_type = jQuery('#year_type').val();
    	var month_type = jQuery('#month_type').val();
    	var search_word = jQuery('#search_word').val();
    	var search_schdl_type = jQuery('#search_schdl_type').val();
    	
    	location.href='/mng/content/calendar.jspx?cmd=calendar_list&year_type='+year_type+'&month_type='+month_type+ '&search_schdl_type='+search_schdl_type+'&search_word='+search_word;
   });  */
        
   jQuery("#btnSave").click(function() { 
	   
	   js_save();
   });
   
	jQuery("#btnDel").click(function() { 
	   
		js_delEvents();
   });
   
   
});

function time_Form(data){
	if(data < 10){
		data = "0"+data;
	}
	return data;
}

function js_searchCalendar()
{
	var events = {
            type: 'POST',
            url : '/mng/content/action/calendar.jspx?cmd=list',
            data : {
            	search_word : jQuery('#search_word').val(),
            	search_schdl_type : jQuery('#search_schdl_type').val()
            }
      }
	calendar.fullCalendar('removeEvents');
    calendar.fullCalendar('removeEventSource', events);
    calendar.fullCalendar('addEventSource', events);
    calendar.fullCalendar('rerenderEvents');
}

function js_dragEvent(calEvent)
{
	var startdt = jQuery.fullCalendar.formatDate( calEvent.start  , 'yyyy-MM-dd HH:mm' );
    var enddt = jQuery.fullCalendar.formatDate( calEvent.end  , 'yyyy-MM-dd HH:mm' );
    
    var seq_no = calEvent.seq_no;
    var schdl_div = calEvent.schdl_div;
    var schdl_type = calEvent.schdl_type;
    var isok = js_saveEventsData(seq_no , startdt, enddt , calEvent.allDay , calEvent.title, schdl_div, schdl_type, calEvent.className);
    
    if( !isok )
        calendar.fullCalendar('revertEvent', calEvent);
}

function js_clickDate(start, end, allDay, from)
{
	var startdt = jQuery.fullCalendar.formatDate( start  , 'yyyy-MM-dd HH:mm' );
    var enddt = jQuery.fullCalendar.formatDate( end  , 'yyyy-MM-dd HH:mm' );
    
    var startdd = jQuery.fullCalendar.formatDate( start  , 'yyyy-MM-dd' );
    var enddd = jQuery.fullCalendar.formatDate( end  , 'yyyy-MM-dd' );
    var starthh = jQuery.fullCalendar.formatDate( start  , 'HH' );
    var endhh = jQuery.fullCalendar.formatDate( end  , 'HH' );
    var startmm = jQuery.fullCalendar.formatDate( start  , 'mm' );
    var endmm = jQuery.fullCalendar.formatDate( end  , 'mm' );
        
	if( allDay )
    {
        startdt = jQuery.fullCalendar.formatDate( start  , 'yyyy-MM-dd' ) + ' 00:00';
        enddt = jQuery.fullCalendar.formatDate( end  , 'yyyy-MM-dd' ) + ' 23:50';
        
        starthh = '00';
        startmm = '00';
        endhh = '23';        
        endmm = '59';
    }
    
	jQuery('#start_dt').val(startdd);
    jQuery('#end_dt').val(enddd);
    
    jQuery("#start_hh").val(starthh);
    jQuery('#start_hh').change();
    jQuery("#start_mm").val(startmm);
    jQuery('#start_mm').change();    
    
    jQuery("#end_hh").val(endhh);
    jQuery('#end_hh').change();
    jQuery("#end_mm").val(endmm);
    jQuery('#end_mm').change();
    
    jQuery('#glbl_schdl_type').val(jQuery('#search_schdl_type').val());
    jQuery('#schdl_nm').val('');
    CKEDITOR.instances.schdl_cntnt_data.setData('');
    var seq_no = 0;
    jQuery("#seq_no").val(seq_no);
    
    jQuery("#schdl_nm").attr('readonly' , false);
    jQuery("#start_dt").attr('disabled' , false);
    jQuery("#end_dt").attr('disabled' , false);
    jQuery("#start_hh").attr('disabled' , false);
    jQuery("#end_hh").attr('disabled' , false);
    jQuery("#start_mm").attr('disabled' , false);
    jQuery("#end_mm").attr('disabled' , false);
    
	calendar.fullCalendar('unselect');
    
    $("#myModal").modal('show');
	
}

function js_clickEvent(calEvent)
{ 
	console.log('calEvent====>',calEvent);
	var startdt = jQuery.fullCalendar.formatDate( calEvent.start  , 'yyyy-MM-dd HH:mm' );
    var enddt = jQuery.fullCalendar.formatDate( calEvent.end  , 'yyyy-MM-dd HH:mm' );
    
    var startdd = jQuery.fullCalendar.formatDate( calEvent.start  , 'yyyy-MM-dd' );
    var enddd = jQuery.fullCalendar.formatDate( calEvent.end  , 'yyyy-MM-dd' );
    var starthh = jQuery.fullCalendar.formatDate( calEvent.start  , 'HH' );
    var endhh = jQuery.fullCalendar.formatDate( calEvent.end  , 'HH' );
    var startmm = jQuery.fullCalendar.formatDate( calEvent.start  , 'mm' );
    var endmm = jQuery.fullCalendar.formatDate( calEvent.end  , 'mm' );
    
    jQuery('#start_dt').val(startdd);
    jQuery('#end_dt').val(enddd);
    
    jQuery("#start_hh").val(starthh);
    jQuery('#start_hh').change();
  	jQuery("#start_mm").val(startmm);
  	jQuery('#start_mm').change();    
    
    jQuery("#end_hh").val(endhh);
    jQuery('#end_hh').change();
    jQuery("#end_mm").val(endmm);
    jQuery('#end_mm').change();
    
    jQuery('#glbl_schdl_type').val(calEvent.schdl_type);
    jQuery('#schdl_nm').val(calEvent.title);
    
    jQuery('#aaaa').html(calEvent.schdl_cntnt);
	CKEDITOR.instances.schdl_cntnt_data.setData(jQuery('#aaaa').text());
    var seq_no = calEvent.seq_no;
    jQuery("#seq_no").val(seq_no);
    
    jQuery("#schdl_nm").attr('readonly' , false);
    
    jQuery("#start_dt").attr('disabled' , false);
    jQuery("#end_dt").attr('disabled' , false);
    jQuery("#start_hh").attr('disabled' , false);
    jQuery("#end_hh").attr('disabled' , false);
    jQuery("#start_mm").attr('disabled' , false);
    jQuery("#end_mm").attr('disabled' , false);

    jQuery('.date-picker').datepicker("hide" );
    jQuery('#schdl_nm').focus();
    
	calendar.fullCalendar('unselect');
    
    $("#myModal").modal('show');
    
}

function addCalanderEvent(id,seq_no, start, end, allDay, title, className, schdl_div, schdl_type)
{

   	var addEvent = {
               title: title,
               start: start,
               end: end,
               allDay:allDay,
               seq_no:seq_no,
               id:id,
               className: className,
               schdl_div: schdl_div,
               schdl_type: schdl_type,
               editable: true
            };
        calendar.fullCalendar( 'renderEvent', addEvent , true );
    
}

function updateCalanderEvent(id,seq_no, start, end, allDay, title, className, schdl_div, schdl_type)
{
    var eventObject = calendar.fullCalendar( 'clientEvents', [id] )[0];

    if (eventObject != null)
    {
        eventObject.title = title;
        eventObject.start = start;
        eventObject.end = end;
        eventObject.allDay = allDay;
        eventObject.seq_no = seq_no;
        eventObject.id = id;
        eventObject.className = className;        
        eventObject.schdl_div = schdl_div;
        eventObject.schdl_type = schdl_type;
        eventObject.editable = true;
        
        calendar.fullCalendar( 'updateEvent', eventObject );
    }
}

function js_getCalendarInfo(calEvent)
{		
	var seq_no = calEvent.seq_no;
    calendar.fullCalendar('unselect');
    
	var http = jQuery.ajax({
		url : '/activity/action/calendar.jspx?cmd=getCalendarInfo',
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
	   				if(data.calendar_list[0].is_allday == 'true')
	   					schdl_dt =  data.calendar_list[0].start_dt;
   					else
   						schdl_dt =  data.calendar_list[0].start_dt+" ~ "+data.calendar_list[0].end_dt;
	   				
   					var schdl_tm = '';
   					if(data.calendar_list[0].start_time == data.calendar_list[0].end_time)
   						schdl_tm =  data.calendar_list[0].start_time;
   					else
   						schdl_tm =  data.calendar_list[0].start_time+" ~ "+data.calendar_list[0].end_time;
		   			
	   				jQuery('#schdl_nm').html(data.calendar_list[0].schdl_nm);
	   				jQuery('#schdl_dt').html(schdl_dt);
	   				jQuery('#schdl_tm').html(schdl_tm);
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

function js_save()
{ 
	var allDay = 'Y';
	var year_type = jQuery('#year_type').val();
	var seq_no = $('#seq_no').val();
	var isok = js_saveEvents(seq_no , allDay);

	if(isok)  $('#myModal').modal("hide"); 
}

function js_saveEventsData(seq_no , startdt, enddt , allDay , schdl_nm, schdl_div, schdl_type,className)
{
	if( seq_no == null || jQuery.type(seq_no) == 'undefined'  || !isNum(seq_no) )
		seq_no = 0;
		
    var isok = false;
    
    //var isAllDay = ch_allDay;
    
	var _url = '/mng/content/action/calendar.jspx?cmd=doEventAdd';
	//var _data = 'start_dd=' + startdt + '&end_dd=' + enddt + '&seq_no=' + seq_no + '&schdl_nm=' + schdl_nm+ '&is_allday=' + isAllDay;
	var _data = jQuery('#eventForm').serialize(true);
    
	var http = jQuery.ajax( {
        url: _url,  
        type: 'POST',
        data : _data,
        async : false,          
        error   : function(xhr)
        {
        	isok = false;
        },
        success: function(xmlDoc)
        {           
            var code = jQuery(xmlDoc).find('code').text();
            var msg = jQuery(xmlDoc).find('msg').text();
            var data = jQuery(xmlDoc).find('data').text();
            
            if(code == '200')
            {
            	if(seq_no == 0){
            		//2개가 add되는 문제로 제거.
            		//addCalanderEvent(schdl_div + '_'+ data, data, jQuery.fullCalendar.parseDate(startdt), jQuery.fullCalendar.parseDate(enddt),allDay, schdl_nm, className , schdl_div, schdl_type);
            	} else
            		updateCalanderEvent(schdl_div + '_'+ data, data, jQuery.fullCalendar.parseDate(startdt), jQuery.fullCalendar.parseDate(enddt),allDay, schdl_nm, className , schdl_div, schdl_type);
            	
            	isok = true;
            } else
            {
                msgStart(msg_com_code_010+"("+msg+")");
                isok = false;
            }
            jQuery("div.fc-view").scrollTop(0);
            jQuery('#calendar').fullCalendar( 'refetchEvents' );
        }           
    } );
	return isok;
}


function js_saveEvents(seq_no , allDay)
{
    if( seq_no == null || jQuery.type(seq_no) == 'undefined'  || !isNum(seq_no) )
        seq_no = 0;
    
    if(!checkFormField('#eventForm')) return;
    
    var startdd = jQuery('#start_dt').val();
    var enddd = jQuery('#end_dt').val();
    var starthh = jQuery('#start_hh').val();
    var endhh = jQuery('#end_hh').val();
    var startmm = jQuery('#start_mm').val();
    var endmm = jQuery('#end_mm').val();
    
    var startdt = startdd + ' ' + starthh + ':' + startmm;
    var enddt = enddd + ' ' + endhh + ':' + endmm;
    
    jQuery('#start_dd').val(startdt);
    jQuery('#end_dd').val(enddt);
    jQuery('#seq_no').val(seq_no);
    jQuery('#schdl_cntnt').val(CKEDITOR.instances.schdl_cntnt_data.getData());
    jQuery('#glbl_schdl_type').val(jQuery('#search_schdl_type').val());
    
    var isok = js_saveEventsData(seq_no , startdt, enddt , allDay );
    return isok;
}

function js_delEvents()
{
	var seq_no = jQuery('#seq_no').val();
	if( seq_no == null || jQuery.type(seq_no) == 'undefined' || !isNum(seq_no) || seq_no <= 0 )
	{
		msgStart(msg_com_code_025, "warning");
		return;
	}
	if(!confirm(msg_com_code_107)) 
	{
		return;
	}
	
	var isok = false;
	
    var _url = '/mng/content/action/calendar.jspx?cmd=doEventDel';
    
    var http = jQuery.ajax( {
        url: _url,  
        type: 'POST',
        data : 'seq_no=' + seq_no,
        async : false,          
        error   : function(xhr)
        {
        	isok = false;
        },
        success: function(xmlDoc)
        {           
            var code = jQuery(xmlDoc).find('code').text();
            var msg = jQuery(xmlDoc).find('msg').text();

            if(code == '200')
            {
            	calendar.fullCalendar( 'removeEvents', seq_no );
            	
            	isok = true;
            	$('#myModal').modal("hide"); 
            } else
            {
                msgStart(msg_com_code_010+"("+msg+")");
                isok = false;
            }
        }           
    } );
    
    return isok;
}

function gotoDate(){
	jQuery('#calendar').fullCalendar('gotoDate', jQuery('#year_type').val(), parseInt(jQuery('#month_type').val())-1);
}

//]]>
</script>
</head>
<body>
<div class="row-fluid">
	<div class="span12">
		<!-- <span type="button" id="_schedule_box" class="btn btn-info" style="float:left;margin-right:5px;background-color: #2ea9d0;border-color: #2ea9d0;color:#ffffff;"><i class="fa fa-calendar-o"></i> 달력보기</span>
		<span type="button" id="_schedule_list" class="btn btn-default" style="float:left;"><i class="fa fa-list"></i> 목록보기</span>
	 -->
		<div class="widget-box modify-css">
		 	
			<div class="widget-body_A" style="margin-top:11px;">
				<div class="widget-main">
				
				<form name='board_list_form' id='board_list_form' method="POST" onsubmit="return false;">
					<table class="condition-table">
					<tbody>
						<tr class='last-tr'>
							
							<th width="120px"><div>일정구분</div></th>
							<td width="120px">
								<select id="search_schdl_type" name="search_schdl_type">
									<option value="01">행사일정</option>
									<option value="02">교육일정</option>
								</select>
							</td>
							
							<th width="100px"><div>날짜</div></th>
							<td width="250px">
								<select id="year_type" name="year_type" class="form-control" onchange="javascript:gotoDate();" style="width: 44%;min-width: 84px;float:left;"></select>
								<select id="month_type" name="month_type" class="form-control" onchange="javascript:gotoDate();" style="width: 23%;min-width: 80px;float:left;"></select>
							</td>
							
							<th width="120px"><div>일정명</div></th>
							<td width="120px">
								<input class="W100P" type="text" id="search_word" name="search_word" style=""/>
							</td>
								
							<td>&nbsp;</td>
							<td width="200px">
								<div id="btn_search_board" class="btn btn-sm btn-warning" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><i class="icon-search"></i><b>조회</b></div>
								<div id="btnReset" class="btn btn-sm btn-success" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><i class="icon-refresh"></i><b>초기화</b></div>
							</td>
						</tr>
					</tbody>
					</table>
				</form>				
				</div>
			</div>
			
			
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
</div>


<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
  <div class="modal-dialog" style="width:700px">
  
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header" style="padding:15px 50px;">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4><span class="glyphicon glyphicon-calendar"></span> 협회일정</h4>
      </div>
      <div class="modal-body" style="padding:15px 20px;">
        <form name="eventForm" id="eventForm">
        <input type="hidden" name="seq_no" id="seq_no" value="0">
        <input type="hidden" name="is_allday" id="is_allday" value="Y">
		<input type="hidden" name="start_dd" id="start_dd" value="">
		<input type="hidden" name="end_dd" id="end_dd" value="">
		<input type="hidden" name="schdl_cntnt" id="schdl_cntnt" value="">
		<input type="hidden" name="glbl_schdl_type" id="glbl_schdl_type" value="">
		
		    <div class="form-group">
              <label for="schdl_nm"><span class="glyphicon glyphicon-check"></span> 일정제목</label>
              <input type='text' id='schdl_nm' name='schdl_nm' style="width:100%" required="true" alt="일정제목" tabindex="1">
            </div>
            <div class="form-group" style="height: 57px;">
              <label for="start_dt" style="float:left;"><span class="glyphicon glyphicon-check"></span> 일정</label>
              		<span class="input-group ubicus_textbox_width" style="clear:both;float:left;width:110px !important;">
		                 <input class="date-picker" id="start_dt" name="start_dt" type="text" data-date-format="yyyy-mm-dd" required="true" alt="시작일시" readonly="true" tabindex="2" style="height: 29px !important;width: 100px;"/>
		                 <span class="input-group-addon" >
		                     <i class="icon- fa fa-calendar"></i>
		                 </span>
		             </span>
		             <span id="start_hour_option" style="float:left;"></span>
		             <span id="start_min_option" style="float:left;"></span>  
					 <span style="float:left;">&nbsp;~&nbsp;</span> 
					<span class="input-group ubicus_textbox_width" style="float:left;width:110px !important;">
		                 <input class="date-picker" id="end_dt" name="end_dt" type="text" data-date-format="yyyy-mm-dd"  required="true" alt="종료일시" readonly="true" tabindex="3" style="height: 29px !important;width: 100px;"/>
		                 <span class="input-group-addon">
		                     <i class="icon- fa fa-calendar"></i>
		                 </span>
		             </span>
		             <span id="end_hour_option" style="float:left;"></span>	
		             <span id="end_min_option" style="float:left;"></span>
		             
            </div>
            <div class="form-group" style="clear:both;">
              <label for="schdl_cntnt_data"><span class="glyphicon glyphicon-check"></span> 일정내용</label>
              <textarea rows="5" cols="900" id="schdl_cntnt_data" name="schdl_cntnt_data" title="내용"></textarea>
            </div>            
		</form>
      </div>
      <div class="modal-footer">
        <button type="submit" class="btn btn-primary  pull-left" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span> 닫기</button>
        <button type="submit" class="btn btn-success  pull-left" id="btnSave"><span class="glyphicon glyphicon-file"></span> 저장</button>
        <button type="submit" class="btn btn-danger  pull-left" id="btnDel"><span class="glyphicon glyphicon-file"></span> 삭제</button>
      </div>
    </div>
    
  </div>
</div> 	
<div id='aaaa' style="display:none;"></div>
<script type="text/javascript" src="/static/lib/ckeditor/ckeditor.js" charset="UTF-8"></script>
<script type="text/javascript" isELIgnored="false">
    //<![CDATA[
    CKEDITOR.replace('schdl_cntnt_data', {            	
    	filebrowserImageUploadUrl : '/bin/FileUploader',
		height:200
   	});	

    //]]>
</script>
</body>
</html>