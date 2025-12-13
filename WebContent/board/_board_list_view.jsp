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
	
	
		   		$("#listData > tr").remove();
		   		
	
		   		var listDataRows=[];
		   		
		   	
		   		var dataCount = 0;
				if( data.board_list ) 
					dataCount =  data.board_list.length;
		
				
				
				
		   		if(code == '200')
		   		{  			
		   			
		   			for(var i=0 ; i < data.board_list.length ; i++)
		   			{
		   				var tdData = "";
		   				tdData += "<td>"+ data.board_list[i].brd_no		+"</td>" ;		   				
		   				tdData += "<td class='tit'><div class='ellipsis long'><a href=\"javascript:js_detail("+data.board_list[i].brd_no+");\">"+ data.board_list[i].ttl 			+"</a></div></td>" ;
		   				tdData += "<td>"+ data.board_list[i].emp_nm 		+"</td>" ;
		   				tdData += "<td>"+ data.board_list[i].read_cnt		+"</td>" ;
		   				tdData += "<td>"+ data.board_list[i].reg_dt			+"</td>" ;
		   				listDataRows.push("<tr>" + tdData + "</tr>");
		   			}
	
		   			if(data.board_list.length == 0 )
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
	
	function js_detail(brd_no)
	{
		
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=doBoardRead',
			data : 'brd_no='+brd_no,
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
					jQuery('#reg_dt').html(json.result.data.board_detail[0].reg_dt);
		   			jQuery('#reg_emp_nm').html(json.result.data.board_detail[0].emp_nm);
		   			jQuery('#ttl').html(json.result.data.board_detail[0].ttl);
		   			jQuery('#aaaa').html(json.result.data.board_detail[0].cntnt);
		   			jQuery('#cntnt').html('');
		   			jQuery('#cntnt').html(jQuery('#aaaa').text());		   	
		   			jQuery('#read_cnt').html(json.result.data.board_detail[0].read_cnt);
		   			jQuery('#email').html(json.result.data.board_detail[0].email);
		   			
		   			if(json.result.data.board_detail[0].is_exist_file == 'Y')
		   			{
		   				js_getFileEntries('gridFileEntriesDiv',false, jQuery('#attach_div_cd').val(), json.result.data.board_detail[0].brd_no);
		   				//jQuery('#file_view').css('display', '');
		   			}
		   			
		   			jQuery('#viewForm').css('display', '');
		   			jQuery('#listBtn').css('display', '');
		   			
		   			//CKEDITOR.instances._cntnt.setData(jQuery('#aaaa').text());
		   			
		   			/*if(json.result.data.board_detail[0].is_exist_file == 'Y')
		   			{
		   				js_getFileEntries('gridFileEntriesDiv',true, jQuery('#attach_div_cd').val(), json.result.data.board_detail[0].brd_no);
		   				jQuery('#file_view').css('display', '');
		   			}
		   			
		   			jQuery('#viewForm').css('display', '');
		   			
		   			js_ContentsMode(json.result.data.board_detail[0].view_type);*/
		   			
		   			
				}
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
	<div style='clear:both;float:left;margin-bottom:10px;'>
<%
	if("11".equals(input.getText("brd_mng_no")))
	{
%>
	
		<p class="rStep_btn">		
			<a href="/board/board.jspx?cmd=list&brd_mng_no=11" style="margin-left:-20px;"><img src="/static/main/img/sub/job_tab_1_on.png" alt="구인" /></a>
			<a href="/board/board.jspx?cmd=list&brd_mng_no=12" style="margin-left:-20px;"><img src="/static/main/img/sub/job_tab_2_off.png" alt="구직" /></a>
		</p>

<%
	} else if("12".equals(input.getText("brd_mng_no")))
	{
%>
		<p class="rStep_btn">		
			<a href="/board/board.jspx?cmd=list&brd_mng_no=11" style="margin-left:-20px;"><img src="/static/main/img/sub/job_tab_1_off.png" alt="구인" /></a>
			<a href="/board/board.jspx?cmd=list&brd_mng_no=12" style="margin-left:-20px;"><img src="/static/main/img/sub/job_tab_2_on.png" alt="구직" /></a>
		</p>
<%		
	}
%>
	</div>	
	<div class="topSearch" style='clear:both;'>
		<div class="">
<form name="search_form" id='search_form'>
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
			<input type="text" class="it " title="" value="" name="search_word" id="search_word"/>			
			<a href="javascript:js_list(1);" class="pbtn05"><span>검색</span></a>
</form>			
		</div>
	</div>
	
	<div class="list">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="10%"/><col width=""/>
				<col width="15%"/><col width="15%"/><col width="15%"/>
			</colgroup>
			<thead>
				<tr><th>번호</th><th>제목</th><th>작성자</th><th>조회수</th><th>작성일자</th></tr>
			</thead>
			<tbody  id="listData">
				<tr><td colspan="5" style="text-align:center;">조회된 결과가 없습니다.</td></tr>				
			</tbody>
		</table>
	</div>

	<div class="paging" id='pagingDiv'>		
	</div>


	<div class="view" id='viewForm' style='display:none;'>
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="165"/><col width="300"/>
				<col width="165"/><col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">이 름</th>
					<td id='reg_emp_nm'></td>
					<th scope="row">이메일</th>
					<td id='email'></td>
				</tr>
				<tr>
					<th scope="row">작성일</th>
					<td id='reg_dt'></td>
					<th scope="row">조회수</th>
					<td id='read_cnt'></td>
				</tr>
				<tr>
					<th scope="row">파일첨부</th>
					<td colspan="3">
						<div class="margin_B_10">				
							<div id="gridFileEntriesDivMain" style="display:; max-height:250px; overflow-y:auto;">	
								<div class="span6" id="gridFileEntriesDiv" style="width:100%"></div>
							</div>
						</div>
					
					</td>
				</tr>
				<tr>
					<th scope="row">제목</th>
					<td colspan="3" id='ttl' ></td>
				</tr>
				<tr>
					<td colspan="4" class="cont" id='cntnt'>
						
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="paging" id='listBtn' style='display:none;'>
		<div class="rbtn">
			<a href="javascript:js_closeView();" class="pbtn02 mid"><span class="list">리스트</span></a>
		</div>
	</div>
	
	<div id='aaaa' style='display:none;'></div>

<!-- end :: content -->
<!-- end :: content -->
</div>
</body>
</html>