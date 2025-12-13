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
<script language="javascript" type="text/javascript">
//<![CDATA[


jQuery(document).ready(function(){
	var tab = $("#ji_tab li a");

	$("#ji_tab li a").each(function(n){
		this.n=n;
	}).click(function(){
		tab.removeClass("on").eq(this.n).addClass("on");
		var scode = tab.eq(this.n).attr("val"); //지회 코드
		
		//탭 클릭 시 조회 조건클리어 > 검색
		//$("#search_gbn_hcode").val("10").attr("selected","selected");   //일반회원
		$("#search_gbn_jicode").val(scode).attr("selected","selected");    //선택된 지회로 콤보 설정
		$("#search_gbn_option").val("company").attr("selected","selected");   //상호명
		$("#search_value").val("");  //조회조건

		goSearch(false);
	});

	//디폴트 지회를 주려면
	/*
	$("#search_gbn_jicode").val('10').attr("selected","selected"); 
	goSearch(false);
	*/

	//검색 버튼 누른 효과
	goSearch(true);  
});




//isScodeSelect == true:지회 탭 변경되도록 한다, false: 지회탭 그대로 둔다
function goSearch(isScodeSelect){

	//검색 버튼을 눌러서 검색을 하면 지회 탭을 활성화 시켜 준다.
	if(isScodeSelect){
		$("#ji_tab li a").removeClass("on");		

		if($("#search_gbn_jicode").val() != '%' ){

			$("#ji_tab li a[val='" + $("#search_gbn_jicode").val() + "']").addClass("on");
		}
	}

	js_list(1);
}




