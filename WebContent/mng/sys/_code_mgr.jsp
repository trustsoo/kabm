<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript">

	function js_getCodeDivList()
	{
		jQuery.ajax({
			url : '/mng/sys/action/codeMgr.jspx?cmd=getCodeDivList', 
			data : '',
			type: 'POST',
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				if(jsonObj.result.code == '200'){
					var listHtml = [];
					var json = jsonObj.result.data.cd_list;
					var jsonCnt = jsonObj.result.data.cd_list.length;
					
					if(jsonCnt > 0){
						for(var i=0; i<jsonCnt; i++){
														
							var mod = i%2;
							if( mod == 0 )
							{
								listHtml.push('<tr class="odd pointer" style="cursor:pointer;" onclick="js_getCodeList(\''+json[i].cd+'\',\''+json[i].cd_nm+'\');">');
							}else{
								listHtml.push('<tr class="even pointer" style="cursor:pointer;" onclick="js_getCodeList(\''+json[i].cd+'\',\''+json[i].cd_nm+'\');">');
							}
							
							listHtml.push('		  <td><strong class="green">'+json[i].cd+'</strong></td>');
							listHtml.push('		  <td>'+json[i].cd_nm+'</td>');
							listHtml.push('	  <td>'+json[i].reg_dt+'</td>');
							listHtml.push('	</tr>');
							
						}
					}else{
						listHtml.push('		<tr>');
						listHtml.push('		  <td colspan="3" style="text-align:center;">조회된 결과가 없습니다.</td>');
						listHtml.push('		</tr>');
						
					}
					jQuery('#list_div').html(listHtml.join(''));
				}
			}
		});
		
	}
	
	
	function js_getCodeList(div_cd, div_nm)
	{
		jQuery('#div_cd').val(div_cd);
		jQuery('#div_nm').val(div_nm);
		jQuery.ajax({
			url : '/mng/sys/action/codeMgr.jspx?cmd=getCodeJsonList', 
			data : 'div_cd='+div_cd,
			type: 'POST',
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				if(jsonObj.result.code == '200'){
					var listHtml = [];
					var json = jsonObj.result.data.cd_list;
					var jsonCnt = jsonObj.result.data.cd_list.length;
					
					if(jsonCnt > 0){
						for(var i=0; i<jsonCnt; i++){
														
							var mod = i%2;
							if( mod == 0 )
							{
								listHtml.push('<tr class="odd pointer" style="cursor:pointer;" onclick="js_getCodeDetail(\''+json[i].cd+'\');">');
							}else{
								listHtml.push('<tr class="even pointer" style="cursor:pointer;" onclick="js_getCodeDetail(\''+json[i].cd+'\');">');
							}
							listHtml.push('		  <td>'+json[i].sort_no+'</td>');
							listHtml.push('		  <td><strong class="green">'+json[i].cd+'</strong></td>');
							listHtml.push('		  <td>'+json[i].cd_nm+'</td>');
							listHtml.push('	  <td>'+json[i].reg_dt+'</td>');
							listHtml.push('	</tr>');
							
						}
					}else{
						listHtml.push('		<tr>');
						listHtml.push('		  <td colspan="4" style="text-align:center;">조회된 결과가 없습니다.</td>');
						listHtml.push('		</tr>');
						
					}
					jQuery('#code_list_div').html(listHtml.join(''));
				}
			}
		});
		
	}
	
	function js_requestSave()
	{
		if(jQuery('#div_cd').val() == '')
		{	
			msgStart(msg_mng_code_002, 'warning', null);
    		return;
		}
		
		if(!checkFormField('#formCodeInput')) return;
		
		var _url = '/mng/sys/action/codeMgr.jspx?cmd=createCode';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : jQuery("#formCodeInput").serialize(),
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				var msg =  jsonObj.result.msg;
				if(jsonObj.result.code == '200')
		   		{
		   			msgStart(msg_mng_code_009, 'success');
		   			jQuery('#cd').attr('readOnly', 'true');
		   			js_getCodeList(jQuery('#div_cd').val(), jQuery('#div_nm').val());
		   		} else
				{
		   			msgStart(msg_mng_code_010+"("+msg+")", 'danger');
				}
	   		}
		});
	}
	
	function js_requestDelete()
	{
		if(!confirm(msg_mng_code_007)) return;
		
		if(!checkFormField('#formCodeInput')) return;
		
		var _url = '/mng/sys/action/codeMgr.jspx?cmd=deleteCode';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : jQuery("#formCodeInput").serialize(),
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				var msg =  jsonObj.result.msg;
				if(jsonObj.result.code == '200')
		   		{
		   			msgStart(msg_mng_code_009, 'success');
		   			js_codeInputFormReset();
		   			js_getCodeList(jQuery('#div_cd').val(), jQuery('#div_nm').val());
		   		} else
				{
		   			msgStart(msg_mng_code_010+"("+msg+")", 'danger');
				}
	   		}
		});
	}
	
	function js_getCodeDetail(cd)
	{	
		var _url = '/common/action/code.jspx?cmd=getCodeDetail';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "GET",
			data : 'cd='+cd,
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
				
		   		if(code == '200')
		   		{	
		   			jQuery(xmlDoc).find('cd_detail').each(function(){
	   				
	 					jQuery('#div_nm_prnt_cd_nm').val(jQuery(this).find('div_nm').text()+' > '+jQuery(this).find('hierarchy_path').text().replace('|', '>'));
	 					jQuery('#cd').val(jQuery(this).find('cd').text());
	 					jQuery('#cd_nm').val(jQuery(this).find('cd_nm').text()); 					
	 					jQuery('#sort_no').val(jQuery(this).find('sort_no').text());
	 					jQuery('#use_yn').val(jQuery(this).find('use_yn').text());
						jQuery('#prnt_cd').val(jQuery(this).find('prnt_cd').text());
						jQuery('#div_cd').val(jQuery(this).find('div_cd').text());
						
						jQuery('#write_dt').html(jQuery(this).find('reg_dt').text()+"/"+jQuery(this).find('upd_dt').text());
						jQuery('#empl_nm').html(jQuery(this).find('reg_emp_nm').text()+"/"+jQuery(this).find('upd_emp_nm').text()); 					
						jQuery('#cd').attr('readOnly', 'true');
						
						jQuery('#mode').val('U');
							
			   		});		   			
		   		}else
				{
		   			msgStart(msg_mng_code_010+"("+msg+")", 'danger');
				}
	   		}
		});	
	}
	
	
	function js_codeInputFormReset()
	{
		document.formCodeInput.reset();
		jQuery('#div_nm_prnt_cd_nm').val('');
	}
	
	jQuery(document).ready(function(){
		js_getCodeDivList();
		
		
		jQuery('#btnCodeSave').bind('click', function(){		
			js_requestSave();
			
		});
		jQuery('#btnCodeDel').bind('click', function(){		
			js_requestDelete();
			
		});
		
		jQuery('#btnNewCode').bind('click', function(){
						
			var prnt_cd = '0';
			var prnt_nm = jQuery('#div_nm').val();
			
			js_codeInputFormReset();
			
			jQuery('#div_nm_prnt_cd_nm').val(prnt_nm);
			jQuery('#prnt_cd').val(prnt_cd);
			
			jQuery('#mode').val('C');			
			jQuery('#cd').removeAttr('readOnly');
			
			
		});
	});


