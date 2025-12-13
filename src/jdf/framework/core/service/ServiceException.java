package jdf.framework.core.service;

public class ServiceException extends RuntimeException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4235647273493583418L;
	private int errCode;
    
    

    public ServiceException()
    {
        super();
    }
    
    public ServiceException(String msg)
    {
        super(msg);
    }
    

    public ServiceException(int errCode)
    {
        super();
        this.errCode=errCode;
        
    }
    
    
    public int getErrorCode()
    {
        return errCode;
    }
    
    
}



