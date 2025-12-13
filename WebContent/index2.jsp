<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/common.jsp"%>
<%@ include file="/common/main_events_calendar.jsp"%>
<%
	String _dummy = String.valueOf( Math.random() );


	User __User = null;
	
	__User = getUserObject(request, response);
	
	String __UserNm = "";
	String __emp_no = "";
	String __mem_div = "";
	
	boolean isLogin = false;
	if(__User != null && ( "U".equals(__User.getAuthLevel()) ||"M".equals(__User.getAuthLevel()) ||"P".equals(__User.getAuthLevel())   ) )
	{
		__UserNm = __User.getName();
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
		edctScheduleTopInput.put("cmd", "getTop5");
		edctScheduleTopList = interact.execute("/edu/edct_schedule", edctScheduleTopInput);	
		
	} catch(Exception ex)
	{}


	

	
	//is_date, ist_title, is_ill, iplace
	
%>
<link rel="stylesheet" type="text/css" href="/static/main/css/popup.css?_dummy=<%=_dummy %>" />
<script type="text/javascript" src="/static/lib/mousewheel/jquery.mousewheel.js"></script>
<script type="text/javascript" src="/static/lib/jquery.easing/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="/static/com/js/mouse.js?_=<%=_dummy%>"></script>
<link href="/static/main/css/main.css?_dummy=<%=_dummy %>" type="text/css" rel="stylesheet"  media="screen" />
<style>
.topArea.cmbMotion{overflow:hidden;position:fixed;top:0;left:0;z-index:200;width:100%;background-color: white;-webkit-transition: height 1s;-moz-transition: height 1s;transition: height 1s;}
.headerWrap.top{padding-top:145px;}

