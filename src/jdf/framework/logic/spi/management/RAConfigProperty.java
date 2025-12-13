package jdf.framework.logic.spi.management;

/**
 * 
 * 
 * 
 * Resource Adapter(RA)가 가질 수 있는 Config항목을 의미한다.
 * 
 * <config-property> <description>CICS server name from CTG.</description>
 * <config-property-name>ServerName</config-property-name>
 * <config-property-type>java.lang.String</config-property-type>
 * <config-property-value>CICS</config-property-value> </config-property>
 * 
 * 
 * 
 * @author
 * @version 1.0
 */
public class RAConfigProperty
{

    private String name;

    private String type;

    private String value;

    /**
     * 기본생성자
     * 
     */
    public RAConfigProperty()
    {
    }

    /**
     * 이름을 가져온다.
     * 
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * 타입명을 가져온다.
     * java에 정의된 기본 자료형으로 반환한다.
     * 
     * @return
     */
    public String getType()
    {
        return type;
    }

    /**
     * 저장된 값을 반환한다.
     * 
     * @return
     */
    public String getValue()
    {
        return value;
    }

    /**
     * 이름을 설정한다.
     * 
     * @param string
     */
    public void setName(String string)
    {
        name = string;
    }

    /**
     * 타입을 설정한다.
     * 
     * @param string
     */
    public void setType(String string)
    {
        type = string;
    }

    /**
     * 값을 설정한다.
     * 
     * 
     * @param string
     */
    public void setValue(String string)
    {
        value = string;
    }

}