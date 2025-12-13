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
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep6';
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

    
	jQuery('#email').val( jQuery('#email1').val() + '@' + jQuery('#email2').val() );
	
	if( jQuery('#email1').val() == '' ||  jQuery('#email2').val() == '')
		jQuery('#email').val( '' );
	
	jQuery('#phone_no').val( jQuery('#phone_no1').val() + '-' + jQuery('#phone_no2').val() + '-' + jQuery('#phone_no3').val() );
	
	if( jQuery('#phone_no1').val() == '' ||  jQuery('#phone_no2').val() == '')
		jQuery('#phone_no').val( '' );
	
	jQuery('#birth').val( jQuery('#birth_yyyy').val() + '.' + jQuery('#birth_mm').val() + '.' + jQuery('#birth_dd').val() );
	
	if( jQuery('#birth_yyyy').val() == '' ||  jQuery('#birth_mm').val() == '' ||  jQuery('#birth_dd').val() == '')
		jQuery('#birth').val( '' );
	
	jQuery('#chrg_ymd').val( jQuery('#chrg_yyyy').val() + '-' + jQuery('#chrg_mm').val() + '-' + jQuery('#chrg_dd').val() );
	
	if( jQuery('#chrg_yyyy').val() == '' ||  jQuery('#chrg_mm').val() == '' ||  jQuery('#chrg_dd').val() == '')
		jQuery('#chrg_ymd').val( '' );
	
	js_nextStep();
}


function js_selectEmail()
{
	jQuery('#email2').val( jQuery('#email3').val());	
}
</script>

</head>

<body>

<form name="frm" id="frm" method="post" action="">
<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type='hidden' name='cp_code' id='cp_code' value='${input.cp_code}'>
<input type='hidden' name='ji_code' id='ji_code' value='${input.ji_code}'>
<input type='hidden' name='dcm_no' id='dcm_no' value='${input.dcm_no}'>
<input type='hidden' name='next_step' id='next_step' value='6'>	
<input type='hidden' name='next_mode' id='next_mode' value='${input.next_mode}'>

<input type="hidden" name='phone_no' id='phone_no' value=''/>
<input type="hidden" name='email' id='email' value=''/>
<input type="hidden" name='birth' id='birth' value=''/>
<input type="hidden" name='chrg_ymd' id='chrg_ymd' value=''/>

	<div id="content">
		<div id="smartwizard" class="sw-main sw-theme-arrows" style="margin-bottom: 15px;">
		<ul class="nav nav-tabs step-anchor">
            <li class="nav-item"><a href="#step-1"><strong>1 단계</strong><br><strong>개인정보 제공 동의</strong></a></li>
            <li class="nav-item"><a href="#step-2"><strong>2 단계</strong><br><strong>신청자 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-3"><strong>3 단계</strong><br><strong>신청업체 입력</strong></a></li>
            <li class="nav-item"><a href="#step-4"><strong>4 단계</strong><br><strong>영업장 정보 입력</strong></a></li>
            <li class="nav-item active"><a href="#step-5"><strong>5 단계</strong><br><strong>책입자 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-6"><strong>6 단계</strong><br><strong>입금정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-7"><strong>7 단계</strong><br><strong>접수완료</strong></a></li>
            <li class="nav-item done"><a href="#step-8"><strong>8 단계</strong><br><strong>결제 하기</strong></a></li>
        </ul>
		</div>
			
			<div class="" style="margin-top:10px;margin-bottom: -50px;margin-top: 20px;">
				<label for="" style="font-weight:bold;font-size:14px;color:#00b9ed ;">
					 공중위생관리책임자 정보를 입력하세요. (책임자=교육자)
				</label> 
				<br>
				<label for="" style="font-weight:bold;font-size:13px;">
					<i class="fa fa-asterisk tred"> 필수입력사항입니다.</i>
				</label> 
			</div>
			<div class="view">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width="200"/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" >성명(교육자)  <i class="fa fa-asterisk tred"></th>
							<td colspan="3">
								<input type="text"  class="it preload"  title="성명" value="" maxlength="20"  name="user_nm" id="user_nm" required="true"/>
							</td>
						</tr>	
						<tr>
							<th scope="row" >성별  <i class="fa fa-asterisk tred"></th>
							<td colspan="3">
								<select id="sex" name="sex" style="width:100px;" required="true">
									<option value="1">남</option>
									<option value="2">여</option>
								</select>
							</td>
						</tr>	
						<tr>
							<th scope="row" >생년월일  <i class="fa fa-asterisk tred"></th>
							<td>
								<select name="birth_yyyy" id="birth_yyyy" required='true' alt="년" style="width:100px;">
									<option value="">년도선택</option>
									<c:forEach begin="1935" end="${currentYear - 9}" step="1" var="YY">
									<c:set var="decr" value="${currentYear-9 - YY + 1935}"/>
						   			   	<option value="${decr}" >${decr}년</option>
									</c:forEach>
								</select>년
							
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
							
						</tr>		
						<tr>
							<th scope="row" >직위  </th>
							<td colspan="3">
								<input type="text"  class="it preload"  title="직위" value="" maxlength="20"  name="pos_nm" id="pos_nm" />
							</td>
						</tr>	
						<tr>
							<th scope="row" class="on">휴대전화번호  <i class="fa fa-asterisk tred"> </th>
							<td>
								<select style="width:80px;" name='phone_no1' id='phone_no1' required="true">
									<option value="010">010</option>
									<option value="011">011</option>
									<option value="016">016</option>
									<option value="017">017</option>
									<option value="019">019</option>
								</select>
								-
								<input type="text" class="it sm" maxlength="4" title="핸드폰" value="" name='phone_no2' id='phone_no2' required='true' />
								-
								<input type="text" class="it sm" maxlength="4" title="핸드폰" value="" name='phone_no3' id='phone_no3' required='true' />
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">이메일  </th>
							<td>
								<input type="text" class="it sm2" title="이메일아이디" value="" name='email1' id='email1' /> @
								<input type="text" class="it sm2" title="이메일주소" value="" name='email2' id='email2' />
								<select style="width:150px;" name='email3' id='email3' onchange="js_selectEmail();">
									<option value="">선택하세요</option>
									<option value="naver.com">naver.com</option>
									<option value="hanmail.net">hanmail.net</option>
									<option value="gmail.com">gmail.com</option>
									<option value="nate.com">nate.com</option>
									<option value="yahoo.com">yahoo.com</option>
									<option value="hotmail.com">hotmail.com</option>
									<option value="korea.com">korea.com</option>
									<option value="chol.com">chol.com</option>
									<option value="netian.com">netian.com</option>
									<option value="dreamwiz.com">dreamwiz.com</option>
								</select>
							</td>
						</tr>
						<tr>
							<th scope="row" >책임자 지정일 </th>
							<td>
								<select name="chrg_yyyy" id="chrg_yyyy" alt="년" style="width:100px;">
									<option value="">년도선택</option>
								<c:forEach begin="1980" end="${currentYear}" step="1" var="YY">
					   			   	<option value="${YY}" >${YY}년</option>
								</c:forEach>
								</select>년
							
								<select name="chrg_mm" id="chrg_mm" alt="월" style="width:100px;">
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
							
								<select name="chrg_dd" id="chrg_dd" alt="일" style="width:100px;">
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