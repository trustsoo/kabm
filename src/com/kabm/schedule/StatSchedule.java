package com.kabm.schedule;

import com.kabm.batch.StatBatch;

import jdf.framework.core.Config;
import jdf.framework.core.Configurable;
import jdf.framework.core.log.Logger;

public class StatSchedule  implements Runnable, Configurable
{
    private final String LOG_ID = "<at:StatSchedule> ";
    private Config conf;
    
    public static void main(String[] args)
    {
        new StatSchedule().run();
    }
    
    public void run() 
    {
        Logger.info.println(LOG_ID+"*********************************start*********************************");
        
        try {
        	String work_date = jdf.framework.core.util.DateTime.getDatePrevDD(jdf.framework.core.util.DateTime.getShortDateString());
        	StatBatch stat = new StatBatch(work_date);
        	stat.excuteStatView();
        	stat.excuteStatMenu();
        	stat.excuteStatPotal();
        	stat.excuteStatDel();
        	
        }catch(Exception e){
			Logger.warn.println(LOG_ID+e.toString());
		}
        Logger.info.println(LOG_ID+"********************************* end *********************************");
    }

    
    public Config getConfigInfo() {
        // TODO Auto-generated method stub
        return conf;
    }

    
    public void setConfigInfo(Config info) {
        // TODO Auto-generated method stub
        conf = info;
    }
    
}