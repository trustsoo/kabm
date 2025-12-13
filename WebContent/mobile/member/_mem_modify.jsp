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
<%@ include file="/common/common.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" class="source">

function lf_corpSelect()
{
	var url = '/common/commonCtrl.jspx?cmd=viewCorpList&view_div=modify';
	window.open(url, "corpPoP" , "width=700,height=530,toolbar=no,scroll=no,menubar=no");

}

function lf_addrSelect()
{
	openPostcode(1);
}

function lf_buildingSelect()
{
	openPostcode(3);
}

function lf_reqSelect()
{
	openPostcode(4);
}

function lf_requestSubmit(div)
{
	var mem_div = jQuery('#mem_div').val();
	
	if(!checkFormField('#frm_mem'))
		return false;
	
	if( mem_div == '1')
	{
		
		if( jQuery('#corp_nm').val() == '')
		{
			jQuery('#corp_nm').addClass('ui-state-error');
    		msgStart(msg_com_code_016+jQuery('#corp_nm').attr('alt'), null);
			return false;
		}
		if( jQuery('#corp_reg_no').val() == '' )
		{
			jQuery('#corp_reg_no').addClass('ui-state-error');
    		msgStart(msg_com_code_016+jQuery('#corp_reg_no').attr('alt'), null);
			return false;
		}
		
	}
	
	if( jQuery('#remark').val() == jQuery('#remark').attr('title') ) 
	{
			jQuery('#remark').val("");
	}
	
	jQuery('#tel_no').val( jQuery('#tel_no1').val() + '-' + jQuery('#tel_no2').val() + '-' + jQuery('#tel_no3').val() );
	jQuery('#email').val( jQuery('#email1').val() + '@' + jQuery('#email2').val() );
	
	if( jQuery('#tel_no1').val() == '' ||  jQuery('#tel_no2').val() == '')
		jQuery('#tel_no').val( '' );
	
	if( jQuery('#email1').val() == '' ||  jQuery('#email2').val() == '')
		jQuery('#email').val( '' );
	
	jQuery('#corp_tel_no').val( jQuery('#corp_tel_no1').val() + '-' + jQuery('#corp_tel_no2').val() + '-' + jQuery('#corp_tel_no3').val() );
	jQuery('#corp_fax_no').val( jQuery('#corp_fax_no1').val() + '-' + jQuery('#corp_fax_no2').val() + '-' + jQuery('#corp_fax_no3').val() );
	jQuery('#building_tel_no').val( jQuery('#building_tel_no1').val() + '-' + jQuery('#building_tel_no2').val() + '-' + jQuery('#building_tel_no3').val() );
	jQuery('#building_fax_no').val( jQuery('#building_fax_no1').val() + '-' + jQuery('#building_fax_no2').val() + '-' + jQuery('#building_fax_no3').val() );
	
	if( jQuery('#corp_tel_no1').val() == '' ||  jQuery('#corp_tel_no2').val() == '')
		jQuery('#corp_tel_no').val( '' );
	
	if( jQuery('#corp_fax_no1').val() == '' ||  jQuery('#corp_fax_no2').val() == '')
		jQuery('#corp_fax_no').val( '' );
	
	if( jQuery('#building_tel_no1').val() == '' ||  jQuery('#building_tel_no2').val() == '')
		jQuery('#building_tel_no').val( '' );
	
	if( jQuery('#building_fax_no1').val() == '' ||  jQuery('#building_fax_no2').val() == '')
		jQuery('#building_fax_no').val( '' );
	
	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=doModify&templet-bypass",
   		type: "POST",
		data : jQuery('#frm_mem').serialize(true),
   		async:false,
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_007, 'danger');
		},
   		success:function(json) 
   		{	
   			var code = json.result.code;
	   		var msg =  json.result.msg;
	   		var data = json.result.data;
	   		
	   		if(code == '200')
			{
	   			msgStart(msg_com_code_009);
			} else
			{	
				msgStart(msg_com_code_007, 'danger');
			}
		}
  	});
}

