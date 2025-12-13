<%@page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="today" type="java.lang.String" scope="request"/>
<jsp:useBean id="prevday" type="java.lang.String" scope="request"/>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">

	.ui-jqgrid tr.jqgrow td {
    text-overflow: ellipsis;-o-text-overflow: ellipsis;
}

</style>

<script type="text/javascript">
	jQuery(document).ready(function(){
		
		//날짜달력 조건생성.
		Create_datepicker('date-picker');
		//기본일자 세팅
		jQuery('#start_dd').val(dateStringFormat('<%= prevday%>'));
		jQuery('#end_dd').val(dateStringFormat('<%= today%>'));
		
		js_getLogList();
		
		jQuery('#btn_logSrch').bind('click', function(e){js_getLogList();});
	
	});
	
	  
	function js_getLogList(page_key)
	{	
		var tot_cnt = 0;
		var row_per_page = 10;
		
		if(page_key == null) page_key = 0;
		
		
		jQuery("#grid-table").jqGrid("GridUnload");
		jQuery("#widget_grid").html("<p><table id=\"grid-table\"></table><div id=\"grid-pager\"></div></p>");
		
		var _url = '/mng/sys/action/cmpnySysMgr.jspx?cmd=getLogList&page_key='+page_key+'&row_per_page='+row_per_page;
		
		//post page문제
		jQuery("#grid-table").jqGrid({
			url : _url,
			datatype : 'json',
			mtype: 'POST',
			postData : jQuery("#srch_form").serialize(),
			jsonReader : {  //****필수요소****
				page: "page", 
				total: "total",
				records: "records",
				repeatitems: false,
				root: "get_log_list", 	//blockname			 
				id: "data_key"
			},
			colNames:['NO', '서버구분', '로그구분', '등록일시', '내용', '상세보기','seq_no'],
			colModel:[
			          {name:'data_key',index:'data_key', width:25, sortable:false, editable:false, align:'center'},
			          {name:'server_alias',index:'server_alias', width:50, editable:false, align:'left', sortable:false},
			          {name:'log_mode',index:'log_mode', width:50, editable:false, align:'left', sortable:false},
			          {name:'write_dt',index:'write_dt', width:100, editable:false, align:'left', sortable:false},
			          {name:'logs',index:'logs', width:150, editable:true, align:'left', sortable:false},
			          {name:'btn',index:'btn', width:25, editable:true, align:'center', sortable:false, formatter:addBtn},
			          {name:'seq_no',index:'seq_no', width:25, editable:true, align:'center', sortable:false, hidden:true}
			],
			height: 400,
			rowNum:10,
			autowidth: true,
			pager : jQuery('#grid-pager'),
			pgtext: null, 
			viewrecords: false,
			deepempty: true,
			altRows: true,
			emptyrecords:"데이터가 없습니다.",
			loadError : function(xhr, str, err)
	        {
	        	processGridError(xhr, str, err);
	        	
	        }, beforeProcessing:function(data)
		    {		    	
		    	if(data.get_log_list.length <= 0 && page_key > 0)
		    	{
		    		alert('여기가 마지막입니다' );
		    		js_getLogList(data.prev_key);
		    	}		    	
		    }, ondblClickRow : function(rowid, iRow, iCol, e)
	        {
			
	        }, onSelectRow: function(rowid,status,e)
			{
	        	selectedRow = jQuery("#grid-table").jqGrid('getRowData',rowid);
	        	jQuery("#seq_no").val(selectedRow.seq_no);
				
			}, loadComplete : function(data)
			{
				
				jQuery("#grid-table").jqGrid('navGrid',"#grid-pager",{edit:false, add:false, del:true, search:false, refresh:false, position:'left'},
						{},
						{},
						{  			
							addCaption : '로그 삭제',
							width:BOARD_EDIT_ADD_WIDTH,
							url:'/mng/sys/action/cmpnySysMgr.jspx?cmd=delLog',
							datatype : 'xml',	
							mtype:'post',
				            savekey:[true,13], //Enter(keycode가 13) 입력시 저장
				            closeAfterDel:true, //폼전송후 입력폼을 닫는다
				            reloadAfterSubmit:true, //폼전송후 그리드를 갱신한다
				            recreateForm : true,
				            afterSubmit : function (response, formid) {

							//삭제질의 창 닫고, 같은 조건으로 다시 조회.
							return processSimpleResponseXML(response, 'grid');
								
				            },
				            onclickSubmit:function(params){
				            	return {seq_no:jQuery('#seq_no').val()};	
				            }
						}		
					);
				
				
				var table = this;
				updatePagerIcons(table);
				
				
				
				//updateNavGridButton('grid-table', 'grid-pager', table);
				
				jQuery('#grid-pager .ui-pg-selbox').hide();
				set_grid_paper("grid-pager", data.prev_key, data.next_key);
				
			}, onPaging : function(pgButton)
			{
				var paper_id = "grid-pager";
				var change = 'N';
				var page_key = 0;
				var nextPageRow = 10;
				
				if(pgButton.indexOf('prev') != -1)
				{
					page_key = jQuery('#'+paper_id+'_prev_key').val();
					if(page_key != "-1"){
						change = 'Y';
					}
				} else if(pgButton.indexOf('next') != -1)
				{
					page_key = jQuery('#'+paper_id+'_next_key').val();
					if(page_key != "-1"){
						change = 'Y';
					}
				} 					
							
				if(change == 'Y'){
					js_getLogList(page_key);
				}
			}
			
		});
		resizeJqGridWidth('grid-table', 'widget_grid', '100%');
	}
	
	
	
	 
	   
	   
    function addBtn (cellvalue, options, rowObject)
	{
    	rowid = rowObject.data_key;
    	
    	
    	
    	return "<button onclick=\"fn_log_popup("+rowid+");\" class=\"btn btn-xs btn-primary\" id=btn_"+rowid+ ">상세보기</button>"
	} 
    
    
     function fn_log_popup(rowid){
    	
    	var window_width = 1250;
		var window_height = 630;
		var seq_no = jQuery("#grid-table").jqGrid('getRowData',rowid).seq_no;
		
		var popupObj = window.open('/mng/cmpnySysMgr.jspx?cmd=getLogDetail&seq_no='+seq_no+'&rowid='+rowid,"getLogDetail_window", 'width='+window_width+', height='+window_height+',menubar=no,status=no,scrollbars=no,top='+(jQuery(window).height() / 2 - window_height / 2)+', left='+(jQuery(window).width() / 2 - window_width / 2));
		//goMenuLink('Y', 'log_popup', '/mng/cmpnySysMgr.jspx?cmd=getLogDetail&seq_no='+seq_no+'&rowid='+rowid, '490', '250');
		popupObj.focus();
		popupObj = null;
		
		return false;
    	
    	
    } 
    
    
    
    
    
    function fn_log_detail_popup(rowid){
    	alert(jQuery("#grid-table").jqGrid('getRowData',rowid).seq_no);
    	jQuery("#data_key2").val(rowid);
    	jQuery("#server_alias2").val(jQuery("#grid-table").jqGrid('getRowData',rowid).server_alias);
    	jQuery("#log_mode2").val(jQuery("#grid-table").jqGrid('getRowData',rowid).log_mode);
    	jQuery("#write_dt2").val(jQuery("#grid-table").jqGrid('getRowData',rowid).write_dt);
    	//jQuery("#log2").val(jQuery("#grid-table").jqGrid('getRowData',rowid).log);
    	
    	var x = jQuery(document).scrollLeft - 750;
		var y = jQuery(document).scrollTop() +200;
		js_viewCommonPopupModal(x, y);
    	
    }
    
    function js_viewCommonPopupModal(x, y)
	{
		jQuery('#common_popup_modal').css('left' , x );
		jQuery('#common_popup_modal').css('top' , y );
		jQuery('#common_popup_modal').css('display', '');
		jQuery('#common_popup_modal').draggable({ handle: "#common_popup_modal_hd" });
		
	}
    
    function js_closeCommonPopup()
	{
		
		jQuery('#common_popup_modal').css('display', 'none');
		
	}
	
	
