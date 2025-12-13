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
	jQuery('#frm input[name="jcode"]').val('');
	jQuery('#confirm_step').val(1);
	js_nextStep();
}


function js_nextStep()
{
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=viewOfflineList';
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

function lf_certJupsu()
{
	if(!checkFormField('#jupsufrm'))
	{
		return;
	}
	
	var http = jQuery.ajax( {
		url: "/edu/lecture/action/offlineAction.jspx?cmd=certJupsu",
   		type: "POST",
		data : jQuery('#jupsufrm').serialize(true),
		async : false,
		datatype: 'json',
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_010);
		},
   		success:function(jsonObj)
   		{
   			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.offline_jupsu_info;
				var jsonCnt = jsonObj.result.data.offline_jupsu_info.length;
				
				
				if(jsonCnt > 0){
					var i = 0;
					var req_user_nm = json[i].req_user_nm;
					var req_tel_no   = json[i].req_tel_no; 
					
					if( req_user_nm != null && req_user_nm != '' && req_tel_no != null && req_tel_no != '' )
					{
						jQuery('#req_user_nm').val(req_user_nm);
						jQuery('#req_tel_no').val(req_tel_no);
						jQuery('#confirm_step').val(2);	
						jQuery('#frm input[name="jcode"]').val(jQuery('#jupsufrm input[name="jcode"]').val() );
						js_nextStep();					
					}else{
						msgOpen(msg_edu_code_010);
					}
				}else{
					msgOpen(msg_edu_code_010);
				}
   			} else
			{
				msgOpen(msg_com_code_010);
				return;
			}
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
			jQuery('#divAddition').css("display","none");
			break;
		case 'H' :
			jQuery('#divM').css("display","none");
			jQuery('#divH').css("display","block");
			jQuery('#divAddition').css("display","none");
			break;
	}


}
function lf_reqCancel(){
	
	location.href='';
}

</script>

</head>

<body>

	<div id="content">
			<div id="reg_tab_div">
			<ul class="contTabs4" id="reg_tab">
                <li onclick="setTabView('M', 'reg_tab_div');" class="on" code="M" style="height:70px;line-height:20px;text-align: left;padding:5px;">
                <div>1.신청자 휴대전화로 확인<br> <span style="font-size:12px;">신청자가 접수한 전체내역을 확인하실 경우 클릭하세요.</span></div>
                </li>
                <li onclick="setTabView('H', 'reg_tab_div');" class="" code="H" style="height:70px;line-height:20px;text-align: left;padding:5px;">
                <div>2.접수코드로 확인<br><span style="font-size:12px;">접수완료시 부여된 접수코드로 해당 접수건만 확인하실 경우 클릭하세요.</span></div>
                </li>
            </ul>
            </div>
			<div class="section-1" id="divM" style="display:;margin-top:0px;">
				<div class="" style="margin-bottom:10px;padding-left: 160px;">
					<label for="" style="font-weight:bold;font-size:14px;color:#ff0016;">
						 신청자 정보를 입력하세요.
					</label> 
					<br>
					<label for="" style="font-weight:bold;font-size:13px;color:#00b9ed ;">
						※ 신청자와 교육생이 다를 경우 반드시 신청자 정보를 입력하여 주시기 바랍니다.
					</label> 
				</div>
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
			<div class="section-1 padding_15"  id="divH" style="display:none;margin-top:0px;">
			<div class="" style="margin-bottom:10px;padding-left: 170px;">
					<label for="" style="font-weight:bold;font-size:14px;color:#ff0016;">
						 접수완료시 부여된 접수코드를 입력하세요.
					</label> 
					<br>
					<label for="" style="font-weight:bold;font-size:13px;color:#00b9ed ;">
						※ 접수코드를 모르시면 [신청자 휴대전화로 확인]을 통해서만 확인 가능합니다.
					</label> 
				</div>
			<div class="con_600">
				<label for="" class="topRadio">
					2. 접수코드로 확인
				</label> 				
				<div class="form" id="div_1" style="display:;">
				<form name="jupsufrm" id="jupsufrm" method="post" action="" >
					<table cellpadding="0" cellspacing="0" class="" summary="" >
						<caption></caption>
						<colgroup>
							<col width=""/><col width=""/>
						</colgroup>
						<tbody>
							<tr>
								<th scope="row" >접수코드</th>
								<td>
									<input type="text" class="it preload" style="width:100%;"  title="접수번호"  value="" name="jcode" id="jcode" required="true"/>
									
								</td>
								<td>
									<a href="javascript:lf_certJupsu();" class="pbtn02 mid"><span class="">확인</span></a>
								</td>
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
				<input type='hidden' name='confirm_step' id='confirm_step' value='1'>	
				<input type="hidden" name="req_tel_no" id="req_tel_no" value="">
				<input type="hidden" name="jcode" id="jcode" value="">
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
									<input type="text"  class="it preload"  title="성명" value="" maxlength="20"  name="req_user_nm" id="req_user_nm" required="true"/>
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
<script type="text/javascript">
	jQuery(document).ready(function(){
			
		   jQuery('.tit_1').text('집합교육 신청내역 확인하기');
		}
	);
	
</script>
</body>
</html>