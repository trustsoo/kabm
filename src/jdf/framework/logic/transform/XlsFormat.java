package jdf.framework.logic.transform;

import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.FieldType;
import jdf.framework.core.io.StreamUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


public class XlsFormat
{
	
	public final static String IS_MAKE_XLS_FILE = "isXLS";
	public final static String IS_MAKE_CSV_FILE = "isCSV";
	public final static String BLOKNAMES = "blockNames";
	public final static String XLS_SHEET_TITLE = "xlsSheetTitle";
	public final static String XLS_CONDITION_TITLE = "xlsCondition";
	
	public final static String RESULT_FILE_PATH = "RESULT_FILE_PATH";
	public final static String RESULT_FILE_NAME = "RESULT_FILE_NAME";
	public final static String RESULT_FILE_SIZE = "RESULT_FILE_SIZE";
	

	public static Map<String, CellStyle> createStyles(Block[] blocks, SXSSFWorkbook wb){
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat fmt = wb.createDataFormat();
		CellStyle style =  null;
        
        //1.Cell Style
        for (int i = 0; i < blocks.length; i++) 
        {
            Field[] fields = blocks[i].getFields();
            for(int p=0; p<fields.length; p++)
            {
            	style = wb.createCellStyle();
            	
            	String align = fields[p].getProperty("align") == null ? "" : fields[p].getProperty("align") ;
            	String dataFormat = fields[p].getProperty("dataFormat") == null ? "" : fields[p].getProperty("dataFormat");
            	
            	
            	style.setAlignment(align.equals("left") || align.equals("") ? XSSFCellStyle.ALIGN_LEFT : align.equals("center") ? XSSFCellStyle.ALIGN_CENTER : XSSFCellStyle.ALIGN_RIGHT);
            	
            	if("".equals(dataFormat))
            	{
	            	int ftype = fields[p].getType();
	                if(ftype == FieldType.INTEGER || ftype == FieldType.FLOAT || ftype == FieldType.DOUBLE  )
					{
	                	jxl.write.Number numberCell = null;
	                	if(ftype == FieldType.INTEGER)
	                	{
	                		style.setDataFormat(fmt.getFormat("#,##0"));
	                	}else if(ftype == FieldType.FLOAT)
	                	{	
	                		style.setDataFormat(fmt.getFormat("#,##0.00"));
	                	}else if(ftype == FieldType.DOUBLE)
	                	{	
	                		style.setDataFormat(fmt.getFormat("#,##0.00"));
	                	}
					}
	             } else
	             {
	            	 style.setDataFormat(fmt.getFormat(dataFormat));
	             }
                
                style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                style.setBorderTop(HSSFCellStyle.BORDER_THIN);	        
                style.setBottomBorderColor(HSSFColor.BLACK.index);
    	        style.setTopBorderColor(HSSFColor.BLACK.index);
    	        style.setLeftBorderColor(HSSFColor.BLACK.index);
    	        style.setRightBorderColor(HSSFColor.BLACK.index);
                
                
        		styles.put(i+"_"+p, style);
            	//style.setDataFormat();
            }
        }
        
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);	        
        headerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        headerStyle.setTopBorderColor(HSSFColor.BLACK.index);
        headerStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        headerStyle.setRightBorderColor(HSSFColor.BLACK.index);	        
        
        styles.put("header", headerStyle);

        return styles;
    }
	
	public static void substitute(File zipfile, Map<String, File> sheets, OutputStream out) throws IOException {
	    ZipFile zip = null;
	    ZipOutputStream zos = null;
	    		
	    try
	    {
	        zip = new ZipFile(zipfile);        		
	        zos = new ZipOutputStream(out);
	 
	        @SuppressWarnings("unchecked")
	        Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
	        while (en.hasMoreElements()) {
	            ZipEntry ze = en.nextElement();
	            
	            if(!sheets.containsKey(ze.getName())){
	                zos.putNextEntry(new ZipEntry(ze.getName()));
	                InputStream is = zip.getInputStream(ze);
	                StreamUtil.copy(is, zos);
	                is.close();
	            }
	        }
	 
	        for (Map.Entry<String, File> entry : sheets.entrySet()) {
	            
	            zos.putNextEntry(new ZipEntry(entry.getKey()));
	            InputStream is = new FileInputStream(entry.getValue());
	            StreamUtil.copy(is, zos);
	            is.close();
	        }
	        zos.close();
	    } catch(IOException ex)
	    {
	    	throw ex;
	    } finally
	    {
	    	if (zos != null) try{zos.close();}catch(Exception ex){};
	        if (zip != null) try{zip.close();}catch(Exception ex){};
	    }
	}

	public static String stripNonValidXMLCharacters(String in) {

		StringBuffer out = new StringBuffer(); // Used to hold the output.

		char current; // Used to reference the current character.



		if (in == null || ("".equals(in))) {

		return "";

		} // vacancy test.

		for (int i = 0; i < in.length(); i++) {

		current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen. 

		if ((current == 0x9) ||

		(current == 0xA) ||

		(current == 0xD) ||

		((current >= 0x20) && (current <= 0xD7FF)) ||

		((current >= 0xE000) && (current <= 0xFFFD)) ||

		((current >= 0x10000) && (current <= 0x10FFFF))) {

		out.append(current);

		} else {

		out.append("_");

		}

		}



		return out.toString();

	}

	
}