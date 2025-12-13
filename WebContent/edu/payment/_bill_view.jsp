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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
<%@ include file="/common/common.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>

<body>
<div id="popup_head_1">영수증확인</div>


<div id='btnDiv' style="display:block;" class="right_btnarea">
	<button class="ui-icon-print" id='btn_print'>인쇄</button>
	<button class="ui-icon-cancel" id='btn_cancel'>닫기</button>
</div>
<div id="printDiv">
<table  cellspacing="0" cellpadding="0" bgcolor="#000000" align="center" style="margin:0px 10px 10px 10px;border:1px solid black;">
  <tr>
<td bgcolor="#FFFFFF">
<table id="Table_01" width="662" height="462" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td height="34" colspan="5"> </td>
		<td colspan="5" align="right" valign="bottom"><span class="text_01">제 ${output.user_lecture_seq_no }호</span></td>
	  <td rowspan="2"> </td>
		
  </tr>
	<tr>
		<td height="50" colspan="10" rowspan="2"><h1>영 수 증</h1> </td>
		
	</tr>
	<tr>
		<td height="50"> </td>
		
	</tr>
	<tr>
	  <td width="46" height="60"> </td>
	  <td width="242" height="60" valign="bottom" class="underline_01">${output.user_nm }<img src="/img/main/nbsp.gif" width=10px">귀하</td>
	  <td height="60" colspan="9"> </td>
		
	</tr>
	<tr>
	  <td width="46" height="71"> </td>
	  <td height="71" colspan="2"><table width="100%" height="68" border="0" cellpadding="0" cellspacing="0">
	    <tr>
	      <td width="17%" height="42" valign="bottom" class="underline_02">일금</td>
	      <td class="underline_03" valign="bottom" width="65%">${output.fmtPrice }</td>
	      <td width="18%" valign="bottom" class="underline_02">원정</td>
	      </tr>
	    </table>
      </td>
		<td width="54" height="71"> </td>
		<td height="71" colspan="5"><table width="100%" height="68" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="13%" valign="bottom" class="underline_02"></td>
		    <td width="87%" class="underline_03" valign="bottom"><fmt:formatNumber value="${output.lgd_amount }" pattern="￦ #,###,###" /></td>
		    </tr>
		  </table>
         </td>
		<td colspan="2"> </td>
		
	</tr>
	<tr>
		<td height="41" colspan="11" class="text_02">내 역 :<img src="/img/main/nbsp.gif" width=5px">위생교육비</td>
		
  </tr>
	<tr>
		<td height="46" colspan="11" class="text_03">상기 금액을 정히 영수함.</td>
		
  </tr>
	<tr>
		<td height="47" colspan="6" align="right">${output.issue_yyyy }년<img src="/img/main/nbsp.gif" width=10px">${output.issue_mm }월<img src="/img/main/nbsp.gif" width=10px">${output.issue_dd }일</td>
		<td colspan="5"> </td>
		
  </tr>
	<tr>
		<td height="111" colspan="7" rowspan="3" align="right"><img src="/img/main/issue_img_01.jpg" width="431" height="105"></td>
		<td width="11" height="111" rowspan="3"> </td>
		<td height="27" colspan="3"> </td>
		
  </tr>
	<tr>
		<td height="22" colspan="3" class="line_01">담 당 자</td>
		<td>
			</td>
	</tr>
	<tr>
		<td height="62" colspan="3" class="line_02"> </td>
		
	</tr>
	<tr>
		<td>
			</td>
		<td>
			</td>
		<td>
			</td>
		<td>
			</td>
		<td>
			</td>
		<td>
			</td>
		<td>
			</td>
		<td>
			</td>
		<td>
			</td>
		<td>
			</td>
		<td>
			</td>
		<td></td>
	</tr>
</table>
</td>
</tr>
</table>
</div>

<script type="text/javascript">

	
	jQuery('#btn_print').bind("click", function(){
		
		jQuery('#btnDiv').css('display', 'none');
		window.print();
	});


	jQuery('#btn_cancel').bind("click", function(){
		window.close();
	});
	
</script>
</body>
</html>