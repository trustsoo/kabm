<%
/******************************************************************************** 
 * Program ID	:  결제목록
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
<%@ include file="/common/common.jsp"%>
<%@ include file="/edu/payment/paycommon.jsp"%>

<html>
<head>
<script language="JavaScript" src="https://pgweb.tosspayments.com/WEB_SERVER/js/receipt_link.js"></script>
<script type="text/javascript" class="source">
	const LGD_MID = '<%=LGD_MID%>';

function gotoPage(page_no)
{	
	document.frm.page_no.value = page_no;
	js_getPaymentList();
}


function js_getPaymentList()
{
	jQuery.ajax({
		url : '/edu/payment/action/paymentAction.jspx?cmd=getPaymentList', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.payment_info;
				var jsonCnt = jsonObj.result.data.payment_info.length;
				jQuery('#tot_cnt').val(jsonObj.result.data.property[0].tot_cnt);
				var curday = getToday();
				
				if(jsonCnt > 0){
					for(var i=0; i<jsonCnt; i++){
													
						var user_lecture_seq_no  = urlDecode(json[i].user_lecture_seq_no); 
						var lecture_seq_no  = urlDecode(json[i].lecture_seq_no); 
						var title   = urlDecode(json[i].title); 
						var lgd_accountnum = urlDecode(json[i].lgd_accountnum);
						var lgd_financename = urlDecode(json[i].lgd_financename);
												
						var lgd_amount = urlDecode(json[i].lgd_amount);
						var issue_dt = urlDecode(json[i].issue_dt);
						var is_issue = urlDecode(json[i].is_issue);
						var lecture_status = urlDecode(json[i].lecture_status);
						var is_cancel = urlDecode(json[i].is_cancel);
						var lgd_paydate = urlDecode(json[i].last_modify_dt);
						
						var status_ext = "입금대기";
						if( "SC0010" == lgd_paytype) status_ext = "승인대기";
						else if( "SC0030" == lgd_paytype) status_ext = "입금대기";
						else if( "SC0040" == lgd_paytype) status_ext = "입금대기";
						else if( "SC0050" == lgd_paytype) status_ext = "입금대기";
						
						var status = urlDecode(json[i].status);
						var status_nm = '';
							if( status == '00' ) status_nm = '결제초기화완료';
							else if( status == '01' ) status_nm = '최종결제요청';
							else if( status == '02' ) status_nm = status_ext;
							else if( status == '03' ) status_nm = '결제완료(완료)';
							else if( status == '96' ) status_nm = '결제정보입력실패';
							else if( status == '95' ) status_nm = '결제자동취소';
							else if( status == '94' ) status_nm = '최종결제실패';
							else if( status == '93' ) status_nm = '최종결제요청실패';
							else if( status == '92' ) status_nm = '결제환경구성실패';
							else if( status == '91' ) status_nm = '결제초기화실패';
							else if( status == '97' ) status_nm = '결제취소실패';
							else if( status == '99' ) status_nm = '결제취소';
						
						var lgd_paytype = urlDecode(json[i].lgd_paytype); 
						var lgd_paytype_nm = "신용카드";
						if( "SC0010" == lgd_paytype) lgd_paytype_nm = "신용카드";
						else if( "SC0030" == lgd_paytype) lgd_paytype_nm = "계좌이체";
						else if( "SC0040" == lgd_paytype){
							lgd_paytype_nm = "가상계좌";
							if(status == '00' || status == '01' ||status == '02' )
								lgd_paytype_nm = "가상계좌<br>("+lgd_financename + " - " +lgd_accountnum+")";
								
							if(is_issue != 'Y') lgd_paytype_nm = "가상계좌";	
						}
						else if( "SC0050" == lgd_paytype) lgd_paytype_nm = "무통장입금";
							
						var lgd_oid = urlDecode(json[i].lgd_oid);
						var lgd_tid = urlDecode(json[i].lgd_tid);
						var lgd_authkey = urlDecode(json[i].lgd_authkey);
						var titleHTML = "<div style='WIDTH:100%;'>["+issue_dt+"]" + title +"</div>";
						var cancelHTML = "";
						if( '03' == status && 'Y' == is_cancel && ('SC0010' == lgd_paytype || 'SC0030' == lgd_paytype) ) 
							cancelHTML = "<a href='javascript:fn_paymentCancel(\"" + user_lecture_seq_no + "\",\"" + lgd_oid + "\",\"" + lgd_tid + "\")' class=\"btn btn-sm btn-danger\"><span>취소</span></a>";
						if( '00' != lecture_status && ('SC0010' == lgd_paytype || 'SC0030' == lgd_paytype) ) cancelHTML = "취소불가";
						
						var printHTML = "<a href='javascript:fn_billDown(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\")' class='btn btn-sm btn-success'><span>영수증</span></a>";
						if( status != '03' ) printHTML = '';
						
						var certHTML = "";
						if( '03' == status && 'SC0010' == lgd_paytype  ) 
							certHTML = "<a href='javascript:fn_billView(\"" + lgd_tid + "\",\"" + lgd_authkey + "\")' class='btn btn-sm btn-primary'><span>전표</span></a>";
						
						var deliveryHTML = "<a href='javascript:fn_DeliveryView(\"" + lgd_oid + "\")' class='btn btn-sm btn-info'><span>교재</span></a>";
						if( status != '03' ) deliveryHTML = '';
						
						listHtml.push('	<div class="panel">');
						listHtml.push('		<a style="background: #cde2f1;" class="panel-heading collapsed" role="tab" id="heading-'+i+'" data-toggle="collapse" data-parent="#accordion" href="#collapse-'+i+'" aria-expanded="false" aria-controls="collapse-'+i+'">');
						listHtml.push('		<span class="flex" style="float:right;"><i class="fa fa-chevron-down"></i></span>');
						listHtml.push('		<h4 class="panel-title flex" style="font-size:14px;">'+titleHTML+'</h4>');
						listHtml.push('		</a>');
						listHtml.push('		<div id="collapse-'+i+'" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading-'+i+'" aria-expanded="false" style="height: 0px;">');
						listHtml.push('			<div class="panel-body">');
						listHtml.push('				<table class="table table-bordered">');
						listHtml.push('					<thead>');
						listHtml.push('						<tr>');
						listHtml.push('							<th>구분</th>');
						listHtml.push('							<th>내용</th>');
						listHtml.push('						</tr>');
						listHtml.push('					</thead>');
						listHtml.push('					<tbody>');
						
						listHtml.push('	<tr>');
						listHtml.push('		  <th>결제방식</th><td>'+lgd_paytype_nm+'</td>');
						listHtml.push('	</tr><tr>');
						listHtml.push('	  	  <th>결제금액</th><td>'+getNumberFormat(lgd_amount)+'</td>');
						listHtml.push('	</tr><tr>');
						listHtml.push('	  	  <th>요청일시</th><td>'+issue_dt+'</td>');
						listHtml.push('	</tr><tr>');
						listHtml.push('	  	  <th>결제일시</th><td>'+lgd_paydate+'</td>');
						listHtml.push('	</tr><tr>');
						listHtml.push('	  	  <th>최종상태</th><td>'+status_nm+'</td>');
						listHtml.push('	</tr><tr>');
						listHtml.push('	  	  <th>선택</th><td>'+cancelHTML+' '+printHTML+' '+certHTML+' '+deliveryHTML+'</td>');
						listHtml.push('	</tr>');
						
						listHtml.push('					</tbody>');
						listHtml.push('				</table>');
						listHtml.push('			</div>');
						listHtml.push('		</div>');
						listHtml.push('	</div>');
					
					
						
					}
				}else{
					listHtml.push('				<table class="table table-bordered">');
					listHtml.push('					<thead>');
					listHtml.push('						<tr>');
					listHtml.push('							<th>구분</th>');
					listHtml.push('							<th>내용</th>');
					listHtml.push('						</tr>');
					listHtml.push('					</thead>');
					listHtml.push('					<tbody>');
					listHtml.push('		<tr>');
					listHtml.push('		  <td style="text-align:center;">조회된 결과가 없습니다.</td>');
					listHtml.push('		</tr>');
					listHtml.push('					</tbody>');
					listHtml.push('				</table>');
					
				}
				jQuery('#accordion-list').html(listHtml.join(''));
				
			}
		}
	});
	
}

function fn_DeliveryView(LGD_OID)
{
	jQuery.ajax({
		url : '/edu/payment/action/paymentAction.jspx?cmd=getPaymentPrchsInfo', 
		data : 'LGD_OID='+LGD_OID,
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.payment_info;
				var jsonCnt = jsonObj.result.data.payment_info.length;
				
				
				if(jsonCnt > 0){
					var i = 0;
					var post_zipcode = urlDecode(json[i].post_zipcode);
					var post_addr = urlDecode(json[i].post_addr);		
					var post_req = urlDecode(json[i].post_req);		
					var post_tel_no = urlDecode(json[i].post_tel_no);		
					var delv_no = urlDecode(json[i].delv_no);		
					var delv_corp = urlDecode(json[i].delv_corp);		
					
						
					if( post_zipcode != '' ||  post_addr != '' )
					{	
						var postHTML = '(' + post_zipcode +') ' +  post_addr;
						jQuery('#post_addr').html( postHTML);
						jQuery('#post_tel_no').html(post_tel_no);
						jQuery('#post_req').html(post_req);
						jQuery('#delv_no_str').html(delv_no);
						jQuery('#delv_corp_str').html(delv_corp);
						
						jQuery('form#frmDelivery #delv_no').val(delv_no);
						jQuery('form#frmDelivery #delv_corp').val(delv_corp);
					}					
					
					var tx = 0;//( jQuery(window).width() - 600 ) / 2+jQuery(window).scrollLeft();
					var ty = jQuery(window).scrollTop() + 100;
					
					$("#viewForm").css({left:tx+"px",top:ty+"px"});
					jQuery('form#frmDelivery #LGD_OID').val(LGD_OID);					
					$('#viewForm').show(); 
					
				}else{
					msgOpen( msg_com_code_000);
				}
			}else{
				msgOpen( msg_com_code_000);
			}
		}
	});
	
}


function fn_paymentCancel(user_lecture_seq_no,LGD_OID , LGD_TID)
{
	jQuery('form#frmPay #user_lecture_seq_no').val(user_lecture_seq_no);
	jQuery('form#frmPay #LGD_OID').val(LGD_OID);
	jQuery('form#frmPay #LGD_TID').val(LGD_TID);
	if( jQuery('form#frmPay #LGD_OID').val() == '')
	{		
		msgStart(msg_pay_code_004);
		return;
	}
	if( jQuery('form#frmPay #LGD_TID').val() == '')
	{		
		msgStart(msg_pay_code_005);
		return;
	}
	
	if( confirm(msg_pay_code_006))
	{
		window.open('about:blank','PAYMENT' , 'top=100,left=100,width=500px, height=400px, scrollbars=no, resize=no,toolbar=no,status=no');
		jQuery('form#frmPay').submit();
	}
	
	
}

function fn_billView(lgd_tid, authdata)
{
	//var url = 'https://pgweb.tosspayments.com/MpFlowCtrl?eventDiv1=search&eventDiv2=getReceipt&MID=kabm01&ORDERID='+orderid;
	//window.open(url, "billView");

	showReceiptByTID(LGD_MID, lgd_tid, authdata);

}

function fn_billDown(lecture_seq_no,user_lecture_seq_no)
{
	var url = '/edu/payment/action/paymentAction.jspx?cmd=downBill&user_lecture_seq_no='+user_lecture_seq_no;
	window.open(url, "billPoP");

}

function fn_closeConfirm()
{
	$('#viewForm').hide(); 
}

function lf_doDeliveryModify()
{
	if( jQuery('form#frmDelivery #user_id').val() == '')
	{		
		msgStart(msg_com_code_016);
		return;
	}
	
	if( jQuery('form#frmDelivery #LGD_OID').val() == '')
	{		
		msgStart(msg_com_code_016);
		return;
	}
	
	var delv_no = jQuery('form#frmDelivery #delv_no').val();
	var delv_corp = jQuery('form#frmDelivery #delv_corp').val();
	
	if( delv_no != '' )
	{
		msgOpen(msg_pay_code_007);
	}else{
		fn_closeConfirm();
		window.open('about:blank','DELIVERY' , 'top=100,left=100,width=735px, height=745px, scrollbars=no, resize=no,toolbar=no,status=no');
		jQuery('form#frmDelivery').submit();
	}
}


jQuery(document).ready(function(){
	
	jQuery('#btn-pass').on("click", function(){
		fn_closeConfirm();
	});	
	
	jQuery('#btn-delivery-req').on("click", function(){
		lf_doDeliveryModify();
	});	
	
	jQuery('#btn-lecture').on("click", function(){
		location.href='/mobile/edu/class/classCtrl.jspx?cmd=list';
	});	
	
	gotoPage('${input.page_no}'); 
	
});

</script>
</head>

<body>

<form id="frmPay" id="frmPay" method="post" action="/edu/payment/paymentCtrl.jspx?cmd=paymentCancel" target="PAYMENT">
<input type='hidden' name='user_seq_no' id='user_seq_no' value='${input.s_user_seq_no}'>
<input type='hidden' name='user_id' id='user_id' value='${input.s_emp_no}'>
<input type="hidden" name="user_lecture_seq_no"  id="user_lecture_seq_no"      value="">
<input type="hidden" name="LGD_OID"  id="LGD_OID"                   value="">
<input type="hidden" name="LGD_TID"  id="LGD_TID"                   value="">
</form>
<form id="frmDelivery" id="frmDelivery" method="post" action="/mobile/edu/payment/paymentCtrl.jspx?cmd=paymentDelivery" target="DELIVERY">
<input type='hidden' name='user_seq_no' id='user_seq_no' value='${input.s_user_seq_no}'>
<input type='hidden' name='user_id' id='user_id' value='${input.s_emp_no}'>
<input type="hidden" name="LGD_OID"  id="LGD_OID" value="">
<input type="hidden" name="delv_corp"  id="delv_corp" value="">
<input type="hidden" name="delv_no"  id="delv_no" value="">
<input type="hidden" name="mod_yn"  id="mod_yn" value="Y">
</form>
<!-- search -->
<form id="frm" name="frm" method="post" action="">
<input type="hidden" id="rnum" name="rnum" value="50"/>
<input type='hidden' name='page_no' id='page_no' value='1'>
<input type='hidden' name='user_seq_no' id='user_seq_no' value='${input.s_user_seq_no}'>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>

<div id="content">
		<!-- start :: content -->
		<div class="alert alert-info">
			<i class="icon-ok blue"></i>			
			<strong class="">1. 해당과목의 영수증 하단 「발급」을 클릭하세요.</strong><br>
			<i class="icon-ok blue"></i>			
			<strong class="">2. 「파일 열기」 또는 「저장」 후 영수증을 출력하세요.</strong>											
		</div>	
			
		<button id="btn-lecture" class="btn btn-lg btn-info btn-block" type="button"> 온라인수강 바로가기 <i class="glyphicon glyphicon-facetime-video" ></i></button>
		<br>
		<div class="accordion" id="accordion-list" role="tablist" aria-multiselectable="true">
		</div>

</div>

</form>


<div id="viewForm" style="border: 1px solid #1e90f1;z-index: 999;width:100%;display: none;position:absolute;left:0px;top:12px;background-color: #FFF;">
	<div style="z-index:999;font-size: 1.0em;font-weight:bold;color:#ffffff;background-color:#3b85c3;height: 2.25em;padding-top: 10px;padding-left: 10px;" id="confirm_title">
	교재배송지
	</div>
	
	<table class="table table-striped jambo_table bulk_action">
		<colgroup>
			<col width="80px"/><col width=""/>
		</colgroup>
		<thead>
			<tr class="headings">
				<th class="column-title">구분</th>
				<th class="column-title no-link last">내용</th>
			</tr>
		</thead>
		<tbody>
			<tr class="even pointer">
				<td class="a-center ">성 명</td>
				<td class=" last" id="user_nm">${output.user_nm }</td>
			</tr>
			<tr class="even pointer">
				<td class="a-center ">연락처</td>
				<td class=" last" id="tel_no">${output.tel_no }</td>
			</tr>
			<tr class="even pointer">
				<td class="a-center ">연락처2</td>
				<td class=" last" id="post_tel_no">${output.post_tel_no }</td>
			</tr>
			<tr class="even pointer">
				<td class="a-center ">교재 수령지</td>
				<td class=" last" id="post_addr">(${output.post_zipcode }) ${output.post_addr }</td>
			</tr>
			<tr class="even pointer">
				<td class="a-center ">배송지 요청사항</td>
				<td class=" last" id="post_req">${output.post_req }</td>
			</tr>
			<tr class="even pointer">
				<td class="a-center ">배송사</td>
				<td class=" last" id="delv_corp_str"></td>
			</tr>
			<tr class="even pointer">
				<td class="a-center ">송장번호</td>
				<td class=" last" id="delv_no_str"></td>
			</tr>
			<tr class="even pointer">
				<td class=" last" id="txt_post_req" colspan="2" style="color:red;font-weight:bold;">
					□ 교재 수령지 미입력시 교재가 발송되지 않습니다.
				</td>
			</tr>			
		</tbody>
	</table>
	<div class="actionBar">
		<a id='btn-delivery-req' class="btn btn-info"><i class="fa fa-file"> 교재배송지 수정하기</i></a>
		<a id='btn-pass' class="btn btn-success"><i class="fa fa-file"> 닫기</i></a>
	</div>		
</div>


</body>
</html>