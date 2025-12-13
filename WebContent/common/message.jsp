<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="msg" value="${message }"/>
<c:choose>
<c:when test="${message == null or message == '' }">
 <c:set var="msg" value="Something happened that we didn't expect. Please Contact our administrator."/>
</c:when>
<c:otherwise>
<c:if test="${ fn:indexOf(message, ': ')  > 0}">
	<c:set var="msg" value="${ fn:substring( message, fn:indexOf(message, ': ') , -1 )}"/>
</c:if>
</c:otherwise>
</c:choose>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

	<div class="error-container">
		<div class="well">
			<h1 class="grey lighter smaller">
				<span class="blue bigger-125">
					<i class="icon- fa fa-random"></i>
					Error
				</span>
				Something Went Wrong
			</h1>
	
			<hr />
			<h3 class="lighter smaller">
				<c:out value="${msg }"/>
			</h3>
	
			<div class="space"></div>
	
			<div>
				<h4 class="lighter smaller">Meanwhile, try one of the following:</h4>
	
				<ul class="list-unstyled spaced inline bigger-110 margin-15">
					<li>
						<i class="icon- fa fa-hand-o-right blue"></i>
						Read the faq
					</li>
	
					<li>
						<i class="icon- fa fa-hand-o-right blue"></i>
						Give us more info on how this specific error occurred!
					</li>
				</ul>
			</div>
	
			<hr />
			<div class="space"></div>
	
			<div class="center">
				<a href="javascript:history.go(-1);" class="btn btn-grey">
					<i class="fa af-arrow-left"></i>
					Go Back
				</a>
	
				<!-- a href="#" class="btn btn-primary">
					<i class="icon- fa fa-dashboard"></i>
					Dashboard
				</a-->
			</div>
		</div>
	</div>
</body>
</html>	