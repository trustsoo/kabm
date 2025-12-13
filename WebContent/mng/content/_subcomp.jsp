<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="thumb_url" type="String" scope="request" />

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

	var subCompList = {};  //

	jQuery(document).ready(function(){		
		
		$(".date-picker").datepicker({format: 'yyyy-mm-dd'});
		
		js_getSubList(1);  
		
		$("#btn_search").click(function(){
			js_getSubList(1);
		});
		
		$("#file_delete").click(function(){
			js_fileDelete();
		});
		

		//"저장"시 insert
		$("#btn_insert").click(function(){
			
			$("#action_mode").val("insert");
			if(js_validation() == false) return ;

			fn_cnslt_detail_upload_save();
			
		});
		
		//"수정"시 update
		$("#btn_edit").click(function(){
			$("#action_mode").val("update");			
			if(js_validation("update") == false) return ;

			fn_cnslt_detail_upload_save();
			
		});		
		
		//우편번호 찾기		
		$("#btn_popaddr_search").click(function(){
			js_JusoPopup();
		});	
		
		//"신규등록" 버큰 클릭 시 화면 정리.
		$("#change_add_btn").click(function(){			
			js_changemode("init");
		});
	});

	
	function js_validation(){
		//1.validation		
		if( $("#cp_code").val().length < 1 ||  $("#ji_code").val().length < 1)
		{
			msgStart("지회를 선택해 주세요", 'info');  
			return false;
		}

		/*if( $("#daepyo").val().length < 1 ){
			msgStart("지회장을 입력해 주세요", 'info');  
			return false;
		}*/

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
		
		if(!checkFormField("form")) return;
		
		//2.ajax 호출
		var _url = '/mng/content/action/subcomp.jspx?cmd=registSub&action=regist';	
		
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
		   			js_changemode("init");  //입력박스 초기화
		   			if(action  == "update")
		   				js_getSubList($('#cur_pg').val());  
		   			else
		   				js_getSubList(1);    //리스트 조회
		   				
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
			
		}

		if(mode == "init")
		{
			$("#cp_code").val("");
			$("#ji_code").val("");
			$("#ji_nm").val("");  //지회명

			$("#daepyo").html("");  //지회장
			$("#jisaname").html("");  //지회명

			$("#samu_nm").val("");
			$("#intro").val("");
			
			$("#div_preview_img").hide();
			$("#img_preview").attr("src","");
			$("#_div_edit_buttons").hide(); //삭제, 수정 버튼
		}else{
			$("#_div_add_button").hide();
			$("#_div_edit_buttons").show();
		}
	}


	//cp_code,ji_code, daepyo, jisaname,zip_code,  addr, tel, fax,e_mail,seq_no,samu_nm, rnum,tot_cnt
	function js_selectMode(cp_code,ji_code)
	{
		if(cp_code == '' || cp_code == 'undefined' ) return;
		if(ji_code == '' || ji_code == 'undefined' ) return;
		
		js_changemode('edit');  //화면 모드 전환
		
		var mapKey = cp_code +"_" + ji_code ;
		var subCompItemMap = subCompList[mapKey];

		//선택된 지회 정보 저장
		$("#cp_code").val(cp_code);
		$("#ji_code").val(ji_code);
		$("#ji_nm").val(subCompItemMap.jisaname);  //지회명

		$("#daepyo").html(subCompItemMap.daepyo);  //지회장
		$("#jisaname").html(subCompItemMap.jisaname);  //지회명

		//--->기타 정보 일단 클리어 
		$('#seq_no').val("");
		$("#samu_nm").val("");
		$("#intro").val("");
		
		if(uploader != null)
			uploader.resetUpload();

		var _url = "/mng/content/action/subcomp.jspx?cmd=getSubItem";	

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'json',
	   		data : "cp_code=" + cp_code + "&ji_code=" + ji_code, 
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

		   		var dataCount = 0;
				if( data.sub_list ) 
					dataCount =  data.sub_list.length;

				if (code == 200){
					if(dataCount > 0){  //수정 모드 
						$('#seq_no').val(data.sub_list[0].seq_no);//IDX

						$("#samu_nm").val(data.sub_list[0].samu_nm);						
						$("#intro").val(data.sub_list[0].intro);

						$("#file_no").val(data.sub_list[0].file_no);  //선택된 파일 번호 기억

						//첨부 문서 미리보기 
						if($("#file_no").val().length > 0){
							$("#img_preview").attr("src","/common/action/attach.jspx?cmd=doDownload&file_no=" +$("#file_no").val() + "&_r_=" + Math.random());
							$("#div_preview_img").show();
						}else{
							$("#file_no").val("");
							$("#div_preview_img").hide();
						}
						
					}else{  //등록 모드 
						$('#seq_no').val('');
					}
				} else {
					msgStart(msg_mng_code_010 + '['+Json.result.msg+']' , 'danger');
				}				
			}			
		} );
	}
	
	function js_fileDelete(){
		
		if(!confirm('해당 파일을 정말 삭제하시겠습니까?')) return;
		
		var file_no = $("#file_no").val();

		var _url = "/common/action/attach.jspx?cmd=doDelete";	

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'json',
	   		data : "file_no=" + file_no, 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(xmlDoc){
				
				var code = jQuery(xmlDoc).find('code').text();
		   		var message = jQuery(xmlDoc).find('msg').text();	
		   		
				if(code == '200') {
					msgStart(msg_com_code_009);
					
					js_file_infofild_clear();
					$("#file_no").val("");
					$("#div_preview_img").hide();
					
				} else {
		   			msgStart(msg_com_code_010+"("+message+")", 'danger');
				}
			}					
		} );
		
	}

	//타이틀 클릭 시 에디터 모드로 간다...
	function js_editMode(seq_no){
	
	}

	function goSearch(cur_pg)
	{
		js_getSubList(cur_pg);
	}
	
	function js_getSubList(cur_pg)
	{
		$('#cur_pg').val(cur_pg);

		var _url = "/mng/content/action/subcomp.jspx?cmd=getSubList";	

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

		   		subCompList = {};  //지회 리스트 정보 저장 객체 
		   		
		   		var dataCount = 0;
				if( data.sub_list ) 
					dataCount =  data.sub_list.length;


		   		if (code == 200){		

		   			for(var i=0 ; i < dataCount ; i++)
		   			{
		   				//STR : 배너 수정에 사용할 데이터 보관
		   				var subCompItem = {};
		   					subCompItem['seq_no'] = data.sub_list[i].seq_no ;
							subCompItem['cp_code'] = data.sub_list[i].cp_code ;
							subCompItem['ji_code'] = data.sub_list[i].ji_code ;							
							subCompItem['jisaname'] = data.sub_list[i].jisaname ;
							subCompItem['daepyo'] = data.sub_list[i].daepyo ;
							
						var mapKey = data.sub_list[i].cp_code +"_" + data.sub_list[i].ji_code ;
							


						subCompList[mapKey] = subCompItem ;

						
		   				var tdData = "";
		   				tdData = "<td style='text-align:center;'>"+ data.sub_list[i].rnum +"</td>" ;  //번호
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].jisaname +"</td>" ;  //지회명
		   				var thumb_url = '<%=thumb_url%>';
		   				if(data.sub_list[i].file_no.length > 0)
		   				{
			   				tdData += "<td style='text-align:center;'>"  +
			   							"<img style='height:60px;' src='" + thumb_url + "/" +data.sub_list[i].file_path + "/" +data.sub_list[i].file_nm + "?_r_=" + Math.random() +"' />"
			   							+ "</td>" ; 
			   			}else {
			   				tdData += "<td style='text-align:center;'>&nbsp;</td>" ; 
			   			}
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].daepyo +"</td>" ;  //지회장
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].samu_nm +"</td>" ;  //사무총장
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].zip_code +"</td>" ;  //우편번호
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].addr +"</td>" ;  //주소
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].tel +"</td>" ;  //연락처
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].fax +"</td>" ;  //팩스
		   				tdData += "<td style='text-align:center;'>"+ data.sub_list[i].e_mail +"</td>" ;  //이메일
		   			
		   				//listDataRows.push("<tr style=\"cursor:pointer;\" onclick=js_editMode('" + data.sub_list[i].seq_no  + "');>" + tdData + "</tr>");
		   				listDataRows.push("<tr style=\"cursor:pointer;\" onclick=js_selectMode('" + data.sub_list[i].cp_code  + "','" + data.sub_list[i].ji_code  + "');>" + tdData + "</tr>");
		   			}

		   			if(dataCount == 0 )
		   				listDataRows.push("<tr><td colspan='9' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 

		   		}else{
		   			listDataRows.push("<tr><td colspan='9' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
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
							<th width="100px"><div>지회명</div></th>
							<td width="200px"><input type="text" name="in_jisaname" id="in_jisaname" value="" maxlength="30" class="form-control" ></td>

							<th width="120px"><div>지회장</div></th>
							<td width="150px"><input type="text" name="in_daepyo" id="in_daepyo" value=""  maxlength="10" class="form-control" ></td>

							<td>&nbsp;</td>
							<td width="80px">
								<!-- <button id="btn_search" name="btn_search" type="submit" class="btn btn-default btn-sm" onclick="return false;">조회</button> -->
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
							<th>지회명</th>
							<th>사진</th>
							<th>지회장</th>
							<th>사무총장</th>
							<th>우편번호</th>						
							<th>주소</th>						
							<th>연락처</th>
							<th>팩스</th>
							<th>이메일</th>
							<!--<th>홈페이지</th>-->
							<!--<th>사용여부</th>-->
							<!--<th>등록일</th>-->
						</tr>
					</thead>
					<tbody id="listData">
						<tr><td colspan="13" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
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
			  <!--
			  <div class="pull-right">
				<span  id="change_add_btn" name="change_add_btn" class="btn btn-success" type="reset">신규등록</span>
	          </div>
	          -->
	      </div>
	    </div>
	</div>	

	<div class="col-md-12 col-sm-12 col-xs-12">
    	<div class="x_panel">
      		<div class="x_title">
	        	<h2>지회상세정보 관리</h2>
	        	<ul class="nav navbar-right panel_toolbox">
	          	<!--<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>-->
	          	<!--<li><a class="close-link"><i class="fa fa-close"></i></a></li> -->
	        	</ul>
	       	 	<div class="clearfix"></div>
	     	</div>
	      	
	      	<div class="x_content">
		    <form id="input_form" name="input_form" method="post">
			  	<input type="hidden" id="seq_no" name="seq_no" ><!-- 선택된 지회 기억 -->
			  	<input type="hidden" id="cp_code" name="cp_code" >
			  	<input type="hidden" id="ji_code" name="ji_code" >			  	
			  	<input type="hidden" name='ji_nm' 	id='ji_nm'	 value=''/>
			  	

				<input type="hidden" name="action_mode" id="action_mode" value='insert'/>      <!--insert, update -->

				<input type="hidden" name='file_nms' 	id='file_nms'	 value=''/>
				<input type="hidden" name='file_paths' 	id='file_paths' value=''/>
				<input type="hidden" name='file_sizes' 	id='file_sizes' value=''/>
				<input type="hidden" name='ext_nms' 	id='ext_nms' 	value=''/>
				<input type="hidden" name='user_file_nms' id='user_file_nms' 	value=''/>
				<input type="hidden" name='attach_div_cd' id='attach_div_cd' 	value='attach_div_05'/>   
				<input type="hidden" name='file_no' 		id='file_no' 		value=''/>    <!-- 수정할때 기존 파일 여부 판단 -->


				
				




				<table class="com_table_box">
					<tbody>
						<tr id="_div_reginfo_tr" style="display:none;">
							<th width="80px">등록일자</th>
							<td height="40px"><span name="reg_date" id="reg_date"></span></td>

							<th width="80px">등록자</th>
							<td><span name="reg_user" id="reg_user"></span></td>
						</tr>

						<tr>
							<th>지회명</th>
							<td height="40px"><span name="jisaname" id="jisaname"></span></td>

							<th>지회장</th>
							<td><span name="daepyo" id="daepyo"></span></td>
						</tr>

						<tr>
							<th>사무총장</th>
							<td><input type="text" id="samu_nm" name="samu_nm" class="form-control"  maxlength="20"></td>
							<th></th>
							<td></td>
						</tr>

					
						

						<tr>
							<th>인사말</th>
							<td colspan="3">
							<textarea id="intro" name="intro" class="form-control" rows="7"></textarea>
							</td>
						</tr>

						<tr>
							<th rowspan="3">지회장 사진</th>
							<td colspan="3">
							권장기준 이미지 최소 사이즈 – 가로 600 X 세로 200  ( 가로 세로 비율을 맞춰서 등록하셔야 합니다 )<br/>
							파일 확장자 – PNG, BMP, JPG, GIF<br/>
							용량제한 – 1MB 까지 <br/>
								<div id="div_preview_img" style="display:none;">
									<img id="img_preview" name="img_preview" src="#" width="150px"/> <!--/common/action/attach.jspx?cmd=doDownload&file_no=5 -->
									<span id="file_delete" class="btn btn-success btn-sm">삭제</span>
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
		            <!--button id="btn_delete" name="btn_delete"  type="button" class="btn btn-success btn-sm">삭제</button-->
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