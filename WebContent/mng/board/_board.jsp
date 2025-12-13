<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="jdf.framework.core.data.*"%>
<%@ page import="jdf.framework.core.util.*"%>
<%@ page import="jdf.framework.core.io.FormatedEntity"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
 <html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">

	jQuery(document).ready(function(){
		
		makeCodeSelectBox('board_type', '', 'view_type', false, true);
		
		jQuery('#btn-save').bind('click', function(e){ js_saveBrdMngDetail(); });
		jQuery('#btn-reg').bind('click', function(e){ js_newForm(); });
		jQuery('#btn-del').bind('click', function(e){ js_delBoard(); });
		
	});

	
	function js_requestBrdMngDetail(_brd_mng_no, e){
		
		var http = jQuery.ajax({
			url : '/mng/board/action/board.jspx?cmd=getBoardDetail',
			data : {
				brd_mng_no : _brd_mng_no
			},
			type : 'GET',
			async : false,
			datatype: 'json',
			error : function(xhr)
	   		{
				alert(xhr.status);
			},
			success:function(json) 
			{
				xmlDDD =json;
				console.log(json);
				var code = json.result.code;
		   		var msg =  json.result.msg;
		   		var data = json.result.data;
				
		   		if(code == '200')
		   		{		   			
		   			jQuery('#reg_dt').html(json.result.data.brd_mng_detail[0].reg_dt);	
		   			jQuery('#reg_emp_nm').html(json.result.data.brd_mng_detail[0].reg_emp_nm);
		   			jQuery('#brd_nm').val(json.result.data.brd_mng_detail[0].brd_nm);
		   			jQuery('#view_type').val(json.result.data.brd_mng_detail[0].view_type);
		   			jQuery('#fileupload_yn').val(json.result.data.brd_mng_detail[0].fileupload_yn);
		   			jQuery('#comment_yn').val(json.result.data.brd_mng_detail[0].comment_yn);
		   			jQuery('#brd_mng_no').val(json.result.data.brd_mng_detail[0].brd_mng_no);
				}
			}
		});		
	}
	
	
	function js_saveBrdMngDetail(){
		
		if(!checkFormField("form")) return;
		
		var http = jQuery.ajax({
			url : '/mng/board/action/board.jspx?cmd=saveBoardDetail',
			data : jQuery("#writeForm").serialize(true),
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
		   			msgStart(msg_mng_code_009, "success", null, 300, 110);
		   			location.href = '/mng/board/manager.jspx?cmd=board';
				}
			}
		});			
	}
	
	function js_delBoard()
	{
		
		if(confirm("게시판을 삭제 하시겠습니까?"))
		{
		
			var http = jQuery.ajax({
				url : '/mng/board/action/board.jspx?cmd=delBoard',
				data : jQuery("#writeForm").serialize(true),
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
			   			msgStart(msg_mng_code_009, "success", null, 300, 110);
			   			location.href = '/mng/board/manager.jspx?cmd=board';
					}
				}
			});	
		} else
		{}
	}
	
	
	
	function js_newForm()
	{
		jQuery("form").each(function() {  
            if(this.id == "writeForm") this.reset();  
        });
		
	}
	
</script>
</head>
<body>


<form name="form" id="form">
<input type="hidden" name="cur_pg" id="cur_pg" value="1">
<input type="hidden" name="row_per_page" id="row_per_page" value="10">
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
</form>

<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      <div class="x_title">
        <h2>게시판목록</h2>
        <ul class="nav navbar-right panel_toolbox">
          <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
          </li>
          <li><a class="close-link"><i class="fa fa-close"></i></a>
          </li>
        </ul>
        <div class="clearfix"></div>
      </div>

      <div class="x_content">
        <div class="table-responsive">
          <table class="table table-striped jambo_table bulk_action">
            <thead>
              <tr class="headings">                
                <th class="column-title">번호 </th>
                <th class="column-title">게시판명</th>
                <th class="column-title">게시판유형</th>
                <th class="column-title">파일업로드 </th>
                <th class="column-title">댓글허용 </th>
                <th class="column-title">게시물 수 </th>
                <th class="column-title">등록일 </th>
                <th class="column-title">게시판주소 </th>
              </tr>
            </thead>

            <tbody id="list_div">
<%
	String css = "";
	for(int idx=0;idx<output.getMaxDataSize();idx++)
	{
		if(idx%2 == 0)
		{
			css = "odd";
		} else
		{
			css = "even";
		}
%>            
              <tr class="<%=css%> pointer" style="cursor:pointer;" onClick="javascript:js_requestBrdMngDetail(<%=output.getText("brd_mng_no", idx)%>);">
              	<td><%=output.getText("brd_mng_no", idx)%></td>
              	<td><%=output.getText("brd_nm", idx)%></td>
              	<td><%=output.getText("view_type_nm", idx)%></td>
              	<td><%=output.getText("fileupload_yn", idx)%></td>
              	<td><%=output.getText("comment_yn", idx)%></td>
              	<td><%=output.getText("bbs_article_cnt", idx)%></td>
              	<td><%=output.getText("reg_dt", idx)%></td>
              	<td>/board/board.jspx?cmd=list&brd_mng_no=<%=output.getText("brd_mng_no", idx)%></td>              	
              </tr>
<%
	}
%>              
            </tbody>
          </table>
        </div>
        
		  
		  <div class="pull-right">
			<span id="btn-reg" class="btn btn-success" type="reset">신규등록</span>
          </div>
      </div>
    </div>
  </div>
  
  <div class="clearfix"></div>
  

     <div class="col-md-12 col-sm-12 col-xs-12">
       <div class="x_panel">
         <div class="x_title">
           <h2 id="edit_title">게시판 정보</h2>
           <ul class="nav navbar-right panel_toolbox">
             <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
             </li>             
             <li><a class="close-link"><i class="fa fa-close"></i></a>
             </li>
           </ul>
           <div class="clearfix"></div>
         </div>
         <div class="x_content">
           
           <form id="writeForm" name="writeForm">
           <input type='hidden' name='brd_mng_no' id='brd_mng_no' value='0'>
				<table class="com_table_box">
					<tr>
						<th style="width:110px;">등록일자</th>
						<td id='reg_dt'></td>
						<th style="width:110px;">등록자/수정자</th>
						<td id='reg_emp_nm'></td>
					</tr>
				    <tr>
				        <th>게시판명</th><td><input type="text" id="brd_nm" name="brd_nm" maxlength="20" class="form-control" ></td>
				        <th>게시판유형</th><td><select id="view_type" name="view_type" class="form-control" required><option value="">선택</option></select></td>
				    </tr>
					<tr>
				        <th>파일업로드</th>
				        <td>
				        	<select id="fileupload_yn" name="fileupload_yn" class="form-control" required>
				        	<option value="">선택</option>
				        	<option value="Y">허용</option>
				        	<option value="N">미허용</option>
				        	</select>
				        </td>
				        <th>댓글여부</th>
				        <td>
				        	<select id="comment_yn" name="comment_yn" class="form-control" required>
				        	<option value="">선택</option>
				        	<option value="Y">허용</option>
				        	<option value="N">미허용</option>
				        	</select>
				        </td>
				    </tr>
				    
				    
				</table>
			</form>
	
			<span class="pull-right" style="padding: 10px 20px 0 0;">
				<span id="btn-save" class="btn btn-sm btn-primary"><i class="icon-file"> 저장</i></span>
				<span id="btn-del" class="btn btn-sm btn-danger"><i class="icon-file"> 삭제</i></span>
			</span>
           
         </div>
       </div>
     </div>

</body>
</html>