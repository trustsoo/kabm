<%
/******************************************************************************** 
 * Program ID	:  강좌목록
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
<style>
.historyCont {
    padding: 10px 10px;
    color: #575757;
    font-size:13px;
}
.historyCont strong{color:#0a07d4;font-size:16px;}
.historyCont p{margin:10px 0;}
</style>
<script type="text/javascript" src="/static/edu/js/common.js?cmd=111"></script>
<script type="text/javascript" class="source">


function js_getUserClassList()
{
	jQuery.ajax({
		url : '/edu/lecture/action/classAction.jspx?cmd=getUserClassList', 
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
				
				if(jsonCnt > 0){
					
					var i=0;
					for(; i<jsonCnt; i++){
													
						var user_lecture_seq_no  = json[i].user_lecture_seq_no; 
						var user_class_seq_no = json[i].user_class_seq_no; 
						var lecture_seq_no  = json[i].lecture_seq_no; 
						var class_no  = json[i].class_no; 
						var title   = urlDecode(json[i].title); 
						var status   = json[i].status;
						var status_nm   = urlDecode(json[i].status_nm);
						var estimate_pass_yn = json[i].estimate_pass_yn;
						var estimate_nm   = urlDecode(json[i].estimate_nm);
						var estimate_cnt   = json[i].estimate_cnt;
						
						var teacher   = urlDecode(json[i].teacher);
						var history_path   = urlDecode(json[i].history_path);
						var edu_path   = urlDecode(json[i].edu_path);
						var photo_path = urlDecode(json[i].photo_path);
						
						var eduHTML = "<a href=\"javascript:fn_downNote('"+class_no+"');\"><b>다운</b></a>";
						if( edu_path == '') eduHTML = "교재참고";
						var titleHTML = "<div style='' class='ellipsis'>" + title +"</div>";
						
						var viewHTML = "<a href=\"javascript:fn_lectureConfirm('"+user_lecture_seq_no+"');\"><b>수강하기</b></a>";
						
						var photoHTML = "<img class='avatar' width='100px' height='80px' style='opacity:0.2;' src='/static/com/img/if_photo.png'>";
						if( photo_path != '') photoHTML = "<img class='avatar' width='100px' src='${photoPath}/"+photo_path+"'>";
						
						var historyHTML = "<span class='btn btn-sm btn-primary' onclick=\"js_getUserClassInfo('"+user_class_seq_no+"','"+teacher+"');\"><b>보기</b></span>";
						
						
						listHtml.push("<li>");
						listHtml.push(photoHTML);
						listHtml.push("<div class='message_date'>");
						listHtml.push("<h3 class='month'>"+( i+1 )+"</h3>");
						listHtml.push("<p class='day'>교시</p>");
						listHtml.push("</div>");
						listHtml.push("<div class='message_wrapper'>");
						listHtml.push("<h4 class='heading'><i class='fa fa-circle-o'></i> "+status_nm+"</h4>" );
						listHtml.push("<blockquote class='message'>"+titleHTML+"</blockquote>");
						listHtml.push("<p class='url'>");
						listHtml.push("<i class='fa fa-user'></i> " + teacher);
						listHtml.push("</p>");
						listHtml.push("</div>");
						listHtml.push("</li>");
						
					}
					
					if( i > 0 )
						jQuery('#eduList').html(listHtml.join(''));
						
				}
				
				
			}
		}
	});
	
}

function js_getUserClassInfo(user_class_seq_no,teacher)
{
	jQuery.ajax({
		url : '/edu/lecture/action/classAction.jspx?cmd=getClassInfo', 
		data : 'user_class_seq_no='+user_class_seq_no,
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			var summary  = '';
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.class_info;
				var jsonCnt = jsonObj.result.data.class_info.length;
				
				if(jsonCnt > 0){					
					summary  = unescapeHTML(json[0].summary);
				}
			}
			jQuery('#historyCont').html(summary);
			jQuery('#teacher_nm').html(teacher + ' 강사이력');
			
			fn_historyView();
		}
	});
	
}

function fn_resultView(user_class_seq_no)
{
	location.href = '/edu/estimate/estimateCtrl.jspx?cmd=viewEstimateResult&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no='+user_class_seq_no;	
}

function fn_lectureList()
{
	location.href = '/mobile/edu/class/classCtrl.jspx?cmd=list';
}
function fn_lectureConfirm()
{
	<c:choose>
		<c:when test="${detail.status == '00' }">	
			var msg = '교재 수령후 수강을 원하시면 “취소” 버튼을 누르시고, 교재 수령전 먼저 수강을 원하시면 “확인”을 눌러주시기 바랍니다.\n\n※ 교육승인이 되어 교재가 발송됩니다. [온라인교육 ⇨ 결제내역]에서 배송지 수정이 가능하나 발송 처리된 이후로는 수정 불가하며, 송장번호도 조회하실 수 있습니다.';
			if(confirm(msg)) fn_lectureView();
		</c:when>
		<c:otherwise>
			fn_lectureView();
		</c:otherwise>
	</c:choose>	
}
function fn_lectureView()
{	
	<c:if test="${detail.status == '09'}">	
		var msg = '평가 횟수(5회)를 초과하였습니다. 사무국(02)465-5400으로 연락하여 재신청하시기 바랍니다.';
		alert( msg );
		return;
	</c:if>

	<c:if test="${detail.status == '01' or detail.status == '02'}">	
		<c:if test="${detail.end_yn == 'Y'}">	
			var msg = '강의 유효시간이 종료되었습니다. 교육 재신청해야합니다.';
			alert( msg );
			return;
		</c:if>	
	</c:if>
	
	if(!confirm('모바일네트워크(3G/LTE/5G)로 연결하시면 데이터통화료가 발생할 수 있으니 와이파이(Wi-Fi)로 연결하시어 동영상수강을 권장합니다. \n 계속하시겠습니까?')) return;
	
	var _url = '';
	<c:choose>
		<c:when test="${detail.status == '02' }">	
			_url = '/mobile/edu/class/classCtrl.jspx?cmd=doClass&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no=';
		</c:when>
		<c:when test="${detail.status == '00' }">
			var msg1 = '1. 반드시 2주(14일)안에 수강 완료하여야하며,\n기간이 지났을 경우 교육 재신청하여야합니다.';
			var msg2 = '2. 동영상 시청 중 제시되는 문제를 통과해야 다음 강의를 계속할 수 잇습니다.';
			var msg3 = '1. 반드시 12월31일까지 수강 완료하여야하며, 미수강시 당해연도 교육 미이수처리 됩니다.';
			var str = msg1 + '\n\n' + msg2;
			if( check_yearend() ) str = msg3 + '\n\n' + msg2;
			if( confirm( str ))
			{			
				_url = '/mobile/edu/class/classCtrl.jspx?cmd=doFirstClass&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no=';
			}
		</c:when>
		<c:otherwise>
			_url = '/mobile/edu/class/classCtrl.jspx?cmd=doClass&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no=';	
		</c:otherwise>
	</c:choose>	
	
	//window.open(_url,'ESTIMATE' , 'top=10,left=10,width=1450px, height=960, scrollbars=yes, resizable=yes,resize=yes,toolbar=no,status=no');
	location.href = _url;
}


jQuery(document).ready(function(){
	
	js_getUserClassList();
	
});
</script>
</head>

<body>
<div id="content">

<form id="frm" name="frm" method="post" action="" onsubmit="return false;">
	<input type="hidden" id="lecture_seq_no" name="lecture_seq_no" value="${input.lecture_seq_no}"/>
	<input type="hidden" id="user_lecture_seq_no" name="user_lecture_seq_no" value="${input.user_lecture_seq_no}"/>
</form>		
<!-- start :: content -->
		
	        
    <div class="x_panel">
      <div class="x_title">
        <h2>${detail.title}</h2>                    
        <div class="clearfix"></div>
      </div>
      <div class="x_content" >
      	<c:if test="${detail.status != '00' and detail.status != '03'}">	
			<span style="font-weight:bold;color:blue;">[${detail.user_lecture_start_dt} ~ ${detail.user_lecture_end_dt}]</span>
			<c:choose><c:when test="${detail.end_yn == 'N'}">	
				<br>강의 유효시간이 <span style="font-weight:bold;color:red;">${detail.diff}</span> 남았습니다.
			</c:when><c:otherwise>
				<br><span style="font-weight:bold;color:red;">강의 유효시간이 종료되었습니다. 교육 재신청하여야합니다.	(교육비환불 불가)</span>
			</c:otherwise></c:choose>
		</c:if>	
      </div>
    </div>
	
	
	<div class="x_content">
		<button type="button" onclick="fn_lectureList();" class="btn btn-dark pull-right" >취소</button>
		<c:choose><c:when test="${detail.status != '03' }"><button type="button" onclick="fn_lectureConfirm();" class="btn btn-primary" ><c:choose><c:when test="${detail.status == '00' }">시작하기</c:when><c:otherwise>계속하기</c:otherwise></c:choose></button></c:when></c:choose>
	</div>
	
	
            
    <div class="x_panel">
      <div class="x_title">
        <h2>강의목록</h2>                    
        <div class="clearfix"></div>
      </div>
      <div class="x_content" >
      	<ul class='class_list' id='eduList'>
      	
        </ul>
      </div>
    </div>
	
	
	<div class="x_content">
		<button type="button" onclick="fn_lectureList();" class="btn btn-dark pull-right" >취소</button>
		<c:choose><c:when test="${detail.status != '03' }"><button type="button" onclick="fn_lectureConfirm();" class="btn btn-primary" ><c:choose><c:when test="${detail.status == '00' }">시작하기</c:when><c:otherwise>계속하기</c:otherwise></c:choose></button></c:when></c:choose>
	</div>
	

</div>	

	
	<div class="alert alert-custom alert-dismissible " role="alert">
		<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
		</button>
		<div class="mail_list">
			<div class="left"><i class="fa fa-star"></i></div>
			<div class="right">
				<h3>주의사항 </h3>
			</div>
		</div>
		<div class="mail_list">
			<div class="left"><i class="fa fa-paperclip"></i></div>
			<div class="right">
				<p>총 3시간(3과목) 수강 완료해야하며, 매 과목당 정해진 시간 미이수시에는 다음 과목 시청이 불가능 합니다.</p>
			</div>
		</div>
		<div class="mail_list">
			<div class="left"><i class="fa fa-paperclip"></i></div>
			<div class="right">
				<p>동영상보기 “시작하기” 클릭후 반드시 2주(14일)일안에 수강 완료하여야하며,기간이 지났을 경우 나의강의실에서 '기간연장'을 클릭하세요.</p>
				<p>※ 단, 책임자교육 신청자의 경우 반드시 당해연도 12월31일까지 수강완료하여야합니다.</p>
			</div>
		</div>
		<div class="mail_list">
			<div class="left"><i class="fa fa-paperclip"></i></div>
			<div class="right">
				<p>학습내용을 얼마나 잘 이해하고 있는지 확인하기 위해 동영상 수강 중에 객관식 10문제의 퀴즈풀기가 있으며, 정답을 맞출 수 있는 횟수 제한은 없습니다.</p>
			</div>
		</div>
		<strong>※ 전 과목 수강을 다시보기 원하실 경우 나의강의실에서 수강완료 후 7일이내에 다시보기 가능합니다. <br> &nbsp;&nbsp;<font color="red">(7일 경과시 다시보기 절대불가)</font></strong>
	</div>
</body>
</html>