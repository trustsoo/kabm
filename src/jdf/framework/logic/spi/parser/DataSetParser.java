package jdf.framework.logic.spi.parser;



import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ExternalData;
import jdf.framework.core.data.schema.IOSchema;


/**
 * jdf.framework.core.data.DataSet 객체를 다흔 형태로 parsing 하기 위한 추상 class
 * 
 * @author
 *
 */
public abstract class DataSetParser
{
    // parser 가 CLIENT 모드에서 사용되는지 SERVER 모드에서 사용되는지여부
    public static final int CLIENT = 0;
    public static final int SERVER = 1;

    protected int mode = CLIENT;    
    
    // Protocol 객체
    protected Protocol protocol;
    
    

        
    public void setClientMode()
    {
        this.mode = CLIENT;
    }
        
    
    public void setServerMode()
    {
        this.mode = SERVER;
    }
    
    
    
    public int getMode()
    {
        return mode;
    }

    public void setProtocol(Protocol protocol)
    {
        this.protocol = protocol;
    }


    public Protocol getProtocol()
    {
        return this.protocol;
    }
  

    
    public abstract FieldParser getFieldParserInstance();
    
    
    
    public abstract ExternalData transExternalData(IOSchema schema, DataSet input)
        throws TranslationException;
    

    public abstract DataSet transDataSet(IOSchema schema, ExternalData input)
        throws TranslationException;
    
    public abstract void transDataSet(IOSchema schema, ExternalData input, DataSet output)
        throws TranslationException;
    
    
}