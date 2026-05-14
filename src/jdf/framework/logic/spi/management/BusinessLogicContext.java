//Source file: D:\\PROJECT\\anylogic\\prototype\\src\\anylogic\\spi\\management\\BLDescriptor.java

package jdf.framework.logic.spi.management;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.cci.Connection;
import jdf.framework.core.data.cci.ConnectionSpec;
import jdf.framework.core.data.cci.SimpleConnectionSpec;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.logic.spi.ResourceAdapter;

import java.io.File;
import java.util.Properties;


/**
 Business Logic Context는 
 BL을 수행하기 위한 ResourceAdapter 객체와
 IOSchema 정보를 가지고 있는 class이
 
 
 
 @author
 @version 1.0
 */
public class BusinessLogicContext implements ConnectionSpec
{

    // BL 이름
    private String name;

    // BL에 해당하는 adapter 명
    private ResourceAdapter ra;

    // BL의 IO schema 정보
    private IOSchema ioschema;

    // io schema화일의 수정일
    private long modifyTime;

    private long lastCheckTime;

    // ioschema의 디렉토리
    private String dir;

    // ioschema의 화일
    private String filename;

    public BusinessLogicContext(String name, ResourceAdapter ra, IOSchema ioschema)
    {

        this.name= name;
        this.ra= ra;
        this.ioschema= ioschema;

        // 생성일을 마지막 체크 time
        this.lastCheckTime= System.currentTimeMillis();
    }

    /**
     IOSchema 정보를 반납한다
     @roseuid 3E92614D000F
     */
    public IOSchema getIOSchema()
    {
        return this.ioschema;
    }

    /**
     @roseuid 3E92616500AB
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * ResourceAdapter를 반납한다.
     * 
     * @return
     */
    public ResourceAdapter getResourceAdapter()
    {
        return this.ra;
    }

    /**
     * BLD의 파일 수정시간을 설정한다.
     * 
     * @param time
     */
    public void setModifyTime(long time)
    {
        this.modifyTime= time;
    }

    /**
     * BLD 파일의 수정시간을 가져온다.
     * 
     * @return
     */
    public long getModifyTime()
    {
        return this.modifyTime;
    }

    /**
     * BLD의 경로와 이름을 설정한다.
     * 
     * @param dir
     * @param name
     */
    public void setFileInfo(String dir, String name)
    {
        this.dir= dir;
        this.filename= name;
    }

    /**
     * BLD에 해당하는 java.io.File 객체를 얻는다.
     * 
     * @return
     */
    public File getIOSchemaFile()
    {
        return new File(dir, filename);
    }

    /**
     * BLD의 디렉토리 정보를 얻는다.
     * 
     * @return
     */
    public String getDir()
    {
        return this.dir;
    }

    /**
     * 파일명을 얻는다.
     * 
     * @return
     */
    public String getFilename()
    {
        return this.filename;
    }

    /**
     * BLD파일을 check(load)한 마지막 시간정보를 설정한다.
     * 
     * @param time
     */
    public void setCheckTime(long time)
    {
        this.lastCheckTime= time;
    }

    /**
     * BLD 의 정보를 마지막으로 load 한 시간정보를 반환한다.
     * 
     * @return
     */
    public long getCheckTime()
    {
        return this.lastCheckTime;
    }

    private Properties props= new Properties();

    private boolean isTransactionSupport= false;

    void setProperty(String key, String value)
    {
        if (key == null)
        {
            System.out.println("key is null.");
            return;
        }

        if (value == null)
        {
            //System.out.println(key+"'s value is null.");
            return;
        }

        this.props.setProperty(key, value);
    }

    void setTransactionSupport(boolean onOff)
    {
        this.isTransactionSupport= onOff;
    }

    /**
     * 속성정보를 가져온다.
     * 
     * @see ConnectionSpec#getProperty(String)
     */
    public String getProperty(String key)
    {
        return props.getProperty(key);
    }

    /**
     * 트랜잭션 지원여부
     * 
     * @see ConnectionSpec#isTransactionSupport()
     */
    public boolean isTransactionSupport()
    {
        return this.isTransactionSupport;
    }

    /** 
     * jdf.framework.core.data.cci.Connection 구현객체를 얻는다.
     * 
     * @see ConnectionSpec#getAdapterConnection()
     */
    public Connection getAdapterConnection()
    {
        return null;
    }
    /**
     * Login 정보(DataSet) 를 얻는다.
     * 
     * @see ConnectionSpec#getLoginDataSet()
     */
    public DataSet getLoginDataSet()
    {
        return null;
    }
    
    /**
     * jdf.framework.core.data.cci.Connection 구현객체를 설정한다.
     * 
     * @see ConnectionSpec#setAdapterConnection(Connection)
     */
    public void setAdapterConnection(Connection conn)
    {
        return;

    }
    
    /**
     * login 정보를 가지고 있는 DataSet을 설정한다.
     * 
     * @see ConnectionSpec#setLoginDataSet(DataSet)
     */
    public void setLoginDataSet(DataSet input)
    {
        return;

    }
    
    /**
     * SimpleConnectionSpec 객체를 생성
     * 
     * @return
     */
    public SimpleConnectionSpec createSimpleConnectionSpec()
    {
        Properties newProps = (Properties) this.props.clone();
        
        return new SimpleConnectionSpec(newProps, this.isTransactionSupport);
    }
}