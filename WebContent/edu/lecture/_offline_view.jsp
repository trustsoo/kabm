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

edu_ing_yn = false;
function js_goDetail(user_offline_seq_no)
{
	jQuery.ajax({
		url : '/edu/lecture/action/offlineAction.jspx?cmd=getUserOfflineInfo', 
		data : 'user_offline_seq_no='+user_offline_seq_no,
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.offline_user_info;
				var jsonCnt = jsonObj.result.data.offline_user_info.length;
				
				
				if(jsonCnt > 0){
					var i = 0;
					var user_offline_seq_no = json[i].user_offline_seq_no;
					var schedule_id   = json[i].schedule_id; 
					var cp_code   = json[i].cp_code; 
					var ji_code   = json[i].ji_code; 
					var dcm_no   = json[i].dcm_no;						
					var jisaname = urlDecode(json[i].jisaname);		
					var is_date = json[i].is_date;		
					
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
					
					var deposit_dt = urlDecode(json[i].deposit_dt);
					var deposit_nm = urlDecode(json[i].deposit_nm);
					var deposit_bank = urlDecode(json[i].deposit_bank);
					
					if( use_yn == 'Y' && web_yn == 'Y' && ing_yn == 'Y' && start_yn == 'Y' && end_yn != 'Y' )
						edu_ing_yn = true;
						
						// 신청업체 시작						
						jQuery('#user_offline_seq_no').val(user_offline_seq_no);
						jQuery('#ill_no').val(ill_no);
						jQuery('#corp_reg_no').val(corp_reg_no);
						jQuery('#president_nm').val(president_nm);
						jQuery('#corp_addr_nm').val(corp_addr_nm);
						jQuery('#corp_nm').val(corp_nm);
						
						jQuery('#re_corp_zipcode').val(re_corp_zipcode);
						jQuery('#re_corp_addr').val(re_corp_addr);
						jQuery('#re_corp_nm').val(re_corp_nm);
						jQuery('#re_president_nm').val(re_president_nm);
						jQuery('#re_corp_reg_no').val(re_corp_reg_no);						
						// 신청업체 끝	
						
						// 영업장정보 시작	
						jQuery('#building_nm').val(building_nm);
						jQuery('#building_zipcode').val(building_zipcode);
						jQuery('#building_addr').val(building_addr);
						jQuery('#building_area').val(building_area);
						jQuery('#building_start_dt').val(building_start_dt);
						
						var building_tel_noArr = building_tel_no.split('-');
						if( building_tel_noArr.length == 3 )
						{
							jQuery('#building_tel_no1').val(building_tel_noArr[0]);
							jQuery('#building_tel_no2').val(building_tel_noArr[1]);
							jQuery('#building_tel_no3').val(building_tel_noArr[2]);
						}
						
						if( building_none == 'Y' )
						{
							jQuery("input:checkbox[id='chk_1']").prop("checked", true);
						}else{
							jQuery("input:checkbox[id='chk_1']").prop("checked", false);
						}					
						// 영업장정보 끝
						
						// 책임자정보 시작
						jQuery('#user_nm').val(user_nm);
						jQuery('#sex').val(sex);
						jQuery('#pos_nm').val(pos_nm);
						
						
						var birthArr = birth.split('.');
						if( birthArr.length == 3 )
						{
							jQuery('#birth_yyyy').val(birthArr[0]);
							jQuery('#birth_mm').val(birthArr[1]);
							jQuery('#birth_dd').val(birthArr[2]);
						}
						
						var phone_noArr = phone_no.split('-');
						if( phone_noArr.length == 3 )
						{
							jQuery('#phone_no1').val(phone_noArr[0]);
							jQuery('#phone_no2').val(phone_noArr[1]);
							jQuery('#phone_no3').val(phone_noArr[2]);
						}
						
						var emailArr = email.split('@');
						if( emailArr.length == 2 )
						{
							jQuery('#email1').val(emailArr[0]);
							jQuery('#email2').val(emailArr[1]);
						}
						
						var chrg_ymdArr = chrg_ymd.split('-');
						if( chrg_ymdArr.length == 3 )
						{
							jQuery('#chrg_yyyy').val(chrg_ymdArr[0]);
							jQuery('#chrg_mm').val(chrg_ymdArr[1]);
							jQuery('#chrg_dd').val(chrg_ymdArr[2]);
						}
						// 책임자정보 끝
						
						// 뱅킹정보시작
						var deposit_dtArr = deposit_dt.split('-');
						if( deposit_dtArr.length == 3 )
						{
							jQuery('#deposit_yyyy').val(deposit_dtArr[0]);
							jQuery('#deposit_mm').val(deposit_dtArr[1]);
							jQuery('#deposit_dd').val(deposit_dtArr[2]);
						}
						jQuery('#deposit_nm').val(deposit_nm);
						jQuery('#deposit_bank').val(deposit_bank);
						// 뱅킹정보끝
						
						jQuery('#detail').show();
					
				}
			}
		}
	});
	
}

