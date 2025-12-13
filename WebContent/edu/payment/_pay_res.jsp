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

String LGD_OID = input.getText("LGD_OID");
String LGD_BUYERID = input.getText("LGD_BUYERID");
String LGD_BUYER = input.getText("LGD_BUYER");
String LGD_BUYERHP = input.getText("LGD_BUYERHP");
String LGD_AMOUNT = input.getText("LGD_AMOUNT");
String LGD_RESPMSG = "";
StringBuffer sb = new StringBuffer();
DataSet output = new DataSet();
InteractionBean interact = new InteractionBean();
boolean issuccess = false;
%>
<%
   
    /*
     *************************************************
     * 1.최종결제 요청 - BEGIN
     *  (단, 최종 금액체크를 원하시는 경우 금액체크 부분 주석을 제거 하시면 됩니다.)
     *************************************************
     */

    //String CST_PLATFORM                 = request.getParameter("CST_PLATFORM");
    //String CST_MID                      = request.getParameter("CST_MID");
    String LGD_PAYKEY                   = request.getParameter("LGD_PAYKEY");

	setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[PAYLES_1]"+input.toString());

	try
	{
		input.put("cmd" , "INSERT_PAYMENT");
		input.put("LGD_PAYKEY" ,LGD_PAYKEY);
		output = interact.execute("/payment/PaymentMgr", input);

		setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[PAYLES_00] 결제 초기정보입력 성공");

		//해당 API를 사용하기 위해 WEB-INF/lib/XPayClient.jar 를 Classpath 로 등록하셔야 합니다.
	    XPayClient xpay = new XPayClient();
	   	boolean isInitOK = xpay.Init(configPath, CST_PLATFORM);

	   	if( !isInitOK ) {
	    	//API 초기화 실패 화면처리
	    	sb = new StringBuffer();
	        sb.append( "결제요청을 초기화 하는데 실패하였습니다.<br>");
	        sb.append( "LG텔레콤에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");
	        sb.append( "mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
	        sb.append( "문의전화 LG텔레콤 1544-7772<br>");
	        LGD_RESPMSG = sb.toString();

	        setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[PAYLES_91]"+LGD_RESPMSG);
	        input.put("cmd" , "UPDATE_PAYMENT_STATUS");
	        input.put("status" , "91");
			input.put("LGD_PAYKEY" ,LGD_PAYKEY);
			output = interact.execute("payment/PaymentMgr", input);
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
				output = interact.execute("payment/PaymentMgr", input);
				LGD_RESPMSG = xpay.m_szResMsg;
				/*
		         * 2. 최종결제 요청 결과처리
		         *
		         * 최종 결제요청 결과 리턴 파라미터는 연동메뉴얼을 참고하시기 바랍니다.
		         */
		         if ( xpay.TX() ) {
		             //1)결제결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
		             DataSet result = new DataSet();
		             for (int i = 0; i < xpay.ResponseNameCount(); i++)
		             {

		                 for (int j = 0; j < xpay.ResponseCount(); j++)
		                 {
		                	 result.put( xpay.ResponseName(i) , xpay.Response(xpay.ResponseName(i), j));
		                 }
		             }
		             //최종 승인금액
		             LGD_AMOUNT = xpay.Response("LGD_AMOUNT",0);

		             setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+xpay.m_szResCode+"]"+"[PAYLES_4]"+result.toString());

		             result.put("cmd" , "UPDATE_PAYMENT");
             		 result.put("status" , "02");
             		 result.put("LGD_PAYKEY" ,LGD_PAYKEY);
             		 result.put("LGD_RESCODE" ,xpay.m_szResCode);
             		 result.put("LGD_RESPMSG" ,xpay.m_szResMsg);
    				 output = interact.execute("/payment/PaymentMgr", result);
    				 LGD_RESPMSG = xpay.m_szResMsg;
		             String LGD_TID = result.getText("LGD_TID");
		             if( "0000".equals( xpay.m_szResCode ) ) {
		             	//최종결제요청 결과 성공 DB처리
		             	//out.println("최종결제요청 결과 성공 DB처리하시기 바랍니다.<br>");
		             	setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+xpay.m_szResCode+"]"+"[PAYLES_5] 결제 성공 DB처리 시작");
		             	boolean isDBOK = true; //DB처리 실패시 false로 변경해 주세요.

		             	try
		             	{
		             		interact.beginTransaction();

		             		input.put("cmd" , "INSERT_USER_LECTURE");
		    		        output = interact.execute("/payment/PaymentMgr", input);
		             		String user_lecture_seq_no = output.getText("user_lecture_seq_no");

		             		if(user_lecture_seq_no == null || "".equals(user_lecture_seq_no))
		             			throw new Exception();
		             		setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+xpay.m_szResCode+"]"+"[PAYLES_6] INSERT_USER_LECTURE");

		             		input.put("cmd" , "INSERT_USER_CLASS");
		             		input.put("user_lecture_seq_no" , user_lecture_seq_no);
		    		        output = interact.execute("/payment/PaymentMgr", input);
		    		        setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+xpay.m_szResCode+"]"+"[PAYLES_7] INSERT_USER_CLASS");

		             		input.put("cmd" , "UPDATE_PAYMENT_LECTURE");
		    		        input.put("status" , "03");
		    		        input.put("LGD_OID" ,LGD_OID);
		    				input.put("LGD_TID" ,LGD_TID);
		    				output = interact.execute("/payment/PaymentMgr", input);
		    				setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+xpay.m_szResCode+"]"+"[PAYLES_8] DB처리 끝");

		             		interact.commitTransaction();
		             		issuccess = true;
		             		try
		             		{ 	
		             			input.put("cmd" , "GET_USER_LECTURE_DETAIL");
			             		input.put("user_lecture_seq_no" , user_lecture_seq_no);
			    		        DataSet lectureInfo = interact.execute("/edu/LectureMgr", input);
			    		        String end_dd = "당해 연도";
			    		        if(lectureInfo != null ) end_dd = lectureInfo.getText("end_dd");
			    		        if(end_dd.length() == 8)
			    		        	end_dd = DateTime.trans(end_dd, "yyyyMMdd", "yyyy년 MM월 dd일");
			    		        if("".equals(end_dd)) end_dd = "당해 연도";
			    		        
			             		String mem_div_nm = "신규및승계자교육";
			        			String mem_div = lectureInfo.getText("lecture_div");
			        			if( "2".equals(mem_div)) mem_div_nm = "공중위생관리책임자교육";
			        			
			        			String add_msg = "";
			        			if( "2".equals(mem_div)) add_msg = "단, 반드시 "+end_dd+" 이내에 수강 완료하셔야 합니다.";
			        			
			        			String subject = "[한국건물위생관리협회]온라인교육비 결제확인";
			        			String sms_msg = LGD_BUYER+ "님 "+mem_div_nm+" 교육비 결제가 완료되었으니 나의강의실에서 시작하기 클릭 후 반드시 2주(14일) 이내로 수강 완료하시기 바랍니다. "+add_msg;
			        			
			        			String mmdd = DateTime.getFormatString("MMdd");
			        			if( "1219".equals(mmdd) && "2".equals(mem_div) )  
			        				sms_msg = LGD_BUYER+ "님 "+mem_div_nm+" 교육비 결제가 완료되었으니 나의강의실에서 시작하기 클릭 후 반드시 "+end_dd+"까지 수강 완료하시기 바랍니다. 반드시 당해 연도 이내에 수강 완료하셔야 합니다.";
			        			sendMMS( subject, sms_msg, LGD_BUYER , LGD_BUYERHP );
		             		}catch(Exception sse){sse.printStackTrace(); }
		             		
		             	}catch(Exception e){
		             		try
		    				{
		             			issuccess = false;
		             			isDBOK = false;
		             			interact.rollbackTransaction();
		    					input.put("cmd" , "UPDATE_PAYMENT_STATUS");
		    			        input.put("status" , "96");
		    					input.put("LGD_PAYKEY" ,LGD_PAYKEY);
		    					output = interact.execute("/payment/PaymentMgr", input);
		    				} catch(Exception ex2)
		    				{}
		             	}


		             	//최종결제요청 결과 성공 DB처리 실패시 Rollback 처리
		             	if( !isDBOK ) {
		             		xpay.Rollback("상점 DB처리 실패로 인하여 Rollback 처리 [TID:" +xpay.Response("LGD_TID",0)+",MID:" + xpay.Response("LGD_MID",0)+",OID:"+xpay.Response("LGD_OID",0)+"]");
		             		setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[LGD_RESPCODE:"+xpay.Response("LGD_RESPCODE",0)+"]"+"[PAYLES_95] DB처리 실패:" +xpay.Response("LGD_RESPMSG",0));

		             		LGD_RESPMSG = sb.toString();
		                     if( "0000".equals( xpay.m_szResCode ) ) {
		                    	 LGD_RESPMSG = "[결제실패]오류로 인한 자동취소가 정상적으로 완료 되었습니다.";
		                     }else{
		                    	 LGD_RESPMSG = "[결제실패]오류로 인한 자동취소가 정상적으로 처리되지 않았습니다.<br>관리자에게 문의 바랍니다.";
		                     }
		                    input.put("cmd" , "UPDATE_PAYMENT_STATUS");
	    			        input.put("status" , "95");
	    					input.put("LGD_PAYKEY" ,LGD_PAYKEY);
	    					output = interact.execute("/payment/PaymentMgr", input);
	    					issuccess = false;
		             	}

		             }else{
		            	setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+xpay.m_szResCode+"]"+"[PAYLES_94] 결제 실패 DB처리 시작");
		            	input.put("cmd" , "UPDATE_PAYMENT_STATUS");
    			        input.put("status" , "94");
    					input.put("LGD_PAYKEY" ,LGD_PAYKEY);
    					output = interact.execute("/payment/PaymentMgr", input);
		             	//최종결제요청 결과 실패 DB처리
		             	//out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");
		             }
		         }else {
		             //2)API 요청실패 화면처리
		             setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+xpay.m_szResCode+"]"+"[PAYLES_93]"+xpay.m_szResMsg);
		             input.put("cmd" , "UPDATE_PAYMENT_STATUS");
 			         input.put("status" , "93");
 					 input.put("LGD_PAYKEY" ,LGD_PAYKEY);
 					 output = interact.execute("/payment/PaymentMgr", input);
		         	//최종결제요청 결과 실패 DB처리
		         	//out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");
		         }

	    	}catch(Exception e) {
	    		sb = new StringBuffer();
	    		sb.append("LG텔레콤 제공 API를 사용할 수 없습니다. 환경파일 설정을 확인해 주시기 바랍니다. ");
	    		sb.append(""+e.getMessage());
	    		LGD_RESPMSG = sb.toString();
	    		setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[PAYLES_92]"+LGD_RESPMSG);
	    		input.put("cmd" , "UPDATE_PAYMENT_STATUS");
		        input.put("status" , "92");
				input.put("LGD_PAYKEY" ,LGD_PAYKEY);
				output = interact.execute("/payment/PaymentMgr", input);
	    	}
	   	}
	}catch(Exception e){
		sb = new StringBuffer();
		sb.append("결제 초기 데이터입력 실패 ");
		sb.append(""+e.getMessage());
		LGD_RESPMSG = sb.toString();
		setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[PAYLES_90]"+LGD_RESPMSG);

		input.put("cmd" , "UPDATE_PAYMENT_STATUS");
        input.put("status" , "90");
		input.put("LGD_PAYKEY" ,LGD_PAYKEY);
		output = interact.execute("/payment/PaymentMgr", input);
	}


%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>결제 결과</title>

</script>
</head>
<body>
<div class="popupLayer" style="">
	<a href="#" onclick="self.close();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
	<div class="popupCont_top">
		<div class="stit">
			결제내역
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
					<th scope="row">결제번호</th>
					<td><%= LGD_OID %></td>
				</tr>
				<tr>
					<th scope="row">결제내역</th>
					<td><%= input.getText("LGD_PRODUCTINFO")  %></td>
				</tr>
				<tr>
					<th scope="row">결제승인금액</th>
					<td><%= LGD_AMOUNT %></td>
				</tr>
				<tr>
					<th scope="row">결제결과메시지</th>
					<td height="120px" valign="middle" align="center" style="color:#5FB221;"><b><%=LGD_RESPMSG%><br>
					<% if( issuccess ){ %>
						[나의강의실]에서 동영상을 수강하시기 바랍니다.
					<% }%>
					
					</b></td>
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
<% if( issuccess ){ %>
	alert( '[나의강의실]에서 동영상을 수강하시기 바랍니다.' );

<% }%>
function fn_reload()
{
	opener.location = '/edu/payment/paymentCtrl.jspx?cmd=viewPaymentList';
	self.close();
}

</script>
</body></html>
