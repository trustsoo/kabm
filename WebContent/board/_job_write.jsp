<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<%
	String brd_no = input.getText("brd_no");
	String readOnly = "";
	
	String session_user_id = getUserObject(request, response).getId();
	
	if("".equals(brd_no)) readOnly = "";
	else
	{
		if(output.getText("user_id").equals(session_user_id))
		{
			readOnly = "";
		} else
		{
			readOnly = "readOnly";
		}
		
	}
	
%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/static/lib/ckeditor/ckeditor.js" charset="UTF-8"></script>
<script type="text/javascript" src="/static/lib/swfupload/js/swfupload.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/lib/swfupload/js/extend/swfupload.swfobject.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/lib/swfupload/js/extend/swfupload.queue.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/lib/swfupload/js/extend/fileprogress.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/lib/swfupload/js/extend/handlers.js" charset="utf-8"></script>
<script type="text/javascript" type="text/javascript">
//<![CDATA[	
	
	jQuery(document).ready(function(){		
		//makeCodeSelectBox('qna_category', '', 'question_div', false, true);
		
		initUploader('<%=getMaxAttachSizeValue()%>', 3, '*.*', {"isPublic":"N"}, {"filename":"file_nms", "path":"file_paths", "filesize":"file_sizes", "extname":"ext_nms", "user_filename":"user_file_nms"}, 'js_file_save_after_upload');
<%
		if(!"".equals(brd_no))
		{
%>		
			jQuery('#career_yn').val('<%=output.getText("career_yn")%>');
		
			js_getFileEntries('gridFileEntriesDiv',false, jQuery('#attach_div_cd').val(), <%=brd_no%>);
<%
		}
%>
		
<%
		if("2".equals(input.getText("recruit_div")))
		{
%>	
		jQuery('#gender').val('<%=input.getText("gender")%>');
<%	
		}
%>	
		
	});
	
	function js_before_save()
	{
		
		
		if(__numFilesQueued > 0)
		{
			__Uploader.startUpload();
		}
		else
		{
			js_file_save_after_upload();					
		}
		
	}
	
	//2
	function js_file_save_after_upload()
	{	
		js_save();
		
		if(__Uploader != null)
	    {
	    	
	    	var stats = __Uploader.getStats();
	    	stats.files_queued = 0;
	    	stats.in_progress = 0;
	    	stats.queue_errors= 0;
	    	stats.successful_uploads = 0;
	    	stats.upload_cancelled = 0; 
	    	stats.upload_errors=0;
	    	
	    	__Uploader.setStats(stats);
	    }
	}
	
	
	
	function js_save(){
		
		if(!checkFormField("form")) return;
		
		jQuery("#cntnt").val(CKEDITOR.instances._cntnt.getData());
		
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=createJobWrite',
			data : jQuery("#form").serialize(true),
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
		   			msgStart(msg_mng_code_009, "success", null, 300, 110);
		   			location.href = '/board/board.jspx?cmd=list_job&brd_mng_no=<%=input.getText("brd_mng_no") %>&recruit_div=<%=input.getText("recruit_div") %>&cur_pg=<%=input.getText("cur_pg") %>';
				}
			}
		});			
	}
	
	function js_delete()
	{	
		if(!confirm(msg_com_code_107)) return;
		
		var http = jQuery.ajax({
			url : '/board/action/board.jspx?cmd=delJob',
			data : jQuery("#form").serialize(true),
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
		   			msgStart(msg_mng_code_009, "success", null, 300, 110);
		   			location.href = '/board/board.jspx?cmd=list_job&brd_mng_no=<%=input.getText("brd_mng_no") %>&recruit_div=<%=input.getText("recruit_div") %>&cur_pg=<%=input.getText("cur_pg") %>';
				}
			}
		});	
	}
	
</script>
</head>
<body>
<div id="content">
<!-- start :: content -->
<!-- start :: content 1:구인, 2:구직-->


<form name='form' id='form'>
<input type='hidden' id='recruit_div' name='recruit_div' value='<%=input.getText("recruit_div")%>'>
<input type="hidden" name='file_nms' id='file_nms' value=''/>
<input type="hidden" name='file_paths' id='file_paths' value=''/>
<input type="hidden" name='file_sizes' id='file_sizes' value=''/>
<input type="hidden" name='ext_nms' id='ext_nms' value=''/>
<input type="hidden" name='user_file_nms' id='user_file_nms' value=''/>
<input type="hidden" name="attach_div_cd" id="attach_div_cd" value='attach_div_03'/>      
<input type="hidden" name="brd_mng_no" id="brd_mng_no" value='<%=input.getText("brd_mng_no")%>'/>
<input type="hidden" name="brd_no" id="brd_no" value='<%=output.getText("brd_no")%>'/>
<input type="hidden" name="cntnt" id="cntnt" value=''/> 

