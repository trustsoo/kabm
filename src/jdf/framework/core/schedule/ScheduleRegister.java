/*
 * @(#)SchedulerRegister.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */
 
package jdf.framework.core.schedule;


import jdf.framework.core.Config;
import jdf.framework.core.Configurable;
import jdf.framework.core.Configuration;
import jdf.framework.core.SimpleConfig;
import jdf.framework.core.log.Logger;
import jdf.framework.core.service.AbstractManager;
import jdf.framework.core.util.DateTime;
import jdf.framework.core.xml.XMLReferer;

import java.util.*;

/**
 * <b><code>SchedulerRegister</code></b>
 * <p>
 * 화면 Tr code에 관계된 내용을 읽어 메모리에 적재하는 작업과 
 * Screen code객체를 return한다.
 * </p>
 * 
 * @author
 * @version 1.0
 */
 

public class ScheduleRegister extends AbstractManager
{
	private final String LOG_ID="<at:ScheduleRegister> ";
	
	
	private static Map _basket = new HashMap();
	
	
	private final static long SEC = 1000;
	private final static long MIN = 60*SEC;
	private final static long HOUR = 60*MIN;

	/**
	 * 
	 * @uml.property name="timer"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private static Scheduler timer = null;

	/*
	 * 
	 */
    private String config_file = null;
    
    
    //private static ScheduleRegister mgr;
    
    private static Hashtable objStore = new Hashtable();
    
    private static Hashtable defaultEnv = null;
    
    private Hashtable env = null;
    
    
    /**
     * 초기 생성자
     *
     */
    private ScheduleRegister()
    {
        this(null);
    }
    
    
    /**
     * 초기 생성자
     *
     */
    private ScheduleRegister(Hashtable env)
    {
        
        if(env==null)
            env = getDefaultEnv();
        
        this.env = env;
        
        objStore.put(env, this);
    }
    
    
    /**
     * ScheduleRegister 기본 Instance 를 얻는다.
     *
     */
    public static ScheduleRegister getInstance()
    {
        return getInstance(null);
    }
    
    
    /**
     * ScheduleRegister 기본 Instance 를 얻는다.
     *
     */
    public static ScheduleRegister getInstance(Hashtable env)
    {
        if(env == null)
            env = getDefaultEnv();
        
        ScheduleRegister mgr = (ScheduleRegister) objStore.get(env);
        
        if(mgr==null)
        {
            mgr = new ScheduleRegister(env);
            objStore.put(env, mgr);
        }
        
        return mgr;
    }

