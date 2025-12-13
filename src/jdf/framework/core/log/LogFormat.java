package jdf.framework.core.log;


/**
 * <p>
 * 일반적인 byte stream에서 Log 출력형태를 얻기위한 추상 Class
 * </p>
 *
 * <pre>
 * 다음과 같은 출력을 얻을 수 있다.
 * 6E 61 6D 65 20 20 20 20 20 20 00 86 14 73 00  : name      
 * 00 1C 54 00 00 00 1F 55 35 31 9A              :
 * </pre>
 *
 * @author
 * @version 1.0
 */


public interface LogFormat
{
    /*
    public String formating(String data)
    {
        return formating( data.getBytes(), data.length() );
    }
    
    
    public String formating(byte[] data)
    {
        return formating( data, data.length );
    }
    
    
    
    public String formating(String data, int size)
    {
        return formating( data.getBytes(), size );
    }
    
    
    
    public abstract String formating(byte[] data, int size);
    */
    
    public String formating(LogInfo log);
    
}

