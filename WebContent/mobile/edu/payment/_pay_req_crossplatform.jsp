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
<%@ include file="/mobile/edu/payment/paycommon.jsp"%>
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
LGD_RETURNURL = LGD_RETURNURL + "&lecture_seq_no="+lecture_seq_no+"&user_seq_no="+user_seq_no;
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
    * ISP 카드결제 연동을 위한 파라미터(필수)
    */
    String LGD_KVPMISPWAPURL		= "";
    String LGD_KVPMISPCANCELURL     = "";
    	
    String LGD_MPILOTTEAPPCARDWAPURL = ""; //iOS 연동시 필수

    /*
    * 계좌이체 연동을 위한 파라미터(필수)
    */
    String LGD_MTRANSFERWAPURL 		= "";
    String LGD_MTRANSFERCANCELURL 	= "";  
    	   
    /*
     *************************************************
     * 2. MD5 해쉬암호화 (수정하지 마세요) - BEGIN
     *
     * MD5 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
     *************************************************
     *
      * 해쉬 암호화 생성( LGD_MID + LGD_OID + LGD_AMOUNT + LGD_TIMESTAMP )
      * LGD_MID          : 상점아이디
      * LGD_OID          : 주문번호
      * LGD_AMOUNT       : 금액
      * LGD_TIMESTAMP    : 타임스탬프
      *
      * MD5 해쉬데이터 암호화 검증을 위해
      * 토스페이먼츠에서 발급한 상점키(MertKey)를 환경설정 파일(lgdacom/conf/mall.conf)에 반드시 입력하여 주시기 바랍니다.
      */
      String LGD_HASHDATA = "";
      XPayClient xpay = null;
      try {
     	 
     	 xpay = new XPayClient();
     	 xpay.Init(configPath, CST_PLATFORM);
     	 
     	 if(LGD_TIMESTAMP == null || "".equals(LGD_TIMESTAMP)) {
     		 LGD_TIMESTAMP = xpay.GetTimeStamp();
     	 }
     	 LGD_HASHDATA = xpay.GetHashData(LGD_MID, LGD_OID, LGD_AMOUNT, LGD_TIMESTAMP);
     	 
      } catch(Exception e) {
     	e.printStackTrace();
     	out.println("토스페이먼츠 제공 API를 사용할 수 없습니다. 환경파일 설정을 확인해 주시기 바랍니다. ");
  		out.println(""+e.getMessage());
  		return;
      } finally {
     	xpay = null;
      }
 	String LGD_CUSTOM_PROCESSTYPE = "TWOTR";
 	/*
 	*************************************************
 	* 2. MD5 해쉬암호화 (수정하지 마세요) - END
 	*************************************************
 	*/
     
  	 Map payReqMap = new HashMap();
     
     payReqMap.put("CST_PLATFORM"                , CST_PLATFORM);                   	// 테스트, 서비스 구분
     payReqMap.put("CST_MID"                     , CST_MID );                        	// 상점아이디
     payReqMap.put("CST_WINDOW_TYPE"			 , LGD_WINDOW_TYPE );					// 전송방식 구분
     payReqMap.put("LGD_WINDOW_TYPE"             , LGD_WINDOW_TYPE );                   // 상점아이디
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
     payReqMap.put("LGD_VERSION"         		, "JSP_Non-ActiveX_Standard");			// 버전정보 (삭제하지 마세요)
     payReqMap.put("LGD_CUSTOM_USABLEPAY"  		, LGD_CUSTOM_USABLEPAY );				// 디폴트 결제수단 (해당 필드를 보내지 않으면 결제수단 선택 UI 가 보이게 됩니다.)
     payReqMap.put("LGD_CUSTOM_SWITCHINGTYPE"  	, LGD_CUSTOM_SWITCHINGTYPE );			// 신용카드 카드사 인증 페이지 연동 방식
     //payReqMap.put("LGD_WINDOW_VER"  			, LGD_WINDOW_VER );						// 결제창 버젼정보 
     payReqMap.put("LGD_ENCODING"  				, LGD_ENCODING );
     payReqMap.put("LGD_ENCODING_RETURNURL"  	, LGD_ENCODING_RETURNURL );
     payReqMap.put("LGD_ENCODING_NOTEURL"  		, LGD_ENCODING_NOTEURL );
     payReqMap.put("LGD_DOMAIN_URL"				, "xpayvvip" );      // 모바일전용
     payReqMap.put("LGD_OSTYPE_CHECK"           , LGD_OSTYPE_CHECK );
     
     payReqMap.put("LGD_CUSTOM_FIRSTPAY"		, "SC0010" );				// 디폴트 결제수단
	 //payReqMap.put("LGD_PCVIEWYN"				, "Y" );						// 휴대폰번호 입력 화면 사용 여부
     
    

	//iOS 연동시 필수
	payReqMap.put("LGD_MPILOTTEAPPCARDWAPURL"	, LGD_MPILOTTEAPPCARDWAPURL );


	/*
	****************************************************
	* 신용카드 ISP(국민/BC)결제에만 적용 - BEGIN 
	****************************************************
	*/
	payReqMap.put("LGD_KVPMISPWAPURL"			, LGD_KVPMISPWAPURL );	
	payReqMap.put("LGD_KVPMISPCANCELURL"		, LGD_KVPMISPCANCELURL );
	/*
	****************************************************
	* 신용카드 ISP(국민/BC)결제에만 적용  - END
	****************************************************
	*/
		
	/*
	****************************************************
	* 계좌이체 결제에만 적용 - BEGIN 
	****************************************************
	*/
	payReqMap.put("LGD_MTRANSFERWAPURL"			, LGD_MTRANSFERWAPURL );	
	payReqMap.put("LGD_MTRANSFERCANCELURL"		, LGD_MTRANSFERCANCELURL );
	
	/*
	****************************************************
	* 계좌이체 결제에만 적용  - END
	****************************************************
	*/
	
	
	/*
	****************************************************
	* 모바일 OS별 ISP(국민/비씨), 계좌이체 결제 구분 값
	****************************************************
	1) Web to Web
	- 안드로이드: A (디폴트)
	- iOS: N
	  ** iOS일 경우, 반드시 N으로 값을 수정
	2) App to Web(반드시 SmartXPay_AppToWeb_연동가이드를 참조합니다.)
	- 안드로이드, iOS: A
	*/
	payReqMap.put("LGD_KVPMISPAUTOAPPYN"	, "A");					// 신용카드 결제 사용시 필수
	payReqMap.put("LGD_MTRANSFERAUTOAPPYN"	, "A");					// 계좌이체 결제 사용시 필수
	
     
     
     // 가상계좌(무통장) 결제연동을 하시는 경우  할당/입금 결과를 통보받기 위해 반드시 LGD_CASNOTEURL 정보를 LG 유플러스에 전송해야 합니다 .
     payReqMap.put("LGD_CASNOTEURL"          , LGD_CASNOTEURL );               // 가상계좌 NOTEURL
     payReqMap.put("LGD_CLOSEDATE"          , LGD_CLOSEDATE );               // 가상계좌  종료일시

    /*Return URL에서 인증 결과 수신 시 셋팅될 파라미터 입니다.*/
	 payReqMap.put("LGD_RESPCODE"  		 , "" );
	 payReqMap.put("LGD_RESPMSG"  		 , "" );
	 payReqMap.put("LGD_PAYKEY"  		 , "" );
	 
	 payReqMap.put("LGD_BUYERHP"  		 , LGD_BUYERHP );
	 
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
	lgdwin = open_paymentwindow(document.getElementById('LGD_PAYINFO'), '<%= CST_PLATFORM %>', LGD_window_type);
}
/*
* FORM 명만  수정 가능
*/
function getFormObject() {
        return document.getElementById("LGD_PAYINFO");
}

