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

function js_nextStep()
{
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep8';
	jQuery('#next_step').val('8');
	jQuery('#next_mode').val('');
	jQuery('#frm').attr('action', _url);
	jQuery('#frm').submit();
}


function lf_requestSubmit()
{

    js_nextStep();
}

function lf_requestAdd()
{
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep3';
	jQuery('#frm').attr('action', _url);
	jQuery('#next_mode').val('add');
	jQuery('#next_step').val('3');
	jQuery('#frm').submit();
}
function gotoPage(page_no)
{	
	document.frmSearch.page_no.value = page_no;
	js_getUserOfflineList();
}


function js_getUserOfflineList()
{
	jQuery.ajax({
		url : '/edu/lecture/action/offlineAction.jspx?cmd=getUserOfflineList', 
		data : jQuery("#frmSearch").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.offline_user_info;
				var jsonCnt = jsonObj.result.data.offline_user_info.length;
				
				if( jsonObj.result.data.property.length > 0 )
					jQuery('#tot_cnt').val(jsonObj.result.data.property[0].tot_cnt);
				
				if(jsonCnt > 0){
					for(var i=0; i<jsonCnt; i++){
						var user_offline_seq_no = json[i].user_offline_seq_no;
						var schedule_id   = json[i].schedule_id; 
						var cp_code   = json[i].cp_code; 
						var ji_code   = json[i].ji_code; 
						var dcm_no   = json[i].dcm_no;						
						var jisaname = urlDecode(json[i].jisaname);		
						var jisatel = urlDecode(json[i].jisatel);	
						
						var jcode = json[i].jcode;
						var jdate = json[i].jdate;
						var corp_nm = urlDecode(json[i].corp_nm);
						var mod_yn = json[i].mod_yn;
						var user_nm = urlDecode(json[i].user_nm);
						var birth = json[i].birth;
						var phone_no = json[i].phone_no;
						
						var ill_no = json[i].ill_no;
						var req_user_nm = urlDecode(json[i].req_user_nm);
						var req_tel_no = json[i].req_tel_no;
						var re_corp_zipcode = json[i].re_corp_zipcode;
						var re_corp_addr = urlDecode(json[i].re_corp_addr);
						var re_corp_nm = urlDecode(json[i].re_corp_nm);
						var re_president_nm = urlDecode(json[i].re_president_nm);
						var re_corp_reg_no = json[i].re_corp_reg_no;
						
						var building_none = json[i].building_none;
						var building_nm = urlDecode(json[i].building_nm);
						var building_start_dt = json[i].building_start_dt;
						var building_area = urlDecode(json[i].building_area);
						var building_addr = urlDecode(json[i].building_addr);
						var building_jibun = urlDecode(json[i].building_jibun);
						var building_zipcode = json[i].building_zipcode;
						var building_tel_no = json[i].building_tel_no;
						var chrg_ymd = json[i].chrg_ymd;
						var pos_nm = urlDecode(json[i].pos_nm);
						var tel_no = json[i].tel_no;
						
						
						var email = json[i].email;
						var sex = json[i].sex;
						var is_gb = json[i].is_gb;
						var reg_date = json[i].reg_date;
						
						var corp_reg_no = json[i].corp_reg_no;
						var president_nm = urlDecode(json[i].president_nm);
						var corp_addr_nm = urlDecode(json[i].corp_addr_nm);
						
						var stime = urlDecode(json[i].stime);
						var etime = urlDecode(json[i].etime);
						var is_date   = urlDecode(json[i].is_date); 
						var iplace = urlDecode(json[i].iplace);
						var sex_txt = '';
						if( sex == '1') sex_txt = '남';
						if( sex == '2') sex_txt = '여';
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td rowspan="2">'+(i+1)+ '</td>');
						listHtml.push('		  <td>'+jcode+ '</td>');
						listHtml.push('		  <td>'+jisaname+ '</td>');
						
						listHtml.push('		  <td>'+is_date+ '</td>');
						listHtml.push('		  <td>'+stime +'~'+ etime +'</td>');
						
						listHtml.push('		  <td>'+corp_nm +'</td>');
						listHtml.push('		  <td>'+building_nm +'</td>');
						
						listHtml.push('		  <td>'+user_nm+'</td>');
						listHtml.push('		  <td>'+birth+'</td>');					
						listHtml.push('	</tr>');
						listHtml.push('	<tr>');
						listHtml.push('		  <td>'+jdate+'</td>');
						listHtml.push('		  <td>'+jisatel+ '</td>');
						
						listHtml.push('		  <td colspan="2">'+iplace+ '</td>');
						
						listHtml.push('		  <td colspan="2">'+corp_addr_nm +'</td>');						
						listHtml.push('		  <td>'+phone_no+'</td>');						
						listHtml.push('		  <td>'+sex_txt+'</td>');
						listHtml.push('	</tr>');
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="11" style="text-align:center;">신청 가능한 교육이 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				
			}
		}
	});
	
}



