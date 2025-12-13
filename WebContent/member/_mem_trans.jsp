<%
/******************************************************************************** 
 * Program ID	: 멤버정보 작성
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
<script type="text/javascript" src="/js/year.js"></script>
<script type="text/javascript" class="source">

function lf_idConfirm()
{
	if(jQuery('#user_id').val() == '')
	{
		msgStart(msg_mem_code_001);
        jQuery('#isConfirm').val('N')
		return;
	}

	var http = jQuery.ajax( {
		url: CONTEXT_PATH +"/member/memberAction.jspx?cmd=idConfirm&templet-bypass&user_id="+jQuery('#user_id').val(),
   		type: "POST",
		data : '',
   		async:false,
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_007);
		},
   		success:function(xmlDoc)
   		{
	   		var code = jQuery(xmlDoc).find('code').text();
	   		var data = jQuery(xmlDoc).find('data').text();

			if( code == "success")
			{
				var json = textToJson( data );
				var user_id = json.data[0].user_id;

				if( user_id != '' )
				{
					msgStart(msg_mem_code_003);
                    jQuery('#isConfirm').val('N');
				}
				else
				{
					msgStart(msg_mem_code_002);
                    jQuery('#isConfirm').val('Y');
                    jQuery('#confirmSSN').val(jQuery('#user_id').val());
				}
			} else
			{
				gf_processError(code, msg_com_code_007, data);
                jQuery('#isConfirm').val('N')
			}
		}
  	});

}


function lf_requestSubmit()
{
	if(!checkFormField('#frm'))
	{
		return false;
	}
	
    if( jQuery('#isConfirm').val() == 'N' || jQuery('#user_id').val() !=  jQuery('#confirmSSN').val())
    {
        msgStart(msg_mem_code_006);
		return;
    }
	
	var http = jQuery.ajax( {
		url: CONTEXT_PATH +"/member/memberAction.jspx?cmd=doIdModify&templet-bypass",
   		type: "POST",
		data : jQuery('#frm').serialize(true),
   		async:false,
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_007);
		},
   		success:function(xmlDoc) 
   		{	
	   		var code = jQuery(xmlDoc).find('code').text(); 		
	   		var data = jQuery(xmlDoc).find('data').text();
	   		
			
			if( code == "success")
			{
				msgStart(msg_com_code_009);
				alert( '아이디 변경이 정상 처리되었습니다. \n다시 로그인 해주시기 바랍니다.');
				location.href = '/common/logout.jsp';
			} else
			{	
				gf_processError(code, msg_com_code_007, data);
			}
		}
  	});
}

</script>
</head>
		
<body>

<br>
<div style="width:600px;margin-left:150px;">
<table width="100%"  border="2" align="left" cellpadding="0" cellspacing="0">
  <tr>
    <th scope="col"><table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
      <tr align="left">
        <td height="30" colspan="2" scope="col" style="font-weight:bold;font-size:1.2em;color:#48AB46;">주의사항</td>
      </tr>
      <tr align="left">
        <td width="23" valign="top"><img src="/img/main/gaib_04.gif" width="20" height="20"></td>
        <td>&nbsp;개인정보 보호를 위해 기존 주민등록번호를 아이디로 변경하여야 합니다.        
        </td>        
      </tr>
      <tr align="left">
        <td width="23"><img src="/img/main/gaib_04.gif" width="20" height="20"></td>
        <td>&nbsp;변경 후 주민등록번호는 안전하게 삭제 처리 됩니다.     
        </td>        
      </tr>
           
    </table></th>
  </tr>
</table>
</div>
	
<form name="frm" id="frm" method="post" onsubmit="return false;">
<input type="hidden" name="isConfirm" id="isConfirm" value="N">
<input type="hidden" name="confirmSSN" id="confirmSSN" value="">

<table width="600"  border="0" align="center" cellpadding="0" cellspacing="0">
     <tr align="left">
       <td height="30" colspan="5" nowrap></td>
     </tr>
     <tr align="left">
       <td width="23"><img src="/img/main/gaib_04.gif" width="20" height="20"></td>
       <td width="132" height="25"><strong class="style1">현재아이디:</strong></td>
       <td>&nbsp;</td>
       <td colspan="2">${input.user_id }
         </td>
     </tr>  
     <tr align="left">
       <td height="5" colspan="5" nowrap></td>
     </tr>
     <tr align="left">
       <td width="23"><img src="/img/main/gaib_04.gif" width="20" height="20"></td>
       <td width="132" height="25"><strong class="style1">신규아이디:</strong></td>
       <td>&nbsp;</td>
       <td><input name='new_user_id' id='user_id' alt='신규아이디' type="text" class="w200px" value='' required='true' /></td>
       <td width="262"><a href="javascript:lf_idConfirm();"><img src="/img/main/gaib_03.gif" width="80" height="25" border="0"></a></td>
     </tr>
     
   </table>

					
<!-- right -->

<br>
<div id='btnDiv' class="center_btnarea">
	<button class="ui-icon-document" id='btn_reg'>저장</button>
</div>
	

</form>


<script type="text/javascript">
jQuery(document).ready(
		function()
		{	
			
			
			jQuery('form#frm button').each(
					function()
					{
						var cls = jQuery(this).attr('class');
						jQuery(this).button({
				            icons: {
				            	primary: cls
							}
						});
					}
				);
			
			
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
			});	
			
		}
	);
</script>


</body>
</html>			