<%
/******************************************************************************** 
 * Program ID	:  수강목록
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

<script type="text/javascript" class="source">

function gotoPage(page_no)
{	
	document.frm.page_no.value = page_no;
	fn_getClassList();
}


function fn_extension(lecture_seq_no, user_lecture_seq_no)
{
	jQuery.ajax({
		url : '/edu/estimate/action/estimateAction.jspx?cmd=setTermExtend', 
		data : 'lecture_seq_no='+lecture_seq_no+'&user_lecture_seq_no=' + user_lecture_seq_no,
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			var isok = false;
			if(jsonObj.result.code == '200'){
				msgStart(msg_edu_code_006);
				gotoPage('1');
			}else{
				msgStart( msg_com_code_010);
			}
		}
	});
	
}


function fn_getClassList()
{
	jQuery.ajax({
		url : '/edu/estimate/action/estimateAction.jspx?cmd=getEstimateList', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.estimate_info;
				var jsonCnt = jsonObj.result.data.estimate_info.length;
				jQuery('#tot_cnt').val(jsonObj.result.data.property[0].tot_cnt);
				var curday = getToday();
				
				if(jsonCnt > 0){
					var i=0;
					for(; i<jsonCnt; i++){
													
						var lecture_seq_no  = urlDecode(json[i].lecture_seq_no); 
						var user_lecture_seq_no  = urlDecode(json[i].user_lecture_seq_no); 
						var title   = urlDecode(json[i].title); 
						var start_dt = urlDecode(json[i].start_dt);
						var end_dt = urlDecode(json[i].end_dt);
						var status = urlDecode(json[i].status);
						var cls_status = urlDecode(json[i].cls_status);
						var status_nm = urlDecode(json[i].status_nm);
						var cls_status_nm = urlDecode(json[i].cls_status_nm);
						var class_no = json[i].class_no;
						var certication_file_path = urlDecode(json[i].certication_file_path); 
						var realPath = '${input.certifyPath}/'+certication_file_path;
						var certication_file_path_proof = urlDecode(json[i].certication_file_path_proof); 
						var realPath_proof = '${input.certifyPath}/'+certication_file_path_proof;
						
						var end_yn = json[i].end_yn;
						var teminate_yn = json[i].teminate_yn;
						var close_yn = json[i].close_yn;
												
						var std_yyyy = urlDecode(json[i].std_yyyy);
						var start_dd = urlDecode(json[i].start_dd);
						var end_dd = urlDecode(json[i].end_dd);
						var std_end_dd = urlDecode(json[i].std_end_dd);
						var req_start_dd =  start_dd;
						var req_end_dd =  end_dd;
						var isReq = 'N';
						
						if( std_end_dd >=  curday  ) 
						{ 
						  isReq = 'Y';
						}
						
						var user_lecture_start_dt = urlDecode(json[i].user_lecture_start_dt);
						var user_lecture_end_dt = urlDecode(json[i].user_lecture_end_dt);
						
						var titleHTML =  "<div style='WIDTH:200px;display: inline-block;' class='ellipsis'>" + title +"</div>";
						var btnHTML = "";
						if( status == '00' )
						{
							user_lecture_end_dt = '';
							start_dt = '수강시작전';
							titleHTML = "<div style='WIDTH:200px;display: inline-block;' class='ellipsis'><a href='javascript:fn_lectureView(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + isReq + "\")'>" + title +"</a></div>" ;
							btnHTML =  "<a href='javascript:fn_lectureView(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + isReq + "\")' class='btn btn-primary'><span>시작하기</span></a>";
						}
						
						if(class_no != '' && status != '03' ){
							status_nm = '<a href="javascript:fn_lectureView(\'' + lecture_seq_no + '\',\'' + user_lecture_seq_no + '\',\'' + isReq + '\');" class="pbtn01_1 mini" ><span>'+class_no + '교시 ' + cls_status_nm+'</span></a>';
							titleHTML = "<div style='WIDTH:200px;display: inline-block;' class='ellipsis'><a href='javascript:fn_lectureView(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + isReq + "\")'>" + title +"</a></div>";
							btnHTML =  "<a href='javascript:fn_lectureView(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + isReq + "\")' class='btn btn-primary'><span>계속하기</span></a>";
						}
						if( (status == '01' || status == '02') &&  end_yn == 'Y' ){
							status_nm = '<a href="javascript:fn_extension(\'' + lecture_seq_no + '\',\'' + user_lecture_seq_no + '\');" class="pbtn01_1 mini" ><span>기간연장</span></a>';
							titleHTML =  "<div style='WIDTH:200px;display: inline-block;' class='ellipsis'>" + title +"</div>";
							btnHTML = "";
						}
						if( (status == '01' || status == '02') &&  teminate_yn == 'Y' ){
							status_nm = '<span class="tred">수강기간종료</span>'; 
							titleHTML =  "<div style='WIDTH:200px;display: inline-block;' class='ellipsis'>" + title +"</div>";
							btnHTML = "";
						}
						
						if( status == '03' && close_yn == 'N'){
							//btnHTML =  "<a href='javascript:fn_lectureReView(\"" + user_lecture_seq_no + "\")' class='pbtn01_1 mini'><span>다시보기</span></a>";
							btnHTML = "<a href='#' class='btn btn-primary' onclick='javascript:fn_lectureReView(\"" + user_lecture_seq_no + "\")'><span>다시보기</span></a>";
						}
						
						if( status != '00' && status != '03' ) end_dt = '진행중';
						
						
						
						listHtml.push("<li>");
						listHtml.push("<div class='message_date'>");
						listHtml.push(btnHTML);
						listHtml.push("</div>");
						listHtml.push("<div class='message_wrapper'>");
						listHtml.push("<h4 class='heading'><i class='fa fa-circle-o'></i> "+status_nm+"</h4>" );
						listHtml.push("<blockquote class='message'>수강가능 : "+user_lecture_end_dt+"</blockquote>");
						listHtml.push("<blockquote class='message'>"+titleHTML+"</blockquote>");
						listHtml.push("<p class='url'>");
						listHtml.push("<i class='fa fa-clock-o'></i> " + start_dt + " ~ " + end_dt);
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

function fn_lectureReView( user_lecture_seq_no)
{	
	if(!confirm('모바일네트워크(3G/LTE/5G)로 연결하시면 데이터통화료가 발생할 수 있으니 와이파이(Wi-Fi)로 연결하시어 동영상수강을 권장합니다. \n 계속하시겠습니까?')) return;
	
	location.href = '/mobile/edu/class/classCtrl.jspx?cmd=reViewClass&user_lecture_seq_no=' + user_lecture_seq_no;
}

function fn_lectureView(lecture_seq_no,user_lecture_seq_no,isReq)
{
	if( isReq == 'Y')
		location.href = '/mobile/edu/class/classCtrl.jspx?cmd=view&lecture_seq_no='+lecture_seq_no+'&user_lecture_seq_no='+user_lecture_seq_no;
	else
		msgStart('교육수강 가능 기간이 아닙니다.');
}

jQuery(document).ready(function(){
	
	gotoPage('${input.page_no}'); 
	
});

</script>
</head>

<body>

<!-- search -->
<form id="frm" name="frm" method="post" action="">
<input type="hidden" id="rnum" name="rnum" value="100"/>
<input type='hidden' name='page_no' id='page_no' value='1'>
<input type='hidden' name='user_seq_no' id='user_seq_no' value='${input.s_user_seq_no}'>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
</form>
<div id="content">
	<!-- start :: content -->
	<!-- start :: content -->
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
				<p>총 3시간(3과목) 수강 완료해야하며, 매 과목당 정해진 시간 미이수시에는 다음 과목 시청이 불가능합니다.</p>
			</div>
		</div>
		<div class="mail_list">
			<div class="left"><i class="fa fa-paperclip"></i></div>
			<div class="right">
				<p>동영상보기 ‘시작하기’ 클릭 후 반드시 2주(14일)이내에 수강 완료해야하며, 기간이 지났을 경우 ‘기간연장’을 클릭하세요.</p>
			</div>
		</div>
		<strong>※ (단, 책임자교육 신청자의 경우 반드시 당해연도 12월31일까지 수강 완료하여야 합니다.)</strong><br><br>
		<div class="mail_list">
			<div class="left"><i class="fa fa-paperclip"></i></div>
			<div class="right">
				<p>학습내용을 얼마나 잘 이해하고 있는지 확인하기 위해 동영상 수강 중에 객관식 10문제의 퀴즈풀기가 있으며, 정답을 맞출 수 있는 횟수 제한은 없습니다.</p>
			</div>
		</div>
		<strong>※ 전 과목 수강을 다시보기 원하실 경우 나의강의실에서 수강완료 후 7일이내에 다시보기 가능합니다. <br> &nbsp;&nbsp;<font color="red">(7일 경과시 다시보기 절대불가)</font></strong>
	</div>
	
	
    <div class="x_panel">
      <div class="x_title">
        <h2>수강목록</h2>                    
        <div class="clearfix"></div>
      </div>
      <div class="x_content" >
      	<ul class='edu_list' id='eduList'>
      	
        </ul>
      </div>
    </div>
	
	
<!-- end :: content -->
</div>


</body>
</html>