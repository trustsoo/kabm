package jdf.framework.core.data.cci;

import java.util.Properties;


/**
 * ConnectionSpec 구현 Class
 * 
 * 
 * @author
 *
 */
public class SimpleConnectionSpec implements ConnectionSpec
{
    private Properties props; 
    
    private boolean isTransactionSupport = false;
    
    /**
     * 생성자
     * 
     * @param props
     * @param isTransactionSupport
     */
    public SimpleConnectionSpec(Properties props,boolean isTransactionSupport )
    {
        this.props=props;
        this.isTransactionSupport = isTransactionSupport;
    }
    
    

    /**
     * 속성값을 얻는다.
     * 
     */
    public String getProperty(String key)
    {
        return this.props.getProperty(key);
    }
    
    /**
     * 속성값을 설정한다.
     * 
     * @param key
     * @param val
     */
    public void setProperty(String key, String val )
    {
        this.props.setProperty(key, val);
    }

    /**
     * transaction 지원여부
     */
    public boolean isTransactionSupport()
    {
        return this.isTransactionSupport;
    }

}