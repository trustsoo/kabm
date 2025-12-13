//인젝션 필드 확인....
//인젝션 관련 값이 있는지 체크 :있으면 true
function isInjectField(frmObj){
	var formObj = $(frmObj);
	var fg = false;

	//var inputList=formObj.find("input[type=text]");
	formObj.find("input[type=text]").each(function()
		{
			var regexp = /\-\-|'|;|<|>|\"|\+/;

			if( regexp.test($(this).val()) == true ){
				alert("입력값에 허용되지 않는 문자가 입력됐습니다.\n[--],['],[;],[<],[>],[\"],[+]");
				fg = true;
			}

		
			//한문은 금지되어있음. 혹시나 필요할 경우 찾아서 추가해주시기 바람.
			if(fg==false){
				var regexp2 =/^[가-힣a-zA-Zㄱ-ㅎㅏ-ㅣ0-9,\ \$\!\.\+\:\=\_\*\?\@\%\(\)\[\]\|\\|\/]{1,5000}$/;
				if(regexp2.test($(this).val())==false && $(this).val()!=""){
				alert("허용되지 않는 문자가 입력됐습니다.\n특수문자는 \n([@],[$],[!],[?],[*],[%],[:],[=],[+],[.],[,],[|],[/],[_])\n만 사용가능합니다.");
				fg=true;
				}
			}
		});

	return fg;
}