/*
 * @(#)DelimiterParser.java
 *
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author  JeongHo Eun, 94eun@hanmail.net
 */
 
package jdf.framework.core.io.parser;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.io.BytesParser;
import jdf.framework.core.io.FormatedEntity;
import jdf.framework.core.io.ParsingException;
import jdf.framework.core.util.ByteToStringPool;
import jdf.framework.core.util.ReflectUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 구분자(delimiter)를 이용하여 문자열을 파싱한다.
 * 구분자의 길이엔 제한이 없다.  
 * [구분자]
 * Default: 0x01
 * MultiRows: 0x02
 * 
 * </p>
 * 
 * @author advan94
 * @version 0.1
 */
public final class DelimiterBytesParser implements BytesParser
{

    
    public static final char MDM_CH = (char)1;  //1차 구분자
	public static final char SDM_CH = (char)2;  //2차 구분자
	
	public final String MDM;
	public final String SDM;

	public final int MDM_LENGTH;
	public final int SDM_LENGTH;
    
    private final static String ENCODE = "UTF-8";

	/**
	 * 
	 * @uml.property name="pool"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static ByteToStringPool pool;
	
	public void setEncoding(String encode) 
	{			
	}
	
	
	static
    {
        try {
            Config conf = Configuration.lookup("/network/parser/pool");
            int size = conf.getInt("size", 1000);
            boolean isPool = conf.getBoolean("enable", true);

			if (isPool)
				pool = new ByteToStringPool(size, ENCODE);

        }
        catch(Exception e)
        {
        }
    }
	
	// 마지막 필드에서 구분자를 무시할지 여부
	private boolean isIgnore = false;
    
    public DelimiterBytesParser()
    {
        this(String.valueOf( MDM_CH ), String.valueOf( SDM_CH ), false);
    }
    
    
    public DelimiterBytesParser(String del)
    {
		this(del, String.valueOf( SDM_CH ), false);
    }

	public DelimiterBytesParser(String del, boolean isIgnore)
    {
		this(del, String.valueOf( SDM_CH ), isIgnore);
    }
    
    
    public DelimiterBytesParser(String del1, String del2)
    {
        this(del1, del2, false);
    }
	
    /**
	 * 원하는 문자열을 구분자로 해당 바이트를 FormatedEntity로 파싱해주는 파서이다.
	 * @param del1 length 자를 길이
	 * @param del2 string 자를 대상
	 * @param isIgnore 바이트내에 구분자로 구분되야 할 데이터가 있을지라도 
	 *                FormatedEntity의 마지막 필드일 경우, 
	 *                이를 무시하고 모두 마지막 필드의 내용으로 처리할지 여부
	 *                예를 들어 마지막이 contents 같은 스트링일때, 구분자가 CRLF일 경우,
	 *                contents내에 CRLF가 포함될수 있으므로, 
	 *                남은 바이트를 모두를 구분자에 상관없이 파싱할 마지막 필드의 내용으로 처리 여부
	 *                false일 경우, 바이트가 남아 있으면 모두 버린다.(일반적인 경우)
	 *                true일 경우, 구분자를 포함하여 바이트를 모두 마지막 필드내용으로 처리한다.
	 */
	public DelimiterBytesParser(String del1, String del2, boolean isIgnore)
    {
        MDM = del1;
        SDM = del2;

		MDM_LENGTH = MDM.length();
		SDM_LENGTH = SDM.length();

		this.isIgnore = isIgnore;
    }
    

    
    //private static Hashtable hash = new Hashtable();
    
