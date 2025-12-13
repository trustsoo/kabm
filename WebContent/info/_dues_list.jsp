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

function fn_getDuesList()
{
	jQuery.ajax({
		url : '/info/action/info.jspx?cmd=getDuesList', 
		data : jQuery("#frm").serialize(),
		type: 'POST',
		dataType: 'json',
		async : false,	
		success : function(jsonObj)
		{
			if(jsonObj.result.code == '200'){
				var listHtml = [];
				var json = jsonObj.result.data.dues_list;
				var jsonCnt = jsonObj.result.data.dues_list.length;
				
				if(jsonCnt > 0){
					for(var i=0; i<jsonCnt; i++){
						var cp_code  = urlDecode(json[i].cp_code); 
						var fyear  = urlDecode(json[i].fyear); 
						var field_name   = urlDecode(json[i].field_name); 
						var month_fee = urlDecode(json[i].month_fee);
						var month_in = urlDecode(json[i].month_in);
						var jan_amt = urlDecode(json[i].jan_amt);
						
					 	if( field_name == '00')
				 		{
					 		listHtml.push('	<tr>');
							listHtml.push('		  <td>전기이월</td>');
							listHtml.push('	  	  <td> </td>');
							listHtml.push('	  	  <td> </td>');
							listHtml.push('	  	  <td>'+addComma(jan_amt)+'</td>');
							listHtml.push('	</tr>');
				 		}else if( field_name == '99'){
				 			listHtml.push('	<tr>');
							listHtml.push('		  <td>합 계</td>');
							listHtml.push('	  	  <td>'+addComma(month_fee)+'</td>');
							listHtml.push('	  	  <td>'+addComma(month_in)+'</td>');
							listHtml.push('	  	  <td> </td>');
							listHtml.push('	</tr>');
				 		}else{
				 			listHtml.push('	<tr>');
							listHtml.push('		  <td>'+field_name+ ' 월'+'</td>');
							listHtml.push('	  	  <td>'+addComma(month_fee)+'</td>');
							listHtml.push('	  	  <td>'+addComma(month_in)+'</td>');
							listHtml.push('	  	  <td>'+addComma(jan_amt)+'</td>');
							listHtml.push('	</tr>');
				 		}
						
						
					}
				}else{
					listHtml.push('		<tr>');
					listHtml.push('		  <td colspan="4" style="text-align:center;">조회된 결과가 없습니다.</td>');
					listHtml.push('		</tr>');
					
				}
				jQuery('#list_div').html(listHtml.join(''));
				
			}
		}
	});
	
}


jQuery(document).ready(function(){
	
	fn_getDuesList(); 
	
});

</script>
</head>

<body>

<!-- search -->
<form id="frm" name="frm" method="post" action="">

<input type='hidden' name='cp_code' id='cp_code' value='${input.cp_code}'>
<input type='hidden' name='ill_no' id='ill_no' value='${input.ill_no}'>

<div id="content">
		<!-- start :: content -->
			<div class="topSearch" style='clear:both;text-align: left;'>
			<select name="fyear" id="fyear" title="조회년도" style="float:left;text-align:center;">
				<%
				String toDate = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date());
				for (int i=Integer.parseInt(toDate) ; i >= 2000 ; i--) {
				%>
				<option value="<%= i %>"><%= i %> 년</option>
				<% } %>
			</select>
			<a href="javascript:fn_getDuesList();" class="pbtn05" style="margin-left:50px;"><span>검색</span></a>
			</div>
			<div class="list_type2">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<thead>
						<tr>
							<th>구분</th>
							<th>월회비</th>
							<th>입금액</th>
							<th>미납회비</th>
						</tr>
					</thead>
					<tbody id="list_div">
						<tr>
							<td colspan="4">검색된 결과가 없습니다</td>
						</tr>
					</tbody>
				</table>
			</div>

		<!-- end :: content -->
		</div>
</form>		
		
</body>
</html>