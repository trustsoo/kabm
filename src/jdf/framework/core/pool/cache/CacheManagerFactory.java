/*
 * Created on 2004-05-11
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.core.pool.cache;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.data.schema.CacheInfo;
import jdf.framework.core.log.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 
 * CacheManager 구현 객체를 관리하는 Factory Class
 * 
 * 
 * <pre>
 * 
 *  
 *   ex)
 *   CacheManagerFactory cmf = CacheManagerFactory.getInstance();
 *   
 *   CacheInfo info = new CacheInfo();
 *   
 *   CacheManager cm = cmf.createCacheManager(&quot;/test/object&quot;, info);
 *   CacheManager cm = cmf.createCacheManager(target.getClass().getName(), info);
 *  
 *   
 *   
 *  
 * </pre>
 * 
 * @author
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CacheManagerFactory
{

    private static CacheManagerFactory instance;

    private Map managerMap = new HashMap();

    private Object[] managerArray = new Object[0];

    private static Class cacheMgrClass = null;

    // 모든 캐쉬를 총괄 관리하는 map
    private Map cacheObjectCenter = new HashMap();

    private CacheManagerFactory()
    {

    }

    /**
     * 초기 생성자를 반환한다.
     * 
     * @return
     */
    public static CacheManagerFactory getInstance()
    {
        if (instance == null)
        {
            instance = new CacheManagerFactory();

            try
            {

                Config conf = Configuration.lookup("/resource/anylogic/cache");

                String classNm = conf.getString("manager-class", "jdf.framework.core.pool.cache.CacheManagerImpl");

                cacheMgrClass = Class.forName(classNm);

                Logger.info.println("<CacheManagerFactory> set manager class:" + classNm);

            } catch (Exception e)
            {
                Logger.warn.println("<CacheManagerFactory> set manager class error", e);
                cacheMgrClass = CacheManagerImpl.class;
            }

        }

        return instance;
    }

    public Map createMap(String uri)
    {
        Map map = new HashMap();
        this.cacheObjectCenter.put(uri, map);

        return map;
    }

    /**
     * 모든 캐쉬내용을 지운다.
     * 
     *  
     */
    public void clearCache()
    {
        Set keySet = this.cacheObjectCenter.keySet();
        
        
        Iterator iter = keySet.iterator();
        
        while(iter.hasNext())
        {
            Object key = iter.next();
            Map cache = (Map) this.cacheObjectCenter.get(key);
            
            cache.clear();
        }

        Logger.info.println("<CacheManagerFactory> clear ALL cache object");
    }

    /**
     * 특정 uri 의 캐쉬를 지운다. uri는 anylogic에서 xbrl/common/getTaxonomy 와 같은 tr코드 이다.
     * 
     * @param uri
     */
    public void clearCache(String uri)
    {
        Map cache = (Map) this.cacheObjectCenter.get(uri);
        cache.clear();

        Logger.info.println("<CacheManagerFactory> clear cache object : " + uri);
    }

    /**
     * 
     * CacheManager 구현 객체를 생성한다.
     * 
     * 
     * @param uri
     * @param cacheInfo
     * @return
     */
    public synchronized CacheManager createCacheManager(String uri, CacheInfo cacheInfo)
    {
        CacheManager mgr = null;

        String name = uri;
        int maxnum = cacheInfo.getMaxObjectCount();
        long expireTime = cacheInfo.getExpireSecondTime();

        try
        {
            mgr = (CacheManager) cacheMgrClass.newInstance();
            mgr.setInfo(name, maxnum, expireTime);

            this.managerMap.put(uri, mgr);

            this.managerArray = this.managerMap.entrySet().toArray();
        } catch (Exception e)
        {
            Logger.err.println("<CacheManagerFactory> getCacheManager error", e);
        }

        return mgr;
    }

    /**
     * 기존에 생성된 CacheManager를 반납한다. 없으면 null
     * 
     * @param uri
     * @return
     */
    public CacheManager getCacheManager(String uri)
    {
        return (CacheManager) this.managerMap.get(uri);
    }

    /**
     * 현재 관리되고 있는 CacheManager의 갯수를 return
     * 
     * @return
     */
    public int getCacheManagerSize()
    {
        return this.managerArray.length;
    }

    /**
     * CacheManager를 가져온다.
     * 
     * @param i
     * @return
     */
    public CacheManager getCacheManager(int i)
    {
        return (CacheManager) this.managerArray[i];
    }

    /**
     * 초기화
     * 
     *  
     */
    public synchronized void reset()
    {
        this.managerMap.clear();

        this.managerArray = new Object[0];
    }

}