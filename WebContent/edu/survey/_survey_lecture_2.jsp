<%@page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp"%>
<%
	String lecture_seq_no = input.getText("lecture_seq_no");
	String user_lecture_seq_no = input.getText("user_lecture_seq_no");
%>

<!DOCTYPE html>
<html>
<head>
<script src="/static/com/js/common.js?_=0.4169023180614877" language="javascript" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.qnaWrap ul { font-size: 1.2em; }
.qnaWrap ul li h5.title	{font-weight:bold; font-size:1.2em; }
.qnaWrap dl {margin-left:20px;  font-size: 1.2em;}
.qnaWrap dt { }
.qnaWrap dd {margin-left:20px;  }

.center {text-align:center;  }

.qnaWrap ul li div.answer2 {
    padding: 10px 25px 10px 25px;
    border-top: 1px solid #a5a5a5;
    font-size: 1.2em;
    line-height: 1.2em;
}
.qnaWrap ul li div.tit {
    display: block;
    padding: 10px 0 10px 25px;
    color: #165a94;
    font-weight: bold;
    font-size: 1.2em;
    line-height: 2.0em;
}
.qnaWrap ul li div.tit {
    display: block;
    padding: 10px 0 10px 25px;
    color: #165a94;
    font-weight: bold;
    font-size: 1.2em;
    line-height: 2.0em;
}
.list_type2 table td.tdlt {
    text-align:left;
}
.list_type2 table.noline  {
    border:0px;
}
.list_type2 table.noline td {
    border:0px;
    padding: 0px 8px;
}
.list_type2 table.noline th {
    border:0px;
}
</style>
<script type="text/javascript">
	
	jQuery(document).ready(function(){
		var title = '위생교육 만족도 설문조사';
		document.title = title;
		jQuery('#_head_text_title').html(title);
		
		//설문참여 등록
		jQuery('#btnSave').bind('click', function(){
			js_requestSave();
			return false;
		});
		
	});
	
	
	//설문참여 등록
	function js_requestSave()
	{
		var sendData = jQuery("#form").serialize();
		var _url = '/edu/survey/action/survey.jspx?cmd=lectureSurveyAnsw';
				
		if(!checkFormField('#form'))
		{
			return;
		}
				
		if( !checkEtc('q101', 'q1011')){
			msgStart('일반사항 1번문항에서 기타를 선택하시면 내용을 입력해주세요', 'warning', null);
			$('#q1011').focus()
			return;
		}
		
		var idx =0;
		var str ='';
		$("input:checkbox[name=sq81]:checked").each(function (index) {  
	        if(idx == 0)
	        	str = $(this).val();
	        else
				str += "," + $(this).val(); 
	        idx++;
	    });
		
		if( idx == 0 ){
			msgStart('교육과목 선정을 위한 과목을 선택해주세요', 'warning', null);
			return;
		}
		if( idx > 3 ){
			msgStart('교육과목 선정을 위한 과목을 3개까지만 선택해주세요', 'warning', null);
			return;
		}
		
		$('#q81').val(str);
		
		if( !checkEtc('q73', 'q731')){
			msgStart('교육운영 3번문항에서 기타를 선택하시면 내용을 입력해주세요', 'warning', null);
			$('#q731').focus();
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
					alert('설문에 참여해주셔서 감사합니다.');
					jQuery('#btnSave').addClass('hide');
					try{
						opener.fn_certOpenDown();
					}catch(e){
						try{
							if(window.opener) { window.opener.document.location.href = '/edu/lecture/classCtrl.jspx?cmd=viewClassList';} 
							self.close();							
						}catch(e){}
					}
				
				}else{
					alert('죄송합니다. 처리중 문제가 발생하였습니다.');
					return;
				}
			}			
		});	
	}
	
	function checkEtc(etcChk, etcObj)
	{
		if( $('input:radio[id='+etcChk+']').is(':checked') )
		{
			if( $('#'+etcObj).val() == '' ) return false;
		}
		return true;
	}
	
</script>
</head>

