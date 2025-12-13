<%@ page import="java.security.MessageDigest" %>
<%
/*
 * [최종결제요청 페이지(STEP2-2)]
 *
 * LG텔레콤으로 부터 내려받은 LGD_PAYKEY(인증Key)를 가지고 최종 결제요청.(파라미터 전달시 POST를 사용하세요)
 */

String configPath = "D:/repository/git/kabm/WebContent/edu/lgdacom";  //LG텔레콤에서 제공한 환경파일("/conf/lgdacom.conf,/conf/mall.conf") 위치 지정.
String thisServerName = request.getServerName();
int port = request.getLocalPort();

String domain = "";

try
{
	if(thisServerName == null || "".equals(thisServerName))
	{
		jdf.framework.core.Config conf = jdf.framework.core.Configuration.lookup("/site");
		thisServerName = conf.getString("domain");
	}
	domain = thisServerName;
	if( port != 80 && port != 443) domain = thisServerName + ":" + port;
}catch(Exception e ){}

try
{
	jdf.framework.core.Config conf = jdf.framework.core.Configuration.lookup("/payment");
	String tmpconfigPath = conf.getString("confpath");
	if(tmpconfigPath != null && !"".equals(tmpconfigPath) )
		configPath = tmpconfigPath;
}catch(Exception e ){}
/*
 *************************************************
 * 1.최종결제 요청 - BEGIN
 *  (단, 최종 금액체크를 원하시는 경우 금액체크 부분 주석을 제거 하시면 됩니다.)
 *************************************************
 */

 
 /*
 * 가상계좌(무통장) 결제 연동을 하시는 경우 아래 LGD_CASNOTEURL 을 설정하여 주시기 바랍니다.
 */
String LGD_CASNOTEURL		= "https://edu.kabm.org/edu/payment/action/pay_virtualReturn.jsp";
								//LG텔레콤에서 제공한 환경파일("/conf/lgdacom.conf") 위치 지정.
try
{
	
	LGD_CASNOTEURL = "https://" + domain + "/edu/payment/paymentResult.jspx";
}catch(Exception e ){}

								
								
/*
 * LGD_RETURNURL 을 설정하여 주시기 바랍니다. 반드시 현재 페이지와 동일한 프로트콜 및  호스트이어야 합니다. 아래 부분을 반드시 수정하십시요.
 */
String LGD_RETURNURL		= "https://상점URL/returnurl.jsp";// FOR MANUAL
try
{
	LGD_RETURNURL = "https://" + domain + "/edu/payment/pay_certKey.jsp";
}catch(Exception e ){}

/*
 * [결제 인증요청 페이지(STEP2-1)]
 *
 * 샘플페이지에서는 기본 파라미터만 예시되어 있으며, 별도로 필요하신 파라미터는 연동메뉴얼을 참고하시어 추가 하시기 바랍니다.
 */
 String LGD_MERTKEY          = "1d1b1cc15c595b6f2bb9b90f678ae5b8";				//상점MertKey(mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다)
 String CST_PLATFORM         = "test";                 //LG데이콤 결제서비스 선택(test:테스트, service:서비스)
 String CST_MID              = "kabm01";                      //LG데이콤으로 부터 발급받으신 상점아이디를 입력하세요.
 String LGD_MID              = ("test".equals(CST_PLATFORM.trim())?"t":"")+CST_MID;

 String LGD_CUSTOM_USABLEPAY = "SC0010-SC0030-SC0040";        	//상점정의 초기결제수단
 String LGD_CUSTOM_SKIN      = "red";                                                //상점정의 결제창 스킨(red, yellow, purple)
 String LGD_CUSTOM_SWITCHINGTYPE = "IFRAME"; //신용카드 카드사 인증 페이지 연동 방식 (수정불가)
 String LGD_WINDOW_VER		= "2.5";												//결제창 버젼정보
 String LGD_WINDOW_TYPE      = "iframe";               //결제창 호출 방식 (수정불가)
 String LGD_ENCODING         = "UTF-8";
 String LGD_ENCODING_RETURNURL = "UTF-8";
 String LGD_ENCODING_NOTEURL = "UTF-8";
 String LGD_OSTYPE_CHECK = "P";               //결제모듈이  PC에서만 실행되게 처리

%>
<%!

    public String LGD_MERTKEY          = "1d1b1cc15c595b6f2bb9b90f678ae5b8";				//상점MertKey(mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다)
    public String CST_PLATFORM         = "test";                 //LG데이콤 결제서비스 선택(test:테스트, service:서비스)
    public String CST_MID              = "kabm01";                      //LG데이콤으로 부터 발급받으신 상점아이디를 입력하세요.
    public String LGD_MID              = ("test".equals(CST_PLATFORM.trim())?"t":"")+CST_MID;

public void setLog(String LGD_OID, String LGD_BUYERID,String LGD_PAYKEY, String msg )
{
	try
    {
    	Logger.user.println("[PAYMENT]["+LGD_OID+"]["+LGD_BUYERID+"][PAYKEY:"+LGD_PAYKEY+"] : " + msg );
    }catch(Exception e){}
}

public String GetAuthData(String MID, String TID, String MertKey) {
    String hashdata = "";
    String text = MID + TID + MertKey;
    try {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] pb = messageDigest.digest(text.getBytes("MS949"));
        StringBuffer sb = new StringBuffer(pb.length << 1);
        int i = 0;

        for(int iend = pb.length; i < iend; ++i) {
            int val = pb[i] + 256 & 255;
            sb.append(Integer.toHexString(val >> 4)).append(Integer.toHexString(val & 15));
        }

        hashdata = sb.toString();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return hashdata;
}
%>