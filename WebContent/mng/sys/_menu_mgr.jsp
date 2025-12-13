<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>	
	<script type="text/javascript" src="/static/lib/jstree/jquery.jstree.js"></script>
	<style type="text/css">
		.form-group{margin:0px;}
	</style>
	<script type="text/javascript">

	function jsInitCmpnyTree()
	{		
		console.log('jsInitCmpnyTree');
		jQuery("#menu_tree").bind("before.jstree", function (e, data) {			
		}).jstree({
			"plugins":["themes","json_data","ui","crrm","dnd","types"],		
			"themes" : {"theme" : "classic"},
			 "json_data" : { 
				 "ajax" : {
					 "url" : "/mng/sys/action/menuMgr.jspx",
					 "data" : function(n)
					 {
						 return {
							 "cmd" : "getMenuListTreeFormat",
							 "menu_id" : n.attr ? n.attr("id") : 'top'
						 };
					 }
				 }
			 },
			 "types" : {
					// I set both options to -2, as I do not need depth and children count checking
					// Those two checks may slow jstree a lot, so use only when needed
					"max_depth" : -2,
					"max_children" : -2,
					// I want only `drive` nodes to be root nodes 
					// This will prevent moving or creating any other type as a root node
					"valid_children" : [ "drive" ],
					"types" : {
						// The default type
						"default" : {
							// I want this type to have no children (so only leaf nodes)
							// In my case - those are files
							"valid_children" : [ "default", "folder" ],
							"start_drag" : true,
							"move_node" : true,
							"delete_node" : false,
							"remove" : false,
							"icon" : {
		                        "image" : "/static/com/img/file.png"
		                    }
						},
						// The `folder` type
						"folder" : {
							// can have files and other folders inside of it, but NOT `drive` nodes
							"valid_children" : [ "default", "folder" ],
							"start_drag" : true,
							"move_node" : true,
							"delete_node" : false,
							"remove" : false,
							"icon" : {
		                        "image" : "/static/com/img/folder.png"
		                    }
						},							
						// The `drive` nodes 
						"drive" : {
							// can have files and folders inside, but NOT other `drive` nodes
							"valid_children" : [ "default", "folder" ],
							/*"icon" : {
								"image" : "/static/com/img/root.png"
							},*/
							// those prevent the functions with the same name to be used on `drive` nodes
							// internally the `before` event is used
							"start_drag" : true,
							"move_node" : true,
							"delete_node" : false,
							"remove" : false,
							"icon" : {
		                        "image" : "/static/com/img/root.png"
		                    }							
						}
					}
				},
				"ui" : {
					// this makes the node with ID node_4 selected onload
					"initially_select" : ["start" ]
				},
				// the core plugin - not many options here
				"core" : { 
					// just open those two nodes up
					// as this is an AJAX enabled tree, both will be downloaded from the server
					"initially_open" : [ "start" ] 
				}
		}).bind("select_node.jstree", function (event, data) {            
			var parent = null;
			var p_menu_nm = null;
			var p_menu_id = null;
			var p_menu_info = null;
			try
			{			
				parent = data.inst._get_parent(data.rslt.obj);
				
				//console.log(parent);
				
				p_menu_nm = parent.attr('menu_nm');
				p_menu_id = parent.attr('id');				
				p_menu_info = p_menu_nm+"("+p_menu_id+")";
				
			} catch(e)
			{
				p_menu_nm = "None";
				p_menu_id = "top";
				p_menu_info = "None";
			}            
			js_getMenuDetail(data.rslt.obj.attr("id"), p_menu_info);	
			jQuery('#div_menu_detail').css('display', '');
        	
        }).bind("dblclick.jstree", function (event, data) {        	
        	//js_requestPurge();	
        	
        }).bind("move_node.jstree", function(event, data){        	
        	
        	var eventID = data.rslt.o.attr("id");
            var groupID = data.rslt.np.attr("id");
            var position = data.rslt.cp;
            //console.log(position);            
            js_requestMenuMove(eventID, groupID, position);
        });
	}
	
	
	function js_requestMenuMove(menu_id, p_menu_id, position)
	{
		if(confirm(msg_mng_code_007))
		{
			var _url = '/mng/sys/action/menuMgr.jspx?cmd=moveMenu';
			var http = jQuery.ajax( {
		   		url: _url,	   		
		   		type: "POST",
				data : '&menu_id='+menu_id+'&p_menu_id='+p_menu_id+'&position='+position,
		   		async : false,
		   		error : function(xhr)
		   		{
					alert(xhr.status);
				},
				success:function(xmlDoc) 
		   		{
					var code = jQuery(xmlDoc).find('code').text(); 		
			   		var data = jQuery(xmlDoc).find('data').text();
			   		var msg =  jQuery(xmlDoc).find('msg').text();
			   		//console.log(code);
			   		if(code == 200)
			   		{
			   			msgStart(msg_mng_code_009);		   			
			   			
			   			jQuery("#menu_tree").jstree('refresh',-1);
			   			jQuery('#div_menu_detail').css('display', 'none');
	    						   			
			   		} else
					{
			   			msgStart(msg_mng_code_010+"("+msg+")", 'danger');
					}
		   		}
			});
		}
	}
	
	/*메뉴 상세 정보 조회*/
	function js_getMenuDetail(menu_id, p_menu_info)
	{
		var http = jQuery.ajax( {
	   		url: '/mng/sys/action/menuMgr.jspx?cmd=getMenuSimpleDetail&menu_id='+menu_id,	   		
	   		type: "POST",
	   		async : false,
	   		error : function(xhr)
	   		{
				alert(xhr.status);
			},
			success:function(xmlDoc) 
	   		{
				var code = jQuery(xmlDoc).find('code').text(); 		
		   		var data = jQuery(xmlDoc).find('data').text();
		   		var msg =  jQuery(xmlDoc).find('msg').text();

				var cd = null;
				var val = null;				
				//console.log(code);
		   		if(code == '200')
		   		{			   					
		   					   		
		   			jQuery(xmlDoc).find('menu_detail').each(function(){
			   			
		   				jQuery('#p_menu_info').val(p_menu_info);
		   				jQuery('#menu_id').val(jQuery(this).find('menu_id').text());
		   				jQuery('#p_menu_id').val(jQuery(this).find('p_menu_id').text());
		   				jQuery('#url').val(jQuery(this).find('url').text());
		   				jQuery('#menu_nm').val(jQuery(this).find('menu_nm').text());
		   				jQuery('#sort_no').val(jQuery(this).find('sort_no').text());
		   				jQuery('#depth').val(jQuery(this).find('level').text());
		   				jQuery('#params').val(jQuery(this).find('params').text());
		   				jQuery('#layout').val(jQuery(this).find('layout').text());
		   				jQuery('#auth_type').val(jQuery(this).find('auth_type').text());
		   				jQuery('#view_yn').val(jQuery(this).find('view_yn').text());
		   				
		   				jQuery('#menu_icon').val(jQuery(this).find('menu_icon').text());
		   				jQuery('#popup_yn').val(jQuery(this).find('popup_yn').text());
		   				
			   			jQuery('#write_dt').html(jQuery(this).find('reg_dt').text()+"/"+jQuery(this).find('upd_dt').text());
						jQuery('#empl_nm').html(jQuery(this).find('reg_user_nm').text()+"/"+jQuery(this).find('upd_user_nm').text());	
						
						jQuery('#menu_id').attr('readOnly', 'true');
						
						
			   		});		   			
		   		}else
				{
		   			msgStart(msg_mng_code_010+"("+msg+")", 'warning');
				}
	   		}
		});	
	}
	
	/*그룹 정보 저장*/
	function js_requestMenuInfoSave()
	{	
		if(!checkFormField('#menu_info_form')) return;
		
		var _url = '/mng/sys/action/menuMgr.jspx?cmd=updateMenu';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : jQuery("#menu_info_form").serialize(),
	   		async : false,
	   		error : function(xhr)
	   		{
				alert(xhr.status);
			},
			success:function(xmlDoc) 
	   		{
				var code = jQuery(xmlDoc).find('code').text(); 		
		   		var data = jQuery(xmlDoc).find('data').text();
		   		var msg =  jQuery(xmlDoc).find('msg').text();
		   		//console.log(code);
		   		if(code == 200)
		   		{
		   			console.log(msg_mng_code_009);
		   			msgStart(msg_mng_code_009);		   			
		   			
    				
		   			jQuery("#menu_tree").jstree('refresh',-1);
    						   			
		   		} else
				{
		   			msgStart(msg_mng_code_010+"("+msg+")", 'danger');
				}
	   		}
		});
	}
	
	function js_requestMenuInfoApply()
	{
		var _url = '/mng/sys/action/menuMgr.jspx?cmd=updateWebSiteMenu';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : '0=0',
	   		async : false,
	   		error : function(xhr)
	   		{
				alert(xhr.status);
			},
			success:function(xmlDoc) 
	   		{
				var code = jQuery(xmlDoc).find('code').text(); 		
		   		var data = jQuery(xmlDoc).find('data').text();
		   		var msg =  jQuery(xmlDoc).find('msg').text();
		   		//console.log(code);
		   		if(code == 200)
		   		{
		   			msgStart(msg_mng_code_009);
    				
		   			jQuery("#menu_tree").jstree('refresh',-1);
    						   			
		   		} else
				{
		   			msgStart(msg_mng_code_010+"("+msg+")", 'danger');
				}
	   		}
		});
	}
	
	function js_addMenuInputForm()
	{
		var selectedTree = jQuery('#menu_tree').jstree("get_selected");
		
		var menu_id = selectedTree.attr('id');
		var menu_nm = selectedTree.attr('menu_nm');
		var current_depth = selectedTree.attr('level');
		
		var depth = parseInt(current_depth)+1;
		
		js_menuinfoFormReset();
		jQuery('#menu_id').removeAttr('readOnly');
		jQuery('#p_menu_id').val(menu_id);
		jQuery('#depth').val(depth);
		
		
		jQuery('#p_menu_info').html(menu_nm+"("+menu_id+")");
	}
	
	/*입력폼 리셋*/
	function js_menuinfoFormReset()
	{
		document.menu_info_form.reset();
		jQuery('#write_dt').html('');
		jQuery('#empl_nm').html('');
		jQuery('#p_menu_info').html('');
	}
    
	jQuery(document).ready(function(){		
		jsInitCmpnyTree();
		
		jQuery('#btnMenuRefresh').bind('click', function(){jQuery("#menu_tree").jstree('refresh',-1);});
		
		jQuery('#btnMenuAdd').bind('click', function(){
			js_addMenuInputForm();
		});
		
		jQuery('#btnMenuInfoSave').bind('click', function(){
			js_requestMenuInfoSave();
		});
		
		jQuery('#btnMenuApply').bind('click', function(){
			js_requestMenuInfoApply();
		});
		
	});
	
	</script>