	/**
	 * 기본 설정값을 정의한다.
	 * 
	 * @uml.property name="defaultEnv"
	 */
	private static Hashtable getDefaultEnv() {
		if (defaultEnv == null) {
			defaultEnv = new Hashtable();
			//defaultEnv.put(CONFIG_DIRECTORY, "./config");
			defaultEnv.put(CONFIG_DIRECTORY, Configuration.getConfigPath());
			defaultEnv.put(CONFIG_FILENAME, "schedule.xml");
		}

		return defaultEnv;
	}

    
	public void setConfigFile(String configfile) throws Exception
	{
        String tempfile = null;
        if(configfile == null)
        	config_file = CONFIG_FILENAME;
        else
        	config_file = configfile;		
	}        
    
  
    
    
    /**
     * 화면 Tr code에 관계된 내용을 읽어 메모리에 적재하는 작업을 한다.
     *
     *
     **/
	public void initialize() throws Exception
	{
	     
	     try
	     {
		    Logger.info.println(LOG_ID+"initialize.");
            //String dir = "./config";
	        //String fileName = "schedule.xml";
	        
	        String dir = (String) env.get(CONFIG_DIRECTORY);
	        String fileName = (String) env.get(CONFIG_FILENAME);
	        
	         
	        XMLReferer reader = new XMLReferer(dir, fileName);
        	        
        	        
            /*      XML 을 읽는다.       */
            reader.lookup("/hts/scheduler/task"); //lookup으로 고정
			
			
			if(timer != null)
			    timer.cancel();    // 기존에 time가 있으면 작업중지
			    
            timer = new Scheduler();   
			    
			    
			while( reader.next() )
			{
			    
			    
			    String name=null;
			    
			    try
			    {
			        name = reader.getString("name");
			        Class process = Class.forName( reader.getString("class") );
				    Runnable runner = (Runnable)process.newInstance();

				    /* 파라미터 설정 */
				    Config simpleConf = new SimpleConfig( reader.find("param").getAttributeMap() );
				    ( (Configurable)runner ).setConfigInfo( simpleConf );
					
			        String sPeriod = reader.getString("period");
			        String sTime = reader.getString("startTime");
			        String sScript = reader.getString("script");
			        
			        
			        long period=0;
			        
			        if(sPeriod !=null && sPeriod.indexOf("hour") >0 )
			        {
			            period = Long.parseLong(sPeriod.substring(0, sPeriod.indexOf("hour")));
			            period = period * HOUR;
			        }
			        
			        else if(sPeriod !=null && sPeriod.indexOf("min") >0 )
			        {
			            period = Long.parseLong(sPeriod.substring(0, sPeriod.indexOf("min")));
			            period = period * MIN;
			        }
			        
			        else if(sPeriod !=null && sPeriod.indexOf("sec") >0 )
			        {
			            period = Long.parseLong(sPeriod.substring(0, sPeriod.indexOf("sec")));
			            period = period * SEC;
			        }
			        
			        
			        
                    Schedule job;
                    
                    if (sScript != null && sScript.length() > 0)
                    {
                        job = new Schedule(name, sScript, process);
                            
                        ScheduleParser parser = new ScheduleParser(sScript);
                        SchedulerTask task = new SchedulerTask(parser, runner);
                        timer.schedule(task);

                        job.setTask(task);
                    }
                    else
                    {
                        // 시작시간이 있는 경우
                        if(sTime !=null && sTime.length() > 0)
                        {
                            String yyyyMMdd = DateTime.getShortDateString(); // 오늘 yyyyMMdd
                            
                            Date startTime = DateTime.getDate( yyyyMMdd+sTime, "yyyyMMddHH:mm:ss");
                            
                            if(startTime.getTime() <= System.currentTimeMillis())
                            {
                                Calendar cal = Calendar.getInstance();
                
                                cal.add(Calendar.DATE, 1);
                
                                yyyyMMdd=DateTime.getString(cal.getTime(), "yyyyMMdd");
                                startTime = DateTime.getDate( yyyyMMdd+sTime, "yyyyMMddHH:mm:ss");
                            }
                            
                            
                            
                            
                            job = new Schedule(name, startTime, period, process);
                            
                            ScheduleParser parser = new ScheduleParser(startTime, period);
    			            SchedulerTask task = new SchedulerTask(parser, runner);
    			            timer.schedule(task);
                            
                            job.setTask(task);
                           
                            
                        }
                        // 시작시간이 없는 경우
                        else
                        {
                            job = new Schedule(name, period, process);
                            
                            ScheduleParser parser = new ScheduleParser(period);
                            SchedulerTask task = new SchedulerTask(parser, runner);
                            timer.schedule(task);

                            job.setTask(task);
                        }
                    }
                    
			        
                    synchronized (_basket) {
                        _basket.put(name, job); 
                    }
        	        
        	        
    	            // 성공 Msg 출력
                    Logger.info.println(LOG_ID+"["+name+"] regist sucess");
        	    
        	    }
        	    catch(Exception e)
        	    {
        	        e.printStackTrace();
        	        // 실패 Msg 출력
        	        Logger.warn.println(LOG_ID+"["+name+"] fail. cause "+e.toString());
        	    }

        	
        	}
        	
        	//Logger.info.println("<Scheduler> "+registNum+" regist sucess.");
        }
        
        catch(Exception e)
        {
            //e.printStackTrace();
            throw e;
        }
        
	}



	public Map getScheduleMap()
	{
		return _basket;
	}
	
	
}	
	
