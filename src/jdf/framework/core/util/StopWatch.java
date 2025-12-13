package jdf.framework.core.util;

import jdf.framework.core.log.Logger;

/**
 * 
 * @(#) StopWatch.java Copyright 1999-2000 by LG-EDS Systems, Inc., Information
 *      Technology Group, Application Architecture Team, Application
 *      Intrastructure Part. 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042,
 *      KOREA. All rights reserved.
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 */
public final class StopWatch
{
    private String desc="";
    
    private long start = 0;

    /**
     * StopWatch constructor comment.
     */
    public StopWatch()
    {
        reset();
    }
    
    public StopWatch(String desc)
    {
        this.desc=desc;
        Logger.debug.println(this.desc + " CHK START");
        /*if (Logger.user.isPrintMode())
            Logger.user.println(this.desc + " CHK START");*/
        reset();
    }


    /**
     * c'tor에서 호출되며, 시작 시간을 설정한다.
     */
    private void reset()
    {
        start = System.currentTimeMillis();
    }

    /**
     * 현재 시간과 시작 시간의 차이를 리턴한다.
     * 
     * @return long
     */
    public long getEllapsed()
    {
        long now = System.currentTimeMillis();
        return (now - start);
    }

    public double getEllapseds()
    {
        long now = System.currentTimeMillis();
        return (double) (now - start) / 1000;
    }

    
    
    public void printLog()
    {
    	Logger.debug.println(this.desc + " PTIME:" + getEllapseds());
        //if (Logger.user.isPrintMode())
            //Logger.user.println(this.desc + " PTIME:" + getEllapsed());

    }
}