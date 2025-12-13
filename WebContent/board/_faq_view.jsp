<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body>
<div id="content">
<!-- start :: content -->
<!-- start :: content -->


	<div class="faqWrap">
		<ul id="faq">
		 <li>
		 	<a class='tit'><%=output.getText("question") %></a>
		 	<div class='answer'>
				<%=new jdf.framework.core.data.schema.format.UnEscapeHtmlFormatter("4.0").format(output.getText("answer")) %>
		 	</div>
		 </li>
		</ul>
	</div>
	
</div>
</body>
</html>