/*
 * @(#)DaemonService.java
 *
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */
 
package jdf.framework.core.service;

/**
 * Service의 한 형태중 단독으로 Daemon처럼 내부 서비스를 구현한 추상클래스이다.
 *
 * @author
 * @version 1.0
 */
  
public abstract class DaemonService extends AbstractService
{
    public DaemonService()
    {
        setName(getClass().getName()); // 클래스명이 서비스명
    }

    public DaemonService(String name)
    {
        setName(name);
    }

    /**
     * Service 중지
     */
    public synchronized void stop() throws Exception
    {
        setState(STOPPING); 
        
        if(thisService != null){
            stopService();			//실질적인 정지를 구현해야 한다.
			thisService.interrupt();  // Thread를 중지시킨다.
		}
        else
		{
            throw new Exception("this service did not started.");
        }
        
        setState(STOPPED); // 상태를 '종료'로 한다.
    }

    /**
	 * Daemon 마다 실질적은 정지 코드가 다르므로 이 stopService()를 구현해야 한다.
	 */
	public abstract void stopService() throws Exception;

    /**
     * 서비스의 threading 처리를 위해 
     * 시작시 해야할 로직을 구현해 주어야 한다.
     */
    public abstract void run();
}    