	/**
     * FormatedEntity 객체에 입력값으로 들어온 byte stream을 parsing하여 값을 넣는다.
     *
     * @param data FormatedEntity를 구현한 객체
     * @param contentBytes 읽어들일 byte stream
     * @return int parsing한 bytes stream 길이
     */
    public int parse(FormatedEntity data, byte[] contentBytes ) 
        throws ParsingException
	{
	    int len = 0;
        int offset = 0;
			
		int del_len = 0;
		try {
			int inputLength = contentBytes.length;
			
            String contentStr = getString(contentBytes, 0, inputLength);

            len = contentStr.length();
            offset = 0;


            Class c = data.getClass();
			
			
			Field[] field = ReflectUtil.getFields(c);   //public만
			
			
            int fieldCount = field.length; //필드 개수
            
            
            String blit = null;

			for(int i=0; i<fieldCount; i++)
			{
                int delimOff = 0;

                int MDM_index = contentStr.indexOf(MDM,offset);
                int SDM_index = contentStr.indexOf(SDM,offset);

                if( MDM_index==-1 && SDM_index==-1)
                {
                    blit = contentStr.substring(offset,len);
                }
                else
                {
                    if ( SDM_index == -1 )
                    {
                        delimOff = MDM_index;
						del_len = MDM_LENGTH;
                    }
                    else if ( MDM_index == -1 )
                    {
                        delimOff = SDM_index;
						del_len = SDM_LENGTH;
                    }
                    else
                    {
                        delimOff = Math.min(MDM_index,SDM_index);  //가장 작은 위치를 반환
						del_len = (MDM_index == delimOff) ? MDM_LENGTH : SDM_LENGTH;
                    }
					
                 // 쓰레기 처리를 안 하며, 필드가 마지막 필드라면,
					if (!isIgnore && i == fieldCount-1)
						blit = contentStr.substring(offset);
					else
						blit = contentStr.substring(offset,delimOff);
                }

                
                Class fieldClass = field[i].getType();
			    
			    
			    if( fieldClass == Long.TYPE)
				{
				    long currentLong = Long.parseLong(blit);
				    
				    field[i].setLong( data, currentLong );
				}
				else if( fieldClass == Integer.TYPE)
				{
				    int currentInt = Integer.parseInt(blit);
				    field[i].setInt( data, currentInt);
				}
                else if( fieldClass == Short.TYPE)
				{
				    int currentShort = Short.parseShort(blit);
				    field[i].setInt( data, currentShort);
				}
				else if( fieldClass == String.class)
				{
				    field[i].set( data, blit );
				}
				
				else if( fieldClass == Float.TYPE)
				{
				    float f = Float.parseFloat( blit );
				    field[i].setFloat( data, f );
				}
				
				else if( fieldClass == Double.TYPE)
				{
				    double d = Double.parseDouble( blit );
				    field[i].setDouble( data, d );
				}
                
                else if( fieldClass == Character.TYPE)
				{
				    field[i].setChar( data, blit.charAt(0));
				    
				}
                else if( fieldClass == Boolean.TYPE)
				{
				    boolean b = Boolean.getBoolean( blit );
                    field[i].setBoolean( data, b );
				    
				}
				else if (fieldClass.isArray() &&  fieldClass.getComponentType()==Character.TYPE )
				{
					char[] tempChars = new char[blit.length()];
					blit.getChars(0, blit.length(), tempChars, 0);

					field[i].set( data, tempChars );
				}
				
			 // byte 배열이면
				//취급하지 않는다.
                
				
				// FormatedEntity 구현 객체의 배열인 경우
				else if (fieldClass.isArray() &&  fieldClass.getComponentType()!=Byte.TYPE )
				{
					Class entity = fieldClass.getComponentType();
                    
                    FormatedEntity child = null;
                    
                    try {    
                        child = (FormatedEntity) entity.newInstance();
                     }
				    catch(ClassCastException ccex) {
				        throw new ParsingException(entity.getName()+" type 배열은 아직 지원하지 않는 타입입니다");
				    }   
                        
                        
                    Field[] childfield = ReflectUtil.getFields(entity);   // public만
                    int childfieldCount = childfield.length; //필드 개수
                    
                    String childStr = contentStr.substring(offset, len); //자식의 문자열를 구한다.

                    int ArrayNum = getItemCount(SDM, childStr) / childfieldCount; //배열 개수
                    
                    List list = new ArrayList(ArrayNum);
                    
                    int tmpIn = 0;

                    for(int k=0; k<ArrayNum; k++)
                    {
                        int index = seekIndexCount(SDM, (k+1) * childfieldCount, childStr);
                        String subStr = childStr.substring(tmpIn, index);
                        byte[] tempBytes = null;
						
						if (pool == null)
							tempBytes = childStr.substring(tmpIn, index).getBytes(ENCODE);
						else
							tempBytes = pool.get(subStr);

                        tmpIn = index+1;
                
					    parse(child, tempBytes);
                          
                        list.add(k, child );
                        
                        if(k != ArrayNum-1)
                            child = (FormatedEntity) entity.newInstance();
                    }
                    
                
                    Object[] objList = list.toArray();
                    
                    if (field[i].get(data) == null)
					    field[i].set(data, Array.newInstance(entity,ArrayNum) ); //Array.newInstance(entity,ArrayNum)
					    
                
					for(int k=0; k<ArrayNum; k++)
					    Array.set( field[i].get(data), k, objList[k]);
				}
				
			 // 내포관계의 FormatedEntity클래스인 경우라고 가정하고 처리.
				else 
				{
					FormatedEntity child=null;
                    
                    try {
                        child = (FormatedEntity) field[i].get( data );
                    }
				    catch(ClassCastException ccex) {
				        throw new ParsingException(fieldClass.getName()+"는 아직 지원하지 않는 타입입니다");
				    }
				        
                    if(child == null) // 자식 필드를 초기화 하지 않았을 경우
                    {
                        child = (FormatedEntity) fieldClass.newInstance(); //생성하고
                    }
                    Field[] childfield = ReflectUtil.getFields(fieldClass);   // public만
                    int childfieldCount = childfield.length; //필드 개수
                    
                    String childStr = contentStr.substring(offset, len); //자식의 문자열를 구한다.

                    int index = seekIndexCount(MDM, childfieldCount, childStr);
                    childStr = childStr.substring(0, index);
                    
                    byte[] tempBytes = null;
					if (pool == null)
						tempBytes = childStr.getBytes(ENCODE);
					else
						tempBytes = pool.get(childStr);
                    
                    parse(child, tempBytes);
                    field[i].set( data, child );

                    delimOff = offset + index * del_len;
                }
		
                
			    offset = delimOff + del_len;
                
		    }// for
		    
		    
			return len;	


	    }
	    catch(ParsingException pex)
	    {
	        throw pex;
	    }
	    
	    catch( Exception e )
		{
			e.printStackTrace();
			
			ParsingException pe = new ParsingException(e.getMessage());
			pe.setParsingBytes(contentBytes);
			
			throw pe;
		}
    }

