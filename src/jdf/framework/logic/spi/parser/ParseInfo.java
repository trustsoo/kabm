package jdf.framework.logic.spi.parser;




/**
 * <b><code>ParseInfo</code></b>
 * <p>
 * Legacy 시스템이나 일반적인 TCP/IP 소켓을 이용한 데이타 처리 시스템과 
 * 데이타를 주고 받기 위해 사용되는 데이타에서 하나의 필드가 가지는 
 * 데이타 타입을 정의한다.
 * </p>
 *
 * @author
 * @version 1.0
 */
public class ParseInfo
{
    
    public int fileType;
    public int toType;
    
    public int fieldSize;
    
    public int decimalPoint;
    public int stPoint;
    
    
    
    
    ParseInfo(int fileType, int toType, int decimalPoint, int stPoint, int fieldSize)
    {
        this.fileType=fileType;
        this.toType=toType;
        this.decimalPoint=decimalPoint;
        this.stPoint=stPoint;
        this.fieldSize=fieldSize;
    }
    
    
    
}
