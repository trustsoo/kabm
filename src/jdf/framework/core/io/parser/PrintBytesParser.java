/*
 * @(#)FormatedInputStream.java
 *
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
 
package jdf.framework.core.io.parser;

import jdf.framework.core.io.BytesParser;
import jdf.framework.core.io.FormatedEntity;
import jdf.framework.core.io.ParsingException;
import jdf.framework.core.util.Hexa;
import jdf.framework.core.util.ReflectUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 문자는 ASCII형태로 숫자는 binary형태로 parsing한다.
 * </p>
 *
 * @author  advan94
 * @version 1.1
 */
public final class PrintBytesParser implements BytesParser
{
    private final static String ENCODE = "UTF-8";
    
    
    private final static int VAR_LENGTH_MODE = 0;
    
    private final static int SHORT_SIZE=2;
    private final static int INT_SIZE=4;
    private final static int LONG_SIZE=8;
    private final static int DOUBLE_SIZE=8;
    private final static int FLOAT_SIZE=4;
    private final static int CHAR_SIZE=1;
    
    
    

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
	    int total = 0;
        int fieldLenNo = 0;
			
		try {
			int inputLength = contentBytes.length;
			
			
			
			Class<? extends FormatedEntity> c = data.getClass();
			Field[] field = ReflectUtil.getFields(c);
			//Field[] field = data.getClass().getFields();
			
			int[] lengths = data.getFieldsLength( ); //각 필드의 길이

			int fieldCount = field.length; //필드 갯수
        
        
            
            Class<?> fieldClass;               // 필드 class
		    String blit;     // 필드 class명
            
            int startPoint;

			for(int i=0; i<fieldCount; i++)
			{
			    startPoint = total;
			    fieldClass = field[i].getType();
			    
			    //System.out.println("======="+field[i].getName());
			    
			  //일반적인 배열이 아닌 필드

				if(fieldClass == String.class)
				{
					// 길이가 정해지지 않은 필드(길이가 0인) 는 들어오 byte끝까지 parsing한다.
				    if ( lengths[fieldLenNo]== VAR_LENGTH_MODE )
				        blit = getString(contentBytes, startPoint,inputLength);
				    
				    else
				    {
				        total += lengths[fieldLenNo];
				        blit = getString(contentBytes, startPoint,total);
				    }
				    
				    
				    field[i].set( data, blit.trim() );
				}
				
				
				else if(fieldClass == Integer.TYPE)
				{
				    total += INT_SIZE;
				    
				    int currentInt = parseInt( contentBytes,startPoint );
				    field[i].setInt( data, currentInt);
				}
				
				else if(fieldClass == Long.TYPE)
				{
				    total += LONG_SIZE;
				    
				    long currentLong = parseLong( contentBytes,startPoint );
				    field[i].setLong( data, currentLong );
				}
				
				else if(fieldClass == Short.TYPE)
				{
				    total += SHORT_SIZE;
				    
				    short currentShort = (short) parseShort( contentBytes,startPoint );
				    field[i].setShort( data, currentShort);
				}
				
				else if(fieldClass == Double.TYPE)
				{
				    total += DOUBLE_SIZE;
				    
				    double currentDoubl = Double.longBitsToDouble( parseLong( contentBytes,startPoint ) );
				    field[i].setDouble( data, currentDoubl );
				}
				
				else if(fieldClass == Float.TYPE)
				{
				    total += FLOAT_SIZE;
				    
				    float currentFloat = Float.intBitsToFloat( parseInt( contentBytes,startPoint ) );
				    field[i].setFloat( data, currentFloat);
				}
				
                
                else if(fieldClass == Character.TYPE)
				{
				    total += CHAR_SIZE;
				    
				    char currentChar = (char) contentBytes[startPoint];
				    field[i].setChar( data, currentChar);
				}

				
				
				// byte 배열이면
				else if ( fieldClass.getComponentType()==Byte.TYPE )
				{
				    int byteLen = lengths[fieldLenNo]; //읽을 길이
                    total += byteLen; //이제까지 읽은 길이
					

					byte[] tempBytes = new byte[ byteLen ];

					System.arraycopy( contentBytes, startPoint, tempBytes, 0, byteLen );

					if (field[i].get(data) == null)
						field[i].set(data, new byte[byteLen]);

					for(int k=0; k<byteLen; k++)
						 Array.setByte( field[i].get(data), k,tempBytes[k]);
				    
				}

				
				// FormatedEntity 구현 객체의 배열인 경우
				else if (fieldClass.isArray() )
				{
                    /*
                    String className = field[i].getType().getComponentType().getName();
                    Class entity = Class.forName(className);
                    */
                    Class<?> entity = field[i].getType().getComponentType();
                    
                    FormatedEntity child = null;
                    
                    try {    
                        child = (FormatedEntity) entity.newInstance();
                     }
				    catch(ClassCastException ccex) {
				        throw new ParsingException(entity.getName()+" type 배열은 아직 지원하지 않는 타입입니다");
				    }   
                        
                        
                    int childLen = getArraySum( child.getFieldsLength() ); //자식의 길이를 구한다.
                    
                    int ArrayNum = (inputLength-total) / childLen; //배열개수
                    
                    List<FormatedEntity> list = new ArrayList<>(ArrayNum);
                    
                    for(int k=0; k<ArrayNum; k++)
                    {
                        byte[] tempBytes = new byte[ childLen ];

					    System.arraycopy( contentBytes, startPoint, tempBytes, 0, childLen );
                        
                        parse(child, tempBytes);  
                          
                        list.add(k, child );
                        
                        total += childLen;
                        startPoint = total;
                        
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
                        //Class entity = Class.forName(fieldClassName);
                        
                        child = (FormatedEntity) fieldClass.newInstance(); //생성하고
                    }
                    
                    int byteLen = getArraySum( child.getFieldsLength() ); //총 읽은 길이
                    
                    total += byteLen; //이제까지 읽은 길이
				    byte[] tempBytes = new byte[ byteLen ];
                    
                    System.arraycopy( contentBytes, startPoint, tempBytes, 0, byteLen );
				    
				    parse(child, tempBytes);
				    field[i].set( data, child );
                }
                ++fieldLenNo;
			
			}// for

            /*
			if (inputLength != total)
				throw new ParsingException("input stream bigger than Data structure");
			*/	
			return total;	


	    }
	    catch(ParsingException pex)
	    {
	        throw pex;
	    }
	    
