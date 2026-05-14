//Source file: D:\\PROJECT\\anylogic\\prototype\\src\\anylogic\\spi\\management\\DeploymentManager.java

package jdf.framework.logic.spi.management;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.io.SmartFile;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.BeanUtil;
import jdf.framework.core.xml.XMLReferer;
import jdf.framework.logic.adapter.java.JavaProcessor;
import jdf.framework.logic.adapter.java.ProcessorFactoryImpl;
import jdf.framework.logic.adapter.java.ResourceAdapterImtpl;
import jdf.framework.logic.spi.ResourceAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;


/**
 * Resource Adapter를 설치하고, 기본 Resource Adapter운영시 필요한 Business Logic(이하BL)
 * Descriptor를 읽고 로드시킨다.
 * 
 * 
 * 실질적인 Resource Adapter deploy 정보를 읽는것은 RADeployDescriptorFactory 이고 Business
 * Logic Descriptor 정보를 읽는것은 BLDescriptorFactory 이다
 * 
 * 이 클래스는 XML 이 저장되어있는 화일이나 DB에서 XML 문서정보까지만 가지고 오는 역활을 하고, 이 XML 문서를 각 Factory로
 * 넘겨 파싱한다음 각 Descriptor 정보를 가져온다.
 * 
 * @author
 * @version 1.0
 */
public class DeploymentManager
{
	private final static String LOG_ID="<l:DeploymentManager> ";

    private static DeploymentManager instance;

    static
    {
        instance = new DeploymentManager();
    }

    private boolean isLoad = false;

    // Resource-Adapter descriptor 저장소
    private Map<String, RADeployDescriptor> descriptorMap = new HashMap<>();

    // Resource-Adapter 객체 저장소
    private Map<String, ResourceAdapter> raMap = new HashMap<>();

    private DeploymentManager()
    {
    }

    /**
     * 생성자를 얻는다.
     * 
     * @return
     */
    public synchronized static DeploymentManager getInstance()
    {
        return instance;
    }

    /**
     * @roseuid 3E92734A01A5
     */
    public void init()
    {
    }

    /**
     * @roseuid 3E94F83300EA
     */
    public void getManagedConnectionFactory()
    {
    }

    /**
     * Resource Adapter 정보를 로드한다. 보통 ./config/RAdeploy 디렉토리의 xml 을 읽어서 초기화 한다.
     * 
     * 
     */
    public synchronized void load()
    {
        
        dropResourceAdapters();
        
        // RA deployment
        this.deployResourceAdapter();

        Logger.info.println("<DeploymentManager> ResourceAdapter deployed");

        // BL Descriptor load
        this.loadBLDescriptor();

        Logger.info.println("<DeploymentManager> BL Descriptor load");

        isLoad = true;
    }

    public boolean isLoad()
    {
        return this.isLoad;
    }

    /**
     * @roseuid 3E92395B008C
     */
    public void close()
    {
    }

    private void makeRADescriptor(String dir)
    {
        File file = new File(dir);
        file.mkdirs();

        SmartFile rafile = new SmartFile(dir, "dbms.xml");

        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<connector>\n"
                + "<adapter-name>dbms</adapter-name>\n" + "<resourceadapter>\n"
                + "<class>jdf.framework.logic.adapter.dbms.ResourceAdapterImpl</class>\n"
                + "<processor>jdf.framework.logic.adapter.dbms.DbmsProcessor</processor>\n"
                + "<processfactory>jdf.framework.logic.adapter.dbms.ProcessorFactoryImpl</processfactory>\n"
                + "</resourceadapter>\n" + "</connector>";

        rafile.setContent(content);
        Logger.info.println(LOG_ID+"create basic(dbms) RA Descriptor");
    }

    /**
     * 
     * 
     */
    private void deployJavaResourceAdapter()
    {
        String adapterName = "java";

        RADeployDescriptor descriptor = new RADeployDescriptor();
        descriptor.setName(adapterName);
        descriptor.setResourceAdpaterClassName(ResourceAdapterImtpl.class.getName());
        descriptor.setProcessorClassName(JavaProcessor.class.getName());
        descriptor.setProcessorFactory(new ProcessorFactoryImpl());
        descriptor.setConfigPropertyArray(new RAConfigProperty[] {});

        ResourceAdapter ra = new ResourceAdapterImtpl();
        ra.start();

        raMap.put(adapterName, ra);
        descriptorMap.put(adapterName, descriptor);

        Logger.info.println(LOG_ID+"JAVA adapter deploy complete");

    }

