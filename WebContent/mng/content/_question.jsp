<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>

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
		makeCodeSelectBox('qna_category', '', 'in_question_div', false, true); 
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

		//저장
		$("#btn_save_answer").click(function(){
			js_crudAction("update_answer"); 
		});
		

		//삭제
		$("#btn_delete").click(function(){
			js_crudAction("delete_question"); 
		});
<%
	if(!"".equals(input.getText("seq_no")))
	{
%>
	js_AnswerMode('<%=input.getText("seq_no")%>');
<%
	}
%>		
		
	});


	//답변 등록 / 삭제  (update_answer , delete_question)
	function js_crudAction(action)
	{		
		if(!$("#seq_no").val() )
		{
			
			msgStart("선택된 문의내용이 없습니다", 'info');  
			return;
		}

		if(action == "update_answer"){		
			var inData = "";
			if(CKEDITOR.instances.answer){//편집기 활성화 된 상태
				inData = CKEDITOR.instances.answer.getData();
			}else{ //textarea 상태
				inData = $("#answer").val();
			}

			if(inData == ""){
				//alert("답변을 입력해 주세요");
				msgStart("답변을 입력해 주세요", 'info');  
				return;
			}
		}

		if(action == "delete_question"){	
			if(confirm("정말 삭제하시겠습니다까?") == false){
				return;
			}
		}


		//action = update_answer or delete_question
		var _url = '/mng/content/action/question.jspx?cmd=registQuestion&action=' + action;	


        var submitForm = $("#input_form");
        //기존 히든 프레임이 있으면 삭제
        if( $("#submit_HiddenIframe").length) $("#submit_HiddenIframe").remove();

        //submit용 히든 프레임 생성 
        $("body").append('<iframe name="submit_HiddenIframe" id="submit_HiddenIframe" width="200px" height="200px"></iframe>');

		$(submitForm).attr("target", "submit_HiddenIframe");
        $(submitForm).attr("action", _url);
        
        $('#title').val($('#title').html() );
		$('#question').val($('#question').html() );

        var iframe_load_event_fg = false;  //중복 알람 방지용


        $(submitForm).submit(function(){
                 $("#submit_HiddenIframe").load(function() {
                 	//STR : 중복 알람 방지용
                 if (iframe_load_event_fg) {
                 		iframe_load_event_fg = true;
                 		return;                 	
                 	}
                 
                 	iframe_load_event_fg = true;
                 	//END : 중복 알람 방지용

                 	try
                 	{ 	
	                 	var iframeObj = this;
	                 	var rtnDocument=$(iframeObj).get(0).contentDocument;  //document

	                 	var code = ($(rtnDocument).find("result > code")[0]).textContent ;
	                 	var msg = ($(rtnDocument).find("result > msg")[0]).textContent ;

	                 	if(code == 200){
				   			msgStart(msg_mng_code_009);
				   			js_clearInputForm();  //입력박스 초기화
				   			
				   			
				   			if(action == "update_answer")
				   				js_getList($('#cur_pg').val());  
					   		else				   			
				   				js_getList(1);    //리스트 조회
				   				
				   		}else{
				   			msgStart(msg_mng_code_010 + '['+msg+']' , 'danger');
				   		}

				   	}catch(e){
				   		msgStart(msg_mng_code_010 + ":" + e, 'danger');
				   	}
				   	$("#submit_HiddenIframe").remove();
				   	
            });
        }).submit();

	}


	function goSearch(cur_pg)
	{
		js_getList(cur_pg);
	}


	//조회, 답변, 삭제 등 모든 액션 수행 후 폼 클리어
	function js_clearInputForm(){
		$('#reg_date').html("");//등록 일자
		$('#reg_user').html("");//등록 자

		$("#seq_no").val();

		$('#question_div_nm').html("");
		$('#answer_yn_nm').html("");
		$('#email').val("");
		$('#title').val("");
		$('#question').val("");
		$('#_answer_td').html("");
		
		$('#str_email').html("");
		$('#str_title').html("");
		$('#str_question').html("");

		$('#_div_buttons').hide();  //삭제/저장 버튼 숨김
	}

	//답변창 활성화
	function js_AnswerMode(seq_no){
		if(seq_no == '' || seq_no == 'undefined' ) return;

		var _url = "/mng/content/action/question.jspx?cmd=preModify";	

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'text',
	   		data : "seq_no=" + seq_no, 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(data)
			{	
				$("#_preModify_table").html(data);
			}			
		} );
	}


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

		var _url = "/mng/content/action/question.jspx?cmd=getQuestionList";	

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
				if( data.question_list ) 
					dataCount =  data.question_list.length;

		   		if (code == 200){		

		   			for(var i=0 ; i < data.question_list.length ; i++)
		   			{
		   				var tdData = "";
		   				tdData += "<td style='text-align:center;'>"+ data.question_list[i].seq_no 			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.question_list[i].question_div_nm 	+"</td>" ; 
		   				tdData += "<td style='text-align:left;'>"+ data.question_list[i].title 			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.question_list[i].name 			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.question_list[i].email 			+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.question_list[i].reg_ddtm 		+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.question_list[i].answer_yn_nm	+"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.question_list[i].answer_ddtm 	+"</td>" ; 
		   				
		   				listDataRows.push("<tr style=\"cursor:pointer;\" onclick=js_AnswerMode('" + data.question_list[i].seq_no  + "');>" + tdData + "</tr>");
		   			}

		   			if(data.question_list.length == 0 )
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
						<th width="100px"><div>구분</div></th>
						<td width="150px">
							<select id="in_question_div" name="in_question_div" class="form-control" >
						    	<option value="%">전체</option>						  
							</select>
						</td>
						
						<th width="100px"><div>등록일</div></th>
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
						<th width="100px">구분</th>
						<th>제목</th>
						<th width="100px">성명</th>
						<th width="200px">이메일</th>
						<th width="100px">등록일</th>
						<th width="70px">답변여부</th>
						<th width="100px">답변일자</th>
					</tr>
				</thead>
				<tbody id="listData">
					<tr><td colspan="9" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
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

<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      	<div class="x_title">
        	<h2>문의내용</h2>
	        <ul class="nav navbar-right panel_toolbox">
	          <!--<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>-->
	          <!--<li><a class="close-link"><i class="fa fa-close"></i></a></li> -->
	        </ul>
	        <div class="clearfix"></div>
      	</div>

      	<form id="input_form" name="input_form" method="post">
      	<div class="x_content" id="_preModify_table">
		  	<input type="hidden" id="seq_no" name="seq_no" >
		  	<input type="hidden" id="email" name="email" >
		  	<input type="hidden" id="title" name="title" >
		  	<input type="hidden" id="question" name="question" >
		  	<input type="hidden" id="user_nm" name="user_nm" value="">
			<table class="com_table_box">
				<tbody>
					<tr id="_div_reginfo_tr">
						<th width="80px">등록일자</th>
						<td><span name="reg_date" id="reg_date"></span></td>

						<th width="80px">등록자</th>
						<td><span name="reg_user" id="reg_user"></span></td>
					</tr>

					<tr>
						<th>구분</th>
						<td><span name="question_div_nm" id="question_div_nm"></span></td>

						<th>답변여부</th>
						<td><span name="answer_yn_nm" id="answer_yn_nm"></span></td>
					</tr>

					<tr>
						<th>이메일</th>						
						<td colspan="3"><span  id="str_email"></span></td>
					</tr>


					<tr>
						<th>제목</th>						
						<td colspan="3"><span  id="str_title"></span></td>
					</tr>

					<tr>
						<th>문의내용</th>
						<td colspan="3"><div  id="str_question"></div></td>
					</tr>


					<tr>
						<th>답변</th>
						<td colspan="3"  id="_answer_td">&nbsp;</td>
					</tr>

				</tbody>
			</table>
		</div>
		</form>	
		


		<div class="buttons" style="float:right" >
		  	<div id="_div_buttons" style="display:none">
				<button id="btn_delete" name="btn_delete"  type="button" class="btn btn-success btn-sm">삭제</button>
	            <button id="btn_save_answer" name="btn_save_answer" type="button"  class="btn btn-sm btn-primary" onclick="return false;">저장</button>
	        </div>
        </div>      	
    </div>
</div>
<div class="clearfix"></div>




</body>
</html>