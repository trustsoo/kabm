<%@page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">

	.ui-jqgrid tr.jqgrow td {
    text-overflow: ellipsis;-o-text-overflow: ellipsis;
}

</style>

<script type="text/javascript">
	
	function goSearch(cur_pg)
	{
		js_getEmpList(cur_pg);
	}
	
	function js_getEmpList(cur_pg)
	{
		jQuery('#cur_pg').val(cur_pg);
		
		jQuery.ajax({
			url : '/mng/sys/action/empMgr.jspx?cmd=getEmpList', 
			data : jQuery("#form").serialize(),
			type: 'POST',
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				if(jsonObj.result.code == '200'){
					var listHtml = [];
					var json = jsonObj.result.data.emp_info;
					var jsonCnt = jsonObj.result.data.emp_info.length;
					jQuery('#tot_cnt').val(jsonObj.result.data.property[0].tot_cnt);
					
					if(jsonCnt > 0){
						for(var i=0; i<jsonCnt; i++){
														
							var mod = i%2;
							if( mod == 0 )
							{
								listHtml.push('<tr class="odd pointer" style="cursor:pointer;" onclick="js_getEmpinfo(\''+json[i].emp_cd+'\');">');
							}else{
								listHtml.push('<tr class="even pointer" style="cursor:pointer;" onclick="js_getEmpinfo(\''+json[i].emp_cd+'\');">');
							}
							
							listHtml.push('		  <td>'+json[i].emp_cd+'</td>');
							listHtml.push('		  <td>'+json[i].emp_nm+'</td>');
							listHtml.push('	  <td>'+json[i].tel_no+'</td>');
							listHtml.push('	  <td>'+json[i].mobile_no+'</td>');
							listHtml.push('	  <td>'+json[i].email+'</td>');
							listHtml.push('	  <td>'+json[i].use_yn+'</td>');
							listHtml.push('	  <td class=" last">'+json[i].auth+'</td>');
							listHtml.push('	</tr>');
							
						}
					}else{
						listHtml.push('		<tr>');
						listHtml.push('		  <td colspan="7" style="text-align:center;">조회된 결과가 없습니다.</td>');
						listHtml.push('		</tr>');
						
					}
					jQuery('#list_div').html(listHtml.join(''));
					
					js_Paging('pagingDiv', jQuery('#cur_pg').val(), jQuery('#tot_cnt').val(), jQuery('#row_per_page').val(), 'goSearch');
					js_Paging_text('page_text_info', jQuery('#cur_pg').val(), jQuery('#tot_cnt').val(), jQuery('#row_per_page').val());
				}
			}
		});
		
	}
	function js_getEmpinfo(emp_cd)
	{
		if(emp_cd == '' || emp_cd == 'undefined' ) return;
		
		jQuery.ajax({
			url : '/mng/sys/action/empMgr.jspx?cmd=getEmpInfo', 
			data : 'emp_cd='+emp_cd,
			type: 'POST',
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				if(jsonObj.result.code == '200'){
					var json = jsonObj.result.data.emp_info;
					var jsonCnt = jsonObj.result.data.emp_info.length;
					
					if(jsonCnt > 0){
						jQuery('#edit_title').html('관리자정보 수정');
						
						jQuery('#emp_cd').val(json[0].emp_cd);
						jQuery('#emp_nm').val(json[0].emp_nm);
						jQuery('#tel_no').val(json[0].tel_no);
						jQuery('#mobile_no').val(json[0].mobile_no);
						jQuery('#email').val(json[0].email);
						jQuery('#use_yn').val(json[0].use_yn);
						jQuery('#emp_auth').val(json[0].auth);
					}
				}
			}
		});
		
	}
	function js_saveForm()
	{
		if(checkFormField("#writeForm"))
		{
			jQuery.ajax({
				url : '/mng/sys/action/empMgr.jspx?cmd=saveEmpinfo', 
				data : jQuery("#writeForm").serialize(),
				type: 'POST',
				dataType: 'json',
				async : false,	
				success : function(jsonObj)
				{
					if(jsonObj.result.code == '200'){
						msgStart(msg_mng_code_009);
						js_getEmpList(1);
					}else{
						msgStart(msg_mng_code_010 + '['+jsonObj.result.msg+']' , 'danger');
					}
				}
			});
		}
	}
	function js_newForm()
	{
		jQuery("form").each(function() {  
            if(this.id == "writeForm") this.reset();  
        });
		
		jQuery('#edit_title').html('관리자정보 신규등록');
	}
	
	jQuery(document).ready(function(){
		
		
		js_getEmpList(1);
		
		makeCodeSelectBox('emp_auth', '', 'emp_auth', false, true);
		makeCodeSelectBox('use_yn', '', 'use_yn', false, true);
		
		jQuery('#btn-save').bind('click', function(e){ js_saveForm(); });
		jQuery('#btn-reg').bind('click', function(e){ js_newForm(); });
		
	});
	
	
