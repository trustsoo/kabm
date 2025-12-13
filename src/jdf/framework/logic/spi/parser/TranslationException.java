package jdf.framework.logic.spi.parser;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 변환관련 Exception
 * 
 * @author
 * 
 */
public class TranslationException extends RuntimeException
// public class TranslationException extends java.rmi.RemoteException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int errCode;

    private Throwable e;

    /**
     * 기본생성자
     * 
     */
    public TranslationException()
    {
        super();
    }

    /**
     * 생성자
     * 
     * @param e
     */
    public TranslationException(Throwable e)
    {
        super(e.toString());
        this.e = e;
    }

    /**
     * 문자열 메세지를 이용한 생성자
     * 
     * @param msg
     */
    public TranslationException(String msg)
    {
        super(msg);
    }

    /**
     * error code 를 이용한 생성자
     * 
     * @param errCode
     */
    public TranslationException(int errCode)
    {
        super();
        this.errCode = errCode;

    }

    /**
     * error code 반환
     * 
     * 
     * @return
     */
    public int getErrorCode()
    {
        return errCode;
    }

    public void printStackTrace()
    {
        super.printStackTrace();
        if (e != null)
            e.printStackTrace();
    }

    public void printStackTrace(PrintStream ps)
    {
        super.printStackTrace(ps);
        if (e != null)
            e.printStackTrace(ps);
    }

    public void printStackTrace(PrintWriter pw)
    {
        super.printStackTrace(pw);
        if (e != null)
            e.printStackTrace(pw);
    }

}
