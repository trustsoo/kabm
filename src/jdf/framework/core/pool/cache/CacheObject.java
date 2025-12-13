/*
 * Created on 2004-05-11
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.core.pool.cache;

/**
 * 캐쉬할 객체를 감싸는 wrapper class
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CacheObject
{
	
	private long createTime;
	
	private Object storeObj;
	
	private long expireTime;
	
	private int hitCount=0;
	
	
	/**
     * 기본생성자
     * 
	 */
	public CacheObject(Object obj, long expire_time_gap)
	{
		this.storeObj=obj;
		
		
		this.createTime=System.currentTimeMillis();
		this.expireTime=this.createTime+expire_time_gap;
	}
	
	/**
	 * 현재 저장된 객체가 사용만료기간이 지났는지 검사
	 * 
	 * @return
	 */
	public boolean isExpired()
	{
		if(createTime <expireTime && expireTime<System.currentTimeMillis()) {
			
			//System.err.println("expired "+expireTime);
			//System.err.println("expired "+System.currentTimeMillis());
			return true;
		}
			
		else
			return false;
	}
	
	
	
	
	/**
	 * cache에 저장된 객체
	 * 
	 * @return
	 */
	public Object getObject()
	{
		this.hitCount++;
		return this.storeObj;
	}
	
	/**
	 * 객체를 쓴 횟수
	 * 
	 * @return
	 */
	public int getHitCount()
	{
		return this.hitCount;
	}
	
	

}