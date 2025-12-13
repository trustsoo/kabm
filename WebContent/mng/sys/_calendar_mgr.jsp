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
</style>
<script src="/static/lib/bootstrap/js/fullcalendar.min.js"></script>
<script src="/static/lib/bootstrap/js/bootbox.min.js"></script>
<script type="text/javascript" type="text/javascript">
//<![CDATA[
var calendar = null;
var prsn_vctn_view = true;
var ch_allDay = 'Y';

jQuery(document).ready(function(){

	auto_search(document, 'btn_search_board');
	
	if(<c:out value="${input.isCalAdmin }"/>){
		jQuery('#calendar_list_btn').css('display', '');	
	}
	
	YearSelectBox('year_type', 5);
	MonthSelectBox('month_type');
	
	//날짜달력 조건생성.
	Create_datepicker('date-picker');
	
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
			day: '일간'
		},	
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		events: {
            url : '/mng/sys/action/calendar.jspx?cmd=list',
            data : { search_word : jQuery('#search_word').val(),
            		search_schdl_div : jQuery('#search_schdl_div').val(),
            	 	search_schdl_type : jQuery('#search_schdl_type').val(),
            	  	calAdmin : <c:out value="${input.isCalAdmin }"/>
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
			
			var isCalAdmin = <c:out value="${input.isCalAdmin }"/>;
			
			if( isCalAdmin )
				copiedEventObject.editable = true;
			else
				copiedEventObject.editable = false;
			
			if($extraEventClass) copiedEventObject['className'] = [$extraEventClass];
			if($extraEventType) copiedEventObject['schdl_type'] = [$extraEventType];
			if($extraEventDiv) copiedEventObject['schdl_div'] = [$extraEventDiv];
			
			
			var start_dt = jQuery.fullCalendar.formatDate( date  , 'yyyy-MM-dd HH:mm' );
		    var end_dt = jQuery.fullCalendar.formatDate( enddt , 'yyyy-MM-dd HH:mm' );
		    
			var isok = js_saveEventsData(0 , start_dt, end_dt , allDay , copiedEventObject.title , $extraEventDiv, $extraEventType,$extraEventClass );    
			
			
			//jQuery('#calendar').fullCalendar('renderEvent', copiedEventObject, true);
			
			
		}
		,		
		eventResize:function( calEvent, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
			
			js_dragEvent(calEvent);
            
        }
		,
		eventDrop:function(calEvent,dayDelta,minuteDelta,allDay,revertFunc){
			
			if( calEvent.schdl_type == "PRSN_VCTN" )
				revertFunc();
			else{
				js_dragEvent(calEvent);
			}
			
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
 			
			//Fri May 16 2014 23:59:00 GMT+0900 (대한민국 표준시)
            js_clickEvent(calEvent);
			
		},
		dayClick:function( date, allDay, jsEvent, view ) {
			
			//jQuery('.fc-cell-overlay').css('top', jQuery('.fc-cell-overlay').position().top+jQuery("div.fc-view").scrollTop()*2);
			
            var singleClick = date.toUTCString();

            if(doubleClick==singleClick){
                console.log('Double-click!');                
                doubleClick = null;
                prsn_vctn_view = false;
                
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
	
	jQuery('#schdl_div').on('change',function(){
		js_scheduleCode();
    });
	
	jQuery('#search_schdl_div').on('change',function(){
		js_searchCode();
		js_searchCalendar();
    });
	
	jQuery('#search_schdl_type').on('change',function(){
		js_searchCalendar();
    });
	
	js_searchCode();
	js_scheduleCode();
	setHourSelectBoxCalendar("start_hh", "start_hour_option");
    setHourSelectBoxCalendar("end_hh", "end_hour_option");
    
    setMinSelectBoxCalendar("start_mm", "start_min_option", 10);
    setMinSelectBoxCalendar("end_mm", "end_min_option" , 10);
    
    
    jQuery("#btnReset").click(function() {  
        jQuery("form").each(function() {  
               if(this.id == "board_list_form") this.reset();  
            });  
    }); 
    
    jQuery("#btn_search_board").click(function() {    	
    	jQuery("div.fc-view").scrollTop(0);
    	js_searchCalendar();
   }); 
    
    jQuery("#calendar_list_btn").click(function() {    	
    	var start_dt = jQuery.fullCalendar.formatDate( jQuery('#calendar').fullCalendar('getView').visStart  , 'yyyy-MM-dd' );
    	var end_dt = jQuery.fullCalendar.formatDate( jQuery('#calendar').fullCalendar('getView').visEnd  , 'yyyy-MM-dd' );
    	var search_schdl_div = jQuery('#search_schdl_div').val();
    	var search_schdl_type = jQuery('#search_schdl_type').val();
    	var search_word = jQuery('#search_word').val();
    	
    	goMenuLink('Y', '-1_main_admin_mngr_60122', '/consult/calendar/calendar.jspx?cmd=calendar_list&start_dt='+start_dt+'&end_dt='+end_dt+'&search_word='+search_word+'&search_schdl_div='+search_schdl_div+'&search_schdl_type='+search_schdl_type);
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
            url : '/mng/sys/action/calendar.jspx?cmd=list',
            data : {
            	search_word : jQuery('#search_word').val() ,
            	search_schdl_div : jQuery('#search_schdl_div').val(),
            	search_schdl_type : jQuery('#search_schdl_type').val(),
            	calAdmin : <c:out value="${input.isCalAdmin }"/>
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
        endmm = '50';
    }
    
	jQuery('#start_dd').val(startdd);
    jQuery('#end_dd').val(enddd);
    
    jQuery("#start_hh").val(starthh);
    jQuery('#start_hh').change();
    jQuery("#start_mm").val(startmm);
    jQuery('#start_mm').change();    
    
    jQuery("#end_hh").val(endhh);
    jQuery('#end_hh').change();
    jQuery("#end_mm").val(endmm);
    jQuery('#end_mm').change();
    
    jQuery('#schdl_nm').val('');
    var seq_no = 0;
    
    
    jQuery("#schdl_div").css('display', 'none');
    jQuery("#schdl_div").val('GLOBAL_SCHEDULE');
    jQuery('#schdl_div').change();
    
    
    <c:choose>
    <c:when test="${ input.isCalAdmin}">    
	    jQuery("#schdl_div").attr('disabled' , false);
	    jQuery("#schdl_type").attr('disabled' , false);
    </c:when>
    <c:otherwise>
	    jQuery("#schdl_div").attr('disabled' , true);
	    jQuery("#schdl_type").attr('disabled' , false);
    </c:otherwise>
    </c:choose>
    
    jQuery("#schdl_nm").attr('readonly' , false);
    jQuery("#start_dd").attr('disabled' , false);
    jQuery("#end_dd").attr('disabled' , false);
    jQuery("#start_hh").attr('disabled' , false);
    jQuery("#end_hh").attr('disabled' , false);
    jQuery("#start_mm").attr('disabled' , false);
    jQuery("#end_mm").attr('disabled' , false);
    
    jQuery( "#calendar-confirm" ).removeClass('hide').dialog({
        resizable: false,
        modal: true,
        title: "일정관리",
        width: 575,
        buttons: [
            {
                html: "<i class='icon- fa fa-save'></i>&nbsp; 저장",
                "class" : "btn btn-success btn-xs",
                click: function() {
                	ch_allDay = 'N';
                    var isok = js_saveEvents(seq_no , allDay);
                    prsn_vctn_view = true;
                    js_scheduleCode();
                    if( isok ) jQuery( this ).dialog( "close" );
                }
            }
            ,
            {
                html: "<i class='icon- fa fa-reply bigger-110'></i>&nbsp; 취소",
                "class" : "btn btn-xs",
                click: function() {
                	prsn_vctn_view = true;
                    js_scheduleCode();
                    jQuery( this ).dialog( "close" );
                }
            }
        ]
    });
    
    if(!prsn_vctn_view){
    	jQuery('option[value=PRSN_VCTN]').remove();
    }
    
    calendar.fullCalendar('unselect');
	
}

function js_clickEvent(calEvent)
{	
	var startdt = jQuery.fullCalendar.formatDate( calEvent.start  , 'yyyy-MM-dd HH:mm' );
    var enddt = jQuery.fullCalendar.formatDate( calEvent.end  , 'yyyy-MM-dd HH:mm' );
    
    var startdd = jQuery.fullCalendar.formatDate( calEvent.start  , 'yyyy-MM-dd' );
    var enddd = jQuery.fullCalendar.formatDate( calEvent.end  , 'yyyy-MM-dd' );
    var starthh = jQuery.fullCalendar.formatDate( calEvent.start  , 'HH' );
    var endhh = jQuery.fullCalendar.formatDate( calEvent.end  , 'HH' );
    var startmm = jQuery.fullCalendar.formatDate( calEvent.start  , 'mm' );
    var endmm = jQuery.fullCalendar.formatDate( calEvent.end  , 'mm' );
    
    jQuery('#start_dd').val(startdd);
    jQuery('#end_dd').val(enddd);
    
    jQuery("#start_hh").val(starthh);
    jQuery('#start_hh').change();
  	jQuery("#start_mm").val(startmm);
  	jQuery('#start_mm').change();    
    
    jQuery("#end_hh").val(endhh);
    jQuery('#end_hh').change();
    jQuery("#end_mm").val(endmm);
    jQuery('#end_mm').change();
    
    
    jQuery('#schdl_nm').val(calEvent.title);
    var seq_no = calEvent.seq_no;
    var schdl_div = calEvent.schdl_div;
    var schdl_type = calEvent.schdl_type;

    jQuery("#schdl_div").val(schdl_div);
    jQuery('#schdl_div').change();
    
    jQuery("#schdl_type").val(schdl_type);
    jQuery('#schdl_type').change();
    
    jQuery("#schdl_div").attr('disabled' , true);
    jQuery("#schdl_type").attr('disabled' , true);
    
    if( !calEvent.editable )
    {
    	jQuery("#schdl_nm").attr('readonly' , true);
    	
    	jQuery("#start_dd").attr('disabled' , true);
        jQuery("#end_dd").attr('disabled' , true);
        jQuery("#start_hh").attr('disabled' , true);
        jQuery("#end_hh").attr('disabled' , true);
        jQuery("#start_mm").attr('disabled' , true);
        jQuery("#end_mm").attr('disabled' , true);
    	
    	jQuery( "#calendar-confirm" ).removeClass('hide').dialog({
            resizable: false,
            modal: true,
            title: "일정관리",
            width: 575,
            buttons: [                
                {
                    html: "<i class='icon- fa fa-ban bigger-110'></i>&nbsp; 닫기",
                    "class" : "btn btn-xs",
                    click: function() {
                        jQuery( this ).dialog( "close" );
                    }
                }
            ]
        });
    }else{
    	var button_option = [];
    	
        jQuery("#schdl_nm").attr('readonly' , false);
        
        jQuery("#start_dd").attr('disabled' , false);
        jQuery("#end_dd").attr('disabled' , false);
        jQuery("#start_hh").attr('disabled' , false);
        jQuery("#end_hh").attr('disabled' , false);
        jQuery("#start_mm").attr('disabled' , false);
        jQuery("#end_mm").attr('disabled' , false);

        if( schdl_type == "PRSN_VCTN"){
        	jQuery("#schdl_nm").attr('readonly' , true);
        	 jQuery("#start_dd").attr('disabled' , true);
             jQuery("#end_dd").attr('disabled' , true);
             
        	button_option = [
        	{
                html: "<i class='icon- fa fa-save'></i>&nbsp; 저장",
                "class" : "btn btn-success btn-xs",
                click: function() {
                	ch_allDay = 'N';
                    var isok = js_saveEvents(seq_no , calEvent.allDay);                         
                    if( isok ) jQuery( this ).dialog( "close" );
                }
            },
            {
                html: "<i class='icon- fa fa-reply bigger-110'></i>&nbsp; 취소",
                "class" : "btn btn-xs",
                click: function() {
                    jQuery( this ).dialog( "close" );
                }
            }
          ]
        } else{
        	button_option = [
				{
				    html: "<i class='icon- fa fa-save'></i>&nbsp; 저장",
				    "class" : "btn btn-success btn-xs",
				    click: function() {
				    	ch_allDay = 'N';
				        var isok = js_saveEvents(seq_no , calEvent.allDay);                         
				        if( isok ) jQuery( this ).dialog( "close" );
				    }
				}
				,
				{
				    html: "<i class='icon- fa fa-trash-o bigger-110'></i>&nbsp; 삭제",
				    "class" : "btn btn-danger btn-xs",
				    click: function() {
				        var isok = js_delEvents(seq_no);
				        if( isok ) jQuery( this ).dialog( "close" );
				    }
				}
				,
				{
				    html: "<i class='icon- fa fa-reply bigger-110'></i>&nbsp; 취소",
				    "class" : "btn btn-xs",
				    click: function() {
				        jQuery( this ).dialog( "close" );
				    }
				}
			]
        }
        
	    jQuery( "#calendar-confirm" ).removeClass('hide').dialog({
	        resizable: false,
	        modal: true,
	        title: "일정관리",
	        width: 606,
	        buttons: button_option
	    });
    
    }
    jQuery('.date-picker').datepicker("hide" );
    jQuery('#schdl_nm').focus();
}

function js_searchCode(){
    
    try
    {
        makeSelectBoxCodeFromURL('/common/action/code.jspx?cmd=getUseCodeList' + '&div_cd='+jQuery('#search_schdl_div').val(),'search_schdl_type', false);
          
    } catch(e)
    {       
    }
}
function js_scheduleCode(){
    
    try
    {
    	makeSelectBoxCodeFromURL('/common/action/code.jspx?cmd=getUseCodeList' + '&div_cd='+jQuery('#schdl_div').val(),'schdl_type', true);
    	
    	if(!prsn_vctn_view)
        	jQuery('option[value=PRSN_VCTN]').remove();
    	
    } catch(e)
    {       
    }
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

function js_saveEventsData(seq_no , startdt, enddt , allDay , schdl_nm, schdl_div, schdl_type,className)
{
	if( seq_no == null || jQuery.type(seq_no) == 'undefined'  || !isNum(seq_no) )
		seq_no = 0;
		
    var isok = false;
    
    var isAllDay = ch_allDay;
    
	var _url = '/mng/sys/action/calendar.jspx?cmd=doEventAdd';
	var _data = 'schdl_div=' + schdl_div +'&schdl_type=' + schdl_type  +'&start_dt=' + startdt + '&end_dt=' + enddt + '&seq_no=' + seq_no + '&schdl_nm=' + schdl_nm+ '&is_allday=' + isAllDay;
    
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
                msgStart(msg_mng_code_010+"("+msg+")");
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
    
    var schdl_div = jQuery("#schdl_div").val();
    var schdl_type = jQuery("#schdl_type").val();
    var startdd = jQuery('#start_dd').val();
    var enddd = jQuery('#end_dd').val();
    var starthh = jQuery('#start_hh').val();
    var endhh = jQuery('#end_hh').val();
    var startmm = jQuery('#start_mm').val();
    var endmm = jQuery('#end_mm').val();
    
    var startdt = startdd + ' ' + starthh + ':' + startmm;
    var enddt = enddd + ' ' + endhh + ':' + endmm;
    
    var schdl_nm = jQuery('#schdl_nm').val();
    var className = js_setClassName(schdl_div, schdl_type);
    var isok = js_saveEventsData(seq_no , startdt, enddt , allDay , schdl_nm, schdl_div, schdl_type, className);
    return isok;
}

function js_setClassName(schdl_div, schdl_type)
{
	var className = '<c:out value="${colorMap.DFT_ETC }"/>';
	
	if(schdl_div == 'PERSONAL_SCHEDULE' )
	{
		if( schdl_type == 'PRSN_SCHDL' )
			className = '<c:out value="${colorMap.PRSN_SCHDL }"/>';
		else if( schdl_type == 'PRSN_VCTN' )
			className = '<c:out value="${colorMap.PRSN_VCTN }"/>';
	}else{
		if( schdl_type == 'GLBL_SCHDL' )
			className = '<c:out value="${colorMap.GLBL_SCHDL }"/>';
		else if( schdl_type == 'GLBL_HOLIDAY' )
            className = '<c:out value="${colorMap.GLBL_HOLIDAY }"/>';
	}
	return className;
}

function js_delEvents(seq_no)
{
	if( seq_no == null || jQuery.type(seq_no) == 'undefined' || !isNum(seq_no) || seq_no <= 0 )
	{
		msgStart(msg_mng_code_004, "warning");
		return;
	}
	if(!confirm(msg_mng_code_007)) 
	{
		return;
	}
	
	var isok = false;
	
    var schdl_div = jQuery("#schdl_div").val();
    
    var _url = '/mng/sys/action/calendar.jspx?cmd=doEventDel';
    
    var http = jQuery.ajax( {
        url: _url,  
        type: 'POST',
        data : 'schdl_div=' + schdl_div + '&seq_no=' + seq_no,
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
            	calendar.fullCalendar( 'removeEvents', schdl_div + '_'+ seq_no );
            	
            	isok = true;
            } else
            {
                msgStart(msg_mng_code_010+"("+msg+")");
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
		<div class="widget-box">
			<div class="widget-body btb-none">
				<div class="widget-main no-padding">		
				<form name='board_list_form' id='board_list_form' method="POST" onsubmit="return false;">
					<table class="ubi_table_box" border="0" cellpadding="0" cellspacing="0" style="margin-top:0px">
						<colgroup><col width="100px"><col width="100px"><col width="120px"><col width="150px"><col width="120px"><col width="150px"><col width=""></colgroup>
						<tbody style="border-top:3px solid #4A90CC;">
							<tr>
								<th>날짜</th>
								<td>
									<select id="year_type" name="year_type" onchange="javascript:gotoDate();"></select>
									<select id="month_type" name="month_type" onchange="javascript:gotoDate();"></select>
								</td>
								<th>일정구분</th>
								<td>
									<select id="search_schdl_div" name="search_schdl_div" style='display:none'>
									    <option value="">전체</option>
					                    <option value="PERSONAL_SCHEDULE">개인일정</option>
					                    <option value="GLOBAL_SCHEDULE" selected>전체일정</option>
					                </select>
					                <select id="search_schdl_type" name="search_schdl_type" alt="일정구분">
					                    <option value="">전체</option>
					                </select>  
								</td>
								<th>일정명</th>
								<td>
									<input class="ubicus_textbox_width_long" type="text" id="search_word" name="search_word" style="width:250px !important;"/>
								</td>
								<td>
									<span class="btn_right">
										<span id="btnReset" class="btn btn-xs btn-info btn-curved" ><i class="icon-refresh"></i> 초기화</span>
										<span id="btn_search_board" class="btn btn-xs btn-primary btn-curved" ><i class="icon-search"></i> 조회</span>
									</span>
								</td>
							</tr>
							
						</tbody>
					</table>	
					</form>
				</div><!-- /widget-main -->
			</div><!-- /widget-body -->
				
			
			<div class="col-xs-12">
				<!-- PAGE CONTENT BEGINS -->

				<div class="row">
					<div class="col-sm-9" style="width:80%;">
						<div class="space"></div>

						<div id="calendar"></div>
					</div>

					<div class="col-sm-3" style="width:20%;">
						<div class="widget-box transparent">
							<div class="widget-header">
								<h4>간편 일정</h4>
							</div>

							<div class="widget-body">
								<div class="widget-main no-padding">
									<div id="external-events">										
									
										<c:set var="className" value="${colorMap.DFT_ETC }" />
										<c:forEach items="${global_output }" var="global_output" varStatus="status" >         
										    <c:choose>
                                                <c:when test="${ global_output.cd == 'GLBL_SCHDL'}">
                                                    <c:set var="className" value="${colorMap.GLBL_SCHDL}" />
                                                </c:when>
                                                <c:when test="${ global_output.cd == 'GLBL_HOLIDAY'}">
                                                    <c:set var="className" value="${colorMap.GLBL_HOLIDAY}" />
                                                </c:when>
										        <c:otherwise>
										            <c:set var="className" value="${colorMap.DFT_ETC}" />
										        </c:otherwise>
										        
										    </c:choose>
										    
										    <div class="external-event <c:out value="${className}"/>" data-class="<c:out value="${className}"/>" data-cd="<c:out value="${global_output.cd}"/>" data-div="GLOBAL_SCHEDULE">
                                                <i class="icon- fa fa-arrows"></i>
                                                <c:out value="${global_output.cd_nm}"/>
                                            </div>
										</c:forEach>
										<c:forEach items="${person_output }" var="person_output" varStatus="status" >         
                                            <c:choose>
                                            	<c:when test="${ person_output.cd == 'PRSN_VCTN'}">
                                                    <c:set var="className" value="${colorMap.PRSN_VCTN}" />
                                                </c:when>
                                                <c:when test="${ person_output.cd == 'PRSN_SCHDL'}">
                                                    <c:set var="className" value="${colorMap.PRSN_SCHDL}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="className" value="${colorMap.DFT_ETC}" />
                                                </c:otherwise>                                                
                                            </c:choose>
                                            
                                            <div class="external-event <c:out value="${className}"/>" data-class="<c:out value="${className}"/>" data-cd="<c:out value="${person_output.cd}"/>" data-div="PERSONAL_SCHEDULE">
                                                <i class="icon- fa fa-arrows"></i>
                                                <c:out value="${person_output.cd_nm}"/>
                                            </div>
                                        </c:forEach>
									    <label><span class="lbl">마우스로 끌어서 날짜에 놓으세요</span></label>
									</div>
								</div>
							</div>
							<!-- div>
								<a href="#" id="calendar_list_btn" class="btn btn-xs btn-primary" style="display:none;">
									<i class="icon- fa fa-calendar" title="일정리스트 보기"></i>
									일정리스트 보기
								</a>
							</div-->
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
</div>

<div id="calendar-confirm" class="hide popup_templet default_type">
<form name="eventForm" id="eventForm">
    <table  cellspacing='0' cellpadding='0' border='0'> 
		<tbody>
		<tr>
		    <td style="height:30px; width:60px;">일정구분</td>
            <td style="height:30px;" colspan="2">&nbsp;
                <select id="schdl_div" name="schdl_div" style="display:none">
                    <option value="PERSONAL_SCHEDULE">개인일정</option>
                    <option value="GLOBAL_SCHEDULE">전체일정</option>                    
                </select>                
                <select id="schdl_type" name="schdl_type" required="true" alt="일정구분">
                    <option value="">선택</option>
                </select>                
            </td>
        </tr>
        <tr> 
            <td style="height:30px;">일정제목</td>
            <td colspan='2' >&nbsp; <input type='text' id='schdl_nm' name='schdl_nm' style="width:510px" required="true" alt="일정제목" tabindex="1">  
            </td> 
        </tr>  
		<tr> 
			<td style="height:30px;">일정</td>
			<td>&nbsp;
				<span class="input-group ubicus_textbox_width" style="width:110px !important;">
	                 <input class="date-picker W100P" id="start_dd" name="start_dd" type="text" data-date-format="yyyy-mm-dd" required="true" alt="시작일시" readonly="true" tabindex="2"/>
	                 <span class="input-group-addon">
	                     <i class="icon- fa fa-calendar"></i>
	                 </span>
	             </span>
	             <span id="start_hour_option"></span>
	             <span id="start_min_option"></span>  
			</td> 
			<td>~ &nbsp;&nbsp;&nbsp;
				<span class="input-group ubicus_textbox_width" style="width:110px !important;">
	                 <input class="date-picker W100P" id="end_dd" name="end_dd" type="text" data-date-format="yyyy-mm-dd"  required="true" alt="종료일시" readonly="true" tabindex="3"/>
	                 <span class="input-group-addon">
	                     <i class="icon- fa fa-calendar"></i>
	                 </span>
	             </span>
	             <span id="end_hour_option"></span>	
	             <span id="end_min_option"></span>    		
			</td> 
		</tr>
		
		</tbody> 
	</table> 
</form>
</div>

<!-- iframe src="https://www.google.com/calendar/embed?src=blffot637do35g8hc1hf9a046s%40group.calendar.google.com&ctz=Asia/Dili" style="border: 0" width="0" height="0" frameborder="0" scrolling="no"></iframe-->

</body>
</html>