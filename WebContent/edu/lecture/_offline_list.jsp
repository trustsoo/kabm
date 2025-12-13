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


function gotoPage(page_no)
{	
	document.frm.page_no.value = page_no;
	js_getUserOfflineList();
}


function js_getUserOfflineList()
{
	jQuery.ajax({
		url : '/edu/lecture/action/offlineAction.jspx?cmd=getUserOfflineHistory', 
		data : jQuery("#frm").serialize(),
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
						var jisatel = json[i].jisatel;
						var is_date = json[i].is_date;
						var stime = json[i].stime;
						var etime = json[i].etime;
						var iplace = json[i].iplace;
						
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
						
						
						var use_yn = json[i].use_yn;
						var web_yn = json[i].web_yn;
						var start_yn = json[i].start_yn;
						var end_yn = json[i].end_yn;
						var ing_yn = json[i].ing_yn;
						
						
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
						
						if( use_yn == 'Y' && web_yn == 'Y' && ing_yn == 'Y' && start_yn == 'Y' && end_yn != 'Y' )
							listHtml.push('		  <td><span onclick="js_popEduList(\''+user_offline_seq_no+'\',\''+is_gb+'\',\''+ji_code+'\');" class="btn btn-sm btn-primary">교육변경</span></td>');						
						else
							listHtml.push('		  <td></td>');
						
						listHtml.push('		  <td rowspan="2"><span onclick="js_goDetail(\''+user_offline_seq_no+'\');" class="btn btn-sm btn-success">내용변경</span></td>');
						
						
						listHtml.push('	</tr>');
						listHtml.push('	<tr>');
						listHtml.push('		  <td>'+jdate+'</td>');
						listHtml.push('		  <td>'+jisatel+ '</td>');
						
						listHtml.push('		  <td colspan="2">'+iplace+ '</td>');
						
						listHtml.push('		  <td colspan="2">'+corp_addr_nm +'</td>');						
						listHtml.push('		  <td>'+phone_no+'</td>');						
						listHtml.push('		  <td>'+sex_txt+'</td>');
						
						if( use_yn == 'Y' && web_yn == 'Y' && ing_yn == 'Y' && start_yn == 'Y' && end_yn != 'Y' )
							listHtml.push('		  <td><span onclick="js_goCancel(\''+user_offline_seq_no+'\');" id="btn-reg" class="btn btn-sm btn-danger" type="reset">신청취소</span></td>');
						else
							listHtml.push('		  <td></td>');
						
						listHtml.push('	</tr>');
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="11" style="text-align:center;">신청 내역이 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				js_userPaging('pagingDiv', jQuery('#page_no').val(), jQuery('#tot_cnt').val(), jQuery('#rnum').val(), 'gotoPage');
			}
		}
	});
	
}

function js_goDetail(user_offline_seq_no)
{
	if( jQuery("input:checkbox[id='chk_alarm']").is(":checked") != true )
	{
		msgOpen("다음 단계로 넘어가기 위해서는 아래 주의사항을 꼭 정독하고 확인을 눌러주시기 바랍니다.");
		return;
	}
	
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=viewOfflineView';
	jQuery('#user_offline_seq_no').val(user_offline_seq_no);
	jQuery('#frm').attr('action', _url);
	jQuery('#frm').submit();
}

function js_goCancel(user_offline_seq_no)
{
	
	if(!confirm('제출하신 정보가 삭제 됩니다. 정말 취소하시겠습니까?')) return;
	
	jQuery('#user_offline_seq_no').val(user_offline_seq_no);
	
	jQuery.ajax({
		url : '/edu/lecture/action/offlineAction.jspx?cmd=getUserOfflineCancel', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				msgOpen( '취소가 완료되었습니다. \n교육비 입금 완료한 경우 해당지회로 연락하셔서 환불요청하세요.');
				js_getUserOfflineList();
			}else{
				msgOpen(msg_com_code_007);
			}
		}
	});
	
}

function js_popEduList(user_offline_seq_no, is_gb, ji_code)
{
	if( jQuery("input:checkbox[id='chk_alarm']").is(":checked") != true )
	{
		msgOpen("다음 단계로 넘어가기 위해서는 아래 주의사항을 꼭 정독하고 확인을 눌러주시기 바랍니다.");
		return;
	}
	var url = '/common/common.jspx?cmd=offlineEduList&is_gb='+is_gb + '&user_offline_seq_no='+user_offline_seq_no+ '&ji_code='+ji_code;
	window.open(url, "offlinePoP" , "width=1024,height=780,toolbar=no,scroll=no,menubar=no");
}