function setPaymentType(pay_type){
	document.getElementById("LGD_CUSTOM_FIRSTPAY").value = pay_type;
	launchCrossPlatform();
}

</script>
<style type="text/css">

.paySelec {
    width: 100%;
    padding: 10px 15px;
    display: inline-block;
}
.paySelec li {
    width: 115px;
    height: 130px;
    float: left;
    display: inline-block;
    margin: 12px;
    border: 1px #cdcdcd solid;
    position: relative;
    box-shadow: #cdcdcd 0px 1px 3px;
}
.paySelec li a {
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    color: #4593fc;
}
.paySelec li a strong {
    margin-top: 77px;
    width: 100%;
    height: 36px;
    line-height: 20px;
    font-size: 14px;
	display: inline-block;
	text-align: center;
}
.paySelec li.card {
    background: url(/static/mobile/img/card.png) no-repeat left top;
    background-size: 125px;
}
.paySelec li.account {
    background: url(/static/mobile/img/account.png) no-repeat left top;
    background-size: 125px;
}
.paySelec li.noAccount {
    background: url(/static/mobile/img/noAccount.png) no-repeat left top;
    background-size: 125px;
}


</style>
</head>
<body>
<div class="popupLayer" style="">
	<div style="font-size:1.3em;font-weight:bold;color:#1e90f1;margin-top:20px;">▣  요청하신 결제정보는 다음과 같습니다</div>
	<div class="popupCont_bottom " >
		
		<table class="table table-striped jambo_table bulk_action">
			<colgroup>
				<col width="100px"/><col width=""/>
			</colgroup>
			<thead>
				<tr class="headings">
					<th class="column-title">구분</th>
					<th class="column-title no-link last">내용</th>
				</tr>
			</thead>
			<tbody>
				<tr class="even pointer">
					<td class="a-center ">교육자 이름</td>
					<td class=" last"><%= LGD_BUYER %></td>
				</tr>
				<tr class="even pointer">
					<td class="a-center ">교육자 IP</td>
					<td class=" last"><%= LGD_BUYERIP %></td>
				</tr>
				<tr class="even pointer">
					<td class="a-center ">교육자 ID</td>
					<td class=" last"><%= LGD_BUYERID %></td>
				</tr>
				<tr class="even pointer">
					<td class="a-center ">교육명</td>
					<td class=" last"><%= LGD_PRODUCTINFO %></td>
				</tr>
				<tr class="even pointer">
					<td class="a-center ">결제금액</td>
					<td class=" last"><%= HtmlFormat.formatInt(amount) %></td>
				</tr>
				<tr class="even pointer">
					<td class="a-center ">교육자이메일</td>
					<td class=" last"><%= LGD_BUYEREMAIL %></td>
				</tr>
				<tr class="even pointer">
					<td class="a-center ">결제요청번호</td>
					<td class=" last"><%= LGD_OID %></td>
				</tr>
			</tbody>
		</table>
			
	</div>
	<div class="alert alert-success">
		<button type="button" class="close" data-dismiss="alert">
			<i class="icon-remove"></i>
		</button>
		<i class="icon-ok green"></i>			
		<strong class="">* 위 내용이 맞으시면 계속 버튼을 눌러 진행하십시요.</strong>
	</div>
	<div class="btnWrap center">
		<a href="#" id="btn-next" class="btn btn-success">계속</a>
		<a href="#" id="btn-prev" class="btn btn-default">취소</a>
	</div>
