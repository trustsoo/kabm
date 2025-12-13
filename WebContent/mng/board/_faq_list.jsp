<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="start_dt" type="java.lang.String" scope="request" />
<jsp:useBean id="end_dt" type="java.lang.String" scope="request" />
<%
String cur_pg 					= input.getText("cur_pg");
if( cur_pg.equals("0") || cur_pg.equals(""))	cur_pg = "1";
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/static/lib/ckeditor/ckeditor.js" charset="UTF-8"></script>
<title>Insert title here</title>

<script type="text/javascript" type="text/javascript">
//<![CDATA[	
	
	jQuery(document).ready(function(){
		$(".date-picker").datepicker({
			format: 'yyyy-mm-dd',			
			todayHighlight: true,
			autoclose: true
		});
		
		makeCodeSelectBox('faq_category', '', 'category_cd', false, true);		
		makeCodeSelectBox('faq_category', '', 'category_cd2', false, true);
		
		
		jQuery('#btn-save').bind('click',function(){			
			js_detail_save();
		})		
		
		jQuery('#search_btn').bind('click', function(){js_list(1)});
		
		jQuery('#btn-cancel').bind('click', function(){
			
			jQuery('#viewForm').css('display', 'none');
			js_writeFormReset();
			
		});
		
		jQuery('#btn-del').bind('click', function(){
			
			if( !confirm(msg_com_code_107) ) return;
			
			jQuery('#viewForm').css('display', 'none');
			
			js_faq_remove();
			
		});
		
		jQuery('#btn-reg').bind('click', function(){
			
			jQuery('#viewForm').css('display', '');
			
			js_writeFormReset();
			
		});
		
		js_list(1);
		
	});
	
	function js_faq_remove()
	{
		
			var http = jQuery.ajax({
				url : '/mng/board/action/faq.jspx?cmd=doFaqDel',
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
			   			location.href = '/mng/board/community.jspx?cmd=faq_list';
					}
				}
			});	
		
	}
	
	
	function js_writeFormReset()
	{
		jQuery('#reg_dt').html('');
		jQuery('#reg_emp_nm').html('');
		jQuery('#faq_no').val(-1);
		jQuery('#question').val('');
		jQuery('#answer').val('');
		jQuery('#category_cd2').val('');
		jQuery('#use_yn2').val('');		
		jQuery('#ttl').val('');	
		
		
		jQuery('#aaaa').html('');
		CKEDITOR.instances._answer.setData(jQuery('#aaaa').text());
	}
	
	
	
	
	//db에 저장
	function js_detail_save()
	{
		
		if(!checkFormField("form")) return;
		
		jQuery("#answer").val(CKEDITOR.instances._answer.getData());
		if( jQuery("#answer").val() == '' )
		{
			alert('내용을 입력하세요' );
			return;
		}
		
		
		var http = jQuery.ajax({
			url : '/mng/board/action/faq.jspx?cmd=doFaqAdd',
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
		   			js_writeFormReset();
		   			js_list('<%=cur_pg%>');
				}
			}
		});	
	}
	
	
	
	function js_detail(faq_no)
	{
		
		var http = jQuery.ajax({
			url : '/mng/board/action/faq.jspx?cmd=doBoardRead',
			data : 'faq_no='+faq_no,
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
		   			jQuery('#reg_dt').html(data.detail[0].reg_ddtm);
		   			jQuery('#reg_emp_nm').html(data.detail[0].emp_nm);
		   			jQuery('#faq_no').val(data.detail[0].faq_no);	   			
		   			jQuery('#answer').val('');
		   			jQuery('#category_cd2').val(data.detail[0].category_cd);
		   			
		   			jQuery('#use_yn2').val(data.detail[0].use_yn);		   			
		   			jQuery('#question').val(data.detail[0].question);		   			
		   			
		   			jQuery('#aaaa').html(data.detail[0].answer);
		   			CKEDITOR.instances._answer.setData(jQuery('#aaaa').text());
		   			
		   			jQuery('#viewForm').css('display', '');
				}
			}
		});	
	}
	
	function js_list(cur_pg)
	{
		jQuery('#cur_pg').val(cur_pg);
		
		var http = jQuery.ajax({
			url : '/mng/board/action/faq.jspx?cmd=doFaqList',
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
				if( data.list ) 
					dataCount =  data.list.length;
		
		   		if(code == '200')
		   		{  			
		   			
		   			for(var i=0 ; i < data.list.length ; i++)
		   			{
		   				var tdData = "";
		   				tdData += "<td style='text-align:center;'>"+ data.list[i].faq_no 			+"</td>" ; 		   				
		   				tdData += "<td style='text-align:center;'>"+ data.list[i].category_nm 	+"</td>" ; 		   				
		   				tdData += "<td style='text-align:left;'>"+ data.list[i].question 			+"</td>" ;
		   				tdData += "<td style='text-align:center;'>"+ data.list[i].reg_ddtm			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.list[i].emp_nm 		+"</td>" ;
		   				
		   				
		   				listDataRows.push("<tr style=\"cursor:pointer;\" onclick=js_detail('" + data.list[i].faq_no  + "');>" + tdData + "</tr>");
		   			}

		   			if(data.list.length == 0 )
		   				listDataRows.push("<tr><td colspan='5' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 

		   		}else{
		   			listDataRows.push("<tr><td colspan='5' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}
		   		

				$("#listData").html(listDataRows.join());
				//msgStart("조회되었습니다", 'info'); 		 

				js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');
				js_Paging_text('page_text_info', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val());  		   			
		   		
			}
		});
	}

		
	
	
	
//]]>
</script>

<style>
.P-L90  {padding-left:90px;}
</style>

</head>
<body>			 	


<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      

      <div class="x_content">
      
      
      <table class="condition-table">
<form name="search_form" id='search_form'>
<input type="hidden" name="board_row_per_page" id="board_row_per_page" value='<%=input.getText("board_row_per_page")%>'/>
<input type="hidden" name="cur_pg" id="cur_pg" value='<%=cur_pg%>'/>
<input type="hidden" name="p_reg_nm" id="p_reg_nm" value=''/>
<input type="hidden" name="search_word" id="search_word" value=''/>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>


			<tr>
				<th width="150px">
					<div>카톄고리</div>
				</th>
				<td width="150px">
					<select id="category_cd" name="category_cd">
						<option value="">::전체::</option>
					</select>
				</td>			
				<td width="20px"></td>
				<th width="150px">
					<div>사용여부</div>
				</th>
				<td width="150px" colspan="4">
					<select id="use_yn" name="use_yn">
						<option value="">::전체::</option>
						<option value="Y">사용</option>
						<option value="N">미사용</option>
					</select>
				</td>
				
			</tr>
			<tr>				
				<th width="150px">
					<div>등록일</div>
				</th>				
				<td width="120px">
					<span class="input-group textbox_width">
							<input class="date-picker W100P form-control" id="start_dt" name="start_dt" type="text" value="<%=start_dt %>" data-date-format="yyyy-mm-dd"/>
							<span class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</span>
					</span>
				</td>
				<td width="20px"> ~ </td>
				<td width="120px">
						<span class="input-group textbox_width">
							<input class="date-picker W100P form-control" id="end_dt" name="end_dt" type="text" value="<%=end_dt %>" data-date-format="yyyy-mm-dd"/>
							<span class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</span>
						</span>
				</td>	
				<td>
					<div id="search_btn" class="btn btn-sm btn-warning" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><b>조회</b></div>
				</td>
			</tr>
</form>			
		</table>
        
        <div class="table-responsive">
          <table class="table table-striped jambo_table bulk_action">
            <thead>
              <tr class="headings">               
                <th class="column-title">번호 </th>
                <th class="column-title">카테고리</th>
                <th class="column-title">질문</th>
                <th class="column-title">등록일</th>
                <th class="column-title">등록자 </th>
              </tr>
            </thead>

            <tbody id="listData">
					<tr><td colspan="5" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
				</tbody>
          </table>
        </div>
        
        <div class="row">
		  	<div class="col-sm-5">
		  		<div class="dataTables_info" id="page_text_info" role="status" aria-live="polite">0 to 0 of 0 건</div>
		  	</div>
		  	<div class="col-sm-7">
		  		<div class="pull-right">
					<span id="btn-reg" class="btn btn-success" type="reset">신규등록</span>
		          </div>
		  		<div class="dataTables_paginate paging_simple_numbers" id="datatable-checkbox_paginate" style="float:left;text-align:left;">
			  		<ul class="pagination" id="pagingDiv" style="margin:0px;">
			  		</ul>
		  		</div>
		  	</div>
		  </div>
		  
      </div>
    </div>
  </div>
  
  <div class="clearfix"></div>
  

     <div class="col-md-12 col-sm-12 col-xs-12" style='display:none;' id='viewForm'>
       <div class="x_panel">
         <div class="x_title">
           <h2 id="edit_title">게시물 상세</h2>
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
<input type="hidden" name="faq_no" id="faq_no" value='-1'/>   
<input type="hidden" name="answer" id="answer" value=''/> 

				<table class="com_table_box">
					<tr>
						<th style="width:110px;">등록일자</th>
						<td id='reg_dt'></td>
						<th style="width:110px;">등록자/수정자</th>
						<td id='reg_emp_nm'></td>
					</tr>
				    <tr>
				        <th>카테고리</th><td><select id="category_cd2" name="category_cd" class="form-control" required="true" alt="카테고리"><option value="">선택</option></select></td>
				        <th>사용여부</th><td><select id="use_yn2" name="use_yn" class="form-control" required="true" alt="사용여부"><option value="">선택</option><option value="Y">사용</option><option value="N">미사용</option></select></td>
				    </tr>					
				    <tr>
				        <th>질문</th><td colspan="3"><input type="text" id="question" name="question" maxlength="300" class="form-control" required="true"></td>
				    </tr>
				    <tr id='cntnt_form'>
				        <th>내용</th><td colspan="3"><textarea rows="5" cols="800" id="_answer" name="answer" title="내용" required="true"></textarea></td>
				    </tr>

<script type="text/javascript" isELIgnored="false">
    //<![CDATA[
    CKEDITOR.replace('_answer', {            	
    	filebrowserImageUploadUrl : '/bin/FileUploader',
		height:200
   	});	
    //]]>
</script>			
				    
				</table>
			</form>
	
			<span class="pull-right" style="padding: 10px 20px 0 0;">
				<span id="btn-save" class="btn btn-sm btn-primary">
				<i class="icon- fa fa-save"> 저장</i>					 
				</span>								
				<span id="btn-cancel" class="btn btn-sm btn-default">
					<i class="icon- fa fa-undo"> 취소</i> 
				</span>
				<span id="btn-del" class="btn btn-sm btn-dark">
					<i class="icon- fa fa-save"> 삭제</i>					 
				</span>	
			
				
			</span>
           
         </div>
       </div>
     </div>
     
     <div id='aaaa' style="display:none;"></div>
</body>




</html>