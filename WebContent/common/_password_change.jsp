<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.security.*"%>
<%@ page import="java.security.spec.*"%>
<%
	KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
	generator.initialize(1024);
	
	KeyPair keyPair = generator.genKeyPair();
	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	
	PublicKey publicKey = keyPair.getPublic();
	PrivateKey privateKey = keyPair.getPrivate();
	
	// 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
	session.setAttribute("__rsaPrivateKey__", privateKey);
	
	// 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
	RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
	
	String publicKeyModulus = publicSpec.getModulus().toString(16);
	String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
	

%>
<link rel="stylesheet" href="/static/lib/bootstrap/css/jquery-ui-1.10.3.full.min.css" />
<script type="text/javascript" type="text/javascript">

	jQuery(document).ready(function(){
		//고객리스트조회 그리드 띄우기
		//jQuery('#pwchgModal').draggable();
		
		jQuery('#pw_chg_dialog').on("click", function(){
			initPasswordForm();
			pwchgModal_dialog();
		});	
		
		jQuery('#btnChgPw').on("click", function(){
			js_requestUpdatePw();
		});
		
		jQuery("#new_pw_chk").on("keydown", function(evt){
			if (evt.keyCode==13) js_requestUpdatePw();
		});	
		
		jQuery('#btnChgPwClose').on("click", function(){
			jQuery('#pwchgModal').modal('hide');
		});

	});
	
	function getEncryptedForm() {
		var old_password = jQuery('#cur_pw').val();
		var new_password = jQuery('#new_pw').val();
		var new_password_confirm = jQuery('#new_pw_chk').val();		
		var encryptedForm = {};

	    var rsa = new RSAKey();
	    try {
		    var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
		    var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;	    
	    
		    rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);
	
		    //비밀번호를 RSA로 암호화한다.
		    encryptedForm.secu_old_psswd = rsa.encrypt(old_password);
		    encryptedForm.secu_new_psswd = rsa.encrypt(new_password);
		    encryptedForm.secu_new_psswd_confirm = rsa.encrypt(new_password_confirm);		
		     	
	    } catch(err) {
	        console.log(err);
	        encryptedForm = null;
	    }
	    return encryptedForm;	    
	}	
	
	function initPasswordForm(){
		jQuery("#cur_pw").val('');
		jQuery("#new_pw").val('');
		jQuery("#new_pw_chk").val('');
		
	}
	
	function pwchgModal_dialog()
	{
		var pwchgModal_width = 400;
		jQuery('#pwchgModal').css('width', pwchgModal_width);
		jQuery('#pwchgModal').css('left', jQuery(window).width()/2 - pwchgModal_width/2);
		jQuery('#pwchgModal').css('position', 'absolute');

		jQuery('#pwchgModal').modal('show');
		
		return false;
	}
	
	function validatePw(pw){
		var chk = true;
		var check = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*])(?=.*[0-9]).{1,}$/;
		var patt_4num = /(\w)\1\1/; // 같은 영문자&숫자 연속 3번 정규식
		var patt_4num2 = /([\{\}\[\]\/?.,;:|*~`!^_@\#$%&\\\'\"])\1\1/; // 같은 특수문자 연속 4번 정규식
		var patt_cont = /(012)|(123)|(234)|(345)|(456)|(567)|(678)|(789)|(890)/; // 연속된 숫자 정규식
		
		if(!check.test(pw)){
	    	msgStart('새 비밀번호는 문자, 숫자, 특수문자(!@#$%^*)의 조합으로 입력해주세요.','warning');
			chk = false;
			return chk; 
		}
		
		/*
		if(patt_4num.test(pw)){
	    	msgStart('동일한 글자를 연속 3회 입력할 수 없습니다.='+pw,'warning');
			chk = false;
			return chk; 
		}
		if(patt_4num2.test(pw)){
	    	msgStart('동일한 특수글자를 연속 3회 입력할 수 없습니다.='+pw,'warning');
			chk = false;
			return chk; 
		}
		
		if(patt_cont.test(pw)){
	    	msgStart('연속된 숫자를 입력할 수 없습니다.='+pw,'warning');
			chk = false;
			return chk; 
		}
		for(var i=0;i < alpaBig.length - pw.length+1;i++){
	        if(alpaBig.substring(i,i+pw.length) == pwd)
	        {
	        	msgStart("ABCDEF처럼 연속된 문자는 사용할 수 가 없습니다.="+pw);
				chk = false;
				return chk; 
	        }
	    }
		for(var i=0;i < alpaSmall.length - pw.length+1;i++){
	        if(alpaBig.substring(i,i+pw.length) == pwd)
	        {
	        	msgStart("ABCDEF처럼 연속된 문자는 사용할 수 가 없습니다.="+pw);
				chk = false;
				return chk; 
	        }
	    }
		*/
		
		if(pw.length < 8){
			msgStart(msg_com_code_304+'[새 비밀번호]', 'warning');
			chk = false;
			return chk; 
		}
		
		return chk; 
	}
	
	function js_requestUpdatePw(){
		
		if( !validatePw( jQuery('#new_pw').val()) ) {
			jQuery('#new_pw').focus();
			return;
		}
		
		var _data = getEncryptedForm();

		if(!_data) {
			msgStart("비밀번호 암호화에 실패하였습니다", "danger", null, 300, 120);
			return;
		}
		
		var _url = '/common/action/password_change.jspx?cmd=updatePw';
		
		var http = jQuery.ajax({
	   		url: _url,	   		
	   		type: "POST",
			data : _data,
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(Json)
			{	
				//console.dir(Json);
				var code = Json.result.code;
		   		var msg =  Json.result.msg;
		   		var data = Json.result.data.check_info
		   		
		   		data = data.length == 0 ? data : data[0];
		   		
		   		console.log(data.result_msg);
		   		if(code == 200)
		   		{	   				
		   			//msgStart(msg_com_code_009);
		   			if(data.result_msg == "success"){
		   				msgStart(msg_com_code_301, 'success', null, 300, 120);	 
		   				jQuery('#btnChgPwClose').click();
		   			}else if(data.result_msg == "failure_1"){
		   				msgStart(msg_com_code_303, 'danger', null, 400, 120);
		   			}else if(data.result_msg == "failure_2"){
		   				msgStart(msg_com_code_302, 'danger', null, 400, 120);
		   			}else if(data.result_msg == "failure_3"){
		   				msgStart(msg_com_code_305, 'danger', null, 400, 120);
		   			}	   			
		   		} else
				{
		   			msgStart(msg_com_code_010+"("+msg+")", 'danger', null, 300, 120);		   			
				}
		   		jQuery('#btnChgPw').css('display','');
			}			
		});		
	}

</script>
<!-- script 태그에서 가져오는 자바스크립트 파일의 순서에 주의해야한다! 순서가 틀릴경우 자바스크립트 오류가 발생한다. -->
<script type="text/javascript" src="/static/lib/security/rsa/jsbn.js"></script>
<script type="text/javascript" src="/static/lib/security/rsa/rsa.js"></script>
<script type="text/javascript" src="/static/lib/security/rsa/prng4.js"></script>
<script type="text/javascript" src="/static/lib/security/rsa/rng.js"></script>

<form name="formPwChg" id="formPwChg" onSubmit="javascript:return false;">
<input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
<input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />
<div id="pwchgModal"  class="modal-content">	
	<div class="widget-box" style="display:;">
		<div class="widget-header widget-header-flat header-color-blue" id="pw_data_modal_hd">
			<h5 class="lighter"><b>비밀번호 변경</b></h5>
			
		</div>
		<div class="widget-body">
			<div class="widget-main" id="cust_data_div">
				<div class="row" style="margin-left:-12px;">
					<div class="col-xs-12">
						<div class="left">
							<div style="color:red;vertical-align:middle;font-size:11px;">특수문자(!@#$%^*), 숫자, 영문자를 각 한자리 이상씩 포함한 8자리 이상으로 넣어주세요.</div>
						</div>
						<div class="right">
						<table border-collapse="collapse" border="0" cellpadding="5" cellspacing="0">						
							<colgroup>
								<col width="390px">
							</colgroup>
							<tbody>
								<tr>
									<td>
										<label style="width:100px;font-size:12px;">현재 비밀번호</label>
										<input  name="cur_pw" id="cur_pw" type="password" required="true" style="width:200px"/>
									</td>
								</tr>
				
								<tr>
									<td>
										<label style="width:100px;font-size:12px;">새 비밀번호</label>	
										<input name="new_pw" id="new_pw" type="password" required="true" style="width:200px"/>
									</td>
								</tr>
				
								<tr>
									<td>
										<label style="width:100px;font-size:12px;">새 비밀번호 확인</label>									
										<input name="new_pw_chk" id="new_pw_chk" type="password" required="true" style="width:200px"/>
									</td>
								</tr>
							</tbody>
						</table>	
						</div>
					</div>
				</div>
			</div>
			
			<div class="widget-toolbox padding-8 clearfix" style="text-align:right;">				
				<span type="button" id="btnChgPw" class="btn btn-xs btn-primary">
					<i class="fa fa-save"></i><span class="bigger-110"> 비밀번호 변경</span>						 
				</span>
				<span type="button" id="btnChgPwClose" class="btn btn-xs btn-danger">
					<i class="fa fa-remove"></i>	<span class="bigger-110"> 닫기</span>
				</span>
			</div>
			
		</div>
	</div><!-- /divide_div -->
</div>		
	
	
	
</form>