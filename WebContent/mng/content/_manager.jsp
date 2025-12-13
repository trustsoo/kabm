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
	var managerList = {};  //조회된 임원 정보 저장

	function js_pageInit(){
		makeCodeSelectBox('use_yn', '', 'use_yn', false, true); //사용여부
	}

	jQuery(document).ready(function(){
		
		$(".date-picker").datepicker({format: 'yyyy-mm-dd'});
		
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
			js_crudAction();
		});		

		//우편번호 찾기		
		$("#btn_popaddr_search").click(function(){
			js_JusoPopup();
		});		

		//지회검색
		$("#btn_search_sub").click(function(){
			js_subPopup();
		});		

		
		
	});

	function js_validation(action){
		//1.validation		
		if(action == "update")
		{
			if( $("#emp_no").val().length < 1 ){				
				msgStart("수정할 임원을 선택해 주세요", 'info');  
				return false;
			}
		}

		if(action == "delete")
		{
			if( $("#emp_no").val().length < 1 ){				
				msgStart("삭제할 임원을 선택해 주세요", 'info'); 		
				return false;
			}

			if(confirm("정말 삭제하시겠습니다까?") == false){
				return false;
			}
		}


		if(action != "delete")
		{
			//TO-DO : 필수 입력 항목을 점검 해야 한다.		
			if( $("#emp_nm").val().length < 1 ){
				msgStart("성명을 입력해 주세요", 'info');  
				return false;
			}

			if($.isNumeric($("#odr").val()) == false ){
				msgStart("게시순서는 숫자만 입력해 주세요", 'info');  
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


	//insert/update
	function js_crudAction(){
		var action = $("#action_mode").val();

		//2.ajax 호출
		var _url = '/mng/content/action/manager.jspx?cmd=registManager&action=regist';	
		if(action == "delete")	{
			_url = '/mng/content/action/manager.jspx?cmd=registManager&action=delete';	
		}

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


	function goSearch(cur_pg)
	{
		js_getList(cur_pg);
	}


	//mode = add:신규등록 모드, edit:수정 모드
	function js_changemode(mode){
		$("#file_no").val("");	
			
		if(mode == "add"){
			$('#emp_no').val("");

			$("#_div_reginfo_tr").hide();
			$("#_div_add_button").show();   //등록 버튼
			$("#_div_edit_buttons").hide(); //삭제, 수정 버튼

			$('#reg_date').html("");			//등록 일자
			$('#reg_user').html("");			//등록 자	

            $('#emp_nm').val("");  //성명
			//$("#use_yn").val("N").attr("selected","selected");  //사용여부 "N"로 선택되게			
			$('#duty_nm').val(""); //직위
			$('#pos_nm').val("");  //직책
			$('#tel_no').val("");  //연락처
			$('#fax_no').val("");  //팩스
			$('#cmpny_nm').val(""); //회사명
			$('#odr').val("5000");      //게시순서
			$('#post_no').val("");  //우편번호
			$('#addr').val("");  //주소

			$("#div_preview_img").hide();
			$("#img_preview").attr("src","");
		}else{
			$("#_div_reginfo_tr").show();
			$("#_div_add_button").hide();
			$("#_div_edit_buttons").show();
		}
	}


	//타이틀 클릭 시 에디터 모드로 간다...
	function js_editMode(emp_no){
		if(uploader != null)
			uploader.resetUpload();
		
		js_changemode('edit');  //화면 모드 전환

		var managerItem = managerList[emp_no];
		$('#emp_no').val(managerItem.emp_no);//IDX
		$('#emp_nm').val(managerItem.emp_nm);//성명


		$('#reg_date').html(managerItem.reg_ddtm);				//등록 일자
		$('#reg_user').html(managerItem.reg_emp_nm);			//등록 자		
		$("#use_yn").val(managerItem.use_yn).attr("selected","selected"); 		//사용여부


		$('#duty_nm').val(managerItem.duty_nm);//직위
		$('#pos_nm').val(managerItem.pos_nm);//직책
		$('#tel_no').val(managerItem.tel_no);//연락처
		$('#fax_no').val(managerItem.fax_no);//팩스
		$('#cmpny_nm').val(managerItem.cmpny_nm);//회사명
		$('#odr').val(managerItem.odr);//게시순서
		$('#post_no').val(managerItem.post_no);//우편번호
		$('#addr').val(managerItem.addr);//주소
	
		
		$("#file_no").val(managerItem.file_no);  //파일번호(신규/수정 구분하기 위함)
		
		//첨부 문서 미리보기 
		if(	$("#file_no").val().length > 0 ){
			
			$("#img_preview").attr("src","/common/action/attach.jspx?cmd=doDownload&file_no=" + $("#file_no").val() + "&_r_=" + Math.random());
			$("#div_preview_img").show();
		}else{
			$("#div_preview_img").hide();
		}
		

	}

	function js_getList(cur_pg)
	{
		$('#cur_pg').val(cur_pg);

		var _url = "/mng/content/action/manager.jspx?cmd=getManagerList";	

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
		   		
		   		managerList = {};  

		   		var dataCount = 0;
				if( data.manager_list ) 
					dataCount =  data.manager_list.length;


		   		if (code == 200){		
		   			
		   			for(var i=0 ; i < dataCount ; i++)
		   			{
		   				//STR : 배너 수정에 사용할 데이터 보관
		   				var managerItem = {};
		   				managerItem['emp_no'] = data.manager_list[i].emp_no ;
		   				managerItem['emp_nm'] = data.manager_list[i].emp_nm ;
		   				managerItem['use_yn'] = data.manager_list[i].use_yn ;
		   				managerItem['duty_nm'] = data.manager_list[i].duty_nm ;
		   				managerItem['pos_nm'] = data.manager_list[i].pos_nm ;
		   				managerItem['tel_no'] = data.manager_list[i].tel_no ;
		   				managerItem['fax_no'] = data.manager_list[i].fax_no ;
		   				managerItem['cmpny_nm'] = data.manager_list[i].cmpny_nm ;
		   				managerItem['odr'] = data.manager_list[i].odr ;
		   				managerItem['post_no'] = data.manager_list[i].post_no ;
		   				managerItem['addr'] = data.manager_list[i].addr ;
		   				managerItem['reg_ddtm'] = data.manager_list[i].reg_ddtm ;
		   				managerItem['reg_emp_cd'] = data.manager_list[i].reg_emp_cd ;

		   				managerItem['file_no'] = data.manager_list[i].file_no ;

		   				managerList[data.manager_list[i].emp_no] = managerItem;
		   				
		   				
						//END : 배너 수정에 사용할 데이터 보관
		   				var tdData = "";
		   				tdData = "<td style='text-align:center;'>"+ data.manager_list[i].emp_no +"</td>" ; 
		   				var thumb_url = '${ thumb_url}';
		   				if(data.manager_list[i].file_no.length > 0)
		   				{
			   				tdData += "<td style='text-align:center;'>"  +
			   							"<img style='height:60px;' src='" + thumb_url + "/" +data.manager_list[i].file_path + "/" +data.manager_list[i].file_nm + "?_r_=" + Math.random() +"' />"
			   							+ "</td>" ; 
			   			}else {
			   				tdData += "<td style='text-align:center;'>&nbsp;</td>" ; 
			   			}

		   				tdData += "<td style='text-align:left;'>"+ data.manager_list[i].emp_nm +"</td>" ; //성명

		   				tdData += "<td style='text-align:left;'>"+ data.manager_list[i].duty_nm +"</td>" ;  //직위
		   				tdData += "<td style='text-align:center;'>"+ data.manager_list[i].pos_nm +"</td>" ;  //직책
		   				tdData += "<td style='text-align:center;'>"+ data.manager_list[i].cmpny_nm +"</td>" ;  //회사명
		   				tdData += "<td style='text-align:center;'>"+ data.manager_list[i].post_no +"</td>" ;  //우편번호
		   				tdData += "<td style='text-align:center;'>"+ data.manager_list[i].addr +"</td>" ;  //주소
		   				tdData += "<td style='text-align:center;'>"+ data.manager_list[i].tel_no +"</td>" ;  //연락처
		   				tdData += "<td style='text-align:center;'>"+ data.manager_list[i].fax_no +"</td>" ;  //팩스
		   				tdData += "<td style='text-align:center;'>"+ data.manager_list[i].use_yn +"</td>" ;  //사용여부
		   				tdData += "<td style='text-align:center;'>"+ data.manager_list[i].reg_ddtm +"</td>" ;  //등록일

		   				listDataRows.push("<tr style=\"cursor:pointer;\" onclick=js_editMode('" + data.manager_list[i].emp_no  + "');>" + tdData + "</tr>");
		   			}
						
		   			if(dataCount == 0 )
		   				listDataRows.push("<tr><td colspan='12' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 
					
		   		}else{
		   			listDataRows.push("<tr><td colspan='12' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}

				$("#listData").html(listDataRows.join());
				

				js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val(), 'goSearch');
				js_Paging_text('page_text_info', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val());  	
			}			
		} );
	}

	//우편번호조회
	function js_JusoPopup() {
	    var popupOption = 'directories=no, toolbar=no, location=no, menubar=no, status=no, scrollbars=no, resizable=no, left=400, top=200, width=570, height=420';
	    var pop=window.open("/common/jusoPopup.jsp", "주소 찾기", popupOption);
	}

	//주소 콜백
	function if_cust_jusoCallBack(roadFullAddr,roadAddrPart1,addrDetail,roadAddrPart2,engAddr,jibunAddr,zipNo){		
		$("#post_no").val(zipNo);
		$("#addr").val(roadFullAddr );			
	}

	//지회 검색
	function js_subPopup(){
		var popupOption = 'directories=no, toolbar=no, location=no, menubar=no, status=no, scrollbars=no, resizable=no,  left=100, top=100,  width=570, height=500';
	    var pop=window.open("/mng/content/_subcompPopup.jsp", "지회 찾기", popupOption);	
	}

	//지회 IDX, 지회장
	function if_search_subComp_CallBack(president_nm){
		$("#emp_nm").val(president_nm);
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
							<th width="100px"><div>성명</div></th>
							<td width="120px"><input type="text" name="in_emp_nm" id="in_emp_nm" value="" maxlength="15" class="form-control" ></td>

							<th width="120px"><div>회사명</div></th>
							<td width="250px"><input type="text" name="in_cmpny_nm" id="in_cmpny_nm" value=""  maxlength="30" class="form-control" ></td>

							<td>&nbsp;</td>
							<td width="80px">
								<!--<button id="btn_search" name="btn_search" type="submit" class="btn btn-default btn-sm" onclick="return false;">조회</button> -->
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
							<th width="100px">사진</th>
							<th>성명</th>
							<th>직위</th>
							<th>직책</th>
							<th>회사명</th>						
							<th>우편번호</th>						
							<th>주소</th>
							<th>연락처</th>
							<th>팩스</th>							
							<th>사용여부</th>
							<th width="70px">등록일</th>
						</tr>
					</thead>
					<tbody id="listData">
						<tr><td colspan="12" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
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

	<div class="col-md-12 col-sm-12 col-xs-12">
    	<div class="x_panel">
      		<div class="x_title">
	        	<h2>임원정보</h2>
	        	<ul class="nav navbar-right panel_toolbox"></ul>
	       	 	<div class="clearfix"></div>
	     	</div>
	      	
	      	<div class="x_content">
		    <form id="input_form" name="input_form" onsubmit="return false;">
			  	<input type="hidden" id="emp_no" name="emp_no" ><!-- 선택된 임원 일련번호  기억 -->
			  	<input type="hidden" name="action_mode" id="action_mode" value=''/>      <!--insert, update -->

				<input type="hidden" name='file_nms' 	id='file_nms'	 value=''/>
				<input type="hidden" name='file_paths' 	id='file_paths' value=''/>
				<input type="hidden" name='file_sizes' 	id='file_sizes' value=''/>
				<input type="hidden" name='ext_nms' 	id='ext_nms' 	value=''/>
				<input type="hidden" name='user_file_nms' id='user_file_nms' 	value=''/>
				<input type="hidden" name='attach_div_cd' id='attach_div_cd' 	value='attach_div_04'/>   
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
							<th>성명</th>
							<td>
                                <div class="col-md-6 col-sm-6 col-xs-12 has-feedback">
								    <input type="text" id="emp_nm" name="emp_nm"  maxlength="15" calss="form-control has-feedback-left">
									<button id="btn_search_sub" name="btn_search_sub" type="button"  class="btn btn-sm btn-primary" onclick="return false;">지회검색</button>
								</div>
							</td>

							<th>사용여부</th>
							<td>
								<select id="use_yn" name="use_yn" class="form-control" >	
								<!--					    
								    <option value="Y" selected>사용</option>
								    <option value="N">미사용</option>						    
								  -->
								</select>
							</td>
						</tr>

						<tr>
							<th>직위</th>
							<td><input type="text" id="duty_nm" name="duty_nm" class="form-control"  maxlength="15"></td>

							<th>직책</th>
							<td><input type="text" id="pos_nm" name="pos_nm" class="form-control"  maxlength="15"></td>
						</tr>

						<tr>
							<th>연락처</th>
							<td><input type="text" id="tel_no" name="tel_no" class="form-control"  maxlength="20"></td>

							<th>팩스</th>
							<td><input type="text" id="fax_no" name="fax_no" class="form-control"  maxlength="20"></td>
						</tr>

						<tr>
							<th>회사명</th>
							<td><input type="text" id="cmpny_nm" name="cmpny_nm" class="form-control"  maxlength="30"></td>

							<th>게시순서</th>
							<td><input type="text" id="odr" name="odr" class="form-control" value="5000"  maxlength="9"></td>
						</tr>

						<tr>
							<th>주소</th>
							<td colspan="3">
								<table class="table table_layout_fixed">
									<tbody>
										<tr>
											<td width="80px">우편번호</td>
											<td width="100px"><input type="text" id="post_no" name="post_no" class="form-control" maxlength="7"></td>											
											<td width="100px">
													<button id="btn_popaddr_search" name="btn_popaddr_search" type="button"  class="btn btn-sm btn-primary" onclick="return false;">우편번호 조회</button>
											</td>
											<td> &nbsp;</td>
										</tr>
										<tr><td colspan="4"><input type="text" id="addr" name="addr" class="form-control" ></td></tr>
									</tbody>
								</table>
							</td>
						</tr>
						

						
						<tr>
							<th rowspan="3">사진</th>
							<td colspan="3">
							권장기준 이미지 최소 사이즈 – 가로 100 X 세로 200  ( 가로 세로 비율을 맞춰서 등록하셔야 합니다 )<br/>
							파일 확장자 – PNG, BMP, JPG, GIF<br/>
							용량제한 – 1MB 까지 <br/>
							
							<div id="div_preview_img" style="display:none;">
								<img id="img_preview" name="img_preview" src="#" width="150px"/> <!--/common/action/attach.jspx?cmd=doDownload&file_no=5 -->
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