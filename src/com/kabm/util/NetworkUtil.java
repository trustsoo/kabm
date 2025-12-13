package com.kabm.util;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import jdf.framework.core.util.Utility;

public class NetworkUtil {

	public static String getHostAddress() 
	{
		try {
			NetworkInterface netface = null;
			Enumeration e = NetworkInterface.getNetworkInterfaces();
		
		    while(e.hasMoreElements()) 
		    {
		    	netface = (NetworkInterface)e.nextElement();
				Enumeration e2 = netface.getInetAddresses();
			    	
	    		while(e2.hasMoreElements()) 
	    		{
	    			InetAddress ip 	= (InetAddress)e2.nextElement();
	    			
	    			if ( !ip.isLoopbackAddress() && !ip.isLinkLocalAddress())
	    			{	    				
	    				return ip.getLocalHost().getHostAddress();
	    			}
	    			
	    		}
		    }
		} catch (IOException e) 
		{
			//System.out.println("NetworkUtil.getHostAddress() error:"+Utility.getStackTrace(e));
			return "127.0.0.1";
		}
		
		return null;
	}
	
	public static String getIPByURL(String url) 
	{
		String ipaddr = null;
		try{
			InetAddress ip = InetAddress.getByName(url);
			ipaddr = ip.getHostAddress();
		} catch (IOException e) 
		{
			//System.out.println("NetworkUtil.getHostAddress() error:"+Utility.getStackTrace(e));
			return null;
		}

		return ipaddr;
	}
	
	public static String getRemoteAddr(HttpServletRequest request)
	{
		String temp = request.getHeader("X-Forwarded-For");
		if(temp != null && !"".equals(temp)) return temp;
		
		return request.getRemoteAddr();
	}
	
	public static String getRemotePort(HttpServletRequest request)
	{
		String temp = request.getHeader("X-Forwarded-For");
		if(temp != null && !"".equals(temp)) return temp;
		
		return request.getRemotePort()+"";
	}
	
}
