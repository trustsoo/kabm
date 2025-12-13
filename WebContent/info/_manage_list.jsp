<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	jQuery(document).ready(function(){
		$("#qna a.tit").click(function(){
			$("div",this.parentNode).toggle();
		});
		
	});
	
	function goLaw(div){
		
		var _url = '';
		if(div == 1) _url = 'https://www.law.go.kr/LSW/lsInfoP.do?efYd=20200708&lsiSeq=217247#0000';
		else if( div == 2 ) _url = 'https://www.law.go.kr/LSW/lsInfoP.do?efYd=20200604&lsiSeq=218535#0000';
		else _url = 'https://www.law.go.kr/LSW/lsInfoP.do?efYd=20210101&lsiSeq=213143#0000';
		
		window.open(_url);
	}
</script>
<style>
.qnaWrap ul li h5.title	{font-weight:bold; font-size:1.2em; }
.qnaWrap dl {margin-left:20px;  font-size: 1.2em;}
.qnaWrap dt { }
.qnaWrap dd {margin-left:20px;  }

.qnaWrap .mgl20 {margin-left:20px;  }
.qnaWrap .ftl {float:left; }
.qnaWrap .ftr {float:right; }
.qnaWrap .blue {color:blue; }
.qnaWrap .link {cursor:pointer; }
.qnaWrap .clear {clear:both; }
.qnaWrap .tile-stats {
    position: relative;
    display: block;
    margin: 20px 1px 30px 5px;
    border: 2px solid #6a9ee0;
    -webkit-border-radius: 5px;
    overflow: hidden;
    padding: 15px;
    -webkit-background-clip: padding-box;
    -moz-border-radius: 5px;
    -moz-background-clip: padding;
    border-radius: 5px;
    background-clip: padding-box;
    background: #FFF;
    transition: all 300ms ease-in-out;
    font-weight:bold;
    font-size: 1.2em;
    color:#215698;
}
.qnaWrap .arrow-stats { margin-top: 20px;padding:40px 20px 0px 20px;font-size: 40px;}
</style>
</head>
<body>
<div id="content">
<!-- start :: content -->
	
	<div class="qnaWrap">
		<ul id="qna">
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 건물위생관리업이란? ◆</a>	
				<div class="answer" style="display:block;">
					<h5 class="title ftl">공중이 이용하는 *건축물·**시설물 등의 청결유지와 실내공기정화를 위한 청소등을 대행하는 영업.</h5> <span class="ftr blue link" onclick="goLaw(1);">[공중위생관리법 제2조 제1항 제7호]</span>
					<dl class="clear">
						<dt>* 건축물</dt>
						<dd>1. 주택(단독주택: 단독주택, 다중주택, 다가구주택, 공관, 공동주택: 아파트, 연립주택, 다세대주택, 기숙사)</dd>
						<dd>2. 제1종 근린생활시설(슈퍼마켓과 일용품의 소매점, 휴게음식점·제과점, 이용원·미용원·일반목욕장 및 세탁소)</dd>
						<dd>3. 제2종 근린생활시설(일반음식점·기원, 휴게음식점·제과점으로서 제1종 근린생활시설에 해당하지 아니하는 것, 서점, 테니스장·체력단련장·에어로빅장·볼링장·당구장·실내낚시터·골프연습장)</dd>
						<dd>4. 문화 및 집회시설, 의료시설, 교육연구 및 복지시설, 운동시설, 숙박시설, 위락시설, 공장 등</dd>						
					</dl>					
					<dl>
						<dt>** 시설물</dt>
						<dd>넓은 의미에서 시설물은 공통된 특성을 갖는 건물군이나 도로, 하부 구조물 등을 가리키나 대개는 인간의 생활환경과 밀접한 관계를 갖는 장치, 즉 건물 내의 전기, 전화, 상·하수도, 난방 등의 배관과 집·분산 및 연결 장치 등을 총칭함.</dd>
					</dl>
				</div>
			</li>
			
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 건물위생관리업 영업신고절차 ◆</a>	
				<div class="answer" style="display: none;">
					
					
					<span class="ftl tile-stats">1. 영업장<br>
							2. 창고<br>
							3. 필수보유 장비
					</span>
					<span class="ftl arrow-stats">⇒</span>
					<span class="ftl tile-stats">4. 신규 및 승계자 <br>
												   위생교육 수료<br>
												   (수료증)

					</span>
					<span class="ftl arrow-stats">⇒</span>
					<span class="ftl tile-stats">5. 해당 시·군·구 <br>
												담당부서(위생과)에 <br>영업신고

					</span>
					
					
					<h5 class="title clear">1. 영업장</h5> 
					<dl>
						<dt>가. 크기 무관</dt>
						<dt>나. 영업장 건축물의 용도: 업무시설 또는 제2종근린생활시설만 가능</dt>	
					</dl>					
					<h5 class="title">2. 창고</h5>
					<dl>
						<dt>가. 크기 무관</dt>
						<dt>나. 영업신고증</dt>
						<dt>나. 영업장과 별도의 공간으로 분리 또는 구획해야 함</dt>
					</dl>
					<h5 class="title">3. 필수보유 장비</h5><span class="ftr blue link" onclick="goLaw(3);">[공중위생관리법 시행규칙 별표1]</span>
					<dl>
						<dt>가. 25cm 이상의 마루광택기 2대 이상</dt>
						<dt>나. 진공청소기(집수, 집진) 2대 이상</dt>
						<dt>다. 안전벨트, 안전모, 로프</dt>
						<dt>라. 먼지, 일산화탄소, 이산화탄소 측정장비: 3,000㎡이상의 건축물을 청소하는 경우만 해당</dt>
					</dl>
					<h5 class="title">4. 신규 및 승계자 위생교육 수료증</h5><span class="ftr blue link" onclick="goLaw(1);">[공중위생관리법 제17조 제2항]</span>
					<dl>
						<dt>가. 반드시 영업신고 전 교육 수료</dt>
						<dt>나. 부득이한 경우(천재지변, 본인의 질병ㆍ사고, 업무상 국외출장 등의 사유) 영업신고 후 6개월안에 교육 수료</dt>						
					</dl>
					<h5 class="title">5. 해당 시·군·구청 공중위생담당부서에 영업신고</h5><span class="ftr blue link" onclick="goLaw(1);">[공중위생관리법 시행규칙 제3조]</span>
					<dl>
						<dt>가. 제출서류</dt>
						<dd>- 영업신고서</dd>
						<dd>- 영업시설 및 설비개요서</dd>
						<dd>- 영업시설 및 설비의 사용에 관한 권리를 확보하였음을 증명하는 서류</dd>
    					<dd>- 신규 및 승계자 위생교육 수료증</dd>
    					<dt>나. 일부 지자체는 보건소에서 업무 담당: 신고전 확인 필수</dt>															
					</dl>
				</div>
			</li>
			
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 건물위생관리업 변경신고 ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title">1. 사유</h5> <span class="ftr blue link" onclick="goLaw(3);">[공중위생관리법 시행규칙 제3조의2 제1항]</span>
					<dl>
						<dt>가. 영업소의 명칭 또는 상호가 변경된 경우</dt>
						<dt>나. 영업소의 주소가 변경된 경우</dt>
						<dt>다. 신고한 영업장 면적의 3분의 1이상이 변경된 경우</dt>
						<dt>라. 대표자의 성명 또는 생년월일이 변경된 경우</dt>						
					</dl>					
					<h5 class="title">2. 변경신고 서류</h5> <span class="ftr blue link" onclick="goLaw(3);">[공중위생관리법 시행규칙 제3조의2 제2항]</span>
					<dl>
						<dt>가. 영업신고사항변경신고서</dt>
						<dt>나. 영업신고증</dt>
						<dt>다. 변경사항 증명 서류</dt>
					</dl>
				</div>
			</li>
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 건물위생관리업 폐업신고 ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title ftl">1. 최종 영업신고를 한 시·군·구청에 공중위생영업을 폐업한 날부터 20일 이내에 폐업 신고를 반드시 해야 함</h5> <span class="ftr blue link" onclick="goLaw(1);">[공중위생관리법 제3조 제2항]</span>
							
					<h5 class="title clear">2. 폐업신고 서류</h5> 					
					<dl>
						<dt>가. 영업 폐업신고서</dt>		    				
					</dl>
				</div>
			</li>
			
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 건물위생관리업 지위승계신고 ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title">1. 사유</h5> <span class="ftr blue link" onclick="goLaw(3);">[공중위생관리법 시행규칙 제3조의4]</span>
					<dl>
						<dt>가. 영업을 양수 한 경우</dt>
						<dt>나. 영업을 승계 받은 경우</dt>
					</dl>					
					<h5 class="title">2. 지위승계신고 서류</h5> 					
					<dl>
						<dt>가. 영업자 지위승계신고서</dt>
						<dt>나. 양수: 양도·양수 증명서류 사본</dt>
						<dd>상속: 상속인임을 증명할 수 있는 서류</dd>
					    <dd>기타: 지위승계를 증명할 수 있는 서류</dd>					    				
					</dl>
				</div>
			</li>
			
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 받아야 하는 위생교육 ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title">1. 신규 및 승계자 위생교육</h5> <span class="ftr blue link" onclick="goLaw(1);">[공중위생관리법 제17조제2항]</span>
					<dl>
						<dt>가. 대상: 영업을 하려는 자(영업신고증에 대표자로 등록되어야 하는 자)</dt>
						<dt>나. 사유</dt>
						<dd>1) 최초 영업 신고</dd>
					    <dd>2) 대표자 변경</dd>
					    <dd>3) 영업신고 행정구역 변경(주소 이전)</dd>	
					    <dt>다. 위생교육 면제 <span class="ftr blue link" onclick="goLaw(3);">[공중위생관리법 시행규칙 제23조제7항]</span></dt>
						<dd>1) 위생교육을 받은 날부터 2년 이내에 같은 업종의 영업을 하려는 경우</dd>								
					</dl>
					
					<h5 class="title">2. 공중위생관리 책임자 위생교육</h5> <span class="ftr blue link" onclick="goLaw(1);">[공중위생관리법 제17조제1항]</span>
					
					<dl>
						<dt>가. 대상: 사업현장 책임자(소장, 반장등), 대표자(현장이 없는 경우)</dt>
						<dt>나. 사유</dt>
						<dd>1) 매년</dd>
					    <dd><span class="mgl20">- 유효기간이 1년이 아니라 년도가 바뀌면 받아야 함</span></dd>
					    <dt>다. 위생교육 면제 <span class="ftr blue link" onclick="goLaw(3);">[공중위생관리법 시행규칙 제23조제5항]</span></dt>
						<dd>1) 휴업신고를 한 다음 해부터 영업을 재개하기 전까지</dd>								
					</dl>
				</div>
			</li>
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 건물위생관리업 영업시 반드시 준수해야하는 사항은? ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title">1.반드시 영업신고를 하고 영업을 하여야 함</h5>
					<span class="mgl20">- 위반시 영업장 폐쇄</span> <span class="ftr blue link" onclick="goLaw(3);">[공중위생관리법 시행규칙 별표7]</span>
					<h5 class="title">2. 위생관리 의무를 반드시 준수하여야 함</h5>
					<span class="mgl20">- 위반시 과태료 60만원</span> <span class="ftr blue link" onclick="goLaw(2);">[공중위생관리법 시행령 별표2]</span>
					<h5 class="title">3. 관계공무원의 출입·검사 기타 조치를 거부·방해 또는 기피하지 않아야 함</h5>
					<span class="mgl20">- 위반시 과태료 150만원</span> <span class="ftr blue link" onclick="goLaw(2);">[공중위생관리법 시행령 별표2]</span>
					<h5 class="title">4. 지자체의 개선명령은 반드시 준수하여야 함</h5>
					<span class="mgl20">- 위반시 과태료 150만원</span> <span class="ftr blue link" onclick="goLaw(2);">[공중위생관리법 시행령 별표2]</span>
					<h5 class="title">5. 공중위생관리책임자 위생교육을 영업장별로 매년 받아야 함</h5>
					<span class="mgl20">- 위반시 과태료 60만원</span> <span class="ftr blue link" onclick="goLaw(2);">[공중위생관리법 시행령 별표2]</span>
				</div>
			</li>
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 건물위생관리업 업종코드 ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title">1. 통계청(한국표준산업분류표): N74211(건축물 일반청소업)</h5>
					<h5 class="title">2. 국세청: 749300(건축물 일반청소업)</h5>
					<h5 class="title">3. 조달청(나라장터): 1162(건물위생관리업)</h5>					
				</div>
			</li>
		</ul>
	</div>
	
<!-- end :: content -->
</div>

</body>
</html>