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
	
		var tab = $(".st_2 li a");

		$(".st_2 li a").each(function(n){
			this.n=n;
		}).click(function(){
			tab.removeClass("on").eq(this.n).addClass("on");
			var jicode = tab.eq(this.n).attr("val"); //지회 코드

			goJi(jicode);
		});
	});
		
	function goJi(jicode){
		
		var _url = '/intro/intro.jspx?cmd=org&jicode='+jicode;
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
.qnaWrap ol.st_2 {
    overflow: hidden;
    margin-left: -5px;
}
.qnaWrap ol.st_2 li {
    float: left;
    margin: 0 0 5px 5px;
    border-top: 0px solid #a5a5a5;
}
.qnaWrap ol.st_2 li a span {
    color: #48a3f0;
    padding: 0 10px;
    line-height: 30px;
}
.qnaWrap ol.st_2 li a {
    display: block;
    width: 150px;
    height: 30px;
    text-align: center;
    border: 1px solid #48a3f0;
    color: #48a3f0;
    border-radius: 0;
    min-width: 86px;
}
.qnaWrap ol.st_2 li a.on {
    background: #48a3f0;
}
.qnaWrap ol.st_2 li a.on span {
    color: #fff;
}
</style>
</head>
<body>
<div id="content">
<!-- start :: content -->
	
	<div class="qnaWrap">
		<ul id="qna">
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 회원의 구분 ◆</a>	
				<div class="answer" style="display:block;">
					<h5 class="title">1. 정회원: 건물위생관리업을 신고한 업체로 회비납부의무 등을 준수한 업체</h5>
					<h5 class="title">2. 일반회원: 건물위생관리업을 신고한 업체</h5>
					<h5 class="title">3. 특별회원</h5>
					<dl class="clear">
						<dt>가. 건물위생관리업에 연관되는 영업을 하는 업체</dt>
						<dt>나. 협회 및 건물위생관리업계의 발전에 공헌한 업체</dt>
					</dl>
				</div>
			</li>
			
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 정회원 가입방법 ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title">1. 다음의 구비서류를 관할 지회에 제출</h5>					
					<dl>
						<dt>가. 입회원서 2부<!-- <span class="ftr blue link" onclick="goLaw(3);">[서식 다운로드]</span> --></dt>
						<dt>나. 회원실적현황 2부<!-- <span class="ftr blue link" onclick="goLaw(3);">[서식 다운로드]</span> --></dt>
						<dt>다. 건물위생관리업 영업신고증 사본 2부</dt>
						<dt>라. 사업자등록증 사본 2부</dt>	
					</dl>					
					<h5 class="title">2. 입회비와 당월 월회비를 관할 지회에 납부</h5>
					<dl>
						<dt>※ 특별회원사와 제주소재 업체는 서울지회(중앙회)에 서류제출과 회비를 납부</dt>						
					</dl>
					<dl>
						<dt>※ 관할 지회 주소 및 연락처</dt>						
					</dl>
					
					<ol class="st_2">							
						<li><a href="#" val="0010"><span class="">서울지회</span></a></li>						
						<li><a href="#" val="0020"><span class="">부산.울산.경남지회</span></a></li>						
						<li><a href="#" val="0030"><span class="">대전.세종.충남지회</span></a></li>						
						<li><a href="#" val="0040"><span class="">인천지회</span></a></li>						
						<li><a href="#" val="0050"><span class="">광주.전남지회</span></a></li>						
						<li><a href="#" val="0060"><span class="">전북지회</span></a></li>						
						<li><a href="#" val="0070"><span class="">대구지회</span></a></li>						
						<li><a href="#" val="0080"><span class="">충북지회</span></a></li>						
						<li><a href="#" val="0090"><span class="">제주지회</span></a></li>						
						<li><a href="#" val="00A0"><span class="">경북지회</span></a></li>						
						<li><a href="#" val="00B0"><span class="">경기지회</span></a></li>						
						<li><a href="#" val="00C0"><span class="">강원지회</span></a></li>
					</ol>
					
				</div>
			</li>
			
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 입회비 및 월회비 ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title">1. 입회비: 400,000원(정회원과 특별회원)</h5>
					<h5 class="title">2. 월회비: 회원 실적평수를 기준으로 3,306㎡(1,000평)당 1,000원</h5>
					<dl>
						<dt>가. 하한선: 99,174㎡(30,000평) - 30,000원</dt>
						<dt>나. 상한선: 991,736㎡(300,000평) - 300,000원</dt>
						<dt>다. 특별회원: 50,000원 정액</dt>					
					</dl>
				</div>
			</li>
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 정회원의 권리 ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title ">1. 협회의 선거권 및 피선거권</h5>							
					<h5 class="title ">2. 협회 시설의 이용권 및 각종 자료의 열람권</h5> 
					<h5 class="title ">3. 회무에 관한 건의 및 청원권</h5>					
					<dl>
						<dt>※ 특별회원사는 1번의 권리는 제외 2, 3번만 해당</dt>		    				
					</dl>
				</div>
			</li>
			
			<li>	
				<a href="#" class="tit" id="qna_33">◆ 정회원 혜택 ◆</a>	
				<div class="answer" style="display: none;">
					<h5 class="title">1. 협회 소속 전국 회원명부 제공</h5>
					<h5 class="title">2. 협회 회원증 제공(매년 갱신)</h5>
					<h5 class="title">3. 협회 주관 행사 우선 초청</h5>					
					<dl>
						<dt>가. 협회 정기총회</dt>
						<dt>나. 협회 창립기념행사</dt>
						<dt>다. 각종 전문 세미나</dt>
						<dt>라. 국제 청소위생산업전</dt>
						<dt>마. 전국 건물관리 기능경진대회</dt>
					</dl>
					<h5 class="title">4. 국제 건물관리협회 주관 대회/ 전시회 참석</h5>					
					<dl>
						<dt>가. 세계건물위생관리연맹(WFBSC) 세계대회 참관(여행비 자체 부담)</dt>
						<dt>나. 아시아건물관리연맹(ABMA) 아시아대회 참관(여행비 자체 부담)</dt>
						<dt>다. 기타 세계 유명 청소관련 전시회 참관단 참여 가능</dt>						
					</dl>
					<h5 class="title">5. 협회 고문변호사, 노무사, 세무사 상담자격 부여</h5>		
					<h5 class="title">6. 협회 청소용역업체 추천의뢰시 우선 추천</h5>		
					<h5 class="title">7. 각종 표창 우선 추천</h5>	
					<dl>
						<dt>가. 대통령표창</dt>
						<dt>나. 보건복지부장관 표창</dt>
						<dt>다. 서울시장표창</dt>
						<dt>라. 직능경제인단체총연합회장 표창</dt>
						<dt>마. 건축물관리연합회장 표창</dt>
						<dt>바. 건물위생관리협회장 표창</dt>
					</dl>
					<h5 class="title">8. 협회 홈페이지 정회원 전용 자료 이용가능</h5>	
					<h5 class="title">9. 건물위생관리업 관련 각종 자료 제공 및 이용가능</h5>	
					<h5 class="title">10. 청소장비업체(협회 특별회원사)를 통한 정회원특별 할인가 적용(추진중)</h5>	
				</div>
			</li>
		</ul>
	</div>
	
<!-- end :: content -->
</div>

</body>
</html>