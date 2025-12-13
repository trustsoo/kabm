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
					for(var i=0; i<jsonCnt; i++){
													
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
						var titleHTML = "<div style='WIDTH:300px;' class='ellipsis'>" + title +"</div>";
						
						var viewHTML = "<a href=\"javascript:fn_lectureConfirm('"+user_lecture_seq_no+"');\"><b>수강하기</b></a>";
						
						var photoHTML = "<img width='100px' height='80px' style='opacity:0.2;' src='/static/com/img/if_photo.png'>";
						if( photo_path != '') photoHTML = "<img width='100px' src='${photoPath}/"+photo_path+"'>";
						
						var historyHTML = "<span class='btn btn-sm btn-primary' onclick=\"js_getUserClassInfo('"+user_class_seq_no+"','"+teacher+"');\"><b>보기</b></span>";
						
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td>'+( i+1 ) + '교시'+'</td>');
						listHtml.push('		  <td>'+photoHTML +'</td>');
						listHtml.push('		  <td>'+teacher+'</td>');
						listHtml.push('	  	  <td>'+historyHTML+'</td>');
						listHtml.push('	  	  <td>'+titleHTML+'</td>');
						listHtml.push('	  	  <td>'+status_nm+'</td>');
						listHtml.push('	  	  <td>'+estimate_cnt + '회'+'</td>');
						listHtml.push('	  	  <td>'+estimate_nm+'</td>');
						listHtml.push('	  	  <td>'+eduHTML+'</td>');
						listHtml.push('	</tr>');
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="8" style="text-align:center;">조회된 결과가 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				
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

function fn_historyView()
{
	jQuery('#teacher_hist').show();
}
function fn_historyHide()
{
	jQuery('#teacher_hist').hide();
}
function fn_downNote()
{
	alert('준비중입니다.');	
}
function fn_resultView(user_class_seq_no)
{
	location.href = '/edu/estimate/estimateCtrl.jspx?cmd=viewEstimateResult&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no='+user_class_seq_no;	
}

function fn_lectureList()
{
	location.href = '/edu/lecture/classCtrl.jspx?cmd=viewClassList';
}
function fn_lectureConfirm()
{
	<c:choose>
		<c:when test="${detail.status == '00' }">	
			var msg = '<b>교재 수령후 수강을 원하시면 “취소” 버튼을 누르시고, 교재 수령전 먼저 수강을 원하시면 “확인”을 눌러주시기 바랍니다.</b> <br><br>※ 교육승인이 되어 교재가 발송됩니다.<br> [온라인교육 ⇨ 결제내역]에서 배송지 수정이 가능하나 발송 처리된 이후로는 수정 불가하며, 송장번호도 조회하실 수 있습니다.';
			confirmOpen('교재 수령전 수강 확인', msg, fn_lectureView);
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
	
	var _url = '';
	<c:choose>
		<c:when test="${detail.status == '02' }">	
			_url = '/edu/lecture/classCtrl.jspx?cmd=doClass&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no=';
		</c:when>
		<c:when test="${detail.status == '00' }">
			var msg1 = '1. 반드시 2주(14일)안에 수강 완료하여야하며,\n기간이 지났을 경우 교육 재신청하여야합니다.';
			var msg2 = '2. 동영상 시청 중 제시되는 문제를 통과해야 다음 강의를 계속할 수 잇습니다.';
			var msg3 = '1. 반드시 12월31일까지 수강 완료하여야하며, 미수강시 당해연도 교육 미이수처리 됩니다.';
			var str = msg1 + '\n\n' + msg2;
			if( check_yearend() ) str = msg3 + '\n\n' + msg2;
			if( confirm( str ))
			{			
				_url = '/edu/lecture/classCtrl.jspx?cmd=doFirstClass&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no=';
			}
		</c:when>
		<c:otherwise>
			_url = '/edu/lecture/classCtrl.jspx?cmd=doClass&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no=';	
		</c:otherwise>
	</c:choose>	
	
	window.open(_url,'ESTIMATE' , 'top=10,left=10,width=1450px, height=960, scrollbars=yes, resizable=yes,resize=yes,toolbar=no,status=no');
	confirmNo();
}


jQuery(document).ready(function(){
	
	js_getUserClassList();
	
});
</script>
</head>

<body>
<div id="content">
		<!-- start :: content -->
		
		<div class="directions">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
				<tbody>
					<tr>
						<th scope="row">주의사항</th>
						<td>
							<span style="font-size:16px;color:red;font-weight:bold;">*아래 버튼 클릭이 안되시면 "알림마당 > 자주묻는질문" 에서 꼭 확인하시기 바랍니다. </span><br/>
							<span style="font-size:16px;color:red;font-weight:bold;">( <a href="/board/board.jspx?cmd=faq_list&faq_no=37"> 바로가기 클릭 </a> )</span><br/>
							- 총 3시간(3과목) 수강 완료해야하며, 매 과목당 정해진 시간 미이수시에는 다음 과목 시청이 불가능 합니다. <br/>
							- 동영상보기 “시작하기” 클릭후 반드시 2주(14일)일안에 수강 완료하여야하며,기간이 지났을 경우 나의강의실에서 '기간연장'을 클릭하세요.     <br />
							  &nbsp; &nbsp; ※ 단, 책임자교육 신청자의 경우 반드시 당해연도 12월31일까지 수강완료하여야합니다.  <br />
							- 학습내용을 얼마나 잘 이해하고 있는지 확인하기 위해 동영상 수강 중에 객관식 10문제의 퀴즈풀기가 있으며,  <br />&nbsp; 정답을 맞출 수 있는 횟수 제한은 없습니다. <br />
							<strong>※ 전 과목 수강을 다시보기 원하실 경우 나의강의실에서 수강완료 후 7일이내에 다시보기 가능합니다. <br> &nbsp;&nbsp;<font color="red">(7일 경과시 다시보기 절대불가)</font></strong>
							
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="LbtnWrap">
			<a href="javascript:fn_lectureList();"><span class="">취소</span></a>
			<c:choose><c:when test="${detail.status != '03' }"><a href="javascript:fn_lectureConfirm();"><span class=""><c:choose><c:when test="${detail.status == '00' }">시작하기</c:when><c:otherwise>계속하기</c:otherwise></c:choose></span></a></c:when></c:choose>
		</div>
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
					<tr>
						<th scope="row">강의유효기간</th>
						<td>
							<c:if test="${detail.status != '00' and detail.status != '03'}">	
								<span style="font-weight:bold;color:blue;">[${detail.user_lecture_start_dt} ~ ${detail.user_lecture_end_dt}]</span>
								<c:choose><c:when test="${detail.end_yn == 'N'}">	
									<br>강의 유효시간이 <span style="font-weight:bold;color:red;">${detail.diff}</span> 남았습니다.
								</c:when><c:otherwise>
									<br><span style="font-weight:bold;color:red;">강의 유효시간이 종료되었습니다. 교육 재신청하여야합니다.	(교육비환불 불가)</span>
								</c:otherwise></c:choose>
							</c:if>	
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		
		<div class="list">
			<table cellpadding="0" cellspacing="0" class="nobg" summary="" >
				<caption></caption>
				<colgroup>
					<col width="80px" /><col width="110px"/><col width="50px"/><col width=""/><col width="70px"/><col width="70px"/><col width="80px"/><col width="100px"/>
				</colgroup>
				<thead>
					<tr>
						<th scope="row">수업</th>
						<th scope="row" colspan="2">강사소개</th>
						<th scope="row">강사이력</th>
						<th scope="row">과목</th>
						<th scope="row">수강</th>
						<th scope="row">횟수</th>
						<th scope="row">평가</th>
						<th scope="row">교재</th>
					</tr>
				</thead>
				<tbody id="list_div">
				</tbody>
			</table>
		</div>
		</form>
		<div class="LbtnWrap">
			<a href="javascript:fn_lectureList();"><span class="">취소</span></a>
			<c:choose><c:when test="${detail.status != '03' }"><a href="javascript:fn_lectureConfirm();"><span class=""><c:choose><c:when test="${detail.status == '00' }">시작하기</c:when><c:otherwise>계속하기</c:otherwise></c:choose></span></a></c:when></c:choose>
		</div>
		<!-- end :: content -->
		</div>
	</div>


<div class="view" id="teacher_hist" style="border: 1px solid #1e90f1;border-right: 1px solid #1e90f1;z-index: 999;width:500px;display: none;position:fixed;left:40%;top:20%;background-color: #FFF;">
	<div style="z-index:999;font-size: 1.6em;font-weight:bold;color:#ffffff;background-color:#3b85c3;height: 2.25em;padding-top: 6px;padding-left: 10px;" >
	<span class="pull-left" style="padding: 7px 20px 0 0;" id="teacher_nm">
	강사이력
	</span>
	<span class="pull-right" style="padding: 0px 20px 0 0;">		
		<span onclick="fn_historyHide()" class="btn btn-sm btn-danger"><i class="fa fa-file"> 닫기</i></span>
	</span>
	</div>
	<div class="historyCont" id="historyCont">
		<strong>[학력사항]</strong><br />
		<p>
			□ 극동대학교 대학원 경영학과<br>    
			&nbsp;&nbsp;&nbsp;건물자산관리학 전공(경영학 박사)<br><br>
			&nbsp;&nbsp;&nbsp;논문 : 건물위생관리업의 현황과 발전요인에 관한 연구
		</p>
		<strong>[경력사항]</strong><br />
		<p>
			□ 현 한국건물위생관리협회 사무총장<br />
			□ 현 한국건축물관리연합회 사무총장<br />
			□ 현 서울특별시 명예공중위생감시원<br />
			□ NCS 환경미화 학술모듈 집필진<br />
			□ 건물위생관리사 이론 및 실기강사<br />
			□ 서비스업 안전보건강사<br />
			□ 한국산업표준(KS) 인증 심사위원<br />
			□ 농어촌관광사업 등급 현장 심사위원<br />
			□ 건축물위생관리지도사<br />
		</p>
	</div>
</div>

</body>
</html>