/*
 * @(#)ResouceException.java
 * 
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
package jdf.framework.core.data;

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * 
 * ResourceException
 * 
 * 
 * @author
 * @version 1.0
 * @since 2002-12-19 오전 8:38:23
 *
 */
public class ResourceException extends RemoteException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SQLException sqle;

	private int errCode = 0;

    /**
     * 기본생성자
     *
     */
	public ResourceException()
	{
		super();
	}

    /**
     * 생성자
     * 
     * @param sqle
     */
	public ResourceException(SQLException sqle)
	{
		this("java.sql.SQLException:" + sqle.getMessage(), sqle);

		this.sqle = sqle;
		this.errCode = sqle.getErrorCode();
	}
	
	public ResourceException(SQLException sqle, String trcode)
	{
		this(trcode+" SQLException:" + sqle.getMessage(), sqle);

		this.sqle = sqle;
		this.errCode = sqle.getErrorCode();
	}

    /**
     * 메세지 문자를 이용한 생성자
     * 
     * @param msg
     */
	public ResourceException(String msg)
	{
		super(msg);
	}

    /**
     * 메세지 문자와 err 코드를 이용한 생성자
     * 
     * @param msg
     * @param errCode
     */
	public ResourceException(String msg, int errCode)
	{
		super(msg);
		this.errCode = errCode;

	}
    
    /**
     * Exception 을 이용한 생성자
     * 
     * @param e
     */
	public ResourceException(Exception e)
	{
		this(e.getMessage(), e);

	}

	private Throwable error;

    /**
     * 
     * 
     * @param msg
     * @param cause
     */
	public ResourceException(String msg, Throwable cause)
	{
		super(msg, cause);
		//super(msg);

		this.error = cause;

	}

    /**
     * 에러코드를 반환한다.
     * @return
     */
	public int getErrorCode()
	{
		return errCode;
	}

    /**
     * error trace를 출력
     * 
     */
	public void printStackTrace()
	{
		if (this.error != null)
			error.printStackTrace();
		else
			super.printStackTrace();

	}
	
	/**
	 * DBMS 관련 SQLException 발생된 경우는 이 method를 return 한다.
	 * 만약 없으면 null을 return 한다.
	 * @return
	 */
	public SQLException getSQLException()
	{
		return this.sqle;
	}

}
