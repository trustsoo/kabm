<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script language='javascript'>
<%
String sEncodeData = requestReplace(request.getParameter("EncodeData"), "encodeData");
String sReserved1  = requestReplace(request.getParameter("param_r1"), "");
String sReserved2  = requestReplace(request.getParameter("param_r2"), "");
String sReserved3  = requestReplace(request.getParameter("param_r3"), "");
String url = request.getParameter("url");

%>

		function fnLoad()
		{

			parent.opener.parent.document.form_sms.EncodeData.value = "<%= sEncodeData %>";
			
			parent.opener.parent.document.form_sms.param_r1.value = "<%= sReserved1 %>";
			parent.opener.parent.document.form_sms.param_r2.value = "<%= sReserved2 %>";
			parent.opener.parent.document.form_sms.param_r3.value = "<%= sReserved3 %>";
			
			parent.opener.parent.smsResponse();
			
			self.close();
		}
</script>
</head>
<body onLoad="fnLoad()">
<%!
public static String requestReplace (String paramValue, String gubun) {
        String result = "";
        
        if (paramValue != null) {
        	
        	paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        	paramValue = paramValue.replaceAll("\\*", "");
        	paramValue = paramValue.replaceAll("\\?", "");
        	paramValue = paramValue.replaceAll("\\[", "");
        	paramValue = paramValue.replaceAll("\\{", "");
        	paramValue = paramValue.replaceAll("\\(", "");
        	paramValue = paramValue.replaceAll("\\)", "");
        	paramValue = paramValue.replaceAll("\\^", "");
        	paramValue = paramValue.replaceAll("\\$", "");
        	paramValue = paramValue.replaceAll("'", "");
        	paramValue = paramValue.replaceAll("@", "");
        	paramValue = paramValue.replaceAll("%", "");
        	paramValue = paramValue.replaceAll(";", "");
        	paramValue = paramValue.replaceAll(":", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll("#", "");
        	paramValue = paramValue.replaceAll("--", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll(",", "");
        	
        	if(gubun != "encodeData"){
        		paramValue = paramValue.replaceAll("\\+", "");
        		paramValue = paramValue.replaceAll("/", "");
            paramValue = paramValue.replaceAll("=", "");
        	}
        	
        	result = paramValue;
            
        }
        return result;
  }
%>
</body>
</html>