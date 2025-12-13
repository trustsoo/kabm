<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="lgdacom.XPayClient.XPayClient"%>
<jsp:useBean id="input" type="jdf.framework.core.data.DataSet" scope="request" />
<%@ include file="/common/common.jsp"%>
<%@ include file="/edu/payment/paycommon.jsp"%>
<%
    /*
     * [상점 결제결과처리(DB) 페이지]
     *
     * 1) 위변조 방지를 위한 hashdata값 검증은 반드시 적용하셔야 합니다.
     *
     */
    String LGD_PAYKEY = ""; 
    String LGD_RESPCODE = "";           // 응답코드: 0000(성공) 그외 실패
    String LGD_RESPMSG = "";            // 응답메세지
    //String LGD_MID = "";                // 상점아이디 
    String LGD_OID = "";                // 주문번호
    String LGD_AMOUNT = "";             // 거래금액
    String LGD_TID = "";                // LG텔레콤이 부여한 거래번호
    String LGD_PAYTYPE = "";            // 결제수단코드
    String LGD_PAYDATE = "";            // 거래일시(승인일시/이체일시)
    String LGD_HASHDATA = "";           // 해쉬값
    String LGD_FINANCECODE = "";        // 결제기관코드(은행코드)
    String LGD_FINANCENAME = "";        // 결제기관이름(은행이름)
    String LGD_ESCROWYN = "";           // 에스크로 적용여부
    String LGD_TIMESTAMP = "";          // 타임스탬프
    String LGD_ACCOUNTNUM = "";         // 계좌번호(무통장입금) 
    String LGD_CASTAMOUNT = "";         // 입금총액(무통장입금)
    String LGD_CASCAMOUNT = "";         // 현입금액(무통장입금)
    String LGD_CASFLAG = "";            // 무통장입금 플래그(무통장입금) - 'R':계좌할당, 'I':입금, 'C':입금취소 
    String LGD_CASSEQNO = "";           // 입금순서(무통장입금)
    String LGD_CASHRECEIPTNUM = "";     // 현금영수증 승인번호
    String LGD_CASHRECEIPTSELFYN = "";  // 현금영수증자진발급제유무 Y: 자진발급제 적용, 그외 : 미적용
    String LGD_CASHRECEIPTKIND = "";    // 현금영수증 종류 0: 소득공제용 , 1: 지출증빙용
    String LGD_PAYER = "";    			// 임금자명
    
    /*
     * 구매정보
     */
    String LGD_BUYER = "";              // 구매자
    String LGD_PRODUCTINFO = "";        // 상품명
    String LGD_BUYERID = "";            // 구매자 ID
    String LGD_BUYERADDRESS = "";       // 구매자 주소
    String LGD_BUYERPHONE = "";         // 구매자 전화번호
    String LGD_BUYEREMAIL = "";         // 구매자 이메일
    String LGD_BUYERSSN = "";           // 구매자 주민번호
    String LGD_PRODUCTCODE = "";        // 상품코드
    String LGD_RECEIVER = "";           // 수취인
    String LGD_RECEIVERPHONE = "";      // 수취인 전화번호
    String LGD_DELIVERYINFO = "";       // 배송지

    LGD_RESPCODE            = request.getParameter("LGD_RESPCODE");
    LGD_RESPMSG             = request.getParameter("LGD_RESPMSG");
    //LGD_MID                 = request.getParameter("LGD_MID");
    LGD_OID                 = request.getParameter("LGD_OID");
    LGD_AMOUNT              = request.getParameter("LGD_AMOUNT");
    LGD_TID                 = request.getParameter("LGD_TID");
    LGD_PAYTYPE             = request.getParameter("LGD_PAYTYPE");
    LGD_PAYDATE             = request.getParameter("LGD_PAYDATE");
    LGD_HASHDATA            = request.getParameter("LGD_HASHDATA");
    LGD_FINANCECODE         = request.getParameter("LGD_FINANCECODE");
    LGD_FINANCENAME         = request.getParameter("LGD_FINANCENAME");
    LGD_ESCROWYN            = request.getParameter("LGD_ESCROWYN");
    LGD_TIMESTAMP           = request.getParameter("LGD_TIMESTAMP");
    LGD_ACCOUNTNUM          = request.getParameter("LGD_ACCOUNTNUM");
    LGD_CASTAMOUNT          = request.getParameter("LGD_CASTAMOUNT");
    LGD_CASCAMOUNT          = request.getParameter("LGD_CASCAMOUNT");
    LGD_CASFLAG             = request.getParameter("LGD_CASFLAG");
    LGD_CASSEQNO            = request.getParameter("LGD_CASSEQNO");
    LGD_CASHRECEIPTNUM      = request.getParameter("LGD_CASHRECEIPTNUM");
    LGD_CASHRECEIPTSELFYN   = request.getParameter("LGD_CASHRECEIPTSELFYN");
    LGD_CASHRECEIPTKIND     = request.getParameter("LGD_CASHRECEIPTKIND");
    LGD_PAYER     			= request.getParameter("LGD_PAYER");

    LGD_BUYER               = request.getParameter("LGD_BUYER");
    LGD_PRODUCTINFO         = request.getParameter("LGD_PRODUCTINFO");
    LGD_BUYERID             = request.getParameter("LGD_BUYERID");
    LGD_BUYERADDRESS        = request.getParameter("LGD_BUYERADDRESS");
    LGD_BUYERPHONE          = request.getParameter("LGD_BUYERPHONE");
    LGD_BUYEREMAIL          = request.getParameter("LGD_BUYEREMAIL");
    LGD_BUYERSSN            = request.getParameter("LGD_BUYERSSN");
    LGD_PRODUCTCODE         = request.getParameter("LGD_PRODUCTCODE");
    LGD_RECEIVER            = request.getParameter("LGD_RECEIVER");
    LGD_RECEIVERPHONE       = request.getParameter("LGD_RECEIVERPHONE");
    LGD_DELIVERYINFO        = request.getParameter("LGD_DELIVERYINFO");
    
    /*
     * hashdata 검증을 위한 mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다. 
     * LG텔레콤에서 발급한 상점키로 반드시변경해 주시기 바랍니다.
     */  

    StringBuffer sb = new StringBuffer();
    sb.append(LGD_MID);
    sb.append(LGD_OID);
    sb.append(LGD_AMOUNT);
    sb.append(LGD_RESPCODE);
    sb.append(LGD_TIMESTAMP);
    sb.append(LGD_MERTKEY);
    
    System.out.println(LGD_MID);
    System.out.println(LGD_OID);
    System.out.println(LGD_AMOUNT);
    System.out.println(LGD_RESPCODE);
    System.out.println(LGD_TIMESTAMP);
    System.out.println(LGD_MERTKEY);

    byte[] bNoti = sb.toString().getBytes();
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digest = md.digest(bNoti);

    StringBuffer strBuf = new StringBuffer();
    for (int i=0 ; i < digest.length ; i++) {
        int c = digest[i] & 0xff;
        if (c <= 15){
            strBuf.append("0");
        }
        strBuf.append(Integer.toHexString(c));
    }

    String LGD_HASHDATA2 = strBuf.toString();  //상점검증 해쉬값  
    System.out.println(LGD_HASHDATA2);
    System.out.println(LGD_HASHDATA);
    /*
     * 상점 처리결과 리턴메세지
     *
     * OK  : 상점 처리결과 성공
     * 그외 : 상점 처리결과 실패
     *
     * ※ 주의사항 : 성공시 'OK' 문자이외의 다른문자열이 포함되면 실패처리 되오니 주의하시기 바랍니다.
     */    
    LGD_RESPMSG = "결제결과 상점 DB처리(LGD_CASNOTEURL) 결과값을 입력해 주시기 바랍니다.";
    
    DataSet output = new DataSet();
    InteractionBean interact = new InteractionBean();
    
    input.put("cmd" , "GET_PAYMENT_INFO_BY_OID");
	input.put("LGD_OID" ,LGD_OID);
	output = interact.execute("/payment/PaymentMgr", input);
	
	LGD_PAYKEY = output.getText("LGD_PAYKEY");
	String user_seq_no = output.getText("user_seq_no");
	String lecture_seq_no = output.getText("lecture_seq_no");
	String tel_no = output.getText("tel_no");
	String user_nm = output.getText("user_nm");
