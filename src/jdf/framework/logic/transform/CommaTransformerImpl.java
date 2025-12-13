package jdf.framework.logic.transform;

import java.io.IOException;
import java.io.OutputStream;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResultSetDataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;


/**
 * , 를 구분자로 데이터를 구분한다.
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CommaTransformerImpl extends TransformerBase
{
    private static String DELIMITER= ",";

    public CommaTransformerImpl()
    {}

    public int transform(DataSet input, OutputStream out) throws TransformerException
    {
        try
        {
            Block[] blocks= input.getIOSchema().getOutputBlocks();

            
            //System.out.println(defaultEncoding);
            
            
            byte[] result= transToCommaString(blocks, input).getBytes(defaultEncoding);

            out.write(result);

            return result.length;
        }
        catch (IOException e)
        {
            throw new TransformerException("transform error",e);
        }
    }

    /**
     * DataSet 를  ByteArray로 변환시킨다.
     *
     *
     * @param blocks
     * @param blocks
     *
     * @return byteArray
     */
    private String transToCommaString(Block[] blocks, DataSet dataset) throws TransformerException
    {
        dataset.unfixNull();

        StringBuffer out= new StringBuffer();

        try
        {

            for (int i= 0; i < blocks.length; i++)
            {
                Block block= blocks[i];

                int iterationNum= 0;

                if (block.isIterationNumSet())
                    iterationNum= block.getIterationNum();
                else
                {
                    Object x= dataset.get(block.getIterationRefName());

					try
					{
						if (x.getClass() == java.lang.String.class)
							iterationNum = Integer.parseInt((String) x);

						else if (x.getClass() == java.lang.Integer.class)
							iterationNum = ((Integer) x).intValue();
					}
					catch (Exception ee)
					{
						iterationNum = 1;
					}
                }

                Field[] fields= block.getFields();
                String firstKeyName = null;
                for (int k = 0; k < fields.length; k++) {
    				Field field = fields[k];
    				String keyName = field.getName();
    				if (k == 0)
    					firstKeyName = keyName;

    				int tmpCount = dataset.getCount(keyName);
    				if (tmpCount > iterationNum)
    					iterationNum = tmpCount;
    			}
                
                //boolean isResultSetDataSet = false;
                
                //System.out.println(dataset.getClass().getName());;
                
    			/*if (dataset instanceof ResultSetDataSet) {
    				isResultSetDataSet = true;
    			}*/
                
    			
    			//System.out.println(isResultSetDataSet);

                for (int j= 0; j < iterationNum; j++)
                {
                    for (int k= 0; k < fields.length; k++)
                    {
                        Field field= fields[k];

                        String keyName= field.getName();

                        Object val= dataset.get(keyName, j);
                        //System.out.println(keyName+"===>"+val+"");
                        if (val == null)
                        {
                            val= field.getDefaultValue();
                            if (val instanceof java.lang.String)
                            {
                                String x= val.toString();

                                int z= x.indexOf(".count");
                                if (z > 0)
                                {
                                    String refFieldName= x.substring(2, z);
                                    val= new Integer(dataset.getCount(refFieldName));
                                }

                            }

                            dataset.put(keyName, val, j);
                        }
                        
                        if(val == null) val = "";


                        if (k >0 && k <= fields.length -1)
                            out.append(DELIMITER);

                        out.append(val.toString());
                    }
                    
                    out.append("\n");
                    if (true) {
    					iterationNum = dataset.getCount(firstKeyName);

    				}
                }

            }
        }
        catch (Exception ioe)
        {
        	
            ioe.printStackTrace();
            throw new TransformerException(ioe.toString());
        }

        return out.toString();

    }

}