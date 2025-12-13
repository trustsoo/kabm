<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%
String LGD_RESPCODE = request.getParameter("LGD_RESPCODE");
String LGD_RESPMSG 	= request.getParameter("LGD_RESPMSG");

Map payReqMap = request.getParameterMap();

String _dummy = String.valueOf( Math.random() );
%>

<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<link href="/static/main/css/sub.css?_dummy=<%=_dummy %>" type="text/css" rel="stylesheet"  media="screen" />
<link href="/static/main/css/popup.css?_dummy=<%=_dummy %>" type="text/css" rel="stylesheet"  media="screen" />
	
</head>
<body>
<div class="popupLayer" style="">
	<div class="popupCont_top">
		<div class="stit">
			인증결과
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
					<th scope="row">결과코드</th>
					<td><%= LGD_RESPCODE %></td>
				</tr>
				<tr>
					<th scope="row">결과메시지</th>
					<td><%= LGD_RESPMSG %></td>
				</tr>	
				<tr>
					<th scope="row">안내</th>
					<td>잠시만 기다려 주세요...
					<br><br>
					</td>
				</tr>
				</tbody>
			</table>
			<br>
			<span style="color:red;font-weight:bold;">자동으로 완료되지 않을 때에는 아래 "계속"버튼을 눌러 계속 진행하시기 바랍니다.</span>
			<br><br>
			<button type="submit" onclick="setLGDResult()" style="width:100px; height:30px;">계속</button>
		</div>
		
	</div>
</div>
<form method="post" name="LGD_RETURNINFO" id="LGD_RETURNINFO">
<%
for (Iterator i = payReqMap.keySet().iterator(); i.hasNext();) {
	Object key = i.next();
	if (payReqMap.get(key) instanceof String[]) {
		String[] valueArr = (String[])payReqMap.get(key);
		for(int k = 0; k < valueArr.length; k++)
			out.println("<input type='hidden' name='" + key + "' id='"+key+"'value='" + valueArr[k] + "'/>");
	} else {
		String value = payReqMap.get(key) == null ? "" : (String) payReqMap.get(key);
		out.println("<input type='hidden' name='" + key + "' id='"+key+"'value='" + value + "'/>");
	}
}
%>
</form>

<script type="text/javascript">
	
		window.onload= function () {
			try {
				parent.payment_return();
			} catch (e) {
				alert(e.message);
			}
	    }
		function setLGDResult() {
			
			try {
				parent.payment_return();
			} catch (e) {
				alert(e.message);
			}
		}
		
	</script>
</body>
</html>