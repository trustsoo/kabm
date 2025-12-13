<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>    
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<% 
String user_nm = getUserObject(request, response).getName();
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<script>
jQuery(document).ready(function(){
	
	requestCntInfos();
	requestQnAList();
	requestNoticeList();
	requestBoardList();
	
});

function requestNoticeList()
{
	var http = jQuery.ajax({
		url : '/board/action/board.jspx?cmd=getNoticeList',
		data : 'board_row_per_page=8&cur_pg=1&search_txt=&search_type=&p_reg_nm=',
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
	   		
	   		if(code == '200')
	   		{  			
	   			var _html = [];
	   			for(var i=0 ; i < data.notice_list.length ; i++)
	   			{
	   				
	   				var reg_dt = data.notice_list[i].reg_ddtm.substring(5,data.notice_list[i].reg_ddtm.length);
	   				var reg_mm = reg_dt.split('-')[0];
	   				var reg_dd = reg_dt.split('-')[1];
	   				var title = data.notice_list[i].ttl;
	   				var notice_div_nm = data.notice_list[i].notice_div_nm;
	   				_html.push("<article class='media event'>");
	   				_html.push("<a class='pull-left date'>");
	   				_html.push("<p class='month'>"+reg_mm+"월</p>");
	   				_html.push("<p class='day'>"+reg_dd+"</p>");
	   				_html.push("</a>");
	   				_html.push("<div class='media-body'>");
	   				_html.push("<a class='title' href='/mng/board/community.jspx?cmd=notice_list' title='"+title+"'>["+notice_div_nm+"]</a>");
	   				_html.push("<p>"+title+"</p>");
	   				_html.push("</div>");
	   				_html.push("</article>");    
	   				
	   			}
	   			if(data.notice_list.length > 0 )
	   				jQuery('#noticeList').html(_html.join(''));
	   		}		   			
	   		
		}
	});
}

function requestBoardList()
{
	var http = jQuery.ajax({
		url : '/mng/board/action/community.jspx?cmd=doBoardList',
		data : 'board_row_per_page=8&cur_pg=1&search_word=&p_reg_nm=',
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
	   		
	
	   		if(code == '200')
	   		{  			
	   			var _html = [];
	   			
	   			console.log(data.board_list.length);
	   			
	   			for(var i=0 ; i < data.board_list.length ; i++)
	   			{
	   				
	   				var reg_dt = data.board_list[i].reg_dt.substring(5,data.board_list[i].reg_dt.length);
	   				var reg_mm = reg_dt.split('-')[0];
	   				var reg_dd = reg_dt.split('-')[1];
	   				var title = data.board_list[i].ttl;
	   				var brd_nm = data.board_list[i].ttl;
	   				_html.push("<article class='media event'>");
	   				_html.push("<a class='pull-left date'>");
	   				_html.push("<p class='month'>"+reg_mm+"월</p>");
	   				_html.push("<p class='day'>"+reg_dd+"</p>");
	   				_html.push("</a>");
	   				_html.push("<div class='media-body'>");
	   				_html.push("<a class='title' href='/mng/board/community.jspx?cmd=list' title='"+title+"'>["+brd_nm+"]</a>");
	   				_html.push("<p>"+title+"</p>");
	   				_html.push("</div>");
	   				_html.push("</article>");    
	   				
	   			}
	   			if(data.board_list.length > 0 )
	   				jQuery('#boardList').html(_html.join(''));
	   		}		   			
	   		
		}
	});
}


/*QNALIST*/
function requestQnAList()
{
	var http = jQuery.ajax({
		url : '/mng/content/action/question.jspx?cmd=getQuestionList',
		data : 'in_question_div=&row_per_page=8&cur_pg=1',
		type : 'GET',
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
			
	   		if(code == '200')
	   		{	
	   			console.log(data.question_list);
	   			//var seq_no = data.question_list
	   			var _html = [];
	   			for(var i=0 ; i < data.question_list.length ; i++)
	   			{
	   				var seq_no = data.question_list[i].seq_no;
	   				var title  = data.question_list[i].title;
	   				var question_div_nm = data.question_list[i].question_div_nm;
	   				var name = data.question_list[i].name;
	   				var answer_yn_nm = data.question_list[i].answer_yn_nm;
	   				var reg_ddtm = data.question_list[i].reg_ddtm.substring(5,data.question_list[i].reg_ddtm.length);
	   				var reg_mm = reg_ddtm.split('-')[0];
	   				var reg_dd = reg_ddtm.split('-')[1];
	   				_html.push("<article class='media event'>");
	   				_html.push("<a class='pull-left date'>");
	   				_html.push("<p class='month'>"+reg_mm+"월</p>");
	   				_html.push("<p class='day'>"+reg_dd+"</p>");
	   				_html.push("</a>");
	   				_html.push("<div class='media-body'>");
	   				_html.push("<a class='title' href='/mng/content/content.jspx?cmd=questionList&seq_no="+seq_no+"' title='"+title+"'>["+question_div_nm+"]"+answer_yn_nm+"</a>");
	   				_html.push("<p>"+title+"</p>");
	   				_html.push("</div>");
	   				_html.push("</article>");                   
	   			}
	   			
	   			if(data.question_list.length > 0 )
	   				jQuery('#qnaList').html(_html.join(''));
			}
		}
	});
}


