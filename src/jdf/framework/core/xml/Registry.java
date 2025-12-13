/*
 * @(#)Registry.java
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

package jdf.framework.core.xml;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.GeneralConfiguration;
import jdf.framework.core.util.StringFormater;

import org.w3c.dom.Document;


/**
 * <b><code>Registry</code> </b>
 * <p>
 * <code>GeneralConfiguration</code> 객체를 확장하여 XML형태의 Config화일을 읽어 환경을 설정하기 위한
 * class이다. 기존의 Config interfacing하여 사용한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */

public final class Registry extends GeneralConfiguration
{

    private static Document cfgDocument = null;

    private static long cfgDocument_filetime = 0;

    /*
     * 설정화일의 마지막 수정일
     */
    private long last_modified = 0;

    /*
     * XML 화일을 읽는 Class
     */
    transient private XMLReferer referer = null;

    /*
     * 마지막으로 조회한 경로
     */
    private String lastLookupPath = null;

    /*
     * Serializable이 되었는지 유무
     */
    private static boolean isRemoteConfigMode = false;

    /*
     * XML문서에서 config를 찾을 위치, element명
     */
    private final static String ROOT_ELEMENT = "/config";

    private final static int ROOT_ELEMENT_LEN = ROOT_ELEMENT.length();

    private final static long FRESH_MIN_TIME = 1000;

    // 화일 체크하는 최소주기

    private long lastCheckTime = 0;

    // 마지막 체크 시간

    private String fullPath;

    // property 항목들
    // <property name="Name" value="anyframe"/>
    private static ConfigProperty[] props = new ConfigProperty[] {};

    static
    {

        if (configuration_file == null)
        {
            File default_file = null;

            default_file = new File(System.getProperty("user.dir"),
                    "config.xml");

            configuration_file = System.getProperty("config.file", default_file
                    .getAbsolutePath());

        }

    }

    /**
     * @exception ConfigurationException
     */
    public Registry() throws ConfigurationException
    {
        super();
        this.referer = getConfigXMLReferer();
        // Registry가 생성시 마다 XMLReferer도 새로 생성
    }

    /**
     * 초기 생성자
     * 
     * @exception ConfigurationException
     */
    public Registry(String fullPath) throws ConfigurationException
    {
        super(fullPath);
        this.referer = getConfigXMLReferer();
        // Registry가 생성시 마다 XMLReferer도 새로 생성

        this.referer.lookup(fullPath);

        this.fullPath = fullPath;

    }

    private static XMLReferer getConfigXMLReferer()
            throws ConfigurationException
    {
        XMLReferer xmlRef = null;
        try
        {
            File f = new File(configuration_file);
            if (cfgDocument_filetime != f.lastModified())
            {

                cfgDocument = DocBuilder.getDocument(f);
                cfgDocument_filetime = f.lastModified();

                xmlRef = new XMLReferer(cfgDocument);

                setProperties(xmlRef);
            }

            if (xmlRef == null)
                xmlRef = new XMLReferer(cfgDocument);

            return xmlRef;
        } catch (Exception e)
        {
            throw new ConfigurationException(e.toString());
        }
    }

    /**
     * xml에서 <properties>의 항목을 읽어 props 변수에 세팅한다.
     * 
     * @param xmlRef
     */
    private static void setProperties(XMLReferer xmlRef)
    {
        xmlRef.mark();
        xmlRef.lookup("/config/properties/property");

        List tmp = new ArrayList();
        tmp.add(new ConfigProperty("CONFIG_DIR", Configuration.getConfigPath()));
        
        
        while (xmlRef.next())
        {
            String name = xmlRef.getString("name");
            String value = xmlRef.getString("value");

            ConfigProperty p = new ConfigProperty(name, value);

          System.out.println(">>>>>>>>>." + name + "=" + value);
            tmp.add(p);
        }

        props = (ConfigProperty[]) tmp.toArray(new ConfigProperty[] {});

        xmlRef.reset();
    }

    /**
     * attribute 의 값을 가져온다.
     * 
     */
    public String getString(String attrName)
    {
        try
        {
            initialize();

            String val = referer.getString(attrName);
            return replaceWithProperty(attrName, val);
        } catch (Exception ex)
        {
        }

        return null;
    }

