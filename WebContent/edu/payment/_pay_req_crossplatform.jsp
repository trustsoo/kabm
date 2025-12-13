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
String datetime = DateTime.getShortDateString();
String LGD_CLOSEDATE = datetime.substring(0, 4)+"1231235959";

%>


<%
	                                                         
    String LGD_OID              = _OID;                      //주문번호(상점정의 유니크한 주문번호를 입력하세요)
    String LGD_AMOUNT           = amount;                   //결제금액("," 를 제외한 결제금액을 입력하세요)
    String LGD_BUYER            = user_nm;                    //구매자명
    String LGD_PRODUCTINFO      = product_nm;              //상품명
    String LGD_BUYEREMAIL       = email;               //구매자 이메일
    String LGD_TIMESTAMP        = time_stamp;                //타임스탬프
    String LGD_BUYERID          = user_id;       			//구매자 아이디
    String LGD_BUYERIP          = user_ip;       			//구매자IP
    String LGD_BUYERHP       = tel_no;               //구매자 전화번호
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
     * LG유플러스에서 발급한 상점키(MertKey)를 환경설정 파일(lgdacom/conf/mall.conf)에 반드시 입력하여 주시기 바랍니다.
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
     
     
     
     
  	 Map payReqMap = new HashMap();
     
     payReqMap.put("CST_PLATFORM"                , CST_PLATFORM);                   	// 테스트, 서비스 구분
     payReqMap.put("CST_MID"                     , CST_MID );                        	// 상점아이디
     payReqMap.put("LGD_WINDOW_TYPE"             , LGD_WINDOW_TYPE );                        	// 상점아이디
     payReqMap.put("LGD_MID"                     , LGD_MID );                        	// 상점아이디
     payReqMap.put("LGD_OID"                     , LGD_OID );                        	// 주문번호
     payReqMap.put("LGD_BUYER"                   , LGD_BUYER );                      	// 구매자
     payReqMap.put("LGD_PRODUCTINFO"             , LGD_PRODUCTINFO );                	// 상품정보
     payReqMap.put("LGD_AMOUNT"                  , LGD_AMOUNT );                     	// 결제금액
     payReqMap.put("LGD_BUYEREMAIL"              , LGD_BUYEREMAIL );                 	// 구매자 이메일
     payReqMap.put("LGD_CUSTOM_SKIN"             , LGD_CUSTOM_SKIN );                	// 결제창 SKIN
     payReqMap.put("LGD_CUSTOM_PROCESSTYPE"      , LGD_CUSTOM_PROCESSTYPE );         	// 트랜잭션 처리방식
     payReqMap.put("LGD_TIMESTAMP"               , LGD_TIMESTAMP );                  	// 타임스탬프
     payReqMap.put("LGD_HASHDATA"                , LGD_HASHDATA );      	           	// MD5 해쉬암호값
     payReqMap.put("LGD_RETURNURL"   			, LGD_RETURNURL );      			   	// 응답수신페이지
     payReqMap.put("LGD_VERSION"         		, "JSP_SmartXPay_1.0");			   	   	// 버전정보 (삭제하지 마세요)
     payReqMap.put("LGD_CUSTOM_USABLEPAY"  		, LGD_CUSTOM_USABLEPAY );				// 디폴트 결제수단 (해당 필드를 보내지 않으면 결제수단 선택 UI 가 보이게 됩니다.)
     payReqMap.put("LGD_CUSTOM_SWITCHINGTYPE"  	, LGD_CUSTOM_SWITCHINGTYPE );			// 신용카드 카드사 인증 페이지 연동 방식
     payReqMap.put("LGD_WINDOW_VER"  			, LGD_WINDOW_VER );						// 결제창 버젼정보 
     payReqMap.put("LGD_ENCODING"  				, LGD_ENCODING );
     payReqMap.put("LGD_ENCODING_RETURNURL"  	, LGD_ENCODING_RETURNURL );
     payReqMap.put("LGD_ENCODING_NOTEURL"  		, LGD_ENCODING_NOTEURL );
     payReqMap.put("LGD_OSTYPE_CHECK"           , LGD_OSTYPE_CHECK );
     
     
     // 가상계좌(무통장) 결제연동을 하시는 경우  할당/입금 결과를 통보받기 위해 반드시 LGD_CASNOTEURL 정보를 LG 유플러스에 전송해야 합니다 .
     payReqMap.put("LGD_CASNOTEURL"          , LGD_CASNOTEURL );               // 가상계좌 NOTEURL
     payReqMap.put("LGD_CLOSEDATE"          , LGD_CLOSEDATE );               // 가상계좌  종료일시

    /*Return URL에서 인증 결과 수신 시 셋팅될 파라미터 입니다.*/
	 payReqMap.put("LGD_RESPCODE"  		 , "" );
	 payReqMap.put("LGD_RESPMSG"  		 , "" );
	 payReqMap.put("LGD_PAYKEY"  		 , "" );

	 session.setAttribute("PAYREQ_MAP", payReqMap);

 %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>전자결제서비스</title>
