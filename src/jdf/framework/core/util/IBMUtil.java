package jdf.framework.core.util;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;

public class IBMUtil {
	
	public static native int getpid();

	static {
		String module = getLibPath();
		System.out.println("module path : " + module);
		if(module != "NOT")
			System.load(module.trim());
	}

	public static String getLibPath() {
		try {
			Config conf = Configuration.lookup("/ibm");
			
			String path = conf.getString("path");
			if(path == null) return "NOT";
			
			String module = conf.getString("module");
			
			return path + "/" +  module; 
		}
		catch(Exception e) {
			
		}
		
		return null;
	}

	public static void main(String[] args) {
		System.out.println("PID = " + IBMUtil.getpid());
	}
}