<div class="view" id='viewForm'>
		<table cellpadding="0" cellspacing="0" class="" summary="" >
			<caption></caption>
			<colgroup>
				<col width="165"/><col width=""/>
			</colgroup>
			<tbody>
<%
	if("1".equals(input.getText("recruit_div")))
	{
%>			
				<tr>
					<th scope="row">경력/신입</th>
					<td>
						<select name="career_yn" id="career_yn" required='true' <%=readOnly%>>	
							<option value=''>선택</option>	
							<option value='A'>무관</option>					
							<option value='N'>신입</option>
							<option value='Y'>경력</option>
							
						</select>
					</td>
				</tr>
				<tr>
					<th scope="row">구인직종</th>
					<td>
						<input type="text" class="it " title="" value="<%=output.getText("job_title") %>" name="job_title"  id='job_title' required='true' <%=readOnly%>/>
					</td>
				</tr>
				
				<tr>
					<th scope="row">이메일</th>
					<td>
						<input type="text" class="it " title="" value="<%=input.getText("email") %>" name="email"  id='email"' readOnly/>
					</td>
				</tr>
				<tr>
					<th scope="row">연락처  </th>
					<td>
						<input type="text" class="it " title="" value="<%=input.getText("mobile_no") %>" name="phone"  id='phone' readOnly/>
					</td>
				</tr>
				<tr>
					<th scope="row">구인자명  </th>
					<td>
						<input type="text" class="it " title="" value="<%=input.getText("user_nm") %>" name="user_nm"  id='user_nm' readOnly/>
					</td>
				</tr>
<%
	}  else if("2".equals(input.getText("recruit_div")))
	{
%>
				<tr>
					<th scope="row">경력/신입</th>
					<td>
						<select name="career_yn" id="career_yn" required='true' <%=readOnly%>>	
							<option value=''>선택</option>					
							<option value='N'>신입</option>
							<option value='Y'>경력</option>
							
						</select>
					</td>
				</tr>
				<tr>
					<th scope="row">성별</th>
					<td>
						<select name="gender" id="gender" readOnly>	
							<option value=''>선택</option>				
							<option value='M'>남자</option>
							<option value='F'>여자</option>
							
						</select>
					</td>
				</tr>
				<tr>
					<th scope="row">나이 </th>
					<td>
						<input type="text" class="it " title="" value="<%=output.getText("age") %>" name="age"  id='age'  required='true' <%=readOnly%>/>
					</td>
				</tr>
				<tr>
					<th scope="row">구직직종</th>
					<td>
						<input type="text" class="it " title="" value="<%=output.getText("job_title") %>" name="job_title"  id='job_title' required='true' <%=readOnly%>/>
					</td>
				</tr>
				
				<tr>
					<th scope="row">이메일</th>
					<td>
						<input type="text" class="it " title="" value="<%=input.getText("email") %>" name="email"  id='email"' readOnly/>
					</td>
				</tr>
				<tr>
					<th scope="row">연락처  </th>
					<td>
						<input type="text" class="it " title="" value="<%=input.getText("mobile_no") %>" name="phone"  id='phone' readOnly/>
					</td>
				</tr>
				<tr>
					<th scope="row">구직자명  </th>
					<td>
						<input type="text" class="it " title="" value="<%=input.getText("user_nm") %>" name="user_nm"  id='user_nm' readOnly/>
					</td>
				</tr>
<%		
	}
%>	
	
<%
	if(!"".equals(brd_no))
	{
%>				
				<tr>
					<th scope="row">파일첨부</th>
					<td>
						<div class="margin_B_10">				
							<div id="gridFileEntriesDivMain" style="display:; max-height:250px; overflow-y:auto;">	
								<div class="span6" id="gridFileEntriesDiv" style="width:100%"></div>
							</div>
						</div>
					
					</td>
				</tr>
<%
	}