    /**
     * 해당 value에서 치환시킬 문자가 있으면 property 의 값으로 치환한다.
     * 
     * @param value
     * @return
     */
    private String replaceWithProperty(String attrName, String value)
    {
        if (props.length == 0 || value == null || value.indexOf("$") < 0)
            return value;
        String result = value;
        for (int i = 0; i < props.length; i++)
        {

            String replaceName = props[i].getReplaceName();
            String toVal = props[i].getValue();

            result = StringFormater.replaceStr(result, replaceName, toVal);

            //System.out.println(replaceName + " ===435== " + result);
            //  치환할 문자가 있는 경우만
            if (result.indexOf("$") < 0)
            {
                referer.setString(attrName, result);
                //System.out.println(attrName + "***34*****>>" + referer.getString(attrName));
                return result;
            }
            
            
        }

        return result;
    }

    /**
     * @exception ConfigurationException
     */
    public void initialize() throws ConfigurationException
    {
        checkSerializable();
        checkModify();
    }

    /**
     * 이 객체가 Serializable되었으면 referer가 null이 되므로 referer를 다시 생성하고, 다음 마지막으로
     * lookup한 곳이 있으면 다시 lookup하게 한다.
     */
    private void checkSerializable() throws ConfigurationException
    {
        if (referer == null)
        {
            this.referer = getConfigXMLReferer();
            this.referer.lookup(lastLookupPath);

        }

    }

    //private static Object lock = new Object();

    /**
     * 설정화일의 변경사항을 체크한다.
     *  
     */
    public void checkModify() throws ConfigurationException
    {
        try
        {
            // Remote Configuration Service를 실시하면 checkModify하지 않는다.
            if (isRemoteConfigMode)
                return;

            /* FRESH_MIN_TIME 기간만큼은 다시 checkModify() 하지 않는다 */
            long checkTime = System.currentTimeMillis();
            if (checkTime - lastCheckTime < FRESH_MIN_TIME)
                return;

            lastCheckTime = checkTime;

            File file = new File(configuration_file);

            if (!file.canRead())
                throw new ConfigurationException(this.getClass().getName()
                        + " - Can't open configuration file: "
                        + configuration_file);

            long fileTime = file.lastModified();
            if (last_modified == fileTime)
                return;

            this.referer = getConfigXMLReferer();
            this.referer.lookup(this.fullPath);

            //System.out.println("\n\n\n\n\n\n\n\nLOOKUP
            // ------------------------> "+this.fullPath);

            last_modified = fileTime;

        } catch (ConfigurationException e)
        {
            throw e;
        } catch (Exception e)
        {

            e.printStackTrace();

            last_modified = 0;
            throw new ConfigurationException(this.getClass().getName()
                    + " - Can't open configuration file: " + e.getMessage());
        }
    }

    /**
     * 새로운 위치를 찾은 Registry를 새로 생성하여 반납한다.
     *  
     */
    public Config lookup(String path) throws ConfigurationException
    {
        String fullPath = referer.getFullPath() + "/" + path;

        return new Registry(fullPath);
    }

    /***************************************************************************
     * 별도의 목적을 위하여
     */

    public List keys()
    {
        XMLReferer ref = new XMLReferer(configuration_file);

        List result = new ArrayList();

        findPath(ref, ROOT_ELEMENT, result);

        return result;
    }

    private static void findPath(XMLReferer refer, String nodeName, List keyList)
    {
        try
        {
            refer.lookup(nodeName);

            List list2 = refer.getTagList();

            for (int x = 0; x < list2.size(); x++)
            {
                String childName = (String) list2.get(x);

                findPath(refer, nodeName + "/" + childName, keyList);
            }

            refer.lookup(nodeName);

            List list = refer.getAttributeList();

            for (int x = 0; x < list.size(); x++)
            {
                //System.out.println(nodeName+"/"+list.get(x));

                String pathName = nodeName + "/" + list.get(x);

                keyList.add(pathName.substring(ROOT_ELEMENT_LEN));
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * InputStream에서 xml 을 읽어서 로드한다.
     * 
     * @param is
     * @throws Exception
     */
    public static void load(InputStream is) throws Exception
    {
        try
        {
            Document inDoc = DocBuilder.getDocument(is);

            // 미구현
            //referer = new XMLReferer(inDoc); // Registry가 생성시 마다 XMLReferer도
            // 새로 생성

            isRemoteConfigMode = true;
        } catch (Exception e)
        {
            throw e;
        }

    }

    static class ConfigProperty
    {
        private String name;

        private String value;

        private String replaceName;

        ConfigProperty(String name, String value)
        {
            this.name = name;
            this.value = value;

            this.replaceName = "${" + name + "}";
        }

        String getName()
        {
            return this.name;
        }

        String getReplaceName()
        {
            return this.replaceName;
        }

        String getValue()
        {
            return this.value;
        }

    }

}