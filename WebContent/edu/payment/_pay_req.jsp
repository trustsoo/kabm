<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ page import="java.io.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="lgdacom.XPayClient.XPayClient"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp"%>
<%@ include file="/edu/payment/paycommon.jsp"%>
<%
String lecture_seq_no = input.getText("lecture_seq_no");
String user_seq_no = input.getText("user_seq_no");
String amount = input.getText("amount");
String user_id = input.getText("user_id");
String user_nm = input.getText("user_nm");
String product_nm = input.getText("product_nm");
String email = input.getText("email");
String tel_no = input.getText("tel_no");
String user_ip = input.getText("user_ip");
String time_stamp = DateTime.getTimestampString();
String _OID = "TR_" + StringFormater.fillZero(user_seq_no, 6) + "_" + StringFormater.fillZero(lecture_seq_no,3) + "_" + time_stamp;
DataSet output = new DataSet();
InteractionBean interact = new InteractionBean();

%>
<%
	
	/*
     * [결제 인증요청 페이지(STEP2-1)]
     *
     * 샘플페이지에서는 기본 파라미터만 예시되어 있으며, 별도로 필요하신 파라미터는 연동메뉴얼을 참고하시어 추가 하시기 바랍니다.
     */

    /*
     * 1. 기본결제 인증요청 정보 변경
     *
     * 기본정보를 변경하여 주시기 바랍니다.(파라미터 전달시 POST를 사용하세요)
     */
    
    String LGD_OID              = _OID;                      //주문번호(상점정의 유니크한 주문번호를 입력하세요)
    String LGD_AMOUNT           = amount;                   //결제금액("," 를 제외한 결제금액을 입력하세요)
    String LGD_BUYER            = user_nm;                    //구매자명
    String LGD_PRODUCTINFO      = product_nm;              //상품명
    String LGD_BUYEREMAIL       = email;               //구매자 이메일
    String LGD_TIMESTAMP        = time_stamp;                //타임스탬프
    String LGD_CUSTOM_SKIN      = "red";                                                //상점정의 결제창 스킨(red, blue, cyan, green, yellow)
    String LGD_BUYERID          = user_id;       			//구매자 아이디
    String LGD_BUYERIP          = user_ip;       			//구매자IP


    /*
     *************************************************
     * 2. MD5 해쉬암호화 (수정하지 마세요) - BEGIN
     *
     * MD5 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
     *************************************************
     *
     * 해쉬 암호화 적용( LGD_MID + LGD_OID + LGD_AMOUNT + LGD_TIMESTAMP + LGD_MERTKEY )
     * LGD_MID          : 상점아이디
     * LGD_OID          : 주문번호
     * LGD_AMOUNT       : 금액
     * LGD_TIMESTAMP    : 타임스탬프
     * LGD_MERTKEY      : 상점MertKey (mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다)
     *
     * MD5 해쉬데이터 암호화 검증을 위해
     * LG데이콤에서 발급한 상점키(MertKey)를 환경설정 파일(lgdacom/conf/mall.conf)에 반드시 입력하여 주시기 바랍니다.
     */
    StringBuffer sb = new StringBuffer();
    sb.append(LGD_MID);
    sb.append(LGD_OID);
    sb.append(LGD_AMOUNT);
    sb.append(LGD_TIMESTAMP);
    sb.append(LGD_MERTKEY);

    byte[] bNoti = sb.toString().getBytes();
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digest = md.digest(bNoti);

    StringBuffer strBuf = new StringBuffer();
    for (int i=0 ; i < digest.length ; i++) {
        int c = digest[i] & 0xff;
        if (c <= 15){
            strBuf.append("0");
        }
        strBuf.append(Integer.toHexString(c));
    }

    String LGD_HASHDATA = strBuf.toString();
    String LGD_CUSTOM_PROCESSTYPE = "TWOTR";
    /*
     *************************************************
     * 2. MD5 해쉬암호화 (수정하지 마세요) - END
     *************************************************
     */
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>결제 확인</title>
<script type="text/javascript">
/*
 * 상점결제 인증요청후 PAYKEY를 받아서 최종결제 요청.
 */
function doPay_ActiveX(){
    ret = xpay_check(document.getElementById('LGD_PAYINFO'), '<%= CST_PLATFORM %>');

    if (ret=="00"){     //ActiveX 로딩 성공
        var LGD_RESPCODE        = dpop.getData('LGD_RESPCODE');       //결과코드
        var LGD_RESPMSG         = dpop.getData('LGD_RESPMSG');        //결과메세지

        if( "0000" == LGD_RESPCODE ) { //인증성공
            var LGD_PAYKEY      = dpop.getData('LGD_PAYKEY');         //LG데이콤 인증KEY
            var msg = "인증결과 : " + LGD_RESPMSG + "\n";
            msg += "LGD_PAYKEY : " + LGD_PAYKEY +"\n\n";
            document.getElementById('LGD_PAYKEY').value = LGD_PAYKEY;
            document.getElementById('LGD_PAYINFO').submit();
        } else { //인증실패
            alert("인증이 실패하였습니다. " + LGD_RESPMSG);
            /*
             * 인증실패 화면 처리
             */
            //jQuery('form#frmResult #LGD_RESPMSG').val(LGD_RESPMSG);
            //jQuery('form#frmResult').submit();
        }
    } else {
        alert("LG U+ 전자결제를 위한 ActiveX Control이  설치되지 않았습니다.");
        /*
         * 인증실패 화면 처리
         */
    }
}

