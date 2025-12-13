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
			url : '/board/action/board.jspx?cmd=getBoardList',
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
	
		   		var listDataRows=[];
		   				   	
		   		var dataCount = 0;
				if( data.board_list ) 
					dataCount =  data.board_list.length;
						
		   		
				$("#qna").html(listDataRows.join(' '));
				
				js_userPaging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');		   			
		   		
				$("#qna a.tit").click(function(){
					$("div",this.parentNode).toggle();
				});
								
				
		   		if(code == '200')
		   		{  					   			
		   			for(var i=0 ; i < data.board_list.length ; i++)
		   			{
		   				var imp_yn = data.board_list[i].imp_yn;
		   				jQuery('#aaaa').html(data.board_list[i].cntnt);
						var tdData = "";
		   				
		   				tdData += "<li>";
		   				
		   				if( imp_yn == 'Y' ){
		   					tdData += "	<a href=\"#\" class='tit' style='font-weight:bold;' id='qna_"+data.board_list[i].brd_no+"'>"+ '<i class="fa fa-bell tblue"></i> ' + data.board_list[i].ttl+"</a>";
		   				}else{
		   					tdData += "	<a href=\"#\" class='tit' id='qna_"+data.board_list[i].brd_no+"'>"+ data.board_list[i].ttl+"</a>";
		   				}
		   				
		   				
		   				tdData += "	<div class='answer' style='display:none'>";
		   				tdData += jQuery('#aaaa').text();						
		   				tdData += "	</div>";
		   				tdData += "</li>";
		   				listDataRows.push( tdData);
		   						   				
		   			}	
	
		   		}else{
		   			alert("[" + code + "] " + msg);	
		   		}
		   		
				$("#qna").html(listDataRows.join(' '));
				//js_userPaging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');		   			
		   		
				$("#qna a.tit").click(function(){
					$("div",this.parentNode).toggle();
				});
				
			}
		});
	}
	
	function js_closeView()
	{
		jQuery('#viewForm').css('display', 'none');
		jQuery('#listBtn').css('display', 'none');
	}
</script>

</head>
<body>
<div id="content">
<!-- start :: content -->
<!-- start :: content -->

<!-- <input type="hidden" name="search_type" id="search_type" value='ttl'/>
<input type="hidden" name="search_word" id="search_word" value=''/> -->

	<div class="topSearch" style='clear:both;'>
		<div class="">
<form name="search_form" id='search_form' onsubmit="return false;">
<input type="hidden" name="board_row_per_page" id="board_row_per_page" value='<%=input.getText("board_row_per_page")%>'/>
<input type="hidden" name="cur_pg" id="cur_pg" value='<%=cur_pg%>'/>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>		
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>
<input type="hidden" name="brd_mng_no" id="brd_mng_no" value='<%=input.getText("brd_mng_no")%>'/>
<input type="hidden" name="p_reg_nm" id="p_reg_nm" value=''/>
<input type="hidden" name="start_dt" id="start_dt" value=''/>
<input type="hidden" name="end_dt" id="end_dt" value=''/>

			
			<select name="search_type" title="search_type">
				<option value="ttl" selected="selected">제목+본문</option>
			</select>
			<input type="text" class="it " title="" value="" name="search_word" id="search_word" onKeyUp="checkForEnter(event);"/>			
			<a href="javascript:js_list(1);" class="pbtn05"><span>검색</span></a>
</form>			
		</div>
	</div>
	
	<div class="qnaWrap">
		<ul id="qna">
		</ul>
	</div>
	
	


<!-- end :: content -->
<!-- end :: content -->
</div>
<div id='aaaa' style='display:none;'></div>
</body>
</html>