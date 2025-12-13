/*
 * @(#)Scheduler.java	1.7 00/02/02
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package jdf.framework.core.schedule;

import jdf.framework.core.log.Logger;

/**
 * 등록된 태스크(SchedulerTask)를 스케줄 하는 기능을 합니다.
 * 태스크는, 스크립트에 따라서 실행되며 스케줄 됩니다. 
 * 단일의 백그라운드 쓰레드를 이용하여, 등록되 테스크를 연속하여 실행시킵니다.
 * 주의사항으로, 태스크는 신속히 실행될 필요가 있습니다. 타이머 태스크의 완료에 시간이 너무 걸리면, 
 * 태스크를 실행시켜주는 내부 쓰레드가 「점유」됩니다. 
 * 이것에 의해 후속의 태스크의 실행이 늦어 위반한 태스크가 완료된 후, 
 * 연달아서 등록된 후속 테스크들이 실행되게 됩니다. 
 * 이것을 방지하기 위하여, 각각의 태스크를 내부 쓰레드가 동작시킬때, 태스크를 쓰레드로 동작시킬수 있습니다.
 * SchedulerTask 생성시, 쓰레드로 동작시킬지 여부를 결정지으면 됩니다.
 * 또한, 디폴트로서 태스크를 실행시케주는 내부 쓰레드는 「demon thread」로 실행되지 않기 때문에, 
 * 어플리케이션을 종료하지 않게 할 수 있습니다. 내부쓰레드를 즉시 완료 시키기 위하여, 
 * 호출측은 cancel 메소드를 호출할 필요가 있습니다. 
 * stop 메소드의 호출 등에 의해 내부쓰레드가 예상치 못한 종료시, 
 * 태스크를 스케줄 하려고 하면, cancel 메소드가 호출된 경우 처럼, IllegalStateException 가 발생합니다. 
 * 이 클래스는 쓰레드에 대해서 안전합니다. 외부의 동기화를 하지 않아도, 
 * 복수의 쓰레드로 단일의 Scheduler 오브젝트를 공유 할 수 있습니다. 
 * 
 * 이 클래스에서는, 리얼타임은 보증되지 않습니다. Object.wait(long) 메소드를 사용해, 태스크가 스케줄 됩니다. 
 * 
 * 구현상의 주의: 이 클래스는, 동시에 스케줄 된 다수의 태스크를 스켸줄링 합니다 
 * (수천개에서도 문제는 없습니다). 
 * 태스크 큐를 표현하기 위해서 바이너리 heap가 내부적으로 사용되기 때문에, 
 * 태스크를 스케줄 하는 비용은 O(log n)가 됩니다. n 는, 동시에 스케줄 된 태스크의 수입니다. 
 * 
 * 예)
 * Scheduler scheduler = new Scheduler();
 * 
 * ScheduleParser parser = new ScheduleParser("date on SUMOTUWETHFRSA time every 10s");
 * SchedulerTask task = new SchedulerTask(parser, new SchedulerTest());
 * //쓰레드로 동작시키고자 할때
 * //SchedulerTask task = new SchedulerTask(parser, new SchedulerTest(), true);
 * scheduler.schedule(task);
 * 
 * 사용될 스크립트를 ScheduleParser를 이용하여, 태스크 생성시 넘겨준 후, 스케줄러에 등록합니다.
 * SchedulerTest() : 실행될 객체로 반드시 Runnable을 구현해야 합니다.
 * 
 * 스크립트에 대한 설명은 ScheduleParser을 참고하시기 바랍니다.
 * 
 * @author  JeongHo Eun,<a href="mailto:94eun@hanmail.net">94eun@hanmail.net</a>
 * @version 1.0, 21/12/05
 * @see     ScheduleParser
 * @see     SchedulerTask
 */
public class Scheduler {

	/**
	 * The timer task queue.  This data structure is shared with the timer
	 * thread.  The timer produces tasks, via its various schedule calls,
	 * and the timer thread consumes, executing timer tasks as appropriate,
	 * and removing them from the queue when they're obsolete.
	 * 
	 * @uml.property name="queue"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private TaskQueue queue = new TaskQueue();

	/**
	 * The timer thread.
	 * 
	 * @uml.property name="thread"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private SchedulerThread thread = new SchedulerThread(queue);


    /**
     * This object causes the timer's task execution thread to exit
     * gracefully when there are no live references to the Scheduler object and no
     * tasks in the timer queue.  It is used in preference to a finalizer on
     * Scheduler as such a finalizer would be susceptible to a subclass's
     * finalizer forgetting to call it.
     */
    private Object threadReaper = new Object() {
        protected void finalize() throws Throwable {
            synchronized(queue) {
                thread.newTasksMayBeScheduled = false;
                queue.notify(); // In case queue is empty.
            }
        }
    };

