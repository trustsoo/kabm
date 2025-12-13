<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<%
String brd_no 					= input.getText("brd_no");
String comment_yn			= output.getText("comment_yn");
String fileupload_yn			= output.getText("fileupload_yn");
String viewMode 						= input.getText("viewMode");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/static/main/css/board.css?_=1128" rel="stylesheet">
<script>
	
	function js_detail()
	{
		var brd_no = jQuery('#brd_no').val();
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=doBoardRead',
			data : 'brd_no='+brd_no,
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
					jQuery('#reg_dt').html(json.result.data.board_detail[0].reg_dt);
		   			jQuery('#reg_emp_nm').html(json.result.data.board_detail[0].emp_nm);
		   			jQuery('#ttl').html(json.result.data.board_detail[0].ttl);
		   			jQuery('#aaaa').html(json.result.data.board_detail[0].cntnt);
		   			jQuery('#cntnt').html('');
		   			jQuery('#cntnt').html(jQuery('#aaaa').text());		   	
		   			jQuery('#read_cnt').html(json.result.data.board_detail[0].read_cnt);
		   			jQuery('#email').html(json.result.data.board_detail[0].email);
		   			
		   			var fileupload_yn = json.result.data.board_detail[0].fileupload_yn;
		   			var comment_yn = json.result.data.board_detail[0].comment_yn;
		   			
		   			if(fileupload_yn == 'Y' && json.result.data.board_detail[0].is_exist_file == 'Y')
		   			{
		   				js_getFileEntries('gridFileEntriesDiv',false, jQuery('#attach_div_cd').val(), json.result.data.board_detail[0].brd_no);
		   			}
		   			
				}
			}
		});	
	}
	
	/*댓글 조회*/	
	var jsRequestCommentRead = function()
	{
		var brd_no = jQuery('#brd_no').val();
		var user_id = jQuery('#user_id').val();
		var board_comment_page_cnt = jQuery('#board_comment_page_cnt').val();
		
		var _url = '/board/action/board.jspx?cmd=doCommentRead';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : jQuery('#frm').serialize(true),		
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(xmlDoc)
			{	
				var code = jQuery(xmlDoc).find('code').text();
		        var msg = jQuery(xmlDoc).find('msg').text();

		        if(code == '200')
		        {
		        	var comment_list = [];
					jQuery(xmlDoc).find('comment_info').each(function(){
						comment_list.push("<div class=\"itemdiv dialogdiv\" id=\"comment_"+jQuery(this).find('cmnt_no').text()+"\">");
						comment_list.push("	 <div class=\"user\" style=\"margin-top:10px\">");
						comment_list.push("	   		<span class=\"blue\"> "+jQuery(this).find('user_nm').text()+"</span>");
						comment_list.push("	 </div>");
						comment_list.push("	 <div class=\"body comment_content_box\">");
						comment_list.push("	 	<div style=\"\" >"); 
            			comment_list.push("			<span class=\"orange\">"+jQuery(this).find('reg_dt').text()+"</span>");

            			if(user_id == jQuery(this).find('user_id').text() ){
                            comment_list.push("         <span style=\"float:right;padding-left:5px\">");
                            comment_list.push("             <span onClick=\"javascript:jsRequestCommentDel("+jQuery(this).find('cmnt_no').text()+")\" style=\"cursor:pointer;font-size:15px;color:red; font-weight:bold;\">X</span>");
                            comment_list.push("         </span>");
                        }                       
                        else
                        {
                            comment_list.push("         <span style=\"float:right;padding-left:10px\">");
                            comment_list.push("         </span>");                           
                        } 						
						
						comment_list.push("		</div>");
						
						comment_list.push("	 	<div class=\"text P-T5\">");
						comment_list.push(			jQuery(this).find('cntnt').text());
						comment_list.push("		</div>");
						comment_list.push("	 </div>");
						comment_list.push("</div>");
						jQuery('#last_page').val(jQuery(this).find('last_page').text());
						if(jQuery(this).find('rnum').text()%board_comment_page_cnt == 0){
							jQuery('#pre_page_comment_cnt').val(board_comment_page_cnt);	
						}
						else{
							jQuery('#pre_page_comment_cnt').val(jQuery(this).find('rnum').text()%board_comment_page_cnt);	
						}
					});
					
					jQuery('#board_comments').html(comment_list.join(""));
					
					if(jQuery('#last_page').val()>0){
						var comment_page = [];
						
						comment_page.push("<ul class=\"pagination\">");
						comment_page.push("		<li id=\"pagination_prev\">");
						comment_page.push("			<a href=\"javascript:comment_page('P');\">");
						comment_page.push("				<i class=\"icon- fa fa-angle-double-left\"></i>");
						comment_page.push("			</a>");
						comment_page.push("		</li>");
						
						for(var ins=1;ins<=jQuery('#last_page').val();ins++){
							comment_page.push("		<li id=\"pagination_"+ins+"\">");
							comment_page.push("			<a href=\"javascript:comment_page("+ins+");\">"+ins+"</a>");
							comment_page.push("		</li>");
						}
							
						comment_page.push("		<li id=\"pagination_next\">");
						comment_page.push("			<a href=\"javascript:comment_page('N');\">");
						comment_page.push("				<i class=\"icon- fa fa-angle-double-right\"></i>");
						comment_page.push("			</a>");
						comment_page.push("		</li>");
						comment_page.push("</ul>");
						comment_page.push("<p class=\"hr_line\"></p>");
					
						jQuery('#board_comments_page').html(comment_page.join(""));
					}
					
					jQuery('.pagination').css('margin', '5px');
					jQuery('#pagination_'+jQuery('#pre_page').val()).addClass("active");
					if(jQuery('#pre_page').val() == jQuery('#last_page').val()){
						jQuery('#pagination_next').addClass("disabled");
					}
					if(jQuery('#pre_page').val() == 1){
						jQuery('#pagination_prev').addClass("disabled");
					}
		        } else
		        {
		        	msgStart(msg_com_code_010+"("+msg+")");
		        }
			}			
		});	
	};		
	
	/*댓글 저장*/
	var jsRequestCommentSave = function(){
		
		var brd_no = jQuery('#brd_no').val();
		
		if(jQuery('#user_id').val()=="")
		{
			msgStart(msg_board_code_002);	
			jQuery('#comment_area').focus();
			return;
		}
		
		if(jQuery('#comment_area').val()=="")
		{
			msgStart(msg_board_code_001);	
			jQuery('#comment_area').focus();
			return;
		}
		else
		{
			var _url = '/board/action/board.jspx?cmd=doCommentAdd';
			var http = jQuery.ajax( {
		   		url: _url,	   		
		   		type: "POST",
				data : {
						use_cntnt:jQuery('#comment_area').val(),
						use_brd_no:brd_no
					   },
		   		async : false,	   		
		   		error 	: function(xhr)
		   		{
					alert(xhr.status);
				},
				success: function(xmlDoc)
				{	
					jsRequestCommentRead();
					jQuery('#comment_area').val('');
				}			
			});	
		}
	};
	
	/*댓글 삭제*/
	var jsRequestCommentDel = function(cmnt_no, use_emp_no)
	{
		if(!confirm("댓글을 삭제하시겠습니까?")) return
			
		var _url = '/board/action/board.jspx?cmd=doCommentDel';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : {
					use_cmnt_no:cmnt_no,
					use_emp_no:use_emp_no
				   },
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(xmlDoc)
			{	
				jQuery('#comment_'+cmnt_no).remove();
				if(jQuery('#pre_page').val() == 1){
					comment_page('P');
				}
				comment_page(jQuery('#pre_page').val());
				msgStart(msg_board_code_003);
			}			
		});	
	}
	
	/*댓글 페이지 이동*/
	var comment_page = function(mv_page){

		if(mv_page=='P'){
			if(jQuery('#pre_page').val() > 1){
				jQuery('#pre_page').val(parseInt(jQuery('#pre_page').val())-1);
				jsRequestCommentRead();
			}
		} else if(mv_page=='N'){
			if(jQuery('#pre_page').val() < jQuery('#last_page').val()){
				jQuery('#pre_page').val(parseInt(jQuery('#pre_page').val())+1);
				jsRequestCommentRead();
			}
		} else{
			jQuery('#pre_page').val(mv_page);
			jsRequestCommentRead();
		}
		
	};
	
	jQuery(document).ready(function(){
		js_detail();
		
		<%if(comment_yn.equals("Y")) {%>
		jQuery("#comment_area").on("keypress", function(e){
			if(e.keyCode == 13){
				jsRequestCommentSave();
				e.preventDefault();
			}			
		});
		
		jQuery("#btncommentSave").on("click", function(e){
			jsRequestCommentSave();
			e.preventDefault();
		});
		jsRequestCommentRead();
		<%}%>
	});
