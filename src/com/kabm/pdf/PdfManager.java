/**
 * 
 */
package com.kabm.pdf;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfStamper;
import com.kabm.util.InteractionUtil;
import com.kabm.util.PdfUtil;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.log.Logger;


/**
 * @author trustsoo
 *
 */
public class PdfManager extends InteractionUtil{
	private final static String CLSS_NM = "[PdfManager]";
	
	public final static String FONT_GULIM = "ARIALN.TTF";
	public final static String FONT_BATANG = "H2GSRB.TTF";
	public final static String IST_Cyber_11 = "IST_Cyber_11";
	public final static String IST_Cyber_12 = "IST_Cyber_12";
	public final static String IST_Cyber_21 = "IST_Cyber_21_1";
	public final static String IST_Cyber_22 = "IST_Cyber_22";
	public final static String IST_Cyber_95 = "IST_Cyber_95";

	private static float[] axis_11_no = {107,717,11}; // pos_x, pos_y, font_size
	private static float[] axis_11_nm = {195,568,14};
	private static float[] axis_11_sn = {195,540,14};
	private static float[] axis_11_addr = {195,497,14};
	private static float[] axis_11_cp = {195,480,14};
	private static float[] axis_11_cpdr = {195,438,14};
	private static float[] axis_11_dt = {210,200,18};
	private static float[] axis_11_qr = {80,145,100,100}; // pos_x, pos_y, width, height
	
	private static float[] axis_12_no = {119,734,11}; // pos_x, pos_y, font_size            
	private static float[] axis_12_nm = {195,568,14};  
	private static float[] axis_12_sn = {195,540,14};   
	private static float[] axis_12_addr = {195,497,14}; 
	private static float[] axis_12_cp = {195,480,14};   
	private static float[] axis_12_cpdr = {195,438,14};
	private static float[] axis_12_dt = {210,200,18};   
	private static float[] axis_12_qr = {80,145,100,100}; // pos_x, pos_y, width, height    
	
	private static float[] axis_21_no = {110,717,11}; // pos_x, pos_y, font_size            
	private static float[] axis_21_nm = {195,568,14};  
	private static float[] axis_21_sn = {195,540,14};  
	private static float[] axis_21_cp =  {195,512,14}; 
	private static float[] axis_21_cpdr = {195,468,14}; 
	private static float[] axis_21_wk = {195,436,14};     
	private static float[] axis_21_yyyy = {430,344,18};
	private static float[] axis_21_dt = {210,200,18};  
	private static float[] axis_21_qr = {80,145,100,100}; // pos_x, pos_y, width, height    
	
	private static float[] axis_22_no = {119,734,11}; // pos_x, pos_y, font_size            
	private static float[] axis_22_nm = {195,568,14};  
	private static float[] axis_22_sn = {195,540,14};   
	private static float[] axis_22_cp = {195,512,14};   
	private static float[] axis_22_cpdr = {195,468,14};  
	private static float[] axis_22_wk = {195,436,14};   
	private static float[] axis_22_dt = {210,200,18};    
	private static float[] axis_22_qr = {80,145,100,100}; // pos_x, pos_y, width, height 
	
	private static float[] axis_95_no = {370,315,11}; // pos_x, pos_y, font_size            
	private static float[] axis_95_nm = {80,236,20};  
	private static float[] axis_95_amko = {120,195,20};
	private static float[] axis_95_amno = {330,195,20};
	private static float[] axis_95_yyyy = {272,108,12};
	private static float[] axis_95_mm = {330,108,12};
	private static float[] axis_95_dd = {370,108,12};
	
	public static void main(String args[] )
	{		
		
		try
		{
			DataSet input = new DataSet();
			input.put("no", "42752-87660");
			input.put("qr", "12227009041452910최성수제이와이시스템0245689542대전광역시 서구 유등로17번길 109 초록마을아파트 507동 202호수제이와이시스템0245689542대전광역시 서구 유등로1");
			input.put("user_nm", "홍민호");
			input.put("ssn", "731103-1******");
			input.put("home_addr", "대전광역시 서구 유등로17번길 109 초록마을아파트 507동 202호");
			input.put("wk_place", "한넷데이타시스템 빌딩");
			input.put("std_yyyy", "2020년도");
			
			input.put("corp_nm", "제이와이시스템(주)");
			input.put("corp_addr", "대전광역시 서구 복수동로73번길 25, 2층 203호 (금암빌딩)");
			input.put("today", "2017 년 01 월 17 일");
			
			input.put("am_ko", "삼만오천");
			input.put("am_no", "35,000");
			input.put("yyyy", "2017");
			input.put("mm", "05");
			input.put("dd", "17");
			PdfManager.excuteProcess(IST_Cyber_21 , input);
		}catch(Exception e){e.printStackTrace(); }	
		
	}
	
