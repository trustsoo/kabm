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

<html>
<head>
<script type="text/javascript" class="source">


function gotoPage(page_no)
{	
	document.frm.page_no.value = page_no;
	js_getPaymentList();
}


function js_getPaymentList()
{
	jQuery.ajax({
		url : '/edu/payment/action/paymentAction.jspx?cmd=getPaymentOffline', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.payment_off;
				var jsonCnt = jsonObj.result.data.payment_off.length;
				jQuery('#tot_cnt').val(jsonObj.result.data.property[0].tot_cnt);
				var curday = getToday();
				
				if(jsonCnt > 0){
					for(var i=0; i<jsonCnt; i++){
						
						var user_offline_seq_no  = urlDecode(json[i].user_offline_seq_no); 
						var jcode  = urlDecode(json[i].jcode); 
						var dtl_code   = urlDecode(json[i].dtl_code); 
						var dtl_code_nm = urlDecode(json[i].dtl_code_nm); 
						
						var user_seq_no = urlDecode(json[i].user_seq_no);
						var jdate = urlDecode(json[i].jdate);
						var reg_date = urlDecode(json[i].reg_date);
						var ydate = urlDecode(json[i].ydate);
						
						var ist_title = urlDecode(json[i].ist_title);
						var iplace = urlDecode(json[i].iplace);
						var is_ill = urlDecode(json[i].is_ill);
						var amount = urlDecode(json[i].amount);
						
						var printHTML = "<a href='javascript:fn_billDown(\"" + user_offline_seq_no + "\",\"" + jcode + "\")' class='pbtn01_1 mini'><span>발급</span></a>";
						if( true ) printHTML = '';
						//if( ydate == '' ) printHTML = '';
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td>'+ist_title+'</td>');
						listHtml.push('		  <td>'+iplace+'</td>');
						listHtml.push('		  <td>'+is_ill+'</td>');
						listHtml.push('		  <td>'+addComma(amount)+'</td>');
						listHtml.push('	  	  <td>'+ydate+'</td>');
						listHtml.push('	  	  <td>'+jdate+'</td>');
						//listHtml.push('	  	  <td>'+printHTML+'</td>');
						listHtml.push('	</tr>');
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="7" style="text-align:center;">조회된 결과가 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				
				js_userPaging('pagingDiv', jQuery('#page_no').val(), jQuery('#tot_cnt').val(), jQuery('#rnum').val(), 'gotoPage');
			}
		}
	});
	
}

function fn_billDown(user_offline_seq_no,jcode)
{
	var url = '/edu/payment/action/paymentAction.jspx?cmd=downOfflineBill&user_offline_seq_no='+user_offline_seq_no+'&jcode='+jcode;
	window.open(url, "billPoP");

}

jQuery(document).ready(function(){
	
	gotoPage('${input.page_no}'); 
	
});

</script>
</head>

<body>

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
										1. 영수증출력을 클릭하세요.<br>
										2. 해당과목의 영수증 하단 「발급」을 클릭하세요.<br>
										3. 「파일 열기」 또는 「저장」 후 영수증을 출력하세요.	<br>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<ul class="contTabs3">
				<li><a href="/edu/payment/paymentCtrl.jspx?cmd=viewPaymentList" >온라인교육</a></li>
				<li><a href="#" class="on">집합교육</a></li>
			</ul>
				
			<div class="list_type2">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width=""/><col width="20%"/>
						<col width="10%"/><col width="10%"/><col width="10%"/><col width="10%"/><col width="10%"/>
					</colgroup>
					<thead>
						<tr>
							<th>제목</th>
							<th>장소</th>
							<th>교육일자</th>
							<th>결제금액</th>
							<th>결제일자</th>
							<th>접수일자</th>
							<!-- <th>영수증</th> -->
						</tr>
					</thead>
					<tbody id="list_div">
						<tr>
							<td colspan="7">검색된 결과가 없습니다</td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="paging" id='pagingDiv'></div>

		<!-- end :: content -->
		<!-- end :: content -->
		</div>

</form>

</body>
</html>