package com.kabm.util;

import java.util.Hashtable;


public class ResResultConstant {

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// String Code List
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Informational 1xx
	public final static String RESULT_CONTINUE                        = "100"; // Section 10.1.1: Continue
    public final static String RESULT_SWITCHING_PROTOCOL              = "101"; // Section 10.1.2: Switching Protocols

	//Successful 2xx
	public final static String RESULT_OK                              = "200"; // Section 10.2.1: OK
	public final static String RESULT_CREATE                          = "201"; // Section 10.2.2: Created
	public final static String RESULT_ACCEPTED                        = "202"; // Section 10.2.3: Accepted
	public final static String RESULT_NON_AUTHORITATIVE_INFOMATION    = "203"; // Section 10.2.4: Non-Authoritative Information
	public final static String RESULT_NO_CONTENT                      = "204"; // Section 10.2.5: No Content
	public final static String RESULT_RESET_CONTENT                   = "205"; // Section 10.2.6: Reset Content
	public final static String RESULT_PARTIAL_CONTENT                 = "206"; // Section 10.2.7: Partial Content

	//Redirection 3xx
	public final static String RESULT_MULTIPLE_CHOICES                = "300"; // Section 10.3.1: Multiple Choices
	public final static String RESULT_MOVED_PERMANENTLY               = "301"; // Section 10.3.2: Moved Permanently
	public final static String RESULT_FOUND                           = "302"; // Section 10.3.3: Found
	public final static String RESULT_SEE_OTHER                       = "303"; // Section 10.3.4: See Other
	public final static String RESULT_NOT_MODIFIED                    = "304"; // Section 10.3.5: Not Modified
	public final static String RESULT_USE_PROXY                       = "305"; // Section 10.3.6: Use Proxy
	public final static String RESULT_TEMPORARY_REDIRECT              = "307"; // Section 10.3.8: Temporary Redirect

	//Client Error 4xx
	public final static String RESULT_BAD_REQUEST                     = "400"; // Section 10.4.1: Bad Request
	public final static String RESULT_UNAUTHORIZED                    = "401"; // Section 10.4.2: Unauthorized
	public final static String RESULT_PAYMENT_REQUIRED                = "402"; // Section 10.4.3: Payment Required
	public final static String RESULT_FORBIDDEN                       = "403"; // Section 10.4.4: Forbidden
	public final static String RESULT_NOT_FOUND                       = "404"; // Section 10.4.5: Not Found
	public final static String RESULT_METHOD_NOT_ALLOWED              = "405"; // Section 10.4.6: Method Not Allowed
	public final static String RESULT_NOT_ACCEPTABLE                  = "406"; // Section 10.4.7: Not Acceptable
	public final static String RESULT_AUTHENTICATION_REQUIRED         = "407"; // Section 10.4.8: Proxy Authentication Required
	public final static String RESULT_REQUEST_TIME_OUT                = "408"; // Section 10.4.9: Request Time-out
	public final static String RESULT_CONFLICT                        = "409"; // Section 10.4.10: Conflict
	public final static String RESULT_GONE                            = "410"; // Section 10.4.11: Gone
	public final static String RESULT_LENGTH_REQUIRED                 = "411"; // Section 10.4.12: Length Required
	public final static String RESULT_PRECONDITION_FAILED             = "412"; // Section 10.4.13: Precondition Failed
	public final static String RESULT_REQUEST_ENTITY_TOO_LARGE        = "413"; // Section 10.4.14: Request Entity Too Large
	public final static String RESULT_REQUEST_URL_TOO_LARGE           = "414"; // Section 10.4.15: Request-URI Too Large
	public final static String RESULT_UNSUPPORTED_MEDIA_TYPE          = "415"; // Section 10.4.16: Unsupported Media Type
	public final static String RESULT_REQUESTED_RANGE_NOT_SATISFIABLE = "416"; // Section 10.4.17: Requested range not satisfiable
	public final static String RESULT_EXPECTATION_FAILED              = "417"; // Section 10.4.18: Expectation Failed

