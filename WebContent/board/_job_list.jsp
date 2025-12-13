<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%
String cur_pg 					= input.getText("cur_pg");
String recruit_div = input.getText("recruit_div");
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
			url : '/board/action/board.jspx?cmd=getJobList&recruit_div=<%=recruit_div%>',
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
				if( data.job_list ) 
					dataCount =  data.job_list.length;
		
				
				
				
		   		if(code == '200')
		   		{  			
		   			
		   			for(var i=0 ; i < data.job_list.length ; i++)
		   			{
		   				var tdData = "";
		   				var career = '';
		   				var gender = '';
		   				
		   				if(data.job_list[i].career_yn == 'Y') career ='경력';
		   				else if(data.job_list[i].career_yn == 'N') career ='신입';
		   				else if(data.job_list[i].career_yn == 'A') career ='무관';
		   				else career = '';
		   				
		   				if(data.job_list[i].gender == 'M') gender ='남자';
		   				else if(data.job_list[i].gender == 'F') gender ='여자';
		   				else gender = '';
		   				
<%
	if(recruit_div.equals("1"))
	{
%>
				tdData += "<td>"+ data.job_list[i].brd_no		+"</td>" ;		   				
				tdData += "<td class='tit'><div class='ellipsis'><a href=\"javascript:js_detail("+data.job_list[i].brd_no+");\">"+ data.job_list[i].ttl 			+"</a></div></td>" ;
				tdData += "<td>"+ data.job_list[i].job_title 		+"</td>" ;
				tdData += "<td>"+ data.job_list[i].user_nm 		+"</td>" ;
				tdData += "<td>"+ career		+"</td>" ;
				tdData += "<td>"+ data.job_list[i].reg_dt			+"</td>" ;
<%
		
	} else if(recruit_div.equals("2"))
	{
%>		   				
		   				
		   				tdData += "<td>"+ data.job_list[i].brd_no		+"</td>" ;		   				
		   				tdData += "<td class='tit'><div class='ellipsis'><a href=\"javascript:js_detail("+data.job_list[i].brd_no+");\">"+ data.job_list[i].ttl 			+"</a></div></td>" ;
		   				tdData += "<td>"+ data.job_list[i].job_title 		+"</td>" ;
		   				tdData += "<td>"+ data.job_list[i].user_nm 		+"</td>" ;
		   				tdData += "<td>"+ career		+"</td>" ;
		   				tdData += "<td>"+ gender		+"</td>" ;
		   				tdData += "<td>"+ data.job_list[i].reg_dt			+"</td>" ;
<%
	}
%>
		   				listDataRows.push("<tr>" + tdData + "</tr>");
		   			}
	
		   			if(data.job_list.length == 0 )
		   			{
		   				listDataRows.push("<tr><td colspan='7' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>");		   				
		   			}
		   			else
		   			{
		   				//js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');	
		   			}
		   				
	
		   		}else{
		   			listDataRows.push("<tr><td colspan='6' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}
		   		
	
				$("#listData").html(listDataRows.join(' '));
				js_userPaging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');		   			
		   		
			}
		});
	}
	
	function js_detail(brd_no)
	{
		location.href='/board/board.jspx?cmd=write_job&brd_mng_no=<%=input.getText("brd_mng_no")%>&recruit_div=<%=input.getText("recruit_div")%>&brd_no='+brd_no+'&cur_pg='+jQuery('#cur_pg').val();
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

	<div class="topSearch" style='clear:both;'>
		<div class="">
<form name="search_form" id='search_form'>
<input type="hidden" name="board_row_per_page" id="board_row_per_page" value='<%=input.getText("board_row_per_page")%>'/>
<input type="hidden" name="cur_pg" id="cur_pg" value='<%=cur_pg%>'/>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>		
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>
<input type="hidden" name="brd_mng_no" id="brd_mng_no" value='<%=input.getText("brd_mng_no")%>'/>


			<select name="search_type" title="search_type">
				<option value="ttl" selected="selected">제목+본문</option>
			</select>
			<input type="text" class="it " title="" value="" name="search_word" id="search_word"/>			
			<a href="javascript:js_list(1);" class="pbtn05"><span>검색</span></a>
</form>			
		</div>
	</div>
<%if("1".equals(input.getText("recruit_div")))
		{
%>
<p class="rStep_btn">
	<a href="/board/board.jspx?cmd=list_job&brd_mng_no=11&recruit_div=1" ><img src="/static/main/img/sub/job_tab_1_on.png" alt="구인" /></a>
	<a href="/board/board.jspx?cmd=list_job&brd_mng_no=12&recruit_div=2" style="margin-left: -20px;"><img src="/static/main/img/sub/job_tab_2_off.png" alt="구직" /></a>
</p>
<% }else{ %>
<p class="rStep_btn">
	<a href="/board/board.jspx?cmd=list_job&brd_mng_no=11&recruit_div=1" ><img src="/static/main/img/sub/job_tab_1_off.png" alt="구인" /></a>
	<a href="/board/board.jspx?cmd=list_job&brd_mng_no=12&recruit_div=2" style="margin-left: -20px;"><img src="/static/main/img/sub/job_tab_2_on.png" alt="구직" /></a>
</p>
<% } %>
	<div class="list">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
<%
		if("1".equals(input.getText("recruit_div")))
		{
%>			
			<colgroup>
				<col width="10%"/><col width=""/>
				<col width="15%"/><col width="15%"/><col width="15%"/>
			</colgroup>
			<thead>
				<th>번호</th>
				<th>제목</th>
				<th>직종</th>
				<th>이름</th>
				<th>경력/신입</th>
				<th>등록일</th>
			</thead>

<%
		} else if("2".equals(input.getText("recruit_div")))
		{
%>			
			<colgroup>
				<col width="10%"/><col width=""/>
				<col width="15%"/><col width="15%"/><col width="15%"/><col width="15%"/><col width="15%"/>
			</colgroup>
			<thead>
				<th>번호</th>
				<th>제목</th>
				<th>직종</th>
				<th>이름</th>
				<th>경력/신입</th>
				<th>성별</th>
				<th>등록일</th>
			</thead>
<%
		}
%>
			<tbody  id="listData">
				<tr><td colspan="7" style="text-align:center;">조회된 결과가 없습니다.</td></tr>				
			</tbody>
		</table>
	</div>

	<div class="paging" id='pagingDiv'>		
	</div>
	
	<div class="btnWrap" style='text-align:right !important;'>
			<a href="/board/board.jspx?cmd=write_job&brd_mng_no=<%=input.getText("brd_mng_no") %>&recruit_div=<%=input.getText("recruit_div") %>" class="pbtn02" ><span>등록</span></a>
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