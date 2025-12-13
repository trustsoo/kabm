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
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp" %>

<%
	String thisServerName = request.getServerName();
	int port = request.getLocalPort();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" language="javascript" src="/static/com/js/common_cetify.js"></script>

<style>
.tab-content .col-md-12 {
	width:100%;
}
.topRadio{ margin: 20px 0;}
</style>
</head>

<body>
<div class="pageTit">
	<h2>
		비밀번호 찾는 방법을 선택해주세요.
	</h2>
</div>
<div class="x_content">
	<ul class="nav nav-tabs bar_tabs" style="background: #ffffff;" id="myTab" role="tablist">
		<li class="nav-item active">
			<a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">휴대전화인증</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">이메일인증</a>
		</li>					
	</ul>
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade active in" id="home" role="tabpanel" aria-labelledby="home-tab">
			
			<label for="" class="topRadio">
				<i class="fa fa-certificate"></i> 1. 회원정보에 등록한 휴대전화로 수신
			</label> 
			
			<p>회원정보에 등록한 휴대전화 번호로 <strong style="color:red;">임시비밀번호</strong>를 받아 보실 수 있습니다.</p>
			<div class="form" id="div_1" style="display:;">
			<form name="smsfrm" id="smsfrm" method="post" action="" >
			<input type="hidden" name="tel_no" id="tel_no">
			<input type=hidden name="user_id" id="user_id" value="${input.user_id }">
			
				<label for="user_nm" class="">이름 </label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<input type="text" class="form-control" title="이름" value="" name="user_nm" id="user_nm" required="true"/>
					</div>						
				</div>
				<label for="user_nm" class="">휴대전화 </label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<div class="input-group">
							<select class="form-control " style="width:80px;" name='phone_1' id='phone_1'>
								<option value="010" selected="selected">010</option>
								<option value="011">011</option>
								<option value="016">016</option>
								<option value="017">017</option>
								<option value="019">019</option>
							</select>
							<input type="text" class="form-control" style="width:166px;"  title="휴대전화번호"  value="" name="phone_2" id="phone_2" required="true"/>
						</div>	
					</div>
					<div class="col-md-3 col-sm-3">
						<img src="/static/main/img/sub/icon_q.jpg" alt="" /> <span style="font-weight:bold;color:red;"> (-)없이 입력하세요 (ex:10044545)</span>
					</div>					
				</div>
				
				</form>
				<div class="x_content">
					<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
					<a href="javascript:lf_passFinderSms();" class="btn btn-primary"><span class="">임시비밀번호요청</span></a>
				</div>
			</div>											
		</div>
		<div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
			
			<label for="" class="topRadio">
				<i class="fa fa-certificate"></i> 2. 회원정보에 등록한 이메일로 수신
			</label> 
			
			<p>회원정보에 등록한 이메일로 <strong style="color:red;">임시비밀번호</strong>를 받아 보실 수 있습니다.</p>
			<div class="form" id="div_2" >
				<form name="emailfrm" id="emailfrm" method="post" action="" >
				<input type=hidden name="user_id" id="user_id" value="${input.user_id }">
				<label for="user_nm" class="">이름 </label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<input type="text" class="form-control" title="이름" value="" name="user_nm" id="user_nm" required="true"/>	
					</div>						
				</div>
				
				<label for="user_nm" class="">이메일 </label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<input type="text" class="form-control"  title="이메일" value="" name="email" id="email" required="true"/>
					</div>						
				</div>
				</form>
				<div class="x_content">
					<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
					<a href="javascript:lf_passFinderEmail();" class="btn btn-primary"><span class="">임시비밀번호요청</span></a>
				</div>	
					
			</div>			
		</div>				
	</div>
</div>

<script type="text/javascript" class="source">

jQuery(document).ready(function(){
	
	jQuery('#mobileTitle').html('비밀번호 찾기');
});

function lf_passFinderSms()
{
	if(!checkFormField('#smsfrm'))
	{
		return false;
	}
	jQuery('#tel_no').val( jQuery('#phone_1').val() + jQuery('#phone_2').val() );
	
	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=passFinderSms",
   		type: "POST",
		data : jQuery('#smsfrm').serialize(true),
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
				var resultCode = data.resultCode;

				if( resultCode == '100' )
				{
					msgStart(msg_mem_code_008);
					
				}else if( resultCode == '200' )
				{
					msgStart(msg_mem_code_010);
					
				}else if( resultCode == '300' )
				{
					msgStart(msg_mem_code_008);
				}
				else
				{
					msgStart(msg_com_code_010);
				}
			} else
			{
				msgStart(msg_com_code_010);
				return;
			}
		}
  	});

}

function lf_passFinderEmail()
{
	if(!checkFormField('#emailfrm'))
	{
		return false;
	}
	
	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=passFinderEmail",
   		type: "POST",
		data : jQuery('#emailfrm').serialize(true),
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
				var resultCode = data.resultCode;

				if( resultCode == '100' )
				{
					msgStart(msg_mem_code_008);
					
				}else if( resultCode == '200' )
				{
					msgStart(msg_mem_code_012);
					
				}else if( resultCode == '300' )
				{
					msgStart(msg_mem_code_008);
				}
				else
				{
					msgStart(msg_com_code_010);
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

</body>
</html>