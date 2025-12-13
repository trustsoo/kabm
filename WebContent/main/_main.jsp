<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>    
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>

<body>


<div class="alert alert-success">
	<button type="button" class="close" data-dismiss="alert">
		<i class="icon-remove"></i>
	</button>
	<i class="icon-ok green"></i>			
	<strong class="green">
		<small>님</small>
	</strong> 환영합니다. 좋은 하루 되세요.
</div>

</body>
</html>