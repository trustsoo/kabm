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
   		url: "/common/action/common.jspx?cmd=getCorpList&templet-bypass",
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
			
			var cpost  = jQuery(this).find('cpost').text();
			var cp_addr   = urlDecode(jQuery(this).find('cp_addr').text()); 
			var cp_jibun   = urlDecode(jQuery(this).find('cp_jibun').text());
			var rprstt_kr   = urlDecode(jQuery(this).find('rprstt_kr').text());
			var mb_no   = urlDecode(jQuery(this).find('mb_no').text());
			
			var rprsttHTML = "<div style='WIDTH:80px;' class='ellipsis'>" + rprstt_kr +"</div>";
			
			var addr_nm = '('+cpost+')'+ cp_addr + ' '+ cp_jibun;
			
			
			var corpHTML = "<div style='' class='ellipsis'><a href='javascript:fn_setCorpInfo(\"" + corp_reg_no + "\",\"" + corp_nm + "\",\"" + cp_code + "\",\"" + ill_no + "\",\"" + addr_nm + "\",\"" + mb_no + "\",\"" + rprstt_kr + "\")' >" + corp_nm +"</a></div>";
			var addrHTML = "<div style='white-space: initial;' class='ellipsis'><a href='javascript:fn_setCorpInfo(\"" + corp_reg_no + "\",\"" + corp_nm + "\",\"" + cp_code + "\",\"" + ill_no + "\",\"" + addr_nm + "\",\"" + mb_no + "\",\"" + rprstt_kr + "\")' >" + addr_nm +"</a></div>";

			_html  = '		<tr style="background-color: #f9dfdf;">';
			_html += '		  <td class="a-center ">'+ corp_reg_no +'</td>';
			_html += '		  <td class="a-center ">'+ corpHTML +'</td>';
			_html += '		  <td class="a-center last">'+ rprsttHTML +'</td>';
			_html += '		</tr>';
			
			_html += '		<tr>';
			_html += '		  <td class="a-left last" colspan="3">'+ addrHTML +'</td>';
			_html += '		</tr>';
			
			j++;
			
			listHtml.push(_html);
			i++;
			
			
		});	
		
		$('#tot_cnt').val(tot_cnt);
		
		
		if( i <= 0 || j<=1)
		{
			var msgTxt = '조회된 결과가 없습니다.';
			if( jQuery('#view_div').val() == 'mem' ) msgTxt = '동일 회사가 없습니다. 아래창의 [회사 변경정보 or 신규 등록정보]란에 회사 정보를 입력하세요.';
			listHtml.push('		<tr>');
			listHtml.push('		  <td colspan="3" style="text-align:center;font-weight:bold;color:red;white-space: initial;">'+msgTxt+'</td>');
			listHtml.push('		</tr>');
			js_mobilePaging('pagingDiv',1, 0, $('#rnum').val(), 'gotoPage');	
		}else{
			js_mobilePaging('pagingDiv', $('#page_no').val(), $('#tot_cnt').val(), $('#rnum').val(), 'gotoPage');	
			
		}
		
		jQuery('#listData').html(listHtml.join(''));
		
	} else 
	{
		msgStart(msg_com_code_005, 'warning');
	}
	
}

function fn_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,addr_nm,mb_no,president)
{
	opener.lf_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,addr_nm,mb_no,president);
	window.close();
}

</script>
<style type="text/css">
.x_panel{padding: 1px 5px;}
.x_panel .x_title {
	background-color: #34495e;
	padding: 4px 5px 2px;
	color: #fff;
	font-weight:bold;
}
</style>

</head>

<body>


<div class="x_panel">
	<div class="x_title">
		<h2>회사찾기 </h2>		
		<div class="clearfix"></div>
	</div>
	<div class="x_content" style="display: block;">
		<form id="frmSearch" name="frmSearch" method="post" action="" >
		<input type="hidden" id="rnum" name="rnum" value="5"/>
		<input type='hidden' name='page_no' id='page_no' value='1'>
		<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
		<input type="hidden" name="view_div" id="view_div" value='${input.view_div }'/>		
			<div class="form-group row">								
				<div class="col-md-12 col-sm-12">
					<div class="input-group">
					<input type="text" class="form-control" name="title" id="title" value="${input.title }" />
					<span class="input-group-btn">
					<a href="javascript:gotoPage(1);" class="btn btn-default">회사명검색</a>
					</span>									
					</div>
				</div>			
			</div>
		</form>		
		<div class="table-responsive">
			<table class="table table-striped jambo_table bulk_action">
				<thead>
					<tr class="headings">
						<th class="column-title">사업자등록번호</th>
						<th class="column-title">회사명</th>
						<th class="column-title last">대표자</th>
					</tr>
					<tr class="headings">
						<th class="column-title last" colspan="3">주소</th>
					</tr>
				</thead>
				<tbody id="listData">
									
				</tbody>
			</table>
		</div>
	
		<div class="paging" id='pagingDiv'></div>
	</div>
	
</div>


<!-- btn paging Box -->

<div class="x_panel">
	<div class="x_title">
		<h2>주의사항 </h2>		
		<div class="clearfix"></div>
	</div>
	<div class="x_content" style="display: block;">

		<div class="directions">
			<table cellpadding="0" cellspacing="0" class="" summary="" >
				<tbody>
					<tr>
						<td>
							<strong>1.<b style="font-weight:bold;color:red;">회사명,대표자,회사주소</b>는 반드시 관할시.군.구청에서 발급받은 <b style="font-weight:bold;color:red;">영업신고증상의 정보</b>여야 합니다.<br>
							&nbsp;&nbsp;정보가 다르거나 변경된 경우 [회사 변경정보 or 신규 등록정보]에 내용 입력 후 “정보변경 요청”을 눌러주세요. </strong>
						</td>
					</tr>
					<tr>
						<td>
							<strong>2.사업자등록번호가 미등록 되어있을시 협회로 문의하시기 바랍니다. TEL:02-465-5900</strong>
						</td>
						
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>		


<script type="text/javascript">

	jQuery('#page_no').val('${input.page_no}');
	gotoPage('${input.page_no}'); 
	
	
</script>
</body>
</html>