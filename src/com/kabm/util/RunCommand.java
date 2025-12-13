package com.kabm.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jdf.framework.core.log.Logger;

public class RunCommand 
{
	private static final String LOG_ID="<at:RunCommand> ";
	
	public static boolean runChmod(String filename) throws Exception
	{
		
		List<String> command = null;
		
			try
			{
				command = new ArrayList<String>();					
				command.add("chmod");
				command.add("-f");
				command.add("755");
				command.add(filename);
				runCommand(((String[])command.toArray(new String[1])));
				
			} catch(Exception ex)
			{
				Logger.warn.println(LOG_ID+ex.getMessage());
			}
		
		return true;
	}
	
	
	
	private static void runCommand(String[] command) throws Exception
	{
		Runtime rt = null;
		Process proc = null;
		InputStreamReader is = null;
		BufferedReader br = null;
		
		try
		{
			rt = Runtime.getRuntime();
			proc = rt.exec(command);			
			br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line = null;
			Logger.debug.println(LOG_ID+"<INFO>");
			StringBuffer sb = new StringBuffer();
			while ( (line = br.readLine()) != null)
			{
				sb.append(line);
			}
			Logger.debug.println(LOG_ID+sb.toString());
			Logger.debug.println(LOG_ID+"</INFO>");
			int exitVal = proc.waitFor();
		} catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		} finally
		{
			if(br != null) try{br.close();}catch(Exception ex){}
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
		RunCommand.runChmod("D:\\workspace\\kftc\\WebContent\\test\\test.html");
	}
	
	
}