%>	
<%
	if("".equals(readOnly))
	{
%>				
				    
			    <tr id='fileUploadForm'>
			        <th>첨부파일</th>
			        <td>
			        	<div class="full_width" id="file_view">		
							<div class="widget-body_A margin_3_0">
								<div class="margin_15">
									<fieldset>
										<div id='gridFileEntriesDivMain' class="row-fluid" style="display:none;   overflow-y: auto;">	
											<div class="span6" id="gridFileEntriesDiv" style="height:110px;"></div>
										</div>
										<div id="no_files" style="display:none;">
											<label class="font_size20 W130_R">첨부파일이 없습니다.</label>
										</div>
										<div id="fileView" style="margin:5px;">				
											<div>	
												<div class="fieldset flash" id="swfUploadProgress" style="display:none; margin:0px; height:140px; width:810px;">
													<span class="legend">Upload Queue</span>
												</div>
												<span id="divStatus"> 0 Files Uploaded</span>
												<div class="P-L90 pull-right">
													<span id="swfUploadBtn"></span>
													<input id="swfUploadBtnCancel" type="button" value="Cancel All Uploads" disabled="disabled" style="margin-left: 2px; height: 22px; font-size: 8pt;" />
												</div>
												
											</div>
												
											<noscript>
												<div style="background-color: #FFFF66; border-top: solid 4px #FF9966; border-bottom: solid 4px #FF9966; margin: 10px 25px; padding: 10px 15px;">
													We're sorry.  SWFUpload could not load.  You must have JavaScript enabled to enjoy SWFUpload.
												</div>
											</noscript>
											<div id="divLoadingContent" class="content" style="background-color: #FFFF66; border-top: solid 4px #FF9966; border-bottom: solid 4px #FF9966; margin: 10px 25px; padding: 10px 15px; display: none;">
												SWFUpload is loading. Please wait a moment...
											</div>
											<div id="divLongLoading" class="content" style="background-color: #FFFF66; border-top: solid 4px #FF9966; border-bottom: solid 4px #FF9966; margin: 10px 25px; padding: 10px 15px; display: none;">
												SWFUpload is taking a long time to load or the load has failed.  Please make sure that the Flash Plugin is enabled and that a working version of the Adobe Flash Player is installed.
											</div>						
										</div>
									</fieldset>
									
								</div>
							</div>
						</div>
			        </td>
			    </tr>
<%
	}
%>				<tr>
					<th scope="row">제목</th>
					<td>
						<input type="text" class="it full" style="" title="제목" value="<%=output.getText("ttl") %>" name="ttl"  id='ttl' required='true'/>
					</td>
				</tr>	
				<tr>
					<th scope="row">내용</th>
					<td colspan="2">
<%
	if("".equals(readOnly))
	{
%>						
						<textarea class="full" name="_cntnt" id='_cntnt' class="txt" cols="" rows="" title="내용" required='true' <%=readOnly%>><%=output.getText("cntnt") %></textarea>
						<script type="text/javascript" isELIgnored="false">
						    //<![CDATA[
						    CKEDITOR.replace('_cntnt', {            	
						    	filebrowserImageUploadUrl : '/bin/FileUploader',
								height:200
						   	});	
								
						    //]]>
						</script>
<%
	}else{
%>			
						<%=new jdf.framework.core.data.schema.format.UnEscapeHtmlFormatter("4.0").format(output.getText("cntnt")) %>
<%	} %>			
					</td>
				</tr>
				
			</tbody>
		</table>
	</div>
	
</form>	

				
		<div class="btnWrap">
<%
	if("".equals(brd_no))
	{
%>			
			<a href="javascript:js_before_save();" class="pbtn02" ><span>확인</span></a>
<%
	} else
	{
		if(output.getText("user_id").equals(session_user_id))
		{
%>
			<a href="javascript:js_before_save();" class="pbtn02" ><span>수정</span></a>
			<a href="javascript:js_delete();" class="pbtn01"><span>삭제</span></a>
<%		
		}
	}
%>
		<a href="/board/board.jspx?cmd=list_job&brd_mng_no=<%=input.getText("brd_mng_no") %>&recruit_div=<%=input.getText("recruit_div") %>&cur_pg=<%=input.getText("cur_pg") %>" class="pbtn01"><span>목록</span></a>	
		</div>

	</div>
<!-- end :: content -->
<!-- end :: content -->
</div>
<div id='aaaa' style="display:none;"></div>
</body>
</html>