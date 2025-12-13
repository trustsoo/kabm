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

<script type="text/javascript" class="source">


function gotoPage(page_no)
{	
	document.frm.page_no.value = page_no;
	js_getLectureList();
}

function fn_lectureView(lecture_div , lecture_seq_no , isReq , std_yyyy )
{
	
	if( isReq == 'Y')
	{		
		if( lecture_div == '2'){
			var msg = '공중위생관리책임자(기존업체) 교육 대상이 맞습니까? \n신규개설 및 승계자 대상인 경우 [취소] 클릭하신 후 다시 선택해 주세요.';
			//var msg = '2020년도 공중위생관리책임자(기존업체)교육 미이수 업체가 맞습니까?\n\n코로나19로 인해 2020년도 책임자교육이 21년 6월 30일까지 연장되었습니다. \n반드시 2020년도 미이수업체만 교육신청하시기 바랍니다. \n(※ 2021년도 책임자교육은 2021년 7월중 오픈예정입니다.) \n\n또한 신규개설 및 승계자 대상인 경우 [취소]클릭하신 후 다시 선택해 주세요.';
			//if( !confirm(msg)) return;
			//msg = '다시한번 2020년도 공중위생관리책임자교육 신청이 맞습니까?\n\n이 교육은 2021년도 교육이 아니며, 2021년도 교육으로 인정되지 않습니다. \n이로 인한 착오수강에 대한 책임은 신청자 본인에게 있습니다.';
			if( !confirm(msg)) return;	
		}else{
			var msg = '신규개설 및 승계자 교육 대상이 맞습니까? \n공중위생관리책임자(기존업체) 대상인 경우 [취소] 클릭하신 후 다시 선택해 주세요.';
			if( !confirm(msg)) return;
				
		}
		location.href = '/mobile/edu/lecture/lectureCtrl.jspx?cmd=viewLectureView&lecture_seq_no='+lecture_seq_no+'&std_yyyy='+std_yyyy;
	}
	else
		msgStart('수강신청 가능 기간이 아닙니다.');
}

jQuery(document).ready(function(){
	
});

</script>
<style>
.x_panel {
	padding: 0px 0px;
	border: 1px solid #d4d4d4;
}
.x_panel h2{
	overflow: visible;
}
.x_panel .x_title {
	background-color: #34495e;
	padding: 4px 5px 2px;
	color: #fff;
	font-weight:bold;
}
.panel_toolbox>li>a:hover {
    background-color: transparent;
}

</style>
</head>


<body>


<!-- search -->
<form id="frm" name="frm" method="post" action="" >
<input type="hidden" id="rnum" name="rnum" value="20"/>
<input type='hidden' name='page_no' id='page_no' value='1'>
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>
</form>

<div class="x_panel">
	<div class="x_title">
		<h2>신규개설 및 승계자교육 </h2>
		<ul class="nav navbar-right panel_toolbox">
		<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
		</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
		<div style="text-align:left;display:block;padding-left: 5px;">
		<p>
			<span style="font-size:14px;font-weight:bold;">※ 수강신청기간</span> <br />
			<span style="font-size:14px;font-weight:bold;">- ${new_start } ~ ${new_end }</span><br />
		</p>
		<span style="font-size:14px;font-weight:bold;">※ 신규개설및승계자교육이란?</span>
		<p style="height: 30px;padding: 5px 0;"><span style="font-weight:bold;color:blue;">건물위생관리업을 새로 창업(업종추가)</span>하거나<br><span style="font-weight:bold;color:blue;">지위승계(대표자변경)를 받은 대표자가 최초로</span> 받아야하는 교육</p><br>
		<span style="font-size:14px;font-weight:bold;">※ 신규개설및승계자교육 대상자란?</span>
		<p style="padding: 5px 0;"> 1.『건물위생관리업』영업신고하기 위한 대표자<br>
			2.『건물위생관리업』영업신고 이후 위생교육 미이수 대표자<br>
			3.『건물위생관리업』의 대표자가 승계된 경우 승계받은 대표자<br>
			  *대표자  승계된 경우, 신규대표자가 신규가입 하여야 함.<br>
		      <span style="font-weight:bold;color:blue;">(기존의 전 대표자 아이디 연동 불가)</span><br>
		</p>						
		<p >	
			<b>(<font style="color:red;font-weight:bold;font-size:14px;">"★반드시 대표자 본인"</font>이 직접 이수해야 함)<br>(교육생=대표자)</b>
		</p>
		<br>
		</div>
		<a href="javascript:fn_lectureView(1, '${new_lecture_seq_no}' , '${new_req}' , '${new_std_yyyy}' );" class="btn btn-info btn-block"><span class=""><i class="fa fa-check"></i> 신규개설 및 승계자교육 신청하기</span></a>
	</div>
