<%@page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<style>

	</style>
	<script type="text/javascript" type="text/javascript">
	
	var checkUnload = true;
    $(window).on("beforeunload", function(){
        if(checkUnload) return "이 페이지를 벗어나면 작성된 내용은 저장되지 않습니다.";
    });
    
        // 문항 복사시 select의 selected option 여부까지 복사되도록 하기 위한 플러그인
        (function (original) {
            jQuery.fn.clone = function () {
                var result           = original.apply(this, arguments),
                    my_textareas     = this.find('textarea').add(this.filter('textarea')),
                    result_textareas = result.find('textarea').add(result.filter('textarea')),
                    my_selects       = this.find('select').add(this.filter('select')),
                    result_selects   = result.find('select').add(result.filter('select'));

                for (var i = 0, l = my_textareas.length; i < l; ++i) $(result_textareas[i]).val($(my_textareas[i]).val());
                for (var i = 0, l = my_selects.length;   i < l; ++i) result_selects[i].selectedIndex = my_selects[i].selectedIndex;

                return result;
            };
        }) (jQuery.fn.clone);
        var question_div_prefix = "question_";

        var _htmlTemplate = null;
        var _answTemplate = null;

        var sampleAnswer = ["매우만족", "만족", "보통", "불만족", "매우불만족"];

        jQuery(document).ready(function(){
            _htmlTemplate = "<label class='font_size20 font_bold'>#_qstn_no</label>" +
	            "<div style='float: right; padding-top: 0px;'>" +
	            "<button type='button' name='i_qstn_del' class='btn btn-xs btn-danger js-qstn-del-btn'><i class='icon- fa fa-minus'></i> 삭제</button>" +
	            "<button type='button' name='i_qstn_add' class='btn btn-xs btn-primary js-qstn-add-btn'><i class='icon- fa fa-plus'></i> 다음 문항으로 복사</button>" +
	            "<button type='button' name='i_qstn_add_last' class='btn btn-xs btn-info js-qstn-add-last-btn'><i class='icon- fa fa-copy'></i> 마지막 문항으로 복사</button>" +
	            "</div>" +
	            "<input type='hidden' id='i_qstn_seq' name='i_qstn_seq' value='#_qstn_seq' />" + // survey.jspx에서 사용, 현재 화면에 출력된 문제의 key값을 배열로 전달(첫 설정이후 변경안됨)
	            "<input type='hidden' id='i_qstn_id' name='i_qstn_id' value='#_qstn_id' />" + // 자기자신문항의 id(첫 설정이후 변경안됨) : q1
	            "<input type='hidden' id='i_qstn_ord' name='i_qstn_ord' value='1' />" + // 화면에 뿌려질 때 진짜 순서(id는 뒤죽박죽될수 있지만 ord는 무조건 순차적으로 저장되어있다.)
	            "<input type='hidden' id='i_exmp_add_seq' name='i_exmp_add_seq' value='0' />" + // 자기문항의 보기가 추가된 횟수(추가할때마다 +1씩 증가한다, -1은 하지 않는다.)
	            "<table class='com_table_box' border='0' cellpadding='0' cellspacing='0'>" +
	            "   <colgroup><col width='90px'><col width='500px'><col width='90px'><col width='110px'><col width='90px'><col width=''></colgroup>" +
	            "       <tbody>" +
	            "           <tr>" +
	            "               <th>문항형식</th>" +
	            "               <td>" +
	            "                   <select class='form-control W50P' id='i_answ_type' name='i_answ_type' alt='문항형식' required='true'>" +
                "						<option value=''>::선택::</option>" +
                "						<option value='S'>객관식</option>" +
                "						<option value='D'>주관식</option>" +
                "					</select>" +
	            "				</td>" +
                "				<th>보기 개수</th>" +
                "				<td colspan=3>" +
                "					<span id='p_answ_cnt' style='display:none;'>" +
                "				        <a class='btn btn-success btn-xs js-answ-plus' style='margin: 0; border: 0;'><i class='fa fa-plus'></i> 보기 개수 추가하기</a>" +
                "				        <input type='hidden' id='i_answ_cnt' name='i_answ_cnt' value='2' />" + // 보기개수가 화면에 뿌려진 개수 출력
                "					</span>" +
                "				</td>" +
                "			</tr>" +
                "		<tr id='p_answ_type' style='display:none;'>" +
                "			<th> 복수답안</th>" +
                "			<td>" +
                "				<select class='form-control W50P' id='i_answ_multi_type' name='i_answ_multi_type' alt='복수답안'>" +
                "					<option value='Y'>허용</option>" +
                "					<option value='N' selected>미허용</option>" +
                "				</select>" +
                "			</td>" +
                "			<th>복수답안개수</th>" +
                "			<td>" +
                "				<span id='p_answ_multi_type_cnt' style='display:none;'>" +
                "				    <input class='W50P' id='i_answ_multi_cnt' name='i_answ_multi_cnt' type='text' style='width:30px !important;height:26px !important; text-align: center' data-min='2' data-max='7' value='2'/>" +
                "				</span>" +
                "			</td>" +
                "			<th>연결여부</th>" +
                "			<td>" +
                "				<select class='form-control W50P' id='i_connect_yn' name='i_connect_yn' alt='복수답안'>" +
                "					<option value='Y'>허용</option>" +
                "					<option value='N' selected>미허용</option>" +
                "				</select>" +
                "			</td>" +
                "		</tr>" +
                "			<tr>" +
                "				<th>지문</th>" +
                "				<td colspan=5>" +
                "					<textarea class='W100P textArea_height100' name='i_qstn_answ' id='i_qstn_answ' style='width:100% !important;' required='true' alt='지문'></textarea>" +
                "				</td>" +
                "			</tr>" +
                "#exmp_word" +
                "		</tbody>" +
                "</table>" +
                "<hr/>";

            _answTemplate = "<tr id='p_exmp_word_' style='display:none;'>" +
	            "   <th>#exmp_word <a class='js-answ-del' style='margin: 0; border: 0; cursor: pointer; color: #d15b47; padding: 3px;'><i class='icon fa fa-times'></i></a></th>" +
	            "   <td colspan=3>" +
	            "      <span class='input-group ubicus_textbox_width_long' style=' width:100% !important;'>" +
	            "          <input class='W100P' id='i_exmp_word_' name='i_exmp_word_' type='text' required='true' value='#value' alt='보기 입력' placeholder='보기 입력'/>" +
	            "          <input type='hidden' id='i_exmp_seq' name='i_exmp_seq' value='#_exmp_seq' />" + // survey.jspx에서 사용, 현재 화면에 출력된 보기의 key값을 배열로 전달(첫 설정이후 변경안됨)
	            "          <input type='hidden' id='i_exmp_id' name='i_exmp_id' value='#_exmp_id' />" + // 자기보기의 id(첫 설정이후 변경안됨) : 'a1'
	            "      </span>" +
	            "   </td>" +
	            "   <th id='th_connect_num' style='display:;'>연결문항번호</th>" +
	            "   <td id='td_connect_num' style='display:;'>" +
	            "      <span>" +
	            "          <input id='i_connect_num' name='i_connect_num' type='text' style='width:71px !important;height:26px !important; text-align: center; display:none;' data-min='2' data-max='99'/>" +
	            "      </span>" +
	            "   </td>" +
	            "</tr>";


            jQuery("#qstn_cnt").spinner({
                create: function( event, ui ) {
                    //add custom classes and icons
                    jQuery(this)
                        .next().addClass("btn btn-success").html('<i class="icon fa fa-plus"></i>')
                        .next().addClass("btn btn-danger").html('<i class="icon fa fa-minus"></i>');

                    //larger buttons on touch devices
                    //if(ace.click_event == "tap") jQuery(this).closest('.ui-spinner').addClass('ui-spinner-touch');
                },
                max : 100,
                min : 1,
                spin : function(event, ui) {
                    var fieldsetCount = jQuery("[id^=question_]").length;
                    var qstnSeq = Number(jQuery("#qstn_add_seq").val());
                    if(fieldsetCount < ui.value) { // 문항 추가
                        // 문항 추가 할때만 증가시켜줌
                        jQuery("#qstn_add_seq").val(qstnSeq + 1);
                        js_changQstnCnt(qstnSeq + 1, "change");
                    } else { // 문항 삭제
                        jQuery("[id^='"+question_div_prefix+"']").last().remove();
                    }
                    js_fieldsetIdChange();
                }
            });

            js_changQstnCnt(1, "change");
            jQuery('#btnSave').on('click', function(){
                js_requestSave();
            });

            jQuery('#btnList').on('click', function(){
                location.href='/survey/survey.jspx?cmd=list&cur_pg=<%=input.getText("cur_pg")%>&row_per_page=<%=input.getText("row_per_page")%>&srch_txt=<%=input.getText("srch_txt")%>';
            });

            <%	if (!"".equals(input.getText("paper_id"))) { %>
            js_requestData('<%=input.getText("paper_id")%>');
            jQuery('#paper_id').val('<%=input.getText("paper_id")%>');

            jQuery('#btnDel').bind('click', function(){js_requestDrop();});

            jQuery('#btnCopy').bind('click', function(){js_requestCopy();});

            <%	} else { %>
            jQuery( "#qstn_cnt" ).spinner("value", 1);
            <%	} %>

            jQuery('body').on('click', '.js-qstn-del-btn', function(e) {
                // 자기 문항 전체 삭제
                $(this).closest('fieldset').remove();// 자기자신 문항 Fieldset 삭제
                js_fieldsetIdChange();
            }).on('click', '.js-qstn-add-btn, .js-qstn-add-last-btn', function(e) {
                // 자기 문항 복사하여 밑에 추가
                // 문항 Count +1 증가
                jQuery('#qstn_add_seq').val(Number(jQuery('#qstn_add_seq').val()) + 1);
                var qstnIdx = Number(jQuery('#qstn_add_seq').val());
                var $parent = $(this).closest('fieldset');
                var $thisQuestion = $parent.clone(true);

                if($(this).hasClass('js-qstn-add-btn')) { // 다음 문항으로 복사
                    $parent.after($thisQuestion);
                } else { // 마지막 문항으로 복사사
                   jQuery("fieldset").last().after($thisQuestion);
                }

                // 자기자신 fieldset id 값 변경
                $thisQuestion.attr('id', $thisQuestion.attr('id').split('_')[0] + '_' + qstnIdx);
                jQuery('[id^="i_qstn_id_"]', $thisQuestion).val('q' + qstnIdx);
                jQuery('[id^="i_qstn_seq"]', $thisQuestion).val(qstnIdx);
                // 자기자신 내부의 input 요소들 id, name 값 변경
                var idArray = ['i_qstn_id_', 'i_exmp_add_seq_', 'i_answ_type_', 'p_answ_cnt_', 'p_answ_type_', 'i_answ_multi_type_', 'i_answ_cnt_',
                    'p_answ_multi_type_cnt_', 'i_answ_multi_cnt_', 'i_connect_yn_', 'i_qstn_answ_', 'p_exmp_word_', 'i_exmp_word_', 'i_exmp_id_',
                    'th_connect_num_', 'td_connect_num_', 'i_connect_num_', 'i_qstn_ord_'
                ];
                js_fieldsetInputNumber($thisQuestion, idArray);
                // 보기 개수별로 들어가는 input이지만 survey.jspx에서 배열로 쓰기 위해 동일한 ID/NAME을 가지기 때문에 따로 변경시켜줌
                jQuery('[id^="i_exmp_seq_"]', $thisQuestion).attr('id', 'i_exmp_seq_' + qstnIdx).attr('name', 'i_exmp_seq_' + qstnIdx);

                jQuery('[id^="i_answ_type_"]', $thisQuestion).bind('change', function(){js_changeAnswerType(qstnIdx, "type");});
                jQuery('[id^="i_answ_multi_type_"]', $thisQuestion).bind('change', function(){js_changeAnswerMultiType(qstnIdx);});
                jQuery('[id^="i_connect_yn_"]', $thisQuestion).bind('change', function(){js_changeConnectYn(qstnIdx);});

                js_fieldsetIdChange();
            }).on('click', '.js-answ-plus', function(e){
                // 보기개수 추가 버튼 클릭 이벤트
                var $parent = $(this).closest('fieldset');
				var exmpCount = Number(jQuery('[id^="i_exmp_add_seq_"]', $parent).val()) + 1;
                jQuery('[id^="i_exmp_add_seq_"]', $parent).val(exmpCount);
                jQuery('[id^="i_answ_cnt_"]', $parent).val(jQuery('[id^="p_exmp_word_"]', $parent).length + 1);

                var qstnSeq = jQuery('[id^="i_qstn_seq"]', $parent).val();
                js_changAnswCnt(qstnSeq, exmpCount);

                var $trs = jQuery('[id^="p_exmp_word_"]', $parent);
                $.each($trs, function(idx, node) {
                    $(node).find('th').first().html('보기 ' + (idx + 1) + " <a class='js-answ-del' style='margin: 0; border: 0; cursor: pointer; color: #d15b47; padding: 3px;'><i class='icon fa fa-times'></i></a></a>");
                });
            }).on('click', '.js-answ-del', function(e) {
                // 보기 삭제 버튼 클릭 이벤트
                var $parent = $(this).closest('fieldset');
                var $trs = jQuery('[id^="p_exmp_word_"]', $parent);
                if($trs.length <= 2) {
                    msgStart('보기 개수는 최소 2개이상이어야 합니다.', 'danger');
                    return;
                }
                $(this).closest('tr').remove();

                jQuery('[id^="i_answ_cnt_"]', $parent).val($trs.length - 1);

                $trs = jQuery('[id^="p_exmp_word_"]', $parent);
                $.each($trs, function(idx, node) {
                    $(node).find('th').first().html('보기 ' + (idx + 1) + " <a class='js-answ-del' style='margin: 0; border: 0; cursor: pointer; color: #d15b47; padding: 3px;'><i class='icon fa fa-times'></i></a></a>");
                });
            }).on('keypress keyup', '[id^="i_answ_multi_cnt"], [id^="i_connect_num"]', function(e) {
                // 복수답안개수, 연결문항번호 input의 키 입력값 유효성 검사
                var $this = $(this);
                // 숫자가 아닌 값일 경우 입력 제한
                if(e.which != 8 && isNaN(String.fromCharCode(e.which))){
                    e.preventDefault();
                }
                var msg = '';
                if($this.attr('id').indexOf('multi') > -1) {
                    msg = '복수답안 개수는 최소 ' +$this.data('min')+ '개, 최대 ' +$this.data('max')+ '개까지 가능합니다.';
                } else {
                    msg = '연결문항번호는 ' +$this.data('min')+ '부터, 최대 ' +$this.data('max')+ '까지 가능합니다.';
                }
                
                // 누른 값이 최소/최대값을 벗어난 경우 리턴
                if(e.type === 'keypress' && e.key.toLowerCase() != 'backspace' && Number($this.val() + String.fromCharCode(e.which)) != 1 &&
                    (Number($this.val() + String.fromCharCode(e.which)) < $this.data('min') || Number($this.val() + String.fromCharCode(e.which)) > $this.data('max'))) {
                    msgStart(msg, 'danger');
                    e.preventDefault();
                }
                // 입력된 값이 최소/최대값을 벗어난 경우 value값 없애고 리턴
                if(e.type === 'keyup' && e.key.toLowerCase() != 'backspace' && Number($this.val()) != 1 &&
                    (Number($this.val()) < $this.data('min') || Number($this.val()) > $this.data('max'))) {
                    msgStart(msg, 'danger');
                    $this.val('');
                    e.preventDefault();
                }

            }).on('focusout', '[id^="i_answ_multi_cnt"], [id^="i_connect_num"]', function(e) {
                // 복수답안개수 value에 빈값이거나 2보다 작은 숫자일 경우 자동으로 2값 세팅
                var $this = $(this);
                if($this.val() === '' || $this.val() == '1') {
                    $this.val('2');
                }
            });
        });
        /**
         * 복사된 문항 Fieldset 내의 id/name 속성들 idx 값 변경
         * */
        function js_fieldsetInputNumber(parentNode, idArray) {
            var qstn_id = parentNode.find('[id^="i_qstn_id_"]').val();
            var num = qstn_id.substr(1);

            for(var i = 0, length = idArray.length; i < length; i ++) {
                var thisId = idArray[i];

                var node = jQuery('[id^="'+thisId+'"]', parentNode);

                if(node.length <= 1) {
                    // 문항 관련 input
                    var $this = jQuery(node);
                    var $thisIds = $this.attr('id').split('_'), len = $thisIds.length;
                    $thisIds[len - 1] = num;
                    $this.attr('id', $thisIds.join('_'));

                    if($this.attr('name')) {
                        $this.attr('name', $thisIds.join('_'));
                    }

                } else {
                    // 보기 관련 input
                    jQuery.each(node, function(idx, nod) {
                        var $this = jQuery(nod);
                        var $thisIds = $this.attr('id').split('_'), len = $thisIds.length;
                        $thisIds[len - 2] = num;
                        $this.attr('id', $thisIds.join('_'));

                        if($this.attr('name')) {
                            $this.attr('name', $thisIds.join('_'));
                        }
                    });
                }
            }

        }

        /**
         * 문항별 Wrapper의 Label text와 qstn_ord 순서 변경
         * */
        function js_fieldsetIdChange() {
            // Fieldset 전부 문항 텍스트와 qstn_ord 값 순서대로 변경
            var $questions = jQuery('#form').find('fieldset[id^="question_"]');
            jQuery.each($questions, function(idx, node){
                // 문항 넘버링
                $(node).find('>label').text((idx + 1) + '번 문항');
                $(node).find('[id^="i_qstn_ord_"]').val(idx + 1);
            });
            // 문항 개수 input value 변경
            jQuery('#qstn_cnt').val($questions.length);
        }



        function js_addSpinerListener(parentNo, htmlID, initNum, minNum, maxNum) {
            jQuery( "#"+htmlID).spinner({
                create: function( event, ui ) {
                    //add custom classes and icons
                    jQuery(this)
                        .next().addClass('btn btn-success').html('<i class="icon fa fa-plus"></i>')
                        .next().addClass('btn btn-danger').html('<i class="icon fa fa-minus"></i>');

                    //larger buttons on touch devices
                    //if(ace.click_event == "tap") jQuery(this).closest('.ui-spinner').addClass('ui-spinner-touch');
                },
                max : maxNum,
                min : minNum,
                spin : function(event, ui) {
                    if (htmlID.indexOf('i_answ_cnt_') != -1) {
                        if (ui.value > jQuery("[id^='p_exmp_word_" + parentNo + "_']").length) {
                            jQuery("#i_exmp_add_seq_" + parentNo).val(parseInt(jQuery("#i_exmp_add_seq_" + parentNo).val(), 10) + 1);
                        }
                        js_changAnswCnt(parentNo, ui.value);
                    }
                }
            });

            jQuery( "#"+htmlID).spinner("value", initNum);
        }

        /**
         * 문항 내용 생성
         *
         * @param i 문항 번호
         * @param is_change_type 값있음 : 문항개수 선택하여 변경, 값없음 : 페이지 로딩 시
         */
        function js_makeQuestionHtml(num, is_change_type) {
            if (jQuery("#qstn_cnt").val() == "") {
                jQuery("#qstn_cnt").val(1);
                jQuery("#qstn_add_seq").val(1);
            } else if (is_change_type) {
                jQuery("#qstn_cnt").val(parseInt(jQuery("#qstn_cnt").val(), 10) + 1);
                console.log("qstn_cnt : " + jQuery("#qstn_cnt").val() + ", qstn_add_seq : " + jQuery("#qstn_add_seq").val());
            }

            var _html = _htmlTemplate;
            _html = _html.replace(/i_answ_cnt/g, 'i_answ_cnt_'+num);
            _html = _html.replace(/i_answ_multi_cnt/g, 'i_answ_multi_cnt_'+num);

            _html = _html.replace(/i_answ_type/g, 'i_answ_type_'+num);
            _html = _html.replace(/p_answ_type/g, 'p_answ_type_'+num);
            _html = _html.replace(/p_answ_cnt/g, 'p_answ_cnt_'+num);
            _html = _html.replace(/i_answ_multi_type/g, 'i_answ_multi_type_'+num);
            _html = _html.replace(/p_answ_multi_type_cnt/g, 'p_answ_multi_type_cnt_'+num);

            _html = _html.replace(/i_qstn_answ/g, 'i_qstn_answ_'+num);
            _html = _html.replace(/#_qstn_no/g, num+'번 문항');

            _html = _html.replace(/i_qstn_id/g, 'i_qstn_id_'+num);
            _html = _html.replace(/#_qstn_id/g, 'q'+num);
            _html = _html.replace(/#_qstn_seq/g, num);
            _html = _html.replace(/i_qstn_ord/g, 'i_qstn_ord_'+num);
            _html = _html.replace(/i_exmp_add_seq/g, 'i_exmp_add_seq_'+num);

            _html = _html.replace(/i_connect_yn/g, 'i_connect_yn_'+num);

            var sub_html1 = js_makeAnswerHtml(num, 1);
            sub_html1 = sub_html1.replace(/#value/g, sampleAnswer[0]);

            var sub_html2 = js_makeAnswerHtml(num, 2);
            sub_html2 = sub_html2.replace(/#value/g, sampleAnswer[1]);

            _html = _html.replace(/#exmp_word/g, sub_html1+sub_html2);

            return _html;
        }

        /**
         * 문항개수 변경
         *
         * @param i 문항 번호
         * @param mode 값있음 : 문항개수 선택하여 변경, 값없음 : 페이지 로딩 시
         */
        function js_changQstnCnt(num, mode) {
            var is_change_type = (mode == undefined) ? false : true;
            var currentQuestionCnt = 0;
            try {
                currentQuestionCnt = jQuery("[id^='"+question_div_prefix+"']").length;
            } catch(e) {
                currentQuestionCnt = 0;
            }
            var qstnSeq = Number(jQuery('#qstn_add_seq').val());

            if (num == 1 && currentQuestionCnt == 1) return;

            if (currentQuestionCnt == 0) {
                // 제일 처음 문항
                jQuery('#questionheader').after("<fieldset id='"+question_div_prefix+ num+"'>"+js_makeQuestionHtml(num, is_change_type)+"</filedset>");

                jQuery('#i_answ_type_'+num).bind('change', function(){js_changeAnswerType(num, "type");});
                jQuery('#i_answ_multi_type_' +num).bind('change', function(){js_changeAnswerMultiType(num);});
                jQuery('#i_connect_yn_'+num).bind('change', function(){js_changeConnectYn(num);});
            } else if((num < qstnSeq + 1 && is_change_type == false) || (num > currentQuestionCnt && is_change_type == true)) {
                // 두번째 문항부터
                jQuery("[id^='"+question_div_prefix+"']").last().after("<fieldset id='"+question_div_prefix+(num)+"'>"+js_makeQuestionHtml(num, is_change_type)+"</filedset>");

                jQuery('#i_qstn_ord_'+num).val(num);
                jQuery('#i_answ_type_'+num).bind('change', function(){js_changeAnswerType(num, "type");});
                jQuery('#i_answ_multi_type_'+num).bind('change', function(){js_changeAnswerMultiType(num);});
                jQuery('#i_connect_yn_'+num).bind('change', function(){js_changeConnectYn(num);});

            } else {
                // 문항 삭제는 spinner 또는 삭제버튼 자체 이벤트에서 지움
                // jQuery("[id^='"+question_div_prefix+"']").last().remove();
            }

        }

        function js_makeAnswerHtml(parentNo, num) {
            var exmp_no = -1;

            if (jQuery("#i_exmp_add_seq_" + parentNo).val() == undefined) {
                exmp_no = num;
            } else {
                exmp_no = parseInt(jQuery("#i_exmp_add_seq_" + parentNo).val());
            }

            jQuery("#i_exmp_add_seq_" + parentNo).val(exmp_no);

            var _html = _answTemplate;
            _html = _html.replace(/p_exmp_word_/g, 'p_exmp_word_'+parentNo+'_'+num);
            _html = _html.replace(/i_exmp_word_/g, 'i_exmp_word_'+parentNo+'_'+num);
            _html = _html.replace(/#exmp_word/g, '보기  '+num);
            _html = _html.replace(/i_exmp_id/g, 'i_exmp_id_'+parentNo+'_'+num);
            _html = _html.replace(/#_exmp_id/g, 'a'+exmp_no);
            _html = _html.replace(/i_exmp_seq/g, 'i_exmp_seq_'+parentNo);
            _html = _html.replace(/#_exmp_seq/g, exmp_no);

            _html = _html.replace(/th_connect_num/g, 'th_connect_num_'+parentNo+'_'+num);
            _html = _html.replace(/td_connect_num/g, 'td_connect_num_'+parentNo+'_'+num);
            _html = _html.replace(/i_connect_num/g, 'i_connect_num_'+parentNo+'_'+num);

            return _html;
        }

        function js_changAnswCnt(parentNo, num) {

            var currentAnswerCnt = 0;
            try {
                currentAnswerCnt = jQuery("[id^='p_exmp_word_" + parentNo + "_']").length;
            } catch(e) {
                currentAnswerCnt = 0;
            }

            if (num == 2 && currentAnswerCnt == 2) return;

            if (num > currentAnswerCnt) {
                jQuery("[id^='p_exmp_word_"+parentNo+"_']").last().after(js_makeAnswerHtml(parentNo, num));

                if (num <= 5) {
                    jQuery('#i_exmp_word_'+parentNo+'_'+num).val(sampleAnswer[num-1]);
                } else {
                    jQuery('#i_exmp_word_'+parentNo+'_'+num).val('');
                }
                // 연결여부가 허용일 때 추가되는 보기문항 TR의 연결문항번호 Input을 출력
                if(jQuery('#i_connect_yn_'+parentNo).val() === 'Y'){
                    jQuery('#i_connect_num_'+parentNo+'_'+num).show();
                }

                js_changeAnswerType(parentNo);
            } else {
                for (var i=currentAnswerCnt; i>num; i--) {
                    jQuery("[id^='p_exmp_word_" + parentNo + "_']").last().remove();
                }
            }
        }

        /**
         * 문항 형식 변경
         *
         * @param i 문항 번호
         * @param mode 값있음 : 문항형식 변경 시, 값없음 : 페이지 로딩 or 문항개수 선택 시
         */
        function js_changeAnswerType(i, mode) {
            var answer_type = jQuery('#i_answ_type_'+i).val(); //S,D

            var is_change_type = (mode == undefined) ? false : true;

            // 문항형식을 객관식으로 변경하였을 경우
            if (answer_type == "S" && is_change_type) {
                var i_exmp_add_seq = parseInt(jQuery("#i_exmp_add_seq_" + i).val(), 10);
                if (i_exmp_add_seq == 0) {
                    jQuery("#i_exmp_add_seq_" + i).val(2);
                } else {
                    for (var j=1; j<=2; j++) {
                        jQuery("#i_exmp_word_" + i + "_" + j).val(sampleAnswer[j-1]);
                        jQuery("#i_exmp_id_" + i + "_" + j).val('a' + (++i_exmp_add_seq));
                    }
                    jQuery("#i_exmp_add_seq_" + i).val(i_exmp_add_seq);
                }
            }

            if (answer_type == 'S') { //객관식
                jQuery('#p_answ_cnt_'+i).css('display', '');
                jQuery('#p_answ_type_'+i).css('display', '');

                jQuery("[id^='p_exmp_word_"+i+"_']").each(function() {
                    jQuery(this).css('display', '');
                });

                jQuery("[id^='i_exmp_word_"+i+"_']").each(function() {
                    jQuery(this).attr('required', 'true');
                });
            } else {

                js_changAnswCnt(i, 2);
                jQuery('#i_answ_cnt_'+i).val(2);
                jQuery('#p_answ_cnt_'+i).css('display', 'none');
                jQuery('#p_answ_type_'+i).css('display', 'none');


                jQuery("[id^='p_exmp_word_"+i+"_']").each(function(){
                    jQuery(this).css('display', 'none');
                });

                jQuery("[id^='i_exmp_word_"+i+"_']").each(function(){
                    jQuery(this).removeAttr('required');
                });
            }
        }


        function js_changeAnswerMultiType(i) {
            var answ_multi_type = jQuery('#i_answ_multi_type_'+i).val();

            if (answ_multi_type == 'Y') {
                jQuery('#p_answ_multi_type_cnt_'+i).css('display', '');
            } else {
                jQuery('#p_answ_multi_type_cnt_'+i).css('display', 'none');
            }
        }

        function js_changeConnectYn(i)
        {
            var connect_yn = jQuery('#i_connect_yn_'+i).val();

            if (connect_yn == 'Y') {
                jQuery('[name^=i_connect_num_'+i+']').css('display', '');
            } else {
                jQuery('[name^=i_connect_num_'+i+']').css('display', 'none');
            }

        }

        function js_requestData(paper_id) {
            var _url = '/survey/action/survey.jspx?cmd=getSurveyPaper&paper_id='+paper_id;

            var http = jQuery.ajax({
                url: _url,
                type: "GET",
                data : "0=0",
                dataType: "json",
                async : false,
                error 	: function(xhr) {
                    alert(xhr.status);
                },
                success: function(json) {
                    var code = json.result.code;
                    var msg = json.result.msg;
                    var item = json.result.data;

                    if(code == '200') {
                        jQuery.each(item.paper_info, function(k, v){
                            var paper_item = v;
                            jQuery.each(paper_item, function(kk, vv){
                                jQuery('#'+kk).val(vv);
                            });
                        });
                        jQuery("[id^='"+question_div_prefix+"']").remove();

                        var old_pks = null;
                        for (var k=0; k<item.qstn_list.length; k++) {
                            if (old_pks == null || old_pks != item.qstn_list[k].qstn_no) {
                                var qstnIdx = item.qstn_list[k].qstn_no.split('_');
	                                qstnIdx = qstnIdx[qstnIdx.length - 1];
	                                qstnIdx = qstnIdx.substr(1);

                                js_changQstnCnt(qstnIdx);

                                old_pks = item.qstn_list[k].qstn_no;
                            }
                        }
                        // 문항 label 텍스트 및 qstn_ord의 value값 화면순서대로 재부여
                        js_fieldsetIdChange();

                        var old_pk = null;
                        var x = 1;

                        for (var k = 0, kc = item.qstn_list.length; k < kc; k++) {
                            var printIdx = [];
                            if (old_pk == null || old_pk != item.qstn_list[k].qstn_no) {

                                var qstnIdx = item.qstn_list[k].qstn_no.split('_');
                                    qstnIdx = qstnIdx[qstnIdx.length - 1];
                                    qstnIdx = qstnIdx.substr(1);

                                jQuery('#i_qstn_id_'+(qstnIdx)).val((item.qstn_list[k].qstn_no).replace(jQuery("#paper_id").val() + "_", ""));
                                jQuery('#i_exmp_add_seq_'+(qstnIdx)).val(item.qstn_list[k].exmp_add_seq);
                                jQuery('#i_answ_type_'+(qstnIdx)).val(item.qstn_list[k].answ_type);
                                jQuery('#i_qstn_answ_'+(qstnIdx)).val(item.qstn_list[k].qstn_answ);
                                jQuery('#i_answ_multi_type_'+(qstnIdx)).val(item.qstn_list[k].multi_yn);
                                jQuery('#i_answ_cnt_'+(qstnIdx)).val(item.qstn_list[k].answ_cnt);
                                jQuery('#i_connect_yn_'+(qstnIdx)).val(item.qstn_list[k].connect_yn);

                                js_changeAnswerType(qstnIdx);
                                js_changeAnswerMultiType(qstnIdx);
                                js_changeConnectYn(qstnIdx);

                                if (item.qstn_list[k].answ_type == 'S') {
                                    var answ_cnt = item.qstn_list[k].answ_cnt;
                                    var i_exmp_word_len =  jQuery("[id^='i_exmp_word_"+qstnIdx+"_']").length;

                                    if (answ_cnt > i_exmp_word_len) {
                                        for (var j=i_exmp_word_len+1; j<=answ_cnt; j++) {
                                            js_changAnswCnt(qstnIdx, j);
                                        }
                                    } else if (answ_cnt < i_exmp_word_len) {
                                        for (var j=i_exmp_word_len;j>answ_cnt;j-- ) {
                                            js_changAnswCnt(qstnIdx, j);
                                        }
                                    }

                                    // console.log(jQuery("#paper_id").val());
	                                var p_exmp_word = jQuery('[id^=p_exmp_word_'+qstnIdx+']'),
                                        th_connect_num = jQuery('[id^=th_connect_num_'+qstnIdx+']'),
                                        td_connect_num = jQuery('[id^=td_connect_num_'+qstnIdx+']'),
                                        i_exmp_id = jQuery('[id^=i_exmp_id_'+qstnIdx+']'),
                                        i_exmp_word = jQuery('[id^=i_exmp_word_'+qstnIdx+']'),
                                        i_connect_num = jQuery('[id^=i_connect_num_'+qstnIdx+']'),
                                        i_exmp_seq = jQuery('[id^=i_exmp_seq_'+qstnIdx+']');

                                    for (var q = 0, qc = item.qstn_exmp_list.length; q < qc; q++) {
                                        if (item.qstn_list[k].qstn_no == item.qstn_exmp_list[q].qstn_no) {
                                            if (item.qstn_exmp_list[q].exmp_seq_no != '') {
                                                var sQstnOrd = item.qstn_exmp_list[q].exmp_seq_no.split('_');
                                                    sQstnOrd = sQstnOrd[sQstnOrd.length - 1];
                                                    sQstnOrd = sQstnOrd.substr(1);
	                                            var sQstnStr = qstnIdx+'_'+sQstnOrd;

                                                var sQstnSort = Number(item.qstn_exmp_list[q].exmp_sort_no) - 1;
                                                p_exmp_word.eq(sQstnSort).attr('id', 'p_exmp_word_'+sQstnStr);
                                                th_connect_num.eq(sQstnSort).attr('id', 'th_connect_num_'+sQstnStr);
                                                td_connect_num.eq(sQstnSort).attr('id', 'td_connect_num_'+sQstnStr);
                                                i_exmp_seq.eq(sQstnSort).val(sQstnOrd);
                                                i_exmp_id.eq(sQstnSort)
	                                                .attr('id', 'i_exmp_id_'+sQstnStr)
	                                                .attr('name', 'i_exmp_id_'+sQstnStr).val("a"+sQstnOrd);
                                                i_exmp_word.eq(sQstnSort)
                                                    .attr('id', 'i_exmp_word_'+sQstnStr)
                                                    .attr('name', 'i_exmp_word_'+sQstnStr).val(item.qstn_exmp_list[q].exmp_word);
                                                i_connect_num.eq(sQstnSort)
                                                    .attr('id', 'i_connect_num_'+sQstnStr)
                                                    .attr('name', 'i_connect_num_'+sQstnStr).val((item.qstn_exmp_list[q].connect_qstn_no).replace(jQuery("#paper_id").val() + "_q", ""));
                                                printIdx.push(q);
                                            }
                                        }
                                    }
                                }

                                if (item.qstn_list[k].multi_yn == 'Y') {
                                    jQuery('#i_answ_multi_cnt_'+(qstnIdx)).val(item.qstn_list[k].multi_cnt);
                                }

                                x++;
                            }

                            old_pk = item.qstn_list[k].qstn_no;

                            for (var xx=printIdx.length-1; xx>=0; xx--) {
                                item.qstn_exmp_list.splice(xx, 1);
                            }
                        }
                    } else {
                        msgStart(msg_com_code_010+"("+msg+")", 'danger');
                    }
                }
            });
        }

        function js_requestSave() {
            if(!checkFormField('#form')) return;
            if(jQuery("[id^='"+question_div_prefix+"']").length <= 0) {
                msgStart('설문 문항이 1개 이상 등록되어야 저장이 가능합니다.', 'danger');
                return;
            }
            var _url = '/survey/action/survey.jspx?cmd=makeSurvey';
            var http = jQuery.ajax({
                url: _url,
                type: "POST",
                data : jQuery("#form").serialize(),
                dataType: "json",
                async : false,
                error 	: function(xhr) {
                    alert(xhr.status);
                },
                success: function(json) {
                    var code = json.result.code;
                    var msg = json.result.msg;

                    if (code == '200') {
                        msgStart(msg_com_code_009);
                        jQuery('#btnList').click();
                    } else {
                        msgStart(msg_com_code_010+"("+msg+")", 'danger');
                    }
                }
            });
        }

        function js_requestDrop() {
            if(confirm(msg_com_code_007)) {
                var _url = '/survey/action/survey.jspx?cmd=dropSurvey';

                var http = jQuery.ajax({
                    url: _url,
                    type: "POST",
                    data : "&paper_id="+jQuery('#paper_id').val(),
                    dataType: "json",
                    async : false,
                    error : function(xhr) {
                        alert(xhr.status);
                    },
                    success: function(json) {
                        var code = json.result.code;
                        var msg = json.result.msg;

                        if (code == '200') {
                            msgStart(msg_com_code_009);

                            setTimeout(function(){
                                location.href='/survey/survey.jspx?cmd=list';
                            }, 1000);
                        } else if(code == '404') {
                            msgStart(msg_com_code_010+"(이미 설문일정 결과가 있음.)", 'danger');
                        } else {
                            msgStart(msg_com_code_010+"("+msg+")", 'danger');
                        }
                    }
                });
            } else {
            }
        }

        function js_requestCopy() {
            if(confirm('설문지를 복사하시겠습니까?')) {
                var _url = '/survey/action/survey.jspx?cmd=copyPaper';

                var http = jQuery.ajax({
                    url: _url,
                    type: "POST",
                    data : "&paper_id="+jQuery('#paper_id').val(),
                    dataType: "json",
                    async : false,
                    error : function(xhr) {
                        alert(xhr.status);
                    },
                    success: function(json) {
                        var code = json.result.code;
                        var msg = json.result.msg;

                        if (code == '200') {
                            msgStart(msg_com_code_009);

                            setTimeout(function(){
                                location.href='/survey/survey.jspx?cmd=list';
                            }, 1000);
                        } else {
                            msgStart(msg_com_code_010+"("+msg+")", 'danger');
                        }
                    }
                });
            }
        }


	</script>
</head>
<body>

<div class="x_panel">     
	<div class="x_content">
			<div class="margin_3_0">
				<div class="margin_15">
					<form name='form' id='form'>
						<input type='hidden' name='paper_id' id='paper_id' value='0'>
						<input type='hidden' name='qstn_add_seq' id='qstn_add_seq' value='0'>
						<table class="com_table_box">
							<tbody style="border-top:3px solid #4A90CC;">
							<tr class='last-tr'>
								<th width="100px"> 설문지 제목</th>
								<td style="width:500px !important;">
									<input class="form-control" id="paper_title" name="paper_title" type="text" required='true' alt='설문지 제목'/>
								</td>
								<th width="100px"> 문항 개수</th>
								<td>
									<div class="jquery-spinner"><input class="W50P" id="qstn_cnt" name="qstn_cnt" type="text" style="width:30px !important;height:26px !important" readOnly/></div>
								</td>
							</tr>
							</tbody>
						</table>
						<hr/>
						<fieldset id="questionheader"></fieldset>
					</form>
				</div>
			</div>

			<div class="pull-right">
				<button id="btnList" class="btn btn-sm btn-default">
					<i class="icon- fa fa-list"> 목록</i>
				</button>
				<button id="btnSave" class="btn btn-sm btn-primary">
					<i class="icon- fa fa-save"> 저장</i>
				</button>
				<% if(!"".equals(input.getText("paper_id"))) { %>
				<button id="btnDel" class="btn btn-sm btn-danger">
					<i class="icon- fa fa-ban"> 삭제</i>
				</button>
				<button id="btnCopy" class="btn btn-sm btn-info">
					<i class="icon- fa fa-plus"> 설문지복사</i>
				</button>
				<% } %>
			</div>
    </div>
</div>		    
<div class="row-fluid">
	<div class="span12">
		<div class="widget-box border_0">
			
		</div>
	</div>
</div>
</body>
</html>