	    catch( Exception e )
		{
			//e.printStackTrace();
			
			ParsingException pe = new ParsingException(data.getClass().getName() + " "+ e.toString());
			pe.setParsingBytes(contentBytes);
			
			throw pe;
		}
    }

    
    
    

    private final static int parseShort( byte[] src, int offset ) 
    {
        return ((src[offset]&0xff) << 8) | (src[offset+1]&0xff);
    }

    
    private final static int parseInt( byte[] src, int offset ) 
    {
        return ((src[offset]&0xff) << 24) | ((src[offset+1]&0xff) << 16) |
              ((src[offset+2]&0xff) << 8) | (src[offset+3]&0xff);
    }

    private final static long parseLong( byte[] src, int offset ) 
    {
        return ((long) parseInt(src,offset) << 32 ) | 
              ((long) parseInt(src, offset+4) & 0xffffffffL );
    }


    private final static String getString(byte[] contentBytes, int startPoint, int total)
        throws java.io.UnsupportedEncodingException
    {
        return new String( contentBytes, startPoint, total-startPoint, ENCODE );
        //return new String( contentBytes, startPoint, total-startPoint);
  
    }


	
    /**
	 * 엔티티의 총 바이트 길이를 구한다.
	 *
	 * @param int[] 정수형 배열
	 * @return int 정수형 배열의 총합
	 **/
	private final static int getArraySum(int[] inArray)
	{
	    
	    int totalLen = 0;
	    
	    for(int j=0; j<inArray.length; j++)
	    {
	        //totalLen = totalLen+inArray[j];
	        totalLen += inArray[j];
	    }
	    
	    return totalLen;
	}
    



   
	/**
	* Entity에 들어있는 값을 가지고 format  된 byte[]를 생성한다
	*
	*/
	public byte[] getBytes(FormatedEntity entity) throws RuntimeException
	{
	    
	    StringBuffer resultBuffer = new StringBuffer();

		try
		{
    		Class<? extends FormatedEntity> c = entity.getClass();
			Field[] field = ReflectUtil.getFields(c);
            
    		
    		//ByteArrayBuffer bytesBuffer = new ByteArrayBuffer();
    		ByteArrayOutputStream bytesBuffer = new ByteArrayOutputStream(256);
    
    		int fieldCount = field.length;
    		int[] lengths = entity.getFieldsLength();
    
    		int lenNum = 0;
    		
    		Class<?> fieldClass;
    		
    
    		for (int i=0; i<fieldCount; i++)
    		{
    
    			fieldClass = field[i].getType();
    			
    			//String이면  
    			if(fieldClass == String.class)
    			{
    			    String data = (String) field[i].get( entity );
    			    
    			    byte[] bytes = new byte[ lengths[lenNum] ];
    			    
    			    if(data != null)
    			    {
    			        if(lengths[lenNum] == VAR_LENGTH_MODE)
    			        {
    			            bytes = data.getBytes(ENCODE);
    			            
    			        }
    			        else
    			        {
    			            //bytes = StringFormater.fillSpace(data ,lengths[lenNum] ).getBytes();
    			            byte[] strBytes = data.getBytes(ENCODE);
    			            
    			         // 한글인 경우를 위해
    			            if(strBytes.length > lengths[lenNum] )
		    	                System.arraycopy( strBytes, 0, bytes, 0, lengths[lenNum] );
		    	                
					        else
					            System.arraycopy( strBytes, 0, bytes, 0, strBytes.length );
    			        }
    			        
					}
		    		
		    		resultBuffer.append(Hexa.toHexString(bytes)).append("\t").append(field[i].getName()+":").append(data).append("\n");
		    		
		    		bytesBuffer.write (bytes);
    			}
    			
    
    
    			// int
    			else if(fieldClass == Integer.TYPE )
    			{
    			    int val = field[i].getInt( entity );
    			    byte[] bVal = getIntBytes(val);
    			    
    			    String sVal = Hexa.toHexString(bVal);
    			    
    			    resultBuffer.append(sVal).append("\t").append(field[i].getName()+":").append(val).append("\n");
    			    
    			    bytesBuffer.write( getIntBytes( field[i].getInt( entity ) ));
    			
    			}
    			
    			// long
    			else if(fieldClass == Long.TYPE )
    			{
    			    long val = field[i].getLong( entity );
    			    byte[] bVal = getLongBytes(val);
    			    
    			    String sVal = Hexa.toHexString(bVal);
    			    
    			    resultBuffer.append(sVal).append("\t").append(field[i].getName()+":").append(val).append("\n");
    			    
    				bytesBuffer.write ( getLongBytes( field[i].getLong( entity ) ));
    			}
    			
    			// short
    			else if(fieldClass == Short.TYPE )
    			{
    			    short val = field[i].getShort( entity );
    			    byte[] bVal = getShortBytes(val);
    			    
    			    String sVal = Hexa.toHexString(bVal);
    			    
    			    resultBuffer.append(sVal).append("\t").append(field[i].getName()+":").append(val).append("\n");
    			    
    				bytesBuffer.write ( getShortBytes( field[i].getShort( entity ) ));
    			}
    			
    				
    				
    			else if( fieldClass.isArray() )
    			{
    			
    				// byte[] 이면
    			    if( fieldClass.getComponentType()==Byte.TYPE)
    			    {
                    
    			    	// byte array의 길이를 field_length 변수에서 구한다.
    			    	int byteLen = lengths[lenNum];
                    
    			    	byte[] temp = new byte[byteLen];
    			    	for( int j=0; j<byteLen; j++)
    			    		temp[j] = Array.getByte( field[i].get(entity), j );
                    
                    
                        resultBuffer.append(Hexa.toHexString(temp)).append("\t").append(field[i].getName()+":").append("byte array").append("\n");
    			    	
    			    	bytesBuffer.write( temp );
    			    	lenNum++;
    			    }
    			    
    			    // FormatedEntity[]
    			    else
    			    {
    			        try
    			        {
    			            FormatedEntity[] child = (FormatedEntity[]) field[i].get(entity);
    			            
    			            for(int k=0; k<child.length; k++)
    			                bytesBuffer.write ( getBytes(child[k]) );
    			        }
		    	        catch(ClassCastException cce) {
		    	            throw new RuntimeException(fieldClass.getComponentType().getName()
		    	                                            +"는 아직 지원하지 않는 배열타입입니다");
		    	        }
    			    }		
    			
    		    }
    		
    		
    		    // char
    			else if(fieldClass == Character.TYPE )
    			{
    			    char val = field[i].getChar(entity);
    			    byte[] bVal = new byte[] {(byte) field[i].getChar(entity)};
    			    
    			    String sVal = Hexa.toHexString(bVal);
    			    
    			    resultBuffer.append(sVal).append("\t").append(field[i].getName()+":").append(val).append("\n");
    			    
    			
    			    bytesBuffer.write ( (byte) field[i].getChar(entity) );
    			}
    			
    			
    			// float
    			else if(fieldClass == Float.TYPE )
    				bytesBuffer.write ( getIntBytes( Float.floatToIntBits(field[i].getFloat(entity)) ));	
    			
    			
    			// double
    			else if(fieldClass == Double.TYPE )
    				bytesBuffer.write ( getLongBytes( Double.doubleToLongBits(field[i].getDouble(entity)) ));
    		
    		
    		
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
    			    
    			    byte[] agrBytes = getBytes(child);
    			    bytesBuffer.write (agrBytes);
    			}
    
    			lenNum++;
    	
    
    	    }
    	
    	    System.out.println(resultBuffer.toString());
    
    		return bytesBuffer.toByteArray();
    	}
    	 catch(RuntimeException re)
        {
            //re.printStackTrace();
            throw re;
        }
        catch(IOException ioe)
        {
            //ioe.printStackTrace();
            throw new RuntimeException(ioe.getMessage());
        }
        catch(IllegalAccessException iie)
        {
            iie.printStackTrace();
            throw new RuntimeException(iie.getMessage());
        }
      
    
    
    
    }



    // size 2
    private final static byte[] getShortBytes(int src)
    {
        byte[] data = new byte[2];
        data[0] = (byte)(src >> 8);
	    data[1] = (byte)(src);
	    
        
        return data;
    }
    
    // size 4
    private final static byte[] getIntBytes(int src)
    {
        byte[] data = new byte[4];
        data[0] = (byte)(src >> 24);
	    data[1] = (byte)(src >> 16);
	    data[2] = (byte)(src >> 8);
	    data[3] = (byte)src;
        
        return data;
        
    }
    
    // size 8
    private final static byte[] getLongBytes(long src)
    {
        byte[] data = new byte[8];
        data[0] = (byte)(src >> 56);
	    data[1] = (byte)(src >> 48);
	    data[2] = (byte)(src >> 40);
	    data[3] = (byte)(src >> 32);
        data[4] = (byte)(src >> 24);
	    data[5] = (byte)(src >> 16);
	    data[6] = (byte)(src >> 8);
	    data[7] = (byte)src;
        
        return data;
    }





	


}
