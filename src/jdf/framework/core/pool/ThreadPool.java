package jdf.framework.core.pool;

import java.io.*;
import java.util.*;

import jdf.framework.core.log.*;
import jdf.framework.core.util.collection.*;


/**
 * <p>
 * Thread 자원을 효과적으로 이용하기 위해 pooling을 한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public final class ThreadPool extends ThreadGroup {
	private static boolean isExecute = true;

	private final static int THREAD_TIMEOUT = 60 * 1000;

	private int timeout = THREAD_TIMEOUT;

	private int pt_Priority;

	private int init_size;

	private int maximum_size;

	private int pool_size;

	private boolean has_closed = false;

	private static int group_number = 0;

	private int thread_id = 0;

	private BlockingQueue que;

	private static List queList = new ArrayList();

	private String poolName = "";

	/**
	 * Thread Pool을 초기화 한다.
	 * 
	 * @param poolName
	 *            pooling할 thread의 대표명
	 * @param initial_size
	 *            초기 Thread pool 사이즈
	 * @param maximum_size
	 *            최대 Thread pool 사이즈
	 * @param pt_Priority
	 *            thread pool 에서 동작하는 thread의 priority
	 * @param timeout
	 *            쓰지 않는 thread를 정리할 timeout시간
	 * 
	 */
	public ThreadPool(String poolName, int initial_size, int maximum_size, int pt_Priority, int timeout) {
		super("ThreadPool[" + poolName + "]" + group_number++);

		this.que = new BlockingQueue();
		queList.add(this.que);

		this.poolName = poolName;

		this.runner = new Pooled_thread[maximum_size];

		this.timeout = timeout;
		this.pt_Priority = pt_Priority;

		this.init_size = initial_size;
		this.maximum_size = (maximum_size > 0) ? maximum_size : Integer.MAX_VALUE;

		pool_size = Math.min(initial_size, this.maximum_size);

		for (int i = pool_size; --i >= 0;) {
			runner[i] = new Pooled_thread(this);
			runner[i].start();
		}

		// Background Pooled_Thread를 감시하며, 널널할때 초기화 시킨다.
		new MonitorRunnable(this);

	}

	/**
	 * Thread Pool을 초기화 한다.
	 * 
	 * @param poolName
	 *            pooling할 thread의 대표명
	 * @param initial_size
	 *            초기 Thread pool 사이즈
	 * @param maximum_size
	 *            최대 Thread pool 사이즈
	 * @param pt_Priority
	 *            thread pool 에서 동작하는 thread의 priority
	 * @param timeout
	 *            쓰지 않는 thread를 정리할 timeout시간
	 * 
	 */
	public ThreadPool(int initial_size, int maximum_size, int pt_Priority) {
		this("", initial_size, maximum_size, pt_Priority, THREAD_TIMEOUT);
	}

	/**
	 * Thread Pool을 초기화 한다.
	 * 
	 * @param initial_size
	 *            초기 Thread pool 사이즈
	 * @param maximum_size
	 *            최대 Thread pool 사이즈
	 * 
	 */
	public ThreadPool(int initial_size, int maximum_size) {
		this("", initial_size, maximum_size, Thread.NORM_PRIORITY, THREAD_TIMEOUT);
	}

	/**
	 * Thread Pool을 초기화 한다.
	 * 
	 * @param poolName
	 *            pooling할 thread의 대표명
	 * @param initial_size
	 *            초기 Thread pool 사이즈
	 * @param maximum_size
	 *            최대 Thread pool 사이즈
	 * 
	 */
	public ThreadPool(String poolName, int initial_size, int maximum_size) {
		this(poolName, initial_size, maximum_size, Thread.NORM_PRIORITY, THREAD_TIMEOUT);
	}

	/**
	 * Thread Pool을 초기화 한다.
	 * 
	 * @param poolName
	 *            pooling할 thread의 대표명
	 * @param initial_size
	 *            초기 Thread pool 사이즈
	 * @param maximum_size
	 *            최대 Thread pool 사이즈
	 * @param pt_Priority
	 *            thread pool 에서 동작하는 thread의 priority
	 * 
	 */
	public ThreadPool(String poolName, int initial_size, int maximum_size, int pt_Priority) {
		this(poolName, initial_size, maximum_size, pt_Priority, THREAD_TIMEOUT);
	}

	/***************************************************************************
	 * Create a dynamic Thread pool as if you had used ThreadPool(0, true);
	 */
	private ThreadPool() {
		super("ThreadPool" + group_number++);
		this.maximum_size = 0;
	}

	int waitThreadNum = 0;

	Pooled_thread[] runner;

	// long lastAccessTime;

	private Object lock = new Object();

	/**
	 * Thread 종료
	 * 
	 */
	public static void stopExecute() {
		isExecute = false;

		for (int i = 0; i < queList.size(); i++) {
			try {
				BlockingQueue q = (BlockingQueue) queList.get(i);
				q.close();
			} catch (Throwable e) {
				// e.printStackTrace();
			}
			System.out.println("stop ThreadPool queue");
		}

	}

	/**
	 * pooling할 thread를 실행시킨다.
	 * 
	 * @param action
	 *            pooling할 thread
	 * 
	 */
	public void execute(Runnable action) throws Closed {

		if (has_closed)
			throw new Closed();

		/*
		 * System.out.println("execute : pool size="+pool_size );
		 * System.out.println("execute : que size="+que.size() );
		 * System.out.println("execute : waitThreadNum size="+waitThreadNum);
		 */

		if (pool_size < maximum_size) {
			if (waitThreadNum == 0 && !que.isEmpty()) {
				synchronized (lock) {
					++pool_size;

					System.out.println(pool_size + " " + maximum_size + " NEW THREAD " + action.getClass().getName());

					runner[pool_size - 1] = new Pooled_thread(this);
					runner[pool_size - 1].start();
				}
			}
		}

		que.enqueue(action); // Attach action to it.

		// lastAccessTime = System.currentTimeMillis();

	}

	/**
	 * 대기중인 Thread 의 숫자를 반환한다.
	 * 
	 * @return
	 */
	public int getWaitThreadNum() {
		return waitThreadNum;
	}

	/**
	 * 최대 Pooling 할 수 있는 용량 갯수를 반환한다.
	 * 
	 * @return
	 */
	public int getTotalPooledSize() {
		return pool_size;
	}

	/**
	 * 
	 * @author
	 * 
	 */
	public static class Closed extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Closed() {
			super("Tried to execute operation on a closed ThreadPool");

		}
	}

	/***************************************************************************
	 * Kill all the threads waiting in the thread pool, and arrangefor all
	 * threads that came out of the pool, but which are working,to die natural
	 * deaths when they're finished with whatever they'redoing. Actions that
	 * have been passed to execute() but whichhave not been assigned to a thread
	 * for execution are discarded. No further operations are permitted on a
	 * closed pool, thoughclosing a closed pool is a harmless no-op.
	 */
	public synchronized void close() {
		has_closed = true;
		que.close(); // release all waiting threads
	}

	private class MonitorRunnable implements Runnable {
		ThreadPool p;

		MonitorRunnable(ThreadPool p) {
			this.p = p;
			// (new Thread(this,"ThreadPool Monitor")).start();
			Thread mon = new Thread(this, "ThreadPool Monitor");
			mon.setDaemon(true);
			mon.start();
		}

		public void run() {
			try {
				while (isExecute) {
					Thread.sleep(p.timeout);

					// 현재 pool size가 int 사이즈보다 큰경우
					// 모두 대기중인 경우만.
					/*
					 * System.out.println("p.pool_size:"+p.pool_size);
					 * System.out.println("p.init_size:"+p.init_size);
					 * System.out.println("p.waitThreadNum:"+p.waitThreadNum);
					 */

					synchronized (p) // ThreadPool에서 execute할때는 수행하면 않된다.
					{
						if (p.pool_size == p.waitThreadNum) {
							// 초기값보다 많으면 감소시킴.
							if (p.pool_size > p.init_size) {
								for (int j = p.pool_size - 1; j >= p.init_size; j--) {
									// p.runner[j].interrupt();
									p.runner[j].close();
								}

								// p.pool_size = p.waitThreadNum = p.init_size;

								p.thread_id = p.init_size;

							}

							// 초기값보다 적으면 증가시킴
							else if (p.pool_size < p.init_size) {
								for (int j = p.pool_size; j < p.init_size; j++) {
									p.runner[j] = new Pooled_thread(p);
									p.runner[j].start();
								}

								p.pool_size = p.waitThreadNum = p.init_size;

								p.thread_id = p.init_size;

							}
						}
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("[jdf.framework.core.pool.ThreadPool] service stop");
		}

	}

	/**
	 * 미리 동작되고 있는 Pooled thread
	 * 
	 * 
	 */
	private class Pooled_thread extends Thread {

		boolean isClosed = false;

		ThreadPool supervisor;

		public Pooled_thread(ThreadPool supervisor) {

			// super( ThreadPool.this, "T["+supervisor.poolName+"] " +
			// supervisor.thread_id++ );
			super(ThreadPool.this, "T[" + supervisor.poolName + "] ");
			supervisor.thread_id++;

			// super( "T" + thread_id++ );

			this.supervisor = supervisor;

			setPriority(supervisor.pt_Priority);
		}

		public void close() {
			isClosed = true;
		}

		public void run() {
			// try {

			Runnable toRun = null;

			while (isExecute && !has_closed && !isClosed) {
				try {

					++supervisor.waitThreadNum;

					toRun = que.dequeue();

					--supervisor.waitThreadNum;

					toRun.run();

					toRun = null;
				} catch (Throwable e) {
					System.err.println("[jdf.framework.core.pool.Pooled_thread:"+supervisor.poolName+"] err "+e.toString());

					try {
						Thread.sleep(500);
					} catch (Exception ee) {
					}
				}

			}

			--supervisor.pool_size;
			System.err.println("[jdf.framework.core.pool.Pooled_thread:"+supervisor.poolName+"] stop service");
			
			/*
			 * } catch(NullPointerException ne) { // BlockingQueue에서 timeout이 걸려
			 * 빠져나온 경우 // null이 반환된다. } catch(InterruptedException e) { //
			 * ignore it, stop thread } catch(BlockingQueue.Closed e) { //
			 * ignore it, stop thread }
			 * 
			 * finally { --supervisor.pool_size; }
			 */

		}
	}

	private static class BlockingQueue {
		private RunnableList elements = new RunnableList();

		// private Vector elements = new Vector();
		private boolean closed = false;

		/***********************************************************************
		 * The Closed exception is thrown if you try to used an explicitlyclosed
		 * queue. See close.
		 */
		public static class Closed extends RuntimeException {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private Closed() {
				super("Tried to access closed BlockingQueue");
			}
		}

		/***********************************************************************
		 * Enqueue an object
		 */
		public synchronized final void enqueue(Runnable new_element) throws BlockingQueue.Closed {
			if (closed)
				throw new Closed();

			elements.addLast(new_element);
			// elements.addElement(new_element);

			notify();
			// notifyAll();
		}

		/***********************************************************************
		 * Dequeues an element; blocks if the queue is empty (until something is
		 * enqueued). Be careful of nested-monitor lockout if you call
		 * thisfunction. You must ensure that there's a way to get something
		 * intothe queue that does not involve calling a synchronized method
		 * ofwhatever class is blocked, waiting to dequeue something.
		 * 
		 * @see dequeue
		 * @see enqueue
		 * @return s the dequeued object always
		 */
		public synchronized final Runnable dequeue() throws InterruptedException, BlockingQueue.Closed {
			try {
				while (elements.size() <= 0) {
					wait();

					if (closed)
						throw new Closed();
				}

				return elements.remove(0);

				/*
				 * Object ob = (Object) elements.get(0); elements.remove(0);
				 * return ob;
				 */
			} catch (NoSuchElementException e) { // Shouldn't happen
				throw new Error("Internal error (BlockingQueue)");
			}
		}

		/***********************************************************************
		 * The isEmpty() method is inherently unreliable in a
		 * multithreadedsituation. In code like the following, it's possible for
		 * a thread to sneak in after the test but before the dequeue operation
		 * and stealthe element you thought you were dequeueing. BlockingQueue
		 * queue = new BlockingQueue(); //... if( !some_queue.isEmpty() )
		 * some_queue.dequeue(); To do the foregoing reliably, you must
		 * synchronize on the queue as follows: BlockingQueue queue = new
		 * BlockingQueue(); //... synchronized( queue ) { if(
		 * !some_queue.isEmpty() ) some_queue.dequeue(); } The same effect can
		 * be achieved if the test/dequeue operationis done inside a
		 * synchronized method, and the only way toadd or remove queue elements
		 * is from other synchronized methods.
		 */
		// public synchronized final boolean isEmpty()
		public final boolean isEmpty() {
			return elements.size() == 0;
		}

		/*
		 * Releasing a blocking queue causes all threads that are blocked
		 * [waiting in dequeue() for items to be enqueued] to be released. The
		 * dequeue() call will throw a BlockingQueue.Closed runtime exception
		 * instead of returning normally in this case. Once a queue is closed,
		 * any attempt to enqueue() an item will also result in a
		 * BlockingQueue.Closed exception toss.
		 */
		public synchronized void close() {
			closed = true;
			notifyAll();
		}

		public int size() {
			return elements.size();
		}

	}

}