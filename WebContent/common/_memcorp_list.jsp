<%
/******************************************************************************** 
 * Program ID	:  회사검색목록
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
	
	<title>회사검색</title>
	
<script type="text/javascript" class="source">


function gotoPage(page_no)
{	
	document.frmSearch.page_no.value = page_no;
	fn_setInteraction();
}


//조회
function fn_setInteraction()
{	
	
	var http = jQuery.ajax( {
   		url: "/common/action/common.jspx?cmd=getMemCorpList&templet-bypass",
   		type: "POST",
		data : jQuery('#frmSearch').serialize(true) + "&cpage=" + jQuery('#page_no').val(),
   		async:false,
   		beforeSend : function(){
		},
		error 	: function(xml)
   		{	
			msgStart(msg_com_code_007, 'danger');
		},
   		success: lf_responseList
  	})
}

function lf_responseList(xmlDoc)
{	
	jQuery("#ta_list01 > tbody").html("");
	prjList = new Array();
	
	var code = jQuery(xmlDoc).find('code').text(); 		
	var data = jQuery(xmlDoc).find('data').text();
	var msg =  jQuery(xmlDoc).find('msg').text();
	
	if ( code == "200")
	{
		var listHtml = [];
		var i = 0;
		var j = 1;
		var _html = '';
		var tot_cnt = 0;
		jQuery(xmlDoc).find('corp_info').each(function(){
			
			if(i == 0)
				tot_cnt  = jQuery(this).find('tot_cnt').text(); 
			
			var corp_reg_no  = jQuery(this).find('corp_reg_no').text(); 
			var corp_nm   = urlDecode(jQuery(this).find('corp_nm').text()); 
			var cp_code  = jQuery(this).find('cp_code').text();
			var ill_no  = jQuery(this).find('ill_no').text();
			
			var hname   = urlDecode(jQuery(this).find('hname').text()); 
			var jiname   = urlDecode(jQuery(this).find('jiname').text()); 
			var cptel   = urlDecode(jQuery(this).find('cptel').text()); 
			var cpfax   = urlDecode(jQuery(this).find('cpfax').text()); 
			
			var cpost  = jQuery(this).find('cpost').text();
			var cp_addr   = urlDecode(jQuery(this).find('cp_addr').text()); 
			var cp_jibun   = urlDecode(jQuery(this).find('cp_jibun').text());
			var rprstt_kr   = urlDecode(jQuery(this).find('rprstt_kr').text());
			var mb_no   = urlDecode(jQuery(this).find('mb_no').text());
			
			var rprsttHTML = "<div style='WIDTH:80px;' class='ellipsis'>" + rprstt_kr +"</div>";
			
			var addr_nm = '('+cpost+')'+ cp_addr + ' '+ cp_jibun;
			
			var corpHTML = "<div style='WIDTH:180px;' class='ellipsis'><a href='javascript:fn_setCorpInfo(\"" + corp_reg_no + "\",\"" + corp_nm + "\",\"" + cp_code + "\",\"" + ill_no + "\",\"" + hname + "\",\"" + jiname + "\",\"" + cptel + "\",\"" + cpfax + "\",\"" + addr_nm + "\",\"" + mb_no + "\")' >" + corp_nm +"</a></div>";
			var memHTML = "<div style='WIDTH:300px;' class='ellipsis'><a href='javascript:fn_setCorpInfo(\"" + corp_reg_no + "\",\"" + corp_nm + "\",\"" + cp_code + "\",\"" + ill_no + "\",\"" + hname + "\",\"" + jiname + "\",\"" + cptel + "\",\"" + cpfax + "\",\"" + addr_nm + "\",\"" + mb_no + "\")' >" + hname +"</a></div>";

			
			
			_html  = '		<tr>';
			_html += '		  <td>'+ corp_reg_no +'</td>';
			_html += '		  <td>'+ corpHTML +'</td>';
			_html += '		  <td>'+ rprsttHTML +'</td>';
			_html += '		  <td>'+ memHTML +'</td>';
			_html += '		</tr>';
			j++;
			
			listHtml.push(_html);
			i++;
			
			
		});	
		
		$('#tot_cnt').val(tot_cnt);
		
		
		if( i <= 0 || j<=1)
		{
			listHtml.push('		<tr>');
			listHtml.push('		  <td colspan="4" style="text-align:center;">조회된 결과가 없습니다.</td>');
			listHtml.push('		</tr>');
			js_userPaging('pagingDiv',1, 0, $('#rnum').val(), 'gotoPage');	
		}else{
			js_userPaging('pagingDiv', $('#page_no').val(), $('#tot_cnt').val(), $('#rnum').val(), 'gotoPage');	
			
		}
		
		jQuery('#listData').html(listHtml.join(''));
		
	} else 
	{
		msgStart(msg_com_code_005, 'warning');
	}
	
}

function fn_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,hname,jiname,cptel, cpfax, addr_nm,mb_no)
{
	opener.lf_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,hname,jiname,cptel, cpfax, addr_nm,mb_no);
	window.close();
}

</script>
</head>

<body>
<div id="popup_head_1">회원사 검색</div>
<!-- search -->
<div class="x_content">
<form id="frmSearch" name="frmSearch" method="post" action="" >
	<input type="hidden" id="rnum" name="rnum" value="10"/>
	<input type='hidden' name='page_no' id='page_no' value='1'>
	<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>

	<table class="condition-table">
		<tbody>
			<tr>
				<th width="100px"><div>회원사구분</div></th>
				<td>
					<select name="search_gbn_hcode" id="search_gbn_hcode" title="">
						<option value="" selected="selected">회원전체</option>
						<option value="10">정회원</option>
						<option value="20">일반회원</option>
						<option value="30">특별회원</option>
						<option value="40">특별비회원</option>
					</select>
				</td>
				<th width="100px"><div>회원사명</div></th>
				<td>
					<input type="text" class="form-control" name="title" id="title" value="${input.title }" style="width:200px;" />
				</td>
				<td style="padding:0px;">
					<a href="javascript:gotoPage(1);" class="pbtn05"><span>검색</span></a>
				</td>
			</tr>
			
		</tbody>
	</table>
</form>
</div>

<!-- data listBox -->
<div class="list">
	<table cellpadding="0" cellspacing="0" class="st_2" summary="">
		<caption></caption>
		<colgroup>
			<col width="120px" /><col width="180" /><col width="80" /><col width="300" />
		</colgroup>
		<thead>
			<tr>
				<th>사업자등록번호</th>
				<th>회사명</th>
				<th>대표자</th>
				<th>회원사구분</th>
			</tr>
		</thead>
		<tbody id="listData">
			<tr><td colspan="4" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
		</tbody>
	</table>
</div>

<!-- btn paging Box -->
<div class="paging" id='pagingDiv'></div>


<script type="text/javascript">

	jQuery('#page_no').val('${input.page_no}');
	gotoPage('${input.page_no}'); 
	
	
</script>
</body>
</html>