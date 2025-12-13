<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%
	String start_dt = input.getText("start_dt");
	String end_dt = input.getText("end_dt");
	String search_word = input.getText("search_word");
	String search_schdl_div = input.getText("search_schdl_div");
	String search_schdl_type = input.getText("search_schdl_type");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
	.jqgrow {cursor: default;}
</style>
<script type="text/javascript" type="text/javascript">
//<![CDATA[	
	
	jQuery(document).ready(function(){
		
		//날짜달력 조건생성.
		$(".date-picker").datepicker({
			format: 'yyyy-mm-dd',			
			todayHighlight: true,
			autoclose: true
		});
		
		
		if('<%=start_dt%>' != ""){
			jQuery('#dt1').val('<%=start_dt%>');
			jQuery('#dt2').val('<%=end_dt%>');
			jQuery('#search_schdl_div').val('<%=search_schdl_div%>');
			if('<%=search_schdl_div%>' != "")
				makeSelectBoxCodeFromURL('/common/action/code.jspx?cmd=getUseCodeList' + '&div_cd='+jQuery('#search_schdl_div').val(),'search_schdl_type', false);
			jQuery('#search_schdl_type').val('<%=search_schdl_type%>');
			jQuery('#search_word').val('<%=search_word%>');
		} else{
			//getAgoDate(년,월.일) - 날짜 가져오기.
			if( jQuery('#dt1').val() == "" )		jQuery('#dt1').val(getAgoDate(0,0,0).substr(0, 7)+'-01');
			if( jQuery('#dt2').val() == "" )		jQuery('#dt2').val(getAgoDate(0,0,0));
		}
		
		gridCalendarList();
		
		jQuery('#search_schdl_div').on('change',function(){
			makeSelectBoxCodeFromURL('/common/action/code.jspx?cmd=getUseCodeList' + '&div_cd='+jQuery('#search_schdl_div').val(),'search_schdl_type', false);
	    });
		
		jQuery("#btn_search_List").click(function() {    	
			gridCalendarList();
	   }); 
		
		jQuery("#_schedule_list").click(function() {    	
	    	var start_dt = jQuery('#dt1').val();
	    	var end_dt = jQuery('#dt2').val();
	    	var search_word = jQuery('#search_word').val();
	    	
	    	location.href='/mng/content/calendar.jspx?cmd=calendar_list&start_dt='+start_dt+'&end_dt='+end_dt+'&search_word='+search_word;
	    	
	   }); 
	    
	    jQuery("#_schedule_box").click(function() {    	
	    	var start_dt = jQuery('#dt1').val();
	    	var end_dt = jQuery('#dt2').val();
	    	var search_word = jQuery('#search_word').val();
	    	
	    	location.href='/mng/content/calendar.jspx?cmd=list&start_dt='+start_dt+'&end_dt='+end_dt+'&search_word='+search_word;
	   }); 
		
		auto_search(document, 'btn_search_List');
		
	});
	
	function gridCalendarList()
	{
		jQuery("#widget_grid").html("<p><table id=\"grid-table\"></table><div id=\"grid-pager\"></div></p>");
		
		var _url = '/mng/content/action/calendar.jspx?cmd=getCalendarList&'+jQuery("#list_form").serialize()+'&page=<%=input.getText("cur_pg")%>';
		
		jQuery("#grid-table").jqGrid({
			url : _url,
			datatype : 'json',
			mtype: 'POST',
			//postData : jQuery("#form").serialize(),
			jsonReader : {  //****필수요소****
				page: "page", 
				total: "total",
				records: "records",
				repeatitems: false,
				root: "calendar_list", 	//blockname			 
				id: "no"	
			},
			colNames:['NO', '일정종류', '일정대상', '일정제목', '일정기간'],
			colModel:[
					  {name:'no',index:'no', width:20, sortable:false, align:'center'},
					  {name:'cal_type',index:'cal_type', width:40, sortable:false, align:'center'},
			          {name:'cal_emp_nm',index:'cal_emp_nm', width:40, sortable:false, align:'center'},
			          {name:'cal_title',index:'cal_title', width:40, sortable:false, align:'center'},
			          {name:'cal_dt',index:'cal_dt', width:80, sortable:false, align:'center'}
			         ],  
			height: "375",
			rowNum:"9999",
			autowidth: true,
			pager : jQuery('#grid-pager'),
			viewrecords: true,
			altRows: true,
			emptyrecords:"데이터가 없습니다.",
			loadError : function(xhr, str, err)
	        {
	        	processGridError(xhr, str, err);
	        }, ondblClickRow : function(rowid, iRow, iCol, e)
	        {		
	        	
	        }, onSelectRow: function(ids)
			{	
	        	
			}, loadComplete : function(data)
			{
				var table = this;
				updatePagerIcons(table);
				
				updateNavGridButton('grid-table', 'grid-pager', table, false);
			}, onPaging : function(pgButton)
			{
				var change = 'Y';
				var nextPageNum = 0;
				var nextPageRow = 0;
				if(pgButton == 'user')
				{
					nextPageNum = jQuery('.ui-pg-input').val();
				}
				else if(pgButton.indexOf('first') != -1)
				{
					if(jQuery("#grid-table").jqGrid("getGridParam", "page") == 0 || jQuery("#grid-table").jqGrid("getGridParam", "page") == 1){
						change = 'N';
					}
					nextPageNum = '1';
				} else if(pgButton.indexOf('prev') != -1)
				{
					if(jQuery("#grid-table").jqGrid("getGridParam", "page") == 0 || jQuery("#grid-table").jqGrid("getGridParam", "page") == 1){
						change = 'N';
					}
					nextPageNum = parseInt(jQuery("#grid-table").jqGrid("getGridParam", "page")) -1;
				} else if(pgButton.indexOf('next') != -1)
				{
					if(jQuery("#grid-table").jqGrid("getGridParam", "page") == jQuery("#grid-table").getGridParam("lastpage")){
						change = 'N';
					}
					nextPageNum = parseInt(jQuery("#grid-table").jqGrid("getGridParam", "page")) + 1;
				} else if(pgButton.indexOf('last') != -1)
				{
					if(jQuery("#grid-table").jqGrid("getGridParam", "page") == jQuery("#grid-table").getGridParam("lastpage")){
						change = 'N';
					}
					nextPageNum = jQuery("#grid-table").getGridParam("lastpage");
				}					
				
				nextPageRow = jQuery('.ui-pg-selbox').val();
				
				if(change == 'Y'){
					jQuery('#grid-table').setGridParam(
						{
							url:'/mng/content/action/calendar.jspx?cmd=getCalendarList&'+jQuery("#list_form").serialize()+'&page='+nextPageNum
						}
						).trigger('reloadGrid');
				}
			}
			
		});
		resizeJqGridWidth('grid-table', 'widget_grid', '100%');
		jQuery('#gview_counsel_gridDiv > div .ui-jqgrid-sortable').css('font-size', '12px');
	}
	
