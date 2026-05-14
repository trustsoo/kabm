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

function lf_requestSubmit()
{
	if( jQuery("input:checkbox[id='chk_alarm']").is(":checked") != true )
	{
		msgOpen("신청 완료를 위해서는 아래 주의사항을 꼭 정독하고 확인을 눌러주시기 바랍니다.");
		return;
	}
	
	location.href='/edu/lecture/offlineCtrl.jspx?cmd=viewOfflineList';
}


</script>
<script src="//t1.kakaocdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	function openPostcode(div) {
		new kakao.Postcode({
			oncomplete: function(data) {
				var postcode = data.zonecode;
				var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
				if( div == 1)
				{
					lf_setAddrInfo(postcode, addr);
				}else if( div == 2)
				{
					lf_setPostInfo(postcode, addr);
				}else if( div == 3)
				{
					lf_setBuildingInfo(postcode, addr);
				}
			}
		}).open();
	}
</script>

</head>

<body>

<form name="frm" id="frm" method="post" action="">
<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type='hidden' name='cp_code' id='cp_code' value='${input.cp_code}'>
<input type='hidden' name='ji_code' id='ji_code' value='${input.ji_code}'>
<input type='hidden' name='dcm_no' id='dcm_no' value='${input.dcm_no}'>

	
	<div id="content">
		<div id="smartwizard" class="sw-main sw-theme-arrows" style="margin-bottom: 15px;">
		<ul class="nav nav-tabs step-anchor">
            <li class="nav-item"><a href="#step-1"><strong>1 단계</strong><br><strong>개인정보 제공 동의</strong></a></li>
            <li class="nav-item"><a href="#step-2"><strong>2 단계</strong><br><strong>신청자 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-3"><strong>3 단계</strong><br><strong>신청업체 입력</strong></a></li>
            <li class="nav-item"><a href="#step-4"><strong>4 단계</strong><br><strong>영업장 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-5"><strong>5 단계</strong><br><strong>책입자 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-6"><strong>6 단계</strong><br><strong>입금정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-7"><strong>7 단계</strong><br><strong>접수완료</strong></a></li>
            <li class="nav-item active"><a href="#step-8"><strong>8 단계</strong><br><strong>결제 하기</strong></a></li>
        </ul>
		</div>
		
		<div class="directions" style="margin-top:20px;">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
				<tbody>
					<tr>
						<th scope="row">주의사항</th>
						<td>
						
						<strong>* 공중위생관리책임자 온라인 접수가 완료되었습니다. </strong><br>
						<font color="red">
						<strong>* 교육비를 해당계좌로 송금하여 주시고, 입금 확인 또는 환불요청시에는 반드시 해당지회로 연락하시기 바랍니다.</strong>
						</font>	
						<div class="pull-right">
								<input type="checkbox" class="check" name="chk_alarm" id="chk_alarm"/> 
								<span style="font-size:16px;color:red;font-weight:bold;">주의사항 확인</span>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>	

		<div class="view" style="margin-top:10px;">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
				<caption></caption>
				<colgroup>
					<col width="10%"/><col width=""/><col width="10%"/><col width=""/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row">지회</th><td>${output.jisaname }</td>
						<th scope="row">지회연락처</th><td>${output.jisatel}</td>
											
					</tr>
					<tr>
						<th scope="row">교육날짜</th><td>${output.is_date }</td>
						<th scope="row">교육과정</th><td>${output.ist_title }</td>	
					</tr>
					<tr>
						<th scope="row">교육시간</th><td>${output.stime} ~ ${output.etime}</td>
						<th scope="row">교육장소</th><td>${output.iplace }</td>
					</tr>	
					<tr>
						<th scope="row">교육비</th><td>${output.edct_expns }</td>
						<th scope="row">계좌번호</th><td>${output.bank_no}</td>
					</tr>
					<tr>
						<th scope="row">은행명</th><td>${output.bank_name}</td>
						<th scope="row">예금주</th><td>${output.bank_ju}</td>
					</tr>
									
				</tbody>				
			</table>
		</div>
		
		<div class="btnWrap">
			<a href="#" class="pbtn02" id='btn_reg'><span class="">확인</span></a>
		</div>	
	
		</div>
</form>



<script type="text/javascript">
	jQuery(document).ready(function()	{

			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
			});
		}
	);
	
</script>
</body>
</html>