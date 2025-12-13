<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" type="text/javascript">
//<![CDATA[	
	
	jQuery(document).ready(function(){		
		makeCodeSelectBox('qna_category', '', 'question_div', false, true);
	});
	
function js_save(){
		
		if(!checkFormField("form")) return;
		
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=createQuestion',
			data : jQuery("#form").serialize(true),
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
		   			location.href = '/board/board.jspx?cmd=write_suggest';
				}
			}
		});			
	}
</script>
</head>
<body>
<div id="content">
<!-- start :: content -->
<!-- start :: content -->

	<div class="writeWrap">
<form name='form' id='form'>
		<table class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="25%"/><col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">제목 : </th>
					<td>
						<input type="text" class="it " title="" value="" name="title"  id='title' required='true'/>
					</td>
				</tr>
				<tr>
					<th scope="row">질문 분류: </th>
					<td>
						<select name="question_div" id="question_div" required='true'>	
							<option value=''>선택</option>						
						</select>
					</td>
				</tr>
				
				<tr>
					<th scope="row">성명: </th>
					<td>
						<input type="text" class="it " title="" value="<%=input.getText("user_nm")%>" name="name" id='name' readOnly/>
					</td>
				</tr>
				
				<tr>
					<th scope="row">전화번호 : </th>
					<td>
						<input type="text" class="it " title="" value="" name="tel_no" id='tel_no'/>
					</td>
				</tr>
				
				<tr>
					<th scope="row">이메일 : </th>
					<td>
						<input type="text" class="it " title="" value="" name="email" id='email'/>
					</td>
				</tr>
				
				<tr>
					<th scope="row">내용 : </th>
					<td>
						<textarea name="question" id='question' class="txt" cols="" rows="" title="" required='true'></textarea>
					</td>
				</tr>
			</tbody>
		</table>
</form>		
		<div class="btnWrap">
			<a href="javascript:js_save();" class="pbtn02" ><span>확인</span></a>
			<a href="javascript:document.form.reset();" class="pbtn01"><span>취소</span></a>
		</div>

	</div>
<!-- end :: content -->
<!-- end :: content -->
</div>
</body>
</html>