</script>
</head>
<body>


<div class="row">
		<div class="span12">
		
			<div class="widget-box border_0">		 	
				<div class="widget-body_A">
					<div class="widget-main">
						<form name='srch_form' id='srch_form' onSubmit="javascript:return false;">											
							<table class="ubi_table_box" border="0" cellpadding="0" cellspacing="0" style="margin-top:0px">						
								<colgroup><col width="100px"><col width="100px"><col width="100px"><col width="100px"><col width="100px"><col width="150px"><col width="100px"><col width=""></colgroup>
								<tbody style="border-top:3px solid #4A90CC;">								
									<tr>
										<th>서버구분</th>
										<td>
											<select name="server_alias" id="server_alias" class="W100P">
												<option value="">전체</option>
												<option value="W1">W1</option>
											</select>
										</td>
										<th>로그구분</th>
										<td>
											<select name="log_mode" id="log_mode" class="W100P">
												<option value="">전체</option>
												<option value="S">sys</option>
												<option value="E">err</option>
												<option value="W">warn</option>
												<option value="I">info</option>
												<option value="D">debug</option>
												<option value="U">user</option>
												<option value="Q">sql</option>
												<option value="P">packet</option>
												
												<option value="batch">batch</option>
											</select>
										</td>
										<th><font>조회기간</font></th>
										<td>
											<span class="input-group ubicus_textbox_width">
												<input class="date-picker W100P" id="start_dd" name="start_dd" type="text" data-date-format="yyyy-mm-dd"/>
												<span class="input-group-addon">
													<i class="icon- fa fa-calendar"></i>
												</span>
											</span>
											<span id="start_hour_option"></span>
											~
											<span class="input-group ubicus_textbox_width">
												<input class="date-picker W100P" id="end_dd" name="end_dd" type="text" data-date-format="yyyy-mm-dd"/>
												<span class="input-group-addon">
													<i class="icon- fa fa-calendar"></i>
												</span>
											</span>
										</td>							
										<td colspan="2">
											<span class="pull-right">
												<button id="btn_logSrch" class="btn btn-xs btn-primary btn_right"><i class='icon- fa fa-search'></i>검색</button>
											</span>
										</td>
									</tr>	
								</tbody>								
							</table>						
						</form>
					</div>
				</div>
			</div>
			
			
			<div class="widget-box">
				<div class="widget-body_A">
					<div class="widget-main">
						
						<input id="grid-pager_prev_key" type="hidden"/>
						<input id="grid-pager_next_key" type="hidden"/>
						
						<div id="widget_grid" class="tab-pane in active" style="min-height:410px;">
						<table id="grid-table" style="font-size:11px; line-height:20px;"></table>
						</div>
					</div><!-- /widget-main -->
				</div><!-- /widget-body -->
			</div><!-- /widget-box -->
			
		</div>
