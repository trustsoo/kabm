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
<script type="text/javascript" class="source">

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
					msgOpen(msg_mem_code_008);
					
				}else if( resultCode == '200' )
				{
					msgOpen(msg_mem_code_010);
					
				}else if( resultCode == '300' )
				{
					msgOpen(msg_mem_code_008);
				}
				else
				{
					msgOpen(msg_com_code_010);
				}
			} else
			{
				msgOpen(msg_com_code_010);
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
					msgOpen(msg_mem_code_008);
					
				}else if( resultCode == '200' )
				{
					msgOpen(msg_mem_code_012);
					
				}else if( resultCode == '300' )
				{
					msgOpen(msg_mem_code_008);
				}
				else
				{
					msgOpen(msg_com_code_010);
				}
			} else
			{
				msgOpen(msg_com_code_010);
				return;
			}
		}
  	});

}
//SMS
function fn_certifySms() {
	
	cf_certifySms("https://<%=thisServerName%>:<%=port%>/member/smsCert/smsCert.jspx?cmd=smsResultUrl","https://<%=thisServerName%>:<%=port%>/member/smsCert/smsCert.jspx?cmd=smsErrorUrl");
	
}

function smsResponse() {
	
	$.ajax({
		url : '/member/smsCert/action/smsCert.jspx?cmd=smsPassCheck',
		type : 'POST',
		dataType : 'json',
		data : {
			"enc_data" : encodeURIComponent(document.form_sms.EncodeData.value),
			"user_id" : '${input.user_id}'
		},
		success:function(data){
			if( data.iRtn == "0") {
				
				var password = data.password;
				
				if( password != '' &&  password != 'null' && password != 'undefind')
				{
					jQuery('#txt_passwd').html(password);
					jQuery('#txt_idbox').show();
					msgOpen( '회원님 임시비밀번호는 [<b style="color: red;font-weight:bold;">'+password+'</b>] 입니다.' );
				}else{
					msgOpen( msg_mem_code_008 );
				}
			}else {
				msgOpen( data.sRtnMsg );
			}
		},
		error:function(){
			msgOpen(msg_com_code_029);
		}
	});
}

function smsError() {
	
	$.ajax({
		url : '/member/smsCert/action/smsCert.jspx?cmd=smsError',
		type : 'POST',
		dataType : 'json',
		data : {
			"enc_data" : encodeURIComponent(document.form_sms.EncodeData.value)
		},
		success:function(data){
			if( data.iRtn == "0") {
				
				var errCode = data.sErrorCode;
				var msg = '죄송합니다. 인증에 실패하였습니다. :';
				if( errCode == '0001' ) msg += '인증불일치(사용자인증, 인증번호 불일치)';
				else if( errCode == '0011' ) msg += '유효하지 않은 응답 SEQ';
				else if( errCode == '0012' ) msg += '유효하지 않은 인증정보(주민번호, 휴대폰번호, 이통사)';
				else if( errCode == '0013' ) msg += '암호화 데이터 처리오류';
				else if( errCode == '0014' ) msg += '암호화 프로세스 오류';
				else if( errCode == '0015' ) msg += '암호화 데이터 오류';
				else if( errCode == '0016' ) msg += '복호화 프로세스 오류';
				else if( errCode == '0017' ) msg += '복호화 데이터 오류';
				else if( errCode == '0018' ) msg += '이통사 통신오류';
				else if( errCode == '0020' ) msg += '유효하지 않은 제휴사 코드';
				else if( errCode == '0021' ) msg += '중단된 제휴사 코드';
				else if( errCode == '0022' ) msg += '휴대폰인증 사용이 불가한 제휴사 코드';
				else if( errCode == '0031' ) msg += '인증번호 확인 실패(해당 데이터 없음)';
				else if( errCode == '0032' ) msg += '인증번호 확인 실패(주민번호 불일치)';
				else if( errCode == '0033' ) msg += '인증번호 확인 실패(요청SEQ 불일치)';
				else if( errCode == '0034' ) msg += '인증번호 확인 실패(기 처리건)';
				else if( errCode == '0050' ) msg += '명의도용 차단서비스 가입자';
				else if( errCode == '9998' ) msg += '본인인증 결과값 전달 실패';
				else if( errCode == '9999' ) msg += '정의되지 않은 오류';
				
			}else {
				msgOpen( data.sRtnMsg );
			}
		},
		error:function(){
			msgOpen(msg_com_code_029);
		}
	});
	
}