	//Server Error 5xx
	public final static String RESULT_INTERNAL_SERVER_ERROR           = "500"; // Section 10.5.1: Internal Server Error
	public final static String RESULT_NOT_IMPLEMENTED                 = "501"; // Section 10.5.2: Not Implemented
	public final static String RESULT_BAD_GATEWAY                     = "502"; // Section 10.5.3: Bad Gateway
	public final static String RESULT_SERVICE_UNAVAILABLE             = "503"; // Section 10.5.4: Service Unavailable
	public final static String RESULT_GATEWAY_TIME_OUT                = "504"; // Section 10.5.5: Gateway Time-out
	public final static String RESULT_HTTP_VERSION_NOT_SUPPORTED      = "505"; // Section 10.5.6: HTTP Version not supported

	//Database Error 6xx, Ignore Success Message 
	public final static String RESULT_DATABASE_ERROR                  = "600"; // DB Document : Database Error
	public final static String RESULT_DATABASE_NOT_CONNECTED          = "601"; // DB Document : Database Not Connected
	public final static String RESULT_DATABASE_DATA_NOT_FOUND         = "602"; // DB Document : Database Data Not found
	public final static String RESULT_DATABASE_INVALID_STATEMENT      = "603"; // DB Document : Database Invalid statement
	public final static String RESULT_DATABASE_INVALID_CONDITION      = "604"; // DB Document : Database Invalid Condition
    public final static String RESULT_DATABASE_INVALID_TABLE_NAME     = "605"; // DB Document : Database Invalid Table Name
    public final static String RESULT_DATABASE_INVALID_COLUMN_NAME    = "606"; // DB Document : Database Invalid Column Name

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Integer Code List
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Informational 1xx
	public final static int INT_RESULT_CONTINUE                        = 100; // Section 10.1.1: Continue
    public final static int INT_RESULT_SWITCHING_PROTOCOL              = 101; // Section 10.1.2: Switching Protocols

	//Successful 2xx
	public final static int INT_RESULT_OK                              = 200; // Section 10.2.1: OK
	public final static int INT_RESULT_CREATE                          = 201; // Section 10.2.2: Created
	public final static int INT_RESULT_ACCEPTED                        = 202; // Section 10.2.3: Accepted
	public final static int INT_RESULT_NON_AUTHORITATIVE_INFOMATION    = 203; // Section 10.2.4: Non-Authoritative Information
	public final static int INT_RESULT_NO_CONTENT                      = 204; // Section 10.2.5: No Content
	public final static int INT_RESULT_RESET_CONTENT                   = 205; // Section 10.2.6: Reset Content
	public final static int INT_RESULT_PARTIAL_CONTENT                 = 206; // Section 10.2.7: Partial Content

	//Redirection 3xx
	public final static int INT_RESULT_MULTIPLE_CHOICES                = 300; // Section 10.3.1: Multiple Choices
	public final static int INT_RESULT_MOVED_PERMANENTLY               = 301; // Section 10.3.2: Moved Permanently
	public final static int INT_RESULT_FOUND                           = 302; // Section 10.3.3: Found
	public final static int INT_RESULT_SEE_OTHER                       = 303; // Section 10.3.4: See Other
	public final static int INT_RESULT_NOT_MODIFIED                    = 304; // Section 10.3.5: Not Modified
	public final static int INT_RESULT_USE_PROXY                       = 305; // Section 10.3.6: Use Proxy
	public final static int INT_RESULT_TEMPORARY_REDIRECT              = 307; // Section 10.3.8: Temporary Redirect