function lf_setEduChange(cp_code, ji_code, dcm_no, user_offline_seq_no){
	
	if( user_offline_seq_no == null || user_offline_seq_no == '' || user_offline_seq_no == 'undefined') 
	{
		msgOpen('변경하시려면 교육을 선택하셔야 합니다.');
		return;
	}	
	if(!confirm('교육일자를 변경하시겠습니까?')) return;
	
	jQuery('#cp_code').val(cp_code);
	jQuery('#ji_code').val(ji_code);
	jQuery('#dcm_no').val(dcm_no);
	
	jQuery.ajax({
		url : '/edu/lecture/action/offlineAction.jspx?cmd=getUserOfflineChange&user_offline_seq_no='+user_offline_seq_no, 
		data : jQuery("#frmChange").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				msgStart( msg_com_code_006);
				js_getUserOfflineList();
			}else{
				msgOpen(msg_com_code_007);
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

<form id="frmChange" name="frmChange" method="post" action="" >
<input type="hidden" id="cp_code" name="cp_code" value=""/>
<input type="hidden" id="ji_code" name="ji_code" value=""/>
<input type="hidden" id="dcm_no" name="dcm_no" value=""/>
</form>

<form id="frm" name="frm" method="post" action="" >
<input type="hidden" id="rnum" name="rnum" value="10"/>
<input type='hidden' name='page_no' id='page_no' value='1'>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
<input type='hidden' name='req_user_nm' id='req_user_nm' value='${input.req_user_nm}'>	
<input type='hidden' name='req_tel_no' id='req_tel_no' value='${input.req_tel_no}'>
<input type='hidden' name='user_offline_seq_no' id='user_offline_seq_no' value=''>
<input type='hidden' name='confirm_step' id='confirm_step' value='${input.confirm_step}'>
<input type='hidden' name='jcode' id='jcode' value='${input.jcode}'>
</form>

	<div id="content">
				
		<div class="directions" style="margin-bottom:10px;">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
				<tbody>
					<tr>
						<th scope="row">주의사항</th>
						<td>
						<p style="margin-left:0px;color:red;font-weight:bold;font-size:14px;">		
						※ [교육일자 및 교육시간]은 동일지회 내에서는 변경가능하나, 타지회로 변경은 불가하오니 새로 신청하시기 바랍니다.

						
						<br>
						</p>	
						<p style="margin-left:0px;color:blue;font-weight:bold;font-size:14px;">							
						※ 취소하고자하는 지회의 [신청취소] 버튼을 클릭하시고, 교육비 입금하신 경우 입금한 지회로 연락하시어 환불요청 하시기 바랍니다.						
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
		<strong style="color:red;font-size:16px;">• 신청서 접수 내역입니다.</strong><br>
		<strong style="color:blue;font-size:14px;">• 교육비 입금을 완료하셔야 최종 접수완료로 처리되며, 입금확인은 각 신청지회로 확인하시기 바랍니다.</strong><br>
		</td></tr>
		</table>
		
		<div class="list_type3">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="4%"/>
				<col width="11%"/><col width="10%"/>
				<col width="8%"/><col width="12%"/>				
				<col width=""/><col width=""/>
				<col width="10%"/><col width="8%"/>
				<col width="9%"/><col width="9%"/>
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
					<th scope="row">교육변경</th>
					<th scope="row" rowspan="2">내용변경</th>
				</tr>
				<tr>
					<th scope="row">접수일자</th>
					<th scope="row">지회연락처</th>
					<th scope="row" colspan="2">교육장소</th>
					<th scope="row" colspan="2">회사주소</th>
					<th scope="row">핸드폰</th>
					<th scope="row">성별</th>
					<th scope="row">신청취소</th>
				</tr>
			</thead>
			<tbody id="list_div">
				
			</tbody>
		</table>
	</div>
	<div class="paging" id='pagingDiv'></div>
	
		
</div>

</form>	
<script type="text/javascript">
	jQuery(document).ready(function(){
			
		   jQuery('.tit_1').text('집합교육 신청내역 확인하기');
		}
	);
	
</script>
</body>
</html>