</div>
<div class="x_panel">
	<div class="x_title">
		<h2>${old_std_yyyy }년도 공중위생관리책임자 교육</h2>
		<ul class="nav navbar-right panel_toolbox">
		<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
		</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
		<div style="text-align:left;display:block;padding-left: 5px;">
		<p>
			<span style="font-size:14px;font-weight:bold;">※ 수강신청기간</span> <br />
			<span style="font-size:14px;font-weight:bold;">- ${old_start } ~ ${old_end }</span><br />
		</p>
		<span style="font-size:14px;font-weight:bold;">※ 공중위생관리책임자교육이란?</span>
		<p style="height: 70px;padding: 5px 0;">
			『건물위생관리업』영업 신고하고 <span style="font-weight:bold;color:blue;">신규교육 수료 후 다음해부터 매년 현장책임자가 받아야하는 교육</span>(기존업체에 해당)
		</p>
		<span style="font-size:14px;font-weight:bold;">※ 공중위생관리책임자교육 대상자란?</span>
		<p>
			1. 모든 영업장 별로 관리소장 또는 반장 등의  현장책임자<br>
			2. 영업장이 없는 경우, 대표자  또는 본사에서 지정한 관리직원<br />
		</p>
		<p>	
			<!-- <font style="color:red;font-weight:bold;font-size:16px;text-align:center;">※ 2020년도 미이수업체만 신청하세요.<br>
			이 교육은 2021년도 교육이 아니며,<br>2021년도 교육으로 인정되지 않습니다.<br>(‘21년도 교육: ‘21년 7월중 오픈예정)</font> -->
		</p>
		<br>
		</div>
		<a href="javascript:fn_lectureView(2, '${old_lecture_seq_no}' , '${old_req}' , '${old_std_yyyy}' );" class="btn btn-success btn-block"><span class=""><i class="fa fa-check"></i> 공중위생관리책임자 교육신청하기</span></a>			
	</div>
</div>	

<div class="x_panel">
	<div class="x_title" style="background-color: #337ab7;">
		<h2>교육안내</h2>
		<ul class="nav navbar-right panel_toolbox">
		<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
		</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content" style="display: none;">
		<div style="text-align:left;display:block;padding-left: 5px;">
			
				<table><tr><td style="text-align:left;font-size:14px;">
				<strong style="color:red">[수강기간]</strong><br>
				
				• 신규개설및승계자 : 365일 수강가능<br>
				• 공중위생관리책임자(기존업체) : 7월중 ~ 12월31일까지 수강가능 <br>
				<strong style="color:red">[수강신청 절차]</strong><br>
				
				교육생등록 ⇨ 교육신청 ⇨ 교육비 결제 ⇨ 나의강의실(교육수강 및 퀴즈풀기) ⇨ 수료증 발급<br>
				</td></tr>
				</table>
			
		</div>
	</div>
