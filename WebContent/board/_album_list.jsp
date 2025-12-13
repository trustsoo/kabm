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
<style>

.imgBoard img{
max-width: 100%;
    padding: 5px;
    border: 1px solid #ccc;
    height: auto;
    background: #fff;
    box-shadow: 1px 1px 7px rgb
}    
</style>
<script>
	jQuery(document).ready(function(){
		js_list(1);
	});
	
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
	
	
		   		$("#listUL > li").remove();
		   		
	
		   		var listDataRows=[];
		   		
		   	
		   		var dataCount = 0;
				if( data.board_list ) 
					dataCount =  data.board_list.length;
		
				
		   		if(code == '200')
		   		{  			
		   			var thumb_url = '<%=input.getText("thumb_url")%>';
		   			var thumb_url = '<%=input.getText("thumb_url")%>';
		   			for(var i=0 ; i < data.board_list.length ; i++)
		   			{
		   				var tdData = "";
		   				
		   				tdData += "<li>";
		   				tdData += "<a href='javascript:fn_viewMax(" +data.board_list[i].file_no+ ");'>";
		   				tdData += "	<img src='" + thumb_url + "/" +data.board_list[i].file_path + "/" +data.board_list[i].file_nm + "' alt=''/>";
		   				tdData += "<span class=''>";
		   				tdData +=  "<em>["+data.board_list[i].reg_dt+"]</em>";
		   				tdData +=  data.board_list[i].ttl;		   				
		   				tdData +=  "</span>";
		   				tdData +=  "</a>";
		   				tdData +=  "</li>";
		   				listDataRows.push(tdData);
		   			}
	
		   			if(data.board_list.length == 0 )
		   			{
		   				listDataRows.push("<li>조회된 결과가 없습니다.</li>");		   				
		   			}
		   			else
		   			{
		   				//js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');	
		   			}
		   				
	
		   		}else{
		   			listDataRows.push("<li style='text-align:center;'>[" + code + "] " + msg + " </li>"); 
		   		}
		   		
				$("#listUL").html(listDataRows.join(' '));
				js_userPaging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');		   			
		   		
			}
		});
	}
	
	function js_closeView()
	{
		$('#ImagePop').hide();
		jQuery('#overlay_t').hide();
	}
	
	function fn_viewMax(file_no)
	{
		var _url = '/common/action/attach.jspx?cmd=doDownload&file_no=' + file_no;
		$("#load").html('');
		$("#load").scrollTop(0);
		$("#load").html('<img src="'+_url+'">');
		
		jQuery('#overlay_t').show();
		$("#ImagePop").show();
		//return false;
	}
	
</script>
</head>
<body>
<div id="content">
<!-- start :: content -->
<!-- start :: content -->

	<ul class="imgBoard" id="listUL">		
	</ul>

	<div class="paging" id="pagingDiv">
	</div>

<!-- end :: content -->
<!-- end :: content -->

	<div class="ImagePop" id="ImagePop" style="display:none">
		<a href="#" onclick="js_closeView();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" width="22" height="22" alt="close" /></a>
		<div id="load" class="loadCont">
			
		</div>
	</div>
	
</div>
<form name="search_form" id='search_form'>
<input type="hidden" name="board_row_per_page" id="board_row_per_page" value='<%=input.getText("board_row_per_page")%>'/>
<input type="hidden" name="cur_pg" id="cur_pg" value='<%=cur_pg%>'/>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>		
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>
<input type="hidden" name="brd_mng_no" id="brd_mng_no" value='<%=input.getText("brd_mng_no")%>'/>
<input type="hidden" name="p_reg_nm" id="p_reg_nm" value=''/>
<input type="hidden" name="start_dt" id="start_dt" value='2017-01-01'/>
<input type="hidden" name="end_dt" id="end_dt" value='9999-12-31'/>
<input type="hidden" name="search_type" id="search_type" value=''/>
<input type="hidden" name="search_word" id="search_word" value=''/>
<input type="hidden" name="album_yn" id="album_yn" value='Y'/>
</form>	


<script language="javascript" type="text/javascript">
//<![CDATA[



//]]>
</script>
		
</body>
</html>