/*
 * Created on 2004-05-11
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.core.data.schema;

/**
 * Cache 정보를 세팅한다.
 * 
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CacheInfo
{
	
	
	private int maxObjectCount;
	
	
	private long expireSecondTime;


	public CacheInfo()
	{
			
	}
	
	
	
	
	

	/**
	 * 지속시간을 가져온다.
	 * 
	 * @return
	 */
	public long getExpireSecondTime()
	{
		return expireSecondTime;
	}

	/**
	 * 최대갯체수를 얻는다.
	 * 
	 * @return
	 */
	public int getMaxObjectCount()
	{
		return maxObjectCount;
	}

	/**
	 * 지속시간을 정의한다.
	 * 
	 * @param l
	 */
	public void setExpireSecondTime(long l)
	{
		expireSecondTime = l;
	}

	/**
	 * 최대 갯체수를 정의한다.
	 * 
	 * @param i
	 */
	public void setMaxObjectCount(int i)
	{
		maxObjectCount = i;
	}

}
