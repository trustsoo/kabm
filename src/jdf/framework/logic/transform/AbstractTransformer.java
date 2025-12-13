/*
 * Created on 2004-06-17
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.transform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import jdf.framework.core.data.DataSet;


/**
 * Transformer Interface 를 1차적으로 구현한 추상 class
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class AbstractTransformer implements Transformer
{

	/**
	 * 기본 생성자
	 */
	public AbstractTransformer()
	{
		super();
		
	}
    
    /**
     * transformer 의 content type을 반환한다.
     * 기본은 문자형으로 text/txt; charset=euc-kr 과 같다.
     */
    public String getContentType()
    {
        return "text/txt; charset=euc-kr";
    }

	/**
	 * 실제 변환 메쏘드
	 * 반환 숫자는 총 반환된 문자의 길이수이다.
     * 
	 * @see jdf.framework.logic.transform.Transformer#transform(jdf.framework.core.data.DataSet, java.io.OutputStream)
	 */
	abstract public int transform(DataSet source, OutputStream out) throws TransformerException;
	
	
	
	
	/**
	 * 실제 변환 메쏘드
	 * 
	 * @see jdf.framework.logic.transform.Transformer#transform(jdf.framework.core.data.DataSet, java.io.Writer)
	 */
	public int transform(DataSet source, Writer writer) throws TransformerException
	{
		try
		{
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
            
			int re = transform(source, bs);
			writer.write(bs.toString());

			return re;
		}
		catch (IOException e)
		{
			throw new TransformerException("transform error",e);
		}
	}

}
