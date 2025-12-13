<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>    
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>

<body>

<div id="content">
<!-- start :: content -->
<!-- start :: content -->


	<div class="location_top">
	</div>
	<div class="location_content">
		<div class="mapWrap" id="map" style="width:100%;height:350px;">
		
		<!-- * 카카오맵 - 지도퍼가기 -->
<!-- 1. 지도 노드 -->
<div id="daumRoughmapContainer1568626192482" class="root_daum_roughmap root_daum_roughmap_landing"></div>

<!--
	2. 설치 스크립트
	* 지도 퍼가기 서비스를 2개 이상 넣을 경우, 설치 스크립트는 하나만 삽입합니다.
-->
<script charset="UTF-8" class="daum_roughmap_loader_script" src="https://ssl.daumcdn.net/dmaps/map_js_init/roughmapLoader.js"></script>

<!-- 3. 실행 스크립트 -->
<script charset="UTF-8">
	new daum.roughmap.Lander({
		"timestamp" : "1568626192482",
		"key" : "v2ki",
		"mapWidth" : "898",
		"mapHeight" : "348"
	}).render();
</script>

		</div>
<!-- 		
		<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=eb3a5afae1011e80690cfede67fbe2a9"></script>

<script>
		var container = document.getElementById('map');
		var options = {
			center: new kakao.maps.LatLng(33.450701, 126.570667),
			level: 3
		};

		var map = new kakao.maps.Map(container, options);
		
		// 마커가 표시될 위치입니다 
		var markerPosition  = new kakao.maps.LatLng(33.450701, 126.570667); 

		// 마커를 생성합니다
		var marker = new kakao.maps.Marker({
		    position: markerPosition
		});

		// 마커가 지도 위에 표시되도록 설정합니다
		marker.setMap(map);

		var iwContent = '<div style="padding:5px;">한국건물위생관리협회</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
		    iwPosition = new kakao.maps.LatLng(33.450701, 126.570667); //인포윈도우 표시 위치입니다

		// 인포윈도우를 생성합니다
		var infowindow = new kakao.maps.InfoWindow({
		    position : iwPosition, 
		    content : iwContent 
		});
		  
		// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
		infowindow.open(map, marker); 
	</script> -->
	
		<ul class="location_cont">
			<li>
				<strong>오시는 길</strong>
				<div class="">
					<em>주소:</em> 서울시 성동구 성수동2가 273-24 번지 경협회관 6층<br />
					<em>대표전화 :</em> 02-465-5900
				</div>
			</li>
			<li>
				<strong>지하철로 오실 때</strong>
				<p>2호선 성수역 하차 3번출구</p>
				<div class="">
					(시청쪽에서 승차시는 첫째칸, 잠실쪽에서 승차시는 맨 뒷칸을 이용)
				</div>
			</li>
			<li>
				<strong>기차로 오실 때</strong>
				<p>서울역 > 성수역(약23분 소요)</p>
				<div class="">
					지하철 4호선 서울역 승차 후 동대문역사문화공원역에서 2호선으로 환승 성수역 하차<br />
					지하철 1호선 서울역 승차 후 시청역에서 2호선으로 환승 성수역에서 하차
				</div>
			</li>
			<li>
				<strong>고속버스로 오실 때</strong>
				<p>강남고속버스터미널역 > 성수역(약21분 소요)</p>
				<div class="">
					지하철 7호선 승차 후 건대입구역에서 2호선으로 갈아타서 성수역 하차<br />
					동서울고속버스터미널(강변역) > 성수역( 약6분 소요 )<br />
					지하철 2호선 승차 후 성수역에서 하차
				</div>
			</li>
			<li>
				<strong>비행기로 오실 때</strong>
				<p>김포공항역 > 성수역(약59분 소요)</p>
				<div class="">
					5호선 김포공항역에서 승차 후 영등포구청 2호선으로 환승 성수역에서 하차
				</div>
			</li>
		</ul>
	</div>

<!-- end :: content -->
<!-- end :: content -->
</div>


</body>
</html>