</script>

</head>
<body>
<div id="content">
<!-- start :: content -->
<!-- start :: content -->
<form name="frm" id="frm">
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>
<input type="hidden" name="brd_mng_no" id="brd_mng_no" value='<%=input.getText("brd_mng_no")%>'/>
<input type="hidden" name="brd_no" id="brd_no" value='<%=input.getText("brd_no")%>'/>
<input type="hidden" name='board_comment_page_cnt' id='board_comment_page_cnt' value='5'/>
<input type="hidden" name='pre_page_comment_cnt' id='pre_page_comment_cnt' value='1'/>
<input type="hidden" name='pre_page' id='pre_page' value='1'/>
<input type="hidden" name='last_page' id='last_page' value=''/>
<input type="hidden" name="user_id" id="user_id" value='<%=input.getText("user_id")%>'/>

</form>
<%		
	if("9".equals(input.getText("brd_mng_no")) || "22".equals(input.getText("brd_mng_no")) ||
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
	
	<div class="view" id='viewForm' >
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="165"/><col width="300"/>
				<col width="165"/><col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">이 름</th>
					<td id='reg_emp_nm'></td>
					<th scope="row">이메일</th>
					<td id='email'></td>
				</tr>
				<tr>
					<th scope="row">작성일</th>
					<td id='reg_dt'></td>
					<th scope="row">조회수</th>
					<td id='read_cnt'></td>
				</tr>
				<%if(fileupload_yn.equals("Y")) {%>
				<tr>
					<th scope="row">파일첨부</th>
					<td colspan="3">
						<div class="margin_B_10">				
							<div id="gridFileEntriesDivMain" style="display:; max-height:250px; overflow-y:auto;">	
								<div class="span6" id="gridFileEntriesDiv" style="width:100%"></div>
							</div>
						</div>
					
					</td>
				</tr>
				<%} %>
				<tr>
					<th scope="row">제목</th>
					<td colspan="3" id='ttl' ></td>
				</tr>
				<tr>
					<td colspan="4" class="cont" id='cntnt'>
						
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<%if(!"modal".equals(viewMode)){ %>
	<div class="paging" id='listBtn' >
		<div class="rbtn">
			<a href="javascript:history.go(-1);" class="pbtn02 mid"><span class="list">뒤로가기</span></a>
		</div>
	</div>
	<%} %>	
	<br>
	<%if(comment_yn.equals("Y")) {%>
	<div id="comment_box" class="widget-body_A comment_box">
		<div class="widget-main">
			<div class="form-actions">
				<div class="input-group">
					<input  id="comment_area" name="comment_area"  placeholder="로그인 후 의견을 입력하세요." class="form-control"></input>
             
					<span class="input-group-btn">
						<span class="btn btn-sm btn-info no-radius" type="button" id="btncommentSave">
							<i class="icon- fa fa-share"> 댓글</i> 
						</span>
					</span>
				</div>
			</div>
				
			<div>
				<div id="board_comments"></div>
				<div id="board_comments_page" style="    text-align: center;"></div>
			</div>
			
		</div>
	</div>	
	<%}%>
	
	<div id='aaaa' style='display:none;'></div>

<!-- end :: content -->
<!-- end :: content -->
</div>
</body>
</html>