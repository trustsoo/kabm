<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<%

boolean isHttp = request.getScheme().equals("http");
String domain = request.getServerName();  // 도메인

if(isHttp) {
	response.sendRedirect("https://"+domain+"/mobile/login.jsp");
}

	String _dummy = String.valueOf( Math.random() );
	boolean isAndroid = isAndroid(request);
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
	<link rel="stylesheet" href="/static/mobile/css/mobileDefault.css?_dummy=<%=_dummy %>" />

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
		var _url = '/mobile/action/login.jspx?cmd=getSecretKey';		
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
		var _url = '/mobile/action/login.jspx';		
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
		        	
		        	var close_yn = jQuery(xmlDoc).find('close_yn').text();
		        	var tmp_pass_yn = jQuery(xmlDoc).find('tmp_pass_yn').text();
		        	var user_nm = jQuery(xmlDoc).find('user_nm').text();
		        	var corp_nm = jQuery(xmlDoc).find('corp_nm').text();
		        	var corp_addr = jQuery(xmlDoc).find('corp_addr').text();
		        	
		        	if( close_yn == 'Y')
						alert(user_nm + "님 환영합니다.\n" + "폐업처리된 정보입니다. 협회에 문의하세요. Tel. 02)465-5900");
					else
						openCorpChangePop(user_nm , corp_nm, corp_addr, tmp_pass_yn);
					
		        } else
		        {
		        	msgStart(msg, 'warning');
		        }
		        
		        jQuery('#psswd').val('');
			}			
		});
	}
	
	function openCorpChangePop(user_nm , corp_nm, corp_addr, tmp_pass_yn)
	{
		if(tmp_pass_yn == 'Y'){
			location.href="/main/member.jspx?cmd=passModifyView";
    	}else{
    		if(getCookie('popup_layer_001' ) != 'N')
			{						
				jQuery('#popUname').html(user_nm);
				jQuery('#popCorpName').html(corp_nm);
				jQuery('#popCorpAddr').html(corp_addr);
				
				openLayerPop('popup_layer_001');
			}else{
				if(jQuery('#redirectUrl').val() == '')
	        	{
					location.href="/mobile/main.jspx";
	        	}else{
		        	location.href=jQuery('#redirectUrl').val();
		        }	
			}
        }
	}
	
	function goUserChange()
	{
		location.href='/mobile/member/member.jspx?cmd=memModifyView';
	}
	
	// layerPopup
    openLayerPop = function(target){
        $('#' + target).css('display', 'block');
        $('html,body').css('overflow','hidden');
    }

    closeLayerPopup = function(){
        $('.layerPopoup').css('display', 'none');
        $('html,body').css('overflow','');
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

      <div class="login_wrapper" style="width: 87%;">
        <div class="animate form" style="width:auto;">
          <section class="login_mobile">
            <form name="loginForm" id="loginForm" onSubmit="return false;" autocomplete="off">
			<input type='hidden' name='redirectUrl' id='redirectUrl' value='<%=redirectUrl%>'/>
			<input type='hidden' name='psswd' id='securedPassword' value=''/>
			<input type='hidden' name='login_div' id='login_div' value='EMP'/>
              <h1>온라인 교육 로그인</h1>

              <div>
                <input type="text" class="form-control" placeholder="Username" id='user_id' name='user_id' value="<%=cookie_user_id %>"/>
              </div>
              <div>
                <input type="password" class="form-control" id='plain_psswd' name="plain_psswd" placeholder="Password"/>
              </div>
              <div>
                <a class="btn btn-primary submit" style="width:100%" href="javascript:validateEncryptedForm();">로그인</a>
              </div>

              <div class="clearfix"></div>
              
              <div>
				<a class="btn btn-default " href="/mobile/member/member.jspx?cmd=id_find">아이디찾기</a>
				<a class="btn btn-default " href="/mobile/member/member.jspx?cmd=passwd_confirm">비밀번호찾기</a>
				<a class="btn btn-primary " href="/mobile/member/join.jspx">회원가입</a>
			   </div>

              <div class="separator">
                
                <div class="clearfix"></div>
                <br />

                <div>
                  <h1><img src="/static/main/img/common/logo2.jpg" alt="사단법인 한국건물위생관리협회"></h1>
                  <p>©2020 All Rights Reserved.</p>
                </div>
              </div>
            </form>
          </section>
        </div>

      </div>
    </div>
    
    
    <div class="layerPopoup layerMainPop" id="popup_layer_001" style="min-height: 300px;display:none;">	
		<div class="dim"></div>
		<div class="popWrap" style="position: absolute;top: 10px;width: 100%;margin: 0 auto;">
			<div class="contents">
				<div class="header" style="width: 100%;">
					업체정보 확인
				</div>
	            <div class="mainPop" style="font-weight: bold;padding: 15px;">
	            
						<table class="table table-striped jambo_table bulk_action">
							<colgroup>
								<col width="90px"/><col width=""/>
							</colgroup>
							<tbody>
								<tr class="even pointer">
									<th class="a-center">성함(교육생)</th>
									<td class=" last" id="popUname"></td>
								</tr>
								<tr class="even pointer">
									<th class="a-center">회사명</th>
									<td class=" last" id="popCorpName"></td>
								</tr>
								<tr class="even pointer">
									<th class="a-center">회사주소</th>
									<td class=" last" id="popCorpAddr"></td>
								</tr>
							</tbody>
						</table>
						
						반드시 교육생 및 회사정보가 맞는지 확인바랍니다.<br>
						정보가 맞는 경우 맨 아래 닫기를 누르시고, 회사정보가 변경된 경우 변경내용 입력 후 변경요청을 클릭하세요.<br> 
						<span style="color:red;">※ 교육생은 변경불가 (새로 가입해야 함)</span><br>
						<span style="color:red;">※ 교육생(대표자or책임자)이 여러 개 회사를 가지고있는 경우 개별 가입 해야 함</span><br>
			            
	        	</div>
	        	<span class="pull-right" style="padding: 0 0;">		
					<a class="pbtn05" onclick="goUserChange();"><span class="">확인</span></a>
				</span>
	            <div style="width: 200px; height:22px; padding:5px; bottom:0px; text-align: 1">
					<a href="#" onclick="setCookie('popup_layer_001','N',1) ; goUserChange();"><img src="/static/com/img/checkbox.jpg" style="vertical-align: -6px;width:21px;"> 오늘하루 그만보기</a>
				</div>
	        </div>	
			<!-- <div class="btnWrap">
	            <a href="javascript:;" style="" class="btnClose" onclick="closeLayerPopup();">닫기</a>
	        </div> -->
		</div>
	</div>	
	
    <!-- 맨 아래 있어야 함  -->
<script type="text/javascript" src="/static/com/js/common.js"></script>

  </body>
  
</html>
