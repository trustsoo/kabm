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
String LGD_PAYKEY  = request.getParameter("LGD_PAYKEY");
String LGD_OID = input.getText("LGD_OID");
String LGD_BUYERID = input.getText("LGD_BUYERID");
String LGD_RESPMSG = input.getText("LGD_RESPMSG");
String LGD_AMOUNT  = input.getText("LGD_AMOUNT");
DataSet result = new DataSet();
InteractionBean interact = new InteractionBean();
try
{
	Logger.user.println("[PAYMENT]["+LGD_OID+"]["+LGD_BUYERID+"] : RESULT");
	Logger.user.println("[PAYMENT]["+LGD_OID+"]["+LGD_BUYERID+"] : " + input.toString());
		
}catch(Exception e){
	e.printStackTrace();
}

try
{
	String lecture_seq_no = input.getText("lecture_seq_no");
	String user_seq_no = input.getText("user_seq_no");
	
	if(lecture_seq_no == null || "".equals(lecture_seq_no))
	{
		LGD_RESPMSG = "FAIL:강의번호오류";
	}
	else if(user_seq_no == null || "".equals(user_seq_no))
	{
		LGD_RESPMSG = "FAIL:수강자정보오류";
	}else{
		input.put("cmd" , "INSERT_PAYMENT");
		interact.execute("payment/PaymentMgr", input);
		LGD_RESPMSG ="정상적으로 요청되었습니다. 선택하신 휴대폰이나 메일을 확인하시기 바랍니다.<br><br>수신된 계좌로 입금하시면 바로 교육을 시작하실 수 있습니다.";
		
		
		//해당 API를 사용하기 위해 WEB-INF/lib/XPayClient.jar 를 Classpath 로 등록하셔야 합니다.
		XPayClient xpay = new XPayClient();
		boolean isInitOK = xpay.Init(configPath, CST_PLATFORM);

		if( !isInitOK ) {
			//API 초기화 실패 화면처리
			StringBuffer sb = new StringBuffer();
		    sb.append( "결제요청을 초기화 하는데 실패하였습니다.<br>");
		    sb.append( "LG텔레콤에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");
		    //sb.append( "mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
		    //sb.append( "문의전화 LG텔레콤 1544-7772<br>");
		    LGD_RESPMSG = sb.toString();
	
		    setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[PAYLES_91]"+LGD_RESPMSG);
		    input.put("cmd" , "UPDATE_PAYMENT_STATUS");
		    input.put("status" , "91");
			input.put("LGD_PAYKEY" ,LGD_PAYKEY);
			interact.execute("payment/PaymentMgr", input);
		}else{
			
			try{
	   			/*
	   	   	     *************************************************
	   	   	     * 1.최종결제 요청(수정하지 마세요) - END
	   	   	     *************************************************
	   	   	     */
		    	xpay.Init_TX(LGD_MID);
		    	xpay.Set("LGD_TXNAME", "PaymentByKey");
		    	xpay.Set("LGD_PAYKEY", LGD_PAYKEY);

		    	//금액을 체크하시기 원하는 경우 아래 주석을 풀어서 이용하십시요.
		    	//String DB_AMOUNT = "DB나 세션에서 가져온 금액"; //반드시 위변조가 불가능한 곳(DB나 세션)에서 금액을 가져오십시요.
		    	//xpay.Set("LGD_AMOUNTCHECKYN", "Y");
		    	//xpay.Set("LGD_AMOUNT", DB_AMOUNT);
		    	setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[PAYLES_3]"+"최종결제 요청");
		    	input.put("cmd" , "UPDATE_PAYMENT_STATUS");
		        input.put("status" , "01");
				input.put("LGD_PAYKEY" ,LGD_PAYKEY);
				interact.execute("payment/PaymentMgr", input);
				//LGD_RESPMSG = xpay.m_szResMsg;
				/*
		         * 2. 최종결제 요청 결과처리
		         *
		         * 최종 결제요청 결과 리턴 파라미터는 연동메뉴얼을 참고하시기 바랍니다.
		         */
		         if ( xpay.TX() ) {
		             //1)결제결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
		             
		             for (int i = 0; i < xpay.ResponseNameCount(); i++)
		             {

		                 for (int j = 0; j < xpay.ResponseCount(); j++)
		                 {
		                	 result.put( xpay.ResponseName(i) , xpay.Response(xpay.ResponseName(i), j));
		                 }
		             }
		             
		         }else {
		             //2)API 요청실패 화면처리
		             setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+xpay.m_szResCode+"]"+"[PAYLES_93]"+xpay.m_szResMsg);
		             input.put("cmd" , "UPDATE_PAYMENT_STATUS");
 			         input.put("status" , "93");
 					 input.put("LGD_PAYKEY" ,LGD_PAYKEY);
 					 interact.execute("/payment/PaymentMgr", input);
 					 
 					  LGD_RESPMSG = xpay.m_szResMsg;
		         	//최종결제요청 결과 실패 DB처리
		         	//out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");
		         }

	    	}catch(Exception e) {
	    		StringBuffer sb = new StringBuffer();
	    		sb.append("LG텔레콤 제공 API를 사용할 수 없습니다. 환경파일 설정을 확인해 주시기 바랍니다. ");
	    		sb.append(""+e.getMessage());
	    		LGD_RESPMSG = sb.toString();
	    		setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[PAYLES_92]"+LGD_RESPMSG);
	    		input.put("cmd" , "UPDATE_PAYMENT_STATUS");
		        input.put("status" , "92");
				input.put("LGD_PAYKEY" ,LGD_PAYKEY);
				interact.execute("/payment/PaymentMgr", input);
	    	}
		}
	}
}catch(Exception e){	
	if( "".equals(LGD_RESPMSG)) LGD_RESPMSG="FAIL:입력중 오류발생!";

}finally{

}

	
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>결제 결과</title>

</head>

<body>
<div class="popupLayer" style="">
	<a href="#" onclick="self.close();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
	<div class="popupCont_top">
		<div class="stit">
			결제 결과
		</div>
	</div>
	<div class="popupCont_bottom " style="height: 695px;">
		<div class="edu_info">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="150"/><col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">입금자성명</th>
					<td height="120px" valign="middle" align="center" style="color:#5FB221;"><b><%=result.getText("LGD_PAYER")%></b></td>
				</tr>
				<tr>
					<th scope="row">입금은행</th>
					<td height="120px" valign="middle" align="center" style="color:#5FB221;"><b><%=result.getText("LGD_FINANCENAME")%></b></td>
				</tr>
				<tr>
					<th scope="row">입금계좌</th>
					<td height="120px" valign="middle" align="center" style="color:#5FB221;"><b><%=result.getText("LGD_ACCOUNTNUM")%></b></td>
				</tr>
				<tr>
					<th scope="row">결과메시지</th>
					<td height="120px" valign="middle" align="center" style="color:#5FB221;"><b><%=LGD_RESPMSG%></b></td>
				</tr>
				</tbody>
			</table>
		</div>
 
		<div class="btnWrap center">
			<a href="javascript:fn_reload();" class="pbtn02"><span >확인</span></a>
			
		</div>
	</div>
</div>

<script type="text/javascript">

function fn_reload()
{
	opener.location = '/edu/payment/paymentCtrl.jspx?cmd=viewPaymentList';
	self.close();
}



</script>
</body>
</html>
