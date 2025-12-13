<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ include file="/common/common.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style type="text/css">
	.jqgrow {cursor: default;}
</style>


<script type="text/javascript" type="text/javascript">
//<![CDATA[	
	function js_pageInit(){
		makeCodeSelectBox('popup_div', '', 'in_popup_div', false, true); 
	 	makeCodeSelectBox('use_yn', '', 'use_yn', false, true); //사용여부
		makeCodeSelectBox('view_div', '', 'view_div', false, true); //구분
		makeCodeSelectBox('popup_type', '', 'popup_type', false, true); 
		makeCodeSelectBox('popup_div', '', 'popup_div', false, true); 
		makeCodeSelectBox('link_method', '', 'link_method', false, true); 
		
	}

	jQuery(document).ready(function(){
		
		$(".date-picker").datepicker({
			format: 'yyyy-mm-dd',			
			todayHighlight: true,
			autoclose: true
		});
		
		
		js_pageInit();		
		js_getList(1);

		
		$("#btn_search").click(function(){
			js_getList(1); 
		});

		//"신규등록" 버큰 클릭 시 화면 정리.
		$("#change_add_btn").click(function(){
			js_changemode("add");
		});

		//"저장"시 insert
		$("#btn_insert").click(function(){
			//js_crudAction("insert");
			$("#action_mode").val("insert");
			if(js_validation("insert") == false) return ;

			fn_cnslt_detail_upload_save();
		});
		
		//"수정"시 update
		$("#btn_edit").click(function(){
			//js_crudAction("update");
			$("#action_mode").val("update");			
			if(js_validation("update") == false) return ;
			
			fn_cnslt_detail_upload_save();
		});		

		//"삭제"시 delete
		$("#btn_delete").click(function(){
			//js_crudAction("delete");
			$("#action_mode").val("delete");
			if(js_validation("delete") == false) return ;
			//js_crudAction("delete");
			js_crudAction();
		});		
	});

	function js_validation(action){
		//1.validation
		//수정 일때는 수정할 배너가 선택됐는지 점검 한다.
		if(action == "update")
		{
			if( $("#seq_no").val().length < 1 ){
				//alert("수정할 베너를 선택해 주세요");
				msgStart("수정할 팝업을 선택해 주세요", 'info');  
				return false;
			}
		}

		if(action == "delete")
		{
			if( $("#seq_no").val().length < 1 ){
				msgStart("삭제할 팝업을 선택해 주세요", 'info');  
				return false;
			}

			if(confirm("정말 삭제하시겠습니다까?") == false){
				return false;
			}
		}

		if(action != "delete")
		{
			if( $("#title").val().length < 1 ){
				msgStart("제목을 입력해 주세요", 'info');  
				return false;
			}

			if( $("#link").val().length < 1 ){
				//msgStart("링크를 입력해 주세요", 'info');  
				//return false;
			}

			if( $("#width").val().length < 1 ){
				msgStart("가로크기를 입력해 주세요", 'info');  
				return false;
			}

			if( $("#height").val().length < 1 ){
				msgStart("세로크기를  입력해 주세요", 'info');  
				return false;
			}

			if(isNaN($("#width").val())   || isNaN($("#width").val()) ){
				msgStart("팝업크기는 숫자만 가능합니다.", 'info');  
				return false;	
			}

			//팝업 사이즈 0 허용 안함
			if($("#width").val() == '0'   || $("#width").val() =='0' ){
				msgStart("팝업크기는 0보다 커야합니다.", 'info');  
				return false;	
			}
			
		}

	}


	//1. 파일 업로드 
	function fn_cnslt_detail_upload_save()
	{	
		uploader.startUpload(function(errorCount){
            
            if(errorCount == 0){
            	js_file_save_after_upload();
            }else{
                msgStart(errorCount+'개의 파일이 업로드되지 못했습니다.', "error", null, 300, 110);
            }
        });

	}

	//2. 파일 업로드가 성공 하면 콜백 되는 함수
	function js_file_save_after_upload()
	{	
		js_crudAction();		

	    //한번에 1개의 파일만 처리하기 위함.
	    js_file_infofild_clear();
	}
	
	//파일정보 클리어 
	function js_file_infofild_clear(){
		$("#file_nms").val("");
		$("#file_paths").val("");
		$("#file_sizes").val("");
		$("#ext_nms").val("");
		$("#user_file_nms").val("");
	}


	//insert/update/delete
	function js_crudAction(){
		var action = $("#action_mode").val();
	
		if(action != "delete")
		{			
			//애디터가 활성화 되지 않았을 경우는 textarea 의 contents를 그대로 전송
			try
			{
					$("#contents").val(CKEDITOR.instances.contents.getData());	
			}catch(e){
			}
		}

		if(action == "add"){
			//이미지 타입일때는 이미지를 필수로 업로드 하도록 한다.
			if($('#view_div').val() == 'view_div_01' && $("#file_nms").val().length < 1 ){
				alert("팝업에 사용할 이미지를 선택해 주세요");
				return;
			}
		}
		
		//2.ajax 호출
		var _url = '/mng/content/action/popup.jspx?cmd=registPopup&action=regist';	 //insert/update
		if(action == "delete")	{
			_url = '/mng/content/action/popup.jspx?cmd=registPopup&action=delete';	//delete
		}

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'json',
	   		data : jQuery("#input_form").serialize()  , 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(Json)
			{			
				var code = Json.result.code;
		   		var msg =  Json.result.msg;
		   		var data = Json.result.data;
		   		
		   		//화명 갱신
		   		if(code == 200){		   			
		   			alert(msg_mng_code_009);
		   			if(action  == "update")
		   				js_getList($('#cur_pg').val());  
		   			else
		   				location.reload(true);
		   			
		   			if(uploader != null)
		   				uploader.resetUpload();	
		   		}else{
		   			//msgStart(msg_mng_code_010 + '['+Json.result.msg+']' , 'danger');
		   			alert(msg_mng_code_010 + '['+Json.result.msg+']' );
		   			location.reload(true);
		   		}
			}			
		} );
	}

	//mode = add:신규등록 모드, edit:수정 모드
	function js_changemode(mode){
		$("#file_no").val("");

		if(mode == "add"){
			$("#_div_reginfo_tr").hide();
			$("#_div_add_button").show();   //등록 버튼
			$("#_div_edit_buttons").hide(); //삭제, 수정 버튼

			$('#seq_no').val("");//베너 IDX

			$('#reg_date').html("");//등록 일자
			$('#reg_user').html("");//등록 자


			
	       	$("#start_dt").val("${start_dt}");
	       	$("#end_dt").val("${end_dt}");

	       	$('#title').val("");
	       	$('#link').val("");
	       	$('#width').val("");
	       	$('#height').val("");

	       	//$("#banner_img").val("");   	//이미지 링크 제거
			//$("#image_preview").hide();     //이미지 미리보기 숨김.

			

			$("#contents").val('');  
			try{
			 CKEDITOR.instances.contents.setData('');
			}catch(e){}

			//배너 이미지 숨김
			$("#div_preview_img").hide();
			$("#img_preview").attr("src","#");	

		}else{
			$("#_div_reginfo_tr").show();
			$("#_div_add_button").hide();
			$("#_div_edit_buttons").show();
		}
	}

	
	//타이틀 클릭 시 에디터 모드로 간다...
	function js_editMode(seq_no){
		if(seq_no == '' || seq_no == 'undefined' ) return;
		
		if(uploader != null)
			uploader.resetUpload();
		
		js_changemode('edit');  //화면 모드 전환
	
		var http = jQuery.ajax( {
	   		url: "/mng/content/action/popup.jspx?cmd=getPopupItem",	
	   		datatype :'json',
	   		data : "seq_no=" + seq_no, 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(Json)
			{	
				var code = Json.result.code;
		   		var msg =  Json.result.msg;
		   		var data = Json.result.data;


				if (code == 200 ){		
						
			   		if(data.popup_list.length != 0 ){
			   			$('#seq_no').val(data.popup_list[0].seq_no);// IDX

						$('#reg_date').html(data.popup_list[0].reg_ddtm);//등록 일자
						$('#reg_user').html(data.popup_list[0].popup_emp_nm);//등록 자

						$('#start_dt').val(data.popup_list[0].start_dt);
						
				       	$("#end_dt").val(data.popup_list[0].end_dt);

				       	$('#title').val(data.popup_list[0].title);
				       	$('#link').val(data.popup_list[0].link);
				       	$("#link_method").val(data.popup_list[0].link_method).attr("selected","selected");
				       	$('#width').val(data.popup_list[0].width);
				       	$('#height').val(data.popup_list[0].height);

				       	$("#use_yn").val(data.popup_list[0].use_yn).attr("selected","selected");
				       	$("#popup_div").val(data.popup_list[0].popup_div).attr("selected","selected");
				       	$("#view_div").val(data.popup_list[0].view_div).attr("selected","selected");
				       	$("#popup_type").val(data.popup_list[0].popup_type).attr("selected","selected");


				       	$("#file_no").val(	data.popup_list[0].file_no);  //선택된 파일 번호 기억


						//첨부 문서 미리보기 
						if(	$("#file_no").val().length > 0 ){
							
							$("#img_preview").attr("src","/common/action/attach.jspx?cmd=doDownload&file_no=" + $("#file_no").val() + "&_r_=" + Math.random());
							$("#div_preview_img").show();
						}else{
							$("#div_preview_img").hide();
						}

						 //Json 안의 태그는 인코딩 되어 있으므로 임시로 옮겨놓은 다음에 .text()를 통해 완성되 HTML을 가져오도록 한다.
						 $("#_contents_hidden").html("");
						 $("#_contents_hidden").html(data.popup_list[0].contents);
						

						 $("#contents").val( $("#_contents_hidden").text() );
						 try{
						 	CKEDITOR.instances.contents.setData($("#_contents_hidden").text());	
						 }catch(e){

						 }
						 
						// CKEDITOR.instances.contents.insertHtml(data.popup_list[0].contents);
					} else {
						msgStart(msg_mng_code_010 + '[조회된 데이터가 없습니다]' , 'danger');
					}
			

		   		}else{
		   			msgStart(msg_mng_code_010 + '['+Json.result.msg+']' , 'danger');
		   		}
			}			
		} );

	}

	function goSearch(cur_pg)
	{
		js_getList(cur_pg);
	}

	function js_getList(cur_pg)
	{
		var fromDate    = $("#in_reg_ddtm_from").val().replace(/-/gi,"");
		var toDate 		= $("#in_reg_ddtm_to").val().replace(/-/gi,"");
		

		if(   fromDate.length != 8 || toDate.length != 8  || fromDate > toDate  )
		{
			msgStart("조회 날짜를 확인해 주세요", 'info');  
			return;
		}
		
		$('#cur_pg').val(cur_pg);

		var _url = "/mng/content/action/popup.jspx?cmd=getPopupList";	

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'json',
	   		data : jQuery("#search_form").serialize(), 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(Json)
			{	
				
				var code = Json.result.code;
		   		var msg =  Json.result.msg;
		   		var data = Json.result.data;

				
				if(Json.result.data.property[0].tot_cnt )
				{
						$('#tot_cnt').val(Json.result.data.property[0].tot_cnt);
				} else {
						$('#tot_cnt').val(0);
				}


		   		$("#listData > tr").remove();
		   		

		   		var listDataRows=[];
		   		
		   	
		   		var dataCount = 0;
				if( data.popup_list ) 
					dataCount =  data.popup_list.length;

		   		if (code == 200){		

		   			for(var i=0 ; i < data.popup_list.length ; i++)
		   			{
		   				var tdData = "";
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].seq_no 			+"</td>" ; 
		   				
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].popup_div_nm 	+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].view_div_nm 	+"</td>" ;  //구성방법 
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].popup_type_nm 	+"</td>" ;  //게시형태 
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].title 			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].link			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].reg_ddtm 		+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].use_yn 			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].start_dt 		+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.popup_list[i].end_dt 			+"</td>" ; 
		   				
		   				
		   				listDataRows.push("<tr style=\"cursor:pointer;\" onclick=js_editMode('" + data.popup_list[i].seq_no  + "');>" + tdData + "</tr>");
		   			}

		   			if(data.popup_list.length == 0 )
		   				listDataRows.push("<tr><td colspan='10' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 

		   		}else{
		   			listDataRows.push("<tr><td colspan='10' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}
		   		

				$("#listData").html(listDataRows.join());
				//msgStart("조회되었습니다", 'info'); 		 

				js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val(), 'goSearch');
				js_Paging_text('page_text_info', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val());  	
			}			
		} );
	}

	/** 
    * 폼요소 초기화 
    * Reset form element
    * 
    * @param e jQuery object
    */
	function resetFormElement(e) {
        e.wrap('<form>').closest('form').get(0).reset(); 
        //리셋하려는 폼양식 요소를 폼(<form>) 으로 감싸고 (wrap()) , 
        //요소를 감싸고 있는 가장 가까운 폼( closest('form')) 에서 Dom요소를 반환받고 ( get(0) ),
        //DOM에서 제공하는 초기화 메서드 reset()을 호출
        e.unwrap(); //감싼 <form> 태그를 제거
    }
	
