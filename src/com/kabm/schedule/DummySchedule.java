package com.kabm.schedule;



import jdf.framework.core.Config;
import jdf.framework.core.Configurable;



public class DummySchedule implements Runnable, Configurable
{
	private final String LOG_ID = "<at:DummySchedule> ";
	private Config conf;
	
	public void run() 
	{
		System.out.println(">>DummySchedule");
	}

	
	public Config getConfigInfo() {
		// TODO Auto-generated method stub
		return conf;
	}

	
	public void setConfigInfo(Config info) {
		// TODO Auto-generated method stub
		conf = info;
	}
	
	
	
}