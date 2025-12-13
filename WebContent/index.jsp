<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/common.jsp"%>
<%@ include file="/common/main_events_calendar2.jsp"%>

<%

boolean isHttp = request.getScheme().equals("http");
String domain = request.getServerName();  // 도메인

if(isHttp) {
	response.sendRedirect("https://"+domain+"/index.jsp");
}

	String _dummy = String.valueOf( Math.random() );


	User __User = null;
	
	__User = getUserObject(request, response);
	
	String __UserNm = "";
	String __emp_no = "";
	String __mem_div = "";
	String __CorpNm = "";
	
	boolean isLogin = false;
	if(__User != null && ( "U".equals(__User.getAuthLevel()) ||"M".equals(__User.getAuthLevel()) ||"P".equals(__User.getAuthLevel())   ) )
	{
		__UserNm = __User.getName();
		__CorpNm = __User.getDataSet().getText("corp_nm");
		__emp_no = (String)__User.getUser_seq_no();
		__mem_div = (String)__User.getDataSet().getText("mem_div");
		isLogin = true;
	}
	
	DataSet input = new DataSet();
	jdf.framework.core.data.InteractionBean interact = new jdf.framework.core.data.InteractionBean();
	DataSet noticeList = new DataSet();
	DataSet faqList = new DataSet();
	DataSet pdsList = new DataSet();
	DataSet eventList = new DataSet();
	
	try
	{
		input.put("board_row_per_page", "5");
		input.put("cur_pg", "1");
		input.put("emp_no", __emp_no);
		input.put("start_dt", "");
		input.put("end_dt", "");
		input.put("p_reg_nm", "");
		input.put("search_word", "");
		input.put("search_txt", "");
		input.put("use_yn", "Y");
		input.put("category_cd", "");
		
		
		
		input.put("cmd", "getNoticeList");
		noticeList = interact.execute("/community/board", input);	
	} catch(Exception ex)
	{}
	
	try
	{
		
		input.put("cmd", "list");
		faqList = interact.execute("/community/FAQ", input);	
	} catch(Exception ex)
	{}
	
	try
	{
		input.put("brd_mng_no", 10);
		input.put("cmd", "getBoard");
		pdsList = interact.execute("/community/board", input);	
	} catch(Exception ex)
	{}
	
	
	String thumb_url = "";
	try
	{
		Config conf = Configuration.lookup("/file/thumb");
		thumb_url = conf.getString("url");
		
	}catch(Exception e){}
	
	try
	{
		input.put("board_row_per_page", 3);
		input.put("cur_pg", 1);
		input.put("brd_mng_no", 4);
		input.put("cmd", "getAlbumBoard");
		eventList = interact.execute("/community/board", input);	
	} catch(Exception ex)
	{}
	

	//STR : 최근 교육 리스트 TOP
	DataSet edctScheduleTopInput = new DataSet();
	DataSet edctScheduleTopList = new DataSet();
	try
	{
		Calendar cal=Calendar.getInstance(); //현재 시스템이 가지고 있는 날짜 데이터 가지고 오기
	 	
 		//조회 하는 년/월이 오늘과 같은지 비교키 위하여 임시 기억...
 		int y=cal.get(Calendar.YEAR);
	 	int m=cal.get(Calendar.MONTH)+1;
	 	
	 	edctScheduleTopInput.put("inYyyyMm",y + ((m < 10)?"0":"")  + m);
	 	//edctScheduleTopInput.put("inYyyyMm","201101");
		edctScheduleTopInput.put("cmd", "getTop5");
		edctScheduleTopList = interact.execute("/edu/edct_schedule", edctScheduleTopInput);	
		
	} catch(Exception ex)
	{}


	

	
	//is_date, ist_title, is_ill, iplace
	
%>
<script language="javascript" src="/static/ebook/001/common.js"></script>
<link rel="stylesheet" type="text/css" href="/static/main/css/popup.css?_dummy=<%=_dummy %>" />
<style>
.topArea.cmbMotion{overflow:hidden;position:fixed;top:0;left:0;z-index:200;width:100%;background-color: white;-webkit-transition: height 1s;-moz-transition: height 1s;transition: height 1s;}
.headerWrap.top{padding-top:145px;}

