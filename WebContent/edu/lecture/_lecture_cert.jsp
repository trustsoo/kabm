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


function fn_getClassList()
{
	jQuery.ajax({
		url : '/edu/estimate/action/estimateAction.jspx?cmd=getCertList', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.cert_info;
				var jsonCnt = jsonObj.result.data.cert_info.length;
				jQuery('#tot_cnt').val(jsonObj.result.data.property[0].tot_cnt);
				var curday = getToday();
				
				if(jsonCnt > 0){
					for(var i=0; i<jsonCnt; i++){
						var lecture_seq_no  = urlDecode(json[i].lecture_seq_no); 
						var user_lecture_seq_no  = urlDecode(json[i].user_lecture_seq_no); 
						var title   = urlDecode(json[i].title); 
						var start_dt = urlDecode(json[i].start_dt);
						var end_dt = urlDecode(json[i].end_dt);
						var end_dt_ko = urlDecode(json[i].end_dt_ko);
						var float_end_dt = urlDecode(json[i].float_end_dt);
						var status = urlDecode(json[i].status);
						var status_nm = urlDecode(json[i].status_nm);
						var addr = urlDecode(json[i].addr);
						var corp_addr = urlDecode(json[i].corp_addr);
						var corp_nm = urlDecode(json[i].corp_nm);
						var building_nm = urlDecode(json[i].building_nm);
						var building_addr = urlDecode(json[i].building_addr);
						var reg_dt = urlDecode(json[i].reg_dt);
						var mod_dt = urlDecode(json[i].mod_dt);
						
						var cert_building_nm = urlDecode(json[i].cert_building_nm);
						var cert_addr_nm = urlDecode(json[i].cert_addr_nm);
						var cert_addr = urlDecode(json[i].cert_addr);
						var std_yyyy = urlDecode(json[i].std_yyyy);
						var survey_yn = urlDecode(json[i].survey_yn);
						var lecture_div = urlDecode(json[i].lecture_div);
						console.log(survey_yn);
						if( status == '00' )
						{
							user_lecture_end_dt = '';
							start_dt = '';							
						}
						
						var cert_pathHTML = '수강중';
						var end_pathHTML = '수강중';
						if(status == '03')
						{
							if( reg_dt == '' || reg_dt == 'N' ){
								end_pathHTML = "<a href='javascript:fn_certConfirm(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + end_dt_ko + "\",\"" + float_end_dt + "\",\"" + std_yyyy + "\",\"" + lecture_div + "\",1);' class='pbtn01_1 mini'><span>확인</span></a>";
								cert_pathHTML = '확인전';
							}else{
								if(survey_yn == 'Y')
								{
									cert_pathHTML = reg_dt;
									end_pathHTML = "<a href='javascript:fn_certDown(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + end_dt_ko + "\",\"" + float_end_dt + "\",\"" + std_yyyy + "\",\"" + lecture_div + "\",1);' class='pbtn01_1 mini'><span>받기</span></a>";
									
									building_nm = cert_building_nm;
								}else{
									end_pathHTML = "<a href='javascript:fn_survey(\"" + lecture_seq_no + "\",\"" + user_lecture_seq_no + "\",\"" + end_dt_ko + "\",\"" + float_end_dt + "\",\"" + std_yyyy + "\",\"" + lecture_div + "\",1);' class='pbtn01_1 mini'><span>받기</span></a>";
									
								}
							}
						}
							
						
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td>'+std_yyyy+'년</td>');
						listHtml.push('		  <td>'+title+'</td>');
						listHtml.push('	  	  <td>'+start_dt+'</td>');
						listHtml.push('	  	  <td>'+end_dt+'</td>');
						listHtml.push('	  	  <td>'+corp_nm+'</td>');
						listHtml.push('	  	  <td>'+corp_addr+'</td>');
						listHtml.push('	  	  <td>'+building_nm+'</td>');
						listHtml.push('	  	  <td>'+building_addr+'</td>');
						listHtml.push('	  	  <td>'+end_pathHTML+'</td>');
						listHtml.push('	</tr>');
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="9" style="text-align:center;">조회된 결과가 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				
				js_userPaging('pagingDiv', jQuery('#page_no').val(), jQuery('#tot_cnt').val(), jQuery('#rnum').val(), 'gotoPage');
			}
		}
	});
	
}

