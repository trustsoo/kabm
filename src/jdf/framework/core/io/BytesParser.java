/*
 * @(#)BytesParser.java
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
 
package jdf.framework.core.io;




public interface BytesParser
{	

	 /**
     * FormatedEntity 객체에 입력값으로 들어온 byte stream을 parsing하여 값을 넣는다.
     *
     * @param data FormatedEntity를 구현한 객체
     * @param byte 읽어들일 byte stream
     * @return int parsing한 bytes stream 길이
     */
    public int parse(FormatedEntity data, byte[] contentBytes ) 
        throws ParsingException;
	
    
   /**
	* Entity에 들어있는 값을 가지고 format 된 byte[]를 생성한다
	*
	* RuntimeException  StringFormater
	* IOException ByteArrayOutputStream
	*/
	public byte[] getBytes(FormatedEntity entity) 
	    throws RuntimeException;
	
}
