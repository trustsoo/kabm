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
	var url = '/member/join.jspx?cmd=join_step_7';
	jQuery('#frm').attr('action', url);
	jQuery('#frm').submit();
}

function lf_requestSubmit()
{
	if( jQuery("input:checkbox[id='chk_0']").is(":checked") == false )
	{
		jQuery("input:checkbox[id='chk_0']").focus();
		msgOpen(msg_join_code_05);
		return;
	}
	
	if( jQuery("input:checkbox[id='chk_1']").is(":checked") == false )
	{
		jQuery("input:checkbox[id='chk_1']").focus();
		msgOpen(msg_join_code_05);
		return;
	}
	
	if( jQuery("input:checkbox[id='chk_2']").is(":checked") == false )
	{
		jQuery("input:checkbox[id='chk_2']").focus();
		msgOpen(msg_join_code_05);
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
	var url = '/common/common.jspx?cmd=viewCorpList&view_div=mem';
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
	
	<div class="pageTit">
		<h2><span class="tit_1">회원가입</span></h2>
	</div>
	<div id="content">
			<ul class="joinTab">				
			<% if( MEM_DIV_NEW.equals(mem_div)){ %>
			<!-- 신규교육 스탭 -->
				<li><img src="/static/main/img/sub/join_tab_1_2.jpg"  alt="신규교육" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_2_1_off.jpg"  alt="약관동의" /></li>
			<%}%>
			<% if( MEM_DIV_REP.equals(mem_div)){ %>
			<!-- 책임자교육  스탭 -->
				<li><img src="/static/main/img/sub/join_tab_1_4.jpg"  alt="책임자교육" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_2_2_off.jpg"  alt="약관동의" /></li>
			<%}%>
				<li class=""><img src="/static/main/img/sub/join_tab_3_off.jpg"  alt="교육동의" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_4_off.jpg"  alt="본인확인" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_5_off.jpg"  alt="기본정보입력" /></li>
				<li class="on"><img src="/static/main/img/sub/join_tab_6_on.jpg"  alt="회사정보입력" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_7_off.jpg"  alt="등록확인" /></li>
			</ul>
			
			<div class="directions">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<tbody>
						<tr>
							<th scope="row">필독사항</th>
							<td>
							<input type="checkbox" class="check" name="chk_0" id="chk_0"/>
							<span style="font-weight:bold;color:red;">회사정보(회사명, 대표자, 회사주소)는  반드시  관할 시,군,구청에서 발급된 영업신고증상의 정보이어야 합니다.</span><br>
							<p style="margin-left:20px;font-weight:bold;color:blue;">
							*회사정보 변경된 경우 사업자등록증만 변경하고, 영업신고증은 변경신고 하지 않아 발생하는 불이익에 관하여 협회에 책임이 없음을 알려드립니다.
							</p>
							<input type="checkbox" class="check" name="chk_1" id="chk_1"/>
							<span style="font-weight:bold;color:red;">대표자, 책임자의 개인아이디로 가입하므로 추후 회사정보 변경시 [교육생 성명 및 생년월일]은 변경이 불가합니다. </span><br><br>
							
							<input type="checkbox" class="check" name="chk_2" id="chk_2"/>
							<span style="font-weight:bold;color:red;">『신규개설 및 승계자교육』의 경우,  협회 프로그램에 등록되지 않아 회사명이 검색되지 않을 수 있습니다.</span><br>
							<p style="margin-left:20px;">	
							     *귀사의 정보를  [회사 변경정보 or 신규등록정보] 란에 입력 ▶ [다음] or [변경요청]을 클릭 ▶ 수강완료 ▶ 1~2일 후 수료증 발급가능<br>
							     *영업신고 예정 및  회사정보가 미확정  된 경우 아래 예시와 같이 입력<br>
							       (예시)<br>
								       ⊙ 회사명:  미정<br>
								       ⊙ 대표자: 홍길동<br>
								       ⊙ 회사주소: 서울특별시 성동구 /  경기도 김포시 / 강원도 철원군  (*반드시 관할시.군.구까지는 입력해야 관공서 결과보고 가능)<br>
								       ⊙ 사업자등록번호: 미신고 
							</p>
							
							</td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="view">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width="80px"/><col width=""/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="" rowspan="8">회사<br>정보  </th>
							<td colspan="3">								
								<span style="font-weight:bold;color:red;font-size:18px;">								
								※ (✔)는 반드시 관할 시.군.구청에서 발급받은 [영업신고증]의 정보이어야 합니다.								
								</span><br>
								<span style="font-weight:bold;color:blue;font-size:13px;">								
								1) 신규업체: 회사정보가 검색되지 않은 경우
								</span><br>
								<span style="font-weight:bold;font-size:12px;">								
								: 하단 [회사변경정보 or 신규등록정보]란 정보입력
								</span><br>
								<span style="font-weight:bold;color:blue;font-size:13px;">								
								2) 기존업체: 회사정보(회사명,대표자,회사주소 등)가 변경되었으나 반영되지 않은 경우
								</span><br>
								<span style="font-weight:bold;font-size:12px;">								
								 :  [선택]을 눌러 기존의 “회사명”을 선택 후 → 하단 [회사변경정보 or 신규등록정보]란 변경된 정보입력<br>
       								* 반드시 변경된 항목에만 입력 부탁드립니다.
								</span><br>
								
							</td>
						</tr>
						<tr>
							<th scope="row" class="on"><span style="font-weight:bold;color:red;">√</span> 회사명 <c:if test="${ input.mem_div == '1' }"><i class="fa fa-asterisk tred"></c:if><br><span>(영업소명칭)</span> </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="회사명" name='corp_nm' id='corp_nm' alt='회사명' />
								<a class="pbtn01 mini" id='btn_memSel'><span class="">선택</span></a>
								<span style="font-weight:bold;color:red;"> [선택] 버튼을 눌러 회사를 찾아서 선택하세요.</span>
							</td>
						</tr>
						<tr>
							<th scope="row" class="on"><span style="font-weight:bold;color:red;">√</span> 대표자 <c:if test="${ input.mem_div == '1' }"><i class="fa fa-asterisk tred"></c:if> </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="대표자" value="" name='corp_president' id='corp_president' alt='대표자'/>
							</td>
						</tr>
						<tr>
							<th scope="row" class="on"><span style="font-weight:bold;color:red;">√</span> 회사주소 <c:if test="${ input.mem_div == '1' }"><i class="fa fa-asterisk tred"></c:if> </th>
							<td>
								<input type="text" class="it long" readonly="readonly" title="회사주소" value="" name='addr_nm' id='addr_nm' alt='회사주소'/>
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">사업자등록번호 <c:if test="${ input.mem_div == '1' }"><i class="fa fa-asterisk tred"></c:if> </th>
							<td>
								<input type="text" class="it " maxlength="15" readonly="readonly" title="사업자등록번호" value="" name='corp_reg_no' id='corp_reg_no' alt='사업자등록번호'/>
								<!-- <span style="font-weight:bold;color:red;"> (*)찾는 회사가 없으면 하단 [회사 변경정보 or 신규 등록정보]란에 입력하세요.</span> -->
								
							</td>
						</tr>
						<tr>
							<th scope="row" class="">회사전화번호  </th>
							<td>
								<select style="width:80px;" name='corp_tel_no1' id='corp_tel_no1'>
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
								<input type="text" class="it sm" maxlength="4" title="회사전화번호" value="" name='corp_tel_no2' id='corp_tel_no2' />
								-
								<input type="text" class="it sm" maxlength="4" title="회사전화번호" value="" name='corp_tel_no3' id='corp_tel_no3' />
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">회사팩스번호  </th>
							<td>
								<select style="width:80px;" name='corp_fax_no1' id='corp_fax_no1'>
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
								<input type="text" class="it sm" maxlength="4" title="회사팩스번호" value="" name='corp_fax_no2' id='corp_fax_no2' />
								-
								<input type="text" class="it sm" maxlength="4" title="회사팩스번호" value="" name='corp_fax_no3' id='corp_fax_no3' />
							</td>
						</tr>
						<tr>
							<td colspan="3">
								
								<span style="font-weight:bold;font-size:18px;">
								※ <span style="font-weight:bold;color:blue;">신규업체</span>인 경우, 하단에 회사정보입력 후 다음을 클릭하세요.
								</span><br>
								<span style="font-weight:bold;font-size:14px;padding-left:20px;">
								 - 영업신고예정일 경우, <span style="font-weight:bold;color:blue;">상단의 필독사항⑶ 예시 참고하여 입력</span> 부탁드립니다.
								</span><br>
								<span style="font-weight:bold;font-size:18px;">
								※ <span style="font-weight:bold;color:blue;">회사정보가 변경</span>된 경우, 하단에 변경된 회사정보를 입력 후  다음을 클릭하세요.
								</span><br>
								<span style="font-weight:bold;font-size:14px;padding-left:20px;">
								- 반드시 <span style="font-weight:bold;color:blue;">변경된 항목에만 입력</span> 부탁드리며, <span style="font-weight:bold;color:blue;">정확한 정보에 한해서만 처리</span>됩니다.
								</span><br>
								<span style="font-weight:bold;font-size:18px;">
								※ 관할시.군.구청에 신규(변경)신고를 완료하신경우 더 빠른 업무처리를 위해 <span style="font-weight:bold;color:blue;">영업신고증을 
    									협회 팩스(02-6234-0376)로 보내주시면 신속히 처리</span>해 드리도록 하겠습니다.
								</span><br>
								<span style="font-weight:bold;font-size:18px;">
								※ 변경요청사항은 교육수료 완료 후 순차적으로 처리됩니다.v
								</span><br>
								<table cellpadding="0" cellspacing="0" class="" summary="" >
								<caption></caption>
								<colgroup>
									<col width="100px"/><col width=""/>
								</colgroup>
								<tbody>
								<tr>
									<th  colspan="2" >회사 정보변경 또는 신규업체 등록정보 입력
									</th>
								</tr>
								<tr>
										<th scope="row" class="on">관할 시.군.구청<br>신고여부 </th>
										<td>
											<input type="checkbox" class="check" name="re_corp_an_end" id="re_corp_an_end" value="Y">	신규(변경)신고 완료	<br>
											<input type="checkbox" class="check" name="re_corp_an_ing" id="re_corp_an_ing" value="Y">	신규(변경)신고 예정	<br>					
										</td>
										
									</tr>
									<tr>
										<th scope="row" class="on">회사명<br>(영업소명칭) </th>
										<td>
											<input type="text" class="it " title="변경회사명" name='re_corp_nm' id='re_corp_nm' alt='변경회사명' />								
										</td>
									</tr>
									<tr>
										<th scope="row" class="on">대표자 </th>
										<td>
											<input type="text" class="it " title="대표자" value="" name='re_corp_president' id='re_corp_president' alt='변경대표자'/>
										</td>
									</tr>
									<tr>
										<th scope="row" rowspan="2" class="on">회사주소  </th>
										<td>
											<input type="text" class="it " readonly="readonly" title="회사주소-우편번호" value="" name='re_corp_post' id='re_corp_post' alt='변경회사주소-우편번호'/>
											<a  class="pbtn01 mini" id='btn_postSel'><span class="">선택</span></a>
										</td>
									</tr>
									<tr>
										<td>
											<input type="text" class="it long" title="" value="" name='re_corp_addr' id='re_corp_addr' alt='회사주소' />
										</td>
									</tr>
									<tr>
										<th scope="row" class="on">사업자등록번호 </th>
										<td>
											<input type="text" class="it " maxlength="15" title="사업자등록번호" value="" name='re_corp_reg_no' id='re_corp_reg_no' alt='변경사업자등록번호'/>
										</td>
									</tr>
									<tr>
										<th scope="row" class="">회사전화번호  </th>
										<td>
											<select style="width:80px;" name='re_corp_tel_no1' id='re_corp_tel_no1'>
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
											<input type="text" class="it sm" maxlength="4" title="회사전화번호" value="" name='re_corp_tel_no2' id='re_corp_tel_no2' />
											-
											<input type="text" class="it sm" maxlength="4" title="회사전화번호" value="" name='re_corp_tel_no3' id='re_corp_tel_no3' />
										</td>
									</tr>
									<tr>
										<th scope="row" class="on">회사팩스번호  </th>
										<td>
											<select style="width:80px;" name='re_corp_fax_no1' id='re_corp_fax_no1'>
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
											<input type="text" class="it sm" maxlength="4" title="회사팩스번호" value="" name='re_corp_fax_no2' id='re_corp_fax_no2' />
											-
											<input type="text" class="it sm" maxlength="4" title="회사팩스번호" value="" name='re_corp_fax_no3' id='re_corp_fax_no3' />
										</td>
									</tr>
								</tbody>
								</table>
							</td>
						</tr>
						
						
						<tr>
							<th scope="row" class="" rowspan="5">영업장<br>정보  </th>
							<th scope="row" class="on">영업장명  </th>
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
							<th scope="row" class="on">영업장팩스번호  </th>
							<td>
								<select style="width:80px;" name='building_fax_no1' id='building_fax_no1'>
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
								<input type="text" class="it sm" maxlength="4" title="영업장팩스번호" value="" name='building_fax_no2' id='building_fax_no2' />
								-
								<input type="text" class="it sm" maxlength="4" title="영업장팩스번호" value="" name='building_fax_no3' id='building_fax_no3' />
							</td>
						</tr>
						<tr>
							<th scope="row" class="on" colspan="2">적요사항  </th>
							<td>
							<textarea name='remark' id='remark'  class="txt defaultText" cols="" rows="" ></textarea>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="btnWrap">
				<a href="#" class="pbtn02" id='btn_reg'><span class="">다음</span></a>
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