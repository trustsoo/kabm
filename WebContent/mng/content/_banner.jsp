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
	var bannerList = {};  //조회된 배너 정보 저장
	

	function js_pageInit(){
		
		makeCodeSelectBox('banner_div', '', 'in_banner_div', false, true); //조회조건 구분

		makeCodeSelectBox('use_yn', '', 'use_yn', false, true); //사용여부
		makeCodeSelectBox('banner_div', '', 'banner_div', false, true); //구분

	}
	
	
	jQuery(document).ready(function(){
		
		$(".date-picker").datepicker({
			format: 'yyyy-mm-dd',			
			todayHighlight: true,
			autoclose: true
		});
		
		js_pageInit() ;
		js_getList(1);  //
		
		
		$("#btn_search").click(function(){
			js_getList(1);
		});


		//"신규등록" 버큰 클릭 시 화면 정리.
		$("#change_add_btn").click(function(){
			js_changemode("add");
		});

		//"저장"시 insert
		$("#btn_insert").click(function(){
			$("#action_mode").val("insert");
			if(js_validation("insert") == false) return ;

			fn_cnslt_detail_upload_save();
			
		});
		
		//"수정"시 update
		$("#btn_edit").click(function(){
			$("#action_mode").val("update");			
			if(js_validation("update") == false) return ;

			fn_cnslt_detail_upload_save();
		});		

		//"삭제"시 delete
		$("#btn_delete").click(function(){
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
				msgStart("수정할 베너를 선택해 주세요", 'info');  
				return false;
			}
		}

		if(action == "delete")
		{
			if( $("#seq_no").val().length < 1 ){
				msgStart("삭제할 베너를 선택해 주세요", 'info');  
				return false;
			}

			if(confirm("정말 삭제하시겠습니다까?") == false){
				return false;
			}
		}

		if(action != "delete")
		{
			if( $("#title").val().length < 1 ){
				//alert("제목을 입력해 주세요");
				msgStart("제목을 입력해 주세요", 'info');  
				return false;
			}

			if( $("#link").val().length < 1 ){
				//alert("링크를 입력해 주세요");
				msgStart("링크를 입력해 주세요", 'info');  
				return false;
			}
		}


		//2. injection 확인
		if(isInjectField($("#input_form"))) return false;

		//insert 시에는 배너 이미지를 필수로 입력 받도록 한다.
		if(action == "insert")
		{
			//STR : File upload 및 Validation
			var stats = 1; //__Uploader.getStats();
		    	
			if(stats.files_queued < 1) {
				msgStart("업로드 할 배너 이미지를 선택해 주세요", 'info');  
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

	function js_crudAction(){
		var action = $("#action_mode").val();


		if(action == '' || action == 'undefined' ) return;

		//2.ajax 호출
		var _url = '/mng/content/action/banner.jspx?cmd=registBanner&action=regist';	 //insert/update
		if(action == "delete")	{
			_url = '/mng/content/action/banner.jspx?cmd=registBanner&action=delete';	//delete
		}


		//3. 최종 파일 확인....

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'json',
	   		data : jQuery("#input_form").serialize(), 
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
		   			msgStart(msg_mng_code_009);
		   			js_changemode("add");  //입력박스 초기화
		   			
		   			if(action  == "update")
		   				js_getList($('#cur_pg').val());  
		   			else
		   				js_getList(1);    //리스트 조회
		   			
		   			if(uploader != null)
		   				uploader.resetUpload();	
		   		}else{
		   			msgStart(msg_mng_code_010 + '['+Json.result.msg+']' , 'danger');
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
			
			$("#title").val(""); //타이틀 클리어 	
			$("#link").val("");	//링크 클리어

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

		var bannerItemMap = bannerList[seq_no];
		$('#seq_no').val(seq_no);//베너 IDX

		$('#reg_date').html(bannerItemMap.reg_ddtm);//등록 일자
		$('#reg_user').html(bannerItemMap.emp_nm);//등록 자
		$("#banner_div").val(bannerItemMap.banner_div).attr("selected","selected"); //구분
		$("#use_yn").val(bannerItemMap.use_yn).attr("selected","selected"); 		//사용여부
		$("#title").val(bannerItemMap.title);//타이틀
		$("#link").val(bannerItemMap.link);//링크
		
		//첨부 문서 미리보기 
		if(bannerItemMap.file_no.length > 0){
			$("#file_no").val(bannerItemMap.file_no);  //선택된 파일 번호 기억
			$("#img_preview").attr("src","/common/action/attach.jspx?cmd=doDownload&file_no=" + bannerItemMap.file_no + "&_r_=" + Math.random());
			$("#div_preview_img").show();
		}else{
			$("#file_no").val("");
			$("#div_preview_img").hide();
		}
		
	}
	
	function goSearch(cur_pg)
	{
		js_getList(cur_pg);
	}

	//----> 등록 된 베너 리스트 가져와서 리스트로 출력.
	function js_getList(cur_pg)
	{
		var fromDate    = $("#in_ddtm_from").val().replace(/-/gi,"");
		var toDate 		= $("#in_ddtm_to").val().replace(/-/gi,"");
		

		if(   fromDate.length != 8 || toDate.length != 8  || fromDate > toDate  )
		{
			msgStart("조회 날짜를 확인해 주세요", 'info');  
			return;
		}
		
		$('#cur_pg').val(cur_pg);

		var _url = "/mng/content/action/banner.jspx?cmd=getBannerList";	

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

		   		//var json = Json.result.data.emp_info;
				//var jsonCnt = Json.result.data.emp_info.length;
				
				if(Json.result.data.property[0].tot_cnt )
				{
						$('#tot_cnt').val(Json.result.data.property[0].tot_cnt);
				} else {
						$('#tot_cnt').val(0);
				}


		   		$("#listData > tr").remove();
		   		var listDataRows=[];

		   		bannerList = {}; //배너 리스트 정보 저장 객체 
		   		

		   		if (code == 200){		

		   			for(var i=0 ; i < data.banner_list.length ; i++)
		   			{
		   				//STR : 배너 수정에 사용할 데이터 보관
		   				var bannerItem = {};
							bannerItem['seq_no'] = data.banner_list[i].seq_no ;
							bannerItem['banner_div'] = data.banner_list[i].banner_div ;
							bannerItem['use_yn'] = data.banner_list[i].use_yn ;
							bannerItem['title'] = data.banner_list[i].title ;
							bannerItem['link'] = data.banner_list[i].link ;
							bannerItem['reg_ddtm'] = data.banner_list[i].reg_ddtm ;
							bannerItem['reg_emp_cd'] = data.banner_list[i].reg_emp_cd ;
							bannerItem['emp_nm'] = data.banner_list[i].emp_nm ;

							bannerItem['file_no'] = data.banner_list[i].file_no ;

							//alert(data.banner_list[i].file_no);


						bannerList[data.banner_list[i].seq_no] = bannerItem ;
						//END : 배너 수정에 사용할 데이터 보관

		   				

		   				var tdData = "";
		   				tdData = "<td style='text-align:center;'>"+ data.banner_list[i].seq_no +"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.banner_list[i].banner_div_nm +"</td>" ; 
		   				tdData += "<td style='text-align:left;'>"+ data.banner_list[i].title +"</td>" ; 

		   				//tdData += "<td style='text-align:left;'><a href='#' onclick='javascript:js_editMode(\""  + data.banner_list[i].seq_no  +"\");'>"+ data.banner_list[i].title +"</a></td>" ; 
		   				tdData += "<td style='text-align:left;'>"+ data.banner_list[i].link +"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.banner_list[i].reg_ddtm +"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.banner_list[i].use_yn +"</td>" ; 


		   				listDataRows.push("<tr style=\"cursor:pointer;\" onclick=js_editMode('" + data.banner_list[i].seq_no  + "');>" + tdData + "</tr>");
		   			}

		   			if(data.banner_list.length == 0 )
		   				listDataRows.push("<tr><td colspan='6' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 

		   		}else{
		   			listDataRows.push("<tr><td colspan='6' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}

				$("#listData").html(listDataRows.join());
				//msgStart("조회되었습니다", 'info'); 		 

				js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val(), 'goSearch');
				js_Paging_text('page_text_info', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val());  	
			}			
		} );
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
			<input type="hidden" name="row_per_page" id="row_per_page" value="5">
			<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>


			
			<table class="condition-table">
				<tbody>
					<tr class='last-tr'>
						<th width="100px"><div>구분</div></th>
						<td width="150px">
							<select id="in_banner_div" name="in_banner_div" class="form-control" >
						    <option value="%">전체</option>						  
							</select>
						</td>
						<td width="10px">&nbsp;</td>
						<td width="120px">
							<span class="input-group textbox_width">
									<input class="date-picker W100P form-control" id="in_ddtm_from" name="in_ddtm_from"  type="text" value="${fromDate}" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
							</span>
						</td>
						<td width="20px"> ~ </td>
						<td width="120px">
								<span class="input-group textbox_width">
									<input class="date-picker W100P form-control" id="in_ddtm_to" name="in_ddtm_to" type="text" value="${toDate}" data-date-format="yyyy-mm-dd"/>
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
								</span>
						</td>	
						
						<td>&nbsp;</td>
						<td width="80px">
							<div id="btn_search" class="btn btn-sm btn-warning" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><b>조회</b></div>
							<!--
							<button id="btn_search" name="btn_search" type="submit" class="btn btn-default btn-sm" onclick="return false;">조회</button>
							-->
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
						<th width="60px" class="column-title">No</th>
						<th width="100px">구분</th>
						<th>제목</th>
						<th>링크</th>
						<th width="100px">등록일</th>
						<th width="100px">사용여부</th>
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
</div>	

<div class="clearfix"></div>

<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      	<div class="x_title">
        	<h2>배너정보</h2>
	        <ul class="nav navbar-right panel_toolbox">
	          <!--<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>-->
	          <!--<li><a class="close-link"><i class="fa fa-close"></i></a></li> -->
	        </ul>
	        <div class="clearfix"></div>
      	</div>

      	<div class="x_content">
	    <form id="input_form" name="input_form" onsubmit="return false;">
		  	<input type="hidden" id="seq_no" name="seq_no" ><!-- 선택된 베너 기억 -->
			<input type="hidden" name="action_mode" id="action_mode" value=''/>      <!--insert, update -->

			<input type="hidden" name='file_nms' 	id='file_nms'	 value=''/>
			<input type="hidden" name='file_paths' 	id='file_paths' value=''/>
			<input type="hidden" name='file_sizes' 	id='file_sizes' value=''/>
			<input type="hidden" name='ext_nms' 	id='ext_nms' 	value=''/>
			<input type="hidden" name='user_file_nms' id='user_file_nms' 	value=''/>
			<input type="hidden" name='attach_div_cd' id='attach_div_cd' 	value='attach_div_01'/>   
			<input type="hidden" name='file_no' 		id='file_no' 		value=''/>    <!-- 수정할때 기존 파일 여부 판단 -->


			<table class="com_table_box">
				<tbody>
					<tr id="_div_reginfo_tr" style="display:none">
						<th width="80px">등록일자</th>
						<td><span name="reg_date" id="reg_date"></span></td>

						<th width="80px">등록자</th>
						<td><span name="reg_user" id="reg_user"></span></td>
					</tr>

					<tr>
						<th>구분</th>
						<td>
		                    <select id="banner_div" name="banner_div" class="form-control" >
		                    <!--				    
							    <option value="MAIN_BANNER">메인베너</option>
							    <option value="MAIN_ADV">메인광고</option>
							    <option value="SUB_BANNER">서브베너</option>
							  -->
							</select>
	                    </td>

						<th>사용여부</th>
						<td>
							<select id="use_yn" name="use_yn"  class="form-control" >					    
							<!--    <option value="Y">사용</option>
							    <option value="N" selected>미사용</option>						    
							 -->
							</select>
						</td>
					</tr>

					<tr>
						<th>제목</th>
						<td colspan="3"><input type="text" id="title" name="title"  class="form-control" maxlength="120" /></td>
					</tr>

					<tr>
						<th>링크</th>
						<td colspan="3"><input type="text" id="link" name="link"  class="form-control"  maxlength="120" /></td>
					</tr>

					<tr>
						<th rowspan="3">배너이미지</th>
						<td colspan="3">
						권장기준 이미지 최소 사이즈 – 가로 600 X 세로 200  ( 가로 세로 비율을 맞춰서 등록하셔야 합니다 )<br/>
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
				    
				</tbody>
			</table>
		</form>	
		</div>

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