	//Client Error 4xx
	public final static int INT_RESULT_BAD_REQUEST                     = 400; // Section 10.4.1: Bad Request
	public final static int INT_RESULT_UNAUTHORIZED                    = 401; // Section 10.4.2: Unauthorized
	public final static int INT_RESULT_PAYMENT_REQUIRED                = 402; // Section 10.4.3: Payment Required
	public final static int INT_RESULT_FORBIDDEN                       = 403; // Section 10.4.4: Forbidden
	public final static int INT_RESULT_NOT_FOUND                       = 404; // Section 10.4.5: Not Found
	public final static int INT_RESULT_METHOD_NOT_ALLOWED              = 405; // Section 10.4.6: Method Not Allowed
	public final static int INT_RESULT_NOT_ACCEPTABLE                  = 406; // Section 10.4.7: Not Acceptable
	public final static int INT_RESULT_AUTHENTICATION_REQUIRED         = 407; // Section 10.4.8: Proxy Authentication Required
	public final static int INT_RESULT_REQUEST_TIME_OUT                = 408; // Section 10.4.9: Request Time-out
	public final static int INT_RESULT_CONFLICT                        = 409; // Section 10.4.10: Conflict
	public final static int INT_RESULT_GONE                            = 410; // Section 10.4.11: Gone
	public final static int INT_RESULT_LENGTH_REQUIRED                 = 411; // Section 10.4.12: Length Required
	public final static int INT_RESULT_PRECONDITION_FAILED             = 412; // Section 10.4.13: Precondition Failed
	public final static int INT_RESULT_REQUEST_ENTITY_TOO_LARGE        = 413; // Section 10.4.14: Request Entity Too Large
	public final static int INT_RESULT_REQUEST_URL_TOO_LARGE           = 414; // Section 10.4.15: Request-URI Too Large
	public final static int INT_RESULT_UNSUPPORTED_MEDIA_TYPE          = 415; // Section 10.4.16: Unsupported Media Type
	public final static int INT_RESULT_REQUESTED_RANGE_NOT_SATISFIABLE = 416; // Section 10.4.17: Requested range not satisfiable
	public final static int INT_RESULT_EXPECTATION_FAILED              = 417; // Section 10.4.18: Expectation Failed

	//Server Error 5xx
	public final static int INT_RESULT_INTERNAL_SERVER_ERROR           = 500; // Section 10.5.1: Internal Server Error
	public final static int INT_RESULT_NOT_IMPLEMENTED                 = 501; // Section 10.5.2: Not Implemented
	public final static int INT_RESULT_BAD_GATEWAY                     = 502; // Section 10.5.3: Bad Gateway
	public final static int INT_RESULT_SERVICE_UNAVAILABLE             = 503; // Section 10.5.4: Service Unavailable
	public final static int INT_RESULT_GATEWAY_TIME_OUT                = 504; // Section 10.5.5: Gateway Time-out
	public final static int INT_RESULT_HTTP_VERSION_NOT_SUPPORTED      = 505; // Section 10.5.6: HTTP Version not supported

	//Database Error 6xx, Ignore Success Message 
	public final static int INT_RESULT_DATABASE_ERROR                  = 600; // DB Document : Database Error
	public final static int INT_RESULT_DATABASE_NOT_CONNECTED          = 601; // DB Document : Database Not Connected
	public final static int INT_RESULT_DATABASE_DATA_NOT_FOUND         = 602; // DB Document : Database Data Not found
	public final static int INT_RESULT_DATABASE_INVALID_STATEMENT      = 603; // DB Document : Database Invalid statement
	public final static int INT_RESULT_DATABASE_INVALID_CONDITION      = 604; // DB Document : Database Invalid Condition
    public final static int INT_RESULT_DATABASE_INVALID_TABLE_NAME     = 605; // DB Document : Database Invalid Table Name
    public final static int INT_RESULT_DATABASE_INVALID_COLUMN_NAME    = 606; // DB Document : Database Invalid Column Name


	private static Hashtable resultMessageTable = null;

