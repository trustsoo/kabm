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
	String cert_div = input.getText("cert_div");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" class="source">

var ssn = '${input.ssn}';
var sex = '${input.sex}';

function lf_nextSubmit()
{
	var url = '/member/signUp.jspx?cmd=join_step_7';
	jQuery('#frm').attr('action', url);
	jQuery('#frm').submit();
}

function lf_idConfirm()
{
	if(jQuery('#user_id').val() == '')
	{
		msgOpen(msg_mem_code_001, 'warning');
        jQuery('#isConfirm').val('N')
		return;
	}

	var http = jQuery.ajax( {
		url: "/member/action/memberAction.jspx?cmd=idConfirm",
   		type: "POST",
		data : jQuery('#frm').serialize(true),
		async : false,
		datatype: 'json',
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_010);
		},
   		success:function(json)
   		{
   			var code = json.result.code;
	   		var msg =  json.result.msg;
	   		var data = json.result.data;
	   		
	   		if(code == '200')
			{
				var user_id = data.user_id;

				if( user_id != '' )
				{
					msgOpen(msg_mem_code_003);
                    jQuery('#isConfirm').val('N');
				}
				else
				{
					msgOpen(msg_mem_code_002);
                    jQuery('#isConfirm').val('Y');
                    jQuery('#confirmSSN').val(jQuery('#user_id').val());
				}
			} else
			{
				msgOpen(msg_com_code_010);
                jQuery('#isConfirm').val('N')
			}
		}
  	});

}