</div>




<div id="common_popup_modal" style="display:none;position:absolute;bottom:100px;left:10%;width:800px;z-index: 1049" class="col-xs-12 col-sm-6 widget-container-span">
	<div class="widget-box">
		<div class="widget-header widget-header-flat header-color-blue" id="common_popup_modal_hd">
			<h5 class="lighter"><b id="pop_title">로그 상세보기</b></h5>
			
		</div>
		<div class="widget-body bb-none">
			<div class="widget-main">
				<form name="commonForm" id="commonForm">				
					<input id="seq_no" name="seq_no" type="hidden" value="-1"></input>
					<table class="ubi_table_box" border="0" cellpadding="0" cellspacing="0">
						<colgroup><col width="150px"><col width=""></colgroup>
						<tbody>							
							<tr>
								<th>No.</th>
								<td>
									<input class="W100P" name="data_key2" id="data_key2" type="text" readonly>
								</td>
							</tr>
							<tr>
								<th>서버구분</th>
								<td>
									<input class="W100P" name="server_alias2" id="server_alias2" type="text" readonly>
								</td>
							</tr>
							<tr>
								<th>로그구분</th>
								<td>
									<input class="W100P" name="log_mode2" id="log_mode2" type="text" readonly>
								</td>
							</tr>
							<tr>
								<th>등록일시</th>
								<td>
									<input class="W100P" name="write_dt2" id="write_dt2" type="text" readonly>
								</td>
							</tr>
							<tr>
								<th>내용</th>
								<td>
									<textarea class="W100P" style="height:200px; background-color:#FFFFDB; color:#933;" wrap="virtual" name="log2" id="log2" readonly="readonly"></textarea> 
									<!-- input class="W100P" style="height:100px;" wrap="virtual" name="log2" id="log2" type="textarea" readonly-->
								</td>
							</tr>
						</tbody>
					</table>
				</form>	
			</div>

			<div class="widget-toolbox padding-8 clearfix">
				
				<span class="btn btn-xs btn-danger pull-right" onClick="javascript:js_closeCommonPopup();">
					<i class="icon-remove"></i>
					<span class="bigger-110"> 취소</span>
				</span> &nbsp; &nbsp; &nbsp;
				
			</div>
		</div>
	</div>
</div>




</body>
</html>									