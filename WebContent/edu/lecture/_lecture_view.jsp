<%
/******************************************************************************** 
 * Program ID	:  강좌목록
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
<script type="text/javascript" class="source">


function js_getClassList()
{
	jQuery.ajax({
		url : '/edu/lecture/action/classAction.jspx?cmd=getClassCodeList', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.class_info;
				var jsonCnt = jsonObj.result.data.class_info.length;
				var curday = getToday();
				
				if(jsonCnt > 0){
					for(var i=0; i<jsonCnt; i++){
													
						var class_no  = urlDecode(json[i].class_no); 
						var title   = urlDecode(json[i].title); 
						
						
						var titleHTML = "<div style='WIDTH:400px;' class='ellipsis'>" + title +"</div>";

						listHtml.push('	<tr>');
						listHtml.push('		  <td>'+( i+1 ) + '교시'+'</td>');
						listHtml.push('		  <td>'+titleHTML+'</td>');
						listHtml.push('	</tr>');
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="2" style="text-align:center;">조회된 결과가 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				
			}
		}
	});
	
}


function lf_doPayment()
{
	if( jQuery('form#frmPay #lecture_seq_no').val() == '')
	{		
		msgStart(msg_pay_code_001);
		return;
	}
	if( jQuery('form#frmPay #user_seq_no').val() == '')
	{		
		msgStart(msg_pay_code_002);
		return;
	}
	if( jQuery('form#frmPay #amount').val() == '')
	{		
		msgStart(msg_pay_code_003);
		return;
	}
	
	window.open('about:blank','PAYMENT' , 'top=100,left=100,width=735px, height=745px, scrollbars=no, resize=no,toolbar=no,status=no');
	jQuery('form#frmPay').submit();
}

function lf_openBanking()
{
	var lecture_seq_no = jQuery('#lecture_seq_no').val();
	var std_yyyy = jQuery('#std_yyyy').val();
	var _url = '/edu/payment/paymentCtrl.jspx?cmd=writeBill&lecture_seq_no='+lecture_seq_no+'&std_yyyy='+ std_yyyy;
	window.open(_url,'BANKING' , 'top=100,left=100,width=535px, height=435, scrollbars=no, resize=no,toolbar=no,status=no');
}

jQuery(document).ready(function(){
	
	
	js_getClassList();
	
	var lecture_div = jQuery('#lecture_div').val();
	var time_over = jQuery('#time_over').val();
	var end_dd = jQuery('#end_dd').val();
	if(lecture_div == '2' && time_over == 'Y' )
	{
		var msg = '';		
		msg += '<h4 class="tred">온라인 책임자교육 수강신청이 종료되었습니다.</h4>';
		msg += '* 책임자교육은 ' +end_dd+ ' 자정까지 수강완료 해야 하므로 지금 신청하여도 기간내에 수강이 불가능합니다. (교육시간 3시간)<br>';
		msg += '* 만약 교육수강 듣기만을 원하시면 계속 진행하시되 수료증 발급은 불가함을 알려드립니다.';
		
		jQuery('.pop_cont').css('width', '600px');
		msgOpen(msg);
	}
	
});
</script>
</head>

<body>

<form id="frmPay" id="frmPay" method="post" action="/edu/payment/paymentCtrl.jspx?cmd=paymentDelivery" target="PAYMENT">
<input type="hidden" id="lecture_seq_no" name="lecture_seq_no" value="${input.lecture_seq_no}"/>
<input type="hidden" id="lecture_div" name="lecture_div" value="${detail.lecture_div}"/>
<input type="hidden" id="std_yyyy" name="std_yyyy" value="${input.std_yyyy}"/>
<input type='hidden' name='user_seq_no' id='user_seq_no' value='${input.s_user_seq_no}'>
<input type='hidden' name='amount' id='amount' value='${detail.amount}'>
<input type='hidden' name='time_over' id='time_over' value='${detail.time_over}'>
<input type='hidden' name='end_dd' id='end_dd' value='${detail.end_dd}'>
<input type='hidden' name='user_id' id='user_id' value='${input.s_emp_no}'>
<input type='hidden' name='user_nm' id='user_nm' value='${input.s_emp_name}'>
<input type='hidden' name='user_ip' id='user_nm' value='${input.s_user_ip}'>
<input type='hidden' name='email' id='email' value='${input.s_email}'>
<input type='hidden' name='tel_no' id='tel_no' value='${input.s_tel_no}'>
<input type='hidden' name='product_nm' id='product_nm' value='${detail.title}'>
</form>

<!-- search -->
<form id="frm" name="frm" method="post" action="" onsubmit="return false;">
<input type="hidden" id="lecture_seq_no" name="lecture_seq_no" value="${input.lecture_seq_no}"/>
<input type="hidden" id="lecture_div" name="lecture_div" value="${detail.lecture_div}"/>
<!-- search Data -->
<div id="content">
<!-- start :: content -->
<!-- start :: content -->

	<div class="edu_info">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="100px"/><col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">제목</th>
					<td>${detail.title}</td>
				</tr>
				<tr>
					<th scope="row">평가금액</th>
					<td>${ el:formatIntByString(detail.amount) }원</td>
				</tr>
				<tr>
					<th scope="row">강의설명</th>
					<td>${detail.summary}</td>
				</tr>
				<tr>
					<th scope="row">강의구성</th>
					<td>
						<table cellpadding="0" cellspacing="0" class="inner" style="margin: 5px;" summary="" >
							<caption></caption>
							<colgroup>
								<col width="80"/><col width=""/>
							</colgroup>
							<thead>
								<tr>
									<th scope="row">구분</th>
									<th scope="row">제목</th>
								</tr>
							</thead>
							<tbody id="list_div">
								
							</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</div>


	<div class="LbtnWrap center">
		<a href="javascript:history.go(-1);"><span class="">취소</span></a>
		<a href="javascript:lf_doPayment();"><span class="">실시간계좌이체,가상계좌이체 및 신용카드 결제</span></a>
		<!-- <a href="javascript:lf_openBanking();"><span class="">무통장입금(예정)확인</span></a> -->
	</div>

	<div class="directions">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<tbody>
				<tr>
					<th scope="row">주의사항</th>
					<td>
						신용카드와 계좌이체는 결제 완료 즉시 교육을 수강하실 수 있습니다.<br>
						가상계좌 신청 시에는 발급 받으신 계좌로 정해진 금액을 송금하시면 자동으로 입금 확인 됩니다.<br>
						가상계좌 입금 후 10분 이내 입금 확인이 되지 않으시면 담당자에게 문의 바랍니다. 
					</td>
				</tr>
			</tbody>
		</table>
	</div>

<!-- end :: content -->
<!-- end :: content -->
</div>

</body>
</html>