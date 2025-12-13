package com.kabm.util;

import java.util.Random;

public class PasswordUtil
{
	public static void main(String args[]) {
		PasswordUtil pu = new PasswordUtil();
		System.out.println(pu.getPassword());
	}
	public static String getPassword()
	{
		StringBuffer buffer = new StringBuffer();
		Random random = new Random();
				
		String chars[] = "0,1,2,3,4,5,6,7,8,9".split(",");
				
		for (int i = 0; i < 6; i++) {
		buffer.append(chars[random.nextInt(chars.length)]);
		}
		return buffer.toString();
	}
	
}