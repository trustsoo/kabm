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
				msgOpen(msg_edu_code_006);
				window.location.reload();
			}else{
				msgOpen( msg_com_code_010);
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
					for(var i=0; i<jsonCnt; i++){
													
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
							start_dt = '';
							titleHTML = "<div style='WIDTH:200px;display: inline-block;' class='ellipsis'><a href='javascript:fn_lectureView(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + isReq + "\")'>" + title +"</a></div>" ;
							btnHTML =  "<a href='javascript:fn_lectureView(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + isReq + "\")' class='pbtn01_1 mini'><span>시작하기</span></a>";
						}
						
						if(class_no != '' && status != '03' ){
							status_nm = '<a href="javascript:fn_lectureView(\'' + lecture_seq_no + '\',\'' + user_lecture_seq_no + '\',\'' + isReq + '\');" class="pbtn01_1 mini" ><span>'+class_no + '교시 ' + cls_status_nm+'</span></a>';
							titleHTML = "<div style='WIDTH:200px;display: inline-block;' class='ellipsis'><a href='javascript:fn_lectureView(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + isReq + "\")'>" + title +"</a></div>";
							btnHTML =  "<a href='javascript:fn_lectureView(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + isReq + "\")' class='pbtn01_1 mini'><span>계속하기</span></a>";
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
							btnHTML = "<a href='#' class='pbtn04' onclick='javascript:fn_lectureReView(\"" + user_lecture_seq_no + "\")'><span>다시보기</span></a>";
						}
						
						if( status != '03' ) end_dt = '';
						
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td>'+titleHTML+'</td>');
						listHtml.push('		  <td style="border-left: 0px;">'+btnHTML+'</td>');
						listHtml.push('		  <td>'+user_lecture_end_dt+'</td>');
						listHtml.push('	  	  <td>'+start_dt+'</td>');
						listHtml.push('	  	  <td>'+end_dt+'</td>');
						listHtml.push('	  	  <td>'+status_nm+'</td>');
						
						listHtml.push('	</tr>');
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="7" style="text-align:center;">조회된 결과가 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				
				js_userPaging('pagingDiv', jQuery('#page_no').val(), jQuery('#tot_cnt').val(), jQuery('#rnum').val(), 'gotoPage');
			}
		}
	});
	
}

function fn_lectureReView( user_lecture_seq_no)
{
	_url = '/edu/lecture/classCtrl.jspx?cmd=reViewClass&user_lecture_seq_no=' + user_lecture_seq_no;
	window.open(_url,'REVIEW' , 'top=10,left=10,width=1450px, height=960, scrollbars=yes, resizable=yes,resize=yes,toolbar=no,status=no');
}

function fn_lectureView(lecture_seq_no,user_lecture_seq_no,isReq)
{
	if( isReq == 'Y')
		location.href = '/edu/lecture/classCtrl.jspx?cmd=viewClassView&lecture_seq_no='+lecture_seq_no+'&user_lecture_seq_no='+user_lecture_seq_no;
	else
		msgStart('교육수강 가능 기간이 아닙니다.');
}

function fn_certDown(lecture_seq_no,user_lecture_seq_no,end_dt, cert_div)
{
	var url = '/edu/lecture/action/lectureAction.jspx?cmd=certDown&cert_div='+cert_div+'&lecture_seq_no='+lecture_seq_no+'&user_lecture_seq_no='+user_lecture_seq_no+'&end_dt='+end_dt;
	jQuery("#certfrm input[name='cert_div']").val(cert_div);
	jQuery("#certfrm input[name='lecture_seq_no']").val(lecture_seq_no);
	jQuery("#certfrm input[name='user_lecture_seq_no']").val(user_lecture_seq_no);
	jQuery("#certfrm input[name='end_dt']").val(end_dt);
	
	document.certfrm.submit();
}

jQuery(document).ready(function(){
	
	gotoPage('${input.page_no}'); 
	
});

</script>
</head>

<body>
<form id="certfrm" name="certfrm" method="post" action="/edu/lecture/action/lectureAction.jspx?cmd=certDown" target="certPoP">
<input type="hidden" id="cert_div" name="cert_div" value=""/>
<input type="hidden" id="lecture_seq_no" name="lecture_seq_no" value=""/>
<input type="hidden" id="user_lecture_seq_no" name="user_lecture_seq_no" value=""/>
<input type="hidden" id="end_dt" name="end_dt" value=""/>
</form>

<!-- search -->
<form id="frm" name="frm" method="post" action="">
<input type="hidden" id="rnum" name="rnum" value="20"/>
<input type='hidden' name='page_no' id='page_no' value='1'>
<input type='hidden' name='user_seq_no' id='user_seq_no' value='${input.s_user_seq_no}'>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>

<div id="content">
		<!-- start :: content -->
		<!-- start :: content -->
			<div class="directions" style="margin-bottom:20px;">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<tbody>
						<tr>
							<th scope="row">수강방법</th>
							<td>
										제목 하단의 「해당 과목」을 선택 후 「시작하기/계속하기」 클릭하여 수강하세요.<br>
										(수강 최초에는 ‘시작하기’로 보여지고, 시작하기 클릭 이후부터는 ‘계속하기’로 보여집니다.)<br>
										
							</td>
						</tr>
						<tr>
							<th scope="row" style="border-right: 0px solid #ce9702;height:10px;" colspan="2"><hr></th>
						</tr>
						<tr>
							<th scope="row">주의사항</th>
							<td>
										- 총 3시간(3과목) 수강 완료해야하며, 매 과목당 정해진 시간 미이수시에는 다음 과목 시청이 불가능합니다.<br>
										- 동영상보기 ‘시작하기’ 클릭 후 반드시 2주(14일)이내에 수강 완료해야하며, 기간이 지났을 경우 ‘기간연장’을 클릭하세요.	<br>									  
										  &nbsp;&nbsp;(단, 책임자교육 신청자의 경우 반드시 당해연도 12월31일까지 수강 완료하여야 합니다.)<br>
										- 학습내용을 얼마나 잘 이해하고 있는지 확인하기 위해 동영상 수강 중에 객관식 10문제의 퀴즈풀기가 있으며, <br> &nbsp;&nbsp;정답을 맞출 수 있는 횟수 제한은 없습니다.<br>
										<strong>※ 전 과목 수강을 다시보기 원하실 경우 나의강의실에서 수강완료 후 7일이내에 다시보기 가능합니다. <br> &nbsp;&nbsp;<font color="red">(7일 경과시 다시보기 절대불가)</font></strong>

										
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="list_type2">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width=""/><col width="10%"/><col width="15%"/>
						<col width="10%"/><col width="10%"/><col width="15%"/>
					</colgroup>
					<thead>
						<tr>
							<th colspan="2">제목</th>
							<th>수강가능일</th>
							<th>시작일</th>
							<th>종료일</th>
							<th>상태</th>
							<!-- <th>수료증</th> -->
							<!-- <th>수료확인증</th> -->
							<!-- <th>영수증</th> -->
						</tr>
					</thead>
					<tbody id="list_div">
						<tr>
							<td colspan="7">검색된 결과가 없습니다</td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="paging" id='pagingDiv'></div>


		<!-- end :: content -->
		</div>

<iframe name="certPoP" width="1" height="0"></iframe>
</body>
</html>