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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
<title>집합교육선택검색</title>
<script type="text/javascript" class="source">

function gotoPage(page_no)
{	
	document.frmSearch.page_no.value = page_no;
	js_getOfflineList();
}


function js_getOfflineList()
{
	jQuery.ajax({
		url : '/common/action/common.jspx?cmd=getOfflineEduList', 
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
						var edct_expns = urlDecode(json[i].edct_expns);
						var bank_no = urlDecode(json[i].bank_no);
						var bank_name = urlDecode(json[i].bank_name);
						var bank_ju = urlDecode(json[i].bank_ju);
						var srcv_date = urlDecode(json[i].srcv_date);
						var ercv_date = urlDecode(json[i].ercv_date);
						var brd_no = urlDecode(json[i].brd_no);
						var jisaname = urlDecode(json[i].jisaname);		
						
						var use_yn = urlDecode(json[i].use_yn);
						var web_yn = urlDecode(json[i].web_yn);
						var start_yn = urlDecode(json[i].start_yn);
						var end_yn = urlDecode(json[i].end_yn);
						var ing_yn = urlDecode(json[i].ing_yn);
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td>'+jisaname+ '</td>');
						listHtml.push('		  <td>'+ist_title+'</td>');
						listHtml.push('		  <td>'+is_date+'</td>');
						listHtml.push('		  <td>'+stime +'~'+ etime +'</td>');
						
						listHtml.push('		  <td>'+edct_expns+'</td>');
						
						listHtml.push('		  <td>'+apcnt + '/' + obcnt+'</td>');
						listHtml.push('		  <td>'+srcv_date +'~'+ ercv_date +'</td>');
						
						var stateHTML = '교육종료';
						
						if( start_yn == 'N')
							stateHTML = '<font color="blue">접수예정<font>';
							
						if( web_yn == 'N' || end_yn == 'Y' )
							stateHTML = '<font color="red">접수마감<font>';
						
						if( web_yn == 'Y' && ing_yn == 'Y' )
							stateHTML = '<a href="javascript:js_selectEdu(\''+cp_code+'\',\''+ji_code+'\',\''+dcm_no+'\');"><span class="">선택하기</span></a>';
							
						if( use_yn == 'N')
							stateHTML = '<font color="black">교육종료<font>';
							
							
						
						listHtml.push('		  <td><div class="LbtnWrap padding_no">'+stateHTML+'</div></td>');
						listHtml.push('	</tr>');
						
						listHtml.push('	<tr>');
						listHtml.push('		  <td colspan="6">'+iplace+'</td>');
						listHtml.push('		  <td colspan="2">'+bank_name + ' ' + bank_no + ' ' + bank_ju +  '</td>');
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

function js_selectEdu(cp_code, ji_code, dcm_no)
{
	var user_offline_seq_no = jQuery('#user_offline_seq_no').val();
	opener.lf_setEduChange(cp_code, ji_code, dcm_no,user_offline_seq_no);
	window.close();	
}

jQuery(document).ready(function(){
	
	//jQuery('#ji_code').val('${input.ji_code}');
	if( jQuery('#search_gbn_jicode').val() == '' ){		
		var listHtml = [];
		listHtml.push('		<tr>');
		listHtml.push('		  <td colspan="11" style="text-align:center;">신청 가능한 지회 정보가 없습니다.</td>');
		listHtml.push('		</tr>');
		jQuery('#list_div').html(listHtml.join(''));
	}else{
		js_getOfflineList();
	}
});
</script>
</head>

<body>


<!-- search -->
<form id="frmSearch" name="frmSearch" method="post" action="" onsubmit="return false;">
<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type='hidden' name='user_offline_seq_no' id='user_offline_seq_no' value='${input.user_offline_seq_no}'>
<input type="hidden" id="rnum" name="rnum" value="10"/>
<input type='hidden' name='page_no' id='page_no' value='1'>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
<input type='hidden' name='search_gbn_jicode' id='search_gbn_jicode' value='${input.ji_code}'>
<!-- search Data -->

<div id="popup_head_1">집합교육 선택</div>

<div class="x_content">
<!-- start :: content -->
<!-- start :: content -->
	<div class="topSearch" style='clear:both;text-align: left;padding-bottom: 10px;'>
			<select name="yyyy" id="yyyy" title="조회년도" style="float:left;text-align:center;">
				<%
				String toDate = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date());
				for (int i=Integer.parseInt(toDate) ; i >= 2000 ; i--) {
				%>
				<option value="<%= i %>"><%= i %> 년</option>
				<% } %>
			</select>&nbsp;&nbsp;
			<select name="ji_code" id="ji_code" title="">
				
				<c:forEach begin="0" end="${output_count}" step="1" var="idx">
					<c:if test="${ input.ji_code == output[idx].jicode }">
					<option value="${output[idx].jicode}">${output[idx].company}</option>
					</c:if>
				</c:forEach>
				
			</select>
			
			<a href="javascript:js_getOfflineList();" class="pbtn05" style="margin-left:50px;"><span>검색</span></a>
	</div>
			
	<div class="list_type2">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="5%"/><col width=""/>
				<col width="10%"/><col width="10%"/><col width="6%"/><col width="7%"/>				
				<col width="20%"/><col width="13%"/><col width="10%"/>
				
			</colgroup>
			<thead>
				<tr>
					<th scope="row">지회</th>
					<th scope="row">교육과정</th>
					<th scope="row">교육날짜</th>
					<th scope="row">교육시간</th>
					<th scope="row">교육비</th>
					<th scope="row">인원</th>
					<th scope="row">접수기간</th>
					<th scope="row">상태</th>
				</tr>
				<tr>
					<th scope="row" colspan="6">교육장소</th>
					<th scope="row" colspan="2">계좌번호</th>
				</tr>
			</thead>
			<tbody id="list_div">
				
			</tbody>
		</table>
	</div>
	<div class="paging" id='pagingDiv'></div>
	

</div>

</body>
</html>