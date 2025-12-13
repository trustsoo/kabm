<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="lgdacom.XPayClient.XPayClient"%>

<%

	String configPath 	= "C:/ikasm_cyber_webapps/WEB/WebContent/lgdacom";  //LG텔레콤에서 제공한 환경파일("/conf/lgdacom.conf") 위치 지정.
	try
	{
		jdf.framework.core.Config conf = jdf.framework.core.Configuration.lookup("/payment");
		String tmpconfigPath = conf.getString("confpath");
		if(tmpconfigPath != null && !"".equals(tmpconfigPath) )
			configPath = tmpconfigPath;
	}catch(Exception e ){}
    /*
     * [LG텔레콤 환경파일 UPDATE]
     *
     * 이 페이지는 LG텔레콤 환경파일을 UPDATE 합니다.(수정하지 마세요.)
     */
    String CST_PLATFORM = request.getParameter("CST_PLATFORM");
    String CST_MID      = request.getParameter("CST_MID");

    if( CST_PLATFORM == null || "".equals(CST_PLATFORM.trim()) ) {
        out.println("[TX_PING error] CST_PLATFORM 파라미터 누락<br>");
        return;
    }
    if( CST_MID == null || "".equals(CST_MID.trim()) ) {
        out.println("[TX_PING error] CST_MID 파라미터 누락<br>");
        return;
    }
    String LGD_MID      = ("test".equals(CST_PLATFORM.trim())?"t":"")+CST_MID;

    XPayClient xpay = new XPayClient();
    xpay.Init(configPath, CST_PLATFORM);
    xpay.Init_TX(LGD_MID);

    out.println( "patch result = " + xpay.Patch("lgdacom.conf") );
%>
