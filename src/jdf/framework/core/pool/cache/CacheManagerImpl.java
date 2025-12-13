/*
 * Created on 2004-05-14
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.core.pool.cache;

import java.util.LinkedList;
import java.util.Map;

import jdf.framework.core.log.Logger;


/**
 * 
 * CacheManager 구현객체
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */ 
public class CacheManagerImpl implements CacheManager
{

	private Map cache;

	private LinkedList list = new LinkedList();

	private String name;

	private int maxnum;

	private long expireTime;
	
	/**
     * 기본생성자
     * 
	 */
	public CacheManagerImpl()
	{
	}

    /**
     * 정보 설정
     */
	public void setInfo(String name, int maxnum, long expireTime)
	{
		this.name = name;
		this.maxnum = maxnum;
		this.expireTime = expireTime;
		
		
		this.cache = CacheManagerFactory.getInstance().createMap(name); 
	}
	
	/**
     *  캐쉬명을 반환
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * 캐쉬할 객체를 입력한다.
	 * 
	 * @param key
	 * @param obj
	 */
	public synchronized void insert(Object key, Object obj)
	{
		if (this.list.size() >= this.maxnum)
		{
			Object preKey = this.list.removeFirst();

			this.cache.remove(preKey);

		}

		CacheObject cobj = new CacheObject(obj, this.expireTime);

		this.cache.put(key, cobj);

		this.list.addLast(key);
	}

	/**
	 * 캐쉬된 객체를 호출한다.
	 * 
	 * @param key
	 * @return
	 */
	public synchronized Object call(Object key)
	{
		CacheObject cobj = (CacheObject) cache.get(key);

		if (cobj != null)
		{

			if (cobj.isExpired())
			{
				this.cache.remove(key);
				this.list.remove(key);

				return null;
			}

		}
		else
			return null;

		Logger.debug.println("<CacheManager> find in cache. " + this.name + " " + cobj.getHitCount());

		return cobj.getObject();
	}

	/**
	 * 캐쉬된 객체의 갯수를 반환한다.
     * 
	 * @see jdf.framework.core.data.schema.CacheManager#size()
	 */
	public int size()
	{
		return this.list.size();
	}

	/**
	 * 초기화
	 * 
	 * @see jdf.framework.core.pool.cache.CacheManager#reset()
	 */
	public synchronized void reset()
	{
		this.cache.clear();
		this.list.clear();
	}
	
	
	

	/**
	 * 객체를 캐쉬한다.
     * osProp 가 true 이면 캐쉬내용을 다른 서버로 전파시킨다.
     * 
	 * @see jdf.framework.core.pool.cache.CacheManager#insert(java.lang.Object, java.lang.Object, boolean)
	 */
	public void insert(Object key, Object obj, boolean isProp)
	{
		insert(key,obj);

	}

	/**
	 * 초기화
     * isProp 가 true 이면 다른 서버도 초기화
     * 
	 * @see jdf.framework.core.pool.cache.CacheManager#reset(boolean)
	 */
	public void reset(boolean isProp)
	{
		reset();

	}

}
