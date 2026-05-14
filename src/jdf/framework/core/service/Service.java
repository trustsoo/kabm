/*
 * @(#)Service.java
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


import javax.naming.Referenceable;
import java.io.Serializable;

/**
 * 기본적으로 서버단의 서비스는 
 * <pre>
 * 1) 독립적으로 실행가능하며, 결과를 기다리지 않는다.
 * 2) 시작,중지,멈춤등의 제어가 가능해야 한다.
 * 3) 현재서비스의 상태를 알수 있어야 한다.
 * 4) 시작시만 매개 변수(Config형태)를 정의할 수 있다.
 * </pre>
 * 
 * @author
 * @version 1.0
 */
public interface Service extends Configurable,Startable,Stoppable,Serializable,Referenceable
{
    
    public static final int STOPPED = 0;
    public static final int STARTING= 1;
    public static final int RUNNING = 2;
    public static final int STOPPING= 3;
    public static final int PAUSING = 4;
    public static final int PAUSED  = 5;
    public static final int RESUMING= 6;
    
    
    
    /**
     * Service 시작
     * (Startable 인터페이스에서 상속받지만 명학히 하기 위해)
     */
    public void start() throws Exception;
    
    /**
     * Service 중지
     * (Stoppable 인터페이스에서 상속받지만 명학히 하기 위해)
     */
    public void stop() throws Exception;
    
    /**
     * Service 잠시멈춤
     */
    public void pause() throws Exception;
    
    /**
     * Service 다시 시작
     */
    public void resume() throws Exception;

	/**
	 * 서비스의 상태를 얻는다.
	 * : STOPPED, STARTING, RUNNING, STOPPING,
	 * PAUSING, PAUSED, or RESUMING.
	 * 
	 * @uml.property name="state"
	 */
	public int getState();

	public int getServiceState();
	
	/**
	 * 서비스의 상태를 설정한다.
	 * 
	 * @uml.property name="state"
	 */
	public void setState(int mode);

    
    
    
    /**
     * 서비스의 고유한 ID를 얻는다.
     */
    public String getInstanceID();

	/**
	 * 서비스의 의미있는 이름을 얻는다.
	 * 
	 * @uml.property name="name"
	 */
	public String getName();

	/**
	 * 서비스의 의미있는 이름을 설정한다.
	 * 
	 * @uml.property name="name"
	 */
	public void setName(String name);

    

}
