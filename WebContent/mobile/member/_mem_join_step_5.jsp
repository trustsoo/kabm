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

jQuery(document).ready(function() {
	jQuery('#btn_addrSel').bind("click", function(){
		lf_addrSelect();
	});
	
	jQuery('#btn_buildingSel').bind("click", function(){
		lf_buildingSelect();
	});
	
	jQuery('#btn_reg').bind("click", function(){
		lf_requestSubmit();
	});
});


function lf_nextSubmit()
{
	var url = '/mobile/member/join.jspx?cmd=join_step_6';
	jQuery('#frm').attr('action', url);
	jQuery('#frm').submit();
}

function lf_idConfirm()
{
	if(jQuery('#user_id').val() == '')
	{
		msgStart(msg_mem_code_001, 'warning');
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
					msgStart(msg_mem_code_003);
                    jQuery('#isConfirm').val('N');
				}
				else
				{
					msgStart(msg_mem_code_002);
                    jQuery('#isConfirm').val('Y');
                    jQuery('#confirmSSN').val(jQuery('#user_id').val());
				}
			} else
			{
				msgStart(msg_com_code_010);
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
	lf_nextSubmit();
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

function checkAlphaNum(param){
	  var str = param.value;
	  var i = str.length - 1;
	  var regType1 = /^[a-z0-9+]*$/;
	  if(regType1.test(param.value)){
		  return ;
	  }
	  param.value='';
	  param.focus();
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
					}
				}
			}).open();
		}
	</script>

</head>

<body>

<form name="frm" id="frm" method="post" action="">
<input type="hidden" name="isConfirm" id="isConfirm" value="N">
<input type="hidden" name="confirmSSN" id="confirmSSN" value="">

<input type="hidden" name="mem_div" id="mem_div" value="<%=mem_div%>">
<input type="hidden" name='sex' id='sex' value='${input.sex}'/>
<input type="hidden" name='birth_yyyy' id='birth_yyyy' value='${input.birth_yyyy}'/>
<input type="hidden" name='birth_mm' id='birth_mm' value='${input.birth_mm}'/>
<input type="hidden" name='birth_dd' id='birth_dd' value='${input.birth_dd}'/>
<input type="hidden" name='ssn' id='ssn' value='${input.ssn}'/>

<input type="hidden" name='tel_no' id='tel_no' value=''/>
<input type="hidden" name='email' id='email' value=''/>

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
			<a href="#step-66" class="disabled" isdone="0" rel="6">
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
			<h2 class="StepTitle">기본정보입력</h2>
			
			<div class="x_content">
				<label for="user_nm" class="">성명 <i class="fa fa-asterisk tred"></i></label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<input type="text" class="form-control " title="성명" value="${input.user_nm}" name='user_nm' id='user_nm' required='true' readonly="readonly"/>
						<% if( MEM_DIV_NEW.equals(mem_div)){ %> <span style="font-weight:bold;color:red;">*대표자 성명이 맞는지 확인하시기 바랍니다.</span><%} %>
					</div>
				</div>
				<label for="user_id" class="">아이디 <i class="fa fa-asterisk tred"></i></label>
				<div class="form-group row">
					<div class="col-md-12 col-sm-12">
						<div class="input-group">
							<input type="text" class="form-control " title="아이디" value="" name='user_id' id='user_id' required='true' onkeyup="checkAlphaNum(this)"/>
							<span class="input-group-btn">
							<a href="javascript:lf_idConfirm();" class="btn btn-default">중복확인</a>
							</span>
						</div>								
					</div>
					<div class="col-md-3 col-sm-3">
						<span style="font-weight:bold;color:red;">*영어 소문자와 숫자만 사용 가능합니다.</span>
					</div>
				</div>
				<label for="password" class="">비밀번호 <i class="fa fa-asterisk tred"></i></label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<input type="password" class="form-control " title="비밀번호" value="" name='password' id='password' required='true'/>
					</div>
				</div>
				<label for="pw_confirm" class="">비밀번호 확인 <i class="fa fa-asterisk tred"></i></label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<input type="password" class="form-control " title="비밀번호확인" value="" name='pw_confirm' id='pw_confirm' required='true'/>
					</div>
				</div>
				<% if(!"E".equals(cert_div)){ %>
				<label for="tel_no1" class="">핸드폰 <i class="fa fa-asterisk tred"></i></label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<input type="text" class="form-control " title="핸드폰" value="" name='tel_no1' id='tel_no1' required='true' value=""  readonly="readonly" />
								<% if( MEM_DIV_NEW.equals(mem_div)){ %> <span style="font-weight:bold;color:red;">*로그인 후 [정보변경]에서 변경 가능합니다.</span><%} %>
					</div>
				</div>
				<%} %>
				<% if("E".equals(cert_div)){ %>		
				<label for="tel_no1" class="">핸드폰 <i class="fa fa-asterisk tred"></i></label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<div class="input-group">
						<select class="form-control " style="width:80px;" name='tel_no1' id='tel_no1'>
							<option value="010">010</option>
							<option value="011">011</option>
							<option value="016">016</option>
							<option value="017">017</option>
							<option value="019">019</option>
						</select>
						<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="핸드폰" value="" name='tel_no2' id='tel_no2' required='true' />
						<input type="text" class="form-control " style="width:60px;margin-left:2px;" maxlength="4" title="핸드폰" value="" name='tel_no3' id='tel_no3' required='true' />
						</div>
					</div>	
				</div>
				<%} %>
				<label for="email1" class="">이메일 <i class="fa fa-asterisk tred"></i></label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<div class="input-group">
						<input type="text" class="form-control" title="이메일아이디" value="" name='email1' id='email1' required='true' />
						<span style="display: table-cell;vertical-align: middle;">@</span> 
						<input type="text" class="form-control" title="이메일주소" value="" name='email2' id='email2' required='true'/>
						</div>
						<select class="form-control" style="width:100%;" name='email3' id='email3' onchange="js_selectEmail();">
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
					</div>
				</div>
				<label for="zipcode" class="">자택주소 <i class="fa fa-asterisk tred"></i></label>
				<div class="form-group row">								
					<div class="col-md-12 col-sm-12">
						<div class="input-group">
							<input type="text" class="form-control " readonly="readonly" title="자택주소 우편번호" value="" name='zipcode' id='zipcode' alt='자택주소-우편번호' required='true'/>
							<span class="input-group-btn">
							<a href="#" class="btn btn-default" id='btn_addrSel'>선택</a>
							</span>
						</div>					
						<input type="text" class="form-control " title="자택주소" value="" name='addr' id='addr' alt='자택주소' required='true'/>
					</div>
				</div>
				
				<div class="actionBar">
					<a href="/mobile/login.jsp" class="btn btn-default">취소</a>
					<a href="javascript:lf_requestSubmit();" class="btn btn-success">다음</a>
				</div>	
			</div>			
		</div>
	</div>
	
</div>

</form>

<script type="text/javascript" class="source">

jQuery(document).ready(function() {
	
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
});
</script>
</body>
</html>