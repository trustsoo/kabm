/*
 * @(#)DocBuilder.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */

package jdf.framework.core.xml;

// JAXP packages

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <b><code>DocBuilder</code> </b>
 * <p>
 * org.w3c.dom.Document 객체를 얻기위한 목적의 클래스이다. 현재는 JAXP1.1의 DocumentBuilderFactory를
 * 이용한다.
 * 
 * </p>
 * 
 * @author
 * @version 1.0
 */

public class DocBuilder
{

    // Step 1: create a DocumentBuilderFactory and configure it
    private static DocumentBuilderFactory dbf;

    private static boolean validation = false;

    private static boolean ignoreWhitespace = true;

    private static boolean ignoreComments = false;

    private static boolean putCDATAIntoText = false;

    private static boolean createEntityRefs = false;

    private static final int DOC_BUILDER_LENGTH = 30;

    private static DocumentBuilder[] db = new DocumentBuilder[DOC_BUILDER_LENGTH];

    private static int seq = 0;

    static
    {
        try
        {

            dbf = DocumentBuilderFactory.newInstance();

            // Optional: set various configuration options
            dbf.setValidating(validation);
            dbf.setIgnoringComments(ignoreComments);
            dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
            dbf.setCoalescing(putCDATAIntoText);

            // The opposite of creating entity ref nodes is expanding them
            // inline
            dbf.setExpandEntityReferences(!createEntityRefs);

            dbf.setNamespaceAware(true);
        } catch (Throwable te)
        {
            te.printStackTrace();
        }

        // At this point the DocumentBuilderFactory instance can be saved
        // and reused to create any number of DocumentBuilder instances
        // with the same configuration options.

        // Step 2: create a DocumentBuilder that satisfies the constraints
        // specified by the DocumentBuilderFactory

        try
        {
            for (int i = 0; i < DOC_BUILDER_LENGTH; i++)
            {
                db[i] = dbf.newDocumentBuilder();

                // DTD 검증기능 제거를 위해...
                db[i].setEntityResolver(new UtilXmlResolver(true));

            }

            // org.apache.xerces.jaxp.DocumentBuilderImpl
            System.out.println("<DocBuilder> class:" + db.getClass().getName());

        } catch (Throwable pce)
        {
            // System.out.println(pce);
            pce.printStackTrace();
        }

    }

    /**
     * DocumentBuilder 객체를 얻는다.
     * 
     * 
     * 
     * @return
     */
    private synchronized static DocumentBuilder getDocumentBuilder()
    {
        seq++;

        if (seq >= DOC_BUILDER_LENGTH)
            seq = 0;

        //System.out.println(">>> ******* "+seq);
        return db[seq];

    }

    /**
     * java.io.File 객체로 부터 XML 내용을 분석하여 org.w3c.dom.Document 객체로 변환한다.
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public static Document getDocument(File file) throws Exception
    {

        Document doc = null;

        try
        {

            // doc = getDocumentBuilder().parse(file);
            doc = getDocumentBuilder().parse(new BufferedInputStream(new FileInputStream(file)));

        } catch (SAXException se)
        {
            // System.out.println(se.getMessage());
            throw se;

        } catch (IOException ioe)
        {
            // System.out.println(ioe);
            // ioe.printStackTrace();
            throw ioe;

        }

        return doc;

    }

    /**
     * 새로운 org.w3c.dom.Document 객체 생성
     * 
     * @return
     */
    public static Document newDocument()
    {
        return getDocumentBuilder().newDocument();
    }

    /**
     * java.io.InputStream 객체로 부터 XML 내용을 분석하여 org.w3c.dom.Document 객체로 변환한다.
     * 
     * @param is
     * @return
     * @throws Exception
     */
    public static Document getDocument(InputStream is) throws Exception
    {

        Document doc = null;

        try
        {

            doc = getDocumentBuilder().parse(is);
        } catch (SAXException se)
        {
            // System.out.println(se.getMessage());
            throw se;
        } catch (IOException ioe)
        {
            // System.out.println(ioe);
            throw ioe;

        }

        return doc;

    }
    
    /**
     * java.io.InputStream 객체로 부터 XML 내용을 분석하여 org.w3c.dom.Document 객체로 변환한다.
     * 
     * @param is
     * @param encode
     * @return
     * @throws Exception
     */
    public static Document getDocument(InputSource src) throws Exception
    {

        Document doc = null;

        try
        {
            doc = getDocumentBuilder().parse(src);
        } catch (SAXException se)
        {
            // System.out.println(se.getMessage());
            throw se;
        } catch (IOException ioe)
        {
            // System.out.println(ioe);
            throw ioe;

        }

        return doc;

    }
    
