package jdf.framework.logic.transform;

import jdf.framework.core.Configuration;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.FieldType;
import jdf.framework.core.util.StringUtil;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Xls 문서로 DataSet 을 변환한다.
 * 
 * @author 
 * 
 */
public class XlsTransformerImpl extends TransformerBase
{
	private HashMap<String , WritableCellFormat> cellMap = new HashMap<String , WritableCellFormat>();
	private WritableCellFormat headerFormat = null;
	private String getHeaderTitle(Field f)
	{
		String name = f.getLabel();
		if (name == null)
			name = f.getName();

		return name;
	}
	
	public File transform(DataSet ds, String blockName, OutputStream out) throws TransformerException
	{				
		return transform(null, null, ds, blockName, out);
	}	
	
	
	public File transform(String title, String condition, DataSet ds, String blockName, OutputStream out) throws TransformerException
	{				
		return transform(title, condition, null, null, ds, blockName, out);
	}
	
	/**
	 * 엑셀파일로 변환한다.  
	 */
    public File transform(String title, String condition, WritableWorkbook workbook, File infile, DataSet ds, String blockName, OutputStream out) throws TransformerException
    {               
        Block[] blocks = ds.getIOSchema().getOutputBlocks();
        Label label =null;
        jxl.write.Blank blank=null;
        //FileInputStream fin = null;
        cellMap = new HashMap<String , WritableCellFormat>();
    	this.headerFormat = null;
    	File file = null;
        boolean isContainHeader = true;
        try
        {
        	if(title == null || title.equals("")) title = "Sheet1";
        	int l=0;
        	WritableSheet sheet = null;
        	int row_start_idx = 0;
        	int col = blocks[0].getFields().length;            
            int col_end_idx = col-1;     
        	
        	if( workbook == null )
            {
        		isContainHeader = false;
    	        String dataDir = Configuration.getConfigPath() + File.separator + "xls";
    	        File dir = new File(dataDir);
    	        if(!dir.exists()) dir.mkdirs();
    	        
    	        file = new File(dataDir+ File.separator+System.currentTimeMillis()+".xls");
    	        
    	        WorkbookSettings ws = new WorkbookSettings();
    			ws.setUseTemporaryFileDuringWrite(true);
    			
                workbook = Workbook.createWorkbook(file, ws);
                sheet = workbook.createSheet(new String(title.getBytes(defaultEncoding), defaultEncoding), 0);
                
                //STEP 1. TITLE 출력
                if(title != null && !title.equals(""))
                {
                    sheet.addCell(new Label(0, row_start_idx, title, getTitleFormat()));
                    sheet.mergeCells(0, row_start_idx, col_end_idx, row_start_idx); //col stx, row stx, col end, row end 
                    row_start_idx = row_start_idx +1;
                }
                
                //STEP 2. 조건 출력
                if(condition != null && !condition.equals(""))
                {
                    sheet.addCell(new Label(0, row_start_idx, condition, getConditionFormat()));
                    sheet.mergeCells(0, row_start_idx, col_end_idx, row_start_idx); //col stx, row stx, col end, row end 
                    row_start_idx++;
                }       
                
            }else{
            	file = infile;
            	sheet = workbook.getSheet(0);
            	row_start_idx = sheet.getRows();
            }
            

            for (int i = 0; i < blocks.length; i++) 
            {
            	if(blocks[i].getProperty("dummy") != null && "true".equals(blocks[i].getProperty("dummy"))) continue;
            	
            	Map<String, String> colspanArray = new HashMap<String, String>();
        		Map<String, String> rowsSpanArray = new HashMap<String, String>();
            	
            	if(blocks[i].getProperty("header") != null)
            	{
            		Block headerBlock = ds.getIOSchema().getBlockById(blocks[i].getProperty("header"));
            		Field[] headerFields = headerBlock.getFields();
            		//Map<String, String> colspanArray = new HashMap<String, String>();
            		//Map<String, String> rowsSpanArray = new HashMap<String, String>();
            		for (int j = 0; j < headerFields.length; j++) 
            		{
            			Field f = headerFields[j];
            			
            			sheet.addCell(new Label( j , row_start_idx, getHeaderTitle(f), getHeaderFormat("")));
            			
            			if(f.getProperty("colspan") != null)
            				colspanArray.put(j+"", f.getProperty("colspan"));
            			
            			if(f.getProperty("rowspan") != null)
            				rowsSpanArray.put(j+"", f.getProperty("rowspan"));
            			
            		}
            		
            		
            		Iterator<String> ite1 = colspanArray.keySet().iterator();
            		while(ite1.hasNext())
            		{
            			String key = ite1.next();
            			String val = colspanArray.get(key);
            			sheet.mergeCells(Integer.parseInt(key), row_start_idx, Integer.parseInt(key) + Integer.parseInt(val), row_start_idx); //col stx, row stx, col end, row end 
            		}
            		
            		
            		row_start_idx++;
            		
            	}
            	
            	
                Field[] fields = blocks[i].getFields();
                if (fields.length > 0) 
                {
                    String fisrtKeyName = null;
                                        
                    //STEP 3. 헤더 출력
                    for (int j = 0; j < fields.length; j++) 
                    {
                        Field f = fields[j];
                        if (j == 0)
                            fisrtKeyName = f.getName();
                        if( !isContainHeader )
                        {
                        	if(!rowsSpanArray.containsKey(j+""))
                        		sheet.addCell(new Label( j , row_start_idx, getHeaderTitle(f), getHeaderFormat("")));
                        	else
                        		sheet.addCell(new Label( j , row_start_idx, ""));
                        }
                        
                        
                        Iterator<String> ite1 = rowsSpanArray.keySet().iterator();
                		while(ite1.hasNext())
                		{
                			String key = ite1.next();
                			String val = rowsSpanArray.get(key);                			
                			sheet.mergeCells(Integer.parseInt(key), row_start_idx-Integer.parseInt(val), Integer.parseInt(key), row_start_idx); //col stx, row stx, col end, row end 
                			sheet.getWritableCell(Integer.parseInt(key), row_start_idx-Integer.parseInt(val)).setCellFormat(getHeaderFormat(""));
                		}
                		
                        
                        
                    }
                    
                    if( !isContainHeader )
                    	row_start_idx++;
                    	
                    // STEP 4. 테이블 내용 출력
                    int dataCount = ds.getCount(fisrtKeyName);
                    String bgcolor = "";
                    String colorOnly = "";                    
                    String value = "";
                    String align = "";
                    HashMap<String,String> attrMap = null; 
                    
                    int m = row_start_idx;
                    for (int k = 0; k < dataCount; k++) 
                    {
                    	if(k % 60000 == 0 && k > 0)
					    {
					        l++;
					        workbook.createSheet( (new String(title.getBytes(defaultEncoding), defaultEncoding) ) +( l+1),l);
					        m = 0;
					    }
						sheet = workbook.getSheet(l);
						
                        for (int p = 0; p < fields.length; p++) 
                        {   
                            bgcolor = "";
                            colorOnly = "";
                            align = "";
                            value = ds.getText(fields[p].getName(),k);
                            attrMap = new HashMap(); 
                            
                            try
                            {
                                //배경색을 지정한경우는 값이 있는경우만 색을 지정한다.
                                if(!value.equals("") && !value.equals("."))   bgcolor =  StringUtil.nvl(fields[p].getProperty("background-color")).toLowerCase();
                               
                                colorOnly =  StringUtil.nvl(fields[p].getProperty("background-color-only"));
                               if(colorOnly.toUpperCase().equals("YES") || colorOnly.toUpperCase().equals("Y")) value = "";
                               align = StringUtil.nvl(fields[p].getProperty("align"));
                                
                               attrMap.put("bgcolor", bgcolor);
                               attrMap.put("align", align);
                               
                               
                            }catch(Exception e)   {    }
                            
                            int ftype = fields[p].getType();
                            if(ftype == FieldType.INTEGER || ftype == FieldType.FLOAT || ftype == FieldType.DOUBLE  )
							{
                            	jxl.write.Number numberCell = null;
                            	if(ftype == FieldType.INTEGER)
                            	{	
                            		int val = ds.getInt(fields[p].getName(),k);
                            		numberCell = new jxl.write.Number(p, m, val, getBodyFormat(attrMap, ftype, fields[p].getFormat() ,p));
                            	}else if(ftype == FieldType.FLOAT)
                            	{	
                            		float val = ds.getFloat(fields[p].getName(),k);
                            		numberCell = new jxl.write.Number(p, m, val, getBodyFormat(attrMap, ftype, fields[p].getFormat() ,p));
                            	}else if(ftype == FieldType.DOUBLE)
                            	{	
                            		double val = ds.getDouble(fields[p].getName(),k);
                            		numberCell = new jxl.write.Number(p, m, val, getBodyFormat(attrMap, ftype, fields[p].getFormat() ,p));
                            	}
								sheet.addCell(numberCell);
							}else{
								value = setHTMLFormat(value);	                            
								sheet.addCell(new Label( p , m, value , getBodyFormat(attrMap, fields[p].getType(), fields[p].getFormat() ,p)));
	                            
							}
                        }
                        
                        // STEP 5. 컬럼길이 설정
                        if( k== 0 || m ==0 ) setColoumWidth(fields, sheet, col);                        
                        
                        m++;
                    }
                }
            }
            
            // STEP 6. 엑셀출력
            workbook.write();
            workbook.close();
            
            //fin = new FileInputStream(file);
            
            
        
        } catch(Exception ex)
        {
            ex.printStackTrace();
        } 
        
        return file;        
    }  

