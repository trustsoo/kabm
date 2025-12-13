<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>

<!--  start : 팝업 /pop -->
<c:if test="${output[0].popup_type == 'popup_type_01'}">
	<!DOCTYPE html>

	<html lang="ko">
	<head>
		<meta charset="utf-8">
		<title>${output[0].title}</title>
		<link rel="stylesheet" type="text/css" href="/static/main/css/common.css?_=<%=String.valueOf( Math.random() )%>" />
		<link rel="stylesheet" type="text/css" href="/static/main/css/popup.css" />
		<script type="text/javascript" src="/static/com/js/common.js?_=<%=String.valueOf( Math.random() )%>"></script>
	</head>

	<body>

	<div class="popupLayer" style="min-height: 200px;">
		<a href="#" onclick="self.close();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
		<div class="popupCont_top">
			<div class="stit">
				${output[0].title}
			</div>
		</div>

		<c:choose>
		<c:when test="${output[0].view_div eq 'view_div_01'}">		
			<c:if test="${fn:length(output[0].file_no) > 0}">
					<c:choose>
					<c:when test="${output[0].link_method eq 'link_method_pop'}">	 
						
						<c:choose>
							<c:when test="${ output[0].link != '' && output[0].link ne null}">	 
								<a href="#"  onclick="javascrip:window.open('${output[0].link}');"><img style="width:100%;" src="/common/action/attach.jspx?cmd=doDownload&file_no=${output[0].file_no}" alt="${output[0].title}" /></a>
							</c:when>
							<c:otherwise>
								<img style="width:100%;" src="/common/action/attach.jspx?cmd=doDownload&file_no=${output[0].file_no}" alt="${output[0].title}" />
							</c:otherwise>
						</c:choose>
							
						
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${ output[0].link != '' && output[0].link ne null}">	 
								<a href='${output[0].link}'"><img style="width:100%;" src="/common/action/attach.jspx?cmd=doDownload&file_no=${output[0].file_no}" alt="${output[0].title}" /></a>
							</c:when>
							<c:otherwise>
								<img style="width:100%;" src="/common/action/attach.jspx?cmd=doDownload&file_no=${output[0].file_no}" alt="${output[0].title}" />
							</c:otherwise>
						</c:choose>
					
					
									
					</c:otherwise>
					</c:choose>
			</c:if>
		</c:when>   	
		<c:otherwise>	
			
				<div class="popupCont_bottom ">	
					${output[0].contents}

					
				</div>
				
				
		</c:otherwise>
		</c:choose>

		<div style=" padding-left:10px; bottom:0px; text-align: 1">
					<a href="#" onclick="setCookie('popup_layer_${output[0].seq_no}','N',1) ;self.close();"><img src="/static/com/img/checkbox.jpg" style="vertical-align: -6px;"> 오늘하루 그만보기</a>
		</div>
	</div>
	</body>
	</html>
</c:if>
<!--  end : 팝업 /pop -->



<!--  start : 팝업 레이어 -->
<c:if test="${output[0].popup_type == 'popup_type_02'}">
	<!--STR : 팝업 위치 결정-->
	<c:set var="align_attr"  value="left:50%;  top:200px;"  />

	<c:if test="${open_idx eq 1}">
		<c:set var="align_attr"  value="left:55%; top:220px;"  />
	</c:if>
	<!--END : 팝업 위치 결정-->




	<div class="mainpopLayer" id="popup_layer_${output[0].seq_no}" style="${align_attr} width:${output[0].width}px; height:${output[0].height}px;">
		<a href="#" onclick="$('#popup_layer_${output[0].seq_no}').hide();return false;" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
		
		<div class="mainpopCont_top">	
			<div class="stit">
				${output[0].title}
			</div>
		</div>
		
		<div class="mainpopCont_bottom ">
			<div class="main_contents">			
				<c:choose>
				   	<c:when test="${output[0].view_div eq 'view_div_01'}">		
						<c:if test="${fn:length(output[0].file_no) > 0}">
								<c:choose>
								<c:when test="${output[0].link_method eq 'link_method_pop'}">	 
									<c:choose>
										<c:when test="${ output[0].link != '' && output[0].link ne null}">	 
											<a href="#"  onclick="javascrip:window.open('${output[0].link}');"><img style="width:100%;" src="/common/action/attach.jspx?cmd=doDownload&file_no=${output[0].file_no}" alt="${output[0].title}" /></a>
										</c:when>
										<c:otherwise>
											<img style="width:100%;" src="/common/action/attach.jspx?cmd=doDownload&file_no=${output[0].file_no}" alt="${output[0].title}" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${ output[0].link != '' && output[0].link ne null}">	 								
											<a href='${output[0].link}'"><img style="width:100%;" src="/common/action/attach.jspx?cmd=doDownload&file_no=${output[0].file_no}" alt="${output[0].title}" /></a>
										</c:when>
										<c:otherwise>
											<img style="width:100%;" src="/common/action/attach.jspx?cmd=doDownload&file_no=${output[0].file_no}" alt="${output[0].title}" />
										</c:otherwise>
									</c:choose>		
								</c:otherwise>
								</c:choose>
						</c:if>
				   	</c:when>   	
				   	<c:otherwise>		
						${output[0].contents}
					</c:otherwise>
				</c:choose>
				
				
			</div>			
		</div>

		<div style="width: 200px; height:30px; position:absolute; padding-left:10px; bottom:0px; text-align: 1">
				<a href="#" onclick="setCookie('popup_layer_${output[0].seq_no}','N',1) ;$('#popup_layer_${output[0].seq_no}').hide();"><img src="/static/com/img/checkbox.jpg" style="vertical-align: -6px;"> 오늘하루 그만보기</a>
		</div>
	</div>
</c:if>
<!--  end : 팝업 레이어 -->
