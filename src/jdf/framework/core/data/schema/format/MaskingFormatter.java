package jdf.framework.core.data.schema.format;

import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;




public class MaskingFormatter extends Formatter
{
	
	private final static String LOG_ID = "<f:MaskingFormatter> ";
	
	private String maskRangeStr =  null;
	
	
	/*
	 *  mask:-0~-4
	 * mask:0~4
	 *	mask:3,4/*
	 *	mask:-4
	 * 
	 * */
	public MaskingFormatter(String format) throws IllegalArgumentException
	{	
		try
		{
		
			maskRangeStr = SmartStringArray.split(":", format)[1];
			
			
		} catch(ArrayIndexOutOfBoundsException ae)
		{
			throw new IllegalArgumentException("parsing error. usage:mask:0~4.("+format+")"+ae.getMessage());
		}catch(Exception ex)
		{
			throw new IllegalArgumentException("parsing error."+ex.getMessage());
		}
		
	}
	
	
	public String format(Object data) throws IllegalArgumentException
	{	
		String a = data.toString();
		
		if(a == null || a.length() <= 0) return a;
		
		
		if("email".equals(maskRangeStr)) return emailFormat(data);
		else if("tel".equals(maskRangeStr)) return telFormat(data);
		
		
		String[] temp = SmartStringArray.split(",", maskRangeStr);
		StringBuffer sb = null;
		
		try
		{
			for(int idx=0; idx<temp.length; idx++)
			{
				String[] ranges = SmartStringArray.split("~", temp[idx]);
				
				sb = new StringBuffer(a);
				
				if(ranges.length == 1)
				{
					
					if(ranges[0].equals("*"))
					{
						for(int kdx=0; kdx<sb.length(); kdx++)
						{
							sb.setCharAt(kdx, '*');
						}
					} else
					{				
						int position = Integer.parseInt(ranges[0]);
						
						if(Math.abs(position) > a.length()) return a;
										
						if(position < 0) sb.reverse();				
						
						sb.setCharAt(Math.abs(position), '*');
						
						if(position < 0) sb.reverse();
						
					}
					
					a = sb.toString();
					
				} else if(ranges.length == 2)
				{
					int position_1 = Integer.parseInt(ranges[0]);
					int position_2 = Integer.parseInt(ranges[1]);
					
					
					if(Math.abs(position_1) > a.length() || Math.abs(position_2) > a.length()) return a;
					
					
					if(Math.abs(position_1) > Math.abs(position_2))
						throw new IllegalArgumentException("parsing error. invalid masking range. 범위 수치가 잘못되었습니다. 시작 범위의 절대값이 종료 범위의 절대값 보다 클수 없습니다.");
					
					
					if(position_2 < 0) sb.reverse();		
					
					for(int kdx=Math.abs(position_1); kdx<=Math.abs(position_2); kdx++)
					{
						sb.setCharAt(kdx, '*');
					}
					
					if(position_2 < 0) sb.reverse();		
					
					a = sb.toString();
					
				} else
				{
					throw new IllegalArgumentException("parsing error. invalid masking range. ex)mask:1~2 or mask:4~5 mask:1 or mask:1,4");
				}
			
			}
		} catch(Exception ex)
		{
			Logger.warn.println(LOG_ID+ex.getMessage());
			return a;
		}
		
		return sb.toString();
		
	}
	
	private String telFormat(Object data) throws IllegalArgumentException
	{	
		String a = data.toString();
		String[] temp = SmartStringArray.split("-", a);
		if(temp.length != 3) return a;
		
		int maskingLen = temp[1].length();
		
		StringBuffer result = new StringBuffer();
		
		result.append(temp[0]).append("-");
		for(int idx = 0; idx<maskingLen; idx++)
		{
			result.append("*");
		}
		result.append("-")
			  .append(temp[2]);
		
		return result.toString();
		
	}
	
	private String emailFormat(Object data) throws IllegalArgumentException
	{	
		String a = data.toString();
		String[] temp = SmartStringArray.split("@", a);
		
		
		if(temp.length < 2) return a;
		
		
		
		int position_1 = 0;
		int position_2 = 1;
		
		if(position_2 >= temp[0].length()) return a;
		
		
		StringBuffer sb = new StringBuffer(temp[0]);
		sb.reverse();	
		
		
		for(int kdx=position_1; kdx<=position_2; kdx++)
		{
			sb.setCharAt(kdx, '*');
		}
		
		
		sb.reverse();		
		
		String b = sb.toString();
		
		
		StringBuffer result = new StringBuffer();
		result.append(b).append("@").append(temp[1]);
		
		return result.toString();
		
		
	}
	
}