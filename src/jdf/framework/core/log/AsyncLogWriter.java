/*
 * @(#)AsyncLogWriter.java
 *
 * NOTICE !
 * You can not copy or redistribute this code.
 *
 *
 * @author
 * 이 소스는 정말 함부로 쓰지 말것. 
 */

package jdf.framework.core.log;

import jdf.framework.core.pool.ThreadPool;
import jdf.framework.core.util.Queue;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Hashtable;


/**
 * <p>
 * 로그기록을 원하는 출력소로 비동기적으로 출력한다. 이 class의 용도는 개별적인 log기록에는 별 도움이 되지 못한다. 출력하는 장소가 느리거나, formating이 필요하거나 출력까지의 시간이 걸리는
 * log에 대해서 background 처리가 필요한 경우 이용한다.
 * </p>
 * 
 * <pre>
 *   
 *    다음과 같은 출력을 얻을 수 있다.
 *    6E 61 6D 65 20 20 20 20 20 20 00 86 14 73 00  : name      
 *    00 1C 54 00 00 00 1F 55 35 31 9A              :
 *    
 * </pre>
 * 
 * @author
 * @version 1.0
 */

// public final class AsyncLogWriter extends Thread
public final class AsyncLogWriter extends Writer implements Runnable
{

	private static boolean isRunning = true;
	
	/*
     * Queue에 데이타가 쌓인후 바로 출력하지 않고, 어느정도 쌓이는 시간을 설정한다.
     */
	private static final int BUF_TIME = 10; // 10 mSec[최소:10]

	/*
     * backgournd thread 최대한 대기하는 시간, 그 이후에는 그냥 죽는다.
     */
	private static final long LIVE_TIME = 10000; // 10초

	/*
     * background 의 최대 동시 Thread수 (Procces수*5 갯수 이상 넘지 않는 것이 좋다.)
     */
	private static final int MAX_THREAD_NUM = 10;

	private static final int MIN_THREAD_NUM = 3;

	private PrintStream out; // 출력 stream

	private LogFormat format; // 결과물을 formating하기위한 class

	private Queue que; // 데이타가 쌓이는 Queue

	private AsyncLogWriter backRunner = null; // Queue에 쌓인 데이타를 출력하는 Thread

	// private boolean notLog = false; // 로그를 출력할 것인가? false:출력/true:안출력

	private static Hashtable queues = new Hashtable(); // Queue를 널기위한

	private static Hashtable bgRunners = new Hashtable(); // backgournd
	
	
	// thread를

	// 위한

	/*
     * Thread Pool
     */
	private static ThreadPool pool = new ThreadPool("AsyncLogWriter", MIN_THREAD_NUM, MAX_THREAD_NUM,
			Thread.NORM_PRIORITY - 1);

	// ThreadPool

	private AsyncLogWriter() {
	}

	/**
     * 기본 생성자
     * 
     * @param out
     */
	public AsyncLogWriter(PrintStream out) {
		// this(out, null);
		this(out, new NormalLogFormat());
	}

	/**
     * 여기서 PrintStream에 따라 Queue를 생성하고 그곳에 출력할 데이타를 차곡차곡 쌓는다. 다음 backgournd(thread)로 Queue를 입력값을 가지는 새로운 AsyncLogWriter를
     * 생성하고, thread로 실행시키며, 이 thread가 queue가 다 소진될때까지 출력작업을 계속하며, 모두 끝나면, wait()상태가 된다.
     * 
     * 다시 출력이 들어오면, Queue에 데이타를 넣고, 예전의 thread를 bgRunners에서 가져와 notify시킨다.
     */
	public AsyncLogWriter(PrintStream out, LogFormat format) {
		this.out = out;
		this.format = format;		
		synchronized (queues) {
			this.que = (Queue) queues.get(out);
			if (que == null) {
				this.que = new Queue();
				queues.put(out, que);
			}
		}

	}
	
	public static void stopService()
	{
		isRunning=false;
	}

	/** * Writer 구현부 ** */

	public void close()
	{
		reset();

		if (out != System.out || out != System.err)
			out.close();
	}

	/**
     * 출력log를 flushing 한다.
     * 
     */
	public void flush()
	{
		out.flush();
	}

	/**
     * 
     * 
     */
	public void write(char cbuf[], int off, int len, boolean isDWrite) throws IOException
	{
		LogInfo info = new LogInfo(null, null, new String(cbuf, off, len), isDWrite);

		push(info);

		info = null;
	}
	
	public void write(char cbuf[], int off, int len) throws IOException
	{
		write(cbuf, off, len, false);
	}

