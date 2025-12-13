/**
 * 
 */
package com.kabm.batch;

import com.kabm.util.InteractionUtil;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.log.Logger;

/**
 * @author trustsoo
 *
 */
public class StatBatch extends InteractionUtil{
		
	private final static String CLSS_NM = "[StatBatch]";
	private String work_date = "";
	private String yyyy = "";
	private String mm = "";
	private String dd = "";
	
	public StatBatch(String wdate)
	{
		work_date = wdate;
		if( work_date != null && work_date.length() == 8 )
		{
			yyyy = work_date.substring(0, 4);
			mm = work_date.substring(4,6);
			dd = work_date.substring(6,8);
		}
	}
	
	public void excuteStatView() throws ResourceException, Exception
	{
		DataSet input = new DataSet();
		try{
			Logger.info.println(CLSS_NM+" excuteStatView Start![" + work_date + "]"  );
			input.put("work_date", work_date);
			input.put("yyyy", yyyy);
			input.put("mm", mm);
			input.put("dd", dd);
			
			input.put("cmd", "excuteStatView");
			setData("/batch/StatBatch", input);
			
			Logger.info.println(CLSS_NM+" excuteStatView end![" + work_date + "]"  );
			
		}catch(Exception e){
			Logger.warn.println(CLSS_NM+e.toString());
		}
		
	}
	
	public void excuteStatMenu() throws ResourceException, Exception
	{
		DataSet input = new DataSet();
		try{
			Logger.info.println(CLSS_NM+" excuteStatMenu Start![" + work_date + "]"  );
			input.put("work_date", work_date);
			input.put("yyyy", yyyy);
			input.put("mm", mm);
			input.put("dd", dd);
			
			input.put("cmd", "excuteStatMenu");
			setData("/batch/StatBatch", input);
			
			Logger.info.println(CLSS_NM+" excuteStatMenu end![" + work_date + "]"  );
			
		}catch(Exception e){
			Logger.warn.println(CLSS_NM+e.toString());
		}
	}
	
	public void excuteStatPotal() throws ResourceException, Exception
	{
		DataSet input = new DataSet();
		try{
			Logger.info.println(CLSS_NM+" excuteStatPotal Start![" + work_date + "]"  );
			input.put("work_date", work_date);
			input.put("yyyy", yyyy);
			input.put("mm", mm);
			input.put("dd", dd);
			
			input.put("cmd", "excuteStatPotal");
			setData("/batch/StatBatch", input);
			
			Logger.info.println(CLSS_NM+" excuteStatPotal end![" + work_date + "]"  );
		}catch(Exception e){
			Logger.warn.println(CLSS_NM+e.toString());
		}
	}
	
	public void excuteStatDel() throws ResourceException, Exception
	{
		
		DataSet input = new DataSet();
		try{
			Logger.info.println(CLSS_NM+" excuteStatDel Start![" + work_date + "]"  );
			input.put("work_date", work_date);
			input.put("yyyy", yyyy);
			input.put("mm", mm);
			input.put("dd", dd);
			
			input.put("cmd", "excuteStatDel");
			setData("/batch/StatBatch", input);
			
			Logger.info.println(CLSS_NM+" excuteStatDel end![" + work_date + "]"  );
		}catch(Exception e){
			Logger.warn.println(CLSS_NM+e.toString());
		}
	}
	
}