/*대쉬보드 건수*/
function requestCntInfos()
{
	var http = jQuery.ajax({
		url : '/mng/action/main.jspx?cmd=getCntInfos',
		data : '0=0',
		type : 'GET',
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
			
	   		if(code == '200')
	   		{	
	   			jQuery('#uv').html(json.result.data.pv_uv_cnt[0].today_uv_cnt + "/" + json.result.data.pv_uv_cnt[0].tot_uv_cnt);
	   			jQuery('#pv').html(json.result.data.pv_uv_cnt[0].today_pv_cnt + "/" + json.result.data.pv_uv_cnt[0].tot_pv_cnt)
	   			
	   			jQuery('#qna').html(json.result.data.qna_cnt[0].non_cnt + "/" + json.result.data.qna_cnt[0].qna_tot_cnt);
	   			jQuery('#bbs').html(json.result.data.bbs_cnt[0].rcnt_cnt + "/" + json.result.data.bbs_cnt[0].bbs_tot_cnt);
			}
		}
	});		

}

</script>

</head>

<body> 


<div class="alert alert-success">
	<button type="button" class="close" data-dismiss="alert">
		<i class="icon-remove"></i>
	</button>
	<i class="icon-ok green"></i>			
	<strong class="">
		<%=user_nm %>
		<small>님</small>
	</strong> 환영합니다. 관리자 시스템입니다. 좋은 하루 되세요.
</div>

<div role="main">
          <div class="">
            <div class="row top_tiles">
              <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                <div class="tile-stats">
                  <div class="icon"><i class="fa fa-caret-square-o-right"></i></div>
                  <div class="count" id='uv'>0/0</div>
                  <h3>접속 통계(UV)</h3>
                  <p>당일/최근한달</p>
                </div>
              </div>
              <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                <div class="tile-stats">
                  <div class="icon"><i class="fa fa-comments-o"></i></div>
                  <div class="count" id='PV'>0/0</div>
                  <h3>페이지 뷰 통계(PV)</h3>
                  <p>당일/최근한달</p>
                </div>
              </div>
              <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                <div class="tile-stats">
                  <div class="icon"><i class="fa fa-check-square-o"></i></div>
                  <div class="count" id='qna'>0/0</div>
                  <h3>건의사항 통계</h3>
                  <p>미답변건수/총 건수 (최근한달)</p>
                </div>
              </div>
              <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                <div class="tile-stats">
                  <div class="icon"><i class="fa fa-check-square-o"></i></div>
                  <div class="count" id='bbs'>0/0</div>
                  <h3>게시물 통계</h3>
                  <p>최근한달건수/총 건수</p>
                </div>
              </div>
            </div>



            <div class="row">
              <div class="col-md-4">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>최근 문의 리스트</h2>                    
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content" id='qnaList'> 
                  	<article class="media event">                      
                      <div class="media-body">                        
                        <p>최근 문의 리스트가 없습니다.</p>
                      </div>
                    </article>                   
                  </div>
                </div>
              </div>

              <div class="col-md-4">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>최근 공지사항</h2>                    
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content" id='noticeList'>
                  	<article class="media event">                      
                      <div class="media-body">                        
                        <p>최근 공지사항이 없습니다.</p>
                      </div>
                    </article>
                  </div>
                </div>
              </div>

              <div class="col-md-4">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>최근 게시물</h2>                    
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content" id='boardList'>
                    <article class="media event">                      
                      <div class="media-body">                        
                        <p>최근 게시물이 없습니다.</p>
                      </div>
                    </article>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /page content -->


</body>
</html>