function lf_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,addr_nm,mb_no,president)
{
	jQuery('#corp_reg_no').val(corp_reg_no);
	jQuery('#corp_nm').val(corp_nm);
	jQuery('#cp_code').val(cp_code);
	jQuery('#ill_no').val(ill_no);
	jQuery('#addr_nm').val(addr_nm);
	jQuery('#corp_president').val(president);
		
	jQuery('#corp_reg_no').removeClass('ui-state-error');
	jQuery('#corp_nm').removeClass('ui-state-error');
}

function lf_setAddrInfo(zipcode ,addr)
{
	jQuery('#zipcode').val(zipcode);
	jQuery('#addr').val(addr);
	
	jQuery('#zipcode').removeClass('ui-state-error');
	jQuery('#addr').removeClass('ui-state-error');
}

function lf_setRequestInfo(zipcode ,addr)
{
	jQuery('#re_corp_post').val(zipcode);
	jQuery('#re_corp_addr').val(addr);	
	jQuery('#re_corp_post').removeClass('ui-state-error');
	jQuery('#re_corp_addr').removeClass('ui-state-error');	
	
}

function lf_setBuildingInfo(zipcode ,addr)
{
	jQuery('#building_zipcode').val(zipcode);
	jQuery('#building_addr').val(addr);	
	jQuery('#building_zipcode').removeClass('ui-state-error');
	jQuery('#building_addr').removeClass('ui-state-error');
}

function lf_corpSelect()
{
	var url = '/common/common.jspx?cmd=viewCorpList&view_div=mod';
	window.open(url, "corpPoP" , "width=700,height=530,toolbar=no,scroll=no,menubar=no");
}

function lf_buildingSelect()
{
	openPostcode(3);
}

function lf_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,addr_nm,mb_no,president)
{
	jQuery('#corp_reg_no').val(corp_reg_no);
	jQuery('#corp_nm').val(corp_nm);
	jQuery('#cp_code').val(cp_code);
	jQuery('#ill_no').val(ill_no);
	jQuery('#addr_nm').val(addr_nm);
	jQuery('#corp_president').val(president);
}

function lf_setBuildingInfo(zipcode ,addr)
{
	jQuery('#building_zipcode').val(zipcode);
	jQuery('#building_addr').val(addr);	
	jQuery('#building_zipcode').removeClass('ui-state-error');
	jQuery('#building_addr').removeClass('ui-state-error');
}

function js_selectEmail()
{
	jQuery('#email2').val( jQuery('#email3').val());	
}


function lf_memberReq()
{
	jQuery('#re_corp_tel_no').val( jQuery('#re_corp_tel_no1').val() + '-' + jQuery('#re_corp_tel_no2').val() + '-' + jQuery('#re_corp_tel_no3').val() );
	jQuery('#re_corp_fax_no').val( jQuery('#re_corp_fax_no1').val() + '-' + jQuery('#re_corp_fax_no2').val() + '-' + jQuery('#re_corp_fax_no3').val() );
	
	if( jQuery('#re_corp_tel_no1').val() == '' ||  jQuery('#re_corp_tel_no2').val() == '')
		jQuery('#re_corp_tel_no').val( '' );
	
	if( jQuery('#re_corp_fax_no1').val() == '' ||  jQuery('#re_corp_fax_no2').val() == '')
		jQuery('#re_corp_fax_no').val( '' );
	
		
	var re_corp_post = jQuery('#re_corp_post').val();
	var re_corp_addr = jQuery('#re_corp_addr').val();	
	var re_corp_nm = jQuery('#re_corp_nm').val();
	var re_corp_reg_no = jQuery('#re_corp_reg_no').val();
	var re_corp_president = jQuery('#re_corp_president').val();
	
	var re_corp_tel_no = jQuery('#re_corp_tel_no').val();
	var re_corp_fax_no = jQuery('#re_corp_fax_no').val();
	
	if( re_corp_post == '' && re_corp_addr == '' && re_corp_nm == '' && re_corp_reg_no == '' && re_corp_president == ''  && re_corp_tel_no == ''  && re_corp_fax_no == '' ){
		msgOpen("변경하실 정보를 입력하세요.");
		return;
	}
	
		
	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=memberReq",
   		type: "POST",
		data : jQuery('#frm_mem').serialize(true),
		async : false,
		datatype: 'json',
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_007, 'danger');
		},
   		success:function(json)
   		{
   			var code = json.result.code;
	   		var msg =  json.result.msg;
	   		var data = json.result.data;
	   		
	   		if(code == '200')
			{
				msgStart(msg_com_code_009);
				
				jQuery('#tdReqBtn').html( ' <p style="font-weight:bold;color:red;">'+yyyymmddFormat(getToday(),'.')+' <br>정보변경 요청 처리중</p>');
			} else
			{
				msgStart(msg_com_code_007,'info');
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
        	//var postcode = data.postcode1 + '-' + data.postcode2;
        	//var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
        	//var addr = data.address1;
			var postcode = data.zonecode;
			var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
			if( div == 1)
			{
				lf_setAddrInfo(postcode, addr);
			}else if( div == 2)
			{
			}else if( div == 3)
			{
				lf_setBuildingInfo(postcode, addr);
			}else if( div == 4)
			{
				lf_setRequestInfo(postcode, addr);
			}
        }
    }).open();
}
</script>

