/*
 * @(#)BytesParserFactory.java
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

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;



public class BytesParserFactory
{

	/**
	 * 
	 * @uml.property name="asciiParser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static BytesParser asciiParser = new jdf.framework.core.io.parser.AsciiBytesParser();

	/**
	 * 
	 * @uml.property name="binaryParser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static BytesParser binaryParser = new jdf.framework.core.io.parser.BinaryBytesParser();

	/**
	 * 
	 * @uml.property name="delimiterParser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static BytesParser delimiterParser = new jdf.framework.core.io.parser.DelimiterBytesParser();

	/**
	 * 
	 * @uml.property name="defaultParser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static BytesParser defaultParser;

    
    static
    {
        try {
            Config conf = Configuration.lookup("/network/parser");
            String className = conf.getString("className","jdf.framework.core.io.parser.BinaryBytesParser");
            
            defaultParser = (BytesParser) Class.forName(className).newInstance();
        }
        catch(Exception e)
        {
        }
    }

	
    /**
	 * FormatedEntity 객체에 입력값으로 들어온 byte stream을 parsing하여 값을 넣는다.
	 * 
	 * @param data FormatedEntity를 구현한 객체
	 * @param byte 읽어들일 byte stream
	 * @return int parsing한 bytes stream 길이
	 * 
	 * @uml.property name="asciiParser"
	 */
	public static BytesParser getAsciiParser() {
		return asciiParser;

	}

	/**
	 * 
	 * @uml.property name="binaryParser"
	 */
	public static BytesParser getBinaryParser() {
		return binaryParser;
	}


    public static BytesParser getBinaryParser(boolean fillSpace)
    {
        return new jdf.framework.core.io.parser.BinaryBytesParser(fillSpace);
    }

	/**
	 * 
	 * @uml.property name="delimiterParser"
	 */
	public static BytesParser getDelimiterParser() {
		return delimiterParser;
	}

    
    
    public static BytesParser getDelimiterParser(String delimiter)
    {
        return new jdf.framework.core.io.parser.DelimiterBytesParser(delimiter);
    }
    
    
    public static BytesParser getDelimiterParser(String del1, String del2)
    {
        return new jdf.framework.core.io.parser.DelimiterBytesParser(del1, del2);
    }
    
}