</div>

<div id="viewForm" style="border: 1px solid #1e90f1;z-index: 999;width:100%;display: none;position:absolute;left:0px;top:-47px;background-color: #FFF;">
	<div style="z-index:999;font-size: 1.0em;font-weight:bold;color:#ffffff;background-color:#3b85c3;height: 2.95em;padding-top: 10px;padding-left: 10px;" id="confirm_title">
	결제하실 방법을 선택하세요
	</div>
	
	<%-- <table class="table table-striped jambo_table bulk_action">
		<colgroup>
			<col width=""/><col width=""/>
		</colgroup>		
		<tbody>
			
			<tr class="even pointer">
				<td class=" last" id="txt_post_req" colspan="2" style="color:red;font-weight:bold;">
					<img src="/static/main/img/new/quick.jpg" onclick="setPaymentType('SC0010')"> <img src="/static/main/img/new/quick.jpg" onclick="setPaymentType('SC0020')">
				</td>
			</tr>
			<tr class="even pointer">
				<td class=" last" id="txt_post_req" colspan="2" style="color:red;font-weight:bold;">
					<img src="/static/main/img/new/quick.jpg" onclick="setPaymentType('SC0030')"> <img src="/static/main/img/new/quick.jpg" onclick="setPaymentType('SC0040')">
				</td>
			</tr>
		</tbody>
	</table> --%>
	<ul class="paySelec">        
          <li class="card"><a href="#" onclick="setPaymentType('SC0010');" onkeypress="if (event.keyCode == 13) {setPaymentType('SC0010');}"><strong>신용카드</strong></a></li>
          <li class="account"><a href="#" onclick="setPaymentType('SC0030');" onkeypress="if (event.keyCode == 13) {setPaymentType('SC0030');}"><strong>실시간계좌이체</strong></a></li>
          <li class="noAccount line2"><a href="#" onclick="setPaymentType('SC0040');" onkeypress="if (event.keyCode == 13) {setPaymentType('SC0040');}"><strong>가상계좌<br> <span>(무통장입금)</span></strong></a></li>
      </ul>
	<div class="actionBar">
		<a id='btn-pass' class="btn btn-default">뒤로</a>
	</div>	
	
</div>

<form method="post" name="LGD_PAYINFO" id="LGD_PAYINFO" action="">
<%
	for(Iterator i = payReqMap.keySet().iterator(); i.hasNext();){
		Object key = i.next();
		out.println("<input type='hidden' name='" + key + "' id='"+key+"' value='" + payReqMap.get(key) + "'>" );
	}
%>
</form>
<script type="text/javascript">

jQuery(document).ready(
		function()
		{	
			
			jQuery('#btn-next').on("click", function(){
				$('#viewForm').show();
				$('.popupLayer').hide();
				
			});	
			
			jQuery('#btn-prev').on("click", function(){
				history.go(-1);
			});
			
			jQuery('#btn-pass').on("click", function(){				
				$('#viewForm').hide();
				$('.popupLayer').show();
			});
		}
	);
</script>
</body>

</html>
