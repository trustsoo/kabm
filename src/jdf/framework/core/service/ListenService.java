/*
 * @(#)ListenService.java
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

import java.net.ServerSocket;
import java.util.List;
import java.util.Vector;

/**
 * Service의 한 형태중 통신을 listening하고 처리하기 위한 서비스를 구현한 추상클래스이다.
 *
 * @author
 * @version 1.0
 */
  
public abstract class ListenService extends AbstractService
{
    
    /**
     * Listening 하는 서비스이므로 미리 ServerSocket을 정의
     */
    protected ServerSocket sSock;   

    public ListenService()
    {
        setName(getClass().getName()); // 클래스명이 서비스명
    }
    
    public ListenService(String name)
    {
        setName(name);
    }

    protected void setServerSocket(ServerSocket sSock)
    {
        this.sSock = sSock;
    }

    /**
     * Service 중지
     */
    public synchronized void stop() throws Exception
    {
        setState(STOPPING);
        
        if(sSock != null)
            sSock.close();  // ServerSocket 을 closing하고
        else
            throw new ServiceException("this service did not started.");
        
        setState(STOPPED);
    }

    /**
     * 서비스의 threading 처리를 위해 
     * 시작시 해야할 로직을 구현해 주어야 한다.
     */
    public abstract void run();

    private List children = new Vector();

	/**
	 * 현재 Listening하는 서비스의 자식들 객체를 가져온다.
	 * 
	 * @uml.property name="children"
	 */
	public List getChildren() {
		return children;
	}

    /**
     * 현재 Listening하는 서비스의 자식들 객체의 갯수를 반납한다.
     *
     */
    public int getChildrenSize()
    {
        return children.size();
    }

    /**
     * 현재 Listening하는 서비스의 자식 객체로 등록한다.
     *
     */
    public void addChild(Object child)
    {
        children.add(child);
    }

    /**
     * 현재 Listening하는 서비스의 자식 객체를 지운다.
     *
     */
    public synchronized void removeChild(Object child)
    {
        children.remove(child);
    }
 
}