//]]>
</script>
</head>
<body>	
<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="span12">
		<button type="button" id="_schedule_box" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg">달력보기</button>
		<button type="button" id="_schedule_list" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg">목록보기</button>
	</div>
    <div class="x_panel">
		<div class="x_content">
				<form name='list_form' id='list_form' method="POST" onsubmit="return false;">
				<input type="hidden" name="rows" id="rows" value="12" />
				<input type="hidden" name="cur_pg" id="cur_pg" value="1">
				<input type="hidden" name="row_per_page" id="row_per_page" value="10">
				<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
			 
					<table class="condition-table">
					<tbody>
						<tr class='last-tr'>
							<th width="120px"><div>등록일</div></th>
							<td width="120px">
								<span class="input-group textbox_width">
										<input class="date-picker W100P form-control" id="dt1" name="dt1"  type="text" value="${fromDate}" data-date-format="yyyy-mm-dd"/>
										<span class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</span>
								</span>
							</td>
							<td width="20px"> ~ </td>
							<td width="120px">
									<span class="input-group textbox_width">
										<input class="date-picker W100P form-control" id="dt2" name="dt2" type="text" value="${toDate}" data-date-format="yyyy-mm-dd"/>
										<span class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</span>
									</span>
							</td>	
							
							<th width="120px"><div>일정명</div></th>
							<td width="120px">
								<input class="W100P" type="text" id="search_word" name="search_word" style=""/>
							</td>
								
							<td>&nbsp;</td>
							<td width="200px">
								<div id="btn_search_List" class="btn btn-sm btn-warning" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><i class="icon-search"></i><b>조회</b></div>
								<div id="btnReset" class="btn btn-sm btn-success" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><i class="icon-refresh"></i><b>초기화</b></div>
							</td>
						</tr>
					</tbody>
					</table>
					
				</form>				
				</div>
			
			
				<div class="x_content">	
					<table class="table table-striped jambo_table bulk_action">
						<thead class="thin-border-bottom center" >
							
							<tr class="headings">
								<th width="60px">No</th>
								<th width="100px">구분</th>
								<th>구성방법</th>
								<th width="100px">게시형태</th>
								<th>제목</th>
								<th>링크</th>
								<th width="70px">등록일</th>
								<th width="70px">사용여부</th>
								<th width="70px">게시시작</th>
								<th width="70px">게시종료</th>
							</tr>
						</thead>
						<tbody id="listData">
							<tr><td colspan="10" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
						</tbody>
					</table>			
		      	</div>
		      	
		      	<div class="row">
				  	<div class="col-sm-5">
				  		<div class="dataTables_info" id="page_text_info" role="status" aria-live="polite">0 to 0 of 0 건</div>
				  	</div>
				  	<div class="col-sm-7">
				  		
				  		<div class="dataTables_paginate paging_simple_numbers" id="datatable-checkbox_paginate" style="float:left;text-align:left;">
					  		<ul class="pagination" id="pagingDiv" style="margin:0px;">
					  		</ul>
				  		</div>
				  	</div>
				  </div>		
		     	</div>
			</div>
	</div>
