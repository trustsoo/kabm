package jdf.framework.core.log;


/**
 * @(#) Logger.java
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team, 
 * Application Infrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 * 
 * NOTICE !      You can copy or redistribute this code freely, 
 * but you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author  WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 */

public class Logger
{
	private final static String LOG_ID="<f:Logger> ";
	
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public final static String DEFAULT_LOG_TIME_FORMAT="yyyyMMdd HHmmssSSS";
	

    /**
     * The "critical system level error" log stream. This stream is already 
     * open and ready to accept output data. 
     * <p>
     * Typically this stream corresponds to display output or another 
     * output destination specified by the host environment or user. By 
     * convention, this output stream is used to display error messages 
     * or other information that should come to the immediate attention 
     * of a user even if the principal output stream, the value of the 
     * variable <code>out</code>, has been redirected to a file or other 
     * destination that is typically not continuously monitored. 
     *
     */
    public final static LoggerWriter sys = getLoggerWriter(LoggerWriter.SYS);

    
    /**
     * The "critical business error" log stream. This stream is already 
     * open and ready to accept output data. 
     * <p>
     * Typically this stream corresponds to display output or another 
     * output destination specified by the host environment or user. By 
     * convention, this output stream is used to display error messages 
     * or other information that should come to the immediate attention 
     * of a user even if the principal output stream, the value of the 
     * variable <code>out</code>, has been redirected to a file or other 
     * destination that is typically not continuously monitored. 
     *
     */
    public final static LoggerWriter err = getLoggerWriter(LoggerWriter.ERR);

    
    /**
     * The "business warnning" log stream. This stream is already 
     * open and ready to accept output data. Typically this stream 
     * corresponds to display output or another output destination 
     * specified by the host environment or user. 
     * <p>
     * For simple stand-alone Java applications, a typical way to write 
     * a line of output data is: 
     * <ul><code>Logger.out.println(data)</code></ul>
     * <p>
     * See the <code>println</code> methods in class <code>LoggerWriter</code>. 
     *
     * @see     jdf.framework.core.log.LoggerWriter#println()
     * @see     jdf.framework.core.log.LoggerWriter#println(boolean)
     * @see     jdf.framework.core.log.LoggerWriter#println(char)
     * @see     jdf.framework.core.log.LoggerWriter#println(char[])
     * @see     jdf.framework.core.log.LoggerWriter#println(double)
     * @see     jdf.framework.core.log.LoggerWriter#println(float)
     * @see     jdf.framework.core.log.LoggerWriter#println(int)
     * @see     jdf.framework.core.log.LoggerWriter#println(long)
     * @see     jdf.framework.core.log.LoggerWriter#println(java.lang.Object)
     * @see     jdf.framework.core.log.LoggerWriter#println(java.lang.String)
     */
    public final static LoggerWriter warn = getLoggerWriter(LoggerWriter.WARN);
    
    /**
     * The "business infomation" log stream. This stream is already 
     * open and ready to accept output data. Typically this stream 
     * corresponds to display output or another output destination 
     * specified by the host environment or user. 
     * <p>
     * For simple stand-alone Java applications, a typical way to write 
     * a line of output data is: 
     * <ul><code>Logger.out.println(data)</code></ul>
     * <p>
     * See the <code>println</code> methods in class <code>LoggerWriter</code>. 
     *
     * @see     jdf.framework.core.log.LoggerWriter#println()
     * @see     jdf.framework.core.log.LoggerWriter#println(boolean)
     * @see     jdf.framework.core.log.LoggerWriter#println(char)
     * @see     jdf.framework.core.log.LoggerWriter#println(char[])
     * @see     jdf.framework.core.log.LoggerWriter#println(double)
     * @see     jdf.framework.core.log.LoggerWriter#println(float)
     * @see     jdf.framework.core.log.LoggerWriter#println(int)
     * @see     jdf.framework.core.log.LoggerWriter#println(long)
     * @see     jdf.framework.core.log.LoggerWriter#println(java.lang.Object)
     * @see     jdf.framework.core.log.LoggerWriter#println(java.lang.String)
     */
    public final static LoggerWriter info = getLoggerWriter(LoggerWriter.INFO);

    
    /**
     * The "debug & tracing" log stream. This stream is already 
     * open and ready to accept output data. 
     * <p>
     * Typically this stream corresponds to display output or another 
     * output destination specified by the host environment or user. By 
     * convention, this output stream is used to display error messages 
     * or other information that should come to the immediate attention 
     * of a user even if the principal output stream, the value of the 
     * variable <code>out</code>, has been redirected to a file or other 
     * destination that is typically not continuously monitored. 
     *
     */
    public final static LoggerWriter debug = getLoggerWriter(LoggerWriter.DEBUG);
    
    
    
    
    public final static LoggerWriter user = getLoggerWriter(LoggerWriter.USER);
    
    
    
    
    public final static LoggerWriter sql = getLoggerWriter(LoggerWriter.SQL);
    
    
    
