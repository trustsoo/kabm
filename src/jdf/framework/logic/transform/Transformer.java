package jdf.framework.logic.transform;

import jdf.framework.core.data.DataSet;

import java.io.OutputStream;
import java.io.Writer;


/**
 * DataSet을 원하는 OutputStream에 변환하여 기록한다.
 * 
 * @see jdf.framework.logic.transform.TransformFactory
 * 
 * @author
 * @version 1.0
 * @since 2003-12-17 오전 9:24:12
 *
 */
public interface Transformer
{
    
    public void setDefaultEncoding(String encoding);
    
    
    /**
     * OutputStream을 통해 변환/출력한다.
     * 
     * 
     * @param source
     * @param out
     * @return
     * @throws TransformerException
     */
    public int transform(DataSet source, OutputStream out) throws TransformerException;

    
    /**
     * 
     * Writer를 통해 변환/출력한다.
     * 
     * @param source
     * @param writer
     * @return
     * @throws TransformerException
     */
    public int transform(DataSet source, Writer writer) throws TransformerException;
    
    /**
     * 변환 문서의 content type 리턴
     * 
     * @return
     */
    public String getContentType();
}