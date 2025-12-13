<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<%
String cur_pg 					= input.getText("cur_pg");
if( cur_pg.equals("0") || cur_pg.equals(""))	cur_pg = "1";

String faq_no = input.getText("faq_no");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	jQuery(document).ready(function(){
		js_list(1);		
		
		var faq_no = '<%=faq_no%>';
		if(faq_no != '' )
		{
			$("#faq_"+faq_no).click();
		}
	});
	function js_list(cur_pg)
	{
		jQuery('#cur_pg').val(cur_pg);
		
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=getFaqList',
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
				console.log($('#tot_cnt').val());
	
		   		//$("#faq > li").remove();
		   		
	
		   		var listDataRows=[];
		   		
		   	
		   		var dataCount = 0;
				if( data.list ) 
					dataCount =  data.list.length;
		
				
		   		if(code == '200')
		   		{	
		   			for(var i=0 ; i < data.list.length ; i++)
		   			{
		   				jQuery('#aaaa').html(data.list[i].answer);
		   				
		   				var tdData = "";
		   				
		   				tdData += "<li>";
		   				tdData += "	<a href=\"#\" class='tit' id='faq_"+data.list[i].faq_no+"'>"+data.list[i].question+"</a>";
		   				tdData += "	<div class='answer' style='display:none'>";
		   				tdData += jQuery('#aaaa').text();						
		   				tdData += "	</div>";
		   				tdData += "</li>";
		   				listDataRows.push( tdData);
		   			}	   				
	
		   		}else{
		   			alert("[" + code + "] " + msg);		   			 
		   		}
		   		
				$("#faq").html(listDataRows.join(' '));
				
				js_userPaging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');		   			
		   		
				$("#faq a.tit").click(function(){
					$("div",this.parentNode).toggle();
				});
				
			}
		});
	}
	
	
	function js_changeCategory(cd)
	{
		jQuery('#category_cd').val(cd);		
		
		if(cd == '') cd = 'all';
		var selectID = null;
		var unSelectID = [];
		
		for(var k = 0; k<jQuery('#cateUL > li').length; k++)
		{
			if(jQuery(jQuery('#cateUL > li')[k]).find('a').attr('id') == cd)
			{
				selectID = cd;
			} else
			{
				unSelectID.push(jQuery(jQuery('#cateUL > li')[k]).find('a').attr('id'));
			}
		}
		
		jQuery('#'+selectID).attr('class', 'on');
		
		for(var k =0; k<unSelectID.length; k++)
		{
			jQuery('#'+unSelectID[k]).attr('class', '');
		}				
		js_list(1);
		
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
<form name="search_form" id='search_form'>
<input type="hidden" name="board_row_per_page" id="board_row_per_page" value='<%=input.getText("board_row_per_page")%>'/>
<input type="hidden" name="cur_pg" id="cur_pg" value='<%=cur_pg%>'/>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>		
<input type="hidden" name="category_cd" id="category_cd" value=''/>
<input type="hidden" name="start_dt" id="start_dt" value=''/>
<input type="hidden" name="end_dt" id="end_dt" value=''/>
<input type="hidden" name="use_yn" id="use_yn" value='Y'/>
</form>		
	
	<div class="tabWrap">
		<ul class="" id='cateUL'>
			<li><a href="javascript:js_changeCategory('');" class="on" id='all'><span class="">전체</span></a></li>
<%
	for(int idx=0;idx<output.getMaxDataSize(); idx++)
	{
%>
			<li><a href="javascript:js_changeCategory('<%=output.getText("cd", idx)%>');" class='' id='<%=output.getText("cd", idx)%>'><span class=""><%=output.getText("cd_nm", idx)%></span></a></li>
<%		
	}

%>				
					
		</ul>
	</div>

	<div class="faqWrap">
		<ul id="faq">
		</ul>
	</div>
	
	<div class="paging" id='pagingDiv'>		
	</div>
	
</div>
<div id='aaaa' style='display:none;'></div>
</body>
</html>