function fn_certConfirm(lecture_seq_no,user_lecture_seq_no,end_dt,float_end_dt, std_yyyy, lecture_div, cert_div)
{
	jQuery("#certfrm input[name='cert_div']").val(cert_div);
	jQuery("#certfrm input[name='lecture_seq_no']").val(lecture_seq_no);
	jQuery("#certfrm input[name='user_lecture_seq_no']").val(user_lecture_seq_no);
	jQuery("#certfrm input[name='end_dt']").val(end_dt);
	jQuery("#certfrm input[name='float_end_dt']").val(float_end_dt);
	jQuery("#certfrm input[name='std_yyyy']").val(std_yyyy);
	
	fn_getCertInfo(lecture_seq_no, end_dt, lecture_div);
	//alert( '확인전 반드시 아래 회사정보를 확인하시기 바랍니다. 확인 이후에는 수정이 불가합니다.');
	
}


function fn_getCertInfo(lecture_seq_no, end_dt, lecture_div)
{
	jQuery.ajax({
		url : '/edu/lecture/action/lectureAction.jspx?cmd=getCertInfo', 
		data : jQuery("#certfrm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var json = jsonObj.result.data.MEM_INFO;
				var jsonCnt = jsonObj.result.data.MEM_INFO.length;
				
				if(jsonCnt > 0){
					if( lecture_div == 1 ) jQuery('#cert_title').html('신규개설및승계자');
					else jQuery('#cert_title').html('공중위생관리책임자');
					
					jQuery('#cert_user_nm').html(json[0].user_nm);
					jQuery('#cert_ssn').html(json[0].ssn);
					jQuery('#cert_building_nm').html(json[0].building_nm);
					jQuery('#cert_building_addr').html(json[0].building_addr);
					jQuery('#cert_addr').html(json[0].addr);
					jQuery('#cert_corp_nm').html(json[0].corp_nm);
					jQuery('#cert_addr_nm').html(json[0].addr_nm);
					
					jQuery('#cert_end_dt').html(end_dt);
					
					var tx = ( jQuery(window).width() - 600 ) / 2+jQuery(window).scrollLeft();
					var ty = jQuery(window).scrollTop() + 200;
					
					$("#viewForm").css({left:tx+"px",top:ty+"px"});
					
					$('#viewForm').show(); 
				}
			}
		}
	});
	
}

function fn_closeConfirm()
{
	$('#viewForm').hide(); 
}


function fn_certRequest()
{
	fn_closeConfirm();
	confirmNo();
	
	jQuery.ajax({
		url : '/edu/lecture/action/lectureAction.jspx?cmd=certConfirm', 
		data : jQuery("#certfrm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				alert( '수료증 발급 승인이 완료되었습니다.');
				gotoPage(1);
			}else{
				alert( '처리중 오류가 발생하였습니다.');				
			}
		}
	});
}


function fn_certDown(lecture_seq_no,user_lecture_seq_no,end_dt,float_end_dt, std_yyyy, cert_div)
{
	var url = '/edu/lecture/action/lectureAction.jspx?cmd=certDown&cert_div='+cert_div+'&lecture_seq_no='+lecture_seq_no+'&user_lecture_seq_no='+user_lecture_seq_no+'&float_end_dt='+float_end_dt;
	document.getElementById("certfrm").action = url;
	jQuery("#certfrm input[name='cert_div']").val(cert_div);
	jQuery("#certfrm input[name='lecture_seq_no']").val(lecture_seq_no);
	jQuery("#certfrm input[name='user_lecture_seq_no']").val(user_lecture_seq_no);
	jQuery("#certfrm input[name='end_dt']").val(end_dt);
	jQuery("#certfrm input[name='float_end_dt']").val(float_end_dt);
	jQuery("#certfrm input[name='std_yyyy']").val(std_yyyy);
	
	document.certfrm.submit();
}
var openObj;
function fn_certOpenDown()
{
	if(jQuery("#certfrm input[name='user_lecture_seq_no']").val() == '' )
	{
		alert('선택된 항목이 없습니다. 화면 새로 고침 후 다시 다운로드 하시기 바랍니다.');
		return;
	}
	if(openObj != null) openObj.close();
	document.certfrm.submit();
}
function fn_survey(lecture_seq_no,user_lecture_seq_no,end_dt,float_end_dt, std_yyyy, lecture_div, cert_div)
{
	var url = '/edu/lecture/action/lectureAction.jspx?cmd=certDown&cert_div='+cert_div+'&lecture_seq_no='+lecture_seq_no+'&user_lecture_seq_no='+user_lecture_seq_no+'&float_end_dt='+float_end_dt;
	document.getElementById("certfrm").action = url;
	jQuery("#certfrm input[name='cert_div']").val(cert_div);
	jQuery("#certfrm input[name='lecture_seq_no']").val(lecture_seq_no);
	jQuery("#certfrm input[name='user_lecture_seq_no']").val(user_lecture_seq_no);
	jQuery("#certfrm input[name='end_dt']").val(end_dt);
	jQuery("#certfrm input[name='float_end_dt']").val(float_end_dt);
	jQuery("#certfrm input[name='std_yyyy']").val(std_yyyy);
	
	
	var _url = '/edu/survey/survey.jspx?user_lecture_seq_no='+user_lecture_seq_no+'&lecture_seq_no='+lecture_seq_no+'&lecture_div='+lecture_div;
	
	openObj = window.open( _url , 'survey', 'top=10,left=10,width=950px, height=750px, scrollbars=yes, resizable=yes,resize=yes,toolbar=no,status=no');	
}

