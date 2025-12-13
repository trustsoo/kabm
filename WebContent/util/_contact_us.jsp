<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<script type="text/javascript" type="text/javascript">
//<![CDATA[	

var imsi;

	jQuery(document).ready(function(){
		$('#email3').change(function() { 
			$("#email2").val(this.value);
		});


		$("#registQuestion").click(function(){
			if( $("#name").val().length < 1 ){				
				msgStart("이름을 입력하세요.", 'info');  
				return false;
			}

			if( $("#tel_no1").val().length < 1 ){				
				msgStart("핸드폰번호를 입력하세요.", 'info');  
				return false;
			}

			if( $("#tel_no2").val().length < 1 ){				
				msgStart("핸드폰번호를 입력하세요.", 'info');  
				return false;
			}

			if( $("#email1").val().length < 1 ){				
				msgStart("이메일을 입력하세요.", 'info');  
				return false;
			}

			if( $("#email2").val().length < 1 ){				
				msgStart("이메일을 입력하세요.", 'info');  
				return false;
			}

			if( $("#title").val().length < 1 ){				
				msgStart("제목을 입력하세요.", 'info');  
				return false;
			}
		
			if( $("#question").val().length < 1 ){				
				msgStart("내용을 입력하세요.", 'info');  
				return false;
			}

			var _url = '/util/action/question.jspx?cmd=registQuestion&action=regist';	 //insert/update
		

			var http = jQuery.ajax( {
		   		url: _url,	
		   		datatype : 'json',
		   		data : jQuery("#input_form").serialize()  , 
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
			   		

			   		//화명 갱신
			   		if(code == 200){
			   			//alert(msg_mng_code_009);
			   			alert("광고문의가 접수 되었습니다");
			   			location.href = "/";
			   		}else{
			   			//msgStart(msg_mng_code_010 + '['+Json.result.msg+']' , 'danger');
			   			//alert(msg_mng_code_010 + '['+Json.result.msg+']' );
			   			alert(msg_mng_code_010) ;

			   			location.reload(true);
			   		}
				}			
			} );
		
		});
	});

	



//]]>
</script>

	<div class="container">
		<div class="pageTit">
			<h2>
				<span class="tit_2">CONTACT US</span>
			</h2>
			<p class="rStep">
			</p>
		</div>
		<div id="content">
		<!-- start :: content -->
		<!-- start :: content -->

			<div class="writeWrap">

			<form id="input_form" >

				<table class="" summary="" >
					<caption></caption>
					<colgroup>
						<col width="25%"/><col width=""/>
					</colgroup>
					<thead>
						<tr>
							<th scope="row">구분</th>
							<td>
								<select name="question_div"  id="question_div" title="" style="color:#666;">
									<option value="qna_category_02" selected="selected">광고문의</option>
								</select>
							</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th scope="row">이름 : </th>
							<td>
								<input name="name" id="name" type="text" class="it size02" title="" value=""/>
							</td>
						</tr>
						
						<tr>
							<th scope="row">핸드폰 : </th>
							<td>
								
								<SELECT name=tel_no0>
									<OPTION selected value=010>010</OPTION><OPTION value=011>011</OPTION><OPTION value=016>016</OPTION><OPTION value=017>017</OPTION><OPTION value=018>018</OPTION><OPTION value=019>019</OPTION>
								</SELECT>

								- <input name="tel_no1" id="tel_no1" type="text" class="it size01" title="" value=""/>
								- <input name="tel_no2" id="tel_no2" type="text" class="it size01" title="" value=""/>
							</td>
						</tr>
						<tr>
							<th scope="row">이메일 : </th>
							<td>
								<input id="email1" name="email1" type="text" class="it size03" title="" value=""/> 
								@
								<input id="email2" name="email2" type="text" class="it size03" title="" value=""/>

								<SELECT id="email3" name='email3'> 
									<OPTION selected value="">직접입력</OPTION> 
									<OPTION value='naver.com'>naver.com</OPTION> 
									<OPTION value='daum.net'>daum.net</OPTION> 
									<OPTION value='dreamwiz.com'>dreamwiz.com</OPTION> 
									<OPTION value='empal.com'>empal.com</OPTION> 
									<OPTION value='hanmail.net'>hanmail.net</OPTION> 
									<OPTION value='hanmir.com'>hanmir.com</OPTION> 
									<OPTION value='hanafos.com'>hanafos.com</OPTION> 
									<OPTION value='hotmail.com'>hotmail.com</OPTION> 
									<OPTION value='lycos.co.kr'>lycos.co.kr</OPTION> 
									<OPTION value='nate.com'>nate.com</OPTION> 
									<OPTION value='paran.com'>paran.com</OPTION> 
									<OPTION value='nate.com'>nate.com</OPTION> 
									<OPTION value='netian.com'>netian.com</OPTION> 
									<OPTION value='yahoo.co.kr'>yahoo.co.kr</OPTION> 
									<OPTION value='kornet.net'>kornet.net</OPTION> 
									<OPTION value='nownuri.net'>nownuri.net</OPTION> 
									<OPTION value='unitel.co.kr'>unitel.co.kr</OPTION> 
									<OPTION value='freechal.com'>freechal.com</OPTION> 
									<OPTION value='korea.com'>korea.com</OPTION>
									<OPTION value='orgio.net'>orgio.net</OPTION>
									<OPTION value='chollian.net'>chollian.net</OPTION>
									<OPTION value='hitel.net'>hitel.net</OPTION>
								</SELECT>

							</td>
						</tr>
						<tr>
							<th scope="row">제목 : </th>
							<td>
								<input name="title" id="title" type="text" class="it " title="" value="" /> 
							</td>
						</tr>
						<tr>
							<th scope="row">내용 : </th>
							<td>
								<textarea name="question" id="question" class="txt" cols="" rows="" title=""></textarea>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="btnWrap">
					<a href="#" class="pbtn02" id="registQuestion"><span>확인</span></a>
					<a href="/" class="pbtn01"><span>취소</span></a>
				</div>

			</div>
			</form>
		<!-- end :: content -->
		<!-- end :: content -->
		</div>
	</div>


<style type="text/css">
<!--


//-->
</style>
