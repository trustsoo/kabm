<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp"%>
<%
	String src = input.getText("src"); //어느 화면에서 왔니?
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" type="text/javascript">

	jQuery(document).ready(function(){
		
		jQuery('#send_state').html("SMS 전송");
	
		// 문자 발송
		jQuery('#send_sms').on('click', function() {
			
			// 발송 함수
			smsSendRequest();
			return false;
		});
		
		jQuery('#special_sms').css('display', '');
		jQuery('#sms_content_view').css('display', '');
		
		//상용구 버튼
		jQuery('#sms_content_btn').bind('click', function(){
			goMenuLink('Y', '-1_tr_msg', '/common/common.jspx?cmd=smsContents&templet=voc_popup', 800, 555);
			return false;
		});
		
		//특수문자 탭
		jQuery("#contents_tabs" ).tabs({                                                                  
            activate:function(event,ui){
            	var uiIdx = ui.newTab.index();
            }                                                                          
        });
		
<%
	if(!"".equals(input.getText("hp_no")))
	{
%>
		jQuery('#hp_no').val(formattedTelnoHipen('<%=input.getText("hp_no")%>'));
		//jQuery('#hp_no').attr('readonly',true);
<%
	}
%>		
	
	});

	//상용구 입력란 초기화.
	function jsRequestInit(){
		jQuery('#seq_no').val("-1");
		
		jQuery('#ttl').val("");
		jQuery('#hp_no').val("");
		jQuery('#sms_content').val("");
		
		jQuery("#checkByte").html("0");
		
		jQuery('#sms_content').removeClass("ui-state-error");
		jQuery('#insert_icon').css('display', '');
		jQuery('#update_icon').css('display', 'none');
		
	}

	var byteIs;
	var lmsFlag = false;
	function smsChk() {
		jQuery('#send_state').html("SMS 전송");
		
		var curText;
		var strLen;
		var lastByte;
		var thisChar;
		var escChar;
		var curTotalMsg;
		var okMsg;
				
		curText = jQuery('#sms_content').val();
		strLen = curText.length;
		byteIs = 0;

		for (i = 0; i < strLen; i++) {
			thisChar = curText.charAt(i);
			escChar = escape(thisChar);

			// ´,¨, ¸ : 2byte 임에도 브라우져에서 1byte로 계산 
			if (thisChar == "´" || thisChar == "¨" || thisChar == "¸" || thisChar == "§") {
				byteIs++;
			}

			if (escChar.length > 4) {
				byteIs += 2; //특수문자 한글인 경우. 
			} else if (thisChar != '\r') { //개행을 제외한 이외의 경우 
				byteIs += 1;
			}
			// 80바이트 초과시
			if (byteIs > 80) {
				if (lmsFlag == false) {
					msgStart(msg_com_code_080);
					lmsFlag = true;
				}
				jQuery('#send_state').html("LMS 전송");
			}

			if (byteIs > 2000) { // 3페이지까지 
				msgStart(msg_com_code_081);
				thisText = curText.substring(0, i);
				jQuery('#sms_content').val(thisText);
				byteIs = lastByte;
				break;
			}

			lastByte = byteIs;
		}

		curTotalMsg = Math.ceil(byteIs / 2000);
		curEndByte = curTotalMsg * 2000;

		jQuery("#checkByte").html(byteIs);
	}
	
	// 문자 입력
	function addText(value){
		jQuery('#sms_content').val(jQuery('#sms_content').val() + value) ;
		smsChk();
		jQuery('#sms_content').focus();
	}
	
	function js_send()
	{
		try 
		{
			if(byteIs < 80)
			{					
				jQuery('#sms_type').val('S');
				jQuery('#sms_cntnt').val( jQuery("#sms_content").val() );
			} else if (byteIs >= 80) {
				jQuery('#sms_type').val('L');
				jQuery('#lms_ttl').val( jQuery("#sms_content").val().substring(0, 20) );
				jQuery('#sms_cntnt').val( jQuery("#sms_content").val() );
			}	
			
			var _url = '/common/action/send.jspx?cmd=sendSMS';
			var http = jQuery.ajax({
				url: _url,
				type: "POST",
				data: jQuery('#form_Contents_Input').serialize(),
				dataType: 'json',
				async: false,
				error: function(xhr){
					alert(xhr.status);
				},
				success: function(json){
					var code = json.result.code;
					var msg = json.result.msg;
					var item = json.result.data;

					if(code == '200')
					{			
						
						msgStart(msg_com_code_009);						
					} else
					{
						msgStart(msg_com_code_010+"("+msg+")", 'warning');
					}
					
					jsRequestInit();
				}
			});
				
			
		
		} catch (e) {}
	}
	
	//문자 전송
	function smsSendRequest()
	{
		if(!checkFormField('#form_Contents_Input')) return;
		
		
	<%
		if(!"".equals(src))
		{
	%>
				try
				{
					if(byteIs < 80)
					{					
						jQuery('#sms_type').val('S');
						jQuery('#sms_cntnt').val( jQuery("#sms_content").val() );
					} else if (byteIs >= 80) {
						jQuery('#sms_type').val('L');
						jQuery('#lms_ttl').val( jQuery("#sms_content").val().substring(0, 20) );
						jQuery('#sms_cntnt').val( jQuery("#sms_content").val() );
					}	
					
					opener.setExtendDataSend('SMS', jQuery('#form_Contents_Input').serialize());
				} catch(e)
				{						
				}
						
	<%
		} else
		{
	%>
				js_send();
	<%
		}
	%>	
		//self.close();
	}
	
		