jQuery(document).ready(function(){
	
	gotoPage('${input.page_no}'); 
	
	
	jQuery('#btn-save').on("click", function(){
		confirmOpen('수료증발급확인' ,'아래 회사정보를 확인 하셨습니까? <br>잘못 기재된 경우 반드시 정보변경을 통해 수정 후 발급 받으시기 바랍니다. <br>확인을 누르시면 수정 불가하오니, 정보가 다른경우 취소를 누르시기 바랍니다.' , fn_certRequest );
	});	
	jQuery('#btn-pass').on("click", function(){
		fn_closeConfirm();
	});	
	
	jQuery('#btn-req').on("click", function(){
		location.href='/member/member.jspx?cmd=memModifyView';
	});	
});

</script>
</head>

<body>
<form id="certfrm" name="certfrm" method="post" action="/edu/lecture/action/lectureAction.jspx?cmd=certDown" >
<input type="hidden" id="cert_div" name="cert_div" value=""/>
<input type="hidden" id="lecture_seq_no" name="lecture_seq_no" value=""/>
<input type="hidden" id="user_lecture_seq_no" name="user_lecture_seq_no" value=""/>
<input type="hidden" id="end_dt" name="end_dt" value=""/>
<input type="hidden" id="float_end_dt" name="float_end_dt" value=""/>
<input type="hidden" id="std_yyyy" name="std_yyyy" value=""/>
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
						<th scope="row">주의사항</th>
						<td>
									- 수료증 받기 전 회사정보(회사명, 회사주소)가 맞는지 반드시 확인하시기 바랍니다.<br>
									&nbsp;&nbsp;<strong style="color:blue;font-size:14px;">회사명,회사주소</strong>는 반드시 관할시.군.구청에서 발급받은 <strong style="color:blue;font-size:14px;">영업신고증 상의 정보</strong>여야 합니다.<br>
									&nbsp;&nbsp;단, 신규업체중 사전교육 이수자는 영업신고전 상태이므로 관계없습니다.<br>
									<br>
									
									<p style="padding-left:20px">
									<strong style="color:red;font-size:14px;">※ 영업신고 전 또는 업체 정보 변경의 경우</strong><br>
									아래 ①②③번의 경우 “정보변경” ⇨ “회사변경정보 or 신규등록정보”란에 회사정보 입력 후 「변경요청」을 클릭하시면 수강 완료하고 1일~2일후 수료증 발급 가능합니다.<br><br>
								      ① 회사정보가 아직 미결정 되었을 경우 아래 예시와 같이 입력<br>
								      </p>
								      <table>  
								      <tr>
								      	<td rowspan="4" width="50px" style="padding:10px 0px 0px 20px;vertical-align:top;">예시) </td><td>‧ 회사명: 미정</td>
								      </tr>
								      <tr>
								      	<td>‧ 대표자: 홍길동</td>    
								      </tr>
								      <tr>
								      	<td>‧ 회사주소: 경기도 김포시/ 강원도 철원군/ 서울특별시 성동구 (*반드시 관할시.군.구까지는 입력해야 관공서 결과보고 가능)</td>    
								      </tr>
								      <tr>
								      	<td> ‧ 사업자등록번호: 미신고</td>    
								      </tr>	
										</table>	
									<p style="padding-left:20px">							                           
									      ② 영업신고 전일 경우 신고예정인 회사명,대표자,회사주소,사업자등록번호 입력<br>
									      ③ 회사명,대표자,회사주소 등 회사 정보가 변경 되었을 경우 변경된 내용 입력<br>
									</p>
									<br>
									- 회사정보 변경된 경우 사업자등록증만 변경하고 영업신고증은 변경신고를 하지 않아 발생하는 불이익에  관하여<br> &nbsp;&nbsp;협회는 책임이 없음을 알려드립니다.<br>
									- 신규개설및승계자교육은 수료증에 영업장명(청소현장)이 표기되지 않습니다.<br>
									
									<strong style="color:red;font-size:14px;">※ 수료증이 정상적으로 열리지 않거나 문제가 발생하면 설치하세요.</strong>&nbsp;<a href="https://get.adobe.com/kr/reader/" target="_blank"><img src="/static/main/img/common/pdf-ico.jpg" height="20"></a>
							
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
			<div class="list_type2">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width="70px"/><col width="80px"/><col width="80px"/><col width="80px"/>
						<col width="100px"/><col width="250px"/><col width="100px"/><col width=""/><col width="80px"/>
					</colgroup>
					<thead>
						<tr>
							<th rowspan="2">교육연도</th>
							<th rowspan="2">수강과목</th>
							<th rowspan="2">시작일</th>
							<th rowspan="2">종료일</th>
							<th>회사명</th>
							<th>회사주소</th>
							<th rowspan="2">영업장명<br>(청소현장)</th>
							<th rowspan="2">영업장주소<br>(청소현장)</th>
							<th rowspan="2">수료증</th>
						</tr>
						<tr>
							<th colspan="2" class="tred">* 반드시 영업신고증 상의 정보</th>
						</tr>
					</thead>
					<tbody id="list_div">
						<tr>
							<td colspan="9">검색된 결과가 없습니다</td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="paging" id='pagingDiv'></div>
			
			

		<!-- end :: content -->
		</div>
		        
      </div>
    </div>
  </div>