//]]>
</script>
</head>
<body>

<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
		<div class="x_content">
		<form id="search_form" >
			<input type="hidden" name="cur_pg" id="cur_pg" value="1">
			<input type="hidden" name="row_per_page" id="row_per_page" value="10">
			<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>


			<table class="condition-table">
				<tbody>
					<tr class='last-tr'>
						<th width="100px"><div>구분</div></th>
						<td width="150px">
							<select id="in_popup_div" name="in_popup_div" class="form-control" >
						    	<option value="%">전체</option>						  
							</select>
						</td>
						
						<th width="120px"><div>등록일</div></th>
						<td width="120px">
							<span class="input-group textbox_width">
									<input class="date-picker W100P form-control" id="in_reg_ddtm_from" name="in_reg_ddtm_from"  type="text" value="${fromDate}" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
							</span>
						</td>
						<td width="20px"> ~ </td>
						<td width="120px">
								<span class="input-group textbox_width">
									<input class="date-picker W100P form-control" id="in_reg_ddtm_to" name="in_reg_ddtm_to" type="text" value="${toDate}" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
								</span>
						</td>	
						<td>&nbsp;</td>
						<td width="80px">
							<!--<button id="btn_search" name="btn_search" type="submit" class="btn btn-default btn-sm" onclick="return false;">조회</button>-->
							<div id="btn_search" class="btn btn-sm btn-warning" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><b>조회</b></div>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	    </div>

               
      	<div class="x_content">			
			
			<table class="table table-striped jambo_table bulk_action">
				<thead class="thin-border-bottom center" >
					
					<tr class="headings">
						<th width="60px">No</th>
						<th width="100px">구분</th>
						<th>구성방법</th>
						<th width="100px">게시형태</th>
						<th>제목</th>
						<th>링크</th>
						<th width="70px">등록일</th>
						<th width="70px">사용여부</th>
						<th width="70px">게시시작</th>
						<th width="70px">게시종료</th>
					</tr>
				</thead>
				<tbody id="listData">
					<tr><td colspan="10" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
				</tbody>
			</table>			
      	</div>


		<div class="row">
		  	<div class="col-sm-5">
		  		<div class="dataTables_info" id="page_text_info" role="status" aria-live="polite">0 to 0 of 0 건</div>
		  	</div>
		  	<div class="col-sm-7">
		  		<div class="pull-right">
					<span id="change_add_btn" class="btn btn-success" type="reset">신규등록</span>
		          </div>
		  		<div class="dataTables_paginate paging_simple_numbers" id="datatable-checkbox_paginate" style="float:left;text-align:left;">
			  		<ul class="pagination" id="pagingDiv" style="margin:0px;">
			  		</ul>
		  		</div>
		  	</div>
		  </div>
		  
     	</div>
