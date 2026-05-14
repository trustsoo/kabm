/*
 * Created on 2004-05-25
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.servlet;

import jdf.framework.core.Configuration;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.io.SmartFile;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.StringFormater;
import jdf.framework.logic.spi.management.BLContextFactory;
import jdf.framework.logic.transform.schema.XsdGenerator;
import jdf.framework.logic.transform.schema.XsdGeneratorFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


/**
 * 
 * 
 * @author
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IOSchemaInfo extends HttpServlet implements AnyLogicControl
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final static String LOG_ID = "<al-svlt:IOSchemaInfo> ";

    

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        // res.setHeader("cache-control", "no-cache");
        // res.setHeader("expires", "0");
        // res.setHeader("progma", "no-cache");

        try
        {
            // Logger.debug.println("---------- 1");
            // /ioschema/dir/name1.xml 
            String loc = req.getPathInfo();
            int lp = loc.lastIndexOf(".");

            String name = loc.substring(loc.indexOf("/", 1) + 1, lp);

            String resType = loc.substring(lp + 1);

            Logger.debug.println(LOG_ID+"tr:" + name);

            BLContextFactory blf = BLContextFactory.getInstance();

            IOSchema schema = blf.getIOSchema(name);

            java.io.OutputStream osteam = res.getOutputStream();

            if ("html".equals(resType))
            {
                osteam.write(getHtmlBasedSchemaByteArray(schema, resType, req));

            } else if ("xls".equals(resType))
            {
                res.setContentType("application/vnd.ms-excel; charset=EUC-KR");
                osteam.write(getHtmlBasedSchemaByteArray(schema, resType, req));
            } else if ("gxl".equals(resType))
            {
                String encoding = "euc-kr";
                osteam.write(("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n").getBytes(encoding));
                osteam.write(getGridLayoutXml(schema, req));

            }

            else
            {
                String encoding = "euc-kr";

                String contentType = req.getContentType();

                try
                {
                    encoding = SmartStringArray.split("=", contentType)[1].toUpperCase();
                    encoding = StringFormater.replaceStr(encoding, "\"", "");

                } catch (Exception e)
                {
                    Logger.warn.println(LOG_ID + "default encoding:" + encoding);
                }

                osteam.write(("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n").getBytes(encoding));
                osteam.write(getSchemaXmlByteArray(schema, resType, req, encoding));
            }

            // osteam.flush();
        } catch (Exception e)
        {
            Logger.err.println(LOG_ID+"process error", e);
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private Object getValue(Object val)
    {
        if (val == null)
            return "";
        return val;
    }

    private byte[] getGridLayoutXml(IOSchema schema, HttpServletRequest req)
    {
        Block[] b = schema.getOutputBlocks();

        boolean isGroupHeader = false;
        String GBHEADER = "1";
        String RW_RATE = "R_F";
        String VHEADER = null; 
        String HDR_KIND = null;
        
        if(b.length>0) {
            VHEADER = b[0].getProperty("grid-VHEADER");
            if(VHEADER==null || VHEADER.length()==0)
                VHEADER="FD000";
            
            HDR_KIND = b[0].getProperty("grid-HDR_KIND");
            if(HDR_KIND==null || HDR_KIND.length()==0)
                HDR_KIND="BM_GH";
        }

        for (int i = 0; i < b.length && i < 1; i++)
        {
            Field[] fields = b[i].getFields();

            for (int j = 0; j < fields.length; j++)
            {
                if (fields[j].getProperty("grid-group-size") != null)
                {
                    isGroupHeader = true;
                    GBHEADER = "2";
                    // break;
                }

                String w = fields[j].getProperty("grid-width");
                if (w != null && w.indexOf("%") > 0)
                    RW_RATE = "R_UF";

            }
        }

        StringBuffer buf = new StringBuffer();

        buf.append("<panel name='WebPonentGrid' type='' version=''>");
        buf.append("    <property_list/>");
        buf.append("    <layer_list>");
        buf.append("        <layer name='FNO_1'>");
        buf.append("            <property_list>");
        buf.append("                <property name='STCD' type='string' value='100'/>");
        buf.append("                <property name='FNO' type='string' value='FNO'/>");
        buf.append("                <property name='LNO' type='string' value='1'/>");
        buf.append("                <property name='HDR_KIND' type='string' value='"+HDR_KIND+"'/>");
        buf.append("                <property name='RW_RATE' type='string' value='" + RW_RATE + "'/>");
        buf.append("                <property name='VHEADER' type='string' value='"+VHEADER+"'/>");
        buf.append("                <property name='GBHEADER' type='string' value='" + GBHEADER + "'/>");
        buf.append("            </property_list>");
        buf.append("            <field_list>");

        StringBuffer tmp1 = new StringBuffer();
        StringBuffer tmp2 = new StringBuffer();

        //int group_point = 0;

        for (int i = 0; i < b.length && i < 1; i++)
        {
            Field[] fields = b[i].getFields();

            int seq = 0;
            int last_x_point = -1;

            for (int j = 0; j < fields.length; j++)
            {
                Field f = fields[j];
                String label = f.getLabel();
                if (label == null || label.length() == 0)
                    label = f.getName();

                String idx = getNum(j);

                String sizeInfo = f.getProperty("grid-width");
                if (sizeInfo == null || sizeInfo.length() == 0)
                {
                    sizeInfo = "70";

                    int size = fields[j].getSize();
                    if (size > 1)
                        sizeInfo = String.valueOf(size);
                }

                String ctype = "SC"; 
                // String type = "S"; 
                String type = f.getProperty("grid-type");
                String align = "R"; 

                

                if (type == null || type.length() == 0)
                {
                    type = "S";
                    if (f.getType() == Field.STRING)
                    {
                        type = "S";
                    }
                    if (f.getType() == Field.INTEGER)
                    {
                        type = "I";
                    } else if (f.getType() == Field.FLOAT)
                    {
                        type = "F";
                    } else if (f.getType() == Field.LONG)
                    {
                        type = "L";
                    }
                }

                if (f.getProperty("grid-checkbox") != null)
                {
                    ctype = "CK";
                    align = "M";
                } else if (f.getProperty("grid-seq") != null)
                {
                    align = "M";
                    type = "I";
                }
                
                if (f.getProperty("grid-align") != null)
                    align = f.getProperty("grid-align");
                

                String show = "Y";
                if ("false".equals(f.getProperty("grid-show")))
                    show = "N";

                buf.append("<field name='field_" + idx + "'>");
                buf.append(" <property_list>");
                buf.append("  <property name='CODE' type='string' value='A" + idx + "'/>");
                buf.append("  <property name='NAME' type='string' value='" + fields[j].getName() + "'/>");
                buf.append("  <property name='SHOW' type='string' value='" + show + "'/>");
                // buf.append(" <property name='HCLICK' type='string'
                // value='Y'/>");
                
                if(f.getProperty("grid:WIDTH")==null)
                    buf.append("  <property name='WIDTH' type='string' value='" + sizeInfo + "'/>");
                if(f.getProperty("grid:RSORT")==null)
                    buf.append("  <property name='RSORT' type='string' value='" + align + "'/>");
                if(f.getProperty("grid:TYPE")==null)
                    buf.append("  <property name='TYPE' type='string' value='" + type + "'/>");
                if(f.getProperty("grid:CTYPE")==null)
                    buf.append("  <property name='CTYPE' type='string' value='" + ctype + "'/>");
                
                String cattr =  fields[j].getProperty("grid-cattr");
                if(cattr!=null) {
                    buf.append("  <property name='CATTR' type='string' value='" + cattr + "'/>");
                }
                if ("true".equals(f.getProperty("header-click")))
                    buf.append("  <property name='HCLICK' type='string' value='Y'/>");
                
                
                // 
                java.util.Properties props = f.getProperties();
                java.util.Iterator itr = props.keySet().iterator();
                
                while( itr.hasNext() )
                {
                    String propKeyNm = (String) itr.next();
                    if(propKeyNm!=null && propKeyNm.indexOf("grid:")==0) {
                        String gridPropNm = propKeyNm.substring(5);
                        String gridPropVal = props.getProperty(propKeyNm);
                        buf.append("  <property name='").append(gridPropNm).append("' type='string' value='").append(gridPropVal).append("'/>");
                    }
                }
                
                
                
                buf.append(" </property_list>");
                buf.append(" <ref_list/>");
                buf.append("</field>");

                String y1 = "0";
                String y2 = "0";
                

                String idx2 = getNum(seq);

                if (isGroupHeader)
                {
                    String grid_group_size = f.getProperty("grid-group-size");
                    if (grid_group_size == null)
                    {
                        y2 = "1";
                    } else
                    {
                        int x_point = Integer.parseInt(grid_group_size);
                        int g_x2 = j + x_point - 1;
                        last_x_point = g_x2;
                        String grid_group_name = f.getProperty("grid-group-name");

                        grid_group_name = getLabel(grid_group_name, req);

                        tmp1.append("<property name='H" + idx2 + "' type='SC' value='" + grid_group_name + "'/>");
                        tmp2.append("<property name='H" + idx2 + "' type='SC' value='" + j + "_0_" + g_x2 + "_0'/>");

                        seq++;
                        idx2 = getNum(seq);
                        y1 = "1";
                        y2 = "1";
                    }

                    if (j <= last_x_point)
                        y1 = "1";

                }

                label = getLabel(label, req);

                tmp1.append("<property name='H" + idx2 + "' type='SC' value='" + label + "'/>");

                tmp2.append("<property name='H" + idx2 + "' type='SC' value='" + j + "_" + y1 + "_" + j + "_" + y2
                        + "'/>");

                seq++;
            }
        }
        buf.append("</field_list>");
        buf.append("        <sub_list>");
        buf.append("            <sub name='HEADER'>");
        buf.append("                <property_list>");

        buf.append(tmp1.toString());
        buf.append("</property_list>");
        buf.append("</sub>");
        buf.append("<sub name='HLOCA'>");
        buf.append("    <property_list>");
        buf.append(tmp2.toString());

        buf.append("          </property_list>");
        buf.append("                </sub>");
        buf.append("            </sub_list>");
        buf.append("        </layer>");
        buf.append("    </layer_list>");
        buf.append("</panel>");

        return buf.toString().getBytes();
    }

    private static String getNum(int i)
    {
        if (i > -1 && i < 10)
            return "0" + i;

        return String.valueOf(i);
    }

    private String getLabel(String label, HttpServletRequest req)
    {
        if (label == null)
            return "";

        Enumeration enumNm = req.getParameterNames();
        while (enumNm.hasMoreElements())
        {
            String paramKey = (String) enumNm.nextElement();
            String[] vs = req.getParameterValues(paramKey);

            for (int i = 0; vs != null && i < vs.length; i++)
            {
                String v = chkUnicode(vs[i]);
                label = StringFormater.replaceStr(label, "{$" + paramKey + "[" + i + "]}", v);

                if (i == 0)
                    label = StringFormater.replaceStr(label, "{$" + paramKey + "}", v);
            }
        }

        int stx = label.indexOf("{$");
        if (stx >= 0)
        {
            int etx = label.indexOf("}", stx);

            String otherParam = label.substring(stx, etx + 1);
            label = StringFormater.replaceStr(label, otherParam, "");
        }

        return label;
    }

    /**
     * 
     * 
     * @param schema
     * @param resType
     * @param req
     * @return
     */
    private byte[] getHtmlBasedSchemaByteArray(IOSchema schema, String resType, HttpServletRequest req)
    {
        StringBuffer buf = new StringBuffer();
        buf
                .append("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:x=\"urn:schemas-microsoft-com:office:excel\" xmlns=\"http://www.w3.org/TR/REC-html40\">");

        buf.append("<head>");
        buf.append("<meta http-equiv=Content-Type content='text/html; charset=ks_c_5601-1987'>");
        buf.append("</head>");

        buf.append("<body>");
        buf.append("<table border='1'>");

        buf.append("<tr><th colspan='7' align='left'> * INPUT SCHEMA</th></tr>");
        buf.append("<tr>");
        buf.append("<th width='150'>Block Name</th>");
        buf.append("<th width='40'>Seq</th>");
        buf.append("<th width='100'>Name</th>");
        buf.append("<th width='150'>Label</th>");
        buf.append("<th width='60'>Type</th>");
        buf.append("<th width='60'>Size</th>");
        buf.append("<th width='150'>Default</th>");
        buf.append("<th width='150'>Format</th>");
        buf.append("</tr>");

        Block[] b = schema.getInputBlocks();

        for (int i = 0; i < b.length; i++)
        {

            Field[] fields = b[i].getFields();
            for (int j = 0; j < fields.length; j++)
            {
                buf.append("<tr>");

                if (j == 0)
                {
                    buf.append("<td rowspan='").append(fields.length).append("'>").append(b[i].getName());

                    buf.append("<br/>repeat=").append(b[i].getIterationNum());
                    buf.append("<br/>repeatRef=").append(b[i].getIterationRefName());

                    buf.append("</td>");
                }
                buf.append("<td x:num>").append((j + 1)).append("</td>");
                buf.append("<td x:str>").append(fields[j].getName()).append("</td>");
                buf.append("<td x:str>").append(getValue(fields[j].getLabel())).append("</td>");

                buf.append("<td x:str>").append(fields[j].getTypeName()).append("</td>");
                buf.append("<td x:num>").append(fields[j].getSize()).append("</td>");

                buf.append("<td x:str>").append(getValue(fields[j].getDefaultValue())).append("</td>");
                buf.append("<td x:str>").append(getValue(fields[j].getFormat())).append("</td>");

                buf.append("</tr>");
            }
        }

        buf.append("<tr><th colspan='7' align='left'> * OUTPUT SCHEMA</th></tr>");
        buf.append("<tr>");
        buf.append("<th width='150'>Block Name</th>");
        buf.append("<th width='40'>Seq</th>");
        buf.append("<th width='100'>Name</th>");
        buf.append("<th width='150'>Label</th>");
        buf.append("<th width='60'>Type</th>");
        buf.append("<th width='60'>Size</th>");
        buf.append("<th width='150'>Default</th>");
        buf.append("<th width='150'>Format</th>");
        buf.append("</tr>");

        b = schema.getOutputBlocks();

        for (int i = 0; i < b.length; i++)
        {

            Field[] fields = b[i].getFields();
            for (int j = 0; j < fields.length; j++)
            {
                buf.append("<tr>");

                if (j == 0)
                {
                    buf.append("<td rowspan='").append(fields.length).append("'>").append(b[i].getName());

                    buf.append("<br/>repeat=").append(b[i].getIterationNum());
                    buf.append("<br/>repeatRef=").append(b[i].getIterationRefName());

                    buf.append("</td>");
                }
                buf.append("<td x:num>").append((j + 1)).append("</td>");
                buf.append("<td x:str>").append(fields[j].getName()).append("</td>");
                buf.append("<td x:str>").append(getValue(fields[j].getLabel())).append("</td>");

                buf.append("<td x:str>").append(fields[j].getTypeName()).append("</td>");
                buf.append("<td x:num>").append(fields[j].getSize()).append("</td>");

                buf.append("<td x:str>").append(getValue(fields[j].getDefaultValue())).append("</td>");
                buf.append("<td x:str>").append(getValue(fields[j].getFormat())).append("</td>");

                buf.append("</tr>");
            }
        }

        buf.append("</table>");
        buf.append("</body>");
        buf.append("</html>");

        return buf.toString().getBytes();
    }

    /**
     * 
     * 
     * <element name="dataset"> <complexType><sequence><element name="result">
     * <complexType><sequence><any namespace="##targetNamespace"
     * maxOccurs="unbounded"/> </sequence> </complexType> </element> </sequence>
     * </complexType> </element>
     * 
     */
    //private final static String XsdInfo = "<element name=\"dataset\"><complexType><sequence><element name=\"result\"><complexType><sequence><any namespace=\"##targetNamespace\" maxOccurs=\"unbounded\"/></sequence></complexType></element></sequence></complexType></element>";

    /**
     * 
     * 
     * @param schema
     * @param mode
     * @return
     */
    private byte[] getSchemaXmlByteArray(IOSchema schema, String mode, HttpServletRequest req, String encoding)
            throws Exception
    {
        if ("xsd".equals(mode))
        {

            String xsd = (String) schema.getAttribute(IOSchema.XML_SCHEMA_TEXT);

            if (xsd == null)
            {

                XsdGenerator gen = XsdGeneratorFactory.newXsdGenerator();

                String reqUrl = req.getRequestURL().toString();

                // http://
                int pt = reqUrl.indexOf("/", 8);

                //reqUrl = AnyLogicControlServlet.getHostName() + reqUrl.substring(pt);
                reqUrl = reqUrl.substring(pt);

                String svltAliasNm = AnyLogicControlServlet.getServletAliasName();
                reqUrl = StringFormater.replaceStr(reqUrl, svltAliasNm + "/ioschema", svltAliasNm + "/process");
                reqUrl = StringFormater.replaceStr(reqUrl, ".xsd", ".soap");

                // gen.setTargetNamespace(schema.getName());
                gen.setTargetNamespace(reqUrl.substring(1));

                gen.appendXsdText("<element name=\"dataset\"><complexType><sequence>");

                gen.appendXsdText("<element name=\"request\"><complexType><sequence>");
                gen.generate(schema, IOSchema.IN, null);
                gen.appendXsdText("</sequence></complexType></element>");

                gen.appendXsdText("<element name=\"result\"><complexType><sequence>");
                gen.generate(schema, IOSchema.OUT, null);
                gen.appendXsdText("</sequence></complexType></element>");

                gen.appendXsdText("</sequence></complexType></element>");

                xsd = gen.getXsdText();

                schema.setAttribute(IOSchema.XML_SCHEMA_TEXT, xsd);
            }

            return xsd.getBytes(encoding);

        } else if ("wsdl".equals(mode))
        {
            // WSDL
            String wsdlTmplFileName = Configuration.getConfigPath() + "/webservices/anylogic_wsdl_tmpl.txt";
            SmartFile sf = new SmartFile(wsdlTmplFileName);

            String wsdl = sf.getContent();

            XsdGenerator gen = XsdGeneratorFactory.newXsdGenerator();
            gen.setLocalName("xs");
            gen.setTargetNamespace(schema.getName());

            String wsdl_req_xsd = gen.generate(schema, IOSchema.IN, null);
            // System.out.println(wsdl_req_xsd);
            wsdl = StringFormater.replaceStr(wsdl, "${ws_req_xsd}", wsdl_req_xsd);

            String wsdl_resp_xsd = gen.generate(schema, IOSchema.OUT, null);
            // System.out.println("");
            // System.out.println(wsdl_resp_xsd);
            wsdl = StringFormater.replaceStr(wsdl, "${ws_resp_xsd}", wsdl_resp_xsd);
            
            String doc = schema.getDescription();
            wsdl = StringFormater.replaceStr(wsdl, "${ws_doc}", doc);

            // http://localhost:8080/anylogic/ioschema/testService.xml
            String reqUrl = req.getRequestURL().toString();
            String svltAliasNm = AnyLogicControlServlet.getServletAliasName();
            reqUrl = StringFormater.replaceStr(reqUrl, "anylogic/ioschema/", "anylogic/process/");
            reqUrl = StringFormater.replaceStr(reqUrl, ".wsdl", ".soap");

            wsdl = StringFormater.replaceStr(wsdl, "${ws_addr}", reqUrl);
            
            
            int pt = reqUrl.indexOf("/", 8);
            reqUrl = reqUrl.substring(pt+1); //IOSchemaProcess와도 맞쳐주어야 한다. ,XSD 의 namespace도 고려
            
            wsdl = StringFormater.replaceStr(wsdl, "${ws_ds_ns}", reqUrl);

            return wsdl.getBytes();

        } else
            return schema.toString().getBytes(encoding);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        doGet(req, res);
    }

    private static String chkUnicode(String theString)
    {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;)
        {
            aChar = theString.charAt(x++);
            if (aChar == '\\')
            {
                aChar = theString.charAt(x++);
                if (aChar == 'u')
                {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++)
                    {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else
                {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

}