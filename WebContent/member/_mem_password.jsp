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
<%@ include file="/common/common.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" class="source">
function lf_requestCancel()
{
	location.href='/index.jsp';
}
function lf_requestSubmit()
{
	if(!checkFormField('#frm'))
	{
		return false;
	}
	
	
	if( jQuery('#password').val() != jQuery('#pw_confirm').val() )
	{
		msgStart(msg_mem_code_004);
		return;
	}

	
	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=doPasswordModify",
   		type: "POST",
		data : jQuery('#frm').serialize(true),
   		async:false,
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_007);
		},
   		success:function(json) 
   		{	
   			var code = json.result.code;
	   		var msg =  json.result.msg;
	   		var data = json.result.data;
	   		
	   		if(code == '200')
			{
				msgOpen(msg_com_code_009);
				location.href='/logout.jsp';
			} else
			{	
				msgOpen(msg_com_code_007);
			}
		}
  	});
}

</script>
</head>
		
<body>			
<form name="frm" id="frm" method="post" onsubmit="return false;">

<div class="pageTit">
		<h2><span class="tit_1">비밀번호 변경</span></h2>
	</div>
	<div id="content">
		
		<div class="view">
			<div style="margin-bottom:10px;display:block;font-size:16px;color:red;font-weight:bold;">
			※ 회원님의 개인정보를 안전하게 보호하고, 개인정보 도용으로 인한 피해를 예방하기 위해 <br>90일 이상 비밀번호를 변경하지 않은 경우 비밀번호 변경을 권장하고 있습니다. 
			</div>
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width="200"/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">신규비밀번호  </th>
							<td>
								<input type="password" class="it " title="비밀번호" value="" name='password' id='password' required='true'/>
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">신규비밀번호확인  </th>
							<td>
								<input type="password" class="it " title="비밀번호" value="" name='pw_confirm' id='pw_confirm' required='true'/>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			<div class="btnWrap">
				<a href="#" class="pbtn02" id='btn_reg'><span class="">비밀번호 변경하기</span></a>
				
				<a href="#" class="pbtn03" id='btn_cancel'><span class="">다음에 변경하기</span></a>
			</div>
		</div>
</form>


<script type="text/javascript">
jQuery(document).ready(
		function()
		{	
			
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
			});
			
			jQuery('#btn_cancel').bind("click", function(){
				lf_requestCancel();
			});	
			
		}
	);
</script>


</body>
</html>			