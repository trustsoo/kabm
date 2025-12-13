<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>    
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>

<%
DataSet calcList = new DataSet();
try
{
	DataSet input = new DataSet();
	jdf.framework.core.data.InteractionBean interact = new jdf.framework.core.data.InteractionBean();
	input.put("board_row_per_page", 50);
	input.put("cur_pg", 1);
	input.put("brd_mng_no", 23);
	input.put("cmd", "getAlbumBoard");
	calcList = interact.execute("/community/board", input);	
} catch(Exception ex)
{}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
h1 {font-size: 2em;}
h2 {    font-size: 1.5em;}

.payTable_list2 ul li strong {
    display: block;
    height: 25px;
    font-size: 15px;
    padding: 4px 0 0 25px;
    line-height: 25px;
    color: #1e90f1;
    background: url(/static/main/img/sub/bul_notice.jpg) no-repeat 0 50%;
}
</style>
</head>

<body>

<div id="content">
<!-- start :: content -->
<!-- start :: content -->

	<div class="payTable_top">
	</div>
	<div class="payTable">
		<div class="tit">
			<strong>표준도급비산출표</strong>
		</div>
		<div class="payTable_list">
			<ul class="">
				<li>
					<strong>2024년(상반기) 건물위생관리업(청소) 표준도급비 산출기준표</strong>
					<p style="margin-top: 5px;">
						<a href="javascript:js_clickList('/static/html/pay_2024_1.html');">(1) 2024년도 상반기 도급비 산출표 </a><br />
						<a href="javascript:js_clickList('/static/html/pay_2024_2.html');">(2) 2024년도 상반기 도급비 산출표 세부내역  </a>
					</p>
				</li>
				
				<li>
					<strong>건축물 청소도급비 산출 기준</strong>
					<p style="margin-top: 5px;">
						<a href="javascript:js_clickList('/static/html/pay_2017_3.html');">(1) 산출구분 및 근거</a> <br />
						<a href="javascript:js_clickList('/static/html/pay_2017_4.html');">(2) 건물청소용역비 산출기준 총괄</a><br />
						<a href="javascript:js_clickList('/static/html/pay_2017_5.html');">- 인건비 산출기준</a><br />
						<a href="javascript:js_clickList('/static/html/pay_2017_6.html');">(3) 기타 </a>
					</p>
				</li>
				<li>
					<strong>건축물 용도별 1인 작업평수 기준</strong>
					<p style="margin-top: 5px;">
						<a href="javascript:js_clickList('/static/html/pay_2017_7.html');">건축물 용도별 1인작업 평수 기준 </a>
					</p>
				</li>
				<li class="list_type2">
					<strong>건축물 위생관리(청소) 도급비 산출내역표</strong>
					<table cellpadding="0" cellspacing="0" id="layerBtns" class="joinForm" summary="" style="margin-top: 5px;">
						<caption></caption>
						<colgroup>
							<col width=""/><col width=""/>
						</colgroup>
						</thead>
						<tbody>
							<tr>
								<td>2023 <a href="#/static/html/pay_2023_4.html">[하반기 세부내역]</a></td>
								<td>2023 <a href="#/static/html/pay_2023_2.html">[상반기 세부내역]</a></td>
								<td>2022 <a href="#/static/html/pay_2022_4.html">[하반기 세부내역]</a></td>
								<td>2022 <a href="#/static/html/pay_2022_2.html">[상반기 세부내역]</a></td>
								<td>2021 <a href="#/static/html/pay_2021_4.html">[하반기 세부내역]</a></td>
								<td>2021 <a href="#/static/html/pay_2021_2.html">[상반기 세부내역]</a></td>


								
							</tr>
							<tr>
								<td>2020 <a href="#/static/html/pay_2020_4.html">[하반기 세부내역]</a></td>
								<td>2020 <a href="#/static/html/pay_2020_2.html">[상반기 세부내역]</a></td>
								<td>2019 <a href="#/static/html/pay_2019_4.html">[하반기 세부내역]</a></td>
								<td>2019 <a href="#/static/html/pay_2019_2.html">[상반기 세부내역]</a></td>
								<td>2018 <a href="#/static/html/pay_2018_4.html">[하반기 세부내역]</a></td>
								<td>2018 <a href="#/static/html/pay_2018_2.html">[상반기 세부내역]</a></td>
								<!-- <td>2017 <a href="#/static/html/pay_2017_2.html">[세부내역]</a></td> -->
								<!-- <td>2016 <a href="#/static/html/pay_2016s.html">[세부내역]</a></td> -->
								<!-- <td>2015 <a href="#/static/html/pay_2015s.html">[세부내역]</a></td> -->
								<!-- <td>2014 <a href="#/static/html/pay_2014s.html">[세부내역]</a></td> -->
								<!-- <td>2013 <a href="#/static/html/pay_2013s.html">[세부내역]</a></td> -->
								<!-- <td>2012 <a href="#/static/html/pay_2012s.html">[세부내역]</a></td> -->
								<!-- <td>2011 <a href="#/static/html/pay_2011s.html">[세부내역]</a></td> -->
								<!-- <td>2010 <a href="#/static/html/pay_2010s.html">[세부내역]</a></td> -->
								<!-- <td>2009 <a href="#/static/html/pay_2009s.html">[세부내역]</a></td> -->
								<!-- <td>2008 <a href="#/static/html/pay_2008s.html">[세부내역]</a></td> -->
								<!-- <td>2007 <a href="#/static/html/pay_2007s.html">[세부내역]</a></td>
								<td>2006 <a href="#/static/html/pay_2006s.html">[세부내역]</a></td> -->
								<!-- <td>2005 <a href="#/static/html/pay_2005s.html">[세부내역]</a></td> -->
							</tr>
						</tbody>
					</table>
				</li>
			</ul>
		</div>
	</div>
	
	<div class="list" style="margin-top:20px;">
		<table cellpadding="0" cellspacing="0" class="" summary="" style="border-top: 2px solid #1e90f1;border-bottom: 2px solid #1e90f1;">
			<caption></caption>
			<colgroup>
				<col width="10%"/><col width=""/>
				<col width="15%"/>
			</colgroup>
			<thead>
				<tr><th>번호</th><th>제목</th><th>다운로드</th></tr>
			</thead>
			<tbody  id="listData">
			
