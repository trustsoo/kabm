package jdf.framework.core.data.schema.format;

import java.text.DecimalFormat;



/**
 * 
 * 숫자 포매팅
 * 
 * 
 * @author
 * @version 1.0
 * @since 2003-12-07 오후 7:01:24
 *  
 */
public class NumberFormatter extends Formatter
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DecimalFormat df;

    public NumberFormatter(String formatInfo) throws IllegalArgumentException
    {
        try
        {
            df = new DecimalFormat();
            df.applyPattern(formatInfo);
        } catch (Exception e)
        {
            throw new IllegalArgumentException("parsing err. " + e.getMessage());
        }

    }

    /**
     * 
     * 
     * @see jdf.framework.core.data.schema.format.Formatter#format(java.lang.Object)
     */
    public String format(Object data) throws IllegalArgumentException
    {
        if (data == null)
            throw new IllegalArgumentException("data is null");

        Class valClass = data.getClass();

        if (valClass == String.class)
        {

            try
            {
                String sv = data.toString();
                    
                //sv = StringFormater.replaceStr(sv," ", "");
                sv = sv.trim();
                // 공백을 제거해 준다.
                
                return df.format( Double.parseDouble(sv) );
                

            } catch (Exception e)
            {
                return data.toString();
            }

        }

        else if (valClass == Integer.class)
            return df.format(((Integer) data).intValue());

        else if (valClass == Long.class)
            return df.format(((Long) data).longValue());

        else if (valClass == Float.class)
            return df.format(((Float) data).floatValue());

        //else if (valClass == Double.class)
        //return df.format(((Double) data).doubleValue());

        return df.format(data);
        //return data.toString();
    }

}