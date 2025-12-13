<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	jQuery(document).ready(function(){
		getList();
     });
	
	function getList()
	{
		
		jQuery.ajax({
			url : '/mng/sys/action/envVarMgr.jspx?cmd=getEnvVar', 
			data : '',
			type : 'POST',
			async : false,
			datatype: 'xml',
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
					var listHtml = [];
					var i = 0;
					var j = 1;
					var _html = '';
					jQuery(xmlDoc).find('env_var_list').each(function(){
						
						var seq_no = jQuery(this).find('seq_no').text();
						var key = jQuery(this).find('key').text();
						var val = jQuery(this).find('val').text();
						
						
						_html  = '		<tr>';
						_html += '		  <td>'+ seq_no +'</td>';
						_html += '		  <td>'+ key +'</td>';
						_html += '		  <td><input class="full-width" id="val_'+ seq_no +'" name="val_'+ seq_no +'" value="'+ val +'"/></td>';
						_html += '		  <td><button id="btn_'+ seq_no +'" class="btn btn-xs btn-curved btn-primary" onclick="updateEnvVal('+ seq_no +');"><i class="icon- fa fa-save"> 저장</i></button></td>';
						_html += '		</tr>';
						j++;
						
						listHtml.push(_html);
						i++;
					});	
					
					if( i <= 0 || j<=1)
					{
						listHtml.push('		<tr>');
						listHtml.push('		  <td colspan="4" style="text-align:center;">조회된 결과가 없습니다.</td>');
						listHtml.push('		</tr>');	
					}
					jQuery('#listData').html(listHtml.join(''));
					
				}
			}
		});
	}
	
	function updateEnvVal(no)
	{
		
		var _url = '/mng/sys/action/envVarMgr.jspx?cmd=updateEnvVar';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : {
				seq_no : no,
				val : jQuery("#val_"+ no).val() 
			},		
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(xmlDoc)
			{
				
				msgStart(msg_mng_code_053);//수정되었습니다.
				 
				//다시 조회.
				getList();
			}			
		});
		
		
	}
	
	function fn_encodeData(cmd)
	{
		
		var val = '';
		if( cmd == 'convertEnc' )
		{
			val = jQuery("#dectxt").val();
		}else{
			val = jQuery("#enctxt").val();
		}
		
		var _url = '/mng/sys/action/envVarMgr.jspx?cmd=convertData';
		var http = jQuery.ajax( {
	   		url: _url,	   		
	   		type: "POST",
			data : {
				val : val,
				div : cmd
			},		
	   		async : false,	   		
	   		error 	: function(xhr)
	   		{
				alert(xhr.status);
			},
			success: function(xmlDoc)
			{
				var data = jQuery(xmlDoc).find('data').text();
				if( cmd == 'convertEnc' )
				{
					jQuery('#enctxt').val(data);
				}else{
					jQuery('#dectxt').val(data);
				}
			}			
		});
	}
	
	
	
</script>
</head>
<body>
<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      <div class="x_title">
        <h2>환경변수관리</h2>
        <ul class="nav navbar-right panel_toolbox">
          <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
          </li>
          <li><a class="close-link"><i class="fa fa-close"></i></a>
          </li>
        </ul>
        <div class="clearfix"></div>
      </div>
      <div class="x_content">

			<form name="commonForm" id="commonForm">
			<table class="table table-striped table-bordered table-hover table_layout_fixed">
				<thead class="thin-border-bottom center" >
					
					<tr>
						<th width="60px">No</th>
						<th width="250px">KEY</th>
						<th>VAL</th>
						<th width="100px"></th>
					</tr>
				</thead>
				<tbody id="listData">
					<tr><td colspan="4" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
				</tbody>
			</table>
			</form>
			
      </div>
    </div>
  </div>	
<div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      <div class="x_title">
        <h2>암호화</h2>
        <ul class="nav navbar-right panel_toolbox">
          <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
          </li>
          <li><a class="close-link"><i class="fa fa-close"></i></a>
          </li>
        </ul>
        <div class="clearfix"></div>
      </div>
      <div class="x_content">
		<table class="table table-striped table-bordered table-hover table_layout_fixed">
			<tbody>
				<tr>
					<th width="80px">인코드</th>
					<td><input type="text" name="dectxt" id="dectxt" value="" class="full-width"></td>
					<td width="80px"><a id="btn_enc" class="btn btn-xs btn-curved btn-primary" onclick="fn_encodeData('convertEnc');"><i class="icon- fa fa-save"> 인코드</i></a></td>
					<th width="80px">디코드</th>
					<td><input type="text" name="enctxt" id="enctxt" value="" class="full-width"></td>
					<td width="80px"><a id="btn_dec" class="btn btn-xs btn-curved btn-primary" onclick="fn_encodeData('convertDec');"><i class="icon- fa fa-save"> 디코드</i></a></td>
				</tr>
			</tbody>
		</table>
      </div>
    </div>
  </div>	

</body>
</html>									