    /**
     * Creates a new timer.  The associated thread does <i>not</i> run as
     * a daemon.
     *
     * @see Thread
     * @see #cancel()
     */
    public Scheduler() {
        thread.start();
    }

    /**
     * Creates a new timer whose associated thread may be specified to 
     * run as a daemon.  A deamon thread is called for if the timer will
     * be used to schedule repeating "maintenance activities", which must
     * be performed as long as the application is running, but should not
     * prolong the lifetime of the application.
     *
     * @param isDaemon true if the associated thread should run as a daemon.
     *
     * @see Thread
     * @see #cancel()
     */
    public Scheduler(boolean isDaemon) {
        thread.setDaemon(isDaemon);
        thread.start();
    }

    /**
     * Schedule the specifed timer task for execution at the specified
     * time with the specified period, in milliseconds.  If period is
     * positive, the task is scheduled for repeated execution; if period is
     * zero, the task is scheduled for one-time execution. Time is specified
     * in Date.getTime() format.  This method checks timer state, task state,
     * and initial execution time, but not period.
     *
     * @throws IllegalArgumentException if <tt>time()</tt> is negative.
     * @throws IllegalStateException if task was already scheduled or
     *         cancelled, timer was cancelled, or timer thread terminated.
     */
    public void schedule(SchedulerTask task) {
        
		synchronized(queue) {
            if (!thread.newTasksMayBeScheduled)
                throw new IllegalStateException("Scheduler already cancelled.");

            synchronized(task.lock) 
            {
                if (task.state != SchedulerTask.VIRGIN)
                    throw new IllegalStateException("Task already scheduled or cancelled");

                task.nextExecutionTime = task.scheduledExecutionTime() + System.currentTimeMillis();
  
                task.state = SchedulerTask.SCHEDULED;
            }

            queue.add(task);
            if (queue.getMin() == task)
                queue.notify();
        }
    }

    /**
     * Terminates this timer, discarding any currently scheduled tasks.
     * Does not interfere with a currently executing task (if it exists).
     * Once a timer has been terminated, its execution thread terminates
     * gracefully, and no more tasks may be scheduled on it.
     *
     * <p>Note that calling this method from within the run method of a
     * timer task that was invoked by this timer absolutely guarantees that
     * the ongoing task execution is the last task execution that will ever
     * be performed by this timer.
     *
     * <p>This method may be called repeatedly; the second and subsequent 
     * calls have no effect.
     */
    public void cancel() {
        synchronized(queue) {
            thread.newTasksMayBeScheduled = false;
            queue.clear();
            queue.notify();  // In case queue was already empty.
        }
    }
}

/**
 * This "helper class" implements the timer's task execution thread, which
 * waits for tasks on the timer queue, executions them when they fire,
 * reschedules repeating tasks, and removes cancelled tasks and spent
 * non-repeating tasks from the queue.
 */
class SchedulerThread extends Thread {
    /**
     * This flag is set to false by the reaper to inform us that there
     * are no more live references to our Scheduler object.  Once this flag
     * is true and there are no more tasks in our queue, there is no
     * work left for us to do, so we terminate gracefully.  Note that
     * this field is protected by queue's monitor!
     */
    boolean newTasksMayBeScheduled = true;

	/**
	 * Our Scheduler's queue.  We store this reference in preference to
	 * a reference to the Scheduler so the reference graph remains acyclic.
	 * Otherwise, the Scheduler would never be garbage-collected and this
	 * thread would never go away.
	 * 
	 * @uml.property name="queue"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private TaskQueue queue;


    SchedulerThread(TaskQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            mainLoop();
        } finally {
            // Somone killed this Thread, behave as if Scheduler cancelled
            synchronized(queue) {
                newTasksMayBeScheduled = false;
                queue.clear();  // Eliminate obsolete references
            }
        }
    }

    /**
     * The main timer loop.  (See class comment.)
     */
    private void mainLoop() {
        while (true) {
            try {
                SchedulerTask task;
                boolean taskFired;
                synchronized(queue) {
                    // Wait for queue to become non-empty
                    while (queue.isEmpty() && newTasksMayBeScheduled)
                        queue.wait();
                    if (queue.isEmpty())
                        break; // Queue is empty and will forever remain; die

                    
                    // Queue nonempty; look at first evt and do the right thing
                    long currentTime, executionTime;
                    task = queue.getMin();
                    
                    synchronized(task.lock) {
                        
                        if (task.state == SchedulerTask.CANCELLED) 
                        {
                        	
                        	
                            queue.removeMin();
                            
                            Logger.info.println("<at:Scheduler>"+task.getName()+" removed.");
                            
                            continue;  // No action required, poll queue again
                        }
                        
                        currentTime = System.currentTimeMillis();
                        executionTime = task.nextExecutionTime;

                        /*if ( taskFired = (executionTime<=currentTime) )
                        {
			                queue.rescheduleMin(task.scheduledExecutionTime() + currentTime);
                        }*/
                        taskFired = (executionTime<=currentTime) ? true : false;

                    }

                    if (!taskFired) // Task hasn't yet fired; wait
                        queue.wait(executionTime - currentTime);
                }

                if (taskFired)  // Task fired; run it, holding no locks
                {
                    task.run();
                    queue.rescheduleMin(task.scheduledExecutionTime() + System.currentTimeMillis());
                }

            } catch(InterruptedException e)
			{
            }
        }
    }
}

