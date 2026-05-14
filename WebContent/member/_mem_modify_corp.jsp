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
<%@ include file="/common/common.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" class="source">

function lf_requestSubmit()
{

    if(!checkFormField('#frm'))
	{
		return false;
	}
	
	if( jQuery('#corp_nm').val() == '')
	{
		jQuery('#corp_nm').addClass('ui-state-error');
		msgStart(msg_com_code_016+jQuery('#corp_nm').attr('alt'), null);
		return false;
	}
	
	if( jQuery('#zipcode').val() == '' ){
		jQuery('#zipcode').addClass('ui-state-error');
   		jQuery('#zipcode').focus();
   		jQuery('#zipcode').select();
   		msgStart(msg_com_code_016+jQuery('#zipcode').attr('alt'), null);
		return false;
	}
	if( jQuery('#addr').val() == '' ){
		jQuery('#addr').addClass('ui-state-error');
   		jQuery('#addr').focus();
   		jQuery('#addr').select();
   		msgStart(msg_com_code_016+jQuery('#addr').attr('alt'), null);
		return false;
	}
	
	jQuery('#corp_tel_no').val( jQuery('#corp_tel_no1').val() + '-' + jQuery('#corp_tel_no2').val() + '-' + jQuery('#corp_tel_no3').val() );
	jQuery('#corp_fax_no').val( jQuery('#corp_fax_no1').val() + '-' + jQuery('#corp_fax_no2').val() + '-' + jQuery('#corp_fax_no3').val() );
	
	if( jQuery('#corp_tel_no1').val() == '' ||  jQuery('#corp_tel_no2').val() == '')
		jQuery('#corp_tel_no').val( '' );
	
	if( jQuery('#corp_fax_no1').val() == '' ||  jQuery('#corp_fax_no2').val() == '')
		jQuery('#corp_fax_no').val( '' );
	
	jQuery('#tel_no').val( jQuery('#tel_no1').val() + '-' + jQuery('#tel_no2').val() + '-' + jQuery('#tel_no3').val() );
	jQuery('#email').val( jQuery('#email1').val() + '@' + jQuery('#email2').val() );
	
	if( jQuery('#tel_no1').val() == '' ||  jQuery('#tel_no2').val() == '')
		jQuery('#tel_no').val( '' );
	
	if( jQuery('#email1').val() == '' ||  jQuery('#email2').val() == '')
		jQuery('#email').val( '' );
	
	lf_update();
}

function lf_update()
{
	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=doModify",
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
			} else
			{	
				msgStart(msg_com_code_007, 'danger');
			}
		}
  	});
	
}

function lf_addrSelect()
{
	openPostcode(1);
}

function lf_setAddrInfo(zipcode ,addr)
{
	jQuery('#zipcode').val(zipcode);
	jQuery('#addr').val(addr);
	jQuery('#zipcode').removeClass('ui-state-error');
	jQuery('#addr').removeClass('ui-state-error');
}

function js_selectEmail()
{
	jQuery('#email2').val( jQuery('#email3').val());	
}
function lf_corpSelect()
{
	var url = '/common/common.jspx?cmd=viewCorpList&view_div=modcorp';
	window.open(url, "corpPoP" , "width=700,height=530,toolbar=no,scroll=no,menubar=no");
}

function lf_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,addr_nm,mb_no)
{
	jQuery('#corp_reg_no').val(corp_reg_no);
	jQuery('#corp_nm').val(corp_nm);
	jQuery('#cp_code').val(cp_code);
	jQuery('#ill_no').val(ill_no);
	jQuery('#addr_nm').val(addr_nm);
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
			}
		}
	}).open();
}

</script>
</head>

<body>

<form name="frm" id="frm" method="post" action="">
<input type="hidden" name="mem_div" id="mem_div" value="${output.mem_div}">
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

	<div class="pageTit">
		<h2><span class="tit_1">법인회원정보수정</span></h2>
	</div>
	<div id="content">			
			<div class="view">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width=""/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">담당자성명 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="text" class="it " title="담당자성명" value="${output.user_nm}" name='user_nm' id='user_nm' required='true' readonly="readonly"/>
								
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">사업자등록번호 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="text" class="it " title="아이디" value="${output.user_id}" name='user_id' id='user_id' required='true' readonly="readonly"/>								
							</td>
						</tr>
						
						<tr>
							<th scope="row" class="on">핸드폰  <i class="fa fa-asterisk tred"></th>
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
							<th scope="row" class="on">이메일  <i class="fa fa-asterisk tred"></th>
							<td>
								<input type="text" class="it sm2" title="이메일아이디" value="" name='email1' id='email1' required='true' readonly="readonly"/> @
								<input type="text" class="it sm2" title="이메일주소" value="" name='email2' id='email2' required='true' readonly="readonly"/>
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
						<tr>
							<th scope="row" class="on">회사명 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="text" class="it "  title="회사명" name='corp_nm' id='corp_nm' alt='회사명' value="${output.corp_nm }"/>
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
								<input type="text" class="it sm" maxlength="4" title="회사전화번호" value="" name='corp_tel_no2' id='corp_tel_no2' required='true' />
								-
								<input type="text" class="it sm" maxlength="4" title="회사전화번호" value="" name='corp_tel_no3' id='corp_tel_no3' required='true' />
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
								<input type="text" class="it sm" maxlength="4" title="회사팩스번호" value="" name='corp_fax_no2' id='corp_fax_no2' required='true' />
								-
								<input type="text" class="it sm" maxlength="4" title="회사팩스번호" value="" name='corp_fax_no3' id='corp_fax_no3' required='true' />
							</td>
						</tr>
						<tr>
							<th scope="row" rowspan="2" class="on">회사주소  <i class="fa fa-asterisk tred"></th>
							<td>
								<input type="text" class="it " readonly="readonly" title="회사주소 우편번호" value="${output.zipcode }" name='zipcode' id='zipcode' alt='회사주소-우편번호'/>
								<a href="#" class="pbtn01 mini" id='btn_addrSel'><span class="">선택</span></a>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" class="it long" title="회사주소" value="${output.addr }" name='addr' id='addr' alt='회사주소' />
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			<div class="btnWrap">
				<a href="#" class="pbtn02" id='btn_reg'><span class="">저장</span></a>
			</div>
		</div>
</form>



<script type="text/javascript">
	jQuery(document).ready(function()	{

			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
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