if( output.getCount("LGD_PAYKEY") > 0 && !"".equals(user_seq_no) && !"".equals(lecture_seq_no) && !"".equals(LGD_PAYKEY) )
{
	input.put("LGD_PAYKEY",LGD_PAYKEY);
	input.put("user_seq_no",user_seq_no);
	input.put("lecture_seq_no",lecture_seq_no);
    if (LGD_HASHDATA2.trim().equals(LGD_HASHDATA)) { //해쉬값 검증이 성공이면
    	
    	LGD_RESPMSG = "[결제최초요청]결제요청 정보를 수록하였습니다."; 
    	
        if ( ("0000".equals(LGD_RESPCODE.trim())) ){ //결제가 성공이면
        	if( "R".equals( LGD_CASFLAG.trim() ) ) {
                /*
                 * 무통장 할당 성공 결과 상점 처리(DB) 부분
                 * 상점 결과 처리가 정상이면 "OK"
                 */    
                //if( 무통장 할당 성공 상점처리결과 성공 ) 
                	
                	setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+LGD_RESPCODE.trim()+"]"+"[PAYLES_2]"+LGD_RESPMSG.trim());
                	
		             input.put("cmd" , "UPDATE_PAYMENT");
		             input.put("status" , "02");
		             input.put("LGD_PAYKEY" ,LGD_PAYKEY);
		             input.put("LGD_RESCODE" ,LGD_RESPCODE.trim());
		             input.put("LGD_RESPMSG" ,LGD_RESPMSG.trim());
   				 output = interact.execute("/payment/PaymentMgr", input);
   				 
   				LGD_RESPMSG = "OK";//[할당성공]결제 처리를 위한 할당이 정상적으로 완료 되었습니다.
        		
        	}else if( "I".equals( LGD_CASFLAG.trim() ) ) {
        		setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+LGD_RESPCODE.trim()+"]"+"[PAYLES_3] 결제 성공 DB처리 시작");
             	boolean isDBOK = true; //DB처리 실패시 false로 변경해 주세요.

             	try
             	{
             		interact.beginTransaction();

             		input.put("cmd" , "INSERT_USER_LECTURE");
    		        output = interact.execute("/payment/PaymentMgr", input);
             		String user_lecture_seq_no = output.getText("user_lecture_seq_no");

             		if(user_lecture_seq_no == null || "".equals(user_lecture_seq_no))
             			throw new Exception();
             		setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+LGD_RESPCODE.trim()+"]"+"[PAYLES_4] INSERT_USER_LECTURE");

             		input.put("cmd" , "INSERT_USER_CLASS");
             		input.put("user_lecture_seq_no" , user_lecture_seq_no);
    		        output = interact.execute("/payment/PaymentMgr", input);
    		        setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+LGD_RESPCODE.trim()+"]"+"[PAYLES_5] INSERT_USER_CLASS");

             		input.put("cmd" , "UPDATE_PAYMENT_LECTURE");
    		        input.put("status" , "03");
    		        input.put("LGD_OID" ,LGD_OID);
    				input.put("LGD_TID" ,LGD_TID);
    				output = interact.execute("/payment/PaymentMgr", input);
    				setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+LGD_RESPCODE.trim()+"]"+"[PAYLES_6] DB처리 끝");

             		interact.commitTransaction();
             		LGD_RESPMSG = "OK"; //[DB처리성공]결제 처리가 정상적으로 완료 되었습니다.
             		
             		try
             		{
             			input.put("cmd" , "GET_USER_LECTURE_DETAIL");
	             		input.put("user_lecture_seq_no" , user_lecture_seq_no);
	    		        DataSet lectureInfo = interact.execute("/edu/LectureMgr", input);
	    		        String end_dd = "당해 연도";
	    		        if(lectureInfo != null ) end_dd = lectureInfo.getText("end_dd");
	    		        if(end_dd.length() == 8)
	    		        	end_dd = DateTime.trans(end_dd, "yyyyMMdd", "yyyy년 MM월 dd일");
	    		        if("".equals(end_dd)) end_dd = "당해 연도";
	    		        
             			String mem_div_nm = "신규및승계자교육";
	        			String mem_div = lectureInfo.getText("lecture_div");
	        			if( "2".equals(mem_div)) mem_div_nm = "공중위생관리책임자교육";
	        			
	        			String add_msg = "";
	        			if( "2".equals(mem_div)) add_msg = "단, 반드시 "+end_dd+" 이내에 수강 완료하셔야 합니다.";
	        			
	        			String subject = "[한국건물위생관리협회]온라인교육비 결제확인";
	        			String sms_msg = LGD_BUYER+ "님 "+mem_div_nm+" 교육비 결제가 완료되었으니 나의강의실에서 시작하기 클릭 후 반드시 2주(14일) 이내로 수강 완료하시기 바랍니다. "+add_msg;
	        			
	        			String mmdd = DateTime.getFormatString("MMdd");
	        			if( "1219".equals(mmdd) && "2".equals(mem_div) )  
	        				sms_msg = LGD_BUYER+ "님 "+mem_div_nm+" 교육비 결제가 완료되었으니 나의강의실에서 시작하기 클릭 후 반드시 "+end_dd+"까지 수강 완료하시기 바랍니다. 반드시 당해 연도 이내에 수강 완료하셔야 합니다.";
	        			sendMMS( subject, sms_msg, user_nm , tel_no );
             		}catch(Exception sse){sse.printStackTrace(); }
             		
             	}catch(Exception e){
             		try
    				{
             			isDBOK = false;
             			interact.rollbackTransaction();
    					input.put("cmd" , "UPDATE_PAYMENT_STATUS");
    			        input.put("status" , "96");
    					input.put("LGD_PAYKEY" ,LGD_PAYKEY);
    					output = interact.execute("/payment/PaymentMgr", input);
    				} catch(Exception ex2)
    				{}
             		LGD_RESPMSG = "[DB처리실패]오류로 인한 DB등록취소처리가 정상적으로 완료 되었습니다.";
             	}
             	
             	//최종결제요청 결과 성공 DB처리 실패시 Rollback 처리
             	if( !isDBOK ) {
             		setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[LGD_RESPCODE:"+LGD_RESPCODE.trim()+"]"+"[PAYLES_95] DB처리 실패:" +LGD_RESPCODE.trim());

             		LGD_RESPMSG = "[결제실패]오류로 인한 DB등록취소처리가 정상적으로 완료 되었습니다.";
                     
                    input.put("cmd" , "UPDATE_PAYMENT_STATUS");
			        input.put("status" , "95");
					input.put("LGD_PAYKEY" ,LGD_PAYKEY);
					output = interact.execute("/payment/PaymentMgr", input);
             	}
             	
            		
        	}else if( "C".equals( LGD_CASFLAG.trim() ) ) {
 	            /*
    	         * 무통장 입금취소 성공 결과 상점 처리(DB) 부분
        	     * 상점 결과 처리가 정상이면 "OK"
            	 */    
            	//if( 무통장 입금취소 성공 상점처리결과 성공 ) 
        		setLog( LGD_OID,  LGD_BUYERID, LGD_TID,  "무통장 결제 취소요청이 완료" );
                try
                {
                	String user_lecture_seq_no = output.getText("user_lecture_seq_no");
                	if(user_lecture_seq_no == null || "".equals(user_lecture_seq_no))
             			throw new Exception();
                	
                	input.put("user_lecture_seq_no" , user_lecture_seq_no);
                	
                	interact.beginTransaction();
                	input.put("cmd" , "UPDATE_PAYMENT_CANCEL");
        	        input.put("status" , "99");
        			input.put("LGD_TID" ,LGD_TID);
        			output = interact.execute("payment/PaymentMgr", input);

        			input.put("cmd" , "UPDATE_USER_LECTURE_STATUS");
        	        input.put("status" , "99");
        			output = interact.execute("payment/PaymentMgr", input);

        			input.put("cmd" , "UPDATE_USER_CLASS_STATUS");
        	        input.put("status" , "9");
        			output = interact.execute("payment/PaymentMgr", input);

        			interact.commitTransaction();
                }catch(Exception e){                	
                	try
    				{
             			interact.rollbackTransaction();
    					input.put("cmd" , "UPDATE_PAYMENT_STATUS");
    			        input.put("status" , "97");
    					input.put("LGD_PAYKEY" ,LGD_PAYKEY);
    					output = interact.execute("/payment/PaymentMgr", input);
    				} catch(Exception ex2)
    				{}
                	LGD_RESPMSG = "결제완료 후 취소 상태 적용 실패[관리자에게 문의하십시요]";
                }
        	}
        } else { //결제가 실패이면
            /*
             * 거래실패 결과 상점 처리(DB) 부분
             * 상점결과 처리가 정상이면 "OK"
             */  
           //if( 결제실패 상점처리결과 성공 ) 
        	   
        	   setLog(LGD_OID,LGD_BUYERID,LGD_PAYKEY,"[RESCODE:"+LGD_RESPCODE.trim()+"]"+"[PAYLES_94] 결제 실패 DB처리 시작");
          			input.put("cmd" , "UPDATE_PAYMENT_STATUS");
			        input.put("status" , "94");
					input.put("LGD_PAYKEY" ,LGD_PAYKEY);
					output = interact.execute("/payment/PaymentMgr", input);
			
					LGD_RESPMSG = "[결제실패]결제 실패 DB처리 가 정상적으로 완료 되었습니다.";
        }
    } else { //해쉬값이 검증이 실패이면
        /*
         * hashdata검증 실패 로그를 처리하시기 바랍니다. 
         */      
         
        input.put("cmd" , "UPDATE_PAYMENT_STATUS");
        input.put("status" , "90");
 		input.put("LGD_PAYKEY" ,LGD_PAYKEY);
 		output = interact.execute("/payment/PaymentMgr", input);
 		
 		LGD_RESPMSG = "결제결과 상점 DB처리(LGD_CASNOTEURL) 해쉬값 검증이 실패하였습니다.";     
    }
}else {
	input.put("cmd" , "UPDATE_PAYMENT_STATUS");
       input.put("status" , "91");
		input.put("LGD_PAYKEY" ,LGD_PAYKEY);
		output = interact.execute("/payment/PaymentMgr", input);
		
		LGD_RESPMSG = "결제결과 초기 저장 정보 조회 실패.";     
}
   
out.println(LGD_RESPMSG);    
%>