<style>
.wizard_verticle .x_panel {
	padding: 0px 0px;
	border: 1px solid #d4d4d4;
}
.wizard_verticle .x_panel .x_title {
	background-color: #34495e;
	padding: 4px 5px 2px;
	color: #fff;
	font-weight:bold;
}

.wizard_verticle .panel_toolbox>li>a:hover {
    background-color: transparent;
}

.wizard_verticle .panel_toolbox {
    min-width: 20px;
}

.wizard_verticle .x_panel hr {
    margin-top: 10px;
    margin-bottom: 10px;
    border: 0;
    border-top: 2px dotted #73879c;
}
.wizard_verticle .x_panel .col-md-12 {
	width:100%;
}

</style>

</head>
		
<body>			
<form name="frm_mem" id="frm_mem" method="post" onsubmit="return false;">
<input type="hidden" name="user_id" id="user_id" value="${output.user_id }">
<input type="hidden"  name="user_seq_no" id="user_seq_no" value="${output.user_seq_no}">
<input type="hidden" name="cp_code" id="cp_code" value="${output.cp_code }">
<input type="hidden" name="ill_no" id="ill_no" value="${output.ill_no }">
<input type="hidden" name='sex' id='sex' value='${output.sex}'/>
<input type="hidden" name='birth_yyyy' id='birth_yyyy' value='${output.birth_yyyy}'/>
<input type="hidden" name='birth_mm' id='birth_mm' value='${output.birth_mm}'/>
<input type="hidden" name='birth_dd' id='birth_dd' value='${output.birth_dd}'/>

<input type="hidden" name='email' id='email' value=''/>
<input type="hidden" name='tel_no' id='tel_no' value=''/>
<input type="hidden" name='corp_tel_no' id='corp_tel_no' value='${output.corp_tel_no}'/>
<input type="hidden" name='corp_fax_no' id='corp_fax_no' value='${output.corp_fax_no}'/>
<input type="hidden" name='building_tel_no' id='building_tel_no' value='${output.building_tel_no}'/>
<input type="hidden" name='building_fax_no' id='building_fax_no' value='${output.building_fax_no}'/>

<input type="hidden" name='re_corp_tel_no' id='re_corp_tel_no' value=''/>
<input type="hidden" name='re_corp_fax_no' id='re_corp_fax_no' value=''/>

