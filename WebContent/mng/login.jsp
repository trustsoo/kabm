<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String _dummy = String.valueOf( Math.random() );
%>
<%
	String redirectUrl = request.getParameter("redirectUrl") == null ? "" : request.getParameter("redirectUrl");
	
    //설정한 쿠키값 가져오기
	int cookie_expired = 0;
	String cookie_user_id = "";
	try
	{
	    cookie_expired  = Integer.parseInt(com.kabm.util.SitePropertyManager.getString("COOKIE_USER_ID_EXPIRED_DAY")) * 60*60*24 * 1000 ;
	}catch(Exception e){}
    
	try
	{
		String cookieValues = "";
		String cookieName = "";
		cookie_user_id = "";
		Cookie[] cookies = request.getCookies();
		String ckey = com.kabm.util.SitePropertyManager.getString("COOKIE_USER_ID_KEY");;
		
		for(Cookie cookie : cookies)
		{
		    cookieName = cookie.getName();
		    
			if(ckey.equals(cookieName))
			{
				byte[] decodeCookie = jdf.framework.core.util.encrypt.CipherUtil.stringToHex(cookie.getValue());
				cookieValues = new String(jdf.framework.core.util.encrypt.CipherUtil.decode(ckey.getBytes(),  decodeCookie));
				break;
			}
		}
		
		if(!cookieValues.equals("")) 
		{
		    cookie_user_id = cookieValues.split("\\^")[1].trim(); 
		}		
		
	} catch(Exception ex){}
	
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8"/>
	<META HTTP-EQUIV="Expires" CONTENT="-1">
	<META HTTP-EQUIV="pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
	<title>KABM</title>
	
    <!--inline styles related to this page-->
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="VOC CARE SYSTEM" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0 " />
	
	<!--basic styles-->
	
	<link href="/static/lib/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" />
	<link href="/static/lib/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet" />
	<link href="/static/lib/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="/static/lib/iCheck/skins/flat/green.css" />
	<link rel="stylesheet" href="/static/lib/animate.css/animate.min.css" />
	<link rel="stylesheet" href="/static/lib/nprogress/nprogress.css" />
	<!-- PNotify -->
    <link href="/static/lib/pnotify/dist/pnotify.css" rel="stylesheet">
    <link href="/static/lib/pnotify/dist/pnotify.buttons.css" rel="stylesheet">
    <link href="/static/lib/pnotify/dist/pnotify.nonblock.css" rel="stylesheet">
    
	<!-- page specific plugin styles -->
	<link rel="stylesheet" href="/static/lib/bootstrap-daterangepicker/daterangepicker.css" />

	
	<!-- page styles -->
	<link rel="stylesheet" href="/static/com/css/templetDefault.css?_dummy=<%=_dummy %>" />

	<script type="text/javascript">
		window.jQuery || document.write("<script src='/static/lib/jquery/dist/jquery.min.js'>"+"<"+"/script>");
	</script>
	
	<script type="text/javascript" src="/static/lib/bootstrap/dist/js/bootstrap.min.js"></script>
	
	<!-- PNotify -->
    <script src="/static/lib/pnotify/dist/pnotify.js"></script>
    <script src="/static/lib/pnotify/dist/pnotify.buttons.js"></script>
    <script src="/static/lib/pnotify/dist/pnotify.nonblock.js"></script>
    <!-- FastClick -->
    <script src="/static/lib/fastclick/lib/fastclick.js"></script>
    <!-- NProgress -->
    <script src="/static/lib/nprogress/nprogress.js"></script>
    <!-- iCheck -->
    <script src="/static/lib/iCheck/icheck.min.js"></script>
	
	<script type="text/javascript" src="/static/com/js/notification.js"></script>
	
	
	<!-- script 태그에서 가져오는 자바스크립트 파일의 순서에 주의해야한다! 순서가 틀릴경우 자바스크립트 오류가 발생한다. -->
    <script type="text/javascript" src="/static/lib/security/rsa/jsbn.js"></script>
    <script type="text/javascript" src="/static/lib/security/rsa/rsa.js"></script>
    <script type="text/javascript" src="/static/lib/security/rsa/prng4.js"></script>
    <script type="text/javascript" src="/static/lib/security/rsa/rng.js"></script>	
	
	<script type="text/javascript">
	
	function js_getSecretInfo()
	{
		var secretInfo = [];
		var _url = '/mng/action/login.jspx?cmd=getSecretKey';		
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "GET",					
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(xmlDoc)
			{		
				var code = jQuery(xmlDoc).find('code').text();
		        var msg = jQuery(xmlDoc).find('msg').text();
		        if(code == '200')
		        {
		        	
		        	var publicKeyModulus = jQuery(xmlDoc).find('modulus').text();
		        	var publicKeyExponent = jQuery(xmlDoc).find('exponent').text();
		        	secretInfo.push(publicKeyModulus);
		        	secretInfo.push(publicKeyExponent);
		        } else
		        {
		        	msgStart('로그인 암호화 정보 취득중 오류가 발생하였습니다.:'+msg, 'warning');
		        }
			}
		});
		return secretInfo;
	}
	
	
	function validateEncryptedForm() {
	    var username = jQuery("#user_id").val();
	    var psswd = jQuery("#plain_psswd").val();
	    
	    if (!username || !psswd) {
	        alert("ID/비밀번호를 입력해주세요.");
	        return;
	    }

	    try {
	    	
	    	
	    	var secretInfo = js_getSecretInfo();
	    	
	    	
	    	
	        var rsaPublicKeyModulus = secretInfo[0];
	        var rsaPublicKeyExponent = secretInfo[1];
	        submitEncryptedForm(username, psswd, rsaPublicKeyModulus, rsaPublicKeyExponent);
	    } catch(err) {
	        console.log(err);
	    }
	    return;
	}

	function submitEncryptedForm(username, psswd, rsaPublicKeyModulus, rsaPpublicKeyExponent) {
	    var rsa = new RSAKey();
	    rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);

	    // 사용자ID와 비밀번호를 RSA로 암호화한다.
	    var securedUsername = rsa.encrypt(username);
	    var securedPassword = rsa.encrypt(psswd);

	    // POST 로그인 폼에 값을 설정하고 발행(submit) 한다.
	    jQuery('#securedPassword').val(securedPassword);
	    
	    js_Login();
	}
	
	function js_Login()
	{		
		var _url = '/mng/action/login.jspx';		
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : { 
					user_id : jQuery('#user_id').val(),
					psswd:jQuery('#securedPassword').val(),					
					redirectUrl : jQuery('#redirectUrl').val(),
					cid : jQuery('#redirectUrl').val()
			},		
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
	   			msgStart(msg_com_code_007);
			},
			success: function(xmlDoc)
			{				
				var code = jQuery(xmlDoc).find('code').text();
		        var msg = jQuery(xmlDoc).find('msg').text();
		        
		        if(code == '200')
		        {
		        	if( jQuery(xmlDoc).find('exist_duplicate').text() == 'Y')
		        	{
		        		alert(msg_mng_code_104);
		        	}
		        	
		        	
		        	if(jQuery(xmlDoc).find('is_init_psswd').text() == 'Y')
		        	{
		        		alert(msg_mng_code_108);
		        	} else
		        	{
		        		if(jQuery(xmlDoc).find('is_noti_psswd_upd_expire').text() == 'Y')
			        	{
			        		alert(msg_mng_code_105);
			        	}
		        	}
		        	
		        	if(jQuery(xmlDoc).find('is_exist_next_work_schedule').text() == 'N')
		        	{
		        		alert(msg_mng_code_202);
		        	}
		        	
		        	if(jQuery('#redirectUrl').val() == '')
		        	{
		        		var ckeyVal = jQuery(xmlDoc).find('ckey').text();
		        		var k = ckeyVal.split("#")[0];
		        		if(jQuery('#cid').prop("checked"))
		        		{
			        		var v = ckeyVal.split("#")[1];
			        		setCookie(k,v, (new Date((new Date()).getTime() + <%=cookie_expired%>) )  );
		        		}
		        		else
		        		{
		        			var k = ckeyVal.split("#")[0];
			        		setCookie(k,"", (new Date((new Date()).getTime() -1) )  );
		        		}
		        		
		        		location.href="/mng/main.jspx";
		        	}
		        	else
		        	{
		        		location.href=jQuery('#redirectUrl').val();
		        	}
		        	
		        } else
		        {
		        	msgStart(msg, 'warning');
		        }
		        
		        jQuery('#psswd').val('');
			}			
		});
	}
	
	jQuery(document).ready(function(){
		
		jQuery('#plain_psswd').keydown(function(evt){
			if (evt.keyCode==13) {
				//js_Login();
				validateEncryptedForm();
				/* 
					Fix the bug that closes, immediately, an login-error message box 
					after a user press the enter key(keyCode 13) at the psswd input.
					(so , in this case ,you cannot see the alert message)
				*/	
				evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
			}
			
			
		});
		
		jQuery('.position-relative').css('padding-top', jQuery(window).height()*1.2/3);
	});

	function setCookie(name, value, expire) {
        document.cookie = name + "=" + escape(value)
        + ( (expire) ? "; expires=" + expire.toGMTString() : "")
	}
	
	</script>