	public static String excuteProcess(String pdfDiv, DataSet input) throws Exception
	{
		String saved_file = null;
		PdfStamper pdfStamper = null;
		PdfContentByte content = null;
		
		try
		{				
			Logger.info.println("[excuteProcess]:" +" start! :"  );
			saved_file = PdfUtil.getPdfFile();
			pdfStamper = PdfUtil.getPdfContentStamper(pdfDiv + ".pdf", saved_file);
			content = PdfUtil.getPdfContentByte(pdfStamper);
			
			if( IST_Cyber_11.equals(pdfDiv) )
			{				
				PdfUtil.setAbsText(content, input.getText("no"), axis_11_no, FONT_GULIM);
				PdfUtil.setAbsText(content, input.getText("user_nm"), axis_11_nm, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("ssn"), axis_11_sn, FONT_BATANG);
				PdfUtil.setBlockText(content, input.getText("home_addr"), axis_11_addr, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("corp_nm"), axis_11_cp, FONT_BATANG);
				PdfUtil.setBlockText(content, input.getText("corp_addr"), axis_11_cpdr, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("today"), axis_11_dt, FONT_BATANG);
				
				PdfUtil.setQrCode(content, input.getText("qr"), axis_11_qr);;
			}else if( IST_Cyber_12.equals(pdfDiv) ){
				PdfUtil.setAbsText(content, input.getText("no"), axis_12_no, FONT_GULIM);
				PdfUtil.setAbsText(content, input.getText("user_nm"), axis_12_nm, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("ssn"), axis_12_sn, FONT_BATANG);
				PdfUtil.setBlockText(content, input.getText("home_addr"), axis_12_addr, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("corp_nm"), axis_12_cp, FONT_BATANG);
				PdfUtil.setBlockText(content, input.getText("corp_addr"), axis_12_cpdr, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("today"), axis_12_dt, FONT_BATANG);
				
				PdfUtil.setQrCode(content, input.getText("qr"), axis_12_qr);;
				
			}else if( IST_Cyber_21.equals(pdfDiv) ){
				PdfUtil.setAbsText(content, input.getText("no"), axis_21_no, FONT_GULIM);
				PdfUtil.setAbsText(content, input.getText("user_nm"), axis_21_nm, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("ssn"), axis_21_sn, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("corp_nm"), axis_21_cp, FONT_BATANG);
				PdfUtil.setBlockText(content, input.getText("corp_addr"), axis_21_cpdr, FONT_BATANG);
				PdfUtil.setBlockText(content, input.getText("wk_place"), axis_21_wk, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("std_yyyy"), axis_21_yyyy, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("today"), axis_21_dt, FONT_BATANG);
				
				PdfUtil.setQrCode(content, input.getText("qr"), axis_21_qr);;
				
			}else if( IST_Cyber_22.equals(pdfDiv) ){
				PdfUtil.setAbsText(content, input.getText("no"), axis_22_no, FONT_GULIM);
				PdfUtil.setAbsText(content, input.getText("user_nm"), axis_22_nm, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("ssn"), axis_22_sn, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("corp_nm"), axis_22_cp, FONT_BATANG);
				PdfUtil.setBlockText(content, input.getText("corp_addr"), axis_22_cpdr, FONT_BATANG);
				PdfUtil.setBlockText(content, input.getText("wk_place"), axis_22_wk, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("today"), axis_22_dt, FONT_BATANG);
				
				PdfUtil.setQrCode(content, input.getText("qr"), axis_22_qr);;
				
			}else if( IST_Cyber_95.equals(pdfDiv) ){
				PdfUtil.setAbsText(content, input.getText("no"), axis_95_no, FONT_GULIM);
				PdfUtil.setAbsText(content, input.getText("user_nm") + "  귀하", axis_95_nm, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("am_ko"), axis_95_amko, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("am_no"), axis_95_amno, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("yyyy"), axis_95_yyyy, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("mm"), axis_95_mm, FONT_BATANG);
				PdfUtil.setAbsText(content, input.getText("dd"), axis_95_dd, FONT_BATANG);
			}
			
			Logger.info.println("[excuteProcess]:" +" end! :"  );
			
		}catch(Exception le){
			le.printStackTrace();
			Logger.err.println(CLSS_NM+"[excuteProcess]:CHANNEL-ERROR:"+le.getMessage() );
		}finally{
			if(pdfStamper != null ) pdfStamper.close();
		}
				    	
		return saved_file;
	}
	
}
