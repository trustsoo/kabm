<%
/********************************************************************************
 * Program ID	: 멤버정보 작성
 * FileName		:
 * @version		: 1.0
 *  Comment		:
 ********************************************************************************/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ include file="/common/common.jsp" %>

<%
	String thisServerName = request.getServerName();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" class="source">

jQuery(document).ready(function(){
	jQuery('#mobileTitle').html('비밀번호 찾기');
	jQuery('#user_id').keydown(function(evt){
		if (evt.keyCode==13) {
			lf_idConfirm();
			evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
		}
	});
	
});

function lf_requestSubmit()
{
	var url = '/mobile/member/member.jspx?cmd=passwd_find';
	jQuery('#frm').attr('action', url);
	jQuery('#frm').submit();
}


function lf_idConfirm()
{
	if(jQuery('#user_id').val() == '')
	{
		msgStart(msg_mem_code_001, 'warning');
		return;
	}

	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=idConfirm",
   		type: "POST",
		data : jQuery('#frm').serialize(true),
		async : false,
		datatype: 'json',
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_010);
		},
   		success:function(json)
   		{
   			var code = json.result.code;
	   		var msg =  json.result.msg;
	   		var data = json.result.data;
	   		
	   		if(code == '200')
			{
				var user_id = data.user_id;

				if( user_id != '' )
				{
					lf_requestSubmit();
					
				}
				else
				{
					msgStart(msg_mem_code_007);
                    return;
				}
			} else
			{
				msgStart(msg_com_code_010);
				return;
			}
		}
  	});

}

</script>

</head>

<body>

<form name="frm" id="frm" method="post" action="" >
	<h2>비밀번호 아이디 확인</h2>	
	<div id="content">
		<div class="section-1">
				<div class="con_center">
					<strong>찾고자 하는 비밀번호의 아이디를 입력해 주세요.</strong>
					
					<div class="inputBox">
						<input type="text" class="form-control" title="" value="" name="user_id" id="user_id"/>
					</div>
					
					<div class="actionBar">
						<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
						<a href="javascript:lf_idConfirm();" class="btn btn-primary"><span class="">다음</span></a>
					</div>	
				
				</div>
			</div>
		</div>
	</div>
</form>

</body>
</html>