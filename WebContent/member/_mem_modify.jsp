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
	<script src="//t1.kakaocdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script>
		function openPostcode(div) {
			new kakao.Postcode({
				oncomplete: function(data) {
					var postcode = data.zonecode;
					var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
					if( div === 1)
					{
						lf_setAddrInfo(postcode, addr);
					}else if( div === 2)
					{
					}else if( div === 3)
					{
						lf_setBuildingInfo(postcode, addr);
					}else if( div === 4)
					{
						lf_setRequestInfo(postcode, addr);
					}
				}
			}).open();
		}
	</script>
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

<div class="pageTit">
		<h2><span class="tit_1">정보변경</span></h2>
	</div>
	<div id="content">

			<div class="directions">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<tbody>
						<tr>
							<th scope="row">필독사항</th>
							<td>

							<span style="font-weight:bold;color:red;">▶ 회사정보(회사명, 대표자, 회사주소)는  반드시  관할 시,군,구청에서 발급된 영업신고증상의 정보이어야 합니다.</span><br>
							<p style="margin-left:20px;font-weight:bold;color:blue;">
							*회사정보 변경된 경우 사업자등록증만 변경하고, 영업신고증은 변경신고 하지 않아 발생하는 불이익에 관하여 협회에 책임이 없음을 알려드립니다.
							</p>
							<span style="font-weight:bold;color:red;">▶ 대표자, 책임자의 개인아이디로 가입하므로 추후 회사정보 변경시 [교육생 성명 및 생년월일]은 변경이 불가합니다. </span><br><br>
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
							<th scope="row" class="" rowspan="8">교육생<br>정보  </th>
							<th scope="row" class="on">회원구분 <i class="fa fa-asterisk tred"> </th>
							<td>

								<c:choose><c:when test="${ output.mem_div == '1' }">책임자</c:when><c:otherwise>신규</c:otherwise></c:choose>
								<input type="hidden" class="it " title="회원구분" value='${output.mem_div }' alt='회원구분' name='mem_div' id='mem_div' />

							</td>
						</tr>
						<tr>
							<th scope="row" class="on">성 명 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="text" class="it " title="성 명" value='${output.user_nm }' alt='성명' name='user_nm' id='user_nm' readonly="readonly" required='true' />
							</td>
						</tr>
						<tr>
							<th scope="row" >성별 <i class="fa fa-asterisk tred"></th>
							<td>
								<input type="text" class="it " title="성별" value='<c:choose><c:when test="${ output.sex == '1' }">남</c:when><c:when test="${ output.sex == '2' }">여</c:when><c:otherwise></c:otherwise></c:choose>' alt='성별' name='sex_nm' id='sex_nm' readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<th scope="row" >생년월일 <i class="fa fa-asterisk tred"></th>
							<td>
								<input type="text" class="it " title="생년월일" value='${output.birth_yyyy}-${output.birth_mm}-${output.birth_dd}' alt='생년월일' name='birth' id='birth' readonly="readonly" />
							</td>
						</tr>
						<tr>
							<th scope="row" class="on" >이메일 <i class="fa fa-asterisk tred">  </th>
							<td>
								<input type="text" class="it sm2" title="이메일아이디" value="" name='email1' id='email1' readonly="readonly"/> @
								<input type="text" class="it sm2" title="이메일주소" value="" name='email2' id='email2' readonly="readonly"/>
								<!-- <select style="width:150px;" name='email3' id='email3' onchange="js_selectEmail();">
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
								</select> -->
							</td>
						</tr>
						<!--  <tr>
							<th scope="row" class="">무통장입금 시 입금자명 : </th>
							<td>
								<input type="text" class="it lag" title="무통장입금 시 입금자명" alt='무통장입금 시 입금자명' value="${output.account_nm }" name='account_nm' id='account_nm' />
							</td>
						</tr>  -->
						<tr>
							<th scope="row" class="on">핸드폰 <i class="fa fa-asterisk tred"> </th>
							<td>
								<select style="width:80px;" name='tel_no1' id='tel_no1'>
									<option value="010">010</option>
								</select>
								-
								<input type="text" class="it sm" maxlength="4" title="핸드폰" value="" name='tel_no2' id='tel_no2' required='true' />
								-
								<input type="text" class="it sm" maxlength="4" title="핸드폰" value="" name='tel_no3' id='tel_no3' required='true' />
							</td>
						</tr>
						<tr>
							<th scope="row" rowspan="2" class="on">자택주소 <i class="fa fa-asterisk tred"></th>
							<td>
								<input type="text" class="it " readonly="readonly" title="자택주소 우편번호" value="${output.zipcode }" name='zipcode' id='zipcode' alt='자택주소-우편번호' required='true'/>
								<a class="pbtn01 mini" id='btn_addrSel'><span class="">선택</span></a>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" class="it long" title="자택주소" value="${output.addr }" name='addr' id='addr' alt='자택주소' required='true'/>
							</td>
						</tr>

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
								: 하단 [회사변경정보 or 신규등록정보]란 정보입력 → [변경요청] 클릭
								</span><br>
								<span style="font-weight:bold;color:blue;font-size:13px;">
								2) 기존업체: 회사정보(회사명,대표자,회사주소 등)가 변경되었으나 반영되지 않은 경우
								</span><br>
								<span style="font-weight:bold;font-size:12px;">
								 :  [선택]을 눌러 기존의 “회사명”을 선택 후 → 하단 [회사변경정보 or 신규등록정보]란 변경된 정보입력 → [변경요청] 클릭<br>
       								* 반드시 변경된 항목에만 입력 부탁드립니다.
								</span><br>
							</td>
						</tr>

						<tr>
							<th scope="row" class="on"><span style="font-weight:bold;color:red;">√</span> 회사명 <c:if test="${ output.mem_div == '1' }"><i class="fa fa-asterisk tred"></c:if> </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="회사명" name='corp_nm' id='corp_nm' alt='회사명' value='${output.corp_nm }' />
								<!-- <a href="#" class="pbtn01 mini" id='btn_memSel'><span class="">선택</span></a> -->
							</td>
						</tr>
						<tr>
							<th scope="row" class="on"><span style="font-weight:bold;color:red;">√</span> 대표자 <c:if test="${ output.mem_div == '1' }"><i class="fa fa-asterisk tred"></c:if> </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="대표자" value="${output.rprstt_kr }" name='corp_president' id='corp_president' alt='대표자'/>
							</td>
						</tr>
						<tr>
							<th scope="row" class=""><span style="font-weight:bold;color:red;">√</span> 회사주소 <c:if test="${ output.mem_div == '1' }"><i class="fa fa-asterisk tred"></c:if> </th>
							<td>
								<input type="text" class="it long" readonly="readonly" title="회사주소" value="${output.addr_nm }" name='addr_nm' id='addr_nm' alt='회사주소'/>
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">사업자등록번호 <c:if test="${ output.mem_div == '1' }"><i class="fa fa-asterisk tred"></c:if> </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="사업자등록번호" value="${output.corp_reg_no }" name='corp_reg_no' id='corp_reg_no' alt='사업자등록번호'/>
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
								※ <span style="font-weight:bold;color:blue;">신규업체</span>인 경우, 하단의 회사정보입력 후 [변경요청]을 클릭하세요.
								</span><br>
								<span style="font-weight:bold;font-size:14px;padding-left:20px;">
								- 영업신고예정일 경우, <span style="font-weight:bold;color:blue;">상단의 필독사항⑶ 예시 참고하여 입력</span> 부탁드립니다.
								</span><br>
								<span style="font-weight:bold;font-size:18px;">
								※ <span style="font-weight:bold;color:blue;">회사정보가 변경</span>된 경우, 하단의 변경된 회사정보를 입력 후 [변경요청]을 클릭하세요.
								</span><br>
								<span style="font-weight:bold;font-size:14px;padding-left:20px;">
								- 반드시 <span style="font-weight:bold;color:blue;">변경된 항목에만 입력</span> 부탁드리며, <span style="font-weight:bold;color:blue;">정확한 정보에 한해서만 처리</span>됩니다.
								</span><br>
								<span style="font-weight:bold;font-size:18px;">
								※ <span style="font-weight:bold;color:blue;">관할시.군.구청에 신규(변경)신고를 완료</span>하신경우 더 빠른 업무처리를 위해 <span style="font-weight:bold;color:blue;">영업신고증을 협회 팩스(02-6234-0376)로 보내주시면 신속히 처리</span>해 드리도록 하겠습니다.<br>
								※ 변경요청사항은 교육수료 완료 후 순차적으로 처리됩니다.
								</span><br>
								<table cellpadding="0" cellspacing="0" class="" summary="" >
								<caption></caption>
								<colgroup>
									<col width="100px"/><col width=""/><col width="150px"/>
								</colgroup>
								<tbody>
								<tr>
									<th  colspan="3" style="background: #def1b9;">회사 정보변경 또는 신규업체 등록정보 입력
									</th>
								</tr>
								<tr>
										<th scope="row" class="on" style="background: #def1b9;">관할 시.군.구청<br>신고여부 </th>
										<td>
											<input type="checkbox" class="check" name="re_corp_an_end" id="re_corp_an_end" value="Y" <c:if test="${output.re_corp_an_end == 'Y'}">checked</c:if> >	신규(변경)신고 완료	<br>
											<input type="checkbox" class="check" name="re_corp_an_ing" id="re_corp_an_ing" value="Y" <c:if test="${output.re_corp_an_ing == 'Y'}">checked</c:if> >	신규(변경)신고 예정	<br>
										</td>

									</tr>
									<tr>
										<th scope="row" class="on" style="background: #def1b9;">회사명<br>(영업소명칭) </th>
										<td>
											<input type="text" class="it " title="회사명" value="${reqOutput.re_corp_nm}" name='re_corp_nm' id='re_corp_nm' alt='변경회사명' />
										</td>
										<td rowspan="7" id="tdReqBtn">
											<c:if test="${output.req_date == null or output.req_date == '' }">
												<div class="btnWrap">
													<a class="pbtn04" id='btn_mem_req'><span class="">변경요청</span></a>
												</div>
											</c:if>
											<c:if test="${fn:length(output.req_date) > 4 }">
												<p style="font-weight:bold;color:red;">${output.req_date} <br>정보변경 요청 처리중</p>
											</c:if>
										</td>
									</tr>
									<tr>
										<th scope="row" class="on" style="background: #def1b9;">대표자 </th>
										<td>
											<input type="text" class="it " title="대표자" value="${reqOutput.re_corp_president}" name='re_corp_president' id='re_corp_president' alt='변경대표자'/>
										</td>
									</tr>
									<tr>
										<th scope="row" rowspan="2" class="on" style="background: #def1b9;">회사주소  </th>
										<td>
											<input type="text" class="it " readonly="readonly" title="회사주소-우편번호" value="${reqOutput.re_corp_post}" name='re_corp_post' id='re_corp_post' alt='변경회사주소-우편번호'/>
											<a  class="pbtn01 mini" id='btn_reqSel'><span class="">선택</span></a>
										</td>
									</tr>
									<tr>
										<td>
											<input type="text" class="it long" title="" value="${reqOutput.re_corp_addr}" name='re_corp_addr' id='re_corp_addr' alt='변경회사주소' />
										</td>
									</tr>
									<tr>
										<th scope="row" class="on" style="background: #def1b9;">사업자등록번호 </th>
										<td>
											<input type="text" class="it " title="사업자등록번호" value="${reqOutput.re_corp_reg_no}" maxlength="15" name='re_corp_reg_no' id='re_corp_reg_no' alt='변경사업자등록번호'/>
										</td>
									</tr>
									<tr>
										<th scope="row" class="" style="background: #def1b9;">회사전화번호  </th>
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
										<th scope="row" class="on" style="background: #def1b9;">회사팩스번호  </th>
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
								<input type="text" class="it lag" title="영업장명" value="${output.building_nm }" name='building_nm' id='building_nm' alt='영업장명' />
								(* 영업장명 = 청소하는 현장)
							</td>
						</tr>
						<tr>
							<th scope="row" rowspan="2" class="">영업장주소  </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="영업장-우편번호" value="${output.building_zipcode }" name='building_zipcode' id='building_zipcode' alt='영업장-우편번호' />
								<a class="pbtn01 mini" id='btn_buildingSel'><span class="">선택</span></a>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" class="it long" title="영업장주소" value="${output.building_addr }" name='building_addr' id='building_addr' alt='영업장주소'  />
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
							<th scope="row" class="on" colspan="2">적요사항 <br>(비고란) </th>
							<td>
							<textarea name='remark' id='remark'  class="txt defaultText" cols="" rows="" >${output.remark }</textarea>
								<p class="tip tred"></p><br>


							</td>
						</tr>

					</tbody>
				</table>
			</div>

			<div class="btnWrap">
				<a class="pbtn02" id='btn_reg'><span class="">저장</span></a>
				<a class="pbtn01" id='btn_close'><span class="">닫기</span></a>
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
				location.href = '/index.jsp';
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