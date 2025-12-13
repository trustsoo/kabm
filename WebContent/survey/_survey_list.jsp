<%@page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="output" type="jdf.framework.core.data.DataSet" scope="request" />
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
#widget_grid .jqgrow{cursor:pointer;}
</style>
<script type="text/javascript" type="text/javascript">
//<![CDATA[
    jQuery(document).ready(function(){
    	js_getList();	
    	
		jQuery('#change_add_btn').bind('click', function(){location.href='/survey/survey.jspx?cmd=write';});
		
    });
	
	
	function js_getList(cur_pg)
	{
		var _url = "/survey/action/survey.jspx?cmd=getSurveyList";	

		var http = jQuery.ajax( {
	   		url: _url,	
	   		datatype : 'json',
	   		data : '', 
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

		   		$("#listData > tr").remove();
		   		

		   		var listDataRows=[];		   		
		   		surveyList = {};  

		   		var dataCount = 0;
				if( data.paper_info ) 
					dataCount =  data.paper_info.length;


		   		if (code == 200){		
		   			
		   			for(var i=0 ; i < dataCount ; i++)
		   			{
		   			
		   				//STR : 데이터 보관
		   				var surveyItem = {};
		   				surveyItem['paper_id'] = data.paper_info[i].paper_id ;
		   				surveyItem['paper_title'] = data.paper_info[i].paper_title ;
		   				surveyItem['qstn_cnt'] = data.paper_info[i].qstn_cnt ;
		   				surveyItem['reg_dt'] = data.paper_info[i].reg_dt ;
		   				surveyItem['upd_dt'] = data.paper_info[i].upd_dt ;
		   				
		   				surveyList[data.paper_info[i].paper_id] = surveyItem;
		   						   				
						//END : 데이터 보관
		   				var tdData = "";
		   				tdData = "<td style='text-align:center;'>"+ (i+1) +"</td>" ; 		   				
		   				tdData += "<td style='text-align:left;'><a href='/survey/survey.jspx?cmd=write&paper_id="+data.paper_info[i].paper_id+"'>"+ data.paper_info[i].paper_title +"</a></td>" ; 

		   				tdData += "<td style='text-align:left;'>"+ data.paper_info[i].qstn_cnt +"</td>" ;  
		   				tdData += "<td style='text-align:center;'>"+ data.paper_info[i].reg_dt +"</td>" ; 
		   				tdData += "<td style='text-align:center;'>"+ data.paper_info[i].upd_dt +"</td>" ;  
		   				tdData += "<td style='text-align:center;'><span class='btn btn-success btn-xs' onclick=\"js_requestDrop('" + data.paper_info[i].paper_id  + "');\">삭제</span></td>" ; 

		   				listDataRows.push("<tr>" + tdData + "</tr>");
		   			}
						
		   			if(dataCount == 0 )
		   				listDataRows.push("<tr><td colspan='6' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>"); 
					
		   		}else{
		   			listDataRows.push("<tr><td colspan='6' style='text-align:center;'>[" + code + "] " + msg + " </td></tr>"); 
		   		}

				$("#listData").html(listDataRows.join());
				
			}			
		} );
	}
	
	function js_requestDrop(paper_id) {
		if(confirm(msg_com_code_107)) {
			var _url = '/survey/action/survey.jspx?cmd=dropSurvey';

			var http = jQuery.ajax({
		        url: _url,
		        type: "POST",
		        data : "&paper_id="+paper_id,
		        dataType: "json",
		        async : false,
		        error : function(xhr) {
					alert(xhr.status);
				},
				success: function(json) {
					var code = json.result.code;
					var msg = json.result.msg;
					var item = json.result.data;

					if (code == '200') {
						msgStart(msg_com_code_009);
						js_getList();
					} else if(code == '404') {
						msgStart(msg_com_code_010+"(이미 설문일정 결과가 있음.)", 'danger');
					} else {
						msgStart(msg_com_code_010+"("+msg+")", 'danger');
					}
				}
			});
		} else {
		}
	}
	
//]]>
</script>
</head>
<body>
	<div class="col-md-12 col-sm-12 col-xs-12">	
	
	    <div class="x_panel">
	      	<div class="x_content">			
				<table class="table table-striped jambo_table bulk_action">
					<thead class="thin-border-bottom center" >
					
					<tr class="headings">
							<th width="80px">설문번호</th>
							<th>설문지제목</th>
							<th width="100px">문항개수</th>
							<th width="100px">등록일시</th>
							<th width="100px">수정일시</th>
							<th width="60px">삭제</th>	
						</tr>
					</thead>
					<tbody id="listData">
						<tr><td colspan="6" style="text-align:center;">조회된 결과가 없습니다.</td></tr>
					</tbody>
				</table>			
	      	</div>

	      	<div class="row">
			  	<div class="col-sm-12">
			  		<div class="pull-right">
					<span id="change_add_btn" class="btn btn-success" type="reset">신규등록</span>
		          </div>
			  	</div>
			  </div>
			  
	      </div>
	    </div>
	</div>
	

</body>
</html>