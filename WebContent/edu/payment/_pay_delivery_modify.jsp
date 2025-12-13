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
<script type="text/javascript" class="source">


function lf_postSelect()
{
	openPostcode(2);
}


function lf_requestSubmit(div)
{
	
	var post_sel_div = jQuery('#post_sel_div').val();
	
	switch(post_sel_div)
	{
		case 'CORP' :
			jQuery('#post_zipcode').val( jQuery('#cpost').val() );
			jQuery('#post_addr').val( jQuery('#addr_nm').val() );
			jQuery('#post_tel_no').val('');
			
			break;
		case 'HOME' :
			jQuery('#post_zipcode').val( jQuery('#zipcode').val() );
			jQuery('#post_addr').val( jQuery('#addr').val() );
			jQuery('#post_tel_no').val('');
			
			break;
		case 'NEW' :
			
			if(!checkFormField('#frm_mem'))
				return false;
			
			jQuery('#post_tel_no').val( jQuery('#post_tel_no1').val() + '-' + jQuery('#post_tel_no2').val() + '-' + jQuery('#post_tel_no3').val() );
			if( jQuery('#post_tel_no1').val() == '' ||  jQuery('#post_tel_no2').val() == '')
				jQuery('#post_tel_no').val( '' );
			
			jQuery('#post_zipcode').val( jQuery('#new_post_zipcode').val() );
			jQuery('#post_addr').val( jQuery('#new_post_addr').val() );
			
			break;	
	}
	
	var http = jQuery.ajax( {
		url: "/edu/payment/action/paymentAction.jspx?cmd=doDeliveryModify&templet-bypass",
   		type: "POST",
		data : jQuery('#frm_mem').serialize(true),
   		async:false,
   		error 	: function(xml)
   		{
			msgStart(msg_com_code_007, 'danger');
		},
   		success:function(json) 
   		{	
   			var code = json.result.code;
	   		var msg =  json.result.msg;
	   		var data = json.result.data;
	   		
	   		if(code == '200')
			{
	   			opener.js_getPaymentList();
	   			window.close();	
			} else
			{	
				msgStart(msg_com_code_007, 'danger');
			}
		}
  	});
}

function lf_setPostInfo(zipcode ,addr)
{
	jQuery('#new_post_zipcode').val(zipcode);
	jQuery('#new_post_addr').val(addr);
	
	jQuery('#new_post_zipcode').removeClass('ui-state-error');
	jQuery('#new_post_addr').removeClass('ui-state-error');
}

function setDeliveryView(code)
{
	jQuery('#post_sel_div').val(code);
	switch(code)
	{
		case 'CORP' :
			jQuery('#DIVCORP').css("display","block");
			jQuery('#DIVHOME').css("display","none");
			jQuery('#DIVNEW').css("display","none");
			break;
		case 'HOME' :
			jQuery('#DIVCORP').css("display","none");
			jQuery('#DIVHOME').css("display","block");
			jQuery('#DIVNEW').css("display","none");
			break;
		case 'NEW' :
			jQuery('#DIVCORP').css("display","none");
			jQuery('#DIVHOME').css("display","none");
			jQuery('#DIVNEW').css("display","block");
			break;	
	}


}

</script>
<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
<script>
function openPostcode(div) {
    new daum.Postcode({
        oncomplete: function(data) {
        	//var postcode = data.postcode1 + '-' + data.postcode2;
        	//var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
        	//var addr = data.address1;
			var postcode = data.zonecode;
			var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
			if( div == 1)
			{
				
			}else if( div == 2)
			{
				lf_setPostInfo(postcode, addr);
			}
        }
    }).open();
}
</script>

<style>
.popupLayer .popupCont_bottom table th {
    text-align: center!important;
    vertical-align: middle!important;
}
</style>
</head>
		
<body>			
<form name="frm_mem" id="frm_mem" method="post" >
<input type="hidden" name='cpost' id='cpost' value="${output.cpost }" />
<input type="hidden" name='addr_nm' id='addr_nm' value="${output.addr_nm }" />
<input type="hidden" name='zipcode' id='zipcode' value="${output.zipcode }" />
<input type="hidden" name='addr' id='addr' value="${output.addr }" />