    public final static LoggerWriter packet = getLoggerWriter(LoggerWriter.PACKET);
    
    
    /**
     * 성능측정 로그
     */
    public final static LoggerWriter mon = getLoggerWriter(LoggerWriter.MONITOR);
    
    


    /**
     * Don't let anyone instantiate this class
     */
    private Logger() {  }
    
    

    static String getLogId(int mode) 
    {
        String logId = null;
        
        switch ( mode ) {
            case LoggerWriter.SYS:
                logId = "sys";
                break;
            case LoggerWriter.ERR:
                logId = "err";
                break;
            case LoggerWriter.WARN:
                logId = "warn";
                break;
            case LoggerWriter.INFO:
                logId = "info";
                break;
            case LoggerWriter.DEBUG:
                logId = "debug";
                break;
            case LoggerWriter.USER:
                logId = "user";
                break;    
            case LoggerWriter.SQL:
                logId = "sql";
                break;    
            case LoggerWriter.PACKET:
                logId = "packet";
                break;    
                
            case LoggerWriter.MONITOR:
                logId = "monitor";
                break; 
            default:
                logId = "";
                break;
        }
        return logId;
    }
    
    static String getLogMark(int mode) 
    {
        String mark = null;
        
        switch ( mode ) {
            case LoggerWriter.SYS:
                mark = LoggerWriter.MARK_SYS;
                break;
            case LoggerWriter.ERR:
                mark = LoggerWriter.MARK_ERR;
                break;
            case LoggerWriter.WARN:
                mark = LoggerWriter.MARK_WARN;
                break;
            case LoggerWriter.INFO:
                mark = LoggerWriter.MARK_INFO;
                break;
            case LoggerWriter.DEBUG:
                mark = LoggerWriter.MARK_DEBUG;
                break;
            case LoggerWriter.USER:
                mark = LoggerWriter.MARK_USER;
                break;    
            case LoggerWriter.SQL:
                mark = LoggerWriter.MARK_SQL;
                break;    
            case LoggerWriter.PACKET:
                mark = LoggerWriter.MARK_PACKET;
                break;    
            case LoggerWriter.MONITOR:
                mark = LoggerWriter.MARK_MONITOR;
                break;    
            default:
                mark = "";
                break;
        }
        return mark;
    }
            
        
   
    




    private static LoggerWriter getLoggerWriter(int serverty) 
    {
        LoggerWriter  logger = null;
        
        try
        {
            String logId = getLogId(serverty);
            String logMark = getLogMark(serverty);
            
            logger = LoggerFactory.getLoggerWriter(logId, logMark);
            
            
        }
        catch(Exception e)
        {
            System.err.println(LOG_ID+"getLoggerWriter err. serverty:"+serverty+" "+e.getMessage());
            e.printStackTrace();
        }
        
        return logger;
            
            

    }


/*
    public final static UserLoggerWriter reg = getUserLoggerWriter(UserLoggerWriter.REG);
    public final static UserLoggerWriter unreg = getUserLoggerWriter(UserLoggerWriter.UNREG);

    private static String userId;

    private static UserLoggerWriter getUserLoggerWriter(int serverty) {
        UserLoggerWriter  userLogger = null;
        try{

			//System.out.println("userId ["+userId+"] in getUserLoggerWriter");
            if ( serverty == UserLoggerWriter.REG && userId != null ) 
                userLogger = new UserLoggerWriter(serverty, userId);
            else
                userLogger = new UserLoggerWriter(UserLoggerWriter.UNREG);
        }
        catch(Exception e) {
            if ( serverty == LoggerWriter.SYS ) {
                userLogger.println("LoggerWriter initialization fail : " + e.getMessage());
                userLogger.println("LoggerWriter is reinitialized with DefaultLoggerWriter");
                userLogger.flush();
            }
        }
        return userLogger;
    }
    
    public static void setUserId(String id) {
        if ( id == null ) return;
        userId = id;
        reg.userId = id ;
        reg.mode = UserLoggerWriter.REG;
    }
*/
}