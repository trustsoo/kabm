<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>    
<%@ page import="jdf.framework.view.auth.User"%>
<%@ page import="jdf.framework.core.http.SessionAttributes"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="el" uri="/tld/el-functions"%>
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
		js_getMemberList(cur_pg);
	}
	
	function js_getMemberList(cur_pg)
	{
		jQuery('#cur_pg').val(cur_pg);
		
		jQuery.ajax({
			url : '/mng/member/action/memberMgr.jspx?cmd=getMemberList', 
			data : jQuery("#search_form").serialize(),
			type: 'POST',
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				if(jsonObj.result.code == '200'){
					var listHtml = [];
					var json = jsonObj.result.data.MEM_INFO;
					var jsonCnt = jsonObj.result.data.MEM_INFO.length;
					jQuery('#tot_cnt').val(jsonObj.result.data.property[0].tot_cnt);
					
					if(jsonCnt > 0){
						for(var i=0; i<jsonCnt; i++){
														
							var mod = i%2;
							if( mod == 0 )
							{
								listHtml.push('<tr class="odd pointer" style="cursor:pointer;" onclick="js_getMemberinfo(\''+json[i].user_id+'\');">');
							}else{
								listHtml.push('<tr class="even pointer" style="cursor:pointer;" onclick="js_getMemberinfo(\''+json[i].user_id+'\');">');
							}
							
							listHtml.push('		  <td>'+json[i].user_id+'</td>');
							listHtml.push('		  <td>'+json[i].user_nm+'</td>');
							listHtml.push('		  <td>'+json[i].corp_nm+'</td>');
							listHtml.push('		  <td>'+json[i].hname+'</td>');
							listHtml.push('		  <td>'+json[i].jiname+'</td>');
							listHtml.push('	  <td>'+json[i].tel_no+'</td>');
							listHtml.push('	  <td>'+json[i].email+'</td>');
							listHtml.push('	  <td class=" last">'+json[i].mem_div_nm+'</td>');
							listHtml.push('	</tr>');
							
							
							
						}
					}else{
						listHtml.push('		<tr>');
						listHtml.push('		  <td colspan="8" style="text-align:center;">조회된 결과가 없습니다.</td>');
						listHtml.push('		</tr>');
						
					}
					jQuery('#list_div').html(listHtml.join(''));
					
					js_Paging('pagingDiv', jQuery('#cur_pg').val(), jQuery('#tot_cnt').val(), jQuery('#row_per_page').val(), 'goSearch');
					js_Paging_text('page_text_info', jQuery('#cur_pg').val(), jQuery('#tot_cnt').val(), jQuery('#row_per_page').val());
				}
			}
		});
		
	}
	
	function js_getMemberinfo(user_id)
	{
		if(user_id == '' || user_id == 'undefined' ) return;
		jQuery('#user_id').prop('readonly', true);
		
		jQuery.ajax({
			url : '/mng/member/action/memberMgr.jspx?cmd=getMemberInfo', 
			data : 'user_id='+user_id,
			type: 'POST',
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				if(jsonObj.result.code == '200'){
					var json = jsonObj.result.data.MEM_INFO;
					var jsonCnt = jsonObj.result.data.MEM_INFO.length;
					
					if(jsonCnt > 0){
						jQuery('#edit_title').html('관리자정보 수정');
						
						jQuery('#user_id').val(json[0].user_id);
						jQuery('#user_nm').val(json[0].user_nm);
						jQuery('#tel_no').val(json[0].tel_no);
						jQuery('#email').val(json[0].email);
						jQuery('#mem_div').val(json[0].mem_div);
						jQuery('#mb_no').val(json[0].mb_no);
						
						var addr_nm = '('+json[0].cpost+')'+ json[0].cp_addr + ' '+ json[0].cp_jibun;
						lf_setCorpInfo(json[0].corp_reg_no ,json[0].corp_nm,
									   json[0].cp_code,json[0].ill_no,
									   json[0].hname,json[0].jiname,
									   json[0].cptel, json[0].cpfax, 
									   addr_nm,mb_no)
						
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
				url : '/mng/member/action/memberMgr.jspx?cmd=saveMemberinfo', 
				data : jQuery("#writeForm").serialize(),
				type: 'POST',
				dataType: 'json',
				async : false,	
				success : function(jsonObj)
				{
					if(jsonObj.result.code == '200'){
						msgStart(msg_mng_code_009);
						js_getMemberList(1);
					}else{
						msgStart(msg_mng_code_010 + '['+jsonObj.result.msg+']' , 'danger');
					}
				}
			});
		}
	}
	function js_initPass()
	{
		if( !confirm(msg_mng_code_109) ) return;
		jQuery.ajax({
			url : '/mng/member/action/memberMgr.jspx?cmd=passwdReset', 
			data : jQuery("#writeForm").serialize(),
			type: 'POST',
			dataType: 'json',
			async : false,	
			success : function(jsonObj)
			{
				if(jsonObj.result.code == '200'){
					msgStart(msg_mng_code_009);
				}else{
					msgStart(msg_mng_code_010 + '['+jsonObj.result.msg+']' , 'danger');
				}
			}
		});
	}
	function js_newForm()
	{
		jQuery("form").each(function() {  
            if(this.id == "writeForm") this.reset();  
        });
		
		jQuery('#edit_title').html('사용자정보 신규등록');
		jQuery('#user_id').prop('readonly', false);
		lf_corpSelect();
	}
	

	function lf_corpSelect()
	{
		var url = '/common/common.jspx?cmd=viewMemCorpList';
		window.open(url, "corpPoP" , "width=700,height=550,toolbar=no,scroll=no,menubar=no");
	}

	function lf_setCorpInfo(corp_reg_no ,corp_nm,cp_code,ill_no,hname,jiname,cptel, cpfax, addr_nm,mb_no)
	{
		if( jQuery('#user_id').val() == '' ) jQuery('#user_id').val(mb_no);
		jQuery('#corp_reg_no').val(corp_reg_no);
		jQuery('#corp_nm').val(corp_nm);
		jQuery('#cp_code').val(cp_code);
		jQuery('#ill_no').val(ill_no);
		jQuery('#hname').val(hname);
		jQuery('#jiname').val(jiname);
		jQuery('#cptel').val(cptel);
		jQuery('#cpfax').val(cpfax);
		jQuery('#addr_nm').val(addr_nm);
	}

	jQuery(document).ready(function(){
		$(".date-picker").datepicker({format: 'yyyy-mm-dd'});
		
		js_getMemberList(1);
		
		jQuery('#btn_search').bind('click', function(e){ js_getMemberList(1); });
		jQuery('#btn-save').bind('click', function(e){ js_saveForm(); });
		jQuery('#btn-reg').bind('click', function(e){ js_newForm(); });
		jQuery('#btn-pass').bind('click', function(e){ js_initPass(); });
		
		jQuery('#btn_memSel').bind("click", function(){
			lf_corpSelect();
		});
				
		jQuery("input[name=search_user_nm]").keydown(function (key) {			 
	        if(key.keyCode == 13){
	        	js_getMemberList(1);
	        }	 
	    });
		
		jQuery("input[name=search_user_id]").keydown(function (key) {			 
	        if(key.keyCode == 13){
	        	js_getMemberList(1);
	        }	 
	    });
		
		jQuery("input[name=search_corp_nm]").keydown(function (key) {			 
	        if(key.keyCode == 13){
	        	js_getMemberList(1);
	        }	 
	    });
	});
	
	
</script>
</head>
<body>

<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      <div class="x_content">
		<form id="search_form" >
			<input type="hidden" name="cur_pg" id="cur_pg" value="1">
			<input type="hidden" name="row_per_page" id="row_per_page" value="10">
			<input type="hidden" name="tot_cnt" id="tot_cnt" value='0'/>

			<table class="condition-table">
				<tbody>
					<tr>
						<th width="100px"><div>회원사</div></th>
						<td width="150px">
							<select name="search_gbn_hcode" id="search_gbn_hcode" title="">
								<option value="" selected="selected">회원전체</option>
								<option value="10">정회원</option>
								<option value="20">일반회원</option>
								<option value="30">특별회원</option>
								<option value="40">특별비회원</option>
							</select>
						</td>
						<th width="100px"><div>지회</div></th>
						<td width="150px">
							<select name="search_gbn_jicode" id="search_gbn_jicode" title="">
				
								<option value="" selected="selected">지회선택</option>
								<c:forEach begin="0" end="${output_count}" step="1" var="idx">
									<option value="${output[idx].jicode}">${output[idx].company}</option>
								</c:forEach>
							</select>
						</td>
						<th width="100px"><div>회사명</div></th>
						<td width="150px" colspan="2">
							<input name="search_corp_nm" id="search_corp_nm" type="text" class="it mid " title="" value="" />
						</td>
						
					</tr>
					<tr class='last-tr'>
						<th width="100px"><div>가입구분</div></th>
						<td width="150px">
							<select name="search_mem_div" id="search_mem_div" title="">
								<option value="" selected="selected">가입전체</option>
								<option value="2">회원사</option>
								<option value="0">신규</option>
								<option value="1">책임자</option>
								<option value="3">개인</option>
								<option value="4">기업</option>
							</select>
						</td>
						<th width="100px"><div>성명</div></th>
						<td width="150px">
							<input name="search_user_nm" id="search_user_nm" type="text" class="it mid " title="" value="" />
						</td>
						<th width="100px"><div>아이디</div></th>
						<td width="150px">
							<input name="search_user_id" id="search_user_id" type="text" class="it mid " title="" value="" />
						</td>
						<td width="80px" >
							<div id="btn_search" class="btn btn-sm btn-warning" style="float:right; font-size:15px; padding:0px 10px; margin-right:10px;"><b>조회</b></div>
							
						</td>
					</tr>	
				</tbody>
			</table>
		</form>
	    </div>

      <div class="x_content">
        <div class="table-responsive">
          <table class="table table-striped jambo_table bulk_action">
            <thead>
              <tr class="headings">                
                <th class="column-title">ID </th>
                <th class="column-title">이름</th>
                <th class="column-title">회사명</th>
                <th class="column-title">회원사</th>
                <th class="column-title">지회</th>
                <th class="column-title">전화번호 </th>
                <th class="column-title">이메일 </th>
                <th class="column-title no-link last"><span class="nobr">가입구분</span></th>
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
           </ul>
           <div class="clearfix"></div>
         </div>
         <div class="x_content">
           
           <form id="writeForm" name="writeForm">
           <input type="hidden" name="cp_code" id="cp_code" value="">
           <input type="hidden" name="ill_no" id="ill_no" value="">
           <input type="hidden" name="mb_no" id="mb_no" value="">
           
				<table class="com_table_box">
					<tr>
						<th style="width:110px;">사용자ID</th>
						<td><input type="text" id="user_id" name="user_id" maxlength="20" class="form-control" required></td>
						<th style="width:110px;">사용자이름</th>
						<td><input type="text" id="user_nm" name="user_nm" maxlength="20" class="form-control" required></td>
					</tr>
				    <tr>
				        <th>전화번호</th><td><input type="text" id="tel_no" name="tel_no" maxlength="20" class="form-control" ></td>
				         <th>이메일</th><td><input type="text" id="email" name="email" maxlength="50" class="form-control" ></td>
				    </tr>
					<tr>
				        <th>가입구분</th>
				        <td colspan="3">
				        	<select name="mem_div" id="mem_div" title="" class="form-control" style="width:20%;display: inline;">
								<option value="2" selected="selected">회원사</option>
								<option value="0">신규</option>
								<option value="1">책임자</option>
								<option value="3">개인</option>
								<option value="4">기업</option>
							</select>
				        </td>	
				    </tr>
				</table>
			
			
				<table class="com_table_box">
					<tr>
						<th style="width:110px;">회사명</th>
						<td><input type="text" id="corp_nm" name="corp_nm" maxlength="20" class="form-control" style="width:80%;display: inline;" alt='회사명' readonly="readonly" required>
						<span id="btn_memSel" class="btn btn-success" type="reset">선택</span>
						</td>
						<th style="width:110px;">사업자등록번호</th>
						<td><input type="text" id="corp_reg_no" name="corp_reg_no" maxlength="20" class="form-control" alt='사업자등록번호' readonly>
						</td>
					</tr>
				    <tr>
				        <th>회원사구분</th>
				        <td><input type="text" id="hname" maxlength="20" class="form-control" readonly></td>
				        <th>지회</th>
				        <td><input type="text" id="jiname" maxlength="20" class="form-control" readonly></td>				    	
					</tr>
					<tr>
						<th>회사주소  </th>
						<td colspan="3">
							<input type="text" title="회사주소" value="" id='addr_nm' alt='회사주소' class="form-control"  readonly/>
						</td>
					</tr>
					<tr>
						<th>회사전화번호  </th>
						<td>
							<input type="text" title="회사전화번호" name="corp_tel_no" id='cptel' alt='회사전화번호' class="form-control"  readonly/>
						</td>
						<th>회사팩스번호  </th>
						<td>
							<input type="text" title="회사팩스번호" name="corp_fax_no"  id='cpfax' alt='회사팩스번호' class="form-control" readonly/>
						</td>
					</tr>
				</table>
			</form>
			<span class="pull-right" style="padding: 10px 20px 0 0;">
				<span id="btn-save" class="btn btn-sm btn-primary"><i class="fa fa-file"> 저장</i></span>
				<span id="btn-pass" class="btn btn-sm btn-success"><i class="fa fa-file"> 비밀번호초기화</i></span>
			</span>
           
         </div>
       </div>
     </div>

</body>
</html>									