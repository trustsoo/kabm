<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%

String _dummy = String.valueOf( Math.random() );
%>

<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<body>

<% 
String LGD_RESPCODE = request.getParameter("LGD_RESPCODE");
String LGD_RESPMSG 	= request.getParameter("LGD_RESPMSG");
String LGD_MID		= request.getParameter("LGD_MID");

if("0000".equals(LGD_RESPCODE)){
	String LGD_PAYKEY = request.getParameter("LGD_PAYKEY");
	String LGD_PAYTYPE	= request.getParameter("LGD_PAYTYPE");
	String LGD_OID      = request.getParameter("LGD_OID");
	String LGD_AMOUNT      = request.getParameter("LGD_AMOUNT");
	String LGD_HASHDATA = request.getParameter("LGD_HASHDATA");
	String LGD_BUYEREMAIL      = request.getParameter("LGD_BUYEREMAIL");
	String LGD_BUYERIP      = request.getParameter("LGD_BUYERIP");
	String LGD_AFFILIATECODE      = request.getParameter("LGD_AFFILIATECODE");
	String LGD_FINANCECODE      = request.getParameter("LGD_FINANCECODE");
	String LGD_AUTHTYPE      = request.getParameter("LGD_AUTHTYPE");
	String LGD_FINANCENAME      = request.getParameter("LGD_FINANCENAME");
	String LGD_TIMESTAMP      = request.getParameter("LGD_TIMESTAMP");
	String LGD_BUYER      = request.getParameter("LGD_BUYER");
	String LGD_CLOSEDATE      = request.getParameter("LGD_CLOSEDATE");
	
	String lecture_seq_no = request.getParameter("lecture_seq_no");
	String user_seq_no = request.getParameter("user_seq_no");
%>

<div class="popupLayer" style="">
	<div class="alert alert-success">
		<button type="button" class="close" data-dismiss="alert">
			<i class="icon-remove"></i>
		</button>
		<i class="icon-ok green"></i>			
		<strong class="">▣  인증 처리중입니다..</strong>
	</div>
	<div class="popupCont_bottom ">
		
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
					<td class="a-center ">결과코드</td>
					<td class=" last"><%= LGD_RESPCODE %></td>
				</tr>
				<tr class="even pointer">
					<td class="a-center ">결과메시지</td>
					<td class=" last"><%= LGD_RESPMSG %></td>
				</tr>
				<tr class="even pointer">
					<td class="a-center ">안내</td>
					<td class=" last">잠시만 기다려주세요</td>
				</tr>
			</tbody>
			</table>		
		
	</div>	
	<div class="alert alert-success">
		<button type="button" class="close" data-dismiss="alert">
			<i class="icon-remove"></i>
		</button>
		<i class="icon-ok green"></i>			
		<strong class="">자동으로 완료되지 않을 때에는 아래 "계속"버튼을 눌러 계속 진행하시기 바랍니다.</strong>
	</div>
	<div class="btnWrap center">
		<a href="javascript:setLGDResult();" class="btn btn-success">계속</a>
		<a href="javascript:self.close();" class="btn btn-default">취소</a>
	</div>
</div>


<form method="post" name="LGD_PAYINFO" id="LGD_PAYINFO" action="">		
<input type="hidden" id="LGD_RESPCODE"	name="LGD_RESPCODE"	value="<%= LGD_RESPCODE %>"/>
<input type="hidden" id="LGD_RESPMSG"	name="LGD_RESPMSG"	value="<%= LGD_RESPMSG %>"/>
<input type="hidden" id="LGD_PAYKEY"	name="LGD_PAYKEY"	value="<%= LGD_PAYKEY %>"/>	
<input type="hidden" id="LGD_MID"		name="LGD_MID"		value="<%= LGD_MID %>"/>
<input type="hidden" id="LGD_OID"		name="LGD_OID"		value="<%= LGD_OID %>"/>
<input type="hidden" id="LGD_AMOUNT"	name="LGD_AMOUNT"		value="<%=LGD_AMOUNT  %>"/>
<input type="hidden" id="LGD_HASHDATA"	name="LGD_HASHDATA"		value="<%=LGD_HASHDATA  %>"/>
<input type="hidden" id="LGD_PAYTYPE"	name="LGD_PAYTYPE"	value="<%= LGD_PAYTYPE %>"/>
<input type="hidden" id="LGD_BUYEREMAIL"		name="LGD_BUYEREMAIL"		value="<%=LGD_BUYEREMAIL  %>"/>
<input type="hidden" id="LGD_BUYERIP"		name="LGD_BUYERIP"		value="<%=LGD_BUYERIP  %>"/>
<input type="hidden" id="LGD_AFFILIATECODE"		name="LGD_AFFILIATECODE"		value="<%=LGD_AFFILIATECODE  %>"/>
<input type="hidden" id="LGD_FINANCECODE"		name="LGD_FINANCECODE"		value="<%=LGD_FINANCECODE  %>"/>
<input type="hidden" id="LGD_AUTHTYPE"		name="LGD_AUTHTYPE"		value="<%=LGD_AUTHTYPE  %>"/>
<input type="hidden" id="LGD_FINANCENAME"		name="LGD_FINANCENAME"		value="<%=LGD_FINANCENAME  %>"/>
<input type="hidden" id="LGD_TIMESTAMP"		name="LGD_TIMESTAMP"		value="<%=LGD_TIMESTAMP  %>"/>
<input type="hidden" id="LGD_BUYER"		name="LGD_BUYER"		value="<%=LGD_BUYER  %>"/>
<input type="hidden" id="LGD_CLOSEDATE"		name="LGD_CLOSEDATE"		value="<%=LGD_CLOSEDATE  %>"/>

<input type="hidden" id="user_seq_no"	name="user_seq_no"	value="<%= user_seq_no %>"/>
<input type="hidden" id="lecture_seq_no"	name="lecture_seq_no"	value="<%= lecture_seq_no %>"/>

</form>

<script type="text/javascript">
function setLGDResult() {
	
	try {
		var LGD_PAYTYPE = document.getElementById('LGD_PAYTYPE').value;
		
		if( LGD_PAYTYPE == 'SC0040')
		{	
			document.getElementById("LGD_PAYINFO").action = "/mobile/edu/payment/paymentCtrl.jspx?cmd=paymentResult";
			document.getElementById("LGD_PAYINFO").submit();
		}else{
			document.getElementById("LGD_PAYINFO").action = "/mobile/edu/payment/paymentCtrl.jspx?cmd=paymentRes";
			document.getElementById("LGD_PAYINFO").submit();
		}

	} catch (e) {
		alert(e.message);
	}
}


window.onload= function () {
	setLGDResult();
}	
</script>

<%}else{%>

<div class="popupLayer" style="">
	<div class="alert alert-success">
		<button type="button" class="close" data-dismiss="alert">
			<i class="icon-remove"></i>
		</button>
		<i class="icon-ok green"></i>			
		<strong class="">▣  인증처리에 실패하였습니다.</strong>
	</div>
	<div class="popupCont_bottom ">
		
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
					<td class="a-center ">결과코드</td>
					<td class=" last"><%= LGD_RESPCODE %></td>
				</tr>
				<tr class="even pointer">
					<td class="a-center ">결과메시지</td>
					<td class=" last"><%= LGD_RESPMSG %></td>
				</tr>
			</tbody>
			</table>		
		
	</div>	
	<div class="btnWrap center">
		<a href="javascript:self.close();" class="btn btn-default">취소</a>
	</div>
</div>

<%}%>

</body>
</html>