</div>
<div class="x_panel">
	<div class="x_title" style="background-color: #337ab7;">
		<h2>교육신청 및 결제안내</h2>
		<ul class="nav navbar-right panel_toolbox">
		<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
		</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content" style="display: none;">
		<div style="text-align:left;display:block;padding-left: 5px;">
			
				<font color="blue">[로그인 ⇨ 교육신청 ⇨ 교육비 결제]   1인 ₩ 38,000원</font><br>
				1. 아이디와 패스워드 입력 후 로그인하세요.<br>

				2. 온라인교육 ⇨ 온라인교육신청을 클릭하세요.<br>
				
				3. 수강하려는 과목을 확인 후 [교육신청]을 클릭하세요.<br>
				    (신규개설및승계자교육과 공중위생관리책임자교육 혼동 없으시기 바랍니다.)<br>
				
				4. 계좌이체,신용카드,가상계좌 중 결제 방법을 선택하세요.<br>
				
				5. 결제완료<br>
				
				
				<table><tr><td style="border-right:1px solid #a5a5a5;"><font color="red">♣ 주의사항 ♣</font><br>
				- 공중위생관리책임교육은 반드시 당해연도 12월31일 자정까지 수강완료 해야 하므로 12월31일 21시이후<br>
				  신청자는 수강이 불가능합니다. (교육시간 3시간)</td></tr>
				</table>
			
		</div>
	</div>
</div>

<div class="x_panel">
	<div class="x_title" style="background-color: #337ab7;">
		<h2>동영상 수강안내</h2>
		<ul class="nav navbar-right panel_toolbox">
		<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
		</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content" style="display: none;">
		<div style="text-align:left;display:block;padding-left: 5px;">
			<font color="blue">※ 필수사항<br>
			① 회원로그인 및 교육비 결제 완료<br>
			② 수강완료 인정은 3시간(3과목) 교육이수<br></font>
			1. 온라인교육 ⇨ 나의강의실을 클릭하세요.<br>

			2. 제목 하단의 [해당 과목]을 선택 후 [시작하기/계속하기]를 클릭하여 수강하세요.<br>
						(수강 최초에는 ‘시작하기’로 보여지고, 시작하기 클릭 이후부터는 ‘계속하기’로 보여집니다.)<br>
						
						
			<table><tr><td style="border-right:1px solid #a5a5a5;"><font color="red">♣ 주의사항 ♣</font><br>
			- 총 3시간(3과목) 수강 완료해야하며, 매 과목당 정해진 시간 미이수시에는 다음 과목 시청이 불가능
			  합니다.<br>
			
			- 동영상보기 ‘시작하기’ 클릭 후 반드시 2주(14일)이내에 수강 완료해야하며, 기간이 지났을 경우 
			  나의강의실에서 ‘기간연장’을 클릭하세요.<br>
			  
			  단, 책임자교육 신청자의 경우 반드시 당해연도 12월31일까지 수강 완료하여야 합니다.<br>
			
			- 학습내용을 얼마나 잘 이해하고 있는지 확인하기 위해 동영상 수강 중에 객관식 10문제의 퀴즈풀기가
			  있으며, 정답을 맞출 수 있는 횟수 제한은 없습니다.<br>
			  
			- 전 과목 수강을 다시보기 원하실 경우 나의강의실에서 수강완료 후 7일이내에 다시보기 가능합니다. (7일 경과시 다시보기 절대불가)<br>
				</td></tr>
			</table>
		</div>
	</div>
</div>

