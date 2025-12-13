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
<link href="/static/main/css/board.css?_=1127" rel="stylesheet">
<title>Insert title here</title>

<script type="text/javascript" type="text/javascript">
//<![CDATA[	
	
	 var uploader = null;
	
	jQuery(document).ready(function(){
		
		$(".date-picker").datepicker({
			format: 'yyyy-mm-dd',			
			todayHighlight: true,
			autoclose: true
		});
		
		makeSelectBoxCodeFromURL('/mng/board/action/community.jspx?cmd=getAllBoardMngNo', 'brd_mng_no', false);
		makeSelectBoxCodeFromURL('/mng/board/action/community.jspx?cmd=getAllBoardMngNo', 'brd_mng_no2', false);
		makeCodeSelectBox('board_type', '', 'view_type', false, true);
		makeCodeSelectBox('file_div', '', 'brd_div', false, true);		
		
		jQuery('#btn-save').bind('click',function(){
			
			fn_cnslt_detail_upload_save();
		})
		
		
		jQuery('#search_btn').bind('click', function(){js_list(1)});
		
		jQuery('#btn-cancel').bind('click', function(){
			
			jQuery('#file_view').css('display', 'none');			
			jQuery('#viewForm').css('display', 'none');
			js_writeFormReset();
			
		});
		
		
		jQuery('#btn-reg').bind('click', function(){
			
			jQuery('#file_view').css('display', 'none');			
			jQuery('#viewForm').css('display', '');
			
			js_writeFormReset();
			
		});
		
		jQuery('#btn-del').bind('click', function(){
			
			if( !confirm(msg_com_code_107) ) return;
			
			jQuery('#file_view').css('display', 'none');			
			jQuery('#viewForm').css('display', 'none');
			
			js_board_remove();
			
		});
		
		jQuery('#brd_mng_no2').bind('change', function(){			
			jQuery('#view_type').val(jQuery("#brd_mng_no2 option:selected").attr('etc1'));
			js_ContentsMode(jQuery('#view_type').val());
			js_attachMode(jQuery("#brd_mng_no2 option:selected").attr('etc2'));
		});
		
		js_list(1);
		
	});
	
	function js_ContentsMode(val)
	{
		if(val =='board_type_02') jQuery('#cntnt_form').css('display', 'none');
		else jQuery('#cntnt_form').css('display', '');
	}
	
	function js_attachMode(yn)
	{
		if(yn == 'Y')
			jQuery('#fileUploadForm').css('display', '');
		else
			jQuery('#fileUploadForm').css('display', 'none');
	}
	
	
	function js_writeFormReset()
	{
		jQuery('#reg_dt').html('');
		jQuery('#reg_emp_nm').html('');
		jQuery('#brd_no').val(-1);
		jQuery('#p_brd_no').val(-1);
		jQuery('#cntnt').val('');
		jQuery('#brd_mng_no2').val('');
		jQuery('#view_type').val('');
		jQuery('#brd_div').val('');
		jQuery('#ttl').val('');	
		jQuery('#imp_yn').val('N');	
		
		jQuery('#file_nms').val('');	
		jQuery('#file_paths').val('');	
		jQuery('#file_sizes').val('');	
		jQuery('#ext_nms').val('');	
		jQuery('#user_file_nms').val('');	
		
		
		
		jQuery('#aaaa').html('');
		CKEDITOR.instances._cntnt.setData(jQuery('#aaaa').text());
	}
	
	
	
	function fn_cnslt_detail_upload_save()
	{	
		uploader.startUpload(function(errorCount){
            console.log('errorCount:'+errorCount);
            if(errorCount == 0){
            	js_detail_save();
            }else{
                msgStart(errorCount+'개의 파일이 업로드되지 못했습니다.', "error", null, 300, 110);
            }
        });
		
	}
	
	//db에 저장
	function js_detail_save()
	{
		jQuery("#cntnt").val(CKEDITOR.instances._cntnt.getData());
		var view_type = jQuery('#view_type').val();
		
		var http = jQuery.ajax({
			url : '/mng/board/action/community.jspx?cmd=doBoardAdd&view_type='+view_type,
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
		   			if(uploader != null)
		   				uploader.resetUpload();
				}
			}
		});	
	}
	
	
	
	function js_detail(brd_no)
	{		
		if(uploader != null)
			uploader.resetUpload();
		
		var http = jQuery.ajax({
			url : '/mng/board/action/community.jspx?cmd=doBoardRead',
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
		   			js_attachMode(json.result.data.board_detail[0].fileupload_yn);
		   			js_ContentsMode(json.result.data.board_detail[0].view_type);
		   			
		   			jQuery('#reg_dt').html(json.result.data.board_detail[0].reg_dt);
		   			jQuery('#reg_emp_nm').html(json.result.data.board_detail[0].emp_nm);
		   			jQuery('#brd_no').val(json.result.data.board_detail[0].brd_no);
		   			jQuery('#p_brd_no').val(json.result.data.board_detail[0].brd_no);
		   			jQuery('#cntnt').val('');
		   			jQuery('#brd_mng_no2').val(json.result.data.board_detail[0].brd_mng_no);
		   			jQuery('#imp_yn').val(json.result.data.board_detail[0].imp_yn);
		   			
		   			jQuery('#view_type').val(json.result.data.board_detail[0].view_type);
		   			jQuery('#brd_div').val(json.result.data.board_detail[0].brd_div);
		   			jQuery('#ttl').val(json.result.data.board_detail[0].ttl);		   			
		   			
		   			jQuery('#aaaa').html(json.result.data.board_detail[0].cntnt);
		   			CKEDITOR.instances._cntnt.setData(jQuery('#aaaa').text());
		   			
		   			if(json.result.data.board_detail[0].is_exist_file == 'Y')
		   			{
		   				js_getFileEntries('gridFileEntriesDiv',true, jQuery('#attach_div_cd').val(), json.result.data.board_detail[0].brd_no);
		   				jQuery('#file_view').css('display', '');
		   			}
		   			
		   			jQuery('#viewForm').css('display', '');
		   			
		   			jQuery('#last_page').val('');
		        	jsRequestCommentRead(brd_no);
				}
			}
		});	
	}
	
	function js_list(cur_pg)
	{
		jQuery('#cur_pg').val(cur_pg);
		
		var http = jQuery.ajax({
			url : '/mng/board/action/community.jspx?cmd=doBoardList',
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
		   				var imp_yn = data.board_list[i].imp_yn;
		   				var tdData = "";
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].brd_no 			+"</td>" ; 		   				
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].brd_nm 	+"</td>" ; 		 
		   				if( imp_yn == 'Y' ){
		   					tdData += "<td style='text-align:center;font-weight:bold;'>"+ '[중요]'+ data.board_list[i].ttl 			+"</td>" ; 
		   				}else{
		   					tdData += "<td style='text-align:center;'>"+ data.board_list[i].ttl 			+"</td>" ; 
		   				}
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].reg_dt			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].emp_nm 		+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].read_count 			+"</td>" ; 
		   				
		   				
		   				listDataRows.push("<tr style=\"cursor:pointer;\" onclick=js_detail('" + data.board_list[i].brd_no  + "');>" + tdData + "</tr>");
		   			}

		   			if(data.board_list.length == 0 )
		   				listDataRows.push("<tr><td colspan='6' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 

		   		}else{
		   			listDataRows.push("<tr><td colspan='6' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}
		   		

				$("#listData").html(listDataRows.join());
				//msgStart("조회되었습니다", 'info'); 		 

				js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');
				js_Paging_text('page_text_info', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val());  		   			
		   		
			}
		});
	}
	
	//삭제
	function js_board_remove()
	{
		
		var http = jQuery.ajax({
			url : '/mng/board/action/community.jspx?cmd=doDelBoard',
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
		   			jQuery('#btn-cancel').click();
		   			js_list('<%=cur_pg%>');
				}
			}
		});	
	}
		
	/*댓글 조회*/	
	var jsRequestCommentRead = function(brd_no)
	{
		var board_comment_page_cnt = jQuery('#board_comment_page_cnt').val();
		
		var _url = '/board/action/board.jspx?cmd=doCommentRead&brd_no='+brd_no;
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : jQuery('#comment_form').serialize(true),		
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(xmlDoc)
			{	
				var code = jQuery(xmlDoc).find('code').text();
		        var msg = jQuery(xmlDoc).find('msg').text();

		        if(code == '200')
		        {
		        	var comment_list = [];
					jQuery(xmlDoc).find('comment_info').each(function(){
						comment_list.push("<div class=\"itemdiv dialogdiv\" id=\"comment_"+jQuery(this).find('cmnt_no').text()+"\">");
						comment_list.push("	 <div class=\"user\" style=\"margin-top:10px\">");
						comment_list.push("	   		<span class=\"blue\"> "+jQuery(this).find('user_nm').text()+"</span>");
						comment_list.push("	 </div>");
						comment_list.push("	 <div class=\"body comment_content_box\">");
						comment_list.push("	 	<div style=\"\" >"); 
            			comment_list.push("			<span class=\"orange\">"+jQuery(this).find('reg_dt').text()+"</span>");

            			
                        comment_list.push("         <span style=\"float:right;padding-left:5px\">");
                        comment_list.push("             <span onClick=\"javascript:jsRequestCommentDel("+jQuery(this).find('cmnt_no').text()+","+brd_no+")\" style=\"cursor:pointer;font-size:15px;color:red; font-weight:bold;\">X</span>");
                        comment_list.push("         </span>");
                        				
						
						comment_list.push("		</div>");
						
						comment_list.push("	 	<div class=\"text P-T5\">");
						comment_list.push(			jQuery(this).find('cntnt').text());
						comment_list.push("		</div>");
						comment_list.push("	 </div>");
						comment_list.push("</div>");
						jQuery('#last_page').val(jQuery(this).find('last_page').text());
						
						if(jQuery(this).find('rnum').text()%board_comment_page_cnt == 0){
							jQuery('#pre_page_comment_cnt').val(board_comment_page_cnt);	
						}
						else{
							jQuery('#pre_page_comment_cnt').val(jQuery(this).find('rnum').text()%board_comment_page_cnt);	
						}
					});
					
					jQuery('#board_comments').html(comment_list.join(""));
					
					if(jQuery('#last_page').val()>0){
						var comment_page = [];
						
						comment_page.push("<ul class=\"pagination\">");
						comment_page.push("		<li id=\"pagination_prev\">");
						comment_page.push("			<a href=\"javascript:comment_page('P' , "+brd_no+");\">");
						comment_page.push("				<i class=\"icon- fa fa-angle-double-left\"></i>");
						comment_page.push("			</a>");
						comment_page.push("		</li>");
						
						for(var ins=1;ins<=jQuery('#last_page').val();ins++){
							comment_page.push("		<li id=\"pagination_"+ins+"\">");
							comment_page.push("			<a href=\"javascript:comment_page("+ins+", "+brd_no+");\">"+ins+"</a>");
							comment_page.push("		</li>");
						}
							
						comment_page.push("		<li id=\"pagination_next\">");
						comment_page.push("			<a href=\"javascript:comment_page('N' , "+brd_no+");\">");
						comment_page.push("				<i class=\"icon- fa fa-angle-double-right\"></i>");
						comment_page.push("			</a>");
						comment_page.push("		</li>");
						comment_page.push("</ul>");
						comment_page.push("<p class=\"hr_line\"></p>");
					
						jQuery('#board_comments_page').html(comment_page.join(""));
					}else{
						
						jQuery('#board_comments_page').html('');
					}
					
					jQuery('.pagination').css('margin', '5px');
					jQuery('#pagination_'+jQuery('#pre_page').val()).addClass("active");
					if(jQuery('#pre_page').val() == jQuery('#last_page').val()){
						jQuery('#pagination_next').addClass("disabled");
					}
					if(jQuery('#pre_page').val() == 1){
						jQuery('#pagination_prev').addClass("disabled");
					}
		        } else
		        {
		        	msgStart(msg_com_code_010+"("+msg+")");
		        }
			}			
		});	
	};		
	
	
	/*댓글 삭제*/
	var jsRequestCommentDel = function(cmnt_no, brd_no)
	{
		if(!confirm("댓글을 삭제하시겠습니까?")) return
			
		var _url = '/board/action/board.jspx?cmd=doCommentDel';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : {
					use_cmnt_no:cmnt_no,
					brd_no:brd_no
				   },
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(xmlDoc)
			{	
				jQuery('#comment_'+cmnt_no).remove();
				if(jQuery('#pre_page').val() == 1){
					comment_page('P', brd_no);
				}
				comment_page(jQuery('#pre_page').val() , brd_no);
				msgStart(msg_board_code_003);
			}			
		});	
	}
	
	/*댓글 페이지 이동*/
	var comment_page = function(mv_page, brd_no){

		if(mv_page=='P'){
			if(jQuery('#pre_page').val() > 1){
				jQuery('#pre_page').val(parseInt(jQuery('#pre_page').val())-1);
				jsRequestCommentRead(brd_no);
			}
		} else if(mv_page=='N'){
			if(jQuery('#pre_page').val() < jQuery('#last_page').val()){
				jQuery('#pre_page').val(parseInt(jQuery('#pre_page').val())+1);
				jsRequestCommentRead(brd_no);
			}
		} else{
			jQuery('#pre_page').val(mv_page);
			jsRequestCommentRead(brd_no);
		}
		
	};
	
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
					<div>게시판</div>
				</th>
				<td width="150px" colspan='4'>
					<select id="brd_mng_no" name="brd_mng_no">
						<option value="">::전체::</option>
					</select>
				</td>			
			</tr>
			<tr class="last-tr">
	
				<th>
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
                <th class="column-title">게시판</th>
                <th class="column-title">제목</th>
                <th class="column-title">등록일</th>
                <th class="column-title">등록자 </th>
                <th class="column-title">조회수 </th>
                
              </tr>
            </thead>

            <tbody id="listData">
					<tr><td colspan="6" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
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
<input type="hidden" name='file_nms' id='file_nms' value=''/>
<input type="hidden" name='file_paths' id='file_paths' value=''/>
<input type="hidden" name='file_sizes' id='file_sizes' value=''/>
<input type="hidden" name='ext_nms' id='ext_nms' value=''/>
<input type="hidden" name='user_file_nms' id='user_file_nms' value=''/>
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>      
<input type="hidden" name="brd_no" id="brd_no" value='-1'/>     
<input type="hidden" name="p_brd_no" id="p_brd_no" value='-1'/> 
<input type="hidden" name="cntnt" id="cntnt" value=''/> 

				<table class="com_table_box">
					<tr>
						<th style="width:110px;">등록일자</th>
						<td id='reg_dt'></td>
						<th style="width:110px;">등록자/수정자</th>
						<td id='reg_emp_nm'></td>
					</tr>
				    <tr>
				        <th>게시판</th><td><select id="brd_mng_no2" name="brd_mng_no" class="form-control" required><option value="">선택</option></select></td>
				        <th>게시판유형</th><td><select id="view_type" name="view_type" class="form-control" disabled><option value="">선택</option></select></td>
				    </tr>
					<tr>
				        <th>자료구분</th><td><select id="brd_div" name="brd_div" class="form-control" required><option value="">선택</option></select></td>
				        <th>중요구분</th><td><select id="imp_yn" name="imp_yn" class="form-control" required><option value="N">일반</option><option value="Y">중요</option></select></td>
				    </tr>
				    <tr>
				        <th>제목</th><td colspan="3"><input type="text" id="ttl" name="ttl" maxlength="50" class="form-control" ></td>
				    </tr>
				    <tr id='cntnt_form'>
				        <th>내용</th><td colspan="3"><textarea rows="5" cols="800" id="_cntnt" name="_cntnt" title="내용"></textarea></td>
				    </tr>