    /**
     * Delimit기준으로 분리되는 아이템의 갯수 반환
     * 예) 1|2|3 --> 3개
     */
    private final static int getItemCount(String delimitor, String str)
    {
        int count = 0;
        
        int len = str.length();
		int offset = 0;

		while(true)
		{
			int delimOff = str.indexOf(delimitor,offset);
			if( delimOff==-1 )
			{
			    count++;
				break;
			}

			count++;
			offset = delimOff + delimitor.length();
			if( offset >= len ) 
				break;
		}

        return count;
    }
    /**
     * str에 대하여 delimitor가 count 만큼 나온후 최초 위치를 반환
     */
    private final static int seekIndexCount(String delimitor, int delimitCount, String str)
    {
        int count = 0;
        
        int len = str.length();
		int offset = 0;

		while(true)
		{
			int delimOff = str.indexOf(delimitor,offset);
			if( delimOff==-1 )
			{
			    count++;
                if (count == delimitCount)
                    return len;
				break;
			}

			count++;
            if (count == delimitCount)
                    return delimOff++;

			offset = delimOff + delimitor.length();
			if( offset >= len ) 
				break;
		}

        return len;
    }

    
    private final String getString(byte[] contentBytes, int startPoint, int total)
        throws java.io.UnsupportedEncodingException
    {
        if (pool == null)
			return new String( contentBytes, startPoint, total-startPoint, ENCODE );
		else
			return pool.get(contentBytes, startPoint, total-startPoint);
    }

    /**
	* Entity에 들어있는 값을 가지고 format 된 byte[]를 생성한다
	*
	* RuntimeException  StringFormater
	* IOException ByteArrayOutputStream
	*/
	public byte[] getBytes(FormatedEntity entity) throws RuntimeException
	{
		return getBytes(entity, MDM);
	}

