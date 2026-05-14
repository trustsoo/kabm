package jdf.framework.logic.spi.parser.driver;

import jdf.framework.core.data.BytesData;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ExternalData;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.FieldType;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.log.Logger;
import jdf.framework.logic.spi.parser.DataSetParser;
import jdf.framework.logic.spi.parser.FieldParser;
import jdf.framework.logic.spi.parser.TranslationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * <p>
 * 문자는 ASCII형태로 숫자는 binary형태로 parsing한다.
 * </p>
 * 
 * @author
 * @version 1.1
 */
public class DefaultDataSetParser extends DataSetParser
{
    private final static String LOG_ID = "<al:DefaultDataSetParser> ";

    public DefaultDataSetParser()
    {
    }

    /**
     * ExternalData(바이트)를 DataSet으로 변환
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

        transToDataSet(blocks, input.toByteArray(), 0, output);
    }

    /**
     * ExternalData(바이트)를 DataSet으로 변환
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
            // System.out.println("CLINET MODE ----------------- ");
            break;

        case SERVER:
            blocks = schema.getInputBlocks();
            output = schema.getInputDataSetInstance();
            // System.out.println("SERVER MODE ----------------- ");
            break;
        }

        return transToDataSet(blocks, input.toByteArray(), 0, output);
    }

    /**
     * DataSet을 ExternalData(바이트)으로 변환
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

        byte[] result = transToByteArray(blocks, input);

        ExternalData extData = new BytesData(result);

        return extData;
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
    private byte[] transToByteArray(Block[] blocks, DataSet dataset) throws TranslationException
    {
        if (blocks == null)
            throw new TranslationException("input param[block] null");

        if (dataset == null)
            throw new TranslationException("input param[dataset] null");

        dataset.unfixNull();

        

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try
        {

            for (int i = 0; i < blocks.length; i++)
            {

                Block block = blocks[i];

                int iterationNum = 0;

                if (block.isIterationNumSet())
                    iterationNum = block.getIterationNum();
                else
                {
                    String refNm = block.getIterationRefName();

                    Object x = dataset.get(refNm);

                    if (x == null)
                    {
                        // throw new TranslationException(refNm+" is not exist
                        // in IOSchema");
                        iterationNum = 1;
                    } else if (x.getClass() == String.class)
                        iterationNum = Integer.parseInt((String) x);

                    else if (x.getClass() == Integer.class)
                        iterationNum = ((Integer) x).intValue();
                }

                Field[] fields = block.getFields();

                for (int j = 0; j < iterationNum; j++)
                {
                    for (int k = 0; k < fields.length; k++)
                    {
                        Field field = fields[k];

                        int fieldSize = field.getSize();
                        String keyName = field.getName();

                        Object val = dataset.get(keyName, j);

                        if (val == null)
                        {
                            val = field.getDefaultValue();
                            if (val instanceof String)
                            {
                                String x = val.toString();

                                int z = x.indexOf(".count");
                                if (z > 0)
                                {
                                    String refFieldName = x.substring(2, z);
                                    val = new Integer(dataset.getCount(refFieldName));
                                }

                            }

                            dataset.put(keyName, val, j);
                        }

                        FieldParser define = protocol.getFieldParser(field.getTypeName());
                        if (define == null)
                        {
                            define = new DefaultFieldParser();
                            // throw new
                            // TranslationException(field.getTypeName()+"
                            // FieldParser is not exist");
                        }

                        boolean little_endian = field.isLittleEndian();
                        byte[] bytes = define.trans(val, fieldSize, little_endian);

                        out.write(bytes);
                    }
                }

            }
        } catch (TranslationException te)
        {
            throw te;
        } catch (IOException ioe)
        {
            throw new TranslationException("IOE:" + ioe.getMessage());
        } catch (Exception e)
        {
            Logger.err.println(LOG_ID + "trans error", e);
            throw new TranslationException(e.getMessage());
        }

        return out.toByteArray();

    }

    /**
     * ByteArray를 DataSet로 변환시킨다.
     * 
     * 
     * @param blocks
     * @param blocks
     * 
     * @return byteArray
     */
    private DataSet transToDataSet(Block[] blocks, byte[] inBytes, int src_position, DataSet dataset)
            throws TranslationException
    {
        String keyName = null;
        

        // System.out.println("++++++++++" + blocks.length);

        // ByteArray에서 parsing하려는 배열 Point
        int stPoint = src_position;

        try
        {
            for (int i = 0; i < blocks.length; i++)
            {

                Block block = blocks[i];

                int iterationNum = 1;

                if (block.isIterationNumSet())
                    iterationNum = block.getIterationNum();
                else
                {
                    String refNm = block.getIterationRefName();

                    if (refNm != null)
                    {
                        Object x = dataset.get(refNm);

                        // System.out.println(x+" REF ==
                        // "+block.getIterationRefName());

                        if (x == null)
                            iterationNum = 1;

                        else if (x.getClass() == String.class)
                        {

                            try
                            {
                                iterationNum = Integer.parseInt(((String) x).trim());
                            } catch (NumberFormatException nfe)
                            {
                                Logger.warn.println(LOG_ID + "interation number :" + x);
                                iterationNum = 0;
                            }
                        }

                        else if (x.getClass() == Integer.class)
                            iterationNum = ((Integer) x).intValue();

                    }

                }

                // System.out.println("iterationNum == " + iterationNum);

                Field[] fields = block.getFields();

                if (iterationNum == -1)
                    iterationNum = Integer.MAX_VALUE;

                // System.out.println(" 반복횟수 ===="+ iterationNum);

                for (int j = 0; j < iterationNum; j++)
                {

                    for (int k = 0; k < fields.length; k++)
                    {
                        Field field = fields[k];

                        int fieldSize = field.getSize();
                        keyName = field.getName();

                        // System.out.println(" ------------- " + keyName);

                        Object val = null;

                        // -1 이면 나머지 모든 byte를 parsing
                        if (fieldSize == -1)
                            fieldSize = inBytes.length - stPoint;

                        try
                        {
                            // *****
                            FieldParser define = protocol.getFieldParser(field.getTypeName());

                            boolean little_endian = field.isLittleEndian();
                            val = define.trans(inBytes, stPoint, fieldSize, little_endian);

                        } catch (Exception e)
                        {
                            Logger.err.println("<DefaultDataSetParser> transToDataSet", e);
                            throw new TranslationException(keyName + "[" + j + "] parse error. " + e.toString());
                        }

                        if (field.getType() == FieldType.FLOAT)
                        {
                            float tmpVal = Float.parseFloat(val.toString());

                            int p = 1;
                            try
                            {
                                p = (int) Math.pow(10, field.getDecimalPoint());
                            } catch (Exception e)
                            {

                            }
                            float resultVal = tmpVal / p;

                            val = new Float(resultVal);
                        } else if (field.getType() == FieldType.DOUBLE)
                        {
                            double tmpVal = Double.parseDouble(val.toString());

                            double p = 1;
                            try
                            {
                                p = Math.pow(10, field.getDecimalPoint());
                            } catch (Exception e)
                            {

                            }
                            double resultVal = tmpVal / p;
                            val = new Double(resultVal);
                        }

                        dataset.put(keyName, val, j);

                        stPoint += fieldSize;

                    }

                    if (stPoint >= inBytes.length)
                        break;
                }

            }

            return dataset;

        } catch (ArrayIndexOutOfBoundsException aie)
        {
            throw new TranslationException("byte array err." + keyName);
        } catch (TranslationException te)
        {
            throw te;
        } catch (Exception e)
        {
            Logger.err.println("<DefaultDataSetParser> transToDataSet", e);
            throw new TranslationException(keyName + ":" + e.toString());
            // return dataset;
        }

    }

    /**
     * @see jdf.framework.core.data.parser.DataSetParser#getFieldParser()
     */
    public FieldParser getFieldParserInstance()
    {
        return new DefaultFieldParser();
    }

}