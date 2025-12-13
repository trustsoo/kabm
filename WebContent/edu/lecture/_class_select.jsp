<%
/******************************************************************************** 
 * Program ID	:  강좌선택
 * FileName		: 
 * @version		: 1.0
 *  Comment		: 
 ********************************************************************************/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ include file="/common/common.jsp"%>

<html>
<head>
<script type="text/javascript" src="/static/edu/js/common.js?cmd=111"></script>
<script type="text/javascript" class="source">


function js_getUserClassList()
{
	jQuery.ajax({
		url : '/edu/lecture/action/classAction.jspx?cmd=getUserClassAllList', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.class_info;
				var jsonCnt = jsonObj.result.data.class_info.length;
				
				var chk = 0;
				if(jsonCnt > 0){
					var statusCnt = 0;
					for(var i=0; i<jsonCnt; i++){
						var status   = json[i].status;
						if( status == '0' ) statusCnt++;
					}
					for(var i=0; i<jsonCnt; i++){
													
						var user_lecture_seq_no  = json[i].user_lecture_seq_no; 
						var lecture_seq_no  = json[i].lecture_seq_no; 
						var class_no  = json[i].class_no; 
						var title   = urlDecode(json[i].title); 
						var status   = json[i].status;
						
						var play_time   = json[i].play_time;
						var teacher   = urlDecode(json[i].teacher);
						var photo_path = urlDecode(json[i].photo_path);
						
						
						var titleHTML = "<div style='WIDTH:300px;' class='ellipsis'>" + title +"</div>";
						
						var viewHTML = "<a href=\"javascript:fn_lectureConfirm('"+user_lecture_seq_no+"');\"><b>수강하기</b></a>";
						
						var photoHTML = "<img width='100px' height='80px' style='opacity:0.2;' src='/static/com/img/if_photo.png'>";
						if( photo_path != '') photoHTML = "<img width='100px' src='${photoPath}/"+photo_path+"'>";
						
						var classDiv = "선택" + ( i);
						var checkHTML = "";
						var checked = '';
						var clicked = '';						
						if( class_no == '1'){
							classDiv = "필수";
							checked = 'checked';
							clicked = "onclick='fn_clickFirst();return false;' onkeydown='fn_clickFirst();return false;'";
						}else{
							if( status == '0' && statusCnt <= 4 ) checked = 'checked';
						}
												
						
						if( status == '0' || status == '9' ) checkHTML = "<input type='checkbox' name='class_nos' "+checked+" "+clicked+" class='check' value='"+class_no+"'>";
												
						if( status == '0' ) chk++;
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td style="font-weight:bold;">'+classDiv+'</td>');
						listHtml.push('		  <td style="text-align:left;">'+checkHTML+'</td>');
						listHtml.push('		  <td>'+photoHTML +'</td>');
						listHtml.push('		  <td>'+teacher+'</td>');
						listHtml.push('	  	  <td>'+titleHTML+'</td>');
						listHtml.push('	  	  <td>'+play_time+' 분</td>');
						listHtml.push('	</tr>');
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="6" style="text-align:center;">조회된 결과가 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				
				if(chk == 6 ) 
				{
					jQuery('input:checkbox[name="class_nos"]').each(function(){
						if( this.val() != '1') 
							this.checked = false;
					});
				}
			}
		}
	});
	
}

function fn_clickFirst()
{
	alert('필수 수강 과목입니다.');
	return false;
}
function fn_lectureList()
{
	location.href = '/edu/lecture/classCtrl.jspx?cmd=viewClassList';
}

