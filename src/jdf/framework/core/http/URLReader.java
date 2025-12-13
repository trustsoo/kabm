package jdf.framework.core.http;

import java.net.*;
import java.io.*;

/**
 * <p>
 * url connection 을 맺은 후 해당 HTML 문서 내용을 가져온다.
 * </p>
 * 
 * <pre>
 * 
 *  예제)
 *  URLReader reader = new URLReader(&quot;www.yahoo.co.kr&quot;);
 *  String content = reader.getContent();
 *  
 * </pre>
 * 
 * @author
 * @version 1.0
 */

public class URLReader
{

    private URL url;

    private String content = null;

    /**
     * url connection 을 맺는다. 단 HTTPS 프로토콜은 지원하지 않는다.
     * 
     * @param url
     */
    public URLReader(String urlname) throws MalformedURLException
    {
        url = new URL(urlname);
    }

    /**
     * 접속한 page 의 HTML 문서내용을 가져온다.
     * 
     * 
     */
    public String getContent()
    {

        if (content != null)
            return content;

        BufferedReader in = null;
        StringBuffer _content = new StringBuffer();

        try
        {
            in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
                _content.append(inputLine).append("\n");

            in.close();

        } catch (Exception ex)
        {
            // System.out.println("getContent");
            ex.printStackTrace();
        } finally
        {
            content = _content.toString();
        }

        return content;

    }

    /**
     * 해당문서의 &lt;title&gt;태그의 내용(즉 문서의 타이틀)을 가져온다.
     * 
     * 
     */
    public String getTitle()
    {

        content = getContent();

        int startPoint = 0;
        int endPoint = 0;

        if (content.indexOf("<title>") >= 0)
            startPoint = content.indexOf("<title>");
        else
            startPoint = content.indexOf("<TITLE>");

        if (content.indexOf("</title>") >= 0)
            endPoint = content.indexOf("</title>");
        else
            endPoint = content.indexOf("</TITLE>");

        // return (startPoint+"/"+endPoint);
        if (startPoint + endPoint == -2)
            return "";

        return (content.substring(startPoint + 7, endPoint));
    }

    /**
     * 접속한 page를 읽어 Reader 객체를 반환한다.
     * 
     * 
     */
    public Reader getReader() throws IOException
    {

        URLConnection conn = url.openConnection();

        return new InputStreamReader(conn.getInputStream());

    }

}