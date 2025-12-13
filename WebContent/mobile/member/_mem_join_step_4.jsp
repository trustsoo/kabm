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
	String mem_div = input.getText("mem_div");

	String thisServerName = request.getServerName();
	int port = request.getLocalPort();
	
%>  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" language="javascript" src="/static/com/js/common_cetify.js"></script>
<script type="text/javascript" class="source">

jQuery(document).ready(function(){
		
});


function lf_certSubmit()
{
	if(!checkFormField('#frm'))
	{
		return;
	}
	
	if( jQuery('#cert_no').val() == '' || jQuery('#rcv_cert_no').val() == '' || jQuery('#rcv_cert_no').val() != jQuery('#cert_no').val()  )
	{
		msgStart( '인증번호가 일치하지 않습니다. 인증번호를 확인 하시기 바랍니다.' );
		return;
	}
	
	jQuery('#tel_no').val(jQuery('#sms_tel_no').val());
	jQuery('#email').val(jQuery('#cert_email').val());
	var yyyy = jQuery('#birth_yyyy').val();
	var mm = jQuery('#birth_mm').val();
	var dd = jQuery('#birth_dd').val();
	jQuery('#ssn').val( yyyy.substring(2,4) +  mm + '' + dd);
	lf_requestSubmit();
}
function lf_requestSubmit()
{
	var url = '/mobile/member/join.jspx?cmd=join_step_5';
	jQuery('#frm').attr('action', url);
	jQuery('#frm').submit();
}

function lf_certSms()
{
	if(!checkFormField('#smsfrm'))
	{
		return;
	}
	jQuery('#sms_tel_no').val( jQuery('#phone_1').val() + jQuery('#phone_2').val() );
	
	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=certSms",
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
				jQuery('#rcv_cert_no').val(resultCode);
				$("#cert_div").val('H');
				jQuery('#divAddition').css("display","");
				msgStart("입력하신 휴대전화로 인증번호를 발송하였습니다.");
			} else
			{
				msgStart(msg_com_code_010);
				return;
			}
		}
  	});

}

function lf_certEmail()
{
	if(!checkFormField('#emailfrm'))
	{
		return false;
	}
	
	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=certEmail",
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

				jQuery('#rcv_cert_no').val(resultCode);
				$("#cert_div").val('E');
				jQuery('#divAddition').css("display","");
				msgStart("입력하신 이메일로 인증번호를 발송하였습니다.");
			} else
			{
				msgStart(msg_com_code_010);
				return;
			}
		}
  	});

}