    /**
     * resource-adapter deploy 정보가 담긴 xml화일을 읽고
     * 
     * RADeployDescriptor 객체를 생성하여 보관한다.
     * 
     * 
     * @author
     * 
     * To change the template for this generated type comment go to
     * Window>Preferences>Java>Code Generation>Code and Comments
     * 
     * @roseuid 3E9255480148
     */
    private synchronized void deployResourceAdapter()
    {
        String[] files = getDescriptorFiles();

        // RA Descriptor 가 존재하지 않으면 dbms RA descriptor를 만든다.
        //  사용자가 anylogic을 사용하는지도 모르게한다.
        if (files.length == 0)
        {
            makeRADescriptor(getRootDir());
            files = getDescriptorFiles();
        }

        Logger.info.println(LOG_ID+"load RA adapters. " + getRootDir());

        deployJavaResourceAdapter();

        for (int j = 0; j < files.length; j++)
        {
            try
            {

                RADeployDescriptor descriptor = new RADeployDescriptor();

                Logger.info.println(LOG_ID+"read " + files[j]);

                // XML Document load
                XMLReferer doc = new XMLReferer(getRootDir(), files[j]);

                doc.lookup("/connector");

                // 이름 정의
                String name = doc.find("adapter-name").getText();

                Logger.info.println(LOG_ID+"[" + name + "] adapter initializing ...");

                descriptor.setName(name);

                // resource adapter class 정의
                doc.lookup("resourceadapter");
                String className = doc.find("class").getText();
                descriptor.setResourceAdpaterClassName(className);
                if (className == null || className.length() == 0)
                    throw new Exception("adapter classname error");
                //Logger.info.println(LOG_ID+"RA class name:" + className);

                String pClassName = doc.find("processor").getText();
                descriptor.setProcessorClassName(pClassName);

                if (pClassName == null || pClassName.length() == 0)
                    throw new Exception("processor classname error");
                //Logger.info.println(LOG_ID+"Processor class name:" + pClassName);

                // process info factory 정의
                String pifName = doc.find("processfactory").getText();
                ProcessorFactory fac = (ProcessorFactory) Class.forName(pifName).newInstance();
                descriptor.setProcessorFactory(fac);
                //Logger.info.println(LOG_ID+"ProcessInfoFactory name:" + pifName);

                Class<?> ra_class = Class.forName(className);
                ResourceAdapter ra = (ResourceAdapter) ra_class.newInstance();

                // properties 정의
                doc.lookup("config-property");

                List<RAConfigProperty> configList = new ArrayList<>();
                while (doc.next())
                {
                    String cname = doc.find("config-property-name").getText();
                    String ctype = doc.find("config-property-type").getText();
                    String cvalue = doc.find("config-property-value").getText();

                    try
                    {
                        RAConfigProperty rconfig = new RAConfigProperty();

                        rconfig.setName(cname);
                        rconfig.setType(ctype);
                        rconfig.setValue(cvalue);

                        configList.add(rconfig);

                        BeanUtil.setBeanProperty(ra, cname, cvalue);
                        Logger.debug.println(LOG_ID+"set property name:" + cname + " val:" + cvalue);

                    } catch (Exception eee)
                    {
                        Logger.err.println(LOG_ID+"set property[" + cname + "] error.");

                    }

                }

                descriptor.setConfigPropertyArray((RAConfigProperty[]) configList.toArray(new RAConfigProperty[] {}));

                ra.start();

                raMap.put(name, ra);

                descriptorMap.put(name, descriptor);

                Logger.info.println(LOG_ID+"[" + name + "] deploy complete");

            } catch (Exception e)
            {
                Logger.err.println(LOG_ID+"deploy err", e);
            }
        }

    }

    private synchronized void dropResourceAdapters()
    {
        try
        {
            Set<String> keySet = raMap.keySet();

            Object[] keyArray = keySet.toArray();

            for (int i = 0; i < keyArray.length; i++)
            {

                String adapterNm = "";
                try
                {
                    ResourceAdapter ra = (ResourceAdapter) raMap.get(keyArray[i]);
                    adapterNm = ra.getClass().getName();
                    ra.stop();
                    Logger.info.println(LOG_ID+"drop adapter success" + adapterNm);
                } catch (Exception ee)
                {
                    Logger.err.println(LOG_ID+"drop adapter error " + adapterNm, ee);
                }

            }

        } catch (Exception e)
        {

        }
    }

    /**
     * 어댑터명에 해당하는 ResourceAdapter 구현객체를 얻는다.
     * 
     * @param name
     * @return
     */
    public synchronized ResourceAdapter getResourceAdapter(String name)
    {
        return (ResourceAdapter) raMap.get(name);
    }

    /**
     * 어댑터명에 해당하는 RADeployDescriptor 구현객체를 얻는다.
     * 
     */
    public synchronized RADeployDescriptor getRADescriptor(String name)
    {
        return (RADeployDescriptor) descriptorMap.get(name);
    }

    /**
     * 모든 RADeployDescriptor 정보를 배열로 얻는다.
     * 
     * @return
     */
    public synchronized RADeployDescriptor[] getRADescriptors()
    {
        return (RADeployDescriptor[]) descriptorMap.values().toArray(new RADeployDescriptor[] {});
    }

    /**
     * 
     * BL descriptor를 읽어 로드한다.
     * 
     * @author
     * 
     * To change the template for this generated type comment go to
     * Window>Preferences>Java>Code Generation>Code and Comments
     * 
     * @roseuid 3E92590E004E
     * 
     */
    public synchronized void loadBLDescriptor()
    {
        BLContextFactory mgr = BLContextFactory.getInstance();
        mgr.initialize();

    }

    /**
     * Business Object의 정보가 있는 화일 리스트를 반환
     * 
     */
    private static String[] getDescriptorFiles()
    {

        String[] files = getDescriptorFiles(new File(getRootDir()));

        return files;
    }

    /**
     * Business Object의 정보가 있는 화일들의 디렉토리
     * 
     */
    private static String getRootDir()
    {
        try
        {
            Config conf = Configuration.lookup("/resource/anylogic/resource-adapter");
            String dir = conf.getString("dir");

            if (dir == null || dir.length() == 0)
                dir = "." + File.separator + "RAdeploy";

            if (dir.indexOf(".") == 0)
                dir = Configuration.getConfigPath() + dir.substring(1);

            return dir;
        } catch (Exception e)
        {
            Logger.err.println(LOG_ID+"getRootDir", e);
            return Configuration.getConfigPath() + File.separator + "RAdeploy";
        }

    }

    private static String[] getDescriptorFiles(File dir)
    {
        if ((!dir.exists()) || (!dir.isDirectory()))
        {
            return new String[0];
        }

        String[] contents = dir.list(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                if (name.endsWith(".xml"))
                    return true;
                else
                    return false;
            }
        });

        return contents;
    }

}