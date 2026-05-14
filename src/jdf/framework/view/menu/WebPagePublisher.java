package jdf.framework.view.menu;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.http.JDFrameContextListener;
import jdf.framework.core.http.URLReader;
import jdf.framework.core.io.SmartFile;
import jdf.framework.core.log.Logger;
import jdf.framework.view.menu.entity.MenuItem;

import java.util.List;
import java.util.Map;


/**
 * 
 * web page 자동생성 모듈
 * 
 * @author
 *
 */
public class WebPagePublisher implements Runnable
{
    private final static String LOG_ID="<t:WebPublisher> "; 

    private static final String THREAD_NM = "anytemplet.publisher";

    private String domainName;

    private String documentRoot;

    private Map siteGenMenuMap;

    private boolean work = true;

    private String[] siteKeyArray;

    public WebPagePublisher(String domainName, String documentRoot, Map map)
    {
        this.domainName = domainName;
        this.documentRoot = documentRoot;
        this.siteGenMenuMap = map;
        
        Logger.info.println(LOG_ID+"domain:" + domainName);
		Logger.info.println(LOG_ID+"docRoot:" + documentRoot);
		Logger.info.println(LOG_ID+"pub num:" + map.size());
        

        siteKeyArray = (String[]) siteGenMenuMap.keySet().toArray(new String[] {});
    }

    public WebPagePublisher(Map map)
    {
        this.siteGenMenuMap = map;

        siteKeyArray = (String[]) siteGenMenuMap.keySet().toArray(new String[] {});
    }

    public void destroy()
    {
        Logger.debug.println(LOG_ID+"closing");
        this.work = false;
    }

    public void run()
    {
        waiting(10);

        try
        {

            Config conf = Configuration.lookup("/site");
            domainName = conf.getString("domain", "localhost");
            
            if(domainName.indexOf("http:")<0)
                domainName = "http://" + domainName;
            
            //documentRoot = conf.getString("documentRoot", "/home/htdocs");
            documentRoot = JDFrameContextListener.getDocumentRoot();
            
            /*
            if (this.domainName == null) {
                domainName = conf.getString("domain", "localhost");
                
                if(domainName.indexOf("http:")<0)
                    domainName = "http://" + domainName;
            }
                
            if (this.documentRoot == null)
                documentRoot = conf.getString("documentRoot", "/home/htdocs");
            */    

            Logger.info.println(LOG_ID+"START SERVICE. domain:" + domainName);

            while (work)
            {
                waiting(5);

                for (int i = 0; i < siteKeyArray.length; i++)
                {
                	waiting(1);
                	
                    try
                    {
                        String key = siteKeyArray[i];
                        List genList = (List) siteGenMenuMap.get(key);
                        if (genList != null)
                            generate(domainName, documentRoot, genList);
                    } catch (Exception ee)
                    {
                    }
                }
            }
        } catch (Exception e)
        {
        }
        //Logger.info.println(LOG_ID+"END SERVICE");
        System.out.println("[anytemplet.menu.WebPagePublisher] stop service");
    }

    /**
     * 
     * 전페이지를 publishing 한다.
     * 
     *  
     */
    public void publishAll()
    {
        for (int i = 0; i < siteKeyArray.length; i++)
        {

            String key = siteKeyArray[i];
            List genList = (List) siteGenMenuMap.get(key);
            if (genList != null)
            {
                for (int j = 0; j < genList.size(); j++)
                {
                    MenuItem menu = (MenuItem) genList.get(j);

                    if (generate(menu.getOriginPage(), menu.getUrl(), menu.getPageSize()))
                        menu.checkPublishTime();
                    waiting(5);

                }

            }
            //generate(domainName, documentRoot, genList);

        }
    }

    private void waiting(int time)
    {
        try
        {
            Thread.sleep(time * 1000);
        } catch (Exception e)
        {
        }
    }

    /**
     * html 페이지 생
     * 
     * @param sourcePage
     * @param targetPage
     * @param minPageSize
     * @return
     */
    public synchronized boolean generate(String sourcePage, String targetPage, int minPageSize)
    {
    	Logger.info.println(LOG_ID+"pubish "+ domainName + sourcePage);
    	
        try
        {
            URLReader reader = new URLReader(domainName + sourcePage);
            String content = reader.getContent();

            if (content.length() >= minPageSize)
            {
                String fileNm = documentRoot + targetPage;

                SmartFile file = new SmartFile(fileNm);
                file.setContent(content);

                Logger.info.println(LOG_ID+"publish:" + targetPage + "[" + content.length() + "]");

                return true;
            } else
            {
                Logger.warn.println(LOG_ID+"publish:" + targetPage + "[" + content.length() + "]");
            }

        } catch (Exception e)
        {
        	
            Logger.err.println(LOG_ID+"publish:" + targetPage, e);
        }

        return false;

    }

    private void generate(String domainName, String documentRoot, List menuList)
    {
        for (int i = 0; i < menuList.size(); i++)
        {
            MenuItem menu = (MenuItem) menuList.get(i);
            try
            {
                if (menu.getPublishTimeGap() > 0
                        && System.currentTimeMillis() - menu.getLastPublishTime() > menu.getPublishTimeGap())
                {
                    if (generate(menu.getOriginPage(), menu.getUrl(), menu.getPageSize()))
                        menu.checkPublishTime();
                    waiting(5);
                }

            } catch (Exception e)
            {
                Logger.err.println(LOG_ID+"HTML generate fail: " + menu.getUrl() + " " + e.toString());
            }
        }
    }

    /**
     * 
     * http connection 통해 해당 페이지를 읽은 후 static page를 생성하여 준다.
     * 
     * fullURL은 http://www.iblug.com/main.jsp filename은
     * /home/htdocs/index.html
     * 
     * @param fullUrl
     * @param filename
     * @throws Exception
     */
    public static void generate(String fullUrl, String filename) throws Exception
    {

        URLReader reader = new URLReader(fullUrl);
        String content = reader.getContent();

        //Config conf= Configuration.lookup("/site");
        //String documentRoot= conf.getString("documentRoot", "/home/htdocs");
        //SmartFile file= new SmartFile(documentRoot + filename);

        SmartFile file = new SmartFile(filename);
        file.setContent(content);
    }

    public void start()
    {

        try
        {

            // 기존의 Thread를 죽인다.
            int i = Thread.activeCount();
            Thread athread[] = new Thread[i];
            Thread.enumerate(athread);

            Logger.debug.println(LOG_ID+"active thread = " + i);

            for (int w = 0; w < i; w++)
            {
                if (athread[w] != null && THREAD_NM.equals(athread[w].getName()))
                {
                    // stop 말고 방법 강
                    athread[w].stop();
                    Logger.debug.println(LOG_ID+"destroy old publisher.");
                }

            }

        } catch (Throwable e)
        {
            e.printStackTrace();
        }

        (new Thread(this, THREAD_NM)).start();
    }
} 