/*
 * @(#)SchedulerTask.java	1.6 00/02/02
 *
 * Copyright 1999, 2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package jdf.framework.core.schedule;

import jdf.framework.core.log.Logger;
import jdf.framework.core.pool.ThreadPool;
import jdf.framework.core.util.DateTime;

import java.util.Date;
import java.util.Timer;

/**
 * A task that can be scheduled for one-time or repeated execution by a Timer.
 * 
 * @author  Josh Bloch
 * @version 1.6, 02/02/00
 * @see	    Timer
 * @since   1.3
 */
public class SchedulerTask
{
    // 기본 생성 Thread 수와 최대 Thread 생성수

    private static int INIT_SIZE = 2;

    private static int MAX_SIZE = 4;

	/**
	 * 
	 * @uml.property name="pool"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected static ThreadPool pool = new ThreadPool(
		"SchedulerTask",
		INIT_SIZE,
		MAX_SIZE);

	

	/**
     * This object is used to control access to the SchedulerTask internals.
     */
    final Object lock = new Object();

    /**
     * The state of this task, chosen from the constants below.
     */
    int state = VIRGIN;

    /**
     * This task has not yet been scheduled.
     */
    static final int VIRGIN = 0;

    /**
     * This task is scheduled for execution.  If it is a non-repeating task,
     * it has not yet been executed.
     */
    static final int SCHEDULED   = 1;

    /**
     * This non-repeating task has already executed (or is currently
     * executing) and has not been cancelled.
     */
    static final int EXECUTED    = 2;

    /**
     * This task has been cancelled (with a call to SchedulerTask.cancel).
     */
    static final int CANCELLED   = 3;

    /**
     * Next execution time for this task in the format returned by
     * System.currentTimeMillis, assuming this task is schedule for execution.
     * For repeating tasks, this field is updated prior to each task execution.
     */
    long nextExecutionTime;


	private Runnable runner;

	/**
	 * 
	 * @uml.property name="parser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private ScheduleParser parser;

	private boolean isThread;

	private Date nextExecutionDate;
	
	private String name;

    /**
     * Creates a new timer task.
	 * 이 생성자는 스케쥴에 의해 객체(Runnable)가 동작할때 쓰레드로 동작하지 않는다.
	 * 객체의 동작이 너무 오래 걸려 다른 쓰레드에 영향을 미칠것으로 예상된다면, 
	 * 아래 생성자를 이용하여, isThread를 true로 한다.
	 *
	 * @param parser <code>ScheduleParser</code>로 해당 스케줄 스크립트를 담당하여, 다음 주기를 알려준다.
	 * @param runner <code>Runnable</code>을 구현한 스케쥴에 의해 동작하는 객체
     */
    public SchedulerTask(ScheduleParser parser, Runnable runner)
	{
		this(parser, runner, false);
    }

	/**
     * Creates a new timer task.
	 *
	 * @param parser <code>ScheduleParser</code>로 해당 스케줄 스크립트를 담당하여, 다음 주기를 알려준다.
	 * @param runner <code>Runnable</code>을 구현한 스케쥴에 의해 동작하는 객체
	 * @param isThread 스케쥴러가 동작할때 쓰레드로 동작하지 여부
     */
    public SchedulerTask(ScheduleParser parser, Runnable runner, boolean isThread)
	{
		this.parser = parser;
		this.runner = runner;
		this.isThread = isThread;

		try
		{
			parser.parser();
			
			long nexttime = parser.getNextWaitTimeFromNow();	    
			Logger.info.println("<f:SchedulerTask>["+name+"] Next Action Date: " + DateTime.getString( new Date(new Date().getTime() + nexttime), "yyyy년 M월 d일 E요일 HH:mm:ss") );
		}
		catch (ScheduleParser.ScheduleParseException e)
		{
		    Logger.warn.println(e.toString());
		}
    }
    
    
    public SchedulerTask(ScheduleParser parser, Runnable runner, String name, boolean isThread)
	{
		this.parser = parser;
		this.runner = runner;
		this.isThread = isThread;
		this.name = name;

		try
		{
			parser.parser();
			
			long nexttime = parser.getNextWaitTimeFromNow();	    
			Logger.info.println( "<f:SchedulerTask>["+name+"] Next Action Date: " + DateTime.getString( new Date(new Date().getTime() + nexttime), "yyyy년 M월 d일 E요일 HH:mm:ss") );
		}
		catch (ScheduleParser.ScheduleParseException e)
		{
			Logger.warn.println(e.toString());
		}
    }

	/**
     * The action to be performed by this timer task.
     */
    public void run()
	{
		try
		{
			
			if (runner != null)
			{
				if (isThread)
                {
                    pool.execute(runner);
                }
                else
                {
                    runner.run();
                }
				long nexttime = parser.getNextWaitTimeFromNow();	
				Logger.info.println( "<f:SchedulerTask>["+name+"] Next Action Date: " + DateTime.getString( new Date(new Date().getTime() + nexttime), "yyyy년 M월 d일 E요일 HH:mm:ss") );
			}
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

    public boolean cancel() {
        synchronized(lock) {
            boolean result = (state == SCHEDULED);
            state = CANCELLED;
            return result;
        }
    }


    /**
	 * 현재시간을 기준으로 다음 주기까지 걸리는 시간을 long값으로 반환한다.
     * @return 다음주기시간 - 현재시간
	 */
    public long scheduledExecutionTime() {
        synchronized(lock) {
            long waittime = parser.getNextWaitTimeFromNow();
            
			if (waittime == -1)
            {
                this.state = SchedulerTask.CANCELLED;
                waittime = 1000;    //임의적으로 딜레이
            }

            nextExecutionDate = new Date(new Date().getTime() + waittime);	//Date 객체
            
            return waittime;
        }
    }
	
	/**
	 * 다음 이 스케쥴 task가 동작할 시간을 알고 싶을때,
	 * 다음 주기에 해당하는 <code>Date</code> 객체를 반환한다.
	 *
	 * @param Date 다음 주기의 <code>Date</code> 객체
	 */
	public Date nextExecutionDate()
	{
		return nextExecutionDate;
	}
	
	public String getName()
	{
		return name == null ? "unknown" : name;
		
	}

}
