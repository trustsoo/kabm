package jdf.framework.core.db;



/**
 * @(#) NoAffectedException.java
 * Copyright 1999-2000 by  Power User's Group. All rights reserved.
 * Created by Lee WonYoung ( wyounglee@lgeds.lg.co.kr, 019-310-7324 )
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the programmer.
 */

public class NoAffectedException extends BusinessException {

	/**
	 * 
	 */
	public NoAffectedException() {
		super("no affected exception");
	}

	/**
	 * @param s java.lang.String
	 */
	public NoAffectedException(String s) {
		super(s);
	}

}