a.pbtn04					{display:inline-block; vertical-align:middle; color:#fff; height:32px; padding:4px; border:0; background:#48a3f0; cursor:pointer; }
a.pbtn04 span				{display:block; font-weight:bold; color:#fff; height:24px; padding:0 10px; font-size:12px; line-height:26px; background:#48a3f0; text-align:center;  }
a.pbtn05					{display:inline-block; vertical-align:middle; color:#fff; min-width:100px; height:30px; padding:4px; border:0; background:#575757; border-radius:7px; cursor:pointer;  }
a.pbtn05 span				{display:block; font-weight:bold; height:22px;  font-size:13px; line-height:22px; background:#575757; text-align:center; padding:0 10px; color:#fff; }

</style>
	<div class="main_container">
		<div class="mainContent">
			<div class="l_cont">
				<!-- LOGIN -->
				<div class="loginBox">
					<% if( isLogin){ %>
						<table class="" summary="" style="width:100%;height:80px;">
							<caption></caption>
							<colgroup>
								<col width="100%"/>
							</colgroup>
							<tbody>
								<tr>
									<td style="text-align:center;">
										<strong><%=__UserNm %>님</strong> <br><br>
										한국건물위생관리협회 오신 걸 환영합니다. 
									</td>
								</tr>
							</tbody>
						</table>
					
					
						<div class="loginBtn">
							<a href="/logout.jsp">로그아웃</a>
							<a href="/member/member.jspx?cmd=memModifyView">정보변경</a>
							<a href="/member/member.jspx?cmd=passModifyView">비밀번호변경</a>
						</div>
					<% }else{ %>
						<form name="login_form" id="login_form" onSubmit="return false;" autocomplete="off">
						<table class="" summary="" style="width:100%;height:80px;">
							<caption></caption>
							<colgroup>
								<col width="90"/><col width="150"/><col width=""/>
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">아이디</th>
									<td><input type="text" class="it " title="" value="" id='user_id' name='user_id' required='true' tabindex="1" onKeyDown="checkForEnter(event);"/></td>
									<td rowspan="2">
										<a href="javascript:actionClick()"class="loginBtn" tabindex="3">LOGIN</a>
									</td>
								</tr>
								<tr>
									<th scope="row">비밀번호</th>
									<td><input type="password" class="it " title="" value="" id='psswd' name="psswd" required='true' tabindex="2" onKeyDown="checkForEnter(event);"/></td>
								</tr>
							</tbody>
						</table>
						</form>
						<div class="loginBtn">
							<a href="/member/member.jspx?cmd=id_find">아이디찾기</a>
							<a href="/member/member.jspx?cmd=passwd_confirm">비밀번호찾기</a>
							<a href="/member/join.jspx?cmd=join_step_1">회원가입</a>
						</div>
					<% } %>	
					
					
					
				</div>
				<!-- LOGIN -->
				
				<!-- 이달의 행사일정 -->
				<div class="planBoxWrap">
				<div class="planBox">
					<div class="tit titFont">
						<span>이달의 행사일정</span> <span style="float:right;margin-right:30px;"><a href="javascript:setInitEduList();"><i class="fa fa-refresh"></i></a></span>
					</div>
					<div id="main_events_calendar">
					<%=getEventsCalendar(request)%>		

					</div>
					<script language="javascript" type="text/javascript">
						//<![CDATA[
							//이달의 행사 월 변경
							function getEventsCalendar(y,m){
								//alert(y + "," + m);
								var _url = "/common/action/main_contents.jspx?cmd=getMainEventsCalendar&y=" + y + "&m=" + m;	

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

					<ul class="calList" id="calList">
						<%
						for (int i=0 ; i < edctScheduleTopList.getCount("is_date"); i++){
							out.println("<li>" );
							out.println("<em class=''>" + edctScheduleTopList.getText("is_date", i) + " ("+edctScheduleTopList.getText("stime", i) +"-"+ edctScheduleTopList.getText("etime", i)+ ") " + "</em>" );
							out.println("<span>" + edctScheduleTopList.getText("ist_title", i) +  "</span>" );
							out.println("<span>" + edctScheduleTopList.getText("jisaname", i) + " / " + edctScheduleTopList.getText("iplace", i)  + "</span>" );
							out.println("</li>" );
						}
						%>

					</ul>

				</div>
				</div>
				<!-- 이달의 행사일정 -->

			</div>
			<div class="c_cont">
				<!-- 온라인교육 배너 -->
				<div class="bnr_1">
					<a href="javascript:goMenuPageEdu('/edu/lecture/lectureCtrl.jspx?cmd=viewLectureList', 'U', '<%=__mem_div %>' );" class="btn_1_1 titFont">온라인교육<br> 신청하기 > </a>
					<a href="javascript:goAlertEdu('/edu/lecture/offlineCtrl.jspx?cmd=offlineSelect', 'N', '<%=__mem_div %>' );" class="btn_1_2 titFont">집합교육<br> 신청하기 >  </a>
				</div>
				<!-- 온라인교육 배너 -->


				<!--  출력 -->
				<div class="bnr_2">
					<a href="javascript:goMenuPageEdu('/edu/lecture/lectureCtrl.jspx?cmd=certList', 'U', '<%=__mem_div %>' );" class="btn_2_1 titFont">수료증 출력</a>
					<a href="javascript:goMenuPageEdu('/edu/payment/paymentCtrl.jspx?cmd=viewPaymentList', 'U', '<%=__mem_div %>' );" class="btn_2_2 titFont">영수증 출력</a>
				</div>
				<!--  출력 -->
				
				<!--  표준도급비 -->
				<div class="bnr_3">
					<a href="/info/info.jspx?cmd=calc" class="titFont">표준도급비</a>
				</div>
				<!--  표준도급비 -->


				<!-- 메인 게시판 -->
				<div class="noticeBox" >
					<ul class="newsTabCont tab3" id="newsTab">
						<li class="on"><a href="#">공지사항</a>
							<div class="">
								<ul class="">
<%
	for(int idx=0; idx<noticeList.getMaxDataSize(); idx++)
	{
%>								
									<li>
										<a href="/board/board.jspx?cmd=notice_view&brd_no=<%=noticeList.getText("brd_no", idx)%>&brd_mng_no=<%=noticeList.getText("brd_mng_no", idx) %>"><%=jdf.framework.core.util.HtmlFormat.fixLength(noticeList.getText("ttl", idx), 30) %></a>
										<span class=""><%=noticeList.getText("reg_ddtm", idx) %></span>
									</li>
<%
	}
%>									
								</ul>
							</div>
						</li>

						<li><a href="#">자료실</a>
							<div class="">
								<ul class="">
<%
	for(int idx=0; idx<pdsList.getMaxDataSize(); idx++)
	{
%>								
									<li>
										<a href="/board/board.jspx?cmd=view&brd_mng_no=<%=pdsList.getText("brd_mng_no", idx) %>&brd_no=<%=pdsList.getText("brd_no", idx) %>"><%=jdf.framework.core.util.HtmlFormat.fixLength(pdsList.getText("ttl", idx), 30) %></a>
										<span class=""><%=pdsList.getText("reg_dt", idx) %></span>
									</li>
<%
	}
%>										
								</ul>
							</div>
						</li>
					</ul>
				</div>
				<script language="javascript" type="text/javascript">
				//<![CDATA[
					var rolling_main_visual = new krcodeTab ({
						tab : "#newsTab >li> a"	
						,content :"#newsTab div"	
						,parentAddClass :  'on'						
						,eventType : "mouseover focus"				
					});
				//]]>
				</script>
				<!-- 메인 게시판 -->
				
			</div>
			<div class="r_cont">
				
				
				<!--  교육신청 -->
				<div class="bnr_4" style="height: 151px;">
					<a href="javascript:goMenuPageEdu('/edu/lecture/classCtrl.jspx?cmd=viewClassList', 'U', '<%=__mem_div %>' );" class="titFont">나의강의실  바로가기 > </a>
					<img src="/static/main/img/main/edu_quick_menu_main2_1.jpg" alt="" usemap="#main_right_banner2" style="position: relative;top: -153px;left: 255px;"/>
				</div>
				<map name="main_right_banner2">
					<area shape="rect" coords="5,5, 82, 73" href="https://www.google.co.kr/chrome/index.html" 		target="_blank" outline="none">
					<area shape="rect" coords="5,80, 82, 160" href="/static/edu/doc/크롬설치방법_202010.pdf" 		target="_blank" outline="none">
					
				</map>
				<!--  교육신청 -->
				
				<!--  썸네일 롤링 배너 -->
				<div class="thumList" id="thumListBox">
					<ul class="cont">
<%
	for(int idx=0; idx<eventList.getMaxDataSize(); idx++)
	{
%>						
						<li><a href="/board/board.jspx?cmd=list&brd_mng_no=<%=eventList.getText("brd_mng_no", idx) %>"><img src="<%=thumb_url %>/<%=eventList.getText("file_path", idx)%>/<%=eventList.getText("file_nm", idx)%>" width="332" height="219" alt="" /></a></li>
						
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
				


				<!--  전화문의 -->
				<div class="callBnr titFont">
					<div class="tit">
						전화문의
					</div>
					<strong>02-465-5900</strong>
					<div class="info">
						<img src="/static/main/img/main/call_bnr.jpg" width="245" height="82" alt="" />
					</div>
				</div>
				<!--  전화문의 -->
			</div>
		</div>		
	</div>


	<!-- S :: 배너슬라이드 -->
	<div class="mainSlide" id="mainFootBanner">
		<div class="soldeWrap">	
		<ul class="" id="mainFootBannerList">
		
		</ul>

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
				        	
							if( close_yn == 'Y')
								alert(user_nm + "님 환영합니다.\n" + "폐업처리된 정보입니다. 협회에 문의하세요. Tel. 02)465-5900");
							else
								openCorpChangePop(user_nm , tmp_pass_yn);
							
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
			
			function openCorpChangePop(user_nm , tmp_pass_yn)
			{
				if(tmp_pass_yn == 'Y'){
					location.href="/member/member.jspx?cmd=passModifyView";
	        	}		        	
	        	else{
	        		if(getCookie('popup_layer_001' ) != 'N')
					{						
						jQuery('#popUname').html(user_nm);
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

		</div>
		<div class="btns">
			<a href="#" class="left"><img src="/static/main/img/main/footer_slide_btn_l.jpg" width="14" height="18" alt="" /></a>
			<a href="#" class="right"><img src="/static/main/img/main/footer_slide_btn_r.jpg" width="14" height="18" alt="" /></a>
		</div>
	</div>


	<div class="layerPopoup layerMainPop" id="popup_layer_001" style="min-height: 300px;display:none;">	
		<div class="dim"></div>
		<div class="popWrap" style="position: absolute;top: 100px;width: 500px;margin: 0 30%;">
			<div class="contents">
				<div class="header" style="width: 100%;">
					업체정보 확인
				</div>
	            <div class="mainPop" style="font-weight: bold;padding: 15px;">
	            
						<span id="popUname"></span>님 환영합니다.<br>
				
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
	