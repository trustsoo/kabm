/*
 * @(#)FormatedEntity.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */

package jdf.framework.core.io;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jdf.framework.core.Entity;
import jdf.framework.core.data.DataSet;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.ReflectUtil;
import jdf.framework.core.util.Utility;

/**
 * <b><code>FormatedEntity</code></b>
 * <p>
 * 주문이나 그밖의 byte stream형태를 처리하기 위한 객체의 interface이다.
 * </p>
 *
 * @author
 * @version 1.0
 * @see jdf.framework.core.io.FormatedInputStream
 * @see jdf.framework.core.io.FormatedOutputStream
 */

public abstract class FormatedEntity extends Entity //implements Externalizable
{
	
	
	/**
	 * 각 스트림 필드의 길이를 지정한다.
	 *<p>
	 *<pre> 
	 * 예)
	 *	public int[] getFieldsLength() {
	 *   return new int[] {7,3,1,1,4,2}; 
	 *  }
	 * 만약 두번째 field가 소수점(총길이7, 소수점길이3)이 들어가는 경우는
	 * 
	 *	public int[] getFieldsLength() {
	 *   return new int[] {7, point(7,3) ,1,4,2}; 
	 *  }
	 *</pre>
	 *
	 *
	 * @return int[] 각 필드길이 배열
	 **/
	public abstract int[] getFieldsLength();
	
	
	public String[] getFieldsLabel()
	{
		return null;
	}
	
	public boolean[] getFieldsRequired()
	{
		return null;
	}
	
	
	public String[] getFieldsCodeUrl()
	{
		return null;
	}
	
	
	public boolean[] getFieldsSystemMaked()
	{
		return null;
	}
	

	public int length()
	{
	    int len=getArraySum( getFieldsLength() ); // 우선 자신의 길이를 더하고,
	    
	    //Field[] field = getClass().getFields();
	    
	    Class c = this.getClass();
	    
	    //Field[] field = (Field[]) hash.get(c);
	    Field[] field = ReflectUtil.getFields(c);
            
	    
	    Object obj;
	    for(int i=0; i<field.length; i++)
	    {
	        try {
                obj = field[i].get(this);
	        } catch(IllegalAccessException ie) {
	            continue;
	        }
	        
	        if ( obj instanceof FormatedEntity)
	        {
	            FormatedEntity entity = (FormatedEntity) obj;
	            len+= entity.length();
            }
        }
        
        return len;
	}
	
	
	public final static int getArraySum(int[] inArray)
	{
	    int totalLen = 0;
	    
	    for(int j=0; j<inArray.length; j++)
	    {
	        
	        if(inArray[j]<I_DIV)
	            totalLen = totalLen+inArray[j];
	        else
	            totalLen = totalLen+getTotalLen(inArray[j]);
	        
	    }
	    
	    return totalLen;
	}

	private static final int I_DIV =10000;
	private static final String DIV="0000";
	
	/**
	 * point(12, 3)
	 * --> 1200003
	 *
	 **/    
    protected static int point(int total, int end)
	{
        StringBuffer sb = new StringBuffer();
        sb.append(total).append(DIV).append(end);
        
        Integer i = new Integer( sb.toString() );
        
        return i.intValue();
    }
    
    
    public static int getTotalLen(int val)
    {
        if(val<I_DIV)
            return val;
        String x = String.valueOf(val);
        return Integer.parseInt( x.substring(0, x.indexOf(DIV) ) );
    }
    
    
    public static int getPointLen(int val)
    {
        if(val<I_DIV)
            return 0;
        String x = String.valueOf(val);
        return Integer.parseInt( x.substring(x.indexOf(DIV) ) );
    }

    /**   Externalizable 위해    
    private static BytesParser parser;
    
    static {
        parser = BytesParserFactory.getBinaryParser();
    }

    public void writeExternal(java.io.ObjectOutput out)
        throws IOException
    {
        byte[] bytes = parser.getBytes(this);
            
        out.writeInt(bytes.length);
        out.write(bytes);
        
    }

    public void readExternal(java.io.ObjectInput in)
        throws IOException, ClassNotFoundException
    {
        int size = in.readInt();
        
        byte[] bytes = new byte[size];
        
        in.read(bytes);

        parser.parse(this, bytes);
    }
**/
    
    
    /*public String biz_key = null;
    public String task_run_no = null;
    public String task_no = null;
    */
    
    private Properties prop = new Properties();
    
    public void setProperty(String key, String value)
    {
    	try
    	{
    		prop.setProperty(key, value);
    	} catch(NullPointerException ex)
    	{
    		Logger.warn.println("<at:FormatedEntity> "+"key("+key+") value is null");
    	} catch(Exception ex)
    	{
    		Logger.err.println("<at:FormatedEntity> "+Utility.getStackTrace(ex));
    	}
    }
    
    public Object getProperty(String key)
    {
    	return prop.getProperty(key);
    }
    
    public List<String> getPropertyKeys()
    {
    	List<String> list = new ArrayList<String>();
    	Iterator ite = prop.keySet().iterator();
    	while(ite.hasNext())
    	{
    		list.add((String)ite.next());
    	}
    	
    	return list;
    	
    }
    
    private DataSet data;
    
    public void setOringinalData(DataSet data)
    {
    	this.data = data;
    }
    
    public DataSet getOringinalData()
    {
    	return data;
    }
    

}