</head>
<body>
<div class="col-md-6 col-xs-12">
    <div class="x_panel">
      <div class="x_title">
        <h2>메뉴관리</h2>
        <ul class="nav navbar-right panel_toolbox">
          <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
          </li>
          <li><a class="close-link"><i class="fa fa-close"></i></a>
          </li>
        </ul>
        <div class="clearfix"></div>
      </div>
      <div class="x_content">
      	<div id="menu_tree"></div>
	  </div>
	   <div class="pull-right">
			<button id="btnMenuAdd" class="btn btn-sm btn-primary">
						<i class="icon- fa fa-plus"> 메뉴 추가</i>					 
					</button>
			<button id="btnMenuRefresh" class="btn btn-sm">
				<i class="icon- fa fa-undo"> 메뉴 새로고침</i>					 
			</button>
                  <button id="btnMenuApply" class="btn btn-sm btn-primary">
				<i class="icon- fa fa-save"> 메뉴 반영</i>					 
			</button>
		</div>								
	</div>
</div>	
<div class="col-md-6 col-xs-12">
    <div class="x_panel">
      <div class="x_title">
        <h2>메뉴관리</h2>
        <ul class="nav navbar-right panel_toolbox">
          <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
          </li>
          <li><a class="close-link"><i class="fa fa-close"></i></a>
          </li>
        </ul>
        <div class="clearfix"></div>
      </div>
      <div class="x_content">
      	<form name='menu_info_form' id='menu_info_form' class="form-horizontal" role="form">	
		<input type="hidden" name='depth' id='depth' value=''/>						
		<input type="hidden" name='p_menu_id' id='p_menu_id' value=''/>
		<table class="com_table_box">
			<tr>
				<th>상위메뉴정보</th>
				<td><input type="text" id="p_menu_info"  maxlength="20" class="form-control" readonly></td>
			</tr>
			<tr>
				<th>메뉴 ID<span style="color:red;">(*)</span></th>
				<td><input type="text" id="menu_id" name="menu_id"  placeholder="메뉴 ID" maxlength="20" class="form-control" required></td>
			</tr>
			<tr>
				<th>메뉴명<span style="color:red;">(*)</span></th>
				<td><input type="text" id="menu_nm" name="menu_nm"  placeholder="메뉴명" maxlength="50" class="form-control" required></td>
			</tr>
			<tr>
				<th>URL<span style="color:red;">(*)</span></th>
				<td><input type="text" id="url" name="url"  placeholder="URL" maxlength="300" class="form-control" required></td>
			</tr>
			<tr>
				<th>화면 Layout<span style="color:red;">(*)</span></th>
				<td><input type="text" id="layout" name="layout"  placeholder="화면 Layout" maxlength="20" class="form-control" required></td>
			</tr>
			<tr>
				<th>정렬 순서<span style="color:red;">(*)</span></th>
				<td><input type="text" id="sort_no" name="sort_no"  placeholder="정렬 순서" maxlength="20" class="form-control" required></td>
			</tr>
			<tr>
				<th>메뉴 확장 정보</th>
				<td><input type="text" id="params" name="params"  placeholder="another-url:aaaa.jspx,bbb.jspx layout:xxxxx" maxlength="500" class="form-control"></td>
			</tr>
			<tr>
				<th>팝업 여부</th>
				<td><select name='popup_yn' id='popup_yn' placeholder="팝업 여부" class="form-control" alt='팝업여부' >
					<option value='Y' selected>Y</option>
					<option value='N'>N</option>									
				</select></td>
			</tr>
			<tr>
				<th>인증 레벨</th>
				<td><select name='auth_type' id='auth_type' placeholder="인증 레벨" class="form-control" alt='인증 레벨' >
					<option value='N'>로그인/비로그인 모두 허용</option>
					<option value='U,M,P' selected>로그인 사용자 전체</option>
					<option value='U,M' selected>로그인 교육자만 허용</option>
					<option value='M'>로그인회원관리자만 허용</option>
					<option value='S,A'>일반관리자만 허용</option>
					<option value='S'>시스템관리자만 허용</option>
				</select></td>
			</tr>
			<tr>
				<th>메뉴 아이콘</th>
				<td><input type="text" id="menu_icon" name="menu_icon"  placeholder="메뉴 아이콘" maxlength="30" class="form-control"></td>
			</tr>
			<tr>
				<th>메뉴 노출 여부<span style="color:red;">(*)</span></th>
				<td><select name='view_yn' id='view_yn' placeholder="메뉴 노출 여부" class="form-control" alt='메뉴 노출 여부' required>
					<option value='Y'>예</option>
					<option value='N'>아니오</option>									
				</select></td>
			</tr>
	    </table>
		</form>
      	
	  </div>
		<div class="pull-right">
			<button id="btnMenuInfoSave" class="btn btn-sm btn-primary">
				<i class="icon- fa fa-save"> 저장</i>					 
			</button>								
			<button class="btn btn-sm" onClick="javascript:document.menu_info_form.reset();">
				<i class="icon- fa fa-undo"> 취소</i> 
			</button>
		</div>
										
	</div>
</div>	
			

	
	
  </div>
</div>	
</body>
</html>