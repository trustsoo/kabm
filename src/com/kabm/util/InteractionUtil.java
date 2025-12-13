/**
 * 
 */
package com.kabm.util;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.data.ResourceException;

/**
 * @author trustsoo
 *
 */
public class InteractionUtil{ 
	
		
	public static void setData(String trName, DataSet input) throws ResourceException
	{		
			InteractionBean interact = new InteractionBean();
			interact.execute(trName, input);
	}
	
	public static DataSet getData(String trName, DataSet input) throws ResourceException
	{		
			InteractionBean interact = new InteractionBean();
			DataSet output = new DataSet();
			output = interact.execute(trName, input);
			return output;
	}
	
}