<body>
<form name="form" id="form"  onSubmit="javascript:return false;">
<input type="hidden" name="lecture_seq_no" id="lecture_seq_no" value="<%=lecture_seq_no%>" />
<input type="hidden" name="user_lecture_seq_no" id="user_lecture_seq_no" value="<%=user_lecture_seq_no%>" />
<input type="hidden" name="paper_id" id="paper_id" value="<%=lecture_seq_no%>" />
<input type='hidden' name='q81' id="q81" value=''>
<div class="popup_content">
	<div class="class_popup_tit ">
	    <strong>위생교육 만족도 설문조사</strong>
	</div>
	
	<div class="qnaWrap">
		<ul id="qna">
			<li>	
				<div class="tit">◆ 전반적 만족도 ◆</div>	
				<div class="answer2 list_type2" style="display:block;">
					<table>
							<colgroup>
								<col width="500px"/><col width="10%"/>
								<col width="10%"/><col width="10%"/><col width="10%"/><col width="10%"/>
							</colgroup>
							<thead>
								<tr>
									<th>항목</th>
									<th>매우<br>그렇다</th>
									<th>그렇다</th>
									<th>보통</th>
									<th>아니다</th>
									<th>전혀<br>아니다</th>
								</tr>
							</thead>
							<tbody>
							<tr>
								<td class="tdlt">1. 본 과정의 교과목 구성이 적절하게 편성되었다</td>
								<td><input type='radio' name='q11' value='1' required="true" alt="전반적 만족도 1번"></td>
								<td><input type='radio' name='q11' value='2' required="true" alt="전반적 만족도 1번"></td>
								<td><input type='radio' name='q11' value='3' required="true" alt="전반적 만족도 1번"></td>
								<td><input type='radio' name='q11' value='4' required="true" alt="전반적 만족도 1번"></td>
								<td><input type='radio' name='q11' value='5' required="true" alt="전반적 만족도 1번"></td>
							</tr>
							<tr>
								<td class="tdlt">2. 본 교육을 이수함으로써 업무에 도움이 될 것이다</td>
								<td><input type='radio' name='q21' value='1' required="true" alt="전반적 만족도 2번"></td>
								<td><input type='radio' name='q21' value='2' required="true" alt="전반적 만족도 2번"></td>
								<td><input type='radio' name='q21' value='3' required="true" alt="전반적 만족도 2번"></td>
								<td><input type='radio' name='q21' value='4' required="true" alt="전반적 만족도 2번"></td>
								<td><input type='radio' name='q21' value='5' required="true" alt="전반적 만족도 2번"></td>
							</tr>
							</tbody>
						</table>
				</div>
			</li>
			<li>	
				<div class="tit">◆ 교육강사 평가 ◆</div>	
				<div class="answer2 list_type2" style="display:block;">
					<table>
							<colgroup>
								<col width="500px"/><col width=80px"/><col width="10%"/>
								<col width="10%"/><col width="10%"/><col width="10%"/><col width="10%"/>
							</colgroup>
							<thead>
								<tr>
									<th colspan="2">항목</th>
									<th>매우<br>그렇다</th>
									<th>그렇다</th>
									<th>보통</th>
									<th>아니다</th>
									<th>전혀<br>아니다</th>
								</tr>
							</thead>
							<tbody>
							<tr>
								<td rowspan="3" class="tdlt">
									<h5 class="title">1. 강의 내용</h5>
									<dl>
										<dt>교육내용이 해당분야의 지식과 기술을 습득할 수 있는 내용으로 구성되었다</dt>						
									</dl>
								</td>
								<td>1교시</td>
								<td><input type='radio' name='q31' value='1' required="true" alt="강의 내용 1교시"></td>
								<td><input type='radio' name='q31' value='2' required="true" alt="강의 내용 1교시"></td>
								<td><input type='radio' name='q31' value='3' required="true" alt="강의 내용 1교시"></td>
								<td><input type='radio' name='q31' value='4' required="true" alt="강의 내용 1교시"></td>
								<td><input type='radio' name='q31' value='5' required="true" alt="강의 내용 1교시"></td>
							</tr>
							<tr>								
								<td>2교시</td>
								<td><input type='radio' name='q32' value='1' required="true" alt="강의 내용 2교시"></td>
								<td><input type='radio' name='q32' value='2' required="true" alt="강의 내용 2교시"></td>
								<td><input type='radio' name='q32' value='3' required="true" alt="강의 내용 2교시"></td>
								<td><input type='radio' name='q32' value='4' required="true" alt="강의 내용 2교시"></td>
								<td><input type='radio' name='q32' value='5' required="true" alt="강의 내용 2교시"></td>
							</tr>
							<tr>
								<td>3교시</td>
								<td><input type='radio' name='q33' value='1' required="true" alt="강의 내용 3교시"></td>
								<td><input type='radio' name='q33' value='2' required="true" alt="강의 내용 3교시"></td>
								<td><input type='radio' name='q33' value='3' required="true" alt="강의 내용 3교시"></td>
								<td><input type='radio' name='q33' value='4' required="true" alt="강의 내용 3교시"></td>
								<td><input type='radio' name='q33' value='5' required="true" alt="강의 내용 3교시"></td>
							</tr>
							<tr>
								<td rowspan="3" class="tdlt">
									<h5 class="title">2. 강의 준비성</h5>
									<dl>
										<dt>강의 자료가 풍부하다</dt>
										<dt>(동영상, 사례등의 준비가 잘되있다)</dt>
									</dl>
								</td>
								<td>1교시</td>
								<td><input type='radio' name='q41' value='1' required="true" alt="강의 준비성 1교시"></td>
								<td><input type='radio' name='q41' value='2' required="true" alt="강의 준비성 1교시"></td>
								<td><input type='radio' name='q41' value='3' required="true" alt="강의 준비성 1교시"></td>
								<td><input type='radio' name='q41' value='4' required="true" alt="강의 준비성 1교시"></td>
								<td><input type='radio' name='q41' value='5' required="true" alt="강의 준비성 1교시"></td>
							</tr>
							<tr>								
								<td>2교시</td>
								<td><input type='radio' name='q42' value='1' required="true" alt="강의 준비성 2교시"></td>
								<td><input type='radio' name='q42' value='2' required="true" alt="강의 준비성 2교시"></td>
								<td><input type='radio' name='q42' value='3' required="true" alt="강의 준비성 2교시"></td>
								<td><input type='radio' name='q42' value='4' required="true" alt="강의 준비성 2교시"></td>
								<td><input type='radio' name='q42' value='5' required="true" alt="강의 준비성 2교시"></td>
							</tr>
							<tr>
								<td>3교시</td>
								<td><input type='radio' name='q43' value='1' required="true" alt="강의 준비성 3교시"></td>
								<td><input type='radio' name='q43' value='2' required="true" alt="강의 준비성 3교시"></td>
								<td><input type='radio' name='q43' value='3' required="true" alt="강의 준비성 3교시"></td>
								<td><input type='radio' name='q43' value='4' required="true" alt="강의 준비성 3교시"></td>
								<td><input type='radio' name='q43' value='5' required="true" alt="강의 준비성 3교시"></td>
							</tr>
							<tr>
								<td rowspan="3" class="tdlt">
									<h5 class="title">3. 강의 스킬</h5>
									<dl>
										<dt>강사의 진행이 매끄럽고 자연스럽다</dt>
  										<dt>(표정, 시선, 발음, 태도, 제스처등)</dt>						
									</dl>
								</td>
								<td>1교시</td>
								<td><input type='radio' name='q51' value='1' required="true" alt="강의 스킬 1교시"></td>
								<td><input type='radio' name='q51' value='2' required="true" alt="강의 스킬 1교시"></td>
								<td><input type='radio' name='q51' value='3' required="true" alt="강의 스킬 1교시"></td>
								<td><input type='radio' name='q51' value='4' required="true" alt="강의 스킬 1교시"></td>
								<td><input type='radio' name='q51' value='5' required="true" alt="강의 스킬 1교시"></td>
							</tr>
							<tr>								
								<td>2교시</td>
								<td><input type='radio' name='q52' value='1' required="true" alt="강의 스킬 2교시"></td>
								<td><input type='radio' name='q52' value='2' required="true" alt="강의 스킬 2교시"></td>
								<td><input type='radio' name='q52' value='3' required="true" alt="강의 스킬 2교시"></td>
								<td><input type='radio' name='q52' value='4' required="true" alt="강의 스킬 2교시"></td>
								<td><input type='radio' name='q52' value='5' required="true" alt="강의 스킬 2교시"></td>
							</tr>
							<tr>
								<td>3교시</td>
								<td><input type='radio' name='q53' value='1' required="true" alt="강의 스킬 3교시"></td>
								<td><input type='radio' name='q53' value='2' required="true" alt="강의 스킬 3교시"></td>
								<td><input type='radio' name='q53' value='3' required="true" alt="강의 스킬 3교시"></td>
								<td><input type='radio' name='q53' value='4' required="true" alt="강의 스킬 3교시"></td>
								<td><input type='radio' name='q53' value='5' required="true" alt="강의 스킬 3교시"></td>
							</tr>
							<tr>
								<td rowspan="3" class="tdlt">
									<h5 class="title">4. 전문지식 수준</h5>
									<dl>
										<dt>과목에 대한 전문성과 지식이 우수하다</dt>						
									</dl>
								</td>
								<td>1교시</td>
								<td><input type='radio' name='q61' value='1' required="true" alt="전문지식 수준 1교시"></td>
								<td><input type='radio' name='q61' value='2' required="true" alt="전문지식 수준 1교시"></td>
								<td><input type='radio' name='q61' value='3' required="true" alt="전문지식 수준 1교시"></td>
								<td><input type='radio' name='q61' value='4' required="true" alt="전문지식 수준 1교시"></td>
								<td><input type='radio' name='q61' value='5' required="true" alt="전문지식 수준 1교시"></td>
							</tr>
							<tr>								
								<td>2교시</td>
								<td><input type='radio' name='q62' value='1' required="true" alt="전문지식 수준 2교시"></td>
								<td><input type='radio' name='q62' value='2' required="true" alt="전문지식 수준 2교시"></td>
								<td><input type='radio' name='q62' value='3' required="true" alt="전문지식 수준 2교시"></td>
								<td><input type='radio' name='q62' value='4' required="true" alt="전문지식 수준 2교시"></td>
								<td><input type='radio' name='q62' value='5' required="true" alt="전문지식 수준 2교시"></td>
							</tr>
							<tr>
								<td>3교시</td>
								<td><input type='radio' name='q63' value='1' required="true" alt="전문지식 수준 3교시"></td>
								<td><input type='radio' name='q63' value='2' required="true" alt="전문지식 수준 3교시"></td>
								<td><input type='radio' name='q63' value='3' required="true" alt="전문지식 수준 3교시"></td>
								<td><input type='radio' name='q63' value='4' required="true" alt="전문지식 수준 3교시"></td>
								<td><input type='radio' name='q63' value='5' required="true" alt="전문지식 수준 3교시"></td>
							</tr>
							</tbody>
						</table>
				</div>
			</li>
			
			<li>	
				<div class="tit">◆ 교육 운영 ◆</div>	
				<div class="answer2 list_type2" style="display:block;">
					<table>
						<colgroup>
							<col width="500px"/><col width="10%"/>
							<col width="10%"/><col width="10%"/><col width="10%"/><col width="10%"/>
						</colgroup>
						<thead>
							<tr>
								<th>항목</th>
								<th>매우<br>그렇다</th>
								<th>그렇다</th>
								<th>보통</th>
								<th>아니다</th>
								<th>전혀<br>아니다</th>
							</tr>
						</thead>
						<tbody>
						<tr>
							<td class="tdlt">1. 교육과정 안내 및 수강절차가 간편하고 알기 쉬웠다</td>
							<td><input type='radio' name='q71' value='1' required="true" alt="교육 운영 1번"></td>
							<td><input type='radio' name='q71' value='2' required="true" alt="교육 운영 1번"></td>
							<td><input type='radio' name='q71' value='3' required="true" alt="교육 운영 1번"></td>
							<td><input type='radio' name='q71' value='4' required="true" alt="교육 운영 1번"></td>
							<td><input type='radio' name='q71' value='5' required="true" alt="교육 운영 1번"></td>
						</tr>
						<tr>
							<td class="tdlt">2. 교육수강이 끊김없이 원활하게 진행되었다</td>
							<td><input type='radio' name='q72' value='1' required="true" alt="교육 운영 2번"></td>
							<td><input type='radio' name='q72' value='2' required="true" alt="교육 운영 2번"></td>
							<td><input type='radio' name='q72' value='3' required="true" alt="교육 운영 2번"></td>
							<td><input type='radio' name='q72' value='4' required="true" alt="교육 운영 2번"></td>
							<td><input type='radio' name='q72' value='5' required="true" alt="교육 운영 2번"></td>
						</tr>
						<tr>
							<td colspan="6" class="tdlt">3. 온라인 교육에 대한 불만이 있다면 해당 항목을 선택해 주시기 바랍니다. (없다면 다음 항목으로)</td>							
						</tr>
						</tbody>
					</table>
					<table style="border-top:0px;">
						<colgroup>
							<col width="15%"/><col width="15%"/><col width="15%"/><col width="15%"/><col width="5%"/><col width="35%"/>
						</colgroup>
						<thead>
							<tr>
								<th>조작의 어려움</th>
								<th>복잡한 신청과정</th>
								<th>강사와의 소통불가</th>
								<th>교육생간의 정보공유 불가</th>
								<th colspan="2">기타</th>
							</tr>
						</thead>
						<tbody>						
						<tr>
							<td><input type='radio' name='q73' value='조작의어려움' alt="교육 운영 3번"></td>
							<td><input type='radio' name='q73' value='복잡한신청과정' alt="교육 운영 3번"></td>
							<td><input type='radio' name='q73' value='강사와의소통불가' alt="교육 운영 3번"></td>
							<td><input type='radio' name='q73' value='교육생간의정보공유불가' alt="교육 운영 3번"></td>
							<td><input type='radio' name='q73' id='q73' value='기타' alt="교육 운영 3번"></td>
							<td><input type='text' name='q731' id='q731' value='' class="form-control" maxlength="50"></td>
						</tr>
						
						</tbody>
					</table>
				</div>
			</li>
			
			<li>	
				<div class="tit">◆ 교육과목 선정 ◆</div>
				<div class="answer2 list_type2" style="display:block;">
					<h5 class="title ">다음 년도에 교육을 받고 싶은 과목을 선택하여 주십시오.(복수로 3개까지 선택가능합니다)</h5>		
					<table>
						<colgroup>
							<col width="15"/><col width="10%"/><col width="15"/><col width="10%"/>
							<col width="15"/><col width="10%"/><col width="15"/><col width="10%"/>
						</colgroup>
						<thead>
							<tr>
								<th>과목명</th>
								<th>선택</th>
								<th>과목명</th>
								<th>선택</th>
								<th>과목명</th>
								<th>선택</th>
								<th>과목명</th>
								<th>선택</th>
							</tr>
						</thead>
						<tbody>
						<tr>
							<td>석재관리</td>
							<td><input type='checkbox' name='sq81' id='sq801' value='석재관리'></td>
							<td>먼지관리</td>
							<td><input type='checkbox' name='sq81' id='sq802' value='먼지관리'></td>
							<td>청소약품</td>
							<td><input type='checkbox' name='sq81' id='sq803' value='청소약품'></td>
							<td>산업안전보건</td>
							<td><input type='checkbox' name='sq81' id='sq804' value='산업안전보건'></td>
						</tr>
						<tr>
							<td>타일관리</td>
							<td><input type='checkbox' name='sq81' id='sq805' value='타일관리'></td>
							<td>청소장비(개요)</td>
							<td><input type='checkbox' name='sq81' id='sq806' value='청소장비(개요)'></td>
							<td>청소도구</td>
							<td><input type='checkbox' name='sq81' id='sq807' value='청소도구'></td>
							<td>현장노무점검</td>
							<td><input type='checkbox' name='sq81' id='sq808' value='현장노무점검'></td>
						</tr>
						<tr>
							<td>카펫관리</td>
							<td><input type='checkbox' name='sq81' id='sq809' value='카펫관리'></td>
							<td>습식청소장비</td>
							<td><input type='checkbox' name='sq81' id='sq810' value='습식청소장비'></td>
							<td>화장실청소</td>
							<td><input type='checkbox' name='sq81' id='sq811' value='화장실청소'></td>
							<td>CS(서비스)</td>
							<td><input type='checkbox' name='sq81' id='sq812' value='CS(서비스)'></td>
						</tr>
						<tr>
							<td>나무관리</td>
							<td><input type='checkbox' name='sq81' id='sq813' value='나무관리'></td>
							<td>기타청소장비</td>
							<td><input type='checkbox' name='sq81' id='sq814' value='기타청소장비'></td>
							<td>일상청소관리</td>
							<td><input type='checkbox' name='sq81' id='sq815' value='일상청소관리'></td>
							<td>바닥관리</td>
							<td><input type='checkbox' name='sq81' id='sq816' value='바닥관리'></td>
						</tr>
						</tbody>
					</table>
					
				</div>
			</li>
			<li>	
				<div class="tit">◆ 위생교육의 발전을 위한 요청사항이 있다면 적어주시기 바랍니다.(100자 이내) ◆</div>
				<div class="answer2 list_type2" style="display:block;">
					<textarea class='W100P textArea_height100' name='q91' style='width:100% !important;' maxlength='100'  alt="위생교육의 발전을 위한 요청사항"></textarea>
				</div>
			</li>
			<li>	
				<div class="tit">◆ 일반 사항 ◆</div>
				<div class="answer2 list_type2" style="display:block;">
					<h5 class="title">▪ 직위</h5>	
					<table class="noline">
						<colgroup>
							<col width="10%"/><col width="10%"/>
							<col width="10%"/><col width="10%"/><col width=""/>
						</colgroup>							
						<tbody>
						<tr>
							<td>① 소장 <input type='radio' name='q101' value='소장' required="true" alt="일반 사항 1번"></td>
							<td>② 감독 <input type='radio' name='q101' value='감독' required="true" alt="일반 사항 1번"></td>
							<td>③ 반장 <input type='radio' name='q101' value='반장' required="true" alt="일반 사항 1번"></td>
							<td>④ 기타 <input type='radio' name='q101' id='q101' value='기타' required="true" alt="일반 사항 1번"></td>
							<td><input type='text' name='q1011' id='q1011' value='' class="form-control" maxlength="50"></td>						
						</tr>							
						</tbody>
					</table>
					<h5 class="title">▪ 해당업종 근무기간</h5>
					<table class="noline">
						<colgroup>
							<col width=""/><col width=""/>
							<col width=""/><col width=""/>
						</colgroup>							
						<tbody>
						<tr>
							<td>① 1년 미만 <input type='radio' name='q102' value='1년미만' required="true" alt="일반 사항 2번"></td>
							<td>② 1년이상~5년미만 <input type='radio' name='q102' value='1년이상~5년미만' required="true" alt="일반 사항 2번"></td>
							<td>③ 5년이상~10년미만 <input type='radio' name='q102' value='5년이상~10년미만' required="true" alt="일반 사항 2번"></td>
							<td>④ 10년이상 <input type='radio' name='q102' value='10년이상' required="true" alt="일반 사항 2번"></td>
						</tr>							
						</tbody>
					</table>
					<h5 class="title">▪ 온라인과 오프라인 교육 중 어느 것을 선호하십니까?</h5>
					<table class="noline">
						<colgroup>
							<col width="200px"/><col width=""/>
						</colgroup>							
						<tbody>
						<tr>
							<td>① 오프라인(집합)교육 <input type='radio' name='q103' value='오프라인(집합)교육' required="true" alt="일반 사항 3번"></td>
							<td class="tdlt">② 온라인교육 <input type='radio' name='q103' value='온라인교육' required="true" alt="일반 사항 3번"></td>
						</tr>						
						</tbody>
					</table>
				</div>
			</li>
		</ul>
	</div>
	<div class="x_panel" >     
		<div class="x_content">
			<div class="center">
				<a id="btnSave" class="btn btn-primary btn-curved" style="margin:20px;font-size:large">
					<i class="icon-arrow-right"> 설문저장하기</i>
				</a>
			</div>
		</div>
	</div>	
</div>	

</form>
</body>
</html>