    /**
     * 타이틀 포멧을 가져온다.
     * @return
     */
    public WritableCellFormat getTitleFormat()
    {
        WritableFont.FontName gulim = WritableFont.createFont("굴림");
        WritableFont gulim11Bold = new WritableFont(gulim, 10, WritableFont.BOLD);
        WritableCellFormat gulim11Boldformat = new WritableCellFormat(gulim11Bold);
        try
        {
            gulim11Boldformat.setAlignment(Alignment.CENTRE);
            gulim11Boldformat.setVerticalAlignment(VerticalAlignment.CENTRE);
            gulim11Boldformat.setWrap(true);            

        } catch (WriteException e)
        {
            e.printStackTrace();
        }
        return gulim11Boldformat;
    }
    
    

    /**
     * 조건 포멧을 가져온다.
     * @return
     */
    public WritableCellFormat getConditionFormat()
    {
        WritableFont.FontName gulim = WritableFont.createFont("굴림");
        WritableFont gulim11Bold = new WritableFont(gulim, 10, WritableFont.NO_BOLD);
        WritableCellFormat gulim11Boldformat = new WritableCellFormat(gulim11Bold);
        try
        {
            gulim11Boldformat.setAlignment(Alignment.RIGHT);
            gulim11Boldformat.setVerticalAlignment(VerticalAlignment.CENTRE);
            gulim11Boldformat.setWrap(true);            
        } catch (WriteException e)
        {
            e.printStackTrace();
        }
        return gulim11Boldformat;
    }
    
	
    

