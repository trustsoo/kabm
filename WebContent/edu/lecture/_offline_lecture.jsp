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

function fn_lectureView(is_gb )
{
	if(is_gb == '03' )
		//location.href = '/edu/lecture/offlineCtrl.jspx?cmd=offlineEduList&is_gb='+is_gb;
		msgOpen('서비스 준비중입니다.<br>신규는 아래 신청서를 다운로드하여 팩스 또는 이메일로 신청하시기 바랍니다.');
	else
		msgOpen('서비스 준비중입니다.<br>신규는 아래 신청서를 다운로드하여 팩스 또는 이메일로 신청하시기 바랍니다.');
	
}

jQuery(document).ready(function(){

	
});

</script>
</head>

<body>

<div id="content">
		
			<div class="edu_tit2">
				<strong>교육신청</strong>
			</div>
			<div class="mypage">
				<div class="section-1">
					<ul class="applybtns">
						<li class="">
							<strong>신규개설 및 승계자 교육</strong>
							<p>
								건물위생관리업 개설자와 개설이후 위생교육을 이수하지 않은<br>
								신규개설자 그리고, 업체 대표 승계자만이 교육대상자입니다.<br />
								<span style="font-weight:bold !important;">(<font style="color:red;font-weight:bold;font-size:20px;">"대표자 본인"</font>이 직접 이수해야 함)</span>
							</p>
							<div class="">
								<a href="javascript:fn_lectureView('02' );" class="pbtn02"><span class="">준비중</span></a>
							</div>
						</li>
						<li class="">
							<strong>공중위생관리책임자 교육</strong>
							<p>
								기존업체를 말하며, 귀사의 모든 영업장별로 관리소장님 또는 <br>
								반장님 등으로 반드시 공중위생관리책임자를 지정하고,<br />
								매년 위생교육을 이수하여야 합니다.<br />
								<span style="font-weight:bold !important;">(영업장이 없는 경우 귀사의 대표자께서 이수해야 함)</span>
							</p>
							<div class="">
								<a href="javascript:fn_lectureView('03' );" class="pbtn02_1"><span class="">준비중</span></a>
							</div>
						</li>
					</ul>
				</div>
			</div>
			
			<div class="edu_tit2">
				<strong>교육안내</strong>
			</div>
			<div class="view" style="margin-top:10px;">
				<div class="list_type2">
					<table><tr><td style="text-align:left;font-size:14px;">
					<strong style="color:red">[수강기간]</strong><br>
					<strong>
					    • 신규개설및승계자 : 매달 1회 실시 (서울지회에서만 실시함)<br>
						• 공중위생관리책임자(기존업체) : 전국 11개 지회 : 5월말~9월중 / 지회별 1~2회 실시<br>
						                                <span style="margin-left:215px;">서울지회 : 9월~12월중 / 11회 실시</span><br></strong>
						  (※ 교육일정과 횟수는 변동될 수 있으며, 교육일정을 확인하시기 바랍니다.)
					</td></tr>
					</table>
				</div>
				<br>
				
				<table cellpadding="0" cellspacing="0" class="" summary="" >
				<caption></caption>
				<colgroup>
					<col width="120"/><col width="300"/>
					<col width="120"/><col width=""/>
				</colgroup>
				<tbody>
					
					<tr>
						<th scope="row">교육신청 및 결제안내</th>
						<td colspan="3" id='cntnt'>
						<font color="blue">
							▶교육비: 1인 30,000원<br>
							▶ 계좌번호: 지회별 계좌번호가 다르니 교육수강하려는 해당 지회로 송금  <br>
							   &nbsp;  &nbsp; (※ 집합교육은 무통장입금만 가능합니다.) <br>
							▶ 접수마감: 신청접수 후 입금완납자로 교육인원 제한
							
						
						</font>
					  </br></br>
							<strong>[팩스 또는 이메일로 신청접수시]</strong><br>
							1. 수강하려는 교육과정의 교육신청서 다운받아 해당지회 <strong style="color:red">팩스 또는 이메일로 송부</strong> </br>
							2. 해당지회 계좌로 <strong style="color:red">무통장 입금</strong>			
										
							<table cellpadding="0" cellspacing="0" class="" summary="" >
								<caption></caption>
								<tbody id="list_div">
									<tr>
										<th>신규개설및승계자</th>
										<td><a href="/static/edu/doc/신규및승계자교육신청서.hwp"><i class="fa fa-download"></i> 교육신청서 다운</a></td>
										<td><a href="/static/edu/doc/신규및승계자교육연기원.hwp"><i class="fa fa-download"></i> 교육연기원 다운</a></td>
									</tr>
									<tr>
										<th></i>공중위생관리책임자</th>
										<td><a href="/static/edu/doc/공중위생관리책임자지정및교육신청서.hwp"><i class="fa fa-download"></i> 교육신청서 다운</a></td>
										<td></td>
									</tr>
								</tbody>
							</table>
							
							<br>
							※ 참고사항: 신규개설및승계자 집합교육시에는 주차공간이 협소하오니 대중교통(지하철 2호선 성수역 3번출구)을 이용하여 주시기 바랍니다.<br>
							<br>
							<strong>[온라인상에서 신청접수시]</strong><br>
							1. 온라인교육 ⇨ 집합교육신청을 클릭하세요.<br>
							
							2. ① 수강하려는 과목을 확인 후 「교육신청」 클릭하세요.<br>
							       &nbsp;  &nbsp;(신규개설및승계자교육과 공중위생관리책임자교육 혼동 없으시기 바랍니다.)<br>
							
							   &nbsp;②  해당일정 확인 후 「신청하기」 클릭하여 접수 후 지회 계좌로 무통장 입금<br>
														
						</td>
					</tr>
					<tr>
						<th scope="row">수료증발급 안내</th>
						<td colspan="3" id='cntnt'>
						
								교육 당일 교육 종료 후 현장에서 교부 됩니다.<br>

								<strong style="color:red">※ 수료증 미교부시 교육 미수료로 처리되어 재교육을 받게되오니 유념하시기 바랍니다.</strong>

						</td>
					</tr>
					<tr>
						<th scope="row">영수증출력 안내</th>
						<td colspan="3" id='cntnt'>
						
								교육 신청 및 교육비 완납자에 한해 교육당일 현장에서 교부 됩니다.<br>

						</td>
					</tr>
					<tr>
						<th scope="row">교재수령 안내</th>
						<td colspan="3" id='cntnt'>
						
								교육 신청자에 한해 교육당일 현장에서 교부 됩니다.<br>

						</td>
					</tr>
				</tbody>
			</table>
			</div>

		<!-- end :: content -->
		</div>
</body>
</html>