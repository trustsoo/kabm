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
String user_lecture_seq_no = input.getText("user_lecture_seq_no");
String user_id = input.getText("user_id");
String LGD_OID = input.getText("LGD_OID");
String _TID = input.getText("LGD_TID");
String LGD_RESPMSG = "";
StringBuffer sb = new StringBuffer();
DataSet output = new DataSet();
InteractionBean interact = new InteractionBean();

%>
<%
	
	/*
     * [결제취소 요청 페이지]
     *
     * LG텔레콤으로 부터 내려받은 거래번호(LGD_TID)를 가지고 취소 요청을 합니다.(파라미터 전달시 POST를 사용하세요)
     * (승인시 LG텔레콤으로 부터 내려받은 PAYKEY와 혼동하지 마세요.)
     */
                                                                                        //상점아이디(자동생성)
    String LGD_TID              = _TID;                      //LG텔레콤으로 부터 내려받은 거래번호(LGD_TID)
    LGD_TID     				= ( LGD_TID == null )?"":LGD_TID;

    XPayClient xpay = new XPayClient();
    xpay.Init(configPath, CST_PLATFORM);
    xpay.Init_TX(LGD_MID);
    xpay.Set("LGD_TXNAME", "Cancel");
    xpay.Set("LGD_TID", LGD_TID);

    setLog( LGD_OID,  user_id, LGD_TID,  "결제취소 시작" );
    /*
     * 1. 결제취소 요청 결과처리
     *
     * 취소결과 리턴 파라미터는 연동메뉴얼을 참고하시기 바랍니다.
     */

    if (xpay.TX()) {
        //1)결제취소결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
        LGD_RESPMSG = xpay.m_szResMsg;
        setLog( LGD_OID,  user_id, LGD_TID,  "결제 취소요청이 완료" );
        try
        {
        	interact.beginTransaction();
        	input.put("cmd" , "UPDATE_PAYMENT_CANCEL");
	        input.put("status" , "99");
			input.put("LGD_TID" ,LGD_TID);
			output = interact.execute("payment/PaymentMgr", input);

			input.put("cmd" , "UPDATE_USER_LECTURE_STATUS");
	        input.put("status" , "99");
			input.put("user_lecture_seq_no" ,user_lecture_seq_no);
			output = interact.execute("payment/PaymentMgr", input);

			input.put("cmd" , "UPDATE_USER_CLASS_STATUS");
	        input.put("status" , "9");
			input.put("user_lecture_seq_no" ,user_lecture_seq_no);
			output = interact.execute("payment/PaymentMgr", input);

			interact.commitTransaction();
        }catch(Exception e){
        	LGD_RESPMSG = "결제완료 후 취소 상태 적용 실패[관리자에게 문의하십시요]";
        	try
			{
     			interact.rollbackTransaction();
			} catch(Exception ex2)
			{}
        }
    }else {
        //2)API 요청 실패 화면처리
        setLog( LGD_OID,  user_id, LGD_TID,  "["+xpay.m_szResCode+"]" + xpay.m_szResMsg );
        LGD_RESPMSG = xpay.m_szResMsg;
    }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>결제취소 결과</title>

</head>

<body>

<div class="popupLayer" id="payInputLayer">
	<a href="#" onclick="self.close();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
	<div class="popupCont_top">
		<div class="stit">
			결제취소 결과메시지
		</div>
	</div>
	<div class="popupCont_bottom">
		<div class="edu_info">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
				<caption></caption>
				<colgroup>
					<col width="200"/><col width=""/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row">결제 결과메시지</th>
						<td  valign="middle" align="center" style="color:#5FB221;"><b><%=LGD_RESPMSG%></b></td>
					</tr>
				</tbody>
			</table>
		</div>			
		<div class="btnWrap">
			<a href="javascript:fn_reload();" class="pbtn01" ><span class="">확인</span></a>
		</div>
	</div>
</div>

<script type="text/javascript">

function fn_reload()
{
	opener.js_getPaymentList();
	self.close();
}

</script>
</body>
</html>