    /**
     * 테이블헤더 포멧을 가져온다.
     * @return
     */
    public WritableCellFormat getHeaderFormat(String color)
    {
    	if( headerFormat != null) return headerFormat;
    	
        WritableFont.FontName gulim = WritableFont.createFont("굴림");

        WritableFont gulim11Bold = new WritableFont(gulim, 10, WritableFont.NO_BOLD);
        WritableCellFormat gulim11Boldformat = new WritableCellFormat(gulim11Bold);
        try
        {

            if(color == null || color.equals(""))
            	gulim11Boldformat.setBackground( Colour.GRAY_25);  
            
            gulim11Boldformat.setBorder(Border.ALL, BorderLineStyle.THIN);            
            gulim11Boldformat.setAlignment(Alignment.CENTRE);
            gulim11Boldformat.setVerticalAlignment(VerticalAlignment.CENTRE);
            gulim11Boldformat.setWrap(true);            
        } catch (WriteException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        headerFormat = gulim11Boldformat;
        
        return headerFormat;
    }    
    
    
    /**
     * 테이블 본문 포멧을 가져온다.
     * @return
     */
    public WritableCellFormat getBodyFormat(HashMap<String,String> attrMap)
    {
        return getBodyFormat(attrMap , FieldType.STRING , "", 0);
    }        
    
    /**
     * 테이블 본문 포멧을 가져온다.
     * @return
     */
    public WritableCellFormat getBodyFormat(HashMap<String,String> attrMap, int ftype, String format, int p)
    {
        
    	
    	
        WritableCellFormat writablecellformat = null;
        if( cellMap.get("F"+p ) == null )
        {
        	WritableFont.FontName gulim = WritableFont.createFont("굴림");
            WritableFont gulim11Bold = new WritableFont(gulim, 10, WritableFont.NO_BOLD);
            
            if( ftype == FieldType.STRING )writablecellformat =  new WritableCellFormat(gulim11Bold) ;
            else if( ftype == FieldType.FLOAT )writablecellformat =  new WritableCellFormat(NumberFormats.FLOAT) ;
            else if( ftype == FieldType.DOUBLE )writablecellformat =  new WritableCellFormat(NumberFormats.FLOAT) ;
            else if( ftype == FieldType.INTEGER )writablecellformat =  new WritableCellFormat(NumberFormats.INTEGER) ;
            else writablecellformat =  new WritableCellFormat(gulim11Bold) ;
            
            try
            {
            	if(ftype != FieldType.STRING) 
            	{
            		NumberFormat fivedps = null;
            		if(format != null && !"".equals(format)) 
            		{	
            			try{
            				fivedps = new NumberFormat(format);
            			}catch(Exception e){};
            			if( fivedps != null) 
            			{
            				writablecellformat =  new WritableCellFormat(fivedps) ;
            			}
            		}
            	}
            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        	
        	
        	String bgcolor = attrMap.get("bgcolor");
            String align = attrMap.get("align");               
            try
            {
	            if(bgcolor != null && !bgcolor.equals("")) writablecellformat.setBackground(XlsColor.getColur( bgcolor ));
	            //writablecellformat.setWrap(true);   
	            writablecellformat.setBorder(Border.ALL, BorderLineStyle.THIN);  
	            writablecellformat.setAlignment(  align.equals("left") || align.equals("")? Alignment.LEFT : align.equals("center") ? Alignment.CENTRE : Alignment.RIGHT );
	            writablecellformat.setVerticalAlignment(VerticalAlignment.CENTRE);
            } catch (Exception e)
            {
            	e.printStackTrace();
            }
        	
        	cellMap.put("F"+p , writablecellformat );
        }else{
        	writablecellformat = (WritableCellFormat)cellMap.get("F"+p);
        }
        
        return writablecellformat;
    }        

	
    
    public void setColoumWidth(Field[] fields, WritableSheet sheet, int col)
    {
    	for (int j = 0; j < col; j++) 
        {
            Field f = fields[j];
            int width = 0;
            try
            {
               width = Integer.parseInt( f.getProperty("width"));
            }catch(Exception e)   {    }
            if(width != 0)
            {
                sheet.setColumnView(j, width);
            }
        } 
    }	
    
    private String setHTMLFormat(String data) {
		String clean = "";
		try
		{
			clean = data.replaceAll("&quot;", "\"").replaceAll("&#039;","\'");
			clean = clean.replaceAll("&#043;", "\\+");
			clean = clean.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
	        clean = clean.replaceAll("&#40;", "\\(").replaceAll("&#41;", "\\)");
	        
		} catch(Exception ex)
		{
			clean = data;
		}
		
		return clean;

	}
	
}