<script language="javascript" src="https://xpay.tosspayments.com/xpay/js/xpay_crossplatform.js" type="text/javascript"></script>
<script type="text/javascript">
var LGD_CLOSEDATE = '<%=LGD_CLOSEDATE%>';
/*
* 수정불가.
*/
	var LGD_window_type = '<%=LGD_WINDOW_TYPE%>';
	
/*
* 수정불가
*/
function launchCrossPlatform(){
	lgdwin = openXpay(document.getElementById('LGD_PAYINFO'), '<%= CST_PLATFORM %>', LGD_window_type, null, "", "");
}
/*
* FORM 명만  수정 가능
*/
function getFormObject() {
        return document.getElementById("LGD_PAYINFO");
}

/*
 * 인증결과 처리
 */
function payment_return() {
	var fDoc;
	
		fDoc = lgdwin.contentWindow || lgdwin.contentDocument;
	
		
	if (fDoc.document.getElementById('LGD_RESPCODE').value == "0000") {
		var LGD_PAYTYPE = fDoc.document.getElementById('LGD_PAYTYPE').value;
		if( LGD_PAYTYPE == 'SC0040')
		{
			document.getElementById("LGD_CLOSEDATE").value = LGD_CLOSEDATE;
			document.getElementById("LGD_PAYTYPE").value = LGD_PAYTYPE;
			document.getElementById("LGD_PAYKEY").value = fDoc.document.getElementById('LGD_PAYKEY').value;
			document.getElementById("LGD_PAYINFO").target = "_self";
			document.getElementById("LGD_PAYINFO").action = "/edu/payment/paymentCtrl.jspx?cmd=paymentResult";
			document.getElementById("LGD_PAYINFO").submit();
		}else{
			document.getElementById("LGD_CLOSEDATE").value = '';
			document.getElementById("LGD_PAYTYPE").value = LGD_PAYTYPE;
			document.getElementById("LGD_PAYKEY").value = fDoc.document.getElementById('LGD_PAYKEY').value;
			document.getElementById("LGD_PAYINFO").target = "_self";
			document.getElementById("LGD_PAYINFO").action = "/edu/payment/paymentCtrl.jspx?cmd=paymentRes";
			document.getElementById("LGD_PAYINFO").submit();
		}
	} else {
		alert("LGD_RESPCODE (결과코드) : " + fDoc.document.getElementById('LGD_RESPCODE').value + "\n" + "LGD_RESPMSG (결과메시지): " + fDoc.document.getElementById('LGD_RESPMSG').value);
		closeIframe();
	}
}
function fn_reload()
{
	if(window.opener) { window.opener.document.location.href = '/edu/payment/paymentCtrl.jspx?cmd=viewPaymentList';} 
	//opener.location.href = '/edu/payment/paymentCtrl.jspx?cmd=viewPaymentList';
	self.close();
}
</script>
</head>
<body>
<div class="popupLayer" style="">
	<a href="#" onclick="self.close();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
	<div class="popupCont_top">
		<div class="stit">
			요청하신 결제정보는 다음과 같습니다
		</div>
	</div>
	<div class="popupCont_bottom " style="height: 695px;">
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
			<a href="javascript:launchCrossPlatform();" class="pbtn02"><span class="">계속</span></a>&nbsp;&nbsp;
			<a href="javascript:self.close();" class="pbtn01"><span class="">취소</span></a>
			<!-- <a href="javascript:fn_reload();" class="pbtn02"><span >확인</span></a> -->
		</div>
	</div>
</div>
<form method="post" name="LGD_PAYINFO" id="LGD_PAYINFO" action="/edu/payment/paymentCtrl.jspx?cmd=paymentRes">

<%
	for(Iterator i = payReqMap.keySet().iterator(); i.hasNext();){
		Object key = i.next();
		out.println("<input type='hidden' name='" + key + "' id='"+key+"' value='" + payReqMap.get(key) + "'>" );
	}
%>
<input type="hidden" name="user_seq_no"   id="user_seq_no"     value="<%= user_seq_no %>">
<input type="hidden" name="lecture_seq_no"   id="lecture_seq_no"     value="<%= lecture_seq_no %>">
<input type="hidden" name="LGD_BUYERHP"   id="LGD_BUYERHP"     value="<%= LGD_BUYERHP %>">
<input type="hidden" name="LGD_PAYTYPE"  	 id="LGD_PAYTYPE"        value="">
<input type="hidden" name="LGD_CLOSEDATE"  	 id="LGD_CLOSEDATE"        value="<%= LGD_CLOSEDATE %>">
</form>

</body>

</html>
