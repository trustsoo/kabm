package jdf.framework.logic.transform;

import java.io.*;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.util.Base64;


/**
 * BASE64 용 데이터 변환기
 * 
 * @author
 * 
 */
public class Base64TransformerImpl extends TransformerBase {
	private BinaryTransformerImpl transformer = new BinaryTransformerImpl();

	public Base64TransformerImpl() {
	}

	/**
	 * 
	 */
	public int transform(DataSet input, OutputStream out) throws TransformerException {
		try {
			ByteArrayOutputStream baout = new ByteArrayOutputStream();

			transformer.transform(input, baout);

			byte[] result = Base64.encodeBytes(baout.toByteArray());

			out.write(result);

			return result.length;

		} catch (IOException ioe) {
			throw new TransformerException("transform error", ioe);
		}
	}

	/**
	 * 변환 메쏘드
	 * 
	 * @see jdf.framework.logic.transform.Transformer#transform(jdf.framework.core.data.DataSet,
	 *      java.io.Writer)
	 */
	public int transform(DataSet source, Writer writer) throws TransformerException {
		try {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();

			int re = transform(source, bs);
			writer.write(bs.toString());

			return re;

		} catch (IOException ioe) {
			throw new TransformerException("transform error", ioe);
		}
	}

	public String getContentType() {
		return "text/txt; charset=" + this.defaultEncoding;
	}

}