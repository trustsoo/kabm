package jdf.framework.logic.spi.parser.driver;

import jdf.framework.logic.spi.parser.FieldParser;
import jdf.framework.logic.spi.parser.ParseUtil;
import jdf.framework.logic.spi.parser.TranslationException;

/**
 * <code>DefaultFieldParser.java</code>
 * 
 * 
 * 
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DefaultFieldParser extends FieldParser
{

    public DefaultFieldParser()
    {}

    /**
     * @see jdf.framework.core.data.parser.FieldParser#trans(byte[], int, int)
     */
    public Object trans(byte[] bytes, int stPoint, int fieldSize) throws TranslationException
    {
        Object result;

        int type= this.getType();
        if (this.getRemoteFieldParser() != null)
            type= this.getRemoteFieldParser().getType();

        try
        {
            switch (type)
            {

                case INTEGER :
                    int crntInt= ParseUtil.transBytesToInt(bytes, stPoint, fieldSize);
                    result= new Integer(crntInt);
                    break;

                case LONG :
                    long crntLong= ParseUtil.transBytesToLong(bytes, stPoint, fieldSize);
                    result= new Long(crntLong);
                    break;
                    
                case DOUBLE:
                    double crntDouble= ParseUtil.transBytesToDouble(bytes, stPoint, fieldSize);
                    result= new Double(crntDouble);
                    break;    

                case STRING :
                default :

                    String str= ParseUtil.transBytesToString(bytes, stPoint, fieldSize);
                    //if (str != null)
                    //    str= str.trim();
                    result= str;
                    break;

            }
        }
        catch (Exception e)
        {
            throw new TranslationException(e.getMessage());
        }

        result= this.transInnerType(result);

        return result;
    }

    /**
     * @see jdf.framework.core.data.parser.FieldParser#trans(Object, int)
     */

    public byte[] trans(Object data, int fieldSize) throws TranslationException
    {

        byte[] result= null;

        data= transRemoteType(data);

        int type= this.getType();
        if (this.getRemoteFieldParser() != null)
            type= this.getRemoteFieldParser().getType();

        try
        {
            switch (type)
            {

                case STRING :
                	String strDt=null;
                	if(data==null)
                		strDt="";
                	else	
                		strDt = data.toString();
                			
                    result= ParseUtil.transStringToBytes(strDt, fieldSize, this.getAlign(), this.getFillData());
                    break;

                case INTEGER :
                    int intVal= 0;
                    try
                    {
                        if (data == null)
                            intVal= 0;
                        else
                            intVal= ((Integer) data).intValue();
                    }
                    catch (ClassCastException cce)
                    {
                        try
                        {
                            intVal= Integer.parseInt(data.toString());
                        }
                        catch (NumberFormatException nfe)
                        {
                            throw new NumberFormatException(this.getKeyName() + " field 값을 [int]형으로 처리불가");
                        }
                    }
                    result= ParseUtil.transIntToBytes(intVal, fieldSize);
                    break;

                case LONG :
                    long longVal= 0;
                    try
                    {
                        if (data == null)
                            longVal= 0;
                        else
                            longVal= ((Long) data).longValue();
                    }
                    catch (ClassCastException cce)
                    {
                        try
                        {
                            longVal= Long.parseLong(data.toString());
                        }
                        catch (NumberFormatException nfe)
                        {
                            throw new NumberFormatException(this.getKeyName() + " field 값을 [long]형으로 처리불가");
                        }
                    }
                    result= ParseUtil.transLongToBytes(longVal, fieldSize);
                    break;
                    
                case DOUBLE:
                    double doubleVal = 0;
                    try
                    {
                        if (data == null)
                            doubleVal= 0;
                        else
                            doubleVal= ((Double) data).doubleValue();
                    }
                    catch (ClassCastException cce)
                    {
                        try
                        {
                            doubleVal= Double.parseDouble(data.toString());
                        }
                        catch (NumberFormatException nfe)
                        {
                            throw new NumberFormatException(this.getKeyName() + " field 값을 [double]형으로 처리불가");
                        }
                    }
                    result= ParseUtil.transDoubleToBytes(doubleVal, fieldSize);
                    break;

                default :
                    result= ParseUtil.transStringToBytes("", fieldSize, this.getAlign(), this.getFillData());

            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            throw new TranslationException(e.getClass().getName()+" ["+this.getKeyName()+"] "+e.getMessage());
        }

        return result;
    }
}