<%
	for(int idx=0; idx<calcList.getMaxDataSize(); idx++)
	{
%>			<tr>
				<td><%=calcList.getText("brd_no", idx) %></td>
				<td><%=jdf.framework.core.util.HtmlFormat.fixLength(calcList.getText("ttl", idx), 200) %></td>
				<td>
					<span class="btn btn-sm btn-info no-radius" type="button" onclick="js_fileDown('<%=calcList.getText("file_no", idx) %>')">
						<i class="icon- fa fa-download"> 다운</i> 
					</span>					
				</td>
			</tr>	
				
<%
	}
%>				
							
			</tbody>
		</table>
	</div>

	<div class="layerPop" id="layerPop" style="top: 110px;display:none">
		<a href="#" onclick="$('#layerPop').hide();return false;" class="btnClose" style="top: 8px;right: 25px;"><img src="/static/main/img/sub/btn_close.jpg" width="22" height="22" alt="close" /></a>
		<div id="layerPopHead" style="background: #307ecc;border-color: #307ecc;color: #FFF;border: 1px solid #CCC;min-height: 38px;box-sizing: content-box;">			
		</div>
		<div id="load" class="loadCont" style="height:650px;">
			
		</div>
	</div>


<!-- end :: content -->
<!-- end :: content -->
</div>

<script language="javascript" type="text/javascript">
//<![CDATA[
$("#layerBtns a").click(function(){
	var url = this.href.split("#")[1];
	
	$("#load").html('');
	$("#load").scrollTop(0);
	$("#load").load(url);
	
	$("#layerPop").show();
	return false;
});

$('#layerPopHead').draggable();

function js_clickList(url)
{
	$("#load").html('');
	$("#load").scrollTop(0);
	$("#load").load(url);
	
	$("#layerPop").show();
}

function js_fileDown(file_no){
	
	var _down = '/common/action/attach.jspx?cmd=doDownload&file_no='+file_no;
	window.open(_down, '_new');
}
//]]>
</script>
		
</body>
</html>