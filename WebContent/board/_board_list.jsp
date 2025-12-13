<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="codeList" type="jdf.framework.core.data.DataSet" scope="request" />
<%
String cur_pg 					= input.getText("cur_pg");
if( cur_pg.equals("0") || cur_pg.equals(""))	cur_pg = "1";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	jQuery(document).ready(function(){
		js_list(1);
	});
	
	function checkForEnter(event)
	{
		if ((event.which && event.which == 13) || (event.keyCode && event.keyCode == 13))
   		{
			js_list(1);
		}
	}
	
	function js_list(cur_pg)
	{
		jQuery('#cur_pg').val(cur_pg);
		
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=getBoardList',
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
					$('#tot_cnt').val(json.result.data.property[0].tot_cnt);
				} else {
					$('#tot_cnt').val(0);
				}	
	
		   		$("#listData > tr").remove();		   		
	
		   		var listDataRows=[];		   		
		   	
		   		var dataCount = 0;
				if( data.board_list ) 
					dataCount =  data.board_list.length;
				
		   		if(code == '200')
		   		{  			
		   			
		   			for(var i=0 ; i < data.board_list.length ; i++)
		   			{
		   				var imp_yn = data.board_list[i].imp_yn;
		   				var brd_mng_no = data.board_list[i].brd_mng_no;
		   				
		   				var brd_div_nm = data.board_list[i].brd_div_nm;
		   				if(brd_mng_no == 10 && brd_div_nm != '') brd_div_nm = '<span class="file_div">'+data.board_list[i].brd_div_nm+'</span> ';
		   				else brd_div_nm = '';
		   				
		   				var tdData = "";
		   				tdData += "<td>"+ data.board_list[i].brd_no		+"</td>" ;	
		   				if( imp_yn == 'Y' ){
		   					tdData += "<td class='tit' style='font-weight:bold;'><div class='ellipsis long'>"+ brd_div_nm  +"<a href=\"javascript:js_detail("+data.board_list[i].brd_no+");\">"  + '<i class="fa fa-bell tblue"></i> '+ data.board_list[i].ttl 			+"</a></div></td>" ;
		   				}else{
		   					tdData += "<td class='tit'><div class='ellipsis long'>"+ brd_div_nm  +"<a href=\"javascript:js_detail("+data.board_list[i].brd_no+");\">"+ data.board_list[i].ttl 			+"</a></div></td>" ;
		   				}	
		   				tdData += "<td>"+ data.board_list[i].emp_nm 		+"</td>" ;
		   				tdData += "<td>"+ data.board_list[i].read_cnt		+"</td>" ;
		   				tdData += "<td>"+ data.board_list[i].reg_dt			+"</td>" ;
		   				listDataRows.push("<tr>" + tdData + "</tr>");
		   			}
	
		   			if(data.board_list.length == 0 )
		   			{
		   				listDataRows.push("<tr><td colspan='5' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>");		   				
		   			}
		   			else
		   			{
		   				//js_Paging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');	
		   			}
		   				
	
		   		}else{
		   			listDataRows.push("<tr><td colspan='5' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}
		   		
	
				$("#listData").html(listDataRows.join(' '));
				js_userPaging('pagingDiv', $('#cur_pg').val(), $('#tot_cnt').val(), $('#board_row_per_page').val(), 'js_list');		   			
		   		
			}
		});
	}
	
	function js_detail(brd_no)
	{
			location.href='/board/board.jspx?cmd=view&brd_mng_no=' + jQuery('#brd_mng_no').val() + '&brd_no=' + brd_no;
		
	}
	
	function js_changeCategory(cd)
	{
		jQuery('#brd_div').val(cd);		
		
		if(cd == '') cd = 'all';
		var selectID = null;
		var unSelectID = [];
		
		for(var k = 0; k<jQuery('#cateUL > li').length; k++)
		{
			if(jQuery(jQuery('#cateUL > li')[k]).find('a').attr('id') == cd)
			{
				selectID = cd;
			} else
			{
				unSelectID.push(jQuery(jQuery('#cateUL > li')[k]).find('a').attr('id'));
			}
		}
		
		jQuery('#'+selectID).attr('class', 'on');
		
		for(var k =0; k<unSelectID.length; k++)
		{
			jQuery('#'+unSelectID[k]).attr('class', '');
		}				
		js_list(1);
		
	}
	