<input type='hidden' name='user_seq_no' id='user_seq_no' value='${input.user_seq_no}'>
<input type='hidden' name='user_id' id='user_id' value='${input.user_id}'>
<input type="hidden" name='post_zipcode' id='post_zipcode' value=''/>
<input type="hidden" name='post_addr' id='post_addr' value=''/>
<input type="hidden" name='post_tel_no' id='post_tel_no' value=''/>
<input type="hidden" name='post_sel_div' id='post_sel_div' value='CORP'/>
<input type="hidden" name='LGD_OID' id='LGD_OID' value='${input.LGD_OID}'/>

<div class="popupLayer" style="">
	<a href="#" onclick="self.close();" class="btnClose"><img src="/static/main/img/sub/btn_close.jpg" alt="close" /></a>
	<div class="popupCont_top">
		<div class="stit">
			교재배송지 입력
		</div>
	</div>
	<div class="popupCont_bottom " style="height: 695px;font-size: 14px;">
	
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">
					<i class="icon-remove"></i>
				</button>
				<i class="icon-ok green"></i>			
				<strong class="">◎ 교육비 결제완료 후에, 입력하신 배송지로 교재가 발송됩니다.	</strong><br><br>
				<strong class="">◎ 배송지주소를 정확하게 입력해주시기 바라며, 반송될 경우 착불로 수령 가능합니다. </strong><br><br>
				<strong class="">◎ [회사주소],[자택주소]가 공란이거나 배송지 변경할 경우 [신규(최근)배송지]란 <br>&nbsp;&nbsp;&nbsp;작성하시면 됩니다.</strong> 
			</div>
			
			<div class="view" style="margin-top: 10px;">
				<table cellpadding="0" cellspacing="0" class="" summary="" style="border-top: 1px solid #1e90f1;border-bottom: 1px solid #1e90f1;">
					<caption></caption>
					<colgroup>
						<col width="150px"/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="">상품명 </th>
							<td>
								${ input.product_nm } 교재
							</td>
						</tr>
						<tr>
							<th scope="row" class="">배송지 선택 </th>
							<td>
								<p>
								    <input type="radio" class="flat" name="deliverySel" id="corpSel"  value="CORP" checked="" required /> 회사주소 &nbsp; &nbsp;
			                        <input type="radio" class="flat" name="deliverySel" id="homeSel"  value="HOME" /> 자택주소 &nbsp; &nbsp;
			                        <input type="radio" class="flat" name="deliverySel" id="newSel"  value="NEW" /> 신규(최근)배송지
			                     	
								</p>
							</td>
						</tr>
					</tbody>
				</table>	
			</div>
			
			<div style="font-weight:bold;color:#1e90f1;margin-top:20px;">▣  교재배송지 정보</div>
			<div class="view" id="DIVCORP" style="display:block;margin-top: 10px;">
				<table cellpadding="0" cellspacing="0" class="" summary="" style="border-top: 1px solid #1e90f1;border-bottom: 1px solid #1e90f1;">
					<caption></caption>
					<colgroup>
						<col width="150px"/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">성 명  </th>
							<td>
								${output.user_nm }
							</td>
						</tr>
						
						<tr>
							<th scope="row" class="on">연락처  </th>
							<td>
								${output.tel_no }
							</td>
						</tr>
						<tr>
							<th scope="row" class=""> 회사주소 </th>
							<td>
								(${output.cpost }) ${output.addr_nm }
								
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			
			<div class="view" id="DIVHOME" style="display:none;margin-top: 10px;">
				<table cellpadding="0" cellspacing="0" class="" summary="" style="border-top: 1px solid #1e90f1;border-bottom: 1px solid #1e90f1;">
					<caption></caption>
					<colgroup>
						<col width="150px"/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">성 명  </th>
							<td>
								${output.user_nm }
							</td>
						</tr>
						
						<tr>
							<th scope="row" class="on">연락처  </th>
							<td>
								${output.tel_no }
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">자택주소 </th>
							<td>
								(${output.zipcode }) ${output.addr }
								
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			
			<div class="view" id="DIVNEW" style="display:none;margin-top: 10px;">
				<table cellpadding="0" cellspacing="0" class="" summary="" style="border-top: 1px solid #1e90f1;border-bottom: 1px solid #1e90f1;">
					<caption></caption>
					<colgroup>
						<col width="150px"/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">성 명  </th>
							<td>
								${output.user_nm }
							</td>
						</tr>
						
						<tr>
							<th scope="row" class="on">연락처  </th>
							<td>
								${output.tel_no }
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">연락처2  </th>
							<td>
								<select style="width:80px;" name='post_tel_no1' id='post_tel_no1'>
									<option value="010">010</option>
									<option value="011">011</option>
									<option value="016">016</option>
									<option value="017">017</option>
									<option value="019">019</option>
									<option value="02">02</option>
									<option value="031">031</option>
									<option value="032">032</option>
									<option value="033">033</option>
									<option value="041">041</option>
									<option value="042">042</option>
									<option value="043">043</option>
									<option value="044">044</option>
									<option value="051">051</option>
									<option value="052">052</option>
									<option value="053">053</option>									
									<option value="054">054</option>
									<option value="055">055</option>
									<option value="061">061</option>
									<option value="062">062</option>
									<option value="063">063</option>
									<option value="064">064</option>
									<option value="070">070</option>
								</select>
								-
								<input type="text" class="it sm" maxlength="4" title="연락처2" value="" name='post_tel_no2' id='post_tel_no2' />
								-
								<input type="text" class="it sm" maxlength="4" title="연락처2" value="" name='post_tel_no3' id='post_tel_no3' />
							</td>
						</tr>			
						<tr>
							<th scope="row" rowspan="2" class="on">교재수령지 <i class="fa fa-asterisk tred"> </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="교재수령지우편번호" value="${output.post_zipcode }" name='new_post_zipcode' id='new_post_zipcode' alt='교재수령지우편번호' required='true'/>
								<a class="pbtn01 mini" ><span class="" id='btn_postSel'>선택</span></a>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" class="it long" title="교재수령지" value="${output.post_addr }" name='new_post_addr' id='new_post_addr' alt='교재수령지' required='true'/>
							</td>
						</tr>
						
						
					</tbody>
				</table>
			</div>
			
			<div class="view" style="margin-top: 20px;">
				<table cellpadding="0" cellspacing="0" class="" summary="" style="border-top: 1px solid #1e90f1;border-bottom: 1px solid #1e90f1;">
					<caption></caption>
					<colgroup>
						<col width="150px"/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="">배송시 요청사항 </th>
							<td>
								<input type="text" class="it long" title="배송시 요청사항" value="${output.post_req }" name='post_req' id='post_req' alt='배송시 요청사항' />
								
							</td>
						</tr>
					</tbody>
				</table>	
			</div>
			
			<div class="btnWrap">
				<a class="pbtn02" id='btn_reg'><span class="">저장</span></a>
				<a href="javascript:self.close();" class="pbtn01"><span class="">취소</span></a>
			</div>
		</div>
