<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>


<c:forEach begin="0" end="${output_count}" step="1" var="index">
<c:if test="${fn:length(output[index].file_no) > 0}">
	 <li><a href="#"  onclick="javascrip:window.open('${output[index].link}');"><img src="${input.thumb_url}/${output[index].file_path}/${output[index].file_nm}" alt="${output[index].title}" /></a></li>
</c:if>
</c:forEach>