/**
 * NICE 인증 관련 스크립트 파일
 * 	- 추가 IPIN 인증
 */
window.name ="Parent_window";
$(document).ready(function(){
	//cf_makeIpinPopupCallForm();
	//cf_makeIpinResultForm();
});


/**
 * 업체 정보 암호화 데이터 생성
 * @param rUrl - SMS 인증 후 리턴 받을 URL
 * @param errUrl - SMS 인증 에러 리턴 받을 URL
 * @param param_r1 - 리턴받을 파라미터 값1
 * @param param_r2 - 리턴받을 파라미터 값2
 * @param param_r3 - 리턴받을 파라미터 값3
 */
function cf_certifySms (rUrl, errUrl,  param_r1, param_r2, param_r3) {
	if(param_r1) document.form_sms.param_r1.value = param_r1;
	if(param_r2) document.form_sms.param_r2.value = param_r2;
	if(param_r3) document.form_sms.param_r3.value = param_r3;

	var requestUrl = '/member/smsCert/action/smsCert.jspx?cmd=requestSmsCertify';
	
	$.ajax({
		url : requestUrl,
		type : 'POST',
		dataType : 'json',
		data : {
			"rtn_url" : rUrl,
			"err_url" : errUrl
		},
		success:function(data){
			cf_certifySmsCall(data);
			
		},
		error:function() {
			alert("서버와의 통신중 오류가 발생했습니다.\n잠시 후 다시 이용해 주십시요.");
		}
	});
}

/**
 * 업체 정보 암호화 데이터 수신 및 아이핀 인증창 호출
 * 함수내 주석은 팝업 호출용에서 바닥페이지로 전환으로 바꾸기 위해 주석처리
 * 팝업용으로 사용하기 위해서는 함수내 주석 window.open , target 두개 라인 주석 해제 및
 * niceReceive.jsp 의 onload 함수 변경 해줘야 함.
 * @param responseText
 */
function cf_certifySmsCall(responseText) {
	var jsonData = responseText;
	if(jsonData.iRtn == 0) {
		window.open('', 'popupSMS', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
		document.form_sms.EncodeData.value = jsonData.sEncData;
		document.form_sms.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
		document.form_sms.target = "popupSMS";
		document.form_sms.submit();
		
	} else {
		alert("실명 인증 준비중 오류가 발생 하였습니다.\n관리자에게 문의 하세요.\n" + jsonData.iRtn + " : " + jsonData.sRtnMsg);
	}
}