	/**
	* Entity에 들어있는 값을 가지고 format 된 byte[]를 생성한다
	*
	* RuntimeException  StringFormater
	* IOException ByteArrayOutputStream
	*/
	private byte[] getBytes(FormatedEntity entity, String deli) throws RuntimeException
	{
	    if(entity == null)
	        throw new RuntimeException("entity is null.");

		try
		{
    		Class c = entity.getClass();
			Field[] field = ReflectUtil.getFields(c);
            
    		
            StringBuffer buf = new StringBuffer();

            boolean isArraySizeZero = false;

    		int fieldCount = field.length;
    		
    		Class fieldClass;

    
    		for (int i=0; i<fieldCount; i++)
    		{
    
    			fieldClass = field[i].getType();
    			
    			//String�̸� 
    			if(fieldClass == String.class)
    			{
    			    String data = (String) field[i].get( entity );
    			    
    			    buf.append(data);
    			}
    			
    
    
    			// int
    			else if(fieldClass == Integer.TYPE )
    			    buf.append(String.valueOf( field[i].getInt( entity ) ));
    			
    			// long
    			else if(fieldClass == Long.TYPE )
    				buf.append(String.valueOf( field[i].getLong( entity ) ));
    			
    			// short
    			else if(fieldClass == Short.TYPE )
    				buf.append(String.valueOf( field[i].getShort( entity ) ));
    				
    			// char[]
		    	else if(fieldClass.isArray() && fieldClass.getComponentType()==Character.TYPE)
		    	{
					char[] data = (char[])field[i].get(entity);
					buf.append( new String(data) );
				}
				
    		    // char
    			else if(fieldClass == Character.TYPE )
    			    buf.append(String.valueOf( field[i].getChar(entity) ));
    			
    			// float
    			else if(fieldClass == Float.TYPE )
    				buf.append(String.valueOf( field[i].getFloat(entity) ));	
    			
    			
    			// double
    			else if(fieldClass == Double.TYPE )
    				buf.append(String.valueOf( field[i].getDouble(entity) ));
    		
    		    // boolean
    			else if(fieldClass == Boolean.TYPE )
    				buf.append(String.valueOf( field[i].getBoolean(entity) ));

				else if( fieldClass.isArray() )
    			{
    			    try
                    {
                        FormatedEntity[] child = (FormatedEntity[]) field[i].get(entity);
                        
						for(int k=0; k<child.length; k++)
                        {
                            byte[] b = getBytes(child[k], SDM);
    			            buf.append( new String(b, 0, b.length) );
                            if (k < child.length - 1)
                                buf.append(MDM);
                        }

                        if (child.length == 0) isArraySizeZero = true;
                    }
                    catch(ClassCastException cce) {
                        throw new RuntimeException(fieldClass.getComponentType().getName()
                                                        +"는 아직 지원하지 않는 배열타입입니다");
                    }
    			
    		    }

    			// FormatedEntity
    			else 
    			{
    			    FormatedEntity child=null;
    			    try {
    			        child = (FormatedEntity) field[i].get( entity );
    			    }
    			    catch(ClassCastException cce) {
    			        throw new RuntimeException(fieldClass.getName()+"는 아직 지원하지 않는 배열타입입니다");
    			    }
    			    
                    byte[] b = getBytes(child);
    			    buf.append( new String(b, 0, b.length) );
    			}

                if (i != fieldCount - 1)
                {
                    buf.append(deli);
                }
                
                if (isArraySizeZero)
                {
                    //배열의 사이즈가 0일때, 구분자가 두개가 붙는 현상이 있다. 
                    //따라서, 구분자중 앞에 구분자 한개를 지운다.
                    buf.delete(buf.length()-1, buf.length());
                    isArraySizeZero = false;
                }

    		}
    		return buf.toString().getBytes();
    	}
    	 catch(RuntimeException re)
        {
            //re.printStackTrace();
            throw re;
        }
        catch(IllegalAccessException iie)
        {
            iie.printStackTrace();
            throw new RuntimeException(iie.getMessage());
        }
      
    
    
    
    }

    public static void main(String[] args)
	{
		try
		{
			BytesParser parser = new DelimiterBytesParser("\n\r", "|");
		
			FormatedEntity entity = new TestEntity();

			byte[] data = "B\n\r1\n\rAbc|1|c".getBytes();
			parser.parse( entity, data );
			
			System.out.println(entity);

			byte[] data1 = parser.getBytes(entity);
			System.out.print(new String(data1));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static class TestEntity extends FormatedEntity
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6818659897204342772L;

		public int[] getFieldsLength() {
			return new int[] {
								 1,1,1
			};
		}

		public String a;
		public int b;

		/**
		 * 
		 * @uml.property name="c"
		 * @uml.associationEnd multiplicity="(0 -1)"
		 */
		public Test1Entity[] c;

	}

	public static class Test1Entity extends FormatedEntity
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6424827942073601549L;
		public int[] getFieldsLength() {
			return new int[] {
								 1,1,1
			};
		}

		public String a;
		public int b;
		public String c;

	}
	
}
