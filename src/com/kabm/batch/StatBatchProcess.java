/**
 * 
 */
package com.kabm.batch;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.log.Logger;


/**
 * @author trustsoo
 *
 */
public class StatBatchProcess{
	private final static String CLSS_NM = "[StatBatchProcess]";
	public static void main(String args[] )
	{		
		
		try
		{
			if( args.length < 1 )
			{
				Logger.err.println("Usage : StatBatchProcess [div] : div = view , menu , potal, del");
				return;
			}
			String div = args[0];
			String work_date = jdf.framework.core.util.DateTime.getShortDateString();
			if( args.length == 2 )
			{
				work_date = args[1];
			}
			StatBatch stat = new StatBatch(work_date);
			
			if( div != null && "view".equals( div ) )
			{
				stat.excuteStatView();
			}else if( div != null && "menu".equals( div ) ){
				stat.excuteStatMenu();
			}else if( div != null && "potal".equals( div ) ){
				stat.excuteStatPotal();
			}else if( div != null && "del".equals( div ) ){
				stat.excuteStatDel();
			}
		}catch(ResourceException re){
			Logger.warn.println(CLSS_NM+re.toString());
		}catch(Exception e){
			Logger.warn.println(CLSS_NM+e.toString());
		}
		
		//System.exit(0);
	}
	
}
