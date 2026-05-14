package jdf.framework.core.util;

/******************************************************************************** 
 * Program ID	: PropertyManager
 * FileName		: PropertyManager.java
 * @author		: 조은호
 * @version		: 1.0
 * Comment		: Site에서 사용하는 각종 상수를 정의한다.
 * 				 (WEB-INF/config/site.properties)
 *
 * Modified
 * No       Date       Author	      Comment
 * ---   ----------   ----------   -----------------------------------------------
 * 01     2009-05-03   조은호       최초작성
 ********************************************************************************/


import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.log.Logger;

import java.util.HashMap;
import java.util.Map;

public class PropertyManager
{
	public static final String CMPNY_NO = "-1";
	private static final String LOG_ID="<at:PropertyManager> ";
	
	private static Map<String, DataSet> props = new HashMap<String, DataSet>();
	
	static InteractionBean interact = new InteractionBean();
	
	static{
		reflesh();
	}
	
	public static void reflesh()
	{
		DataSet input = new DataSet();
		DataSet output = new DataSet();
		
		try
		{
			input.put("cmd", "GET_CONF_LIST");
			output = interact.execute("/common/CompanyConf", input);
			for( int idx =0; idx < output.getCount("cmpny_no"); idx++)
			{
				reflesh(CMPNY_NO);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void reflesh(String cmpny_no)
	{
		DataSet input = new DataSet();
		DataSet output = new DataSet();
		
		try
		{
			if(cmpny_no == null ) cmpny_no = CMPNY_NO;
			input.put("cmd", "GET_CONF_LIST");
			input.put("cmpny_no", cmpny_no);
			output = interact.execute("/common/CompanyConf", input);
			
			DataSet rslt = new DataSet();
			String old_cmpny_no = null;
			for(int idx=0; idx<output.getMaxDataSize(); idx++)
			{
				String key = output.getText("key", idx);
				String val = output.getText("val", idx);
				rslt.put(key, val);
			}
			
			
			props.put(cmpny_no, rslt);

			Logger.debug.println(LOG_ID+cmpny_no+"'s porperty ==>"+rslt);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getString(String key)
	{
		return PropertyManager.getString(CMPNY_NO , key);
	}
	public static String getString(String cmpny_no, String key)
	{
		DataSet ds = new DataSet();
		if( !props.containsKey(cmpny_no) || ds == null )
		{
			reflesh(cmpny_no);
		}
		ds = (DataSet)props.get(cmpny_no);
		
		return ds.getText(key);
	}
	
	public static int getInt(String key)
	{
		return PropertyManager.getInt(CMPNY_NO , key);
	}
	public static int getInt(String cmpny_no, String key)
	{
		DataSet ds = new DataSet();		
		if( !props.containsKey(cmpny_no) || ds == null )
		{
			reflesh(cmpny_no);
		}		
		ds = (DataSet)props.get(cmpny_no);
		
		return ds.getInt(key);
	}
}