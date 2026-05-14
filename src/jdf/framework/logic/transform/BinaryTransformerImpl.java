package jdf.framework.logic.transform;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.FieldType;
import jdf.framework.logic.spi.parser.ParseUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;


/**
 * binary 데이터 변환기
 * 
 * @author
 * 
 */
public class BinaryTransformerImpl extends TransformerBase {
	/**
	 * 기본생성자
	 * 
	 */
	public BinaryTransformerImpl() {
	}

	/**
	 * DataSet 를 ByteArray로 변환시킨다.
	 * 
	 */
	public int transform(DataSet input, OutputStream out) throws TransformerException {
		try {
			Block[] blocks = input.getIOSchema().getOutputBlocks();

			byte[] result = transToByteArray(blocks, input);

			out.write(result);

			return result.length;
		} catch (IOException e) {
			throw new TransformerException("transform error", e);
		}
	}

	/**
	 * DataSet 를 ByteArray로 변환시킨다.
	 * 
	 * 
	 * @param blocks
	 * @param blocks
	 * 
	 * @return byteArray
	 */
	private byte[] transToByteArray(Block[] blocks, DataSet dataset) throws TransformerException {

		dataset.unfixNull();

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			for (int i = 0; i < blocks.length; i++) {
				Block block = blocks[i];

				int iterationNum = 0;

				if (block.isIterationNumSet())
					iterationNum = block.getIterationNum();
				else {
					Object x = dataset.get(block.getIterationRefName());

					if (x == null)
						iterationNum = 1;

					else if (x.getClass() == String.class)
						iterationNum = Integer.parseInt((String) x);

					else if (x.getClass() == Integer.class)
						iterationNum = ((Integer) x).intValue();
				}

				Field[] fields = block.getFields();

				for (int j = 0; j < iterationNum; j++) {
					for (int k = 0; k < fields.length; k++) {
						Field field = fields[k];

						int fieldSize = field.getSize();
						String keyName = field.getName();

						Object val = dataset.get(keyName, j);

						if (val == null) {
							val = field.getDefaultValue();
							if (val instanceof String) {
								String x = val.toString();

								int z = x.indexOf(".count");
								if (z > 0) {
									String refFieldName = x.substring(2, z);
									val = new Integer(dataset.getCount(refFieldName));
								}

							}

							dataset.put(keyName, val, j);
						}

						byte[] bytes = null;

						switch (field.getType()) {
						case FieldType.STRING:
							bytes = ParseUtil.transStringToBytes((String) val, fieldSize);

							break;

						case FieldType.STRING_NUMBER:
							bytes = ParseUtil.transStringNumberToBytes((String) val, fieldSize);
							break;

						case FieldType.FLOAT:
						case FieldType.INTEGER:
							int intVal = 0;
							try {
								intVal = ((Integer) val).intValue();
							} catch (ClassCastException cce) {
								try {
									intVal = Integer.parseInt(val.toString());
								} catch (NumberFormatException nfe) {
									throw new NumberFormatException(keyName + " field data not set [int]");
								}
							}
							bytes = ParseUtil.transIntToBytes(intVal, fieldSize);
							break;

						case FieldType.LONG:
							long longVal = 0;
							try {
								longVal = ((Long) val).longValue();
							} catch (ClassCastException cce) {
								try {
									longVal = Long.parseLong(val.toString());
								} catch (NumberFormatException nfe) {
									throw new NumberFormatException(keyName + " field data not set [long]");
								}
							}
							bytes = ParseUtil.transLongToBytes(longVal, fieldSize);
							break;

						}

						out.write(bytes);
					}
				}

			}
		} catch (IOException ioe) {
			throw new TransformerException(ioe.toString());
		}

		return out.toByteArray();

	}

	/**
	 * @see Transformer#transform(DataSet,
	 *      Writer)
	 */
	public int transform(DataSet source, Writer writer) throws TransformerException {
		throw new TransformerException("not support");
	}

	public String getContentType() {
		return "text/txt; charset=euc-kr";
	}

}