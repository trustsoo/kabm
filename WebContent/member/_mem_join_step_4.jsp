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
		alert( '인증번호가 일치하지 않습니다. 인증번호를 확인 하시기 바랍니다.' );
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
	var url = '/member/join.jspx?cmd=join_step_5';
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
				msgOpen("입력하신 휴대전화로 인증번호를 발송하였습니다.");
			} else
			{
				msgOpen(msg_com_code_010);
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
				msgOpen("입력하신 이메일로 인증번호를 발송하였습니다.");
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
	
	//lf_requestSubmit();
	
	
	cf_certifySms("https://<%=thisServerName%>:<%=port%>/member/smsCert/smsCert.jspx?cmd=smsResultUrl","https://<%=thisServerName%>:<%=port%>/member/smsCert/smsCert.jspx?cmd=smsErrorUrl");
	
}

function smsResponse() {
	
	$.ajax({
		url : '/member/smsCert/action/smsCert.jspx?cmd=smsCheck',
		type : 'POST',
		dataType : 'json',
		data : {
			"enc_data" : encodeURIComponent(document.form_sms.EncodeData.value)
		},
		success:function(data){
			if( data.iRtn == "0") {
				
				$("#memName").val(data.sName);
				$("#memCode").val(data.sDupInfo);
				$("#memBirth").val(data.sBirthDate);
				
				$("#user_nm").val(data.sName);
				$("#sex").val(data.sGenderCode);
				$("#birth_yyyy").val(data.sBirthyyyy);
				$("#birth_mm").val(data.sBirthmm);
				$("#birth_dd").val(data.sBirthdd);
				$("#ssn").val(data.ssn);
				$("#tel_no").val(data.sMobileNo);
				$("#cert_div").val('M');
				var sUserId = data.sUserId;
				
				if( sUserId != '' &&  sUserId != 'null' && sUserId != 'undefind')
				{
					if(confirm("이미 등록된 회원 정보가 있습니다.["+sUserId+"] \n계속하시겠습니까?" ) )
							lf_requestSubmit();
				}else{
					lf_requestSubmit();
				}
			}else {
				msgOpen( data.sRtnMsg );
			}
		},
		error:function(){
			msgOpen("데이터 통신에 실패하였습니다.");
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
			msgOpen("데이터 통신에 실패하였습니다.");
		}
	});
	
}


function setTabView(code, divTabName)
{
	jQuery('div#' + divTabName + ' > ul > li').each(
		function(i)
		{
			if(  code == jQuery(this).attr("code") )
			{
				jQuery(this).addClass("on");
			}
			else
				jQuery(this).removeClass("on");
		}
	);


	switch(code)
	{
		case 'M' :
			jQuery('#divM').css("display","block");
			jQuery('#divH').css("display","none");
			jQuery('#divE').css("display","none");
			jQuery('#divAddition').css("display","none");
			break;
		case 'H' :
			jQuery('#divM').css("display","none");
			jQuery('#divH').css("display","block");
			jQuery('#divE').css("display","none");
			jQuery('#divAddition').css("display","none");
			break;
        case 'E' :
			jQuery('#divM').css("display","none");
			jQuery('#divH').css("display","none");
			jQuery('#divE').css("display","block");
			jQuery('#divAddition').css("display","none");
			break;
		

	}


}
</script>

</head>

