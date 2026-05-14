package jdf.framework.logic.spi.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * <b><code>Protocol</code> </b>
 * <p>
 * Protocol에 의해 DataSet의 출력 데이타를 결정한다.
 * 
 * 
 * <pre>
 * 
 *  
 *   
 *   import jdf.framework.core.data.parser.Protocol;
 *   import jdf.framework.core.data.parser.ProtocolFactory;
 *   import jdf.framework.core.data.parser.DataSetParser;
 *   
 *   
 *   	    Protocol protocol = ProtocolFactory.getProtocol();
 *  
 *  	    DataSetParser dataParser = protocol.getParser();
 *  	    dataParser.setServerMode();
 *   
 *  
 * </pre>
 * 
 * 
 * 
 * </p>
 * 
 * @author
 * @version 1.0
 */

public class Protocol
{
    // protocol 명
    private String name;

    // parse driver명
    private DataSetParser parser;

    // protocol 에서 각 field 의 이름에 따른 FieldTypeDef 들의 저장소
    private Map maps = new HashMap();

    /**
     * 기본 생성자
     * 
     * @param name
     * @param parser
     */
    public Protocol(String name, DataSetParser parser)
    {
        this.name = name;
        this.parser = parser;
        this.parser.setProtocol(this);
    }

    /**
     * DataSetParser 객체를 return 한다.
     * 
     * 
     * @return jdf.framework.core.data.parser.DataSetParser
     */
    public DataSetParser getParser()
    {
        try
        {

            // parser 의 상태가 변하는 경우가 생겨서...
            DataSetParser newParser = (DataSetParser) parser.getClass().newInstance();

            newParser.setProtocol(this.parser.getProtocol());

            return newParser;

        } catch (Exception e)
        {
            return parser;
        }

        // return parser;
    }

    /**
     * 타입명에 해당하는 FieldParser 구현객체를 얻는다.
     * 
     * 
     */
    public FieldParser getFieldParser(String fieldTypeName)
    {
        return (FieldParser) maps.get(fieldTypeName);
    }

    /**
     * Returns the name.
     * 
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * 
     * 
     * 
     * 
     */
    void put(FieldParser field)
    {
        maps.put(field.getKeyName(), field);
    }

}