<div id="wizard_verticle" class="form_wizard wizard_verticle">
	<div class="" >
	
		<div id="step-11" class="content" style="display: block;">
			<h2 class="StepTitle">정보변경</h2>
			
			
			<div class="x_panel">
				<div class="x_title">
					<h2>필독사항 </h2>
					<ul class="nav navbar-right panel_toolbox">
					<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
					</li>
					</ul>
					<div class="clearfix"></div>
				</div>
				<div class="x_content">
					<span style="font-weight:bold;color:red;">▶회사정보(회사명, 대표자, 회사주소)는  반드시  관할 시,군,구청에서 발급된 영업신고증상의 정보이어야 합니다.</span><br>
					<p style="margin-left:20px;font-weight:bold;color:blue;">
					*회사정보 변경된 경우 사업자등록증만 변경하고, 영업신고증은 변경신고 하지 않아 발생하는 불이익에 관하여 협회에 책임이 없음을 알려드립니다.
					</p>
					<span style="font-weight:bold;color:red;">▶대표자, 책임자의 개인아이디로 가입하므로 추후 회사정보 변경시 [교육생 성명 및 생년월일]은 변경이 불가합니다. </span><br><br>
					
					<span style="font-weight:bold;color:red;">『신규개설 및 승계자교육』의 경우,  협회 프로그램에 등록되지 않아 회사명이 검색되지 않을 수 있습니다.</span><br>
					
					<p style="margin-left:20px;font-weight:bold;color:blue;">	
					     <i class="fa fa-asterisk tred"></i>귀사의 정보를  [회사 변경정보 or 신규등록정보] 란에 입력 > [다음] or [변경요청]을 클릭 > 수강완료 > 1~2일 후 수료증 발급가능<br><br>
					     <i class="fa fa-asterisk tred"></i>영업신고 예정 및  회사정보가 미확정  된 경우 아래 예시와 같이 입력<br>
					</p>     
					<p style="margin-left:20px;">	     
					       (예시)<br>
						       ⊙ 회사명:  미정<br>
						       ⊙ 대표자: 홍길동<br>
						       ⊙ 회사주소: 서울특별시 성동구 /  경기도 김포시 / 강원도 철원군  (*반드시 관할시.군.구까지는 입력해야 관공서 결과보고 가능)<br>
						       ⊙ 사업자등록번호: 미신고 
					</p>							
				</div>
			</div>	
			
			<div class="x_panel">
				<div class="x_title">
					<h2>교육생정보 </h2>
					<ul class="nav navbar-right panel_toolbox">
					<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
					</li>
					</ul>
					<div class="clearfix"></div>
				</div>
				
				<div class="x_content">
					<label for="mem_div" class="">회원구분 <i class="fa fa-asterisk tred"></i></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<c:choose><c:when test="${ output.mem_div == '1' }">책임자</c:when><c:otherwise>신규</c:otherwise></c:choose>
								<input type="hidden" class="it " title="회원구분" value='${output.mem_div }' alt='회원구분' name='mem_div' id='mem_div' />
						</div>
					</div>
					<label for="user_nm" class="">성명 <i class="fa fa-asterisk tred"></i></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " title="성 명" value='${output.user_nm }' alt='성명' name='user_nm' id='user_nm' readonly="readonly" required='true' />
						</div>
					</div>
					<label for="sex_nm" class="">성별 <i class="fa fa-asterisk tred"></i></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " title="성별" value='<c:choose><c:when test="${ output.sex == '1' }">남</c:when><c:when test="${ output.sex == '2' }">여</c:when><c:otherwise></c:otherwise></c:choose>' alt='성별' name='sex_nm' id='sex_nm' readonly="readonly"/>
						</div>
					</div>
					
					<label for="birth" class="">생년월일 <i class="fa fa-asterisk tred"></i></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " title="생년월일" value='${output.birth_yyyy}-${output.birth_mm}-${output.birth_dd}' alt='생년월일' name='birth' id='birth' readonly="readonly" />
						</div>
					</div>
					<label for="email1" class="">이메일 <i class="fa fa-asterisk tred"></i></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<input type="text" class="form-control" title="이메일아이디" value="" name='email1' id='email1' readonly="readonly" />
							<span style="display: table-cell;vertical-align: middle;">@</span> 
							<input type="text" class="form-control" title="이메일주소" value="" name='email2' id='email2' readonly="readonly"/>
							</div>							
						</div>
					</div>
					
					<label for="tel_no1" class="">핸드폰 <i class="fa fa-asterisk tred"></i></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<select class="form-control " style="width:80px;" name='tel_no1' id='tel_no1'>
								<option value="010">010</option>
							</select>
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="핸드폰" value="" name='tel_no2' id='tel_no2' required='true' />
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="핸드폰" value="" name='tel_no3' id='tel_no3' required='true' />
							</div>
						</div>	
					</div>
					
					<label for="zipcode" class="">자택주소 <i class="fa fa-asterisk tred"></i></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
								<input type="text" class="form-control " readonly="readonly" title="자택주소 우편번호" value="${output.zipcode }" name='zipcode' id='zipcode' alt='자택주소-우편번호' required='true'/>
								<span class="input-group-btn">
								<a href="#" class="btn btn-default" id='btn_addrSel'>선택</a>
								</span>
							</div>					
							<input type="text" class="form-control " title="자택주소" value="${output.addr }" name='addr' id='addr' alt='자택주소' required='true'/>
						</div>
					</div>
				</div>
			</div>	
							
			<div class="x_panel">
				<div class="x_title">
					<h2>회사정보  </h2>
					<ul class="nav navbar-right panel_toolbox">
					<li><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
					</li>
					</ul>
					<div class="clearfix"></div>
				</div>
				<div class="x_content" style="display:none ;">
				
					<span style="font-weight:bold;color:red;font-size:18px;">								
					※ (✔)는 반드시 관할 시.군.구청에서 발급받은 [영업신고증]의 정보이어야 합니다.
					</span><br>
					<span style="font-weight:bold;color:blue;font-size:13px;">								
					1) 신규업체: 회사정보가 검색되지 않은 경우
					</span><br>
					<span style="font-weight:bold;font-size:12px;">								
					: 하단 [회사변경정보 or 신규등록정보]란 정보입력 → [변경요청] 클릭 
					</span><br>
					<span style="font-weight:bold;color:blue;font-size:13px;">								
					2) 기존업체: 회사정보(회사명,대표자,회사주소 등)가 변경되었으나 반영되지 않은 경우
					</span><br>
					<span style="font-weight:bold;font-size:12px;">								
					 :  [선택]을 눌러 기존의 “회사명”을 선택 후 → 하단 [회사변경정보 or 신규등록정보]란 변경된 정보입력 → [변경요청] 클릭<br>
    								* 반드시 변경된 항목에만 입력 부탁드립니다.
					</span><br>
					<hr/>	
					<label for="corp_nm" class=""><span style="font-weight:bold;color:red;">√</span>회사명 <c:if test="${ output.mem_div == '1' }"><i class="fa fa-asterisk tred"></i></c:if></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " readonly="readonly" title="회사명" name='corp_nm' id='corp_nm' alt='회사명' value='${output.corp_nm }' />
						</div>						
					</div>
					
					<label for="corp_president" class=""><span style="font-weight:bold;color:red;">√</span>대표자 <c:if test="${ output.mem_div == '1' }"><i class="fa fa-asterisk tred"></i></c:if></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " readonly="readonly" title="대표자" value="${output.rprstt_kr }" name='corp_president' id='corp_president' alt='대표자'/>
						</div>						
					</div>
					
					<label for="addr_nm" class=""><span style="font-weight:bold;color:red;">√</span>회사주소 <c:if test="${ output.mem_div == '1' }"><i class="fa fa-asterisk tred"></i></c:if></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " readonly="readonly" title="회사주소" value="${output.addr_nm }" name='addr_nm' id='addr_nm' alt='회사주소'/>
						</div>						
					</div>
					
					<label for="corp_reg_no" class="">사업자등록번호 <c:if test="${ output.mem_div == '1' }"><i class="fa fa-asterisk tred"></i></c:if></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " maxlength="15" readonly="readonly" title="사업자등록번호" value="${output.corp_reg_no }" name='corp_reg_no' id='corp_reg_no' alt='사업자등록번호'/>
								
						</div>						
					</div>
					<label for="tel_no1" class="">회사전화번호 </label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<select class="form-control " style="width:80px;" name='corp_tel_no1' id='corp_tel_no1'>
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
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="회사전화번호" value="" name='corp_tel_no2' id='corp_tel_no2' />
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="회사전화번호" value="" name='corp_tel_no3' id='corp_tel_no3' />
							</div>
						</div>	
					</div>
					<label for="corp_fax_no1" class="">회사팩스번호 </label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<select class="form-control " style="width:80px;" name='corp_fax_no1' id='corp_fax_no1'>
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
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="회사팩스번호" value="" name='corp_fax_no2' id='corp_fax_no2' />
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="회사팩스번호" value="" name='corp_fax_no3' id='corp_fax_no3' />
							</div>
						</div>	
					</div>
				</div>
			</div>			
					
			<div class="x_panel">
				<div class="x_title">
					<h2 style="overflow: visible;">회사변경 OR 신규등록 </h2>
					<ul class="nav navbar-right panel_toolbox">
					<li><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
					</li>
					</ul>
					<div class="clearfix"></div>
				</div>
				<div class="x_content" style="display:none ;">
					<span style="font-weight:bold;">
					※ <span style="font-weight:bold;color:blue;">신규업체</span>인 경우, 하단의 회사정보입력 후 [변경요청]을 클릭하세요.
					</span><br>
					<span style="font-weight:bold;padding-left:10px;">
					 - 영업신고예정일 경우, <span style="font-weight:bold;color:blue;">상단의 필독사항⑶ 예시 참고하여 입력</span> 부탁드립니다.
					</span><br>
					<span style="font-weight:bold;">
					※ <span style="font-weight:bold;color:blue;">회사정보가 변경</span>된 경우, 하단의 변경된 회사정보를 입력 후 [변경요청]을 클릭하세요.
					</span><br>
					<span style="font-weight:bold;padding-left:10px;">
					- 반드시 <span style="font-weight:bold;color:blue;">변경된 항목에만 입력</span> 부탁드리며, <span style="font-weight:bold;color:blue;">정확한 정보에 한해서만 처리</span>됩니다.
					</span><br>
					※ <span style="font-weight:bold;color:blue;">관할시.군.구청에 신규(변경)신고를 완료</span>하신경우 더 빠른 업무처리를 위해 <span style="font-weight:bold;color:blue;">영업신고증을 협회 팩스(02-6234-0376)로 보내주시면 신속히 처리</span>해 드리도록 하겠습니다.<br>
								
					<span style="font-weight:bold;">
					※ 변경요청사항은 교육수료 완료 후 순차적으로 처리됩니다.v
					</span>
					<hr/>
					<label for="user_nm" class="">관할 시.군.구청 신고여부</label>
					<div class="form-group row">								
						<div class="col-md-3 col-sm-3">
							<input type="checkbox"  name="re_corp_an_end" id="re_corp_an_end" value="Y" <c:if test="${reqOutput.re_corp_an_end == 'Y'}">checked</c:if>>	신규(변경)신고 완료	
						</div>
						<div class="col-md-3 col-sm-3">
							<input type="checkbox"  name="re_corp_an_ing" id="re_corp_an_ing" value="Y" <c:if test="${reqOutput.re_corp_an_ing == 'Y'}">checked</c:if>>	신규(변경)신고 예정	
						</div>
					</div>
					<label for="re_corp_nm" class="">변경 OR 신규회사명</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " title="변경회사명" name='re_corp_nm' id='re_corp_nm' value="${reqOutput.re_corp_nm}" alt='변경회사명' />	
						</div>						
					</div>
					<label for="re_corp_president" class="">변경 OR 신규대표자</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " title="변경대표자" value="${reqOutput.re_corp_president}" name='re_corp_president' id='re_corp_president' alt='변경대표자'/>
						</div>						
					</div>
					<label for="re_corp_addr" class="">변경 OR 신규회사주소</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<input type="text" class="form-control " readonly="readonly" title="변경회사주소-우편번호" value="${reqOutput.re_corp_post}" name='re_corp_post' id='re_corp_post' alt='변경회사주소-우편번호'/>
							<span class="input-group-btn">
							<a class="btn btn-default" id='btn_reqSel'>선택</a>
							</span>									
							</div>
							<input type="text" class="form-control " title="" value="${reqOutput.re_corp_addr}" name='re_corp_addr' id='re_corp_addr' alt='변경회사주소' />
						</div>
					</div>
					<label for="re_corp_reg_no" class="">변경 OR 신규사업자등록번호</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " maxlength="15" title="변경사업자등록번호" value="${reqOutput.re_corp_reg_no}" name='re_corp_reg_no' id='re_corp_reg_no' alt='변경사업자등록번호'/>
						</div>						
					</div>	
					<label for="re_corp_tel_no1" class="">변경 OR 신규회사전화번호 </label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<select class="form-control " style="width:80px;" name='re_corp_tel_no1' id='re_corp_tel_no1'>
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
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="변경회사전화번호" value="" name='re_corp_tel_no2' id='re_corp_tel_no2' />
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="변경회사전화번호" value="" name='re_corp_tel_no3' id='re_corp_tel_no3' />
							</div>
						</div>	
					</div>
					<label for="re_corp_fax_no1" class="">회사팩스번호 </label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<select class="form-control " style="width:80px;" name='re_corp_fax_no1' id='re_corp_fax_no1'>
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
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="변경회사팩스번호" value="" name='re_corp_fax_no2' id='re_corp_fax_no2' />
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="변경회사팩스번호" value="" name='re_corp_fax_no3' id='re_corp_fax_no3' />
							</div>
						</div>	
					</div>
					<c:if test="${output.req_date == null or output.req_date == '' }">
						<div class="btnWrap">
							<a class="btn btn-success" id='btn_mem_req'>변경요청</a>
						</div>
					</c:if>
				</div>	
					
				<c:if test="${fn:length(output.req_date) > 4 }">
					<div class="x_content">		
					<p style="font-weight:bold;color:red;">${output.req_date} <br>정보변경 요청 처리중</p>
					</div>	
				</c:if>		
				
			</div>
					
			<div class="x_panel">
				<div class="x_title">
					<h2 style="overflow: visible;">영업장 정보 </h2>
					<ul class="nav navbar-right panel_toolbox">
					<li><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
					</li>
					</ul>
					<div class="clearfix"></div>
				</div>
				<div class="x_content" style="display:none;">
					<label for="building_nm" class="">영업장명</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " title="영업장명" value="${output.building_nm }" name='building_nm' id='building_nm' alt='영업장명'/>
						</div>
						<div class="col-md-3 col-sm-3">
							<span style="font-weight:bold;color:red;">(* 영업장명 = 청소하는 현장)</span>
						</div>					
					</div>	
					
					<label for="building_addr" class="">영업장주소</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<input type="text" class="form-control " readonly="readonly" title="영업장-우편번호" value="${output.building_zipcode }" name='building_zipcode' id='building_zipcode' alt='영업장-우편번호'/>
							<span class="input-group-btn">
							<a class="btn btn-default" id='btn_buildingSel'>선택</a>
							</span>									
							</div>
							<input type="text" class="form-control " title="" value="${output.building_addr }" name='building_addr' id='building_addr' alt='영업장주소' />
						</div>
					</div>
					
					<label for="building_tel_no1" class="">영업장전화번호 </label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<select class="form-control " style="width:80px;" name='building_tel_no1' id='building_tel_no1'>
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
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="영업장전화번호" value="" name='building_tel_no2' id='building_tel_no2' />
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="영업장전화번호" value="" name='building_tel_no3' id='building_tel_no3' />
							</div>
						</div>	
					</div>
					<label for="building_fax_no1" class="">영업장팩스번호 </label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<select class="form-control " style="width:80px;" name='building_fax_no1' id='building_fax_no1'>
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
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="영업장팩스번호" value="" name='building_fax_no2' id='building_fax_no2' />
							<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="영업장팩스번호" value="" name='building_fax_no3' id='building_fax_no3' />
							</div>
						</div>	
					</div>
					
					<label for="building_nm" class="">적요사항(비고란)</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<textarea name='remark' id='remark' style="width:100%;"  class="txt defaultText" cols="" rows="" >${output.remark }</textarea>
							<p class="tip tred"></p><br>
						</div>												
					</div>
				</div>
			</div>
			
			<div class="x_content">				
				<a id="btn_reg" class="btn btn-success">저장</a>
				<a id='btn_close' class="btn btn-default">닫기</a>
			</div>			
		</div>
	</div>