</script>
</head>
<body>
<form name="form" id="form">
<input type="hidden" name="cur_pg" id="cur_pg" value="1">
<input type="hidden" name="row_per_page" id="row_per_page" value="10">
<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>
</form>

<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      <div class="x_title">
        <h2>관리자정보관리</h2>
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
                <th class="column-title">관리자ID </th>
                <th class="column-title">관리자이름</th>
                <th class="column-title">전화번호 </th>
                <th class="column-title">핸드폰번호 </th>
                <th class="column-title">이메일 </th>
                <th class="column-title">사용여부 </th>
                <th class="column-title no-link last"><span class="nobr">권한</span></th>
              </tr>
            </thead>

            <tbody id="list_div">
              
            </tbody>
          </table>
        </div>
        <div class="row">
		  	<div class="col-sm-5">
		  		<div class="dataTables_info" id="page_text_info" role="status" aria-live="polite">0 to 0 of 0 건</div>
		  	</div>
		  	<div class="col-sm-7">
		  		<div class="dataTables_paginate paging_simple_numbers" id="datatable-checkbox_paginate">
			  		<ul class="pagination" id="pagingDiv">
			  		</ul>
		  		</div>
		  	</div>
		  </div>
		  
		  <div class="pull-right">
			<span id="btn-reg" class="btn btn-success" type="reset">신규등록</span>
          </div>
      </div>
    </div>
  </div>
  
  <div class="clearfix"></div>
  

     <div class="col-md-12 col-sm-12 col-xs-12">
       <div class="x_panel">
         <div class="x_title">
           <h2 id="edit_title">관리자정보 신규등록</h2>
           <ul class="nav navbar-right panel_toolbox">
             <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
             </li>             
             <li><a class="close-link"><i class="fa fa-close"></i></a>
             </li>
           </ul>
           <div class="clearfix"></div>
         </div>
         <div class="x_content">
           
           <form id="writeForm" name="writeForm">
				<table class="com_table_box">
					<tr>
						<th style="width:110px;">관리자ID</th>
						<td><input type="text" id="emp_cd" name="emp_cd" maxlength="20" class="form-control" required></td>
						<th style="width:110px;">관리자이름</th>
						<td><input type="text" id="emp_nm" name="emp_nm" maxlength="20" class="form-control" required></td>
					</tr>
				    <tr>
				        <th>전화번호</th><td><input type="text" id="tel_no" name="tel_no" maxlength="20" class="form-control" ></td>
				        <th>핸드폰번호</th><td><input type="text" id="mobile_no" name="mobile_no" maxlength="20" class="form-control" ></td>
				    </tr>
					<tr>
				        <th>이메일</th><td colspan="3"><input type="text" id="email" name="email" maxlength="50" class="form-control" ></td>
				    </tr>
				    <tr>
				        <th>권한</th>
				        <td>
					        <select id="emp_auth" name="emp_auth" class="form-control" required>
					        </select>
				        </td>
				        <th>사용여부</th>
				        <td>
				        	<select id="use_yn" name="use_yn" class="form-control" required>
				        	</select>
				        </td>	
				    </tr>
				    
				</table>
			</form>
	
			<span class="pull-right" style="padding: 10px 20px 0 0;">
				<span id="btn-save" class="btn btn-sm btn-primary"><i class="icon-file"> 저장</i></span>
			</span>
           
         </div>
       </div>
     </div>

</body>
</html>									