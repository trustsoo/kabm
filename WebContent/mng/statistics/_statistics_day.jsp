<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>

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
		js_getList(1);  

		$("#btn_search").click(function(){
			js_getList(1);
		});
	});

		
	


	function goSearch(cur_pg)
	{
		js_getList(cur_pg);
	}

	
	function js_getList(cur_pg)
	{
		var fromDate    = $("#in_ddtm_from").val().replace(/-/gi,"");
		var toDate 		= $("#in_ddtm_to").val().replace(/-/gi,"");
		

		if(   fromDate.length != 8 || toDate.length != 8  || fromDate > toDate  )
		{
			msgStart("조회 날짜를 확인해 주세요", 'info');  
			return;
		}
		
		$('#cur_pg').val(cur_pg);

		var _url = "/mng/statistics/action/staticstics_day.jspx?cmd=getTotalList";	


		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'json',
	   		data : jQuery("#search_form").serialize(), 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(Json)
			{			
				var code = Json.result.code;
		   		var msg =  Json.result.msg;
		   		var data = Json.result.data;

				if(Json.result.data.property[0].tot_cnt )
				{
						$('#tot_cnt').val(Json.result.data.property[0].tot_cnt);
				} else {
						$('#tot_cnt').val(0);
				}


		   		$("#listData > tr").remove();
		   		var listDataRows=[];
		   		var chartDataRows=[];

		   		
		   		

		   		if (code == 200){		

		   			for(var i=0 ; i < data.statics_list.length ; i++)
		   			{

		   				var tdData = "";
		   				tdData = "<td style='text-align:center;'>"+ data.statics_list[i].log_date +"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.statics_list[i].sum_pv +"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.statics_list[i].sum_uv +"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.statics_list[i].memberCount +"</td>" ; 
		   				
		   				
		   				listDataRows.push("<tr>" + tdData + "</tr>");

		   				chartDataRows.push({
		   							'log_date':data.statics_list[i].log_date , 
		   							'pageview':data.statics_list[i].sum_pv , 
		   							'usercount':data.statics_list[i].sum_uv,
		   							'memberCount': data.statics_list[i].memberCount
		   						});
		   			}

		   			if(data.statics_list.length == 0 )
		   				listDataRows.push("<tr><td colspan='3' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 

		   		}else{
		   			listDataRows.push("<tr><td colspan='3' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}

				$("#listData").html(listDataRows.join());
				//msgStart("조회되었습니다", 'info'); 		 

				js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val(), 'goSearch');
				js_Paging_text('page_text_info', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val());  	

				init_morris_charts(chartDataRows) ;
			}			
		} );

		
	}


	function init_morris_charts(chartData) {
			
			if( typeof (Morris) === 'undefined'){ return; }


			
			$('#graph_bar_group').html("");	
			
			if ($('#graph_bar_group').length ){
				
				Morris.Bar({
				  element: 'graph_bar_group',
				  data: chartData
				  /* [
					{"log_date": "2016-10-01", "pageview": 807, "usercount": 660},
					{"log_date": "2016-09-30", "pageview": 1251, "usercount": 729},
					{"log_date": "2016-09-29", "pageview": 1769, "usercount": 1018},
					{"log_date": "2016-09-20", "pageview": 2246, "usercount": 1461},
					{"log_date": "2016-09-19", "pageview": 2657, "usercount": 1967},
					{"log_date": "2016-09-18", "pageview": 3148, "usercount": 2627},
					{"log_date": "2016-09-17", "pageview": 3471, "usercount": 3740},
					{"log_date": "2016-09-16", "pageview": 2871, "usercount": 2216},
					{"log_date": "2016-09-15", "pageview": 2401, "usercount": 1656},
					{"log_date": "2016-09-10", "pageview": 2115, "usercount": 1022}
				  ]*/,
				  xkey: 'log_date',
				  barColors: ['#26B99A', '#34495E', '#ACADAC', '#3498DB'],
				  ykeys: ['pageview', 'usercount'],
				  labels: ['페이지뷰', '로그인 User'],
				  hideHover: 'auto',
				  xLabelAngle: 60,
				  resize: true
				});
			}


			$('#graph_line').html("");	
			if ($('#graph_line').length ){
			
				Morris.Line({
				  element: 'graph_line',
				  xkey: 'log_date',
				  ykeys: ['pageview', 'usercount'],
				  labels: ['페이지뷰', '로그인 User'],
				  hideHover: 'auto',
				  lineColors: ['#26B99A', '#34495E', '#ACADAC', '#3498DB'],
				  data: chartData
				  /*[
					{"log_date": "2016-10-01", "pageview": 807, "usercount": 660},
					{"log_date": "2016-09-30", "pageview": 1251, "usercount": 729},
					{"log_date": "2016-09-29", "pageview": 1769, "usercount": 1018},
					{"log_date": "2016-09-20", "pageview": 2246, "usercount": 1461},
					{"log_date": "2016-09-19", "pageview": 2657, "usercount": 1967},
					{"log_date": "2016-09-18", "pageview": 3148, "usercount": 2627},
					{"log_date": "2016-09-17", "pageview": 3471, "usercount": 3740},
					{"log_date": "2016-09-16", "pageview": 2871, "usercount": 2216},
					{"log_date": "2016-09-15", "pageview": 2401, "usercount": 1656},
					{"log_date": "2016-09-10", "pageview": 2115, "usercount": 1022}
				  ]*/,
				  xLabelAngle: 60,
				  resize: true
				});
			}
		};
