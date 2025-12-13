<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/call/common/common.jsp"%>
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%
	
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
		$(".date-picker").datepicker({
			format: 'yyyy-mm-dd',			
			todayHighlight: true,
			autoclose: true
		});
		YearSelectBox('year_code');
		MonthSelectBox('month_code');
		CreateSelectBox('/common/action/code.jspx?cmd=getEmpList', 'emp_no', 'emp_list', 'emp_no', 'emp_nm');
		
		gridStateInit();
		
		//조회
		jQuery('#btn_cnslt_email_hst_search').on('click',function(){
			gridStateInit();
			return false;
		});
		
		jQuery('#exel_down').bind('click', function(e){
			exportXls('/mng/statistics/action/page_stat.jspx?cmd=getPageStat&'+jQuery("#search_form").serialize());
		});
		
		auto_search(document, 'btn_cnslt_email_hst_search');
	});
	
	function gridStateInit()
	{
		jQuery("#widget_grid").html("<p><table id=\"grid-table\"></table><div id=\"grid-pager\"></div></p>");
		
		var _url = '/mng/statistics/action/page_stat.jspx?cmd=getPageStat&'+jQuery("#search_form").serialize()+'&page=<%=input.getText("cur_pg")%>&rows=<%=input.getText("row_per_page")%>';
		
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
				root: "Emp_work_state_list", 	//blockname			 
				id: "emp_no"	
			},
			colNames:['NO', '상담사번호', '상담사ID', '상담사', '○근무일', '● 지각', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', 
			          											'11', '12', '13','14','15','16','17','18','19','20',
			          											'21', '22', '23','24','25','26','27','28','29','30','31'
			          											],
			colModel:[
					  {name:'no',index:'no', width:20, sortable:false, align:'center'},
					  {name:'emp_no',index:'emp_no', hidden:true, sortable:false, align:'center'},
			          {name:'user_id',index:'user_id', width:55, sortable:false, align:'center'},
			          {name:'emp_nm',index:'emp_nm', width:50, sortable:false, align:'center'},
			          {name:'work_day',index:'work_day', width:40, sortable:false, align:'center'},
			          {name:'late_day',index:'late_day', width:35, sortable:false, align:'center'},
			          {name:'d01',index:'d01', width:30, sortable:false, align:'center'},
			          {name:'d02',index:'d02', width:30, sortable:false, align:'center'},
			          {name:'d03',index:'d03', width:30, sortable:false, align:'center'},
			          {name:'d04',index:'d04', width:30, sortable:false, align:'center'},
			          {name:'d05',index:'d05', width:30, sortable:false, align:'center'},
			          {name:'d06',index:'d06', width:30, sortable:false, align:'center'},
			          {name:'d07',index:'d07', width:30, sortable:false, align:'center'},
			          {name:'d08',index:'d08', width:30, sortable:false, align:'center'},
			          {name:'d09',index:'d09', width:30, sortable:false, align:'center'},
			          {name:'d10',index:'d10', width:30, sortable:false, align:'center'},
			          {name:'d11',index:'d11', width:30, sortable:false, align:'center'},
			          {name:'d12',index:'d12', width:30, sortable:false, align:'center'},
			          {name:'d13',index:'d13', width:30, sortable:false, align:'center'},
			          {name:'d14',index:'d14', width:30, sortable:false, align:'center'},
			          {name:'d15',index:'d15', width:30, sortable:false, align:'center'},
			          {name:'d16',index:'d16', width:30, sortable:false, align:'center'},
			          {name:'d17',index:'d17', width:30, sortable:false, align:'center'},
			          {name:'d18',index:'d18', width:30, sortable:false, align:'center'},
			          {name:'d19',index:'d19', width:30, sortable:false, align:'center'},
			          {name:'d20',index:'d20', width:30, sortable:false, align:'center'},
			          {name:'d21',index:'d21', width:30, sortable:false, align:'center'},
			          {name:'d22',index:'d22', width:30, sortable:false, align:'center'},
			          {name:'d23',index:'d23', width:30, sortable:false, align:'center'},
			          {name:'d24',index:'d24', width:30, sortable:false, align:'center'},
			          {name:'d25',index:'d25', width:30, sortable:false, align:'center'},
			          {name:'d26',index:'d26', width:30, sortable:false, align:'center'},
			          {name:'d27',index:'d27', width:30, sortable:false, align:'center'},
			          {name:'d28',index:'d28', width:30, sortable:false, align:'center'},
			          {name:'d29',index:'d29', width:30, sortable:false, align:'center'},
			          {name:'d30',index:'d30', width:30, sortable:false, align:'center'},
			          {name:'d31',index:'d31', width:30, sortable:false, align:'center'}
			         ],  
			height: 577,
			rowNum:999,
			rowList:999,
			autowidth: true,
			//pager : jQuery('#grid-pager'),
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
				console.log(data);
				var table = this;
				updatePagerIcons(table);
				updateNavGridButton('grid-table', 'grid-pager', table);
				
				jQuery('.tab-content').css("height", jQuery('.tab-content').height());
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
								url:'/mng/statistics/action/page_stat.jspx?cmd=getPageStat&'+jQuery("#search_form").serialize()+'&page='+nextPageNum+'&rows='+nextPageRow
							}
							).trigger('reloadGrid');
				}
			}
		});
		
		jQuery('#gview_counsel_gridDiv > div .ui-jqgrid-sortable').css('font-size', '12px');

	}
	
//]]>
</script>
</head>
<body>
	
	<div class="row-fluid">
		<div class="span12">
			<div class="widget-box">
			 	
				<div class="widget-body border_default">
					<div class="widget-main">
						<form name='search_form' id='search_form' onSubmit="javascript:return false;">
		 				    
							<table class="table_input_form_full" border-collapse="collapse" cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="100px"><col width="200px"><col width="100px"><col width="120px"><col width="">
							</colgroup>
							<tbody>
							<tr>
								<th><font>근무월</font></th>
								<td>
									<select id="year_code" name="year_code" class="default"></select>
									<select id="month_code" name="month_code" class="default"></select>
								</td>
								<th><font>상담사</font></th>
								<td>
									<select id="emp_no" name="emp_no" class="default W100P">
									<option value="999999">::전체::</option>
								</select>
								</td>
								<td>
									<span class="pull-right">
										<button id="exel_down" class="btn btn-xs btn-success">
										<i class="icon- fa fa-table bigger-120" title="XLS 다운로드" style="padding:5px 0px 0px 2px;"></i>
										엑셀다운로드
									</button>
									&nbsp;
									<button id="btn_cnslt_email_hst_search" class="btn btn-xs btn-primary btn_right">조회</button>
									</span>
								</td>
							</tr>
						</tbody></table>
						</form>	
					</div><!-- /widget-main -->
				</div><!-- /widget-body -->
			</div>
			
			<div class="widget-box">
				<div class="widget-body border_default">
					<div class="widget-main">
	
						<div class="tab-content" style="padding: 0px 0px; overflow-y: hidden; ">
							<div id="widget_grid" class="tab-pane in active" style="width:1524px;">
								<table id="grid-table" class="grid_body"></table>
								<div id="grid-pager"></div>
							</div>
						</div>
				
					</div><!-- /widget-main -->
				</div><!-- /widget-body -->
			</div><!-- /widget-box -->
		</div>
	</div>
	

</body>
</html>