</script>
</head>
<body>
<div class="col-md-6 col-xs-12">
    <div class="x_panel">
      <div class="x_title">
        <h2>코드분류 목록</h2>
        <ul class="nav navbar-right panel_toolbox">
          <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
          </li>
          <li><a class="close-link"><i class="fa fa-close"></i></a>
          </li>
        </ul>
        <div class="clearfix"></div>
      </div>
      <div class="x_content">
      	<div class="table-responsive">
          <table class="table table-striped jambo_table bulk_action">
            <thead>
              <tr class="headings">                
                <th class="column-title">분류코드</th>
                <th class="column-title">분류명</th>
                <th class="column-title no-link last"><span class="nobr">등록일자</span></th>
              </tr>
            </thead>
            <tbody id="list_div">
              
            </tbody>
          </table>
        </div>
	  </div>			
	</div>
</div>	
<div class="col-md-6 col-xs-12">
    <div class="x_panel">
      <div class="x_title">
        <h2>코드 목록</h2>
        <ul class="nav navbar-right panel_toolbox">
          <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
          </li>
          <li><a class="close-link"><i class="fa fa-close"></i></a>
          </li>
        </ul>
        <div class="clearfix"></div>
      </div>
      <div class="x_content">
      	<div class="table-responsive">
          <table class="table table-striped jambo_table bulk_action">
            <thead>
              <tr class="headings">                
                <th class="column-title">순서</th>
                <th class="column-title">코드</th>
                <th class="column-title">코드명</th>
                <th class="column-title no-link last"><span class="nobr">등록일자</span></th>
              </tr>
            </thead>
            <tbody id="code_list_div">
              
            </tbody>
          </table>
        </div>
        <div class="pull-right">
			<button id="btnNewCode" class="btn btn-sm btn-primary">
				<i class="icon- fa fa-save">코드 신규 등록</i>					 
			</button>	
		</div>		
	  </div>
	</div>
	
	
	<div class="x_panel">
      <div class="x_title">
        <h2>코드상세</h2>
        <ul class="nav navbar-right panel_toolbox">
          <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
          </li>
          <li><a class="close-link"><i class="fa fa-close"></i></a>
          </li>
        </ul>
        <div class="clearfix"></div>
      </div>
      <div class="x_content">
      	<form class="form-horizontal" role="form" id='formCodeInput' name='formCodeInput'>
		<input type='hidden' name='prnt_cd' id='prnt_cd' value=''>
		<input type='hidden' name='mode' id='mode' value=''>
		<input type='hidden' name='div_cd' id='div_cd' value=''>
		<input type='hidden' name='div_nm' id='div_nm' value=''>
		<table class="com_table_box">
			<tr>
				<th>코드분류/코드경로</th>
				<td><input type="text" id="div_nm_prnt_cd_nm"  class="form-control" readonly></td>
			</tr>
			<tr>
				<th>코드 ID<span style="color:red;">(*)</span></th>
				<td><input type="text" id="cd" name="cd"  placeholder="코드 ID" maxlength="20" class="form-control" required format="idFrmt"></td>
			</tr>
			<tr>
				<th>코드명<span style="color:red;">(*)</span></th>
				<td><input type="text" id="cd_nm" name="cd_nm"  placeholder="코드명" maxlength="50" class="form-control" required></td>
			</tr>
			<tr>
				<th>정렬순서<span style="color:red;">(*)</span></th>
				<td><input type="text" id="sort_no" name="sort_no"  placeholder="정렬순서" maxlength="5" class="form-control" required formate='numberFrmt'></td>
			</tr>
			<tr>
				<th>사용 여부</th>
				<td><select name='use_yn' id='use_yn' placeholder="사용 여부" class="form-control" alt='사용 여부'>
					<option value='Y'>예</option>
					<option value='N'>아니오</option>									
				</select></td>
			</tr>
			<tr>
				<th>입력자/수정자</th>
				<td><input type="text" id="empl_nm"  class="form-control" readonly></td>
			</tr>
			<tr>
				<th>입력일시/수정일시</th>
				<td><input type="text" id="write_dt"  class="form-control" readonly></td>
			</tr>
	    </table>
		</form>
      	
	  </div>
		<div class="pull-right">
			<span id="btnCodeSave" class="btn btn-sm btn-primary">
				<i class="icon- fa fa-save"> 저장</i>					 
			</span>								
			<span class="btn btn-sm btn-default" onClick="javascript:document.formCodeInput.reset();">
				<i class="icon- fa fa-undo"> 취소</i> 
			</span>
			<span id="btnCodeDel" class="btn btn-sm btn-dark">
				<i class="icon- fa fa-save"> 삭제</i>					 
			</span>	
		</div>						
	</div>
</div>	

</body>
</html>									