function isActiveXOK(){
	if(lgdacom_atx_flag == true){
    	document.getElementById('LGD_BUTTON1').style.display='none';
        document.getElementById('LGD_BUTTON2').style.display='';
        document.getElementById('LGD_TEXT').innerHTML('* 위 내용이 맞으시면 계속 버튼을 눌러 진행하십시요.');
	}else{
		document.getElementById('LGD_BUTTON1').style.display='';
        document.getElementById('LGD_BUTTON2').style.display='none';
        document.getElementById('LGD_TEXT').innerHTML('* 결제모듈을 설치하여야 합니다.');
	}
}
</script>
</head>

<body onload="isActiveXOK();">
<div id="LGD_ACTIVEX_DIV"/> <!-- ActiveX 설치 안내 Layer 입니다. 수정하지 마세요. -->
<form method="post" id="LGD_PAYINFO" action="/edu/payment/paymentCtrl.jspx?cmd=paymentRes">

<div class="popupLayer" style="">
	<a href="#" onclick="self.close();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
	<div class="popupCont_top">
		<div class="stit">
			요청하신 결제정보는 다음과 같습니다
		</div>
	</div>
	<div class="popupCont_bottom ">
		<div class="edu_info">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="120"/><col width=""/>
			</colgroup>
			<tbody>
				
				<tr>
					<th scope="row">교육자 이름</th>
					<td><%= LGD_BUYER %></td>
				</tr>
				<tr>
					<th scope="row">교육자 IP</th>
					<td><%= LGD_BUYERIP %></td>
				</tr>
				<tr>
					<th scope="row">교육자 ID</th>
					<td><%= LGD_BUYERID %></td>
				</tr>
				<tr>
					<th scope="row">교육명</th>
					<td><%= LGD_PRODUCTINFO %></td>
				</tr>
				<tr>
					<th scope="row">결제금액</th>
					<td><%= HtmlFormat.formatInt(amount) %></td>
				</tr><tr>
					<th scope="row">교육자이메일</th>
					<td><%= LGD_BUYEREMAIL %></td>
				</tr>
				<tr>
					<th scope="row">결제요청번호</th>
					<td><%= LGD_OID %></td>
				</tr>
				</tbody>
			</table>
		</div>
 
		<div class="payInputGuide">* 위 내용이 맞으시면 계속 버튼을 눌러 진행하십시요.</div>

		<div class="btnWrap center">
			<div id="LGD_BUTTON1">결제를 위한 모듈을 다운 중이거나, 모듈을 설치하지 않았습니다. </div>
			<div id="LGD_BUTTON2" style="display:none">
			
			<a href="javascript:doPay_ActiveX();" class="pbtn02"><span class="">계속</span></a>&nbsp;&nbsp;
			<a href="javascript:self.close();" class="pbtn01"><span class="">취소</span></a>
			
			</div>
		</div>
	</div>
</div>	
<input type="hidden" name="CST_PLATFORM"                value="<%= CST_PLATFORM %>">                   <!-- 테스트, 서비스 구분 -->
<input type="hidden" name="CST_MID"                     value="<%= CST_MID %>">                        <!-- 상점아이디 -->
<input type="hidden" name="LGD_MID"                     value="<%= LGD_MID %>">                        <!-- 상점아이디 -->
<input type="hidden" name="LGD_OID"                     value="<%= LGD_OID %>">                        <!-- 주문번호 -->
<input type="hidden" name="LGD_BUYER"                   value="<%= LGD_BUYER %>">                      <!-- 구매자 -->
<input type="hidden" name="LGD_PRODUCTINFO"             value="<%= LGD_PRODUCTINFO %>">                <!-- 상품정보 -->
<input type="hidden" name="LGD_AMOUNT"                  value="<%= LGD_AMOUNT %>">                     <!-- 결제금액 -->
<input type="hidden" name="LGD_BUYEREMAIL"              value="<%= LGD_BUYEREMAIL %>">                 <!-- 구매자 이메일 -->
<input type="hidden" name="LGD_CUSTOM_SKIN"             value="<%= LGD_CUSTOM_SKIN %>">                <!-- 결제창 SKIN -->
<input type="hidden" name="LGD_CUSTOM_PROCESSTYPE"      value="<%= LGD_CUSTOM_PROCESSTYPE %>">         <!-- 트랜잭션 처리방식 -->
<input type="hidden" name="LGD_TIMESTAMP"               value="<%= LGD_TIMESTAMP %>">                  <!-- 타임스탬프 -->
<input type="hidden" name="LGD_HASHDATA"                value="<%= LGD_HASHDATA %>">                   <!-- MD5 해쉬암호값 -->
<input type="hidden" name="LGD_PAYKEY"                  id="LGD_PAYKEY">   							   <!-- LG데이콤 PAYKEY(인증후 자동셋팅)-->
<input type="hidden" name="LGD_VERSION"         		value="JSP_XPay_1.0">
<input type="hidden" name="LGD_BUYERIP"                 value="<%= LGD_BUYERIP %>">           			<!-- 구매자IP -->
<input type="hidden" name="LGD_BUYERID"                 value="<%= LGD_BUYERID %>">           			<!-- 구매자ID -->