</form>


<script type="text/javascript">
jQuery(document).ready(
		function()
		{	
			
			jQuery('#btn_postSel').bind("click", function(){
				lf_postSelect();
			});
			
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit(1);
			});	
			
			var tel_no = '${output.post_tel_no }';
			var tel_noArr = tel_no.split('-');
			if( tel_noArr.length == 3 )
			{
				jQuery('#post_tel_no1').val(tel_noArr[0]);
				jQuery('#post_tel_no2').val(tel_noArr[1]);
				jQuery('#post_tel_no3').val(tel_noArr[2]);
			}
			
			
			jQuery(".defaultText").focus(function(srcc)
		    {
		        if (jQuery(this).val() == jQuery(this)[0].title)
		        {
		        	jQuery(this).removeClass("defaultTextActive");
		        	jQuery(this).val("");
		        }
		    });
		    
		    jQuery(".defaultText").blur(function()
		    {
		        if (jQuery(this).val() == "")
		        {
		        	jQuery(this).addClass("defaultTextActive");
		        	jQuery(this).val(jQuery(this)[0].title);
		        }
		    });
		    
		    jQuery(".defaultText").blur();  
		    
		 // iCheck
		    $(document).ready(function() {
		        if ($("input.flat")[0]) {
		            $(document).ready(function () {
		                $('input.flat').iCheck({
		                    checkboxClass: 'icheckbox_flat-green',
		                    radioClass: 'iradio_flat-green'
		                });
		                
		                $('input.flat').on('ifChecked', function(event){
		                	setDeliveryView( $(this).val() );		    		    	
		    		    });
		            });
		        }
		    });
		    // /iCheck
		   
		}
	);
</script>


</body>
</html>			