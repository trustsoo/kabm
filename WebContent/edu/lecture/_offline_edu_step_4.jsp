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
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.datepicker {
    font-size: 13px !important;
}
.datepicker td, .datepicker th {
    width: 30px;
    height: 25px;
}
</style>
<script type="text/javascript" class="source">

function js_nextStep()
{
	if( jQuery("input:checkbox[id='chk_alarm']").is(":checked") != true )
	{
		msgOpen("다음 단계로 넘어가기 위해서는 아래 주의사항을 꼭 정독하고 확인을 눌러주시기 바랍니다.");
		return;
	}
	
	var _url ='/edu/lecture/offlineCtrl.jspx?cmd=offlineEduStep5';
	jQuery('#frm').attr('action', _url);
	jQuery('#frm').submit();
}

function js_nextEndStep()
{
	var _url ='<%=OFFLINE_SEN_END%>';
	jQuery('#frm').attr('action', _url);
	jQuery('#frm').submit();
}

function lf_requestSubmit()
{

	jQuery('#building_tel_no').val( jQuery('#building_tel_no1').val() + '-' + jQuery('#building_tel_no2').val() + '-' + jQuery('#building_tel_no3').val() );
	
	if( jQuery('#building_tel_no1').val() == '' ||  jQuery('#building_tel_no2').val() == '')
		jQuery('#building_tel_no').val( '' );
	
	
	if( jQuery("input:checkbox[id='chk_1']").is(":checked") == true )
	{
		lf_reset();
		jQuery('#building_none').val( 'Y' );
	}else{		
		jQuery('#building_none').val( 'N' );
	}
	
	js_nextStep();
}

function lf_reset(){
	jQuery('#building_nm').val( '' );
	jQuery('#building_zipcode').val( '' );
	jQuery('#building_addr').val( '' );
	jQuery('#building_tel_no2').val( '' );
	jQuery('#building_tel_no3').val( '' );
	jQuery('#building_area').val( '' );
	jQuery('#building_start_dt').val( '' );
}

function lf_buildingSelect()
{
	openPostcode(3);
}

function lf_setBuildingInfo(zipcode ,addr)
{
	jQuery('#building_zipcode').val(zipcode);
	jQuery('#building_addr').val(addr);	
	jQuery('#building_zipcode').removeClass('ui-state-error');
	jQuery('#building_addr').removeClass('ui-state-error');
}
</script>
<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
<script>
function openPostcode(div) {
    new daum.Postcode({
        oncomplete: function(data) {
			var postcode = data.zonecode;
			var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
			lf_setBuildingInfo(postcode, addr);
        }
    }).open();
}

</script>
</head>

<body>

<form name="frm" id="frm" method="post" action="">
<input type='hidden' name='is_gb' id='is_gb' value='${input.is_gb}'>
<input type='hidden' name='cp_code' id='cp_code' value='${input.cp_code}'>
<input type='hidden' name='ji_code' id='ji_code' value='${input.ji_code}'>
<input type='hidden' name='dcm_no' id='dcm_no' value='${input.dcm_no}'>
<input type='hidden' name='next_step' id='next_step' value='5'>	
<input type='hidden' name='next_mode' id='next_mode' value='${input.next_mode}'>