<input type="hidden" name="LGD_CUSTOM_USABLEPAY"                 value="">		<!-- 신용카드만 사용 -->

<!-- 가상계좌(무통장) 결제연동을 하시는 경우  할당/입금 결과를 통보받기 위해 반드시 LGD_CASNOTEURL 정보를 LG 데이콤에 전송해야 합니다 . -->
<input type="hidden" name="LGD_CASNOTEURL"          	value="<%= LGD_CASNOTEURL %>" >                 <!-- 가상계좌 NOTEURL -->
<input type="hidden" name="user_seq_no"   id="user_seq_no"     value="<%= user_seq_no %>">
<input type="hidden" name="lecture_seq_no"   id="lecture_seq_no"     value="<%= lecture_seq_no %>">
<input type="hidden" name="LGD_BUYERHP"   id="LGD_BUYERHP"     value="<%= LGD_BUYERHP %>">
</form>

<form method="post" name="frmResult" id="frmResult" action="/edu/payment/paymentCtrl.jspx?cmd=paymentResult">
<input type="hidden" name="CST_PLATFORM"                value="<%= CST_PLATFORM %>">                   <!-- 테스트, 서비스 구분 -->
<input type="hidden" name="CST_MID"                     value="<%= CST_MID %>">                        <!-- 상점아이디 -->
<input type="hidden" name="LGD_MID"                     value="<%= LGD_MID %>">                        <!-- 상점아이디 -->
<input type="hidden" name="LGD_OID"                     value="<%= LGD_OID %>">                        <!-- 주문번호 -->
<input type="hidden" name="LGD_BUYER"                   value="<%= LGD_BUYER %>">                      <!-- 구매자 -->
<input type="hidden" name="LGD_PRODUCTINFO"             value="<%= LGD_PRODUCTINFO %>">                <!-- 상품정보 -->
<input type="hidden" name="LGD_AMOUNT"                  value="<%= LGD_AMOUNT %>">                     <!-- 결제금액 -->
<input type="hidden" name="LGD_BUYEREMAIL"              value="<%= LGD_BUYEREMAIL %>">                 <!-- 구매자 이메일 -->
<input type="hidden" name="LGD_CUSTOM_SKIN"             value="<%= LGD_CUSTOM_SKIN %>">                <!-- 결제창 SKIN -->
<input type="hidden" name="LGD_CUSTOM_PROCESSTYPE"      value="<%= LGD_CUSTOM_PROCESSTYPE %>">         <!-- 트랜잭션 처리방식 -->
<input type="hidden" name="LGD_TIMESTAMP"               value="<%= LGD_TIMESTAMP %>">                  <!-- 타임스탬프 -->
<input type="hidden" name="LGD_HASHDATA"                value="<%= LGD_HASHDATA %>">                   <!-- MD5 해쉬암호값 -->
<input type="hidden" name="LGD_PAYKEY"                  id="LGD_PAYKEY">   							   <!-- LG데이콤 PAYKEY(인증후 자동셋팅)-->
<input type="hidden" name="LGD_VERSION"         		value="JSP_XPay_1.0">
<input type="hidden" name="LGD_CUSTOM_USABLEPAY"                 value="">
<input type="hidden" name="LGD_BUYERIP"                 value="<%= LGD_BUYERIP %>">           			<!-- 구매자IP -->
<input type="hidden" name="LGD_BUYERID"                 value="<%= LGD_BUYERID %>">
<input type="hidden" name="LGD_RESPMSG"  id="LGD_RESPMSG"               value="">
<input type="hidden" name="user_seq_no"   id="user_seq_no"     value="<%= user_seq_no %>">
<input type="hidden" name="lecture_seq_no"   id="lecture_seq_no"     value="<%= lecture_seq_no %>">
<input type="hidden" name="LGD_BUYERHP"   id="LGD_BUYERHP"     value="<%= LGD_BUYERHP %>">
</form>

</body>
<!--  xpay.js는 반드시 body 밑에 두시기 바랍니다. -->
<!--  UTF-8 인코딩 사용 시는 xpay.js 대신 xpay_utf-8.js 을  호출하시기 바랍니다.-->
<script language="javascript" src="<%=request.getScheme()%>://xpay.lgdacom.net<%="test".equals(CST_PLATFORM)?(request.getScheme().equals("https")?":7443":":7080"):""%>/xpay/js/xpay_utf-8.js" type="text/javascript">
</script>
</html>