function js_goHistory(){
	location.href='/edu/lecture/offlineCtrl.jspx?cmd=viewOfflineList';
}

jQuery(document).ready(function(){
	
	var user_offline_seq_no = jQuery('#user_offline_seq_no').val();
	js_goDetail(user_offline_seq_no);
	
	
});
</script>

<script type="text/javascript" class="corp">

function setTabView(code, divTabName)
{
	jQuery('div#' + divTabName + ' > ul > li').each(
		function(i)
		{
			if(  code == jQuery(this).attr("code") )
			{
				jQuery(this).addClass("on");
			}
			else
				jQuery(this).removeClass("on");
		}
	);


	switch(code)
	{
		case 'M' :
			jQuery('#divM').css("display","block");
			jQuery('#divH').css("display","none");
			jQuery('#divE').css("display","none");
			jQuery('#divB').css("display","none");
			break;
		case 'H' :
			jQuery('#divM').css("display","none");
			jQuery('#divH').css("display","block");
			jQuery('#divE').css("display","none");
			jQuery('#divB').css("display","none");
			break;
        case 'E' :
			jQuery('#divM').css("display","none");
			jQuery('#divH').css("display","none");
			jQuery('#divE').css("display","block");
			jQuery('#divB').css("display","none");
			break;
        case 'B' :
			jQuery('#divM').css("display","none");
			jQuery('#divH').css("display","none");
			jQuery('#divE').css("display","none");
			jQuery('#divB').css("display","block");
			break;	
	}
}

function lf_requestCorpSubmit(mod_yn)
{
	if( !edu_ing_yn ) 
	{
		msgOpen('해당 교육은 변경할 수 없습니다.');
		return;
	}	
	
	var ill_no = jQuery('#ill_no').val();
	if( ill_no == '' ){
		msgOpen("[선택] 버튼을 눌러 신청업체(용역업체)를 찾아서 선택하세요.");
		return;
	}
	
	jQuery('#mod_yn').val(mod_yn);
	
	if(mod_yn == 'Y'){
		
		var re_corp_zipcode = jQuery('#re_corp_zipcode').val();
		var re_corp_addr = jQuery('#re_corp_addr').val();	
		var re_corp_nm = jQuery('#re_corp_nm').val();
		var re_corp_reg_no = jQuery('#re_corp_reg_no').val();
		var re_president_nm = jQuery('#re_president_nm').val();
		
		if( re_corp_zipcode == '' && re_corp_addr == '' && re_corp_nm == '' && re_corp_reg_no == '' && re_president_nm == '' ){
			msgOpen("변경하실 정보를 입력하세요. 변경 정보가 없으면 위 '저장' 버튼을 선택하세요.");
			return;
		}
	}
    
	
	jQuery.ajax({
			url : '/edu/lecture/action/offlineAction.jspx?cmd=updateUserOfflineCorpInfo', 
			data : jQuery("#frm").serialize(),
			type: 'POST',
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				if(jsonObj.result.code == '200'){
					msgStart(msg_com_code_006);
				}else{
					msgStart(msg_com_code_007);
				}
			}
		});
}


function lf_corpSelect()
{
	var url = '/common/common.jspx?cmd=viewCorpList&view_div=mem';
	window.open(url, "corpPoP" , "width=700,height=530,toolbar=no,scroll=no,menubar=no");
}

function lf_corpAddrSelect()
{
	openPostcode(1);
}

function lf_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,addr_nm,mb_no,president)
{
	jQuery('#corp_reg_no').val(corp_reg_no);
	jQuery('#corp_nm').val(corp_nm);
	jQuery('#ill_no').val(ill_no);
	jQuery('#corp_addr_nm').val(addr_nm);
	jQuery('#president_nm').val(president);
}


function lf_setCorpInfo(zipcode ,addr)
{
	jQuery('#re_corp_zipcode').val(zipcode);
	jQuery('#re_corp_addr').val(addr);	
	jQuery('#re_corp_zipcode').removeClass('ui-state-error');
	jQuery('#re_corp_addr').removeClass('ui-state-error');
}
</script>

<script type="text/javascript" class="building">

