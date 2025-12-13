<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%
	String year_type = input.getText("year_type");
	String month_type = input.getText("month_type");

	String cur_pg 					= input.getText("cur_pg");
	if( cur_pg.equals("0") || cur_pg.equals(""))	cur_pg = "1";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">

.modal-header, h4, .close {
  background-color: #347ac2;
  color:white !important;
  font-size: 24px;
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
<script type="text/javascript" type="text/javascript">
//<![CDATA[	
var $glbl_schdl_type = '${glbl_schdl_type}';

	jQuery(document).ready(function(){
		
		YearSelectBox('year_type', 5);
		MonthSelectBox('month_type');
		
		if('<%=year_type%>' != ""){
			jQuery('#year_type').val('<%=year_type%>');
			jQuery('#month_type').val('<%=month_type%>');
		} 
		
		gridCalendarList(1);
				
		jQuery("#_schedule_list").click(function() {    	
			var year_type = jQuery('#year_type').val();
	    	var month_type = jQuery('#month_type').val();
	    	
	    	if($glbl_schdl_type == '01')
	    		location.href='/common/calendar.jspx?cmd=calendar_list&year_type='+year_type+'&month_type='+month_type;
	    	else
	    		location.href='/common/calendar.jspx?cmd=edu_calendar_list&year_type='+year_type+'&month_type='+month_type;
	    	
	   }); 
	    
	    jQuery("#_schedule_box").click(function() {    
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
	
	
	function gridCalendarList(cur_pg)
	{
		jQuery('#cur_pg').val(cur_pg);
		
		var http = jQuery.ajax({
			url : '/common/action/calendar.jspx?cmd=getCalendarList',
			data : jQuery("#frm").serialize(true),
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
				
		   		if(json.result.data.property[0].tot_cnt )
				{
					$('#tot_cnt').val(json.result.data.property[0].tot_cnt);
				} else {
					$('#tot_cnt').val(0);
				}	
	
		   		$("#listUL > li").remove();
		   			
		   		var listDataRows=[];
		   				   	
		   		var dataCount = 0;
				if( data.calendar_list ) 
					dataCount =  data.calendar_list.length;
						
		   		if(code == '200')
		   		{  			
		   			
		   			for(var i=0 ; i < data.calendar_list.length ; i++)
		   			{
		   				var tdData = "";
		   				
		   				tdData += "<li>";
		   				tdData +=  "<p class='tit'>";
		   				tdData +=  "<a href='javascript:js_clickEvent(" +data.calendar_list[i].seq_no+ ");'>";
		   				tdData +=  "<i class='fa fa-tasks'></i> ";
		   				tdData +=  data.calendar_list[i].schdl_nm;		
		   				tdData +=  "</a>";
		   				tdData +=  "</p>";
		   				
		   				if(data.calendar_list[i].start_dt == data.calendar_list[i].end_dt)
		   				{
		   					if(data.calendar_list[i].start_time == data.calendar_list[i].end_time)
				   				tdData +=  "<p class='date'><i class='fa fa-clock-o'></i> "+data.calendar_list[i].start_dt + ' ' + data.calendar_list[i].start_time+"</p>";
			   				else
			   					tdData +=  "<p class='date'><i class='fa fa-clock-o'></i> "+data.calendar_list[i].start_dt + ' ' + data.calendar_list[i].start_time + " ~ " + data.calendar_list[i].end_time+"</p>";
					   		
		   				}else{
		   					if(data.calendar_list[i].start_time == data.calendar_list[i].end_time)
				   				tdData +=  "<p class='date'><i class='fa fa-clock-o'></i> "+data.calendar_list[i].start_dt + ' ' + data.calendar_list[i].start_time+"</p>";
			   				else
			   					tdData +=  "<p class='date'><i class='fa fa-clock-o'></i> "+data.calendar_list[i].start_dt + ' ' + data.calendar_list[i].start_time + " ~ " + data.calendar_list[i].end_dt + ' ' + data.calendar_list[i].end_time+"</p>";
					   	}
	   						
		   				tdData +=  "</li>";
		   				
		   				listDataRows.push(tdData);
		   			}
	
		   			if(data.calendar_list.length == 0 )
		   			{
		   				listDataRows.push("<li style='text-align:center;width:100%;'><div class='tit'>조회된 결과가 없습니다.</div></li>");		   				
		   			}		   			
	
		   		}else{
		   			listDataRows.push("<li style='text-align:center;width:100%;'><div class='tit'>[" + code + "] " + msg + " </div></li>"); 
		   		}
		   		
				$("#listUL").html(listDataRows.join(' '));
				js_userPaging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val(), 'gridCalendarList');		   			
		   		
			}
		});
	}

	function js_clickEvent(seq_no)
	{		
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
	
	function gotoDate(){
		gridCalendarList(1);
	}
//]]>
</script>
</head>
<body>
<div id="content" style="font-family: 'nanum_square' !important;">	
	<form name='frm' id='frm' method="POST" onsubmit="return false;" >
	<input type="hidden" name="row_per_page" id="row_per_page" value='10'/>
	<input type="hidden" name="cur_pg" id="cur_pg" value='<%=cur_pg%>'/>
	<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
    <input type="hidden" name="search_schdl_type" id="search_schdl_type" value='${glbl_schdl_type}'/>
    
		<div style="display:block;margin-bottom:10px;height: 40px;">
		<span type="button" id="_schedule_box" class="btn btn-default" style="float:left;margin-right:5px;"><i class="fa fa-calendar-o"></i> 달력보기</span>
		<span type="button" id="_schedule_list" class="btn btn-info" style="float:left;background-color: #347ac2;border-color: #347ac2;color:#ffffff;"><i class="fa fa-list"></i> 목록보기</span>
		<span class="pull-right">
			<select id="year_type" name="year_type" class="form-control" onchange="javascript:gotoDate();" style="width: 100px;min-width: 84px;float:left;"></select>
			<select id="month_type" name="month_type" class="form-control" onchange="javascript:gotoDate();" style="width: 80px;min-width: 80px;float:left;"></select>
		</span>
		</div>
	</form>
		
	<div class="calListWrap">
         <ul id="listUL">
             
         </ul>
     </div>
     
    <div class="paging" id='pagingDiv'></div>     
	
</div>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
  <div class="modal-dialog" style="width:700px;">
  
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