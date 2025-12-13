package jdf.framework.core.data;

import java.io.Serializable;

import jdf.framework.core.log.Logger;
import jdf.framework.core.util.Queue;


/**
 * 
 * 비동기로 IO Schema 를 호출할때 사용하는 InteractionBean
 * 
 * 사용법은 InteractionBean 과 똑같다.
 * 
 * @author
 * 
 */
public class AsyncInteractionBean extends InteractionBean
{

    private final static String LOG_ID = "<f:AsyncInteractionBean> ";

    private static Queue que = new Queue();

    static {

	try {

	    int i = Thread.activeCount();
	    Thread athread[] = new Thread[i];
	    Thread.enumerate(athread);

	    // Logger.info.println("<ConnectionPool> active thread = " + i);

	    for (int w = 0; w < i; w++) {
		// Logger.info.println(athread[w].getName());
		if (athread[w] != null
			&& AsyncProcessThread.getThreadName().equals(
				athread[w].getName())) {

		    try {
			((AsyncProcessThread) athread[w]).stopMonitor();

		    } catch (Throwable ee) {

		    }
		}

	    }
	} catch (Throwable e) {

	}

	AsyncProcessThread t = new AsyncProcessThread(que);
	t.start();

    }

    public AsyncInteractionBean() {

    }

    /**
         * 비동기로 BLD를 수행한다.
         * 
         */
    public DataSet execute(String trcode, DataSet input) throws ResourceException
    {

	AsyncProcessInfo info = new AsyncProcessInfo(trcode, input, null);
	que.addLast(info);

	return null;
    }

    /**
         * 비동기로 BLD를 수행한다.
         * 
         */
    public void execute(String trcode, DataSet input, DataSet output)
	    throws Exception
    {

	AsyncProcessInfo info = new AsyncProcessInfo(trcode, input, output);
	que.addLast(info);
    }

    private final static class AsyncProcessInfo implements Serializable
    {
	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	private String trcode;

	private DataSet input;

	private DataSet output;

	AsyncProcessInfo(String trcode, DataSet input, DataSet output) {
	    this.trcode = trcode;
	    this.input = input;
	    this.output = output;
	}

	/**
         * @return Returns the input.
         */
	public DataSet getInputDataSet()
	{
	    return input;
	}

	/**
         * @return Returns the output.
         */
	public DataSet getOutputDataSet()
	{
	    return output;
	}

	/**
         * @return Returns the trcode.
         */
	public String getTrcode()
	{
	    return trcode;
	}

    }

    static class AsyncProcessThread extends Thread
    {
	private Queue que;

	private InteractionBean interact;

	private boolean isWork = true;

	public final static String THREAD_NAME = "anylogic_async_prss";

	AsyncProcessThread(Queue q) {
	    super(THREAD_NAME);
	    this.que = q;
	    this.interact = new InteractionBean();
	}

	public static String getThreadName()
	{
	    return THREAD_NAME;
	}

	public void stopMonitor()
	{
	    this.isWork = false;
	}

	public void run()
	{
	    while (isWork) {
		sleep();

		synchronized (this.que) {
		    if (!this.que.isEmpty()) {
			try {

			    AsyncProcessInfo info = (AsyncProcessInfo) this.que
				    .dequeue();

			    DataSet output = info.getOutputDataSet();
			    if (output == null)
				output = new DataSet();

			    Logger.info.println(LOG_ID + "* async execute "
				    + info.getTrcode());

			    this.interact.execute(info.getTrcode(), info
				    .getInputDataSet(), output);

			} catch (Exception e) {
			    e.printStackTrace();

			}

		    }

		}

	    }
	}

	private void sleep()
	{
	    try {
		Thread.sleep(10);

	    } catch (Exception e) {

	    }
	}

    }

}