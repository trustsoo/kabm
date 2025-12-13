/*
 * Created on 2004-05-11
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.core.pool.cache;

/**
 * CacheObject을 관리하기 위한 Manager Interface
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface CacheManager //implements jdf.framework.core.pool.cache.CacheManager
{

	/**
	 * 
	 * CacheManager의 이름과
	 * 최대 저장객체수,
	 * 그리고 객체가 유효한 시간을 설정한다.
	 * 
	 * 
	 * @param name
	 * @param maxnum
	 * @param expireTime
	 */
	public void setInfo(String name, int maxnum, long expireTime);

	/**
	 * 해당 key 에 해당하는 객체를 저장한다.
	 * 
	 * 
	 * @param key
	 * @param obj
	 */
	public void insert(Object key, Object obj);

	/**
	 * 해당 key 에 해당하는 객체를 저장한다.
     * isProp 가 true 면 다른 서버로 cache 내용을 전파한다.
     * 
	 * @param key
	 * @param obj
	 * @param isProp
	 */
	public void insert(Object key, Object obj, boolean isProp);

	/**
	 * 해당키에 대한 캐쉬된 객체를 부른다.
	 * 
	 * @param key
	 * @return
	 */
	public Object call(Object key);

	/**
	 * 현재 캐쉬된 객체수를 반환한다.
     * 
	 * @see jdf.framework.core.data.schema.CacheManager#size()
	 */
	public int size();

	/**
	 * CacheManager에서 자장하는 객체를 모두 삭제한다.
	 *
	 */
	public void reset();

	/**
	 * 다른 서버로 이벤트를 날리는 경우
	 * 
	 */
	public void reset(boolean isProp);

	/**
	 * 캐쉬명을 반환한다.
     * 
	 * @return
	 */
	public String getName();

}
