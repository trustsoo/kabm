<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/static/js/ubicus.app.js?_dummy=111"></script>	
<script type="text/javascript" src="/static/js/ubicus.common.js?_dummy=111"></script>
<style>
.topSearch{
	margin-bottom: 0px !important;
    padding-bottom: 0px !important;
}
.topSearch .search_div span {
    width: 40px;
    height: 40px;
    background: #347ac2;
    padding-top: 24px;
}
.topSearch .search_div span svg {
    padding: 0px;
}
svg:not(:root) {
    overflow: hidden;
}
svg[Attributes Style] {
    width: 40px;
    height: 40px;
}
.searchAll .schKeyword {
    margin-bottom: 0px;
    padding: 29px 0 37px;
    background: #f9f8f8;
    text-align: center;
    overflow: hidden;
}

.searchAll .schCnt em {
    font-weight: bold;
    color: #347ac2;
}
.searchAll .nav-tabs {
    border-bottom: 0px solid #E5EAEE;
}
.searchAll .nav {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    padding-left: 0;
    margin-bottom: 0;
    list-style: none;
}
.searchAll .nav.nav-tabs.nav-tabs-line .nav-item {
    margin: 0 0 -1px 0;
}
.searchAll .nav.nav-tabs .nav-item {
    margin-right: 0.25rem;
    height: 34px;
    border-color: #ffffff;
}
.searchAll .nav-item {
    display: flex;
}
.searchAll .nav-tabs>li.active>a, .nav-tabs>li.active>a:focus, .nav-tabs>li.active>a:hover {
    -webkit-transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, -webkit-box-shadow 0.15s ease;
    transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, -webkit-box-shadow 0.15s ease;
    transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, box-shadow 0.15s ease;
    transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, box-shadow 0.15s ease, -webkit-box-shadow 0.15s ease;
    color: #347ac2 !important;
    font-weight: bold;
}
.searchAll .nav-tabs>li.active>a {
    border-bottom: 1px solid #347ac2 !important;
}
.searchAll .nav.nav-tabs.nav-tabs-line .nav-link {
    width: 200px;
    height: 50px;
    font-size: 20px;
    justify-content: center;
    border: 0px solid #ddd;
    color: #80808F;
    font-weight: bold;
    padding: 0.85rem 0;
    margin: 0 1rem;
    text-align: center;
}
.searchAll .nav .show > .nav-link, .nav .nav-link:hover:not(.disabled), .nav .nav-link.active {
    -webkit-transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, -webkit-box-shadow 0.15s ease;
    transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, -webkit-box-shadow 0.15s ease;
    transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, box-shadow 0.15s ease;
    transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, box-shadow 0.15s ease, -webkit-box-shadow 0.15s ease;
    color: #347ac2 !important;
    font-weight: bold;
}
.searchAll .nav.nav-tabs.nav-tabs-line .nav-link {
    border: 0;
    border-bottom: 1px solid transparent;
}    
.searchAll .nav.nav-tabs.nav-tabs-line .nav-link:hover:not(.disabled), .searchAll .nav.nav-tabs.nav-tabs-line .nav-link.active, .searchAll .nav.nav-tabs.nav-tabs-line .show > .nav-link {
    border-bottom: 1px solid #347ac2;
}
.searchAll .tab-content {
    padding-top: 5px;
}
.searchAll .mt-5, .my-5 {
    margin-top: 1.25rem !important;
}
.searchAll .tab-content > .active {
    display: block;
}
.searchAll .fade {
    -webkit-transition: opacity 0.15s linear;
    transition: opacity 0.15s linear;
}
.searchAll .tabcontentBox {
    margin-bottom: 20px;
}
.searchAll .tabcontentBox .resultBox {
    margin-top: 20px;
}
.searchAll .tabcontentBox .resultBox > div {
    position: relative;
    margin-top: 20px;
}
.searchAll .tabcontentBox .resultBox .subHead {
    display: inline-block;
        margin: 0 0 12px;
    font-size: 20px;
    color: #222;
    line-height: 24px;
    font-weight: normal;
    text-shadow: 1px 0 0 #222;
}
.searchAll .tabcontentBox .resultBox > div .moreLink {
    float: right;
    display: inline-block;
    padding-left: 30px;
    
}
.searchAll .tabcontentBox .resultBox > div .moreLink a {
    text-decoration: none;
    color: #000;
    font-size: 100%;
}
.searchAll .tabcontentBox .resultBox > ul {
    border-top: 0px solid #e3e3e3;
    border-bottom: 1px solid #e3e3e3;
}
.searchAll .tabcontentBox .resultBox > ul li {
    padding: 12px 0;
}
.searchAll .tabcontentBox .resultBox > ul li dt {
    color: #222;
    font-weight: bold;
    padding: 5px 0;
}
.searchAll .subHead em {
    font-weight: normal;
    font-style: normal;
    text-shadow: 1px 0 0 #347ac2;
}
.searchAll{
	font-family: 'Malgun Gothic' !important;
}
.flex-row {
    -webkit-box-orient: horizontal !important;
    -webkit-box-direction: normal !important;
    -ms-flex-direction: row !important;
    flex-direction: row !important;
}
.flex-column {
    -webkit-box-orient: vertical !important;
    -webkit-box-direction: normal !important;
    -ms-flex-direction: column !important;
    flex-direction: column !important;
}
.highlight { background-color: yellow; font-weight: inherit; color: #000; font-size: inherit; }

</style>
</head>
<body>
<div id="content" class="searchAll">
<!-- start :: content -->

	<div class="topSearch" style='clear:both;'>
		<div class="search_div">
<form name="search_form" id='search_form' onsubmit="return false;">
<input type="hidden" name="row_per_page" id="row_per_page" value='10'/>
<input type="hidden" name="board_cur_pg" id="board_cur_pg" value='1'/>
<input type="hidden" name="board_tot_cnt" id="board_tot_cnt" value='0'/>
<input type="hidden" name="faq_cur_pg" id="faq_cur_pg" value='1'/>
<input type="hidden" name="faq_tot_cnt" id="faq_tot_cnt" value='0'/>
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>
			
			
			<input type="text" class="it " style="border: 2px solid #347ac2;height: 40px;vertical-align: text-bottom;" title="" value="<%=input.getText("top_search_word") %>" name="search_word" id="search_word" onKeyUp="checkForEnter(event);"/>			
			
			<a href="javascript:js_searchAll()">
				<span class="svg-icon svg-icon-primary svg-icon-2x">
					<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="40px" height="35px" viewBox="0 0 25 25" version="1.1">
						<g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
							<rect x="0" y="0" width="25" height="25"></rect>
							<path d="M14.2928932,16.7071068 C13.9023689,16.3165825 13.9023689,15.6834175 14.2928932,15.2928932 C14.6834175,14.9023689 15.3165825,14.9023689 15.7071068,15.2928932 L19.7071068,19.2928932 C20.0976311,19.6834175 20.0976311,20.3165825 19.7071068,20.7071068 C19.3165825,21.0976311 18.6834175,21.0976311 18.2928932,20.7071068 L14.2928932,16.7071068 Z" fill="#ffffff" fill-rule="nonzero"></path>
							<path d="M11,16 C13.7614237,16 16,13.7614237 16,11 C16,8.23857625 13.7614237,6 11,6 C8.23857625,6 6,8.23857625 6,11 C6,13.7614237 8.23857625,16 11,16 Z M11,18 C7.13400675,18 4,14.8659932 4,11 C4,7.13400675 7.13400675,4 11,4 C14.8659932,4 18,7.13400675 18,11 C18,14.8659932 14.8659932,18 11,18 Z" fill="#ffffff" fill-rule="nonzero"></path>
						</g>
					</svg><!--end::Svg Icon-->
				</span>
			</a>
</form>			
		</div>
	</div>
	
	<div class="keywordWr"><div class="schKeyword"><p class="schCnt"><em id="searchWord">''</em> 에 대하여 총 <span style="font-weight:bold;" id="totalCnt">0</span>건의 자료가 검색되었습니다.</p></div></div>
	<ul class="nav nav-tabs nav-tabs-line">
		<li class="nav-item active"><a data-toggle="tab" href="#kt_tab_pane_board" class="nav-link" onclick="js_detailSearch('board',1)">게시물<em id="boardCount">(0)</em></a></li> 
		<li class="nav-item"><a data-toggle="tab" href="#kt_tab_pane_faq" class="nav-link" onclick="js_detailSearch('faq',1)">자주묻는질문<em id="faqCount">(0)</em></a></li> 
	</ul>
	<div class="tab-content mt-5" id="myTabContent">
         <div class="tab-pane active" id="kt_tab_pane_board" role="tabpanel" aria-labelledby="kt_tab_pane_board">
             <div class="tabcontentBox">

                 <div class="resultBox ">
                     <span class="mailBox"></span>
                     <ul id="boardListData">
                         
                     </ul>                     
                     <div role="group" class="btn-more" style="text-align: center;" id="boardMoreLink"></div>
                 </div>

             </div>
         </div>
         <div class="tab-pane fade" id="kt_tab_pane_faq" role="tabpanel" aria-labelledby="kt_tab_pane_faq">
             <div class="tabcontentBox">

                 <div class="resultBox ">
                     <span class="mailBox"></span>
                     <ul id="faqListData">
                         
                     </ul>                     
                     <div role="group" class="btn-more" style="text-align: center;" id="faqMoreLink"></div>
                 </div>

             </div>
         </div>
     </div>    

<!-- end :: content -->
</div>

<div class="modal fade bs-notice-modal-lg" tabindex="-1" role="dialog" aria-hidden="true">
<div class="modal-dialog modal-lg">
<div class="modal-content">
<div class="modal-header">
<h4 class="modal-title" style="float:left;font-weight: bold;font-family:'nanum_square';font-size: 24px;">상세보기</h4>
<button type="button" class="close" data-dismiss="modal" style="font-size:40px;"><span aria-hidden="true">×</span>
</button>
</div>
<div class="modal-body">
</div>
<div class="modal-footer">
</div>
</div>
</div>
</div>

<script>
	jQuery(document).ready(function(){
		if( $('#search_word').val() != ''){
			js_searchAll();
		}
	});
	
	function checkForEnter(event)
	{
		if ((event.which && event.which == 13) || (event.keyCode && event.keyCode == 13))
   		{
			js_searchAll();
		}
	}
	
	function js_searchAll()
	{
		if($('#search_word').val() == ''){
			alert( '검색어를 먼저 입력하세요');
			return;
		} 
		js_boardList(1);
		js_faqList(1);
		js_setTotal();
		$('a[href="#kt_tab_pane_board"]').tab('show');
	}
	
	function js_detailSearch(div , page)
	{
		if($('#search_word').val() == ''){
			return;
		} 
		if(div == 'board')
		{
			js_boardList(page)
		}else if(div == 'faq'){
			js_faqList(page)
		}
		$('a[href="#kt_tab_pane_'+div+'"]').tab('show');
	}
	
	function js_setTotal(){
		
		var totalCnt = Number($('#board_tot_cnt').val()) + Number($('#faq_tot_cnt').val());
		jQuery('#searchWord').html( '\''+ $('#search_word').val()  + '\'' );
		jQuery('#totalCnt').html(totalCnt);
	}
	
	function js_searchMore(div,cur_pg)
	{
		if(div == 1) js_boardList(cur_pg+1);
		else js_faqList(cur_pg+1);
	}
	
	function js_boardList(cur_pg)
	{
		jQuery('#board_cur_pg').val(cur_pg);
		
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=getSearchList',
			data : jQuery("#search_form").serialize(true),
			type : 'POST',
			async : false,
			datatype: 'json',
			error : function(xhr)
	   		{
				alert(xhr.status);
			},
			success:function(json) 
			{
				var code = json.result.code;
		   		var msg =  json.result.msg;
		   		var data = json.result.data;
				
		   		if(json.result.data.property[0].tot_cnt )
				{
					$('#board_tot_cnt').val(json.result.data.property[0].tot_cnt);
				} else {
					$('#board_tot_cnt').val(0);
				}
	
		   		var listDataRows=[];
		   				   	
		   		var dataCount = 0;
				if( data.board_list ) 
					dataCount =  data.board_list.length;
						
		   		if(code == '200')
		   		{  
		   			for(var i=0 ; i < data.board_list.length ; i++)
		   			{
		   				var imp_yn = data.board_list[i].imp_yn;
		   				var cntnt = highlightResult(data.board_list[i].cntnt);
		   				var ttl = replaceAll2((data.board_list[i].ttl), $('#search_word').val(), '<span class="highlight">' + $('#search_word').val() + '</span>');
		   				var tdData = "";
		   				tdData += '<li>';
			   			tdData +=     '<dl>';
			   			tdData +=         '<dt><a style="cursor:pointer;" onclick="js_detailView('+data.board_list[i].brd_mng_no+','+data.board_list[i].brd_no+')" class="j-pop-link" data-link-type="newNotice">['+data.board_list[i].brd_nm+'] '+ ttl	+'</a>';
			   			tdData +=         '<span class="pull-right" style="float:right">'+data.board_list[i].reg_dt+'</span>';
			   			tdData +=         '</dt>';
			   			tdData +=         '<dd style="cursor:pointer;" onclick="js_detailView('+data.board_list[i].brd_mng_no+','+data.board_list[i].brd_no+')">';
			   			tdData +=         cntnt;
			   			tdData +=         '</dd>';
			   			tdData +=     '</dl>';
			   			tdData += '</li>';
			   			listDataRows.push(tdData);			   			
		   			}
	
		   			if(data.board_list.length == 0 )
		   			{
		   				listDataRows.push("<li>조회된 결과가 없습니다.</li>");		   				
		   			}
		   			
		   		}else{
		   			listDataRows.push("<li>[" + code + "] " + msg + " </li"); 
		   		}
		   		
		   		var tot_cnt = $('#board_tot_cnt').val();
		   		var cur_pg = $('#board_cur_pg').val();
		   		var row_per_page = $('#row_per_page').val();
		   		if( tot_cnt > (cur_pg * row_per_page))
		   		{	
		   			$("#boardMoreLink").html('<span onclick="js_searchMore(1,'+cur_pg+')" class="btn btn-sm btn-primary btn-curved"><i class="fa fa-arrow-down"></i> 더보기</span>');
		   		}else{
		   			$("#boardMoreLink").html('');
		   		}
		   		
		   		$("#boardCount").html( '('+tot_cnt+')');	
		   		if(cur_pg <= 1 )
		   		{			   			   		
			   		$("#boardListData").html(listDataRows.join(' '));
		   		}else{
		   			$("#boardListData").append(listDataRows.join(' '));
		   		}				
			}
		});
	}
	
	function js_faqList(cur_pg)
	{
		jQuery('#faq_cur_pg').val(cur_pg);
		
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=getSearchFaqList',
			data : jQuery("#search_form").serialize(true),
			type : 'POST',
			async : false,
			datatype: 'json',
			error : function(xhr)
	   		{
				alert(xhr.status);
			},
			success:function(json) 
			{
				var code = json.result.code;
		   		var msg =  json.result.msg;
		   		var data = json.result.data;
				
		   		if(json.result.data.property[0].tot_cnt )
				{
					$('#faq_tot_cnt').val(json.result.data.property[0].tot_cnt);
				} else {
					$('#faq_tot_cnt').val(0);
				}
	
		   		var listDataRows=[];
		   				   	
		   		var dataCount = 0;
				if( data.list ) 
					dataCount =  data.list.length;
						
		   		if(code == '200')
		   		{  
		   			for(var i=0 ; i < data.list.length ; i++)
		   			{
		   				var answer = highlightResult(data.list[i].answer);
		   				var question = replaceAll2((data.list[i].question), $('#search_word').val(), '<span class="highlight">' + $('#search_word').val() + '</span>');
		   				var tdData = "";
		   				tdData += '<li>';
			   			tdData +=     '<dl>';
			   			tdData +=         '<dt><a style="cursor:pointer;" onclick="js_faqView('+data.list[i].faq_no+')" class="j-pop-link" data-link-type="newNotice">['+data.list[i].category_nm+'] '+ question+'</a>';
			   			tdData +=         '<span class="pull-right" style="float:right">'+data.list[i].reg_ddtm+'</span>';
			   			tdData +=         '</dt>';
			   			tdData +=         '<dd style="cursor:pointer;" onclick="js_faqView('+data.list[i].faq_no+')">';
			   			tdData +=         answer;
			   			tdData +=         '</dd>';
			   			tdData +=     '</dl>';
			   			tdData += '</li>';
			   			listDataRows.push(tdData);			   			
		   			}
	
		   			if(data.list.length == 0 )
		   			{
		   				listDataRows.push("<li>조회된 결과가 없습니다.</li>");		   				
		   			}
		   			
		   		}else{
		   			listDataRows.push("<li>[" + code + "] " + msg + " </li"); 
		   		}
		   		
		   		var tot_cnt = $('#faq_tot_cnt').val();
		   		var cur_pg = $('#faq_cur_pg').val();
		   		var row_per_page = $('#row_per_page').val();
		   		if( tot_cnt > (cur_pg * row_per_page))
		   		{	
		   			$("#faqMoreLink").html('<span onclick="js_searchMore(1,'+cur_pg+')" class="btn btn-sm btn-primary btn-curved"><i class="fa fa-arrow-down"></i> 다음</span>');
		   		}else{
		   			$("#faqMoreLink").html('');
		   		}
		   		
		   		$("#faqCount").html( '('+tot_cnt+')');		   		
		   		if(cur_pg <= 1 )
		   		{			   			   		
			   		$("#faqListData").html(listDataRows.join(' '));
		   		}else{
		   			$("#faqListData").append(listDataRows.join(' '));
		   		}	
		   		
			}
		});
	}
	
	
	const highlightResult = function(_searchContent){
    	const $tempElement = $('<div style="display: none">'+ubicus.common.unescapeHTML(_searchContent)+'</div>');
    	$('body').append($tempElement);
    	let searchContent = $tempElement.text();
		$tempElement.remove();

		let firstIndex = 99999;
		let lastIndex = 0;
		let ln = searchContent.length;
		const qr = $('#search_word').val();
		let qrw = qr.trim();
		if( ln > 500) {			
			
			const a = searchContent.indexOf(qrw);
			const b = searchContent.lastIndexOf(qrw);
			if (firstIndex > a) firstIndex = a;
			if (lastIndex < b) lastIndex = b;			

			if (lastIndex + 50 < ln) searchContent = searchContent.substring(0, lastIndex + 50) + ' ...';
			if (firstIndex >= 50) searchContent = '... ' + searchContent.substring(firstIndex - 50);
		}
		
		searchContent = replaceAll2(searchContent, qrw, '<span class="highlight">' + qrw + '</span>');
		
		return searchContent;
	}
	
	var js_detailView = function(brd_mng_no,brd_no){

	    $(".modal-body").load('/board/board.jspx?cmd=searchView&brd_mng_no=' + brd_mng_no + '&brd_no=' + brd_no + '&templet=popup');

	    $('.bs-notice-modal-lg').modal({});
	};
	
	var js_faqView = function(faq_no){

	    $(".modal-body").load('/board/board.jspx?cmd=faq_view&faq_no=' + faq_no + '&templet=popup');

	    $('.bs-notice-modal-lg').modal({});
	};
	
	function replaceAll2(str, searchStr, replaceStr) {

	   return str.split(searchStr).join(replaceStr);
	}
	
</script>
</body>
</html>