function fn_lectureConfirm()
{
	var chkCnt = 0;
	jQuery('input:checkbox[name="class_nos"]').each(function(){
		if(this.checked)
			chkCnt = chkCnt + 1;
	});
	
	if( chkCnt != 4 ){
		alert( '강의는  반드시 4과목을 수강하셔야 합니다. 4과목을 선택하세요.');
		return;
	}
	var http = jQuery.ajax( {
   		url: "/edu/lecture/action/classAction.jspx?cmd=setSelectClass&templet-bypass",
   		type: "POST",
		data : jQuery('#frm').serialize(true),
   		async:false,
   		beforeSend : function(){
		},
		error 	: function(xml)
   		{	
			msgStart(msg_com_code_007);
		},
   		success: function(xml)
   		{
   			//alert( msg_est_code_001);
   			var lecture_seq_no = jQuery('#lecture_seq_no').val();
   			var user_lecture_seq_no = jQuery('#user_lecture_seq_no').val();
   			
   			location.href = '/edu/lecture/classCtrl.jspx?cmd=viewClassView&step=next&lecture_seq_no='+lecture_seq_no+'&user_lecture_seq_no='+user_lecture_seq_no;
   		}
  	});	
}

jQuery(document).ready(function(){
	
	js_getUserClassList();
	
});
</script>
</head>

<body>
<div id="content">
		<!-- start :: content -->
		
		<form id="frm" name="frm" method="post" action="" onsubmit="return false;">
		<input type="hidden" id="lecture_seq_no" name="lecture_seq_no" value="${input.lecture_seq_no}"/>
		<input type="hidden" id="user_lecture_seq_no" name="user_lecture_seq_no" value="${input.user_lecture_seq_no}"/>
		<div class="edu_info" style="padding: 10px 0;">
			<table cellpadding="0" cellspacing="0" class="" summary="" style="width:100%">
				<caption></caption>
				<colgroup>
					<col width="100px"/><col width=""/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row">제목</th>
						<td>${detail.title}</td>
					</tr>
					<tr>
						<th scope="row">평가금액</th>
						<td>${ el:formatIntByString(detail.amount) }</td>
					</tr>
					<tr>
						<th scope="row">강의설명</th>
						<td>${detail.summary}</td>
					</tr>					
				</tbody>
			</table>
		</div>
		<div class="directions" style="margin-top: 10px;padding:5px 10px;">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
				<tbody>
					<tr>
						<th scope="row">주의사항</th>
						<td>
							
							<c:choose>
							<c:when test="${input.lecture_div == '1' }">
								<strong>1. 수업시간은 <font color="red"><과목당 60분></font> 입니다. </strong><br>
								<strong>2. 반드시 <font color="red">4과목 교육이수</font> 하셔야 합니다.</strong><br/>
								<strong style="padding-left:20px;">(1) 필수과목 : 공중위생관리법</strong><br/>
								<strong style="padding-left:20px;">(2) 선택과목 : 5과목 중 3과목 선택</strong><br/>
								<strong>3. 6과목 교육수강을 원하시는 경우, <font color="red">[나의강의실]에서 수강완료 후 7일이내에 다시보기</font> 가능 <br> &nbsp;&nbsp;<font color="red">(※ 7일이후 다시보기 절대불가)</font></strong>
							</c:when>
							<c:otherwise>
								<span style="font-size:16px;color:red;font-weight:bold;">* 필수과목(법령)을 제외한 나머지 과목(5과목) 중 수강하고자 하는 3과목을 선택하여 수강하세요.</span><br/>
								<strong>※ 전 과목 수강을 다시보기 원하실 경우 나의강의실에서 수강완료 후 7일이내에 다시보기 가능합니다. <br> &nbsp;&nbsp;<font color="red">(7일 경과시 다시보기 절대불가)</font></strong>
							</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="LbtnWrap">
			<a href="javascript:fn_lectureList();"><span class="">취소</span></a>
			<a href="javascript:fn_lectureConfirm();"><span class="">선택하기</span></a>
		</div>
		
		
		<div class="list" style="margin-top: 10px;">
			<table cellpadding="0" cellspacing="0" class="nobg" summary="" >
				<caption></caption>
				<colgroup>
					<col width="50px" /><col width="100px" /><col width="110px"/><col width="100px"/><col width=""/><col width="100px"/>
				</colgroup>
				<thead>
					<tr>
						<th scope="row" colspan="2">수업</th>
						<th scope="row" colspan="2">강사소개</th>
						<th scope="row">과목</th>
						<th scope="row">강의시간</th>
					</tr>
				</thead>
				<tbody id="list_div">
				</tbody>
			</table>
		</div>
		</form>
		
		<!-- end :: content -->
		</div>
	</div>

</body>
</html>