function lf_requestBuildingSubmit()
{
	if( !edu_ing_yn ) 
	{
		msgOpen('해당 교육은 변경할 수 없습니다.');
		return;
	}	
	
	jQuery('#building_tel_no').val( jQuery('#building_tel_no1').val() + '-' + jQuery('#building_tel_no2').val() + '-' + jQuery('#building_tel_no3').val() );
	
	if( jQuery('#building_tel_no1').val() == '' ||  jQuery('#building_tel_no2').val() == '')
		jQuery('#building_tel_no').val( '' );
	
	
	if( jQuery("input:checkbox[id='chk_1']").is(":checked") == true )
	{
		lf_reset();
		jQuery('#building_none').val( 'Y' );
	}else{		
		jQuery('#building_none').val( 'N' );
	}
	
	
	jQuery.ajax({
		url : '/edu/lecture/action/offlineAction.jspx?cmd=updateUserOfflineBuildingInfo', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				msgStart(msg_com_code_006);
			}else{
				msgStart(msg_com_code_007);
			}
		}
	});
	
}

function lf_reset(){
	jQuery('#building_nm').val( '' );
	jQuery('#building_zipcode').val( '' );
	jQuery('#building_addr').val( '' );
	jQuery('#building_tel_no2').val( '' );
	jQuery('#building_tel_no3').val( '' );
	jQuery('#building_area').val( '' );
	jQuery('#building_start_dt').val( '' );
}

function lf_buildingSelect()
{
	openPostcode(2);
}

function lf_setBuildingInfo(zipcode ,addr)
{
	jQuery('#building_zipcode').val(zipcode);
	jQuery('#building_addr').val(addr);	
	jQuery('#building_zipcode').removeClass('ui-state-error');
	jQuery('#building_addr').removeClass('ui-state-error');
}

</script>

<script type="text/javascript" class="student">

function lf_requestStudentSubmit()
{
	if( !edu_ing_yn ) 
	{
		msgOpen('해당 교육은 변경할 수 없습니다.');
		return;
	}	
	
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
	
	
	jQuery.ajax({
		url : '/edu/lecture/action/offlineAction.jspx?cmd=updateUserOfflineStudentInfo', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				msgStart(msg_com_code_006);
			}else{
				msgStart(msg_com_code_007);
			}
		}
	});
	
}


function js_selectEmail()
{
	jQuery('#email2').val( jQuery('#email3').val());	
}

</script>

<script type="text/javascript" class="deposit">

function lf_requestDepositSubmit()
{
	if( !edu_ing_yn ) 
	{
		msgOpen('해당 교육은 변경할 수 없습니다.');
		return;
	}	
	
    if(!checkFormField('#frm'))
	{
		return false;
	}

    
	jQuery('#deposit_dt').val( jQuery('#deposit_yyyy').val() + '-' + jQuery('#deposit_mm').val() + '-' + jQuery('#deposit_dd').val() );
	
	if( jQuery('#deposit_yyyy').val() == '' ||  jQuery('#deposit_mm').val() == '' ||  jQuery('#deposit_dd').val() == '')
		jQuery('#deposit_dt').val( '' );
	
	
	jQuery.ajax({
		url : '/edu/lecture/action/offlineAction.jspx?cmd=updateUserOfflineDepositInfo', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				msgStart(msg_com_code_006);
			}else{
				msgStart(msg_com_code_007);
			}
		}
	});
	
}

</script>

