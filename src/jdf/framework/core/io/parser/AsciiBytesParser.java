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

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.io.BytesParser;
import jdf.framework.core.io.FormatedEntity;
import jdf.framework.core.io.ParsingException;
import jdf.framework.core.util.AsciiUtil;
import jdf.framework.core.util.ByteToStringPool;
import jdf.framework.core.util.ReflectUtil;
import jdf.framework.core.util.StringFormater;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 문자뿐만 아니라 숫자도 ASCII형태로 파싱한다.
 * </p>
 * 
 * @author advan94
 * @version 1.1
 */
public final class AsciiBytesParser implements BytesParser
{
    private static String ENCODE = "KSC5601";
    
    private final static int VAR_LENGTH_MODE = 0;
    
    private final static byte PLUS_CHAR = (byte) '+';
    private final static byte ROW_TOKEN = (byte) '\n';
    private final static byte COL_TOKEN = (byte) '\t';

	/**
	 * 
	 * @uml.property name="pool"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static ByteToStringPool pool;
	
	/*public void setEncoding(String encode)
	{
		AsciiBytesParser.ENCODE = encode;
		if(pool != null)
			pool.setEncoding(encode);
	}*/
	
	public static void setEncoding(String encode)
	{
		AsciiBytesParser.ENCODE = encode;
	}
	
	
	static
    {
        try {
            Config conf = Configuration.lookup("/network/parser/pool");
            int size = conf.getInt("size", 1000);
            boolean isPool = conf.getBoolean("enable", true);
            
            conf = Configuration.lookup("/network/parser/encode");
            String encode = conf.getString("type");            
            
            setEncoding(encode);
			if (isPool) {
				pool = new ByteToStringPool(size, ENCODE);
			}
        }
        catch(Exception e)
        {
        }
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
		int total = 0;
        int fieldLenNo = 0;
			
		
		try {
			int inputLength = contentBytes.length;
			
			
			Class c = data.getClass();
			
			
			Field[] field = ReflectUtil.getFields(c);   // public만  
            //Field[] field = ReflectUtil.getDeclaredFields(c); // 모든 필드....
            //Field[] field = data.getClass().getFields();
			
			
			int[] lengths = data.getFieldsLength( ); //각 필드의 길이

			//int fieldCount = field.length; //필드 개수
            
            
            String blit = null;

			for(int i=0; i<field.length; i++)
			{
			    int startPoint = total;
			    
			    Class fieldClass = field[i].getType();
			    
			    
			    //System.out.println("++++++++++++++++++++++++++ "+field[i].getName());
				
				if( fieldClass == Long.TYPE)
				{
				    total += lengths[fieldLenNo];
				    
				    if(contentBytes[startPoint] == PLUS_CHAR)
				        startPoint++;
				    
				    long currentLong = AsciiUtil.bytesToLong(contentBytes,startPoint, lengths[fieldLenNo]);
				    
				    field[i].setLong( data, currentLong );
				}
				else if( fieldClass == Integer.TYPE)
				{
				    total += lengths[fieldLenNo];
				    
				    int currentInt = AsciiUtil.bytesToInt(contentBytes,startPoint, lengths[fieldLenNo]);
				    field[i].setInt( data, currentInt);
				}
				else if(fieldClass == Character.TYPE)
				{
				    total += lengths[fieldLenNo];
				    
				    char currentChar = (char) contentBytes[startPoint];
				    field[i].setChar( data, currentChar);
				}
				else if( fieldClass == String.class)
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
				    
				    //System.out.println("str trans = "+total);
				}
				
				/* double형은 소수점을 표시해야 하므로
				   총길이 정보와 소수점길이정보를 구해야 한다.
				*/
				else if( fieldClass == Float.TYPE)
				{
				    int tLen=FormatedEntity.getTotalLen( lengths[fieldLenNo] ); //총길이
				    int pLen=FormatedEntity.getPointLen( lengths[fieldLenNo] ); //소수점길이
				    
				    total += tLen;
				    blit = getString(contentBytes, startPoint,total);
				    
				    float f = getFloat( blit, tLen,pLen);
				    field[i].setFloat( data, f );
				}
				
				else if( fieldClass == Double.TYPE)
				{
				    int tLen=FormatedEntity.getTotalLen( lengths[fieldLenNo] ); //총길이
				    int pLen=FormatedEntity.getPointLen( lengths[fieldLenNo] ); //소수점길이			    
				    
				    total += tLen;
				    blit = getString(contentBytes, startPoint,total);
				    
				    double d = getDouble( blit, tLen,pLen);
				    field[i].setDouble( data, d );
				}
				
				
				// char 배열이면
				else if (fieldClass.isArray() &&  fieldClass.getComponentType()==Character.TYPE )
				{
				    
				    int charLen = lengths[fieldLenNo]; //읽을 길이
				    
				    if(charLen == 0)
				        charLen = inputLength-total;
                    
                    total += charLen; //이제까지 읽은 길이
                    
                    byte[] tempBytes = new byte[ charLen ];
					System.arraycopy( contentBytes, startPoint, tempBytes, 0, charLen );

					char[] tempChars = new char[ charLen ];
					
					for (int cc = 0; cc < charLen ; cc++)
						tempChars[cc] = (char) tempBytes[cc];
					
					field[i].set(data, tempChars);
					
				}

				// byte 배열이면
				//else if ( fieldClass.getComponentType().getName().equals("byte") )
				else if (fieldClass.isArray() &&  fieldClass.getComponentType()==Byte.TYPE )
				{
				    
				    int byteLen = lengths[fieldLenNo]; //읽을 길이
				        // byteLen = inputLength-total;
				    
				    if(byteLen == 0)
				        byteLen = inputLength-total;
                    
                    total += byteLen; //이제까지 읽은 길이
                    
                    byte[] tempBytes = new byte[ byteLen ];
					System.arraycopy( contentBytes, startPoint, tempBytes, 0, byteLen );
					
					field[i].set(data, tempBytes);
					
                
                    /*
                    Object f = field[i].get(data);
                
					if (f == null)
					{
						field[i].set(data, new byte[byteLen]);
						f = field[i].get(data);
					}
					
					
						
                    int end = startPoint+byteLen;
					for(int k=startPoint; k<end; k++)
						 Array.setByte( f, k,contentBytes[k]);
					
					*/	
				    
				}
                
				
				// FormatedEntity 구현 객체의 배열인 경우
				else if (fieldClass.isArray())
				{
                    //String className = fieldClass.getComponentType().getName();
                    //Class entity = Class.forName(className);
                    
                    Class entity = fieldClass.getComponentType();
                    
                    FormatedEntity child = null;
                    
                    try {    
                        child = (FormatedEntity) entity.newInstance();
                     }
				    catch(ClassCastException ccex) {
				        throw new ParsingException(entity.getName()+" type 배열은 아직 지원하지 않는 타입입니다");
				    }   
                        
                        
                    int childLen = FormatedEntity.getArraySum( child.getFieldsLength() ); //자식의 길이를 구한다.
                    
                    int ArrayNum = 0;
                    
                    
                    try
                    {
                        FormatedEntity[] arryChild = (FormatedEntity[]) field[i].get( data );
                        ArrayNum = arryChild.length;
                    }
                    catch(Exception eee)
                    {
                    }
                    
                    
                    if(ArrayNum == 0)
                        ArrayNum = (inputLength-total) / childLen; //배열 개수
                        
                    //System.out.println("--------------------   ArrayNum ======= "+ArrayNum);  
                    
                    List list = new ArrayList(ArrayNum);
                    
                    
                    
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
                    
                    
                    int byteLen = FormatedEntity.getArraySum( child.getFieldsLength() ); //총 읽을 길이
                    
                    if(byteLen == 0)    // 길이가 세팅되지 않은 경우는 제일 마지막에 있는 가변데이타라고 가정한다.
                        byteLen = inputLength-total;


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
	    catch(ArrayIndexOutOfBoundsException aie)
	    {
	        ParsingException pe = new ParsingException(fieldLenNo+"th field ArrayIndexOutOfBounds");
	        throw pe;
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
     * FormatedEntity 객체에 입력값으로 들어온 byte stream을 토큰을 이용하여 parsing하여 값을 넣는다.
     * FormatedEntity 객체의 배열 타입이나 내장형은 취급하지 않는다.
     *
     * @param data FormatedEntity를 구현한 객체
     * @param contentBytes 읽어들일 byte stream
     * @param token parsing 에 사용할 토큰
     * @return int parsing한 bytes stream 길이
     */

    public int parse(FormatedEntity data, byte[] contentBytes, char token ) throws ParsingException
	{
	    int fieldLenNo = 0;
			
		try {
			int inputLength = contentBytes.length;

			Class c = data.getClass();
			
			// public member
			Field[] field = ReflectUtil.getFields(c); 

			int[] lengths = data.getFieldsLength( );

	        String blit = null;

	        int total = 0;
		    int tokenPosition = 0;
		    int startPoint = 0;
		    int takeBytes = 0;
		    
		    boolean isFieldToken = false;
		    boolean isRowToken = false;
		    boolean isFindToken = false;

			for(int i=0; i < field.length; i++)
			{
			    startPoint = total;
			    isFieldToken = false;
			    isRowToken = false;
			    isFindToken = false;
			    
			    takeBytes = 0;
			    int idx = 0;
			    
			    
			    for(idx = 1; tokenPosition < inputLength; idx++, tokenPosition++) 
			    {
			    	if (contentBytes[tokenPosition] == (byte)token) {
			    		isFieldToken = true;
			    		isFindToken = true;
			    		break;
			    	}

			    	if (contentBytes[tokenPosition] == ROW_TOKEN) {
			    		isRowToken = true;
			    		isFindToken = true;
			    		break;
			    	}
			    }
			    // token 을 못 찾은 경우
			    if (tokenPosition == inputLength)  
			    {
			    	if (idx == 1) {
			    		break;
			    	}
			    	else {
			    		idx--;
			    	}
			    }

			    if (isFindToken == true) {
			    	takeBytes = idx - 1;
			    }
			    else {
			    	takeBytes = idx;
			    }
			    
			    Class fieldClass = field[i].getType();

				if( fieldClass == Long.TYPE)
				{
				    if(contentBytes[startPoint] == PLUS_CHAR){
				        startPoint++;
				    }
				    
				    long currentLong = AsciiUtil.bytesToLong(contentBytes, startPoint, takeBytes);
				    
				    field[i].setLong( data, currentLong );
				}
				else if( fieldClass == Integer.TYPE)
				{
					int currentInt = AsciiUtil.bytesToInt(contentBytes, startPoint, takeBytes);
				    field[i].setInt( data, currentInt);
				}
				else if(fieldClass == Character.TYPE)
				{
				    char currentChar = (char) contentBytes[startPoint];
				    field[i].setChar( data, currentChar);
				}
				else if( fieldClass == String.class)
				{
				    if ( lengths[fieldLenNo]== VAR_LENGTH_MODE ) {
				        blit = getString(contentBytes, startPoint, inputLength);
				    }
				        
				    else
				    {    
				    	blit = getString(contentBytes, startPoint, tokenPosition);
				    }
				    
				    field[i].set( data, blit.trim());
				    
				    //System.out.println("str trans = "+total);
				}
				
				/**
				 * double형은 소수점을 표시해야 하므로
				 * 총길이 정보와 소수점길이정보를 구해야 한다.
				 */
	            else if( fieldClass == Float.TYPE)
				{
				    int tLen=FormatedEntity.getTotalLen( lengths[fieldLenNo] ); //총길이
				    int pLen=FormatedEntity.getPointLen( lengths[fieldLenNo] ); //소수점길이
				    
				    //blit = getString(contentBytes, startPoint, tokenPosition - 1);
				    blit = getString(contentBytes, startPoint, startPoint + takeBytes);
				    
				    //float f = getFloat( blit, tLen,pLen);
				    float f = getFloat(blit, takeBytes, pLen);
				    field[i].setFloat( data, f );
				}
				// double
				else if( fieldClass == Double.TYPE)
				{
				    int tLen=FormatedEntity.getTotalLen( lengths[fieldLenNo] ); //총길이
				    int pLen=FormatedEntity.getPointLen( lengths[fieldLenNo] ); //소수점길이
				    
				    //blit = getString(contentBytes, startPoint, tokenPosition - 1);
				    blit = getString(contentBytes, startPoint, startPoint + takeBytes);
				    
				    //double d = getDouble( blit, tLen, pLen);
				    double d = getDouble(blit, takeBytes, pLen);
				    field[i].setDouble( data, d );
				}
				// char[]
				else if (fieldClass.isArray() &&  fieldClass.getComponentType()==Character.TYPE )
				{
				    int charLen = takeBytes; //읽을 길이
				    
				    if(charLen == 0)
				        charLen = inputLength-total;
	                
	                byte[] tempBytes = new byte[ charLen ];
					System.arraycopy( contentBytes, startPoint, tempBytes, 0, charLen );
	
					char[] tempChars = new char[ charLen ];
					
					for (int cc = 0; cc < charLen ; cc++)
						tempChars[cc] = (char) tempBytes[cc];
					
					field[i].set(data, tempChars);
					
				}
				// byte[]
				else if (fieldClass.isArray() &&  fieldClass.getComponentType()==Byte.TYPE )
				{
				    int byteLen = takeBytes;
				    
				    if(byteLen == 0)
				        byteLen = inputLength-total;
	                
	                byte[] tempBytes = new byte[ byteLen ];
					System.arraycopy( contentBytes, startPoint, tempBytes, 0, byteLen );
					
					field[i].set(data, tempBytes);
				}
				//
				// FormatedEntity[]
				// 제약조건 : 항상 마지막  필드일 때만 적용 가능하다.
				else if (fieldClass.isArray())
				{
	                Class entity = fieldClass.getComponentType();

	                FormatedEntity child = null;

	                try {    
	                    child = (FormatedEntity) entity.newInstance();
	                 }
				    catch(ClassCastException ccex) {
				        throw new ParsingException(entity.getName()+" type 배열은 아직 지원하지 않는 타입입니다");
				    }   

	                int childLen = FormatedEntity.getArraySum( child.getFieldsLength() ); //자식의 길이를 구한다.
	                
	                int ArrayNum = 0;

	             // 여기서 제대로 구해질지는 의문이다.
	                try
	                {
	                    FormatedEntity[] arryChild = (FormatedEntity[]) field[i].get( data );
	                    ArrayNum = arryChild.length;
	                    //System.out.println("1.ArrayNum : " + ArrayNum);
	                }
	                catch(Exception eee)
	                {
	                }
	                
	                byte[] temp = new byte[inputLength - total];

	                if(ArrayNum == 0) {
	                	System.arraycopy(contentBytes, startPoint, temp, 0, inputLength - total);
	                	//ArrayNum = getTokenCount(temp, Byte.toString(ROW_TOKEN));
	                	ArrayNum = getTokenCount(temp, "\n");
	                    //System.out.println("2.ArrayNum : " + ArrayNum);
	                }
	                    
	                List list = new ArrayList();
	                
	                for(int k=0; k < ArrayNum; k++)
	                {
	                	temp = new byte[inputLength - total];
	                	System.arraycopy(contentBytes, startPoint, temp, 0, inputLength - total);
	                	
	                	int oneRowSize = getLengthToToken(temp, ROW_TOKEN);
	                	byte[] tempBytes = new byte[oneRowSize];
	            
					    System.arraycopy( contentBytes, startPoint, tempBytes, 0, oneRowSize);
	                    
	                    parse(child, tempBytes, token);  
	                      
	                    list.add(k, child );
	                    
	                    total += oneRowSize;
	                    startPoint = total;
	                    
	                    if(k != (ArrayNum - 1)) {
	                        child = (FormatedEntity) entity.newInstance();
	                    }
	                }
	                
	                Object[] objList = list.toArray();
	                
	                // Array.newInstance(entity,ArrayNum)
	                if (field[i].get(data) == null) {
					    field[i].set(data, Array.newInstance(entity,ArrayNum) );
	                }
					    
					for(int k=0; k<ArrayNum; k++) {
					    Array.set( field[i].get(data), k, objList[k]);
					}

			        //throw new ParsingException(entity.getName()+" type 배열은 아직 지원하지 않는 타입입니다");
				
				}
				// FormatedEntity
				else 
				{

	                FormatedEntity child = null;
	                
	                try {
	                    child = (FormatedEntity) field[i].get( data );
	                }
				    catch(ClassCastException ccex) {
				        throw new ParsingException(fieldClass.getName()+"는 아직 지원하지 않는 타입입니다");
				    }
				    
	                if(child == null) 
	                {
	                    child = (FormatedEntity) fieldClass.newInstance(); //���ϰ�
	                }
	                
	                int byteLen = inputLength - total;
	                byte[] tempBytes = new byte[byteLen];
	                
	                System.arraycopy( contentBytes, startPoint, tempBytes, 0, byteLen );
	                
	                Field[] field2 = ReflectUtil.getFields(child.getClass());
	                
	                int oneRowBytesSize = getLengthToToken(tempBytes, COL_TOKEN, field2.length);
	                //Logger.debug.println("field.length : "+field2.length);

	                byte[] oneRowBytes = new byte[oneRowBytesSize];
	                System.arraycopy(contentBytes, startPoint, oneRowBytes, 0, oneRowBytesSize);
	                
				    idx = parse(child, oneRowBytes, (char)COL_TOKEN);

				    field[i].set( data, child );
				}
		
				total += idx;
				tokenPosition = total;
				
	            ++fieldLenNo;
		    }// for(){}
		    
			return total;	
	    }
	    catch(ArrayIndexOutOfBoundsException aie)
	    {
	        ParsingException pe = new ParsingException(fieldLenNo+"th field ArrayIndexOutOfBounds");
	        throw pe;
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
    
    private int getLengthToToken(byte[] src, byte token) 
    {
    	int count = 1;
    	
    	for(int i = 0; i < src.length; count++, i++)
    	{
    		if (src[i] == token)
    		{
    			break;
    		}
    	}
    	
    	if (count > src.length)
    	{
    		return -1;
    	}
    	
    	return count;
    }
    
    
    private int getLengthToToken(byte[] src, byte token, int fieldCount) 
    {
    	int count = 0;
    	int i = 0;
    	for( i= 0; i < src.length; i++)
    	{
    		if(src[i] == token) count++;
    		if(count == fieldCount) {i++;break;}    			
    	}
    	
    	if (i > src.length)
    	{
    		return -1;
    	}
    	
    	return i;
    }
    
    
    
    private int getTokenCount(byte[] src, String tokens) {
    	int count = 0;
    	byte[] tokenBytes = tokens.getBytes();
    	
    	for(int i = 0; i < src.length; i++)
    	{
    		for(int j = 0; j < tokenBytes.length; j++)
    		{
    			if (tokenBytes[j] == src[i]) 
    			{
    				count++;
    				break;
    			}
    		}
    	}
    	
    	return count;
    }

    
    private static final String getString(byte[] contentBytes, int startPoint, int total)
        throws java.io.UnsupportedEncodingException
    {
        if (pool == null)
			return new String( contentBytes, startPoint, total-startPoint, ENCODE );
		else
			return pool.get(contentBytes, startPoint, total-startPoint);
    }


    private static float getFloat(String x, int total, int point)
    {
        int real = total-point;
        
        StringBuffer x2 = new StringBuffer();
        x2.append( x.substring(0,real))
          .append(".")
          .append( x.substring( real) );
        
        Float f = new Float(x2.toString());
        return f.floatValue();
    }


    private static double getDouble(String x, int total, int point)
    {
        int real = total-point;
        
        StringBuffer x2 = new StringBuffer();
        x2.append( x.substring(0,real))
          .append(".")
          .append( x.substring( real) );
        
        Double d = new Double(x2.toString());
        return d.doubleValue();
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
	* Entity에 들어있는 값을 가지고 format 된 byte[]를 생성한다
	*
	* RuntimeException  StringFormater
	* IOException ByteArrayOutputStream
	*/
	public byte[] getBytes(FormatedEntity entity) 
	    throws RuntimeException
	{
	    ByteArrayOutputStream bytesBuffer = new ByteArrayOutputStream();
	    
	    try
	    {
	        Class c = entity.getClass();
	        
            Field[] field = ReflectUtil.getFields(c); // only public member
		    
		    int[] lengths = entity.getFieldsLength();
            
            //System.out.println("------- class name:"+ c.getName());
            //System.out.println("------- len size:"+lengths.length);
            //for(int x=0;x<lengths.length; x++)
            //    System.out.println("------- len :"+lengths[x]);
            
		    int lenNum = 0;
		    
		    Class fieldClass;
		    //String fieldClassName="";
            
		    for (int i=0; i<field.length; i++)
		    {
		        fieldClass = field[i].getType();
		    	//fieldClassName =  fieldClass.getName();
		    	
		    	//System.out.println("---------- "+field[i].getName());
		   
		    	//String�̸� 
		    	if(fieldClass == String.class)
		    	{
		    	    String data = (String) field[i].get( entity );
		    	    
		    	    if(data==null)
		    	        data="";
		    	    
		    	    byte[] bytes = new byte[ lengths[lenNum] ];
		    	    
		    	    if(lengths[lenNum] == VAR_LENGTH_MODE)
		    	        if (pool == null)
							bytes = data.getBytes(ENCODE);
						else
							bytes = pool.get(data);
		    	        
		    	    else
		    	    {
		    	        bytes = StringFormater.fillSpace( (String) field[i].get( entity ) ,lengths[lenNum] ).getBytes();
		    	    
		    	    
		    	        if(bytes.length > lengths[lenNum] )
		    	        {
		    	            byte[] reBytes = new byte[ lengths[lenNum] ];
		    	            
					        System.arraycopy( bytes, 0, reBytes, 0, lengths[lenNum] );
					        
					        bytes = reBytes;
					    }
					}
		    		bytesBuffer.write (bytes);
		    	}
		    	// long
		    	else if(fieldClass == Long.TYPE) {
		    		bytesBuffer.write( AsciiUtil.longToBytes(field[i].getLong(entity), lengths[lenNum]) );
		    	}
		    	// int
		    	else if(fieldClass == Integer.TYPE) {
		    		bytesBuffer.write( AsciiUtil.intToBytes(field[i].getInt(entity), lengths[lenNum]) );
		    	}
		    	// double
		    	else if(fieldClass == Double.TYPE)
		    	{
		    	    double d = field[i].getDouble(entity);
		    	    int tLen=FormatedEntity.getTotalLen( lengths[lenNum] ); //총길이
				    int pLen=FormatedEntity.getPointLen( lengths[lenNum] ); //소수점길이
		    	    
		    	    bytesBuffer.write( AsciiUtil.longToBytes(Math.round(d), tLen-pLen) );
		    	    
		    	    if(pLen != 0)
		    	    {
		    	        String format = "0.0000000000000000000000".substring(0, pLen+2);
		    	        
                        java.text.DecimalFormat decFormat = new java.text.DecimalFormat(format);
		                String pointVal= decFormat.format(d);
		                int pointLen = pointVal.length();
		                bytesBuffer.write (pointVal.substring(pointLen-pLen,pointLen).getBytes() );
		    	    }
		    	}
		    	
		    	else if(fieldClass == Float.TYPE)
		    	{
		    	    float f = field[i].getFloat(entity);
		    	    int tLen=FormatedEntity.getTotalLen( lengths[lenNum] ); //총길이
		    	    
				    int pLen=FormatedEntity.getPointLen( lengths[lenNum] ); //소수점길이
		    	    bytesBuffer.write( AsciiUtil.intToBytes(Math.round(f), tLen-pLen) );
		    	    
		    	    if(pLen != 0)
		    	    {
		    	        String format = "0.0000000000000000000000".substring(0, pLen+2);
		    	        
                        java.text.DecimalFormat decFormat = new java.text.DecimalFormat(format);
		                String pointVal= decFormat.format(f);
		                int pointLen = pointVal.length();
		                bytesBuffer.write (pointVal.substring(pointLen-pLen,pointLen).getBytes() );
	                    
	                    /*
		    	        System.out.println("x1="+AsciiUtil.longToBytes(Math.round(d), tLen-pLen));
		    		    System.out.println("x2="+pointVal);
		    		    System.out.println("x2="+pointVal.substring(pointLen-pLen,pointLen));
		    	        */
		    	    }
		    	    
		    	}

				// char
		    	else if(fieldClass == Character.TYPE)
		    		bytesBuffer.write( field[i].getChar(entity) );

				// char[] 이면
		    	else if(fieldClass.isArray() && fieldClass.getComponentType()==Character.TYPE)
		    	{
            
		    		// byte array의 길이를 field_length 변수에서 구한다.
		    		int charLen = lengths[lenNum];
            
		    		byte[] temp = new byte[charLen];
		    		for( int j=0; j<charLen; j++)
		    			temp[j] = (byte) Array.getChar( field[i].get(entity), j );
            
            
		    		bytesBuffer.write( temp );
		    		//lenNum++;
		    	}
		    	
		    	// byte[] 이면
		    	else if(fieldClass.isArray() && fieldClass.getComponentType()==Byte.TYPE)
		    	{
            
		    		// byte array의 길이를 field_length 변수에서 구한다.
		    		int byteLen = lengths[lenNum];
            
		    		byte[] temp = new byte[byteLen];
		    		for( int j=0; j<byteLen; j++)
		    			temp[j] = Array.getByte( field[i].get(entity), j );
            
            
		    		bytesBuffer.write( temp );
		    		//lenNum++;
		    	}
		    	else if(fieldClass.isArray() )
		    	{
		    	    try
		    	    {
		    	        FormatedEntity[] child = (FormatedEntity[]) field[i].get(entity);
		    	        
		    	        for(int k=0; k<child.length; k++)
		    	            bytesBuffer.write( getBytes(child[k]) );
		    	    }
		    	    catch(ClassCastException cce) {
		    	        throw new RuntimeException(fieldClass.getComponentType().getName()
		    	                                        +"는 아직 지원하지 않는 배열타입입니다");
		    	    }
		    	}
		    	else 
		    	{
		    	    FormatedEntity child=null;

		    	    try {
		    	        child = (FormatedEntity) field[i].get( entity );
		    	    }
		    	    catch(ClassCastException cce) {
						throw new RuntimeException(fieldClass.getName()+"는 아직 지원하지 않는 타입입니다");
		    	    }
		    	        
		    	    byte[] agrBytes = getBytes(child);
		    	    bytesBuffer.write (agrBytes);
		    	}
		    	
		    	
            
		    	lenNum++;
		    	
		    	//System.out.println("==================== end "+lenNum);
		    
            
		    }
		    return bytesBuffer.toByteArray();
	    }
	    catch(NullPointerException ne)
        {
            return bytesBuffer.toByteArray();
        }
        catch(RuntimeException re)
        {
            throw re;
        }
        catch(IllegalAccessException iie)
        {
            throw new RuntimeException(iie.getMessage());
        }
        catch(IOException ioe)
        {
            throw new RuntimeException(ioe.getMessage());
        }
    }

	/**
	* Entity에 들어있는 값을 가지고 format 된 byte[]를 생성한다
	*
	* RuntimeException  StringFormater
	* IOException ByteArrayOutputStream
	*/
	public byte[] getBytes(FormatedEntity entity, char token) throws RuntimeException
	{
	    ByteArrayOutputStream bytesBuffer = new ByteArrayOutputStream();
	    
	    try
	    {
	        Class c = entity.getClass();
	        
	        // only public member
            Field[] field = ReflectUtil.getFields(c); 
		    
		    int[] lengths = entity.getFieldsLength();
		    int lenNum = 0;
		    
		    Class fieldClass;
            
		    boolean tokenStampped = false;

		    for (int i=0; i<field.length; i++)
		    {
		    	//StopWatch sw = new StopWatch();

		    	tokenStampped = false;
		        fieldClass = field[i].getType();
		        //System.out.println("field : " + fieldClass.getName());		   
		    	//String 
		    	if(fieldClass == String.class)
		    	{
		    	    String data = (String) field[i].get( entity );
		    	    
		    	    if(data==null)
		    	    {
		    	        data="";
		    	    }
		    	    
		    	    byte[] bytes = new byte[ lengths[lenNum] ];
		    	    
		    	    if(lengths[lenNum] == VAR_LENGTH_MODE)
		    	    {
		    	        if (pool == null)
		    	        {
							bytes = data.getBytes(ENCODE);
		    	        }
						else
						{
							bytes = pool.get(data);
						}
		    	    }
		    	    else
		    	    {
		    	    	bytes = data.getBytes(ENCODE);
		    	    
		    	        if(bytes.length > lengths[lenNum] )
		    	        {
		    	            byte[] reBytes = new byte[ lengths[lenNum] ];
		    	            
					        System.arraycopy( bytes, 0, reBytes, 0, lengths[lenNum] );
					        
					        bytes = reBytes;
					    }
					}
		    		bytesBuffer.write (bytes);
		    	}
		    	// long
		    	else if(fieldClass == Long.TYPE) {
		    		String longString = field[i].getLong(entity) + "";
		    		bytesBuffer.write(longString.getBytes());
		    	}
		    	// int
		    	else if(fieldClass == Integer.TYPE) {
		    		String intString = field[i].getInt(entity) + "";
		    		bytesBuffer.write(intString.getBytes());
		    	}
		    	// double
		    	else if(fieldClass == Double.TYPE)
		    	{
		    	    String doubleString = field[i].getDouble(entity) + "";
		    	    bytesBuffer.write(doubleString.getBytes());
		    	}
		    	
		    	else if(fieldClass == Float.TYPE)
		    	{
		    	    String floatString = field[i].getFloat(entity) + "";
		    	    bytesBuffer.write(floatString.getBytes());
		    	}
				// char
		    	else if(fieldClass == Character.TYPE)
		    	{
		    		bytesBuffer.write( field[i].getChar(entity) );
		    	}
				// char[]
		    	else if(fieldClass.isArray() && fieldClass.getComponentType()==Character.TYPE)
		    	{
		    		// byte array의 길이를 구한다.
		    		int charLen = Array.getLength(field[i].get(entity)); 

		    		byte[] temp = new byte[charLen*2]; //token 포함

		    		for(int j = 0; j < charLen; j += 2) {
		    			temp[j] = (byte) Array.getChar(field[i].get(entity), j);
		    			if (j == (charLen - 1)) {
		    				temp[j+1] = ROW_TOKEN;
		    			}
		    			else {
		    				temp[j+1] = (byte)token;
		    			}
		    		}
            
		    		bytesBuffer.write( temp );
		    		tokenStampped = true;
		    	}
		    	// byte[]
		    	else if(fieldClass.isArray() && fieldClass.getComponentType()==Byte.TYPE)
		    	{
		    		int byteLen = Array.getLength(field[i].get(entity));
            
		    		byte[] temp = new byte[byteLen*2];

		    		for( int j=0; j < byteLen; j += 2) {
		    			temp[j] = Array.getByte( field[i].get(entity), j );
		    			if (j == (byteLen - 1)){
		    				temp[j+1] = ROW_TOKEN;
		    			}
		    			else {
		    				temp[j+1] = (byte)token;
		    			}
		    		}

		    		bytesBuffer.write( temp );
		    		tokenStampped = true;
		    	}
		    	else if(fieldClass.isArray() )
		    	{
		    	    try
		    	    {
		    	        FormatedEntity[] child = (FormatedEntity[]) field[i].get(entity);
		    	        
		    	        for(int k=0; k<child.length; k++){
		    	            bytesBuffer.write( getBytes(child[k], token));
		    	        }
		    	        
		    	        tokenStampped = true;
		    	    }
		    	    catch(ClassCastException cce) {
		    	        throw new RuntimeException(fieldClass.getComponentType().getName()
		    	                                        +"는 아직 지원하지 않는 배열타입입니다");
		    	    }
		    	}
		    	else 
		    	{
		    	    FormatedEntity child=null;

		    	    try {
		    	        child = (FormatedEntity) field[i].get( entity );
		    	    }
		    	    catch(ClassCastException cce) {
						throw new RuntimeException(fieldClass.getName()+"는 아직 지원하지 않는 배열타입입니다");
		    	    }
		    	        
		    	    byte[] agrBytes = getBytes(child, token);
		    	    bytesBuffer.write (agrBytes);
		    	    tokenStampped = true;
		    	}

		    	if (tokenStampped == false) {
		    		if (i == (field.length - 1 )) {
		    			//System.out.println("ROW_TOKEN STAMPPED " + (int)ROW_TOKEN);
		    			bytesBuffer.write((int)ROW_TOKEN);
		    		}
		    		else {
		    			//System.out.println("TOKEN STAMPPED " + (int)token);
		    			bytesBuffer.write((int)token);
		    		}
		    	}

		    	lenNum++;
		    	//System.out.println("time : " + sw.getEllapseds());
		    }
		    return bytesBuffer.toByteArray();
	    }
	    catch(NullPointerException ne)
        {
            return bytesBuffer.toByteArray();
        }
        catch(RuntimeException re)
        {
            throw re;
        }
        catch(IllegalAccessException iie)
        {
            throw new RuntimeException(iie.getMessage());
        }
        catch(IOException ioe)
        {
            throw new RuntimeException(ioe.getMessage());
        }

	}
	
	/*   
	public static void main(String[] args)
	{
		try
		{
			anyframe.io.BytesParser parser = new anyframe.io.parser.AsciiBytesParser();
		
			FormatedEntity entity = new com.ci.receiver.memory.TestEntity();

			byte[] data = new byte[]{66,49,50,51,97,98,48};
			parser.parse( entity, data );
			
			System.out.println(entity);

			byte[] data1 = parser.getBytes(entity);
			for (int i = 0; i< data1.length;i++ )
				System.out.println(data1[i]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	*/
}
