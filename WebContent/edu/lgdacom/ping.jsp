<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="lgdacom.XPayClient.XPayClient"%>

<%
	String configPath 	= "C:/ikasm_cyber_webapps/WEB/WebContent/lgdacom";	//LG텔레콤에서 제공한 환경파일("/conf/lgdacom.conf") 위치 지정.
	try
	{
		jdf.framework.core.Config conf = jdf.framework.core.Configuration.lookup("/payment");
		String tmpconfigPath = conf.getString("confpath");
		if(tmpconfigPath != null && !"".equals(tmpconfigPath) )
			configPath = tmpconfigPath;
	}catch(Exception e ){}
    /*
     * [LG텔레콤 연결 확인 페이지]
     *
     * 이페이지는 LG텔레콤과의 연결을 테스트 하는 페이지 입니다.(수정하지 마세요.)
     */
    String CST_PLATFORM = request.getParameter("CST_PLATFORM");
    String CST_MID      = request.getParameter("CST_MID"); // 't'가 추가되지 않은 가입요청시 아이디를 입력바랍니다.

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
    xpay.Set("LGD_TXNAME", "Ping");
    xpay.Set("LGD_RESULTCNT", "3");
    xpay.TX();

    out.println("resCode = " + xpay.m_szResCode + "<br>");
    out.println("resMsg = " + xpay.m_szResMsg + "<br>");

    for (int i = 0; i < xpay.ResponseNameCount(); i++)
    {
        out.println(xpay.ResponseName(i) + " = ");
        for (int j = 0; j < xpay.ResponseCount(); j++)
        {
            out.println("\t" + xpay.Response(xpay.ResponseName(i), j) + "<br>");
        }
    }
%>
