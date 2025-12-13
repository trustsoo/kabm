<%@page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>지회 검색</title>
<meta charset="utf-8"/>
	<META HTTP-EQUIV="Expires" CONTENT="-1">
	<META HTTP-EQUIV="pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
	<title>Ubicus(c) VOC</title>
	
	<!--inline styles related to this page-->
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="Ubicus VOC" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!--basic styles-->	
	<link href="/static/lib/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" />
	<link href="/static/lib/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet" />
	<link href="/static/lib/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="/static/lib/iCheck/skins/flat/green.css" />
	<link rel="stylesheet" href="/static/lib/animate.css/animate.min.css" />
	<link rel="stylesheet" href="/static/lib/nprogress/nprogress.css" />
	<!-- PNotify -->
    <link href="/static/lib/pnotify/dist/pnotify.css" rel="stylesheet">
    <link href="/static/lib/pnotify/dist/pnotify.buttons.css" rel="stylesheet">
    <link href="/static/lib/pnotify/dist/pnotify.nonblock.css" rel="stylesheet">
    <link href="/static/lib/jqgrid/css/ui.jqgrid.css" rel="stylesheet" />
    
	<!-- page specific plugin styles -->
	<link rel="stylesheet" href="/static/lib/bootstrap-daterangepicker/daterangepicker.css" />

	
	<!-- page styles -->
	<link rel="stylesheet" href="/static/com/css/templetDefault.css?_dummy=0.238777224472277" />
	<link rel="stylesheet" href="/static/mng/css/mngDefault.css?_dummy=0.238777224472277" />

	<script type="text/javascript">
		window.jQuery || document.write("<script src='/static/lib/jquery/dist/jquery.min.js'>"+"<"+"/script>");
	</script>
	
	<script type="text/javascript" src="/static/lib/bootstrap/dist/js/bootstrap.min.js"></script>
	
	<!-- PNotify -->
    <script src="/static/lib/pnotify/dist/pnotify.js"></script>
    <script src="/static/lib/pnotify/dist/pnotify.buttons.js"></script>
    <script src="/static/lib/pnotify/dist/pnotify.nonblock.js"></script>
    <!-- FastClick -->
    <script src="/static/lib/fastclick/lib/fastclick.js"></script>
    <!-- NProgress -->
    <script src="/static/lib/nprogress/nprogress.js"></script>
    <!-- iCheck -->
    <script src="/static/lib/iCheck/icheck.min.js"></script>
	
	<script type="text/javascript" src="/static/com/js/notification.js"></script>
	
	<!-- jqGrid scripts -->
	<script type="text/javascript" src="/static/lib/jqgrid/js/jquery.jqGrid.js"></script>
	<script type="text/javascript" src="/static/lib/jqgrid/js/i18n/grid.locale-kr.js"></script>
	<script type="text/javascript" src="/static/lib/jqgrid/plugins/jquery.contextmenu.js"></script>
	
	<!-- local scripts -->
	<script src="/static/com/js/ko.js"></script>

	<!-- common scripts -->
	<script src="/static/mng/js/mng_common.js"></script>