<body>

	<div class="pageTit">
		<h2><span class="tit_1">회원가입</span></h2>
	</div>
	<div id="content">
			<ul class="joinTab">				
			<% if( MEM_DIV_NEW.equals(mem_div)){ %>
			<!-- 신규교육 스탭 -->
				<li><img src="/static/main/img/sub/join_tab_1_2.jpg"  alt="신규교육" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_2_1_off.jpg"  alt="약관동의" /></li>
			<%}%>
			<% if( MEM_DIV_REP.equals(mem_div)){ %>
			<!-- 책임자교육  스탭 -->
				<li><img src="/static/main/img/sub/join_tab_1_4.jpg"  alt="책임자교육" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_2_2_off.jpg"  alt="약관동의" /></li>
			<%}%>
				<li class=""><img src="/static/main/img/sub/join_tab_3_off.jpg"  alt="교육동의" /></li>
				<li class="on"><img src="/static/main/img/sub/join_tab_4_on.jpg"  alt="본인확인" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_5_off.jpg"  alt="기본정보입력" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_6_off.jpg"  alt="회사정보입력" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_7_off.jpg"  alt="등록확인" /></li>
			</ul>
			<div id="reg_tab_div">
			<ul class="contTabs4" id="reg_tab">
                <li onclick="setTabView('M', 'reg_tab_div');" class="on" code="M">방법1. 본인 명의 휴대전화로 인증</li>
                <li onclick="setTabView('H', 'reg_tab_div');" class="" code="H">방법2. 수신가능 휴대전화로 인증</li>
                <li onclick="setTabView('E', 'reg_tab_div');" class="" code="E">방법3. 수신가능한 본인 이메일로 수신</li>
            </ul>
            </div>
			<div class="section-1" id="divM" style="display:;">
				<div style="width:650px; margin:0 auto;">
					<label for="" class="topRadio">
						방법1. 본인 명의 휴대전화로 인증
						<span class="">(본인 주민등록번호로 가입된 휴대전화)</span>
					</label>					
					<% if( MEM_DIV_NEW.equals(mem_div)){ %>
					<div class="stit2" style="color:black;">
						「신규 및 승계자교육」은 반드시 <span style="font-weight:bold;color:red;">대표자 본인이 이수 (대표자 인적사항 작성)</span>하여야 합니다.<br />
						 <span style="font-weight:bold;color:red;">직원 등 다른 이름으로 가입한 경우, 추후 수정할 수 없습니다.</span>
					</div>
					<%}else{%>
					<div class="stit2">
						반드시  본인 이름으로 가입 후 교육 이수하셔야 합니다.
					</div>
					<%}%>
					<div class="ment">
						개인정보보호를 위해 주민등록번호 대신 모바일 인증으로 신원을 확인합니다.<br />
						아래의 모바일 인증을 받으시면 다음 단계로 진행하실 수 있습니다.
					</div>

					<div class="btnWrap">
						<a href="javascript:fn_certifySms();"><img src="/static/main/img/sub/btn_mobile.jpg" alt="모바일인증" /></a>
					</div>
				</div>
			</div>
			
			
			<div class="section-1 padding_15"  id="divH" style="display:none;">
			<div style="width:650px; margin:0 auto;">
				<label for="" class="topRadio">
					방법2. 수신가능 휴대전화로 인증
				</label> 
				<% if( MEM_DIV_NEW.equals(mem_div)){ %>
					<div class="stit2" style="color:black;">
						「신규 및 승계자교육」은 반드시 <span style="font-weight:bold;color:red;">대표자 본인이 이수 (대표자 인적사항 작성)</span>하여야 합니다.<br />
						<span style="font-weight:bold;color:red;">직원 등 다른 이름으로 가입한 경우, 추후 수정할 수 없습니다.</span>
					</div>
					<%}else{%>
					<div class="stit2">
						반드시  본인 이름으로 가입 후 교육 이수하셔야 합니다.
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
		<div class="section-1 padding_15"  id="divE" style="display:none;">
			<div style="width:650px; margin:0 auto;">
				<label for="" class="topRadio">
					방법3. 수신가능한 본인 이메일로 수신
				</label> 
				<% if( MEM_DIV_NEW.equals(mem_div)){ %>
					<div class="stit2" style="color:black;">
						「신규 및 승계자교육」은 반드시 <span style="font-weight:bold;color:red;">대표자 본인이 이수 (대표자 인적사항 작성)</span>하여야 합니다.<br />
						<span style="font-weight:bold;color:red;">직원 등 다른 이름으로 가입한 경우, 추후 수정할 수 없습니다.</span>
					</div>
					<%}else{%>
					<div class="stit2">
						반드시  본인 이름으로 가입 후 교육 이수하셔야 합니다.
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
								<th scope="row" >이메일</th>
								<td>
									<input type="text" class="it preload"  title="이메일" value="" name="cert_email" id="cert_email" required="true"/>
								</td>
								<td>
									<a href="javascript:lf_certEmail();" class="pbtn02 mid"><span class="">인증요청</span></a>
								</td>
							</tr>							
						</tbody>
					</table>
					</form>
				</div>
			</div>
		</div>

		<div style="display:none;" id="divAddition" >
			<form name="frm" id="frm" method="post" action="">
			<input type=hidden name="mem_div" id="mem_div" value="<%=mem_div%>">
			<input type=hidden name="memName" id="memName" value="">
			<input type=hidden name="memCode" id="memCode" value="">
			<input type=hidden name="memBirth" id="memBirth" value="">
			<!-- <input type="hidden" name='user_nm' id='user_nm' value=''/> -->
			<!-- <input type="hidden" name='sex' id='sex' value=''/>
			<input type="hidden" name='birth_yyyy' id='birth_yyyy' value=''/>
			<input type="hidden" name='birth_mm' id='birth_mm' value=''/>
			<input type="hidden" name='birth_dd' id='birth_dd' value=''/> -->
			<input type="hidden" name='ssn' id='ssn' value=''/>
			<input type="hidden" name='tel_no' id='tel_no' value=''/>
			<input type="hidden" name='email' id='email' value=''/>
			<input type="hidden" name='cert_div' id='cert_div' value=''/>
			<input type="hidden" name="rcv_cert_no" id="rcv_cert_no">
						
						
			<div class="section-1 padding_15"  style="margin-top: 25px;">
				<div class="con_600">
					<label for="" class="topRadio">
						인증 정보를 입력하세요.
					</label>
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
						</tbody>
					</table>
				</div>
			</div>	
		
	
			<div class="section-1 padding_15"  style="margin-top: 25px;">
				<div class="con_600">
					<% if( MEM_DIV_NEW.equals(mem_div)){ %>
						<div class="stit2">
							신규 및 승계자 교육은 반드시 대표자 본인이 이수하여야 합니다.<br />
							만일, 다른 이름으로 가입 후 교육 이수하셔도 추후 수정할 수 없습니다.
						</div>
						<%}else{%>
						<div class="stit2">
							교육생은 반드시 ‘업체의 교육받는 자’입니다.<br />
							다른 교육생으로 가입하여 교육이수 하여도 추후 변경이 불가합니다.
						</div>
					<%}%>
					
					<table cellpadding="0" cellspacing="0" class="" summary="" >
						<caption></caption>
						<colgroup>
							<col width=""/><col width=""/>
						</colgroup>
						<tbody>
							<tr>
								<th scope="row" >성명</th>
								<td colspan="3">
									<input type="text"  class="it preload"  title="성명" value="" maxlength="20"  name="user_nm" id="user_nm" required="true"/>
								</td>
							</tr>	
							<tr>
								<th scope="row" >성별</th>
								<td colspan="3">
									<select id="sex" name="sex" style="width:100px;">
										<option value="1">남</option>
										<option value="2">여</option>
									</select>
								</td>
							</tr>	
							<tr>
								<th scope="row" >생년월일</th>
								<td>
									<select name="birth_yyyy" id="birth_yyyy" required='true' alt="년" style="width:100px;">
										<option value="">년도선택</option>
									<c:forEach begin="1935" end="${currentYear - 9}" step="1" var="YY">
									<c:set var="decr" value="${currentYear-9 - YY + 1935}"/>
						   			   	<option value="${decr}" >${decr}년</option>
									</c:forEach>									
									</select>년									
								</td>
								<td>
									<select name="birth_mm" id="birth_mm" required='true' alt="월" style="width:100px;">
										<option value="">월선택</option>
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
									</select>월
								</td>
								<td>
									<select name="birth_dd" id="birth_dd" required='true' alt="일" style="width:100px;">
										<option value="">일선택</option>
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
									</select>일
								</td>
							</tr>							
						</tbody>
					</table>
				<div class="form" id="div_3" >
					<div class="btnWrap">
						<a href="javascript:lf_certSubmit();" class="pbtn02"><span class="">다음</span></a>
					</div>
				</div>
				
			</div>
		</div>
			</form>
		</div>

</div>
<!--  SMS 서비스 팝업 호출용 팝업 -->
<form name="form_sms" method="post" target="_popupsms">
<input type="hidden" name="m" value="checkplusSerivce" />
<input type="hidden" name="EncodeData" />
<input type="hidden" name="param_r1" value="" />
<input type="hidden" name="param_r2" value="" />
<input type="hidden" name="param_r3" value="" />
</form>

</body>
</html>