<div class="clearfix"></div>

<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      	<div class="x_title">
        	<h2>문의내용</h2>
	        <ul class="nav navbar-right panel_toolbox">
	          <!--<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>-->
	          <!--<li><a class="close-link"><i class="fa fa-close"></i></a></li> -->
	        </ul>
	        <div class="clearfix"></div>
      	</div>

      	<form id="input_form" name="input_form" method="post">
      	<div class="x_content" id="_preModify_table">
		  	<input type="hidden" id="seq_no" name="seq_no" >
		  	<input type="hidden" id="email" name="email" >
		  	<input type="hidden" id="title" name="title" >
		  	<input type="hidden" id="question" name="question" >
		  	<input type="hidden" id="user_nm" name="user_nm" value="">
			<table class="com_table_box">
				<tbody>
					<tr id="_div_reginfo_tr">
						<th width="80px">등록일자</th>
						<td><span name="reg_date" id="reg_date"></span></td>

						<th width="80px">등록자</th>
						<td><span name="reg_user" id="reg_user"></span></td>
					</tr>

					<tr>
						<th>구분</th>
						<td><span name="question_div_nm" id="question_div_nm"></span></td>

						<th>답변여부</th>
						<td><span name="answer_yn_nm" id="answer_yn_nm"></span></td>
					</tr>

					<tr>
						<th>이메일</th>						
						<td colspan="3"><span  id="str_email"></span></td>
					</tr>


					<tr>
						<th>제목</th>						
						<td colspan="3"><span  id="str_title"></span></td>
					</tr>

					<tr>
						<th>문의내용</th>
						<td colspan="3"><div  id="str_question"></div></td>
					</tr>


					<tr>
						<th>답변</th>
						<td colspan="3"  id="_answer_td">&nbsp;</td>
					</tr>

				</tbody>
			</table>
		</div>
		</form>	

		<div class="buttons" style="float:right" >
		  	<div id="_div_buttons" style="display:none">
				<button id="btn_delete" name="btn_delete"  type="button" class="btn btn-success btn-sm">삭제</button>
	            <button id="btn_save_answer" name="btn_save_answer" type="button"  class="btn btn-sm btn-primary" onclick="return false;">저장</button>
	        </div>
        </div>      	
    </div>
</div>

</body>
</html>