//]]>
</script>
</head>
<body>


<div class="col-md-12 col-sm-12 col-xs-12">

    <div class="x_panel">
		<div class="x_content">
		<form id="search_form" >
			<input type="hidden" name="cur_pg" id="cur_pg" value="1">
			<input type="hidden" name="row_per_page" id="row_per_page" value="100">
			<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
			
			<table class="condition-table">
				<tbody>
					<tr class='last-tr'>
						<th width="100px"><div>조회일자</div></th>						
						<td width="120px">
							<span class="input-group textbox_width">
									<input class="date-picker W100P form-control" id="in_ddtm_from" name="in_ddtm_from"  type="text" value="${fromDate}" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
							</span>
						</td>
						<td width="10px"> ~ </td>
						<td width="120px">
								<span class="input-group textbox_width">
									<input class="date-picker W100P form-control" id="in_ddtm_to" name="in_ddtm_to" type="text" value="${toDate}" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
								</span>
						</td>
						<td width="10px">&nbsp;</td>

						<th width="100px"><div>구분</div></th>
						<td width="100px">
							<select id="in_search_gbn" name="in_search_gbn" class="form-control" >
						    <option value="daytotal">일별</option>						  
						    <option value="monthtotal" selected=true>월별</option>
							</select>
						</td>

						<td>&nbsp;</td>

						<td width="80px">
							<div id="btn_search" class="btn btn-sm btn-warning" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><b>조회</b></div>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	    </div>

         

      	<div class="x_content">	

			<div class="x_content2">
            	<div id="graph_bar_group" style="width:100%; height:300px;" ></div>
            	<!--<div id="graph_line" style="width:100%; height:300px;" ></div>-->
            	
          	</div>
		
			<table class="table table-striped table-bordered table-hover table_layout_fixed">
				<thead class="thin-border-bottom center" >
					
					<tr>
						<th width="200px" class="column-title">일자</th>
						<th width="300px">페이지뷰</th>
						<th>로그인 User</th>
						<th>신규가입</th>
					</tr>
				</thead>
				<tbody id="listData">
					<tr><td colspan="3" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
				</tbody>
			</table>			
      	</div>


		<div class="row">
		  	<div class="col-sm-5">
		  		<div class="dataTables_info" id="page_text_info" role="status" aria-live="polite">0 to 0 of 0 건</div>
		  	</div>
		  	<div class="col-sm-7">
		  		<div class="dataTables_paginate paging_simple_numbers" id="datatable-checkbox_paginate">
			  		<ul class="pagination" id="pagingDiv">
			  		</ul>
		  		</div>
		  	</div>
		  </div>
     	</div>

    </div>
</div>	



<div class="clearfix"></div>


<script src="/static/lib/raphael/raphael.min.js"></script>
<script src="/static/lib/morris.js/morris.min.js"></script>

</body>
</html>