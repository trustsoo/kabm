/*
 * Created on 2004-05-18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.logic.spi.cache;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.io.MultiOutputStream;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.SmartStringArray;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * anyLOGIC의 캐쉬내용을 다른 서버로 전파해주기 위한 Class
 * 
 * 가령 총 3대의 WAS 서버가 존재하고 그중 하나의 서버에서 BLD 의 내용이 캐쉬되었을 경우 그 내용을 다른 2대의 서버로 전송할 필요가
 * 있을 경우 사용된다.
 * 
 * 
 * @author
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HttpCachePropagator extends Thread
{
    public final static int INSERT = 0;

    public final static int REMOVE = 1;

    private List urlList = new ArrayList();

    private List connList = new ArrayList();

    private int mode;

    private String tr;

    private Object key;

    private Object obj;

    private void init()
    {
        if (urlList.size() > 0)
            return;
        try
        {
            Config conf = Configuration.lookup("/resource/anylogic/HttpCachePropagator/target-server");

            String serverList = conf.getString("url");

            String[] urls = SmartStringArray.split(",", serverList);

            for (int i = 0; i < urls.length; i++)
            {
                addUrl(urls[i]);
            }

        } catch (Exception e)
        {
            Logger.warn.println("<HttpCachePropagator> init error", e);
        }
    }

    /**
     * cache 내용을 전파할 다른 WAS url 을 설정한다.
     * 
     * @param urlList
     */
    public void setUrlList(List urlList)
    {
        this.urlList = urlList;
    }

    /**
     * cache 내용을 전파할 다른 WAS 의 servlet url 을 추가한다.
     * 
     * @param url
     */
    private void addUrl(String url)
    {
        this.urlList.add(url);
    }

    /**
     * INSERT 또는 REMOVE 모드에 따라 해당 내용을 설정한다.
     * 
     * @param mode
     * @param tr
     * @param key
     * @param obj
     */
    public void setInfo(int mode, String tr, Object key, Object obj)
    {
        this.mode = mode;
        this.tr = tr;
        this.key = key;
        this.obj = obj;
    }

    /**
     * 
     * 
     * @see Thread#start()
     */
    public void run()
    {
        init();

        try
        {
            send(this.mode, this.tr, this.obj);
        } catch (Exception e)
        {
            Logger.warn.println("<HttpCachePropagator> error", e);
        }

    }

    private void send(int mode, String tr, Object obj) throws IOException
    {
        MultiOutputStream mout = null;
        String path = null;

        for (int i = 0; i < this.urlList.size(); i++)
        {
            try
            {
                path = this.urlList.get(i).toString();

                URL url = new URL(path);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/octet-stream");

                OutputStream os = conn.getOutputStream();

                if (mout == null)
                    mout = new MultiOutputStream(os);
                else
                    mout.addOutputStream(os);

                connList.add(conn);
            } catch (Exception e)
            {
                Logger.warn.println("<HttpCachePropagator> " + path + " is error", e);
            }

        }

        ObjectOutputStream out = new ObjectOutputStream(mout);

        out.writeInt(mode);
        out.writeUTF(tr);
        out.writeObject(key);
        out.writeObject(obj);
        out.flush();
        out.close();

        for (int i = 0; i < this.connList.size(); i++)
        {
            HttpURLConnection rconn = (HttpURLConnection) connList.get(i);

            Logger.debug.println("<HttpCachePropagator> response " + rconn.getResponseCode());
        }

    }

    public static void main(String[] args) throws Exception
    {
        // send("http://anyweb:30000/anylogic/webup");
        // send("http://anyweb:10000/kind_convert/test/test.jsp");

        HttpCachePropagator post = new HttpCachePropagator();

        post.setInfo(HttpCachePropagator.INSERT, "te", "wet", "234234");

        long t1 = System.currentTimeMillis();
        post.start();
        t1 = System.currentTimeMillis() - t1;
        System.out.println(t1);
    }

}