a.pbtn04					{display:inline-block; vertical-align:middle; color:#fff; height:32px; padding:4px; border:0; background:#48a3f0; cursor:pointer; }
a.pbtn04 span				{display:block; font-weight:bold; color:#fff; height:24px; padding:0 10px; font-size:12px; line-height:26px; background:#48a3f0; text-align:center;  }
a.pbtn05					{display:inline-block; vertical-align:middle; color:#fff; min-width:100px; height:30px; padding:4px; border:0; background:#575757; border-radius:7px; cursor:pointer;  }
a.pbtn05 span				{display:block; font-weight:bold; height:22px;  font-size:13px; line-height:22px; background:#575757; text-align:center; padding:0 10px; color:#fff; }

</style>
<div class="contents">
	<div class="main_cont_img">
		<div class="main_cont_img_txt">
			(사)한국건물위생관리협회 위생교육<br> 홈페이지 방문을 환영합니다.
		</div>
		<img src="/static/main/img/new/main_img.jpg" style="height: 283px;width: 744px;"/>

	</div>

	<div class="main_cont_div_right">
	<% if( isLogin){ %>
			
		<div class="main_login_div">
			<div class="login_div">
				<div class="login_info">
					<strong><%=__CorpNm %>	<%=__UserNm %>님</strong> <br><br>
					한국건물위생관리협회 오신 걸 환영합니다.
				</div>			
			</div>
			<div class="join_div">
				<ul>
					<li><a href="/logout.jsp">로그아웃</a></li>
					<li><a href="/member/member.jspx?cmd=memModifyView">정보변경</a></li>
					<li><a href="/member/member.jspx?cmd=passModifyView">비밀번호변경</a></li>
				</ul>
			</div>
		</div>
		
	<% }else{ %>
		
		<form name="login_form" id="login_form" onSubmit="return false;" autocomplete="off">
		<div class="main_login_div">
			<div class="login_div">
				<p><input type="text" id='user_id' name='user_id' required='true' tabindex="1" onKeyDown="checkForEnter(event);" placeholder="아이디"/></p>
				<p><input type="password" id='psswd' name="psswd" required='true' tabindex="2" onKeyDown="checkForEnter(event);" placeholder="비밀번호"/></p>
				<p class="login_btn"><a href="javascript:actionClick()" tabindex="3">로그인</a></p>
			</div>
			<div class="join_div">
				<ul>
					<li><a href="/member/member.jspx?cmd=id_find">아이디찾기</a></li>
					<li><a href="/member/member.jspx?cmd=passwd_confirm">비밀번호찾기</a></li>
					<li><a href="/member/join.jspx?cmd=join_step_1">회원가입</a></li>
				</ul>
			</div>
		</div>
		</form>
	<% } %>
		<ul class="ebook_div">
			<li class="color_f5f5e9">
			<a href="https://www.google.co.kr/chrome/index.html" target="_blank">
				<div class="icon_div">
					<div class="icon_img">
						<img src="/static/main/img/new/06.png"/>
					</div>
					<span class="">크롬<br>다운로드</span>
				</div>
			</a>
			</li>
			<li class="color_f5f5e9">
			<a href="/static/edu/doc/크롬설치방법_202010.pdf" target="_blank">
				<div class="icon_div">
					<div class="icon_img">
						<img src="/static/main/img/new/07.png"/>
					</div>
					<span class="">크롬<br>설치방법</span>
				</div>
			</a>
			</li>
		</ul>
		<ul class="ebook_div" style="display:none;">
			<li class="color_ccdede">
				<a href="javascript:goMenuPageEdu('/edu/lecture/classCtrl.jspx?cmd=viewClassList', 'U', '<%=__mem_div %>' );">
					<div class="icon_div">
						<div class="icon_img">
							<i class="fa fa-file-movie-o" style="color:#20c9c4;font-size: 40px;"></i>
						</div>
						<span class="padding-T20">나의강의실</span>
					</div>
				</a>
			</li>
			<li class="color_ccdede" style="border-right:1px solid #cfcfcf !important;">
				<a href="/info/info.jspx?cmd=member">
					<div class="icon_div">
						<div class="icon_img">
							<i class="fa fa-search" style="color:#65b1f3;font-size: 40px;"></i>
						</div>
						<span class="padding-T20">회원검색</span>
					</div>
				</a>
			</li>		
		</ul>
		<ul class="ebook_div" style="display:none;">
			<li class="color_ccdede">
				<a href="javascript:ecatalog('/static/ebook/001','','','yes');">
					<div class="icon_div">
						<div class="icon_img">
							<i class="fa fa-book" style="color:#1cb6be;font-size: 35px;"></i>
						</div>
						<span class="padding-T10">EBOOK<br/>통사편</span>
					</div>
				</a>
			</li>
			<li class="color_ccdede" style="border-right:1px solid #cfcfcf !important;">
				<a href="javascript:ecatalog('/static/ebook/002','','','yes');">
					<div class="icon_div">
						<div class="icon_img">
							<i class="fa fa-book" style="color:#ffde00;font-size: 35px;"></i>
						</div>
						<span class="padding-T10">EBOOK<br/>화보편</span>
					</div>
				</a>
			</li>		
		</ul>
	</div>
</div>
<div class="contents" style="display:none">
	<ul class="quick_div">
		<li><img src="/static/main/img/new/quick.jpg"/></li>
		<li class="color_ccdede">
			<a href="javascript:goMenuPageEdu('/edu/lecture/lectureCtrl.jspx?cmd=viewLectureList', 'U', '<%=__mem_div %>' );">
				<div class="icon_div">
					<div class="icon_img">
						<img src="/static/main/img/new/01.png"/>
					</div>
					<span>온라인교육 신청하기</span>
				</div>
			</a>
		</li>
		<li class="color_ccdede">
			<a href="javascript:goMenuPageEdu('/common/calendar.jspx?cmd=edu_calendar_list', 'N', '<%=__mem_div %>' );">
				<div class="icon_div">
					<div class="icon_img">
						<img src="/static/main/img/new/02.png" style="padding-top: 5px;"/>
					</div>
					<span>집합교육<br/>신청하기</span>
				</div>
			</a>
		</li>
		<li class="color_ccdede">
			<a href="javascript:goMenuPageEdu('/edu/lecture/lectureCtrl.jspx?cmd=certList', 'U', '<%=__mem_div %>' );">
				<div class="icon_div">
					<div class="icon_img">
						<img src="/static/main/img/new/03.png"/>
					</div>
					<span class="padding-T20">수료증출력</span>
				</div>
			</a>
		</li>
		<li class="color_ccdede">
			<a href="javascript:goMenuPageEdu('/edu/payment/paymentCtrl.jspx?cmd=viewPaymentList', 'U', '<%=__mem_div %>' );">
				<div class="icon_div">
					<div class="icon_img">
						<img src="/static/main/img/new/04.png"/>
					</div>
					<span class="padding-T20">영수증출력</span>
				</div>
			</a>
		</li>
		<li class="color_ccdede" style="border-right:1px solid #ccdede !important;">
			<a href="/info/info.jspx?cmd=calc">
				<div class="icon_div">
					<div class="icon_img">
						<img src="/static/main/img/new/05.png"/>
					</div>
					<span class="padding-T20">표준도급비</span>
				</div>
			</a>
		</li>
	</ul>
</div>
<div class="contents cb padding-T20">
	<div class="notice_div">
		<ul class="notice_div_nav_ul">
			<li class="notice_on"><a href="#">공지사항</a></li>
			<!-- <li><a href="#">자료실</a></li> -->
		</ul>
		<div class="notice_cont_div">
			<ul class="notice_cont_0">
<%
	for(int idx=0; idx<noticeList.getMaxDataSize(); idx++)
	{
%>								
				<li>
					<a href="/board/board.jspx?cmd=notice_view&brd_no=<%=noticeList.getText("brd_no", idx)%>&brd_mng_no=<%=noticeList.getText("brd_mng_no", idx) %>">
					<span class="notice_cont"><%=jdf.framework.core.util.HtmlFormat.fixLength(noticeList.getText("ttl", idx), 50) %></span></a>
					<span class="notice_date"><%=noticeList.getText("reg_ddtm", idx) %></span>
				</li>
<%
	}
%>				
			</ul>
			<ul class="notice_cont_1">
<%
	for(int idx=0; idx<pdsList.getMaxDataSize(); idx++)
	{
%>								
				<li>
					<a href="/board/board.jspx?cmd=view&brd_mng_no=<%=pdsList.getText("brd_mng_no", idx) %>&brd_no=<%=pdsList.getText("brd_no", idx) %>">
					<span class="notice_cont"><%=jdf.framework.core.util.HtmlFormat.fixLength(pdsList.getText("ttl", idx), 50) %></span></a>
					<span class="notice_date"><%=pdsList.getText("reg_dt", idx) %></span>
				</li>
<%
	}
%>			
			</ul>
		</div>
	</div>
	<div class="notice_div_right">
		<div class="event_div">
			<ul class="event_div_nav_ul">
				<li class="event_on"><a href="#"><span>협회교육일정</span></a></li>
				<!-- <li><a href="#"><span>협회행사일정</span></a></li> -->
			</ul>
			<div class="event_cont_div">
				<ul class="event_cont_0" id="calList">
<%
	for (int i=0 ; i < edctScheduleTopList.getCount("is_date"); i++){
%>		
			<li>				
				<span class="event_cont" title="<%=edctScheduleTopList.getText("ist_title", i)%>"><%=jdf.framework.core.util.HtmlFormat.fixLength(edctScheduleTopList.getText("ist_title", i), 35) %></span>
				<span class="event_date"><%=edctScheduleTopList.getText("is_date", i) + ' ' + edctScheduleTopList.getText("stime", i) %></span>
			</li>
<%		
	}
%>	

	
					
				</ul>

			</div>
		</div>
		
		<!--  썸네일 롤링 배너 -->
		<div class="thumList" id="thumListBox" style="display:none;">
			<ul class="cont">
<%
for(int idx=0; idx<eventList.getMaxDataSize(); idx++)
{
%>						
				<li><a href="/board/board.jspx?cmd=list&brd_mng_no=<%=eventList.getText("brd_mng_no", idx) %>"><img src="<%=thumb_url %>/<%=eventList.getText("file_path", idx)%>/<%=eventList.getText("file_nm", idx)%>" width="235" height="180" alt="" /></a></li>
				
<%
}
%>										
					
			</ul>
			<div class="tab">
				<a href="#" class="on"></a>
				<a href="#"></a>
				<a href="#"></a>
			</div>
		</div>
		<script language="javascript" type="text/javascript">
		//<![CDATA[
			var rolling_main_visual = new krcodeTab ({
				tab : "#thumListBox .tab a"	
				,content :"#thumListBox .cont li"	
				,addClass :  'on'						
				,eventType : "mouseover focus"				
				,effect  : 'slide'						
				,interval : 4000				
				,rolling : true							
			});
		//]]>
		</script>
		<!--  썸네일 롤링 배너 -->
		
		
	</div>
</div>
<div class="contents padding-T20">
	<div class="call_div">
		<ul>
			<li><img src="/static/main/img/new/tel.png"/>전화문의 <span>02-465-5900</span></li>
			<li class="two_call">
				<p>근무시간 : 09:00 ~ 18:00(월 ~ 금)</p>
				<p>점심시간 : 12:00 ~ 13:00</p>
			</li>
			<li>
				<p>토.일.공휴일 휴무</p>
			</li>
		</ul>
	</div>
</div>
<div class="contents padding-T20">
	<div class="family_div">
		
		<!-- S :: 배너슬라이드 -->
		<div class="mainSlide" id="mainFootBanner">
			<div class="soldeWrap">	
			<ul class="" id="mainFootBannerList">
			
			</ul>
			</div>
			<div class="btns">
				<a href="#" class="left"><img src="/static/main/img/main/footer_slide_btn_l.jpg" width="14" height="18" alt="" /></a>
				<a href="#" class="right"><img src="/static/main/img/main/footer_slide_btn_r.jpg" width="14" height="18" alt="" /></a>
			</div>
		</div>
	</div>
</div>
			

<script language="javascript" type="text/javascript">
//<![CDATA[
	//이달의 행사 월 변경
	function getEventsCalendar(y,m){
		var _url = "/common/action/main_contents.jspx?cmd=getMainEventsCalendar&y=" + y + "&m=" + m;	

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
				$("#main_events_calendar").html(Data);
			}			
		} );
	}
	
	function setInitEduList()
	{
		var now = new Date();
		
		getEventsCalendar(now.getFullYear() ,now.getMonth()+1 );
		getEduTop5List();
	}
	//최초 교육 일정 가져오기 
	function getEduTop5List(){
		var _url = "/common/action/main_contents.jspx?cmd=getEduTop5List";	

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'text',
	   		data : "", 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				//alert(xhr.status);
			},
			success: function(Data)
			{	
				$("#calList").html(Data);
			}			
		} );
	}
	
	//행사 클릭 시 교육 일정 가져오기 
	function getEduList(fmtIsDate){
		var _url = "/common/action/main_contents.jspx?cmd=getEduList&is_date=" + fmtIsDate;	

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'text',
	   		data : "", 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				//alert(xhr.status);
			},
			success: function(Data)
			{	
				$("#calList").html(Data);
			}			
		} );
	}