jQuery(document).ready(function(){
	
	
	js_getUserOfflineList();
	
	
});
</script>

</head>

<body>

<form id="frmSearch" name="frmSearch" method="post" action="" onsubmit="return false;">
<input type="hidden" id="rnum" name="rnum" value="100"/>
<input type='hidden' name='page_no' id='page_no' value='1'>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
<input type='hidden' name='dcm_no' id='dcm_no' value='${input.dcm_no}'>
<input type='hidden' name='req_user_nm' id='req_user_nm' value='${input.req_user_nm}'>	
<input type='hidden' name='req_tel_no' id='req_tel_no' value='${input.req_tel_no}'>
</form>

<form name="frm" id="frm" method="post" action="">
<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type='hidden' name='cp_code' id='cp_code' value='${input.cp_code}'>
<input type='hidden' name='ji_code' id='ji_code' value='${input.ji_code}'>
<input type='hidden' name='dcm_no' id='dcm_no' value='${input.dcm_no}'>
<input type='hidden' name='next_step' id='next_step' value='8'>	
<input type='hidden' name='next_mode' id='next_mode' value='${input.next_mode}'>
<input type='hidden' name='req_user_nm' id='req_user_nm' value='${input.req_user_nm}'>	
<input type='hidden' name='req_tel_no' id='req_tel_no' value='${input.req_tel_no}'>

<input type='hidden' name='user_offline_seq_no' id='user_offline_seq_no' value=''>
<input type='hidden' name='ill_no' id='ill_no' value=''>
<input type='hidden' name='mod_yn' id='mod_yn' value=''>

	<div id="content">
		<div id="smartwizard" class="sw-main sw-theme-arrows" style="margin-bottom: 15px;">
		<ul class="nav nav-tabs step-anchor">
            <li class="nav-item"><a href="#step-1"><strong>1 단계</strong><br><strong>개인정보 제공 동의</strong></a></li>
            <li class="nav-item"><a href="#step-2"><strong>2 단계</strong><br><strong>신청자 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-3"><strong>3 단계</strong><br><strong>신청업체 입력</strong></a></li>
            <li class="nav-item"><a href="#step-4"><strong>4 단계</strong><br><strong>영업장 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-5"><strong>5 단계</strong><br><strong>책입자 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-6"><strong>6 단계</strong><br><strong>입금정보 입력</strong></a></li>
            <li class="nav-item active"><a href="#step-7"><strong>7 단계</strong><br><strong>접수완료</strong></a></li>
            <li class="nav-item done"><a href="#step-8"><strong>8 단계</strong><br><strong>결제 하기</strong></a></li>
        </ul>
		</div>
			
		
		<table><tr><td style="text-align:left;">
		<strong style="color:red;font-size:20px;">• 신청서 접수가 완료되었습니다.</strong><br>
		<strong style="color:blue;font-size:14px;">• 접수하신 내역은 아래와 같습니다.</strong><br>
		<strong style="color:blue;font-size:14px;">• 신청서 추가 등록하시려면 추가버튼을 클릭하시고, 끝내시려면 다음버튼을 클릭하세요.</strong>
		</td></tr>
		</table>
		
		
		<div class="list_type3">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="5%"/>
				<col width="14%"/><col width="11%"/>
				<col width="9%"/><col width="14%"/>				
				<col width="13%"/><col width="13%"/>
				<col width="12%"/><col width="9%"/>
				
			</colgroup>
			<thead>
				<tr>
					<th scope="row" rowspan="2">번호</th>
					<th scope="row">접수코드</th>
					<th scope="row">지회</th>
					<th scope="row">교육일자</th>
					<th scope="row">교육시간</th>
					<th scope="row">회사명</th>
					<th scope="row">영업장명</th>
					<th scope="row">교육자명</th>
					<th scope="row">생년월일</th>
				</tr>
				<tr>
					<th scope="row">접수일자</th>
					<th scope="row">지회연락처</th>
					<th scope="row" colspan="2">교육장소</th>
					<th scope="row" colspan="2">회사주소</th>
					<th scope="row">핸드폰</th>
					<th scope="row">성별</th>
				</tr>
			</thead>
			<tbody id="list_div">
				
			</tbody>
		</table>
	</div>
	
	<div class="btnWrap" style="margin-bottom:20px;">
		<a href="javascript:history.go(-1);" class="pbtn01"><span class="">뒤로</span></a>
		<a class="pbtn02" id='btn_add'><span class="">추가 <i class="fa fa-plus" style="font-size:18px"></i></span></a>
		<a class="pbtn02" id='btn_reg'><span class="">다음 <i class="fa fa-sign-in" style="font-size:18px"></i></span></a>
	</div>
		
	
</div>

</form>	
	
<script type="text/javascript">
	jQuery(document).ready(function(){
			
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
			});
			
			jQuery('#btn_add').bind("click", function(){
				lf_requestAdd();
			});
			
		}
	);
	
</script>
</body>
</html>