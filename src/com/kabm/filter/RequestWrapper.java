package com.kabm.filter;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import jdf.framework.core.util.StringFormater;

public class RequestWrapper extends HttpServletRequestWrapper 
{
    public RequestWrapper(HttpServletRequest servletRequest) {   
        super(servletRequest);   
    }   
       
    public String[] getParameterValues(String parameter) {   
  
      String[] values = super.getParameterValues(parameter);   
      if (values==null) return null;   
          
      int count = values.length;   
      String[] encodedValues = new String[count];   
      for (int i = 0; i < count; i++) 
      {   
    	  encodedValues[i] = filter(values[i]);   
      }     
      return encodedValues;    
    }   
       
    public String getParameter(String parameter) 
    {   
    	  String rtnValue = null;
          String value = super.getParameter(parameter);
          
          if (value == null) 
          {   
        	  return null;    
          }   
          rtnValue = filter(value);
          return rtnValue;   
    }   
       
    public String getHeader(String name) {   
        String value = super.getHeader(name);   
        if (value == null)   	
            return null;   
        return filter(value);   
           
    }   
  
    //http://www.nationalfinder.com/html/char-asc.htm 참고
    private String filter(String input) {
        if(input==null) {
            return null;
        }
        String clean = StringFormater.contentFilter(input);
        //String clean = new HTMLInputFilter().filter(input.replaceAll("\"", "%22").replaceAll("\'","%27").replaceAll("--", "%2D%2D").replaceAll(";", "%3B").replaceAll("\\+", ""));
        //String clean = input.replaceAll("\"", "%22").replaceAll("\'","%27").replaceAll("--", "%2D%2D").replaceAll(";", "%3B").replaceAll("\\+", "");
        //return clean.replaceAll("<", "%3C").replaceAll(">", "%3E");   
        //.replaceAll(";", "&#59;")
        /*String clean = input.replaceAll("\"", "&quot;").replaceAll("\'","&#039;").replaceAll("\\+", "&#043;").replaceAll("[\uFEFF-\uFFFF]","").replaceAll("\u200B", "");
        clean = clean.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        clean = clean.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        clean = clean.replaceAll("eval\\((.*)\\)", "");
        clean = clean.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        clean = clean.replaceAll("script", "_script_");
        clean = clean.replaceAll("document", "_document_");
        clean = clean.replaceAll("forms", "_forms_");
        clean = clean.replaceAll("cookie", "_cookie_");
        //clean = clean.replaceAll("body", "_body_");
        clean = clean.replaceAll("[\\\"\\\'][\\s]*JAVASCRIPT:(.*)[\\\"\\\']", "\"\"");
        clean = clean.replaceAll("SCRIPT", "_SCRIPT_");
        clean = clean.replaceAll("DOCUMENT", "_DOCUMENT_");
		clean = clean.replaceAll("FORMS", "_FORMS_");
        clean = clean.replaceAll("COOKIE", "_COOKIE_");*/
        //clean = clean.replaceAll("BODY", "_BODY_");
        return clean;
    }
}