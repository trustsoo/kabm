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
						var titleHTML = "<div style='WIDTH:100px;' class='ellipsis'>" + title +"</div>";
						var cancelHTML = "";
						if( '03' == status && 'Y' == is_cancel && ('SC0010' == lgd_paytype || 'SC0030' == lgd_paytype) ) 
							cancelHTML = "<a href='javascript:fn_paymentCancel(\"" + user_lecture_seq_no + "\",\"" + lgd_oid + "\",\"" + lgd_tid + "\")' class=\"pbtn01_1 mini\"><span>취소</span></a>";
						if( '00' != lecture_status && ('SC0010' == lgd_paytype || 'SC0030' == lgd_paytype) ) cancelHTML = "취소불가";
						
						var printHTML = "<a href='javascript:fn_billDown(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\")' class='pbtn01_1 mini'><span>발급</span></a>";
						if( status != '03' ) printHTML = '';
						
						var certHTML = "";
						if( '03' == status && 'SC0010' == lgd_paytype  ) 
							certHTML = "<a href='javascript:fn_billView(\"" + lgd_tid + "\",\"" + lgd_authkey + "\")' class='pbtn01_1 mini'><span>발급</span></a>";
						
						var deliveryHTML = "<a href='javascript:fn_DeliveryView(\"" + lgd_oid + "\")' class='pbtn01_1 mini'><span>확인</span></a>";
						if( status != '03' ) deliveryHTML = '';
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td>'+titleHTML+'</td>');
						listHtml.push('		  <td>'+lgd_paytype_nm+'</td>');
						listHtml.push('	  	  <td>'+getNumberFormat(lgd_amount)+'</td>');
						listHtml.push('	  	  <td>'+issue_dt+'</td>');
						listHtml.push('	  	  <td>'+lgd_paydate+'</td>');
						listHtml.push('	  	  <td>'+status_nm+'</td>');
						listHtml.push('	  	  <td>'+cancelHTML+'</td>');
						listHtml.push('	  	  <td>'+printHTML+'</td>');
						listHtml.push('	  	  <td>'+certHTML+'</td>');
						listHtml.push('	  	  <td>'+deliveryHTML+'</td>');
						listHtml.push('	</tr>');
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="10" style="text-align:center;">조회된 결과가 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				
				js_userPaging('pagingDiv', jQuery('#page_no').val(), jQuery('#tot_cnt').val(), jQuery('#rnum').val(), 'gotoPage');
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
					
					var tx = ( jQuery(window).width() - 600 ) / 2+jQuery(window).scrollLeft();
					var ty = jQuery(window).scrollTop() + 200;
					
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

