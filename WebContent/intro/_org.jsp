<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>    
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" type="text/javascript">
//<![CDATA[	
	jQuery(document).ready(function(){
		$(".originBox").hide();
		$("#ji_intro_layer").hide();
		
		//역대 회장 클릭 전환
		$(".ob_vip").click(function(event) {
			$("#ob_vip_list").attr("src","/static/main/img/org/ob_vip/" + this.id);
		    return false;
		});

		//역대임원 전환(m03_1_co.jpg)
		$(".ob_executives").click(function(event) {
			$("#ob_executives_list").attr("src","/static/main/img/org/ob_executives/" + this.id);
		    return false;
		});
		
		
		var jicode = '${input.jicode}';
		
		if(jicode != ''){
			onClickSub('6010', jicode);
		}

	});
 

	function onClickSub(cp_code,ji_code){
		$(".originBox").hide();
		
		var _url = "/intro/action/intro_org.jspx?cmd=getSubInfo&cp_code=" + cp_code +"&ji_code=" + ji_code;	

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'text',
	   		data : "", 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(Data)
			{		
				$("#ji_intro_layer").html(Data);
				$("#ji_intro_layer").show();
			}			
		} );
	}

	//위원회 클릭
	function onClickOrgGroup(){
		$(".originBox").show();
		$("#ji_intro_layer").hide();		
	}


//]]>
</script>
	<style>
		.origin_content {
			font-family: 'nanum_square' !important;
			font-weight: 600;
		}
		.exChoose{
			overflow: hidden;
			padding-left: 18px;
			margin-top: 10px;
		}

		.orgTitle{
			text-align: right;
			margin-right: 10px;
		}

		table.orgMargin{
			margin-top: 0px!important;
		}
	</style>
</head>

