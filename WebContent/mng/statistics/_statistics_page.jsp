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

		var _url = "/mng/statistics/action/staticstics_page.jspx?cmd=getTotalList";	


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

		   		$("#listData > tr").remove();
		   		var listDataRows=[];

		   		if (code == 200){		

		   			for(var i=0 ; i < data.statics_list.length ; i++)
		   			{

		   				var tdData = "";
		   				tdData = "<td style='text-align:center;'>"+ data.statics_list[i].menu_nm +"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.statics_list[i].total_cnt +"</td>" ; 
		   				
		   				listDataRows.push("<tr>" + tdData + "</tr>");
		   			}

		   			if(data.statics_list.length == 0 )
		   				listDataRows.push("<tr><td colspan='2' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 

		   		}else{
		   			listDataRows.push("<tr><td colspan='2' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}

				$("#listData").html(listDataRows.join());
			}			
		} );

		
	}


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
						<!--
						<th width="100px"><div>구분</div></th>
						<td width="100px">
							<select id="in_search_gbn" name="in_search_gbn" class="form-control" >
						    <option value="daytotal">일별</option>						  
						    <option value="monthtotal" selected=true>월별</option>
							</select>
						</td>
						-->

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
			<!--
			<div class="x_content2">
            	<div id="graph_bar_group" style="width:100%; height:300px;" ></div>
          	</div>
          	-->
		
			<table class="table table-striped table-bordered table-hover table_layout_fixed">
				<thead class="thin-border-bottom center" >
					
					<tr>
						<th width="200px" class="column-title">페이지</th>
						<th width="300px">접속건수</th>
					</tr>
				</thead>
				<tbody id="listData">
					<tr><td colspan="3" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
				</tbody>
			</table>			
      	</div>
    </div>
</div>	

<div class="clearfix"></div>

</body>
</html>