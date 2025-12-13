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

DataSet output = new DataSet();
InteractionBean interact = new InteractionBean();

%>
<%

	String msg = "";

   	try
   	{
   		String lecture_seq_no = input.getText("lecture_seq_no");
   		String user_seq_no = input.getText("user_seq_no");

   		interact.beginTransaction();

   		if(lecture_seq_no == null || "".equals(lecture_seq_no))
   		{
   			msg = "FAIL:강의번호오류";
   			throw new Exception();
   		}
   		if(user_seq_no == null || "".equals(user_seq_no))
   		{
   			msg = "FAIL:수강자정보오류";
   			throw new Exception();
   		}
   		String time_stamp = DateTime.getTimestampString();
   		String _OID = "TR_" + StringFormater.fillZero(user_seq_no, 6) + "_" + StringFormater.fillZero(lecture_seq_no,3) + "_" + time_stamp;


   		input.put("cmd" , "INSERT_USER_LECTURE");
    	output = interact.execute("payment/PaymentMgr", input);
   		String user_lecture_seq_no = output.getText("user_lecture_seq_no");

   		if(user_lecture_seq_no == null || "".equals(user_lecture_seq_no))
   			throw new Exception();

   		input.put("cmd" , "INSERT_USER_CLASS");
   		input.put("user_lecture_seq_no" , user_lecture_seq_no);
    	output = interact.execute("payment/PaymentMgr", input);

    	input.put("cmd" , "INSERT_PAYMENT_NOCASH");
    	input.put("LGD_OID",_OID);
   		input.put("user_lecture_seq_no" , user_lecture_seq_no);
    	output = interact.execute("payment/PaymentMgr", input);


    	msg ="SUCCESS";
   		interact.commitTransaction();
   	}catch(Exception e){
   		if( interact != null)
   			interact.rollbackTransaction();
   		if( "".equals(msg)) msg="FAIL:입력중 오류발생!";

   	}finally{

   	}


%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>무통장 결제 결과메시지</title>

</head>
<body>

<div class="popupLayer" id="payInputLayer">
	<a href="#" onclick="self.close();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
	<div class="popupCont_top">
		<div class="stit">
			무통장 결제 결과메시지
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
						<td  valign="middle" align="center" style="color:#5FB221;"><b><%=msg%></b></td>
					</tr>
				</tbody>
			</table>
		</div>			
		<div class="btnWrap">
			<a href="javascript:window.close();" class="pbtn01" ><span class="">확인</span></a>
		</div>
	</div>
</div>


</body></html>
