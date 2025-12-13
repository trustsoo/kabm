/*
 * @(#)AbstractService.java
 * 
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, 
 * you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author
 */
package jdf.framework.core.service;



import javax.naming.Reference;
import javax.naming.StringRefAddr;

import jdf.framework.core.Config;

/** * Service의 한 형태중 통신을 listening하고 처리하기 위한 서비스를  * 구현한 추상클래스이다. *  * @author * @version 1.0 */
  
public abstract class AbstractService implements Service, Runnable
{
    
    /**
     * 현재의 서비스상태
     */
    private int state = STOPPED;

	/**
	 * 현재 서비스의 Config정보
	 * 
	 * @uml.property name="config"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private Config config;

    
    
     /**
     * 현재 서비스의 단축명
     */
    private String name;
    
    
    
    /**
     * 이 서비스의 Background 실행을 위한 Thread객체
     */    
    protected Thread thisService;   
    
    
    
    
    
    
    public AbstractService()
    {
        //setName(getClass().getName()); // 클래스명이 서비스명
        this(null);
    }
    
    
    public AbstractService(String name)
    {
        if(name==null)
            name = getClass().getName();
            
        setName(name);
        
        ServiceManager mgr = ServiceManager.getInstance();
        mgr.registRefService(name, this);
        
    }
    
    
    
        
    /**
     * 서비스 parameter정보  (Config)을 설정한다.
     */    
    public void setConfigInfo(Config config)
    {
        this.config=config;
    }
    
    /**
     * 서비스의 parameter정보를 가져온다.
     */
    public Config getConfigInfo()
    {
        return this.config;
    }

	/**
	 * 현재 서비스의 상태를 설정한다.
	 * 
	 * @uml.property name="state"
	 */
	public void setState(int mode) {
		this.state = mode;
	}

	/**
	 * 현재 서비스의 상태를 반환한다.
	 * 
	 * @uml.property name="state"
	 */
	public int getState() {

		if (state == PAUSED) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (Exception e) {
			}

		}

		return state;
	}
	
	public int getServiceState(){return state;}

    
    
    
    /**
     * 현재 서비스의 ID를 반환한다. 
     * ID는 클래스명이다.
     */
    public String getInstanceID()
    {
        return getClass().getName();
    }

	/**
	 * 현재 서비스의 이름(alias name)을 얻는다.
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * 현재 서비스의 이름을 정의한다.
	 * 
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

    
     /**
     * Service 시작
     */
    public synchronized void start() throws Exception
    {
        setState(STARTING); // 상태를 '시작'으로한다.
        
        startThread();      // 이 서비스를 thread로 동작시키고
        
        try 
        {
        
            int retry = 3000; // 5분
            
            for(int j=0; j<retry; j++)
            {
                Thread.sleep(100);
                
                if( getState() == RUNNING )
                    return;
                else if( getState() == STOPPED )
                    throw new ServiceException(getName()+" service start fail");
            }
            
            stop();
            new ServiceException("service start timeout");
            
        }
        catch(ServiceException se)
        {
            throw se;
        }
    }

    /**
     * Service 잠시멈춤
     */
    public synchronized void pause() throws Exception
    {
        setState(PAUSED); // 상태를 '대기'로 한다.
    }

    /**
     * Service 다시 시작
     */
    public synchronized void resume() throws Exception
    {
        setState(RESUMING); // 상태를 '대기'로 한다.
        
        synchronized( this )
        {
            notify();
        }
        
        setState(RUNNING);
    }

    /**
     * 서비스의 threading 처리를 위해 
     * 시작시 해야할 로직을 구현해 주어야 한다.
     */
    public abstract void run();

    /**
     * Service 중지를 구현해 주어야 한다.
     */
    public abstract void stop() throws Exception;

    /**
     * 현재 서비스를 threading으로 시작시킨다.
     */
    protected void startThread() throws Exception
    {
        thisService = new Thread(this, name);
        
        thisService.start();
    }

    public javax.naming.Reference getReference() throws javax.naming.NamingException
    {
        Reference ref = new Reference (this.getClass().getName(), 
                                       "jdf.framework.core.service.ServiceFactory",
                                       null);
        ref.add(new StringRefAddr("service.name", getName()));
        
        return ref;
        
    }
}                       
