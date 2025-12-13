/**
 * @(#) GeneralMessage.java
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
 
 
import jdf.framework.core.util.*;

/**
 * <b><code>GeneralMessageStore</code></b>
 * <p>
 * <code>Message</code> Interface의 기본기능을 구현한 추상 Class.
 * </p>
 *
 * @author  WonYoung Lee, wyounglee@lgeds.lg.co.kr.
 * @version 1.0
 */
 
public abstract class GeneralMessageStore implements Message {
    protected static java.util.Properties messages = null;

    private String code = null;
    private String message = null;
    private java.util.Hashtable args = null;

    public GeneralMessageStore(){
        args = new java.util.Hashtable();
        initialize();
    }

    /**
     * @param code java.lang.String
     */
    public GeneralMessageStore(String code) {
        this();
        setCode(code);
    }

    /**
     * Returns the messge key string.
     * @return The messge key string.
     */
    public String getCode() {
        return code;
    }

    /**
     * You have to implements this method in the subclass
     */
    protected abstract void initialize() ;

    /**
     * @return java.lang.String
     * @param s java.lang.String
     */
    private String parseMessage(String s) {
        if ( s == null ) return "";
        //if ( s == null ) return null;
        
        StringBuffer content = new StringBuffer();
        while( s.length() > 0 ) {
            int position = s.indexOf("<@");
            if ( position == -1 ) {
                content.append(s);
                break;
            }
            if ( position != 0 ) content.append(s.substring(0,position));

            if ( s.length() == position + 2 ) break;
            String remainder = s.substring(position+2);
            
            int markEndPos = remainder.indexOf(">");
            if ( markEndPos == -1 ) break;
            
            String argname = remainder.substring(0, markEndPos).trim();
            String value = (String)args.get(argname);
            if ( value != null ) content.append(value);
            
            if ( remainder.length() == markEndPos + 1 ) break;
            s = remainder.substring(markEndPos + 1);
        }
        return content.toString();
    }

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
    public void setArg(String key, String value) {
        args.put(key, value);
    }

    /**
     * Set the message code key.
     * @param code the code string which defined in the message definition file.
     */
    public void setCode(String code) {
        this.code = code;
        message = CharConversion.E2K((String)messages.get(code));
    }

    /**
     * Returns the translated actual string.
     * @return a string representation of the receiver
     */
    public String toString() {
        return parseMessage(message);
    }
}