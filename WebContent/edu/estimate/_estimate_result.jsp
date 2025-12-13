<%
/******************************************************************************** 
 * Program ID	:  평가
 * FileName		: 
 * @version		: 1.0
 *  Comment		: 
 ********************************************************************************/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ include file="/common/common.jsp"%> 
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />

<html>
<head>
<script type="text/javascript" class="source">

function fn_lectureView(lecture_seq_no,user_lecture_seq_no)
{
	location.href = '/edu/lecture/classCtrl.jspx?cmd=viewClassView&lecture_seq_no='+lecture_seq_no+'&user_lecture_seq_no='+user_lecture_seq_no;	
}

function lf_alert( status) 
{
	var msg09 = '5회 불합격 하셨습니다.\n사무국에 역락하여 수강 재신청하기 바랍니다.\n전화. 02)465-5900';
	var msg03 = '교육수강이 완료 되었습니다. \n[정보관리 -> 적요사항]에 업체정보 입력하신 후, \n다음날 [교육수강 -> 수료증]받기 눌러 수료증을 출력하시기 바랍니다.';
	if( status == '09')
		alert( msg09);
	
	if( status == '03')
		alert( msg03 );
}
</script>
</head>

<body>

<div class="search_DataBox01">
	<table class="search_Data" >					
		<tr>
			<td class="con02">
				<b>귀하의 점수는 <font color="red">${lecture.estimate_point }</font>점입니다.<br>
				귀하는  ${lecture.title }  ${lecture.class_no }교시  ${lecture.cls_title } ${lecture.estimate_cnt }차 평가에
				<font color="red"><c:if test="${lecture.estimate_pass_yn != 'Y' }">불</c:if>합격</font> 하셨습니다.
				</b>
			</td>
		</tr>
		</table>
</div>

<form name="frm" id="frm" method="post">

			<% for( int idx=0; idx < output.getCount("estimate_seq_no"); idx++){ %>
				
				<div class="search_DataBox01">
					<table class="search_Data" >					
						<tr>
							<td><%=output.getText("exam_num",idx)%>. <%=output.getText("exam_title",idx)%></td>
						</tr>
					</table>
				</div>
				<% if( output.getText("chart_path",idx) != null && !"".equals(output.getText("chart_path",idx))){ %>
				<div class="search_DataBox01" style="background:#fff !important;">
					<img src="${photoPath}/<%=output.getText("chart_path",idx)%>" style="max-width:100%">
				</div>
				<%} %>
				<div class="search_basicbox01">
					<table id="tb_job" cellpadding="6">
					<tbody>	
					<tr valign="top">
						<td style="line-height:16px; padding:10px">
							
							<% if( "1".equals(output.getText("exam_type",idx)) ){ 
								DataSet reqView = (DataSet)output.get("reqView",idx);
							%>
								<% for( int jdx=0 ; jdx < reqView.getCount("item_seq_no"); jdx++){ 
									String answer = output.getText("answer",idx);
									String item_seq_no = reqView.getText("item_seq_no",jdx);
									String check_yn = reqView.getText("check_yn",jdx);
									
									if( item_seq_no.equals(answer) &&  "Y".equals(check_yn) ){%>
										<font color="red"><b><%=jdx %>) <%=reqView.getText("item_nm",jdx)%>(O)</b></font>	
																	
								<%	}else if( item_seq_no.equals(answer) &&  !"Y".equals(check_yn) ){%>
										<b><%=jdx %>) <%=reqView.getText("item_nm",jdx)%>(X)</b>
								
								<%  }else if( !item_seq_no.equals(answer) &&  "Y".equals(check_yn) ){%>
										<font color="red"><b><%=jdx %>) <%=reqView.getText("item_nm",jdx)%>(정답)</b></font>
								
								<% }else{ %>
										<%=jdx %>) <%=reqView.getText("item_nm",jdx)%>
										
								<% } %>
								<br />	
								<% } %>	
							<% } %>	
						</td>
					</tr>
					</tbody>
					</table>
				</div>
			<%} %>
</form>	

<div class="btnpagBox01">
	<!-- btnArea -->
	<div class="btnArea">
		<button class="ui-icon-arrowreturnthick-1-w" onclick="javascript:fn_lectureView('${lecture.lecture_seq_no}','${lecture.user_lecture_seq_no}');">계속하기</button>
	</div>	
</div>
<script type="text/javascript">

	lf_alert( '${lecture.lecture_status}');
</script>
</body>
</html>