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
	if( jQuery("input:checkbox[id='chk_alarm']").is(":checked") != true )
	{
		msgOpen("다음 단계로 넘어가기 위해서는 아래 주의사항을 꼭 정독하고 확인을 눌러주시기 바랍니다.");
		return;
	}
	
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep4';
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
	var ill_no = jQuery('#ill_no').val();
	if( ill_no == '' ){
		msgOpen("[선택] 버튼을 눌러 신청업체(용역업체)를 찾아서 선택하세요.");
		return;
	}
	
	jQuery('#mod_yn').val('N');
    js_nextStep();
	
}

function lf_requestChange()
{
	var ill_no = jQuery('#ill_no').val();
	if( ill_no == '' ){
		msgOpen("[선택] 버튼을 눌러 신청업체(용역업체)를 찾아서 선택하세요.");
		return;
	}
	
	var re_corp_zipcode = jQuery('#re_corp_zipcode').val();
	var re_corp_addr = jQuery('#re_corp_addr').val();	
	var re_corp_nm = jQuery('#re_corp_nm').val();
	var re_corp_reg_no = jQuery('#re_corp_reg_no').val();
	var re_president_nm = jQuery('#re_president_nm').val();
	
	if( re_corp_zipcode == '' && re_corp_addr == '' && re_corp_nm == '' && re_corp_reg_no == '' && re_president_nm == '' ){
		msgOpen("변경하실 정보를 입력하세요. 변경 정보가 없으면 위 '다음' 버튼을 선택하세요.");
		return;
	}
	
		
	jQuery('#mod_yn').val('Y');
    js_nextStep();
	
}



function lf_corpSelect()
{
	var url = '/common/common.jspx?cmd=viewCorpList&view_div=mem';
	window.open(url, "corpPoP" , "width=700,height=530,toolbar=no,scroll=no,menubar=no");
}

function lf_corpAddrSelect()
{
	openPostcode(3);
}

function lf_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,addr_nm,mb_no,president)
{
	jQuery('#corp_reg_no').val(corp_reg_no);
	jQuery('#corp_nm').val(corp_nm);
	jQuery('#ill_no').val(ill_no);
	jQuery('#corp_addr_nm').val(addr_nm);
	jQuery('#president_nm').val(president);
}


function lf_setBuildingInfo(zipcode ,addr)
{
	jQuery('#re_corp_zipcode').val(zipcode);
	jQuery('#re_corp_addr').val(addr);	
	jQuery('#re_corp_zipcode').removeClass('ui-state-error');
	jQuery('#re_corp_addr').removeClass('ui-state-error');
}
</script>
<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
<script>
function openPostcode(div) {
    new daum.Postcode({
        oncomplete: function(data) {
			var postcode = data.zonecode;
			var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
			lf_setBuildingInfo(postcode, addr);
        }
    }).open();
}

</script>
</head>

<body>

<form name="frm" id="frm" method="post" action="">

<input type='hidden' name='ill_no' id='ill_no' value=''>

<input type='hidden' name='cp_code' id='cp_code' value='${input.cp_code}'>
<input type='hidden' name='ji_code' id='ji_code' value='${input.ji_code}'>

<input type='hidden' name='mod_yn' id='mod_yn' value='N'>	

<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type='hidden' name='dcm_no' id='dcm_no' value='${input.dcm_no}'>
<input type='hidden' name='next_step' id='next_step' value='4'>	
<input type='hidden' name='next_mode' id='next_mode' value='${input.next_mode}'>

	<div id="content">
		<div id="smartwizard" class="sw-main sw-theme-arrows" style="margin-bottom: 15px;">
		<ul class="nav nav-tabs step-anchor">
            <li class="nav-item"><a href="#step-1"><strong>1 단계</strong><br><strong>개인정보 제공 동의</strong></a></li>
            <li class="nav-item"><a href="#step-2"><strong>2 단계</strong><br><strong>신청자 정보 입력</strong></a></li>
            <li class="nav-item active"><a href="#step-3"><strong>3 단계</strong><br><strong>신청업체 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-4"><strong>4 단계</strong><br><strong>영업장 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-5"><strong>5 단계</strong><br><strong>책입자 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-6"><strong>6 단계</strong><br><strong>입금정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-7"><strong>7 단계</strong><br><strong>접수완료</strong></a></li>
            <li class="nav-item done"><a href="#step-8"><strong>8 단계</strong><br><strong>결제 하기</strong></a></li>
        </ul>
		</div>
			
			
			<div class="directions">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<tbody>
						<tr>
							<th scope="row">주의사항</th>
							<td>
							<p style="margin-left:0px;color:blue;font-weight:bold;font-size:14px;">		
							선택 버튼을 눌러 신청업체(용역업체)를 찾아서 선택하세요.<br>
								찾는 회사가 없으면 협회로(☎ 02-465-5900) 연락주세요.<br>
							</p>	
							<p style="margin-left:0px;color:red;font-weight:bold;">							
							※ 신청업체 정보(상호, 대표자, 회사주소)는 반드시 관할시.군.구청에서 발급받은	영업신고증상의 정보를 기재하시기 바랍니다. <br>
							※ 변경사항 발생시 사업자등록증만 변경하고 영업신고증은 변경신고를 하지 않아 발생하는 불이익에 관하여 협회는 책임이 없음을 알려드립니다.
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
				<a class="pbtn01" id='btn_reqCancel'><span class="">취소</span></a>
				<a class="pbtn02" id='btn_mod'><span class="">변경요청</span></a>
			</div>
		</div>
</form>



<script type="text/javascript">
	jQuery(document).ready(function()	{
			
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
		
			jQuery('#btn_corpSel').bind("click", function(){
				lf_corpSelect();
			});
			
			jQuery('#btn_corpAddrSel').bind("click", function(){
				lf_corpAddrSelect();
			});
			
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
			});
			
			jQuery('#btn_mod').bind("click", function(){
				lf_requestChange();
			});
		}
	);
	
</script>
</body>
</html>