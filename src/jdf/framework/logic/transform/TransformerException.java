package jdf.framework.logic.transform;

/**
 * 변환과정중 발생될 수 있는 예외사항 중 가장 상위를 의미한다.
 * TransformException은 다음과 같은 정보를 제공한다.
 *
 * @author Eun Jeong-Ho
 * @version 1.0
 */
public class TransformerException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransformerException()
	{
		super();
	}

	public TransformerException(String msg)
	{
		super(msg);
	}

	private Throwable error;

	public TransformerException(String msg, Throwable err)
	{
		super(msg);

		this.error = err;
	}

	/**
	 * 
	 * @see Throwable#printStackTrace()
	 */
	public void printStackTrace()
	{
		if (error != null)
			error.printStackTrace();
		else
			super.printStackTrace();
	}

}