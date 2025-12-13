<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	jQuery(document).ready(function(){
		
     });
	
	
</script>
</head>
<body>

<div class="row-fluid">
	<div class="span12">
		<div class="widget-box border_0">		 	
			<div class="widget-body_A">
				<div class="widget-main">
					<form name='srch_form' id='srch_form' onSubmit="javascript:return false;">											
						<table class="ubi_table_box" border="0" cellpadding="0" cellspacing="0" style="margin-top:0px">						
							<colgroup><col width="150px"><col width=""></colgroup>
						<tbody>							
							<tr>
								<th>No.</th>
								<td>
									<input class="W100P" name="rowid" id="rowid" type="text" value="<%=output.getText("rowid")%>" readonly>
								</td>
							</tr>
							<tr>
								<th>서버구분</th>
								<td>
									<input class="W100P" name="server_alias" id="server_alias" type="text" value="<%=output.getText("server_alias")%>" readonly>
								</td>
							</tr>
							<tr>
								<th>로그구분</th>
								<td>
									<input class="W100P" name="log_mode" id="log_mode" type="text" value="<%=output.getText("log_mode")%>" readonly>
								</td>
							</tr>
							<tr>
								<th>등록일시</th>
								<td>
									<input class="W100P" name="write_dt" id="write_dt" type="text" value="<%=output.getText("write_dt")%>" readonly>
								</td>
							</tr>
							<tr>
								<th>내용</th>
								<td>
									<textarea class="W100P" style="height:200px; color:#1375EC; background:#F5FEFF" wrap="virtual" name="logs" id="logs" readonly="readonly"><%=output.getText("logs")%></textarea> 
								</td>
							</tr>
						</tbody>						
						</table>						
					</form>
				</div>
			</div>
		</div>
	</div>
</div>	

</body>
</html>									