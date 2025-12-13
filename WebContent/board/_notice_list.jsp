<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%
String cur_pg 					= input.getText("cur_pg");
if( cur_pg.equals("0") || cur_pg.equals(""))	cur_pg = "1";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	jQuery(document).ready(function(){
		js_list(1);
	});
	
	function checkForEnter(event)
	{
		if ((event.which && event.which == 13) || (event.keyCode && event.keyCode == 13))
   		{
			js_list(1);
		}
	}
	
	function js_list(cur_pg)
	{
		jQuery('#cur_pg').val(cur_pg);
		
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=getNoticeList',
			data : jQuery("#search_form").serialize(true),
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
	
	
		   		$("#listData > tr").remove();
		   		
	
		   		var listDataRows=[];
		   		
		   	
		   		var dataCount = 0;
				if( data.notice_list ) 
					dataCount =  data.notice_list.length;
		
				
				
				
		   		if(code == '200')
		   		{  			
		   			
		   			for(var i=0 ; i < data.notice_list.length ; i++)
		   			{
		   				var imp_yn = data.notice_list[i].imp_yn;
		   				var tdData = "";
		   				tdData += "<td>"+ data.notice_list[i].brd_no		+"</td>" ;
		   				tdData += "<td>"+ data.notice_list[i].notice_div_nm		+"</td>" ;
		   				if( imp_yn == 'Y' ){
		   					tdData += "<td class='tit' style='font-weight:bold;'><div class='ellipsis long'><a href=\"javascript:js_detail("+data.notice_list[i].brd_no+","+data.notice_list[i].brd_mng_no+");\">"+ '<i class="fa fa-bell tblue"></i> ' + data.notice_list[i].ttl 			+"</a></div></td>" ;
		   				}else{
		   					tdData += "<td class='tit'><div class='ellipsis long'><a href=\"javascript:js_detail("+data.notice_list[i].brd_no+","+data.notice_list[i].brd_mng_no+");\">"+ data.notice_list[i].ttl 			+"</a></div></td>" ;
		   				}	
		   				
		   				tdData += "<td>"+ data.notice_list[i].emp_nm 		+"</td>" ;
		   				tdData += "<td>"+ data.notice_list[i].reg_ddtm			+"</td>" ;
		   				listDataRows.push("<tr>" + tdData + "</tr>");
		   			}
	
		   			if(data.notice_list.length == 0 )
		   			{
		   				listDataRows.push("<tr><td colspan='5' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>");		   				
		   			}
		   			else
		   			{
		   				//js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');	
		   			}
		   				
	
		   		}else{
		   			listDataRows.push("<tr><td colspan='5' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}
		   		
	
				$("#listData").html(listDataRows.join(' '));
				js_userPaging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');		   			
		   		
			}
		});
	}
	
	function js_detail(brd_no, brd_mng_no)
	{
		
		location.href='/board/board.jspx?cmd=notice_view&brd_no='+brd_no+'&brd_mng_no='+brd_mng_no;
	}
	
</script>

</head>
<body>
<div id="content">
<!-- start :: content -->
<!-- start :: content -->
	<div class="topSearch">
		<div class="">
<form name="search_form" id='search_form' onsubmit="return false;">
<input type="hidden" name="board_row_per_page" id="board_row_per_page" value='<%=input.getText("board_row_per_page")%>'/>
<input type="hidden" name="cur_pg" id="cur_pg" value='<%=cur_pg%>'/>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>		
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>
			<select name="search_type" title="search_type">
				<option value="ttl" selected="selected">제목</option>
			</select>
			<input type="text" class="it " title="" value="" name="search_txt" id="search_txt" onKeyUp="checkForEnter(event);"/>			
			<a href="javascript:js_list(1);" class="pbtn05"><span>검색</span></a>
</form>			
		</div>
	</div>
	
	<div class="list">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="10%"/><col width="15%"/>
				<col width=""/><col width="15%"/><col width="15%"/>
			</colgroup>
			<thead>
				<tr><th>번호</th><th>구분</th><th>제목</th><th>작성자</th><th>작성일자</th></tr>
			</thead>
			<tbody  id="listData">
				<tr><td colspan="5" style="text-align:center;">조회된 결과가 없습니다.</td></tr>				
			</tbody>
		</table>
	</div>

	<div class="paging" id='pagingDiv'>
		<a href="#" class="btn"><img src="/static/main/img/sub/paging_btn_1.jpg" alt="" /></a>
		<a href="#" class="btn"><img src="/static/main/img/sub/paging_btn_2.jpg" alt="" /></a>
		<span class="">
			<strong>1</strong>
			<a href="#">2</a>
			<a href="#">3</a>
			<a href="#">4</a>
			<a href="#">5</a>
			<a href="#">6</a>
			<a href="#">7</a>
			<a href="#">8</a>
			<a href="#">9</a>
		</span>
		<a href="#" class="btn"><img src="/static/main/img/sub/paging_btn_3.jpg" alt="" /></a>
		<a href="#" class="btn"><img src="/static/main/img/sub/paging_btn_4.jpg" alt="" /></a>
		
	</div>

<!-- end :: content -->
<!-- end :: content -->
</div>
</body>
</html>