</script>
<style>
.topRadio{margin-top:5px;font-size:16px;color: #1e90f1;}
.ment{margin-top:10px;}
.stit2{margin-bottom:10px;}
</style>
</head>

<body>
<div id="wizard_verticle" class="form_wizard wizard_verticle">
	<ul class="list-unstyled wizard_steps anchor">
		<li>
			<a href="#step-11" class="selected" isdone="1" rel="1">
				<span class="step_no">1</span>
			</a>
		</li>
		<li>
			<a href="#step-22" class="selected" isdone="1" rel="2">
				<span class="step_no">2</span>
			</a>
		</li>
		<li>
			<a href="#step-33" class="selected" isdone="1" rel="3">
				<span class="step_no">3</span>
			</a>
		</li>
		<li>
			<a href="#step-44" class="selected" isdone="1" rel="4">
				<span class="step_no">4</span>
			</a>
		</li>
		<li>
			<a href="#step-55" class="disabled" isdone="0" rel="5">
				<span class="step_no">5</span>
			</a>
		</li>
		
		<li>
			<a href="#step-66" class="disabled" isdone="0" rel="6">
				<span class="step_no">6</span>
			</a>
		</li>
		<li>
			<a href="#step-77" class="disabled" isdone="0" rel="7">
				<span class="step_no">7</span>
			</a>
		</li>
	</ul>

	<div class="stepContainer" >
	
		<div id="step-11" class="content" style="display: block;">
			<h2 class="StepTitle">수강과목 선택</h2>
			
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
							<i class="fa fa-certificate"></i> 수신가능 휴대전화로 인증
						</label> 
						<% if( MEM_DIV_NEW.equals(mem_div)){ %>
							<div class="stit2" style="color:black;">								
								『신규 및 승계자교육』은 반드시 <span style="font-weight:bold;color:red;">대표자 본인이 이수</span>(대표자 인적사항 작성)하여야 합니다.<br />
						 		<span style="font-weight:bold;color:red;">직원등 다른 이름으로 가입한 경우, 추후 수정할 수 없습니다. </span>
							</div>
						<%}else{%>
							<div class="stit2">
								<span style="font-weight:bold;color:red;">반드시  본인 이름으로 가입 후 교육 이수하셔야 합니다.</span>
							</div>
						<%}%>
						<div class="ment">
							휴대전화 번호로 인증번호를 보내드립니다.<br>
						    수신된 인증번호를 아래에 입력한 후 인증확인을 눌러 본인인증을 하세요.
						</div>
						<div class="form" id="div_1" style="display:;">
						<form name="smsfrm" id="smsfrm" method="post" action="" >
						<input type="hidden" name="sms_tel_no" id="sms_tel_no">
							<table cellpadding="0" cellspacing="0" class="" summary="" >
								<caption></caption>
								<colgroup>
									<col width=""/><col width=""/><col width=""/>
								</colgroup>
								<tbody>
									<tr>
										<th scope="row" colspan="3">휴대전화</th>
									</tr>	
									<tr>
										<td>
											<select name="phone_1" id="phone_1" class="form-control " style="width:75px;margin-right:5px;">
												<option value="010" selected="selected">010</option>
												<option value="011">011</option>
												<option value="016">016</option>
												<option value="017">017</option>
												<option value="019">019</option>
											</select>
										</td>
										<td>	
											<input type="text" class="form-control " style="width:90px;margin-right:5px;"  title="휴대전화번호"  value="" name="phone_2" id="phone_2" required="true"/>
										</td>
										<td>
											<a href="javascript:lf_certSms();" class="btn btn-default">인증요청</a>
										</td>
									</tr>
									<tr>
										<td colspan="3">
											<p class="tip">
												<img src="/static/main/img/sub/icon_q.jpg" alt="" /> (-)없이 입력하세요 (ex:10044545)
											</p>
										</td>
									</tr>
								</tbody>
							</table>
							</form>
						</div>
						<div class="actionBar">
							<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
						</div>											
					</div>
					<div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
						
						<label for="" class="topRadio">
							<i class="fa fa-certificate"></i> 수신가능한 본인 이메일로 수신
						</label> 
						<% if( MEM_DIV_NEW.equals(mem_div)){ %>
							<div class="stit2" style="color:black;">
								『신규 및 승계자교육』은 반드시 <span style="font-weight:bold;color:red;">대표자 본인이 이수</span>(대표자 인적사항 작성)하여야 합니다.<br />
						 		<span style="font-weight:bold;color:red;">직원등 다른 이름으로 가입한 경우, 추후 수정할 수 없습니다. </span>
							</div>
							<%}else{%>
							<div class="stit2">
								<span style="font-weight:bold;color:red;">반드시  본인 이름으로 가입 후 교육 이수하셔야 합니다.</span>
							</div>
						<%}%>
						<div class="ment">수신가능한 본인 이메일로 인증번호를 보내드립니다.</div>
						<div class="form" id="div_2" >
							<form name="emailfrm" id="emailfrm" method="post" action="" >
							
							<table cellpadding="0" cellspacing="0" class="" summary="" >
								<caption></caption>
								<colgroup>
									<col width=""/><col width=""/>
								</colgroup>
								<tbody>
									<tr>
										<th scope="row" colspan="2">이메일</th>
										
									</tr>
									<tr>
										<td>
											<input type="text" class="form-control" style="width:160px;margin-right:5px;" title="이메일" value="" name="cert_email" id="cert_email" required="true"/>
										</td>
										<td>
											<a href="javascript:lf_certEmail();" class="btn btn-default">인증요청</a>											
										</td>
									</tr>							
								</tbody>
							</table>
							</form>
						</div>
						<div class="actionBar">
							<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
						</div>			
					</div>				
				</div>
			</div>
			
			
			<div class="x_content">
				<div style="display:none;" id="divAddition" >
					<form name="frm" id="frm" method="post" action="">
					<input type=hidden name="mem_div" id="mem_div" value="<%=mem_div%>">
					<input type=hidden name="memName" id="memName" value="">
					<input type=hidden name="memCode" id="memCode" value="">
					<input type=hidden name="memBirth" id="memBirth" value="">					
					<input type="hidden" name='ssn' id='ssn' value=''/>
					<input type="hidden" name='tel_no' id='tel_no' value=''/>
					<input type="hidden" name='email' id='email' value=''/>
					<input type="hidden" name='cert_div' id='cert_div' value=''/>
					<input type="hidden" name="rcv_cert_no" id="rcv_cert_no">
					
								
					<div class="section-1 padding_15"  style="margin-top: 15px;">
						<div class="con_600">
							<label for="" class="topRadio">
								<i class="fa fa-certificate"></i> 인증 정보를 입력하세요.
							</label>
							
							<div class="form-group row">
								<label for="cert_no" class="col-form-label col-md-3 col-sm-3 label-align">인증번호</label>
								<div class="col-md-6 col-sm-6">
								<input type="text" class="form-control " title="인증번호" value="" maxlength="10"  name="cert_no" id="cert_no" required="true"/>
								</div>
							</div>
							
						</div>
					</div>	
				
			
					<div class="section-1 padding_15"  style="margin-top: 25px;">
						<div class="con_600">
							<% if( MEM_DIV_NEW.equals(mem_div)){ %>
								<div class="stit2" style="font-weight:bold;color:red;">
									신규 및 승계자 교육은 반드시 대표자 본인이 이수하여야 합니다.<br />
									만일, 다른 이름으로 가입 후 교육 이수하셔도 추후 수정할 수 없습니다.<br />
								</div>
								<%}else{%>
								<div class="stit2" style="font-weight:bold;color:red;">
									교육생은 반드시 ‘업체의 교육받는 자’입니다.<br />
									다른 교육생으로 가입하여 교육이수 하여도 추후 변경이 불가합니다.<br />
								</div>
							<%}%>
							<label for="cert_no" class="">성&nbsp;&nbsp;명</label>
							<div class="form-group row">								
								<div class="col-md-12 col-sm-12">
								<input class="form-control " type="text" title="성명" value="" maxlength="20"  name="user_nm" id="user_nm" required="true">
								</div>
							</div>
							<label for="sex" class="">성&nbsp;&nbsp;별</label>
							<div class="form-group row">								
								<div class="col-md-12 col-sm-12">
									<select id="sex" name="sex" class="form-control ">
										<option value="1">남</option>
										<option value="2">여</option>
									</select>
								</div>
							</div>
							<label for="birth_yyyy" class="">생년월일</label>
							<div class="form-group row">								
								<div class="col-md-3 col-sm-3">
									<select name="birth_yyyy" id="birth_yyyy" required='true' alt="년" class="form-control " style="padding: 0 0px !important;">
										<option value="">년도</option>
										<c:forEach begin="1935" end="${currentYear - 9}" step="1" var="YY">
										<c:set var="decr" value="${currentYear-9 - YY + 1935}"/>
							   			   	<option value="${decr}" >${decr}년</option>
										</c:forEach>
									</select>
								</div>
								<div class="col-md-3 col-sm-3">
									<select name="birth_mm" id="birth_mm" required='true' alt="월" class="form-control " style="padding: 0 0px !important;">
										<option value="">월</option>
									  	<option value="01" >01월</option>
									  	<option value="02" >02월</option>
									  	<option value="03" >03월</option>
									  	<option value="04" >04월</option>
									  	<option value="05" >05월</option>
									  	<option value="06" >06월</option>
									  	<option value="07" >07월</option>
									  	<option value="08" >08월</option>
									  	<option value="09" >09월</option>
									  	<option value="10" >10월</option>
									  	<option value="11" >11월</option>
									  	<option value="12" >12월</option>
									</select>
								</div>
								<div class="col-md-3 col-sm-3">
									<select name="birth_dd" id="birth_dd" required='true' alt="일" class="form-control " style="padding: 0 0px !important;">
										<option value="">일</option>
										<option value="01" >01일</option>
									  	<option value="02" >02일</option>
									  	<option value="03" >03일</option>
									  	<option value="04" >04일</option>
									  	<option value="05" >05일</option>
									  	<option value="06" >06일</option>
									  	<option value="07" >07일</option>
									  	<option value="08" >08일</option>
									  	<option value="09" >09일</option>
									  	<option value="10" >10일</option>
									  	<option value="11" >11일</option>
									  	<option value="12" >12일</option>	
									  	<option value="11" >11일</option>
									  	<option value="12" >12일</option>
									  	<option value="13" >13일</option>
									  	<option value="14" >14일</option>
									  	<option value="15" >15일</option>
									  	<option value="16" >16일</option>
									  	<option value="17" >17일</option>
									  	<option value="18" >18일</option>
									  	<option value="19" >19일</option>
									  	<option value="20" >20일</option>
									  	<option value="21" >21일</option>
									  	<option value="22" >22일</option>
									  	<option value="21" >21일</option>
									  	<option value="22" >22일</option>
									  	<option value="23" >23일</option>
									  	<option value="24" >24일</option>
									  	<option value="25" >25일</option>
									  	<option value="26" >26일</option>
									  	<option value="27" >27일</option>
									  	<option value="28" >28일</option>
									  	<option value="29" >29일</option>
									  	<option value="30" >30일</option>
									  	<option value="31" >31일</option>
									</select>
								</div>			
							</div>
								
						</div>
					</div>
					</form>
					
					<div class="actionBar">
						<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
						<a href="javascript:lf_certSubmit();" class="btn btn-success">다음</a>
					</div>
				</div>			
			</div>
		</div>				
	</div>	
</div>

</body>
</html>