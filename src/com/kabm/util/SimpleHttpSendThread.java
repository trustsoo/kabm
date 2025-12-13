package com.kabm.util;

import java.util.List;

import jdf.framework.core.log.Logger;

public class SimpleHttpSendThread extends Thread
{
	private final String LOG_ID="<at:HttpSendThread> ";
	
	private List<String> urls = null;
	
	
	public SimpleHttpSendThread(String url)
	{
		urls.add(url);
	}
	
	
	public SimpleHttpSendThread(List<String> urls)
	{
		this.urls = urls;
	}
	
	
	public void run()
	{
		try 
		{
			for(String a : urls)
			{
				HttpUtil.stringReceive(a);
				Thread.sleep(3*100);
			}
		} catch (Exception e) {
			Logger.warn.println(LOG_ID+e.getMessage());
		}
	}
}