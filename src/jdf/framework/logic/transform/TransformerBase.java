/*
 * Created on 2004-06-17
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.transform;

import jdf.framework.core.data.DataSet;

import java.io.*;


/**
 * Transformer Interface 를 1차적으로 구현한 class 이 class 를 상속받으면 transform(DataSet
 * source, OutputStream out) 또는 transform(DataSet source, Writer writer) 중 하나만
 * 구현하면 된다.
 * 
 * @author
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TransformerBase implements Transformer {

	protected String defaultEncoding = "utf-8";

	/**
	 * 기본 생성자
	 */
	public TransformerBase() {
		super();

	}

	/**
	 * transformer 의 content type을 반환한다. 기본은 문자형으로 text/txt; charset=euc-kr 과
	 * 같다.
	 */
	public String getContentType() {
		return "text/txt; charset="+defaultEncoding;
	}

	public void setDefaultEncoding(String encoding) {
		this.defaultEncoding = encoding;
	}

	/**
	 * 실제 변환 메쏘드 반환 숫자는 총 반환된 문자의 길이수이다.
	 * 
	 * @see Transformer#transform(DataSet,
	 *      OutputStream)
	 */
	public int transform(DataSet source, OutputStream out) throws TransformerException {

		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(out, defaultEncoding));
			return transform(source, writer);
		} catch (TransformerException te) {
			throw te;
		} catch (UnsupportedEncodingException ue) {
			throw new TransformerException(ue.toString());
		}  finally
		{
			if(writer != null) try{writer.close();}catch(Exception ex){}
		}

	}

	/**
	 * 실제 변환 메쏘드
	 * 
	 * @see Transformer#transform(DataSet,
	 *      Writer)
	 */
	public int transform(DataSet source, Writer writer) throws TransformerException {
		try {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();

			int re = transform(source, bs);
			writer.write(bs.toString());

			return re;
		} catch (IOException e) {
			throw new TransformerException("transform error", e);
		}
	}
	

}