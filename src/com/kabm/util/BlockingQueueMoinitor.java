package com.kabm.util;

import java.util.concurrent.BlockingQueue;

import jdf.framework.core.log.Logger;



public class BlockingQueueMoinitor extends Thread
{
	private BlockingQueue<String> queue = null;
	private String name = null;
	
	public BlockingQueueMoinitor(BlockingQueue<String> queue, String name)
	{
		this.queue = queue;
		this.name = name;
	}
	
	public void run() 
	{	
		int size = 0;
		while(true)
		{
			try
			{
				size = queue.size();
				if(size > 0)
				{
					Logger.info.println("********"+name+" Queue Info*******");				
					Logger.info.println("* QueueSize : "+size);
					Logger.info.println("***********************************");
				}
				Thread.sleep(5 * 1000);
			} catch(Exception ex)
			{
				
			}
		}
	}
}
