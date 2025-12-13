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

<%
	String mem_div = input.getText("mem_div");

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" class="source">

function lf_nextSubmit()
{
	var url = '/mobile/member/join.jspx?cmd=join_step_7';
	jQuery('#frm').attr('action', url);
	jQuery('#frm').submit();
}

function lf_requestSubmit()
{
	if( jQuery("input:checkbox[id='chk_0']").is(":checked") == false )
	{
		jQuery("input:checkbox[id='chk_0']").focus();
		msgStart(msg_join_code_05);
		return;
	}
	
	if( jQuery("input:checkbox[id='chk_1']").is(":checked") == false )
	{
		jQuery("input:checkbox[id='chk_1']").focus();
		msgStart(msg_join_code_05);
		return;
	}
	
	if( jQuery("input:checkbox[id='chk_2']").is(":checked") == false )
	{
		jQuery("input:checkbox[id='chk_2']").focus();
		msgStart(msg_join_code_05);
		return;
	}
	
    if(!checkFormField('#frm'))
	{
		return false;
	}

    
    var mem_div = jQuery('#mem_div').val();
    
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
			// 2022.12.05 임시로 막음 새해 다시 열어야 함
			//jQuery('#corp_reg_no').addClass('ui-state-error');
    		//msgStart(msg_com_code_016+jQuery('#corp_reg_no').attr('alt'), null);
			//return false;
		}
	}

	
	if( jQuery('#remark').val() == jQuery('#remark').attr('title') ) 
	{
			jQuery('#remark').val("");
	}
	
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
	
	if( re_corp_post != '' || re_corp_addr != '' || re_corp_nm != '' || re_corp_reg_no != '' || re_corp_president != ''  || re_corp_tel_no != ''  || re_corp_fax_no != '' ){
		jQuery('#re_yn').val('Y');
	}else{
		jQuery('#re_yn').val('N');
	}
	
	
	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=doCreate",
   		type: "POST",
		data : jQuery('#frm').serialize(true),
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
				lf_nextSubmit();
			} else
			{
				msgStart(msg_com_code_007,'info');
			}
		}
  	});
}

function lf_corpSelect()
{
	var url = '/mobile/common/common.jspx?cmd=viewCorpList&view_div=mem';
	window.open(url, "corpPoP" , "width=700,height=530,toolbar=no,scroll=no,menubar=no");
}


