<%
/******************************************************************************** 
 * Program ID	:  무통장입금 등록
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
	
	<title></title>
	
<script type="text/javascript" class="source">

function lf_requestSubmit()
{
	if( !confirm( '저장하시겠습니까?') ) return;

    if(!checkFormField('#frm_bill'))
	{
		return false;
	}
	var _url = '/edu/payment/action/paymentAction.jspx?cmd=createBanking';
	
	<c:if test="${input.mode == 'U'}">
		_url = '/edu/payment/action/paymentAction.jspx?cmd=modifyBanking';
	</c:if>
    
	var http = jQuery.ajax( {
		url: _url,
   		type: "POST",
		data : jQuery('#frm_bill').serialize(true),
   		async:false,
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_010);
		},
   		success:function(xmlDoc)
   		{
	   		var code = jQuery(xmlDoc).find('code').text();
	   		var data = jQuery(xmlDoc).find('data').text();


			if( code == "200")
			{
				alert( msg_com_code_009);
				window.close();
			} else
			{
				alert(msg_com_code_007);
			}
		}
  	});
}


</script>
</head>

<body>
<form name="frm_bill" id="frm_bill" method="post" onsubmit="return false;">
<input type="hidden" name="user_seq_no" id="user_seq_no" value="${input.user_seq_no }">
<input type="hidden" name="std_yyyy" id="std_yyyy" value="${input.std_yyyy }">
<input type="hidden" name="lecture_seq_no" id="lecture_seq_no" value="${input.lecture_seq_no }">

<div class="popupLayer" id="payInputLayer">
	<a href="#" onclick="self.close();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
	<div class="popupCont_top">
		<div class="stit">
			무통장입금 확인 등록
		</div>
	</div>
	<div class="popupCont_bottom">
		<table cellpadding="0" cellspacing="0" class="payInput" summary="" >
			<caption></caption>
			<colgroup>
				<col width="200"/><col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><img src="/static/main/img/common/pop_icon.gif"/>무통장입금 시  입금자명 </th>
					<td><input name='account_nm' id='account_nm' class="it " alt='무통장입금 시 입금자명' type="text" value='${output.account_nm }' required='true'/></td>
				</tr>
				<tr>
					<th scope="row"><img src="/static/main/img/common/pop_icon.gif"/>입금은행</th>
					<td><input name='bank_nm' id='bank_nm' class="it " alt='입금은행' type="text" value='${output.bank_nm }' required='true'/></td>
				</tr>
				<tr>
					<th scope="row"><img src="/static/main/img/common/pop_icon.gif"/>입금(예정)일자</th>
					<td><input name='deposit_dt' id='deposit_dt' class="it " alt='입금(예정)일자' type="text" value='${output.deposit_dt }' required='true'/></td>
				</tr>
			</tbody>
		</table>
		<div class="directions">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
				<tbody>
					<tr>
						<th scope="row">교육비 <br>납부 계좌</th>
						<td>
							기업은행 027-057754-04-024  &nbsp; &nbsp;<br /> 
						(사)한국건물위생관리협회<br />
						교육비 1인 38,000원
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="btnWrap">
			<a href="javascript:lf_requestSubmit();" class="pbtn02"><span class="">저장</span></a>
			<a href="javascript:window.close();" class="pbtn01" ><span class="">닫기</span></a>
		</div>
	</div>
</div>


</body>
</html>