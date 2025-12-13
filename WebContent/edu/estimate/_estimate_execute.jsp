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

function lf_submit()
{
	if( !checkExamField('#frm') ) return;
		
	var http = jQuery.ajax( {
   		url: "/edu/estimate/action/estimateAction.jspx?cmd=setEstimateResult&templet-bypass",
   		type: "POST",
		data : jQuery('#frm').serialize(true),
   		async:false,
   		beforeSend : function(){
		},
		error 	: function(xml)
   		{	
			msgStart(msg_com_code_007);
		},
   		success: function(xml)
   		{
   			alert( msg_est_code_001);
   			location.href = '/edu/estimate/estimateCtrl.jspx?cmd=viewEstimateResult&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no=${input.user_class_seq_no}';
   			
   		}
  	});	
	
}

function checkExamField(f)
{			
	var result = true;
	jQuery(f).find('input').each(
			function(i){
	            var objtype = jQuery(this);
	            objtype.each(
	            	function(j){	  
	            		
		            	if(jQuery(this).attr('type') == 'radio' )
		            	{	
		            		var cur_checkNm = jQuery(this).attr('name');
		            		var checkedVal = jQuery(":input:radio[name="+cur_checkNm+"]:checked").val();
		            		
		            		if( checkedVal == null || checkedVal == '' )
		            		{
		            			var addStr = jQuery(this).attr('exam_num');
		            						            			
			            		jQuery(this).focus();
			            		jQuery(this).select();
			            		result = false;
			            		
			            		alert('['+addStr +']' + '번 문제를 작성하지 않았습니다.' );
			            		
			            		return false;			            		
		            		}		            		
		            	}
	            	}	            	
	            );
	       		return result;
            }			
		);
		return result;	
}

function js_goClass()
{
	location.href = '/edu/lecture/classCtrl.jspx?cmd=doClass&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no=';	
}

</script>
</head>

<body>

<form name="frm" id="frm" method="post">
<input type="hidden" id="user_lecture_seq_no" name="user_lecture_seq_no" value="${input.user_lecture_seq_no}"/>
<input type="hidden" id="user_class_seq_no" name="user_class_seq_no" value="${input.user_class_seq_no}"/>
			
			<% for( int idx=0; idx < output.getCount("estimate_seq_no"); idx++){ %>
			<input type="hidden" name="estimate_seq_no" id="estimate_seq_no" value="<%=output.getText("estimate_seq_no",idx)%>"> 
				
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
								<% for( int jdx=0 ; jdx < reqView.getCount("item_seq_no"); jdx++){ %>
							      <input type="radio" exam_num="<%=output.getText("exam_num",idx)%>" name="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" id="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" value="<%=reqView.getText("item_seq_no",jdx)%>" />
							      <%=jdx %>) <%=reqView.getText("item_nm",jdx)%>
							      <br />
							 <%	} %> 
							 <%} else if( "3".equals(output.getText("exam_type",idx)) ){  %>
							      예<input type="radio" exam_num="<%=output.getText("exam_num",idx)%>" name="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" id="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" value="예" />&nbsp;&nbsp;
							      아니오<input type="radio" exam_num="<%=output.getText("exam_num",idx)%>" name="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" id="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" value="아니오" />
							 <%}else{ %> 
							     <textarea exam_num="<%=output.getText("exam_num",idx)%>" name="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" id="answr_ctnt_<%=output.getText("estimate_seq_no",idx)%>" class="textarea_BoxBlue" cols="140" rows="5"></textarea>
							 <%} %>
						</td>
					</tr>
					</tbody>
					</table>
				</div>
			<% } %>
</form>	

<div class="btnpagBox01">
	<!-- btnArea -->
	<div class="btnArea">
		<button class="ui-icon-arrowreturnthick-1-w" onclick="history.go(-1);">취소</button>
		<button class="ui-icon-check" onclick="lf_submit();">제출</button>
		<button class="ui-icon-arrowreturnthick-1-w" onclick="javascript:js_goClass();">동영상다시보기</button>
		
	</div>	
</div>

</body>
</html>