function lf_requestSubmit()
{

    if(!checkFormField('#frm'))
	{
		return false;
	}

    if( jQuery('#isConfirm').val() == 'N' || jQuery('#user_id').val() !=  jQuery('#confirmSSN').val())
    {
        msgStart(msg_mem_code_006,'warning');
		return;
    }
    
	if( jQuery('#password').val() != jQuery('#pw_confirm').val() )
	{
		msgStart(msg_mem_code_004);
		return;
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
	
	var yyyy = Number(jQuery('#birth_yyyy').val());
	if(yyyy >= 2000 && sex == '1' ) sex = '3';
	if(yyyy >= 2000 && sex == '2' ) sex = '4';
	
	var ressn = ssn + '' + sex +  '******';
	jQuery('#ssn').val(ressn);
	
	jQuery('#email').val( jQuery('#email1').val() + '@' + jQuery('#email2').val() );
	
	if( jQuery('#email1').val() == '' ||  jQuery('#email2').val() == '')
		jQuery('#email').val( '' );
	
	<% if("E".equals(cert_div)){ %>
	jQuery('#tel_no').val( jQuery('#tel_no1').val() + '-' + jQuery('#tel_no2').val() + '-' + jQuery('#tel_no3').val() );
	
	if( jQuery('#tel_no1').val() == '' ||  jQuery('#tel_no2').val() == '')
		jQuery('#tel_no').val( '' );
	
	<% }%>
	
	lf_create();
}

function lf_create()
{
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
	var url = '/common/common.jspx?cmd=viewCorpList&view_div=sign';
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

<input type="hidden" name='sex' id='sex' value='${input.sex}'/>
<input type="hidden" name='birth_yyyy' id='birth_yyyy' value='${input.birth_yyyy}'/>
<input type="hidden" name='birth_mm' id='birth_mm' value='${input.birth_mm}'/>
<input type="hidden" name='birth_dd' id='birth_dd' value='${input.birth_dd}'/>
<input type="hidden" name='ssn' id='ssn' value='${input.ssn}'/>

<input type="hidden" name='corp_tel_no' id='corp_tel_no' value=''/>
<input type="hidden" name='corp_fax_no' id='corp_fax_no' value=''/>
<input type="hidden" name="corp_reg_no" id="corp_reg_no" value="">
<input type="hidden" name="addr_nm" id="addr_nm" value="">

<input type="hidden" name="isConfirm" id="isConfirm" value="N">
<input type="hidden" name="confirmSSN" id="confirmSSN" value="">

<input type="hidden" name='tel_no' id='tel_no' value=''/>
<input type="hidden" name='email' id='email' value=''/>

	<div class="pageTit">
		<h2><span class="tit_1">회원가입</span></h2>
	</div>
	<div id="content">
			<ul class="joinTab">				
			<% if( MEM_DIV_PESN.equals(mem_div)){ %>		
			<!-- 개인회원 스탭 -->
				<li><img src="/static/main/img/sub/join_newtab_1_2.jpg"  alt="개인회원" /></li>
				<li class="on"><img src="/static/main/img/sub/join_tab_2_1_off.jpg"  alt="약관동의" /></li>
			<%}%>
			<% if( MEM_DIV_CORP.equals(mem_div)){ %>
			<!-- 법인회원  스탭 -->
				<li><img src="/static/main/img/sub/join_newtab_1_3_1.jpg"  alt="법인회원" /></li>
				<li class="on"><img src="/static/main/img/sub/join_tab_2_2_off.jpg"  alt="약관동의" /></li>
			<%}%>
				<li class=""><img src="/static/main/img/sub/join_tab_4_off.jpg"  alt="본인확인" /></li>
				<li class="on"><img src="/static/main/img/sub/join_tab_5_on.jpg"  alt="정보입력" /></li>
				<li class=""><img src="/static/main/img/sub/join_tab_7_off.jpg"  alt="등록확인" /></li>
			</ul>
			

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
								<input type="text" class="it " title="담당자성명" value="${input.user_nm}" name='user_nm' id='user_nm' required='true' readonly="readonly"/>
								
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">사업자등록번호 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="text" class="it " title="아이디" value="" name='user_id' id='user_id' required='true'/>
								<a href="javascript:lf_idConfirm();" class="pbtn03 mini"><span class="">ID 중복확인</span></a>
								<span style="color:red;font-weight:bold;">*)로그인 시 아이디로 사용됩니다.</span>
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">비밀번호 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="password" class="it " title="비밀번호" value="" name='password' id='password' required='true'/>
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">비밀번호 확인 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="password" class="it " title="비밀번호확인" value="" name='pw_confirm' id='pw_confirm' required='true'/>
							</td>
						</tr>
						
						<% if(!"E".equals(cert_div)){ %>
						<tr>
							<th scope="row" class="on">핸드폰 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="text" class="it lag" title="핸드폰" value="" name='tel_no1' id='tel_no1' required='true' value=""  readonly="readonly" />
							</td>
						</tr>
						<%} %>
						<% if("E".equals(cert_div)){ %>
						<tr>
							<th scope="row" class="on">핸드폰  <i class="fa fa-asterisk tred"> </th>
							<td>
								<select style="width:80px;" name='tel_no1' id='tel_no1'>
									<option value="010">010</option>
									<option value="011">011</option>
									<option value="016">016</option>
									<option value="017">017</option>
									<option value="019">019</option>
								</select>
								-
								<input type="text" class="it sm" maxlength="4" title="핸드폰" value="" name='tel_no2' id='tel_no2' required='true' />
								-
								<input type="text" class="it sm" maxlength="4" title="핸드폰" value="" name='tel_no3' id='tel_no3' required='true' />
							</td>
						</tr>
						<%} %>
						<tr>
							<th scope="row" class="on">이메일  <i class="fa fa-asterisk tred"></th>
							<td>
								<input type="text" class="it sm2" title="이메일아이디" value="" name='email1' id='email1' required='true' /> @
								<input type="text" class="it sm2" title="이메일주소" value="" name='email2' id='email2' required='true' />
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
							<th scope="row" class="on">회사명 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="text" class="it "  title="회사명" name='corp_nm' id='corp_nm' alt='회사명' />
								<a href="#" class="pbtn01 mini" id='btn_memSel'><span class="">선택</span></a>
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
								<input type="text" class="it sm" maxlength="4" title="회사전화번호" value="" name='corp_tel_no2' id='corp_tel_no2'  />
								-
								<input type="text" class="it sm" maxlength="4" title="회사전화번호" value="" name='corp_tel_no3' id='corp_tel_no3'  />
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
								<input type="text" class="it sm" maxlength="4" title="회사팩스번호" value="" name='corp_fax_no2' id='corp_fax_no2'  />
								-
								<input type="text" class="it sm" maxlength="4" title="회사팩스번호" value="" name='corp_fax_no3' id='corp_fax_no3'  />
							</td>
						</tr>
						<tr>
							<th scope="row" rowspan="2" class="on">회사주소  <i class="fa fa-asterisk tred"></th>
							<td>
								<input type="text" class="it " readonly="readonly" title="회사주소 우편번호" value="" name='zipcode' id='zipcode' alt='회사주소-우편번호'/>
								<a href="#" class="pbtn01 mini" id='btn_addrSel'><span class="">선택</span></a>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" class="it long" title="회사주소" value="" name='addr' id='addr' alt='회사주소' />
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
			
			jQuery('#btn_addrSel').bind("click", function(){
				lf_addrSelect();
			});
		
			jQuery('#btn_memSel').bind("click", function(){
				lf_corpSelect();
			});
			
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
			});
			
			
			<% if(!"E".equals(cert_div)){ %>
			jQuery('#tel_no').val(formattedTelnoHipen('${input.tel_no}'));
			jQuery('#tel_no1').val( jQuery('#tel_no').val() );
		<% } %>
		
		<% if("E".equals(cert_div)){ %>
			var email = '${input.email }';
			var emailArr = email.split('@');
			if( emailArr.length == 2 )
			{
				jQuery('#email1').val(emailArr[0]);
				jQuery('#email2').val(emailArr[1]);
			}
		<% } %>
		
			
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