	private static void init() {
		
		if (resultMessageTable != null) return;
		
		resultMessageTable = new Hashtable();
		//Informational 1xx
		resultMessageTable.put(RESULT_CONTINUE                        , "Continue");
	    resultMessageTable.put(RESULT_SWITCHING_PROTOCOL              , "Switching Protocols");
	
		//Successful 2xx
		resultMessageTable.put(RESULT_OK                              , "OK");
		resultMessageTable.put(RESULT_CREATE                          , "Created");
		resultMessageTable.put(RESULT_ACCEPTED                        , "Accepted");
		resultMessageTable.put(RESULT_NON_AUTHORITATIVE_INFOMATION    , "Non-Authoritative Information");
		resultMessageTable.put(RESULT_NO_CONTENT                      , "No Content");
		resultMessageTable.put(RESULT_RESET_CONTENT                   , "Reset Content");
		resultMessageTable.put(RESULT_PARTIAL_CONTENT                 , "Partial Content");
	
		//Redirection 3xx
		resultMessageTable.put(RESULT_MULTIPLE_CHOICES                , "Multiple Choices");
		resultMessageTable.put(RESULT_MOVED_PERMANENTLY               , "Moved Permanently");
		resultMessageTable.put(RESULT_FOUND                           , "Found");
		resultMessageTable.put(RESULT_SEE_OTHER                       , "See Other");
		resultMessageTable.put(RESULT_NOT_MODIFIED                    , "Not Modified");
		resultMessageTable.put(RESULT_USE_PROXY                       , "Use Proxy");
		resultMessageTable.put(RESULT_TEMPORARY_REDIRECT              , "Temporary Redirect");
	
		//Client Error 4xx
		resultMessageTable.put(RESULT_BAD_REQUEST                     , "Bad Request");
		resultMessageTable.put(RESULT_UNAUTHORIZED                    , "Unauthorized");
		resultMessageTable.put(RESULT_PAYMENT_REQUIRED                , "Payment Required");
		resultMessageTable.put(RESULT_FORBIDDEN                       , "Forbidden");
		resultMessageTable.put(RESULT_NOT_FOUND                       , "Not Found");
		resultMessageTable.put(RESULT_METHOD_NOT_ALLOWED              , "Method Not Allowed");
		resultMessageTable.put(RESULT_NOT_ACCEPTABLE                  , "Not Acceptable");
		resultMessageTable.put(RESULT_AUTHENTICATION_REQUIRED         , "Proxy Authentication Requiredv");
		resultMessageTable.put(RESULT_REQUEST_TIME_OUT                , "Request Time-out");
		resultMessageTable.put(RESULT_CONFLICT                        , "Conflict");
		resultMessageTable.put(RESULT_GONE                            , "Gone");
		resultMessageTable.put(RESULT_LENGTH_REQUIRED                 , "Length Required");
		resultMessageTable.put(RESULT_PRECONDITION_FAILED             , "Precondition Failed");
		resultMessageTable.put(RESULT_REQUEST_ENTITY_TOO_LARGE        , "Request Entity Too Large");
		resultMessageTable.put(RESULT_REQUEST_URL_TOO_LARGE           , "Request-URI Too Large");
		resultMessageTable.put(RESULT_UNSUPPORTED_MEDIA_TYPE          , "Unsupported Media Type");
		resultMessageTable.put(RESULT_REQUESTED_RANGE_NOT_SATISFIABLE , "Requested range not satisfiable");
		resultMessageTable.put(RESULT_EXPECTATION_FAILED              , "Expectation Failed");
	
		//Server Error 5xx
		resultMessageTable.put(RESULT_INTERNAL_SERVER_ERROR           , "Internal Server Error");
		resultMessageTable.put(RESULT_NOT_IMPLEMENTED                 , "Not Implemented");
		resultMessageTable.put(RESULT_BAD_GATEWAY                     , "Bad Gateway");
		resultMessageTable.put(RESULT_SERVICE_UNAVAILABLE             , "Service Unavailable");
		resultMessageTable.put(RESULT_GATEWAY_TIME_OUT                , "Gateway Time-out");
		resultMessageTable.put(RESULT_HTTP_VERSION_NOT_SUPPORTED      , "HTTP Version not supported");
	
		//Database Error 6xx, Ignore Success Message 
		resultMessageTable.put(RESULT_DATABASE_ERROR                  , "Database Error");
		resultMessageTable.put(RESULT_DATABASE_NOT_CONNECTED          , "Database Not Connected");
		resultMessageTable.put(RESULT_DATABASE_DATA_NOT_FOUND         , "Database Data Not found");
		resultMessageTable.put(RESULT_DATABASE_INVALID_STATEMENT      , "Database Invalid statement");
		resultMessageTable.put(RESULT_DATABASE_INVALID_CONDITION      , "Database Invalid Condition");
	    resultMessageTable.put(RESULT_DATABASE_INVALID_TABLE_NAME     , "Database Invalid Table Name");
	    resultMessageTable.put(RESULT_DATABASE_INVALID_COLUMN_NAME    , "Database Invalid Column Name");
	}
	
	public static String getResultMessage(String resultCode) {
		
		if (resultMessageTable == null) {
			init();
		}
		
		String resultMessage = (String)resultMessageTable.get(resultCode);
		
		return resultMessage;
	}
}
