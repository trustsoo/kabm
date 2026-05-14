/*
 * Created on 2004-03-18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jdf.framework.view.layout.filter;

import jdf.framework.core.log.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * HTTP 출력을 zip 으로 압축하여 전송한다.
 * 
 * 
 * 
 * @author
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HttpCompressionFilter implements Filter
{
    private final static String LOG_ID = "<t:HttpComressionFilter> ";

    private FilterConfig config;

    public void setFilterConfig(FilterConfig config) throws ServletException
    {
        init(config);
    }

    /***************************************************************************
     * 
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException
    {
        this.config = config;
        // Logger.info.println(LOG_ID + " init complete");

    }

    public FilterConfig getFilterConfig()
    {
        return this.config;
    }

    /**
     * 
     * 
     * @see Filter#doFilter(ServletRequest,
     *      ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        boolean supportsGzip = false;

        String encoding = req.getHeader("Accept-Encoding");

        // Logger.debug.println(LOG_ID + "encoding:" + encoding);

        if (encoding != null)
        {
            if (encoding.toLowerCase().indexOf("gzip") > -1)
            {
                supportsGzip = true;
            }
        }

        if (supportsGzip)
        {
            Logger.debug.println(LOG_ID + " compress mode");

            ByteArrayServletResponse respWrap = new ByteArrayServletResponse(resp, false, true, null);

            chain.doFilter(request, respWrap);

            if (respWrap.isSetStatus())
            {
                resp.flushBuffer();
                return;
            }

            respWrap.flushBuffer();

            String contentType = respWrap.getContentType();

            /*
             * chain.doFilter(request, response);
             * 
             * GZIPOutputStream gzos = new
             * GZIPOutputStream(response.getOutputStream());
             * 
             * ByteArrayOutputStream responseMsg = new ByteArrayOutputStream();
             * responseMsg.writeTo(gzos);
             * 
             * 
             * 
             * gzos.flush(); gzos.close();
             * 
             * byte[] comp = responseMsg.toByteArray();
             * 
             */
            try
            {
                response.reset();
                respWrap.copy(resp);

            } catch (Exception ee)
            {
                return;
            }
            resp.setHeader("Content-Encoding", "gzip");
            if (contentType != null)
            {
                response.setContentType(contentType);
                // Logger.debug.println(LOG_ID + " " + contentType);
            }

            byte[] comp = respWrap.toByteArray();
            resp.getOutputStream().write(comp);

            // Logger.debug.println(Hexa.logFormat(comp));
            Logger.debug.println(LOG_ID + "compress size:" + comp.length);

            resp.flushBuffer();
        } else
            chain.doFilter(request, response);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
        return;

    }

}