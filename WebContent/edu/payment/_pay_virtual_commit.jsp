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
    
    String LGD_RESPMSG = "시작실패";
    String LGD_RESPCODE = "0000";
    String LGD_OID = request.getParameter("LGD_OID");        // 주문번호
    String LGD_CASFLAG = request.getParameter("LGD_CASFLAG"); 
    if( LGD_CASFLAG == null || "".equals(LGD_CASFLAG)) LGD_CASFLAG = "I";
    
    DataSet output = new DataSet();
    InteractionBean interact = new InteractionBean();
    
    input.put("cmd" , "GET_PAYMENT_INFO_BY_OID");
	input.put("LGD_OID" ,LGD_OID);
	output = interact.execute("/payment/PaymentMgr", input);
	
	String LGD_TID = output.getText("LGD_TID");
	String LGD_PAYKEY = output.getText("LGD_PAYKEY");
	String user_seq_no = output.getText("user_seq_no");
	String lecture_seq_no = output.getText("lecture_seq_no");
	String tel_no = output.getText("tel_no");
	String user_nm = output.getText("user_nm");
	String status = output.getText("status");
	
if( output.getCount("LGD_PAYKEY") > 0 && !"".equals(user_seq_no) 
		&& !"".equals(lecture_seq_no) && !"".equals(LGD_PAYKEY)
		&& !"03".equals(status) 
		)
{
			input.put("LGD_PAYKEY",LGD_PAYKEY);
			input.put("user_seq_no",user_seq_no);
			input.put("lecture_seq_no",lecture_seq_no);
        	
    		LGD_RESPMSG = "[결제최초요청]결제요청 정보를 수록하였습니다."; 
    	        
        	if( "R".equals( LGD_CASFLAG.trim() ) ) {
                
                	 input.put("cmd" , "UPDATE_PAYMENT");
		             input.put("status" , "02");
		             input.put("LGD_PAYKEY" ,LGD_PAYKEY);
		             input.put("LGD_RESCODE" ,LGD_RESPCODE.trim());
		             input.put("LGD_RESPMSG" ,LGD_RESPMSG.trim());
   				 output = interact.execute("/payment/PaymentMgr", input);
   				 
   				LGD_RESPMSG = "OK";//[할당성공]결제 처리를 위한 할당이 정상적으로 완료 되었습니다.
        		
        	}else if( "I".equals( LGD_CASFLAG.trim() ) ) {
        		
             	boolean isDBOK = true; //DB처리 실패시 false로 변경해 주세요.

             	try
             	{
             		interact.beginTransaction();

             		input.put("cmd" , "INSERT_USER_LECTURE");
    		        output = interact.execute("/payment/PaymentMgr", input);
             		String user_lecture_seq_no = output.getText("user_lecture_seq_no");

             		if(user_lecture_seq_no == null || "".equals(user_lecture_seq_no))
             			throw new Exception();

             		input.put("cmd" , "INSERT_USER_CLASS");
             		input.put("user_lecture_seq_no" , user_lecture_seq_no);
    		        output = interact.execute("/payment/PaymentMgr", input);

             		input.put("cmd" , "UPDATE_PAYMENT_LECTURE");
    		        input.put("status" , "03");
    		        input.put("LGD_OID" ,LGD_OID);
    				input.put("LGD_TID" ,LGD_TID);
    				output = interact.execute("/payment/PaymentMgr", input);
    				
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
	        			String sms_msg = user_nm+ "님 "+mem_div_nm+" 교육비 결제가 완료되었으니 나의강의실에서 시작하기 클릭 후 반드시 2주(14일) 이내로 수강 완료하시기 바랍니다. "+add_msg;
	        			
	        			String mmdd = DateTime.getFormatString("MMdd");
	        			if( "1219".equals(mmdd) && "2".equals(mem_div) )  
	        				sms_msg = user_nm+ "님 "+mem_div_nm+" 교육비 결제가 완료되었으니 나의강의실에서 시작하기 클릭 후 반드시 "+end_dd+"까지 수강 완료하시기 바랍니다. 반드시 당해 연도 이내에 수강 완료하셔야 합니다.";
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
        
   
}
   
out.println(LGD_RESPMSG);    
%>

