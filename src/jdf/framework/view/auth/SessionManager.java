package jdf.framework.view.auth;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.log.Logger;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class SessionManager
{
	private final String LOG_ID = "<at:SessionManager> ";
	
	private static SessionManager instance = null;	
	
	/*
	 * sessionID
	 * User
	 * */
	private static ConcurrentHashMap<String, User> storageBySessionId = null;
	
	/*
	 * user_id
	 * sessionID
	 * */
	private static ConcurrentHashMap<String, String> storageByUserId = null;
	
	public synchronized static SessionManager getInstance()
	{
		if(instance == null)
		{
			instance = new SessionManager();			
		}
		return instance;		
	}
	
	private SessionManager()
	{
		storageBySessionId = new ConcurrentHashMap<String, User>();	
		storageByUserId = new ConcurrentHashMap<String, String>();	
		//storageBySessionIpPort = new ConcurrentHashMap<String, User>();
	}
	
	
	
	/**
	 * @param key(sessionid)
	 * @param user
	 */
	public void put(String key, User user)
	{
		storageBySessionId.put(key, user);		
		Logger.info.println(LOG_ID+key+":("+user.getIp()+":"+user.getPort()+", "+user.getId()+") is putted.");
		storageByUserId.put(user.getId(), key);
	}
	
	public void remove(String key)
	{
		User u = storageBySessionId.remove(key);
		
		String user_id = null;
		if(u != null && u.getId() != null)
		{
			user_id = u.getId();			
			//storageByUserId.remove(user_id);
			String sessionID = storageByUserId.get(user_id);
			if(sessionID != null && sessionID.equals(key)) storageByUserId.remove(user_id);
		} else
		{
			user_id = "";
			
		}
		Logger.info.println(LOG_ID+key+"("+user_id+") is removed.");
	}
	
	public User getBySessionID(String key)
	{
		return storageBySessionId.get(key);
	}
	
	/*
	 * 로그인한 사용자중 User객체의 특정 key에 val으로 할당되어있는 사용자의 ID정보들을 리턴한다.
	 * */
	public List<String> getUserIdsByUserKeyValue(String key, String val)
	{
		Iterator<User> ite = storageBySessionId.values().iterator();
		List<String> userIds = new ArrayList<String>();
		while(ite.hasNext())
		{
			User user = ite.next();
			DataSet data = user.getDataSet();
			if(val.equals(data.getText(key)))
			{
				userIds.add(user.getId());
			}
		}		
		return userIds;		
	}
	
		
	
	public void printUserByUserID(String userid)
	{
		Enumeration<String> enu = storageBySessionId.keys();
		String sessionID = null;
		User user = null;
		while(enu.hasMoreElements())
		{
			try
			{
				sessionID = enu.nextElement();			
				user = storageBySessionId.get(sessionID);
				if(userid.equals(user.getId()))
				{
					Logger.info.println(sessionID+">>"+user.toString());
				}
			} catch(Exception ex)
			{}
		}
		
		
		
	}
	
	public User getUserByUserID(String userid)
	{
		
		
		String key = storageByUserId.get(userid);
		if(key != null)
			return storageBySessionId.get(key);
		
		return null;
		
		
	}
	
	
	public User getOldUserByUserID(String userid, String sessionID)
	{
		
		
		String key = storageByUserId.get(userid);
		if(key != null)
			return storageBySessionId.get(key);
		
		return null;
		
		
	}
	
}