</div>

<div class="clearfix"></div>

<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      	<div class="x_title">
        	<h2>팝업정보</h2>
	        <ul class="nav navbar-right panel_toolbox">	          
	        </ul>
	        <div class="clearfix"></div>
      	</div>

      	<div class="x_content">
	    <form id="input_form" name="input_form" onsubmit="return false;">
		  	<input type="hidden" id="seq_no" name="seq_no" ><!-- 선택된 seq_no 기억 -->
		  	<input type="hidden" name="action_mode" id="action_mode" value=''/>      <!--insert, update -->

			<input type="hidden" name='file_nms' 	id='file_nms'	 value=''/>
			<input type="hidden" name='file_paths' 	id='file_paths' value=''/>
			<input type="hidden" name='file_sizes' 	id='file_sizes' value=''/>
			<input type="hidden" name='ext_nms' 	id='ext_nms' 	value=''/>
			<input type="hidden" name='user_file_nms' id='user_file_nms' 	value=''/>
			<input type="hidden" name='attach_div_cd' id='attach_div_cd' 	value='attach_div_02'/>   
			<input type="hidden" name='file_no' 		id='file_no' 		value=''/>    <!-- 수정할때 기존 파일 여부 판단 -->

			<div id="_contents_hidden" style="display:none;"></div> <!-- 에디터 태그 깨짐 방지용 임시 컨테이너 -->


			<table class="com_table_box">
				<tbody>
					<tr id="_div_reginfo_tr" style="display:none">
						<th width="80px">등록일자</th>
						<td><span name="reg_date" id="reg_date"></span></td>

						<th width="80px">등록자</th>
						<td><span name="reg_user" id="reg_user"></span></td>
						
					</tr>

					<tr>
						<th>게시기간</th>						
						<td colspan="3">
						
							<span class="input-group left" style="width:150px !important;">
									<input class="date-picker  form-control" id="start_dt" name="start_dt"  type="text" value="${start_dt}" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
							</span><span class="left"> ~ </span>	<span class="input-group left" style="width:150px !important;">
									<input class="date-picker  form-control" id="end_dt" name="end_dt" type="text" value="${end_dt}" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
								</span>
                        
                      	
						

						</td>
					</tr>
					
					<tr>
						<th>구분</th>
						<td>
		                     <select id="popup_div" name="popup_div" class="form-control" ></select>
	                    </td>

						<th>사용여부</th>
						<td>
							<select id="use_yn" name="use_yn"  class="form-control" ></select>
						</td>
					</tr>

					<tr>
						<th>제목</th>
						<td colspan="3"><input type="text" id="title" name="title"  class="form-control" maxlength="120" /></td>
					</tr>

					<tr>
						<th>링크</th>
						<td colspan="3">
							<div style="overflow:hidden">
								<div style="float:left; width:70%">
									<input type="text" id="link" name="link"  class="form-control" maxlength="120" />		
								</div>
								<div style="float:right; width:28%">
									<select id="link_method" name="link_method" class="form-control" ></select>	
								</div>
							</div>
						</td>
					</tr>

					<tr>
						<th>게시크기</th>
						<td colspan="3">
							<div class="col-md-6 col-sm-6 col-xs-12 has-feedback">
								가로 <input  id="width" name="width"  type="text" value="" maxlength="50"  calss="form-control has-feedback-left" style="width:50px">px, 
								세로 <input  id="height" name="height"  type="text" value=""  maxlength="50"  calss="form-control has-feedback-left" style="width:50px">px
							</div>
						</td>
					</tr>

					<tr>
						<th>구성방법</th>
						<td>
							<select id="view_div" name="view_div" class="form-control" ></select>
	                    </td>

						<th>게시형태</th>
						<td>
							<select id="popup_type" name="popup_type" class="form-control" ></select>
						</td>
					</tr>
					<tr>
						<th rowspan="3">이미지</th>
						<td colspan="3" id="_banner_img_block" >							
							파일 확장자 – PNG, BMP, JPG, GIF<br/>						
							용량제한 – 1MB 까지 
							<div id="div_preview_img" style="display:none;">
								<img id="img_preview" name="img_preview" src="#" /> <!--/common/action/attach.jspx?cmd=doDownload&file_no=5 -->
							</div>
						</td>
					</tr>
					<tr>
						<td colspan=3>
							<div class="margin_B_10">				
								<div id="gridFileEntriesDivMain" style="display:; max-height:250px; overflow-y:auto;">	
									<div class="span6" id="gridFileEntriesDiv" style="width:100%"></div>
								</div>
							</div>
						</td>
					</tr>
					<tr id='fileUploadForm' >
				        <td colspan="3" id="file_attach" >
				        	
				        </td>
				    </tr>
					<tr>
						<th>내용</th>
						<td colspan="3"><textarea rows="5" cols="900" id="contents" name="contents" title="내용"></textarea></textarea></td>
					</tr>

				</tbody>
			</table>
		</form>	
		</div>
		
