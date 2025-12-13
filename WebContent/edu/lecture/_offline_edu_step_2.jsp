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

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" language="javascript" src="/static/com/js/common_cetify.js"></script>
<script type="text/javascript" class="source">

function lf_certSubmit()
{
	if(!checkFormField('#frm'))
	{
		return;
	}
	
	if( jQuery('#cert_no').val() == '' || jQuery('#rcv_cert_no').val() == '' || jQuery('#rcv_cert_no').val() != jQuery('#cert_no').val()  )
	{
		alert( '인증번호가 일치하지 않습니다. 인증번호를 확인 하시기 바랍니다.' );
		return;
	}
	
	jQuery('#req_tel_no').val(jQuery('#sms_tel_no').val());
	js_nextStep();
}


function js_nextStep()
{
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep3';
	jQuery('#frm').attr('action', _url);
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
		url: "/edu/lecture/action/offlineAction.jspx?cmd=certSms",
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
				//jQuery('#cert_no').val(resultCode);
				jQuery('#divAddition').css("display","");
				msgOpen("입력하신 휴대전화로 인증번호를 발송하였습니다.");
			} else
			{
				msgOpen(msg_com_code_010);
				return;
			}
		}
  	});

}

function lf_reqCancel(){
	
	location.href='';
}

</script>

</head>

<body>

	<div id="content">
		<div id="smartwizard" class="sw-main sw-theme-arrows" style="margin-bottom: 15px;">
		<ul class="nav nav-tabs step-anchor">
            <li class="nav-item"><a href="#step-1"><strong>1 단계</strong><br><strong>개인정보 제공 동의</strong></a></li>
            <li class="nav-item active"><a href="#step-2"><strong>2 단계</strong><br><strong>신청자 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-3"><strong>3 단계</strong><br><strong>신청업체 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-4"><strong>4 단계</strong><br><strong>영업장 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-5"><strong>5 단계</strong><br><strong>책입자 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-6"><strong>6 단계</strong><br><strong>입금정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-7"><strong>7 단계</strong><br><strong>접수완료</strong></a></li>
            <li class="nav-item done"><a href="#step-8"><strong>8 단계</strong><br><strong>결제 하기</strong></a></li>
        </ul>        
		</div>
		<div class="" style="margin-top:10px;">
			<label for="" style="font-weight:bold;font-size:14px;color:#ff0016;">
				 신청자 정보를 입력하세요.
			</label> 
			<br>
			<label for="" style="font-weight:bold;font-size:13px;color:#00b9ed ;">
				※ 신청자와 교육생이 다를 경우 반드시 신청자 정보를 입력하여 주시기 바랍니다.
			</label> 
		</div>
	
			<div class="section-1" id="divM" style="display:;">
				<div class="con_600">
				<label for="" class="topRadio">
					1. 신청자 휴대전화로 인증
				</label> 
				<p>
					휴대전화 번호로 인증번호를 보내드립니다.<br>
				    수신된 인증번호를 아래에 입력한 후 인증확인을 눌러 본인인증을 하세요.
				</p>
				<div class="form" id="div_1" style="display:;">
				<form name="smsfrm" id="smsfrm" method="post" action="" >
				<input type="hidden" name="sms_tel_no" id="sms_tel_no">
				<input type="hidden" name="rcv_cert_no" id="rcv_cert_no">
					<table cellpadding="0" cellspacing="0" class="" summary="" >
						<caption></caption>
						<colgroup>
							<col width=""/><col width=""/>
						</colgroup>
						<tbody>
							<tr>
								<th scope="row" >휴대전화</th>
								<td>
									<select name="phone_1" id="phone_1" style="width:80px;">
										<option value="010" selected="selected">010</option>
										<option value="011">011</option>
										<option value="016">016</option>
										<option value="017">017</option>
										<option value="019">019</option>
									</select>
									<input type="text" class="it preload" style="width:166px;"  title="휴대전화번호"  value="" name="phone_2" id="phone_2" required="true"/>
									
								</td>
								<td>
									<a href="javascript:lf_certSms();" class="pbtn02 mid"><span class="">인증요청</span></a>
								</td>
							</tr>
							<tr>
								<th scope="row"></th>
								<td><p class="tip">
										<img src="/static/main/img/sub/icon_q.jpg" alt="" /> (-)없이 입력하세요 (ex:10044545)
									</p></td>
								<td></td>
							</tr>
						</tbody>
					</table>
					</form>
				</div>
			</div>
			
			</div>
			
		<div class="btnWrap">
			<a href="/edu/lecture/offlineCtrl.jspx?cmd=offlineSelect" class="pbtn01"><span class="">취소</span></a>
		</div>
		
		<div class="section-1 padding_15" style="display:none;" id="divAddition" >
			<div class="con_600">
				<label for="" class="topRadio">
					2. 인증 정보를 입력하세요.
				</label>
				<form name="frm" id="frm" method="post" action="">
				<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
				<input type='hidden' name='dcm_no' id='dcm_no' value='${input.dcm_no}'>
				<input type='hidden' name='cp_code' id='cp_code' value='${input.cp_code}'>
				<input type='hidden' name='ji_code' id='ji_code' value='${input.ji_code}'>
				<input type='hidden' name='next_step' id='next_step' value='3'>	
				<input type="hidden" name="req_tel_no" id="req_tel_no" value="">
					<table cellpadding="0" cellspacing="0" class="" summary="" >
						<caption></caption>
						<colgroup>
							<col width=""/><col width=""/>
						</colgroup>
						<tbody>
							<tr>
								<th scope="row" >인증번호</th>
								<td colspan="3">
									<input type="text" class="it preload"  title="인증번호" value="" maxlength="10"  name="cert_no" id="cert_no" required="true"/>
								</td>
							</tr>
							<tr>
								<th scope="row" >신청자 성명</th>
								<td colspan="3">
									<input type="text"  class="it preload"  title="신청자 성명" value="" maxlength="20"  name="req_user_nm" id="req_user_nm" required="true"/>
								</td>
							</tr>	
										
						</tbody>
					</table>
				</form>
				<div class="form" id="div_3" >
					<div class="btnWrap">
						<a href="javascript:lf_certSubmit();" class="pbtn02"><span class="">다음</span></a>
					</div>
				</div>
				
			</div>
		</div>	
	</div>

</body>
</html>