<div class="x_panel">
	<div class="x_title" style="background-color: #337ab7;">
		<h2>수료증발급 안내</h2>
		<ul class="nav navbar-right panel_toolbox">
		<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
		</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content" style="display: none;">
		<div style="text-align:left;display:block;padding-left: 5px;">
			<font color="blue">※ 반드시 3시간(3과목) 수강 완료하여야 수료증발급이 가능합니다.<br></font>
			1. 온라인교육 ⇨ 수료증발급 클릭 후 수료증 하단 「확인」을 클릭하세요.<br>

			2. 반드시 회사정보가 맞는지 정확히 확인하시고 하단 「계속」을 클릭하세요. <br>
			<strong>(※ 확인 클릭 후에는 업체정보 수정 불가합니다.)</strong><br>
			
			3. 「파일 열기」 또는 「저장」 후 수료증을 출력하세요. <br>
										
			
						
						<table><tr><td style="border-right:1px solid #a5a5a5;"><font color="red">♣ 주의사항 ♣</font><br>
			
		
			- 수료증 받기 전 회사정보(회사명, 회사주소)가 맞는지 반드시 확인하시기 바랍니다.<br>
			  회사명,회사주소는 반드시 관할시.군.구청에서 발급받은 영업신고증상의 정보여야 합니다.<br>
			
			  단, 신규업체중 사전교육 이수하려는 대표자는 영업신고 전 상태이므로 관계없습니다.<br>
			
			  ※ 영업신고 전 또는 업체 정보 변경의 경우<br>
			     아래 ①②③번의 경우 “정보변경” ⇨ “회사변경정보 or 신규등록정보”란에 회사정보 입력 후 「변경요청」을 클릭하시면 수강 완료하고 1일~2일후 수료증 발급 가능합니다.<br>
			      ① 회사정보가 아직 미결정 되었을 경우 아래 예시와 같이 입력<br>
			         예시)  ‧ 회사명: 미정 <br>
			                ‧ 대표자: 홍길동<br>
			                ‧ 회사주소: 경기도 김포시/ 강원도 철원군/ 서울특별시 성동구 (*반드시 관할시.군.구까지는 입력해야 관공서 결과보고 가능)<br>
			                ‧ 사업자등록번호: 미신고<br>
			      ② 영업신고 전일 경우 신고예정인 회사명,대표자,회사주소,사업자등록번호 입력<br>
			      ③ 회사명,대표자,회사주소 등 회사 정보가 변경 되었을 경우 변경된 내용 입력<br>
			
			- 회사정보 변경된 경우 사업자등록증만 변경하고 영업신고증은 변경신고를 하지 않아 발생하는 불이익에 
			  관하여 협회는 책임이 없음을 알려드립니다.<br>
			
			- 신규개설및승계자교육은 수료증에 영업장명(청소현장)이 표기되지 않습니다.


				</td></tr>
			</table>
		</div>
	</div>
</div>

<div class="x_panel">
	<div class="x_title" style="background-color: #337ab7;">
		<h2>영수증출력 안내</h2>
		<ul class="nav navbar-right panel_toolbox">
		<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
		</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content" style="display: none;">
		<div style="text-align:left;display:block;padding-left: 5px;">
			1. 온라인교육 ⇨ [결제내역] 또는 처음 메인화면에서 [영수증출력]을 클릭하세요.<br>

						2. 해당과목의 영수증 하단 「발급」을 클릭하세요.<br>
						
						3. 「파일 열기」 또는 「저장」 후 영수증을 출력하세요.<br>
		</div>
	</div>
</div>
<div class="x_panel">
	<div class="x_title" style="background-color: #337ab7;">
		<h2>교재수령 안내</h2>
		<ul class="nav navbar-right panel_toolbox">
		<li style="float: right;"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
		</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content" style="display: none;">
		<div style="text-align:left;display:block;padding-left: 5px;">
			<font color="blue">※ 교재는 교육비 결제까지 완료된 교육신청자에 한해 발송됩니다.</font><br>

						1. 교재 배송지 입력은 교육비 결제 과정에서 이루어지며, 교재 수령할 배송지 정보를 정확히 입력하시기 바랍니다.<br>
						2. [온라인교육 ⇨ 결제내역]에서 배송지 수정이 가능하나 발송 처리된 이후로는 수정 불가하며, 송장번호도 조회하실 수 있습니다.<br>
						3. 교재수령지 미입력시 교재가 발송되지 않습니다.<br>
						4. 주소 정확히 입력하시기 바라며, 반송될 경우 착불로 수령 가능합니다.<br>
		</div>
	</div>
</div>

</body>
</html>