//]]>
</script>
<script type="text/javascript" type="text/javascript">
//<![CDATA[	
	
	function goAlertEdu()
	{
		
			var txt = '[알림]\n';
				txt += '2020년도 집합교육은 [코로나19]추가\n';
				txt += '확산으로 인해 미실시 하오니 온라인 교육으로\n';
				txt += '수강하시기 바랍니다.';
			
			alert(txt);
		
	}
	function checkForEnter(event)
	{
		if ((event.which && event.which == 13) || (event.keyCode && event.keyCode == 13))
   		{
			actionClick();
		}
	}

	function actionClick()
	{

		if(!checkFormField('#login_form'))
		{
			return;
		}

		var http = jQuery.ajax( {
	   		url: "/common/action/login.jspx",
	   		type: "POST",
			data : jQuery('#login_form').serialize(true),
	   		async : false,
	   		error 	: function(xml)
	   		{
				msgStart(msg_com_code_007);
			},
	   		success:function(xmlDoc)
	   		{
	   			var code = jQuery(xmlDoc).find('code').text();
		        var msg = jQuery(xmlDoc).find('msg').text();

		        if(code == '200') 
		        {
		        	var status = jQuery(xmlDoc).find('status').text();
					var end_yn = jQuery(xmlDoc).find('end_yn').text();
					var close_yn = jQuery(xmlDoc).find('close_yn').text();
		        	var tmp_pass_yn = jQuery(xmlDoc).find('tmp_pass_yn').text();
		        	var user_nm = jQuery(xmlDoc).find('user_nm').text();
		        	var corp_nm = jQuery(xmlDoc).find('corp_nm').text();
		        	var corp_addr = jQuery(xmlDoc).find('corp_addr').text();
		        	
					if( close_yn == 'Y')
						alert(user_nm + "님 환영합니다.\n" + "폐업처리된 정보입니다. 협회에 문의하세요. Tel. 02)465-5900");
					else
						openCorpChangePop(user_nm , corp_nm, corp_addr, tmp_pass_yn);
					
					if( status == '09')
						alert('5회 불합격 하셨습니다. \n사무국에 역락하여 수강 재신청하기 바랍니다.\n전화. 02)465-5900');
					else if( end_yn == 'Y')
						alert('교육수강기간이(14일)이 경과 하였습니다. \n교육수강 메뉴에서 기간연장 하신 후 재수강하시기 바랍니다.');
					
				} else
				{
					msgOpen(msg);
				}
			}
		}
	);
	}
	
	function openCorpChangePop(user_nm , corp_nm, corp_addr, tmp_pass_yn)
	{
		if(tmp_pass_yn == 'Y'){
			location.href="/member/member.jspx?cmd=passModifyView";
       	}		        	
       	else{
       		if(getCookie('popup_layer_001' ) != 'N')
			{						
       			jQuery('#popUname').html(user_nm);
				jQuery('#popCorpName').html(corp_nm);
				jQuery('#popCorpAddr').html(corp_addr);
				
				openLayerPop('popup_layer_001');
			}else{
				alert(user_nm + "님 환영합니다.");
				location.href="/index.jsp";
			}
        }
	}
	
	function goUserChange()
	{
		location.href='/member/member.jspx?cmd=memModifyView';
	}
	
	//banner_div_01:메인베너, banner_div_02:교육 베너
	function initBanner(banner_div){
		var _url = "/common/action/main_contents.jspx?cmd=getBannerList&banner_div=" + banner_div;	

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'text',
	   		data : "", 
			mtype: 'POST',
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				//alert(xhr.status);
			},
			success: function(Data)
			{		
				//alert(Data);
				$("#mainFootBannerList").html(Data);
			}			
		} );
	}

	function initPopup(popup_div){
		var _url = "/common/action/main_contents.jspx?cmd=getPopupList&popup_div=" + popup_div;	
		

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'json',
	   		data : "", 
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

   				var dataCount = 0;
				if( data.popup_list ) 
					dataCount =  data.popup_list.length;
				
				
				if (code == 200){		
					var left = 0;
		   			for(var i=0 ; i < data.popup_list.length ; i++)
		   			{	
		   				

		   				//하루동안 열지 않기		
		   				if(getCookie('popup_layer_' + data.popup_list[i].seq_no ) != 'N')
		   				{

			   				var content_url = "/common/action/main_contents.jspx?cmd=getPopupContent&seq_no=" + data.popup_list[i].seq_no;
			   				
			   				//팝업 형식 
			   				if(data.popup_list[i].popup_type == 'popup_type_01'){
			   					var specs = "fullscreen=no";
			   					specs += ",width=" + data.popup_list[i].width  ;
			   					specs += ",height=" + data.popup_list[i].height  ;	
			   					specs += ",left=" + left  ;	
			   					left += Number(getNumOnly(data.popup_list[i].height));
			   					window.open(content_url, "main_pop_" + data.popup_list[i].seq_no , specs, "false");
			   					
			   					
			   				}

			   				//레이어 형식
			   				if(data.popup_list[i].popup_type == 'popup_type_02'){
			   					var http = jQuery.ajax( {
							   		url: content_url,	
							   		datatype : 'text',
							   		data : "open_idx=" + i, 
									mtype: 'POST',
							   		async : false,	   		
							   		error 	: function(xhr)
							   		{										
									},
									success: function(text)
									{	
										$("body").append(text);
									}	
								} );
			   				}
			   			}
		   			}				   		
		   		}else{
		   		
		   		}				   		
			}			
		} );
	}

	// layerPopup
    openLayerPop = function(target){
        $('#' + target).css('display', 'block');
        $('html,body').css('overflow','hidden');
    }

    closeLayerPopup = function(){
        $('.layerPopoup').css('display', 'none');
        $('html,body').css('overflow','');
    }

	jQuery(document).ready(function(){
		initPopup("popup_div_01");
		initBanner("banner_div_01");
		
		foot_slide();
	});
	
