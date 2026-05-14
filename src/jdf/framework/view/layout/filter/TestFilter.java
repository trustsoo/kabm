package jdf.framework.view.layout.filter;

import javax.servlet.*;
import java.io.IOException;

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