    /**
     * java.io.InputStream 객체로 부터 XML 내용을 분석하여 org.w3c.dom.Document 객체로 변환한다.
     * 
     * @param is
     * @param encode
     * @return
     * @throws Exception
     */
    public static Document getDocument(InputStream is, String encode) throws Exception
    {

        Document doc = null;

        try
        {
            InputSource src = new InputSource(is);
            src.setEncoding(encode);

            doc = getDocumentBuilder().parse(src);
        } catch (SAXException se)
        {
            // System.out.println(se.getMessage());
            throw se;
        } catch (IOException ioe)
        {
            // System.out.println(ioe);
            throw ioe;

        }

        return doc;

    }

    /**
     * URI 정보를 얻고 HTTP connection으로 XML 내용을 분석하여 org.w3c.dom.Document 객체로 변환한다.
     * 
     * @param uri
     * @return
     * @throws Exception
     */
    public static Document getDocument(String uri) throws Exception
    {
        return getDocument(uri, 3000);
    }

    private static Map urlDocMap = Collections.synchronizedMap(new WeakHashMap());

    /**
     * URI 정보를 얻고 HTTP connection으로 XML 내용을 분석하여 org.w3c.dom.Document 객체로 변환한다.
     * 단 timeout (ms) 시간동안 반응이 없으면 에러를 낸다.
     * 
     * @param uri
     * @param timeout
     * @return
     * @throws Exception
     */
    public static Document getDocument(String uri, int timeout) throws Exception
    {

        Document doc = null;

        try
        {
            URL u = new URL(uri);

            URLConnection uc = u.openConnection();

            DocumentInfo tmpDocInfo = (DocumentInfo) urlDocMap.get(uri);

            String modifyTimeStr = "-1";
            if (tmpDocInfo != null)
                modifyTimeStr = tmpDocInfo.getLastModifyTimeString();

            uc.setRequestProperty("anyframe-chk-modifytime", modifyTimeStr);

            // uc.setReadTimeout(timeout);
            uc.connect();

            InputStream is = uc.getInputStream();

            String datetime_str = uc.getHeaderField("mod_time");

            // 만약 파일이 바뀌지 않았다면...
            if (datetime_str != null && datetime_str.equals(modifyTimeStr))
            {
                try
                {
                    is.close();

                } catch (Exception e)
                {
                    // e.printStackTrace();
                }

                try
                {
                    Document d = tmpDocInfo.getDocument();

                    return d;

                } catch (Exception e)
                {

                    // 에러발생시 새롭게 다시 가져온다.
                    uc = u.openConnection();
                    is = uc.getInputStream();

                    // urlDocMap.remove(uri);
                }

            }

            // System.out.println(" ** " + uc.getContentType());
            // System.out.println(" >>>>>>>>>>>> " + uc.getContentEncoding());

            // InputSource src = new InputSource(new
            // BufferedInputStream(uc.getInputStream()));
            InputSource src = new InputSource(is);
            // 제우스 문제인지, 한화증권 JDK 문제인지 BufferedInputStream 쓰면 lock 이 걸린다.

            String contentType = uc.getContentType();
            if (contentType != null && contentType.toLowerCase().indexOf("euc-kr") > 0)
                src.setEncoding("euc-kr");

            doc = getDocumentBuilder().parse(src);

            if (datetime_str != null)
            {
                urlDocMap.put(uri, new DocumentInfo(doc, datetime_str));

            }

        } catch (SAXException se)
        {
            // System.out.println(se.getMessage());
            throw se;

        } catch (IOException ioe)
        {
            // System.out.println(ioe);
            // ioe.printStackTrace();
            throw ioe;

        }

        return doc;

    }

    static class DocumentInfo
    {
        Document doc;

        String last_modify_time;

        DocumentInfo(Document doc, String last_modify_time)
        {
            this.doc = doc;
            this.last_modify_time = last_modify_time;

        }

        Document getDocument()
        {
            return (Document) this.doc.cloneNode(true);
        }

        String getLastModifyTimeString()
        {
            return this.last_modify_time;
        }

    }

    static public class UtilXmlResolver implements EntityResolver
    {
        private boolean m_DisableDtd = false;

        public UtilXmlResolver()
        {
            m_DisableDtd = false;
        }

        public UtilXmlResolver(boolean DisableDtd)
        {
            m_DisableDtd = DisableDtd;
        }

        public InputSource resolveEntity(String publicId, String systemId)
        {
            if (m_DisableDtd)
            {
                return new InputSource(new java.io.ByteArrayInputStream(new byte[0]));
            } else
            {
                return null;
            }
        }
    }
}