package jdf.framework.core.util;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * <p>
 * java.util.Stack과 는 반대로 FIFO(First In First Out)인 Queue를 구현한 Class 일반 QUEUE와
 * 다른점은 데이타가 없으면 Blociking 된다.
 * 
 * @author
 * @version 1.1
 */

public final class BlockingQueue extends LinkedList
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private LinkedList elements = new LinkedList();
    // private Vector elements = new Vector();
    private boolean closed = false;

    private long timeout; // enqueue시 최대 대기시간

    private int limitsize = -1;

    /**
     * 기본생성자 
     * blocking 시 timeout 이 설정되어 있지 않다.
     *
     */
    public BlockingQueue()
    {
        this.timeout = -1;
    }

    /**
     * 기본생성자
     * blocking시 일정시간동안만 대기하도록  timeout 값을 설정할 수 있다.
     * 
     * @param timeout
     */
    public BlockingQueue(long timeout)
    {
        this.timeout = timeout;
    }

    /**
     * 일정크기이상이면 지워버린다.
     * 
     * @param timeout
     *            wait timeout
     * @param limitsize
     *            제한크기
     */
    public BlockingQueue(long timeout, int limitsize)
    {
        this(timeout, limitsize, false);
    }

    private boolean isEnqueWait = false;

    /**
     * 기본생성자
     * queue 에서 데이터를 가져올때 일정시간동안만 대기하며,
     * 특정 사이즈 (limitsize)이상으로 데이터가 enqueue 될 시에는
     * isEnqueWait 에 따라 대기할 지 또는 무시할지를 결정한다.
     * 
     * @param timeout
     * @param limitsize
     * @param isEnqueWait
     */
    public BlockingQueue(long timeout, int limitsize, boolean isEnqueWait)
    {
        this.timeout = timeout;
        if (limitsize > 0 && limitsize < Integer.MAX_VALUE)
        {
            this.limitsize = limitsize;
        }
        this.isEnqueWait = isEnqueWait;
    }

    /***************************************************************************
     * The Closed exception is thrown if you try to used an explicitlyclosed
     * queue. See close.
     */
    public class Closed extends RuntimeException
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Closed()
        {
            super("Tried to access closed BlockingQueue");
        }
    }

    /***************************************************************************
     * Enqueue an object
     */

    public synchronized final void enqueue(Object new_element) throws BlockingQueue.Closed
    {
        if (closed)
            throw new Closed();

        if (0 < limitsize && limitsize < size())
        {

            if (isEnqueWait)
            {
                try
                {
                    wait();
                } catch (Exception e)
                {

                }
            } else
                remove(0);
        }

        addLast(new_element);

        notify();
    }

    /**
     * queing된 모든 객체를 반환한다.
     * 
     * @return
     */
    public synchronized final Object[] dequeueAll()
    {
        if (this.size() == 0)
            return null;

        Object[] r = this.toArray();
        this.clear();
        notify();
        return r;
    }

    /***************************************************************************
     * Dequeues an element; blocks if the queue is empty (until something is
     * enqueued). Be careful of nested-monitor lockout if you call thisfunction.
     * You must ensure that there's a way to get something intothe queue that
     * does not involve calling a synchronized method ofwhatever class is
     * blocked, waiting to dequeue something.
     * 
     * @see dequeue
     * @see enqueue
     * @return s the dequeued object always
     */
    public synchronized final Object dequeue() throws InterruptedException, BlockingQueue.Closed
    {
        try
        {

            long prevTime = 0;

            if (timeout > 0)
                prevTime = System.currentTimeMillis();

            while (size() <= 0)
            {
                if (timeout > 0)
                {
                    wait(timeout);

                    if (size() <= 0)
                    {
                        // System.out.println("b");
                        long checkTime = System.currentTimeMillis();

                        // System.out.println("b2");

                        if (checkTime - prevTime >= timeout)
                        {
                            // System.out.println("b3");
                            throw new InterruptedException();
                        } else
                            prevTime = checkTime;

                        // System.out.println((checkTime-prevTime));
                    }
                } else
                    wait();

                if (closed)
                    throw new Closed();
            }

            /*
             * Object ob = (Object) get(0); remove(0); return ob;
             */

            Object r = remove(0);
            notify();
            return r;
        } catch (NoSuchElementException e)
        { // Shouldn't happen
            throw new Error("Internal error (BlockingQueue)");
        }

    }

    /***************************************************************************
     * The isEmpty() method is inherently unreliable in a
     * multithreadedsituation. In code like the following, it's possible for a
     * thread to sneak in after the test but before the dequeue operation and
     * stealthe element you thought you were dequeueing. BlockingQueue queue =
     * new BlockingQueue(); //... if( !some_queue.isEmpty() )
     * some_queue.dequeue(); To do the foregoing reliably, you must synchronize
     * on the queue as follows: BlockingQueue queue = new BlockingQueue(); //...
     * synchronized( queue ) { if( !some_queue.isEmpty() ) some_queue.dequeue(); }
     * The same effect can be achieved if the test/dequeue operationis done
     * inside a synchronized method, and the only way toadd or remove queue
     * elements is from other synchronized methods.
     */
    public synchronized final boolean isEmpty()
    {
        return size() == 0;
    }

    /*
     * Releasing a blocking queue causes all threads that are blocked [waiting
     * in dequeue() for items to be enqueued] to be released. The dequeue() call
     * will throw a BlockingQueue.Closed runtime exception instead of returning
     * normally in this case. Once a queue is closed, any attempt to enqueue()
     * an item will also result in a BlockingQueue.Closed exception toss.
     */
    public synchronized void close()
    {
        closed = true;
        notifyAll();
    }

    /*
     * public int size() { return size(); }
     */

    public static void main(String[] args) throws Exception
    {
        Thread.sleep(2000);
        System.out.println("start");

        for (int j = 0; j < 1000; j++)
        {
            // Thread.sleep(1);
            System.out.print("");
        }

        /*
         * BlockingQueue que = new BlockingQueue(5000);
         * 
         * 
         * que.enqueue("new");
         * 
         * System.out.println( que.dequeue() );
         * 
         * que.enqueue("new");
         * 
         * Thread.sleep(2000);
         * 
         * System.out.println( que.dequeue() );
         * 
         * System.out.println( que.dequeue() );
         */
    }

}
