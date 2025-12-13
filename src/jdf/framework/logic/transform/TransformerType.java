package jdf.framework.logic.transform;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.log.Logger;


/**
 * 
 * 
 * @author
 *
 */
public class TransformerType
{
	private final static String LOG_ID = "<l:TransformerType> ";
	
	
    public static String COMMA_TYPE="comma";

    public static String BINARY_TYPE="binary";

    public static String BASE64_TYPE="base64-type";

    public static String XML_TYPE="xml";
    
    public static String GRID_TYPE="jq-grid-xml";
    
    public static String HTML_TYPE="html";

    public static String EXCEL_TYPE="xls";
    
    public static String CSV_TYPE = "csv";
        
    public static String JSON_TYPE="json";
    
    public static String TREE_JSON_TYPE="tree-json";
    
    public static String TREE_JSON_TYPE_EX="tree-json-ex";
    
    public static String DYNAMIC_TREE_JSON_TYPE ="dynamic-tree-json";
    
    public static String OBJECT_TYPE="obj";
    
    public static String SOAP_TYPE = "soap";

    static
    {
        try
        {
            Config conf = Configuration.lookup("/resource/anylogic/transfer-extension-name");

            String type = conf.getString("comma-type");
            if (type != null && type.length() > 0)
                COMMA_TYPE = type;
            

            // BINARY_TYPE
            type = conf.getString("binary-type");
            if (type != null && type.length() > 0)
                BINARY_TYPE = type;
            

            // BASE64_TYPE
            type = conf.getString("base64-type");
            if (type != null && type.length() > 0)
                BASE64_TYPE = type;
           
            // XML_TYPE
            type = conf.getString("xml-type");
            if (type != null && type.length() > 0)
                XML_TYPE = type;
            

            // HTML_TYPE
            type = conf.getString("html-type");
            if (type != null && type.length() > 0)
                HTML_TYPE = type;
                        

            // EXCEL_TYPE
            type = conf.getString("excel-type");
            if (type != null && type.length() > 0)
                EXCEL_TYPE = type;
            
            // CSV_TYPE
            type = conf.getString("csv-type");
            if (type != null && type.length() > 0)
                CSV_TYPE = type;
            
            
            // JSON_TYPE
            type = conf.getString("json-type");
            if (type != null && type.length() > 0)
                JSON_TYPE = type;
            
            //JSON_TYPE
            type = conf.getString("tree-json-type");
            if (type != null && type.length() > 0)
            	TREE_JSON_TYPE = type;
            
            
            // OBJECT_TYPE
            type = conf.getString("object-type");
            if (type != null && type.length() > 0)
                OBJECT_TYPE = type;
            
            // OBJECT_TYPE
            type = conf.getString("soap-type");
            if (type != null && type.length() > 0)
                SOAP_TYPE = type;
            

            Logger.info.println(LOG_ID+"file extension type[comma] " + COMMA_TYPE);
            Logger.info.println(LOG_ID+"file extension type[binary] " + BINARY_TYPE);
            Logger.info.println(LOG_ID+"file extension type[base64] " + BASE64_TYPE);
            Logger.info.println(LOG_ID+"file extension type[xml] " + XML_TYPE);
            Logger.info.println(LOG_ID+"file extension type[json] " + JSON_TYPE);
            Logger.info.println(LOG_ID+"file extension type[tree-json] " + TREE_JSON_TYPE);
            Logger.info.println(LOG_ID+"file extension type[html] " + HTML_TYPE);
            Logger.info.println(LOG_ID+"file extension type[excel] " + EXCEL_TYPE);
            Logger.info.println(LOG_ID+"file extension type[csv] " + CSV_TYPE);
            Logger.info.println(LOG_ID+"file extension type[object] " + OBJECT_TYPE);
            Logger.info.println(LOG_ID+"file extension type[jq-grid-xml] " + GRID_TYPE);
            Logger.info.println(LOG_ID+"file extension type[soap] " + SOAP_TYPE);

        } catch (Exception e)
        {
            Logger.err.println("<TransformerType> 변환파일 확장자 설정 오류", e);
        }

    }

}