//]]>
</script>



<div class="layerPopoup layerMainPop" id="popup_layer_001" style="min-height: 300px;display:none;">	
	<div class="dim"></div>
	<div class="popWrap" style="position: absolute;top: 100px;width: 500px;margin: 0 30%;">
		<div class="contents" style="width:100%">
			<div class="header" style="width: 100%;">
				업체정보 확인
			</div>
            <div class="mainPop" style="font-weight: bold;padding: 15px;">
            
					<table class="table table-striped jambo_table bulk_action">
							<colgroup>
								<col width="90px"/><col width=""/>
							</colgroup>
							<tbody>
								<tr class="even pointer">
									<th class="a-center">성함(교육생)</th>
									<td class=" last" id="popUname"></td>
								</tr>
								<tr class="even pointer">
									<th class="a-center">회사명</th>
									<td class=" last" id="popCorpName"></td>
								</tr>
								<tr class="even pointer">
									<th class="a-center">회사주소</th>
									<td class=" last" id="popCorpAddr"></td>
								</tr>
							</tbody>
						</table>
			
					반드시 교육생 및 회사정보가 맞는지 확인바랍니다.<br>
					정보가 맞는 경우 맨 아래 닫기를 누르시고, 회사정보가 변경된 경우 변경내용 입력 후 변경요청을 클릭하세요.<br> 
					<span style="color:red;">※ 교육생은 변경불가 (새로 가입해야 함)</span><br>
					<span style="color:red;">※ 교육생(대표자or책임자)이 여러 개 회사를 가지고있는 경우 개별 가입 해야 함</span><br>
		            
        	</div>
        	<span class="pull-right" style="padding: 0 0;">		
				<a class="pbtn05" onclick="goUserChange();"><span class="">확인</span></a>
			</span>
            <div style="width: 200px; height:22px; padding:5px; bottom:0px; text-align: 1">
				<a href="#" onclick="setCookie('popup_layer_001','N',1) ; goUserChange();"><img src="/static/com/img/checkbox.jpg" style="vertical-align: -6px;width:21px;"> 오늘하루 그만보기</a>
			</div>
        </div>	
		<!-- <div class="btnWrap">
            <a href="javascript:;" style="" class="btnClose" onclick="closeLayerPopup();">닫기</a>
        </div> -->
	</div>
</div>	
	