</script>
<style> .file_div{display: inline-block; min-width: 65px; height: 25px; text-align: center;border: 1px solid #1e90f1; color: #1e90f1; border-radius: 5px;}</style>
</head>
<body>
<div id="content">
<!-- start :: content -->
<!-- start :: content -->
	<div style='clear:both;float:left;margin-bottom:10px;width: 100%;'>

<%
	if("11".equals(input.getText("brd_mng_no")))
	{
%>
	
		<p class="rStep_btn">		
			<a href="/board/board.jspx?cmd=list&brd_mng_no=11" style="margin-left:-20px;"><img src="/static/main/img/sub/job_tab_1_on.png" alt="구인" /></a>
			<a href="/board/board.jspx?cmd=list&brd_mng_no=12" style="margin-left:-20px;"><img src="/static/main/img/sub/job_tab_2_off.png" alt="구직" /></a>
		</p>

<%
	} else if("12".equals(input.getText("brd_mng_no")))
	{
%>
		<p class="rStep_btn">		
			<a href="/board/board.jspx?cmd=list&brd_mng_no=11" style="margin-left:-20px;"><img src="/static/main/img/sub/job_tab_1_off.png" alt="구인" /></a>
			<a href="/board/board.jspx?cmd=list&brd_mng_no=12" style="margin-left:-20px;"><img src="/static/main/img/sub/job_tab_2_on.png" alt="구직" /></a>
		</p>
<%
	} else if("13".equals(input.getText("brd_mng_no")))
	{
%>
	
	<!--str:건물관리연구원 -->
		<div class="buildingManage">
			
			
			<p class="top_title">
				<span>청소</span>를 하는데 연구원이 필요 하냐고 생각하시는 사람들도 있습니다.
			</p>

			<p class="sub_title">
				건물의 진화(進化)에 따라 위생관리업(Cleaning Business)도 발전의 고뇌를 거듭하고 있습니다.
			</p>

			<p class="content">
				1960년대를 Family Building 시대라 한다면 70년대를 Commercial Building, 80년대를 Industrial Building, 90년대를 
				Intelligent Building, 2000년대를 Multi-function Building, 2010년대를 초고층빌딩[Skycrapper]시대라 칭할 수 있으며
				이는 건물에 대한 시대적 요청과 시장(市場)의 요구(needs)에 따라 건물이 대형화, 기능화, 첨단화, 고층화되고 있음을
				대변하고 있습니다. 또한 국 내·외 초고층건물의 비중이 급증하는 추세에 있어 건물관리기술 시스템은 바야흐로 질적·양적
				면에서 급격하게 발전하는 단계를 맞고 있어 건물관리에 대한 전문성과 필요성도 자연히 크게 부각되고 있습니다.
			</p>

			<p class="content">
				또한 건축기술과 건축자재의 발달에 따른 건물관리기술의 질적 성장이 요구되고 있으며 이러한 현실 속에서 본 연구원은
				관리의 합리화, 장비의 현대화, 안전의 생활화, 영업의 세계화라는 큰 명제위에 살아 움직이는 기술개발이라는 소명을
				부여받아 회원사님들의 기대에 부응하고자 노력하고 있습니다. <br/>많은 협조 부탁드립니다.
			</p>

			<p class="content">
				감사합니다.
			</p>

			<p class="signature" >
				건물관리연구원장
			</p>
			
		</div>
		<!--END:건물관리연구원 -->
	
				
<%		
	}else if("9".equals(input.getText("brd_mng_no")) || "22".equals(input.getText("brd_mng_no")) ||
			"24".equals(input.getText("brd_mng_no")) || "25".equals(input.getText("brd_mng_no")) ||
			"26".equals(input.getText("brd_mng_no")) || "27".equals(input.getText("brd_mng_no")))
	{		
%>
	<style>
	
	.tabWrap ul.st_3 li a {
	    display: block;
	    width: 100%;
	    height: 33px;
	    text-align: center;
	    border: 1px solid #48a3f0;
	    color: #48a3f0;
	    border-radius: 0;
	}
	.tabWrap ul.st_3 li a.on {
	    background: #48a3f0;
	}
	.tabWrap ul.st_3 li a.on span {
	    color: #fff;
	}
	.tabWrap ul.st_3 li {
	    float: left;
	    margin: 0 0 5px 5px;
	    width: 32%;
	}
	.tabWrap ul.st_3 li a span {
	    color: #48a3f0;
	    padding: 0 10px;
	    line-height: 30px;
	}
	</style>
<%  } %>
	</div>	
	<div class="topSearch" style='clear:both;'>
		<div class="">
<form name="search_form" id='search_form' onsubmit="return false;">
<input type="hidden" name="board_row_per_page" id="board_row_per_page" value='<%=input.getText("board_row_per_page")%>'/>
<input type="hidden" name="cur_pg" id="cur_pg" value='<%=cur_pg%>'/>
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>		
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>
<input type="hidden" name="brd_mng_no" id="brd_mng_no" value='<%=input.getText("brd_mng_no")%>'/>
<input type="hidden" name="p_reg_nm" id="p_reg_nm" value=''/>
<input type="hidden" name="start_dt" id="start_dt" value=''/>
<input type="hidden" name="end_dt" id="end_dt" value=''/>
<input type="hidden" name="brd_div" id="brd_div" value=''/>
			
			<select name="search_type" title="search_type">
				<option value="ttl" selected="selected">제목+본문</option>
			</select>
			<input type="text" class="it " title="" value="" name="search_word" id="search_word" onKeyUp="checkForEnter(event);"/>			
			<a href="javascript:js_list(1);" class="pbtn05"><span>검색</span></a>
</form>			
		</div>
	</div>
	
<%	if("10".equals(input.getText("brd_mng_no"))){%>	
		
		<div class="tabWrap">
			<ul class="" id='cateUL'>
				<li><a href="javascript:js_changeCategory('');" class="on" id='all'><span class="">전체</span></a></li>
				
<%	for(int idx=0; idx<codeList.getMaxDataSize(); idx++){ %>		
				<li><a href="javascript:js_changeCategory('<%=codeList.getText("cd", idx) %>');" class="off" id='<%=codeList.getText("cd", idx) %>'><span class=""><%=codeList.getText("cd_nm", idx) %></span></a></li>			
<%	}%>	
			</ul>
		</div>		
<%}%>
	<div class="list">
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="10%"/><col width=""/>
				<col width="15%"/><col width="15%"/><col width="15%"/>
			</colgroup>
			<thead>
				<tr><th>번호</th><th>제목</th><th>작성자</th><th>조회수</th><th>작성일자</th></tr>
			</thead>
			<tbody  id="listData">
				<tr><td colspan="5" style="text-align:center;">조회된 결과가 없습니다.</td></tr>				
			</tbody>
		</table>
	</div>

	<div class="paging" id='pagingDiv'>		
	</div>


<!-- end :: content -->
<!-- end :: content -->
</div>
</body>
</html>