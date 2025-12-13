/*
 * Created on 2004-05-18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.spi.cache;

import java.util.ArrayList;
import java.util.List;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;
import jdf.framework.core.pool.cache.CacheManager;
import jdf.framework.core.pool.cache.CacheManagerImpl;
import jdf.framework.core.util.SmartStringArray;



/**
 * LOGIC 에서 데이터 캐쉬를 담당하는 class
 * 
 *  - config.xml에서 /resource/anylogic/cache@manager-class 에 정의되어 사용되어 진다.
 *  - 이 class의 기능은 캐쉬내용을 우선 메모리에 저장하고 다른 WAS 서버가 있다면 그 내용을 전파하는
 *  기능을 가지고 있다.
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MultiServerCacheManager extends CacheManagerImpl implements CacheManager
{

	private List<String> urlList = new ArrayList<>();
	
    /**
     * 기본 생성자
     *
     */
	public MultiServerCacheManager()
	{
		super();
	}

    
    /**
     * 기본정보를 설정한다.
     * 캐쉬의 ID, 최대 저장갯수, 생존시간등을 설정한다.
     * 
     */
	public void setInfo(String name, int maxnum, long expireTime)
	{
		super.setInfo(name, maxnum, expireTime);
		
		init();
	}

	private void init()
	{

		try
		{
			Config conf = Configuration.lookup("/resource/anylogic/cache/HttpCachePropagator/target-server");

			String serverList = conf.getString("url");

			String[] urls = SmartStringArray.split(",", serverList);

			for (int i = 0; i < urls.length; i++)
			{
				urlList.add(urls[i]);
				Logger.debug.println("<MultiServerCacheManager> set URL:"+urls[i]);
			}

		}
		catch (Exception e)
		{
			Logger.warn.println("<MultiServerCacheManager> init error", e);
		}
	}

	/**
     * 데이터를 caching 한다.
     * 
	 * @see jdf.framework.core.pool.cache.CacheManager#insert(java.lang.Object, java.lang.Object)
	 */
	public synchronized void insert(Object key, Object obj)
	{
		//Logger.debug.println("--------------"+key);
		super.insert(key, obj);
		
		if(this.urlList.size()>0) {
			HttpCachePropagator post = new HttpCachePropagator();
			post.setUrlList(this.urlList);
			post.setInfo(HttpCachePropagator.INSERT, this.getName()  ,key, obj);
			post.start();
		}
	}

	/**
     * 이 caching 된 내용을 초기화 한다.
     * 
	 * @see jdf.framework.core.pool.cache.CacheManager#reset()
	 */
	public synchronized void reset()
	{
		//Logger.debug.println("<<--------------");
		
		super.reset();

		if(this.urlList.size()>0) {
			HttpCachePropagator post = new HttpCachePropagator();
			post.setUrlList(this.urlList);
			post.setInfo(HttpCachePropagator.REMOVE, this.getName()  ,null, null);
			post.start();
		}
		
	}
	
	
	

	/** 
     * isProp가 true 이면 다른 WAS 서버로 캐쉬 내용을 전파하고,
     * 그렇지 않으면 내부 메모리로만 보관한다.
     * 
	 * @see jdf.framework.core.pool.cache.CacheManager#insert(java.lang.Object, java.lang.Object, boolean)
	 */
	public void insert(Object key, Object obj, boolean isProp)
	{
		if(isProp)
			insert(key, obj);
		else
			super.insert(key, obj);
	}

	/**
	 * isProp 가 true 이면 다른 WAS 도 캐쉬 를 초기화 하고
     * 그렇지 않은 경우 자신만 초기화한다.
     * 
	 * @see jdf.framework.core.pool.cache.CacheManager#reset(boolean)
	 */
	public void reset(boolean isProp)
	{
		if(isProp)
			reset();
		else
			super.reset();
	}

}