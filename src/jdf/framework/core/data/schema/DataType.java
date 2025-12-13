package jdf.framework.core.data.schema;


/**
 * <b><code>FieldType</code></b>
 * <p>
 * Legacy 시스템이나 일반적인 TCP/IP 소켓을 이용한 데이타 처리 시스템과 
 * 데이타를 주고 받기 위해 사용되는 데이타에서 하나의 필드가 가지는 
 * 데이타 타입을 정의한다.
 * </p>
 *
 * @author
 * @version 1.0
 */
public interface DataType
{
    // 입력/출력
    public final static int ANY = 0;
    
    
    // 입력 데이타 타입
    public final static int INPUT  = 1;
    
    
    // 출력 데이타 타입
    public final static int OUTPUT = 2;
    
    
    /**
     * 필드의 Acccess 가 공개적인 경우
     * 
     */
    public final static String PUBLIC_ACCESS_STR ="public";
    
    /**
     * 필드의 Access 가 비공개적인 경우
     * 
     */
    public final static String PRIVATE_ACCESS_STR ="private";
    
    /**
     * 공개적인 접근인 경우를 포괄적으로 의미하는 상수
     */
    public final static short PUBLIC_ACCESS = 1;
    
    /**
     * 접근이 제한적인 경우를 포괄적으로 의미하는 상수
     */
    public final static short PRIVATE_ACCESS = 0;
    
    
    
    
}
