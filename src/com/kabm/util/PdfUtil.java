/**
 * 
 */
package com.kabm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;
import jdf.framework.core.Config;
import jdf.framework.core.Configuration;

/**
 * @author trustsoo
 *
 */
public class PdfUtil extends InteractionUtil{
		
	private static String CLASS_NAME="<at:PdfUtil> ";
	private static String ORG_PATH="";
	private static String MAKE_PATH="";
	private static String FONT_PATH="";
		
    public static void main( String[] args ) throws Exception {
    	
    	
    }
    
    static{
    	try
		{
    		Config conf = Configuration.lookup("/pdf");
			if( conf != null)
			{
				ORG_PATH = conf.getString("basedir");
				MAKE_PATH = conf.getString("makedir");
				FONT_PATH = conf.getString("fontpath");
			}
		}catch(Exception e){}
    	
    }
    
    public static String getPdfFile() throws Exception 
    {
    	String saved_file = null;
    	try {
    		File df = new File(MAKE_PATH );
		    if( !df.exists() ) df.mkdirs();
		    
		    saved_file = MAKE_PATH + File.separator  + UUID.randomUUID().toString() + ".pdf";
		    
    	} catch (Exception e) {
  	      e.printStackTrace();
  	    } 
    	
    	return saved_file;
    }
    
    public static PdfStamper getPdfContentStamper(String ORG_PDF, String saved_file) throws Exception 
    {
    	PdfReader pdfReader = new PdfReader(ORG_PATH + File.separator + ORG_PDF);
		      
	    File f = new File(saved_file);
	    PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(f));
		    
    	return pdfStamper;
    }
    
    public static PdfContentByte getPdfContentByte(PdfStamper pdfStamper) throws Exception 
    {
    	PdfContentByte content = null;
    	content = pdfStamper.getOverContent(1);
    	
    	return content;
    }
    
    public static void setQrCode(PdfContentByte content, String qr, float[] qrarr) throws Exception
    {
    	float pos_x = qrarr[0];
		float pos_y = qrarr[1];
		float width = qrarr[2];
		float height = qrarr[3];

		// 힌트 설정: UTF-8 인코딩 지정 (핵심!)
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  // 한글 깨짐 해결
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);  // 오류 수정 레벨 (선택)

    	BarcodeQRCode qrcode = new BarcodeQRCode(qr.trim(), 0, 0, hints);
        Image qrcodeImage = qrcode.getImage();
        qrcodeImage.setAbsolutePosition(pos_x, pos_y);
        qrcodeImage.scaleAbsolute(width, height);
        //qrcodeImage.scalePercent(300);
        qrcodeImage.setSpacingBefore(0);
        qrcodeImage.setSpacingAfter(0);
        content.addImage(qrcodeImage);
    }
    
    public static void setAbsText(PdfContentByte cb, String text , float[] qrarr, String FONT) throws Exception
    {    	   
    	float pos_x = qrarr[0];
		float pos_y = qrarr[1];
		float fsize = qrarr[2];
		 
		BaseFont fb = BaseFont.createFont(FONT_PATH + File.separator + FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		
		cb.saveState();
        cb.beginText();
        cb.moveText(pos_x, pos_y);
        cb.setFontAndSize(fb, fsize);
        cb.setColorFill(new BaseColor(0x00,0x00,0x00));
        cb.showText(text);
        cb.endText();
        cb.restoreState();
        
    }
    
    public static void setBlockText(PdfContentByte cb, String text , float[] qrarr, String FONT) throws Exception
    {    	   
    	float pos_x = qrarr[0];
		float pos_y = qrarr[1];
		float fsize = qrarr[2];
		
		text = getBrokenWordsForPdfGeneration(text , 20);
		
		BaseFont fb = BaseFont.createFont(FONT_PATH + File.separator + FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font font = new Font(fb, fsize);
		Chunk ck = new Chunk(text, font);
		Paragraph pg = new Paragraph(ck);
		pg.setKeepTogether(false);
		
		ColumnText ct = new ColumnText(cb);
		Phrase phrase = new Phrase(pg);
		
		ct.setSimpleColumn(phrase, pos_x, pos_y, pos_x+350, pos_y+30, 15, Element.ALIGN_LEFT);
		
		ct.go(); 
    }
    
    public static String getBrokenWordsForPdfGeneration(String longString, int cutIndex){
    	String result = "";
    	
    	boolean isok = true;
    	String[] arr = longString.split(" ");
    	int size = 0;
    	for(int idx = 0 ; idx< arr.length ; idx++)
    	{
    		size += arr[idx].length();
    		
    		if( isok && size >= cutIndex)
    		{	
    			result += "\n" + arr[idx] + " ";
    			isok = false;
    		}else result += arr[idx] + " ";
    	}
    	
    	
    	return result;
        
    }
}
