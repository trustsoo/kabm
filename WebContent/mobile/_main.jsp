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
	
	requestNoticeList();
	
	$('#compose').on('click',function(){		
		if(confirm('모바일네트워크(3G/LTE/5G)로 연결하시면 데이터통화료가 발생할 수 있으니 와이파이(Wi-Fi)로 연결하시어 동영상수강을 권장합니다. \n 계속하시겠습니까?'))
		location.href = '/mobile/edu/class/classCtrl.jspx?cmd=list';
	});
	
	$('#lecture').on('click',function(){		
		location.href = '/mobile/edu/lecture/lectureCtrl.jspx?cmd=viewLectureList';
	});
	
	$('#payment').on('click',function(){		
		location.href = '/mobile/edu/payment/paymentCtrl.jspx?cmd=viewPaymentList';
	});
	
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
	   				var imp_yn = data.notice_list[i].imp_yn;
	   				var imp_nm = (imp_yn == 'Y') ? '[알림]' : ''; 
	   				_html.push("<article class='media event'>");
	   				_html.push("<a class='pull-left date'>");
	   				_html.push("<p class='month'>"+reg_mm+"월</p>");
	   				_html.push("<p class='day'>"+reg_dd+"</p>");
	   				_html.push("</a>");
	   				_html.push("<div class='media-body'>");
	   				_html.push("<a class='title'>"+imp_nm+"</a>");
	   				_html.push("<p><a href='javascript:detailNotice("+data.notice_list[i].brd_no+")'>"+title+"</a></p>");
	   				_html.push("</div>");
	   				_html.push("</article>");    
	   				
	   			}
	   			if(data.notice_list.length > 0 )
	   				jQuery('#noticeList').html(_html.join(''));
	   		}		   			
	   		
		}
	});
}

</script>

</head>

<body> 

<div role="main">
	<div class="page-title">
		<div class="title_left" style="width: 100%;margin: 0px 5px;">
			<h3>한국건물위생관리협회 </h3>
		</div>
	</div>
	
	<div class="x_panel">
		<div class="x_content">
			<button id="lecture" class="btn btn-lg btn-success btn-block" type="button"> 온라인교육 신청 <i class="fa fa-calendar" ></i></button>
		</div>
		<div class="x_content">
			<button id="compose" class="btn btn-lg btn-info btn-block" type="button"> 온라인수강 바로가기 <i class="glyphicon glyphicon-facetime-video" ></i></button>
		</div>
		<div class="x_content">
			<button id="payment" class="btn btn-lg btn-primary btn-block" type="button"> 결제내역/영수증 <i class="fa fa-credit-card" ></i></button>
		</div>
	</div>       


    <div class="x_panel">
      <div class="x_title">
        <h2>공지사항</h2>                    
        <div class="clearfix"></div>
      </div>
      <div class="x_content" id='noticeList'>
      	<article class="media event">                      
          <div class="media-body">                        
            <p>공지사항이 없습니다.</p>
          </div>
        </article>
      </div>
    </div>
	
</div>
<div class="modal fade bs-notice-modal-lg" tabindex="-1" role="dialog" aria-hidden="true">
<div class="modal-dialog modal-lg">
<div class="modal-content">
<div class="modal-header">
<h4 class="modal-title" style="float: left;">공지사항</h4>
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
        <!-- /page content -->
<script>


var detailNotice = function(notice_no){

    $(".modal-body").load('/mobile/board/board.jspx?cmd=notice_view&brd_no='+notice_no+'&brd_mng_no=10');

    $('.bs-notice-modal-lg').modal({
        fadeDuration: 250
      });
};
</script>

</body>
</html>