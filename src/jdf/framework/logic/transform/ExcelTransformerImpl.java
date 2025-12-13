package jdf.framework.logic.transform;

import java.io.IOException;
import java.io.Writer;

import jdf.framework.core.data.DataSet;



/**
 * Excel 타입으로 데이터 변환을 한다.
 * 실제 Excel binary 타입은 아니고, html 기반의 excel 문서로 변환한다.
 * 
 * 
 * 
 * @author
 *
 */
public class ExcelTransformerImpl extends HtmlTransformerImpl
{
    
    
    public ExcelTransformerImpl()
    {
        super();
    }

    public String getContentType()
    {
        return "application/vnd.ms-excel; charset="+defaultEncoding;
    }
    
    protected void writerHeader(DataSet ds, Writer writer) throws IOException
    {

        writer.write("<html>");
        
        writer.write("<head>");
        writer.write("<title>result</title>");
        //writer.write("<link rel='stylesheet' type='text/css' href='/tables.css'>");
        writer.write("</head>");

    }
}