	/**
     * 초기 PrintStream에 따른 Queue 생성또는 재활용과, Queue에 쌓인 로그를 출력하기 위한 thread를 생성 또는 깨운다.
     * 
     */
	private void push(LogInfo info)
	{
		try {
			que.enqueue(info);

			if (backRunner == null)
				this.backRunner = (AsyncLogWriter) bgRunners.get(out);

			if (backRunner == null || backRunner.status == DIEING || backRunner.status == END) {
				/*
                 * while(backRunner != null && backRunner.status == DIEING) { // DIEING 상태는 null이 언제될지 모르기 때문에 looping하면
                 * null이 될때까지 기다린다. try{ Thread.sleep(10); }catch(Exception e) {} }
                 */

				backRunner = new AsyncLogWriter(this);
				bgRunners.put(out, backRunner);

				pool.execute(backRunner);
			}

			else if (backRunner.status == WAITING)
				backRunner.wakeup();

			else if (backRunner.status == RUNNING) {
				// System.out.println("=== RUNNING");
				// que.enqueue(info);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
     * hashtable에서 queue와 background thread를 제거하고, 초기화 시킨다.
     */
	private synchronized void reset()
	{
		out.flush();
		// que=null;
		// queues.remove(out);

		bgRunners.remove(out);

		this.backRunner = null;

		// System.out.println("reset. remain queue:"+queues.size());

	}

	public synchronized void write(String data, boolean isDWrite)
	{
		// if(notLog) return;

		LogInfo info = new LogInfo(null, null, data, isDWrite);

		push(info);

	}

	public synchronized void write(byte[] data, boolean isDWrite)
	{
		// if(notLog) return;

		LogInfo info = new LogInfo(null, null, data, isDWrite);
		push(info);

	}

	public synchronized void write(String sMode, String serverId, String data, boolean isDWrite)
	{
		// if(notLog) return;

		LogInfo info = new LogInfo(sMode, serverId, data, isDWrite);
		push(info);

	}

	public synchronized void write(String sMode, String serverId, byte[] data, boolean isDWrite)
	{
		// if(notLog) return;

		LogInfo info = new LogInfo(sMode, serverId, data, isDWrite);
		push(info);

	}

	public synchronized void write(String sMode, String serverId, Throwable err, boolean isDWrite)
	{
		// if(notLog) return;

		LogInfo info = new LogInfo(sMode, serverId, err, isDWrite);
		push(info);

	}

	/** *********** thread로 동작하는 놈에서 사용되는 Methods *************** */

	private AsyncLogWriter parent;

	private static final int RUNNING = 0;

	private static final int WAITING = 1;

	private static final int DIEING = 2;

	private static final int END = 3;

	int status = RUNNING; // 현재 이 Thread가 활동중인가?

	private AsyncLogWriter(AsyncLogWriter parent) {
		this.parent = parent;

		// System.out.println("background printer thread init");
	}

	public void run()
	{
		// System.out.println("background Thread runnung");
		try {
			Thread.sleep(BUF_TIME);
			// 어느 정도 데이타가 쌍일때까지 시간 Buffering을 한다.

			// print(), printWithFormat()은 looping하면서 queue의 내용을 출력한다.
			if (parent.format == null)
				print();
			else
				printWithFormat();

		} catch (Exception e) {
		}

		/*
         * looping을 빠져나왔다는 의미는 일정시간동안 활동하지 않아 loop를 빠져나온것이다.
         */
		parent.reset();

		parent = null;

		status = END;

	}

	/**
     * format에 맞추어 출력하는 logic
     * 
     */
	private void printWithFormat()
	{
		while (isRunning) {
			if (parent.que.isEmpty()) {
				status = WAITING;

				// parent.out.flush();

				try {

					wait(LIVE_TIME);

					Thread.sleep(BUF_TIME); // 어느 정도 시간 Buffering을 한다.

					// WAITING 상태에서 que가 들어오는 시점과 empty확인 시점은 동일하면 않된다.

					if (parent.que.isEmpty()) // 깨어났는데 queue에 데이타가 없으면, 이 쓰레드는
					// 죽어버린다.
					{
						status = DIEING;

						return;
					}
				} catch (Exception e) {
				}

			}

			// parent.que.pop();
			LogInfo log = (LogInfo) parent.que.dequeue();

			// System.out.println(parent.format.formating(log) );
			parent.out.println(parent.format.formating(log));

			// 우선 APM 서버에 그때그때 데이터를 줘야하기 때문에...
			// 비동기 로그라 성능에 문제가 없을것 같으므로...
			parent.out.flush();
			
			writeDB(log);

		}
		System.out.print("stop async log service - printWithFormat");
	}

	/**
     * format에 없는 형태의 로그출력 print()와 printWithFormat()으로 나눈이유는 매번 parent.format을 검사하는것이 비효율적이기 때문에
     */
	private void print()
	{
		while (isRunning) {
			if (parent.que.isEmpty()) {
				status = WAITING;

				// parent.out.flush();

				try {

					wait(LIVE_TIME);

					Thread.sleep(BUF_TIME); // 어느 정도 시간 Buffering을 한다.

					// WAITING 상태에서 que가 들어오는 시점과 empty확인 시점은 동일하면 않된다.

					if (parent.que.isEmpty()) // 깨어났는데 queue에 데이타가 없으면, 이 쓰레드는
					// 죽어버린다.
					{
						status = DIEING;

						return;

					}
				} catch (Exception e) {
				}

			}

			LogInfo log = (LogInfo) parent.que.dequeue();

			parent.out.println(log.strData);			
			parent.out.flush();
			writeDB(log);
		}
		System.out.print("stop async log service - print");
	}

	/**
     * 잠들어 있는 background single thread를 깨운다.
     */
	private synchronized void wakeup()
	{
		notify();
		status = RUNNING;
	}

	/*
     * protected void finalize() throws Throwable { System.out.println("It performs garbage Collection");
     * super.finalize(); }
     */
	
	public void writeDB(LogInfo info)
	{	
		if(info.isDBWrite)
			DBLogWriter.write(info.threadId, info.serverId, info.sMode, info.strData);		
	}
}