</head>
<body class="login">
    <div>
      <a class="hiddenanchor" id="signup"></a>
      <a class="hiddenanchor" id="signin"></a>

      <div class="login_wrapper">
        <div class="animate form login_form">
          <section class="login_content">
            <form name="loginForm" id="loginForm" onSubmit="return false;" autocomplete="off">
			<input type='hidden' name='redirectUrl' id='redirectUrl' value='<%=redirectUrl%>'/>
			<input type='hidden' name='psswd' id='securedPassword' value=''/>
			<input type='hidden' name='login_div' id='login_div' value='EMP'/>
              <h1>관리자 로그인</h1>
              <div>
                <input type="text" class="form-control" placeholder="Username" id='user_id' name='user_id' value="<%=cookie_user_id %>"/>
              </div>
              <div>
                <input type="password" class="form-control" id='plain_psswd' name="plain_psswd" placeholder="Password"/>
              </div>
              <div>
                <a class="btn btn-default submit" href="javascript:validateEncryptedForm();">로그인</a>
                
              </div>

              <div class="clearfix"></div>

              <div class="separator">
                
                <div class="clearfix"></div>
                <br />

                <div>
                  <h1><img src="/static/main/img/common/logo2.jpg" alt="사단법인 한국건물위생관리협회"></h1>
                  <p>©2017 All Rights Reserved.</p>
                </div>
              </div>
            </form>
          </section>
        </div>

      </div>
    </div>
    
    <!-- 맨 아래 있어야 함  -->
<script type="text/javascript" src="/static/com/js/common.js"></script>

  </body>
  
</html>