<div class="view" id="viewForm" style="border-left: 1px solid #1e90f1;border-right: 1px solid #1e90f1;z-index: 999;width:700px;display: none;position:absolute;left:100px;top:0px;background-color: #FFF;">
	<div style="z-index:999;font-size: 1.6em;font-weight:bold;color:#ffffff;background-color:#3b85c3;height: 2.25em;padding-top: 10px;padding-left: 10px;" id="confirm_title">
	아래 항목이 맞는지 확인해주세요.<br>
	<span style="font-size: 0.8em;font-weight:bold;color:yellow;padding-top:2px;">회사명, 회사주소는 반드시 관할 시.군.구청에서 발급받은 영업신고증상의 정보이어야 합니다.</span>
	</div>
	<table class="com_table_box">
		<tr><th style="width:200px;">수강과목</th><td id="cert_title"></td></tr>
		<tr><th style="width:200px;">성명<span style="font-weight:bold;color:red;">(변경불가)</span></th><td id="cert_user_nm"></td></tr>
		<tr><th style="width:200px;">주민등록번호<span style="font-weight:bold;color:red;">(변경불가)</span></th><td id="cert_ssn"></td></tr>
		<tr><th style="width:200px;">회사명</th><td id="cert_corp_nm"></td></tr>
		<tr><th style="width:200px;">회사주소</th><td id="cert_addr_nm"></td></tr>
		<tr><th style="width:200px;">자택주소</th><td id="cert_addr"></td></tr>
		<tr><th style="width:200px;">영업장명(청소현장)</th><td id="cert_building_nm"></td></tr>
		<tr><th style="width:200px;">영업장주소(청소현장)</th><td id="cert_building_addr"></td></tr>
		<tr><th style="width:200px;">수강완료일자</th><td id="cert_end_dt"></td></tr>
		<tr><td colspan="2">
		<span class="pull-left" style="padding: 2px 20px 0 0;color:red;font-weight:bold;font-size:12px;">* 정보가 맞으시면 [계속]을 클릭하세요.</span>
		<span class="pull-left" style="padding: 2px 20px 0 0;color:red;font-weight:bold;font-size:12px;">* 정보가 다른경우 [정보변경 바로가기]를 통해 수정 후 발급받으시기 바랍니다.</span>
		<span class="pull-left" style="padding: 2px 20px 0 0;color:red;font-weight:bold;font-size:12px;width: 485px;">* 수료증 발급 완료 후에는 수정 불가하며, 정보 미확인으로 인한 불이익에 관하여 협회는 책임이 없음을 알려드립니다.</span>
		<span class="pull-right" style="padding: 10px 20px 0 0;">		
			<span id="btn-req" class="btn btn-sm btn-info"><i class="fa fa-file"> 정보변경 바로가기</i></span>
			<span id="btn-save" class="btn btn-sm btn-primary"><i class="fa fa-file"> 계속</i></span>
			<span id="btn-pass" class="btn btn-sm btn-success"><i class="fa fa-file"> 취소</i></span>
		</span>
		</td></tr>
	</table>
</div>
			  		
<iframe name="certPoP" width="1" height="0"></iframe>
</body>
</html>