/**
 * This class represents a timer task queue: a priority queue of SchedulerTasks,
 * ordered on nextExecutionTime.  Each Scheduler object has one of these, which it
 * shares with its SchedulerThread.  Internally this class uses a heap, which
 * offers log(n) performance for the add, removeMin and rescheduleMin
 * operations, and constant time performance for the the getMin operation.
 */
class TaskQueue {

	/**
	 * Priority queue represented as a balanced binary heap: the two children
	 * of queue[n] are queue[2*n] and queue[2*n+1].  The priority queue is
	 * ordered on the nextExecutionTime field: The SchedulerTask with the lowest
	 * nextExecutionTime is in queue[1] (assuming the queue is nonempty).  For
	 * each node n in the heap, and each descendant of n, d,
	 * n.nextExecutionTime <= d.nextExecutionTime.
	 * 
	 * @uml.property name="queue"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	private SchedulerTask[] queue = new SchedulerTask[128];

    /**
     * The number of tasks in the priority queue.  (The tasks are stored in
     * queue[1] up to queue[size]).
     */
    private int size = 0;

    /**
     * Adds a new task to the priority queue.
     */
    void add(SchedulerTask task) {
        // Grow backing store if necessary
        if (++size == queue.length) {
            SchedulerTask[] newQueue = new SchedulerTask[2*queue.length];
            System.arraycopy(queue, 0, newQueue, 0, size);
            queue = newQueue;
        }

        queue[size] = task;
        fixUp(size);
    }

    /**
     * Return the "head task" of the priority queue.  (The head task is an
     * task with the lowest nextExecutionTime.)
     */
    SchedulerTask getMin() {
        return queue[1];
    }

    /**
     * Remove the head task from the priority queue.
     */
    void removeMin() {
        queue[1] = queue[size];
        queue[size--] = null;  // Drop extra reference to prevent memory leak
        fixDown(1);
    }

    /**
     * Sets the nextExecutionTime associated with the head task to the 
     * specified value, and adjusts priority queue accordingly.
     */
    void rescheduleMin(long newTime) {
        queue[1].nextExecutionTime = newTime;
        fixDown(1);
    }

    /**
     * Returns true if the priority queue contains no elements.
     */
    boolean isEmpty() {
        return size==0;
    }

    /**
     * Removes all elements from the priority queue.
     */
    void clear() {
        // Null out task references to prevent memory leak
        for (int i=1; i<=size; i++)
            queue[i] = null;

        size = 0;
    }

    /**
     * Establishes the heap invariant (described above) assuming the heap
     * satisfies the invariant except possibly for the leaf-node indexed by k
     * (which may have a nextExecutionTime less than its parent's).
     *
     * This method functions by "promoting" queue[k] up the hierarchy
     * (by swapping it with its parent) repeatedly until queue[k]'s
     * nextExecutionTime is greater than or equal to that of its parent.
     */
    private void fixUp(int k) {
        while (k > 1) {
            int j = k >> 1; //j = k /2;
            if (queue[j].nextExecutionTime <= queue[k].nextExecutionTime)
                break;
            SchedulerTask tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
            k = j;
        }
    }

    /**
     * Establishes the heap invariant (described above) in the subtree
     * rooted at k, which is assumed to satisfy the heap invariant except
     * possibly for node k itself (which may have a nextExecutionTime greater
     * than its children's).
     *
     * This method functions by "demoting" queue[k] down the hierarchy
     * (by swapping it with its smaller child) repeatedly until queue[k]'s
     * nextExecutionTime is less than or equal to those of its children.
     */
    private void fixDown(int k) {   //k는 1
        int j;

        while ((j = k << 1) <= size)   // j = k*2;
        {
            if (j < size && queue[j].nextExecutionTime > queue[j+1].nextExecutionTime)
                j++; // j indexes smallest kid

            if (queue[k].nextExecutionTime <= queue[j].nextExecutionTime)
                break;

            SchedulerTask tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
            k = j;
        }

    }
}
