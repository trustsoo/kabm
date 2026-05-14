package jdf.framework.logic.servlet;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;


/**
 * 데이터베이스의 POOL 리스트를 보여주는 서블릿 페이지
 * 
 * @author Eun Jeong-Ho
 */
public class PoolList extends HttpServlet implements AnyLogicControl
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        res.setHeader("cache-control", "no-cache");
        res.setHeader("expires", "0");
        res.setHeader("progma", "no-cache");
                
        PrintWriter out = new PrintWriter(
                                new OutputStreamWriter(res.getOutputStream(), "KSC5601"));
        try
        {
            String connPoolHeader="/connectionPool";
            
            Config conf = Configuration.getInitial();
            
            List keyList = conf.keys();
            
            
            
            for(int j=0;j< keyList.size(); j++ )
            {
                String key = (String) keyList.get(j);
                
                // 일반 Property를 위해서
                if(! key.startsWith("/") )
                    key = "/"+jdf.framework.core.util.StringFormater.replaceStr(key, ".", "/");
                    
                    
                int l=key.lastIndexOf("initialCapacity");
                
                if( key.startsWith(connPoolHeader)&&  l> 0 )
                {
                    String poolName = key.substring(connPoolHeader.length()+1,l-1);
                    out.println(poolName);
                }    
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }

        out.flush();
        out.close();
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        doGet(req, res);
    }
}
