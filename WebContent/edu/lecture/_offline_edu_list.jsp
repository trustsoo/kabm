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
	document.frmSearch.page_no.value = page_no;
	js_getOfflineList();
}


function js_getOfflineList()
{
	jQuery.ajax({
		url : '/edu/lecture/action/offlineAction.jspx?cmd=getOfflineEduList', 
		data : jQuery("#frmSearch").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.offline_info;
				var jsonCnt = jsonObj.result.data.offline_info.length;
				
				if( jsonObj.result.data.property.length > 0 )
					jQuery('#tot_cnt').val(jsonObj.result.data.property[0].tot_cnt);
				
				if(jsonCnt > 0){
					for(var i=0; i<jsonCnt; i++){
						
						var schedule_id   = urlDecode(json[i].schedule_id); 
						var cp_code   = urlDecode(json[i].cp_code); 
						var ji_code   = urlDecode(json[i].ji_code); 
						var dcm_no   = urlDecode(json[i].dcm_no); 
						var is_date   = urlDecode(json[i].is_date); 
						var is_gb   = urlDecode(json[i].is_gb); 
						var ist_title   = urlDecode(json[i].ist_title); 
						var is_ill   = urlDecode(json[i].is_ill); 
						var obcnt   = urlDecode(json[i].obcnt); 
						var dcm_no   = urlDecode(json[i].dcm_no); 
						var apcnt   = urlDecode(json[i].apcnt); 
						var pscnt = urlDecode(json[i].pscnt);
						var rccnt = urlDecode(json[i].rccnt);
						var iplace = urlDecode(json[i].iplace);
						
						var jcha = urlDecode(json[i].jcha);
						var remark = urlDecode(json[i].remark);
						
						var emtp = urlDecode(json[i].emtp);
						var stime = urlDecode(json[i].stime);
						var etime = urlDecode(json[i].etime);
						var edct_expns = addComma( urlDecode(json[i].edct_expns));
						var bank_no = urlDecode(json[i].bank_no);
						var bank_name = urlDecode(json[i].bank_name);
						var bank_ju = urlDecode(json[i].bank_ju);
						var srcv_date = urlDecode(json[i].srcv_date);
						var ercv_date = urlDecode(json[i].ercv_date);
						var brd_no = urlDecode(json[i].brd_no);
						var jisaname = urlDecode(json[i].jisaname);
						var tel = urlDecode(json[i].tel);	
						var dam_tel = urlDecode(json[i].dam_tel);	
						
						var use_yn = urlDecode(json[i].use_yn);
						var web_yn = urlDecode(json[i].web_yn);
						var start_yn = urlDecode(json[i].start_yn);
						var end_yn = urlDecode(json[i].end_yn);
						var ing_yn = urlDecode(json[i].ing_yn);
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td rowspan="2">'+jisaname+ '</td>');
						listHtml.push('		  <td>'+ist_title+'</td>');
						listHtml.push('		  <td>'+is_date+'</td>');
						listHtml.push('		  <td>'+stime +'~'+ etime +'</td>');
						
						listHtml.push('		  <td>'+edct_expns+'</td>');
						
						listHtml.push('		  <td>'+srcv_date +'~'+ ercv_date +'</td>');
						
						var stateHTML = '교육종료';
						
						if( start_yn == 'N')
							stateHTML = '<font color="blue">접수예정<font>';
							
						if( web_yn == 'N' || end_yn == 'Y' )
							stateHTML = '<font color="red">접수마감<font>';
						
						if( web_yn == 'Y' && ing_yn == 'Y' )
							stateHTML = '<a href="javascript:js_nextStep(\''+cp_code+'\',\''+ji_code+'\',\''+dcm_no+'\');"><span class="">신청하기</span></a>';
							
						if( use_yn == 'N')
							stateHTML = '<font color="black">교육종료<font>';
							
							
						
						listHtml.push('		  <td rowspan="2"><div class="LbtnWrap padding_no">'+stateHTML+'</div></td>');
						listHtml.push('		  <td rowspan="2"><div class="LbtnWrap padding_no"><a href="javascript:js_goDetail(\''+brd_no+'\');"><span class="">상세</span></a></div></td>');
						listHtml.push('	</tr>');
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td colspan="2">'+iplace+'</td>');
						listHtml.push('		  <td colspan="2">'+bank_name + ' ' + bank_no + ' ' + bank_ju +  '</td>');
						listHtml.push('		  <td>'+tel+'</td>');
						listHtml.push('	</tr>');
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="11" style="text-align:center;">신청 가능한 교육이 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				js_userPaging('pagingDiv', jQuery('#page_no').val(), jQuery('#tot_cnt').val(), jQuery('#rnum').val(), 'gotoPage');
			}
		}
	});
	
}