</script>
</head>
<body>

	<div class="row-fluid">
		<div class="span12">
			
			
			<div class="alert alert-block alert-success" style='' id='_help_div'>
				<button type="button" class="close" data-dismiss="alert">
					<i class="icon- fa fa-times-circle"></i>
				</button>
				<i class="icon- fa fa-check green"></i>				
				관리 제목 필드는 고객에게 노출되는 내용이 아님에 유의하세요.<br>
<%
	if(!"".equals(src))
	{
%>				
				<i class="icon- fa fa-check green"></i>		
				상담 창을 닫지 마시고 전송 버튼을 클릭하세요. SMS를 통해 전송된 내용이 자동으로 상담내용에 입력됩니다.
<%
	}
%>								
			</div>
			
			<!-- div><i class='icon- fa fa-caret-right blue'></i> 문자 발송</div-->

				<div class="widget-header widget-header-flat popup-header-custom">
					<h5 class="lighter" style="color:#fff"><i class='icon- fa fa-caret-right ' style="color:#fff"></i><b>SMS 전송</b></h5>
					
				</div>
					<div class="widget-main">

						<form class="form-horizontal" id='form_Contents_Input' name='form_Contents_Input' method='POST'>
							<input type='hidden' name='sms_type' id='sms_type' value='S'>
							<input type='hidden' name='lms_ttl' id='lms_ttl'>
							<input type='hidden' name='sms_cntnt' id='sms_cntnt'>
							
							<div class="form-group margin_0">
															
								<div class="col-sm-8 W70P">
									<span class="pull-right P-R12">
										<a id="sms_content_btn" class="btn btn-info btn-sm p-R10_L10 btn-curved"> 상용구</a>
									</span>
									<div style="height:5px;"></div>
									<label class="W80_R"><i class='icon- fa fa-caret-right blue'></i> 수신번호</label>
									<input id="hp_no" name="hp_no" class="ubicus_textbox_width_double" type="text" required='true' alt="수신번호" onkeyup="autoTelnoHipen(this)">
									
									<div style="height:5px;"></div>	
										
									<div>
										<label class="blank_5"></label>
										<label class="W70_R"><i class='icon- fa fa-caret-right blue'></i> 발송내용</label>
									</div>	
								
									<div>
										<textarea class="W100P textArea_height150 resize_none" id="sms_content" name="sms_content" required='true' alt="내용" onkeydown="smsChk();" onkeyup="smsChk();"></textarea>
										<div style="float:left;"> <label><span id="send_state" style="font-weight: bold;"></span></label></div>
										<div style="float:right;"><label><span id="checkByte">0</span>/2000 Byte</label></div>
									</div>
								</div>
								
								<div id="special_sms" class="col-sm-4 W30P">
									<label class="W60_R"> 특수문자</label>
									<div class="special_content" id="contents_tabs">
										<ul>
											<li>
												<a href="#tabs-1">문자 1</a>
											</li>	
											<li>
												<a href="#tabs-2">문자 2</a>
											</li>			
											<li>
												<a href="#tabs-3">문자 3</a>
											</li>				
										</ul>
						
										<div id="tabs-1" class="ui-tabs-panel-extend special_content_tab">			
											<div class="special_content_panel W100P">			
												<div>
													&nbsp;<a href="#" onclick="javascript:addText('★');">★</a>
									                <a href="#" onclick="javascript:addText('☆');">☆</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('☎');">☎</a>
										            &nbsp;<a href="#" onclick="javascript:addText('☏');">☏</a>
										            &nbsp;<a href="#" onclick="javascript:addText('☜');">☜</a>
										            &nbsp;<a href="#" onclick="javascript:addText('☞');">☞</a>
										            &nbsp;<a href="#" onclick="javascript:addText('※');">※</a>
										            &nbsp;<a href="#" onclick="javascript:addText('♡');">♡</a>
									            </div>
									            <div>
										            &nbsp;<a href="#" onclick="javascript:addText('♣');">♣</a>
									              	<a href="#" onclick="javascript:addText('♠');">♠</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('♤');">♤</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('♧');">♧</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('♨');">♨</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('ㆀ');">ㆀ</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('♩');">♩</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('♪');">♪</a>
								              	</div>
								              	<div>
									              	&nbsp;<a href="#" onclick="javascript:addText('♬');">♬</a>
									              	<a href="#" onclick="javascript:addText('■');">■</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('□');">□</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('▣');">▣</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('▤');">▤</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('▦');">▦</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('▨');">▨</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('▩');">▩</a>
								              	</div>
								              	<div>
									              	<a href="#" onclick="javascript:addText('▒');"> ▒</a>
									              	<a href="#" onclick="javascript:addText('▲');">▲</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('△');">△</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('▶');">▶</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('▷');">▷</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('▼');">▼</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('▽');">▽</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('◀');">◀</a>
								              	</div>
								              	<div>
									              	&nbsp;<a href="#" onclick="javascript:addText('◁');">◁</a>
									              	<a href="#" onclick="javascript:addText('◆');">◆</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('◇');">◇</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('◈');">◈</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('●');">●</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('○');">○</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('◎');">◎</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('⊙');">⊙</a>
								              	</div>
								              	<div>
									              	&nbsp;<a href="#" onclick="javascript:addText('◐');">◐</a>
									              	<a href="#" onclick="javascript:addText('◑');">◑</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('←');">←</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('↑');">↑</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('→');">→</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('↓');">↓</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('⇒');">⇒</a>
									              	&nbsp;<a href="#" onclick="javascript:addText('§');">§</a>
								              	</div>
											</div>
										</div>
										
										<div id="tabs-2"  class="ui-tabs-panel-extend special_content_tab">
											<div class="special_content_panel W100P">
												<div>
															 <a href="#" onclick="javascript:addText('（');">（ </a>
										            &nbsp;<a href="#" onclick="javascript:addText('（');">）</a>
										            		 <a href="#" onclick="javascript:addText('［');">［ </a>
										            &nbsp;<a href="#" onclick="javascript:addText('］');">］</a>
										            		 <a href="#" onclick="javascript:addText('｛');">｛ </a>
										            &nbsp;<a href="#" onclick="javascript:addText('｝');">｝</a>
										            		 <a href="#" onclick="javascript:addText('〔');">〔 </a>
										            <a href="#" onclick="javascript:addText('〕');"> 〕</a>
									            </div>
										        <div>
										            		 <a href="#" onclick="javascript:addText('〈');">〈</a>
										            &nbsp;<a href="#" onclick="javascript:addText('〉');">〉</a>
										            		 <a href="#" onclick="javascript:addText('《');">《 </a>
										            &nbsp;<a href="#" onclick="javascript:addText('》');">》</a>
										            		 <a href="#" onclick="javascript:addText('「');">「</a>
										            &nbsp;<a href="#" onclick="javascript:addText('」');">」</a>
										            		 <a href="#" onclick="javascript:addText('『');">『 </a>
										            <a href="#" onclick="javascript:addText('』');"> 』</a>
									            </div>
										        <div>
										            		 <a href="#" onclick="javascript:addText('【');">【</a>
										            &nbsp;<a href="#" onclick="javascript:addText('】');">】</a>
										            		 <a href="#" onclick="javascript:addText('＜');">＜ </a>
										            		 <a href="#" onclick="javascript:addText('＞');"> ＞</a>
										            		 <a href="#" onclick="javascript:addText('≪');">≪ </a>
										            		 <a href="#" onclick="javascript:addText('≫');">≫ </a>
										            &nbsp;<a href="#" onclick="javascript:addText('⇔');">⇔ </a>
										           			 <a href="#" onclick="javascript:addText('↕');">↕</a>
									            </div>
										        <div>
										            &nbsp;<a href="#" onclick="javascript:addText('↗');"> ↗</a>
										            		 <a href="#" onclick="javascript:addText('↙');">↙</a>
										            &nbsp;<a href="#" onclick="javascript:addText('↖');">↖</a>
										            &nbsp;<a href="#" onclick="javascript:addText('↘');">↘</a>
										            &nbsp;<a href="#" onclick="javascript:addText('†');">†</a>
										            &nbsp;<a href="#" onclick="javascript:addText('≠');">≠</a>
										            &nbsp;<a href="#" onclick="javascript:addText('∀');">∀</a>
										            &nbsp;<a href="#" onclick="javascript:addText('∞');">∞</a>
									            </div>
										        <div>
										            &nbsp;<a href="#" onclick="javascript:addText('∏');">∏</a>
										           		 	 <a href="#" onclick="javascript:addText('∧');">∧</a>
										            &nbsp;<a href="#" onclick="javascript:addText('∪');">∪</a>
										            &nbsp;<a href="#" onclick="javascript:addText('◈');">◈</a>
										            &nbsp;<a href="#" onclick="javascript:addText('∴');">∴</a>
										           			 <a href="#" onclick="javascript:addText('∽');">∽</a>
										            &nbsp;<a href="#" onclick="javascript:addText('⊃');">⊃</a>
										            &nbsp;<a href="#" onclick="javascript:addText('￠');">￠</a>
									            </div>
										        <div>
										            &nbsp;<a href="#" onclick="javascript:addText('￥');">￥</a>
										            		 <a href="#" onclick="javascript:addText('㉿');">㉿</a>
										            		 <a href="#" onclick="javascript:addText('㈜');">㈜</a>
										            		 <a href="#" onclick="javascript:addText('㏇');">㏇</a>
										            		 <a href="#" onclick="javascript:addText('™');">™ </a>
										            &nbsp;<a href="#" onclick="javascript:addText('㏂');">㏂</a>
										            		 <a href="#" onclick="javascript:addText('㏘');">㏘</a>
										            		 <a href="#" onclick="javascript:addText('℡');">℡</a>
									            </div>
											</div>								
										</div>
										
										<div id="tabs-3"  class="ui-tabs-panel-extend special_content_tab">
											<div class="special_content_panel W100P">
												<div>
													&nbsp;<a href="#" onclick="javascript:addText('㉠');"> ㉠</a>
										            		 <a href="#" onclick="javascript:addText('㉡');">㉡</a>
										            		 <a href="#" onclick="javascript:addText('㉢');">㉢</a>
										            		 <a href="#" onclick="javascript:addText('㉣');">㉣</a>
										            		 <a href="#" onclick="javascript:addText('㉤');">㉤</a>
										            		 <a href="#" onclick="javascript:addText('㉥');">㉥</a>
										            		 <a href="#" onclick="javascript:addText('㉦');">㉦</a>
										            		 <a href="#" onclick="javascript:addText('㉧');">㉧</a>
										        </div>                                   
										        <div>                                    
										            &nbsp;<a href="#" onclick="javascript:addText('㉨');"> ㉨</a>
										            		 <a href="#" onclick="javascript:addText('㉩');">㉩</a>
										            		 <a href="#" onclick="javascript:addText('㉪');">㉪</a>
										            		 <a href="#" onclick="javascript:addText('㉫');">㉫</a>
										            		 <a href="#" onclick="javascript:addText('㉬');">㉬</a>
										            		 <a href="#" onclick="javascript:addText('㉭');">㉭</a>
										            		 <a href="#" onclick="javascript:addText('㉮');">㉮</a>
										            		 <a href="#" onclick="javascript:addText('㉯');">㉯</a>
												</div>                                   
										        <div>                                    
										            &nbsp;<a href="#" onclick="javascript:addText('㉰');"> ㉰</a>
										            		 <a href="#" onclick="javascript:addText('㉱');">㉱</a>
										            		 <a href="#" onclick="javascript:addText('㉲');">㉲</a>
										            		 <a href="#" onclick="javascript:addText('㉳');">㉳</a>
										            		 <a href="#" onclick="javascript:addText('㉴');">㉴</a>
										            		 <a href="#" onclick="javascript:addText('㉵');">㉵</a>
										            		 <a href="#" onclick="javascript:addText('㉶');">㉶</a>
										            		 <a href="#" onclick="javascript:addText('㉷');">㉷</a>
										         </div>                                   
										         <div>                                    
										            &nbsp;<a href="#" onclick="javascript:addText('㉸');"> ㉸</a>
										            		 <a href="#" onclick="javascript:addText('㉹');">㉹</a>
										            		 <a href="#" onclick="javascript:addText('㉺');">㉺</a>
										            		 <a href="#" onclick="javascript:addText('㉻');">㉻</a>
										            		 <a href="#" onclick="javascript:addText('＇');">＇</a>
										            		 <a href="#" onclick="javascript:addText('￣');">￣</a>
										            		 <a href="#" onclick="javascript:addText('‥');">‥</a>
										            		 <a href="#" onclick="javascript:addText('…');">…</a>
										         </div>                                   
										         <div>                                    
										            &nbsp;<a href="#" onclick="javascript:addText('∥');"> ∥</a>
										            &nbsp;<a href="#" onclick="javascript:addText('＼');">＼</a>
										            &nbsp;<a href="#" onclick="javascript:addText('∼');">∼</a>
										            &nbsp;<a href="#" onclick="javascript:addText('´');">´</a>
										            &nbsp;<a href="#" onclick="javascript:addText('～');">～</a>
										            &nbsp;<a href="#" onclick="javascript:addText('ˇ');">ˇ</a>
										            &nbsp;<a href="#" onclick="javascript:addText('ː');">ː</a>
										            &nbsp;<a href="#" onclick="javascript:addText('˘');">˘</a>
										          </div>                                   
										          <div>                                    
										            &nbsp;<a href="#" onclick="javascript:addText('˝');"> ˝</a>
										            &nbsp;<a href="#" onclick="javascript:addText('˚');">˚</a>
										            &nbsp;<a href="#" onclick="javascript:addText('˙');"> ˙</a>
										            &nbsp;<a href="#" onclick="javascript:addText('¸');"> ¸</a>
										            &nbsp;<a href="#" onclick="javascript:addText('˛');"> ˛</a>
										            &nbsp;<a href="#" onclick="javascript:addText('¡');"> ¡</a>
										            &nbsp;<a href="#" onclick="javascript:addText('¿');"> ¿</a>
										            &nbsp;<a href="#" onclick="javascript:addText('∃');"> ∃</a>
												</div>
											</div>								
										</div>
									</div>
								</div>
								
							</div>
						</form>
						
					</div>
			<div class="right">
				<button id="send_sms" class="btn  btn-sm btn-primary btn-curved p-R10_L10" style="margin:10px"><i class="icon- fa fa-paper-plane"></i> 전송</button>
			</div>
		</div>
	</div>
</body>
</html>