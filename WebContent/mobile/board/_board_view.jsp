<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<%
String brd_no 					= input.getText("brd_no");
String comment_yn			= output.getText("comment_yn");
String fileupload_yn			= output.getText("fileupload_yn");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/static/main/css/board.css?_=1128" rel="stylesheet">
<script>
	
	function js_detail()
	{
		var brd_no = jQuery('#brd_no').val();
		var http = jQuery.ajax({
			url : '/mobile/board/action/board.jspx?cmd=doBoardRead',
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
		   			
		   			var fileupload_yn = json.result.data.board_detail[0].fileupload_yn;
		   			var comment_yn = json.result.data.board_detail[0].comment_yn;
		   			
		   			if(fileupload_yn == 'Y' && json.result.data.board_detail[0].is_exist_file == 'Y')
		   			{
		   				js_getFileEntriesMobile('gridFileEntriesDiv', jQuery('#attach_div_cd').val(), json.result.data.board_detail[0].brd_no);
		   			}
		   			
				}
			}
		});	
	}
	
	
	function js_getFileEntriesMobile(target_div, attach_div_cd, data_no)
	{	
		var listEntries =target_div;
		
		jQuery.ajax({
			url: '/common/action/attach.jspx?cmd=getAttachList',
			data: 'attach_div_cd=' + attach_div_cd + '&data_no=' + data_no,
			type: 'POST',
			dataType: 'json',
			success: function(jsonObj)
			{
				if(jsonObj.result.code == 200){
					var listHtml = [];
					var json = jsonObj.result.data.file_entries;
					var jsonCnt = jsonObj.result.data.file_entries.length;
					
					if(jsonCnt > 0){
						
						
						for(var i=0; i<jsonCnt; i++) {
							listHtml.push('<div class="mail_list" onclick="location.href=\'/common/action/attach.jspx?cmd=doDownload&file_no=' + json[i].file_no +'\'">' );   
							listHtml.push('<div class="left">'+getFileImg(json[i].ext_nm)+'</div>');
							listHtml.push('<div class="right">');
							listHtml.push('	<h3>'+json[i].user_file_nm+' <small>'+grid_fmt_fieSize(json[i].file_size)+'</small></h3>');
							listHtml.push('</div>');
							listHtml.push('</div>');
						}
						
						
					} 
					
					jQuery("#"+listEntries).html(listHtml.join(''));
					listHtml = [];
				}
			}
		});
	}
	
	jQuery(document).ready(function(){
		js_detail();
		
	});
</script>

</head>
<body>
<div id="content">
<!-- start :: content -->
<!-- start :: content -->
<form name="frm" id="frm">
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>
<input type="hidden" name="brd_mng_no" id="brd_mng_no" value='<%=input.getText("brd_mng_no")%>'/>
<input type="hidden" name="brd_no" id="brd_no" value='<%=input.getText("brd_no")%>'/>
<input type="hidden" name='board_comment_page_cnt' id='board_comment_page_cnt' value='5'/>
<input type="hidden" name='pre_page_comment_cnt' id='pre_page_comment_cnt' value='1'/>
<input type="hidden" name='pre_page' id='pre_page' value='1'/>
<input type="hidden" name='last_page' id='last_page' value=''/>
<input type="hidden" name="user_id" id="user_id" value='<%=input.getText("user_id")%>'/>

</form>


<div class="mail_view" style="border-left: 0;">
	<div class="inbox-body">
		<div class="mail_heading">			
			<span class="date" id='reg_dt'> </span>
			<h4 id='ttl'> </h4>
		</div>
		
		<div class="view-mail" id='cntnt'>
		</div>
		<div class="attachment">
		
		<%if(fileupload_yn.equals("Y")) {%>
		 
		<div id="gridFileEntriesDiv" style="width:100%"></div>
		
		<%} %>
		</div>
	
	</div>
</div>

<div id='aaaa' style='display:none;'></div>

<!-- end :: content -->
<!-- end :: content -->
</div>
</body>
</html>