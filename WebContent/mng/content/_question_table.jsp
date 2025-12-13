<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>


<input type="hidden" id="seq_no" name="seq_no" value="${output[0].seq_no}"><!-- 선택된 베너 기억 -->
<input type="hidden" id="email" name="email" value="${output[0].email}">
<input type="hidden" id="title" name="title" value="">
<input type="hidden" id="question" name="question" value="">
<input type="hidden" id="user_nm" name="user_nm" value="${output[0].name}">

<table class="com_table_box">
<tbody>
	<tr id="_div_reginfo_tr">
		<th width="80px">등록일자</th>
		<td><span name="reg_date" id="reg_date">${output[0].reg_ddtm}</span></td>

		<th width="80px">등록자</th>
		<td><span name="reg_user" id="reg_user">${output[0].name}</span></td>
	</tr>

	<tr>
		<th>구분</th>
		<td><span name="question_div_nm" id="question_div_nm">${output[0].question_div_nm}</span></td>

		<th>답변여부</th>
		<td><span name="answer_yn_nm" id="answer_yn_nm">${output[0].answer_yn_nm}</span></td>
	</tr>

	<tr>
		<th>이메일</th>						
		<td colspan="3"><span  id="str_email">${output[0].email}</span></td>
	</tr>


	<tr>
		<th>제목</th>						
		<td colspan="3"><span  id="str_title">${output[0].title}</span></td>
	</tr>

	<tr>
		<th>문의내용</th>
		<td colspan="3"><div id="str_question">${output[0].question}</div></td>
	</tr>


	<tr>
		<th>답변</th>
		<td colspan="3" id="_answer_td">
			<textarea rows="5" cols="800" id="answer" name="answer" title="내용">${output[0].answer}</textarea>
		</td>
	</tr>

</tbody>

</table>

<script type="text/javascript" src="/static/lib/ckeditor/ckeditor.js" charset="UTF-8"></script>

<script type="text/javascript" isELIgnored="false">
    //<![CDATA[
    CKEDITOR.replace('answer', {            	
    	filebrowserImageUploadUrl : '/bin/FileUploader',
		height:200
   	});	

   	$("#_div_buttons").show(); //삭제, 저장 버튼 보이게 처리
    //]]>
</script>


