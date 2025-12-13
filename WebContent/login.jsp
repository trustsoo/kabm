<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/common/common.jsp"%>
<%

boolean isHttp = request.getScheme().equals("http");
String domain = request.getServerName();  // 도메인

if(isHttp) {
	response.sendRedirect("https://"+domain+"/login.jsp");
}


boolean isMobile = isMobile(request);

String _dummy = String.valueOf( Math.random() );	
String redirectUrl = request.getParameter("redirectUrl") == null ? "" : request.getParameter("redirectUrl");

if( isMobile ) response.sendRedirect("/mobile/login.jsp?redirectUrl="+redirectUrl);

%>
<link rel="stylesheet" type="text/css" href="/static/main/css/popup.css?_dummy=<%=_dummy %>" />
<style>

a.pbtn04					{display:inline-block; vertical-align:middle; color:#fff; height:32px; padding:4px; border:0; background:#48a3f0; cursor:pointer; }
a.pbtn04 span				{display:block; font-weight:bold; color:#fff; height:24px; padding:0 10px; font-size:12px; line-height:26px; background:#48a3f0; text-align:center;  }
a.pbtn05					{display:inline-block; vertical-align:middle; color:#fff; min-width:100px; height:30px; padding:4px; border:0; background:#575757; border-radius:7px; cursor:pointer;  }
a.pbtn05 span				{display:block; font-weight:bold; height:22px;  font-size:13px; line-height:22px; background:#575757; text-align:center; padding:0 10px; color:#fff; }

</style>
<script type="text/javascript">

		function checkForEnter(event)
		{
			if ((event.which && event.which == 13) || (event.keyCode && event.keyCode == 13))
    		{
				actionClick();
			}
		}

		function actionClick()
		{

			if(!checkFormField('#login_form'))
			{
				return;
			}

			var http = jQuery.ajax( {
		   		url: "/common/action/login.jspx",
		   		type: "POST",
				data : jQuery('#login_form').serialize(true),
		   		async : false,
		   		error 	: function(xml)
		   		{
					msgStart(msg_com_code_007);
				},
		   		success:function(xmlDoc)
		   		{
		   			var code = jQuery(xmlDoc).find('code').text();
			        var msg = jQuery(xmlDoc).find('msg').text();

			        if(code == '200')
			        {
			        	var status = jQuery(xmlDoc).find('status').text();
						var end_yn = jQuery(xmlDoc).find('end_yn').text();
						var close_yn = jQuery(xmlDoc).find('close_yn').text();
			        	var tmp_pass_yn = jQuery(xmlDoc).find('tmp_pass_yn').text();
			        	var user_nm = jQuery(xmlDoc).find('user_nm').text();
			        	var corp_nm = jQuery(xmlDoc).find('corp_nm').text();
			        	var corp_addr = jQuery(xmlDoc).find('corp_addr').text();
						
			        	if( close_yn == 'Y')
							alert(user_nm + "님 환영합니다.\n" + "폐업처리된 정보입니다. 협회에 문의하세요. Tel. 02)465-5900");
						else
							openCorpChangePop(user_nm , corp_nm, corp_addr, tmp_pass_yn);
						
						if( status == '09')
							alert('5회 불합격 하셨습니다. \n사무국에 역락하여 수강 재신청하기 바랍니다.\n전화. 02)465-5900');
						else if( end_yn == 'Y')
							alert('교육수강기간이(14일)이 경과 하였습니다. \n교육수강 메뉴에서 기간연장 하신 후 재수강하시기 바랍니다.');

					} else
					{
						msgOpen(msg);
					}
				}
			}
		);
		}

		function openCorpChangePop(user_nm , corp_nm, corp_addr, tmp_pass_yn)
		{
			if(tmp_pass_yn == 'Y'){
				location.href="/member/member.jspx?cmd=passModifyView";
        	}else{
        		if(getCookie('popup_layer_001' ) != 'N')
				{						
        			jQuery('#popUname').html(user_nm);
    				jQuery('#popCorpName').html(corp_nm);
    				jQuery('#popCorpAddr').html(corp_addr);
    				
					openLayerPop('popup_layer_001');
				}else{
					alert(user_nm + "님 환영합니다.");
					if(jQuery('#redirectUrl').val() == '')
		        	{
						location.href="/index.jsp";
		        	}else{
			        	location.href=jQuery('#redirectUrl').val();
			        }	
				}
	        }
		}
		
		function goUserChange()
		{
			location.href='/member/member.jspx?cmd=memModifyView';
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
		
		jQuery(document).ready(
				function()
				{

					if( jQuery('#user_id').val() == '' )
					{
						jQuery('#user_id').focus();
					}else{
						jQuery('#psswd').focus();
					}

				}
			)

	</script>
	
<div class="loginWrap">
	<div class="tit">
		<img src="/static/main/img/sub/login_tit.jpg"  alt="" />
	</div>
	<div class="loginForm">
	<form name="login_form" id="login_form" onSubmit="return false;" autocomplete="off">
	<input type='hidden' name='redirectUrl' id='redirectUrl' value='<%=redirectUrl%>'/>
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<tbody>
				<tr>
					<th scope="row">아이디</th>
					<td><input type="text" class="it " title="아이디" id='user_id' name='user_id' value="" required='true' tabindex="1" onKeyDown="checkForEnter(event);"/></td>
					<td rowspan="2">
						<a href="javascript:actionClick();" tabindex="3"><img src="/static/main/img/sub/login_btn_login.jpg"  alt="로그인" /></a>
					</td>
				</tr>
				<tr>
					<th scope="row">비밀번호</th>
					<td><input type="password" class="it " title="비밀번호" id='psswd' name="psswd" required='true' tabindex="2" onKeyDown="checkForEnter(event);"/></td>
				</tr>
			</tbody>
		</table>
	</form>	
	</div>
	<div class="loginBtn">
		<a href="/member/member.jspx?cmd=id_find" class="pbtn01"><span class="">아이디찾기</span></a>
		<a href="/member/member.jspx?cmd=passwd_confirm" class="pbtn01"><span class="">비밀번호찾기</span></a>
		<a href="/member/join.jspx?cmd=join_step_1" class="pbtn02" style="width:160px !important;"><span class="">회원가입</span></a>
	</div>
</div>
	

<div class="layerPopoup layerMainPop" id="popup_layer_001" style="min-height: 300px;display:none;">	
		<div class="dim"></div>
		<div class="popWrap" style="position: absolute;top: 100px;width: 500px;margin: 0 30%;">
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