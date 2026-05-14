package com.kabm.util;

/******************************************************************************** 
 * Program ID	: SitePropertyManager
 * FileName		: SitePropertyManager.java
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

public class SitePropertyManager
{
	private static final String LOG_ID="<at:SitePropertyManager> ";
	
	private static DataSet props = null;
	
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
			props = new DataSet();
			for(int idx=0; idx<output.getMaxDataSize(); idx++)
			{
				String key = output.getText("key", idx);
				String val = output.getText("val", idx);
				props.put(key, val);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static String getString(String key)
	{
		if(  props == null )
		{
			reflesh();
		}
		return props.getText(key);
	}
	
	public static int getInt(String key)
	{
		if( props == null )
		{
			reflesh();
		}		
		return props.getInt(key);
	}
	
	public static boolean getBoolean(String key , boolean initVal)
	{
		if( props == null )
		{
			reflesh();
		}
		boolean result = initVal;
		try
		{
			result = Boolean.parseBoolean(props.getText(key));
		}catch(Exception e){}
		return result;
	}
}