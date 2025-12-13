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
		url: "/mobile/action/main.jspx?cmd=doPasswordModify",
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
	   			msgStart(msg_com_code_009);
			} else
			{	
				msgStart(msg_com_code_007);
			}
	   		
	   		jQuery('#password').val('');
	   		jQuery('#pw_confirm').val('');
		}
  	});
}

</script>
</head>
		
<body>			

<div class="x_panel">
      <div class="x_title">
        <h2>비밀번호 변경</h2>        
        <div class="clearfix"></div>
      </div>
      <div class="x_content">
      	<form name="frm" id="frm" method="post" class="form-horizontal form-label-left" onsubmit="return false;">
      	
      	<label class="control-label col-md-3 col-sm-3 ">신규비밀번호</label>
		<div class="form-group row ">
			<div class="col-md-9 col-sm-9 ">
			<input type="password" class="form-control" alt="비밀번호" value="" name='password' id='password' required='true'/>
			</div>
		</div>
		<label class="control-label col-md-3 col-sm-3 ">비밀번호확인</label>
		<div class="form-group row ">
			<div class="col-md-9 col-sm-9 ">
			<input type="password" class="form-control" alt="비밀번호확인" value="" name='pw_confirm' id='pw_confirm' required='true'/>
			</div>
		</div>
      	</form>
      	
	  </div>			
	</div>
	

<br>
<div style="text-align:center;">
<button id="btn_reg" class="btn btn-sm btn-primary">
	<i class="icon- fa fa-save"> 저장</i>					 
</button>		
</div>


<script type="text/javascript">
jQuery(document).ready(
		function()
		{	
			
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
			});	
			
		}
	);
</script>


</body>
</html>			