<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>    
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
					



<div class="loc_title">${output[0].jisaname}</div>	

<div class="loc_wrap">
	<div class="ico1">지회장소개</div>
	<div class="dummy" style="height:20px;"></div>
	<div class="loc_con">
		<div class="loc_pic">
			<c:if test="${fn:length(output[0].file_no) > 0}">
				<img src="${input.thumb_url}/${output[0].file_path}/${output[0].file_nm}" width="68" height="75" >
			</c:if>
		</div>
		<div class="loc_txt">
			<div class="boss">
				<span class="bossNm">${output[0].daepyo}</span>
				<span class="bossLc">지회장</span>
			</div>
			
			<div class="ico2">지회장인사말</div>
			<div class="comment">${output[0].intro}</div>
			
		</div>
	</div>
</div>

<div class="dummy" style="height:15px;"></div>
<div class="loc_detail">
	<table>
	<tbody>					
		<tr>
			<th class="ico5">사무총장</th>
			<td class="offName">${output[0].samu_nm}</td>
		</tr>
		<tr>
			<th class="ico4">상세정보</th>
			<td>
				우 : ${output[0].zip_code}<br>
				${output[0].addr}<br> 
				TEL : ${output[0].tel}<br>
				FAX : ${output[0].fax}<br>
				<a href="mailto:${output[0].email}">email: ${output[0].e_mail}</a>
				&nbsp;
			</td>
		</tr>
	</tbody>	
	</table>			
</div>

<div style="clear:both;"></div>
