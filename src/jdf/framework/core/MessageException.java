package jdf.framework.core;

/**
 * 
 * @(#) MessageException.java
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team, 
 * Application Intrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 *  
 * NOTICE !      You can copy or redistribute this code freely, 
 * but you should not remove the information about the copyright notice 
 * and the author.
 *  
 * @author  WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 */
public class MessageException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2075855644204313129L;

	public MessageException() {
        super();
    }

    /**
     * @param s java.lang.String
     */
    public MessageException(String s) {
        super(s);
    }
}