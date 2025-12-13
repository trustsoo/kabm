package com.kabm.service.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientRequestQueueManager{
	private final String LOG_ID = "<at:MessageCollector> ";
	static private BlockingQueue<String> requestQ;
	
	public void getInstance(){
		if(requestQ == null)
			requestQ = new LinkedBlockingQueue<String>();
	}
	
	public synchronized void addRequest(String requestMsg) throws InterruptedException, NullPointerException{
		requestQ.put(requestMsg);
	}

	public synchronized String gatherRequest() throws InterruptedException, NullPointerException{
			return requestQ.take();
	}	
	
}
