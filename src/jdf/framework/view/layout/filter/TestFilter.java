package jdf.framework.view.layout.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class TestFilter implements Filter
{

    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    public void init(FilterConfig arg0) throws ServletException
    {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException
    {
        
        
        for(int i=0; i<10; i++)
        {
            System.out.println(i+" ** BEFORE CHAIN doFilter");
            chain.doFilter(req, resp);
            System.out.println(i+" ** AFTER CHAIN doFilter");
        }
            
        

    }

}
