/**
 * @(#) CharConversion.java
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team, 
 * Application Infrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 * 
 * NOTICE !      You can copy or redistribute this code freely, 
 * but you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author  Soo-Kyung Lim, sukyunglim@lgeds.lg.co.kr.
 */

package jdf.framework.core.util;

import java.io.UnsupportedEncodingException;

/**
 * <b><code>CharConversion</code></b>
 * <p>
 * 문자열을 KSC5601 또는 8859_1 방식으로 encoding해준다.
 * 
 * </p>
 * 
 * @author Soo-Kyung Lim, sukyunglim@lgeds.lg.co.kr.
 * @version 1.0
 */

public final class CharConversion {

	/**
	 * Don't let anyone instantiate this class
	 */
	private CharConversion() {
	}

	/**
	 * 8859_1 --> KSC5601.
	 */
	public static String K2E(String korean) {
		String english = null;

		if (korean == null)
			return null;
		// if (korean == null ) return "";

		// english = new String(korean);
		try {
			english = new String(korean.getBytes("KSC5601"), "8859_1");
		} catch (UnsupportedEncodingException e) {
			english = korean;
		}
		return english;
	}

	/**
	 * KSC5601 --> 8859_1.
	 */
	public static String E2K(String english) {
		String korean = null;

		if (english == null)
			return null;
		// if (english == null ) return "";

		try {
			korean = new String(english.getBytes("8859_1"), "KSC5601");
		} catch (UnsupportedEncodingException e) {
			korean = english;
		}
		return korean;
	}

}