<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String callback_func = request.getParameter("callback_func");
	String sFileURL = request.getParameter("sFileURL");
	String sFileName = request.getParameter("sFileName");
	
	System.out.println("callback_func:"+callback_func);
	System.out.println("sFileURL:"+sFileURL);
	System.out.println("sFileName:"+sFileName);



%>
<script type="text/javascript">;
window.parent.CKEDITOR.tools.callFunction("<%=callback_func%>", "<%=sFileURL%>");
</script>