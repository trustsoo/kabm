package jdf.framework.core.util;


import java.util.*;
import java.lang.reflect.*;



/**
 * 쓰레드를 실행시 시간제한 또는 자동 재실행이 가능하게 만든다.
 *
 * 
 *
 * <p>
 * 이 클래스는 두가지 기능을 가지고 있다.<br>
 * 하나는 Thread의 실행시 수행완결시간을 제한 할 수 있으며, 또하나는 Thread의 실행을 모니터링
 * 하며 죽었을 경우 자동으로 재 실행을 시킨다.
 * </p>
 *
 * @author 
 * @version 1.0
 */

public class ThreadRunner extends Thread
{
	//private final static String PRG_NM="ThreadRunnder";
	private Thread clientThread;


	private long timeout;
	private long checkIntervalTime;


	private boolean isContinue = true;


	private int THIS_MODE=0;

	private static final int ANY	 = 99;
	private static final int TIMEOUT = 0;
	private static final int RESTART = 1;
	private static final int RUN_AT_TIME = 2;

	private int atHour=ANY;
	private int atMin=ANY;
	private int atSec=ANY;




	private ThreadRunner(Thread thread)
	{
		clientThread = thread;
		timeout = 0;
		checkIntervalTime = 0;

	}




	private void setMODE(int mode)
	{
		THIS_MODE = mode;
	}




	private void setTimeout( long time )
	{
		timeout = time;
	}




	private void setIntervalTime( long time )
	{
		checkIntervalTime = time;
	}




	private synchronized static Thread clone(Thread object)
	{

		try
		{
				Class c = object.getClass();
				Object newObject = null;

				try {
					newObject = c.newInstance();
				}
				catch(Exception e ){
					e.printStackTrace();
					return null;
				}

				Field[] field = c.getFields();
				for (int i=0 ; i<field.length; i++) {
					try {
						Object f = field[i].get(object);
						field[i].set(newObject, f);
					}
					catch(Exception e){
					}
				}

				return (Thread) newObject;



		}
		catch(Exception ex) {ex.printStackTrace();}

		return null;
	}




	/**
	 *  실행제한시간을 가지고 Thread를 실행시킨다.
	 *
	 * @param thread  실행하고자 하는 Thread
	 * @param time  timeout 시간
	 */
	public static ThreadRunner startTimeout( Thread thread, long time)
	{

		ThreadRunner runner = new ThreadRunner( thread );

		runner.setMODE( TIMEOUT );
		runner.setTimeout ( time );
		runner.start();

		return runner;

	}


	/**
	 *  일정시간간격으로 실행여부를 체크하며, 죽었을 경우 자동으로
	 * 재실행 시킨다. 단 T
	 *
	 * @param thread  실행하고자 하는 Thread
	 * @param time  time check시간 간격
	 */
	public static ThreadRunner startRestartable(Thread thread, long time)
	{

		ThreadRunner runner = new ThreadRunner( thread );


		runner.setMODE( RESTART );
		runner.setIntervalTime( time );
		runner.start();

		return runner;

	}



	/**
	 * 특정시간에 daemon으로 Thread를 실행시킨다. 
	 * 
	 *
	 * time format hh:mm:ss
	 * ex) 메일 밤 2시 - 02:00:00
	 * ex) 매일 30분 마다 - **:30:00
	 *
	 **/
	public static ThreadRunner startAtTime(Thread thread, String time) throws Exception
	{
		ThreadRunner runner = new ThreadRunner( thread );
		
	    
	    runner.setDaemon(true);

		runner.setMODE( RUN_AT_TIME );
		runner.setAtTime( time );
		runner.start();

		return runner;
	}




	// 11:23:42

	private void setAtTime(String time) throws Exception
	{
		String tmpHour;
		String tmpMin;
		String tmpSec;


		try {
			tmpHour = time.substring(0,2);
			tmpMin = time.substring(3,5);
			tmpSec = time.substring(6,8);
		}
		catch(Exception ex)
		{
			throw new Exception("not formal time structure");
		}

		try{
			atHour = Integer.parseInt(tmpHour);
		} catch(NumberFormatException nfex) {}

		try{
			atMin = Integer.parseInt(tmpMin);
		} catch(NumberFormatException nfex) {}

		try{
			atSec = Integer.parseInt(tmpSec);
		} catch(NumberFormatException nfex) {}

    }

    
    private final static long SEC = 1000L;
    private final static long MIN = 60*SEC;
    private final static long HOUR = 60*MIN;
    
    

    /**
     * 기다릴 시간을 리턴한다.
     *
     */
    private long getTimeDiff()
    {
        long atTime=0;    // 실행하고 하는 시간
        long nowTime=0;   // 지금시간
        
        long timeGap = MIN;
        
        
        GregorianCalendar cal = new GregorianCalendar();
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);
		
        
        atTime += atSec * SEC;
        nowTime += ss * SEC;
        
        
        if(atMin != ANY)
        {
            atTime += atMin * MIN;
            nowTime += mm * MIN;
            
            timeGap = HOUR;
        }
        
            
        if(atHour != ANY)
        {
            atTime += atHour*HOUR;
            nowTime += hh*HOUR;
            
            timeGap = 24*HOUR;
        }
        
        
        long timeDiff = atTime - nowTime;
        
        System.out.println("getTimeDiff"+timeDiff);
        
        if (timeDiff > SEC )
            return timeDiff;
        else
            return timeDiff + timeGap;
        
    }
    
        
    

/*
	private boolean isRightNow()
	{

		GregorianCalendar cal = new GregorianCalendar();
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);

		boolean isNow = false;

		if(atSec == ss)
		{
			if( atMin == ANY )
			{
				if(atHour == ANY)
					isNow = true;
			}
			else if (atMin == mm)
				if( atHour == ANY || atHour == hh)
					isNow = true;
		}

		//System.out.println( hh+":"+mm+":"+ss);
		//System.out.println( atHour+":"+atMin+":"+atSec);

		return isNow;

	}

*/



	public void stopThread()
	{
		isContinue = false;
		clientThread.interrupt();
		this.interrupt();
	}



	public void run()
	{
		try
		{
			Thread crntThread = clientThread;


			switch (THIS_MODE)
			{
				case TIMEOUT:

					crntThread.start();
					
					crntThread.join( timeout );

					crntThread.interrupt();
					
					crntThread = null;
					break;


				case RESTART:

					crntThread.start();
					while ( isContinue )
					{

						if ( ! crntThread.isAlive() )
						{
							//Status.println(PRG_NM, "thread is dead. restart thread!!");

							crntThread = clone (crntThread);
							
							crntThread.start();

							clientThread = crntThread;
						}

						Thread.sleep( checkIntervalTime);

					}
					break;

				case RUN_AT_TIME:

					while (true)
					{
						
						Thread.sleep( getTimeDiff() );
						
						if (isContinue)
						{
						    crntThread = clone (crntThread);
					        crntThread.start();
					    }
					    else
					        break;
						
				 	}
					break;
			}
		}
		catch(InterruptedException iex)
		{
			//Status.println(PRG_NM, clientThread.getClass().getName()+" thread is interrupted by administrator." );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}





	public boolean isClientAlive()
	{
		return clientThread.isAlive();
	}



}