<input type="hidden" name='building_tel_no' id='building_tel_no' value=''/>
<input type="hidden" name='building_none' id='building_none' value='N'/>
	
	<div id="content">
		<div id="smartwizard" class="sw-main sw-theme-arrows" style="margin-bottom: 15px;">
		<ul class="nav nav-tabs step-anchor">
            <li class="nav-item"><a href="#step-1"><strong>1 단계</strong><br><strong>개인정보 제공 동의</strong></a></li>
            <li class="nav-item"><a href="#step-2"><strong>2 단계</strong><br><strong>신청자 정보 입력</strong></a></li>
            <li class="nav-item"><a href="#step-3"><strong>3 단계</strong><br><strong>신청업체 입력</strong></a></li>
            <li class="nav-item active"><a href="#step-4"><strong>4 단계</strong><br><strong>영업장 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-5"><strong>5 단계</strong><br><strong>책입자 정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-6"><strong>6 단계</strong><br><strong>입금정보 입력</strong></a></li>
            <li class="nav-item done"><a href="#step-7"><strong>7 단계</strong><br><strong>접수완료</strong></a></li>
            <li class="nav-item done"><a href="#step-8"><strong>8 단계</strong><br><strong>결제 하기</strong></a></li>
        </ul>
		</div>
			<div class="directions">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<tbody>
						<tr>
							<th scope="row">주의사항</th>
							<td>
							<p style="margin-left:0px;color:blue;font-weight:bold;font-size:14px;">		
							영업장(청소현장) 정보를 입력하세요.<br>
							</p>	
							<p style="margin-left:0px;color:red;font-weight:bold;">							
							※ 현장이 없는 경우는 아래 현장없음에 체크하시고, 다음을 클릭하세요.
							</p>
							<div class="pull-right">
								<input type="checkbox" class="check" name="chk_alarm" id="chk_alarm"/> 
								<span style="font-size:16px;color:red;font-weight:bold;">주의사항 확인</span>
							</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="chkBox" style="margin-bottom: -50px;margin-top: 20px;">
				<label for="">
					<input type="checkbox" class="check" name="chk_1" id="chk_1"/> 
					<span style="font-size:18px;color:red;font-weight:bold;">현장없음</span>
				</label> 
			</div>
					
			<div class="view">
				<table cellpadding="0" cellspacing="0" class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width=""/><col width=""/>
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="on">영업장명(청소현장)  </th>
							<td>
								<input type="text" class="it lag" title="영업장명" value="" name='building_nm' id='building_nm' alt='영업장명'/>
								(* 영업장명 = 청소하는 현장)
							</td>
						</tr>
						<tr>
							<th scope="row" rowspan="2" class="on">영업장주소  </th>
							<td>
								<input type="text" class="it " readonly="readonly" title="영업장-우편번호" value="" name='building_zipcode' id='building_zipcode' alt='영업장-우편번호'/>
								<a class="pbtn01 mini" id='btn_buildingSel'><span class="">선택</span></a>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" class="it long" title="" value="" name='building_addr' id='building_addr' alt='영업장주소' />
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">영업장전화번호  </th>
							<td>
								<select style="width:80px;" name='building_tel_no1' id='building_tel_no1'>
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
								<input type="text" class="it sm" maxlength="4" title="영업장전화번호" value="" name='building_tel_no2' id='building_tel_no2' />
								-
								<input type="text" class="it sm" maxlength="4" title="영업장전화번호" value="" name='building_tel_no3' id='building_tel_no3' />
							</td>
						</tr>
						
						<tr>
							<th scope="row" class="on">연면적  </th>
							<td>
								<input type="text" class="it" title="연면적" value="" name='building_area' id='building_area' alt='연면적'/> 	㎡
							</td>
						</tr>
						<tr>
							<th scope="row" class="on">영업개시일 </th>
							<td>
								<span class="input-group textbox_width" style="width: 200px;">
										<input class="date-picker W100P form-control" id="building_start_dt" name="building_start_dt" type="text" value="" data-date-format="yyyy-mm-dd"/>
										<span class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</span>
								</span>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			
			<div class="btnWrap">
				<a class="pbtn01" id='btn_reqCancel'><span class="">취소</span></a>
				<a href="javascript:history.go(-1);" class="pbtn02"><span class=""><i class="fa fa-mail-reply-all" style="font-size:18px"></i> 뒤로</span></a>
				<a class="pbtn02" id='btn_reg'><span class="">다음 <i class="fa fa-sign-in" style="font-size:18px"></i></span></a>
			</div>
						
	
		</div>
</form>



<script type="text/javascript">
	jQuery(document).ready(function()	{
			
			jQuery('#btn_reqCancel').bind("click", function(){				
				if( jQuery('#next_mode').val() == 'add')
				{
					if(!confirm('작성중인 정보가 초기화 됩니다. 정말 취소하시겠습니까?')) return;
					js_nextEndStep();
				}else{
					if(!confirm('작성중인 정보가 초기화 됩니다. 정말 취소하시겠습니까?')) return;
					location.href='<%=OFFLINE_SEN_REDIRECT%>';
				}
			});
			
			jQuery('#btn_buildingSel').bind("click", function(){
				lf_buildingSelect();
			});
			
			jQuery('#btn_reg').bind("click", function(){
				lf_requestSubmit();
			});
			
			jQuery("input:checkbox[id='chk_1']").bind('click', function(){
			    if ( $(this).is(':checked') ) {
			    	lf_reset();
			    }
			});
			
			$(".date-picker").datepicker({
				format: 'yyyy-mm-dd',			
				todayHighlight: true,
				autoclose: true,
				language: 'kr'
			});
		
		}
	);
	
</script>
</body>
</html>