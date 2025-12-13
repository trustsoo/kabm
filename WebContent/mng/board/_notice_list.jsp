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
<script type="text/javascript" src="/static/lib/swfupload/js/swfupload.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/lib/swfupload/js/extend/swfupload.swfobject.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/lib/swfupload/js/extend/swfupload.queue.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/lib/swfupload/js/extend/fileprogress.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/lib/swfupload/js/extend/handlers.js" charset="utf-8"></script>
<title>Insert title here</title>

<script type="text/javascript" type="text/javascript">
//<![CDATA[	
	
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
		makeCodeSelectBox('notice_div', '', 'notice_div', false, true);
		
		jQuery('#btn-save').bind('click',function(){
			
			js_detail_save();
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
			
			js_notice_remove();
			
		});
		
		jQuery('#brd_mng_no2').bind('change', function(){			
			jQuery('#view_type').val(jQuery("#brd_mng_no2 option:selected").attr('etc1'));
			js_ContentsMode(jQuery('#view_type').val());
			
		});
		
		
		
		js_list(1);
		
	});
	
	function js_ContentsMode(val)
	{
		if(val =='board_type_02') jQuery('#cntnt_form').css('display', 'none');
		else jQuery('#cntnt_form').css('display', '');
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
		
	}
	
	
	
	
	
	//db에 저장
	function js_detail_save()
	{
				
		//jQuery("#cntnt").val(CKEDITOR.instances._cntnt.getData());
		
		var http = jQuery.ajax({
			url : '/mng/board/action/community.jspx?cmd=doSetNotice',
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
	
	//삭제
	function js_notice_remove()
	{
		
		var http = jQuery.ajax({
			url : '/mng/board/action/community.jspx?cmd=doDelNotice',
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
	
	
	function js_detail(brd_no)
	{
		
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
		   			jQuery('#reg_dt').html(json.result.data.board_detail[0].reg_dt);
		   			jQuery('#reg_emp_nm').html(json.result.data.board_detail[0].emp_nm);
		   			jQuery('#brd_no').val(json.result.data.board_detail[0].brd_no);
		   			jQuery('#p_brd_no').val(json.result.data.board_detail[0].brd_no);
		   			jQuery('#cntnt').val('');
		   			jQuery('#brd_mng_no2').val(json.result.data.board_detail[0].brd_mng_no);
		   			
		   			jQuery('#view_type').val(json.result.data.board_detail[0].view_type);
		   			jQuery('#brd_div').val(json.result.data.board_detail[0].brd_div);
		   			jQuery('#ttl').val(json.result.data.board_detail[0].ttl);		   			
		   			
		   			jQuery('#aaaa').html(json.result.data.board_detail[0].cntnt);
		   			jQuery('#cntnt').html(jQuery('#aaaa').text());
		   			jQuery('#notice_div').val(json.result.data.board_detail[0].notice_div);
		   			jQuery('#notice_ddtm').html(json.result.data.board_detail[0].notice_ddtm);
		   			jQuery('#notice_emp_nm').html(json.result.data.board_detail[0].notice_emp_nm);
		   			jQuery('#notice_start_dt').val(json.result.data.board_detail[0].notice_start_dt);
		   			jQuery('#notice_end_dt').val(json.result.data.board_detail[0].notice_end_dt);
		   			
		   			if(json.result.data.board_detail[0].notice_div != '') jQuery('#btn-del').show();
		   			else jQuery('#btn-del').hide();
		   			
		   			//CKEDITOR.instances._cntnt.setData(jQuery('#aaaa').text());
		   			
		   			if(json.result.data.board_detail[0].is_exist_file == 'Y')
		   			{
		   				js_getFileEntries('gridFileEntriesDiv',true, jQuery('#attach_div_cd').val(), json.result.data.board_detail[0].brd_no);
		   				jQuery('#file_view').css('display', '');
		   			}
		   			
		   			jQuery('#viewForm').css('display', '');
		   			
		   			js_ContentsMode(json.result.data.board_detail[0].view_type);
		   			
		   			
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
		   				var tdData = "";
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].brd_no 			+"</td>" ; 		   				
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].brd_nm 	+"</td>" ; 		
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].reg_dt 		+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].ttl 			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].read_count 			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].notice_start_dt			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].notice_end_dt 		+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.board_list[i].notice_div_nm 		+"</td>" ;
		   				
		   				
		   				
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
				<td width="150px" colspan='2'>
					<select id="brd_mng_no" name="brd_mng_no">
						<option value="">::전체::</option>
					</select>
				</td>
				<th width="150px">
					<div>공지여부</div>
				</th>
				<td width="150px" colspan='4'>
					<select id="notice_yn" name="notice_yn">
						<option value="">::전체::</option>
						<option value="Y">공지글</option>
						<option value="N">비공지글</option>
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
                <th class="column-title">등록일</th>
                <th class="column-title">제목</th>
                <th class="column-title">조회수 </th>
                <th class="column-title">공지시작일</th>
                <th class="column-title">공지종료일</th>
                <th class="column-title">공지구분 </th>
                
              </tr>
            </thead>

            <tbody id="listData">
					<tr><td colspan="8" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
			</tbody>
          </table>
        </div>
        
        <div class="row">
		  	<div class="col-sm-5">
		  		<div class="dataTables_info" id="page_text_info" role="status" aria-live="polite">0 to 0 of 0 건</div>
		  	</div>
		  	<div class="col-sm-7">
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
 

				<table class="com_table_box">
					<tr>
						<th style="width:110px;">등록일자</th>
						<td id='reg_dt'></td>
						<th style="width:110px;">등록자/수정자</th>
						<td id='reg_emp_nm'></td>
					</tr>
				    <tr>
				        <th>게시판</th><td><select id="brd_mng_no2" name="brd_mng_no" class="form-control" readOnly><option value="">선택</option></select></td>
				        <th>게시판유형</th><td><select id="view_type" name="view_type" class="form-control" disabled><option value="">선택</option></select></td>
				    </tr>
					<tr>
				        <th>자료구분</th><td><select id="brd_div" name="brd_div" class="form-control" readOnly><option value="">선택</option></select></td>
				        <th>공지</th><td><select id="notice_div" name="notice_div" class="form-control"><option value="">선택</option></select></td>
				    </tr>
				    <tr>
						<th style="width:110px;">지정일자</th>
						<td id='notice_ddtm'></td>
						<th style="width:110px;">지정자</th>
						<td id='notice_emp_nm'></td>
					</tr>
				    <tr>
				        <th>게시기간</th>
				        <td colspan="3">
				        	
				        	<span class="input-group left" style="width:150px !important;" >
									<input class="date-picker  form-control" id="notice_start_dt" name="notice_start_dt"  type="text" value="" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
							</span> <span class="left"> ~ </span> <span class="input-group left" style="width:150px !important;" >
									<input class="date-picker  form-control"  id="notice_end_dt" name="notice_end_dt" type="text" value="" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
								</span>
								
				      	</td>
				    </tr>
				    <tr>
				        <th>제목</th><td colspan="3"><input type="text" id="ttl" name="ttl" maxlength="50" class="form-control" readOnly></td>
				    </tr>
				    <tr id='cntnt_form'>
				        <th>내용</th><td colspan="3" id='cntnt'></td>
				    </tr>

	
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
				   		    
				    
				</table>
			</form>
	
			<span class="pull-right" style="padding: 10px 20px 0 0;">
				
				<span id="btn-save" class="btn btn-sm btn-primary">
				<i class="icon- fa fa-save"> 저장</i>					 
				</span>								
				<span id="btn-cancel" class="btn btn-sm btn-default">
					<i class="icon- fa fa-undo"> 취소</i> 
				</span>
				<span id="btn-del" class="btn btn-sm btn-danger">
				<i class="icon- fa fa-save"> 공지삭제</i>					 
				</span>
			
				
			</span>
           
         </div>
       </div>
     </div>
     
     <div id='aaaa' style="display:none;"></div>
</body>




</html>