function lf_postSelect()
{
	openPostcode(2);
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

function lf_setPostInfo(zipcode ,addr)
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

</script>
<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
<script>
function openPostcode(div) {
    new daum.Postcode({
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
<style>
.stepContainer .x_panel {
	padding: 0px 0px;
	border: 1px solid #d4d4d4;
}
.stepContainer .x_panel .x_title {
	background-color: #34495e;
	padding: 4px 5px 2px;
	color: #fff;
	font-weight:bold;
}

.stepContainer .panel_toolbox>li>a:hover {
    background-color: transparent;
}

.stepContainer .panel_toolbox {
    min-width: 20px;
}

.stepContainer .x_panel hr {
    margin-top: 10px;
    margin-bottom: 10px;
    border: 0;
    border-top: 2px dotted #73879c;
}
.stepContainer .x_panel .col-md-12 {
	width:100%;
}

</style>
</head>

<body>

<form name="frm" id="frm" method="post" action="">
<input type=hidden name="mem_div" id="mem_div" value="<%=mem_div%>">
<input type="hidden" name="cp_code" id="cp_code" value="">
<input type="hidden" name="ill_no" id="ill_no" value="">

<input type="hidden" name='user_nm' id='user_nm' value='${input.user_nm}'/>
<input type="hidden" name='sex' id='sex' value='${input.sex}'/>
<input type="hidden" name='birth_yyyy' id='birth_yyyy' value='${input.birth_yyyy}'/>
<input type="hidden" name='birth_mm' id='birth_mm' value='${input.birth_mm}'/>
<input type="hidden" name='birth_dd' id='birth_dd' value='${input.birth_dd}'/>
<input type="hidden" name='ssn' id='ssn' value='${input.ssn}'/>

<input type="hidden" name='user_id' id='user_id' value='${input.user_id}'/>
<input type="hidden" name='password' id='password' value='${input.password}'/>
<input type="hidden" name='account_nm' id='account_nm' value='${input.account_nm}'/>
<input type="hidden" name='tel_no' id='tel_no'  value='${input.tel_no}'/>
<input type="hidden" name='email' id='email' value='${input.email}'/>
<input type="hidden" name='zipcode' id='zipcode' value='${input.zipcode}'/>
<input type="hidden" name='addr' id='addr' value='${input.addr}'/>

<input type="hidden" name='corp_tel_no' id='corp_tel_no' value=''/>
<input type="hidden" name='corp_fax_no' id='corp_fax_no' value=''/>
<input type="hidden" name='building_tel_no' id='building_tel_no' value=''/>
<input type="hidden" name='building_fax_no' id='building_fax_no' value=''/>

<input type="hidden" name='re_corp_tel_no' id='re_corp_tel_no' value=''/>
<input type="hidden" name='re_corp_fax_no' id='re_corp_fax_no' value=''/>
<input type="hidden" name='re_yn' id='re_yn' value='N'/>
	
<div id="wizard_verticle" class="form_wizard wizard_verticle">
	<ul class="list-unstyled wizard_steps anchor">
		<li>
			<a href="#step-11" class="selected" isdone="1" rel="1">
				<span class="step_no">1</span>
			</a>
		</li>
		<li>
			<a href="#step-22" class="selected" isdone="1" rel="2">
				<span class="step_no">2</span>
			</a>
		</li>
		<li>
			<a href="#step-33" class="selected" isdone="1" rel="3">
				<span class="step_no">3</span>
			</a>
		</li>
		<li>
			<a href="#step-44" class="selected" isdone="1" rel="4">
				<span class="step_no">4</span>
			</a>
		</li>
		<li>
			<a href="#step-55" class="selected" isdone="1" rel="5">
				<span class="step_no">5</span>
			</a>
		</li>
		
		<li>
			<a href="#step-66" class="selected" isdone="1" rel="6">
				<span class="step_no">6</span>
			</a>
		</li>
		<li>
			<a href="#step-77" class="disabled" isdone="0" rel="7">
				<span class="step_no">7</span>
			</a>
		</li>
	</ul>

	<div class="stepContainer" >
	
		<div id="step-11" class="content" style="display: block;">
			<h2 class="StepTitle">회사정보입력</h2>
			
			
			<div class="x_panel">
				<div class="x_title">
					<h2>기본정보 승인 </h2>
					<ul class="nav navbar-right panel_toolbox">
					<li><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
					</li>
					</ul>
					<div class="clearfix"></div>
				</div>
				<div class="x_content">
					<input type="checkbox" class="check" name="chk_0" id="chk_0"/>
					<span style="font-weight:bold;color:red;">회사정보(회사명, 대표자, 회사주소)는  반드시  관할 시,군,구청에서 발급된 영업신고증상의 정보이어야 합니다.</span><br>
					<p style="margin-left:20px;font-weight:bold;color:blue;">
					*회사정보 변경된 경우 사업자등록증만 변경하고, 영업신고증은 변경신고 하지 않아 발생하는 불이익에 관하여 협회에 책임이 없음을 알려드립니다.
					</p>
					<input type="checkbox" class="check" name="chk_1" id="chk_1"/>
					<span style="font-weight:bold;color:red;">대표자, 책임자의 개인아이디로 가입하므로 추후 회사정보 변경시 [교육생 성명 및 생년월일]은 변경이 불가합니다. </span><br><br>
					
					<input type="checkbox" class="check" name="chk_2" id="chk_2"/>
					<span style="font-weight:bold;color:red;">『신규개설 및 승계자교육』의 경우,  협회 프로그램에 등록되지 않아 회사명이 검색되지 않을 수 있습니다.</span><br>
							
				</div>
			</div>	
							
			<div class="x_panel">
				<div class="x_title">
					<h2>회사정보 선택 </h2>
					<ul class="nav navbar-right panel_toolbox">
					<li><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
					</li>
					</ul>
					<div class="clearfix"></div>
				</div>
				<div class="x_content" style="display:none ;">
				
					<span style="font-weight:bold;color:red;">								
					※ (✔)는 반드시 관할 시.군.구청에서 발급받은 [영업신고증]의 정보이어야 합니다.								
					</span><br>
					<span style="font-weight:bold;color:blue;">								
					1) 신규업체: 회사정보가 검색되지 않은 경우
					</span><br>
					<span style="font-weight:bold;">								
					: 하단 <font color="red">[회사변경정보 or 신규등록정보]</font>란 정보입력
					</span><br>
					<span style="font-weight:bold;color:blue;">								
					2) 기존업체: 회사정보(회사명,대표자,회사주소 등)가 변경되었으나 반영되지 않은 경우
					</span><br>
					<span style="font-weight:bold;">								
					 :  [선택]을 눌러 기존의 “회사명”을 선택 후 → 하단 <font color="red">[회사변경정보 or 신규등록정보]</font>란 변경된 정보입력<br>
    								<font color="red">* 반드시 변경된 항목에만 입력 부탁드립니다.</font>
					</span>
					<hr/>	
					<label for="user_nm" class="">회사명 <c:if test="${ input.mem_div == '1' }"><i class="fa fa-asterisk tred"></i></c:if></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<input type="text" class="form-control " readonly="readonly" title="회사명" name='corp_nm' id='corp_nm' alt='회사명' />
							<span class="input-group-btn">
							<a class="btn btn-default" id='btn_memSel'>선택</a>
							</span>									
							</div>
						</div>
						<div class="col-md-3 col-sm-3">
							<span style="font-weight:bold;color:red;"> [선택] 버튼을 눌러 회사를 찾아서 선택하세요.</span>
						</div>
					</div>
					
					<label for="user_nm" class="">대표자 <c:if test="${ input.mem_div == '1' }"><i class="fa fa-asterisk tred"></i></c:if></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " readonly="readonly" title="대표자" value="" name='corp_president' id='corp_president' alt='대표자'/>
						</div>						
					</div>
					
					<label for="user_nm" class="">사업자등록번호 <c:if test="${ input.mem_div == '1' }"><i class="fa fa-asterisk tred"></i></c:if></label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " maxlength="15" readonly="readonly" title="사업자등록번호" value="" name='corp_reg_no' id='corp_reg_no' alt='사업자등록번호'/>
								
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
					<label for="tel_no1" class="">회사팩스번호 </label>
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
					※ <span style="font-weight:bold;color:blue;">신규업체</span>인 경우, 하단에 회사정보입력 후 다음을 클릭하세요.
					</span><br>
					<span style="font-weight:bold;padding-left:10px;">
					 - 영업신고예정일 경우, <span style="font-weight:bold;color:blue;">상단의 필독사항⑶ 예시 참고하여 입력</span> 부탁드립니다.
					</span><br>
					<span style="font-weight:bold;">
					※ <span style="font-weight:bold;color:blue;">회사정보가 변경</span>된 경우, 하단에 변경된 회사정보를 입력 후  다음을 클릭하세요.
					</span><br>
					<span style="font-weight:bold;padding-left:10px;">
					- 반드시 <span style="font-weight:bold;color:blue;">변경된 항목에만 입력</span> 부탁드리며, <span style="font-weight:bold;color:blue;">정확한 정보에 한해서만 처리</span>됩니다.
					</span><br>
					<span style="font-weight:bold;">
					※ 관할시.군.구청에 신규(변경)신고를 완료하신경우 더 빠른 업무처리를 위해 <span style="font-weight:bold;color:blue;">영업신고증을 
 									협회 팩스(02-6234-0376)로 보내주시면 신속히 처리</span>해 드리도록 하겠습니다.
					</span><br>
					<span style="font-weight:bold;">
					※ 변경요청사항은 교육수료 완료 후 순차적으로 처리됩니다.v
					</span>
					<hr/>
					<label for="user_nm" class="">관할 시.군.구청 신고여부</label>
					<div class="form-group row">								
						<div class="col-md-3 col-sm-3">
							<input type="checkbox"  name="re_corp_an_end" id="re_corp_an_end" value="Y">	신규(변경)신고 완료	
						</div>
						<div class="col-md-3 col-sm-3">
							<input type="checkbox"  name="re_corp_an_ing" id="re_corp_an_ing" value="Y">	신규(변경)신고 예정	
						</div>
					</div>
					<label for="re_corp_nm" class="">변경 OR 신규회사명</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " title="변경회사명" name='re_corp_nm' id='re_corp_nm' alt='변경회사명' />	
						</div>						
					</div>
					<label for="re_corp_president" class="">변경 OR 신규대표자</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " title="변경대표자" value="" name='re_corp_president' id='re_corp_president' alt='변경대표자'/>
						</div>						
					</div>
					<label for="re_corp_addr" class="">변경 OR 신규회사주소</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<input type="text" class="form-control " readonly="readonly" title="변경회사주소-우편번호" value="" name='re_corp_post' id='re_corp_post' alt='변경회사주소-우편번호'/>
							<span class="input-group-btn">
							<a class="btn btn-default" id='btn_postSel'>선택</a>
							</span>									
							</div>
							<input type="text" class="form-control " title="" value="" name='re_corp_addr' id='re_corp_addr' alt='변경회사주소' />
						</div>
					</div>
					<label for="re_corp_reg_no" class="">변경 OR 신규사업자등록번호</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<input type="text" class="form-control " maxlength="15" title="변경사업자등록번호" value="" name='re_corp_reg_no' id='re_corp_reg_no' alt='변경사업자등록번호'/>
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
				</div>
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
							<input type="text" class="form-control " title="영업장명" value="" name='building_nm' id='building_nm' alt='영업장명'/>
						</div>
						<div class="col-md-3 col-sm-3">
							<span style="font-weight:bold;color:red;">(* 영업장명 = 청소하는 현장)</span>
						</div>					
					</div>	
					
					<label for="building_addr" class="">영업장주소</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<div class="input-group">
							<input type="text" class="form-control " readonly="readonly" title="영업장-우편번호" value="" name='building_zipcode' id='building_zipcode' alt='영업장-우편번호'/>
							<span class="input-group-btn">
							<a class="btn btn-default" id='btn_buildingSel'>선택</a>
							</span>									
							</div>
							<input type="text" class="form-control " title="" value="" name='building_addr' id='building_addr' alt='영업장주소' />
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
					
					<label for="building_nm" class="">적요사항</label>
					<div class="form-group row">								
						<div class="col-md-12 col-sm-12">
							<textarea name='remark' id='remark' style="width:100%;"  class="txt defaultText" cols="" rows="" ></textarea>
						</div>												
					</div>
				</div>
			</div>
			
			<div class="x_content">
				<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
				<a href="javascript:lf_requestSubmit();" class="btn btn-success">다음</a>
			</div>			
		</div>
	</div>
	
</div>

</form>

<script type="text/javascript">
	jQuery(document).ready(function()	{

			jQuery('#btn_memSel').bind("click", function(){
				lf_corpSelect();
			});
			
			jQuery('#btn_buildingSel').bind("click", function(){
				lf_buildingSelect();
			});
			jQuery('#btn_postSel').bind("click", function(){
				lf_postSelect();
			});
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
			});
			
			jQuery('#btn_chg_reg').bind("click", function(){
				lf_requestChange();
			});
			
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