</div>



</form>


<script type="text/javascript">
jQuery(document).ready(
		function()
		{	
			
			jQuery('#btn_memSel').bind("click", function(){
				lf_corpSelect();
			});
			
			jQuery('#btn_addrSel').bind("click", function(){
				lf_addrSelect();
			});
			jQuery('#btn_reqSel').bind("click", function(){
				lf_reqSelect();
			});
			
			jQuery('#btn_buildingSel').bind("click", function(){
				lf_buildingSelect();
			});
			
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit(1);
			});
			
			jQuery('#btn_close').bind("click", function(){
				location.href = '/mobile/index.jsp';
			});
			
			jQuery('#btn_mem_req').bind("click", function(){
				lf_memberReq();
			});
			
			var email = '${output.email }';
			var emailArr = email.split('@');
			if( emailArr.length == 2 )
			{
				jQuery('#email1').val(emailArr[0]);
				jQuery('#email2').val(emailArr[1]);
			}
			var tel_no = '${output.tel_no }';
			var tel_noArr = tel_no.split('-');
			if( tel_noArr.length == 3 )
			{
				jQuery('#tel_no1').val(tel_noArr[0]);
				jQuery('#tel_no2').val(tel_noArr[1]);
				jQuery('#tel_no3').val(tel_noArr[2]);
			}
			var corp_tel_no = '${output.corp_tel_no }';
			var corp_tel_noArr = corp_tel_no.split('-');
			if( corp_tel_noArr.length == 3 )
			{
				jQuery('#corp_tel_no1').val(corp_tel_noArr[0]);
				jQuery('#corp_tel_no2').val(corp_tel_noArr[1]);
				jQuery('#corp_tel_no3').val(corp_tel_noArr[2]);
			}
			var corp_fax_no = '${output.corp_fax_no }';
			var corp_fax_noArr = corp_fax_no.split('-');
			if( corp_fax_noArr.length == 3 )
			{
				jQuery('#corp_fax_no1').val(corp_fax_noArr[0]);
				jQuery('#corp_fax_no2').val(corp_fax_noArr[1]);
				jQuery('#corp_fax_no3').val(corp_fax_noArr[2]);
			}
			
			var re_corp_tel_no = '${reqOutput.re_corp_tel_no }';
			var re_corp_tel_noArr = re_corp_tel_no.split('-');
			if( re_corp_tel_noArr.length == 3 )
			{
				jQuery('#re_corp_tel_no1').val(re_corp_tel_noArr[0]);
				jQuery('#re_corp_tel_no2').val(re_corp_tel_noArr[1]);
				jQuery('#re_corp_tel_no3').val(re_corp_tel_noArr[2]);
			}
			var re_corp_fax_no = '${reqOutput.re_corp_fax_no }';
			var re_corp_fax_noArr = re_corp_fax_no.split('-');
			if( re_corp_fax_noArr.length == 3 )
			{
				jQuery('#re_corp_fax_no1').val(re_corp_fax_noArr[0]);
				jQuery('#re_corp_fax_no2').val(re_corp_fax_noArr[1]);
				jQuery('#re_corp_fax_no3').val(re_corp_fax_noArr[2]);
			}
			
			var building_tel_no = '${output.building_tel_no }';
			var building_tel_noArr = building_tel_no.split('-');
			if( building_tel_noArr.length == 3 )
			{
				jQuery('#building_tel_no1').val(building_tel_noArr[0]);
				jQuery('#building_tel_no2').val(building_tel_noArr[1]);
				jQuery('#building_tel_no3').val(building_tel_noArr[2]);
			}
			var building_fax_no = '${output.building_fax_no }';
			var building_fax_noArr = building_fax_no.split('-');
			if( building_fax_noArr.length == 3 )
			{
				jQuery('#building_fax_no1').val(building_fax_noArr[0]);
				jQuery('#building_fax_no2').val(building_fax_noArr[1]);
				jQuery('#building_fax_no3').val(building_fax_noArr[2]);
			}
			
			
			jQuery(".defaultText").focus(function(srcc)
				    {
				        if (jQuery(this).val() == jQuery(this)[0].title)
				        {
				        	jQuery(this).removeClass("defaultTextActive");
				        	jQuery(this).val("");
				        }
				    });
				    
				    jQuery(".defaultText").blur(function()
				    {
				        if (jQuery(this).val() == "")
				        {
				        	jQuery(this).addClass("defaultTextActive");
				        	jQuery(this).val(jQuery(this)[0].title);
				        }
				    });
				    
				    jQuery(".defaultText").blur();  
			
		}
	);
</script>


</body>
</html>			