<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ include file="/common/common.jsp"%>    
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="cls_list" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="cur_user_class_seq_no" type="java.lang.String" scope="request" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="/static/edu/js/common.js?cmd=111"></script>
<script type="text/javascript" class="source">

function fn_lectureView(lecture_seq_no , isReq , std_yyyy )
{
	if( isReq == 'Y')
	location.href = '/edu/lecture/lectureCtrl.jspx?cmd=viewLectureView&lecture_seq_no='+lecture_seq_no+'&std_yyyy='+std_yyyy;
	else
		msgStart('수강신청 가능 기간이 아닙니다.');
}


function fn_lectureConfirm()
{
	<c:choose>
		<c:when test="${detailLecture.status == '00' }">	
			var msg = '<b>교재 수령후 수강을 원하시면 “취소” 버튼을 누르시고, 교재 수령전 먼저 수강을 원하시면 “확인”을 눌러주시기 바랍니다.</b> <br><br>※ 교육승인이 되어 교재가 발송됩니다.<br> [온라인교육 ⇨ 결제내역]에서 배송지 수정이 가능하나 발송 처리된 이후로는 수정 불가하며, 송장번호도 조회하실 수 있습니다.';
			confirmOpen('교재 수령전 수강 확인', msg, fn_lectureView);
		</c:when>
		<c:otherwise>
			fn_estimateView();
		</c:otherwise>
	</c:choose>	
}
function fn_estimateView()
{	
	<c:if test="${detailLecture.status == '09'}">	
		var msg = '평가 횟수(5회)를 초과하였습니다. 사무국(02)465-5400으로 연락하여 재신청하시기 바랍니다.';
		alert( msg );
		return;
	</c:if>

	<c:if test="${detailLecture.status == '01' or detailLecture.status == '02'}">	
		<c:if test="${detailLecture.end_yn == 'Y'}">
			var msg = '강의 유효시간이 종료되었습니다. "교육수강"메뉴에서 기간 연장 후 진행하세요.';
			alert( msg );
			return;
		</c:if>	
	</c:if>
	
	var _url = '';
	<c:choose>
		<c:when test="${detailLecture.status == '02' }">	
			_url = '/edu/lecture/classCtrl.jspx?cmd=doClass&user_lecture_seq_no=${input.user_lecture_seq_no}&user_class_seq_no=';
		</c:when>
		<c:when test="${detailLecture.status == '00' }">
			var msg1 = '1. 반드시 2주(14일)안에 수강 완료하여야하며,\n기간이 지났을 경우 교육 재신청하여야합니다.';
			var msg2 = '2. 한과목당 평가하기 5번이상 불합격시 교육 재신청하여야합니다.';
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
	
}

//초계산
function getTimer(){
	
	var now = moment();
	
	<c:if test="${detailLecture.end_yn != 'Y'}">
		var to = moment('${detailLecture.user_lecture_end_dt}:59' , 'YYYY.MM.DD HH:mm:ss'); 
		//to = to.endOf('day'); 
		var ms = moment(to,"DD/MM/YYYY HH:mm:ss").diff(moment(now,"DD/MM/YYYY HH:mm:ss"));
		var md = moment.duration(ms);
		var days = ( md.asDays()|0);
		var hours = md.hours();
		var mins = md.minutes();
		var secs = md.seconds() ;
		
		if( days <= 0 ) days = '';
		else days = days +  "일 ";
		
		if( days <= 0 && hours <= 0 ) hours = '';
		else hours = hours + "시간 ";
		
		var s = days + hours + mins +"분 "+ secs + "초";
		$("#diffTime").html( s );
	</c:if>
	
	$("#nowTime").html( moment(now).format('YYYY년 MM월 DD일 HH:mm:ss') );
	
	setTimeout("getTimer()", 1000);
}


jQuery(document).ready(function(){
	
	getTimer();
});

</script>

</head>

<body>
<br>
<div class="mypage">
	<div class="section-1">
		<ul class="applybtns">
			<li class="">
				<strong>신규개설 및 승계자 교육</strong>
				<p>
					수강신청기간 <br />
					- ${new_start } ~ ${new_end }
				</p>
				<div class="">
					<a href="javascript:fn_lectureView(${new_lecture_seq_no} , '${new_req}' , '${new_std_yyyy}' );" class="pbtn02"><span class="">교육신청</span></a>
				</div>
			</li>
			<li class="">
				<strong>공중위생관리책임자 교육</strong>
				<p>
					수강신청기간 <br />
					- ${old_start } ~ ${old_end }
				</p>
				<div class="">
					<a href="javascript:fn_lectureView(${old_lecture_seq_no} , '${old_req}' , '${old_std_yyyy}' );" class="pbtn02_1"><span class="">교육신청</span></a>
				</div>
			</li>
		</ul>
	</div>

<c:if test="${input.isDoing == 'true'}">

    <div class="edu_tit2 ">
        <strong>학습진행현황</strong>
    </div>
    <div class="section-1 mt0">
        <ul class="eduStep">
<% if( cls_list != null && cls_list.getCount("user_class_seq_no") >0 ) 
{
	for( int idx=0; idx < cls_list.getCount("user_class_seq_no") ; idx++)
	{
		String pass_yn = cls_list.getText("estimate_pass_yn",idx);
		String user_class_seq_no = cls_list.getText("user_class_seq_no",idx);
		
			if( pass_yn != null && "Y".equals(pass_yn) ){
%>		                                    
                        <li class="on">
 <% 		}else if( user_class_seq_no != null && user_class_seq_no.equals(cur_user_class_seq_no) ){ %>                         
                        <li class="ing">
 <% 		}else{ %>                         
                        <li>
<%			} %>
							<strong><%=cls_list.getText("class_no",idx) %></strong>
                            <span class="">교시</span>
                        </li>
<%
	} 
}
%>                                 
	      </ul>
	
	
      <div class="limit_cont_1">
          <a href="javascript:fn_lectureConfirm();" class="pbtn03 bold"><span class="pbtn03 bold bul">강의바로가기</span></a>
          <span class="txtBox gray">${detailLecture.user_lecture_start_dt}</span>
          <span class="red bold" id="nowTime"></span>
          <span class="txtBox blue">${detailLecture.user_lecture_end_dt}</span>
          
      </div>

      <div class="limit_cont_2">
          <span class="blue">강의 유효시간이 </span> 
          <c:if test="${detailLecture.end_yn == 'Y'}"><span class="red" id="diffTime">종료되었습니다. 교육수강현황에서 확인하세요.</span><span class="blue" style="font-weight:bold;"><a href="/edu/lecture/classCtrl.jspx?cmd=viewClassList">[확인하러 가기]</a></span> </c:if>
          <c:if test="${detailLecture.end_yn != 'Y'}"><span class="red" id="diffTime"></span> 남았습니다.</c:if>
      </div>
  </div>
</c:if>            
                
<!-- 
	<div class="edu_tit mt20">
		<strong>수강종료</strong>
	</div>

	<div class="section-1 mt0">
		<div class="eduBtn">
			<a href="#"><img src="/static/main/img/sub/eduEnd_btn_1.jpg"alt="" /></a>
			<a href="#"><img src="/static/main/img/sub/eduEnd_btn_2.jpg"alt="" /></a>
		</div>
	</div> -->
</div>

</body>
</html>