function js_list(cur_pg)
{	
	$('#cur_pg').val(cur_pg);

	var _url = "/info/action/info.jspx?cmd=getMBMemberList";	

	//alert(jQuery("#search_form").serialize());


	var http = jQuery.ajax( {
   		url: _url,	
   		datatype : 'json',
   		data : jQuery("#search_form").serialize(), 
		mtype: 'POST',
   		async : false,	   		
   		error 	: function(xhr)
   		{
			alert(xhr.status);
		},
		success: function(Json)
		{			
			var code = Json.result.code;
	   		var msg =  Json.result.msg;
	   		var data = Json.result.data;

			if( Json.result.data.property[0].tot_cnt )
			{
					$('#tot_cnt').val(Json.result.data.property[0].tot_cnt);
			} else {
					$('#tot_cnt').val(0);
			}
			
	   		$("#listData > tr").remove();
	   		var listDataRows=[];

	   		

	   		if (code == 200){		

	   			for(var i=0 ; i < data.mbmaster_list.length ; i++)
	   			{

	   				var tdData = "";
	   				tdData = "<td>"+ data.mbmaster_list[i].ILL_NO +"</td>" ; 
	   				tdData += "<td>"+ data.mbmaster_list[i].COMPANY +"</td>" ; 
	   				tdData += "<td>"+ data.mbmaster_list[i].RPRSTT_KR +"</td>" ; 
	   				tdData += "<td class='tit'>"+ data.mbmaster_list[i].CP_ADDR + data.mbmaster_list[i].CP_JIBUN +"</td>" ; 
	   				tdData += "<td>"+ data.mbmaster_list[i].CPTEL +"</td>" ; 
	   				tdData += "<td>"+ data.mbmaster_list[i].CPFAX +"</td>" ; 


	   				listDataRows.push("<tr>" + tdData + "</tr>");
	   			}

	   			if(data.mbmaster_list.length == 0 )
	   				listDataRows.push("<tr><td colspan='6' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 

	   		}else{
	   			listDataRows.push("<tr><td colspan='6' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
	   		}

			$("#listData").html(listDataRows.join());
			 
			js_userPaging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#row_per_page').val(), 'js_list');	
		}			
	} );
}


//]]>
</script>
</head>

<body>

<div id="content">
<!-- start :: content -->
<!-- start :: content -->
<form id="search_form" onsubmit='return false;'>
			<input type="hidden" name="cur_pg" id="cur_pg" value="1">
			<input type="hidden" name="row_per_page" id="row_per_page" value="10">
			<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>

	<div class="topSearch">
		<div class="">			
			<select name="search_gbn_hcode" id="search_gbn_hcode" title="">
				<option value="10" selected="selected">회원</option>
				<option value="20">일반회원</option>
				<option value="30">특별회원</option>
				<option value="40">특별비회원</option>
			</select>


			
			<select name="search_gbn_jicode" id="search_gbn_jicode" title="">
				
				<option value="%" selected="selected">지회선택</option>
				<c:forEach begin="0" end="${output_count}" step="1" var="idx">
					<option value="${output[idx].jicode}">${output[idx].company}</option>
				</c:forEach>
				<!--
				<option value="0010">서울지회:0010</option>
				<option value="0020">경기지회:0020</option>
				<option value="0030">부산,울산,경남지회:0030</option>
				-->
			</select>
			<select name="search_gbn_option" id="search_gbn_option" title="">
				<option value="company" selected="selected">상호명</option>
				<option value="rprstt_kr">대표자명</option>
				<option value="add">주소</option>
				<option value="cptel">전화번호</option>
				<option value="cdfax">팩스</option>
			</select>
			<input name="search_value" id="search_value" type="text" class="it mid " title="" value="" />
			<a href="#" class="pbtn05" onclick="javascript:goSearch(true)"><span>검색</span></a>
		</div>
	</div>
</form>

	<div class="tabWrap">
		<ul class="st_2" id="ji_tab">
			


			<c:forEach begin="0" end="${output_count}" step="1" var="idx">
				<li><a href="#" val="${output[idx].jicode}"><span class="">${output[idx].company}</span></a></li>
			</c:forEach>
<!--
			<li><a href="#" class="on" val="0010"><span class="">서울지회</span></a></li>
			<li><a href="#" val="0020"><span class="">경기지회</span></a></li>
			<li><a href="#" val="0030"><span class="">부산,울산,경남지회</span></a></li>
			<li><a href="#"><span class="">대전,세종,충남지회</span></a></li>
			<li><a href="#"><span class="">광주,전남지회</span></a></li>
			<li><a href="#"><span class="">전북지회</span></a></li>
			<li><a href="#"><span class="">대구지회</span></a></li>
			<li><a href="#"><span class="">충북지회</span></a></li>
			<li><a href="#"><span class="">제주지회</span></a></li>
			<li><a href="#"><span class="">경북지회</span></a></li>
			<li><a href="#"><span class="">강원지회</span></a></li>
			<li><a href="#"><span class="">인천지회</span></a></li>
			<li><a href="#"><span class="">자격정지</span></a></li>
-->
		</ul>
	</div>
	

	<div class="list">
		<table cellpadding="0" cellspacing="0" class="st_2" summary="" >
			<caption></caption>
			<colgroup>
				<col width="10%"/><col width="15%"/><col width="10%"/><col width=""/>
				<col width="15%"/><col width="15%"/>
			</colgroup>
			<thead>
				<tr>
					<th>NO</th>
					<th>상호</th>
					<th>대표자</th>
					<th>주소</th>
					<th>전화번호</th>
					<th>팩스</th>
				</tr>
			</thead>
			<tbody id="listData">
					<tr><td colspan="6" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
			</tbody>			
		</table>
	</div>

	<div class="paging" id='pagingDiv'>
		<!--
		<a href="#" class="btn"><img src="/static/main/img/sub/paging_btn_1.jpg" alt="" /></a>
		<a href="#" class="btn"><img src="/static/main/img/sub/paging_btn_2.jpg" alt="" /></a>
		<span class="">
			<strong>1</strong>
			<a href="#">2</a>
			<a href="#">3</a>
			<a href="#">4</a>
			<a href="#">5</a>
			<a href="#">6</a>
			<a href="#">7</a>
			<a href="#">8</a>
			<a href="#">9</a>
		</span>
		<a href="#" class="btn"><img src="/static/main/img/sub/paging_btn_3.jpg" alt="" /></a>
		<a href="#" class="btn"><img src="/static/main/img/sub/paging_btn_4.jpg" alt="" /></a>
		-->
	</div>
		

	
<!-- end :: content -->
<!-- end :: content -->
</div>




</body>
</html>