/**
 * @(#) Message.java
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

package jdf.framework.core;

/**
 * <b><code>MessageBox</code></b>
 * <p>
 * CODE -> 메세지로 변환하기 위한 Class
 * </p>
 * 
 * 
 * @author
 * @version 1.0
 */
public interface Message
{

    /**
     * Returns the messge key string.
     * @return The messge key string.
     */
    //String getCode();
    public String getMessage(String grpCode, String code);
    
    public String getMessage(String lang, String grpCode, String code);

    /**
     * Set the key=value pairs.
     * 
     * <ul>
     * <li>key : message key string defined in the message definition file.
     * <li>value : the actual string that you want to be replaced.
     * </ul>
     * @param key the string of key defined in the message definition file.
     * @param value the actual string which you want to be replaced.
     */
    //public void setArg(String key, String value) ;

    /**
     * Set the message code key.
     * @param code the code string which defined in the message definition file.
     */
    //void setCode(String code);

    /**
     * Returns the translated actual string.
     * @return a string representation of the receiver
     */
    public String toString();

    public java.util.List getList(String grpCode);

}