</script>

</head>

<body>

	<div class="pageTit">
		<h2><span class="tit_1">비밀번호 찾기</span></h2>
		<p class="rTxt">
			비밀번호 찾는 방법을 선택해주세요.
		</p>
	</div>
	<div id="content">
		<div class="section-1 padding_15">
			<div class="con_600">
				<label for="" class="topRadio">
					1. 회원정보에 등록한 휴대전화로 수신
				</label> 
				<p>회원정보에 등록한 휴대전화 번호로 <strong style="color:red;">임시비밀번호</strong>를 받아 보실 수 있습니다.</p>
				<div class="form" id="div_1" style="display:;">
					<form name="smsfrm" id="smsfrm" method="post" action="" >
					<input type="hidden" name="tel_no" id="tel_no">
					<input type=hidden name="user_id" id="user_id" value="${input.user_id }">
					<table cellpadding="0" cellspacing="0" class="" summary="" >
						<caption></caption>
						<colgroup>
							<col width=""/><col width=""/>
						</colgroup>
						<tbody>
							<tr>
								<th scope="row">이름</th>
								<td><input type="text" class="it " title="이름" value="" name="user_nm" id="user_nm" required="true"/></td>
								<td></td>
							</tr>
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
									<a href="javascript:lf_passFinderSms();" class="pbtn02 mid"><span class="">임시비밀번호요청</span></a>
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
		<div class="section-1 padding_15">
			<div class="con_600">
				<label for="" class="topRadio">
					2. 회원정보에 등록한 이메일로 수신
				</label> 
				<p>회원정보에 등록한 이메일로 <strong style="color:red;">임시비밀번호</strong>를 받아 보실 수 있습니다.</p>
				<div class="form" id="div_2" >
					<form name="emailfrm" id="emailfrm" method="post" action="" >
					<input type=hidden name="user_id" id="user_id" value="${input.user_id }">
					<table cellpadding="0" cellspacing="0" class="" summary="" >
						<caption></caption>
						<colgroup>
							<col width=""/><col width=""/>
						</colgroup>
						<tbody>
							<tr>
								<th scope="row">이름</th>
								<td><input type="text" class="it " title="이름" value="" name="user_nm" id="user_nm" required="true"/></td>
								<td></td>
							</tr>
							<tr>
								<th scope="row" >이메일</th>
								<td>
									<input type="text" class="it preload"  title="이메일" value="" name="email" id="email" required="true"/>
								</td>
								<td>
									<a href="javascript:lf_passFinderEmail();" class="pbtn02 mid"><span class="">임시비밀번호요청</span></a>
								</td>
							</tr>							
						</tbody>
					</table>
					</form>
				</div>
			</div>
		</div>
		<div class="section-1 padding_15">
			<div class="con_600">
				<label for="" class="topRadio">
					3. 본인 명의 휴대전화로 인증
					<span class="">(본인 주민등록번호로 가입된 휴대전화)</span>
				</label>
				<p>등록한 회원정보로 찾기 어려우시면, 본인 확인 후 <strong style="color:red;">임시비밀번호</strong>를 찾아드립니다.</p>
				
				<div class="form" id="div_3" >
					<div class="ment">
						개인정보보호를 위해 주민등록번호 대신 모바일 인증으로 신원을 확인합니다.<br />
						아래의 모바일 인증을 받으시면 임시비밀번호를 확인하실 수 있습니다.
					</div>
	
					<div class="btnWrap">
						<a href="javascript:fn_certifySms();"><img src="/static/main/img/sub/btn_mobile.jpg" alt="모바일인증" /></a>
					</div>
				</div>
			</div>
		</div>
		<div id="txt_idbox" style="display:none;position: relative;margin-top: 10px;width:100%;border: 1px solid #eff2e3;background: #f8faf4;padding: 8px 19px;text-align: center;">
			<dl class="enter_info">
				<dt>회원님 임시비밀번호는 [<b id="txt_passwd" sytle="color: red;"></b>] 입니다.
				</dt>
			</dl>
		</div>
	</div>
</form>

<!--  SMS 서비스 팝업 호출용 팝업 -->
<form name="form_sms" method="post">
<input type="hidden" name="m" value="checkplusSerivce" />
<input type="hidden" name="EncodeData" />
<input type="hidden" name="param_r1" value="" />
<input type="hidden" name="param_r2" value="" />
<input type="hidden" name="param_r3" value="" />
</form>

</body>
</html>