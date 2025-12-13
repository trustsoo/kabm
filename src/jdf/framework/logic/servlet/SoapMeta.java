package jdf.framework.logic.servlet;

public interface SoapMeta
{
    public static String SOAP_PREFIX = "soap";

    // MS 와 apache 에서 지원하는 SOAP Namespace 가 서로 틀리다.
    public static String SOAP_NS = "http://schemas.xmlsoap.org/soap/envelope/";

    // private static String SOAP_NS =
    // "http://www.w3.org/2001/12/soap-envelope\";
    
    public static String SOAP_ROOT_S_PREFIX = "<" + SOAP_PREFIX + ":Envelope xmlns:" + SOAP_PREFIX + "=\""
    + SOAP_NS + "\" soap:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\"";
    
    
    public static String SOAP_ROOT_S = "<" + SOAP_PREFIX + ":Envelope xmlns:" + SOAP_PREFIX + "=\""
            + SOAP_NS + "\" soap:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\" >";

    public static String SOAP_ROOT_E = "</" + SOAP_PREFIX + ":Envelope>";

    public static String SOAP_HEADER_S = "<" + SOAP_PREFIX + ":Header>";

    public static String SOAP_HEADER_E = "</" + SOAP_PREFIX + ":Header>";

    public static String SOAP_BODY_S = "<" + SOAP_PREFIX + ":Body>";

    public static String SOAP_BODY_E = "</" + SOAP_PREFIX + ":Body>";

    public static String SOAP_FAULT_S = "<" + SOAP_PREFIX + ":Fault>";

    public static String SOAP_FAULT_E = "</" + SOAP_PREFIX + ":Fault>";

    public static String SOAP_FAULT_STR_S = "<" + SOAP_PREFIX + ":faultstring>";

    public static String SOAP_FAULT_STR_E = "</" + SOAP_PREFIX + ":faultstring>";

    public static String SOAP_DETAIL_STR_S = "<" + SOAP_PREFIX + ":detail>";

    public static String SOAP_DETAIL_STR_E = "</" + SOAP_PREFIX + ":detail>";
}