</head>
<script language="javascript">
	var subList = {};  //조회된 지회 정보 저장

	jQuery(document).ready(function(){
		
		js_getSubList(1);  

		$("#btn_search").click(function(){
			js_getSubList(1);
		});
		
		jQuery('#in_jisaname').keydown(function(e){
			if(e.keyCode == 13) {
				js_getSubList(1);
			}
		});
		
		jQuery('#in_daepyo').keydown(function(e){
			if(e.keyCode == 13) {
				js_getSubList(1);
			}
		});
	});

	function goSearch(cur_pg)
	{
		js_getSubList(cur_pg);
	}

	function js_getSubList(cur_pg)
	{
		$('#cur_pg').val(cur_pg);

		var _url = "/mng/content/action/subcomp.jspx?cmd=getSubList";	

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

		   		subList = {};  //지회 리스트 정보 저장 객체 
		   		
		   		var dataCount = 0;
				if( data.sub_list ) 
					dataCount =  data.sub_list.length;


		   		if (code == 200){		

		   			for(var i=0 ; i < dataCount ; i++)
		   			{
		   				//STR : 지회 수정에 사용할 데이터 보관
		   				var subCompItem = {};
		   					subCompItem['seq_no'] = data.sub_list[i].seq_no ;
							subCompItem['cp_code'] = data.sub_list[i].cp_code ;
							subCompItem['ji_code'] = data.sub_list[i].ji_code ;							
							subCompItem['jisaname'] = data.sub_list[i].jisaname ;
							subCompItem['daepyo'] = data.sub_list[i].daepyo ;
							
						var mapKey = data.sub_list[i].cp_code +"_" + data.sub_list[i].ji_code ;
							


						subList[mapKey] = subCompItem ;
		   			
						//END : 지회 수정에 사용할 데이터 보관


		   				var tdData = "";
		   				tdData = "<td style='text-align:center;'>"+ data.sub_list[i].rnum +"</td>" ;  //번호
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].jisaname +"</td>" ;  //지회명
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].daepyo +"</td>" ;  //지회장


		   				listDataRows.push("<tr style=\"cursor:pointer;\" onclick=js_setSubInfo('" + mapKey  + "');>" + tdData + "</tr>");
		   			}

		   			if(dataCount == 0 )
		   				listDataRows.push("<tr><td colspan='12' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 

		   		}else{
		   			listDataRows.push("<tr><td colspan='12' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}

				$("#listData").html(listDataRows.join());
				//msgStart("조회되었습니다", 'info'); 		 

				js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val(), 'goSearch');
				js_Paging_text('page_text_info', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val());  
			}			
		} );
	}

	function js_setSubInfo(mapKey)
	{
		var subItem = subList[mapKey];

		

		opener.if_search_subComp_CallBack(subItem.daepyo);
		window.close();

	}

</script>
<body>
	<div class="col-md-12 col-sm-12 col-xs-12">	
	
	    <div class="x_panel">     
			<div class="x_content">
			<form id="search_form" >
				<input type="hidden" name="cur_pg" id="cur_pg" value="1">
				<input type="hidden" name="row_per_page" id="row_per_page" value="5">
				<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>

				<table class="table table-striped table_layout_fixed">
					<tbody>
						<tr>
							<th width="70px">지회명</th>
							<td width="200px"><input type="text" name="in_jisaname" id="in_jisaname" value="" maxlength="30" class="form-control" ></td>

							<th width="70px"><div>지회장</div></th>
							<td width="150px"><input type="text" name="in_daepyo" id="in_daepyo" value=""  maxlength="10" class="form-control" ></td>

							<td>&nbsp;</td>
							<td width="80px">
								<!-- <button id="btn_search" name="btn_search" type="submit" class="btn btn-default btn-sm" onclick="return false;">조회</button> -->
								<div id="btn_search" class="btn btn-sm btn-warning" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><b>조회</b></div>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		    </div>

			

        
	      	<div class="x_content" style="height: 226px">			
				<table class="table table-striped table-bordered table-hover table_layout_fixed">
					<thead class="thin-border-bottom center" >
						
						<tr>
							<th width="60px">No</th>
							<th>지회명</th>
							<th>지회장</th>
						</tr>
					</thead>
					<tbody id="listData">
						<tr><td colspan="3" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
					</tbody>
				</table>			
	      	</div>
			


			<div class="row">
			  	<div class="dataTables_info" id="page_text_info" role="status" aria-live="polite">0 to 0 of 0 건</div>
			  	
			  	
			  	<div class="dataTables_paginate paging_simple_numbers" id="datatable-checkbox_paginate">
				  		<ul class="pagination" id="pagingDiv">
				  		</ul>
			  		</div>			  	
			  	</div>
	      	</div>
		</div>		
	</div>

	

	<div class="col-md-12 col-sm-12 col-xs-12">
    	<div class="x_panel">
	      	<div class="x_content">
	      		<div width="100%" style="float:right">
	      		<button type="submit" class="btn btn-default btn-sm" onclick="javascript:window.close();">닫기</button>
	      		</div>
			</div>
		</div>
	</div>

	<!-- 맨 아래 있어야 함  -->
	<script type="text/javascript" src="/static/com/js/common.js"></script>
</body>
</html>