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
<script type="text/javascript" class="source">

var ssn = '${input.ssn}';
var sex = '${input.sex}';

jQuery(document).ready(function() {
	
	jQuery('#btn_reg').bind("click", function(){
		lf_requestSubmit();
	});
	
	jQuery('#btn_reqCancel').bind("click", function(){				
		if( jQuery('#next_mode').val() == 'add')
		{
			if(!confirm('작성중인 정보가 초기화 됩니다. 정말 취소하시겠습니까?')) return;
			js_nextEndStep();
		}else{
			if(!confirm('작성중인 정보가 초기화 됩니다. 정말 취소하시겠습니까?')) return;
			location.href='<%=OFFLINE_SEN_REDIRECT%>';
		}
	});

});


function js_nextStep()
{
	if( jQuery("input:checkbox[id='chk_alarm']").is(":checked") != true )
	{
		msgOpen("다음 단계로 넘어가기 위해서는 아래 주의사항을 꼭 정독하고 확인을 눌러주시기 바랍니다.");
		return;
	}
	
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep7';
	jQuery('#frm').attr('action', _url);
	jQuery('#frm').submit();
}

function js_nextEndStep()
{
	var _url ='<%=OFFLINE_SEN_END%>';
	jQuery('#frm').attr('action', _url);
	jQuery('#frm').submit();
}

function lf_requestSubmit()
{

    if(!checkFormField('#frm'))
	{
		return false;
	}

    
	jQuery('#deposit_dt').val( jQuery('#deposit_yyyy').val() + '-' + jQuery('#deposit_mm').val() + '-' + jQuery('#deposit_dd').val() );
	
	if( jQuery('#deposit_yyyy').val() == '' ||  jQuery('#deposit_mm').val() == '' ||  jQuery('#deposit_dd').val() == '')
		jQuery('#deposit_dt').val( '' );
	
	js_nextStep();
}

</script>

</head>

<body>

<form name="frm" id="frm" method="post" action="">
<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type='hidden' name='cp_code' id='cp_code' value='${input.cp_code}'>
<input type='hidden' name='ji_code' id='ji_code' value='${input.ji_code}'>
<input type='hidden' name='dcm_no' id='dcm_no' value='${input.dcm_no}'>
<input type='hidden' name='next_step' id='next_step' value='7'>	
<input type='hidden' name='next_mode' id='next_mode' value='${input.next_mode}'>

<input type='hidden' name='deposit_dt' id='deposit_dt' value=''>

	<div id="content">
		<div id="smartwizard" class="sw-main sw-theme-arrows" style="margin-bottom: 15px;">
		<ul class="nav nav-tabs step-anchor">
            <li class="nav-item"><a href="#step-1"><strong>1 단계</strong><br><strong>개인정보 제공 동의</strong></a></li>
            <li class="nav-item"><a href="#step-2"><strong>2 단계</strong><br><strong>신청자 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-3"><strong>3 단계</strong><br><strong>신청업체 입력</strong></a></li>
            <li class="nav-item"><a href="#step-4"><strong>4 단계</strong><br><strong>영업장 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-5"><strong>5 단계</strong><br><strong>책입자 정보 입력</strong></a></li>
            <li class="nav-item active"><a href="#step-6"><strong>6 단계</strong><br><strong>입금정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-7"><strong>7 단계</strong><br><strong>접수완료</strong></a></li>
            <li class="nav-item done"><a href="#step-8"><strong>8 단계</strong><br><strong>결제 하기</strong></a></li>
        </ul>
		</div>
			
			<div class="directions">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<tbody>
						<tr>
							<th scope="row">주의사항</th>
							<td>
							<p style="margin-left:0px;color:blue;font-weight:bold;font-size:14px;">		
							-교육 신청정보 등록 및 교육비 입금을 완료하셔야 최종 접수완료로 처리되며 교육에 참석하실 수  있습니다.<br>
							-교육당일로부터 7일 이전까지는 입금 완료해 주시기 바랍니다.<br>
							-당일~3일 이내에 입금하실 경우 접수 마감되거나 입금확인이 어려울 수 있습니다. <br>
							&nbsp;&nbsp;(단, 각 지회마다 접수마감 일자가 다를 수 있으며, 조기 마감 될 수도 있습니다.)
							
							<br>
							</p>	
							<p style="margin-left:0px;color:red;font-weight:bold;">							
							※ [입금자명] 작성시, ‘위생교육비’ or ‘한국건물위생관리협회’ or ‘교육비’ 등 작성 절대금지 (입금확인불가)<br>
							- 상호 또는 입금자명으로 정확히 작성하여 주시기 바랍니다.							
							</p>
							<div class="pull-right">
								<input type="checkbox" class="check" name="chk_alarm" id="chk_alarm"/> 
								<span style="font-size:16px;color:red;font-weight:bold;">주의사항 확인</span>
							</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<table><tr><td style="text-align:left;">
				<strong style="color:blue;font-size:14px;">• 입력해주신 입금정보로 교육비가 확인되오니 정확히 입력해주시기 바랍니다.</strong><br>
				<strong style="color:blue;font-size:14px;">• 보내시는 분의 통장 정보를 입력하세요. (협회 계좌정보 아님)</strong>
			</td></tr>
			</table>
		
			<div class="view">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width="200"/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" >입금일자 <i class="fa fa-asterisk tred"> </th>
							<td>
								<select name="deposit_yyyy" id="deposit_yyyy" alt="년" style="width:100px;" required="true">
									<option value="">년도선택</option>
								<c:forEach begin="${currentYear}" end="${nextYear}" step="1" var="YY">
					   			   	<option value="${YY}" >${YY}년</option>
								</c:forEach>
								</select>년
							
								<select name="deposit_mm" id="deposit_mm" alt="월" style="width:100px;" required="true">
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
							
								<select name="deposit_dd" id="deposit_dd" alt="일" style="width:100px;" required="true">
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
						<tr>
							<th scope="row" >(보내시는분)입금자명  <i class="fa fa-asterisk tred"></th>
							<td >
								<input type="text"  class="it preload"  title="입금자명" alt="입금자명" value="" maxlength="50"  name="deposit_nm" id="deposit_nm" required="true"/>
							</td>
						</tr>	
						<tr>
							<th scope="row" >(보내시는분)입금자 은행  <i class="fa fa-asterisk tred"></th>
							<td >
								<input type="text"  class="it preload"  title="입금자은행" alt="입금자은행" value="" maxlength="50"  name="deposit_bank" id="deposit_bank" required="true"/>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="btnWrap">
				<a class="pbtn01" id='btn_reqCancel'><span class="">취소</span></a>
				<a href="javascript:history.go(-1);" class="pbtn02"><span class=""><i class="fa fa-mail-reply-all" style="font-size:18px"></i> 뒤로</span></a>
				<a class="pbtn02" id='btn_reg'><span class="">다음 <i class="fa fa-sign-in" style="font-size:18px"></i></span></a>
			</div>
		</div>
</form>

</body>
</html>