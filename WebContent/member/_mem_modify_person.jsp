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
<input type="hidden" name="user_id" id="user_id" value="${output.user_id }">
<input type="hidden"  name="user_seq_no" id="user_seq_no" value="${output.user_seq_no}">
<input type="hidden" name="mem_div" id="mem_div" value="${output.mem_div}">
<input type="hidden" name="ill_no" id="ill_no" value="${output.ill_no }">
<input type="hidden" name='sex' id='sex' value='${output.sex}'/>
<input type="hidden" name='birth_yyyy' id='birth_yyyy' value='${output.birth_yyyy}'/>
<input type="hidden" name='birth_mm' id='birth_mm' value='${output.birth_mm}'/>
<input type="hidden" name='birth_dd' id='birth_dd' value='${output.birth_dd}'/>

<input type="hidden" name='email' id='email' value='${output.email}'/>
<input type="hidden" name='tel_no' id='tel_no' value='${output.tel_no}'/>

	<div class="pageTit">
		<h2><span class="tit_1">개인회원정보수정</span></h2>
	</div>
	<div id="content">
			
			<div class="view">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width="200"/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">성명 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="text" class="it " title="성명" value="${output.user_nm}" name='user_nm' id='user_nm' required='true' readonly="readonly"/>
								
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
							<th scope="row" rowspan="2" class="on">자택주소  </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="자택주소 우편번호" value="${output.zipcode }" name='zipcode' id='zipcode' alt='자택주소-우편번호'/>
								<a href="#" class="pbtn01 mini" id='btn_addrSel'><span class="">선택</span></a>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" class="it long" title="자택주소" value="${output.addr }" name='addr' id='addr' alt='자택주소' />
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
jQuery(document).ready(
		function()
		{
			jQuery('#btn_addrSel').bind("click", function(){
				lf_addrSelect();
			});
			
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