<script type="text/javascript" isELIgnored="false">
    //<![CDATA[
    CKEDITOR.replace('_cntnt', {            	
    	filebrowserImageUploadUrl : '/bin/FileUploader',
		height:200
   	});	
    //]]>
</script>			
					<tr id="file_view" style='display:none;'>		
						<th>첨부파일</th>
						<td colspan=3>
							<div class="margin_B_10">				
								<div id="gridFileEntriesDivMain" style="display:; max-height:250px; overflow-y:auto;">	
									<div class="span6" id="gridFileEntriesDiv" style="width:100%"></div>
								</div>
							</div>
						</td>
					</tr>	    
				    <tr id='fileUploadForm' style='display:none;'>
				        <th>첨부파일추가</th>
				        <td colspan="3" id="file_attach" >
				        	
				        </td>
				    </tr>
				    
				    
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
			
			<form name="comment_form" id='comment_form'>
			<input type="hidden" name='board_comment_page_cnt' id='board_comment_page_cnt' value='5'/>
			<input type="hidden" name='pre_page_comment_cnt' id='pre_page_comment_cnt' value='1'/>
			<input type="hidden" name='pre_page' id='pre_page' value='1'/>
			<input type="hidden" name='last_page' id='last_page' value=''/>
			</form>    
			       <div id="comment_box" class="widget-body_A comment_box">
					<div class="widget-main">			
							
						<div>
							<div id="board_comments"></div>
							<div id="board_comments_page" style="    text-align: center;"></div>
						</div>
						
					</div>
					</div>	
           
         </div>
       </div>

     </div>
     
     <div id='aaaa' style="display:none;"></div>
     
     <script type="text/javascript">
 
    (function() {
        var f = document.getElementById('writeForm'), $f = $(f);
               
        f.init = function(){
            uploader = ubicus.app.uploader('#file_attach',{
                url: '/upload',
                template: 'ul',
                autoUpload:false,
                uploaded:function(file,$row,i){
                    var inputs = '<input type="hidden" class="js-upload-file-field" name="file_nm" value="'+file['file_nm']+'"/>';
                    inputs    += '<input type="hidden" class="js-upload-file-field" name="file_path" value="'+file['file_path']+'"/>';
                    inputs    += '<input type="hidden" class="js-upload-file-field" name="file_size" value="'+file['size']+'"/>';
                    inputs    += '<input type="hidden" class="js-upload-file-field" name="user_file_nm" value="'+file['name']+'"/>';
                    inputs    += '<input type="hidden" class="js-upload-file-field" name="ext_nm" value="'+file['ext_nm']+'"/>';
                    $row.append(inputs);
                }
                ,acceptFileTypes: /(\.|\/)(jpg|jpeg|bmp|png|gif|hwp|txt|pdf|doc|docx|xls|xlsx|ppt|pptx|zip)$/i
                ,maxNumberOfFiles: 4
            });
            
        };
        
        f.init();
    })();
    
    </script>
     
</body>



</html>