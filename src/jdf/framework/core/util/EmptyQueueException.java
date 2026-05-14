package jdf.framework.core.util;

/**
 * 
 * Queue 가 비었을때 발생하는 RuntimeException
 * 
 * 
 * @author
 * @see Queue
 */
public class EmptyQueueException extends RuntimeException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 
     * 
     */
    public EmptyQueueException()
    {
        super();
    }

    /**
     * 
     * @param message
     */
    public EmptyQueueException(String message)
    {
        super(message);
    }
}