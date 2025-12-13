<%@page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp"%>
<%
	String paper_id = input.getText("paper_id");
	String user_lecture_seq_no = input.getText("user_lecture_seq_no");
%>

<!DOCTYPE html>
<html>
<head>
<style>
	.textArea_height100 {height:100px;}
	.widget-body_A {max-width:1020px; border-top:1px solid #CCC;border-bottom:1px solid #CCC;}
	input[type="radio"], input[type="checkbox"]{
		vertical-align:top;
	}
	
	.qstn{
		padding-left: 50px;
		text-indent: -25px;
		font-weight: bold;
		padding-bottom: 5px;
		border-bottom:1px solid #CCC;
	}

	.qstn-text{
		font-weight:bold;
		font-size:15px;
	}
	
	.exmp-text{
		font-size: 13px !important;
		font-weight: normal !important;
	}
	.center{
		text-align:center;
	}
	.tit2 {
	    color: #1e90f1;
	    font-size: 15px;
	    padding: 0 0 5px 15px;
	    font-weight: bold;
	    background: url(/static/main/img/sub/tit2_bul.jpg) no-repeat 0 2px;
	}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript">
	
	jQuery(document).ready(function(){
		var title = '위생교육 만족도 설문조사';
		document.title = title;
		jQuery('#_head_text_title').html(title);
		
		js_requestData('');
		//설문참여 등록
		jQuery('#btnSave').bind('click', function(){
			js_requestSave();
			return false;
		});
		
	});
	
	
	/*설문지 불러오기 */
	var resPaperObj; // 설문지 obj.
	function js_requestData(isReadonly){
		
		var _url = '/edu/survey/action/survey.jspx?cmd=getSurveyQstnInfo';
		var http = jQuery.ajax({
	   		url: _url,	   		
	   		type: "POST",
	   		data : { paper_id : jQuery('#paper_id').val() },
	   		dataType: "json",
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(json)
			{
				var code = json.result.code;
				var msg = json.result.msg;
				var item = json.result.data;
	
				var htmlAppend = "";
				if(code == '200')
				{
				    // console.log(item)
					if(item.detail_qstn_list.length > 0 ){
						
						resPaperObj = json;
						var qstnIdx = 0;
						var answIdx = 0;
						var qstn_cnt = parseInt(item.detail_qstn_list[0].qstn_cnt);
		
						var arrQstn = new Array(qstn_cnt); // 문항별 질문. 인덱스-1 이 문제번호.
						var arrAnswType = new Array(qstn_cnt); // 문항별 유형. 인덱스-1 이 문제번호.
						var arrAnswCnt = new Array(qstn_cnt); // 문항별 답변갯수. 인덱스-1 이 문제번호.
						var arrQstnNo = new Array(qstn_cnt);
						var arrMultiYn = new Array(qstn_cnt);
						var arrMultiCnt = new Array(qstn_cnt);
						var arrConnectYn = new Array(qstn_cnt);
						var arrQstnOrd = new Array(qstn_cnt);
						jQuery('#paper_id').val(item.detail_qstn_list[0].paper_id);
						jQuery('#s_cnt').val(item.detail_qstn_list[0].s_cnt);	//객관식 문제수
						jQuery('#d_cnt').val(item.detail_qstn_list[0].d_cnt);	//주관식 문제수
						jQuery('#qstn_cnt').val(qstn_cnt);	//문항 총 개수

						for(var i=0; i<item.detail_qstn_list.length; i++){
							if(i == 0){
								arrQstn[qstnIdx] = item.detail_qstn_list[i].qstn_answ; 
								arrAnswType[qstnIdx] = item.detail_qstn_list[i].answ_type;
								arrAnswCnt[qstnIdx] = item.detail_qstn_list[i].answ_cnt;
								arrMultiYn[qstnIdx] = item.detail_qstn_list[i].multi_yn;
								arrMultiCnt[qstnIdx] = item.detail_qstn_list[i].multi_cnt;
								arrQstnNo[qstnIdx] = item.detail_qstn_list[i].qstn_no;
								arrConnectYn[qstnIdx] = item.detail_qstn_list[i].connect_yn;
                                arrQstnOrd[qstnIdx] = item.detail_qstn_list[i].qstn_ord;
								qstnIdx++;
							}else{
								if(item.detail_qstn_list[i-1].qstn_no != item.detail_qstn_list[i].qstn_no){
					
									arrQstn[qstnIdx] = item.detail_qstn_list[i].qstn_answ;
									arrAnswType[qstnIdx] = item.detail_qstn_list[i].answ_type;
									arrAnswCnt[qstnIdx] = item.detail_qstn_list[i].answ_cnt;
									arrMultiYn[qstnIdx] = item.detail_qstn_list[i].multi_yn;
									arrMultiCnt[qstnIdx] = item.detail_qstn_list[i].multi_cnt;
									arrQstnNo[qstnIdx] = item.detail_qstn_list[i].qstn_no;		
									arrConnectYn[qstnIdx] = item.detail_qstn_list[i].connect_yn;
                                    arrQstnOrd[qstnIdx] = item.detail_qstn_list[i].qstn_ord;
									qstnIdx++;
								}
							}
							
							if(qstnIdx == qstn_cnt) break;
						}
						qstnIdx = 0;

						var qstn = "";
						var ans = "";
						var rsrch_dt = "";
						var exmpIdx = 0;
					
						for (var i = 0; i < arrQstn.length; i++) {
							// if(i != 0)
							// 	rsrch_dt += '<hr style="border-top-color: rgb(0, 0, 0); border-top-width: 1px; border-top-style: solid;">';
								
							qstn = '';
							//qstn += '<fieldset class="textbox_space" style="padding-right: 0px; vertical-align: top;">';
							//qstn += '<label class="W120_R"><b>설문 '+(i+1)+'</b></label>';// + "<br>";
							//qstn += '</fieldset>';
							
							arrQstn[i] = arrQstn[i].replace(/\n/gi,"<br>");
							if (arrAnswType[i] == 'D') { // 주관식			
								ans += '<fieldset style="width:80%; padding: 10px;" data-qstnord="'+arrQstnOrd[i]+'" class="js-qstn-wrap" data-qstnprev="0">';
								ans += '<span class="col-sm-12">';
								ans += '<div class="qstn"><label><b>설문 '+(i+1)+'.</b>&nbsp;</label>';
								ans += '<span class="qstn-text">'+arrQstn[i]+'</span></div><br>';
								ans += '<textarea class="textArea_height100" style="width:100% !important;" name="ANSW_DT_' + arrQstnNo[i] + '" id="ANSW_DT_' + arrQstnNo[i] + '" '+( !!isReadonly ? 'disabled' : '')+'></textarea>';
								ans += '</fieldset>';
								exmpIdx++;
							}else { // 객관식
								ans += '<fieldset style="width:80%; padding: 10px;" data-qstnord="'+arrQstnOrd[i]+'" class="js-qstn-wrap" data-qstnprev="0">';
								ans += '<span class="col-sm-12">';
								ans += '<div class="qstn"><label><b>설문 '+(i+1)+'.</b>&nbsp;</label>';
								ans += '<span class="qstn-text">'+arrQstn[i]+'</span></div><br>';
								var answ_cnt = arrAnswCnt[i];
								
								for ( var k = 1; k <= answ_cnt; k++) {
									if(k != 1){
										ans += '<br>';
									}
									var connect_num = (item.detail_qstn_list[exmpIdx].connect_qstn_no).replace(item.detail_qstn_list[i].paper_id+'_q', "");
									
									if(arrMultiYn[i] == "Y")
									{
										if(arrConnectYn[i] == 'Y'){
                                            if(k==1) ans += '      <p style="color:red;">(최대  ' +arrMultiCnt[i]+ '개 선택 가능)</p>';
										    ans +=  '<input type="checkbox" name="ANSW_DT_' + arrQstnNo[i] + '" id="ANSW_DT_' + item.detail_qstn_list[exmpIdx].exmp_seq_no + '" value="' + item.detail_qstn_list[exmpIdx].exmp_seq_no+ '" onClick="multiCntCheck(this)" '+( !!isReadonly ? 'disabled' : '')+'/>&nbsp;<label class="exmp-text" for="ANSW_DT_' + item.detail_qstn_list[exmpIdx].exmp_seq_no + '">' + item.detail_qstn_list[exmpIdx].exmp_word+ '</label>';
											ans +=  '<input type="hidden" name="CONNECT_NUM_' + item.detail_qstn_list[exmpIdx].connect_qstn_no + '" id="CONNECT_NUM_' + item.detail_qstn_list[exmpIdx].connect_qstn_no + '" value="' + item.detail_qstn_list[exmpIdx].connect_qstn_no+ '" />';
											ans +=  '<input type="hidden" name="MULTI_' + arrQstnNo[i] + '" id="MULTI_' + arrQstnNo[i] + '" value="' + item.detail_qstn_list[exmpIdx].multi_cnt+ '" />';
											if((i+2) != connect_num){
												ans += 	'<span style="color:; display:none;"> (' +connect_num+ '번으로 이동)</span>';		
											}
										
											// if(k==1) qstn += '      <span style="color:red;">(' +arrMultiCnt[i]+ '개 선택 )</span>';
										}else{
                                            if(k==1) ans += '      <p style="color:red;">(' +arrMultiCnt[i]+ '개 선택)</p>';
                                            ans +=  '<input type="checkbox" name="ANSW_DT_' + arrQstnNo[i] + '" id="ANSW_DT_' + item.detail_qstn_list[exmpIdx].exmp_seq_no + '" value="' + item.detail_qstn_list[exmpIdx].exmp_seq_no+ '" onClick="multiCntCheck(this)" '+( !!isReadonly ? 'disabled' : '')+'/>&nbsp;<label class="exmp-text" for="ANSW_DT_' + item.detail_qstn_list[exmpIdx].exmp_seq_no + '">' + item.detail_qstn_list[exmpIdx].exmp_word+ '</label>';
                                            ans +=  '<input type="hidden" name="MULTI_' + arrQstnNo[i] + '" id="MULTI_' + arrQstnNo[i] + '" value="' + item.detail_qstn_list[exmpIdx].multi_cnt+ '" />';
										}
									}
									else
									{			
										if(arrConnectYn[i] == 'Y'){
											ans += '<input type="radio" class="border_none" name="ANSW_DT_' + arrQstnNo[i] + '" id="ANSW_DT_' + item.detail_qstn_list[exmpIdx].exmp_seq_no+ '" value="' + item.detail_qstn_list[exmpIdx].exmp_seq_no + '" onClick="ConnectNumCheck(this)"'+( !!isReadonly ? 'disabled' : '')+'/>&nbsp;<label class="exmp-text" for="ANSW_DT_' + item.detail_qstn_list[exmpIdx].exmp_seq_no + '">' + item.detail_qstn_list[exmpIdx].exmp_word+ '</label>';
											ans += '<input type="hidden" name="CONNECT_NUM_' + item.detail_qstn_list[exmpIdx].connect_qstn_no + '" id="CONNECT_NUM_' + item.detail_qstn_list[exmpIdx].connect_qstn_no + '" value="' + item.detail_qstn_list[exmpIdx].connect_qstn_no+ '" />';
											
											if((i+2) != connect_num){
												ans += 	'<span style="color:; display:none;""> (' +connect_num+ '번으로 이동)</span>';		
											}
										}else{
											ans += '<input type="radio" class="border_none" name="ANSW_DT_' + arrQstnNo[i] + '" id="ANSW_DT_' + item.detail_qstn_list[exmpIdx].exmp_seq_no + '" value="' + item.detail_qstn_list[exmpIdx].exmp_seq_no + '" '+( !!isReadonly ? 'disabled' : '')+'/>&nbsp;<label class="exmp-text" for="ANSW_DT_' + item.detail_qstn_list[exmpIdx].exmp_seq_no + '">' + item.detail_qstn_list[exmpIdx].exmp_word + '</label>';	
										}
									}
									exmpIdx++;
								}
								
								ans += '</span>';
								ans += '</fieldset>';
							}
							
							rsrch_dt += qstn + ans;
							ans = "";
						}
						jQuery("#SURVEY_PAPER_DIV").html(rsrch_dt);

                        js_stepFieldset();
					}else{
						
					}
				} else
				{
					msgStart(msg_com_code_010+"("+msg+")", 'danger');
				}
			}			
		});
	}
	/**
	 * 문항의 다음 스텝으로 이동하기 위한 함수
	 *
	 * @param qstnStep : 현재 자기자신의 위치 0부터 시작
	 * @param qstnConnStep : 연결된 문항의 index : ConnectNumCheck()에서 값이 있는 경우 연결된 문항 index의 -1한 값이 부여된다.
	 * */
	 var qstnStep = 0, qstnConnStep = -1;
	 function js_stepFieldset(){
	        var qstns = $('.js-qstn-wrap');
	        qstns.hide();
	        var btnSave = $('#btnSave'), btnNext = $('#btnNext'), btnPrev = $('#btnPrev');
	        btnSave.hide();
	        btnNext.hide();
	        btnPrev.hide();

	        // 총 문항 갯수
	        var qstnCnt = Number($('#qstn_cnt').val());
	        // 현재 문항 보이기
		    var $thisQstn = qstns.eq(qstnStep);
	        $thisQstn.show();

	        if(qstnStep == qstnCnt - 1) {
	            btnSave.show();
	        } else {
	            btnNext.show();
	        }
	        // 1번째 문항에는 가리고 이후부터 보이게
	        if(qstnStep > 0) {
	            btnPrev.show();
	        }
	        // 연결된 문항값 초기화
		    qstnConnStep = -1;
	    }
	    /**
	     * 문항의 보기를 선택했는지 여부 체크
	     *
	     * @param field     현재 화면에 보여지고 있는 문항 자체 jQuery Selector
	     * */
	    function chk_stepFieldsetInput(field){
	        var result = true;

	        var s_answ_cnt = 0;

	        if(field.find( "textarea:not(:disabled)" ).length > 0){
	            field.find( "textarea:not(:disabled)" ).each(function( index ) {
	                if(this.value.trim() == ''){
	                    result = false;
	                }
	            });
	        } else {
	            if(field.find(':input:radio').length > 0) { // radio
	                s_answ_cnt = field.find(':input:radio:not(:disabled):checked').length;
	            } else if(field.find(':input:checkbox').length > 0) { // checkbox
	                s_answ_cnt = field.find(':input:checkbox:not(:disabled):checked').length;
	            }

	            if(s_answ_cnt <= 0){
	                result = false;
	            }
	        }
	        return result;
	    }

	    jQuery(document).ready(function(){
			// 다음버튼
	        $('#btnNext').on('click', function(){
				var $qstnWrap = $('.js-qstn-wrap');
	            var $thisQstn = $qstnWrap.eq(qstnStep);
	            if(!chk_stepFieldsetInput($thisQstn)){

	                msgStart('보기를 선택하여 주십시오.', 'danger');
	                return;
	            }
	            // 연결문항
		        if($thisQstn.find('[name^="CONNECT_"]').length > 0) {
		            var $sel = $thisQstn.find('input:checked');
		            $.each($sel, function(idx, conn){
	                    ConnectNumCheck(conn);
		            });
		        }


	            if($thisQstn.find('textarea').length > 0 || qstnConnStep <= 0) { // 주관식이거나 연결문항이 없을 경우
	                $($qstnWrap.eq(qstnStep + 1)).data('qstnprev', qstnStep); // 다음문항 fieldset에 지금문항 번호 저장
	                qstnStep = qstnStep + 1;
	            } else {
	                $($qstnWrap.eq(qstnConnStep - 1)).data('qstnprev', qstnStep);
	                qstnStep = qstnConnStep - 1;
	            }

	            js_stepFieldset();
	        });
			// 이전버튼
	        $('#btnPrev').on('click', function(){
				// 현재 문항의 선택 초기화
	            var $thisQstn = $($('.js-qstn-wrap').eq(qstnStep));
	            $thisQstn.find('input').prop('checked', false);
	            $thisQstn.find('textarea').val("");

	            // 현재문항idx, 이전문항idx
	            enableConnectNumCheck(qstnStep, $thisQstn.data('qstnprev'));

	            // 이전문항으로 이동 - 바로 이전과 연결문항으로 이동했을 경우
	            qstnStep = Number($thisQstn.data('qstnprev'));

	            $thisQstn.data('qstnprev', 0);
	            js_stepFieldset();
	        });
			
	        $('#btnStart').on('click', function(){
	        	
	        	jQuery('#survey_purpose').hide();
	        	jQuery('#survey_first').hide();
	        	jQuery('#survey_contents').show();
	        	
	        });
	        
	    });
		
		function checkForm(){
			var result = true;
			var s_cnt = parseInt(jQuery('#s_cnt').val());	//객관식 총 문제수
			var d_cnt = parseInt(jQuery('#d_cnt').val());	//주관식 총 문제수
			
			//객관식 답변수(건너뛰는 설문 제외)
			var s_answ_cnt = parseInt(jQuery(':input:radio:not(:disabled):checked, :input:checkbox:not(:disabled):checked').length)
			var s_disabled_cnt = parseInt(jQuery(':input:radio:disabled, :input:checkbox:disabled').parent().length);
			
			if(s_answ_cnt < s_cnt-s_disabled_cnt){
				result = false;
			}
			
			jQuery( "textarea:not(:disabled)" ).each(function( index ) {
				if(this.value.trim() == ''){
					result = false;
				}
			});
			
			return result;
		}
	
	//설문참여 등록
	function js_requestSave()
	{
		var sendData = jQuery("#form").serialize();
		var _url = '/edu/survey/action/survey.jspx?cmd=makeSurveyAnsw';
			
		if(!checkForm()){
			alert('답변하지 않은 설문문항이 있습니다.');
			return;
		}

		var http = jQuery.ajax({
	   		url: _url,	   		
	   		type: "POST",
	   		data : sendData,
	   		//dataType: "json",
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(Json)
			{
				var code = Json.result.code 		
		   		var msg =  Json.result.msg;
	
				if(code == '200'){	
					jQuery('#proc_yn').val( 'Y' );
					alert('설문에 참여해주셔서 감사합니다.');
					jQuery('#btnSave').addClass('hide');
					jQuery('#btnPrev').addClass('hide');
					opener.fn_certOpenDown();
					//window.close();
				
				}else{
					alert('죄송합니다. 처리중 문제가 발생하였습니다.');
					return;
				}
			}			
		});	
	}

	/**
	 * 복수답변이 허용된 문항보기에 대해서 최대 몇 개의 답변을 허용하는지 체크한다.
	 * @param {String} 문항보기 아이디
	 */
	 function multiCntCheck(obj){
			//ConnectNumCheck(obj);
			
			var name = obj.name;
			var chk_name = name.replace("ANSW_DT", "MULTI");
			var chk_cnt = jQuery("#"+chk_name).val();
			
			var check_qstn = jQuery("input[name='"+name+"']:checked");
			if(check_qstn.size() > chk_cnt) 
			{
				var qstn_title = jQuery("#"+name).parent().find("label>b").text();
				msgStart(qstn_title+" 이(가) 복수답안 허용개수를 초과하였습니다.","danger", null, 400, 150);
				
				check_qstn.prop("checked", false);
			}	
			
			return false;
		}
		 

		 /**
		  * 연결문항이 있는지 체크한다.
		  * @param {Node} 선택된 input
		  */
		 function ConnectNumCheck(obj) {
	         var chk_name = jQuery(obj).next().next().attr('name');
	         if(chk_name.indexOf('CONNECT') <= -1) {
	             return;
	         }
	         var connect_val = jQuery("#" + chk_name).val();

	         var paper_id = jQuery('#paper_id').val();

	         var connect_idx = replaceAll(connect_val, paper_id + '_q', "");
	         var now_ord = jQuery(obj).closest('fieldset').data('qstnord');

	         var qstnFieldsets = jQuery('fieldset.js-qstn-wrap');
	         // 연결된 문항이 비활성화 되어 있으면 활성화 시킴
	         jQuery.each(qstnFieldsets, function (idx, node) {
	             var fieldsetIdx = jQuery(node).data('qstnord');
	             if(fieldsetIdx == connect_idx) {

	                 jQuery(node).find('textarea, input').prop('disabled', false);
	                 jQuery(node).find('input').removeAttr('checked');
	                 return true;
	             }
	         });
			// 연결된 문항의 이전 문항은 비활성화(본인 문항 제외)
	         for (var i = parseInt(now_ord) + 1; i < connect_idx; i++){
	             jQuery.each(qstnFieldsets, function (idx, node) {
	                 var fieldsetIdx = jQuery(node).data('qstnord');

					if(fieldsetIdx == i) {
	                    jQuery(node).find('textarea, input').prop('disabled', true);
	                    return true;
					}
	             });
		     }

	         qstnConnStep = Number(connect_idx);

			 return false;
		 }

	    /**
	     * 이전으로 이동시 연결문항으로 인해 disable된 중간 문항이 있으면 enable 처리 한다.
	     * @param nowStep {Number} 현재 화면에 출력되고 있는 문항의 index (0부터 시작)
	     * @param prevStep {Number} 이전으로 돌아갈 문항의 index (0부터 시작)
	     */
	    function enableConnectNumCheck(nowStep, prevStep) {

	        var qstnFieldsets = $('.js-qstn-wrap');
	        nowStep = nowStep + 1;
	        prevStep = prevStep + 1;

	        for (var i = prevStep + 1; i < nowStep; i++){
	            jQuery.each(qstnFieldsets, function (idx, node) {
	                var fieldsetIdx = jQuery(node).data('qstnord');

	                if(fieldsetIdx == i) {
	                    jQuery(node).find('textarea, input').prop('disabled', false);
	                    jQuery(node).find('input').removeAttr('checked');
	                    return true;
	                }
	            });
	        }
	    }
</script>
</head>

<body>
<form name="form" id="form"  onSubmit="javascript:return false;">
<input type="hidden" name="paper_id" id="paper_id" value="<%=paper_id%>" />
<input type="hidden" name="user_lecture_seq_no" id="user_lecture_seq_no" value="<%=user_lecture_seq_no%>" />
<input type="hidden" name="s_cnt" id="s_cnt" value="" />
<input type="hidden" name="d_cnt" id="d_cnt" value="" />
<input type="hidden" id="qstn_cnt" name="qstn_cnt" value=""/><!-- 총 문항 갯수 -->
<div class="popup_content">
	<div class="class_popup_tit ">
	    <strong>위생교육 만족도 설문조사</strong>
	</div>
	<div class="section-1" id="survey_purpose">
		<div class="tit2">
			본 교육과정을 통해 느끼셨던 의견을 수렴하고자 합니다.
		</div>
		<div class="tit2">
			귀하의 소중한 의견은 보다 나은 위생교육 발전을 위해 소중히 쓰도록 하겠습니다. 감사합니다.
		</div>
		
		
	</div>
	<div class="x_panel" id="survey_contents" style="display:none;">     
		<div class="x_content">
			<div class="section-1">
				<div id="type_board_list"></div>					
				<div class="textbox_space widget-body_A btn-curved"  style="border-color:#ADCFD9;" id="SURVEY_PAPER_DIV"></div>		
			</div>
			<div class="center">
				<button id="btnPrev" class="btn btn-sm btn-info">
					<i class="icon- fa fa-list"> 이전</i>
				</button>
				<button id="btnNext" class="btn btn-sm btn-primary">
					<i class="icon- fa fa-list"> 다음</i>
				</button>
				<button id="btnSave" class="btn btn-sm btn-success">
					<i class="icon- fa fa-list"> 등록</i>
				</button>
				
			</div>
		</div>
	</div>
	<div class="x_panel" id="survey_first" style="display:;">     
		<div class="x_content">
			<div class="center">
				<a id="btnStart" class="btn btn-primary btn-curved" style="margin:50px;font-size:xx-large">
					<i class="icon-arrow-right"> 설문시작하기</i>
				</a>
			</div>
		</div>
	</div>	
</div>	

</form>
</body>
</html>