<script type="text/javascript" src="/static/lib/ckeditor/ckeditor.js" charset="UTF-8"></script>
<script type="text/javascript" isELIgnored="false">
    //<![CDATA[
    CKEDITOR.replace('contents', {            	
    	filebrowserImageUploadUrl : '/bin/FileUploader',
		height:200
   	});	

   	$("#_div_buttons").show(); //삭제, 저장 버튼 보이게 처리
    //]]>
</script>


		<div class="buttons" style="float:right">
            <div id="_div_add_button">
            	<button id="btn_insert" name="btn_insert" type="button"  class="btn btn-sm btn-primary" onclick="return false;">저장</button>
            </div>
            <div id="_div_edit_buttons" style="display:none">
	            <button id="btn_delete" name="btn_delete"  type="button" class="btn btn-success btn-sm">삭제</button>
	            <button id="btn_edit" name="btn_edit" type="button" class="btn btn-info btn-sm">수정</button>
	        </div>
        </div>      	
    </div>
</div>
<div class="clearfix"></div>
<script type="text/javascript">
 
    (function() {
        var f = document.getElementById('input_form'), $f = $(f);
               
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
                ,acceptFileTypes: /(\.|\/)(jpg|jpeg|bmp|png|gif)$/i
                ,maxNumberOfFiles: 4
            });            
        };
        
        f.init();
    })();
    
</script>
</body>
</html>