function fn_lectureView(lecture_seq_no)
{
	location.href = '/edu/lecture/lectureCtrl.jspx?cmd=viewLectureView&lecture_seq_no='+lecture_seq_no;	
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
		location.href='/edu/lecture/classCtrl.jspx?cmd=viewClassList';
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
<form id="frmDelivery" id="frmDelivery" method="post" action="/edu/payment/paymentCtrl.jspx?cmd=paymentDelivery" target="DELIVERY">
<input type='hidden' name='user_seq_no' id='user_seq_no' value='${input.s_user_seq_no}'>
<input type='hidden' name='user_id' id='user_id' value='${input.s_emp_no}'>
<input type="hidden" name="LGD_OID"  id="LGD_OID" value="">
<input type="hidden" name="delv_corp"  id="delv_corp" value="">
<input type="hidden" name="delv_no"  id="delv_no" value="">
<input type="hidden" name="mod_yn"  id="mod_yn" value="Y">
</form>
<!-- search -->
<form id="frm" name="frm" method="post" action="">
<input type="hidden" id="rnum" name="rnum" value="20"/>
<input type='hidden' name='page_no' id='page_no' value='1'>
<input type='hidden' name='user_seq_no' id='user_seq_no' value='${input.s_user_seq_no}'>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>

<div id="content">
		<!-- start :: content -->
		
			<div class="directions" style="margin-bottom:20px;">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<tbody>
						<tr>
							<th scope="row">영수증<br>출력안내</th>
							<td>
										1. 해당과목의 영수증 하단 「발급」을 클릭하세요.<br>
										2. 「파일 열기」 또는 「저장」 후 영수증을 출력하세요.	<br>
										
										<strong style="color:red;font-size:14px;">※ 영수증이 정상적으로 열리지 않거나 문제가 발생하면 설치하세요.</strong>&nbsp;<a href="https://get.adobe.com/kr/reader/" target="_blank"><img src="/static/main/img/common/pdf-ico.jpg" height="20"></a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		
			<ul class="contTabs3">
				<li><a href="#" class="on">온라인교육</a></li>
				<!-- <li><a href="/edu/payment/paymentCtrl.jspx?cmd=viewPaymentOffline">집합교육</a></li> -->
				<li style="float:right;border:0px;">
					<span id="btn-lecture" class="btn btn-sm btn-info"><i class="fa fa-file"> 나의강의실 바로가기</i></span>
				</li>
			</ul>
			
			<div class="list_type2">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<colgroup>
						<col width="5%"/><col width="10%"/>
						<col width="10%"/><col width="15%"/>
						<col width="15%"/><col width="10%"/>
						<col width="8%"/><col width="8%"/>
						<col width="8%"/><col width="8%"/>
					</colgroup>
					<thead>
						<tr>
							<th>제목</th>
							<th>결제수단</th>
							<th>결제금액</th>
							<th>신청일자</th>
							<th>결제일자</th>
							<th>상태</th>
							<th>취소요청</th>
							<th>영수증</th>
							<th>카드전표</th>
							<th>교재배송지</th>
						</tr>
					</thead>
					<tbody id="list_div">
						<tr>
							<td colspan="10">검색된 결과가 없습니다</td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="paging" id='pagingDiv'></div>

		<!-- end :: content -->
		<!-- end :: content -->
		</div>

</form>



<div class="view" id="viewForm" style="border-left: 1px solid #1e90f1;border-right: 1px solid #1e90f1;z-index: 999;width:700px;display: none;position:absolute;left:100px;top:0px;background-color: #FFF;">
	<div style="z-index:999;font-size: 1.6em;font-weight:bold;color:#ffffff;background-color:#3b85c3;height: 2.0em;padding-top: 10px;padding-left: 10px;" id="confirm_title">
	교재배송지
	</div>
	<table class="com_table_box">
		<caption></caption>
		<colgroup>
			<col width="150px"/><col width=""/>
		</colgroup>
		<tbody>
			<tr>
				<th scope="row" class="on">성 명  </th>
				<td id="user_nm">
					${output.user_nm }
				</td>
			</tr>
			
			<tr>
				<th scope="row" class="on">연락처  </th>
				<td id="tel_no">
					${output.tel_no }
				</td>
			</tr>
			<tr>
				<th scope="row" class="on">연락처2  </th>
				<td id="post_tel_no">
					${output.post_tel_no }
				</td>
			</tr>
			<tr>
				<th scope="row" class="on">교재수령지 </th>
				<td id="post_addr">
					(${output.post_zipcode }) ${output.post_addr }
					
				</td>
			</tr>
			<tr>
				<th scope="row" class="on">배송시 요청사항  </th>
				<td id="post_req">
					${output.post_req }
					
				</td>
			</tr>
			<tr>
				<th scope="row" class="on">배송사  </th>
				<td id="delv_corp_str">
					
				</td>
			</tr>
			<tr>
				<th scope="row" class="on">송장번호  </th>
				<td id="delv_no_str">
					
				</td>
			</tr>
			<tr><td colspan="2">
			<span class="pull-right" style="padding: 10px 20px 0 0;">		
				<span id="btn-delivery-req" class="btn btn-sm btn-info"><i class="fa fa-file"> 교재배송지 수정하기</i></span>
				<span id="btn-pass" class="btn btn-sm btn-success"><i class="fa fa-file"> 닫기</i></span>
			</span>
			</td></tr>
		</tbody>	
	</table>
</div>

</body>
</html>