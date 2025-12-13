package jdf.framework.logic.transform;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Writer;

import jdf.framework.core.data.DataSet;


/**
 * java Object serialize 용 데이터 변환기
 * 
 * @author
 * 
 */
public class ObjectTransformerImpl implements Transformer {
	/**
	 * 기본생성자
	 * 
	 */
	public ObjectTransformerImpl() {
	}

	
	/**
	 * encoding 정보 설정
	 */
	public void setDefaultEncoding(String encoding) {
		return;

	}

	/**
	 * DataSet 를 Object Stream으로 변환시킨다.
	 * 
	 */
	public int transform(DataSet ds, OutputStream out) throws TransformerException {
		try {

			ObjectOutputStream oos = new ObjectOutputStream(out);

			oos.writeObject(ds);

			return 0;
		} catch (IOException ioe) {
			throw new TransformerException("object transform error", ioe);

		}
	}

	/**
	 * @see jdf.framework.logic.transform.Transformer#transform(jdf.framework.core.data.DataSet,
	 *      java.io.Writer)
	 */
	public int transform(DataSet source, Writer writer) throws TransformerException {
		throw new TransformerException("not support");
	}

	public String getContentType() {
		return "application/octet-stream";
	}

}