function js_nextStep( cp_code, ji_code, dcm_no)
{
	
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep1';
	jQuery('#cp_code').val(cp_code);
	jQuery('#ji_code').val(ji_code);
	jQuery('#dcm_no').val(dcm_no);
	jQuery('#frm').attr('action', _url);
	jQuery('#frm').submit();
}


function js_goDetail( brd_no)
{
	location.href='/board/board.jspx?cmd=view&brd_mng_no=8&brd_no=' + brd_no;	
}

jQuery(document).ready(function(){
	
	
	js_getOfflineList();
	
	
});
</script>
</head>

<body>

<form id="frm" name="frm" method="post" action="" >
<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type='hidden' name='cp_code' id='cp_code' value=''>
<input type='hidden' name='ji_code' id='ji_code' value=''>
<input type='hidden' name='dcm_no' id='dcm_no' value=''>
<input type='hidden' name='next_step' id='next_step' value='1'>
</form>
<!-- search -->
<form id="frmSearch" name="frmSearch" method="post" action="" onsubmit="return false;">
<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type="hidden" id="rnum" name="rnum" value="10"/>
<input type='hidden' name='page_no' id='page_no' value='1'>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
<!-- search Data -->
<div id="content">
<!-- start :: content -->
<!-- start :: content -->
	<div class="directions">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<tbody>
				<tr>
					<th scope="row">주의사항</th>
					<td>
						"신청중"인 교육에 대해 "신청하기" 버튼을 눌러 신청 정보를 입력하시기 바랍니다.<br>
						"상세"를 클릭하시면 해당 교육에 대해 상세한 정보를 확인 하실 수 있습니다.
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="topSearch" style='clear:both;text-align: left;margin-top:20px;'>
			<select name="yyyy" id="yyyy" title="조회년도" style="float:left;text-align:center;">
				<%
				String toDate = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date());
				for (int i=Integer.parseInt(toDate) ; i >= 2000 ; i--) {
				%>
				<option value="<%= i %>"><%= i %> 년</option>
				<% } %>
			</select>&nbsp;&nbsp;
			<select name="search_gbn_jicode" id="search_gbn_jicode" title="">
				
				<option value="%" selected="selected">지회선택</option>
				<c:forEach begin="0" end="${output_count}" step="1" var="idx">
					<option value="${output[idx].jicode}">${output[idx].company}</option>
				</c:forEach>
				
			</select>
			
			<a href="javascript:js_getOfflineList();" class="pbtn05" style="margin-left:50px;"><span>검색</span></a>
	</div>
	<div class="pull-right">
		<label for="" style="font-weight:bold;font-size:14px;color:#ff0016;">
			※ 접수마감인 지회는 해당지회로 연락하세요.
		</label> 
	</div>
	<div class="list_type2">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="5%"/><col width=""/><col width="10%"/>
				<col width="15%"/><col width="10%"/>				
				<col width="18%"/><col width="13%"/><col width="10%"/>
				
			</colgroup>
			<thead>
				<tr>
					<th scope="row" rowspan="2">지회</th>
					<th scope="row">교육과정</th>
					<th scope="row">교육날짜</th>
					<th scope="row">교육시간</th>
					<th scope="row">교육비</th>
					<th scope="row">접수기간</th>
					
					<th scope="row" rowspan="2">상태</th>
					<th scope="row" rowspan="2">상세보기</th>
				</tr>
				<tr>
					<th scope="row" colspan="2">교육장소</th>
					<th scope="row" colspan="2">계좌번호</th>
					<th scope="row">지회연락처</th>
				</tr>
			</thead>
			<tbody id="list_div">
				
			</tbody>
		</table>
	</div>
	<div class="paging" id='pagingDiv'></div>
	
	<div class="LbtnWrap">
		<a href="javascript:history.go(-1);"><span class="">뒤로가기</span></a>		
	</div>

<!-- end :: content -->
<!-- end :: content -->
</div>

</body>
</html>