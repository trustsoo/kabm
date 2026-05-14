package jdf.framework.logic.spi.parser.driver;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ExternalData;
import jdf.framework.core.data.StringData;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.logic.spi.parser.DataSetParser;
import jdf.framework.logic.spi.parser.FieldParser;
import jdf.framework.logic.spi.parser.TranslationException;

/**
 * <p>
 * 문자는 ASCII형태로 숫자는 binary형태로 parsing한다.
 * </p>
 * 
 * @author
 * @version 1.1
 */
public class DelimiterDataSetParser extends DataSetParser
{
	private String delimiter = ",";

	/**
     * 
     * 
     */
	public DelimiterDataSetParser() {
	}

	/**
     * 
     * @param delimiter
     */
	public DelimiterDataSetParser(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
     * String를 DataSet으로 변환
     * 
     * @param schema
     * @param input
     * @param output
     */
	public void transDataSet(IOSchema schema, ExternalData input, DataSet output) throws TranslationException
	{
		Block[] blocks = null;

		switch (mode) {
		case CLIENT:
			blocks = schema.getOutputBlocks();
			break;

		case SERVER:
			blocks = schema.getInputBlocks();
			break;
		}

		transToDataSet(blocks, input.toString(), output);
	}

	/**
     * String를 DataSet으로 변환
     * 
     * @param schema
     * @param input
     * @return DataSet
     */
	public DataSet transDataSet(IOSchema schema, ExternalData input) throws TranslationException
	{
		Block[] blocks = null;
		DataSet output = null;

		switch (mode) {
		case CLIENT:
			blocks = schema.getOutputBlocks();
			output = schema.getOutputDataSetInstance();
			break;

		case SERVER:
			blocks = schema.getInputBlocks();
			output = schema.getInputDataSetInstance();
			break;
		}

		return transToDataSet(blocks, input.toString(), output);
	}

	/**
     * DataSet을 String으로 변환
     * 
     * @param schema
     * @param input
     * @return ExternalData
     */
	public ExternalData transExternalData(IOSchema schema, DataSet input) throws TranslationException
	{
		Block[] blocks = null;

		switch (mode) {
		case CLIENT:
			blocks = schema.getInputBlocks();
			break;

		case SERVER:
			blocks = schema.getOutputBlocks();
			break;
		}

		String result = transToString(blocks, input);

		ExternalData extData = new StringData(result);

		return extData;
	}

	/**
     * DataSet 를 String로 변환시킨다.
     * 
     * 
     * @param blocks
     * @param blocks
     * 
     * @return byteArray
     */
	private String transToString(Block[] blocks, DataSet dataset) throws TranslationException
	{

		dataset.unfixNull();

		StringBuffer out = new StringBuffer();

		boolean isFirst = true;

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

					if (isFirst) {
						isFirst = false;
						out.append(val);
					} else {
						out.append(delimiter);
						out.append(val);
					}
				}
			}

		}

		return out.toString();

	}

	/**
     * String을 DataSet로 변환시킨다.
     * 
     * 
     * @param blocks
     * @param blocks
     * 
     * @return byteArray
     */
	private DataSet transToDataSet(Block[] blocks, String str, DataSet dataset) throws TranslationException
	{
		String keyName = null;

		String[] strs = SmartStringArray.split(this.delimiter, str);
		int start = 0;
		try {
			for (int i = 0; i < blocks.length; i++) {

				Block block = blocks[i];

				int iterationNum = 0;

				if (block.isIterationNumSet())
					iterationNum = block.getIterationNum();
				else {
					Object x = dataset.get(block.getIterationRefName());

					if (x.getClass() == String.class)
						iterationNum = Integer.parseInt((String) x);

					else if (x.getClass() == Integer.class)
						iterationNum = ((Integer) x).intValue();
				}

				Field[] fields = block.getFields();

				for (int j = 0; j < iterationNum; j++) {
					for (int k = 0; k < fields.length; k++) {
						Field field = fields[k];

						keyName = field.getName();
						Object val = null;

						val = strs[start++];

						dataset.put(keyName, val, j);
					}
				}

			}

			return dataset;

		} catch (Exception e) {
			return dataset;
		}

	}

	public FieldParser getFieldParserInstance()
	{
		return null;
	}

}