<body>


		<div id="content">
		<!-- start :: content -->
		<!-- start :: content -->


			<div class="origin_top"></div>

			<div class="origin_content">
				<div class="originImg">
					<img src="/static/main/img/sub/org_img.png" style="width:970px" alt="" usemap="#ji_map" />

					<map name="ji_map">
						<area shape="rect" coords="90,169, 217, 184" href="javascript:onClickSub('6010','0010');" 		target="_self" outline="none">
						<area shape="rect" coords="90,193, 217, 209" href="javascript:onClickSub('6010','0020');" 	target="_self" outline="none">
						<area shape="rect" coords="90,219, 217, 235" href="javascript:onClickSub('6010','0030');" 		target="_self" outline="none">
						<area shape="rect" coords="90,243, 217, 261" href="javascript:onClickSub('6010','0040');"			target="_self" outline="none">
						<area shape="rect" coords="90,269, 217, 287" href="javascript:onClickSub('6010','0050');" 		target="_self" outline="none">
						<area shape="rect" coords="90,295, 217, 313" href="javascript:onClickSub('6010','0060');" 		target="_self" outline="none">
						<area shape="rect" coords="90,320, 217, 338" href="javascript:onClickSub('6010','0070');" 		target="_self" outline="none">
						<area shape="rect" coords="90,345, 217, 360" href="javascript:onClickSub('6010','0080');" 		target="_self" outline="none">
						<area shape="rect" coords="90,370, 217, 385" href="javascript:onClickSub('6010','0090');" 		target="_self" outline="none">

						<area shape="rect" coords="90,395, 217, 410" href="javascript:onClickSub('6010','00A0');" 		target="_self" outline="none">
						<area shape="rect" coords="90,420, 217, 435" href="javascript:onClickSub('6010','00B0');" 		target="_self" outline="none">
						<area shape="rect" coords="90,445, 217, 460" href="javascript:onClickSub('6010','00C0');"			target="_self" outline="none">

						<!--위원회 -->
						<area shape="rect" coords="689, 339, 798, 645" href="javascript:onClickOrgGroup();" target="_self" outline="none">
				
					</map>
				</div>



				<div id="ji_intro_layer">

					<div class="loc_title">서울지회</div>	

					<div class="loc_wrap">
						<div class="ico1">지회장소개</div>
						<div class="dummy" style="height:20px;"></div>
						<div class="loc_con">
							<div class="loc_pic"><img src="/common/action/attach.jspx?cmd=doDownload&file_no=18" width="68" height="75" ></div>
							<div class="loc_txt">
								<div class="boss">
									<span class="bossNm">송문현</span>
									<span class="bossLc">서울지회장</span>
								</div>
								
								<div class="ico2">지회장인사말</div>
								<div class="comment">본 협회는 새시대에 대응할 책무와 소명이 그 어느때 보다도 커지고 있음을 인식하고 장기적 전망에서 새로운 계획과 과감한 사업추진으로 협회는 물론 업계의 발전에 최선을 다할 것을 다짐합니다.</div>
								
							</div>
						</div>
					</div>

					<div class="dummy" style="height:15px;"></div>
					<div class="loc_detail">
						<table>
						<tbody>					
							<tr>
								<th class="ico5">사무총장</th>
								<td class="offName">이재훈</td>
							</tr>
							<tr>
								<th class="ico4">상세정보</th>
								<td>
									우 : 04783-<br>
									서울 성동구 성수이로20길 10602호<br> 
									TEL : 02)465-5900<br>
									FAX : 02)465-5244<br>
									<a href="mailto:kabm@naver.com">email: kabm@naver.com</a>
									&nbsp;
								</td>
							</tr>
						</tbody>	
						</table>			
					</div>
					
					<div style="clear:both;"></div>
				</div>
				
				<ul class="originBox">
					<li>
						<strong>법사위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							협회 정관 및 제 규정의 검토, 개정, 제안을 하는 기구
						</div>
						<p>
							위원장 : 오성민<br />
							위원 : 이서준, 나혁, 김성환, 박광수
						</p>
					</li>
					<li>
						<strong>정책위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							업계의 제반 정책적 사안에 대하여 대정부 정책건의, 관련법규 개정추진 등을 연구 검토하는 기구
						</div>
						<p>
							위원장 : 박주왕<br />
							위원 : 김상현, 이대화, 박광수, 김성환
						</p>
					</li>
					<li>
						<strong>안전보건위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							산업안전, 재해예방 등의 활동과 지원을 하는 기구
						</div>
						<p>
							위원장 : 최해진<br />
							위원 : 이서준, 진영산, 김상현, 김형출
						</p>
					</li>
					<li>
						<strong>조정위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							업계 발전을 위해 회원 상호간 발생되는 분규 등을 조정 심의하며, 정관 제6조에 의거 징계 사유대상에 관한 심의 결정을 하는 기구
						</div>
						<p>
							위원장 : 도태운<br />
							위원 : 김영선, 김영수, 정일형, 박규준
						</p>
					</li>
					<li>
						<strong>국제위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							본회가 한국을 대표하여 가입된 세계빌딩관리협회와의 유기적 협력과 국제적 교류를 통해 각종 정보교환 및 신기술 도입을 목적으로 하는 기구
						</div>
						<p>
							위원장 : 이현종<br />
							위원 : 김태현, 이정호, 김정숙, 장세환
						</p>
					</li>
					<li>
						<strong>교육위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							법정교육과 위생관리 향상을 위한 노하우를 연구 개발하며, 신기술 전수를 위한 각종 세미나	개최 등을 전담하는 기구
						</div>
						<p>
							위원장 : 이영호<br />
							위원 : 이종택, 신황철, 안현민, 전문수
						</p>
					</li>
					<li>
						<strong>홍보위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							협회의 비전, 설립취지, 목적, 주요 사업 내용들을 대내외적으로 홍보하는 기구
						</div>
						<p>
							위원장 : 김득환<br />
							위원 : 신황철, 성정헌, 정재현, 김정숙
						</p>
					</li>
					<li>
						<strong>청년위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							건물위생관리업에 관련된 청년 취업 및 창업을 지원하기 위한 프로그램 운영과 정부 및 지자체의 관련 사업 연계를 목적으로 하는 기구
						</div>
						<p>
							위원장 : 오주환<br />
							위원 : 김태현, 나혁, 박규준, 곽은주
						</p>
					</li>
					<li>
						<strong>환경위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							친환경 서비스 향상을 위한 환경신기술의 개발 및 전문인력 양성을 목적으로 하는 기구
						</div>
						<p>
							위원장 : 이승열<br />
							위원 : 성정헌, 장세환, 안현민, 정재현
						</p>
					</li>
					<li>
						<strong>지방위원회</strong>
						<div class="stit" style="min-height: 15px!important;">
							각 지회간의 업무 교류 및 지회별 회원사들의 건의사항 등을 본회에 전달하는 기구
						</div>
						<p>
							위원장 : 이서준<br />
							위원 : 진영산, 이정호, 정일형, 김영수, 이종택, 임헌표, 전문수, 김형출, 김상현, <br>이대화
						</p>
					</li>
					<li style="width: 97%;">
						<strong>자문교수단</strong>
						<div class="stit">
							건물위생관리법 제정 추진에 따른 논리개발과 자문, 공청회의 효율정 논의 진행을 위한 자문기구
						</div>
						<p>
							자문교수 : 오중근 건국대 학장,김일효 극동대 초빙교수, 유한 극동대 교수, 문경환 고려대 교수, 김판기 용인대 교수, 백민호 강원대 교수,추신철 장안대 교수
						</p>
					</li>
				</ul>
				



				<div class="tabTit">
					<strong>임원소개</strong>
				</div>

				
				<ul class="contTabs2" id="originTab">
					<li><a href="#" class="on">현임원</a></li>
					<li><a href="#">역대회장</a></li>
					<li><a href="#">역대임원</a></li>
				</ul>
				

				<div class="originTabCont">

				

					<table class="" summary="" >
						<colgroup>
							<col width="150"/><col width="100"/>
							<col width="150"/><col width="150"/>
							<col width="150"/><col width=""/>
						</colgroup>
						<thead>
							<tr>
								<th scope="col">직위</th>
								<th scope="col">사진</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
								<th scope="col">연락처</th>
								<th scope="col">주소</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach begin="0" end="${output_count}" step="1" var="index">
							<tr>
								<td>${output[index].duty_nm}
								<c:if test="${fn:length(output[index].pos_nm) > 0}">
								<br>(${output[index].pos_nm})
								</c:if>
								</td>
								<td><c:if test="${fn:length(output[index].file_no) > 0}">									
										<img src="${input.thumb_url}/${output[index].file_path}/${output[index].file_nm}" width="68" height="75" alt="" />
									</c:if>
								</td>
								<td>${output[index].emp_nm}</td>
								<td>${output[index].cmpny_nm}</td>
								<td>(T) ${output[index].tel_no}<br />(F) ${output[index].fax_no}</td>
								<td>${output[index].addr}</td>
							</tr>
						</c:forEach>							
						</tbody>
					</table>
				</div>
				
				
				<div class="originTabCont" style="display:none">
					<div class="obChoose">
						<span><a class="ob_vip" id="ob_vip_01" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />역대회장</a></span>
						<span><a class="ob_vip" id="ob_vip_02" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />고문단</a></span>
						<span><a class="ob_vip" id="ob_vip_03" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />원로위원</a></span>
						<span><a class="ob_vip" id="ob_vip_04" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />자문위원</a></span>
					</div>


					<div class="obChooseCont" style="display:block">
						<div class="orgTitle">[역대회장]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">임기</th>
								<th scope="col">사진</th>
								<th scope="col">성명</th>
								<th scope="col">기간</th>
								<th scope="col">회사명</th>
							</tr>
							</thead>
							<tbody>
								<tr>
									<td>초대</td>
									<td></td>
									<td>오태영</td>
									<td>1971년11월07일 ~ <br> 1974년 11월 06일</td>
									<td>우지기업(주)</td>
								</tr>
								<tr>
									<td>2.3.4대</td>
									<td></td>
									<td>최용환</td>
									<td>1974년11월07일 ~ <br> 1978년 12월 31일</td>
									<td>용진건설(주)</td>
								</tr>
								<tr>
									<td>5대</td>
									<td></td>
									<td>이의복</td>
									<td>1979년1월1일 ~ <br> 1979년 12월 31일</td>
									<td>유정기업(주)</td>
								</tr>
								<tr>
									<td>6대</td>
									<td></td>
									<td>심맹섭</td>
									<td>1980년1월1일 ~ <br> 1981년 12월 31일</td>
									<td>태가실업(주)</td>
								</tr>
								<tr>
									<td>7대</td>
									<td></td>
									<td>김명진</td>
									<td>1982년1월1일 ~ <br> 1984년 11월 09일</td>
									<td>우지기업(주)</td>
								</tr>
								<tr>
									<td>8.9대</td>
									<td><img src="/static/main/img/org/ob_vip/vip_8.jpg" width="68" height="75" alt="" /></td>
									<td>이경구</td>
									<td>1984년11월10일 ~ <br> 1990년02월23일</td>
									<td>대경성업(주)</td>
								</tr>
								<tr>
									<td>10대</td>
									<td><img src="/static/main/img/org/ob_vip/vip_10.jpg" width="68" height="75" alt="" /></td>
									<td>최종만</td>
									<td>1990년2월24일 ~ <br> 1993년02월24일</td>
									<td>(주)고암</td>
								</tr>
								<tr>
									<td>11대</td>
									<td><img src="/static/main/img/org/ob_vip/vip_11.jpg" width="68" height="75" alt="" /></td>
									<td>정덕균</td>
									<td>1993년2월25일 ~ <br> 1995년03월03일</td>
									<td>진양메인티넌스(주)</td>
								</tr>
								<tr>
									<td>12대</td>
									<td><img src="/static/main/img/org/ob_vip/vip_12.jpg" width="68" height="75" alt="" /></td>
									<td>김준언</td>
									<td>1995년3월04일 ~ <br> 1998년02월26일</td>
									<td>(주)동우유니온</td>
								</tr>
								<tr>
									<td>13.14대</td>
									<td><img src="/static/main/img/org/ob_vip/vip_13.jpg" width="68" height="75" alt="" /></td>
									<td>김만업</td>
									<td>1998년2월27일 ~ <br> 2002년02월23일</td>
									<td>유창물산(주)</td>
								</tr>
								<tr>
									<td>15.16대</td>
									<td><img src="/static/main/img/org/ob_vip/vip_15.jpg" width="68" height="75" alt="" /></td>
									<td>김유기</td>
									<td>2002년2월24일 ~ <br> 2007년03월27일</td>
									<td>(주)동도시스템</td>
								</tr>
								<tr>
									<td>17.18대</td>
									<td><img src="/static/main/img/org/ob_vip/vip_17.jpg" width="68" height="75" alt="" /></td>
									<td>엄영회</td>
									<td>2007년3월28일 ~ <br> 2013년03월20일</td>
									<td>영진비엠에스(주)</td>
								</tr>
								<tr>
									<td>19.20대</td>
									<td><img src="/static/main/img/org/ob_vip/head_140.jpg" width="68" height="75" alt="" /></td>
									<td>송문현</td>
									<td>2013년3월21일 ~ <br> 2019년03월20일</td>
									<td>우지기업(주)</td>
								</tr>
								<tr>
									<td>21대</td>
									<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
									<td>강규익</td>
									<td>2019년3월21일 ~ <br> 2022년03월16일</td>
									<td>(주)한미기술</td>
								</tr>
							</tbody>
						</table>
					</div>

					<div class="obChooseCont" style="display:none">
						<div class="orgTitle">[고문단]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">임기</th>
								<th scope="col">사진</th>
								<th scope="col">성명</th>
								<th scope="col">회사명 및 연락처</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>8.9대 회장</td>
								<td><img src="/static/main/img/org/ob_vip/vip_8.jpg" width="68" height="75" alt="" /></td>
								<td>이경구</td>
								<td>대경성업(주)</td>
							</tr>
							<tr>
								<td>10대 회장</td>
								<td><img src="/static/main/img/org/ob_vip/vip_10.jpg" width="68" height="75" alt="" /></td>
								<td>최종만</td>
								<td>(주)고암 <br> 서울특별시 영등포구 당산로47길 19(고암빌딩 7층) <br> T)326-3011 <br> F)326-3010</td>
							</tr>
							<tr>
								<td>11대 회장</td>
								<td><img src="/static/main/img/org/ob_vip/vip_11.jpg" width="68" height="75" alt="" /></td>
								<td>정덕균</td>
								<td>진양메인티넌스(주) <br> 서울특별시 서초구 반포대로 59 (선흥빌딩 4층) <br> T)3019-6114 <br> F)522-3615</td>
							</tr>
							<tr>
								<td>12대 회장</td>
								<td><img src="/static/main/img/org/ob_vip/vip_12.jpg" width="68" height="75" alt="" /></td>
								<td>김준언</td>
								<td>(주)동우유니온 <br> 서울특별시 강남구 논현로168길 24 <br> (글로리아동우빌딩 6층) <br> T)545-2355 <br> F)545-9403</td>
							</tr>
							<tr>
								<td>13.14대 회장</td>
								<td><img src="/static/main/img/org/ob_vip/vip_13.jpg" width="68" height="75" alt="" /></td>
								<td>김만업</td>
								<td>유창물산(주)<br> 서울특별시 송파구 백제고분로46길 24 <br> T)412-4933 <br> F)415-4462</td>
							</tr>
							<tr>
								<td>15.16대 회장</td>
								<td><img src="/static/main/img/org/ob_vip/vip_15.jpg" width="68" height="75" alt="" /></td>
								<td>김유기</td>
								<td>(주)동도시스템 <br> 서울특별시 중구 을지로14길8,203호(을지빌딩) <br> T)2277-0070 <br> F)2268-0023</td>
							</tr>
							<tr>
								<td>17.18대 회장</td>
								<td><img src="/static/main/img/org/ob_vip/vip_17.jpg" width="68" height="75" alt="" /></td>
								<td>엄영회</td>
								<td>영진비엠에스(주) <br> 서울특별시 종로구 돈화문로 11길 29, 낙원오피스텔 701호(돈의동)  <br> T)02-763-1038 <br> F)02-762-1863</td>
							</tr>
							<tr>
								<td>19.20대 회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_140.jpg" width="68" height="75" alt="" /></td>
								<td>송문현</td>
								<td>우지기업(주) <br> 서울특별시 종로구 사직로 10길 17, 2층(내자동, 인왕빌딩)  <br> T)02-737-8822 <br> F)02-737-7027</td>
							</tr>
							<tr>
								<td>21대 회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>강규익</td>
								<td>(주)한미기술 <br> 서울특별시 강남구 학동로44길 6, 203호(논현동, 모아빌딩) <br> T)02-548-3888 <br> F)02-542-3880</td>
							</tr>
							<tr>
								<td>전무이사(명예고문)</td>
								<td><img src="/static/main/img/org/ob_vip/head_199.jpg" width="68" height="75" alt="" /></td>
								<td>김길문</td>
								<td>T)02-2293-9613</td>
							</tr>
							</tbody>
						</table>

					</div>

					<div class="obChooseCont" style="display:none">
						<div class="orgTitle">[원로위원]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
								<th scope="col">연락처</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td>안석순</td>
								<td>보흥실업(주)</td>
								<td>T) 02-761-0631 <br> F) 02-6935-1019</td>
							</tr>
							<tr>
								<td>2</td>
								<td>탁재용</td>
								<td>흥안실업(주)</td>
								<td>T) 02-849-2591 <br> F) 02-849-2590</td>
							</tr>
							<tr>
								<td>3</td>
								<td>김평수</td>
								<td>(주)와이즈비전</td>
								<td>T) 02-599-0425 <br> F) 02-599-0453</td>
							</tr>
							<tr>
								<td>4</td>
								<td>전승일</td>
								<td>승진시스템(주)</td>
								<td>T) 02-955-1335 <br> F) 02-955-1378</td>
							</tr>
							<tr>
								<td>5</td>
								<td>김건치</td>
								<td>(주)동방디에스시스템</td>
								<td>T) 02-794-1674 <br> F) 02-792-9706</td>
							</tr>
							<tr>
								<td>6</td>
								<td>허 증</td>
								<td>경원산업관리(주)</td>
								<td>T) 02-836-4381 <br> F) 02-836-4389</td>
							</tr>
							<tr>
								<td>7</td>
								<td>김시태</td>
								<td>(주)보경실업</td>
								<td>T) 02-718-4221 <br> F) 02-718-4223</td>
							</tr>
							</tbody>
						</table>

					</div>

					<div class="obChooseCont" style="display:none">
						<div class="orgTitle">[자문위원]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
								<th scope="col">연락처</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td>황승모</td>
								<td>(주)한국보안</td>
								<td>T) 051-555-0113 <br> F) 051-554-0113</td>
							</tr>
							<tr>
								<td>2</td>
								<td>이정만</td>
								<td>(주)대한안전관리공사</td>
								<td>T) 02-716-1343 <br> F) 02-716-1336</td>
							</tr>
							<tr>
								<td>3</td>
								<td>박상앙</td>
								<td>비티엠써비스(주)</td>
								<td>T) 02-3483-2000<br> F) 02-3476-8988</td>
							</tr>
							<tr>
								<td>4</td>
								<td>전용수</td>
								<td>신우산업관리(주)</td>
								<td>T) 02-587-7691 <br> F) 02-587-7690</td>
							</tr>
							<tr>
								<td>5</td>
								<td>조남각</td>
								<td>(주)백제기업</td>
								<td>T) 02-718-8855 <br> F) 02-716-9514</td>
							</tr>
							<tr>
								<td>6</td>
								<td>진인은</td>
								<td>(사)진은종합개발</td>
								<td>T) 02-2631-3954<br> F) 02-2631-3964</td>
							</tr>
							<tr>
								<td>7</td>
								<td>신양주</td>
								<td>(주)프로에스콤</td>
								<td>T) 02-2183-0100 <br> F) 02-2183-0140</td>
							</tr>
							</tbody>
						</table>
					</div>
				</div>


				<div class="originTabCont" style="display:none">
					<div class="exChoose">
						<span><a class="ob_executives" id="m03_1_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />8대</a></span>
						<span><a class="ob_executives" id="m03_2_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />9대</a></span>
						<span><a class="ob_executives" id="m03_3_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />10대</a></span>
						<span><a class="ob_executives" id="m03_4_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />11대</a></span>
						<span><a class="ob_executives" id="m03_5_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />12대</a></span>
						<span><a class="ob_executives" id="m03_6_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />13대</a></span>
						<span><a class="ob_executives" id="m03_7_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />14대</a></span>
						<span><a class="ob_executives" id="m03_8_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />15대</a></span>
						<span><a class="ob_executives" id="m03_9_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />16대</a></span>
						<span><a class="ob_executives" id="m03_10_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />17대</a></span>
						<span><a class="ob_executives" id="m03_18_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />18대</a></span>
						<span><a class="ob_executives" id="m03_19_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />19대</a></span>
						<span><a class="ob_executives" id="m03_20_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />20대</a></span>
						<span><a class="ob_executives" id="m03_21_co.jpg" href="#"><img src="/static/main/img/org/bullet_m.gif" alt="" />21대</a></span>

					</div>

					<div class="exChooseCont" style="display:block">
						<div class="orgTitle">[8대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
								<th scope="col">임기</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td>회장</td>
								<td>이경구</td>
								<td>경일공사</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>2</td>
								<td>부회장</td>
								<td>구천서</td>
								<td>신천개발</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>3</td>
								<td>부회장</td>
								<td>민영철</td>
								<td>(주)서울공신</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>4</td>
								<td>감사</td>
								<td>정덕균</td>
								<td>진양흥업공사</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>5</td>
								<td>이사</td>
								<td>진병조</td>
								<td>삼진성흥(주)</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>6</td>
								<td>이사</td>
								<td>심맹섭</td>
								<td>태가실업</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>7</td>
								<td>이사</td>
								<td>김명진</td>
								<td>세왕진흥기업(주)</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>8</td>
								<td>이사</td>
								<td>송재원</td>
								<td>우지기업(주)</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>9</td>
								<td>이사</td>
								<td>장인수</td>
								<td>삼호실업(주)</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>10</td>
								<td>이사</td>
								<td>신학균</td>
								<td>동방흥산(주)</td>
								<td>1986.8.4 ~ 1986.11.8</td>
							</tr>
							<tr>
								<td>11</td>
								<td>전무 이사</td>
								<td>김길문</td>
								<td>(주)용진용역</td>
								<td>1984.11.8 ~ 1986.11.8</td>
							</tr>
							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[9대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
								<th scope="col">임기</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td>회장</td>
								<td>이경구</td>
								<td>대경성업(주)</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>2</td>
								<td>부회장</td>
								<td>김인구</td>
								<td>(주)보경 실업</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>3</td>
								<td>부회장</td>
								<td>최종만</td>
								<td>(주)고암</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>4</td>
								<td>감사</td>
								<td>민영철</td>
								<td>(주)서울 공신</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>5</td>
								<td>감사</td>
								<td>안석순</td>
								<td>보흥 실업(주)</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>6</td>
								<td>이사</td>
								<td>구천서</td>
								<td>신천 개발(주)</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>7</td>
								<td>이사</td>
								<td>진병조</td>
								<td>삼진 성흥(주)</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>8</td>
								<td>이사</td>
								<td>송재원</td>
								<td>태가 실업(주)</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>9</td>
								<td>이사</td>
								<td>장인수</td>
								<td>우지 기업(주)</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>10</td>
								<td>이사</td>
								<td>백옥동</td>
								<td>영정 산업(주)</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>11</td>
								<td>이사</td>
								<td>유남열</td>
								<td>부산.경남 지회</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>12</td>
								<td>이사</td>
								<td>김태형</td>
								<td>대전.충청 지회</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>13</td>
								<td>이사</td>
								<td>김영진</td>
								<td>세왕진흥기업(주)</td>
								<td>1986.11.8 ~ 1988.12.31</td>
							</tr>
							<tr>
								<td>14</td>
								<td>이사</td>
								<td>박덕술</td>
								<td>한국 실업(주)</td>
								<td>1988.12.31 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>15</td>
								<td>이사</td>
								<td>신학균</td>
								<td>동방 흥산(주)</td>
								<td>1986.11.8 ~ 1987.10.5</td>
							</tr>
							<tr>
								<td>16</td>
								<td>이사</td>
								<td>김건치</td>
								<td>동방 흥산(주)</td>
								<td>1987.10.5 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>17</td>
								<td>전무 이사</td>
								<td>김길문</td>
								<td>(주)용진 용역</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[10대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="220"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
								<th scope="col">임기</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td>고문</td>
								<td>이경구</td>
								<td>8대,9대 회장</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>2</td>
								<td>고문</td>
								<td>김용래</td>
								<td>(사)서울시 환경미화원후원회 회장</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>3</td>
								<td>고문</td>
								<td>정준혁</td>
								<td>(사)한국 경비협회 회장</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>4</td>
								<td>고문</td>
								<td>정희병</td>
								<td>(사)한국 방역협회 회장</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>5</td>
								<td>고문</td>
								<td>최두형</td>
								<td>(사)한국 건축물 유지관리 협회 회장</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>6</td>
								<td>고문</td>
								<td>이상정</td>
								<td>(사)한국 공동주택관리 협회 회장</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>7</td>
								<td>자문 위원</td>
								<td>공대식</td>
								<td>명지대학교 교수</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>8</td>
								<td>자문 위원</td>
								<td>김석호</td>
								<td>일본협회 상담역</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>9</td>
								<td>자문 위원</td>
								<td>이용운</td>
								<td>환경관리 연합</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>10</td>
								<td>자문 위원</td>
								<td>김윤신</td>
								<td>한양대학교 교수</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>11</td>
								<td>자문 위원</td>
								<td>송순태</td>
								<td>빌딩관리 연구소 소장</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>12</td>
								<td>회장</td>
								<td>최종만</td>
								<td>(주)고암</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>13</td>
								<td>수석 부회장</td>
								<td>김인구</td>
								<td>(주)보경실업</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>14</td>
								<td>총무 부회장</td>
								<td>안석순</td>
								<td>보흥실업(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>15</td>
								<td>국제 부회장</td>
								<td>김창열</td>
								<td>세왕진흥기업(주)</td>
								<td>1986.11.8 ~ 1988.12.31</td>
							</tr>
							<tr>
								<td>16</td>
								<td>교육 부회장</td>
								<td>문상학</td>
								<td>신천개발(주)</td>
								<td>1988.12.31 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>17</td>
								<td>지방 부회장</td>
								<td>정덕균</td>
								<td>진양메인티넌스(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>18</td>
								<td>감사</td>
								<td>박덕슬</td>
								<td>(주)한국실업</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>19</td>
								<td>감사</td>
								<td>김건치</td>
								<td>(주)동방흥산</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>20</td>
								<td>이사</td>
								<td>임종관</td>
								<td>(주)성림기술산업</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>21</td>
								<td>이사</td>
								<td>구천서</td>
								<td>신천개발(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>22</td>
								<td>이사</td>
								<td>진병조</td>
								<td>삼진성흥(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>23</td>
								<td>이사</td>
								<td>심맹섭</td>
								<td>태가실업(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>24</td>
								<td>이사</td>
								<td>송재원</td>
								<td>우지기업(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>25</td>
								<td>이사</td>
								<td>장인수</td>
								<td>삼호실업(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>26</td>
								<td>이사</td>
								<td>백옥동</td>
								<td>영정산업(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>27</td>
								<td>이사</td>
								<td>박기채</td>
								<td>삼호기연(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>28</td>
								<td>이사</td>
								<td>김유기</td>
								<td>(주)동도시스템</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>29</td>
								<td>이사</td>
								<td>주영의</td>
								<td>수도용역(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>30</td>
								<td>이사</td>
								<td>이상훈</td>
								<td>(주)건도</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>31</td>
								<td>이사</td>
								<td>최기호</td>
								<td>용진건설(주)</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>32</td>
								<td>이사</td>
								<td>유남열</td>
								<td>부산.경남 지회</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>33</td>
								<td>이사</td>
								<td>이세호</td>
								<td>부산.경남 지회</td>
								<td>1992.4.20 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>34</td>
								<td>이사</td>
								<td>김태형</td>
								<td>대전.충남 지회</td>
								<td>1986.11.8 ~ 1990.2.23</td>
							</tr>
							<tr>
								<td>35</td>
								<td>이사</td>
								<td>신현천</td>
								<td>대전.충남 지회</td>
								<td>1992.5.21 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>36</td>
								<td>이사</td>
								<td>손석조</td>
								<td>인천지회</td>
								<td>1990.7.8 ~ 1991.11.31</td>
							</tr>
							<tr>
								<td>37</td>
								<td>이사</td>
								<td>고광조</td>
								<td>인천지회</td>
								<td>1991.11.31 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>38</td>
								<td>이사</td>
								<td>이용철</td>
								<td>광주.전남 지회</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>39</td>
								<td>이사</td>
								<td>전창수</td>
								<td>전북지회</td>
								<td>1990.12.31 ~ 1991.10.5</td>
							</tr>
							<tr>
								<td>40</td>
								<td>이사</td>
								<td>이성노</td>
								<td>전북지회</td>
								<td>1991.10.5 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>41</td>
								<td>이사</td>
								<td>김인석</td>
								<td>대구.경북 지회</td>
								<td>1991.7.24 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>42</td>
								<td>전무 이사</td>
								<td>김길문</td>
								<td>(주)용진용역</td>
								<td>1990.2.23 ~ 1993.2.24</td>
							</tr>









							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[11대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="220"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
								<th scope="col">임기</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td>고문</td>
								<td>이경구</td>
								<td>8대,9대 회장</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>2</td>
								<td>고문</td>
								<td>김용래</td>
								<td>(사)서울시 환경미화원후원회 회장</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>3</td>
								<td>자문위원</td>
								<td>김석호</td>
								<td>일본협회 상담역</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>4</td>
								<td>자문위원</td>
								<td>김윤신</td>
								<td>한양대학교 교수</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>5</td>
								<td>상임고문</td>
								<td>최종만</td>
								<td>10대 회장</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr><tr>
								<td>6</td>
								<td>회장</td>
								<td>정덕균</td>
								<td>진양메인티넌스(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>

							<tr>
								<td>7</td>
								<td>총무부회장</td>
								<td>이종구</td>
								<td>(주)거산상사</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>8</td>
								<td>국제부회장</td>
								<td>김건치</td>
								<td>동방흥산(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>9</td>
								<td>사업부회장</td>
								<td>임종관</td>
								<td>(주)성림기술산업</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>10</td>
								<td>고육부회장</td>
								<td>하만구</td>
								<td>(주)순일기업</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>11</td>
								<td>정책부회장</td>
								<td>김준언</td>
								<td>동우종합용역(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>12</td>
								<td>지방부회장</td>
								<td>김태형</td>
								<td>삼흥기업(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>13</td>
								<td>감사</td>
								<td>탁재용</td>
								<td>흥안실업(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>14</td>
								<td>감사</td>
								<td>이상훈</td>
								<td>(주)건도</td>
								<td>1993.2.24 ~ 1993.12.31</td>
							</tr>
							<tr>
								<td>15</td>
								<td>이사</td>
								<td>장인수</td>
								<td>삼호실업(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>16</td>
								<td>이사</td>
								<td>박덕술</td>
								<td>한국실업(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>17</td>
								<td>이사</td>
								<td>박기채</td>
								<td>삼호기연(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>18</td>
								<td>이사</td>
								<td>김유기</td>
								<td>(주)동도시스템</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>19</td>
								<td>이사</td>
								<td>주영의</td>
								<td>수도용역(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>20</td>
								<td>이사</td>
								<td>최기호</td>
								<td>용진건설(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>21</td>
								<td>이사</td>
								<td>홍성돈</td>
								<td>선흥개발(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>22</td>
								<td>이사</td>
								<td>맹준호</td>
								<td>태광진흥(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>23</td>
								<td>이사</td>
								<td>김시태</td>
								<td>(주)보경실업</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>24</td>
								<td>이사</td>
								<td>한원덕</td>
								<td>(주)한덕엔지리어링</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>25</td>
								<td>이사</td>
								<td>지장용</td>
								<td>미림개발(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>26</td>
								<td>이사</td>
								<td>홍방희</td>
								<td>(주)현대환경공사</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>27</td>
								<td>이사</td>
								<td>전석택</td>
								<td>삼진성흥(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>28</td>
								<td>이사</td>
								<td>엄영회</td>
								<td>영진비엠에스(주)</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>29</td>
								<td>이사</td>
								<td>이세호</td>
								<td>부산.경남 지회</td>
								<td>1993.2.24 ~ 1994.4.8</td>
							</tr>
							<tr>
								<td>30</td>
								<td>이사</td>
								<td>박덕술</td>
								<td>부산.경남 지회</td>
								<td>1994.4.8 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>31</td>
								<td>이사</td>
								<td>김태형</td>
								<td>대전.충청 지회</td>
								<td>1993.2.24 ~ 1994.4.19</td>
							</tr>
							<tr>
								<td>32</td>
								<td>이사</td>
								<td>장종태</td>
								<td>대전.충청 지회</td>
								<td>1994.4.19 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>33</td>
								<td>이사</td>
								<td>고광준</td>
								<td>인천지회</td>
								<td>1990.12.31 ~ 1993.2.24</td>
							</tr>
							<tr>
								<td>34</td>
								<td>이사</td>
								<td>정유남</td>
								<td>광주.전남 지회</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>35</td>
								<td>이사</td>
								<td>이성노</td>
								<td>전북지회</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>36</td>
								<td>이사</td>
								<td>김인석</td>
								<td>대구.경북 지회</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>
							<tr>
								<td>37</td>
								<td>전무 이사</td>
								<td>김길문</td>
								<td>(주)용진용역</td>
								<td>1993.2.24 ~ 1995.3.3</td>
							</tr>



							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[12대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="220"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
								<th scope="col">임기</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td>고문</td>
								<td>이경구</td>
								<td>8대,9대 회장</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>2</td>
								<td>고문</td>
								<td>최종만</td>
								<td>10대 회장</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>3</td>
								<td>고문</td>
								<td>김용래</td>
								<td>(사)서울시 환경미화원후원회 회장</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>4</td>
								<td>자문 위원</td>
								<td>이종구</td>
								<td>(주)대교 산업</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>5</td>
								<td>자문 위원</td>
								<td>김석호</td>
								<td>일본협회 상담역</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>6</td>
								<td>자문 위원</td>
								<td>김윤신</td>
								<td>한양대학교 교수</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>7</td>
								<td>자문 위원</td>
								<td>김창열</td>
								<td>전 협회 부회장</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>8</td>
								<td>상임 고문</td>
								<td>정덕균</td>
								<td>11대 회장</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>9</td>
								<td>회장</td>
								<td>김준언</td>
								<td>동우종합용역(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>10</td>
								<td>총무부회장</td>
								<td>김만업</td>
								<td>유창물산(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>11</td>
								<td>국제부회장</td>
								<td>정덕균</td>
								<td>진양메인티넌스(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>12</td>
								<td>정책부회장</td>
								<td>임석규</td>
								<td>신천개발(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>13</td>
								<td>사업부회장</td>
								<td>서규회</td>
								<td>(주)최신개발</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>14</td>
								<td>교육부회장</td>
								<td>박병준</td>
								<td>명신종합관리(주)</td>
								<td>1995.3.3 ~ 1997.2.26</td>
							</tr>
							<tr>
								<td>15</td>
								<td>교육부회장</td>
								<td>안석순</td>
								<td>보흥실업(주)</td>
								<td>1997.2.26 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>16</td>
								<td>재정홍보부회장</td>
								<td>김유기</td>
								<td>(주)동도시스템</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>17</td>
								<td>기술부회장</td>
								<td>한원덕</td>
								<td>(주)한덕엔지리어링</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>18</td>
								<td>환경부회장</td>
								<td>탁재용</td>
								<td>흥안실업(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>19</td>
								<td>지방부회장</td>
								<td>고광준</td>
								<td>인천지회장</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>20</td>
								<td>감사</td>
								<td>맹준호</td>
								<td>태광진흥(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>21</td>
								<td>감사</td>
								<td>엄영회</td>
								<td>영진비엠에스(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>22</td>
								<td>이사</td>
								<td>김건치</td>
								<td>한국실업(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>23</td>
								<td>이사</td>
								<td>하만구</td>
								<td>삼호기연(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>24</td>
								<td>이사</td>
								<td>임종관</td>
								<td>(주)동도시스템</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>25</td>
								<td>이사</td>
								<td>송재원</td>
								<td>우지기업(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>26</td>
								<td>이사</td>
								<td>장인수</td>
								<td>삼호실업(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>27</td>
								<td>이사</td>
								<td>전석택</td>
								<td>삼진성흥(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>28</td>
								<td>이사</td>
								<td>주영의</td>
								<td>수도용역(주)</td>
								<td>1995.3.3 ~ 1997.6.26</td>
							</tr>
							<tr>
								<td>29</td>
								<td>이사</td>
								<td>김시태</td>
								<td>(주)보경실업</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>30</td>
								<td>이사</td>
								<td>지장용</td>
								<td>미림개발(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>31</td>
								<td>이사</td>
								<td>홍방희</td>
								<td>(주)현대환경공사</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>32</td>
								<td>이사</td>
								<td>허증</td>
								<td>경원산업관리(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>33</td>
								<td>이사</td>
								<td>이상범</td>
								<td>한동용역(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>35</td>
								<td>이사</td>
								<td>한윤학</td>
								<td>(주)현풍개발</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>36</td>
								<td>이사</td>
								<td>남충남</td>
								<td>한일종합관리(주)</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>37</td>
								<td>이사</td>
								<td>김동후</td>
								<td>부산,경남 지회</td>
								<td>1995.3.3 ~ 1997.1.23</td>
							</tr>
							<tr>
								<td>38</td>
								<td>이사</td>
								<td>이상덕</td>
								<td>부산,경남 지회</td>
								<td>1997.1.23 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>39</td>
								<td>이사</td>
								<td>장종태</td>
								<td>대전,충남 지회</td>
								<td>1994.4.19 ~ 1996.2.9</td>
							</tr>
							<tr>
								<td>40</td>
								<td>이사</td>
								<td>김호중</td>
								<td>대전,충남 지회</td>
								<td>1996.2.9 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>41</td>
								<td>이사</td>
								<td>고광준</td>
								<td>인천지회</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>42</td>
								<td>이사</td>
								<td>임학기</td>
								<td>광주,전남 지회</td>
								<td>1995.3.3 ~ 1997.3.13</td>
							</tr>
							<tr>
								<td>43</td>
								<td>이사</td>
								<td>고영근</td>
								<td>광주,전남 지회</td>
								<td>1997.3.13 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>44</td>
								<td>이사</td>
								<td>이성노</td>
								<td>전북지회</td>
								<td>1995.3.3 ~ 1996.3.18</td>
							</tr>
							<tr>
								<td>45</td>
								<td>이사</td>
								<td>황의윤</td>
								<td>전북지회</td>
								<td>1996.3.18 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>46</td>
								<td>이사</td>
								<td>김인석</td>
								<td>대구,경북 지회</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>47</td>
								<td>이사</td>
								<td>목영천</td>
								<td>충북지회</td>
								<td>1995.3.3 ~ 1997.2.21</td>
							</tr>
							<tr>
								<td>48</td>
								<td>이사</td>
								<td>추주식</td>
								<td>충북지회</td>
								<td>1997.2.21 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>49</td>
								<td>이사</td>
								<td>전문수</td>
								<td>제주지회</td>
								<td>1998.2.24 ~ 1998.2.26</td>
							</tr>
							<tr>
								<td>50</td>
								<td>전문 이사</td>
								<td>김길문</td>
								<td>(주)용진용역</td>
								<td>1995.3.3 ~ 1998.2.26</td>
							</tr>


							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[13대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">사진</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td><img src="/static/main/img/org/ob_vip/vip_13.jpg" width="68" height="75" alt="" /></td>
								<td>회장</td>
								<td>김만업</td>
								<td>유창물산(주)</td>
							</tr>
							<tr>
								<td>2</td>
								<td><img src="/static/main/img/org/ob_vip/head_2.jpg" width="68" height="75" alt="" /></td>
								<td>총무담당부 회장</td>
								<td>임석규</td>
								<td>신천 개발(주)</td>
							</tr>
							<tr>
								<td>3</td>
								<td><img src="/static/main/img/org/ob_vip/head_3.jpg" width="68" height="75" alt="" /></td>
								<td>사업담당부 회장</td>
								<td>김유기</td>
								<td>동도 시스템(주)</td>
							</tr>
							<tr>
								<td>4</td>
								<td><img src="/static/main/img/org/ob_vip/head_4.jpg" width="68" height="75" alt="" /></td>
								<td>국제담당부 회장</td>
								<td>엄영회</td>
								<td>영진비엠에스(주)</td>
							</tr>
							<tr>
								<td>5</td>
								<td><img src="/static/main/img/org/ob_vip/head_5.jpg" width="68" height="75" alt="" /></td>
								<td>사업담당부 회장</td>
								<td>한원덕</td>
								<td>한덕 엔지리어링(주)</td>
							</tr>
							<tr>
								<td>6</td>
								<td><img src="/static/main/img/org/ob_vip/head_202.jpg" width="68" height="75" alt="" /></td>
								<td>교육담당부 회장</td>
								<td>안석순</td>
								<td>보흥 실업(주)</td>
							</tr>
							<tr>
								<td>7</td>
								<td></td>
								<td>홍보담당부 회장</td>
								<td>강재현</td>
								<td>백상 기업(주)</td>
							</tr>
							<tr>
								<td>8</td>
								<td><img src="/static/main/img/org/ob_vip/head_7.jpg" width="68" height="75" alt="" /></td>
								<td>지회담당부 회장</td>
								<td>고광준</td>
								<td>(주)원중 기업</td>
							</tr>
							<tr>
								<td>9</td>
								<td></td>
								<td>환경담당부 회장</td>
								<td>김인석</td>
								<td>대구 지회장</td>
							</tr>
							<tr>
								<td>10</td>
								<td></td>
								<td>감사</td>
								<td>맹준호</td>
								<td>태광 진흥(주)</td>
							</tr>
							<tr>
								<td>11</td>
								<td></td>
								<td>감사</td>
								<td>서규회</td>
								<td>(주)최신 개발</td>
							</tr>
							<tr>
								<td>12</td>
								<td><img src="/static/main/img/org/ob_vip/head_199.jpg" width="68" height="75" alt="" /></td>
								<td>전무이사</td>
								<td>김길문</td>
								<td>(사)한국위생관리협회</td>
							</tr>
							<tr>
								<td>13</td>
								<td><img src="/static/main/img/org/ob_vip/head_9.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김치홍</td>
								<td>부산,경남 지회장</td>
							</tr>
							<tr>
								<td>14</td>
								<td><img src="/static/main/img/org/ob_vip/head_194.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김호중</td>
								<td>대전,충남 지회장</td>
							</tr>
							<tr>
								<td>15</td>
								<td><img src="/static/main/img/org/ob_vip/head_10.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>권충화</td>
								<td>광주,전남 지회장</td>
							</tr>
							<tr>
								<td>16</td>
								<td><img src="/static/main/img/org/ob_vip/head_11.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>황의윤</td>
								<td>전북 지회장</td>
							</tr>
							<tr>
								<td>17</td>
								<td></td>
								<td>이사</td>
								<td>양정모</td>
								<td>충북 지회장</td>
							</tr>
							<tr>
								<td>18</td>
								<td><img src="/static/main/img/org/ob_vip/head_12.PNG" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>전문수</td>
								<td>제주 지회장</td>
							</tr>
							<tr>
								<td>19</td>
								<td><img src="/static/main/img/org/ob_vip/head_13.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>유동준</td>
								<td>경북 지회장</td>
							</tr>
							<tr>
								<td>20</td>
								<td><img src="/static/main/img/org/ob_vip/head_14.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>정규철</td>
								<td>경기 지회장</td>
							</tr>
							<tr>
								<td>21</td>
								<td></td>
								<td>이사</td>
								<td>안충원</td>
								<td>강원 지회장</td>
							</tr>
							<tr>
								<td>22</td>
								<td><img src="/static/main/img/org/ob_vip/head_15.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>임종관</td>
								<td>(주)관우 개발</td>
							</tr>
							<tr>
								<td>23</td>
								<td></td>
								<td>이사</td>
								<td>하만구</td>
								<td>(주)순일 기업</td>
							</tr>
							<tr>
								<td>24</td>
								<td><img src="/static/main/img/org/ob_vip/head_16.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>장인수</td>
								<td>삼호 실업(주)</td>
							</tr>
							<tr>
								<td>25</td>
								<td><img src="/static/main/img/org/ob_vip/head_17.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>이수재</td>
								<td>(주)태원 정밀</td>
							</tr>
							<tr>
								<td>26</td>
								<td><img src="/static/main/img/org/ob_vip/head_18.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김시태</td>
								<td>(주)보경 실업</td>
							</tr>
							<tr>
								<td>27</td>
								<td></td>
								<td>이사</td>
								<td>남충남</td>
								<td>한일종합관리(주)</td>
							</tr>
							<tr>
								<td>28</td>
								<td><img src="/static/main/img/org/ob_vip/head_19.BMP" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>홍방희</td>
								<td>(주)현대환경공사</td>
							</tr>
							<tr>
								<td>29</td>
								<td><img src="/static/main/img/org/ob_vip/head_20.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>지장용</td>
								<td>미림 개발(주)</td>
							</tr>
							<tr>
								<td>30</td>
								<td><img src="/static/main/img/org/ob_vip/head_204.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>최기호</td>
								<td>용진 건설(주)</td>
							</tr>
							<tr>
								<td>31</td>
								<td></td>
								<td>이사</td>
								<td>전승일</td>
								<td>(주)승일 그린</td>
							</tr>
							<tr>
								<td>32</td>
								<td></td>
								<td>이사</td>
								<td>강동걸</td>
								<td>(주)협심메인티너스</td>
							</tr>
							<tr>
								<td>33</td>
								<td></td>
								<td>이사</td>
								<td>구자관</td>
								<td>(주)삼구 개발</td>
							</tr>
							<tr>
								<td>34</td>
								<td><img src="/static/main/img/org/ob_vip/head_22.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>변동희</td>
								<td>태가 실업(주)</td>
							</tr>
							<tr>
								<td>35</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>강규익</td>
								<td>(주)한미 기술</td>
							</tr>
							<tr>
								<td>36</td>
								<td><img src="/static/main/img/org/ob_vip/head_24.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>송문현</td>
								<td>우지 기업(주)</td>
							</tr>
							<tr>
								<td>37</td>
								<td></td>
								<td>이사</td>
								<td>이동열</td>
								<td>하나로시설관리(주)</td>
							</tr>
							<tr>
								<td>38</td>
								<td><img src="/static/main/img/org/ob_vip/head_25.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>이재구</td>
								<td>(주)태성 공사</td>
							</tr>
							<tr>
								<td>39</td>
								<td><img src="/static/main/img/org/ob_vip/head_26.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김종구</td>
								<td>태광 실업(주)</td>
							</tr>
							<tr>
								<td>40</td>
								<td><img src="/static/main/img/org/ob_vip/head_27.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>신현우</td>
								<td>미진 기업(주)</td>
							</tr>
							<tr>
								<td>41</td>
								<td><img src="/static/main/img/org/ob_vip/head_28.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>박병희</td>
								<td>(주)동광 개발</td>
							</tr>
							<tr>
								<td>42</td>
								<td><img src="/static/main/img/org/ob_vip/head_29.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김해대</td>
								<td>우진종합관리(주)</td>
							</tr>


							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[14대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">사진</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td><img src="/static/main/img/org/ob_vip/head_1.jpg" width="68" height="75" alt="" /></td>
								<td>회장</td>
								<td>김만업</td>
								<td>유창물산(주)</td>
							</tr>
							<tr>
								<td>2</td>
								<td><img src="/static/main/img/org/ob_vip/head_4.jpg" width="68" height="75" alt="" /></td>
								<td>총무담당부 회장</td>
								<td>엄영회</td>
								<td>영진비엠에스(주)</td>
							</tr>
							<tr>
								<td>3</td>
								<td><img src="/static/main/img/org/ob_vip/head_18.jpg" width="68" height="75" alt="" /></td>
								<td>사업담당부 회장</td>
								<td>김시태</td>
								<td>(주)보경 실업</td>
							</tr>
							<tr>
								<td>4</td>
								<td></td>
								<td>교육담당부 회장</td>
								<td>하만구</td>
								<td>(주)순일 기업</td>
							</tr>
							<tr>
								<td>5</td>
								<td><img src="/static/main/img/org/ob_vip/head_7.jpg" width="68" height="75" alt="" /></td>
								<td>홍보담당부 회장</td>
								<td>고광준</td>
								<td>(주)원중 기업</td>
							</tr>
							<tr>
								<td>6</td>
								<td><img src="/static/main/img/org/ob_vip/head_13.jpg" width="68" height="75" alt="" /></td>
								<td>지회담당부 회장</td>
								<td>유동준</td>
								<td>동남종합동상(합)</td>
							</tr>
							<tr>
								<td>7</td>
								<td><img src="/static/main/img/org/ob_vip/head_9.jpg" width="68" height="75" alt="" /></td>
								<td>노동담당부 회장</td>
								<td>김치홍</td>
								<td>부산.경남 지회장</td>
							</tr>
							<tr>
								<td>8</td>
								<td><img src="/static/main/img/org/ob_vip/head_10.jpg" width="68" height="75" alt="" /></td>
								<td>환경담당부 회장</td>
								<td>권충화</td>
								<td>광주.전남 지회장</td>
							</tr>
							<tr>
								<td>9</td>
								<td><img src="/static/main/img/org/ob_vip/head_26.jpg" width="68" height="75" alt="" /></td>
								<td>기술담당부 회장</td>
								<td>김종구</td>
								<td>대전.충남 지회장</td>
							</tr>
							<tr>
								<td>10</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>서울담당부 회장</td>
								<td>강규익</td>
								<td>(주)한미 기술</td>
							</tr>
							<tr>
								<td>11</td>
								<td></td>
								<td>감사</td>
								<td>맹준호</td>
								<td>태광 진흥(주)</td>
							</tr>
							<tr>
								<td>12</td>
								<td><img src="/static/main/img/org/ob_vip/head_24.jpg" width="68" height="75" alt="" /></td>
								<td>감사</td>
								<td>송문현</td>
								<td>우지 기업(주)</td>
							</tr>
							<tr>
								<td>13</td>
								<td><img src="/static/main/img/org/ob_vip/head_199.jpg" width="68" height="75" alt="" /></td>
								<td>전무이사</td>
								<td>김길문</td>
								<td>(사)한국위생관리협회</td>
							</tr>
							<tr>
								<td>14</td>
								<td><img src="/static/main/img/org/ob_vip/head_28.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>박병희</td>
								<td>인천 지회장</td>
							</tr>
							<tr>
								<td>15</td>
								<td><img src="/static/main/img/org/ob_vip/head_11.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>황의윤</td>
								<td>전북 지회장</td>
							</tr>
							<tr>
								<td>16</td>
								<td><img src="/static/main/img/org/ob_vip/head_29.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김해대</td>
								<td>대구 지회장</td>
							</tr>
							<tr>
								<td>17</td>
								<td></td>
								<td>이사</td>
								<td>양정모</td>
								<td>충북 지회장</td>
							</tr>
							<tr>
								<td>18</td>
								<td><img src="/static/main/img/org/ob_vip/head_12.PNG" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>전문수</td>
								<td>제주 지회장</td>
							</tr>
							<tr>
								<td>19</td>
								<td><img src="/static/main/img/org/ob_vip/head_30.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>서정현</td>
								<td>경북 지회장</td>
							</tr>
							<tr>
								<td>20</td>
								<td><img src="/static/main/img/org/ob_vip/head_14.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>정규철</td>
								<td>경기 지회장</td>
							</tr>
							<tr>
								<td>21</td>
								<td><img src="/static/main/img/org/ob_vip/head_41.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김광수</td>
								<td>강원 지회장</td>
							</tr>
							<tr>
								<td>22</td>
								<td><img src="/static/main/img/org/ob_vip/head_15.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>임종관</td>
								<td>(주)관우 개발</td>
							</tr>
							<tr>
								<td>23</td>
								<td><img src="/static/main/img/org/ob_vip/head_16.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>장인수</td>
								<td>삼호 실업(주)</td>
							</tr>
							<tr>
								<td>24</td>
								<td><img src="/static/main/img/org/ob_vip/head_202.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>안석순</td>
								<td>보흥 실업(주)</td>
							</tr>
							<tr>
								<td>25</td>
								<td><img src="/static/main/img/org/ob_vip/head_3.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김유기</td>
								<td>(주)동도시스템</td>
							</tr>
							<tr>
								<td>26</td>
								<td></td>
								<td>이사</td>
								<td>남충남</td>
								<td></td>
							</tr>
							<tr>
								<td>27</td>
								<td><img src="/static/main/img/org/ob_vip/head_19.BMP" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>홍방희</td>
								<td>(주)현대환경공사</td>
							</tr>
							<tr>
								<td>28</td>
								<td><img src="/static/main/img/org/ob_vip/head_20.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>지장용</td>
								<td>미림 개발(주)</td>
							</tr>
							<tr>
								<td>29</td>
								<td><img src="/static/main/img/org/ob_vip/head_204.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>최기호</td>
								<td>용진 건설(주)</td>
							</tr>
							<tr>
								<td>30</td>
								<td></td>
								<td>이사</td>
								<td>전승일</td>
								<td>(주)승일 그린</td>
							</tr>
							<tr>
								<td>31</td>
								<td></td>
								<td>이사</td>
								<td>강동걸</td>
								<td>(주)협심메인티너스</td>
							</tr>
							<tr>
								<td>32</td>
								<td></td>
								<td>이사</td>
								<td>구자관</td>
								<td>(주)삼구개발</td>
							</tr>
							<tr>
								<td>33</td>
								<td><img src="/static/main/img/org/ob_vip/head_42.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>변동희</td>
								<td>타가 실업(주)</td>
							</tr>
							<tr>
								<td>34</td>
								<td></td>
								<td>이사</td>
								<td>이동열</td>
								<td>하나로시설관리(주)</td>
							</tr>
							<tr>
								<td>35</td>
								<td><img src="/static/main/img/org/ob_vip/head_25.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>이재구</td>
								<td>(주)태성 공사</td>
							</tr>
							<tr>
								<td>36</td>
								<td><img src="/static/main/img/org/ob_vip/head_43.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>고영근</td>
								<td>현대환경개발(주)</td>
							</tr>
							<tr>
								<td>37</td>
								<td><img src="/static/main/img/org/ob_vip/head_27.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>신현우</td>
								<td>미진 기업(주)</td>
							</tr>
							<tr>
								<td>38</td>
								<td></td>
								<td>이사</td>
								<td>이용진</td>
								<td>(주)고암</td>
							</tr>
							<tr>
								<td>39</td>
								<td><img src="/static/main/img/org/ob_vip/head_44.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>조경식</td>
								<td>(주)삼보 기획</td>
							</tr>
							<tr>
								<td>40</td>
								<td></td>
								<td>이사</td>
								<td>김주수</td>
								<td>명신방호실업(주)</td>
							</tr>
							<tr>
								<td>41</td>
								<td><img src="/static/main/img/org/ob_vip/head_45.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김영배</td>
								<td>삼정종합관리(주)</td>
							</tr>





							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[15대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">사진</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td><img src="/static/main/img/org/ob_vip/vip_15.jpg" width="68" height="75" alt="" /></td>
								<td>회장</td>
								<td>김유기</td>
								<td>(주)동도시스템 <br> 우:100-193 <br> 서울 중구 을지로3가 315-4(을지빌딩2층)</td>
							</tr>
							<tr>
								<td>2</td>
								<td></td>
								<td>감사</td>
								<td>주영대</td>
								<td>새재기업(주) <br> 우:137-060 <br> 서초구 방배본동 782-1</td>
							</tr>
							<tr>
								<td>3</td>
								<td><img src="/static/main/img/org/ob_vip/head_20.jpg" width="68" height="75" alt="" /></td>
								<td>감사</td>
								<td>지장용</td>
								<td>미림개발(주) <br> 우:110-140 <br> 종로구 수송동 80-6(석탄회관 빌딩)</td>
							</tr>
							<tr>
								<td>4</td>
								<td><img src="/static/main/img/org/ob_vip/head_4.jpg" width="68" height="75" alt="" /></td>
								<td>총무 및 국제부회장</td>
								<td>엄영회</td>
								<td>영진비엠에스(주) <br> 우:110-320 <br> 서울특별시 종로구 돈화문로 11길 29, 낙원오피스텔 701호(돈의동)</td>
							</tr>
							<tr>
								<td>5</td>
								<td><img src="/static/main/img/org/ob_vip/head_24.jpg" width="68" height="75" alt="" /></td>
								<td>사업부회장</td>
								<td>송문현</td>
								<td>우지기업(주) <br> 우:110-053 <br> 종로구 내자동 167-2(인왕빌딩 2층)</td>
							</tr>
							<tr>
								<td>6</td>
								<td></td>
								<td>교육부회장</td>
								<td>전승일</td>
								<td>(주)승진매인터넌스 <br> 우:132-021 <br> 도봉구 방학 1동 685-3</td>
							</tr>
							<tr>
								<td>7</td>
								<td><img src="/static/main/img/org/ob_vip/head_22.jpg" width="68" height="75" alt="" /></td>
								<td>홍보부회장</td>
								<td>변동희</td>
								<td>(주)태가실업 <br> 우:110-194 <br> 중구 을지로 4가 310-68(삼풍빌딩 1119-3호)</td>
							</tr>
							<tr>
								<td>8</td>
								<td><img src="/static/main/img/org/ob_vip/head_26.jpg" width="68" height="75" alt="" /></td>
								<td>지회부회장</td>
								<td>김종구</td>
								<td>(주)태광실업 <br> 우:301-171 <br> 대전광역시 서구 갈마동 1434(청우코아 5층)</td>
							</tr>
							<tr>
								<td>9</td>
								<td><img src="/static/main/img/org/ob_vip/head_7.jpg" width="68" height="75" alt="" /></td>
								<td>조정부회장</td>
								<td>고광준</td>
								<td>(주)원중기업 <br> 우:405-221 <br> 인천광역시 남동구 구월 1동 227(인향빌딩 9층)</td>
							</tr>
							<tr>
								<td>10</td>
								<td></td>
								<td>환경부회장</td>
								<td>하만구</td>
								<td>(주)순일기업 <br> 우:156-010 <br> 동작구 대방도 399-1(솔표빌딩 505호)</td>
							</tr>
							<tr>
								<td>11</td>
								<td><img src="/static/main/img/org/ob_vip/head_25.jpg" width="68" height="75" alt="" /></td>
								<td>기술부회장</td>
								<td>이재구</td>
								<td>(주)태성공사 <br> 우:151-015 <br> 관악구 신림5동 1429-1(상아탑빌딩 502호)</td>
							</tr>
							<tr>
								<td>12</td>
								<td><img src="/static/main/img/org/ob_vip/head_27.jpg" width="68" height="75" alt="" /></td>
								<td>노동부회장</td>
								<td>신현우</td>
								<td>(주)미진기업 <br> 우:601-010 <br> 부산 동구 초량 1동 1212-5(금민 빌딩 404호)</td>
							</tr>
							<tr>
								<td>13</td>
								<td><img src="/static/main/img/org/ob_vip/head_199.jpg" width="68" height="75" alt="" /></td>
								<td>전무이사</td>
								<td>김길문</td>
								<td>중앙회사무국 <br> 우:110-053 <br> 성동구 성수 2가 3동 273-24(경협빌딩 6층)</td>
							</tr>
							<tr>
								<td>14</td>
								<td><img src="/static/main/img/org/ob_vip/head_10.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>권충화</td>
								<td>건도기업(주) <br> 우:500-040 <br> 광주광역시 북구 중흥 1동 664-1</td>
							</tr>
							<tr>
								<td>15</td>
								<td></td>
								<td>이사(지회장)</td>
								<td>황영수</td>
								<td>(주)삼우통상 <br> 우:402-202 <br> 인천광역시 남구 주안2동 633-4(3층)</td>
							</tr>
							<tr>
								<td>16</td>
								<td></td>
								<td>이사(지회장)</td>
								<td>박재호</td>
								<td>(유)중앙환경기업 <br> 우:561-181 <br> 전주지 덕진구 금암동1가 525-42</td>
							</tr>
							<tr>
								<td>17</td>
								<td><img src="/static/main/img/org/ob_vip/head_45.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>김영배</td>
								<td>삼정종합관리(주) <br> 우:700-421 <br> 대구광역시 중구 동인1가 351-2</td>
							</tr>
							<tr>
								<td>18</td>
								<td></td>
								<td>이사(지회장)</td>
								<td>양정모</td>
								<td>(주)도림 <br> 우:361-420 <br> 청주시 흥덕구 정봉동 33-11 복지회관2층</td>
							</tr>
							<tr>
								<td>19</td>
								<td><img src="/static/main/img/org/ob_vip/head_12.PNG" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>전문수</td>
								<td>(주)삼익 <br> 우:690-170 <br> 제주도 제주시 연동 282-27</td>
							</tr>
							<tr>
								<td>20</td>
								<td><img src="/static/main/img/org/ob_vip/head_46.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>김익진</td>
								<td>(주)경북실업 <br> 우:790-880 <br> 포항시 남구 상도동 33-144</td>
							</tr>
							<tr>
								<td>21</td>
								<td><img src="/static/main/img/org/ob_vip/head_47.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>정규철</td>
								<td>(주)동아 <br> 우:440-806 <br> 수원시 장안구 송죽동 504-6(정빌딩2층)</td>
							</tr>
							<tr>
								<td>22</td>
								<td><img src="/static/main/img/org/ob_vip/head_48.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>김광수</td>
								<td>세계환경개발(주) <br> 우:200-170 <br> 강원 춘천시 퇴계동 871-3</td>
							</tr>
							<tr>
								<td>23</td>
								<td><img src="/static/main/img/org/ob_vip/head_202.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>안석순</td>
								<td>보흥실업(주) <br> 우:150-014 <br> 영등포구 여의도동 14-32(정한빌딩 4층)</td>
							</tr>
							<tr>
								<td>24</td>
								<td><img src="/static/main/img/org/ob_vip/head_15.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>임종관</td>
								<td>(주)관우개발 <br> 우:135-283 <br> 강남구 대치3동 968(안양빌딩 5층)</td>
							</tr>
							<tr>
								<td>25</td>
								<td></td>
								<td>이사</td>
								<td>맹준호</td>
								<td>태광진흥 <br> 우:140-150 <br> 용산구 갈월동 89-9(세종빌딩 702호)</td>
							</tr>
							<tr>
								<td>26</td>
								<td><img src="/static/main/img/org/ob_vip/head_18.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김시태</td>
								<td>(주)보경실업 <br> 우:121-040 <br> 마포구 도화동 50-1(일진빌딩 709호)</td>
							</tr>
							<tr>
								<td>27</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>강규익</td>
								<td>(주)한미기술 <br> 우:135-010 <br> 강남구 논현동 238-14호(모아빌딩 203호)</td>
							</tr>
							<tr>
								<td>28</td>
								<td><img src="/static/main/img/org/ob_vip/head_44.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>조경식</td>
								<td>(주)삼보기획 <br> 우:135-110 <br> 동대문구 신설동 102-37(금성빌딩 201호)</td>
							</tr>
							<tr>
								<td>29</td>
								<td></td>
								<td>이사</td>
								<td>남충남</td>
								<td>한일종합관리(주) <br> 우:137-070 <br> 서초구 서초동 1339-7(청화빌딩 202호)</td>
							</tr>
							<tr>
								<td>30</td>
								<td></td>
								<td>이사</td>
								<td>이용진</td>
								<td>(주)고암 <br> 우:150-810 <br> 영등포구 당산동6가 327(고양빌딩)</td>
							</tr>
							<tr>
								<td>31</td>
								<td></td>
								<td>이사</td>
								<td>구자관</td>
								<td>(주)삼구개발 <br> 우:156-010 <br> 동작구 신대방동 362-43(삼구빌딩)</td>
							</tr>
							<tr>
								<td>32</td>
								<td></td>
								<td>이사</td>
								<td>강동걸</td>
								<td>(주)협심메인티넌스 <br> 우:156-090 <br> 동작구 사당동 206-122(성일빌딩 4층)</td>
							</tr>
							<tr>
								<td>33</td>
								<td><img src="/static/main/img/org/ob_vip/head_204.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>최기호</td>
								<td>용진건설(주) <br> 우:138-831 <br> 송파구 방이동 134-19(승영빌딩 3층)</td>
							</tr>
							<tr>
								<td>34</td>
								<td><img src="/static/main/img/org/ob_vip/head_47.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>이도진</td>
								<td>신천개발(주) <br> 우:110-052 <br> 종로구 적선동 80(적선현대빌딩907호)</td>
							</tr>
							<tr>
								<td>35</td>
								<td><img src="/static/main/img/org/ob_vip/head_50.PNG" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>성정헌</td>
								<td>상명진흥(주) <br> 우:130-040 <br> 중랑구 상봉2동 83-1</td>
							</tr>
							<tr>
								<td>36</td>
								<td><img src="/static/main/img/org/ob_vip/head_48.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>안무정</td>
								<td>(주)신한공사 <br> 우:131-222 <br> 중랑구 상봉2동 83-1</td>
							</tr>
							<tr>
								<td>37</td>
								<td></td>
								<td>이사</td>
								<td>이춘길</td>
								<td>국제흥업(주) <br> 우:140-012 <br> 영등포구 영등포동7가 64-48(광진빌딩 201호)</td>
							</tr>
							<tr>
								<td>38</td>
								<td></td>
								<td>이사</td>
								<td>곽치순</td>
								<td>(주)성도미화 <br> 우:150-037 <br> 영등포구 영등포동7가 64-48(광진빌딩 201호)</td>
							</tr>
							<tr>
								<td>39</td>
								<td></td>
								<td>이사</td>
								<td>이영식</td>
								<td>(주)삼화사 <br> 우:110-450 <br> 종로구 원남동 194번지(창곡빌딩 108호)</td>
							</tr><tr>
								<td>40</td>
								<td><img src="/static/main/img/org/ob_vip/head_19.BMP" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>홍방희</td>
								<td>(주)현대환경공사 <br> 우:462-241 <br> 성남시 중원구 금광1동 1789(재향군인회관4층)</td>
							</tr>
							<tr>
								<td>41</td>
								<td><img src="/static/main/img/org/ob_vip/head_196.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>박종갑</td>
								<td>동방메인텍(주) <br> 우:151-054 <br> 관악구 봉천4돋ㅇ 894-3(오성빌딩 4층)</td>
							</tr>
							<tr>
								<td>42</td>
								<td></td>
								<td>이사</td>
								<td>이동열</td>
								<td>하나로엠피에스(주) <br> 우:151-054 <br> 관악구 봉천4동 894-3(오성빌딩 4층)</td>
							</tr>
							<tr>
								<td>43</td>
								<td><img src="/static/main/img/org/ob_vip/head_195.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>최우영</td>
								<td>성원개발(주) <br> 우:138-190 <br> 송파구 석촌동 184-4(서암빌딩)</td>
							</tr>
							<tr>
								<td>44</td>
								<td><img src="/static/main/img/org/ob_vip/head_49.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>배효도</td>
								<td>(주)대성공사 <br> 우:600-082 <br> 부산 중구 보수동2가 77-7(정동빌딩 2층)</td>
							</tr>



							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[16대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">사진</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td><img src="/static/main/img/org/ob_vip/vip_15.jpg" width="68" height="75" alt="" /></td>
								<td>회장</td>
								<td>김유기</td>
								<td>(주)동도시스템 <br> 우:100-193 <br> 서울 중구 을지로3가 315-4(을지빌딩2층)</td>
							</tr>
							<tr>
								<td>2</td>
								<td><img src="/static/main/img/org/ob_vip/head_20.jpg" width="68" height="75" alt="" /></td>
								<td>감사</td>
								<td>지장용</td>
								<td>미림개발(주) <br> 우:301-040 <br> 서울 종로구 수송동 80-6(석탄회관 빌딩) </td>
							</tr>
							<tr>
								<td>3</td>
								<td><img src="/static/main/img/org/ob_vip/head_194.jpg" width="68" height="75" alt="" /></td>
								<td>감사</td>
								<td>김호중</td>
								<td>평지개발공사 <br> 우:301-040 <br> 대전시 중구 대사동 87-31(조광빌딩 3층) </td>
							</tr>
							<tr>
								<td>4</td>
								<td><img src="/static/main/img/org/ob_vip/head_4.jpg" width="68" height="75" alt="" /></td>
								<td>총무.EMG.서울부회장</td>
								<td>엄영회</td>
								<td>영진비엠에스(주) <br> 우:110-320 <br> 서울특별시 종로구 돈화문로 11길 29, 낙원오피스텔 701호(돈의동) </td>
							</tr>
							<tr>
								<td>5</td>
								<td><img src="/static/main/img/org/ob_vip/head_50.jpg" width="68" height="75" alt="" /></td>
								<td>사업부회장</td>
								<td>송문현</td>
								<td>우지기업(주) <br> 우:110-053 <br> 서울 종로구 내자동 167-2(인왕빌딩 2층) </td>
							</tr>
							<tr>
								<td>6</td>
								<td><img src="/static/main/img/org/ob_vip/head_7.jpg" width="68" height="75" alt="" /></td>
								<td>교육부회장</td>
								<td>고광준</td>
								<td>(주)원중써비스 <br> 우:405-221 <br> 인천시 남동구 구월 1동 277 </td>
							</tr>
							<tr>
								<td>7</td>
								<td><img src="/static/main/img/org/ob_vip/head_22.jpg" width="68" height="75" alt="" /></td>
								<td>홍보부회장</td>
								<td>변동희</td>
								<td>태가실업(주) <br> 우:135-090 <br> 서울 강남구 삼성동 158(두진빌딩 6층) </td>
							</tr>
							<tr>
								<td>8</td>
								<td><img src="/static/main/img/org/ob_vip/head_26.jpg" width="68" height="75" alt="" /></td>
								<td>지회부회장(지회장)</td>
								<td>김종구</td>
								<td> (주)태광실업 <br> 우:302-171 <br> 대전시 서구 갈마동 1434(정원빌딩 5층) </td>
							</tr>
							<tr>
								<td>9</td>
								<td><img src="/static/main/img/org/ob_vip/head_49.jpg" width="68" height="75" alt="" /></td>
								<td>노동부회장(지회장)</td>
								<td>배효도</td>
								<td>(주)대성공사 <br> 우:600-082  <br> 부산 중구 보수동2가 77-7(정동빌딩2층) </td>
							</tr>
							<tr>
								<td>10</td>
								<td></td>
								<td>환경부회장</td>
								<td>전승일</td>
								<td> (주)승진매인터넌스 <br> 우:132-021  <br> 서울 도봉구 방학 1동 685-3(지하 1층) </td>
							</tr>
							<tr>
								<td>11</td>
								<td><img src="/static/main/img/org/ob_vip/head_25.jpg" width="68" height="75" alt="" /></td>
								<td>기술부회장</td>
								<td>이재구</td>
								<td>(주)태성공사 <br> 우:151-015  <br> 서울 관아구 신림5동 1429-1(상아탑빌딩 502호) </td>
							</tr>
							<tr>
								<td>12</td>
								<td><img src="/static/main/img/org/ob_vip/head_195.jpg" width="68" height="75" alt="" /></td>
								<td>국제부회장</td>
								<td>최우영</td>
								<td>성원개발(주) <br> 우:138-190 <br> 서울 송파구 석촌동 184-4(서암빌딩) </td>
							</tr>
							<tr>
								<td>13</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>정책부회장</td>
								<td>강규익</td>
								<td>(주)한미기술 <br> 우:135-010 <br> 서울 강남구 논현동 238-14호(모아빌딩 203호) </td>
							</tr>
							<tr>
								<td>14</td>
								<td><img src="/static/main/img/org/ob_vip/head_18.jpg" width="68" height="75" alt="" /></td>
								<td>조정부회장</td>
								<td>김시태</td>
								<td>(주)보경실업 <br> 우:121-040  <br> 서울 마포구 도화동 50-1(일진빌딩 709호) </td>
							</tr>
							<tr>
								<td>15</td>
								<td><img src="/static/main/img/org/ob_vip/head_204.jpg" width="68" height="75" alt="" /></td>
								<td>법제부회장</td>
								<td>최기호</td>
								<td>용진건설(주) <br> 우:135-080  <br> 서울 강남구 역삼동 788-16(원경빌딩 1층) </td>
							</tr>
							<tr>
								<td>16</td>
								<td><img src="/static/main/img/org/ob_vip/head_47.jpg" width="68" height="75" alt="" /></td>
								<td>조직부회장</td>
								<td>이도진</td>
								<td>신천개발(주) <br> 우:110-052  <br> 서울 종로구 적선동80(적선현대빌딩907호) </td>
							</tr>
							<tr>
								<td>17</td>
								<td></td>
								<td>협력부회장</td>
								<td>이동열</td>
								<td>하나로엠피에스(주) <br> 우:110-290  <br> 서울 종로구 인사동 194-4(하나로빌딩 1002) </td>
							</tr>
							<tr>
								<td>18</td>
								<td><img src="/static/main/img/org/ob_vip/head_50.PNG" width="68" height="75" alt="" /></td>
								<td>지원부회장</td>
								<td>성정헌</td>
								<td>상명진흥(주) <br> 우:130-040  <br> 서울 동대문구 장안동 1888-17(동화빌딩 2층) </td>
							</tr>
							<tr>
								<td>19</td>
								<td><img src="/static/main/img/org/ob_vip/head_199.jpg" width="68" height="75" alt="" /></td>
								<td>전무이사</td>
								<td>김길문</td>
								<td>중앙회사무국 <br> 우:133-831  <br> 서울 성동구 성수 2가 3동 273-24(경협회관 6층) </td>
							</tr>
							<tr>
								<td>20</td>
								<td><img src="/static/main/img/org/ob_vip/head_10.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>권충화</td>
								<td> 건도기업(주) <br> 우:500-040  <br> 광주시 북구 중흥1동 664-1 </td>
							</tr>
							<tr>
								<td>21</td>
								<td><img src="/static/main/img/org/ob_vip/head_51.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>강인선</td>
								<td>(주)은하실업 <br> 우:400-103  <br> 인천시 중구 신흥동 32-12 </td>
							</tr>
							<tr>
								<td>22</td>
								<td></td>
								<td>이사(지회장)</td>
								<td>이춘수</td>
								<td>(주)환경개발 <br> 우:560-020  <br> 전주시 완산구 경원동 3가 86-9(두원빌딩 2층) </td>
							</tr>
							<tr>
								<td>23</td>
								<td><img src="/static/main/img/org/ob_vip/head_45.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>김영배</td>
								<td>삼정종합관리(주) <br> 우:700-421  <br> 대구시 중구 동인1가 351-2 </td>
							</tr>
							<tr>
								<td>24</td>
								<td></td>
								<td>이사(지회장)</td>
								<td>양정모</td>
								<td> (주)도림 <br> 우:361-420  <br> 청주시 흥덕구 정봉동 33-11(복지회관 2층) </td>
							</tr>
							<tr>
								<td>25</td>
								<td><img src="/static/main/img/org/ob_vip/head_12.PNG" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>전문수</td>
								<td>(주)삼익 <br> 우:690-170  <br> 제주도 제주시 연동 282-27 </td>
							</tr>
							<tr>
								<td>26</td>
								<td></td>
								<td>이사(지회장)</td>
								<td>김중현</td>
								<td>(주)엔가드대성방역 <br> 우:730-011  <br> 경북 구미시 원평1동 1028-2(금오시장 301호) </td>
							</tr>
							<tr>
								<td>27</td>
								<td><img src="/static/main/img/org/ob_vip/head_14.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>정규철</td>
								<td>(주)동아 <br> 우:440-806  <br> 수원시 장안구 송죽동 504-6(금오시장 301호) </td>
							</tr>
							<tr>
								<td>28</td>
								<td><img src="/static/main/img/org/ob_vip/head_41.jpg" width="68" height="75" alt="" /></td>
								<td>이사(지회장)</td>
								<td>김광수</td>
								<td>(주)환경21 <br> 우:200-170  <br> 강원 춘천시 퇴계동 871-3 </td>
							</tr>
							<tr>
								<td>29</td>
								<td><img src="/static/main/img/org/ob_vip/head_202.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>안석순</td>
								<td>보흥실업(주) <br> 우:150-014  <br> 서울 영등포구 여의도동 14-32(정원빌딩 505호) </td>
							</tr>
							<tr>
								<td>30</td>
								<td><img src="/static/main/img/org/ob_vip/head_44.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>조경식</td>
								<td>(주)삼보기획 <br> 우:135-110  <br> 서울 동대문구 신설동 102-37(금성빌딩 201호) </td>
							</tr>
							<tr>
								<td>31</td>
								<td><img src="/static/main/img/org/ob_vip/head_27.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>신현우</td>
								<td>미진기업(주) <br> 우:601-010 <br> 부산시 동구 초량 1동 1212-5(금민 빌딩 404호) </td>
							</tr>
							<tr>
								<td>32</td>
								<td><img src="/static/main/img/org/ob_vip/head_196.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>박종갑</td>
								<td>동방메인텍(주) <br> 우:151-054  <br> 서울 관악구 봉천4동 894-3(오성빌딩 4층) </td>
							</tr>
							<tr>
								<td>33</td>
								<td><img src="/static/main/img/org/ob_vip/head_48.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>안무정</td>
								<td>(주)신한공사 <br> 우:131-222  <br> 서울 중랑구 상봉2동 83-1 </td>
							</tr>
							<tr>
								<td>34</td>
								<td></td>
								<td>이사</td>
								<td>박종석</td>
								<td>제일안전관리(주) <br> 우:150-010  <br> 서울 영등포구 여의도동 44-15(충무빌딩 1108호) </td>
							</tr>
							<tr>
								<td>35</td>
								<td><img src="/static/main/img/org/ob_vip/head_197.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>허은강</td>
								<td>아세아환경(주) <br> 우:150-010  <br> 서울 등포구 여의도동 36-2(맨화탄빌딩 1005호) </td>
							</tr>
							<tr>
								<td>36</td>
								<td><img src="/static/main/img/org/ob_vip/head_52.PNG" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>신양주</td>
								<td>(주)프로에스콤 <br> 우:130-010  <br> 서울 동대문구 청량리동 235-6(미주상가 B동 4) </td>
							</tr>
							<tr>
								<td>37</td>
								<td></td>
								<td>이사</td>
								<td>김영춘</td>
								<td>세안기업(주) <br> 우:135-010  <br> 서울 강남구 논현동 213-7(삼화빌딩 202호) </td>
							</tr>
							<tr>
								<td>38</td>
								<td><img src="/static/main/img/org/ob_vip/head_53.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>조남각</td>
								<td>(주)백제기업 <br> 우:121-070  <br> 서울 마포구 용강동 494-97(백제빌딩)  </td>
							</tr>
							<tr>
								<td>39</td>
								<td></td>
								<td>이사</td>
								<td>박재호</td>
								<td>(유)중앙환경기업 <br> 우:561-181  <br> 전주시 덕진구 금암동1가 525-42 </td>
							</tr>
							<tr>
								<td>40</td>
								<td><img src="/static/main/img/org/ob_vip/head_54.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>고봉석</td>
								<td>삼호미화(주)<br> 우:100-180  <br> 서울 중구 다동131(삼덕빌딩 213호) </td>
							</tr>
							<tr>
								<td>41</td>
								<td>	</td>
								<td>이사</td>
								<td>조병문</td>
								<td>국제흥업(주) <br> 우:140-012  <br> 서울 용산구 한강로가 363-1(국제흥업빌딩 5층) </td>
							</tr>
							<tr>
								<td>42</td>
								<td><img src="/static/main/img/org/ob_vip/head_55.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>신건웅</td>
								<td>한덕엔지니어링(주) <br> 우:135-010  <br> 서울 강남구 논현동 66-3(한덕빌딩 3층) </td>
							</tr>
							<tr>
								<td>43</td>
								<td></td>
								<td>이사</td>
								<td>김진식</td>
								<td>동우유니온개발(주) <br> 우:135-120  <br> 서울 강남구 신사동 601-3(동우빌딩 2층) </td>
							</tr>
							<tr>
								<td>44</td>
								<td></td>
								<td>이사</td>
								<td>김연규</td>
								<td>진양메인티넌스(주) <br> 우:137-070  <br> 서울시 서초구 서초동 1535-3(선흥빌딩 4층) </td>
							</tr>
							<tr>
								<td>45</td>
								<td><img src="/static/main/img/org/ob_vip/head_56.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>신철진</td>
								<td>주영종합관리(주) <br> 우:405-220  <br> 인천시 남동구 구월동 126-92(대양빌딩 4층) </td>
							</tr>
							<tr>
								<td>46</td>
								<td><img src="/static/main/img/org/ob_vip/head_205.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>왕종진</td>
								<td>(주)서경 <br> 경기도 수원시 권선구 매교동 4-1 경기빌딩 3층 </td>
							</tr>
							<tr>
								<td>47</td>
								<td></td>
								<td>이사</td>
								<td>구자관</td>
								<td>(주)삼구개발 <br> 서울특별시 동작구 신대방동 262-43 삼구빌딩 </td>
							</tr>


							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[17대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/><col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">사진</th>
								<th scope="col">직위</th>
								<th scope="col">성명</th>
								<th scope="col">회사명</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>1</td>
								<td><img src="/static/main/img/org/ob_vip/head_4.jpg" width="68" height="75" alt="" /></td>
								<td>회장</td>
								<td>엄영회</td>
								<td>영진비엠에스(주) <br> 우:110-320 <br> 서울특별시 종로구 돈화문로 11길 29, 낙원오피스텔 701호(돈의동)  </td>
							</tr>
							<tr>
								<td>2</td>
								<td><img src="/static/main/img/org/ob_vip/head_194.jpg" width="68" height="75" alt="" /></td>
								<td>감사</td>
								<td>김호중</td>
								<td>평지개발공사<br> 우:301-040 <br> 대전시 중구 대사동 87-31(조광빌딩 3층)  </td>
							</tr>
							<tr>
								<td>3</td>
								<td><img src="/static/main/img/org/ob_vip/head_57.JPG" width="68" height="75" alt="" /></td>
								<td>감사</td>
								<td>지영식</td>
								<td>양지이엔에스(주)<br> 우:463-500 <br> 경기도 성남시 분당구 구미동 18 시그마2 오피스텔  </td>
							</tr>
							<tr>
								<td>4</td>
								<td></td>
								<td>감사(07.3.28~08.3.19</td>
								<td>김영춘</td>
								<td>세안기업(주)<br> 우:135-010 <br> 서울 강남구 논현동 213-7(삼화빌딩 202호) </td>
							</tr>
							<tr>
								<td>5</td>
								<td><img src="/static/main/img/org/ob_vip/head_50.jpg" width="68" height="75" alt="" /></td>
								<td>총무 부회장</td>
								<td>송문현</td>
								<td>우지기업(주)<br> 우:110-053 <br> 서울 종로구 내자동 167-2(인왕빌딩 2층)  </td>
							</tr>
							<tr>
								<td>6</td>
								<td><img src="/static/main/img/org/ob_vip/head_47.jpg" width="68" height="75" alt="" /></td>
								<td>사업부회장</td>
								<td>이도진</td>
								<td>신천개발(주)<br> 우:110-052 <br> 서울 종로구 적선동 80(적선현대빌딩 907호)  </td>
							</tr>
							<tr>
								<td>7</td>
								<td><img src="/static/main/img/org/ob_vip/head_18.jpg" width="68" height="75" alt="" /></td>
								<td>조정부회장</td>
								<td>김시태</td>
								<td>(주)보경실업<br> 우:120-80 <br> 서울 마포구 대흥동 325-78(금성빌딩 503호)  </td>
							</tr>
							<tr>
								<td>8</td>
								<td><img src="/static/main/img/org/ob_vip/head_22.jpg" width="68" height="75" alt="" /></td>
								<td>서울 부회장</td>
								<td>변동희</td>
								<td>태가실업(주)<br> 우:135-090 <br> 서울 강남구 삼성동 158(두진빌딩 6층)  </td>
							</tr>
							<tr>
								<td>9</td>
								<td><img src="/static/main/img/org/ob_vip/head_10.jpg" width="68" height="75" alt="" /></td>
								<td>환경 부회장</td>
								<td>권충화</td>
								<td>건도기업(주)<br> 우:500-040 <br> 광주시 북구 중흥1동 664-1  </td>
							</tr>
							<tr>
								<td>10</td>
								<td><img src="/static/main/img/org/ob_vip/head_50.PNG" width="68" height="75" alt="" /></td>
								<td>교육 부회장</td>
								<td>성정헌</td>
								<td>상명진흥(주)<br> 우:130-040 <br> 서울 동대문구 장안동 188-17(동화빌딩 2층)  </td>
							</tr>
							<tr>
								<td>11</td>
								<td><img src="/static/main/img/org/ob_vip/head_20.jpg" width="68" height="75" alt="" /></td>
								<td>홍보 부회장</td>
								<td>지장용</td>
								<td>미림개발(주)<br> 우:1110-140 <br> 서울 종로구 수송동 80-6(석탄회관 빌딩)  </td>
							</tr>
							<tr>
								<td>12</td>
								<td><img src="/static/main/img/org/ob_vip/head_49.jpg" width="68" height="75" alt="" /></td>
								<td>노동 부회장</td>
								<td>배효도</td>
								<td>(주)대성공사<br> 우:600-082 <br> 부산 중구 보수동2가 77-7(정동빌딩2층)  </td>
							</tr>
							<tr>
								<td>13</td>
								<td><img src="/static/main/img/org/ob_vip/head_25.jpg" width="68" height="75" alt="" /></td>
								<td>기술 부회장</td>
								<td>이재구</td>
								<td>(주)태성공사<br> 우:151-015 <br> 서울 관악구 신림5동 1429-1(상아탑빌딩 502호)  </td>
							</tr>
							<tr>
								<td>14</td>
								<td><img src="/static/main/img/org/ob_vip/head_195.jpg" width="68" height="75" alt="" /></td>
								<td>국제 부회장</td>
								<td>최우영</td>
								<td>성원개발(주)<br> 우:138-190 <br> 서울 송파구 석촌동 184-4(서암빌딩)  </td>
							</tr>
							<tr>
								<td>15</td>
								<td><img src="/static/main/img/org/ob_vip/head_204.jpg" width="68" height="75" alt="" /></td>
								<td>정책 부회장</td>
								<td>최기호</td>
								<td>용진건설(주)<br> 우:135-080 <br> 서울 강남구 역삼동 788-16(원경빌딩 1층)  </td>
							</tr>
							<tr>
								<td>16</td>
								<td></td>
								<td>기획 부회장</td>
								<td>양정모</td>
								<td>(주)도림<br> 우:361-420 <br> 청주시 흥덕구 정봉동 33-11(복지회관 2층)  </td>
							</tr>
							<tr>
								<td>17</td>
								<td><img src="/static/main/img/org/ob_vip/head_51.jpg" width="68" height="75" alt="" /></td>
								<td>지회 부회장</td>
								<td>강인선</td>
								<td>(주)은하실업<br> 우:400-103 <br> 인천시 중구 신흥동 32-12  </td>
							</tr>
							<tr>
								<td>18</td>
								<td><img src="/static/main/img/org/ob_vip/head_196.jpg" width="68" height="75" alt="" /></td>
								<td>지원 부회장</td>
								<td>박종갑</td>
								<td>동방메인텍(주)<br> 우:151-054 <br> 서울 관악구 봉천4동 894-3(오성빌딩 4층)  </td>
							</tr>
							<tr>
								<td>19</td>
								<td><img src="/static/main/img/org/ob_vip/head_58.jpg" width="68" height="75" alt="" /></td>
								<td>이사(부산울산경남 지회장)</td>
								<td>김규복</td>
								<td>(주)두성보안공사<br> 우:608-023 <br> 부산 남구 대연3동 55-1 21센츄리시티오피스텔 1동  </td>
							</tr>
							<tr>
								<td>20</td>
								<td><img src="/static/main/img/org/ob_vip/head_59.jpg" width="68" height="75" alt="" /></td>
								<td>이사(대전충남 지회장)</td>
								<td>장복수</td>
								<td>(합)씨디엠<br> 우:301-220 <br> 대전 서구 용문동 276-6 3층  </td>
							</tr>
							<tr>
								<td>21</td>
								<td><img src="/static/main/img/org/ob_vip/head_60.jpg" width="68" height="75" alt="" /></td>
								<td>이사(대전충난 지회장)</td>
								<td>박종길</td>
								<td>대산기업(주)<br> 우:501-100 <br> 대전광역시 서구 가장동 20-9번지  </td>
							</tr>
							<tr>
								<td>22</td>
								<td><img src="/static/main/img/org/ob_vip/head_61.jpg" width="68" height="75" alt="" /></td>
								<td>이사(광주전남 지회장)</td>
								<td>김길종</td>
								<td>현대주택관리(주)<br> 우:501-100 <br> 광주 동구 수기동 23-2 제일오피스텔801호  </td>
							</tr>
							<tr>
								<td>23</td>
								<td><img src="/static/main/img/org/ob_vip/head_62.jpg" width="68" height="75" alt="" /></td>
								<td>이사(전북지회장)</td>
								<td>배선규</td>
								<td>(주)대지<br> 우:570-160 <br> 전북 익산시 영등동 266-20  </td>
							</tr>
							<tr>
								<td>24</td>
								<td><img src="/static/main/img/org/ob_vip/head_63.jpg" width="68" height="75" alt="" /></td>
								<td>이사(대구지회장)</td>
								<td>양봉조</td>
								<td>(주)거성지엠에스 <br> 대구광역시 동구 효목2동 464-1 대구은행3층  </td>
							</tr>
							<tr>
								<td>25</td>
								<td><img src="/static/main/img/org/ob_vip/head_64.jpg" width="68" height="75" alt="" /></td>
								<td>이사(충북지회장)</td>
								<td>신의수</td>
								<td>(주)제이비컴<br> 우:360-805 <br> 충북 청주시 상당구 내덕2동 201-31 청주문화산업지  </td>
							</tr>
							<tr>
								<td>26</td>
								<td><img src="/static/main/img/org/ob_vip/head_65.jpg" width="68" height="75" alt="" /></td>
								<td>이사(충북지회장)</td>
								<td>여창구</td>
								<td>(주)KOS보안<br> 우:373-800 <br> 충북 옥천군 옥천읍 문정리 451-6  </td>
							</tr>
							<tr>
								<td>27</td>
								<td><img src="/static/main/img/org/ob_vip/head_12.PNG" width="68" height="75" alt="" /></td>
								<td>이사(제주지회장)</td>
								<td>전문수</td>
								<td>(주)삼익<br> 우:690-170 <br> 제주도 제주시 연동 282-27   </td>
							</tr>
							<tr>
								<td>28</td>
								<td><img src="/static/main/img/org/ob_vip/head_66.jpg" width="68" height="75" alt="" /></td>
								<td>이사(경북지회장)</td>
								<td>김희두</td>
								<td>(주)내원<br> 우:730-030 <br> 경북 구미시 공단동 112번지  </td>
							</tr>
							<tr>
								<td>29</td>
								<td></td>
								<td>이사(경북지회장)</td>
								<td>김중현</td>
								<td>(주)엔가드대성방역<br> 우:730-011 <br> 경북 구미시 원평1동 1028-2(금오시장 301호)  </td>
							</tr>
							<tr>
								<td>30</td>
								<td><img src="/static/main/img/org/ob_vip/head_67.jpg" width="68" height="75" alt="" /></td>
								<td>이사(경기지회장)</td>
								<td>김영종</td>
								<td>(주)포에스<br> 경기도 수원시 장안구 영화동 393-7번지 2층  </td>
							</tr>
							<tr>
								<td>31</td>
								<td><img src="/static/main/img/org/ob_vip/head_68.jpg" width="68" height="75" alt="" /></td>
								<td>이사(강원지회장)</td>
								<td>김계열</td>
								<td>(주)진산기업<br> 우:220-030 <br> 강원 원주시 학성동 973-9  </td>
							</tr>
							<tr>
								<td>32</td>
								<td><img src="/static/main/img/org/ob_vip/head_41.jpg" width="68" height="75" alt="" /></td>
								<td>이사(강원지회장)</td>
								<td>김광수</td>
								<td>(주)환경21<br> 우:200-170 <br> 강원 춘천시 퇴계동 871-3  </td>
							</tr>
							<tr>
								<td>33</td>
								<td><img src="/static/main/img/org/ob_vip/head_44.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>조경식</td>
								<td>(주)삼보기획<br> 우:135-110 <br> 서울 동대문구 신설동 102-37(금성빌딩 201호)  </td>
							</tr>
							<tr>
								<td>34</td>
								<td><img src="/static/main/img/org/ob_vip/head_48.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>안무정</td>
								<td>(주)신한공사<br> 우:131-222 <br> 서울 중랑구 상봉2동 83-1  </td>
							</tr>
							<tr>
								<td>35</td>
								<td><img src="/static/main/img/org/ob_vip/head_197.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>허은강</td>
								<td>아세아환경(주)<br> 우:150-010 <br> 서울 등포구 여의도동 36-2(맨화탄빌딩 1005호)  </td>
							</tr>
							<tr>
								<td>36</td>
								<td><img src="/static/main/img/org/ob_vip/head_52.PNG" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>신양주</td>
								<td>(주)프로에스콤<br> 우:130-010 <br> 서울 동대문구 청량리동 235-6(미주상가 B동 4)  </td>
							</tr>
							<tr>
								<td>37</td>
								<td><img src="/static/main/img/org/ob_vip/head_53.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>조남각</td>
								<td>(주)백제기업<br> 우:121-070 <br> 서울 마포구 용강동 494-97(백제빌딩)  </td>
							</tr>
							<tr>
								<td>38</td>
								<td></td>
								<td>이사</td>
								<td>조병문</td>
								<td>국제흥업(주)<br> 우:140-012 <br> 서울 용산구 한강로가 363-1(국제흥업빌딩 5층)  </td>
							</tr>
							<tr>
								<td>39</td>
								<td><img src="/static/main/img/org/ob_vip/head_69.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>이재석</td>
								<td>신성BMC(주) <br> 서울특별시 강서구 등촌동 628-13 현대프린스텔 212호  </td>
							</tr>
							<tr>
								<td>40</td>
								<td><img src="/static/main/img/org/ob_vip/head_70.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>정상호</td>
								<td>(주)인광엔지니어링 <br> 서울특별시 마포구 서교동 463-2 관양빌딩 2층  </td>
							</tr>
							<tr>
								<td>41</td>
								<td><img src="/static/main/img/org/ob_vip/head_71.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>박광수</td>
								<td>금학개발(주) <br> 서울특별시 강남구 논현동 116-3 삼영빌딩 403호  </td>
							</tr>
							<tr>
								<td>42</td>
								<td><img src="/static/main/img/org/ob_vip/head_27.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>신현우</td>
								<td>미진기업(주) <br> 부산광역시 동구 초량 1동 1212-5 금민빌딩 404호  </td>
							</tr>
							<tr>
								<td>43</td>
								<td><img src="/static/main/img/org/ob_vip/head_45.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김영배</td>
								<td>삼정종합관리(주)<br> 우:700-421 <br> 대구시 중구 동인1가 351-2  </td>
							</tr>
							<tr>
								<td>44</td>
								<td><img src="/static/main/img/org/ob_vip/head_14.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>정규철</td>
								<td>(주)동아<br> 우:440-806 <br> 수원시 장안구 송죽동 504-6(정빌딩2층)  </td>
							</tr>
							<tr>
								<td>45</td>
								<td><img src="/static/main/img/org/ob_vip/head_56.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>신철진</td>
								<td>주영종합관리(주) <br> 인천광역시 남동구 구월동 1262 대양빌딩4층  </td>
							</tr>
							<tr>
								<td>46</td>
								<td><img src="/static/main/img/org/ob_vip/head_72.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>김육형</td>
								<td>현대환경개발(주) <br> 광주광역시 동구 계림동 578-11 의재회관 5층  </td>
							</tr>
							<tr>
								<td>47</td>
								<td><img src="/static/main/img/org/ob_vip/head_73.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>이용문</td>
								<td>(주)고암 <br> 서울특별시 영등포구 당산동6가 327  </td>
							</tr>
							<tr>
								<td>48</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>강규익</td>
								<td>(주)한미기술<br> 우:135-010 <br> 서울 강남구 논현동 238-14(모아빌딩 203호)  </td>
							</tr>
							<tr>
								<td>49</td>
								<td><img src="/static/main/img/org/ob_vip/head_74.jpg" width="68" height="75" alt="" /></td>
								<td>이사</td>
								<td>박일수</td>
								<td>한국필맙(주) <br> 경기도 고양시 덕양구 덕은동 418-1번지  </td>
							</tr>



							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[18대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">직위</th>
								<th scope="col">사진</th>
								<th scope="col">성명</th>
								<th scope="col">회사명.주소.연락처</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_4.jpg" width="68" height="75" alt="" /></td>
								<td>엄영회</td>
								<td>영진비엠에스(주) <br> 우:110-320  <br> 서울특별시 종로구 돈화문로 11길 29, 낙원오피스텔 701호(돈의동)  <br> (T)02)763-1038 (F)02)762-1863 </td>
							</tr>
							<tr>
								<td>감사</td>
								<td><img src="/static/main/img/org/ob_vip/head_101.jpg" width="68" height="75" alt="" /></td>
								<td>전용수</td>
								<td>신우산업관리(주)<br> 우:150-040  <br> 영등포구 당산동 74-2 금강펜테리움IT타워 1101호  <br> (T)02)587-7691 (F)02)587-7690  </td>
							</tr>
							<tr>
								<td>감사</td>
								<td><img src="/static/main/img/org/ob_vip/head_102.jpg" width="68" height="75" alt="" /></td>
								<td>김영선</td>
								<td>(주)화목<br> 우:431-838  <br> 안양시 동안구 호계동 937-7 A동 1층 3호  <br> (T)031)455-9377 (F)031)457-3357  </td>
							</tr>
							<tr>
								<td>수석 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_103.jpg" width="68" height="75" alt="" /></td>
								<td>송문현</td>
								<td>우지기업(주)<br> 우:110-053  <br> 서울 종로구 내자동 167-2 인왕빌딩 2층  <br> (T)02)737-8822(F)02)739-7027  </td>
							</tr>
							<tr>
								<td>사업 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_104.jpg" width="68" height="75" alt="" /></td>
								<td>이도진</td>
								<td>(주)중앙관리시스템<br> 우:110-052  <br> 서울 종로구 적선동 156 광화문플래티넘 1512호  <br> (T)02)2061-2321 (F)02)2061-2324 </td>
							</tr>
							<tr>
								<td>교육 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_105.jpg" width="68" height="75" alt="" /></td>
								<td>성정헌</td>
								<td>상명진흥(주)<br> 우:130-040  <br> 서울 동대무구 장안동 188-17 동화빌딩 2층  <br> (T)02)2243-1366(F)02)2245-9063  </td>
							</tr>
							<tr>
								<td>조정 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_18.jpg" width="68" height="75" alt="" /></td>
								<td>김시태</td>
								<td>(주)보경실업<br> 우:120-80  <br> 서울 마포구 대흥동 325-78 금성빌딩 503호  <br> (T)02)718-4221 (F)02)718-4223  </td>
							</tr>
							<tr>
								<td>국제 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_195.jpg" width="68" height="75" alt="" /></td>
								<td>최우영</td>
								<td>성원개발(주)<br> 우:135-280  <br> 서울 강남구 대치동 944-30 성원타워 9층  <br> (T)02)424-5671 (F)02)424-5675  </td>
							</tr>
							<tr>
								<td>홍보 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_106.jpg" width="68" height="75" alt="" /></td>
								<td>지장용</td>
								<td>미림개발(주)<br> 우:110-140  <br> 서울 종로구 수송동 80-6 석탄회관 빌딩 <br> (T)02)739-9535 (F)02)723-5770  </td>
							</tr>
							<tr>
								<td>서울 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_22.jpg" width="68" height="75" alt="" /></td>
								<td>변동희</td>
								<td>태가실업(주)<br> 우:135-090  <br> 서울 강남구 삼성동 158 두진빌딩 6층  <br> (T)02)562-9432 (F)02)562-9816  </td>
							</tr>
							<tr>
								<td>지회 부회장(경기지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_107.jpg" width="68" height="75" alt="" /></td>
								<td>김영종</td>
								<td>세균제로/(주)포에스<br> 우:440-820  <br> 경긱도 수원시 장안구 영화동 393-7번지 2층  <br> (T)031)252-6554 (F)031)254-9446  </td>
							</tr>
							<tr>
								<td>환경 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_196.jpg" width="68" height="75" alt="" /></td>
								<td>박종갑</td>
								<td>동방메인텍(주)<br> 우:151-054  <br> 서울 관악구 봉천동 1575-9 장성빌딩 3층  <br> (T)02)878-0151 (F)02)878-0190  </td>
							</tr>
							<tr>
								<td>기술 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_25.jpg" width="68" height="75" alt="" /></td>
								<td>이재구</td>
								<td>(주)태성공사<br> 우:151-015  <br> 서울 관악구 신림5동 1429-1 상아탑빌딩 502호  <br> (T)02)885-1481 (F)02)873-7434  </td>
							</tr>
							<tr>
								<td>조직 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_109.jpg" width="68" height="75" alt="" /></td>
								<td>이용문</td>
								<td>(주)고암<br> 우:150-046  <br> 서울 영등포구 당산동6가 327 고암빌딩 7층  <br> (T)02)326-3011 (F)02)326-3010 </td>
							</tr>
							<tr>
								<td>정책 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>강규익</td>
								<td>(주)한미기술<br> 우:135-010  <br> 서울 강남구 논현동 238-14호 모아빌딩 203호  <br> (T)02)548-3888 (F)02)542-3880  </td>
							</tr>
							<tr>
								<td>봉사 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_111.jpg" width="68" height="75" alt="" /></td>
								<td>안무정</td>
								<td>(주)신한공사<br> 우:131-222<br> 서울 중랑구 상봉2동 83-1  <br> (T)02)496-4383 (F)02)494-5026  </td>
							</tr>
							<tr>
								<td>복지 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_112.jpg" width="68" height="75" alt="" /></td>
								<td>박광수</td>
								<td>금학개발(주)<br> 우:135-010  <br> 서울특별시 강남구 논현동 116-3 삼영빌딩403호  <br> (T)02)549-1906 (F)02)541-6622  </td>
							</tr>
							<tr>
								<td>검정 부회장(광주전남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_113.jpg" width="68" height="75" alt="" /></td>
								<td>김길종</td>
								<td>현대주택관리(주)<br> 우:501-100  <br> 광주 동구 수기동 23-2 제일오피스텔 801호  <br> (T)062-225-5009 (F)062-225-3009  </td>
							</tr>
							<tr>
								<td>지원 부회장(대구지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_114.jpg" width="68" height="75" alt="" /></td>
								<td>양봉조</td>
								<td>(주)거성지엠에스<br> 대구광역시 동구 효목2동 464-1 대구은행 3층  <br> (T)053)742-1221 (F)053)753-8795  </td>
							</tr>
							<tr>
								<td>이사(부산울산경남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_115.jpg" width="68" height="75" alt="" /></td>
								<td>김규복</td>
								<td>(주)두성보안공사<br> 우:608-023  <br> 부산 남구 대연3동 55-1 21 센츄리시티오피스텔 1동 2028호  <br> (T)051)610-1510 (F)051)610-1509  </td>
							</tr>
							<tr>
								<td>이사(대전충남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_116.jpg" width="68" height="75" alt="" /></td>
								<td>홍진학</td>
								<td>(주)대종종합관리<br> 우:300-150  <br> 대전 동구 정동 31-1 신영빌딩 502호  <br> (T)042)621-2613 (F)042)621-2614  </td>
							</tr>
							<tr>
								<td>이사(인천 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_117.jpg" width="68" height="75" alt="" /></td>
								<td>이원교</td>
								<td>한국진돗개안전C&S(주)<br> 우:400-103  <br> 인천 중구 신흥동3가 32-12  <br> (T)032)889-2112 (F)032)889-2114  </td>
							</tr>
							<tr>
								<td>이사(전북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_118.jpg" width="68" height="75" alt="" /></td>
								<td>이종택</td>
								<td>(유)삼문개발<br> 우:561-181  <br> 전주시 완산구 금암1동 710-5  <br> (T)063)251-0611 (F)063)271-0612  </td>
							</tr>
							<tr>
								<td>이사(충북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_119.jpg" width="68" height="75" alt="" /></td>
								<td>신의수</td>
								<td>(주)제이비컴<br> 우:361-290  <br> 충북 청주시 흥덕구 송정동 279-5 세중테크노밸리 1001호  <br> (T)043)215-4886 (F)043)215-4889 </td>
							</tr>
							<tr>
								<td>이사(제주지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_120.PNG" width="68" height="75" alt="" /></td>
								<td>전문수</td>
								<td>(주)삼익<br> 우:690-170  <br> 제주도 제주시 연동 282-27  <br> (T)064)747-2601 (F)064)747-2616  </td>
							</tr>
							<tr>
								<td>이사(경북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_121.jpg" width="68" height="75" alt="" /></td>
								<td>김희두</td>
								<td>(주)내원<br> 우:730-030 <br> 경북 구미시 공단동 112번지  <br> (T)054-463-6868 (F)054-463-6867  </td>
							</tr>
							<tr>
								<td>이사(강원지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_122.jpg" width="68" height="75" alt="" /></td>
								<td>박용준</td>
								<td>승화(유)<br> 우:210-814  <br> 강원 강릉시 구정면 제비리 951  <br> (T)033)610-6105 (F)033)645-5861  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_123.jpg" width="68" height="75" alt="" /></td>
								<td>장인수</td>
								<td>삼호실업(주)<br> 우:150-010  <br> 서울 영등포구 여의도동 25-5 동화빌딩 1403호  <br> (T)02)782-2290 (F)02)784-0636  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_124.jpg" width="68" height="75" alt="" /></td>
								<td>조경식</td>
								<td>(주)삼보기획<br> 우:135-110  <br> 서울 동대문구 신설동 102-37 금성빌딩 201호 <br> (T)02)923-4768 (F)02)925-3294   </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_197.jpg" width="68" height="75" alt="" /></td>
								<td>허은강</td>
								<td>아세아환경(주)<br> 우:150-010  <br> 서울 영등포구 여의도동 36-2 맨하탄빌딩 1005호  <br> (T)02)780-6876 (F)02)783-1353  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_125.jpg" width="68" height="75" alt="" /></td>
								<td>조남각</td>
								<td>(주)백제기업<br> 우:121-070  <br> 서울 마포구 용강동 494-97 신석빌딩2층  <br> (T)02)718-8855 (F)02)716-9514 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_126.jpg" width="68" height="75" alt="" /></td>
								<td>정종범</td>
								<td>(주)순일기업<br> 우:156-020  <br> 서울 동작구 대방동 339-1 솔표빌딩 3층  <br> (T)02)823-1311 (F)02)823-1811  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_52.PNG" width="68" height="75" alt="" /></td>
								<td>신양주</td>
								<td>(주)프로에스콤<br> 우:135-080 <br> 서울 강남구 역삼동 707-34 한신인터벨리24 서관20층  <br> (T)02)2183-0118 (F)02)2183-01177  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_127.jpg" width="68" height="75" alt="" /></td>
								<td>진인은</td>
								<td>(주)진은종합개발<br> 우:150-046  <br> 서울 영등포구 당산동6가 339-3 환희빌딩 3층   <br> T)02)2631-3954 (F)02)2631-3964  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_128.jpg" width="68" height="75" alt="" /></td>
								<td>박기준</td>
								<td>(주)씨앤에스자산관리<br> 우:110-052  <br> 서울 종로구 적선동 80번지 적선현대빌딩 907호  <br> T)02)732-9676 (F)02)733-3829  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_129.jpg" width="68" height="75" alt="" /></td>
								<td>신황철</td>
								<td>두신산업(주)<br> 우:142-070  <br> 서울 강북구 수유동47-14번지 3층  <br> T)02)997-0227 (F)02)997-2465  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_130.jpg" width="68" height="75" alt="" /></td>
								<td>정상호</td>
								<td>(주)인광엔지니어링  <br> 서울특별시 마포구 서교동 463-29 골드만빌딩 3층  <br> T)02)338-4602 (F)02)326-3775  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_131.jpg" width="68" height="75" alt="" /></td>
								<td>이기만</td>
								<td>(주)청송안전시스템<br> 우:405-221  <br> 인천 남동구 구월1동 277 인향콤비타워 1층  <br> T)032)472-5077 (F)032)471-5076  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_132.jpg" width="68" height="75" alt="" /></td>
								<td>정상원</td>
								<td>(주)진양메인티넌스<br> 우:137-070  <br> 서울 서초구 서초동 1535-3번지 선흥빌딩 4층  <br> T)02)522-3611 (F)02)522-3615  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_133.jpg" width="68" height="75" alt="" /></td>
								<td>이현종</td>
								<td>(주)신성비엠씨<br> 우:157-861  <br> 서울 강서구 염창동 240-21 우림블루나인 비즈니스센터 B-2003 <br> T)02)2659-0355 (F)02)2659-0393  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_134.jpg" width="68" height="75" alt="" /></td>
								<td>이정만</td>
								<td>(주)대한안전관리공사<br> 우:121-040  <br> 서울 마포구 도화동 37 진도빌딩 10층  <br> T)02)716-1343 (F)02)716-1336  </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_135.jpg" width="68" height="75" alt="" /></td>
								<td>박일수</td>
								<td>한국필맙(주)<br> 우:121-270  <br> 서울 마포구 상암동 상암근린상가 301호  <br> T)02)372-7641 (F)02)374-2914  </td>
							</tr>


							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[19대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">직위</th>
								<th scope="col">사진</th>
								<th scope="col">성명</th>
								<th scope="col">회사명.주소.연락처</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_140.jpg" width="68" height="75" alt="" /></td>
								<td>송문현</td>
								<td>우지기업(주)<br>우:03169 <br> 서울 종로구 사직로 10길 17 인왕빌딩 2층<br>(T) 02)737-8822 (F) 02)739-7027 </td>
							</tr>
							<tr>
								<td>감사</td>
								<td><img src="/static/main/img/org/ob_vip/head_75.jpg" width="68" height="75" alt="" /></td>
								<td>전용수</td>
								<td>신우산업관리(주)<br> 우:150-040 <br> 영등포구 당산동 74-2 금강펜테리움IT 타워 1101호 <br> (T)02)587-7691 (f)02)587-7690 </td>
							</tr>
							<tr>
								<td>감사</td>
								<td><img src="/static/main/img/org/ob_vip/head_76.jpg" width="68" height="75" alt="" /></td>
								<td>김영선</td>
								<td>(주)화목<br> 우:431-838 <br> 안양시 동안구 호계동 937-7 A동 1층 3호 <br> (T)031)455-9377 (f)031)457-3357 </td>
							</tr>
							<tr>
								<td>수석 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_20.jpg" width="68" height="75" alt="" /></td>
								<td>지장용</td>
								<td>미림개발(주)<br> 우:110-140 <br> 서울 종로구 수송동 80-6 석탄회관 빌딩 <br> (T)02)739-9535 (f)02)723-5770 </td>
							</tr>
							<tr>
								<td>정책 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_50.PNG" width="68" height="75" alt="" /></td>
								<td>성정헌</td>
								<td>상명진흥(주)<br> 우:130-040 <br> 서울 동대문구 장안동 188-17 동화빌딩 2층 <br> (T)02)2243-1366 (f)02)2245-9063 </td>
							</tr>
							<tr>
								<td>사업 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_73.jpg" width="68" height="75" alt="" /></td>
								<td>이용문</td>
								<td>(주)고암<br> 우:150-046 <br> 서울 영등포구 당산동6가 327 고암빌딩 7층 <br> (T)02)326-3011 (f)02)326-3010 </td>
							</tr>
							<tr>
								<td>조정 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_22.jpg" width="68" height="75" alt="" /></td>
								<td>변동희</td>
								<td>태가비엠(주)<br> 우:135-090 <br> 서울 강남구 삼성동 158두진빌딩 6층 <br> (T)02)562-9431 (f)02)562-9816 </td>
							</tr>
							<tr>
								<td>국제 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_195.jpg" width="68" height="75" alt="" /></td>
								<td>최우영</td>
								<td>성원개발(주)<br> 우:135-280 <br> 서울 강남구 대치동 944-30 성원타워 9층<br> (T)02)424-5671 (f)02)424-5675 </td>
							</tr>
							<tr>
								<td>교육 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>강규익</td>
								<td>(주)한미기술<br> 우:135-010 <br> 서울 강남구 논현동 238-14호 모아빌딩 203호 <br> (T)02)548-3888 (f)02)542-3880 </td>
							</tr>
							<tr>
								<td>홍보 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_25.jpg" width="68" height="75" alt="" /></td>
								<td>이재구</td>
								<td>(주)태성공사<br> 우:151-015 <br> 서울 관악구 신림5동 1429-1 상아탑빌딩 502호 <br> (T)02)885-1481 (f)02)873-7434 </td>
							</tr>
							<tr>
								<td>서울 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_196.jpg" width="68" height="75" alt="" /></td>
								<td>박종갑</td>
								<td>동방메인텍(주)<br> 우:151-054 <br> 서울 관악구 봉천동 1575-9 장성빌딩3층 <br> (T)02)878-0151 (f)02)878-0190 </td>
							</tr>
							<tr>
								<td>지회 부회장(인천지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_77.jpg" width="68" height="75" alt="" /></td>
								<td>이원교</td>
								<td>한국진돗개안전C&S(주)<br> 우:400-103 <br> 인천 중구 신흥동3가 32-12 <br> (T)032)889-2112 (f)032)889-2114 </td>
							</tr>
							<tr>
								<td>노동 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_78.jpg" width="68" height="75" alt="" /></td>
								<td>박기준</td>
								<td>(주)씨앤에스자산관리<br> 우:110-052 <br> 서울 종로구 적선동 80번지 적선현대빌딩 907호 <br> (T)02)732-9676 (f)02)733-3829 </td>
							</tr>
							<tr>
								<td>환경 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_79.jpg" width="68" height="75" alt="" /></td>
								<td>진인은</td>
								<td>(주)진은종합개발<br> 우:150-046 <br> 서울 영등포구 당산동6가 339-3 환희빌딩 3층 <br> (T)02)2631-3954 (f)02)2631-3964 </td>
							</tr>
							<tr>
								<td>이사 <br> (부산울산경남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_80.jpg" width="68" height="75" alt="" /></td>
								<td>진영산</td>
								<td>현대종합용역(주)<br> 우:600-101 <br> 부산광역시 중구 중앙대로 131(센트럴오피스텔 1107호) <br> (T)051-469-3568 (f)051-465-1743 </td>
							</tr>
							<tr>
								<td>이사 <br> (대전충남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_81.jpg" width="68" height="75" alt="" /></td>
								<td>홍진학</td>
								<td>(주)대종종합관리<br> 우:300-150 <br> 대전 동구 정동 31-1 신영빌딩 502호 <br> (T)042)621-2613 (f)042)621-2614 </td>
							</tr>
							<tr>
								<td>이사 <br> (광주전남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_82.jpg" width="68" height="75" alt="" /></td>
								<td>김육형</td>
								<td>현대환경개발(주)<br> 우:501-080 <br> 광주 동구 계림동 578-11 의재회관 5층 <br> (T)062)226-5400 (f)062)227-2600 </td>
							</tr>
							<tr>
								<td>이사<br> (전북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_83.jpg" width="68" height="75" alt="" /></td>
								<td>이종택</td>
								<td>(유)삼문개발<br> 우:561-181 <br> 전주시 완산구 금암1동 120-5 <br> (T)063)251-0611 (f)063)271-0612 </td>
							</tr>
							<tr>
								<td>이사 <br> (대구지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_84.JPG" width="68" height="75" alt="" /></td>
								<td>황태봉</td>
								<td>한국종합개발(주)<br> 우:700-810 <br> 대구광역시 중구 대봉로 238-18 <br> (T)053)427-9898 (f)053)427-8326 </td>
							</tr>
							<tr>
								<td>이사 <br> (충북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_85.JPG	" width="68" height="75" alt="" /></td>
								<td>임헌표</td>
								<td>(주)현대산업관리<br> 우:361-300 <br> 충북 청주시 흥덕구 봉명동 1059번지 <br> (T)043)268-4412 (f)043)268-4414 </td>
							</tr>
							<tr>
								<td>이사 <br> (제주지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_12.PNG" width="68" height="75" alt="" /></td>
								<td>전문수</td>
								<td>(주)삼익<br> 우:690-170 <br> 제주도 제주시 연동 282-27 <br> (T)064)747-2601 (f)064)747-2616 </td>
							</tr>
							<tr>
								<td>이사 <br> (경북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_86.jpg" width="68" height="75" alt="" /></td>
								<td>김희두</td>
								<td>(주)내원<br> 우:730-030 <br> 경북 구미시 공단동 112번지 <br> (T)054-463-6868 (f)054-463-6867 </td>
							</tr>
							<tr>
								<td>이사 <br> (경기지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_87.jpg" width="68" height="75" alt="" /></td>
								<td>최해진</td>
								<td>(주)신강개발<br> 우:426-807 <br> 경기 안산시 상록구 본오동 674-3 명성빌딩 지하1층 <br> (T)031)416-2845 (f)031)416-2846 </td>
							</tr>
							<tr>
								<td>이사 <br> (강원지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_88.jpg" width="68" height="75" alt="" /></td>
								<td>박용준</td>
								<td>승화(유)<br> 우:210-814 <br> 강원 강릉시 구정면 제비리 951 <br> (T)033)610-6105 (f)033)645-5861 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_89.jpg" width="68" height="75" alt="" /></td>
								<td>장인수</td>
								<td>삼호실업(주)<br> 우:150-010 <br> 서울 영등포구 여의도동 25-2 동화빌딩 1403호 <br> (T)02)782-2290 (f)02)784-0636 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_90.jpg" width="68" height="75" alt="" /></td>
								<td>조경식</td>
								<td>(주)삼보기획<br> 우:135-110 <br> 서울 동대문구 신설동 102-37 금성빌딩 201호 <br> (T)02)923-4768 (f)02)925-3294 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_91.jpg" width="68" height="75" alt="" /></td>
								<td>이상복</td>
								<td>(주)미동기술<br> 우:138-220 <br> 서울 송파구 잠실동 246번지 3층 <br> (T)02)521-9985 (f)02)521-9986 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_92.jpg" width="68" height="75" alt="" /></td>
								<td>신황철</td>
								<td>두신산업(주)<br> 우:142-070 <br> 서울 강북구 수유동 47-14번지 3층 <br> (T)02)997-0227 (f)02)997-2465 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_93.jpg" width="68" height="75" alt="" /></td>
								<td>박광수</td>
								<td>금학개발(주)<br> 우:135-010 <br> 서울특별시 강남구 논현동 116-3 삼영빌딩 403호 <br> (T)02)549-1906 (f)02)541-6622 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_94.jpg" width="68" height="75" alt="" /></td>
								<td>이정만</td>
								<td>(주)대한안전관리공사<br> 우:121-040 <br> 서울특별시 마포구 도화동 37 진도빌딩 10층 <br> (T)02)716-1906 (f)02)716-1336 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_95.jpg" width="68" height="75" alt="" /></td>
								<td>조남각</td>
								<td>(주)백제기업<br> 우:121-070 <br> 서울 마포구 용강동 494-97 신석빌딩2층 <br> (T)02)718-8855 (f)02)716-9514 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_96.jpg" width="68" height="75" alt="" /></td>
								<td>정종범</td>
								<td>(주)순일기업<br> 우:156-020 <br> 서울 동작구 대방동 339-1 솔표빌딩 3층 <br> (T)02)823-1311 (f)02)823-1811 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_52.PNG" width="68" height="75" alt="" /></td>
								<td>신양주</td>
								<td>(주)프로에스콤<br> 우:135-080 <br> 서울 강남구 역삼동 707-34 한신인터벨리24 서관20층 <br> (T)02)2183-0118 (f)02)2183-0117 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_200.jpg" width="68" height="75" alt="" /></td>
								<td>박현순</td>
								<td>국제흥업(주)<br> 우:140-875 <br> 서울특별시 용산구 서빙고로 5(한강로2가, 신원빌딩) 2층 <br> (T)02)798-9906 (f)02)798-3195 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_198.jpg" width="68" height="75" alt="" /></td>
								<td>이용준</td>
								<td>(주)광진양행<br> 우:150-051 <br> 서울 영등포구 신길1동 110-5 태화빌딩 4층 <br> (T)02)832-6206 (f)02)832-6207 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_97.jpg" width="68" height="75" alt="" /></td>
								<td>이준호</td>
								<td>(주)보경실업<br> 우:120-080 <br> 서울 마포구 대흥동 325-78 금성빌딩 503호 <br> (T)02)718-4221 (f)02)718-4223 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_98.jpg" width="68" height="75" alt="" /></td>
								<td>정상원</td>
								<td>(주)진양메인티넌스<br> 우:137-070 <br> 서울 서초구 서초동 1535-3번지 선흥빌딩 4층 <br> (T)02)522-3611 (f)02)522-3615 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_99.jpg" width="68" height="75" alt="" /></td>
								<td>이현종</td>
								<td>(주)신성비엠씨<br> 우:157-861 <br> 서울 강서구 염창동 240-21우림블루나인 비즈니스센터 B-2003 <br> (T)02)2659-0355 (f)02)2659-0393 </td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_100.jpg" width="68" height="75" alt="" /></td>
								<td>박일수</td>
								<td>한국필맙(주)<br> 우:121-270 <br> 서울 마포구 상암동 상암근린상가 301호 <br> (T)02)372-7641 (f)02)374-2914 </td>
							</tr>

							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[20대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">직위</th>
								<th scope="col">사진</th>
								<th scope="col">성명</th>
								<th scope="col">회사명.주소.연락처</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_140.jpg" width="68" height="75" alt="" /></td>
								<td>송문현</td>
								<td>우지기업(주)<br>우:03169 <br> 서울 종로구 사직로 10길 17 인왕빌딩 2층<br>(T) 02)737-8822 (F) 02)739-7027 </td>
							</tr>
							<tr>
								<td>감사</td>
								<td><img src="/static/main/img/org/ob_vip/head_141.jpg" width="68" height="75" alt="" /></td>
								<td>김영선</td>
								<td>(주)화목<br> 우:14082  <br> 경기 안양시 경수대로 651번길 57 A동 1층 3호 <br> (T) 031)455-9377 (F) 031)457-3357</td>
							</tr>
							<tr>
								<td>감사</td>
								<td><img src="/static/main/img/org/ob_vip/head_142.jpg" width="68" height="75" alt="" /></td>
								<td>홍종열</td>
								<td>(주)길산종합개발<br> 우:02582  <br> 서울 동대문구 왕산로2길 32 금성빌딩 302호  <br> (T) 02)923-3250 (F) 02)953-0926

								</td>
							</tr>
							<tr>
								<td>수석 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_143.jpg" width="68" height="75" alt="" /></td>
								<td>지장용</td>
								<td>미림개발(주)<br> 우:03151  <br> 서울 종로구 종로5길 58 석탄회관 빌딩 3층  <br> (T) 02)739-9535 (F) 02)723-5770

								</td>
							</tr>
							<tr>
								<td>정책 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_145.jpg" width="68" height="75" alt="" /></td>
								<td>박기준</td>
								<td>(주)씨앤에스자산관리<br> 우:03170  <br> 서울 종로구 사직로 130 적선현대 제1빌딩 907호  <br> (T) 02)732-9676 (F) 02)733-3829

								</td>
							</tr>
							<tr>
								<td>사업 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_146.jpg" width="68" height="75" alt="" /></td>
								<td>이용문</td>
								<td>(주)고암 <br> 우:07222 <br> 서울 영등포구 당산로47길 19 고암빌딩 7층 <br> (T) 02)326-3011 (F) 02)326-3010

								</td>
							</tr>
							<tr>
								<td>조정 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_147.jpg" width="68" height="75" alt="" /></td>
								<td>전용수</td>
								<td>신우산업관리(주)<br> 우:150-040  <br> 서울 영등포구 당산로 171 금강펜테리움IT타워 1101호  <br> (T) 02)587-7691 (F) 02)587-7690

								</td>
							</tr>
							<tr>
								<td>국제 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_148.jpg" width="68" height="75" alt="" /></td>
								<td>이현종</td>
								<td>(주)신성BMC<br> dn:07547 <br> 서울 강서구 양천로 583 우림블루나인 비즈니스센터 B-2003  <br> (T) 02)2659-0355 (F) 02)2659-0393

								</td>
							</tr>
							<tr>
								<td>교육 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>강규익</td>
								<td>(주)한미기술<br> 우:06098  <br> 서울 강남구 학동로 44길 6모아빌딩 203호  <br> (T) 02)548-3888 (F) 02)542-3880

								</td>
							</tr>
							<tr>
								<td>홍보 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_25.jpg" width="68" height="75" alt="" /></td>
								<td>이재구</td>
								<td>(주)태성공사<br> 우:08722  <br> 서울 관악구 보라매로 12 농협빌딩 501호  <br> (T) 02)871-8545 (F) 02)873-7434

								</td>
							</tr>
							<tr>
								<td>서울 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_150.jpg" width="68" height="75" alt="" /></td>
								<td>정종범</td>
								<td>(주)순일기업<br> 우:06938  <br> 서울 동작구 노량진로 26 솔표빌딩 3층  <br> (T) 02)823-1311 (F) 02)823-1811

								</td>
							</tr>
							<tr>
								<td>지회 부회장(경기지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_151.jpg" width="68" height="75" alt="" /></td>
								<td>최해진</td>
								<td>(주)신강개발<br> 우:15571  <br> 경기 안산시 상록구 이호로 68-1 명성빌딩 지하1층 <br> (T) 031)416-2845 (F) 031)416-2846

								</td>
							</tr>
							<tr>
								<td>노동 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_52.PNG" width="68" height="75" alt="" /></td>
								<td>신양주</td>
								<td>(주)프로에스콤 <br> 우:06211 <br> 서울 강남구 테헤란로 322 한신인터벨리24 서관 20층 <br> (T) 02)2183-0100 (F) 02)2183-0140

								</td>
							</tr>
							<tr>
								<td>환경 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_152.jpg" width="68" height="75" alt="" /></td>
								<td>진인은</td>
								<td>(주)진은종합개발<br> 우:07222  <br> 서울 영등포구 양평로14길 4 환희빌딩 3층  <br> (T) 02)2631-3954 (F) 02)2631-3964

								</td>
							</tr>
							<tr>
								<td>이사(부산울산경남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_153.jpg" width="68" height="75" alt="" /></td>
								<td>진영산</td>
								<td>현대종합용역(주)<br> 우:48924  <br> 부산광역시 중구 중앙대로 131(센트럴오피스텔 1107호)  <br> (T) 051)469-3568 (F) 051)465-1743

								</td>
							</tr>
							<tr>
								<td>이사(대전충남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_154.jpg" width="68" height="75" alt="" /></td>
								<td>신대식</td>
								<td>(주)미도산업<br>우:35027  <br> 대전 중구 대종로 333번길 15 301호  <br> (T) 042)256-8998 (F) 042)221-8998

								</td>
							</tr>
							<tr>
								<td>이사(인천지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_155.PNG" width="68" height="75" alt="" /></td>
								<td>김용호</td>
								<td>고산실업(주)<br>우:21119  <br> 인천광역시 계양구 효서로 300 삼호프라자 203호  <br> (T) 032)888-0415 (F) 032)888-0416

								</td>
							</tr>
							<tr>
								<td>이사(광주전남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_156.jpg" width="68" height="75" alt="" /></td>
								<td>김육형</td>
								<td>현대환경개발(주)<br> 우:61420  <br> 광주 동구 중흥로 237 의재회관 5층  <br> (T) 062)226-5400 (F) 062)227-2600

								</td>
							</tr>
							<tr>
								<td>이사(전북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_157.jpg" width="68" height="75" alt="" /></td>
								<td>이종택</td>
								<td>(유)삼문개발<br> 우:54931  <br> 전주 덕진구 기린대로 418 전국일보사건물 9층  <br>(T) 063)251-0611 (F) 063)271-0612

								</td>
							</tr>
							<tr>
								<td>이사(대구지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_158..PNG" width="68" height="75" alt="" /></td>
								<td>장세환</td>
								<td>(주)진성티에스<br> 우:42795 <br> 대구 달서구 월곡로 141 장수빌딩 4층  <br> (T) 053)633-0058 (F) 053)633-0242


								</td>
							</tr>
							<tr>
								<td>이사(충북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_159.JPG" width="68" height="75" alt="" /></td>
								<td>임헌표</td>
								<td>(주)현대산업관리<br> 우:28562  <br> 청주시 흥덕구 1순환로 607 3층  <br> (T) 043)268-4411 (F) 043)268-4414

								</td>
							</tr>
							<tr>
								<td>이사(제주지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_160.PNG" width="68" height="75" alt="" /></td>
								<td>전문수</td>
								<td>(주)삼익<br> 우:63124  <br> 제주시 신대로 12길 35 2층  <br> (T) 064)747-2601 (F) 064)747-2616

								</td>
							</tr>
							<tr>
								<td>이사(경북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_161.PNG" width="68" height="75" alt="" /></td>
								<td>김형출</td>
								<td>(주)화진산업<br> 우:39233  <br> 경북 구미시 송원동로 72 터미널상가2층 1,2호  <br> (T) 054)465-1722 (F) 054)465-1723

								</td>
							</tr>
							<tr>
								<td>이사(강원지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_162.jpg" width="68" height="75" alt="" /></td>
								<td>박용준</td>
								<td>승화(유)<br> 우:25621  <br> 강원 강릉시 구정면 남밭길 23-7  <br> (T) 033)610-6105 (F) 033)645-5861

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_163.jpg" width="68" height="75" alt="" /></td>
								<td>장인수</td>
								<td>삼호실업(주)<br> 우:07327  <br> 서울 영등포구 여의나루로 71 동화빌딩 1403호  <br> (T) 02)782-2290 (F) 02)784-0636

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_50.PNG" width="68" height="75" alt="" /></td>
								<td>성정헌</td>
								<td>상명진흥(주)<br> 우:02635  <br> 서울 동대문구 천호대로79길 35 에스엠타운 2층  <br> (T) 02)2243-1366 (F) 02)2245-9063

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_44.jpg" width="68" height="75" alt="" /></td>
								<td>조경식</td>
								<td>(주)삼보기획<br> 우:02478  <br> 서울 동대문구 왕산로 91 금성빌딩 502호  <br> (T) 02)923-4768 (F) 02)925-3294

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_164.jpg" width="68" height="75" alt="" /></td>
								<td>이상복</td>
								<td>(주)미동기술<br> 우:05567  <br> 서울 송파구 삼전로9길 19 3층 301호  <br> (T) 02)521-9985 (F) 02)521-9986

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_165.jpg" width="68" height="75" alt="" /></td>
								<td>신황철</td>
								<td>두신산업(주)<br> 우:01081  <br> 서울 강북구 덕름로19길 8 3층  <br> (T) 02)997-0227 (F) 02)997-2465

								</td>
							</tr><tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_166.jpg" width="68" height="75" alt="" /></td>
								<td>박광수</td>
								<td>금학개발(주)<br> 우:06061  <br> 서울 강남구 학동로45길 7 삼영빌딩 403호  <br> (T) 02)549-1906 (F) 02)541-6622

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_167.jpg" width="68" height="75" alt="" /></td>
								<td>조남각</td>
								<td>(주)백제기업<br> 우:04159 <br> 서울 마포구 대흥로 36-7 신석빌딩 2층  <br> (T) 02)718-8855 (F) 02)716-9514

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_48.jpg" width="68" height="75" alt="" /></td>
								<td>안무정</td>
								<td>(주)신한공사<br> 우:02151  <br> 서울 중랑구 상봉로 117  <br> (T) 02)496-4383 (F) 02)494-5026

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_195.jpg" width="68" height="75" alt="" /></td>
								<td>최우영</td>
								<td>맥서브(주)<br> 우:06180  <br> 서울 강남구 영동대로 85길 28 성원타워 1  <br> (T) 02)2015-0800 (F) 02)2015-0859

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_168.jpg" width="68" height="75" alt="" /></td>
								<td>홍진학</td>
								<td>(주)대종종합관리<br> 우:34629  <br> 대전 동구 대전로815번길 45 신여이딩 502호  <br> (T) 042)621-2613 (F) 042)621-2614

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_169.jpg" width="68" height="75" alt="" /></td>
								<td>이영석</td>
								<td>태진자산관리(주)<br> 우:05035  <br> 서울 광진구 아차산로 62길 8 태진빌딩  <br> (T) 02)458-7517 (F) 02)3437-6434

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_170.jpg" width="68" height="75" alt="" /></td>
								<td>이준호</td>
								<td>(주)보경실업<br> 우:04096 <br> 서울 마포구 독막로 241 금성빌딩 503호  <br> (T) 02)718-4221 (F) 02)718-4223

							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_198.jpg" width="68" height="75" alt="" /></td>
								<td>이용준</td>
								<td>(주)광진씨엔에스<br> 우:07350  <br> 서울 영등포구 도신로 60길 1 남영빌딩 4층  <br> (T) 02)832-6206 (F) 02)832-6207

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_161.jpg" width="68" height="75" alt="" /></td>
								<td>정상원</td>
								<td>(주)진양메인티넌스<br> 우:06670  <br> 서울 서초구 반포대로 59 선흥빌딩 4층  <br> (T) 02)3019-6114 (F) 02)522-3615

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_162.PNG" width="68" height="75" alt="" /></td>
								<td>이영호</td>
								<td>(주)대한안전관리공사<br> 우:04174  <br> 서울 마포구 마포대로 44 진도빌딩 10층 1호  <br> (T) 02)716-1343 (F) 02)716-1336

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_171.jpg" width="68" height="75" alt="" /></td>
								<td>박일수</td>
								<td>한국필맙(주)<br> 우:10542  <br> 경기 고양시 덕양구 해포길 50-1 2층  <br> (T) 02)372-7641 (F) 02)374-2914

								</td>
							</tr>
							</tbody>
						</table>
					</div>

					<div class="exChooseCont" style="display:none">
						<div class="orgTitle">[21대]</div>
						<table class="orgMargin" summary="" >
							<colgroup>
								<col width="150"/><col width="100"/>
								<col width="150"/>
								<col width=""/>
							</colgroup>
							<thead>
							<tr>
								<th scope="col">직위</th>
								<th scope="col">사진</th>
								<th scope="col">성명</th>
								<th scope="col">회사명.주소.연락처</th>
							</tr>
							</thead>

							<tbody>

							<tr>
								<td>회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_23.JPG" width="68" height="75" alt="" /></td>
								<td>강규익</td>
								<td> (주)한미기술  <br> 우:06098   <br> 서울특별시 강남구 학동로44길 6 203호(논현동, 모아빌딩)   <br> (T) 02) 548-3888 (F) 02) 542-3880

								</td>
							</tr>
							<tr>
								<td>감사</td>
								<td><img src="/static/main/img/org/ob_vip/head_172.jpg" width="68" height="75" alt="" /></td>
								<td>김영선</td>
								<td>(주)화목   <br> 우:14109   <br> 경기도 안양시 동안구 흥안대로145번길 102-3, 2층(호계동)   <br> (T) 031) 455-9377 (F) 031) 457-3357

								</td>
							</tr>
							<tr>
								<td>감사</td>
								<td><img src="/static/main/img/org/ob_vip/head_173.jpg" width="68" height="75" alt="" /></td>
								<td>이현종</td>
								<td>(주)신성비엠씨   <br> 우:07547   <br> 서울특별시 강서구 양천로 583, B동 2003호(우림블루나인 비즈니스 센터)   <br> (T) 02) 2659-0355 (F) 02) 2659-0393

								</td>
							</tr>
							<tr>
								<td>총무 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_174.jpg" width="68" height="75" alt="" /></td>
								<td>이영석</td>
								<td> 태진자산관리(주)   <br> 우:05047   <br> 서울특별시 광진구 아차산로62길 8, 태진빌딩 2층 201호(구의동)   <br> (T) 02) 458-7517 (F) 02) 3437-6434

								</td>
							</tr>
							<tr>
								<td>정책 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_175.jpg" width="68" height="75" alt="" /></td>
								<td>홍종열</td>
								<td>길산종합개발(주)   <br> 우:08294   <br> 서울특별시 구로구 구로중앙로28길 35-2(구로동 보성빌딩 301호)   <br> (T) 02) 923-3251 (F) 02) 953-0926

								</td>
							</tr>
							<tr>
								<td>사업 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_52.PNG" width="68" height="75" alt="" /></td>
								<td>신양주</td>
								<td>(주)프로에스콤   <br> 우:06211   <br> 서울특별시 강남구 테헤란로 322(역삼동, 한신인터벨리24 서관 20층)   <br> (T) 02) 2183-0100 (F) 02) 2183-0140

								</td>
							</tr>
							<tr>
								<td>조정 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_176.jpg" width="68" height="75" alt="" /></td>
								<td>전용수</td>
								<td>신우산업관리(주)   <br>우:07217   <br> 서울특별시 영등포구 당산로 171,1101호(금강펜테리움 IT타워)   <br> (T) 02) 587-7691 (F) 02) 587-7690

								</td>
							</tr>
							<tr>
								<td>국제 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_177.jpg" width="68" height="75" alt="" /></td>
								<td>정상원</td>
								<td>진양메인티넌스(주)   <br> 우:06670   <br> 서울특별시 서초구 반포대로 59(선흥빌딩 4층)   <br></td>
							</tr>
							<tr>
								<td>교육 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_162.PNG" width="68" height="75" alt="" /></td>
								<td>이영호</td>
								<td>(주)대한안전관리공사   <br> 우:04174   <br> 서울특별시 마폭 마포대로 44, 10층 1호(도화동, 진도빌딩)   <br> (T) 02) 716-1343 (F) 02) 716-1336

								</td>
							</tr>
							<tr>
								<td>홍보 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_178.jpg" width="68" height="75" alt="" /></td>
								<td>이준호</td>
								<td>(주)보경실업   <br> 우:03504   <br> 서울특별시 은평구 수색로 217-1 405호(증산동, DMC 자이2단지)   <br> (T) 02) 718-4221 (F) 02) 718-4223

								</td>
							</tr>
							<tr>
								<td>재무 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_179.jpg" width="68" height="75" alt="" /></td>
								<td>도태운</td>
								<td>(주)브리콤   <br> 우:06745   <br> 서울특별시 서초구 강남대로30길 36(양재동, 명헌빌딩 2층)   <br> (T) 02) 564-0077 (F) 02) 2057-2149

								</td>
							</tr>
							<tr>
								<td>환경 부회장</td>
								<td><img src="/static/main/img/org/ob_vip/head_180.jpg" width="68" height="75" alt="" /></td>
								<td>이용문</td>
								<td>(주)고암   <br> 우:07222   <br> 서울특별시 영등포구 당산로47길 19(당산동6가, 고암빌딩 7층)   <br> (T) 02) 326-3011 (F) 02) 326-3010

								</td>
							</tr>
							<tr>
								<td>이사(부산울산경남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_1.PNG" width="68" height="75" alt="" /></td>
								<td>박건석</td>
								<td> (주)신청  <br> 우:44228   <br> 울산광역시 북구 호계로 283, 지상4층  <br> (T) 052) 297-9900 (F) 052) 296-6655

								</td>
							</tr>
							<tr>
								<td>이사(대전세종충남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_181.jpg" width="68" height="75" alt="" /></td>
								<td>이정호</td>
								<td> 정원기업(자)  <br> 우:35319   <br> 대전광역시 서구 변동중로 61-1(변동)   <br> (T) 042) 522-7777 (F) 042) 522-0361

								</td>
							</tr>
							<tr>
								<td>이사(인천지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_155.PNG" width="68" height="75" alt="" /></td>
								<td>김용호</td>
								<td>고산실업(주)   <br> 우:21119   <br> 인천광역시 계양구 효서로 300, 203호(작전동, 삼호프라자)   <br> (T) 032) 556-4503 (F) 032) 556-4505

								</td>
							</tr>
							<tr>
								<td>이사(광주전남 지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_182.jpg" width="68" height="75" alt="" /></td>
								<td>김육형</td>
								<td>현대환경개발(주)   <br> 우:58242   <br> 전라남도 나주시 노안삼도로 110-46(송촌동)   <br> (T) 061) 336-3378 (F) 062) 227-2600

								</td>
							</tr>
							<tr>
								<td>이사(전북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_183.jpg" width="68" height="75" alt="" /></td>
								<td>이종택</td>
								<td>(유)삼문개발   <br> 우:54931  <br> 전라북도 전주시 덕진구 기린대로 418,9층(금암동, 전북일보사건물)   <br> (T) 063) 251-0611 (F) 063) 271-0612

								</td>
							</tr>
							<tr>
								<td>이사(대구지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_184.jpg" width="68" height="75" alt="" /></td>
								<td>이서준</td>
								<td>대한방역공사   <br> 우:42189   <br> 대구광역시 수성구 지산로 67   <br> (T) 053) 764-2623 (F) 053) 764-2887

								</td>
							</tr>
							<tr>
								<td>이사(충북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_185.JPG" width="68" height="75" alt="" /></td>
								<td>임헌표</td>
								<td>(주)현대산업관리   <br> 우:28562  <br> 충청북도 청주시 흥덕구 1순환로 607, 3층   <br> (T) 043) 263-6363 (F) 043) 268-4414

								</td>
							</tr>
							<tr>
								<td>이사(제주지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_12.PNG" width="68" height="75" alt="" /></td>
								<td>전문수</td>
								<td>(주)삼익   <br> 우:63124   <br> 제주특별자치도 제주시 신대로12길 35, 2층(연동)   <br> (T) 064) 747-2601 (F) 064) 747-2616

								</td>
							</tr>
							<tr>
								<td>이사(경북지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_161.PNG" width="68" height="75" alt="" /></td>
								<td>김형출</td>
								<td> 화진산업(주)  <br> 우:39233   <br> 경상북도 구미시 송원동로 72 (터미널상가2층 1,2호)<br> (T) 054) 456-1722 (F) 054) 456-1723

								</td>
							</tr>
							<tr>
								<td>이사(경기지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_2.PNG" width="68" height="75" alt="" /></td>
								<td>김상현</td>
								<td>(주)가나티엠   <br> 우:18119   <br> 경기도 오산시 법원로 26, 4층 403호(청학동, 강남프라자)   <br> (T) 031) 377-2846 (F) 031) 377-1165

								</td>
							</tr>
							<tr>
								<td>이사(강원지회장)</td>
								<td><img src="/static/main/img/org/ob_vip/head_186.jpg" width="68" height="75" alt="" /></td>
								<td>이대화</td>
								<td>주식회사대신주택관리   <br> 우:26487  <br> 강원도 원주시 서원대로 500,411~412호(프리미엄 아울렛)   <br> (T) 033) 765-9880 (F) 033) 762-9870

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_50.PNG" width="68" height="75" alt="" /></td>
								<td>성정헌</td>
								<td>상명진흥(주)   <br> 우:02635   <br> 서울특별시 동대문구 천호대로 79길 35, 2층(에스엠타운)   <br> (T) 02) 2243-1366 (F) 02) 2245-9063

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_187.jpg" width="68" height="75" alt="" /></td>
								<td>신황철</td>
								<td>두신산업(주)   <br> 우:01081   <br> 서울특별시 강북구 덕릉로19길 8,3층(수유동)   <br> (T) 02) 997-0227 (F) 02) 997-2465

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_188.jpg" width="68" height="75" alt="" /></td>
								<td>박광수</td>
								<td>금학개발(주)   <br> 우:06061   <br> 서울특별시 강남구 학동로 45길 7(논현동, 삼영빌딩 403호)   <br></td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_189.jpg" width="68" height="75" alt="" /></td>
								<td>조남각</td>
								<td>(주)백제기업   <br> 우:04159   <br> 서울특별시 마포구 대흥로 36-7(신석빌딩 2층)   <br> (T) 02) 718-8855 (F) 02) 716-9514

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_190.jpg" width="68" height="75" alt="" /></td>
								<td>정종범</td>
								<td>(주)순일기업   <br> 우:06938  <br> 서울특별시 동작구 노량진로 26, 솔표빌딩 3층 (대방동)   <br> (T) 02) 823-1311 (F) 02) 823-1811

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_158..PNG" width="68" height="75" alt="" /></td>
								<td>장세환</td>
								<td>(주)진성티에스   <br> 우:42795   <br> 대구광역시 달서구 월곡로 141(장수빌딩 4층)   <br> (T) 053) 633-0058 (F) 053) 633-0242

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_191.jpg" width="68" height="75" alt="" /></td>
								<td>양해승</td>
								<td>(주)만비종합관리   <br> 우:46301   <br> 부산광역시 금정구 온천장로 148,1층(장전동, 효산빌딩)   <br> (T) 051) 507-6071 (F) 051) 507-6073

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_3.PNG" width="68" height="75" alt="" /></td>
								<td>김정숙</td>
								<td>(주)삼정기업   <br> 우:37562   <br> 경상북도 포항시 북구 홍해읍 도음로 917번길 24, 삼정빌딩 3층   <br> (T) 054) 242-3400 (F) 054) 242-5511

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_192.jpg" width="68" height="75" alt="" /></td>
								<td>최해진</td>
								<td>(주)신강개발   <br> 우:15571   <br> 경기도 안산시 상록구 이호로 68-1,3층 301호(본오동, 명성빌딩)   <br> (T) 031) 416-2845 (F) 031) 416-2846

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_4.PNG" width="68" height="75" alt="" /></td>
								<td>이승열</td>
								<td>(주)영창   <br> 우:28541   <br> 충청북도 청주시 상당구 교서로 80-1, 3층(영동)   <br> (T) 043) 255-7757 (F) 043) 254-8857

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_5.PNG" width="68" height="75" alt="" /></td>
								<td>이윤기</td>
								<td>(주)한국방제산업  <br> 우:35390   <br> 대전광역시 서구 가수원로 43(가수원동, 중부빌딩 5층)   <br> (T) 042) 544-3283 (F) 042) 544-3284

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_193.jpg" width="68" height="75" alt="" /></td>
								<td>오성민</td>
								<td> (주)에스엠파트너  <br> 우:07345   <br> 서울특별시 영등포구 63로 40, 633호(여의도동, 라이프오피스텔)   <br> (T) 02) 784-5592 (F) 02) 784-0696

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_6.PNG" width="68" height="75" alt="" /></td>
								<td>김성환</td>
								<td>(주)에스티환경   <br> 우:17762   <br> 경기도 평택시 송탄로 366, 1동 2층(지산동)   <br> (T) 031) 663-9500 (F) 031) 662-1133

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_7.PNG" width="68" height="75" alt="" /></td>
								<td>정재현</td>
								<td>(주)매직오피스   <br> 우:02593   <br> 서울특별시 동대문구 황물로 42,3층 2호(답십리동)   <br> (T) 02) 3394-6353 (F) 02) 3394-4035

								</td>
							</tr>
							<tr>
								<td>이사</td>
								<td><img src="/static/main/img/org/ob_vip/head_202.PNG" width="68" height="75" alt="" /></td>
								<td>고재정</td>
								<td>(주)원중기업   <br> 우:21578   <br> 인천광역시 남동구 인주대로 670, 901호(구월동, 인향빌딩)   <br> (T) 032) 467-6060 (F) 032) 466-4302

								</td>

							</tbody>
						</table>
					</div>
				</div>
				
			</div>
		</div>

		<style type="text/css">
		<!--
			#.obChoose				{overflow:hidden; padding-left:18px; margin-top:10px;  border-bottom:1px solid #3f97e3; }
			.obChoose,.obChooseList {overflow:hidden; padding-left:18px; margin-top:10px;  }
			.obChoose	span		{margin-right:20px;}

		//-->
		</style>



<script language="javascript" type="text/javascript">
//<![CDATA[
var tab = $("#originTab a")
var cont = $(".originTabCont")
$("#originTab a").each(function(n){
	this.n=n;
}).click(function(){
	tab.removeClass("on").eq(this.n).addClass("on");
	cont.hide().eq(this.n).show();
	//탭 클릭시 역대회장/역대임원 첫 이미지로 
	$("#ob_vip_list").attr("src", "/static/main/img/org/ob_vip/m02_1_co.jpg");					
	$("#ob_executives_list").attr("src", "/static/main/img/org/ob_executives/m03_1_co.jpg");					

	return false;
});


var tab2 = $(".obChoose a")
var cont2 = $(".obChooseCont")
$(".obChoose a").each(function(n){
	this.n=n;
}).click(function(){
	//tab2.removeClass("on").eq(this.n).addClass("on");
	cont2.hide().eq(this.n).show();
	return false;
});

var tab3 = $(".exChoose a")
var cont3 = $(".exChooseCont")
$(".exChoose a").each(function(n){
	this.n=n;
}).click(function(){
	cont3.hide().eq(this.n).show();
	return false;
});



//]]>
</script>

</body>
</html>