<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
<script>
function openPostcode(div) {
    new daum.Postcode({
        oncomplete: function(data) {
			var postcode = data.zonecode;
			var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
			
			if(div == 1)
				lf_setCorpInfo(postcode, addr);
			else if(div == 2)
				lf_setBuildingInfo(postcode, addr);
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
<input type='hidden' name='req_user_nm' id='req_user_nm' value='${input.req_user_nm}'>	
<input type='hidden' name='req_tel_no' id='req_tel_no' value='${input.req_tel_no}'>

<input type='hidden' name='user_offline_seq_no' id='user_offline_seq_no' value='${input.user_offline_seq_no}'>
<input type='hidden' name='ill_no' id='ill_no' value=''>
<input type='hidden' name='mod_yn' id='mod_yn' value=''>

<input type="hidden" name='building_tel_no' id='building_tel_no' value=''/>
<input type="hidden" name='building_none' id='building_none' value='N'/>

<input type="hidden" name='phone_no' id='phone_no' value=''/>
<input type="hidden" name='email' id='email' value=''/>
<input type="hidden" name='birth' id='birth' value=''/>
<input type="hidden" name='chrg_ymd' id='chrg_ymd' value=''/>
<input type='hidden' name='deposit_dt' id='deposit_dt' value=''>

<div id="content">
		
	
	<div id="detail" style="display:none;">	
		<div id="reg_tab_div">
		<ul class="contTabs4" id="reg_tab">
	        <li onclick="setTabView('M', 'reg_tab_div');" style="width:160px;" class="on" code="M">1. 신청업체정보</li>
	        <li onclick="setTabView('H', 'reg_tab_div');" style="width:160px;" class="" code="H">2. 영업장정보</li>
	        <li onclick="setTabView('E', 'reg_tab_div');" style="width:160px;" class="" code="E">3. 책임자정보</li>
	        <li onclick="setTabView('B', 'reg_tab_div');" style="width:160px;" class="" code="B">4. 입금정보</li>
	    </ul>
	    </div>
	
		<div id="divM" style="display:;">
			<div class="view">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width=""/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">회사명  </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="회사명" name='corp_nm' id='corp_nm' alt='회사명' />
								<a  class="pbtn01 mini" id='btn_corpSel'><span class="">선택</span></a>
								<span style="font-weight:bold;color:red;"> [선택] 버튼을 눌러 신청업체(용역업체)를 찾아서 선택하세요.</span>
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">사업자등록번호  </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="사업자등록번호" value="" name='corp_reg_no' id='corp_reg_no' alt='사업자등록번호'/>
								<span style="font-weight:bold;color:red;"> <사업자등록번호> 미등록의 경우, 협회로 연락주세요.</span>
								
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">대표자 성명  </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="대표자 성명" value="" name='president_nm' id='president_nm' alt='대표자 성명'/>
								
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">회사주소  </th>
							<td>
								<input type="text" class="it long" readonly="readonly" title="회사주소" value="" name='corp_addr_nm' id='corp_addr_nm' alt='회사주소'/>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			<div class="section-1" style="padding:0px;">
			<ul class="guide2" style="margin:0px 0px;">					
					<li class="q" style="text-align:center;width:100%;">
						동일하시면 다음클릭! <a class="pbtn02" id='btn_reg'><span class="">다음</span></a>
					</li>
			</ul>
			<ul class="guide2" style="margin:0px 0px;padding:0px;">					
					<li class="notice" style="text-align:left;width:100%;border-left:0px;">
						- 상단에 검색된 정보가 영업신고증상의 정보와 다를경우 변경된 정보를 아래에 입력하신 후 변경요청을 클릭하세요.<br>
						- 반드시 변경된 항목만 입력해주시고, 검색된 정보가 동일하시면 변경요청을 절대 클릭하지 말아주세요.
					</li>				
			</ul>
			</div>	
			
			<div class="edu_tit2" style="margin-top:0px;height: 35px;">
				<strong style="font-size: 20px;line-height: 20px;">변경요청 정보 입력</strong>
			</div>
			<div class="view" style="margin-top:5px;">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width=""/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">회사명 </th>
							<td>
								<input type="text" class="it " title="회사명" name='re_corp_nm' id='re_corp_nm' alt='회사명' />
								
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">사업자등록번호 </th>
							<td>
								<input type="text" class="it "  title="사업자등록번호" value="" name='re_corp_reg_no' id='re_corp_reg_no' alt='사업자등록번호'/>
								
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">대표자 성명  </th>
							<td>
								<input type="text" class="it " title="대표자 성명" value="" name='re_president_nm' id='re_president_nm' alt='대표자 성명'/>
								
							</td>
						</tr>
						<tr>
							<th scope="row" rowspan="2" class="on">회사주소  </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="영업장-우편번호" value="" name='re_corp_zipcode' id='re_corp_zipcode' alt='영업장-우편번호'/>
								<a class="pbtn01 mini" id='btn_corpAddrSel'><span class="">선택</span></a>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" class="it long" title="" value="" name='re_corp_addr' id='re_corp_addr' alt='영업장주소' />
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			<div class="btnWrap">
				<a href="javascript:js_goHistory();" class="pbtn03"><span class=""><i class="fa fa-mail-reply-all" style="font-size:18px"></i> 목록</span></a>
				<a class="pbtn02" id='btn_reg_corp_mod'><span class="">변경요청</span></a>
			</div>		
		
		</div>
		
		
		<div id="divH" style="display:none;">
			<div class="chkBox" style="margin-bottom: -50px;margin-top: 20px;">
				<label for="">
					<input type="checkbox" class="check" name="chk_1" id="chk_1"/> 
					<span style="font-size:18px;color:red;font-weight:bold;">현장없음</span>
				</label> 
			</div>
			<div class="view">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width=""/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">영업장명(청소현장)  </th>
							<td>
								<input type="text" class="it lag" title="영업장명" value="" name='building_nm' id='building_nm' alt='영업장명'/>
								(* 영업장명 = 청소하는 현장)
							</td>
						</tr>
						<tr>
							<th scope="row" rowspan="2" class="on">영업장주소  </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="영업장-우편번호" value="" name='building_zipcode' id='building_zipcode' alt='영업장-우편번호'/>
								<a class="pbtn01 mini" id='btn_buildingSel'><span class="">선택</span></a>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" class="it long" title="" value="" name='building_addr' id='building_addr' alt='영업장주소' />
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">영업장전화번호  </th>
							<td>
								<select style="width:80px;" name='building_tel_no1' id='building_tel_no1'>
									<option value="02">02</option>
									<option value="031">031</option>
									<option value="032">032</option>
									<option value="033">033</option>
									<option value="041">041</option>
									<option value="042">042</option>
									<option value="043">043</option>
									<option value="044">044</option>
									<option value="051">051</option>
									<option value="052">052</option>
									<option value="053">053</option>									
									<option value="054">054</option>
									<option value="055">055</option>
									<option value="061">061</option>
									<option value="062">062</option>
									<option value="063">063</option>
									<option value="064">064</option>
									<option value="070">070</option>
								</select>
								-
								<input type="text" class="it sm" maxlength="4" title="영업장전화번호" value="" name='building_tel_no2' id='building_tel_no2' />
								-
								<input type="text" class="it sm" maxlength="4" title="영업장전화번호" value="" name='building_tel_no3' id='building_tel_no3' />
							</td>
						</tr>
						
						<tr>
							<th scope="row" class="on">연면적  </th>
							<td>
								<input type="text" class="it" title="연면적" value="" name='building_area' id='building_area' alt='연면적'/> 	㎡
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">영업개시일 </th>
							<td>
								<span class="input-group textbox_width" style="width: 200px;">
										<input class="date-picker W100P form-control" id="building_start_dt" name="building_start_dt" type="text" value="" data-date-format="yyyy-mm-dd"/>
										<span class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</span>
								</span>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			
			<div class="btnWrap">
				<a href="javascript:js_goHistory();" class="pbtn03"><span class=""><i class="fa fa-mail-reply-all" style="font-size:18px"></i> 목록</span></a>
				<a class="pbtn02" id='btn_reg_building'><span class="">저장</span></a>
			</div>
		</div>
		
		
		<div id="divE" style="display:none;">
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
				<a href="javascript:js_goHistory();" class="pbtn03"><span class=""><i class="fa fa-mail-reply-all" style="font-size:18px"></i> 목록</span></a>
				<a class="pbtn02" id='btn_reg_student'><span class="">저장</span></a>
			</div>
		</div>
		
		<div id="divB" style="display:none;">
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
								<c:forEach begin="2018" end="${nextYear}" step="1" var="YY">
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
				<a href="javascript:js_goHistory();" class="pbtn03"><span class=""><i class="fa fa-mail-reply-all" style="font-size:18px"></i> 목록</span></a>
				<a class="pbtn02" id='btn_reg_deposit'><span class="">저장</span></a>
			</div>
		
		</div>
	</div>
		
</div>

</form>	
<script type="text/javascript">
	jQuery(document).ready(function(){
			
		   jQuery('.tit_1').text('집합교육 신청내역 확인하기');
		}
	);
	
</script>
<script type="text/javascript">
	jQuery(document).ready(function(){
			
		   //기본정보 처리
			jQuery('#btn_corpSel').bind("click", function(){
				lf_corpSelect();
			});
			
			jQuery('#btn_corpAddrSel').bind("click", function(){
				lf_corpAddrSelect();
			});
			
			jQuery('#btn_reg_corp').bind("click", function(){
				lf_requestCorpSubmit('N');
			});
			
			jQuery('#btn_reg_corp_mod').bind("click", function(){
				lf_requestCorpSubmit('Y');
			});
			
			// 영업점 처리
			jQuery('#btn_buildingSel').bind("click", function(){
				lf_buildingSelect();
			});
			
			jQuery('#btn_reg_building').bind("click", function(){
				lf_requestBuildingSubmit();
			});
			
			jQuery("input:checkbox[id='chk_1']").bind('click', function(){
			    if ( $(this).is(':checked') ) {
			    	lf_reset();
			    }
			});
			
			$(".date-picker").datepicker({
				format: 'yyyy-mm-dd',			
				todayHighlight: true,
				autoclose: true
			});
			
			
			jQuery('#btn_reg_student').bind("click", function(){
				lf_requestStudentSubmit();
			});
			
			jQuery('#btn_reg_deposit').bind("click", function(){
				lf_requestDepositSubmit();
			});
		}
	);
	
</script>
</body>
</html>